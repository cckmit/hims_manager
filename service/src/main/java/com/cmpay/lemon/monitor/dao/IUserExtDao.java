package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

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
}
