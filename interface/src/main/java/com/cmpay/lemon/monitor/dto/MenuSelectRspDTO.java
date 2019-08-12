package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.GenericRspDTO;

import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public class MenuSelectRspDTO extends GenericRspDTO {
    List<MenuDTO> menuList;

    public List<MenuDTO> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuDTO> menuList) {
        this.menuList = menuList;
    }

    @Override
    public String toString() {
        return "MenuSelectRspDTO{" +
                "menuList=" + menuList +
                '}';
    }
}
