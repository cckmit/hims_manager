package com.cmpay.lemon.monitor.service;


import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.UserInfoBO;
import com.cmpay.lemon.monitor.bo.UserInfoQueryBO;
import com.cmpay.lemon.monitor.bo.UserPermissionBO;
import com.cmpay.lemon.monitor.entity.UserDO;

import java.util.List;

/**
 * 系统用户服务
 *
 * @author : 曾益
 * @date : 2018/11/5
 */
public interface SystemUserService {
    /**
     * 获取用户权限
     *
     * @param permissionBO
     * @return
     */
    UserPermissionBO getUserPermissions(UserPermissionBO permissionBO);

    /**
     * 查询用户列表
     *
     * @param queryBO
     * @return
     */
    PageInfo<UserDO> findUsers(UserInfoQueryBO queryBO);

    /**
     * 查询用户信息
     *
     * @param userInfoBO
     * @return
     */
    UserInfoBO getUserInfo(UserInfoBO userInfoBO);

    /**
     * 用户新增
     *
     * @param user
     */
    Long add(UserInfoBO user);

    /**
     * 添加用户角色
     *
     * @param userNo
     * @param roleIds
     */
    void addUserRole(Long userNo, List<Long> roleIds);

    /**
     * 用户删除
     *
     * @param userNo
     */
    void delete(Long userNo);

    /**
     * 批量删除用户
     *
     * @param userNos
     */
    void deleteBatch(List<Long> userNos);

    /**
     * 用户修改
     *
     * @param user
     */
    void update(UserInfoBO user);

    /**
     * 用户密码更新
     *
     * @param oldPassword
     * @param newPassword
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 更新用户角色
     *
     * @param userNo
     * @param roleIds
     */
    void updateUserRole(Long userNo, List<Long> roleIds);

    /**
     * 查询所有用户
     *
     * @return
     */
    List<UserInfoBO> getUserList();


    /**
     * 获取用户名全名
     *
     * @return
     */
    String  getFullname(String loginname);

     void syncOldTable();
}
