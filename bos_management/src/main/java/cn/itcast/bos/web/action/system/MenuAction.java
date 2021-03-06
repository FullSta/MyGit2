package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
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
@Controller
@Scope("prototype")
public class MenuAction extends BaseAction<Menu> {
    @Autowired
    private MenuService menuService;

    @Action(value = "menu_list",results = {@Result(name = "success",type = "json")})
    public String list(){
        // 调用业务层,查询所有菜单数据
        List<Menu> menus = menuService.findAll();
        // save valuestack
        ActionContext.getContext().getValueStack().push(menus);

        return SUCCESS;
    }

    @Action(value = "menu_save",results = {@Result(name = "success",type = "redirect",location = "pages/system/menu.html")})
    public String save(){
        // save
        menuService.save(model);
        return SUCCESS;
    }

    @Action(value = "menu_showmenu",results = {@Result(name = "success",type = "json")})
    public String showMenu(){
        // inquire menu list
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();

        List<Menu> menus = menuService.findByUser(user);
        ActionContext.getContext().getValueStack().push(menus);
        return SUCCESS;
    }

}
