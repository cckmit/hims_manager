package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.IdGenUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.MenuBO;
import com.cmpay.lemon.monitor.bo.UserInfoBO;
import com.cmpay.lemon.monitor.bo.UserInfoQueryBO;
import com.cmpay.lemon.monitor.bo.UserPermissionBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dao.IPermiUserDao;
import com.cmpay.lemon.monitor.dao.IUserExtDao;
import com.cmpay.lemon.monitor.dao.IUserRoleExtDao;
import com.cmpay.lemon.monitor.entity.PermiUserDO;
import com.cmpay.lemon.monitor.entity.UserDO;
import com.cmpay.lemon.monitor.entity.UserRoleDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemMenuService;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.CryptoUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    @Autowired
    IPermiUserDao iPermiUserDao;


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
        PageInfo<UserDO> pageInfo = PageUtils.pageQueryWithCount(queryBO.getPageNum(), queryBO.getPageSize(), () -> iUserDao.search(userDO));
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
    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = RuntimeException.class)
    public Long add(UserInfoBO user) {
        String salt = RandomStringUtils.randomAlphanumeric(20);
        String password = CryptoUtils.sha256Hash(user.getPassword(), salt);
        String id = IdGenUtils.generateId(MonitorConstants.PUBLIC_SEQUENCE_NAME);
        Long userNo = Long.valueOf(id);
        user.setUserNo(userNo);
        user.setSalt(salt);
        user.setPassword(password);
        user.setCreateTime(LocalDateTime.now());
        user.setCreateUserNo(Long.valueOf(SecurityUtils.getLoginUserId()));
        UserDO userDO = BeanUtils.copyPropertiesReturnDest(new UserDO(), user);

        //同步老表
        PermiUserDO permiUserDO = new PermiUserDO();
        permiUserDO.setUserId(userDO.getUsername());
        permiUserDO.setUserName(userDO.getFullname());
        permiUserDO.setMobileNum(userDO.getMobile());
        permiUserDO.setEmail(userDO.getEmail());
        permiUserDO.setDeptName(userDO.getDepartment());
        if("总经办".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("001");
        }else if("资金归集项目组".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("002");
        }else if("银行合作研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("003");
        }else if("营销服务研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("004");
        }else if("移动支付研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("005");
        }else if("团体组织交费项目组".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("006");
        }else if("设计项目组".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("007");
        }else if("产品测试部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("008");
        }else if("前端技术研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("009");
        }else if("平台架构部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0010");
        }else if("客户端研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0011");
        }else if("客服中间层项目组".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0012");
        }else if("基础应用研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0013");
        }else if("互联网金融研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0014");
        }else if("公共缴费研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0015");
        }else if("风控大数据研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0016");
        }else if("电商支付研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0017");
        }else if("运维团队".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0018");
        }
        permiUserDO.setIsEnabled(true);
        permiUserDO.setPassword("123456");
        int res;
        res= iPermiUserDao.insert(permiUserDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
        res = iUserDao.insert(userDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
        return userNo;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS,rollbackFor = RuntimeException.class)
    public Long addP(UserInfoBO user) {
        String salt = RandomStringUtils.randomAlphanumeric(20);
        String password = CryptoUtils.sha256Hash(user.getPassword(), salt);
        String id = IdGenUtils.generateId(MonitorConstants.PUBLIC_SEQUENCE_NAME);
        Long userNo = Long.valueOf(id);
        user.setUserNo(userNo);
        user.setSalt(salt);
        user.setPassword(password);
        user.setCreateTime(LocalDateTime.now());
        user.setCreateUserNo(Long.valueOf(SecurityUtils.getLoginUserId()));
        UserDO userDO = BeanUtils.copyPropertiesReturnDest(new UserDO(), user);

        //同步老表
        PermiUserDO permiUserDO = new PermiUserDO();
        permiUserDO.setUserId(userDO.getUsername());
        permiUserDO.setUserName(userDO.getFullname());
        permiUserDO.setMobileNum(userDO.getMobile());
        permiUserDO.setEmail(userDO.getEmail());
        permiUserDO.setDeptName(userDO.getDepartment());
        if("总经办".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("001");
        }else if("资金归集项目组".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("002");
        }else if("银行合作研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("003");
        }else if("营销服务研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("004");
        }else if("移动支付研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("005");
        }else if("团体组织交费项目组".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("006");
        }else if("设计项目组".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("007");
        }else if("产品测试部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("008");
        }else if("前端技术研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("009");
        }else if("平台架构部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0010");
        }else if("客户端研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0011");
        }else if("客服中间层项目组".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0012");
        }else if("基础应用研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0013");
        }else if("互联网金融研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0014");
        }else if("公共缴费研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0015");
        }else if("风控大数据研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0016");
        }else if("电商支付研发部".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0017");
        }else if("运维团队".equals(userDO.getDepartment())){
            permiUserDO.setDeptId("0018");
        }

        permiUserDO.setIsEnabled(true);
        permiUserDO.setPassword(DigestUtils.md5Hex("123456"));
        permiUserDO.setCreateDate(LocalDateTime.now());
        permiUserDO.setUpdateDate(LocalDateTime.now());
        permiUserDO.setIsEnabled(true);
        permiUserDO.setIsLocked(false);
        permiUserDO.setRegisterTime(LocalDateTime.now());
