package cn.itcast.bos.web.action.take_delivery;

import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.take_delivery.PromotionService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/*
    宣传活动管理
 */
@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class PromotionAction extends BaseAction<Promotion> {
    private File titleImgFile;
    private String titleImgFileFileName;

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    @Autowired
    private PromotionService promotionService;

    @Action(value = "promotion_save",results = {@Result(name = "success",type = "redirect",location = "./pages/take_delivery/promotion.html")})
    public String save() throws IOException {
        // 宣传图 上传,在数据表保存 宣传图路径
        String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
        String saveUrl = ServletActionContext.getRequest().getContextPath()+"/upload/";
        // 生成随机图片名
        UUID uuid = UUID.randomUUID();
        String ext = titleImgFileFileName.substring(titleImgFileFileName.lastIndexOf("."));
        String randomFileName =uuid+ext;
        // 保存图片(以绝对路径)
        // File.separator会自动判断操作系统的路径分隔符是左斜线还是右斜线
        File destFile = new File(savePath + File.separator + randomFileName);
        System.out.println(destFile.getAbsolutePath());
        FileUtils.copyFile(titleImgFile,destFile);

        // 将保存路径 相对工程web访问,保存model中
        model.setTitleImg(ServletActionContext.getRequest().getContextPath()
                +"/upload/"+randomFileName);

        // 调用ye业务层,完成活动任务数据的保存
        promotionService.save(model);

      return SUCCESS;
    }

    // 分页查询活动promotion_pageQuery
    @Action(value = "promotion_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        System.out.println("正在执行查询操作");
        // 构造分页查询参数
        Pageable pageable = new PageRequest(page - 1,rows);
        // 调用业务层 完成查询
        Page<Promotion> pageData = promotionService.findPageData(pageable);
        // 压入值栈
        ServletActionContext.getContext().getValueStack().push(pageData);
        return SUCCESS;
    }


}
