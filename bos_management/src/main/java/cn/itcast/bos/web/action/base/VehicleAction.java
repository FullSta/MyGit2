package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Vehicle;
import cn.itcast.bos.service.base.VehicleService;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
@Controller
public class VehicleAction {
    @Autowired
    private VehicleService vehicleService;

    @Action(value = "vehicle_findAll",results = {@Result(name = "success",type = "json")})
    public String findAll(){
        List<Vehicle> list = vehicleService.findAll();
        ActionContext.getContext().getValueStack().push(list);

        return "success";
    }
}
