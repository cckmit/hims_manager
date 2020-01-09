/*
 * @ClassName ILogTypeDao
 * @Description 
 * @version 1.0
 * @Date 2019-07-03 11:31:38
 */
package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.monitor.entity.DictionaryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IDictionaryExtDao extends IDictionaryDao {

    //根据字典id查询字段值
    public List<DictionaryDO> getDicByDicId(DictionaryDO dictionaryDO);

    //查询基地人员信息
    public List<DictionaryDO> getJdInfo(DictionaryDO dictionaryDO);

    String findFieldName(Map<String, String> map);

    String findDepartId(String departName);

    List<DictionaryDO> findUploadPeriod(String reqPeriod);

    List<DictionaryDO> findProManager();
    //查询研发系统所有人员名单
    List<DictionaryDO> findPeople();
    /**
     * 查询错误码模块字典项
     */
    public List<DictionaryDO> findPordmod();

    /**
     *查询错误码渠道字典项
     */

    public List<DictionaryDO> findDictionary();


    public int updateWorkloadLockStatus(String workloadLockStatus);
}