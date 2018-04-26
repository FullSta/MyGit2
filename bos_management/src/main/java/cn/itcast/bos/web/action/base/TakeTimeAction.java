package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.TakeTimeService;
import cn.itcast.bos.web.action.common.BaseAction;
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
public class TakeTimeAction extends BaseAction<TakeTime> {

    @Autowired
    private TakeTimeService takeTimeService;

    @Action(value = "taketime_findall",results = {@Result(name = "success",type = "json")})
    public String findall(){
        List<TakeTime> list = takeTimeService.findAll();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }

    @Action(value = "taketime_save",results = {@Result(name = "success",type = "redirect",location = "./pages/base/take_time.html")})
    public String save(){
        System.out.println("web层正在执行保存操作...");
        takeTimeService.save(model);

        return SUCCESS;
    }

}
