package cn.itcast.bos.service.take_delivery.impl;

import cn.itcast.bos.constant.Constants;
import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.dao.base.FixedAreaRepository;
import cn.itcast.bos.dao.take_delivery.OrderRepository;
import cn.itcast.bos.dao.take_delivery.WorkBillRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WorkBill;
import cn.itcast.bos.service.base.CourierService;
import cn.itcast.bos.service.take_delivery.OrderService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private FixedAreaRepository fixedAreaRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private WorkBillRepository workBillRepository;

    @Autowired
    private CourierService courierService;

    @Autowired
    private JmsTemplate jmsQueueTemplate;

    @Override
    public void saveOrder(Order order) {
//        System.out.println("正在保存订单...");
        order.setOrderNum(UUID.randomUUID().toString());
        order.setOrderTime(new Date());
        order.setStatus("1");
        // 寄件人 省市区
        Area area = order.getSendArea();
        Area persistArea = areaRepository.findByProvinceAndCityAndDistrict(
                area.getProvince(),area.getCity(),area.getDistrict()
        );
        // 收件人 省市区
        Area recArea = order.getRecArea();
        Area persistRecArea = areaRepository.findByProvinceAndCityAndDistrict(
                recArea.getProvince(),recArea.getCity(),recArea.getDistrict()
        );
        order.setSendArea(persistArea);
        order.setRecArea(persistRecArea);
        order.setTelephone(order.getSendMobile());
        //自动分单逻辑,基于CRM地址库的 完全分配 ,获取定区,匹配快递员
        // 1.根据发送方的发送地址得到定区id
        String fixedAreaId = WebClient.create(
                Constants.CRM_MANAGEMENT_URL
                +"/services/customerService/customer/findFixedAreaIdByAddress/"
                +order.getSendAddress()).accept(MediaType.APPLICATION_JSON).get(String.class);
        if (fixedAreaId != null){
            // 2. 根据定区id找到这个定区详细信息
            FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
            // 3.只能根据定区找到快递员,
            Courier courier = fixedArea.getCouriers().iterator().next();
            System.out.println("快递员的名字:"+courier.getName());
            if (courier != null){
                // 把定区和订单绑定
                System.out.println("自动分单成功...");
                saveOrder(order,courier);
                // 生成工单
                generateWorkBill(order);
                return;
            }else {
                System.out.println("该地区没有快递员...");
            }
        }


        // 在上面找不到与客户 完全匹配 的地址,,通过输入的详细地址(分区),找到定区,再通过定区找到快递员
        // 1.得到区域,扫描区域下面的分区
        for(SubArea subArea: persistArea.getSubareas()){
            // 2.如果订单信息包含分区关键字,就得到了该分区
            if(order.getSendAddress().contains(subArea.getKeyWords())){
                // 3.通过分区找到定区,再通过定区找到快递员们.遍历快递员
                Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
                if (iterator.hasNext()){
                    Courier courier = iterator.next();
                    if (courier != null){
                        // 4.在该定区下找到快递员
                        System.out.println("自动分单成功");
                        saveOrder(order,courier);

                        // 生成工单,向快递员发送短信
                        generateWorkBill(order);
                        return;
                    }
                }
            }
        }
        // 在上面找不到与客户 完全匹配 的地址,,通过输入的详细地址(分区),找到定区,再通过定区找到快递员
        // 1.得到区域,扫描区域下面的分区
        for(SubArea subArea: persistArea.getSubareas()){
            // 2.如果订单信息包含分区关键字,就得到了该分区
            if(order.getSendAddress().contains(subArea.getAssistKeyWords())){
                // 3.通过分区找到定区,再通过定区找到快递员们.遍历快递员
                Iterator<Courier> iterator = subArea.getFixedArea().getCouriers().iterator();
                if (iterator.hasNext()){
                    Courier courier = iterator.next();
                    if (courier != null){
                        // 4.在该定区下找到快递员
                        System.out.println("自动分单成功");
                        saveOrder(order,courier);

                        // 生成工单,向快递员发送短信
                        generateWorkBill(order);
                        return;
                    }
                }
            }
        }

    }

    @Override
    public Order findbyOrderNum(String orderNum) {
        return orderRepository.findByOrderNum(orderNum);
    }

    private void generateWorkBill(final Order order) {
        // 生成工单
        WorkBill workBill = new WorkBill();
        workBill.setType("新");
        workBill.setPickstate("新单");
        workBill.setBuildtime(new Date());
        workBill.setRemark(order.getRemark());
        final String smsNumber = RandomStringUtils.randomNumeric(4);
        workBill.setSmsNumber(smsNumber);
        workBill.setOrder(order);
        workBill.setCourier(order.getCourier());
        workBillRepository.save(workBill);
        // 调用MQ服务发送短信SMS_132400363
        // 调用MQ服务,吧信息存到activemq中
        System.out.println("正在准备存入信息到activemq...");
        jmsQueueTemplate.send("IGM", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone",order.getCourier().getTelephone());
                mapMessage.setString("smsNumber",smsNumber);
                mapMessage.setString("sendAddress",order.getSendAddress());
                mapMessage.setString("sendName",order.getSendName());
                mapMessage.setString("sendMobile",order.getSendMobile());
                mapMessage.setString("sendMobileMsg",order.getSendMobileMsg());
                return mapMessage;
            }
        });
        workBill.setPickstate("已通知");
    }

    // 保存自动分单
    private void saveOrder(Order order,Courier courier){
        // 将订单和快递云关联

        order.setCourier(courier);
        // 设置自动分单
        order.setOrderType("1");
        // save
        orderRepository.save(order);
    }
}
