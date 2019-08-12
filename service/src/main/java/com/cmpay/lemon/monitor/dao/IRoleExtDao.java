package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.RoleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: zhou_xiong
 */
@Mapper
public interface IRoleExtDao extends IRoleDao {
    /**
     * 根据用户号查找用户归属角色
     *
     * @param userNo
     * @return
     */
    List<RoleDO> getRolesByUserNo(Long userNo);

    /**
     * 查询所有角色
     *
     * @return
     */
    List<RoleDO> getAllRoles();
}
