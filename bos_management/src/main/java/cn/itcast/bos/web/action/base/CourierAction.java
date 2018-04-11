package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
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

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {


    // 先来一发模型驱动
    private Courier courier= new Courier();
    @Override
    public Courier getModel() {
        return courier;
    }
    @Autowired
    private CourierService courierService;

    // 添加快递员的方法
    @Action(value="courier_save",results = {@Result(name="success",type = "redirect",location = "./pages/base/courier.html")})
    public String save(){
        System.out.println("保存快递员中...");
        courierService.save(courier);
        return SUCCESS;
    }



    //属性驱动接受客户端分页参数
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }
    // 开始操作分页查询(待条件)
    @Action(value = "courier_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        // 封装Pageable对象
        Pageable pageable= new PageRequest(page-1,rows);
        // 封装条件查询对象
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            // root用于获取属性字段,CourieraQuery可以用于简单条件查询,CourierBuilder用于构造复杂条件查询
            public javax.persistence.criteria.Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                //简单单标查询,再组合在一起
                if(StringUtils.isNotBlank(courier.getCourierNum())){
                    Predicate p1=cb.equal(
                      root.get("courierNum").as(String.class),
                      courier.getCourierNum());
                    list.add(p1);
                }
                if(StringUtils.isNotBlank(courier.getCompany())){
                    Predicate p2=cb.like(
                      root.get("company").as(String.class),
                      "%"+courier.getCompany()+"%");
                    list.add(p2);
                }
                //模糊查询
                if(StringUtils.isNotBlank(courier.getType())){
                    Predicate p3=cb.equal(
                      root.get("type").as(String.class),
                      courier.getType());
                    list.add(p3);
                }
                //多表查询,指定为内连接
                Join<Courier,Standard> standardJoin = root.join("standard",
                        JoinType.INNER);
                if(courier.getStandard()!=null
                        && StringUtils.isNotBlank(courier.getStandard().getName())){
                    Predicate p4 = cb.like(standardJoin.get("name").as(String.class),"%"
                    +courier.getStandard().getName()+"%");
                    list.add(p4);
                }

                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 调用业务层,返回page
        Page<Courier> pageDate = courierService.findPageDate(specification,
                pageable);
        //讲返回的pageable对象,装换为datagrid需要的格式
        Map<String,Object> result = new HashMap<String,Object>();
        result.put("total",pageDate.getTotalElements());
        result.put("rows",pageDate.getContent());
        //将结果对象 压入值站顶部
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    // 属性驱动
    private String ids;

    public void setIds(String ids) {
        this.ids = ids;
    }

    //删除快递员状态
    @Action(value = "courier_delBatch",results = {@Result(name="success",type = "redirect",location = "./pages/base/courier.html")})
    public String delBatch(){
        //按照,分割ids
        String[] idArray = ids.split(",");
        System.out.println(idArray[0].toString()+"这是获取到的删除的id");
        //调用业务层,批量作废
        courierService.delBatch(idArray);
        return SUCCESS;
    }

    // 查询没有分配到分区的快递员
    @Action(value = "courier_noassociation",results = {@Result(name="success",type = "json")})
    public String findnoassociation(){
        List<Courier> list = courierService.findnoassociation();
        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }

}
