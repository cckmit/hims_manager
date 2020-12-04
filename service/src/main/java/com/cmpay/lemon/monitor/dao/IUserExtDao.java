package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: zhou_xiong
 */
@Mapper
public interface IUserExtDao extends IUserDao {
    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    UserDO getUserByUserName(String username);
    UserDO getUserByEmail(String email);


    UserDO getUserByMobile(String username);
    List<UserDO> search(UserDO entity);
    List<UserDO> getUserByDept(String dept);


    UserDO getUserByUserFullName(String loginname);
    void updateStatus(UserDO entity);
}
