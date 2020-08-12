/*
 * @ClassName IOrganizationStructureDao
 * @Description 
 * @version 1.0
 * @Date 2020-08-12 11:49:00
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.OrganizationStructureDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IOrganizationStructureDao extends BaseDao<OrganizationStructureDO, Integer> {
}