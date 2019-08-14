/*
 * @ClassName ILogTypeDao
 * @Description 
 * @version 1.0
 * @Date 2019-07-03 11:31:38
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.bo.DictionaryBO;
import com.cmpay.lemon.monitor.entity.DictionaryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDictionaryExtDao extends IDictionaryDao {

    //根据字典id查询字段值
    public List<DictionaryDO> getDicByDicId(DictionaryDO dictionaryDO);
}