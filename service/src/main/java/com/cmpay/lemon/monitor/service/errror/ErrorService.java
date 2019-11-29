package com.cmpay.lemon.monitor.service.errror;

import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.entity.ErcdmgErrorComditionDO;
import com.cmpay.lemon.monitor.entity.ErrorRecordBeanDO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: tu_yi
 */
public interface ErrorService {

    /**
     * 分页查询
     *
     * @param ercdmgErrorComditionBO
     * @return
     */
    ErcdmgErrorComditionRspBO searchErroeList (ErcdmgErrorComditionBO ercdmgErrorComditionBO);

    /**
     * 新增
     *
     * @param ercdmgErrorComditionBO
     */
    void addError(ErcdmgErrorComditionBO ercdmgErrorComditionBO);
    /**
     * 修改
     *
     * @param ercdmgErrorComditionBO
     */
    void updateError(ErcdmgErrorComditionBO ercdmgErrorComditionBO);
    /**
     * 删除
     *
     * @param id 错误码id
     */
    void deleteError(String id);
    /**
     * 批量导入
     *
     * @param file
     */
    void doBatchImport(MultipartFile file);
    // 退回
    void goback(String ids,String status);
    //新增检查
    ErcdmgErrorComditionBO checkErrorCodeExist(ErcdmgErrorComditionBO ercdmgErrorComditionBO);
    //记录错误码导入记录
    void insertErrorRecordBean(ErrorRecordBeanDO errorRecordBean);
    //查看SIT 新增
    String selectSitMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO);
    //查看uat 新增
    String selectUatMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO);
    //新增本地
    void addErcdmgError(ErcdmgErrorComditionBO ercdmgError);
    //修改检查
    void checkErrorUP(ErcdmgErrorComditionBO ercdmgErrorComditionBO);
    //修改sit
    void updateSitMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO);
    //修改uat
    void updateUatMsg(ErcdmgErrorComditionBO ercdmgErrorComditionBO);
    //修改本地
    void updateErcdmgError(ErcdmgErrorComditionBO ercdmgError);
    //SC检查
    ErcdmgErrorComditionBO checkErrorDelete(String id);
    //删除sit
    void deleteSMsg(ErcdmgErrorComditionBO errorSingle);
    //删除uat
    void deleteUatMsg(ErcdmgErrorComditionBO errorSingle);
    // 删除考核数据库
    void delErcdmgError(String id,ErcdmgErrorComditionBO errorSingle);

    /**
     * 调转转交产品经理
     *
     * @param ids 错误码id字符串
     * @return
     */
    ErcdmgErrorComditionRspBO forwardpord (String ids);
}
