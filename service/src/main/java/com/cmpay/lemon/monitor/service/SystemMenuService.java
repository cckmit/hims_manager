package com.cmpay.lemon.monitor.service;


import com.cmpay.lemon.monitor.bo.MenuBO;
import com.cmpay.lemon.monitor.bo.UserMenuBO;

import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/9
 */
public interface SystemMenuService {
    /**
     * 获取用户菜单
     *
     * @param menuBO
     * @return
     */
    UserMenuBO getUserMenus(UserMenuBO menuBO);

    /**
     * 查询所有菜单
     *
     * @return
     */
    UserMenuBO getAllMenus();

    /**
     * 根据Id查找菜单信息
     *
     * @param menuId
     * @return
     */
    MenuBO getMenuById(Long menuId);

    /**
     * 根据父节点查询菜单列表
     *
     * @param parentId
     * @param menus
     * @return
     */
    List<MenuBO> getListParentId(Long parentId, List<Long> menus);

    /**
     * 根据父节点查询菜单列表
     *
     * @param parentId
     * @return
     */
    List<MenuBO> getListParentId(Long parentId);

    /**
     * 查询菜单列表
     *
     * @param menuBO
     * @return
     */
    List<MenuBO> getMenus(MenuBO menuBO);

    /**
     * 获取无按钮菜单
     *
     * @return
     */
    List<MenuBO> getNotButtonList();

    /**
     * 新增菜单
     *
     * @param menuBO
     */
    void add(MenuBO menuBO);

    /**
     * 更新
     *
     * @param menuBO
     * @return
     */
    void update(MenuBO menuBO);

    /**
     * 删除
     *
     * @param menuId
     */
    void delete(Long menuId);
}
