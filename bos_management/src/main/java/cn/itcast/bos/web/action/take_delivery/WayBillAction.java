package cn.itcast.bos.web.action.take_delivery;

import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.service.take_delivery.WayBillService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

/**
 * 运单管理
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class WayBillAction extends BaseAction<WayBill> {

    private static final Logger LOGGER = Logger.getLogger(WayBillAction.class);

    @Autowired
    private WayBillService wayBillService;

    @Action(value = "waybill_save",results = {@Result(name = "success",type = "json")})
    public String save(){
        Map<String,Object> result = new HashMap<String,Object>();
        try {
            // remove no_id object
            if (model.getOrder() != null && (model.getId() == null || model.getOrder().getId() == 0 )){
                model.setOrder(null);
            }
            wayBillService.save(model);
            // save complete
            result.put("success",true);
            result.put("msg","保存运单成功!");
            LOGGER.info("保存运单号:"+model.getWayBillNum()+"成功!");

        }catch (Exception e){
            e.printStackTrace();
            result.put("success",false);
            result.put("msg","保存运单失败!");
            LOGGER.error("保存运单号:"+model.getWayBillNum()+"失败!");
        }
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }

    @Action(value = "waybill_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        // 无条件查询,简单的方式
        Pageable pageable = new PageRequest(page-1,rows,new Sort(
                new Sort.Order(Sort.Direction.DESC,"id")));

        // 调用service层进行查询
        Page<WayBill> pageData = wayBillService.findPageData(model,pageable);
        // 压入值栈返回
        pushPageDataToValueStack(pageData);

        return SUCCESS;
    }

    @Action(value = "waybill_findByWayBillNum",results = {@Result(name = "success",type = "json")})
    public String findByWayBillNum(){
        // 调用业务层查询
        WayBill wayBill = wayBillService.findByWayBillNum(model.getWayBillNum());
        Map<String,Object> result = new HashMap<String,Object>();
        if (wayBill == null){
            // 运单不存在
            result.put("success",false);
        }else {
            // 运单存在
            result.put("success",true);
            result.put("wayBillData",wayBill);
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }


}
