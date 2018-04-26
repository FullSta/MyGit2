package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.dao.system.PermissionRepository;
import cn.itcast.bos.dao.system.RoleRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void save(Role model, String[] permissionIds, String menuIds) {
        roleRepository.save(model);
        // 关联权限
        if(permissionIds != null){
            for (String permissionId : permissionIds){
                Permission permission = permissionRepository.findOne(Integer.parseInt(permissionId));
                model.getPermissions().add(permission);
            }
        }

        // 关联菜单
        if(StringUtils.isNotBlank(menuIds)){
            String[] menuIdArray = menuIds.split(",");
            for (String menuId : menuIdArray){
                Menu menu = menuRepository.findOne(Integer.parseInt(menuId));
                model.getMenus().add(menu);
            }
        }

    }
}
