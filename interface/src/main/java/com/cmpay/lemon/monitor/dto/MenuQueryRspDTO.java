package com.cmpay.lemon.monitor.dto;

import com.cmpay.framework.data.response.GenericRspDTO;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public class MenuQueryRspDTO extends GenericRspDTO {
    /**
     * @Fields menuId
     */
    private Long menuId;
    /**
     * @Fields parentId 父菜单ID，一级菜单为0
     */
    private Long parentId;
    /**
     * 父菜单名
     */
    private String parentName;
    /**
     * @Fields name 菜单名称
     */
    private String name;
    /**
     * @Fields perms 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;
    /**
     * @Fields type 类型   0：目录   1：菜单   2：按钮
     */
    private Integer type;

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MenuQueryRspDTO{" +
                "menuId=" + menuId +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", name='" + name + '\'' +
                ", perms='" + perms + '\'' +
                ", type=" + type +
                '}';
    }
}
