package cn.itcast.bos.web.action;

import cn.itcast.bos.constant.Constants;
import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.ws.rs.core.MediaType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import static cn.itcast.bos.constant.Constants.BOS_MANAGEMENT_URL;

@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
@Controller
public class PromotionAction extends BaseAction<Promotion> {

    @Action(value = "promotion_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){

        // base on webservlet,get bos_management^s list and data
        PageBean<Promotion> pageBean = WebClient.create(BOS_MANAGEMENT_URL+"/services/promotionService/pageQuery?page="
                +page+"&rows="+rows)
                .accept(MediaType.APPLICATION_JSON).get(PageBean.class);

        // pressure entry stack
        ServletActionContext.getContext().getValueStack().push(pageBean);
        return SUCCESS;
    }

    // showDetails
    @Action(value = "promotion_showDetail")
    public String showDetail() throws IOException, TemplateException {
        System.out.println("正在fore执行造页面的操作...");
        // 先判断 id 对应html 是否存在，如果存在 直接返回
        String htmlRealPath = ServletActionContext.getServletContext()
                .getRealPath("/freemarker");
        File htmlFile = new File(htmlRealPath + "/" + model.getId() + ".html");

        // 如果html文件不存在 ，查询数据库，结合freemarker模板生成 页面
        if (!htmlFile.exists()) {
            Configuration configuration = new Configuration(
                    Configuration.VERSION_2_3_22);
            configuration.setDirectoryForTemplateLoading(new File(
                    ServletActionContext.getServletContext().getRealPath(
                            "/WEB-INF/freemarker_templates")));
            // 获取模板对象
            Template template = configuration
                    .getTemplate("promotion_detail.ftl");

            // 动态数据
            // 这是能收到数据的路径http://localhost:9001/bos_management/services/promotionService/promotion/41
            System.out.println(Constants.BOS_MANAGEMENT_URL + "/services/promotionService/promotion/" + model.getId());
            Promotion promotion = WebClient
                    .create(Constants.BOS_MANAGEMENT_URL + "/services/promotionService/promotion/"
                            + model.getId()).accept(MediaType.APPLICATION_JSON)
                    .get(Promotion.class);
            Map<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("promotion", promotion);

            // 合并输出
            template.process(parameterMap, new OutputStreamWriter(
                    new FileOutputStream(htmlFile), "utf-8"));

        }

        // 存在 ，直接将文件返回
        ServletActionContext.getResponse().setContentType(
                "text/html;charset=utf-8");
        FileUtils.copyFile(htmlFile, ServletActionContext.getResponse()
                .getOutputStream());

        return NONE;
    }



}
