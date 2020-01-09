package com.cmpay.lemon.monitor.service;

import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.PageQueryBO;
import com.cmpay.lemon.monitor.bo.RoleBO;
import com.cmpay.lemon.monitor.entity.UserDO;

import java.util.List;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
public interface SystemRoleService {
    /**
     * 查询用户角色
     *
     * @param userNo
     * @return
     */
    List<RoleBO> getRoles(Long userNo);

    /**
     * 查询所有角色
     *
     * @return
     */
    List<RoleBO> getAllRoles();

    /**
     * 根据role id获取role对象
     *
     * @param roleId
     * @return
     */
    RoleBO getRole(Long roleId);

    /**
     * 根据role id获取所有的menus
     *
     * @param roleId
     * @return
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 根据user no查找用户所有角色
     *
     * @param userNo
     * @return
     */
    List<Long> getRolesByUserNo(Long userNo);

    /**
     * 分页查询
     *
     * @param roleBO
     * @param pageQueryBO
     * @return
     */
    PageInfo<RoleBO> getRolesPage(RoleBO roleBO, PageQueryBO pageQueryBO);

    /**
     * 角色新增
     *
     * @param roleBO
     */
    Long add(RoleBO roleBO);

    /**
     * 角色菜单新增
     *
     * @param roleId
     * @param menuIds
     */
    void addRoleMenu(Long roleId, List<Long> menuIds);

    /**
     * 角色修改
     *
     * @param roleBO
     */
    void update(RoleBO roleBO);

    /**
     * 角色菜单修改
     *
     * @param roleId
     * @param menuIds
     */
    void updateRoleMenu(Long roleId, List<Long> menuIds);

    /**
     * 角色删除
     *
     * @param roleId
     */
    void delete(Long roleId);

    /**
     * 角色批量删除
     *
     * @param roleIds
     */
    void deleteBatch(List<Long> roleIds);

    /**
     * 依据权限编号查询该权限人员邮件
     *
     * @param roleId
     */
    List<UserDO> getPermissionGroupMembers(Long roleId);
}
