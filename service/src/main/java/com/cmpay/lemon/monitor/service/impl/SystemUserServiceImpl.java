package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.IdGenUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.MenuBO;
import com.cmpay.lemon.monitor.bo.UserInfoBO;
import com.cmpay.lemon.monitor.bo.UserInfoQueryBO;
import com.cmpay.lemon.monitor.bo.UserPermissionBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dao.IUserExtDao;
import com.cmpay.lemon.monitor.dao.IUserRoleExtDao;
import com.cmpay.lemon.monitor.entity.UserDO;
import com.cmpay.lemon.monitor.entity.UserRoleDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemMenuService;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.CryptoUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 系统用户服务
 *
 * @author : 曾益
 * @date : 2018/11/5
 */
@Service
@Transactional
public class SystemUserServiceImpl implements SystemUserService {
    @Autowired
    private IUserRoleExtDao iUserRoleDao;
    @Autowired
    private IUserExtDao iUserDao;
    @Autowired
    private SystemMenuService systemMenuService;

    /**
     * 获取用户权限
     *
     * @param permissionBO
     * @return
     */
    @Override
    public UserPermissionBO getUserPermissions(UserPermissionBO permissionBO) {
        List<String> permissions = new ArrayList<>();
        if (MonitorConstants.SUPER_ADMIN == permissionBO.getUserNo()) {
            List<MenuBO> menuBOS = systemMenuService.getMenus(new MenuBO());
            permissions = menuBOS.stream().map(o -> o.getPerms()).collect(Collectors.toList());
        } else {
            permissions = iUserRoleDao.getAllPermissions(permissionBO.getUserNo());
        }
        List<String> finalPermissions = new ArrayList<>();
        for (String p : permissions) {
            if (JudgeUtils.isNotBlank(p)) {
                finalPermissions.addAll(Arrays.asList(p.split(",")));
            }
        }
        permissionBO.setPermissions(Optional.ofNullable(finalPermissions).orElse(Collections.emptyList()));
        return permissionBO;
    }

    /**
     * 分页查询
     *
     * @param queryBO
     * @return
     */
    @Override
    public PageInfo<UserDO> findUsers(UserInfoQueryBO queryBO) {
        UserDO userDO = new UserDO();
        userDO.setUsername(queryBO.getUserName());
        if(JudgeUtils.isBlank(userDO.getUsername())){
            userDO.setUsername(null);
        }
        PageInfo<UserDO> pageInfo = PageUtils.pageQueryWithCount(queryBO.getPageNum(), queryBO.getPageSize(), () -> iUserDao.find(userDO));
        return pageInfo;
    }

    /**
     * 查询用户信息
     *
     * @param userInfoBO
     * @return
     */
    @Override
    public UserInfoBO getUserInfo(UserInfoBO userInfoBO) {
        UserDO userDO = iUserDao.get(userInfoBO.getUserNo());
        BeanUtils.copyProperties(userInfoBO, userDO);
        return userInfoBO;
    }

    @Override
    public Long add(UserInfoBO user) {
        String salt = RandomStringUtils.randomAlphanumeric(20);
        String password = CryptoUtils.sha256Hash(user.getPassword(), salt);
        String id = IdGenUtils.generateId(MonitorConstants.PUBLIC_SEQUENCE_NAME);
        Long userNo = Long.valueOf(id);
        user.setUserNo(userNo);
        user.setSalt(salt);
        user.setPassword(password);
        user.setCreateTime(LocalDateTime.now());
        user.setCreateUserId(Long.valueOf(SecurityUtils.getLoginUserId()));
        UserDO userDO = BeanUtils.copyPropertiesReturnDest(new UserDO(), user);
        int res = iUserDao.insert(userDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
        return userNo;
    }

    @Override
    public void addUserRole(Long userNo, List<Long> roleIds) {
        if (JudgeUtils.isNull(roleIds)) {
            return;
        }
        roleIds.forEach(id -> {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUserNo(userNo);
            userRoleDO.setRoleId(id);
            iUserRoleDao.insert(userRoleDO);
        });
    }

    @Override
    public void delete(Long userNo) {
        if (userNo == MonitorConstants.SUPER_ADMIN) {
            BusinessException.throwBusinessException(MsgEnum.SUPER_ADMIN_CANNNOT_DELETE);
        }
        int res = iUserDao.delete(userNo);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_DELETE_FAILED);
        }
        //删除用户的角色
        iUserRoleDao.deleteUserRoleByUserNo(userNo);
    }

    @Override
    public void deleteBatch(List<Long> userNos) {
        if (userNos.size() <= 0) {
            return;
        }
        userNos.forEach(id -> {
            delete(id);
        });
    }

    @Override
    public void update(UserInfoBO user) {
        if (JudgeUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            String password = CryptoUtils.sha256Hash(user.getPassword(), user.getSalt());
            user.setPassword(password);
        }
        UserDO userDO = BeanUtils.copyPropertiesReturnDest(new UserDO(), user);
        int res = iUserDao.update(userDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_UPDATE_FAILED);
        }
    }

    @Override
    public void updatePassword(String oldPassword, String newPassword) {
        String id = SecurityUtils.getLoginUserId();
        if (JudgeUtils.isBlank(id)) {
            BusinessException.throwBusinessException(MsgEnum.LOGIN_SESSION_EXPIRE);
        }
        Long userNo = Long.valueOf(id);
        UserDO oldUser = iUserDao.get(userNo);
        String orginPassword = oldUser.getPassword();
        String comparePassword = CryptoUtils.sha256Hash(oldPassword, oldUser.getSalt());
        if (JudgeUtils.notEquals(orginPassword, comparePassword)) {
            BusinessException.throwBusinessException(MsgEnum.ORGIN_PASSWORD_NOT_CORRECT);
        }
        UserDO userDO = new UserDO();
        userDO.setUserNo(userNo);
        userDO.setPassword(CryptoUtils.sha256Hash(newPassword, oldUser.getSalt()));
        int res = iUserDao.update(userDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_UPDATE_FAILED);
        }
    }

    @Override
    public void updateUserRole(Long userNo, List<Long> roleIds) {
        //删除用户拥有的角色
        iUserRoleDao.deleteUserRoleByUserNo(userNo);
        if (roleIds.size() <= 0) {
            return;
        }
        roleIds.forEach(id -> {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUserNo(userNo);
            userRoleDO.setRoleId(id);
            iUserRoleDao.insert(userRoleDO);
        });
    }

    @Override
    public List<UserInfoBO> getUserList() {
        UserDO userDO = new UserDO();
        List<UserDO> userDOList = iUserDao.find(userDO);
        return BeanConvertUtils.convertList(userDOList, UserInfoBO.class);
    }
}
