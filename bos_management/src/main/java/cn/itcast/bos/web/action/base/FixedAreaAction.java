package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {
    @Autowired
    private FixedAreaService fixedAreaService;

    @Action(value = "fixedArea_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        // 构造pageable
        Pageable pageable = new PageRequest(page-1,rows);
        // 构造条件查询
        Specification<FixedArea> specification = new Specification<FixedArea>() {
            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 条件的集合
                List<Predicate> list = new ArrayList<Predicate>();
                // 构造查询条件
                if (StringUtils.isNotBlank(model.getId())){
                    // 根据定去编号查询
                    Predicate p1 = cb.equal(root.get("id").as(String.class),model.getId());
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(model.getCompany())){
                    // 根据定去编号查询
                    Predicate p2 = cb.equal(root.get("company").as(String.class),
                            "%"+model.getCompany()+"%");
                    list.add(p2);
                }

                return cb.and(list.toArray(new Predicate[0]));
            }
        };

        // 调用业务层查询数据
        Page<FixedArea> pageData = fixedAreaService.findPageData(specification,pageable);
        // 压入值栈
        System.out.println("得到的pageData:"+pageData);
        pushPageDataToValueStack(pageData);

        return SUCCESS;
    }

    //保存定区
    @Action(value = "fixedArea_save",results = {@Result(name = "success",type = "redirect",location = "./pages/base/fixed_area.html")})
    public String save(){
        System.out.println("正在保存定区...");
        System.out.println(model);
        fixedAreaService.save(model);
        return SUCCESS;
    }
}
