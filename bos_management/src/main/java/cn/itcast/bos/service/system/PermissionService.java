package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAll();

    Permission save(Permission model);
}
