package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.service.base.SubAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
@Controller
public class SubAreaAction extends BaseAction<SubArea> {
    @Autowired
    private SubAreaService subAreaService;

    @Action(value = "subArea_findAll",results = {@Result(name = "success",type = "json")})
    public String findAll(){
        List<SubArea> list = subAreaService.findAll();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }

    // 接收上传文件
    private File file;

    public void setFile(File file) {
        this.file = file;
    }
    // 注入
    @Autowired
    private FixedAreaService fixedAreaService;

    @Autowired
    private AreaService areaService;

    @Action(value = "subarea_batchImport",results = {@Result(name = "success",type = "redirect",location = "./pages/base/sub_area.html")})
    public String batchImport() throws Exception {
        System.out.println("前段的表传到后台了...");
        System.out.println(file.getName());
        List<SubArea> areas = new ArrayList<SubArea>();
        // 编写解析代码逻辑
        // 基于.xls格式解析HSSF
        // 1.加载excel文件对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        // 2.读取一个sheet
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        // 2.读取sheet的每一行
        for (Row row : sheet) {
            // 一行数据 对应 一个区域的对象
            if (row.getRowNum() == 0) {
                // 第一行,跳过
                continue;
            }
            // 跳过空行
            if (row.getCell(0) == null
                    || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                continue;
            }
            SubArea subarea = new SubArea();
            subarea.setId(row.getCell(0).getStringCellValue());

//            area.getFixedArea().setId((row.getCell(1).getStringCellValue()));
//            area.getArea().setId(row.getCell(2).getStringCellValue());

            subarea.setKeyWords(row.getCell(3).getStringCellValue());
            subarea.setStartNum(row.getCell(4).getStringCellValue());
            subarea.setEndNum(row.getCell(5).getStringCellValue());
            subarea.setSingle(row.getCell(6).getStringCellValue());
            subarea.setAssistKeyWords(row.getCell(7).getStringCellValue());
            // 根据表中的数据得到fixedArea
            String fixedAreaId = row.getCell(1).getStringCellValue();
            FixedArea fixedArea = fixedAreaService.findOne(fixedAreaId);
            // 根据表中区域编码得到area
            String areaId = row.getCell(2).getStringCellValue();
//            System.out.println(areaId);
            Area area = areaService.findOne(areaId);
//            System.out.println(area.getId());
            subarea.setFixedArea(fixedArea);
            subarea.setArea(area);
            areas.add(subarea);

        }
        // 调用业务层
        subAreaService.saveBatch(areas);

        return NONE;
    }
}
