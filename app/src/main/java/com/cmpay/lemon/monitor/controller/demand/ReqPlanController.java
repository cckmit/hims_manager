package com.cmpay.lemon.monitor.controller.demand;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DictionaryBO;
import com.cmpay.lemon.monitor.bo.ProjectStartBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.dic.DictionaryService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import com.cmpay.lemon.monitor.utils.SvnConstant;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Style;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;
import static com.cmpay.lemon.monitor.utils.FileUtils.doWrite;

/**
 * @author: zhou_xiong
 * 需求计划
 */
@RestController
@RequestMapping(value = MonitorConstants.REQPLAN_PATH)
public class ReqPlanController {

    @Autowired
    private ReqPlanService reqPlanService;
    @Autowired
    private DictionaryService dictionaryService;

    /**
     * 分页需求列表
     *
     * @param reqDTO
     * @return
     */
    @RequestMapping("/list")
    public GenericRspDTO<DemandRspDTO> getUserInfoPage(@RequestBody DemandReqDTO reqDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        String time= DateUtil.date2String(new Date(), "yyyy-MM-dd");
        if(demandBO.getReq_impl_mon()==null||"".equals(demandBO.getReq_impl_mon())){
            demandBO.setReq_impl_mon(month);
        }
        PageInfo<DemandBO> pageInfo = reqPlanService.findDemand(demandBO);
        List<DemandBO> demandBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandBO.class);

        for (int i =0; i < demandBOList.size();i++){
            String reqAbnorType=demandBOList.get(i).getReq_abnor_type();
            String reqAbnorTypeAll = "";
            DemandBO demand = reqPlanService.findById(demandBOList.get(i).getReq_inner_seq());

            //当需求定稿时间、uat更新时间、测试完成时间、需求当前阶段、需求状态都不为空的时候，执行进度实时显示逻辑。
            if (StringUtils.isNotBlank(demand.getPrd_finsh_tm()) && StringUtils.isNotBlank(demand.getUat_update_tm())
                    && StringUtils.isNotBlank(demand.getTest_finsh_tm()) && StringUtils.isNotBlank(demand.getPre_cur_period())
                    && StringUtils.isNotBlank(demand.getReq_sts())) {
                //当前时间大于预计时间，并且所处阶段小于30,并且需求状态不为暂停或取消（30，40）,则该需求进度异常
                if (time.compareTo(demand.getPrd_finsh_tm()) > 0 && Integer.parseInt(demand.getPre_cur_period()) < 30
                        && "30".compareTo(demand.getReq_sts()) != 0 && "40".compareTo(demand.getReq_sts()) != 0) {
                    reqAbnorTypeAll += "需求进度滞后,";
                }
                if (time.compareTo(demand.getUat_update_tm()) > 0 && Integer.parseInt(demand.getPre_cur_period()) >= 30
                        && Integer.parseInt(demand.getPre_cur_period()) < 120 && "30".compareTo(demand.getReq_sts()) != 0
                        && "40".compareTo(demand.getReq_sts()) != 0) {
                    reqAbnorTypeAll += "开发进度滞后,";
                }
                if (time.compareTo(demand.getTest_finsh_tm()) > 0 && Integer.parseInt(demand.getPre_cur_period()) >= 120
                        && Integer.parseInt(demand.getPre_cur_period()) < 140 && "30".compareTo(demand.getReq_sts()) != 0
                        && "40".compareTo(demand.getReq_sts()) != 0) {
                    reqAbnorTypeAll += "测试进度滞后";
                }
                if (StringUtils.isBlank(reqAbnorTypeAll)) {
                    reqAbnorTypeAll += "正常";
                }
            } else if(reqAbnorType.indexOf("01") != -1){
                demandBOList.get(i).setReq_abnor_type("正常");
                continue;
            }else{
                if (reqAbnorType.indexOf("03") != -1) {
                    reqAbnorTypeAll += "需求进度滞后,";
                }
                if (reqAbnorType.indexOf("04") != -1) {
                    reqAbnorTypeAll += "开发进度滞后,";
                }
                if (reqAbnorType.indexOf("05") != -1) {
                    reqAbnorTypeAll += "测试进度滞后";
                }
            }

            if (reqAbnorTypeAll.length() >= 1 && ',' == reqAbnorTypeAll.charAt(reqAbnorTypeAll.length()-1)){
                reqAbnorTypeAll = reqAbnorTypeAll.substring(0,reqAbnorTypeAll.length()-1);
                demandBOList.get(i).setReq_abnor_type(reqAbnorTypeAll);
            }else{
                demandBOList.get(i).setReq_abnor_type(reqAbnorTypeAll);
            }
        }

