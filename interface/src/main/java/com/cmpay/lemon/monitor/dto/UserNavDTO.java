package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.GenericRspDTO;

import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/5
 */
public class UserNavDTO extends GenericRspDTO {
    List<String> permissions;
    List<MenuDTO> menusList;

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<MenuDTO> getMenusList() {
        return menusList;
    }

    public void setMenusList(List<MenuDTO> menusList) {
        this.menusList = menusList;
    }

    @Override
    public String toString() {
        return "UserNavDTO{" +
                "permissions=" + permissions +
                ", menusList=" + menusList +
                '}';
    }
}
