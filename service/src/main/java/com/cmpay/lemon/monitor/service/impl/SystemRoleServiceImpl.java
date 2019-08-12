package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.IdGenUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.PageQueryBO;
import com.cmpay.lemon.monitor.bo.RoleBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dao.IMenuExtDao;
import com.cmpay.lemon.monitor.dao.IRoleExtDao;
import com.cmpay.lemon.monitor.dao.IRoleMenuExtDao;
import com.cmpay.lemon.monitor.dao.IUserRoleExtDao;
import com.cmpay.lemon.monitor.entity.RoleDO;
import com.cmpay.lemon.monitor.entity.RoleMenuDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemRoleService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.PageConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 描述
 *
 * @author : 曾益
 * @date : 2018/11/12
 */
@Service
@Transactional
public class SystemRoleServiceImpl implements SystemRoleService {
    @Autowired
    private IRoleExtDao iRoleDao;
    @Autowired
    private IRoleMenuExtDao iRoleMenuDao;
    @Autowired
    private IMenuExtDao iMenuDao;
    @Autowired
    private IUserRoleExtDao iUserRoleDao;

    /**
     * 查询用户角色
     *
     * @param userNo
     * @return
     */
    @Override
    public List<RoleBO> getRoles(Long userNo) {
        List<RoleDO> roleDOS = null;
        //超级管理员查询所有角色
        if (userNo == MonitorConstants.SUPER_ADMIN) {
            roleDOS = iRoleDao.getAllRoles();
        } else {
            roleDOS = iRoleDao.getRolesByUserNo(userNo);
        }
        List<RoleBO> roleBOS = BeanConvertUtils.convertList(roleDOS, RoleBO.class);
        return Optional.ofNullable(roleBOS).orElse(Collections.emptyList());
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @Override
    public List<RoleBO> getAllRoles() {
        List<RoleDO> roleDOS = iRoleDao.getAllRoles();
        List<RoleBO> roleBOS = BeanConvertUtils.convertList(roleDOS, RoleBO.class);
        return Optional.ofNullable(roleBOS).orElse(Collections.emptyList());
    }

    @Override
    public RoleBO getRole(Long roleId) {
        RoleDO roleDO = iRoleDao.get(roleId);
        BeanUtils.copyProperties(roleDO, roleDO);
        RoleBO roleBO = BeanUtils.copyPropertiesReturnDest(new RoleBO(), roleDO);
        return roleBO;
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        List<Long> menuIds = null;
        if (roleId == MonitorConstants.SUPER_ADMIN) {
            menuIds = iMenuDao.queryAllButtonMenuId();
        } else {
            menuIds = iRoleMenuDao.queryButtonMenuIdsByRoleId(roleId);
        }
        return Optional.ofNullable(menuIds).orElse(Collections.emptyList());
    }

    @Override
    public List<Long> getRolesByUserNo(Long userNo) {
        List<Long> roleIds = iUserRoleDao.queryRolesByUserNo(userNo);
        return Optional.ofNullable(roleIds).orElse(Collections.emptyList());
    }

    @Override
    public PageInfo<RoleBO> getRolesPage(RoleBO roleBO, PageQueryBO pageQueryBO) {
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(roleDO, roleBO);
        //role name为空时，设置为null
        if (JudgeUtils.isBlank(roleDO.getRoleName())) {
            roleDO.setRoleName(null);
        }
        PageInfo<RoleDO> pageInfo = PageUtils.pageQueryWithCount(pageQueryBO.getPageNum(), pageQueryBO.getPageSize(), () -> iRoleDao.find(roleDO));
        List<RoleDO> roleDOS = pageInfo.getList();
        List<RoleBO> roleBOList = BeanConvertUtils.convertList(roleDOS, RoleBO.class);
        PageInfo resPageInfo = new PageInfo(roleBOList);
        PageConvertUtils.convert(pageInfo, resPageInfo);
        return resPageInfo;
    }

    @Override
    public Long add(RoleBO roleBO) {
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(roleDO, roleBO);
        String seqNo = IdGenUtils.generateId(MonitorConstants.PUBLIC_SEQUENCE_NAME);
        Long seq = Long.valueOf(seqNo);
        roleDO.setRoleId(Long.valueOf(seqNo));
        roleDO.setCreateUserId(Long.valueOf(SecurityUtils.getLoginUserId()));
        roleDO.setCreateTime(LocalDateTime.now());
        int res = iRoleDao.insert(roleDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
        return seq;
    }

    @Override
    public void addRoleMenu(Long roleId, List<Long> menuIds) {
        for (Long menuId : menuIds) {
            if (menuId < 0) {
                continue;
            }
            RoleMenuDO roleMenuDO = new RoleMenuDO();
            roleMenuDO.setMenuId(menuId);
            roleMenuDO.setRoleId(roleId);
            iRoleMenuDao.insert(roleMenuDO);
        }
    }

    @Override
    public void update(RoleBO roleBO) {
        RoleDO roleDO = new RoleDO();
        BeanUtils.copyProperties(roleDO, roleBO);
        int res = iRoleDao.update(roleDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_UPDATE_FAILED);
        }
    }

    @Override
    public void updateRoleMenu(Long roleId, List<Long> menuIds) {
        //删除关联权限
        iRoleMenuDao.deleteRoleMenu(roleId);
        if (menuIds.size() <= 0) {
            return;
        }
        addRoleMenu(roleId, menuIds);
    }

    @Override
    public void delete(Long roleId) {
        int res = iRoleDao.delete(roleId);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_DELETE_FAILED);
        }
    }

    @Override
    public void deleteBatch(List<Long> roleIds) {
        roleIds.stream().forEach(id -> {
            iRoleDao.delete(id);
            iRoleMenuDao.deleteRoleMenu(id);
        });
    }
}
