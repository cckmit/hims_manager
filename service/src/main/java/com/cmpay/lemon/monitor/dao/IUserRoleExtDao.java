package com.cmpay.lemon.monitor.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: zhou_xiong
 */
@Mapper
public interface IUserRoleExtDao extends IUserRoleDao {
    /**
     * 获取用户权限列表
     *
     * @param userNo
     * @return
     */
    List<String> getAllPermissions(Long userNo);

    /**
     * 获取用户菜单列表
     *
     * @param userNo
     * @return
     */
    List<Long> getAllMenus(Long userNo);

    /**
     * 根据用户ID删除其拥有的角色
     *
     * @param userNo
     * @return
     */
    int deleteUserRoleByUserNo(Long userNo);

    /**
     * 查询用户拥有角色
     *
     * @param userNo
     * @return
     */
    List<Long> queryRolesByUserNo(Long userNo);
}
