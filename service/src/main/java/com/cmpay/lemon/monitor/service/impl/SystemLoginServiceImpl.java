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
        UserDO userDO =null;
        userDO = iUserDao.getUserByUserName(userLoginBO.getUsername());
        //账号查找不存在
          if(userDO==null){
              try {
                  //使用手机号查找
                  userDO = iUserDao.getUserByMobile(userLoginBO.getUsername());
              }catch (Exception e){
                  BusinessException.throwBusinessException(MsgEnum.LOGIN_ACCOUNT_OR_PASSWORD_ERROR);
              }
              //使用手机号查找也不存在
              if(userDO==null) {
                  BusinessException.throwBusinessException(MsgEnum.LOGIN_ACCOUNT_OR_PASSWORD_ERROR);
              }
          }
            if (!userDO.getStatus().toString().equals(ENABLED)) {
                BusinessException.throwBusinessException(MsgEnum.USER_DISABLED);
            }
            String inputPassword = CryptoUtils.sha256Hash(userLoginBO.getPassword(), userDO.getSalt());
            if (JudgeUtils.notEquals(inputPassword, userDO.getPassword())) {
                BusinessException.throwBusinessException(MsgEnum.LOGIN_ACCOUNT_OR_PASSWORD_ERROR);
            }
            userLoginBO.setUsername(userDO.getUsername());
            userLoginBO.setUserNo(userDO.getUserNo());
            userLoginBO.setMobileNo(userDO.getMobile());
            return userLoginBO;
        }
}
