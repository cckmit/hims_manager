package com.cmpay.lemon.monitor.bo;

import java.util.List;

/**
 * 用户菜单
 *
 * @author : 曾益
 * @date : 2018/11/5
 */
public class UserMenuBO {
    private Long userNo;
    private List<MenuBO> menus;

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public List<MenuBO> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuBO> menus) {
        this.menus = menus;
    }

    @Override
    public String toString() {
        return "UserMenuBO{" +
                "userNo=" + userNo +
                ", menus=" + menus +
                '}';
    }
}
