package com.cmpay.lemon.monitor.dao;

import com.cmpay.lemon.framework.dao.BaseDao;
import com.cmpay.lemon.monitor.entity.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @author: ty
 */
@Mapper
public interface IErcdmgErorDao extends BaseDao<ErcdmgErrorComditionDO, String> {
    /**
     * 查询分页列表
     */
    List<ErcdmgErrorComditionDO> findErcdmgErrorList(ErcdmgErrorComditionDO vo);

    /**
     * 查询sit测试数据
     * @param errorCd
     * @return
     */
    String selectPubtmsg(String errorCd);
    /**
     * 查询sit测试数据
     * @param errorCd
     * @return
     */
    public String selectPubttms(@Param("errorCd") String errorCd, @Param("buscnl")  String buscnl);

    void insertPubtmsg(ErcdmgErrorComditionDO ercdmgError);

    void insertPubttms(ErcdmgErrorComditionDO ercdmgError);

    public ErcdmgErrorComditionDO checkErrorCodeExist(@Param("errorCd") String errorCd, @Param("buscnl")  String buscnl);
    void updatePubtmsg(ErcdmgErrorComditionDO ercdmgError);

    void updatePubttms(ErcdmgErrorComditionDO ercdmgError);

    void deletePubtmsg(String errorCd);
    void deletePubttms(@Param("errorCd") String errorCd,@Param("buscnl")String buscnl);
    void deleteError(String errorCd);
    public String selectUpdateErrorCd(String errorCd);
     void insertForUpload(ErcdmgErrorComditionDO ercdmgError);

    ErcdmgErrorComditionDO selectErrorSingle(String errorCd);

    /**
     *
     */
    void insertErrorRecordBean(ErrorRecordBeanDO errorRecordBeanDO);

    /**
     * 删除备份错误码
     * @param ercdmgError
     */
    void insertErrorlg(ErcdmgErrorComditionDO ercdmgError);

    public List<ErcdmgErrorComditionDO> selectErcdmgByErrorList(String[]errorCds);

    /**
     * 根据错误码 查询技术人员跟邮箱
     * @param errorCdsArr
     * @return
     */
    public List<ErcdmgPordUserDO> selectProdUserbyErrorCds(String[]errorCdsArr);

    /**
     * 根据错误码 查询产品经理跟邮箱
     * @param errorCdsArr
     * @return
     */
    public List<ErcdmgPordUserDO> selectTechUserbyErrorCds(String[]errorCdsArr);

    /**
     * 回退错误码状态
     * @param
     */
    void updateErrorCurtState(@Param("curtState") String statas ,@Param("errorCdsArr") String[] id);

    /**
     * 获取错误码产品接收人信息
     * @param errorList
     * @return
     */
    List<ErcdmgPordUserDO> selectPordUser(List<ErcdmgErrorComditionDO>errorList);

    /**
     * 根据姓名查询用户信息
     * @param username
     * @return
     */
    TPermiUser findByUsername(String username);
}
