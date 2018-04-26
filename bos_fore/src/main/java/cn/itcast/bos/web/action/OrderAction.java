package cn.itcast.bos.web.action;

import cn.itcast.bos.constant.Constants;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.crm.domain.Customer;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;

/**
 * 前端系统订单数据处理
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
    private String sendAreaInfo; // 发件人省市区信息
    private String recAreaInfo; // 收件人 省市区信息

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }

    @Action(value = "order_add",results = {@Result(name = "success",type = "redirect",location = "index.html")})
    public String add(){
//        System.out.println("bos_fore正在生成订单...");
        // 手动封装area关联
        Area sendArea = new Area();
        String[] sendAreaData = sendAreaInfo.split("/");
        sendArea.setProvince(sendAreaData[0]);
        sendArea.setCity(sendAreaData[1]);
        if (sendAreaData.length == 3){
            sendArea.setDistrict(sendAreaData[2]);
        }

        System.out.println(sendArea);

        Area recArea = new Area();
        String[] recAreaData = recAreaInfo.split("/");
        recArea.setProvince(recAreaData[0]);
        recArea.setCity(recAreaData[1]);
        if (recAreaData.length == 3){
            recArea.setDistrict(recAreaData[2]);
        }

        model.setSendArea(sendArea);
        model.setRecArea(recArea);

        // 关联当前登录账户
        Customer customer = (Customer) ServletActionContext.getRequest().getSession().getAttribute("customer");
        if (customer != null ){
            model.setCustomer_id(customer.getId());
        }

        //调用webservice 将数据传递到bos_management系统
        WebClient.create(Constants.BOS_MANAGEMENT_URL
                +"/services/orderService/order").type(MediaType.APPLICATION_JSON).post(model);

        return SUCCESS;
    }
}
