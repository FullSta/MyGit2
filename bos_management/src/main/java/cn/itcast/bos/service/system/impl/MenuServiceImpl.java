package cn.itcast.bos.service.system.impl;

import cn.itcast.bos.dao.system.MenuRepository;
import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Controller
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public void save(Menu menu) {
        // 防止用户没有选中父菜单
        if (menu.getParentMenu() != null && menu.getParentMenu().getId() != 0){
            menu.setParentMenu(null);
        }
        menuRepository.save(menu);
    }

    @Override
    public List<Menu> findByUser(User user) {
        // 针对admin用户显示 所有菜单
        if (user.getUsername().equals("admin")){
            return menuRepository.findAll();
        }else {
            // 根据用户角色查询菜单
            return menuRepository.findByUser(user.getId());
        }


    }
}
