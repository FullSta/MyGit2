package cn.itcast.bos.web.action.take_delivery;

import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class ImageAction extends BaseAction<Object> {
    private File imgFile; // 这是上文件的默认名称
    private String imgFileFileName;
    private String imgFileContentType;

    public void setImgFile(File imgFile) {
        this.imgFile = imgFile;
    }

    public void setImgFileFileName(String imgFileFileName) {
        this.imgFileFileName = imgFileFileName;
    }

    public void setImgFileContentType(String imgFileContentType) {
        this.imgFileContentType = imgFileContentType;
    }

    @Action(value = "image_upload",results = {@Result(name = "success",type = "json")})
    public String upload() throws IOException {
        System.out.println("文件:"+imgFile);
        System.out.println("文件名:"+imgFileFileName);
        System.out.println("文件类型:"+imgFileContentType);

        String savePath = ServletActionContext.getServletContext().getRealPath("/upload/");
        String saveUrl = ServletActionContext.getRequest().getContextPath()+"/upload/";

        // 生成随机图片名
        UUID uuid = UUID.randomUUID();
        String ext = imgFileFileName.substring(imgFileFileName.lastIndexOf("."));
        String randomFileName = uuid+ext;

        // 保存图片(绝对路径),File.separator:根据操作系统自动选择/或者\
        File destFile = new File(savePath+File.separator+randomFileName);
        System.out.println(destFile.getAbsolutePath());
        FileUtils.copyFile(imgFile,destFile);

        // 通知浏览器文件上传成功
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("error",0);
        result.put("url",saveUrl+randomFileName); //返回相对路径
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }

    @Action(value = "image_action",results = {@Result(name = "success",type = "json")})
    public String manage(){
        // 根目录路径,可以指定绝对路径.比如:d:/xxx/upload/xxx.jpg
        String rootPath = ServletActionContext.getServletContext().getRealPath("/")+"upload/";
        String rootUrl = ServletActionContext.getRequest().getContextPath()+"/upload/";
        // 遍历录取的文件信息
        List<Map<String,Object>> fileList = new ArrayList<Map<String,Object>>();
        // 当前上传目录
        File currentPathFile = new File(rootPath);
        // 文件拓展名
        String[] fileTypes = new String[] {"gif","jpg","jpeg","png","bmp"};
        if (currentPathFile.listFiles() != null){
            for (File file:currentPathFile.listFiles()){
                Map<String, Object> hash = new HashMap<String, Object>();
                String fileName = file.getName();
                if (file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if (file.isFile()) {
                    String fileExt = fileName.substring(
                            fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String> asList(fileTypes)
                            .contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime",
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file
                                .lastModified()));
                fileList.add(hash);
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("moveup_dir_path", "");
        result.put("current_dir_path", rootPath);
        result.put("current_url", rootUrl);
        result.put("total_count", fileList.size());
        result.put("file_list", fileList);
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }

}
