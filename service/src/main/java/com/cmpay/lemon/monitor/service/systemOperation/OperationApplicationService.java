package com.cmpay.lemon.monitor.service.systemOperation;


import com.cmpay.lemon.monitor.bo.OperationApplicationBO;
import com.cmpay.lemon.monitor.bo.OperationApplicationRspBO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 投产操作明细导出
     * @param request
     * @param response
     * @param str
     * @throws Exception
     */
    void doOperationDownload(HttpServletRequest request, HttpServletResponse response, String str)throws Exception;
    /**
     * 投产操作明细导出(全部)
     * @param request
     * @param response
     * @throws Exception
     */
    void doAllOperationDownload(HttpServletRequest request, HttpServletResponse response, OperationApplicationBO operationApplicationBO)throws Exception;
   // 操作状态修改
    void updateAllOperationApplication(HttpServletRequest request, HttpServletResponse response, String taskIdStr);
   //修改
    void systemOperationUpdate(List<MultipartFile> files, OperationApplicationBO operationApplicationBO, HttpServletRequest request);
    // 下载附件
    void pkgDownload(HttpServletRequest request, HttpServletResponse response, String proNumber);

    OperationApplicationBO getSystemOperation(String operNumber);
}
