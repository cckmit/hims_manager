package com.cmpay.lemon.monitor.service.impl.workload;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.dao.IPlanDao;
import com.cmpay.lemon.monitor.dao.IWorkLoadDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.workload.ReqWorkLoadService;
import com.cmpay.lemon.monitor.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 需求计划
 * @author: ty
 */
@Service
public class ReqWorkLoadServiceImpl implements ReqWorkLoadService {
    //30 需求状态为暂停
    private static final String REQSUSPEND ="30";
    //40 需求状态为取消
    private static final String REQCANCEL ="40";
    // 30 需求定稿
    private static final int REQCONFIRM = 30;
    // 50 技术方案定稿
    private static final int TECHDOCCONFIRM = 50;
    // 70 测试用例定稿
    private static final int TESTCASECONFIRM = 70;
    // 110 完成SIT测试
    private static final int FINISHSITTEST = 110;
    // 120 UAT版本更新
    private static final int UPDATEUAT = 120;
    // 140 完成UAT测试
    private static final int FINISHUATTEST = 140;
    // 160 完成预投产
    private static final int FINISHPRETEST = 160;
    // 180 完成产品发布
    private static final int FINISHPRD = 180;

    @Autowired
    private IPlanDao planDao;
    @Autowired
    private IWorkLoadDao workLoadDao;
    @Autowired
    private ReqTaskService reqTaskService;

    @Override
    public DemandRspBO findDemand(DemandBO demandBO) {
        String time= DateUtil.date2String(new Date(), "yyyy-MM-dd");
        PageInfo<DemandBO> pageInfo = getPageInfo(demandBO);
        List<DemandBO> demandBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandBO.class);

        for (int i = 0; i < demandBOList.size(); i++) {
            String reqAbnorType = demandBOList.get(i).getReqAbnorType();
            String reqAbnorTypeAll = "";
            DemandBO demand = reqTaskService.findById(demandBOList.get(i).getReqInnerSeq());

            //当需求定稿时间、uat更新时间、测试完成时间、需求当前阶段、需求状态都不为空的时候，执行进度实时显示逻辑。
            if (StringUtils.isNotBlank(demand.getPrdFinshTm()) && StringUtils.isNotBlank(demand.getUatUpdateTm())
                    && StringUtils.isNotBlank(demand.getTestFinshTm()) && StringUtils.isNotBlank(demand.getPreCurPeriod())
                    && StringUtils.isNotBlank(demand.getReqSts())) {
                //当前时间大于预计时间，并且所处阶段小于30,并且需求状态不为暂停或取消（30，40）,则该需求进度异常
                if (time.compareTo(demand.getPrdFinshTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) < REQCONFIRM
                        && REQSUSPEND.compareTo(demand.getReqSts()) != 0 && REQCANCEL.compareTo(demand.getReqSts()) != 0) {
                    reqAbnorTypeAll += "需求进度滞后,";
                }
                if (time.compareTo(demand.getUatUpdateTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) >= REQCONFIRM
                        && Integer.parseInt(demand.getPreCurPeriod()) < UPDATEUAT && REQSUSPEND.compareTo(demand.getReqSts()) != 0
                        && REQCANCEL.compareTo(demand.getReqSts()) != 0) {
                    reqAbnorTypeAll += "开发进度滞后,";
                }
                if (time.compareTo(demand.getTestFinshTm()) > 0 && Integer.parseInt(demand.getPreCurPeriod()) >= UPDATEUAT
                        && Integer.parseInt(demand.getPreCurPeriod()) < FINISHUATTEST && REQSUSPEND.compareTo(demand.getReqSts()) != 0
                        && REQCANCEL.compareTo(demand.getReqSts()) != 0) {
                    reqAbnorTypeAll += "测试进度滞后";
                }
                if (StringUtils.isBlank(reqAbnorTypeAll)) {
                    reqAbnorTypeAll += "正常";
                }
            } else if (reqAbnorType.indexOf("01") != -1) {
                demandBOList.get(i).setReqAbnorType("正常");
                continue;
            } else {
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

            if (reqAbnorTypeAll.length() >= 1 && ',' == reqAbnorTypeAll.charAt(reqAbnorTypeAll.length() - 1)) {
                reqAbnorTypeAll = reqAbnorTypeAll.substring(0, reqAbnorTypeAll.length() - 1);
                demandBOList.get(i).setReqAbnorType(reqAbnorTypeAll);
            } else {
                demandBOList.get(i).setReqAbnorType(reqAbnorTypeAll);
            }
        }
        DemandRspBO demandRspBO = new DemandRspBO();
        demandRspBO.setDemandBOList(demandBOList);
        demandRspBO.setPageInfo(pageInfo);
        return demandRspBO;
    }