//        if(user.getRoleIds().size()>1){
//            iPermiUserDao.insertUserRole(userDO.getUsername(),"20140123162357100001");
//            iPermiUserDao.insertUserRole(userDO.getUsername(),"20170104120156100443");
//        }else{
//            iPermiUserDao.insertUserRole(userDO.getUsername(),"20140123162357100001");
//        }
        int res;
        res= iPermiUserDao.insert(permiUserDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
        res = iUserDao.insert(userDO);
        if (res != 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
        return userNo;
    }

    @Override
    public void addUserRole(Long userNo, List<Long> roleIds, String username) {
        if (JudgeUtils.isNull(roleIds)) {
            return;
        }
        roleIds.forEach(id -> {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUserNo(userNo);
            if(id == (long)5002 ){
                userRoleDO.setRoleId(id);
                iPermiUserDao.insertUserRole(username,"201401010002");
            }
            if(id == (long)5005){
                userRoleDO.setRoleId(id);
                iPermiUserDao.insertUserRole(username,"20170104120156100443");

            }else{
                userRoleDO.setRoleId(id);
            }
            iUserRoleDao.insert(userRoleDO);
        });
        iPermiUserDao.insertUserRole(username,"20140123162357100001");
    }

    @Override
    public void delete(Long userNo) {
        if (userNo == MonitorConstants.SUPER_ADMIN) {
            BusinessException.throwBusinessException(MsgEnum.SUPER_ADMIN_CANNNOT_DELETE);
        }
        UserDO userDO =iUserDao.get(userNo);
        PermiUserDO permiUserDO = new PermiUserDO();
        permiUserDO.setUserId(userDO.getUsername());
        List<PermiUserDO> permiUserDOS = iPermiUserDao.find(permiUserDO);
        if(permiUserDOS!=null) {
            iPermiUserDao.delete(permiUserDOS.get(0).getSeqId());
            iPermiUserDao.deleteUserRole(permiUserDOS.get(0).getUserId());
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
    public void updateUserRole(Long userNo, List<Long> roleIds,String username) {
        //删除用户拥有的角色
        iUserRoleDao.deleteUserRoleByUserNo(userNo);
        iPermiUserDao.deleteUserRole(username);
        if (roleIds.size() <= 0) {
            return;
        }

        roleIds.forEach(id -> {
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUserNo(userNo);
            if(id == (long)5002 ){
                userRoleDO.setRoleId(id);
                iPermiUserDao.insertUserRole(username,"201401010002");
            }
            if(id == (long)5005){
                userRoleDO.setRoleId(id);
                iPermiUserDao.insertUserRole(username,"20170104120156100443");

            }else{
                userRoleDO.setRoleId(id);
            }
            iUserRoleDao.insert(userRoleDO);
        });
        iPermiUserDao.insertUserRole(username,"20140123162357100001");
    }

    @Override
    public List<UserInfoBO> getUserList() {
        UserDO userDO = new UserDO();
        List<UserDO> userDOList = iUserDao.find(userDO);
        return BeanConvertUtils.convertList(userDOList, UserInfoBO.class);
    }

    @Override
    public String getDepartmentByUser(String loginname) {
        UserDO userDO = new UserDO();
        UserDO userByUserName = iUserDao.getUserByUserName(loginname);
        if(userByUserName==null|| StringUtils.isEmpty( userByUserName.getDepartment())){
            MsgEnum.ERROR_CUSTOM.setMsgInfo(loginname+"操作员信息有误，请及时联系管理员");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        return  userByUserName.getDepartment();
    }

    @Override
    public String getFullname(String loginname) {
        UserDO userDO = new UserDO();
        UserDO userByUserName = iUserDao.getUserByUserName(loginname);
        if(userByUserName==null|| StringUtils.isEmpty( userByUserName.getFullname())){
            MsgEnum.ERROR_CUSTOM.setMsgInfo(loginname+"操作员信息有误，请及时联系管理员");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        return  userByUserName.getFullname();
    }

    //同步老表人员数据信息到新表
    @Override
    public void syncOldTable() {
        PermiUserDO permiUserDO = new PermiUserDO();
        //查找所有旧表人员
        List<PermiUserDO> permiUserDOList = iPermiUserDao.find(permiUserDO);
        //遍历同步
        permiUserDOList.forEach(m->{
            UserInfoBO user = new UserInfoBO();
            //用户账号
            user.setUsername(m.getUserId());
            //查找新表是否已经有该用户数据，已有则跳过
            List<UserDO> userDOList = new LinkedList<>();
            userDOList = iUserDao.find(BeanUtils.copyPropertiesReturnDest(new UserDO(), user));
            if(!userDOList.isEmpty()){
                return;
            }
            //固定初始密码12345678
            user.setPassword("12345678");
            //邮箱
            user.setEmail(m.getEmail());
            //用户名
            user.setFullname(m.getUserName());
            //手机号
            user.setMobile(m.getMobileNum());
            //部门
            user.setDepartment(m.getDeptName());
            //部门
            user.setStatus((byte)1);

            String salt = RandomStringUtils.randomAlphanumeric(20);
            String password = CryptoUtils.sha256Hash(user.getPassword(), salt);
            String id = IdGenUtils.generateId(MonitorConstants.PUBLIC_SEQUENCE_NAME);
            Long userNo = Long.valueOf(id);
            user.setUserNo(userNo);
            user.setSalt(salt);
            user.setPassword(password);
            user.setCreateTime(LocalDateTime.now());
            user.setCreateUserNo(Long.valueOf(SecurityUtils.getLoginUserId()));
            UserDO userDO = BeanUtils.copyPropertiesReturnDest(new UserDO(), user);
            iUserDao.insert(userDO);
            //这是普通员工
            UserRoleDO userRoleDO = new UserRoleDO();
            userRoleDO.setUserNo(userNo);
            userRoleDO.setRoleId((long)4001);
            iUserRoleDao.insert(userRoleDO);
        });
    }

    @Override
    public String getTelbyName(String name) {
        UserDO userDO = new UserDO();
        userDO.setFullname(name);
        List<UserDO> userDOS = iUserDao.find(userDO);
        //查询不为空，则判断是否有电话号码，
        if(JudgeUtils.isNotEmpty(userDOS)) {
            String mobile = userDOS.get(0).getMobile();
            if(StringUtils.isNotBlank(mobile)){
                return mobile;
            }
        }
        return "";
    }


    @Async
    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void updateMobile(String fullName,String mobile) {
        System.err.println(222);
        UserDO userDO = new UserDO();
        userDO.setFullname(fullName);
        List<UserDO> userDOS = iUserDao.find(userDO);
        if(JudgeUtils.isEmpty(userDOS)){
            return;
        }else{
            userDOS.get(0).setMobile(mobile);
            iUserDao.update(userDOS.get(0));
        }
    }
}
