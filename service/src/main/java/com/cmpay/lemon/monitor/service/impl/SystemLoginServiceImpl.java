package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.monitor.bo.UserLoginBO;
import com.cmpay.lemon.monitor.dao.IUserExtDao;
import com.cmpay.lemon.monitor.entity.UserDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemLoginService;
import com.cmpay.lemon.monitor.utils.CryptoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统处理服务
 *
 * @author : 曾益
 * @date : 2018/10/31
 */
@Service
@Transactional
public class SystemLoginServiceImpl implements SystemLoginService {
    private static final String ENABLED = "1";
    @Autowired
    private IUserExtDao iUserDao;

    /**
     * 用户登录
     *
     * @param userLoginBO
     * @return
     */
    @Override
    public UserLoginBO login(UserLoginBO userLoginBO) {
        UserDO userDO = iUserDao.getUserByUserName(userLoginBO.getUsername());
        if (JudgeUtils.notEquals(userDO.getStatus().toString(),ENABLED)) {
            BusinessException.throwBusinessException(MsgEnum.USER_DISABLED);
        }
        String inputPassword = CryptoUtils.sha256Hash(userLoginBO.getPassword(), userDO.getSalt());
        if (JudgeUtils.notEquals(inputPassword, userDO.getPassword())) {
            BusinessException.throwBusinessException(MsgEnum.LOGIN_ACCOUNT_OR_PASSWORD_ERROR);
        }
        userLoginBO.setUserNo(userDO.getUserNo());
        userLoginBO.setMobileNo(userDO.getMobile());
        return userLoginBO;
    }
}
