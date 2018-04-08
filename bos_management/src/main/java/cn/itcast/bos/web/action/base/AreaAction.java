package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
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
public class AreaAction extends ActionSupport implements ModelDriven<Area> {

    private Area area;

    @Override
    public Area getModel() {
        return area;
    }
    // 注入业务层对象
    @Autowired
    private AreaService areaService;
    // 接收上传文件
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    // 接收前段传递过来的文件,实现批量导入区域
    @Action(value = "area_batchImport")
    public String batchImport() throws Exception {
        // todo
        List<Area> areas = new ArrayList<Area>();
        // 编写解析代码逻辑
        // 基于.xls格式解析HSSF
        // 1.加载excel文件对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        // 2.读取一个sheet
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        // 2.读取sheet的每一行
        for(Row row:sheet){
            // 一行数据 对应 一个区域的对象
            if(row.getRowNum() == 0){
                // 第一行,跳过
                continue;
            }
            // 跳过空行
            if(row.getCell(0) == null
                    || StringUtils.isBlank(row.getCell(0).getStringCellValue())){
                continue;
            }

            Area area = new Area();
            area.setId(row.getCell(0).getStringCellValue());
            area.setProvince(row.getCell(1).getStringCellValue());
            area.setCity(row.getCell(2).getStringCellValue());
            area.setDistrict(row.getCell(3).getStringCellValue());
            area.setPostcode(row.getCell(4).getStringCellValue());

            // 基于pinyin4j生成城市编码和简码
            String province = area.getProvince();
            String city = area.getCity();
            String district = area.getDistrict();
            province = province.substring(0,province.length()-1);
            city = city.substring(0,city.length()-1);
            district = district.substring(0,district.length()-1);
            // 简码
            String[] headArray = PinYin4jUtils.getHeadByString(province+city+district);
            StringBuffer buffer = new StringBuffer();
            for (String headStr:headArray) {
                buffer.append(headStr);
            }
            String shortCode = buffer.toString();
            area.setShortcode(shortCode);
            // 城市编码
            String cityCode = PinYin4jUtils.hanziToPinyin(city,"");
            area.setCitycode(cityCode);
            areas.add(area);

        }
        // 调用业务层
        areaService.saveBatch(areas);

        return NONE;
    }

}