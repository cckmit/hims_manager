package com.cmpay.lemon.monitor.service.errror;

import com.cmpay.lemon.monitor.bo.*;
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
}
