package com.cmpay.lemon.monitor.bo;

import java.util.List;

/**
 * 用户权限
 *
 * @author : 曾益
 * @date : 2018/11/5
 */
public class UserPermissionBO {
    private Long userNo;
    private List<String> permissions;

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "UserPermissionBO{" +
                "userNo=" + userNo +
                ", permissions=" + permissions +
                '}';
    }
}
