package com.cmpay.lemon.monitor.service.impl.workload;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.BaseWorkloadBO;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.bo.DepartmentMonthlyDetailBO;
import com.cmpay.lemon.monitor.dao.IDictionaryExtDao;
import com.cmpay.lemon.monitor.dao.IPlanDao;
import com.cmpay.lemon.monitor.dao.IWorkLoadDao;
import com.cmpay.lemon.monitor.entity.Constant;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.DictionaryDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.dic.DictionaryService;
import com.cmpay.lemon.monitor.service.impl.demand.ReqTaskServiceImpl;
import com.cmpay.lemon.monitor.service.workload.ReqWorkLoadService;
import com.cmpay.lemon.monitor.utils.*;
import com.google.protobuf.ServiceException;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import static com.cmpay.lemon.monitor.utils.FileUtils.doWrite;

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
                BusinessException.throwBusinessException("文件类型错误");
            }
            f=File.createTempFile("tmp", suffix);
            file.transferTo(f);
            String filepath = f.getPath();
            //excel转java类
            ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
            Map<Integer, Map<Integer,Object>> map = excelReader.readExcelContent();
            for (int i = 1; i <= map.size(); i++) {
                DemandDO demandDO = new DemandDO();
                demandDO.setReqInnerSeq(map.get(i).get(0).toString());
                demandDO.setReqNo(map.get(i).get(1).toString());
                demandDO.setReqNm(map.get(i).get(2).toString());
                demandDO.setReqPrdLine(map.get(i).get(3).toString());
                demandDO.setDevpLeadDept(map.get(i).get(4).toString());
                demandDO.setDevpResMng(map.get(i).get(5).toString());
                if(!JudgeUtils.isEmpty(map.get(i).get(6).toString())) {
                    demandDO.setTotalWorkload(Integer.parseInt( map.get(i).get(6).toString()));
                }else {
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + i + "行的" + "总工作量为空");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                if(!JudgeUtils.isEmpty(map.get(i).get(7).toString())) {
                    demandDO.setInputWorkload(Integer.parseInt( map.get(i).get(7).toString()));
                }else {
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + i + "行的" + "已录入工作量为空");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                if(!JudgeUtils.isEmpty(map.get(i).get(8).toString())) {
                    demandDO.setMonInputWorkload(Integer.parseInt( map.get(i).get(8).toString()));
                }else {
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "第" + i + "行的" + "本月录入工作量为空");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                demandDOS.add(demandDO);
                System.err.println(demandDO.toString());
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
             int i = demandDOS.indexOf(m)+1;
            System.err.println(i);
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
                MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo()  + "第" + i + "行的" + "产品线字典项不存在");
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
                System.err.println(map);
                m.setLeadDeptPro(map.get("leadDeptRate"));
                m.setCoorDeptPro(map.get("coorDeptRate"));
                m.setLeadDeptWorkload(map.get("leadDpetWorkLoad"));
                m.setCoorDeptWorkload(map.get("coorDpetWorkLoad"));
            }
            //m.setUpdateUser(SecurityUtils.getLoginUserId());
            m.setUpdateUser("tu_yi");
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
        System.err.println(deptInfo);
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
                System.err.println("detailRate"+detailRate);
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
        System.err.println(totalRate);
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
            headerNames = new String[]{"银行合作研发部","移动支付研发部","营销服务研发部","电商支付研发部","风控大数据研发部",
                    "互联网金融研发部","前端技术研发部","基础应用研发部","客户端研发部","公共缴费研发部","产品测试部",
                    "平台架构部", "产品研究部","设计项目组","资金归集项目组","客服中间层项目组","行业拓展事业部","金科项目组"};

            OutputStream os = null;
            response.reset();
            try {
                String path = "/home/devadm/temp/propkg/";
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
                BusinessException.throwBusinessException("各部门工作量月统计汇总报表导出失败");
            } catch (Exception e) {
                BusinessException.throwBusinessException("各部门工作量月统计汇总报表导出失败");
            }
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
        if(StringUtils.isNotBlank(lead) && monInputWorkload != 0 && !("30".equals(req_sts)) ){
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
        if (StringUtils.isNotBlank(coor) && monInputWorkload != 0 && !("30".equals(req_sts))){
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
        List<Double> list = Arrays.asList(new Double[18]);
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
            case "银行合作研发部":
                list.set(0, workload + (list.get(0) == null ? 0:list.get(0)) );
                break;
            case "移动支付研发部":
                list.set(1, workload + (list.get(1) == null ? 0:list.get(1)) );
                break;
            case "营销服务研发部":
                list.set(2, workload + (list.get(2) == null ? 0:list.get(2)) );
                break;
            case "电商支付研发部":
                list.set(3, workload + (list.get(3) == null ? 0:list.get(3)) );
                break;
            case "风控大数据研发部":
                list.set(4, workload + (list.get(4) == null ? 0:list.get(4)) );
                break;
            case "互联网金融研发部":
                list.set(5, workload + (list.get(5) == null ? 0:list.get(5)) );
                break;
            case "前端技术研发部":
                list.set(6, workload + (list.get(6) == null ? 0:list.get(6)) );
                break;
            case "基础应用研发部":
                list.set(7, workload + (list.get(7) == null ? 0:list.get(7)) );
                break;
            case "客户端研发部":
                list.set(8, workload + (list.get(8) == null ? 0:list.get(8)) );
                break;
            case "公共缴费研发部":
                list.set(9, workload + (list.get(9) == null ? 0:list.get(9)) );
                break;
            case "产品测试部":
                list.set(10, workload + (list.get(10) == null ? 0:list.get(10)) );
                break;
            case "平台架构部":
                list.set(11, workload + (list.get(11) == null ? 0:list.get(11)) );
                break;
            case "产品研究部":
                list.set(12, workload + (list.get(12) == null ? 0:list.get(12)) );
                break;
            case "设计项目组":
                list.set(13, workload + (list.get(13) == null ? 0:list.get(13)) );
                break;
            case "资金归集项目组":
                list.set(14, workload + (list.get(14) == null ? 0:list.get(14)) );
                break;
            case "客服中间层项目组":
                list.set(15, workload + (list.get(15) == null ? 0:list.get(15)) );
                break;
            case "行业拓展事业部":
                list.set(16, workload + (list.get(16) == null ? 0:list.get(16)) );
                break;
            case "金科项目组":
                list.set(17, workload + (list.get(17) == null ? 0:list.get(17)) );
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
            MsgEnum.ERROR_CUSTOM.setMsgInfo(MsgEnum.ERROR_WORK_IMPORT.getMsgInfo() + "主导部门占比有误请检查！");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        bean.setLeadDeptPro(leadDeptPro);
        //配合部门占比
        String coorDeptPro = bean.getCoorDeptPro();
//        if(coorDeptPro!=null){
//            if (coorDeptNmArr.length != coorDeptRateArr.length) {
//                return ajaxDoneError("配合部门占比有误请检查！");
//            } else {
//                for (int i = 0; i < coorDeptNmArr.length; i++) {
//                    coorDeptPro = coorDeptPro + coorDeptNmArr[i] + ":" + coorDeptRateArr[i] + "%;";
//                }
//            }
//        }
        bean.setCoorDeptPro(coorDeptPro);
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, bean);
        workLoadDao.updateReqWorkLoad(demandDO);
    }
}
