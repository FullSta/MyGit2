package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu,Integer> {

    //一对多中的一方inner join fetch多方:查询结果是它把一方对多方的那个集合里面的对象都查询出来放到一方的集合中
    @Query("from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id =? order by m.priority")
    List<Menu> findByUser(int id);
}
