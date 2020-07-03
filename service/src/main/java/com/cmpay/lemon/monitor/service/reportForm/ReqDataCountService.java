package com.cmpay.lemon.monitor.service.reportForm;


import com.cmpay.lemon.monitor.bo.*;


import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public interface ReqDataCountService {
    /**
     * 本月需求完成情况：进行中、已投产需求数
     */
    Map getProg(String report_start_mon ,String report_end_mon);

    /**
     * 本月需求进度情况：需求阶段、开发阶段、测试阶段、预投产阶段、投产上线阶段
     */
    Map getProgDetl(String report_start_mon ,String report_end_mon);

    /**
     * 按部门统计需求实施异常图表
     */
    Map getAbnoByDept(String report_start_mon ,String report_end_mon);

    /**
     * 按产品线统计需求实施异常图表
     */
    Map getAbnoByLine(String report_start_mon ,String report_end_mon);

    /**
     * 各部门需求完成情况：进行中、已投产需求数（饼图）
     */
    Map getProgByDept(String report_start_mon ,String report_end_mon);

    /**
     * 各部门需求进度情况：需求阶段、开发阶段、测试阶段、预投产阶段、投产上线阶段（饼图）
     */
    Map getProgDetlByDept(String report_start_mon ,String report_end_mon);

    //需求实施总体统计
    List<ReqDataCountBO> getImpl(String req_impl_mon);

    //各部门需求实施情况
    List<ReqDataCountBO> getImplByDept(String req_impl_mon);

    //需求完成总体统计
    List<ReqDataCountBO> getComp(String req_impl_mon);

    //需求完成情况统计
    List<ReqDataCountBO> getCompByDept(String req_impl_mon);

    //需求类型统计
    List<ReqDataCountBO> getReqSts(String req_impl_mon);

    //按基地归属部门统计报表
    List<ReqDataCountBO> getStageByJd(String req_impl_mon);
    //  查询录入不及时报表
    List<ScheduleBO> getProduction(String reqImplMon);
    // 需求文档上传情况报表
    List<DemandBO> getReportForm6(String reqImplMon,String devpLeadDept,String productMng,String firstLevelOrganization);

    // 部门员工工时报表
    List<WorkingHoursBO> getReportForm7(String devpLeadDept,String date,String date1,String date2);
    List<WorkingHoursBO> getReportForm7B(String devpLeadDept,String date,String date1,String date2);
    // 部门工时报表
    List<String> getReportForm8(String date);
    List<WorkingHoursBO> findEpicKeyHours(String epic);
    DemandQualityBO findDemandQuality(String epic);

    /**
     * 获取需求花费工时
     * @param epic
     * @return
     */
    DemandHoursRspBO getDemandHours(String epic);

    DemandHoursRspBO getWorkLoad(String epic);

    /**
     * 根据epic获取缺陷类型数
     * @param epic
     * @return
     */
    DemandHoursRspBO getFlawNumber(String epic);

    /**
     * 根据epic获取评审问题类型数
     * @param epic
     * @return
     */
    DemandHoursRspBO getReviewNumber(String epic);

    // 部门员工工时报表
    List<WorkingHoursBO> findList(String displayName ,String date,String date1,String date2);

    //查询本月需求详情
    List<ReqDataCountBO> selectDetl(ReqMngBO vo);
    // 查询总条数
    Integer findNumberByCondition(ReqMngBO vo) throws  Exception;

    //按中心经理查询
    Map selectByCenter(ReqMngBO vo);

    //按产品查询
    Map selectByProduct(ReqMngBO vo);

    //按测试查询
    Map selectByTest(ReqMngBO vo);

    //按开发查询
    Map selectByEng(ReqMngBO vo);

    void downloadDemandTypeStatistics(String month, HttpServletResponse response);
    //录入不及时报表下载
    void downloadProductionTypeStatistics(String month, HttpServletResponse response);
    //需求文档上传情况报表下载
    void downloadDemandUploadDocumentBO(String month,String devpLeadDept,String productMng, String firstLevelOrganization, HttpServletResponse response);


    void downloadDemandImplementationReport(String month, HttpServletResponse response);

    void downloadDemandCompletionReport(String month, HttpServletResponse response);
    void downloadDemandCompletionReport2(String month, HttpServletResponse response);

    void downloadBaseOwnershipDepartmentStatistics(String month, HttpServletResponse response);
    DemandRspBO findDemand(DemandBO demandBO);

    List<ProductionBO> getProductionVerificationIsNotTimely(int dayNumber);
}
