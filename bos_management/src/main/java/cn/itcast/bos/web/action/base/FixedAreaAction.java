package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
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
    // 查询未关联定区列表
    @Action(value = "fixedArea_findNoAssociationCustomers",results = {@Result(name="success",type = "json")})
    public String findNoAssociationCustomers(){
        // 使用webclient调用webService接口
        Collection<? extends Customer> collection = WebClient.
                create("http://localhost:9002/crm_management/services/customerService/noassociationcustomers")
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }

    // 查询已经关联定区列表
    @Action(value = "fixedArea_findHasAssociationFixedAreaCustomers",results = {@Result(name="success",type = "json")})
    public String findHasAssociationFixedAreaCustomers(){
        // 使用webclient调用webService接口
        Collection<? extends Customer> collection = WebClient.
                create("http://localhost:9002/crm_management/services/customerService/associationfixedareacustomers/"+ model.getId())
                .accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }

    //属性驱动
    private String[] customerIds;

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    //关联客户到定区
    @Action(value = "fixedArea_associationCustomersToFixedArea",results = {@Result(name = "success",type = "redirect",location = "./pages/base/fixed_area.html")})
    public String associationCustomersToFixedArea(){
        String customerIdStr = StringUtils.join(customerIds,",");
        // System.out.println(model.getId());
        WebClient.create(
                "http://localhost:9002/crm_management/services/customerService"
                        + "/associationcustomerstofixedarea?customerIdStr="
                        + customerIdStr + "&fixedAreaId=" + model.getId()).put(
                null);
        return SUCCESS;
    }

    // 关联快递员到定区
    // 属性驱动
    private Integer courierId;
    private Integer takeTimeId;

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    // 调用业务层,定区关联快递员
    @Action(value = "fixedArea_associationCourierToFixedArea",results = {@Result(name = "success",type = "redirect",location = "./pages/base/fixed_area.html")})
    public String associationCourierToFixedArea(){
        fixedAreaService.associationCourierToFixedArea(model,courierId,takeTimeId);
        return SUCCESS;

    }
}
