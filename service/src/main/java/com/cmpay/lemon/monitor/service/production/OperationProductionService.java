package com.cmpay.lemon.monitor.service.production;


import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.entity.ProductionDO;
import com.cmpay.lemon.monitor.entity.ProductionPicDO;
import com.cmpay.lemon.monitor.entity.ScheduleDO;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowBean;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowConditionDO;
import com.cmpay.lemon.monitor.entity.sendemail.MailFlowDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Vector;

/**
 * @author: zhou_xiong
 */
public interface OperationProductionService {

    /**
     * 分页查询
     *
     * @param productionBO
     * @return
     */
    ProductionRspBO find(ProductionBO productionBO);
    ScheduleRspBO find1(ScheduleBO scheduleBO);
    void exportExcel(HttpServletRequest request, HttpServletResponse response, ProductionBO productionBO);

    void updateAllProduction(HttpServletRequest request, HttpServletResponse response, String str);
    void sendGoExport(HttpServletRequest request, HttpServletResponse response, String str);
    void sendGoExportResult(HttpServletRequest request, HttpServletResponse response, String str);
    String proPkgCheck(HttpServletRequest request, HttpServletResponse response, String str);
    void doProductionDetailDownload(HttpServletRequest request, HttpServletResponse response, String str)throws Exception;

    /** 根据人员姓名查询各部门经理邮箱*/
    String findManagerMailByUserName(List<String> userNames);

    void addMailFlow(MailFlowBean bnb);

    // 导出非投产日正常投产申请表EXCEL
    File exportUnusualExcel(List<ProductionBO> list);

    // 导出紧急更新申请表EXCEL
    File exportUrgentExcel(List<ProductionBO> list);
    // 查询一条投产记录的详情
    ProductionBO searchProdutionDetail(String proNumber);

    // 投产审核
    void approval(String proNumber);
    // 投产详情修改
    void updateProductionBean(ProductionBO productionBO);

    void updateAllProduction(ProductionBO bean);

    void addScheduleBean(ScheduleDO scheduleBean);

    void addProduction(ProductionBO bean);
    /** 保存图片基本信息*/
    void addProductionPicBean(ProductionPicDO productionPicDO);

    List<MailGroupBO> searchMailGroupList(MailGroupBO mailGroupBO);

    Vector<File> setVectorFile(MultipartFile file, Vector<File> files, ProductionBO bean);

    MsgEnum productionInput(MultipartFile file, Boolean isApproveProduct, ProductionBO bean);
    /**
     * 投产包上传
     *
     * @param file
     */
    void doBatchImport(MultipartFile file,String reqNumber);
    //查询投产问题
    List<ProblemBO> findProblemInfo(String proNumber);
    //更新投产问题
    void updateProblem(ProblemBO proBean);
    //删除投产问题
    void deleteProblemInfo(String proNumber1);
    //新增投产问题
    void insertProblemInfo(ProblemBO proBean);

    void questionInput(QuestionInputReqBO questionInputReqBO);
    //投产包下载
    void pkgDownload(HttpServletRequest request, HttpServletResponse response, String str);

    //查询邮箱密码
    MailFlowDO searchUserEmail(MailFlowConditionDO mailFlowConditionDO);

    //查询部门经理
    public ProductionDO findDeptManager(String deptName);

    void reissueMail(MultipartFile file, ProductionBO bean);
}
