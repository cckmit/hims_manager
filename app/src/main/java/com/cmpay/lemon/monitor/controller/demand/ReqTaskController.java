package com.cmpay.lemon.monitor.controller.demand;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.annotation.QueryBody;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.DemandDTO;
import com.cmpay.lemon.monitor.dto.DemandReqDTO;
import com.cmpay.lemon.monitor.dto.DemandRspDTO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author: zhou_xiong
 */
@RestController
@RequestMapping(value = MonitorConstants.REQTASK_PATH)
public class ReqTaskController {

    @Autowired
    private ReqTaskService reqTaskService;

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
        if(demandBO.getReq_impl_mon()==null){
            demandBO.setReq_impl_mon(month);
        }
        PageInfo<DemandBO> pageInfo = reqTaskService.findDemand(demandBO);
        List<DemandBO> demandBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandBO.class);

        for (int i =0; i < demandBOList.size();i++){
            String reqAbnorType=demandBOList.get(i).getReq_abnor_type();
            String reqAbnorTypeAll = "";
            DemandBO demand = reqTaskService.findById(demandBOList.get(i).getReq_inner_seq());

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

//        model.addAttribute("deptIdList", dictionaryServiceMgr.findDictionary("DEV_DEPT"));
//        model.addAttribute("reqPeroidList", dictionaryServiceMgr.findDictionary("REQ_PEROID"));
//        model.addAttribute("prdLineList", dictionaryServiceMgr.findDictionary("PRD_LINE"));
//        model.addAttribute("reqAbnorTypeList", dictionaryServiceMgr.findDictionary("REQ_ABNOR_TYPE"));
//        model.addAttribute("reqTaskList", lst);
//        Integer length=reqTaskService.findNumberByCondition(reqTaskVO);
//        model.addAttribute("totalCount", length);
//        model.addAttribute("pageSize", reqTaskVO.getPageSize());
//        model.addAttribute("pageNum", reqTaskVO.getPageNum());
//        model.addAttribute("condition", reqTaskVO);

        DemandRspDTO rspDTO = new DemandRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(demandBOList, DemandDTO.class));
        rspDTO.setPageNum(pageInfo.getPageNum());
        rspDTO.setPages(pageInfo.getPages());
        rspDTO.setTotal(pageInfo.getTotal());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }

    /**
     * 查询需求信息
     *
     * @return
     */
    @RequestMapping("/info/{id}")
    public GenericRspDTO<DemandDTO> getUserInfoById(@PathVariable("req_inner_seq") String req_inner_seq, GenericDTO<NoBody> req) {
        DemandBO demandBO = reqTaskService.findById(req_inner_seq);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, BeanUtils.copyPropertiesReturnDest(new DemandDTO(), demandBO));
    }

    /**
     * 查询所有需求信息
     */
    @RequestMapping("/all")
    public GenericRspDTO<DemandRspDTO> findAll(GenericDTO reqDTO) {
        DemandRspDTO rspDTO = new DemandRspDTO();
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(reqTaskService.findAll(), DemandDTO.class));
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
        reqTaskService.add(demandBO);
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
        reqTaskService.delete(req_inner_seq);
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
        reqTaskService.update(demandBO);
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, NoBody.class);
    }

}
