package com.cmpay.lemon.monitor.controller.demand;

import com.cmpay.framework.data.request.GenericDTO;
import com.cmpay.framework.data.response.GenericRspDTO;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.data.NoBody;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.bo.ReqDataCountBO;
import com.cmpay.lemon.monitor.bo.ReqMngBO;
import com.cmpay.lemon.monitor.constant.MonitorConstants;
import com.cmpay.lemon.monitor.dto.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.reportForm.ReqDataCountService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;
import static com.cmpay.lemon.monitor.utils.FileUtils.doWrite;


@RestController
@RequestMapping(value = MonitorConstants.REQINDEX_PATH)
public class ReqIndexController {

    @Autowired
    private ReqDataCountService reqDataCountService;
    @Autowired
    private ReqPlanService reqPlanService;

    @RequestMapping("index")
    public GenericRspDTO<ReqIndexCountRspDTO> index(@RequestBody DemandReqDTO reqDTO,HttpServletRequest request, Model model) throws Exception {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        String roles = "";
        String deptName = "";
        String userName = "";
        Map reMap = new TreeMap();
        List<ReqDataCountBO> listDetl = new ArrayList<ReqDataCountBO>();
        ReqMngBO vo = new ReqMngBO();
        //DemandBO demandBO = new DemandBO();
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        vo.setReqImplMon(month);
        demandBO.setReqImplMon(month);
        reMap = reqDataCountService.selectByCenter(vo);
        DemandRspBO demandRspBO = reqDataCountService.findDemand(demandBO);
        //获取所有类型
        List<String> li = new ArrayList<>();
        if (null != reMap.get("DataMap")) {
            Set<String> keys = ((Map) reMap.get("DataMap")).keySet();
            for(String key:keys){
                if (!"0".equals(((Map) reMap.get("DataMap")).get(key))) {
                    li.add(key + "：" + ((Map) reMap.get("DataMap")).get(key));
                }
            }
        }
        ReqIndexCountRspDTO rspDTO = new ReqIndexCountRspDTO();
        rspDTO.setLi(li);
        rspDTO.setTotle((reMap.get("totle")).toString());
        rspDTO.setList(reMap.get("DataMap"));
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(demandRspBO.getDemandBOList(), DemandDTO.class));
        rspDTO.setPageNum(demandRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    @RequestMapping("index2")
    public GenericRspDTO<ReqIndexCountRspDTO> index2(@RequestBody DemandReqDTO reqDTO,HttpServletRequest request, Model model) throws Exception {
        DemandBO demandBO = BeanUtils.copyPropertiesReturnDest(new DemandBO(), reqDTO);
        String roles = "";
        String deptName = "";
        String userName = "";
        String type = reqDTO.getOrderDirection();
       if (StringUtils.equals(type, "*")) {
            type = "";
        }
        if (StringUtils.isNotBlank(type)) {
            type = checkType(type.replace("：", ":").split(":")[0]);
        }
        Map reMap = new TreeMap();
        List<ReqDataCountBO> listDetl = new ArrayList<ReqDataCountBO>();
        ReqMngBO vo = new ReqMngBO();
        //DemandBO demandBO = new DemandBO();
        String month = DateUtil.date2String(new Date(), "yyyy-MM");
        vo.setReqImplMon(month);
        vo.setPreCurPeriod(type);
        demandBO.setReqImplMon(month);
        demandBO.setPreCurPeriod(type);
        reMap = reqDataCountService.selectByCenter(vo);
        DemandRspBO demandRspBO = reqDataCountService.findDemand(demandBO);
        //获取所有类型
        List<String> li = new ArrayList<>();
        if (null != reMap.get("DataMap")) {
            Set<String> keys = ((Map) reMap.get("DataMap")).keySet();
            for(String key:keys){
                if (!"0".equals(((Map) reMap.get("DataMap")).get(key))) {
                    li.add(key + "：" + ((Map) reMap.get("DataMap")).get(key));
                }
            }
        }
        ReqIndexCountRspDTO rspDTO = new ReqIndexCountRspDTO();
        rspDTO.setLi(li);
        rspDTO.setTotle((reMap.get("totle")).toString());
        rspDTO.setList(reMap.get("DataMap"));
        rspDTO.setDemandDTOList(BeanConvertUtils.convertList(demandRspBO.getDemandBOList(), DemandDTO.class));
        rspDTO.setPageNum(demandRspBO.getPageInfo().getPageNum());
        rspDTO.setPages(demandRspBO.getPageInfo().getPages());
        rspDTO.setTotal(demandRspBO.getPageInfo().getTotal());
        rspDTO.setPageSize(demandRspBO.getPageInfo().getPageSize());
        return GenericRspDTO.newInstance(MsgEnum.SUCCESS, rspDTO);
    }
    private String checkType(String type) {
        switch (type) {
            case "需求阶段":
                type = "1";
                break;
            case "开发阶段":
                type = "2";
                break;
            case "测试阶段":
                type = "3";
                break;
            case "预投产阶段":
                type = "4";
                break;
            case "已投产":
                type = "5";
                break;
            case "需求异常":
                type = "6";
                break;
            case "取消需求":
                type = "7";
                break;
            case "暂停需求":
                type = "8";
                break;
            case "需求分析":
                type = "9";
                break;
            case "需求撰写":
                type = "10";
                break;
            case "需求定稿":
                type = "11";
                break;
            case "预投产测试":
                type = "12";
                break;
            case "需求进度异常":
                type = "13";
                break;
            case "未启动测试":
                type = "14";
                break;
            case "测试进行中":
                type = "15";
                break;
            case "已完成测试":
                type = "16";
                break;
            case "测试异常":
                type = "17";
                break;
            default:
                break;
        }
        return type;
    }
}
