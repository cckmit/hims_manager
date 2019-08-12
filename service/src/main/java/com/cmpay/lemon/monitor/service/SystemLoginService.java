package com.cmpay.lemon.monitor.service;


import com.cmpay.lemon.monitor.bo.UserLoginBO;

/**
 * 系统登录接口接口
 *
 * @author : 曾益
 * @date : 2018/10/31
 */
public interface SystemLoginService {
    /**
     * 用户登录
     *
     * @param userLoginBO
     * @return
     */
    UserLoginBO login(UserLoginBO userLoginBO);
}
