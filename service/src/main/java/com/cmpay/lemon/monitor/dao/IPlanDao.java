package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.DemandDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author: ty
 */
@Mapper
public interface IPlanDao extends IDemandDao {
    //根据内部编号查询邮箱
    DemandDO searchUserEmailByName(String req_inner_seq);
    //根据名字查询邮箱
    DemandDO searchUserEmail(String name);
    //根据内部编号查询其他邮箱
    DemandDO searchOtherUserEmail(String req_inner_seq);
    //根据姓名查询基地邮件
    DemandDO findBaseChargeEmailByName(String req_inner_seq);

    //根据条件查询部门主管邮箱
    DemandDO findDevpEmail(@Param("devpCoorDepts") String[] devpCoorDepts , @Param("reqInnerSeq") String req_inner_seq);
    //根据条件查询部门主管邮箱
    DemandDO searchUserLEmail(@Param("devpCoorDepts") String[] devpCoorDepts);
    //根据功能点文档更新工作量
    void updateReqWorkLoad(DemandDO bean);
}
