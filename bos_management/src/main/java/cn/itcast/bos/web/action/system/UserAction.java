package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import sun.tools.jstat.Token;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class UserAction extends BaseAction<User> {
    @Action(value = "user_login",results = {@Result(name = "login",type = "redirect",location = "login.html")
            ,@Result(name = "success",type = "redirect",location = "index.html")})
    public String login(){
        // username and password keep_in model
        // base on shiro complete login
        Subject subject = SecurityUtils.getSubject();
        // username and password info
        AuthenticationToken token = new UsernamePasswordToken(model.getUsername(),model.getPassword());
        try {
            subject.login(token);
            //将用户存入到session中

            return SUCCESS;
        }catch (Exception e){
            // login failed
            e.printStackTrace();
            return LOGIN;
        }
    }

    @Action(value = "user_logout",results = {@Result(name = "success",type = "json")})
    public String logout(){


        return SUCCESS;
    }

}