        DemandRspDTO rspDTO = new DemandRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(demandBOList, DemandDTO.class));
        rspDTO.setPageNum(pageInfo.getPageNum());
        rspDTO.setPages(pageInfo.getPages());
        rspDTO.setTotal(pageInfo.getTotal());
        rspDTO.setPageSize(pageInfo.getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 查询需求信息
     *
     * @return
     */
    @RequestMapping("/info/{id}")
    public GenericRspDTO<DemandDTO> getUserInfoById(@PathVariable("req_inner_seq") String req_inner_seq, GenericDTO<NoBody> req) {
        DemandBO demandBO = reqPlanService.findById(req_inner_seq);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, BeanUtils.copyPropertiesReturnDest(new DemandDTO(), demandBO));
    }

    /**
     * 查询所有需求信息
     */
    @RequestMapping("/all")
    public GenericRspDTO<DemandRspDTO> findAll(GenericDTO reqDTO) {
        DemandRspDTO rspDTO = new DemandRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(reqPlanService.findAll(), DemandDTO.class));
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 添加需求信息
     *
     * @param
     * @return
     */
    @RequestMapping("/save")
    public GenericRspDTO add(@RequestBody DemandDTO demandDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDTO);
        reqPlanService.add(demandBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 删除需求信息
     *
     * @param
     * @return
     */
    @RequestMapping("/delete/{id}")
    public GenericRspDTO delete(@PathVariable("req_inner_seq") String req_inner_seq, GenericDTO<NoBody> req) {
        reqPlanService.delete(req_inner_seq);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 更新需求信息
     *
     * @param
     * @return
     */
    @RequestMapping("/update")
    public GenericRspDTO update(@RequestBody DemandDTO demandDTO) {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDTO);
        reqPlanService.update(demandBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    /**
     * 项目启动，建立svn项目文档库以及下发项目启动邮件
     *
     * @return
     */
    @RequestMapping("/goProjectStart")
    public GenericRspDTO<ProjectStartRspDTO> goProjectStart(@RequestBody ProjectStartReqDTO reqDTO) {
        ProjectStartBO demandBO = reqPlanService.goProjectStart(reqDTO.getReq_inner_seq());
        ProjectStartRspDTO projectStartRspDTO = new ProjectStartRspDTO();
        projectStartRspDTO.setReq_inner_seq(demandBO.getReq_inner_seq());
        projectStartRspDTO.setReq_nm(demandBO.getReq_nm());
        projectStartRspDTO.setReq_no(demandBO.getReq_no());
        projectStartRspDTO.setSendTo(demandBO.getSendTo());
        projectStartRspDTO.setCopyTo(demandBO.getCopyTo());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, projectStartRspDTO);
    }
    /**
     * 查询需求计划项目启动信息
     *
     * @return
     */
    @RequestMapping("/projectStart")
    public GenericRspDTO projectStart(@RequestBody ProjectStartReqDTO reqDTO, HttpServletRequest request) {
        // 项目启动邮件
        reqPlanService.projectStart(new ProjectStartBO(reqDTO.getReq_inner_seq(),reqDTO.getReq_no(),reqDTO.getReq_nm(),reqDTO.getSendTo(),reqDTO.getCopyTo()),request);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }
    /**
     * 存量变更
     *
     * @return
     */
    @RequestMapping("/changeReq")
    public GenericRspDTO changeReq(@RequestBody DemandReqDTO reqDTO) {
        reqPlanService.changeReq(reqDTO.getReq_impl_mon());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

    /**
     * 批量导入模板 下载
     *
     * @return
     * @throws IOException
     */
    @PostMapping("/template/download")
    public GenericRspDTO<NoBody> downloadTmp(GenericDTO<NoBody> req, HttpServletResponse response) {
        doWrite("static/gndDownload.xlsm", response);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 需求文档上传页面跳转
     */
    @RequestMapping("/goUploadFile")
    public GenericRspDTO<ProjectStartRspDTO> goUploadFile(@RequestBody DemandReqDTO reqDTO) {
        ProjectStartBO projectStartBO = reqPlanService.goProjectStart(reqDTO.getReq_inner_seq());
        DemandBO demandBO = reqPlanService.findById(reqDTO.getReq_inner_seq());
        ProjectStartRspDTO projectStartRspDTO = new ProjectStartRspDTO();
        projectStartRspDTO.setReq_inner_seq(projectStartBO.getReq_inner_seq());
        projectStartRspDTO.setPre_cur_period(demandBO.getPre_cur_period());
        projectStartRspDTO.setReq_nm(projectStartBO.getReq_nm());
        projectStartRspDTO.setReq_no(projectStartBO.getReq_no());
        projectStartRspDTO.setSendTo(projectStartBO.getSendTo());
        projectStartRspDTO.setCopyTo(projectStartBO.getCopyTo());
        String reqPeriod = dictionaryService.findFieldName("REQ_PEROID",demandBO.getPre_cur_period());
        //将需求阶段赋值成下一阶段
        if (new Integer(reqPeriod) <= 30) {
            reqPeriod = "30";
        }else if (new Integer(reqPeriod) <= 50) {
            reqPeriod = "50";
        }else if (new Integer(reqPeriod) <= 70) {
            reqPeriod = "70";
        }else if (new Integer(reqPeriod) <= 110) {
            reqPeriod = "110";
        }else if (new Integer(reqPeriod) <= 140) {
            reqPeriod = "140";
        }else if (new Integer(reqPeriod) <= 160) {
            reqPeriod = "160";
        }else if (new Integer(reqPeriod) <= 180) {
            reqPeriod = "180";
        }
        List<DictionaryBO> dictionaryBOList = dictionaryService.findUploadPeriod(reqPeriod);
        DictionaryRspDTO dictionaryRspDTO = new DictionaryRspDTO();
        dictionaryRspDTO.setDictionaryDTOList(BeanConvertUtils.convertList(dictionaryBOList, DictionaryDTO.class));
        projectStartRspDTO.setDictionaryRspDTO(dictionaryRspDTO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, projectStartRspDTO);
    }
    /**
     * 文档上传
     *
     * @return
     */
    @PostMapping("uploadProjrctFile")
    public GenericRspDTO<NoBody> uploadProjrctFile( ProjectStartReqDTO reqDTO,HttpServletRequest request) {
        System.out.println("需求文档上传："+reqDTO);
//        ProjectStartBO ProjectStartBO = new ProjectStartBO();
//        BeanConvertUtils.convert(ProjectStartBO, reqDTO);
//        reqPlanService.uploadProjrctFile(ProjectStartBO,files,request);
        return GenericRspDTO.newSuccessInstance();
    }

    /**
     * 文档上传接收文档
     *
     * @return
     */
    @PostMapping("/batch/import")
    public GenericRspDTO<NoBody> batchImport(@RequestParam("file") MultipartFile[] files,HttpServletRequest request, GenericDTO<NoBody> req) {
        for (MultipartFile importfile : files) {
            String fileName = importfile.getOriginalFilename();
            System.out.println("文件名："+fileName);
        }
//        reqTaskService.doBatchImport(file);
        return GenericRspDTO.newSuccessInstance();
    }
}
