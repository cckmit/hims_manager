package com.cmpay.lemon.monitor.service.impl.workload;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cmpay.lemon.common.Env;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.LemonUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.dic.DictionaryService;
import com.cmpay.lemon.monitor.service.impl.demand.ReqTaskServiceImpl;
import com.cmpay.lemon.monitor.service.workload.ReqWorkLoadService;
import com.cmpay.lemon.monitor.utils.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * 需求计划
 * @author: ty
 */
@Service
public class ReqWorkLoadServiceImpl implements ReqWorkLoadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReqTaskServiceImpl.class);
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
    // 180 完成产品发布
    private static final int ZERO = 0;
    // 工作量录入状态打开
    private static final  String WORKLOADENTRYSTATUSOPEN ="打开";

    @Autowired
    private IPlanDao planDao;
    @Autowired
    private IWorkLoadDao workLoadDao;
    @Autowired
    private IDictionaryExtDao dictionaryDao;
    @Autowired
    private DictionaryService dictionaryService;
    @Autowired
    private ReqTaskService reqTaskService;
    @Autowired
    SystemUserService userService;
    @Autowired
    IWorkloadLockedStateDao workloadLockedStateDao;
    @Autowired
    private IDemandExtDao demandDao;
    @Autowired
    private ISupportWorkloadExtDao iSupportWorkloadExtDao;
    @Autowired
    private IOrganizationStructureDao iOrganizationStructureDao;


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

    @Override
    public SupportWorkloadRspBO supportWorkloadfindList(SupportWorkloadBO supportWorkloadBO){
        PageInfo<SupportWorkloadBO> pageInfo = getPageInfo1(supportWorkloadBO);
        List<SupportWorkloadBO> supportWorkloadBOList = BeanConvertUtils.convertList(pageInfo.getList(), SupportWorkloadBO.class);
        SupportWorkloadRspBO supportWorkloadRspBO = new SupportWorkloadRspBO();
        supportWorkloadRspBO.setSupportWorkloadBOList(supportWorkloadBOList);
        supportWorkloadRspBO.setPageInfo(pageInfo);
        return supportWorkloadRspBO;
    }
    private PageInfo<SupportWorkloadBO>  getPageInfo1(SupportWorkloadBO supportWorkloadBO) {
        SupportWorkloadDO supportWorkloadDO = new SupportWorkloadDO();
        BeanConvertUtils.convert(supportWorkloadDO, supportWorkloadBO);
        PageInfo<SupportWorkloadBO> pageInfo = PageUtils.pageQueryWithCount(supportWorkloadBO.getPageNum(), supportWorkloadBO.getPageSize(),
                () -> BeanConvertUtils.convertList(iSupportWorkloadExtDao.findList(supportWorkloadDO), SupportWorkloadBO.class));
        return pageInfo;
    }
    /**
     * 存量变更
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void changeReq(String req_impl_mon){
        //取消固定操作人判断
        // boolean flag = this.authenticationUser();
        if(true){
            try {
                //获取上个月时间
                SimpleDateFormat simpleDateFormatMonth = new SimpleDateFormat("yyyy-MM");
                Date month = simpleDateFormatMonth.parse(req_impl_mon);
                Calendar c = Calendar.getInstance();
                c.setTime(month);
                c.add(Calendar.MONTH, -1);
                String last_month = simpleDateFormatMonth.format(c.getTime());

                DemandDO demandDO = new DemandDO();
                demandDO.setReqImplMon(last_month);
                // 获取上个月需求阶段在技术方案定稿之后的需求
                List<DemandDO> last_list = workLoadDao.find(demandDO);

                //获取登录用户ID
                String update_user = SecurityUtils.getLoginName();
                DemandDO demand = new DemandDO();
                demand.setUpdateUser(update_user);
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
                MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_FAIL_CHANGE.getMsgInfo()+e.getMessage());
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }else{
            //无权限使用该功能
            BusinessException.throwBusinessException(MsgEnum.ERROR_NOT_PRIVILEGE);
        }
    }
    /**
     * 用户身份验证
     */
    public boolean authenticationUser(){
        //获取登录用户名
        String currentUser =  userService.getFullname(SecurityUtils.getLoginName());
        if (currentUser.equals("田群") || currentUser.equals("吴暇")) {
            return true;
        }
        return false;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void doBatchImport(MultipartFile file) {
        File f = null;
        List<DemandDO> demandDOS=new ArrayList<>();
        try {
            //MultipartFile转file
            String originalFilename = file.getOriginalFilename();
            //获取后缀名
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if(suffix.equals("xls")){
                suffix=".xls";
            }else if(suffix.equals("xlsm")||suffix.equals("xlsx")){
                suffix=".xlsx";
            }else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("文件类型错误");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            f=File.createTempFile("tmp", suffix);
            file.transferTo(f);
            String filepath = f.getPath();
            //excel转java类
            ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
            Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
            for (int i = 1; i <= map.size(); i++) {
                DemandDO demandDO = new DemandDO();
                demandDO.setReqInnerSeq(map.get(i).get(0).toString().trim());
                demandDO.setReqNo(map.get(i).get(1).toString().trim());
                demandDO.setReqNm(map.get(i).get(2).toString().trim());
                demandDO.setReqPrdLine(map.get(i).get(3).toString().trim());
                demandDO.setDevpLeadDept(map.get(i).get(4).toString().trim());
                demandDO.setDevpResMng(map.get(i).get(5).toString().trim());
                if(!JudgeUtils.isEmpty(map.get(i).get(6).toString().trim())) {
                    demandDO.setTotalWorkload((int)Float.parseFloat( map.get(i).get(6).toString().trim()));
                }else {
                    int t = i+1;
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + t + "行的" + "总工作量为空");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                if(!JudgeUtils.isEmpty(map.get(i).get(7).toString().trim())) {
                    demandDO.setInputWorkload((int)Float.parseFloat( map.get(i).get(7).toString().trim()));
                }else {
                    int t = i+1;
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + t + "行的" + "已录入工作量为空");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                if(!JudgeUtils.isEmpty(map.get(i).get(8).toString().trim())) {
                    demandDO.setMonInputWorkload((int)Float.parseFloat( map.get(i).get(8).toString().trim()));
                }else {
                    int t = i+1;
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + t + "行的" + "本月录入工作量为空");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                demandDOS.add(demandDO);
            }
        } catch (BusinessException e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }catch (Exception e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }finally {
            f.delete();
        }

        List<DemandDO> updateList = new ArrayList<>();
        demandDOS.forEach(m -> {
            int i = demandDOS.indexOf(m)+2;
            String ReqInnerSeq = m.getReqInnerSeq();
            DemandBO demandBO = reqTaskService.findById(ReqInnerSeq);
            //
            if(demandBO == null){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + i + "行的" + "需求内部编号不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            //判断编号是否规范
            String reqNo = m.getReqNo();
            if (!demandBO.getReqNo().equals(reqNo)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + i + "行的" + "需求编号不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            m.setReqNo(reqNo);
            //判断需求名是否规范
            String ReqNm = m.getReqNm();
            if (!demandBO.getReqNm().equals(ReqNm)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + i + "行的" + "需求名称不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            m.setReqNm(ReqNm);
            //产品线验证
            DictionaryDO dictionaryDO = new DictionaryDO();
            dictionaryDO.setDicId("PRD_LINE");
            dictionaryDO.setValue(m.getReqPrdLine());
            List<DictionaryDO> dic = dictionaryDao.getDicByDicId(dictionaryDO);
            if (dic.size() == 0) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo()  + "第" + i + "行的" + "产品名称字典项不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            m.setReqPrdLine(dic.get(0).getName());
            //需求主导部门
            dictionaryDO.setDicId("DEV_DEPT");
            dictionaryDO.setValue(m.getDevpLeadDept());
            dic = dictionaryDao.getDicByDicId(dictionaryDO);
            if (dic.size() == 0) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + i + "行的"  + "需求主导部门字典项不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            m.setDevpLeadDept(dic.get(0).getName());
            //开发负责人验证
            dictionaryDO.setUserName(m.getDevpResMng());
            dic = dictionaryDao.getJdInfo(dictionaryDO);
            if (dic.size() == 0) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + i + "行的" + "需求提出人不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            m.setDevpResMng(dic.get(0).getUserName());
            //剩余工作量
            m.setRemainWorkload(m.getTotalWorkload()-m.getInputWorkload());
            //本月录入工作量不能大于剩余工作量！
            if(m.getRemainWorkload() < m.getMonInputWorkload()){
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + i + "行的"  + "本月录入工作量不能大于剩余工作量！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            //如果开发配合部门为空
            if (StringUtils.isNotEmpty(m.getDevpLeadDept()) && StringUtils.isEmpty(demandBO.getDevpCoorDept())) {
                m.setLeadDeptPro(m.getDevpLeadDept()+":100%;");
                if (m.getTotalWorkload()==0){
                    m.setLeadDeptWorkload(m.getDevpLeadDept()+":0.00;");
                }else {
                    m.setLeadDeptWorkload(m.getDevpLeadDept()+":"+String.format("%.2f",Double.valueOf(m.getTotalWorkload()))+";");
                }
            }
            //开发配合部门不为空
            if (StringUtils.isNotEmpty(m.getDevpLeadDept()) && StringUtils.isNotEmpty(demandBO.getDevpCoorDept())) {
                Map<String, String> map = checkDeptRate(m.getTotalWorkload(), demandBO.getLeadDeptPro() + demandBO.getCoorDeptPro(), demandBO);
                if(StringUtils.isNotEmpty(map.get("message"))){
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + map.get("message"));
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                m.setLeadDeptPro(map.get("leadDeptRate"));
                m.setCoorDeptPro(map.get("coorDeptRate"));
                m.setLeadDeptWorkload(map.get("leadDpetWorkLoad"));
                m.setCoorDeptWorkload(map.get("coorDpetWorkLoad"));
            }
            m.setUpdateUser(SecurityUtils.getLoginName());
            m.setUpdateTime(new Date());
            updateList.add(m);
        });
        try {
            // 更新数据库
            updateList.forEach(m -> {
                workLoadDao.updateReqWorkLoadImport(m);
            });
        } catch (Exception e) {
            LOGGER.error("需求记录导入失败", e);
            BusinessException.throwBusinessException(MsgEnum.DB_UPDATE_FAILED);
        }
    }

    @Override
    public Map<String, String> checkDeptRate(int totWork, String deptInfo, DemandBO demand) {
        deptInfo = deptInfo.replaceAll("：", ":").replaceAll("；", ";");
        Map<String, String> map = new HashMap<>();
        if (deptInfo.indexOf(":") < 0) {
            map.put("message", "【工作量占比(配合部门投入占比)】格式有误，正确格式为：【银行合作研发部：80%;移动业务研发部：20%】，  请修改后重新导入！");
            return map;
        }
        String[] deptrates = deptInfo.split(";");
        BigDecimal totalRate = new BigDecimal(0.0);
        String coorDeptRate = "";
        String leadDeptRate = "";
        String leadDpetWorkLoad = "";
        String coorDpetWorkLoad = "";
        Set<String> leadDeptName=new HashSet<>();
        Set<String> coorDeptName=new HashSet<>();
        for (int i = 0; i < deptrates.length; i++) {
            String perRate = deptrates[i];
            if (StringUtils.isNotEmpty(perRate)) {
                String[] detailRate = perRate.split(":");
                if (detailRate.length != 2) {
                    map.put("message", "【工作量占比(配合部门投入占比)】格式有误，正确格式为：【银行合作研发部：80%;移动业务研发部：20%】，  请修改后重新导入！");
                    return map;
                }
                String rate = detailRate[1];
                rate = rate.replaceAll("%", "");
                if (StringUtils.isEmpty(rate)) {
                    map.put("message", "【工作量占比(配合部门投入占比)】格式有误，正确格式为：【银行合作研发部：80%;移动业务研发部：20%】，  请修改后重新导入！");
                    return map;
                } else {
                    totalRate = totalRate.add(new BigDecimal(Double.valueOf(rate))).setScale(2,
                            BigDecimal.ROUND_HALF_EVEN);
                }
                if (StringUtils.isBlank(dictionaryService.findFieldName("DEV_DEPT", detailRate[0]))) {
                    map.put("message", "不存在部门："+detailRate[0]);
                    return map;
                }
                if (detailRate[0].equals(demand.getDevpLeadDept())) {
                    leadDeptName.add(detailRate[0]);
                    // 主导部门
                    leadDeptRate = detailRate[0] + ":" + detailRate[1] + ";";
                    leadDpetWorkLoad = leadDpetWorkLoad + detailRate[0] + ":"
                            + new BigDecimal(totWork * Double.valueOf(rate) / 100).setScale(2,
                            BigDecimal.ROUND_HALF_EVEN)
                            + ";";
                } else {
                    coorDeptName.add(detailRate[0]);
                    coorDeptRate = coorDeptRate + detailRate[0] + ":" + detailRate[1] + ";";
                    coorDpetWorkLoad = coorDpetWorkLoad + detailRate[0] + ":"
                            + new BigDecimal(totWork * Double.valueOf(rate) / 100).setScale(2,
                            BigDecimal.ROUND_HALF_EVEN)
                            + ";";
                }
            }
        }
        if (totalRate.compareTo(new BigDecimal(100.0)) != 0) {
            // 返回值 -1 小于 0 等于 1 大于
            map.put("message", "【工作量占比(配合部门投入占比)】合计应等于100%，  请修改后重新导入！");
            return map;
        }
        int size=leadDeptName.size()+coorDeptName.size();
        if(deptrates.length!=size){
            map.put("message", "存在重复的配合部门信息请检查！");
            return map;
        }
        String coorDept=demand.getDevpCoorDept();
        if(StringUtils.isNotBlank(coorDept)){
            String[] coorDeptArr=coorDept.split(",");
            for (int i = 0; i < coorDeptArr.length; i++) {
                if ("产品研究部".equals(coorDeptArr[i]) || "产品测试部".equals(coorDeptArr[i])) {
                    continue;
                }
                if (!coorDeptName.contains(coorDeptArr[i])) {
                    map.put("message", "配合部门占比信息有误请检查！");
                    return map;
                }
            }
        }

        map.put("leadDeptRate", leadDeptRate);
        map.put("coorDeptRate", coorDeptRate);
        map.put("leadDpetWorkLoad", leadDpetWorkLoad);
        map.put("coorDpetWorkLoad", coorDpetWorkLoad);
        map.put("remainWordkLoad", String.valueOf(totWork - demand.getInputWorkload()));
        return map;
    }
    /**
     *
     */
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, DemandBO demandBO, String type, String file_nm) {
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        //需求文档下载
        if("1".equals(type)){ //基地工作量导出
            List<DemandDO> demandDOList = workLoadDao.findList(demandDO);
            List<BaseWorkloadBO> baseWorkloadBOList = new LinkedList<>();
            demandDOList.forEach(m->
                    baseWorkloadBOList.add(BeanUtils.copyPropertiesReturnDest(new BaseWorkloadBO(), m))
            );
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), BaseWorkloadBO.class, baseWorkloadBOList);
            try (OutputStream output = response.getOutputStream();
                 BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
                // 判断数据
                if (workbook == null) {
                    BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
                }
                // 设置excel的文件名称
                String excelName = "base_" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
                response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
                response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
                workbook.write(bufferedOutPut);
                bufferedOutPut.flush();
            } catch (IOException e) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
        }else if ("2".equals(type)){ //各部门工作量月统计明细报表
            List<DemandDO> demandDOList = workLoadDao.findList(demandDO);
            List<DepartmentMonthlyDetailBO> baseWorkloadBOList = new LinkedList<>();
            demandDOList.forEach(m ->
            {
                DemandBO demand = new DemandBO();
                BeanConvertUtils.convert(demand, m);
                //获取本月工作量
                DepartmentMonthlyDetailBO departmentMonthlyDetailBO = BeanUtils.copyPropertiesReturnDest(new DepartmentMonthlyDetailBO(), m);
                departmentMonthlyDetailBO.setCoorDeptWorkload1(getWorkLoad(demand).getCoorDeptWorkload());
                departmentMonthlyDetailBO.setLeadDeptWorkload1(getWorkLoad(demand).getLeadDeptWorkload());
                baseWorkloadBOList.add(departmentMonthlyDetailBO);
            });
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DepartmentMonthlyDetailBO.class, baseWorkloadBOList);
            try (OutputStream output = response.getOutputStream();
                 BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
                // 判断数据
                if (workbook == null) {
                    BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
                }
                // 设置excel的文件名称
                String excelName = "detail_" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
                response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
                response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
                workbook.write(bufferedOutPut);
                bufferedOutPut.flush();
            } catch (IOException e) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
        }else{
            List list = null;
            String[] headerNames =null;
            List<DemandDO>  mon_input_workload_list = workLoadDao.goExportCountForDevp(demandDO);
            List<DemandBO> DevpWorkLoadList = new ArrayList<>();
            for (int i = 0; i < mon_input_workload_list.size(); i++){
                DemandDO demand = mon_input_workload_list.get(i);
                DemandBO demandBO1 = new DemandBO();
                BeanConvertUtils.convert(demandBO1, demand);
                DemandBO devpDemand = getWorkLoad(demandBO1);
                devpDemand.setCoorDeptWorkload(devpDemand.getLeadDeptWorkload()+devpDemand.getCoorDeptWorkload());
                DevpWorkLoadList.add(devpDemand);
            }
            list = dealDevpWorkload(DevpWorkLoadList);// 各部门工作量月统计汇总报表导出  goExportCountForDevp
            headerNames = new String[]{"质量监督组","平台架构研发团队","产品测试团队","前端技术研发团队","金融业务研发团队",
                    "客户业务研发团队","支付业务研发团队","商户业务研发团队", "智慧食堂研发团队", "业务中台研发团队",
                    "资金归集项目组","设计项目组","团体组织交费项目组","客服中间层项目组"};

            OutputStream os = null;
            response.reset();

            String path="";
            if(LemonUtils.getEnv().equals(Env.SIT)) {
                path= "/home/devms/temp/propkg/";
            }
            else if(LemonUtils.getEnv().equals(Env.DEV)) {
                path= "/home/devadm/temp/propkg/";
            }else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            try {
                String fileName = "summarize.xls";
                String filePath = path + fileName;
                ReqWorkLoadExcelUtil util = new ReqWorkLoadExcelUtil();
                String createFile = util.createExcel(filePath, list, null,headerNames,type);
                //告诉浏览器允许所有的域访问
                //注意 * 不能满足带有cookie的访问,Origin 必须是全匹配
                //resp.addHeader("Access-Control-Allow-Origin", "*");
                //解决办法通过获取Origin请求头来动态设置
                String origin = request.getHeader("Origin");
                if (StringUtils.isNotBlank(origin)) {
                    response.addHeader("Access-Control-Allow-Origin", origin);
                }
                //允许带有cookie访问
                response.addHeader("Access-Control-Allow-Credentials", "true");
                //告诉浏览器允许跨域访问的方法
                response.addHeader("Access-Control-Allow-Methods", "*");
                //告诉浏览器允许带有Content-Type,header1,header2头的请求访问
                //resp.addHeader("Access-Control-Allow-Headers", "Content-Type,header1,header2");
                //设置支持所有的自定义请求头
                String headers = request.getHeader("Access-Control-Request-Headers");
                if (StringUtils.isNotBlank(headers)) {
                    response.addHeader("Access-Control-Allow-Headers", headers);
                }
                //告诉浏览器缓存OPTIONS预检请求1小时,避免非简单请求每次发送预检请求,提升性能
                response.addHeader("Access-Control-Max-Age", "3600");
                response.setContentType("application/octet-stream; charset=utf-8");
                response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + fileName);
                try {
                    os = response.getOutputStream();
                    os.write(org.apache.commons.io.FileUtils.readFileToByteArray(new File(filePath)));
                    os.flush();
                } catch (IOException e) {
                    throw e;
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            throw e;
                        }
                    }
                }
                // 删除文件
                 new File(createFile).delete();
            } catch (UnsupportedEncodingException e) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("各部门工作量月统计汇总报表导出失败");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            } catch (Exception e) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("各部门工作量月统计汇总报表导出失败");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
    }
    //  研发中心各二级团队月工作量汇总  （二级团队的工作量）
    @Override
    public List<Double> getExportCountForDevp(DemandBO demandBO){
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        List list = null;
        String[] headerNames =null;
        List<DemandDO>  mon_input_workload_list = workLoadDao.goExportCountForDevp(demandDO);
        List<DemandBO> DevpWorkLoadList = new ArrayList<>();
        for (int i = 0; i < mon_input_workload_list.size(); i++){
            DemandDO demand = mon_input_workload_list.get(i);
            DemandBO demandBO1 = new DemandBO();
            BeanConvertUtils.convert(demandBO1, demand);
            DemandBO devpDemand = getWorkLoad2(demandBO1);
            devpDemand.setCoorDeptWorkload(devpDemand.getLeadDeptWorkload()+devpDemand.getCoorDeptWorkload());
            DevpWorkLoadList.add(devpDemand);
        }
        list = dealDevpWorkload2(DevpWorkLoadList,demandBO.getDevpLeadDept());// 各部门工作量月统计汇总报表导出  goExportCountForDevp
        return list;
    }
    //  研发中心团队月工作量汇总  （部门集合汇总）
    @Override
    public List<Double> getExportCountForDevp2(DemandBO demandBO){
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        System.err.println(demandDO);
        List list = null;
        String[] headerNames =null;
        List<DemandDO>  mon_input_workload_list = workLoadDao.goExportCountForDevp(demandDO);
        System.err.println(mon_input_workload_list);
        List<DemandBO> DevpWorkLoadList = new ArrayList<>();
        for (int i = 0; i < mon_input_workload_list.size(); i++){
            DemandDO demand = mon_input_workload_list.get(i);
            DemandBO demandBO1 = new DemandBO();
            BeanConvertUtils.convert(demandBO1, demand);
            DemandBO devpDemand = getWorkLoad2(demandBO1);
            devpDemand.setCoorDeptWorkload(devpDemand.getLeadDeptWorkload()+devpDemand.getCoorDeptWorkload());
            DevpWorkLoadList.add(devpDemand);
        }
        list = dealDevpWorkload3(DevpWorkLoadList);// 各部门工作量月统计汇总报表导出  goExportCountForDevp
        return list;
    }
    // 二级团队月工作量报表汇总
    @Override
    public void goExportCountForDevp2(HttpServletRequest request, HttpServletResponse response, DemandBO demandBO, String type){
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        List list = null;
        String[] headerNames =null;
        List<DemandDO>  mon_input_workload_list = workLoadDao.goExportCountForDevp(demandDO);
        List<DemandBO> DevpWorkLoadList = new ArrayList<>();
        for (int i = 0; i < mon_input_workload_list.size(); i++){
            DemandDO demand = mon_input_workload_list.get(i);
            DemandBO demandBO1 = new DemandBO();
            BeanConvertUtils.convert(demandBO1, demand);
            DemandBO devpDemand = getWorkLoad(demandBO1);
            devpDemand.setCoorDeptWorkload(devpDemand.getLeadDeptWorkload()+devpDemand.getCoorDeptWorkload());
            DevpWorkLoadList.add(devpDemand);
        }
        list = dealDevpWorkload2(DevpWorkLoadList);// 各部门工作量月统计汇总报表导出  goExportCountForDevp
        headerNames = new String[]{"质量监督组","平台架构研发团队","产品测试团队","前端技术研发团队","信用购机研发组","号码借研发组",
                "营销活动研发组","渠道产品研发组","聚合支付研发组","话费充值研发组","商户业务研发团队", "智慧食堂研发团队", "银行&公共中心研发组",
                "用户&清算&账务研发组","支付研发组","营销研发组","资金归集项目组","设计项目组","团体组织交费项目组","客服中间层项目组"};

        OutputStream os = null;
        response.reset();

        String path="";
        if(LemonUtils.getEnv().equals(Env.SIT)) {
            path= "/home/devms/temp/propkg/";
        }
        else if(LemonUtils.getEnv().equals(Env.DEV)) {
            path= "/home/devadm/temp/propkg/";
        }else {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        try {
            String fileName = "summarize.xls";
            String filePath = path + fileName;
            ReqWorkLoadExcelUtil2 util = new ReqWorkLoadExcelUtil2();
            String createFile = util.createExcel(filePath, list, null,headerNames,type);
            //告诉浏览器允许所有的域访问
            //注意 * 不能满足带有cookie的访问,Origin 必须是全匹配
            //resp.addHeader("Access-Control-Allow-Origin", "*");
            //解决办法通过获取Origin请求头来动态设置
            String origin = request.getHeader("Origin");
            if (StringUtils.isNotBlank(origin)) {
                response.addHeader("Access-Control-Allow-Origin", origin);
            }
            //允许带有cookie访问
            response.addHeader("Access-Control-Allow-Credentials", "true");
            //告诉浏览器允许跨域访问的方法
            response.addHeader("Access-Control-Allow-Methods", "*");
            //告诉浏览器允许带有Content-Type,header1,header2头的请求访问
            //resp.addHeader("Access-Control-Allow-Headers", "Content-Type,header1,header2");
            //设置支持所有的自定义请求头
            String headers = request.getHeader("Access-Control-Request-Headers");
            if (StringUtils.isNotBlank(headers)) {
                response.addHeader("Access-Control-Allow-Headers", headers);
            }
            //告诉浏览器缓存OPTIONS预检请求1小时,避免非简单请求每次发送预检请求,提升性能
            response.addHeader("Access-Control-Max-Age", "3600");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + fileName);
            try {
                os = response.getOutputStream();
                os.write(org.apache.commons.io.FileUtils.readFileToByteArray(new File(filePath)));
                os.flush();
            } catch (IOException e) {
                throw e;
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        throw e;
                    }
                }
            }
            // 删除文件
            new File(createFile).delete();
        } catch (UnsupportedEncodingException e) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("各部门工作量月统计汇总报表导出失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        } catch (Exception e) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("各部门工作量月统计汇总报表导出失败");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }

    }
    //获取本月各部门工作量占比
    @Override
    public  DemandBO getWorkLoad(DemandBO demand) {
        if (StringUtils.isNotEmpty(demand.getDevpLeadDept()) && StringUtils.isEmpty(demand.getDevpCoorDept())) {
            demand.setLeadDeptPro(demand.getDevpLeadDept()+":100%;");
            if (demand.getTotalWorkload() == 0){
                demand.setLeadDeptWorkload(demand.getDevpLeadDept()+":0.00;");
            }else {
                demand.setLeadDeptWorkload(demand.getDevpLeadDept()+":"+String.format("%.2f",Double.valueOf(demand.getTotalWorkload()))+";");
            }
            updateReqWorkLoad(demand);
        }
        //本月工作量
        int monInputWorkload = demand.getMonInputWorkload();
        //主导部门本月工作量
        String LeadDeptCurMonWorkLoad = "";
        String lead = demand.getLeadDeptPro();
        String req_sts = demand.getReqSts();
        // && !("30".equals(req_sts)) 去调判断状态为取消
        if(StringUtils.isNotBlank(lead) && monInputWorkload != 0  ){
            String[] leadSplit = lead.replaceAll("%", "").split(":");
            leadSplit[1] = leadSplit[1].replaceAll(";","");
            LeadDeptCurMonWorkLoad = leadSplit[0]+":"+String.format("%.2f",(Double.valueOf(leadSplit[1])/100)*monInputWorkload)+";";
        }

        //配合部门本月工作量
        String CoorDevpCurMonWorkLoad = "";
        //配合工作量百分比
        String CoorDevpPer = "";
        String[] coorList = new String[20];
        String coor = demand.getCoorDeptPro();
        // && !("30".equals(req_sts)) 去调判断状态为取消
        if (StringUtils.isNotBlank(coor) && monInputWorkload != 0 ){
            coorList = demand.getCoorDeptPro().split(";");
            for (int i = 0; i < coorList.length;i++){
                if (StringUtils.isNotBlank(coorList[i])){
                    String[] CoorDevpCurMonWorkLoadSplit = coorList[i].split(":");
                    if (StringUtils.isNotBlank(CoorDevpCurMonWorkLoadSplit[0]) && StringUtils.isNotBlank(CoorDevpCurMonWorkLoadSplit[1])){
                        CoorDevpPer = String.format("%.2f",((Double.valueOf(CoorDevpCurMonWorkLoadSplit[1].replaceAll("%", "")))/100)*monInputWorkload);
                        CoorDevpCurMonWorkLoad += CoorDevpCurMonWorkLoadSplit[0]+":"+CoorDevpPer+";";
                    }
                }
            }
        }
        DemandBO demand1 = new DemandBO();
        demand1.setLeadDeptWorkload(LeadDeptCurMonWorkLoad);
        demand1.setCoorDeptWorkload(CoorDevpCurMonWorkLoad);
        return  demand1;
    }
    public  DemandBO getWorkLoad2(DemandBO demand) {
        if (StringUtils.isNotEmpty(demand.getDevpLeadDept()) && StringUtils.isEmpty(demand.getDevpCoorDept())) {
            demand.setLeadDeptPro(demand.getDevpLeadDept()+":100%;");
            if (demand.getTotalWorkload() == 0){
                demand.setLeadDeptWorkload(demand.getDevpLeadDept()+":0.00;");
            }else {
                demand.setLeadDeptWorkload(demand.getDevpLeadDept()+":"+String.format("%.2f",Double.valueOf(demand.getTotalWorkload()))+";");
            }
            //updateReqWorkLoad(demand);
        }
        //本月工作量
        int monInputWorkload = demand.getMonInputWorkload();
        //主导部门本月工作量
        String LeadDeptCurMonWorkLoad = "";
        String lead = demand.getLeadDeptPro();
        String req_sts = demand.getReqSts();
        // && !("30".equals(req_sts)) 去调判断状态为取消
        if(StringUtils.isNotBlank(lead) && monInputWorkload != 0  ){
            String[] leadSplit = lead.replaceAll("%", "").split(":");
            leadSplit[1] = leadSplit[1].replaceAll(";","");
            LeadDeptCurMonWorkLoad = leadSplit[0]+":"+String.format("%.2f",(Double.valueOf(leadSplit[1])/100)*monInputWorkload)+";";
        }

        //配合部门本月工作量
        String CoorDevpCurMonWorkLoad = "";
        //配合工作量百分比
        String CoorDevpPer = "";
        String[] coorList = new String[20];
        String coor = demand.getCoorDeptPro();
        // && !("30".equals(req_sts)) 去调判断状态为取消
        if (StringUtils.isNotBlank(coor) && monInputWorkload != 0 ){
            coorList = demand.getCoorDeptPro().split(";");
            for (int i = 0; i < coorList.length;i++){
                if (StringUtils.isNotBlank(coorList[i])){
                    String[] CoorDevpCurMonWorkLoadSplit = coorList[i].split(":");
                    if (StringUtils.isNotBlank(CoorDevpCurMonWorkLoadSplit[0]) && StringUtils.isNotBlank(CoorDevpCurMonWorkLoadSplit[1])){
                        CoorDevpPer = String.format("%.2f",((Double.valueOf(CoorDevpCurMonWorkLoadSplit[1].replaceAll("%", "")))/100)*monInputWorkload);
                        CoorDevpCurMonWorkLoad += CoorDevpCurMonWorkLoadSplit[0]+":"+CoorDevpPer+";";
                    }
                }
            }
        }
        DemandBO demand1 = new DemandBO();
        demand1.setLeadDeptWorkload(LeadDeptCurMonWorkLoad);
        demand1.setCoorDeptWorkload(CoorDevpCurMonWorkLoad);
        return  demand1;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void updateReqWorkLoad(DemandBO bean) {
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, bean);
        workLoadDao.updateReqWorkLoad(demandDO);
    }

    private List<Double> dealDevpWorkload(List<DemandBO> rowList){
        List<Double> list = Arrays.asList(new Double[14]);
        for (int i = 0; i < rowList.size(); i++) {
            //配合部门工作量
            String[] coorList = rowList.get(i).getCoorDeptWorkload().split(";");
            if (StringUtils.isNotBlank(coorList[0])){
                for (int j = 0; j < coorList.length; j++) {
                    list = addWorkload(list,coorList[j]);
                }
            }
        }

        return list;
    }
    private List<Double> addWorkload(List<Double> list,String str){
        String devp = str.split(":")[0];
        Double workload = Double.valueOf(str.split(":")[1]);
        switch (devp) {
            case "质量监督组":
                list.set(0, workload + (list.get(0) == null ? 0:list.get(0)) );
                break;
            case "平台架构研发团队":
                list.set(1, workload + (list.get(1) == null ? 0:list.get(1)) );
                break;
            case "产品测试团队":
                list.set(2, workload + (list.get(2) == null ? 0:list.get(2)) );
                break;
            case "前端技术研发团队":
                list.set(3, workload + (list.get(3) == null ? 0:list.get(3)) );
                break;
            case "信用购机研发组":
                list.set(4, workload + (list.get(4) == null ? 0:list.get(4)) );
                break;
            case "号码借研发组":
                list.set(4, workload + (list.get(4) == null ? 0:list.get(4)) );
                break;
            case "营销活动研发组":
                list.set(5, workload + (list.get(5) == null ? 0:list.get(5)) );
                break;
            case "渠道产品研发组":
                list.set(5, workload + (list.get(5) == null ? 0:list.get(5)) );
                break;
            case "聚合支付研发组":
                list.set(6, workload + (list.get(6) == null ? 0:list.get(6)) );
                break;
            case "话费充值研发组":
                list.set(6, workload + (list.get(6) == null ? 0:list.get(6)) );
                break;
            case "商户业务研发团队":
                list.set(7, workload + (list.get(7) == null ? 0:list.get(7)) );
                break;
            case "智慧食堂研发团队":
                list.set(8, workload + (list.get(8) == null ? 0:list.get(8)) );
                break;
            case "银行&公共中心研发组":
                list.set(9, workload + (list.get(9) == null ? 0:list.get(9)) );
                break;
            case "用户&清算&账务研发组":
                list.set(9, workload + (list.get(9) == null ? 0:list.get(9)) );
                break;
            case "支付研发组":
                list.set(9, workload + (list.get(9) == null ? 0:list.get(9)) );
                break;
            case "营销研发组":
                list.set(9, workload + (list.get(9) == null ? 0:list.get(9)) );
                break;
            case "资金归集项目组":
                list.set(10, workload + (list.get(10) == null ? 0:list.get(10)) );
                break;
            case "设计项目组":
                list.set(11, workload + (list.get(11) == null ? 0:list.get(11)) );
                break;
            case "团体组织交费项目组":
                list.set(12, workload + (list.get(12) == null ? 0:list.get(12)) );
                break;
            case "客服中间层项目组":
                list.set(13, workload + (list.get(13) == null ? 0:list.get(13)) );
                break;

            default:
                break;
        }
        return list;
    }

    private List<Double> dealDevpWorkload2(List<DemandBO> rowList){
        List<Double> list = Arrays.asList(new Double[20]);
        for (int i = 0; i < rowList.size(); i++) {
            //配合部门工作量
            String[] coorList = rowList.get(i).getCoorDeptWorkload().split(";");
            if (StringUtils.isNotBlank(coorList[0])){
                for (int j = 0; j < coorList.length; j++) {
                    list = addWorkload2(list,coorList[j]);
                }
            }
        }

        return list;
    }
    private List<Double> dealDevpWorkload3(List<DemandBO> rowList){
        List<Double> list = Arrays.asList(new Double[20]);
        for (int i = 0; i < rowList.size(); i++) {
            //配合部门工作量
            String[] coorList = rowList.get(i).getCoorDeptWorkload().split(";");
            if (StringUtils.isNotBlank(coorList[0])){
                for (int j = 0; j < coorList.length; j++) {
                    list = addWorkload3(list,coorList[j]);
                }
            }
        }

        return list;
    }
    private List<Double> addWorkload3(List<Double> list,String str){
        String devp = str.split(":")[0];
        Double workload = Double.valueOf(str.split(":")[1]);
        list.set(0, workload + (list.get(0) == null ? 0:list.get(0)) );
        return list;
    }
    private List<Double> dealDevpWorkload2(List<DemandBO> rowList,String dept){
        List<Double> list = Arrays.asList(new Double[20]);
        for (int i = 0; i < rowList.size(); i++) {
            //配合部门工作量
            String[] coorList = rowList.get(i).getCoorDeptWorkload().split(";");
            if (StringUtils.isNotBlank(coorList[0])){
                for (int j = 0; j < coorList.length; j++) {
                    list = addWorkload2(list,coorList[j],dept);
                }
            }
        }

        return list;
    }
    private List<Double> addWorkload2(List<Double> list,String str,String dept){
        String devp = str.split(":")[0];
        Double workload = Double.valueOf(str.split(":")[1]);
        if(dept.equals(devp)){
            list.set(0, workload + (list.get(0) == null ? 0:list.get(0)) );
        }
        return list;
    }
    private List<Double> addWorkload2(List<Double> list,String str){
        String devp = str.split(":")[0];
        Double workload = Double.valueOf(str.split(":")[1]);
        switch (devp) {
            case "质量监督组":
                list.set(0, workload + (list.get(0) == null ? 0:list.get(0)) );
                break;
            case "平台架构研发团队":
                list.set(1, workload + (list.get(1) == null ? 0:list.get(1)) );
                break;
            case "产品测试团队":
                list.set(2, workload + (list.get(2) == null ? 0:list.get(2)) );
                break;
            case "前端技术研发团队":
                list.set(3, workload + (list.get(3) == null ? 0:list.get(3)) );
                break;
            case "信用购机研发组":
                list.set(4, workload + (list.get(4) == null ? 0:list.get(4)) );
                break;
            case "号码借研发组":
                list.set(5, workload + (list.get(5) == null ? 0:list.get(5)) );
                break;
            case "营销活动研发组":
                list.set(6, workload + (list.get(6) == null ? 0:list.get(6)) );
                break;
            case "渠道产品研发组":
                list.set(7, workload + (list.get(7) == null ? 0:list.get(7)) );
                break;
            case "聚合支付研发组":
                list.set(8, workload + (list.get(8) == null ? 0:list.get(8)) );
                break;
            case "话费充值研发组":
                list.set(9, workload + (list.get(9) == null ? 0:list.get(9)) );
                break;
            case "商户业务研发团队":
                list.set(10, workload + (list.get(10) == null ? 0:list.get(10)) );
                break;
            case "智慧食堂研发团队":
                list.set(11, workload + (list.get(11) == null ? 0:list.get(11)) );
                break;
            case "银行&公共中心研发组":
                list.set(12, workload + (list.get(12) == null ? 0:list.get(12)) );
                break;
            case "用户&清算&账务研发组":
                list.set(13, workload + (list.get(13) == null ? 0:list.get(13)) );
                break;
            case "支付研发组":
                list.set(14, workload + (list.get(14) == null ? 0:list.get(14)) );
                break;
            case "营销研发组":
                list.set(15, workload + (list.get(15) == null ? 0:list.get(15)) );
                break;
            case "资金归集项目组":
                list.set(16, workload + (list.get(16) == null ? 0:list.get(16)) );
                break;
            case "设计项目组":
                list.set(17, workload + (list.get(17) == null ? 0:list.get(17)) );
                break;
            case "团体组织交费项目组":
                list.set(18, workload + (list.get(18) == null ? 0:list.get(18)) );
                break;
            case "客服中间层项目组":
                list.set(19, workload + (list.get(19) == null ? 0:list.get(19)) );
                break;

            default:
                break;
        }
        return list;
    }
    @Override
    public Map<String,String> checkDeptRate1(DemandBO demand){
        String ReqInnerSeq = demand.getReqInnerSeq();
        String deptInfo = demand.getLeadDeptPro() + demand.getCoorDeptPro();
        int totWorkload = demand.getTotalWorkload();
        String coorDept = demand.getDevpCoorDept();
        int input_workload =demand.getInputWorkload();
        DemandDO demandDO = workLoadDao.getReqWorkLoad(ReqInnerSeq);
        demandDO.setDevpCoorDept(coorDept);
        demandDO.setInputWorkload(input_workload);
        DemandBO demandBO = new DemandBO();
        BeanConvertUtils.convert(demandBO, demandDO);
        Map<String, String> map = new HashMap<String, String>();
        int workLoad = 0;
        try {
            workLoad = totWorkload;
        } catch (Exception e) {
            map.put("message", "【总工作量】必须是整数！");
            return map;
        }
        if (workLoad == 0){
            map.put("message", "【总工作量】不能为零！");
            return map;
        }
        if (totWorkload < input_workload){
            map.put("message", "【总工作量】不能小于已录入工作量！");
            return map;
        }
        map = checkDeptRate(workLoad, deptInfo, demandBO);
        return map;
    }
    @Override
    public void update(DemandBO bean) {
        DemandBO demand=reqTaskService.findById(bean.getReqInnerSeq());
        //主导部门占比
        String leadDeptPro = bean.getLeadDeptPro();
        if (StringUtils.isEmpty(leadDeptPro)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_UPDATE.getMsgInfo() + "主导部门占比有误请检查！");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        bean.setLeadDeptPro(leadDeptPro);
        //配合部门占比
        String coorDeptPro = bean.getCoorDeptPro();
        bean.setCoorDeptPro(coorDeptPro);
        // 合法性校验以及自动补充数据
        int totWork = bean.getTotalWorkload();
        if (totWork == 0){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_UPDATE.getMsgInfo() + "【总工作量】不能为零！");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (totWork < bean.getInputWorkload()){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_UPDATE.getMsgInfo() + "【总工作量】不能小于已录入工作量！");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        String deptInfo = bean.getLeadDeptPro() + bean.getCoorDeptPro();
        Map<String, String> map = checkDeptRate(totWork, deptInfo, bean);
        String msg = map.get("message");
        if (!StringUtils.isBlank(msg)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_UPDATE.getMsgInfo() + msg );
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (bean.getRemainWorkload() < bean.getMonInputWorkload()) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_UPDATE.getMsgInfo() + "本月录入工作量不能大于剩余工作量！" );
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        bean.setLeadDeptWorkload(map.get("leadDpetWorkLoad"));
        bean.setCoorDeptWorkload(map.get("coorDpetWorkLoad"));
        bean.setRemainWorkload(Integer.parseInt(map.get("remainWordkLoad")));
        //页面需求实施月份
        String req_impl_mon_page = demand.getReqImplMon();
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, bean);
        List<DemandDO> list = workLoadDao.getReqTaskByNameAndUK(demandDO);
        //存放比当前页面实时月份大的实体
        try {
            List<DemandDO> reqImplMonList = new ArrayList<>();
            DateFormat df = new SimpleDateFormat("yyyy-MM");
            Date dt1 = df.parse(req_impl_mon_page);
            Date dt2 = null;
            for (int i = 0; i < list.size(); i++){
                //实施月份
                String req_impl_mon = list.get(i).getReqImplMon();
                //总工作量
                int total_workload = list.get(i).getTotalWorkload();
                //需求状态
                int pre_cur_period = Integer.parseInt(list.get(i).getPreCurPeriod());
                String req_sts = list.get(i).getReqSts();
                if (StringUtils.isNotBlank(req_impl_mon_page) && req_impl_mon_page.equals(req_impl_mon)){
                    continue;
                }else if (StringUtils.isNotBlank(req_impl_mon_page) && !(req_impl_mon_page.equals(req_impl_mon)) && total_workload == 0 && pre_cur_period > 50 && !("30".equals(req_sts))){
                    //存放最小实施日期的对象
                    DemandDO headmost = new DemandDO();
                    for (int j = 0;j < list.size();j++){
                        dt2 = df.parse(list.get(j).getReqImplMon());
                        if (list.get(j).getTotalWorkload() == 0 && Integer.parseInt(list.get(j).getPreCurPeriod()) > 50 && !("30".equals(req_sts)) && dt2.getTime() < dt1.getTime() ){
                            dt1 = dt2;
                            headmost = list.get(j);
                        }
                    }
                    if (StringUtils.isNotBlank(headmost.getReqImplMon())){
                        MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                        MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_UPDATE.getMsgInfo() + "请先填写该需求实施月份为"+headmost.getReqImplMon()+"的【总工作量】！");
                        BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                    }
                }
                if(StringUtils.isNotBlank(req_impl_mon_page) && !(req_impl_mon_page.equals(req_impl_mon))){
                    for (int j = 0;j < list.size();j++){
                        dt2 = df.parse(list.get(j).getReqImplMon());
                        if (dt2.getTime() > dt1.getTime()){
                            reqImplMonList.add(list.get(j));
                        }
                    }
                }
            }
            //已录入工作量
            int input_workload = bean.getMonInputWorkload();
            for(int i=0; i < list.size(); i++){
                //实施月份
                String req_impl_mon = list.get(i).getReqImplMon();
                dt2 = df.parse(req_impl_mon);
                if ( dt2.getTime() < dt1.getTime() ){
                    //已录入工作量
                    input_workload += list.get(i).getMonInputWorkload();
                }
            }
            if (reqImplMonList.size() >= 2){
                //存放最小实施日期的对象
                DemandDO headmost = new DemandDO();
                for (int j = 1; j < reqImplMonList.size(); j++){
                    Date reqImplMonDate1 = df.parse(reqImplMonList.get(0).getReqImplMon());
                    Date reqImplMonDate2 = df.parse(reqImplMonList.get(j).getReqImplMon());
                    if (reqImplMonDate1.getTime() > reqImplMonDate2.getTime()){
                        reqImplMonDate1 = reqImplMonDate2;
                        headmost = reqImplMonList.get(j);
                    }else{
                        headmost = reqImplMonList.get(0);
                    }
                }
                if (headmost != null){
                    //总工作量
                    headmost.setTotalWorkload(totWork);
                    headmost.setLeadDeptWorkload(map.get("leadDpetWorkLoad"));
                    headmost.setCoorDeptWorkload(map.get("coorDpetWorkLoad"));
                    headmost.setLeadDeptPro(leadDeptPro);
                    headmost.setCoorDeptPro(coorDeptPro);
                    //剩余工作量
                    headmost.setRemainWorkload(totWork - input_workload);
                    //已录入工作量
                    headmost.setInputWorkload(input_workload);
                    workLoadDao.updateReqWorkLoad(headmost);
                }
            }else if (reqImplMonList.size() == 1){
                //总工作量
                reqImplMonList.get(0).setTotalWorkload(totWork);
                reqImplMonList.get(0).setLeadDeptWorkload(map.get("leadDpetWorkLoad"));
                reqImplMonList.get(0).setCoorDeptWorkload(map.get("coorDpetWorkLoad"));
                reqImplMonList.get(0).setLeadDeptPro(leadDeptPro);
                reqImplMonList.get(0).setCoorDeptPro(coorDeptPro);
                //剩余工作量
                reqImplMonList.get(0).setRemainWorkload(totWork - input_workload);
                //已录入工作量
                reqImplMonList.get(0).setInputWorkload(input_workload);
                workLoadDao.updateReqWorkLoad(reqImplMonList.get(0));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 默认值设置
        setDefaultValue(bean);
        DemandDO deman = new DemandDO();
        BeanConvertUtils.convert(deman, bean);
        try {
            workLoadDao.updateReqWorkLoad(deman);

        } catch (Exception e) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("工作量提交失败！");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
    }

    @Override
    public void updateWorkloadEntryStatus(WorkloadLockedStateBO workloadLockedStateBO) {
        WorkloadLockedStateDO workloadLockedStateDO = BeanUtils.copyPropertiesReturnDest(new WorkloadLockedStateDO(), workloadLockedStateBO);
        workloadLockedStateDao.update(workloadLockedStateDO);
    }

    @Override
    public WorkloadLockedStateBO getWorkloadEntryStatus(WorkloadLockedStateBO workloadLockedStateBO) {

        WorkloadLockedStateDO workloadLockedStateDO = workloadLockedStateDao.get(workloadLockedStateBO.getEntrymonth());
        WorkloadLockedStateBO workloadLockedStateBO1 =new WorkloadLockedStateBO();
        //查询结果为空则新建该月份的数据
        if(JudgeUtils.isNull(workloadLockedStateDO)){
            WorkloadLockedStateDO workloadLockedStateDO1 =new WorkloadLockedStateDO();
            workloadLockedStateDO1.setEntrymonth(workloadLockedStateBO.getEntrymonth());
            workloadLockedStateDO1.setStatus(WORKLOADENTRYSTATUSOPEN);
            workloadLockedStateDao.insert(workloadLockedStateDO1);
            workloadLockedStateBO1= BeanUtils.copyPropertiesReturnDest(new WorkloadLockedStateBO(), workloadLockedStateDO1);
        }else {
            workloadLockedStateBO1 = BeanUtils.copyPropertiesReturnDest(new WorkloadLockedStateBO(), workloadLockedStateDO);
        }
        return workloadLockedStateBO1;
    }
    @Override
    public void updateFeedbackEntryStatus(WorkloadLockedStateBO workloadLockedStateBO) {
        WorkloadLockedStateDO workloadLockedStateDO = BeanUtils.copyPropertiesReturnDest(new WorkloadLockedStateDO(), workloadLockedStateBO);
        workloadLockedStateDao.updateFeedback(workloadLockedStateDO);
    }

    @Override
    public WorkloadLockedStateBO getFeedbackEntryStatus(WorkloadLockedStateBO workloadLockedStateBO) {

        WorkloadLockedStateDO workloadLockedStateDO = workloadLockedStateDao.getFeedback(workloadLockedStateBO.getEntrymonth());
        WorkloadLockedStateBO workloadLockedStateBO1 =new WorkloadLockedStateBO();
        //查询结果为空则新建该月份的数据
        if(JudgeUtils.isNull(workloadLockedStateDO)){
            WorkloadLockedStateDO workloadLockedStateDO1 =new WorkloadLockedStateDO();
            workloadLockedStateDO1.setEntrymonth(workloadLockedStateBO.getEntrymonth());
            workloadLockedStateDO1.setStatus(WORKLOADENTRYSTATUSOPEN);
            workloadLockedStateDao.insertFeedback(workloadLockedStateDO1);
            workloadLockedStateBO1= BeanUtils.copyPropertiesReturnDest(new WorkloadLockedStateBO(), workloadLockedStateDO1);
        }else {
            workloadLockedStateBO1 = BeanUtils.copyPropertiesReturnDest(new WorkloadLockedStateBO(), workloadLockedStateDO);
        }
        return workloadLockedStateBO1;
    }
    /**
     * 设置默认值（操作人，操作时间）
     * @param bean
     */
    private void setDefaultValue(DemandBO bean) {
        String currentUser =  SecurityUtils.getLoginName();
        bean.setUpdateUser(currentUser);
        bean.setUpdateTime(new Date());

    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void supportWorkloadDown(MultipartFile file) {
        File f = null;
        LinkedList<SupportWorkloadDO> supportWorkloadDOLinkedList = new LinkedList<>();
        try {
            //MultipartFile转file
            String originalFilename = file.getOriginalFilename();
            //获取后缀名
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if (suffix.equals("xls")) {
                suffix = ".xls";
            } else if (suffix.equals("xlsm") || suffix.equals("xlsx")) {
                suffix = ".xlsx";
            } else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("文件类型错误!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            f = File.createTempFile("tmp", suffix);
            file.transferTo(f);
            String filepath = f.getPath();
            //excel转java类
            ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
            Map<Integer, Map<Integer, Object>> map = excelReader.readExcelContent();
            for (int i = 1; i <= map.size(); i++) {
//                `documentNumber` varchar(255) DEFAULT NULL COMMENT '文号',
//                        `processStartDate` varchar(255) DEFAULT NULL COMMENT '流程开始日期',
//                        `productManagementDepartment` varchar(255) DEFAULT NULL COMMENT '产品管理部门',
//                        `productOwner` varchar(255) DEFAULT NULL COMMENT '产品负责人',
//                        `supportingManufacturerProducts` varchar(255) DEFAULT NULL COMMENT '支撑厂家产品',
//                        `supportTheTopic` varchar(2555) DEFAULT NULL COMMENT '支撑主题',
//                        `contentDescription` varchar(2555) DEFAULT NULL COMMENT '内容描述',
//                        `functionPointsDetail` varchar(2048) DEFAULT NULL COMMENT '功能点明细',
//                        `proposalTime` varchar(255) DEFAULT NULL COMMENT '需求提出时间',
//                        `completionTime` varchar(255) DEFAULT NULL COMMENT '需求完成时间',
//                        `supportManager` varchar(255) DEFAULT NULL COMMENT '支撑负责人',
//                        `supportWorkload` varchar(255) DEFAULT NULL COMMENT '支撑工作量',
//                        `finalWorkload` varchar(255) DEFAULT NULL COMMENT '最终工作量',
//                        `supportingManufacturers` varchar(255) DEFAULT NULL COMMENT '支撑厂家',
//                        `costDepartment` varchar(255) DEFAULT NULL COMMENT '成本管理部门',
//                        `secondLevelOrganization` varchar(255) DEFAULT NULL COMMENT '二级主导团队',
//                        `remark` varchar(2555) DEFAULT NULL COMMENT '备注',
                SupportWorkloadDO supportWorkloadDO = new SupportWorkloadDO();
                supportWorkloadDO.setReqImplMon(map.get(i).get(0).toString().trim());
                supportWorkloadDO.setFirstLevelOrganization(map.get(i).get(1).toString().trim());
                supportWorkloadDO.setCostdepartment(map.get(i).get(2).toString().trim());
                supportWorkloadDO.setDocumentnumber(map.get(i).get(3).toString().trim());
                supportWorkloadDO.setProcessstartdate(map.get(i).get(4).toString().trim());
                supportWorkloadDO.setProductmanagementdepartment(map.get(i).get(5).toString().trim());
                supportWorkloadDO.setProductowner(map.get(i).get(6).toString().trim());
                supportWorkloadDO.setSupportingmanufacturerproducts(map.get(i).get(7).toString().trim());
                supportWorkloadDO.setSupportthetopic(map.get(i).get(8).toString().trim());
                supportWorkloadDO.setContentdescription(map.get(i).get(9).toString().trim());
                supportWorkloadDO.setFunctionpointsdetail(map.get(i).get(10).toString().trim());
                supportWorkloadDO.setProposaltime(map.get(i).get(11).toString().trim());
                supportWorkloadDO.setCompletiontime(map.get(i).get(12).toString().trim());
                supportWorkloadDO.setSupportmanager(map.get(i).get(13).toString().trim());
                supportWorkloadDO.setSupportworkload(map.get(i).get(14).toString().trim());
                supportWorkloadDO.setFinalworkload(map.get(i).get(15).toString().trim());
                supportWorkloadDO.setSupportingmanufacturers(map.get(i).get(16).toString().trim());
                supportWorkloadDO.setRemark(map.get(i).get(17).toString().trim());
                supportWorkloadDOLinkedList.add(supportWorkloadDO);
                System.err.println(supportWorkloadDO);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        } finally {
            f.delete();
        }
        supportWorkloadDOLinkedList.forEach(m -> {
            SupportWorkloadDO supportWorkloadDO1 = new SupportWorkloadDO();
            supportWorkloadDO1.setDocumentnumber(m.getDocumentnumber());
            List<SupportWorkloadDO> productionDefectsDOS = iSupportWorkloadExtDao.find(supportWorkloadDO1);
            if (JudgeUtils.isEmpty(productionDefectsDOS)) {
                iSupportWorkloadExtDao.insert(m);
            } else {
                iSupportWorkloadExtDao.update(m);
            }
        });
    }
    //导出
    @Override
    public void getDownload(HttpServletResponse response, SupportWorkloadBO supportWorkloadBO) {
        SupportWorkloadDO supportWorkloadDO = new SupportWorkloadDO();
        BeanConvertUtils.convert(supportWorkloadDO, supportWorkloadBO);
        List<SupportWorkloadDO> demandDOList = iSupportWorkloadExtDao.findList(supportWorkloadDO);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), SupportWorkloadDO.class, demandDOList);
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // 判断数据
            if (workbook == null) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
            // 设置excel的文件名称
            String excelName = "supportWorkloadDO" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }
    @Override
    public void supportWorkloadCountForDevp(HttpServletRequest request, HttpServletResponse response, SupportWorkloadBO supportWorkloadBO ){
        SupportWorkloadDO supportWorkloadDO = new SupportWorkloadDO();
        BeanConvertUtils.convert(supportWorkloadDO, supportWorkloadBO);
        // 获取一级团队支撑工作量汇总数据
        List<SupportWorkloadDO>  mon_input_workload_list = iSupportWorkloadExtDao.supportWorkloadCountForDevp(supportWorkloadDO);
       if(mon_input_workload_list == null || mon_input_workload_list.size()<=0){
           MsgEnum.ERROR_CUSTOM.setMsgInfo("一级团队支撑工作量均为空，请先导入数据!");
           BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
       }
        //获取所有的一级团队
        List<String> firstLevelOrganizationList = iOrganizationStructureDao.findFirstLevelOrganization(new OrganizationStructureDO());
        firstLevelOrganizationList.add("运维团队");
        List<SupportWorkload2DO> DevpWorkLoadList = new ArrayList<>();
        BigDecimal sum = new BigDecimal("0");
        for(int i=0;i<firstLevelOrganizationList.size();i++){
            SupportWorkload2DO supportWorkload2DO = new SupportWorkload2DO();
            // 一级团队赋值
            supportWorkload2DO.setSecondlevelorganization(firstLevelOrganizationList.get(i));
            supportWorkload2DO.setFinalworkload("0");
            // 判断一级团队是否存在支撑工作量
            for(int j=0;j<mon_input_workload_list.size();j++){
                if(firstLevelOrganizationList.get(i).equals(mon_input_workload_list.get(j).getFirstLevelOrganization())){
                    supportWorkload2DO.setFinalworkload(mon_input_workload_list.get(j).getFinalworkload());
                }
            }
            sum = sum.add(new BigDecimal(supportWorkload2DO.getFinalworkload()));
            DevpWorkLoadList.add(supportWorkload2DO);
        }
        // 汇总数据
        SupportWorkload2DO supportWorkload2DO = new SupportWorkload2DO();
        supportWorkload2DO.setSecondlevelorganization("一级团队汇总");
        supportWorkload2DO.setFinalworkload(sum+"");
        DevpWorkLoadList.add(supportWorkload2DO);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), SupportWorkload2DO.class, DevpWorkLoadList);
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // 判断数据
            if (workbook == null) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
            // 设置excel的文件名称
            String excelName = "base_" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }

    }
    @Override
    public void supportWorkloadCountForDevp2(HttpServletRequest request, HttpServletResponse response, SupportWorkloadBO supportWorkloadBO){
        SupportWorkloadDO supportWorkloadDO = new SupportWorkloadDO();
        BeanConvertUtils.convert(supportWorkloadDO, supportWorkloadBO);
        // 获取二级团队支撑工作量汇总数据
        List<SupportWorkloadDO>  mon_input_workload_list = iSupportWorkloadExtDao.supportWorkloadCountForDevp2(supportWorkloadDO);
        if(mon_input_workload_list == null || mon_input_workload_list.size()<=0){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("二级团队支撑工作量均为空，请先导入数据!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        //获取所有的二级团队
        List<OrganizationStructureDO> secondlevelorganizationList = iOrganizationStructureDao.find(new OrganizationStructureDO());
        List<SupportWorkload2DO> DevpWorkLoadList = new ArrayList<>();
        BigDecimal sum = new BigDecimal("0");
        for(int i=0;i<secondlevelorganizationList.size();i++){
            SupportWorkload2DO supportWorkload2DO = new SupportWorkload2DO();
            // 一级团队赋值
            supportWorkload2DO.setSecondlevelorganization(secondlevelorganizationList.get(i).getSecondlevelorganization());
            supportWorkload2DO.setFinalworkload("0");
            // 判断一级团队是否存在支撑工作量
            for(int j=0;j<mon_input_workload_list.size();j++){
                if(secondlevelorganizationList.get(i).getSecondlevelorganization().equals(mon_input_workload_list.get(j).getSecondlevelorganization())){
                    supportWorkload2DO.setFinalworkload(mon_input_workload_list.get(j).getFinalworkload());
                }
            }
            sum = sum.add(new BigDecimal(supportWorkload2DO.getFinalworkload()));
            DevpWorkLoadList.add(supportWorkload2DO);
        }
        // 汇总数据
        SupportWorkload2DO supportWorkload2DO = new SupportWorkload2DO();
        supportWorkload2DO.setSecondlevelorganization("二级团队汇总");
        supportWorkload2DO.setFinalworkload(sum+"");
        DevpWorkLoadList.add(supportWorkload2DO);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), SupportWorkload2DO.class, DevpWorkLoadList);
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // 判断数据
            if (workbook == null) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
            // 设置excel的文件名称
            String excelName = "base_" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }
}
