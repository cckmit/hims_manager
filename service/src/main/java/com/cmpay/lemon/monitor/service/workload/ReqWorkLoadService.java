package com.cmpay.lemon.monitor.service.workload;

import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.bo.ProjectStartBO;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    /**
     * 批量导入
     *
     * @param file
     */
    void doBatchImport(MultipartFile file);

    /**
     * 检查配合部门工作量占比
     * @param totWork 总工作量
     * @param deptInfo 配合部门
     * @param demand 需求信息
     * @return
     */
    Map<String,String> checkDeptRate(int totWork,String deptInfo,DemandBO demand);

    /**
     * 工作量文档下载
     * @param response
     * @param demandBO 选择条件
     * @param type 文件类型
     * @param file_nm 文件名
     */
    void exportExcel(HttpServletRequest request, HttpServletResponse response, DemandBO demandBO, String type, String file_nm);
    void updateReqWorkLoad(DemandBO bean);
    DemandBO getWorkLoad(DemandBO demand);
}
