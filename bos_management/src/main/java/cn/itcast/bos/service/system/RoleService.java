package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Role;

public interface RoleService {
    void save(Role model, String[] permissionIds, String menuIds);
}
