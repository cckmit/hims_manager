package com.cmpay.lemon.monitor.service.systemOperation;


import com.cmpay.lemon.monitor.bo.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: zhou_xiong
 */
public interface OperationApplicationService {

    /**
     * 分页查询
     *
     * @param operationApplicationBO
     * @return
     */
    OperationApplicationRspBO find(OperationApplicationBO operationApplicationBO);

    void systemOperationEntry(List<MultipartFile> files, OperationApplicationBO operationApplicationBO, HttpServletRequest request);

    void addOperationalApplication(OperationApplicationBO bean);

}
