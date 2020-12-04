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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统处理服务
 *
 * @author : 曾益
 * @date : 2018/10/31
 */
@Service
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
          //获取账号登录失败次数
        int fail = userDO.getFailuresNumber();
        System.err.println(userDO.getFailuresNumber());
          //查询账号禁用情况
            if (!userDO.getStatus().toString().equals(ENABLED)) {
                BusinessException.throwBusinessException(MsgEnum.ERROR_FAIL_USER_STATUS);
            }
            //密码校验
            String inputPassword = CryptoUtils.sha256Hash(userLoginBO.getPassword(), userDO.getSalt());
            if (JudgeUtils.notEquals(inputPassword, userDO.getPassword())) {
                // 密码校验失败， 密码失败次数加1
                fail = fail + 1;
                //密码登录失败次数
                if(fail>5){
                    //如果密码失败次数大与5次，则账号禁用,登录失败次数重置
                    userDO.setStatus((byte)0);
                    userDO.setFailuresNumber((byte)0);
                    updateStatus(userDO);
                    // 更新数据库后，页面提示报错
                    BusinessException.throwBusinessException(MsgEnum.ERROR_FAIL_USER_STATUS);
                }else{
                    // 失败次数小于五
                    userDO.setFailuresNumber((byte)fail);
                    updateStatus(userDO);
                }
                BusinessException.throwBusinessException(MsgEnum.LOGIN_ACCOUNT_OR_PASSWORD_ERROR);
            }
            // 密码校验正确，则密码失败次数归0，登录成功
            userDO.setFailuresNumber((byte)0);
            updateStatus(userDO);

            userLoginBO.setUsername(userDO.getUsername());
            userLoginBO.setUserNo(userDO.getUserNo());
            userLoginBO.setMobileNo(userDO.getMobile());
            return userLoginBO;
        }
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BusinessException.class)
    public void updateStatus(UserDO userDO){
        iUserDao.updateStatus(userDO);
    }
}
