package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
}
