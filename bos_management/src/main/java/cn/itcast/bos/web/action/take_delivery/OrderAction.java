package cn.itcast.bos.web.action.take_delivery;

import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.service.take_delivery.OrderService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class OrderAction extends BaseAction<Order> {
    @Autowired
    private OrderService orderService;

    @Action(value = "order_findByOrderNum",results = {@Result(name = "success",type = "json")})
    public String findByOrderNum(){
        // 调用业务层,查询order信息
        Order order = orderService.findbyOrderNum(model.getOrderNum());
        Map<String,Object> result = new HashMap<String,Object>();
        if (order == null) {
            // 订单号不存在
            result.put("success",false);
        }else {
            // 订单号存在
            result.put("success",true);
            result.put("orderData",order);
        }
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }


}
