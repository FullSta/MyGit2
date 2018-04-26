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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
        HSSFSheet sheet;
        try (HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file))) {
            // 2.读取一个sheet
            sheet = hssfWorkbook.getSheetAt(0);
        }
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

    @Action(value = "subArea_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        System.out.println("fenyw cahxun");
        // 构造分页查询对象
        Pageable pageable = new PageRequest(page - 1, rows);
        // 构造条件查询条件
        Specification<SubArea> specification = new Specification<SubArea>() {
            @Override
            public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                // 查询条件
                if (model.getArea() != null){
                    if (StringUtils.isNotBlank(model.getArea().getProvince())){
                        Predicate p1 = cb.like(root.get("province")
                                .as(String.class),"%"+model.getArea().getProvince()+"%");
                        list.add(p1);
                    }
                    if (StringUtils.isNotBlank(model.getArea().getCity())){
                        Predicate p2 = cb.like(root.get("city")
                                .as(String.class),"%"+model.getArea().getCity()+"%");
                        list.add(p2);
                    }
                    if (StringUtils.isNotBlank(model.getArea().getDistrict())){
                        Predicate p3 = cb.like(root.get("district")
                                .as(String.class),"%"+model.getArea().getDistrict()+"%");
                        list.add(p3);
                    }
                }
                if (model.getFixedArea() != null){
                    if (StringUtils.isNotBlank(model.getFixedArea().getFixedAreaName())){
                        Predicate p4 = cb.like(root.get("district")
                                .as(String.class),"%"+model.getFixedArea().getFixedAreaName()+"%");
                        list.add(p4);
                    }
                }
                if (StringUtils.isNotBlank(model.getKeyWords())){
                    Predicate p5 = cb.like(root.get("district")
                            .as(String.class),"%"+model.getKeyWords()+"%");
                    list.add(p5);
                }

                return cb.and(list.toArray(new Predicate[0]));

            }
        };
        // 调用业务层完成查询
        Page<SubArea> pageData = subAreaService.findPageData(specification,pageable);
        // 压入值栈,,调用baseAction定义的方法
        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }

    @Action(value = "subarea_save",results = {@Result(name = "success",type = "redirect",location = "./pages/base/sub_area.html")})
    public String save(){
        subAreaService.save(model);

        return SUCCESS;
    }


}
