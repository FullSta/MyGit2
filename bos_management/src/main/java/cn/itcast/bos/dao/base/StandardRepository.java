package cn.itcast.bos.dao.base;

import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.springframework.data.jpa.repository.JpaRepository;

import cn.itcast.bos.domain.base.Standard;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.xml.ws.Action;

public interface StandardRepository extends JpaRepository<Standard, Integer> {
	
	// 根据收派标准名称查询
	public List<Standard> findByName(String name);

	// 在service层直接调用，不用写出来
	// public Standard save(Standard standard);


	/*下面是为了学习三种查询的方式做的实验
	@Query(value="from Standard where name = ?" ,nativeQuery=false)
	// nativeQuery 为 false 执行JPQL 、 为true 配置SQL
	public List<Standard> queryName(String name);

	@Query// 这种是在bean类上去写query语句
	public List<Standard> queryName2(String name);

	@Query(value="update Standard set minLength=?2 where id =?1")
	@Modifying //表明这是带修改的查询
	public void updateMinlength(Integer id, Integer minLength);*/
}
