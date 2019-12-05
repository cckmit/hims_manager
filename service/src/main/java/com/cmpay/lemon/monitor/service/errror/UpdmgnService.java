package com.cmpay.lemon.monitor.service.errror;

import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionRspBO;
import com.cmpay.lemon.monitor.entity.ErrorRecordBeanDO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * @author: tu_yi
 */
public interface UpdmgnService {

    /**
     * 分页查询
     *
     * @param ercdmgErrorComditionBO
     * @return
     */
    ErcdmgErrorComditionRspBO searchErroeList(ErcdmgErrorComditionBO ercdmgErrorComditionBO);

    //查看
    ErcdmgErrorComditionRspBO details (String ids);


    // 提交投产
    ErcdmgErrorComditionRspBO updmgnProduction (String ids);

    //提交投产
    void submitPorduct(String id,  String emailContent);
    void updatePro();
}
