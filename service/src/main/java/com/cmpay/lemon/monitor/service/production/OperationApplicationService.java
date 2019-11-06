package com.cmpay.lemon.monitor.service.production;

import com.cmpay.lemon.monitor.bo.OperationApplicationBO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OperationApplicationService {
    void systemOperationEntry(List<MultipartFile> files, OperationApplicationBO operationApplicationBO, HttpServletRequest request);

    void addOperationalApplication(OperationApplicationBO bean);
}
