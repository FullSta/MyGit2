package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Integer> {
}
