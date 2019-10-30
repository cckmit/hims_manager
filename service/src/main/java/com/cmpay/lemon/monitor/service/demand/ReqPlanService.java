package com.cmpay.lemon.monitor.service.demand;

import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.bo.ProjectStartBO;
import com.cmpay.lemon.monitor.entity.DemandDO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author: tu_yi
 */
public interface ReqPlanService {
    /**
     * 通过id查找记录
     *
     * @param req_inner_seq
     * @return
     */
    DemandBO findById(String req_inner_seq);

    /**
     * 分页查询
     *
     * @param demandBO
     * @return
     */
    DemandRspBO findDemand(DemandBO demandBO);

    /**
     * 新增
     *
     * @param demandBO
     */
    void add(DemandBO demandBO);

    /**
     * 删除
     *
     * @param req_inner_seq
     */
    void delete(String req_inner_seq);

    /**
     * 修改
     *
     * @param demandBO
     */
    void update(DemandBO demandBO);

    /**
     * 查找所有
     *
     * @return
     */
    List<DemandBO> findAll();

    /**
     * 根据内部编号查询项目启动信息
     * @param req_inner_seq
     * @return
     */
    ProjectStartBO goProjectStart(String req_inner_seq);

    /**
     * 根据编号获取邮箱
     */
    Map<String,String> getMailbox(String req_inner_seq);
    /**
     * 项目启动
     *
     * @param
     */
    void projectStart(ProjectStartBO projectStartBO, HttpServletRequest request);

    /**
     * 发送邮件
     */
    String sendMail(String sendTo, String copyTo, String content, String subject, Vector<File> attachFiles);
    /**
     * 存量变更
     */
    void changeReq(String req_impl_mon);
    /**
     * 需求重新启动
     */
    void rebooting(List<String> ids);
    /**
     * 需求文档上传
     */
    void uploadProjrctFile(ProjectStartBO reqDTO, MultipartFile[] files,HttpServletRequest request);
    /**
     * 获取当前需求阶段
     */
    String getReqPeriod(String preCurPeriod);

    void getReqPlan(HttpServletResponse response, DemandBO demandBO);
    /**
     * 批量导入
     *
     * @param file
     */
    void doBatchImport(MultipartFile file);
}