    private PageInfo<DemandBO>  getPageInfo(DemandBO demandBO) {
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        PageInfo<DemandBO> pageInfo = PageUtils.pageQueryWithCount(demandBO.getPageNum(), demandBO.getPageSize(),
                () -> BeanConvertUtils.convertList(workLoadDao.find(demandDO), DemandBO.class));
        return pageInfo;
    }
    /**
     * 存量变更
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void changeReq(String req_impl_mon){
//        boolean flag = this.authenticationUser();
//        if(flag){
        try {
            //获取上个月时间
            SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
            Date month = simpleDateFormatMonth.parse(req_impl_mon);
            Calendar c = Calendar.getInstance();
            c.setTime(month);
            c.add(Calendar.MONTH, -1);
            String last_month = simpleDateFormatMonth.format(c.getTime());
            System.err.println("上个月"+last_month);

            DemandDO demandDO = new DemandDO();
            demandDO.setReqImplMon(last_month);
            // 获取上个月需求阶段在技术方案定稿之后的需求
            List<DemandDO> last_list = workLoadDao.find(demandDO);

            //获取登录用户ID
            String update_user = SecurityUtils.getLoginUserId();
            DemandDO demand = new DemandDO();
            demand.setUpdateUser("tu_yi");
            demand.setUpdateTime(new Date());
            demand.setReqImplMon(req_impl_mon);

            //循环变更 实施月份为 req_impl_mon 需求
            for (int i = 0; i < last_list.size(); i++) {
                demand.setReqNm(last_list.get(i).getReqNm());
                demand.setReqNo(last_list.get(i).getReqNo());
                demand.setTotalWorkload(last_list.get(i).getTotalWorkload());
                demand.setLeadDeptPro(last_list.get(i).getLeadDeptPro());
                demand.setCoorDeptPro(last_list.get(i).getCoorDeptPro());
                demand.setLeadDeptWorkload(last_list.get(i).getLeadDeptWorkload());
                demand.setCoorDeptWorkload(last_list.get(i).getCoorDeptWorkload());
                //已录入工作量 = 上月已录入工作量 + 本月录入工作量
                demand.setInputWorkload(last_list.get(i).getInputWorkload() + last_list.get(i).getMonInputWorkload());
                //剩余工作量 = 总工作量 - （上月已录入工作量 + 本月录入工作量）
                demand.setRemainWorkload(last_list.get(i).getTotalWorkload() - demand.getInputWorkload());
                //上月已录入工作量 = 本月录入工作量
                demand.setLastInputWorkload(last_list.get(i).getMonInputWorkload());
                workLoadDao.updateRwlByImpl(demand);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //"存量需求转存失败" + e.getMessage();
            BusinessException.throwBusinessException(MsgEnum.ERROR_FAIL_CHANGE+e.getMessage());
        }
//        }else{
//            //无权限使用该功能
//            BusinessException.throwBusinessException(MsgEnum.ERROR_NOT_PRIVILEGE);
//        }
    }
    /**
     * 用户身份验证
     */
    public boolean authenticationUser(){
        //获取登录用户名
        String currentUser =  SecurityUtils.getLoginName();
        if (currentUser.equals("田群") || currentUser.equals("吴暇")) {
            return true;
        }
        return false;
    }

}
