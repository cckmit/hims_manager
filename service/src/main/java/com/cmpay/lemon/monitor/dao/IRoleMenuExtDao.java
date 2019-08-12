/*
 * @ClassName IRoleMenuDao
 * @Description
 * @version 1.0
 * @Date 2018-11-05 12:18:45
 */
package com.cmpay.lemon.monitor.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRoleMenuExtDao extends IRoleMenuDao {
    /**
     * 角色删除
     *
     * @param roleId
     * @return
     */
    int deleteRoleMenu(Long roleId);

    /**
     * 获取角色菜单
     *
     * @param roleId
     * @return
     */
    List<Long> queryButtonMenuIdsByRoleId(Long roleId);
}
