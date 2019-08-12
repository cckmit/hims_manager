package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.bo.MenuBO;
import com.cmpay.lemon.monitor.bo.UserMenuBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dao.IMenuExtDao;
import com.cmpay.lemon.monitor.dao.IUserRoleExtDao;
import com.cmpay.lemon.monitor.entity.MenuDO;
import com.cmpay.lemon.monitor.enums.MenuEnum;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemMenuService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 系统菜单服务
 *
 * @author : 曾益
 * @date : 2018/11/9
 */
@Service
@Transactional
public class SystemMenuServiceImpl implements SystemMenuService {
    @Autowired
    private IMenuExtDao iMenuDao;
    @Autowired
    private IUserRoleExtDao iUserRoleDao;

    /**
     * 获取用户菜单
     *
     * @param menuBO
     * @return
     */
    @Override
    public UserMenuBO getUserMenus(UserMenuBO menuBO) {
        Long userNo = menuBO.getUserNo();
        List<MenuBO> menuBOList = null;
        //超级管理员
        if (userNo == MonitorConstants.SUPER_ADMIN) {
            menuBOList = getAllMenuList(null);
        } else {
            List<Long> menuIds = getAllMenuId(userNo);
            menuBOList = getAllMenuList(menuIds);
        }
        menuBO.setMenus(menuBOList);
        return menuBO;
    }

    @Override
    public UserMenuBO getAllMenus() {
        List<MenuDO> menuDOS = iMenuDao.queryAllMenus();
        List<MenuBO> menuBOS = BeanConvertUtils.convertList(menuDOS, MenuBO.class);
        UserMenuBO userMenuBO = new UserMenuBO();
        userMenuBO.setMenus(menuBOS);
        return userMenuBO;
    }

    @Override
    public MenuBO getMenuById(Long menuId) {
        MenuDO menuDO = iMenuDao.get(menuId);
        MenuBO menuBO = new MenuBO();
        BeanUtils.copyProperties(menuBO, menuDO);
        return menuBO;
    }

    @Override
    public List<MenuBO> getListParentId(Long parentId, List<Long> menus) {
        List<MenuBO> menuBOList = getListParentId(parentId);
        if (JudgeUtils.isNull(menus)) {
            return menuBOList;
        }
        List<MenuBO> userMenus = new ArrayList<>();
        menuBOList.stream()
                .filter(menuBO -> menus.contains(menuBO.getMenuId()))
                .forEach(menu -> userMenus.add(menu));
        return userMenus;
    }

    /**
     * 根据父节点查询菜单
     *
     * @param parentId
     * @return
     */
    @Override
    public List<MenuBO> getListParentId(Long parentId) {
        List<MenuDO> menuDOList = iMenuDao.queryListParentId(parentId);
        List<MenuBO> menuBOList = new ArrayList<>();
        menuDOList.stream().forEach(menu -> {
            MenuBO menuBO = new MenuBO();
            BeanUtils.copyProperties(menuBO, menu);
            menuBOList.add(menuBO);
        });
        return menuBOList;
    }

    @Override
    public List<MenuBO> getMenus(MenuBO menuBO) {
        MenuDO menuDO = new MenuDO();
        BeanUtils.copyProperties(menuDO, menuBO);
        List<MenuDO> menuDOS = iMenuDao.find(menuDO);
        return BeanConvertUtils.convertList(menuDOS, MenuBO.class);
    }

    @Override
    public List<MenuBO> getNotButtonList() {
        List<MenuDO> menuDOList = iMenuDao.queryNotButtonList();
        List<MenuBO> menuBOList = BeanConvertUtils.convertList(menuDOList, MenuBO.class);
        return Optional.ofNullable(menuBOList).orElse(Collections.emptyList());
    }

    @Override
    public void add(MenuBO menuBO) {
        verifyFormData(menuBO);
        MenuDO menuDO = new MenuDO();
        BeanUtils.copyProperties(menuDO, menuBO);
        int res = iMenuDao.insert(menuDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
    }

    /**
     * 更新数据
     *
     * @param menuBO
     */
    @Override
    public void update(MenuBO menuBO) {
        //校验
        verifyFormData(menuBO);
        //更新
        MenuDO menuDO = new MenuDO();
        BeanUtils.copyProperties(menuDO, menuBO);
        int res = iMenuDao.update(menuDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Long menuId) {
        if (menuId <= MonitorConstants.SYSTEM_MENU_MAX) {
            BusinessException.throwBusinessException(MsgEnum.SYSTEM_MENU_CANNOT_DELETE);
        }

        List<MenuDO> childMenus = iMenuDao.queryListParentId(menuId);
        if (childMenus.size() > 0) {
            BusinessException.throwBusinessException(MsgEnum.DELETE_SUBMENU_OR_BUTTON_FIRST);
        }

        int res = iMenuDao.delete(menuId);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_DELETE_FAILED);
        }
    }

    /**
     * 查询无button菜单
     *
     * @return
     */
    private List<MenuBO> queryNotButtonList() {
        List<MenuDO> menuDOList = iMenuDao.queryNotButtonList();
        List<MenuBO> menuBOList = new ArrayList<>();
        menuDOList.stream().forEach(menu -> {
            MenuBO menuBO = new MenuBO();
            BeanUtils.copyProperties(menuBO, menu);
            menuBOList.add(menuBO);
        });
        return menuBOList;
    }

    /**
     * 获取用户拥有的菜单
     *
     * @param userNo
     * @return
     */
    private List<Long> getAllMenuId(Long userNo) {
        List<Long> menus = iUserRoleDao.getAllMenus(userNo);
        return menus;
    }

    /**
     * 查询所有菜单
     *
     * @param menuIdList
     * @return
     */
    private List<MenuBO> getAllMenuList(List<Long> menuIdList) {
        //查询根菜单列表
        List<MenuBO> menuList = getListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);
        return menuList;
    }

    /**
     * 递归获取下级菜单
     *
     * @param menuList
     * @param menuIdList
     * @return
     */
    private List<MenuBO> getMenuTreeList(List<MenuBO> menuList, List<Long> menuIdList) {
        List<MenuBO> subMenuList = new ArrayList<MenuBO>();
        for (MenuBO entity : menuList) {
            //目录
            if (entity.getType() == MenuEnum.CATALOG.getValue()) {
                entity.setList(getMenuTreeList(getListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }

    /**
     * 校验菜单操作表单数据合法性
     *
     * @param menu
     */
    private void verifyFormData(MenuBO menu) {
        if (JudgeUtils.isBlank(menu.getName())) {
            BusinessException.throwBusinessException(MsgEnum.MENU_NAME_CANNOT_NULL);
        }

        if (menu.getParentId() == null) {
            BusinessException.throwBusinessException(MsgEnum.PARENT_MENU_CANNOT_NULL);
        }

        //上级菜单类型
        int parentType = MenuEnum.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            MenuDO parentMenu = iMenuDao.get(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == MenuEnum.CATALOG.getValue() || menu.getType() == MenuEnum.MENU.getValue()) {
            if (parentType != MenuEnum.CATALOG.getValue()) {
                BusinessException.throwBusinessException(MsgEnum.PARENT_MENU_MUSTBE_CATALOG);
            }
        }

        //按钮
        if (menu.getType() == MenuEnum.BUTTON.getValue() && parentType != MenuEnum.MENU.getValue()) {
            BusinessException.throwBusinessException(MsgEnum.PARENT_MENU_MUSTBE_MENU);
        }
    }
}
