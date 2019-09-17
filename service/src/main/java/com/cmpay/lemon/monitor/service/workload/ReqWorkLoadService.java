package com.cmpay.lemon.monitor.service.workload;

import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.bo.ProjectStartBO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author: tu_yi
 */
public interface ReqWorkLoadService {
    /**
     * 分页查询
     *
     * @param demandBO
     * @return
     */
    DemandRspBO findDemand(DemandBO demandBO);
    /**
     * 工作量变更
     */
    void changeReq(String req_impl_mon);
}
