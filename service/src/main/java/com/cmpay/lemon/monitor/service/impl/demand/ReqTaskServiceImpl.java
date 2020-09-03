package com.cmpay.lemon.monitor.service.impl.demand;

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
import com.cmpay.lemon.monitor.bo.jira.CreateIssueResponseBO;
import com.cmpay.lemon.monitor.bo.jira.CreateIssueTestSubtaskRequestBO;
import com.cmpay.lemon.monitor.bo.jira.JiraTaskBodyBO;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.dic.DictionaryService;
import com.cmpay.lemon.monitor.service.jira.JiraOperationService;
import com.cmpay.lemon.monitor.service.reportForm.GenRptService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import com.cmpay.lemon.monitor.utils.ReadExcelUtils;
import com.cmpay.lemon.monitor.utils.jira.JiraUtil;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;
import static com.cmpay.lemon.monitor.utils.FileUtils.importExcel;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * @author: zhou_xiong
 */
@Service
public class ReqTaskServiceImpl implements ReqTaskService {
    //超级管理员
    private static final Long SUPERADMINISTRATOR = (long) 10506;
    //团队主管
    private static final Long SUPERADMINISTRATOR1 = (long) 5004;

    //需求管理员
    private static final Long SUPERADMINISTRATOR2 = (long) 5001;
    //技术负责人
    private static final Long SUPERADMINISTRATOR3 = (long) 5006;
    //产品经理
    private static final Long SUPERADMINISTRATOR4 = (long) 5002;
    //30 需求状态为暂停
    private static final String REQSUSPEND = "30";
    //40 需求状态为取消
    private static final String REQCANCEL = "40";
    // 30 需求定稿
    private static final int REQCONFIRM = 30;
    // 120 UAT版本更新
    private static final int UPDATEUAT = 120;
    // 140 完成UAT测试
    private static final int FINISHUATTEST = 140;
    // 老产品线
    private static final String OLDPRDLINE = "老产品线";
    //  无效问题
    private static final String WUXIAOWENTI = "无效问题";
    private static final Logger LOGGER = LoggerFactory.getLogger(ReqTaskServiceImpl.class);
    @Autowired
    private IDemandExtDao demandDao;
    @Autowired
    private IDictionaryExtDao dictionaryDao;
    @Autowired
    private IDemandStateHistoryExtDao demandStateHistoryDao;
    @Autowired
    private IDemandCurperiodHistoryDao demandCurperiodHistoryDao;
    @Autowired
    private IDemandJiraDao demandJiraDao;
    @Autowired
    private IJiraDepartmentDao jiraDepartmentDao;
    @Autowired
    IDemandJiraSubtaskDao demandJiraSubtaskDao;
    @Autowired
    IDemandJiraDevelopMasterTaskDao demandJiraDevelopMasterTaskDao;
    @Autowired
    IJiraBasicInfoDao jiraBasicInfoDao;
    @Autowired
    private ITPermiDeptDao permiDeptDao;

    @Autowired
    private IPermiUserDao permiUserDao;
    @Autowired
    private IUserRoleExtDao userRoleExtDao;
    @Autowired
    SystemUserService userService;
    @Autowired
    private IDemandChangeDetailsExtDao demandChangeDetailsDao;
    @Autowired
    private IErcdmgErorDao iErcdmgErorDao;
    @Autowired
    private IUserExtDao iUserDao;
    @Autowired
    private IDemandPictureDao iDemandPictureDao;
    @Autowired
    private IDemandNameChangeExtDao iDemandNameChangeExtDao;
    @Autowired
    private IProductionDefectsExtDao productionDefectsDao;
    @Autowired
    ISmokeTestRegistrationExtDao smokeTestRegistrationDao;
    @Autowired
    ISmokeTestFailedCountDao smokeTestFailedCountDao;
    @Autowired
    IWorkingHoursExtDao workingHoursDao;
    @Autowired
    IDemandResourceInvestedDao demandResourceInvestedDao;
    @Autowired
    DictionaryService dictionaryService;
    @Autowired
    IDefectDetailsExtDao defectDetailsDao;
    @Resource
    private GenRptService genRptService;
    @Resource
    private IDemandEaseDevelopmentExtDao easeDevelopmentExtDao;
    /**
     * 自注入,解决getAppsByName中调用findAll的缓存不生效问题
     */
    @Autowired
    private ReqTaskService reqTaskService;

    @Autowired
    private ReqPlanService reqPlanService;

    @Autowired
    private JiraOperationService jiraOperationService;
    @Autowired
    private IOrganizationStructureDao iOrganizationStructureDao;

    @Override
    public DemandBO findById(String req_inner_seq) {
        DemandDO demandDO = demandDao.get(req_inner_seq);
        if (JudgeUtils.isNull(demandDO)) {
            LOGGER.warn("id为[{}]的记录不存在", req_inner_seq);
            BusinessException.throwBusinessException(MsgEnum.DB_FIND_FAILED);
        }
        return BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDO);
    }

    @Override
    public DemandDO findById1(String req_inner_seq) {
        DemandDO demandDO = demandDao.get(req_inner_seq);
        return demandDO;
    }

    @Override
    public List<DemandDO> findById(List<String> ids) {
        LinkedList<DemandDO> demandDOList = new LinkedList<>();
        ids.forEach(m -> {
            DemandDO demandDO = demandDao.get(m);
            if (JudgeUtils.isNotNull(demandDO)) {
                demandDOList.add(demandDO);
            }
        });
        return demandDOList;
    }

    @Override
    public DemandRspBO find(DemandBO demandBO) {
        String time = DateUtil.date2String(new Date(), "yyyy-MM-dd");
        PageInfo<DemandBO> pageInfo = getPageInfo(demandBO);
        List<DemandBO> demandBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandBO.class);

        for (int i = 0; i < demandBOList.size(); i++) {
            if (JudgeUtils.isEmpty(demandBOList.get(i).getIsSvnBuild())) {
                demandBOList.get(i).setIsSvnBuild("否");
            }
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
        demandBOList.forEach(m -> {
            DemandJiraDO demandJiraDO = demandJiraDao.get(m.getReqInnerSeq());
            if (demandJiraDO != null) {
                m.setJiraKey(demandJiraDO.getJiraKey());
            }
        });

        DemandRspBO demandRspBO = new DemandRspBO();
        demandRspBO.setDemandBOList(demandBOList);
        demandRspBO.setPageInfo(pageInfo);
        return demandRspBO;
    }

    private PageInfo<DemandBO> getPageInfo(DemandBO demandBO) {
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        PageInfo<DemandBO> pageInfo = PageUtils.pageQueryWithCount(demandBO.getPageNum(), demandBO.getPageSize(),
                () -> BeanConvertUtils.convertList(demandDao.find(demandDO), DemandBO.class));
        return pageInfo;
    }

    //导出
    @Override
    public void getReqTask(HttpServletResponse response, DemandBO demandBO) {
        List<DemandDO> demandDOList = reqTask(demandBO);

        demandDOList.forEach(m -> {
            List<DemandStateHistoryDO> list = demandStateHistoryDao.getLastRecordByReqInnerSeq(m.getReqInnerSeq());
            if (JudgeUtils.isNotEmpty(list)) {
                m.setRequirementStatusModificationNotes(list.get(0).getRemarks());
            }
        });
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandDO.class, demandDOList);
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // 判断数据
            if (workbook == null) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
            // 设置excel的文件名称
            String excelName = "reqTask_" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }

    private List<DemandDO> reqTask(DemandBO demandBO) {
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        return demandDao.getReqTask(demandDO);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void add(DemandBO demandBO) {
        System.err.println(demandBO);
        // 操作人
        String user = userService.getFullname(SecurityUtils.getLoginName());
        // 操作时间
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //校验数据
        checkReqTask(demandBO);
        //设置时间
        String mon = DateUtil.date2String(new Date(), "yyyy-MM");
        demandBO.setReqImplMon(mon);
        demandBO.setReqStartMon(mon);
        // 判断需求编号或需求名称是否重复
        List<DemandBO> list = reqTaskService.getReqTaskByUK(demandBO);
        if (list.size() > 0) {
            BusinessException.throwBusinessException(MsgEnum.NON_UNIQUE);
        }

        //设置默认值
        //1、“本月期望目标“为”完成产品发布“时，”预计产品发布日期“必须为本月；
        //2、“本月期望目标“为非”完成产品发布“时，”预计产品发布日期“必须是下月之后；
        int year = Integer.parseInt(demandBO.getReqImplMon().substring(0, 4));
        int month = Integer.parseInt(demandBO.getReqImplMon().substring(5, 7));
        String startdata = demandBO.getReqImplMon() + "-01";
        String enddata = demandBO.getReqImplMon() + "-" + getMonthLastDay(year, month);
        if ("180".equals(demandBO.getCurMonTarget())) {
            if (demandBO.getExpPrdReleaseTm().compareTo(startdata) < 0 || demandBO.getExpPrdReleaseTm().compareTo(enddata) > 0) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("本月期望目标为完成产品发布时，预计产品发布日期必须为本月！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        } else {
            if (demandBO.getExpPrdReleaseTm().compareTo(enddata) < 0) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("本月期望目标为非完成产品发布时，预计产品发布日期必须为下月之后！");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }

        demandBO.setReqInnerSeq(reqTaskService.getNextInnerSeq());
        if (JudgeUtils.isEmpty(demandBO.getQaMng())) {
            demandBO.setQaMng("刘桂娟");
        }
        if (JudgeUtils.isEmpty(demandBO.getReqType())) {
            demandBO.setReqType("01");
        }
        if (JudgeUtils.isEmpty(demandBO.getConfigMng())) {
            demandBO.setConfigMng("黄佳海");
        }
        if (JudgeUtils.isEmpty(demandBO.getReqAbnorType())) {
            demandBO.setReqAbnorType("01");
        }
        // 设置是否经理svn默认值 ：否
        if (JudgeUtils.isEmpty(demandBO.getIsSvnBuild())) {
            demandBO.setIsSvnBuild("否");
        }
        setDefaultValue(demandBO);
        setDefaultUser(demandBO);
        setReqSts(demandBO);
        DemandStateHistoryDO demandStateHistoryDO = new DemandStateHistoryDO();
        DemandChangeDetailsDO demandChangeDetailsDO = new DemandChangeDetailsDO();
        try {
            //登记需求表
            demandDao.insert(BeanUtils.copyPropertiesReturnDest(new DemandDO(), demandBO));

            // 登记需求名称及编号变更明细表 ，因为是新增需求，故老编号名称为空
            DemandNameChangeDO demandNameChangeDO = new DemandNameChangeDO();
            // 新内部编号
            demandNameChangeDO.setNewReqInnerSeq(demandBO.getReqInnerSeq());
            // 新名称
            demandNameChangeDO.setNewReqNm(demandBO.getReqNm());
            // 新编号
            demandNameChangeDO.setNewReqNo(demandBO.getReqNo());
            // 操作人
            demandNameChangeDO.setOperator(user);
            // 操作时间
            demandNameChangeDO.setOperationTime(time);
            // 唯一标识
            demandNameChangeDO.setUuid(UUID.randomUUID().toString());
            // 插入数据
            iDemandNameChangeExtDao.insert(demandNameChangeDO);

            String reqInnerSeq = demandDao.getMaxInnerSeq().getReqInnerSeq();
            // 登记需求变更明细表
            demandChangeDetailsDO.setReqInnerSeq(reqInnerSeq);
            demandChangeDetailsDO.setReqNo(demandBO.getReqNo());
            demandChangeDetailsDO.setReqNm(demandBO.getReqNm());
            demandChangeDetailsDO.setCreatUser(user);
            demandChangeDetailsDO.setCreatTime(LocalDateTime.now());
            demandChangeDetailsDO.setIdentification(reqInnerSeq);
            demandChangeDetailsDO.setReqImplMon(demandBO.getReqImplMon());
            demandChangeDetailsDao.insert(demandChangeDetailsDO);
            //登记需求状态表
            demandStateHistoryDO.setReqInnerSeq(reqInnerSeq);
            demandStateHistoryDO.setReqSts("提出");
            demandStateHistoryDO.setRemarks("新建任务");
            demandStateHistoryDO.setReqNm(demandBO.getReqNm());
            demandStateHistoryDO.setReqNo(demandBO.getReqNo());
            //登记需求状态历史表
            //依据内部需求编号查唯一标识
            String identificationByReqInnerSeq = demandChangeDetailsDao.getIdentificationByReqInnerSeq(reqInnerSeq);
            if (identificationByReqInnerSeq == null) {
                identificationByReqInnerSeq = reqInnerSeq;
            }
            demandStateHistoryDO.setIdentification(identificationByReqInnerSeq);
            //获取当前操作员
            demandStateHistoryDO.setCreatUser(user);
            demandStateHistoryDO.setCreatTime(LocalDateTime.now());
            //登记需求状态历史表
            demandStateHistoryDao.insert(demandStateHistoryDO);

            //异步登记jira
            jiraOperationService.createEpic(demandBO);
        } catch (Exception e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void delete(String reqInnerSeq) {
        DemandDO demandDO = demandDao.get(reqInnerSeq);
        if (StringUtils.isNotEmpty(demandDO.getReqNo())) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("已有REQ需求编号的需求，禁止删除");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (permissionCheck(reqInnerSeq)) {
            demandDao.logicDelete(reqInnerSeq);
        } else {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("只有该需求产品经理和开发主导部门部门经理才能删除");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void deleteBatch(List<String> ids) {
        ids.forEach(m -> {
            this.delete(m);
        });
      //  ids.forEach(demandJiraDao::delete);
    }

    /**
     * 得到指定月的天数
     */
    public int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    // 判断是否为角色权限
    public boolean isDepartmentManager(Long juese, Long userid) {
        //查询该操作员角色
        UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setRoleId(juese);
        userRoleDO.setUserNo(userid);
        List<UserRoleDO> userRoleDOS = new LinkedList<>();
        userRoleDOS = userRoleExtDao.find(userRoleDO);
        if (!userRoleDOS.isEmpty()) {
            return true;
        }
        return false;
    }

    // 判断是否为角色权限
    public boolean isDepartmentManager(Long juese) {
        //查询该操作员角色
        UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setRoleId(juese);
        userRoleDO.setUserNo(Long.parseLong(SecurityUtils.getLoginUserId()));
        List<UserRoleDO> userRoleDOS = new LinkedList<>();
        userRoleDOS = userRoleExtDao.find(userRoleDO);
        if (!userRoleDOS.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void update(DemandBO demandBO) {
        // 操作人
        String user = userService.getFullname(SecurityUtils.getLoginName());
        // 操作时间
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        //校验数据
        checkReqTask(demandBO);
        // 如果是超级管理员或者需求管理员，不验证
        if (!isDepartmentManager(SUPERADMINISTRATOR) && !isDepartmentManager(SUPERADMINISTRATOR2)) {
            // 如果项目经理不为空，则判断项目经理是否为技术负责人或团队主管或产品经理，否则报错
            if (StringUtils.isNotBlank(demandBO.getProjectMng())) {
                TPermiUser tPermiUser = iErcdmgErorDao.findByUsername(demandBO.getProjectMng());
                if (tPermiUser == null) {
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("你输入的项目经理名称:" + demandBO.getProjectMng() + "在系统中不存在，请确定后重新输入");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                UserDO userByUserName = iUserDao.getUserByUserName(tPermiUser.getUserId());
                if (!isDepartmentManager(SUPERADMINISTRATOR1, userByUserName.getUserNo()) && !isDepartmentManager(SUPERADMINISTRATOR3, userByUserName.getUserNo()) && !isDepartmentManager(SUPERADMINISTRATOR4, userByUserName.getUserNo())) {
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("项目经理需为产品经理,部门技术负责人或团队主管！");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
            }
        }
        //设置默认值
        setDefaultValue(demandBO);
        setDefaultUser(demandBO);
        setReqSts(demandBO);
        //查询原数据
        DemandDO demandDO = demandDao.get(demandBO.getReqInnerSeq());
        //传入的内部需求编号为REQ前缀,即内部需求编号不为空
        if (!demandBO.getReqInnerSeq().trim().startsWith("REQ")) {
            //校验内部需求编号是否存在
            checkReqNo(demandBO, demandDO);
        }
        try {
            // 判断需求编号，需求名称是否发生变化，如改变则登记需求名称及编号变更明细表
            String oldReqNo = demandDO.getReqNo();
            String oldReqNm = demandDO.getReqNm();
            String newReqNo = demandBO.getReqNo();
            String newReqNm = demandBO.getReqNm();
            if (newReqNo == null) {
                newReqNm = "";
            }
            if (newReqNm == null) {
                newReqNm = "";
            }
            if (oldReqNo == null) {
                oldReqNo = "";
            }
            if (oldReqNm == null) {
                oldReqNm = "";
            }
            if (!oldReqNo.equals(newReqNo) || !oldReqNm.equals(newReqNm)) {
                DemandNameChangeDO demandNameChangeDO = new DemandNameChangeDO();
                // 新内部编号
                demandNameChangeDO.setNewReqInnerSeq(demandDO.getReqInnerSeq());
                // 新名称
                demandNameChangeDO.setNewReqNm(demandDO.getReqNm());
                // 新编号
                demandNameChangeDO.setNewReqNo(demandDO.getReqNo());
                // 操作人
                demandNameChangeDO.setOperator(user);
                // 操作时间
                demandNameChangeDO.setOperationTime(time);
                // 登记表，先查询有无历史记录
                List<DemandNameChangeDO> nameChangeDO = iDemandNameChangeExtDao.findOne(demandNameChangeDO);
                // 如果nameChangeDO为空
                if (nameChangeDO == null || nameChangeDO.size() == 0) {
                    // 新内部编号
                    demandNameChangeDO.setNewReqInnerSeq(demandBO.getReqInnerSeq());
                    // 新名称
                    demandNameChangeDO.setNewReqNm(demandBO.getReqNm());
                    // 新编号
                    demandNameChangeDO.setNewReqNo(demandBO.getReqNo());
                    // 老内部编号
                    demandNameChangeDO.setOldReqInnerSeq(demandDO.getReqInnerSeq());
                    // 老名称
                    demandNameChangeDO.setOldReqNm(demandDO.getReqNm());
                    // 老编号
                    demandNameChangeDO.setOldReqNo(demandDO.getReqNo());
                    // 唯一标识
                    demandNameChangeDO.setUuid(UUID.randomUUID().toString());
                } else {
                    // 新内部编号
                    demandNameChangeDO.setNewReqInnerSeq(demandBO.getReqInnerSeq());
                    // 新名称
                    demandNameChangeDO.setNewReqNm(demandBO.getReqNm());
                    // 新编号
                    demandNameChangeDO.setNewReqNo(demandBO.getReqNo());
                    // 老内部编号
                    demandNameChangeDO.setOldReqInnerSeq(nameChangeDO.get(nameChangeDO.size() - 1).getNewReqInnerSeq());
                    // 老名称
                    demandNameChangeDO.setOldReqNm(nameChangeDO.get(nameChangeDO.size() - 1).getNewReqNm());
                    // 老编号
                    demandNameChangeDO.setOldReqNo(nameChangeDO.get(nameChangeDO.size() - 1).getNewReqNo());
                    // 唯一标识
                    demandNameChangeDO.setUuid(nameChangeDO.get(nameChangeDO.size() - 1).getUuid());
                }
                // 插入
                iDemandNameChangeExtDao.insert(demandNameChangeDO);

            }
            //如果修改了需求节点计划时间
            if (demandBO.getRevisionTimeNote() != null && !demandBO.getRevisionTimeNote().isEmpty()) {
                reqPlanService.registrationTimeNodeHistoryTable(demandBO);
            }
            //如果修改了需求当前阶段
            if (!demandBO.getPreCurPeriod().equals(demandDO.getPreCurPeriod())) {
                //登记需求阶段记录表
                String remarks = "手动修改";
                reqPlanService.registrationDemandPhaseRecordForm(demandBO, remarks);
            }
            // 这个数值为int类型，该操作不会对其产生修改，但默认新对象数值为0，搜索并赋值保证不会变化
            //总工作量
            demandBO.setTotalWorkload(demandDO.getTotalWorkload());
            //已录入总工作量
            demandBO.setInputWorkload(demandDO.getInputWorkload());
            //上月录入
            demandBO.setLastInputWorkload(demandDO.getLastInputWorkload());
            //剩余录入工作量
            demandBO.setRemainWorkload(demandDO.getRemainWorkload());
            //本月录入工作量
            demandBO.setMonInputWorkload(demandDO.getMonInputWorkload());

            demandDao.update(BeanUtils.copyPropertiesReturnDest(demandDO, demandBO));
        } catch (Exception e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.DB_UPDATE_FAILED);
        }
        jiraOperationService.createEpic(demandBO);
    }

    private void checkReqNo(DemandBO demandBO, DemandDO demandDO) {
        //判断需求编号是否修改,若已经修改则查询修改后的内部需求编号是否已经被使用
        if (!demandBO.getReqNo().trim().equals(demandDO.getReqNo().trim())) {
            DemandDO demandDO1 = new DemandDO();
            demandDO1.setReqImplMon(demandBO.getReqImplMon());
            demandDO1.setReqNo(demandBO.getReqNo());
            List<DemandDO> demandDOS = demandDao.getReqTaskByNo(demandDO1);
            if (!demandDOS.isEmpty()) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("该内部需求编号有重复，请确认后输入!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
    }

    @Override
    public List<DemandBO> findAll() {
        return BeanConvertUtils.convertList(demandDao.find(new DemandDO()), DemandBO.class);
    }

    @Override
    public List<DemandBO> getReqTaskByUK(DemandBO demandBO) {
        DemandDO demandDO = new DemandDO();
        BeanUtils.copyPropertiesReturnDest(demandDO, demandBO);
        return BeanConvertUtils.convertList(demandDao.getReqTaskByUK(demandDO), DemandBO.class);
    }

    @Override
    public Boolean checkNumber(String req_no) {
        boolean bool = false;
        String[] reqNo = req_no.split("-");
        if (reqNo.length == 3) {
            if ((("REQ".equals(reqNo[0]) || "REQJIRA".equals(reqNo[0]))
                    && reqNo[1].matches("^\\d{8}$") && reqNo[2].matches("^\\d{4,5}$"))) {
                bool = true;
            }
        }
        return bool;
    }

    /**
     * 默认值设置
     *
     * @param demandBO
     */
    public void setDefaultValue(DemandBO demandBO) {
        // 计划投入数 =投入资源*开发周期/22
//        int inpoyRes = demandBO.getInputRes();
//        int devCycle = demandBO.getDevCycle();
//        Double expInput = new BigDecimal(new Double(inpoyRes) * new Double(devCycle))
//                .divide(new BigDecimal(String.valueOf(22)), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
//        //Double expInput = (double) (inpoyRes * devCycle / 22);
//        demandBO.setExpInput(expInput);
    }

    /**
     * 设置用户
     *
     * @param demandBO
     */
    public void setDefaultUser(DemandBO demandBO) {
        if (StringUtils.isBlank(demandBO.getCreatUser())) {
            demandBO.setCreatUser(SecurityUtils.getLoginName());
            demandBO.setCreatTime(new Date());
        }
        demandBO.setUpdateUser(SecurityUtils.getLoginName());
        demandBO.setUpdateTime(new Date());
    }

    /**
     * 变更需求状态
     *
     * @param demandBO
     */
    public void setReqSts(DemandBO demandBO) {
        if (!"30".equals(demandBO.getReqSts()) && !"40".equals(demandBO.getReqSts())) {
            //修改需求状态
            if ("10".equals(demandBO.getPreCurPeriod())) {
                //提出
                demandBO.setReqSts("10");
            } else if ("180".equals(demandBO.getPreCurPeriod())) {
                //完成
                demandBO.setReqSts("50");
            } else {
                //进行中
                demandBO.setReqSts("20");
            }
        }
    }

    /**
     * 校验需求
     *
     * @param demandBO
     */
    public void checkReqTask(DemandBO demandBO) {
        //判断编号是否规范
        String reqNo = demandBO.getReqNo();
        if (StringUtils.isNotBlank(reqNo) && !reqTaskService.checkNumber(reqNo)) {
            BusinessException.throwBusinessException(MsgEnum.ERROR_REQ_NO);
        }

        //如果选择取消或暂停，月初备注不能为空
        if ("30".equals(demandBO.getReqSts()) && StringUtils.isBlank(demandBO.getMonRemark())
                || "40".equals(demandBO.getReqSts()) && StringUtils.isBlank(demandBO.getMonRemark())) {
            BusinessException.throwBusinessException(MsgEnum.NULL_REMARK);
        }

        //主导部门和配合部门不能相同
        if (StringUtils.isNotBlank(demandBO.getDevpCoorDept())) {
            String[] devp_coor_dept = demandBO.getDevpCoorDept().split(",");
            for (int i = 0; i < devp_coor_dept.length; i++) {
                if (devp_coor_dept[i].equals(demandBO.getDevpLeadDept())) {
                    BusinessException.throwBusinessException(MsgEnum.ERROR_DEVP);
                }
            }
        }
    }


    @Override
    public String getNextInnerSeq() {
        // 从最大的值往后排
        DemandBO reqTask = getMaxInnerSeq();
        if (reqTask == null) {
            return "XQ00000001";
        } else {
            String maxInnerSeq = reqTask.getReqInnerSeq();
            if (StringUtils.isBlank(maxInnerSeq)) {
                return "XQ00000001";
            } else {
                int nextSeq = Integer.parseInt(maxInnerSeq.substring(2)) + 1;
                String innerSeq = StringUtils.leftPad(String.valueOf(nextSeq), 8, "0");
                return "XQ" + innerSeq;
            }
        }
    }

    @Override
    public DemandBO getMaxInnerSeq() {
        DemandDO demandDO = null;
        demandDO = demandDao.getMaxInnerSeq();
        if (demandDO != null) {
            return BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDO);
        } else {
            return null;
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void doBatchImport(MultipartFile file) {
        // 操作人
        String user = userService.getFullname(SecurityUtils.getLoginName());
        // 操作时间
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        File f = null;
        List<DemandDO> demandDOS = new ArrayList<>();
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
                DemandDO demandDO = new DemandDO();
                demandDO.setReqProDept(map.get(i).get(0).toString().trim());
                demandDO.setReqProposer(map.get(i).get(1).toString().trim());
                demandDO.setReqMnger(map.get(i).get(2).toString().trim());
                demandDO.setReqPrdLine(map.get(i).get(3).toString().trim());
                demandDO.setReqNm(map.get(i).get(4).toString().trim());
                demandDO.setReqDesc(map.get(i).get(5).toString().trim());
                if (!JudgeUtils.isEmpty(map.get(i).get(6).toString().trim())) {
                    demandDO.setExpInput(Double.parseDouble(map.get(i).get(6).toString().trim()));
                }
                if (!JudgeUtils.isEmpty(map.get(i).get(7).toString().trim())) {
                    demandDO.setProjectedWorkload((int)Double.parseDouble(map.get(i).get(7).toString().trim()));
                }
                demandDO.setIsCut(map.get(i).get(8).toString().trim());
                demandDO.setMonRemark(map.get(i).get(9).toString().trim());
                demandDO.setExpPrdReleaseTm(map.get(i).get(10).toString().trim());
                demandDO.setPreMonPeriod(map.get(i).get(11).toString().trim());
                demandDO.setCurMonTarget(map.get(i).get(12).toString().trim());
                demandDO.setReqInnerSeq(map.get(i).get(13).toString().trim());
                demandDO.setReqNo(map.get(i).get(14).toString().trim());
                demandDO.setRiskFeedbackTm(map.get(i).get(15).toString().trim());
                demandDO.setPreCurPeriod(map.get(i).get(16).toString().trim());
                demandDO.setRiskSolution(map.get(i).get(17).toString().trim());
                demandDO.setPrdFinshTm(map.get(i).get(18).toString().trim());
                demandDO.setUatUpdateTm(map.get(i).get(19).toString().trim());
                demandDO.setDevpLeadDept(map.get(i).get(20).toString().trim());
                demandDO.setDevpCoorDept(map.get(i).get(21).toString().trim());
                demandDO.setProductMng(map.get(i).get(22).toString().trim());
                demandDO.setReqStartMon(map.get(i).get(23).toString().trim());
                demandDO.setReqImplMon(map.get(i).get(24).toString().trim());
                demandDOS.add(demandDO);
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
        //  List<DemandDO> demandDOS = importExcel(file, 0, 1, DemandDO.class);
        List<DemandDO> insertList = new ArrayList<>();
        List<DemandDO> updateList = new ArrayList<>();
        demandDOS.forEach(m -> {
            int i = demandDOS.indexOf(m) + 2;
            if (StringUtils.isBlank(m.getReqProDept())) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("第" + i + "行" + "产品管理部门不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            if (StringUtils.isBlank(m.getReqNm())) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("第" + i + "行" + "需求名称不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            if (StringUtils.isBlank(m.getReqDesc())) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("第" + i + "行" + "需求描述不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            if (StringUtils.isBlank(m.getReqProposer())) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("第" + i + "行" + "需求提出人不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            if (StringUtils.isBlank(m.getReqMnger())) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("第" + i + "行" + "产品负责人不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            if (StringUtils.isBlank(m.getReqPrdLine())) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("第" + i + "行" + "产品名称不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            if (StringUtils.isBlank(m.getReqStartMon())) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("第" + i + "行" + "启动月份不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            if (StringUtils.isBlank(m.getReqImplMon())) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("第" + i + "行" + "实施月份不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }

            //判断编号是否规范
            String reqNo = m.getReqNo();
            if (StringUtils.isNotBlank(reqNo) && !reqTaskService.checkNumber(reqNo)) {
                BusinessException.throwBusinessException(MsgEnum.ERROR_REQ_NO);
            }

            DictionaryDO dictionaryDO = new DictionaryDO();
            dictionaryDO.setDicId("PRD_LINE");
            dictionaryDO.setValue(m.getReqPrdLine());
            List<DictionaryDO> dic = dictionaryDao.getDicByDicId(dictionaryDO);
            if (dic.size() == 0) {
                MsgEnum.ERROR_IMPORT.setMsgInfo("第" + i + "行" + "产品名称字典项不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
            if (OLDPRDLINE.equals(dic.get(0).getRemark())) {
                MsgEnum.ERROR_IMPORT.setMsgInfo("第" + i + "行" + "产品名称为老产品线名称已经废弃，请选择新的产品线名称");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
            m.setReqPrdLine(dic.get(0).getName());

            dictionaryDO.setUserName(m.getReqProposer());
            dic = dictionaryDao.getJdInfo(dictionaryDO);
            if (dic.size() == 0) {
                MsgEnum.ERROR_IMPORT.setMsgInfo("第" + i + "行" + "需求提出人不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }

            dictionaryDO.setUserName(m.getReqMnger());
            dic = dictionaryDao.getJdInfo(dictionaryDO);
            if (dic.size() == 0) {
                MsgEnum.ERROR_IMPORT.setMsgInfo("第" + i + "行" + "产品负责人不存在");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }

            if (StringUtils.isNotBlank(m.getPreCurPeriod())) {
                dictionaryDO.setDicId("REQ_PEROID");
                dictionaryDO.setValue(m.getPreCurPeriod());
                dic = dictionaryDao.getDicByDicId(dictionaryDO);
                if (dic.size() == 0) {
                    MsgEnum.ERROR_IMPORT.setMsgInfo("第" + i + "行" + "最新进展字典项不存在");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
                }
                m.setPreCurPeriod(dic.get(0).getName());
            }


            if (StringUtils.isNotBlank(m.getPreMonPeriod())) {
                dictionaryDO.setDicId("REQ_PEROID");
                dictionaryDO.setValue(m.getPreMonPeriod());
                dic = dictionaryDao.getDicByDicId(dictionaryDO);
                if (dic.size() == 0) {
                    MsgEnum.ERROR_IMPORT.setMsgInfo("第" + i + "行" + "月初需求阶段字典项不存在");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
                }
                m.setPreMonPeriod(dic.get(0).getName());
            }
            m.setPreCurPeriod(StringUtils.isBlank(m.getPreCurPeriod()) ? m.getPreMonPeriod() : m.getPreCurPeriod());
            m.setPreMonPeriod(StringUtils.isBlank(m.getPreMonPeriod()) ? m.getPreCurPeriod() : m.getPreMonPeriod());

            if (StringUtils.isNotBlank(m.getCurMonTarget())) {
                dictionaryDO.setDicId("REQ_PEROID");
                dictionaryDO.setValue(m.getCurMonTarget());
                dic = dictionaryDao.getDicByDicId(dictionaryDO);
                if (dic.size() == 0) {
                    MsgEnum.ERROR_IMPORT.setMsgInfo("第" + i + "行" + "本月预计完成阶段字典项不存在");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
                }
                m.setCurMonTarget(dic.get(0).getName());
            }

            DemandBO tmp = new DemandBO();
            BeanUtils.copyPropertiesReturnDest(tmp, m);
            setReqSts(tmp);
            setDefaultUser(tmp);
            BeanUtils.copyPropertiesReturnDest(m, tmp);

            List<DemandDO> dem = demandDao.getReqTaskByUKImpl(m);
            if (dem.size() == 0) {
                // 默认设置需求状态为新增
                m.setReqType("01");
                // 默认设置qa人员为刘桂娟
                m.setQaMng("刘桂娟");
                // 默认设置配置人员为黄佳海
                m.setConfigMng("黄佳海");
                m.setReqAbnorType("01");
                //设置默认值
                //插入SVN为否
                m.setIsSvnBuild("否");
                m.setReqSts("10");
                insertList.add(m);
            } else {
                //设置默认值
                m.setReqInnerSeq(dem.get(0).getReqInnerSeq());
                m.setReqStartMon("");
                updateList.add(m);
            }


        });

        try {
            // 插入数据库
            insertList.forEach(m -> {
                //获取下一条内部编号
                String nextInnerSeq = getNextInnerSeq();
                String reqInnerSeq = m.getReqInnerSeq();
                m.setReqInnerSeq(nextInnerSeq);
                demandDao.insert(m);
                // 登记需求名称及编号变更明细表 ，因为是新增需求，故老编号名称为空
                DemandNameChangeDO demandNameChangeDO = new DemandNameChangeDO();
                // 新内部编号
                demandNameChangeDO.setNewReqInnerSeq(m.getReqInnerSeq());
                // 新名称
                demandNameChangeDO.setNewReqNm(m.getReqNm());
                // 新编号
                demandNameChangeDO.setNewReqNo(m.getReqNo());
                // 操作人
                demandNameChangeDO.setOperator(user);
                // 操作时间
                demandNameChangeDO.setOperationTime(time);
                // 唯一标识
                demandNameChangeDO.setUuid(UUID.randomUUID().toString());
                // 插入数据
                iDemandNameChangeExtDao.insert(demandNameChangeDO);

                // 登记需求变更明细表
                DemandChangeDetailsDO demandChangeDetailsDO = new DemandChangeDetailsDO();
                demandChangeDetailsDO.setReqInnerSeq(m.getReqInnerSeq());
                demandChangeDetailsDO.setReqNo(m.getReqNo());
                demandChangeDetailsDO.setReqNm(m.getReqNm());
                demandChangeDetailsDO.setCreatUser(user);
                demandChangeDetailsDO.setCreatTime(LocalDateTime.now());
                demandChangeDetailsDO.setIdentification(nextInnerSeq);
                demandChangeDetailsDO.setReqImplMon(m.getReqImplMon());
                demandChangeDetailsDao.insert(demandChangeDetailsDO);
                DemandStateHistoryDO demandStateHistoryDO = new DemandStateHistoryDO();
                demandStateHistoryDO.setReqInnerSeq(nextInnerSeq);
                demandStateHistoryDO.setReqSts("提出");
                demandStateHistoryDO.setRemarks("新建任务");
                demandStateHistoryDO.setReqNm(m.getReqNm());
                //依据内部需求编号查唯一标识
                String identificationByReqInnerSeq = demandChangeDetailsDao.getIdentificationByReqInnerSeq(reqInnerSeq);
                if (identificationByReqInnerSeq == null) {
                    identificationByReqInnerSeq = reqInnerSeq;
                }
                demandStateHistoryDO.setIdentification(identificationByReqInnerSeq);
                //登记需求状态历史表
                //获取当前操作员
                demandStateHistoryDO.setCreatUser(user);
                demandStateHistoryDO.setCreatTime(LocalDateTime.now());
                demandStateHistoryDao.insert(demandStateHistoryDO);
            });

            // 更新数据库
            updateList.forEach(m -> {
                DemandDO demandDO = demandDao.get(m.getReqInnerSeq());
                // 这个数值为int类型，该操作不会对其产生修改，但默认新对象数值为0，搜索并赋值保证不会变化
                //总工作量
                m.setTotalWorkload(demandDO.getTotalWorkload());
                //已录入总工作量
                m.setInputWorkload(demandDO.getInputWorkload());
                //上月录入
                m.setLastInputWorkload(demandDO.getLastInputWorkload());
                //剩余录入工作量
                m.setRemainWorkload(demandDO.getRemainWorkload());
                //本月录入工作量
                m.setMonInputWorkload(demandDO.getMonInputWorkload());
                //人月三个字段赋值  投入资源setInputRes  开发周期setDevCycle  人月setExpInput
                m.setInputRes(demandDO.getInputRes());
                m.setDevCycle(demandDO.getDevCycle());
                m.setExpInput(demandDO.getExpInput());
                // 判断需求编号，需求名称是否发生变化，如改变则登记需求名称及编号变更明细表
                String oldReqNo = demandDO.getReqNo();
                String oldReqNm = demandDO.getReqNm();
                String newReqNo = m.getReqNo();
                String newReqNm = m.getReqNm();
                if (newReqNo == null) {
                    newReqNm = "";
                }
                if (newReqNm == null) {
                    newReqNm = "";
                }
                if (oldReqNo == null) {
                    oldReqNo = "";
                }
                if (oldReqNm == null) {
                    oldReqNm = "";
                }
                if (!oldReqNo.equals(newReqNo) || !oldReqNm.equals(newReqNm)) {
                    DemandNameChangeDO demandNameChangeDO = new DemandNameChangeDO();
                    // 新内部编号
                    demandNameChangeDO.setNewReqInnerSeq(demandDO.getReqInnerSeq());
                    // 新名称
                    demandNameChangeDO.setNewReqNm(demandDO.getReqNm());
                    // 新编号
                    demandNameChangeDO.setNewReqNo(demandDO.getReqNo());
                    // 操作人
                    demandNameChangeDO.setOperator(user);
                    // 操作时间
                    demandNameChangeDO.setOperationTime(time);
                    // 登记表，先查询有无历史记录
                    List<DemandNameChangeDO> nameChangeDO = iDemandNameChangeExtDao.findOne(demandNameChangeDO);
                    // 如果nameChangeDO为空
                    if (JudgeUtils.isNull(nameChangeDO)) {
                        // 新内部编号
                        demandNameChangeDO.setNewReqInnerSeq(m.getReqInnerSeq());
                        // 新名称
                        demandNameChangeDO.setNewReqNm(m.getReqNm());
                        // 新编号
                        demandNameChangeDO.setNewReqNo(m.getReqNo());
                        // 老内部编号
                        demandNameChangeDO.setOldReqInnerSeq(demandDO.getReqInnerSeq());
                        // 老名称
                        demandNameChangeDO.setOldReqNm(demandDO.getReqNm());
                        // 老编号
                        demandNameChangeDO.setOldReqNo(demandDO.getReqNo());
                        // 唯一标识
                        demandNameChangeDO.setUuid(UUID.randomUUID().toString());
                    } else {
                        // 新内部编号
                        demandNameChangeDO.setNewReqInnerSeq(m.getReqInnerSeq());
                        // 新名称
                        demandNameChangeDO.setNewReqNm(m.getReqNm());
                        // 新编号
                        demandNameChangeDO.setNewReqNo(m.getReqNo());
                        // 老内部编号
                        demandNameChangeDO.setOldReqInnerSeq(nameChangeDO.get(nameChangeDO.size() - 1).getNewReqInnerSeq());
                        // 老名称
                        demandNameChangeDO.setOldReqNm(nameChangeDO.get(nameChangeDO.size() - 1).getNewReqNm());
                        // 老编号
                        demandNameChangeDO.setOldReqNo(nameChangeDO.get(nameChangeDO.size() - 1).getNewReqNo());
                        // 唯一标识
                        demandNameChangeDO.setUuid(nameChangeDO.get(nameChangeDO.size() - 1).getUuid());
                    }
                    // 插入
                    iDemandNameChangeExtDao.insert(demandNameChangeDO);

                }
                demandDao.update(m);
            });

        } catch (Exception e) {
            LOGGER.error("需求记录导入失败", e);
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }

        jiraOperationService.batchCreateEpic(demandDOS);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void doBatchDown(MultipartHttpServletRequest request, HttpServletResponse response) {
        MultipartFile file = request.getFile(FILE);
        //response.resetBuffer();
        response.reset();
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            Map<String, Object> resMap = BatchDown(file);

            File srcfile[] = (File[]) resMap.get("srcfile");
            //压缩包名称
            String zipPath = "";
            if (LemonUtils.getEnv().equals(Env.SIT)) {
                zipPath = "/home/devms/temp/propkg/";
            } else if (LemonUtils.getEnv().equals(Env.DEV)) {
                zipPath = "/home/devadm/temp/propkg/";
            } else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            String zipName = DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".zip";
            //压缩文件
            File zip = new File(zipPath + zipName);
            reqTaskService.ZipFiles(srcfile, zip, true);
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + zipName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
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
            int len = 0 ;
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(zip);
            while ((len = fis.read(buffer)) > 0) {
                output.write(buffer, 0, len);
                output.flush();
            }
           // output.write(org.apache.commons.io.FileUtils.readFileToByteArray(zip));
            bufferedOutPut.flush();

            // 删除文件
            zip.delete();
        } catch (Exception e) {
            LOGGER.error("下载失败：", e);
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }

    private Map<String, Object> BatchDown(MultipartFile file) {
        List<DemandDO> demandDOS = importExcel(file, 0, 1, DemandDO.class);
        List<DemandDO> List = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        demandDOS.forEach(m -> {
            if (StringUtils.isBlank(m.getReqNo())) {
                MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo() + "需求编号不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }
            if (StringUtils.isBlank(m.getReqNm())) {
                MsgEnum.ERROR_IMPORT.setMsgInfo(MsgEnum.ERROR_IMPORT.getMsgInfo() + "需求名称不能为空");
                BusinessException.throwBusinessException(MsgEnum.ERROR_IMPORT);
            }

            //判断编号是否规范
            String reqNo = m.getReqNo();
            if (StringUtils.isNotBlank(reqNo) && !reqTaskService.checkNumber(reqNo)) {
                BusinessException.throwBusinessException(MsgEnum.ERROR_REQ_NO);
            }

            int start = m.getReqNo().indexOf("-") + 1;
            String reqMonth = m.getReqNo().substring(start, start + 6);
            m.setReqStartMon(reqMonth);
            List.add(m);
        });

        //循环文件目录
        if (List != null) {
            File srcfile[] = new File[List.size() * 5];
            int num = 0;
            //要压缩的文件
            for (int i = 0; i < List.size(); i++) {
                //需求说明书、技术方案、原子功能点评估表
                String path = "";
                if (LemonUtils.getEnv().equals(Env.SIT)) {
                    path = "/home/devms/temp/Projectdoc/" + List.get(i).getReqStartMon() + "/"
                            //  String path = "D:\\home\\devadm\\temp\\Projectdoc" + List.get(i).getReqStartMon() + "/"
                            + List.get(i).getReqNo() + "_" + List.get(i).getReqNm();
                } else if (LemonUtils.getEnv().equals(Env.DEV)) {
                    path = "/home/devadm/temp/Projectdoc/" + List.get(i).getReqStartMon() + "/"
                            //  String path = "D:\\home\\devadm\\temp\\Projectdoc" + List.get(i).getReqStartMon() + "/"
                            + List.get(i).getReqNo() + "_" + List.get(i).getReqNm();
                } else {
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                File file1 = new File(path + "/开发技术文档/");
                if (!file1.exists() && !file1.isDirectory()) {
                    file1.mkdir();
                }
                File[] tempFile1 = file1.listFiles();
                if (tempFile1 == null) {
                    tempFile1 = new File[0];
                }
                for (int j = 0; j < tempFile1.length; j++) {
                    if (tempFile1[j].getName().contains("原子功能点评估表(电子工单)") || tempFile1[j].getName().contains("技术方案说明书")) {
                        srcfile[num] = new File(path + "/开发技术文档/" + tempFile1[j].getName());
                        num++;
                    }
                }

                File file2 = new File(path + "/产品文档/");
                if (!file2.exists() && !file2.isDirectory()) {
                    file2.mkdir();
                }
                File[] tempFile2 = file2.listFiles();
                if (tempFile2 == null) {
                    tempFile2 = new File[0];
                }
                for (int j = 0; j < tempFile2.length; j++) {
                    if (tempFile2[j].getName().contains("需求方案说明书")) {
                        srcfile[num] = new File(path + "/产品文档/" + tempFile2[j].getName());
                        num++;
                    }
                }

            }
            map.put("srcfile", srcfile);
        }

        return map;
    }

    /**
     *  文件压缩
     */
    @Override
    public String ZipFiles(File[] srcfile, File zipfile, boolean flag) {
        try {
            byte[] buf = new byte[1024];
            FileOutputStream fileOutputStream = new FileOutputStream(zipfile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream out = new ZipOutputStream(cos);

            for (int i = 0; i < srcfile.length; i++) {
                if (srcfile[i] != null) {
                    FileInputStream in = new FileInputStream(srcfile[i]);
                    if (flag) {
                        String demandName = "";
                        if (LemonUtils.getEnv().equals(Env.SIT)) {
                            demandName = srcfile[i].getPath().substring(35, srcfile[i].getPath().length());
                        } else if (LemonUtils.getEnv().equals(Env.DEV)) {
                            demandName = srcfile[i].getPath().substring(36, srcfile[i].getPath().length());
                        } else {
                            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                        }
                        String name = demandName.substring(0, demandName.indexOf("/"));
                        String path = demandName.substring(demandName.lastIndexOf("/") + 1);
                        out.putNextEntry(new ZipEntry(name + "/" + path));
                    } else {
                        out.putNextEntry(new ZipEntry(srcfile[i].getPath()));
                    }

                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.closeEntry();
                    in.close();
                }
            }
            out.close();
        } catch (IOException e) {
            LOGGER.error("压缩失败：", e);
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }

        return "";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateReqSts(String reqInnerSeq, String reqNo, String reqSts, String reqStsRemarks, String reqNm) {
        if (!permissionCheck(reqInnerSeq)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("只有该需求产品经理和开发主导部门部门经理才能进行操作");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (JudgeUtils.isEmpty(reqInnerSeq) || JudgeUtils.isEmpty(reqSts)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("输入数据不能为空");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        DemandDO demandDO1 = demandDao.get(reqInnerSeq);
        if (demandDO1.getReqSts().equals("50")) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("需求状态为已完成时，不允许修改需求状态");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (reqSts.equals("50")) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("不允许手动将状态变更为已完成");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (demandDO1.getReqSts().equals(reqSts)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("状态并未修改，请确定修改状态选项后再执行此操作");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        demandDO1.getReqSts();


        DemandDO demandDO = new DemandDO();
        demandDO.setReqInnerSeq(reqInnerSeq);
        demandDO.setReqSts(reqSts);
        demandDao.updateReqSts(demandDO);
        DemandStateHistoryDO demandStateHistoryDO = new DemandStateHistoryDO();
        demandStateHistoryDO.setReqNm(reqNm);
        demandStateHistoryDO.setReqInnerSeq(reqInnerSeq);
        demandStateHistoryDO.setRemarks(reqStsRemarks);
        demandStateHistoryDO.setOldReqSts(reqStsCheck(demandDO1.getReqSts()));
        reqSts = reqStsCheck(reqSts);
        demandStateHistoryDO.setReqSts(reqSts);
        demandStateHistoryDO.setReqNo(reqNo);
        demandStateHistoryDO.setCreatTime(LocalDateTime.now());
        //依据内部需求编号，同一需求查统一标识
        String identificationByReqInnerSeq = demandChangeDetailsDao.getIdentificationByReqInnerSeq(reqInnerSeq);
        if (identificationByReqInnerSeq == null) {
            identificationByReqInnerSeq = reqInnerSeq;
        }
        demandStateHistoryDO.setIdentification(identificationByReqInnerSeq);
        //获取当前操作员
        demandStateHistoryDO.setCreatUser(userService.getFullname(SecurityUtils.getLoginName()));
        demandStateHistoryDao.insert(demandStateHistoryDO);
    }


    @Override
    public String reqStsCheck(String reqSts) {
        switch (reqSts) {
            case "10": {
                reqSts = "提出";
                break;
            }
            case "20": {
                reqSts = "进行中";
                break;
            }
            case "30": {
                reqSts = "取消";
                break;
            }
            case "40": {
                reqSts = "暂停";
                break;
            }
            case "50": {
                reqSts = "已完成";
                break;
            }
            default: {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("状态码异常!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }
        return reqSts;
    }

    @Override
    public void updatePreCurPeriod(DemandBO demand) {
        DemandDO demandDO = BeanUtils.copyPropertiesReturnDest(new DemandDO(), demand);
        demandDao.updatePreCurPeriod(demandDO);
    }

    //判断是否是该项目产品经理或者部门经理
    public boolean permissionCheck(String reqInnerSeq) {
        //查询该操作员是否为超级管理员
        UserRoleDO userRoleDO = new UserRoleDO();
        userRoleDO.setRoleId(SUPERADMINISTRATOR);
        userRoleDO.setUserNo(Long.parseLong(SecurityUtils.getLoginUserId()));
        List<UserRoleDO> userRoleDOS = new LinkedList<>();
        userRoleDOS = userRoleExtDao.find(userRoleDO);
        if (!userRoleDOS.isEmpty()) {
            return true;
        }

        //当前操作员姓名
        PermiUserDO permiUserDO = new PermiUserDO();
        String loginName = SecurityUtils.getLoginName();
        permiUserDO.setUserId(loginName);
        List<PermiUserDO> permiUserDOS = permiUserDao.find(permiUserDO);
        if (permiUserDOS.isEmpty()) {
            return false;
        }
        String userName = permiUserDOS.get(0).getUserName();
        //依据内部需求编号查内部需求
        DemandDO demandDO = demandDao.get(reqInnerSeq);
        //获得产品经理，获得开发主导部门部门经理
        String productMng = demandDO.getProductMng();
        String devpLeadDept = demandDO.getDevpLeadDept();
        TPermiDeptDO permiDeptDO = new TPermiDeptDO();
        //根据操作人姓名查询组织表的一级团队负责人
        OrganizationStructureDO organizationStructureDO = new OrganizationStructureDO();
        organizationStructureDO.setSecondlevelorganization(devpLeadDept);
        organizationStructureDO.setFirstlevelorganizationleader(userName);
        List<OrganizationStructureDO> organizationStructureDOList =  iOrganizationStructureDao.find(organizationStructureDO);
        if (organizationStructureDOList!=null && organizationStructureDOList.size()>0){
            //当前操作人为该需求主导部门的一级团队负责人
            return true;
        }

        //判断开发主导部门和部门经理不为空
        if (!(StringUtils.isBlank(devpLeadDept) && (StringUtils.isBlank(productMng)))) {
            //有产品经理，无主导部门,判断该操作员是否是该产品经理
            if (!StringUtils.isBlank(productMng) && (StringUtils.isBlank(devpLeadDept))) {


                if (productMng.equals(userName)) {
                    return true;
                } else {
                    return false;
                }
            }
            //有主导部门，无产品经理,判断该操作员是否是该开发部门项目经理 devpLeadDept
            if (!StringUtils.isBlank(devpLeadDept) && (StringUtils.isBlank(productMng))) {
                permiDeptDO.setDeptName(devpLeadDept);

                //获得开发主导部门查询该部门部门经理
                List<TPermiDeptDO> tPermiDeptDOS = permiDeptDao.find(permiDeptDO);
                String deptManagerName = tPermiDeptDOS.get(0).getDeptManagerName();
                if (deptManagerName.equals(userName)) {
                    return true;
                } else {
                    return false;
                }
            }
            //有主导部门，产品经理,判断该操作员是否是该开发部门项目经理 或产品经理
            if (!StringUtils.isBlank(productMng) && (!StringUtils.isBlank(devpLeadDept))) {
                permiDeptDO.setDeptName(devpLeadDept);
                //获得开发主导部门查询该部门部门经理
                List<TPermiDeptDO> tPermiDeptDOS = permiDeptDao.find(permiDeptDO);
                String deptManagerName = tPermiDeptDOS.get(0).getDeptManagerName();
                if (deptManagerName.equals(userName) || productMng.equals(userName)) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return false;
    }

    @Override
    public List<String> lists(DemandBO demand) {
        DemandDO demandDO = BeanUtils.copyPropertiesReturnDest(new DemandDO(), demand);
        Map<String, Object> resMap = BatLists(demandDO);
        File srcfile[] = (File[]) resMap.get("srcfile");
        String list[] = new String[srcfile.length];
        List<String> li = new ArrayList<>();
        for (int i = 0; i < srcfile.length; i++) {
            if (srcfile[i] != null) {
                System.err.println(srcfile[i].getName());
                li.add(srcfile[i].getName());
            }
        }
        if (li.size() == 0) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("项目已启动,未上传文档!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        return li;
    }

    @Override
    public DemandStateHistoryRspBO findDemandChangeDetails(DemandChangeDetailsBO demandChangeDetailsBO) {
        if (!demandChangeDetailsBO.getReqInnerSeq().isEmpty() && !demandChangeDetailsBO.getReqNo().isEmpty()) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("只需要传一个条件!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (!demandChangeDetailsBO.getReqInnerSeq().isEmpty() && demandChangeDetailsBO.getReqNo().isEmpty()) {
            String identification = demandChangeDetailsDao.getIdentificationByReqInnerSeq(demandChangeDetailsBO.getReqInnerSeq());
            if (identification == null) {
                DemandStateHistoryDO demandStateHistoryDO = new DemandStateHistoryDO();
                demandStateHistoryDO.setIdentification(demandChangeDetailsBO.getReqInnerSeq());
                PageInfo<DemandStateHistoryBO> pageInfo = PageUtils.pageQueryWithCount(demandChangeDetailsBO.getPageNum(), demandChangeDetailsBO.getPageSize(),
                        () -> BeanConvertUtils.convertList(demandStateHistoryDao.find(demandStateHistoryDO), DemandStateHistoryBO.class));
                List<DemandStateHistoryBO> demandStateHistoryBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandStateHistoryBO.class);
                DemandStateHistoryRspBO demandStateHistoryRspBO = new DemandStateHistoryRspBO();
                demandStateHistoryRspBO.setDemandStateHistoryBOList(demandStateHistoryBOList);
                demandStateHistoryRspBO.setPageInfo(pageInfo);
                return demandStateHistoryRspBO;
            }
            DemandStateHistoryDO demandStateHistoryDO = new DemandStateHistoryDO();
            demandStateHistoryDO.setIdentification(identification);
            PageInfo<DemandStateHistoryBO> pageInfo = PageUtils.pageQueryWithCount(demandChangeDetailsBO.getPageNum(), demandChangeDetailsBO.getPageSize(),
                    () -> BeanConvertUtils.convertList(demandStateHistoryDao.find(demandStateHistoryDO), DemandStateHistoryBO.class));
            List<DemandStateHistoryBO> demandStateHistoryBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandStateHistoryBO.class);
            DemandStateHistoryRspBO demandStateHistoryRspBO = new DemandStateHistoryRspBO();
            demandStateHistoryRspBO.setDemandStateHistoryBOList(demandStateHistoryBOList);
            demandStateHistoryRspBO.setPageInfo(pageInfo);
            return demandStateHistoryRspBO;
        } else if (demandChangeDetailsBO.getReqInnerSeq().isEmpty() && !demandChangeDetailsBO.getReqNo().isEmpty()) {
            DemandStateHistoryDO demandStateHistoryDO1 = new DemandStateHistoryDO();
            demandStateHistoryDO1.setReqNo(demandChangeDetailsBO.getReqNo());
            List<DemandStateHistoryDO> demandStateHistoryDOS = demandStateHistoryDao.find(demandStateHistoryDO1);
            if (JudgeUtils.isEmpty(demandStateHistoryDOS)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("未查询到数据，请检查输入后，重新查询(初始化导入数据无法通过该查询)");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            DemandStateHistoryDO demandStateHistoryDO2 = new DemandStateHistoryDO();
            demandStateHistoryDO2.setReqInnerSeq(demandStateHistoryDOS.get(0).getReqInnerSeq());
            List<DemandStateHistoryDO> list = demandStateHistoryDao.find(demandStateHistoryDO2);
            String identification = list.get(0).getIdentification();
            if (JudgeUtils.isBlank(identification)) {
                identification = demandStateHistoryDOS.get(0).getReqInnerSeq();
            }
            DemandStateHistoryDO demandStateHistoryDO = new DemandStateHistoryDO();
            demandStateHistoryDO.setIdentification(identification);
            PageInfo<DemandStateHistoryBO> pageInfo = PageUtils.pageQueryWithCount(demandChangeDetailsBO.getPageNum(), demandChangeDetailsBO.getPageSize(),
                    () -> BeanConvertUtils.convertList(demandStateHistoryDao.find(demandStateHistoryDO), DemandStateHistoryBO.class));
            List<DemandStateHistoryBO> demandStateHistoryBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandStateHistoryBO.class);
            DemandStateHistoryRspBO demandStateHistoryRspBO = new DemandStateHistoryRspBO();
            demandStateHistoryRspBO.setDemandStateHistoryBOList(demandStateHistoryBOList);
            demandStateHistoryRspBO.setPageInfo(pageInfo);
            return demandStateHistoryRspBO;
        } else {
            //未传参数 输出所有
            DemandStateHistoryDO demandStateHistoryDO = new DemandStateHistoryDO();
            PageInfo<DemandStateHistoryBO> pageInfo = PageUtils.pageQueryWithCount(demandChangeDetailsBO.getPageNum(), demandChangeDetailsBO.getPageSize(),
                    () -> BeanConvertUtils.convertList(demandStateHistoryDao.find(demandStateHistoryDO), DemandStateHistoryBO.class));
            List<DemandStateHistoryBO> demandStateHistoryBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandStateHistoryBO.class);
            DemandStateHistoryRspBO demandStateHistoryRspBO = new DemandStateHistoryRspBO();
            demandStateHistoryRspBO.setDemandStateHistoryBOList(demandStateHistoryBOList);
            demandStateHistoryRspBO.setPageInfo(pageInfo);
            return demandStateHistoryRspBO;
        }
    }

    @Override
    public DemandCurperiodHistoryRspBO findDemandCurperiodDetails(DemandChangeDetailsBO demandChangeDetailsBO) {
        if (!demandChangeDetailsBO.getReqInnerSeq().isEmpty() && !demandChangeDetailsBO.getReqNo().isEmpty()) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("只需要传一个条件!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        if (!demandChangeDetailsBO.getReqInnerSeq().isEmpty() && demandChangeDetailsBO.getReqNo().isEmpty()) {
            String identification = demandChangeDetailsDao.getIdentificationByReqInnerSeq(demandChangeDetailsBO.getReqInnerSeq());
            if (identification == null) {
                DemandCurperiodHistoryDO demandCurperiodHistoryDO = new DemandCurperiodHistoryDO();
                demandCurperiodHistoryDO.setIdentification(demandChangeDetailsBO.getReqInnerSeq());
                PageInfo<DemandCurperiodHistoryBO> pageInfo = PageUtils.pageQueryWithCount(demandChangeDetailsBO.getPageNum(), demandChangeDetailsBO.getPageSize(),
                        () -> BeanConvertUtils.convertList(demandCurperiodHistoryDao.find(demandCurperiodHistoryDO), DemandCurperiodHistoryBO.class));
                List<DemandCurperiodHistoryBO> demandCurperiodHistoryBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandCurperiodHistoryBO.class);
                DemandCurperiodHistoryRspBO demandCurperiodHistoryRspBO = new DemandCurperiodHistoryRspBO();
                demandCurperiodHistoryRspBO.setDemandCurperiodHistoryBOList(demandCurperiodHistoryBOList);
                demandCurperiodHistoryRspBO.setPageInfo(pageInfo);
                return demandCurperiodHistoryRspBO;
            }
            DemandCurperiodHistoryDO demandCurperiodHistoryDO = new DemandCurperiodHistoryDO();
            demandCurperiodHistoryDO.setIdentification(identification);
            PageInfo<DemandCurperiodHistoryBO> pageInfo = PageUtils.pageQueryWithCount(demandChangeDetailsBO.getPageNum(), demandChangeDetailsBO.getPageSize(),
                    () -> BeanConvertUtils.convertList(demandCurperiodHistoryDao.find(demandCurperiodHistoryDO), DemandCurperiodHistoryBO.class));
            List<DemandCurperiodHistoryBO> demandCurperiodHistoryBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandCurperiodHistoryBO.class);
            DemandCurperiodHistoryRspBO demandCurperiodHistoryRspBO = new DemandCurperiodHistoryRspBO();
            demandCurperiodHistoryRspBO.setDemandCurperiodHistoryBOList(demandCurperiodHistoryBOList);
            demandCurperiodHistoryRspBO.setPageInfo(pageInfo);
            return demandCurperiodHistoryRspBO;
        } else if (demandChangeDetailsBO.getReqInnerSeq().isEmpty() && !demandChangeDetailsBO.getReqNo().isEmpty()) {
            DemandChangeDetailsDO demandChangeDetailsDO = new DemandChangeDetailsDO();
            demandChangeDetailsDO.setReqNo(demandChangeDetailsBO.getReqNo());
            List<DemandChangeDetailsDO> demandChangeDetailsDOS = null;
            demandChangeDetailsDOS = demandChangeDetailsDao.find(demandChangeDetailsDO);
            if (JudgeUtils.isEmpty(demandChangeDetailsDOS)) {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("未查询到数据，请检查输入后，重新查询(初始化导入数据无法通过该查询)");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            String identification = demandChangeDetailsDao.getIdentificationByReqInnerSeq(demandChangeDetailsDOS.get(0).getReqInnerSeq());
            DemandCurperiodHistoryDO demandCurperiodHistoryDO = new DemandCurperiodHistoryDO();
            demandCurperiodHistoryDO.setIdentification(identification);
            PageInfo<DemandCurperiodHistoryBO> pageInfo = PageUtils.pageQueryWithCount(demandChangeDetailsBO.getPageNum(), demandChangeDetailsBO.getPageSize(),
                    () -> BeanConvertUtils.convertList(demandCurperiodHistoryDao.find(demandCurperiodHistoryDO), DemandCurperiodHistoryBO.class));
            List<DemandCurperiodHistoryBO> demandCurperiodHistoryBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandCurperiodHistoryBO.class);
            DemandCurperiodHistoryRspBO demandCurperiodHistoryRspBO = new DemandCurperiodHistoryRspBO();
            demandCurperiodHistoryRspBO.setDemandCurperiodHistoryBOList(demandCurperiodHistoryBOList);
            demandCurperiodHistoryRspBO.setPageInfo(pageInfo);
            return demandCurperiodHistoryRspBO;
        } else {
            //未传参数 输出所有
            DemandCurperiodHistoryDO demandCurperiodHistoryDO = new DemandCurperiodHistoryDO();
            PageInfo<DemandCurperiodHistoryBO> pageInfo = PageUtils.pageQueryWithCount(demandChangeDetailsBO.getPageNum(), demandChangeDetailsBO.getPageSize(),
                    () -> BeanConvertUtils.convertList(demandCurperiodHistoryDao.find(demandCurperiodHistoryDO), DemandCurperiodHistoryBO.class));
            List<DemandCurperiodHistoryBO> demandCurperiodHistoryBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandCurperiodHistoryBO.class);
            DemandCurperiodHistoryRspBO demandCurperiodHistoryRspBO = new DemandCurperiodHistoryRspBO();
            demandCurperiodHistoryRspBO.setDemandCurperiodHistoryBOList(demandCurperiodHistoryBOList);
            demandCurperiodHistoryRspBO.setPageInfo(pageInfo);
            return demandCurperiodHistoryRspBO;
        }
    }

    //获取已经上传的文档
    private Map<String, Object> BatLists(DemandDO demandDO) {
        System.err.println(demandDO);
        List<DemandDO> List = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        int start = demandDO.getReqNo().indexOf("-") + 1;
        String reqMonth = demandDO.getReqNo().substring(start, start + 6);
        demandDO.setReqStartMon(reqMonth);
        List.add(demandDO);
        //循环文件目录
        if (List != null) {
            File srcfile[] = new File[List.size() * 50];
            int num = 0;
            //要压缩的文件
            for (int i = 0; i < List.size(); i++) {
                //需求说明书、技术方案、原子功能点评估表
                String path = "";
                if (LemonUtils.getEnv().equals(Env.SIT)) {
                    path = "/home/devms/temp/Projectdoc/" + List.get(i).getReqStartMon() + "/"
                            + List.get(i).getReqNo() + "_" + List.get(i).getReqNm();
                } else if (LemonUtils.getEnv().equals(Env.DEV)) {
                    path = "/home/devadm/temp/Projectdoc/" + List.get(i).getReqStartMon() + "/"
                            + List.get(i).getReqNo() + "_" + List.get(i).getReqNm();
                } else {
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                    MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
                    BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
                }
                File file1 = new File(path + "/开发技术文档/");
                if (!file1.exists() && !file1.isDirectory()) {
                    file1.mkdir();
                }
                File[] tempFile1 = file1.listFiles();
                if (tempFile1 == null) {
                    tempFile1 = new File[0];
                }
                for (int j = 0; j < tempFile1.length; j++) {
                    srcfile[num] = new File(path + "/开发技术文档/" + tempFile1[j].getName());
                    num++;

                }

                File file2 = new File(path + "/产品文档/");
                if (!file2.exists() && !file2.isDirectory()) {
                    file2.mkdir();
                }
                File[] tempFile2 = file2.listFiles();
                if (tempFile2 == null) {
                    tempFile2 = new File[0];
                }
                for (int j = 0; j < tempFile2.length; j++) {
                    srcfile[num] = new File(path + "/产品文档/" + tempFile2[j].getName());
                    num++;
                }

                File file3 = new File(path + "/测试文档/");
                if (!file3.exists() && !file3.isDirectory()) {
                    file3.mkdir();
                }
                File[] tempFile3 = file3.listFiles();
                if (tempFile3 == null) {
                    tempFile3 = new File[0];
                }
                for (int j = 0; j < tempFile3.length; j++) {
                    srcfile[num] = new File(path + "/测试文档/" + tempFile3[j].getName());
                    num++;
                }

                File file4 = new File(path + "/评审文档/");
                if (!file4.exists() && !file4.isDirectory()) {
                    file4.mkdir();
                }
                File[] tempFile4 = file4.listFiles();
                if (tempFile4 == null) {
                    tempFile4 = new File[0];
                }
                for (int j = 0; j < tempFile4.length; j++) {
                    srcfile[num] = new File(path + "/评审文档/" + tempFile4[j].getName());
                    num++;
                }

                File file5 = new File(path + "/项目管理文档/");
                if (!file5.exists() && !file5.isDirectory()) {
                    file5.mkdir();
                }
                File[] tempFile5 = file5.listFiles();
                if (tempFile5 == null) {
                    tempFile5 = new File[0];
                }
                for (int j = 0; j < tempFile5.length; j++) {
                    srcfile[num] = new File(path + "/项目管理文档/" + tempFile5[j].getName());
                    num++;
                }

                File file6 = new File(path + "/预投产投产文档/");
                if (!file6.exists() && !file6.isDirectory()) {
                    file6.mkdir();
                }
                File[] tempFile6 = file6.listFiles();
                if (tempFile6 == null) {
                    tempFile6 = new File[0];
                }
                for (int j = 0; j < tempFile6.length; j++) {
                    srcfile[num] = new File(path + "/预投产投产文档/" + tempFile6[j].getName());
                    num++;
                }


            }
            map.put("srcfile", srcfile);
        }

        return map;
    }

    @Override
    public List<DemandBO> getPrdFnishAbnor(String month) {
        return BeanConvertUtils.convertList(demandDao.getPrdFnishAbnor(month), DemandBO.class);
    }

    @Override
    public List<DemandBO> getTestFnishAbnor(String month) {
        return BeanConvertUtils.convertList(demandDao.getTestFnishAbnor(month), DemandBO.class);
    }

    @Override
    public List<DemandBO> getUatUpdateAbnor(String month) {
        return BeanConvertUtils.convertList(demandDao.getUatUpdateAbnor(month), DemandBO.class);
    }

    @Override
    public void updateReqAbnorType(DemandBO reqTask) {
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, reqTask);
        demandDao.updateReqAbnorType(demandDO);
    }

    @Override
    public List<DemandBO> getPrdFnishWarn() {
        return BeanConvertUtils.convertList(demandDao.getPrdFnishWarn(), DemandBO.class);
    }

    @Override
    public List<DemandBO> getUatUpdateWarn() {
        return BeanConvertUtils.convertList(demandDao.getUatUpdateWarn(), DemandBO.class);
    }

    @Override
    public List<DemandBO> getTestFnishWarn() {
        return BeanConvertUtils.convertList(demandDao.getTestFnishWarn(), DemandBO.class);
    }

    @Override
    public void WeedAndMonthFeedback(DemandBO reqTask) {
        //查询原数据
        DemandDO demandDO = demandDao.get(reqTask.getReqInnerSeq());
        //如果修改了需求当前阶段
        if (!reqTask.getPreCurPeriod().equals(demandDO.getPreCurPeriod())) {
            //登记需求阶段记录表
            String remarks = "手动修改";
            reqPlanService.registrationDemandPhaseRecordForm(reqTask, remarks);
        }
        DemandDO aDo = new DemandDO();
        BeanConvertUtils.convert(aDo, reqTask);
        demandDao.WeedAndMonthFeedback(aDo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void approvalProcess(MultipartFile file, String ids) {
        // 操作人
        String user = userService.getFullname(SecurityUtils.getLoginName());
        // 操作时间
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String[] idsList = ids.split("~");
        if (file.isEmpty()) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择上传文件!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }

        //依据环境配置路径
        String path = "";
        String wwwpath = "";
        if (LemonUtils.getEnv().equals(Env.SIT)) {
            path = "/home/devms/temp/approval/process/";
            wwwpath = "/home/devms/wwwroot/home/devms/temp/approval/process/";
        } else if (LemonUtils.getEnv().equals(Env.DEV)) {
            path = "/home/devadm/temp/approval/process/";
            wwwpath = "/home/devadm/wwwroot/home/devadm/temp/approval/process/";
        } else {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("当前配置环境路径有误，请尽快联系管理员!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        for (int i = 0; i < idsList.length; i++) {
            String reqInnerSeq = idsList[i];
            // 获取需求信息
            DemandDO demandDO = demandDao.get(reqInnerSeq);
            // 存储地址
            String picLocal = path + reqInnerSeq;
            File fileDir = new File(wwwpath + reqInnerSeq);
            File filePath = new File(fileDir.getPath() + "/" + file.getOriginalFilename());
            if (fileDir.exists()) {
                File[] oldFile = fileDir.listFiles();
                for (File o : oldFile) o.delete();
            } else {
                fileDir.mkdir();
            }
            try {
                file.transferTo(filePath);
                // 是，已上传则修改
                if ("是".equals(demandDO.getIsApprovalProcess())) {
                    DemandPictureDO demandPictureDO = new DemandPictureDO();
                    demandPictureDO.setPicReqinnerseq(demandDO.getReqInnerSeq());
                    demandPictureDO.setPicMoth(demandDO.getReqImplMon());
                    DemandPictureDO demandPicture = iDemandPictureDao.showOne(demandPictureDO);
                    demandPicture.setPicName(file.getOriginalFilename());
                    demandPicture.setPicLocal(picLocal);
                    demandPicture.setPicUser(user);
                    demandPicture.setPicTime(time);
                    demandPicture.setPicMoth(demandDO.getReqImplMon());
                    demandPicture.setPicReqnm(demandDO.getReqNm());
                    demandPicture.setPicReqno(demandDO.getReqNo());
                    //修改JK需求审核表
                    iDemandPictureDao.update(demandPicture);
                } else {
                    // 没有上传新增
                    DemandPictureDO demandPicture = new DemandPictureDO();
                    demandPicture.setPicReqinnerseq(reqInnerSeq);
                    demandPicture.setPicName(file.getOriginalFilename());
                    demandPicture.setPicLocal(picLocal);
                    demandPicture.setPicUser(user);
                    demandPicture.setPicTime(time);
                    demandPicture.setPicMoth(demandDO.getReqImplMon());
                    demandPicture.setPicReqnm(demandDO.getReqNm());
                    demandPicture.setPicReqno(demandDO.getReqNo());
                    //登记JK需求审核表
                    iDemandPictureDao.insert(demandPicture);
                }

            } catch (IllegalStateException e) {
                e.printStackTrace();
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("文件上传失败");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            } catch (IOException e) {
                e.printStackTrace();
                MsgEnum.ERROR_CUSTOM.setMsgInfo("");
                MsgEnum.ERROR_CUSTOM.setMsgInfo("文件上传失败");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
        }

    }

    @Override
    public DemandBO approvalFindOne(String id, String month) {
        DemandPictureDO demandPictureDO = new DemandPictureDO();
        demandPictureDO.setPicReqinnerseq(id);
        demandPictureDO.setPicMoth(month);
        DemandPictureDO demandPicture = iDemandPictureDao.showOne(demandPictureDO);
        System.err.println(demandPicture);
        DemandBO demandBO = new DemandBO();
        String site = demandPicture.getPicLocal() + "/" + demandPicture.getPicName();
        System.err.println(site);
        demandBO.setSite(site);
        return demandBO;
    }

    @Override
    public DemandnNameChangeRspBO numberNameChangeDetail(DemandNameChangeBO demandNameChangeBO) {
        PageInfo<DemandNameChangeBO> pageInfo = getDemandnNameChange(demandNameChangeBO);
        List<DemandNameChangeBO> demandBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandNameChangeBO.class);
        DemandnNameChangeRspBO demandnNameChangeRspBO = new DemandnNameChangeRspBO();
        demandnNameChangeRspBO.setDemandNameChangeBOS(demandBOList);
        demandnNameChangeRspBO.setPageInfo(pageInfo);
        return demandnNameChangeRspBO;
    }

    @Override
    public void productionDefectIntroduction(MultipartFile file) {
        File f = null;
        LinkedList<ProductionDefectsDO> productionDefectsDOLinkedList = new LinkedList<>();
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
                //productionDefects
//流水号 serialNumber   文号  documentNumber	流程状态 processStatus	流程开始日期	 processStartDate 当前环节	currentSession
// 当前执行人	currentExecutor 问题录入(问题提出人) problemRaiser	问题录入(问题标题)	questionTitle
// 问题录入(问题编号)	questionNumber 问题录入(问题详细)questionDetails	问题分析(问题原因及解决方案)	solution
// 问题分析(问题归属部门)problemAttributionDept	问题分析(问题负责人)	personInCharge 问题分析(问题类型)	questionType
// 回复审核(问题定位) identifyTheProblem	程序修改(开发负责人) developmentLeader	程序修改(更新类型) updateType
                ProductionDefectsDO productionDefectsDO = new ProductionDefectsDO();
                productionDefectsDO.setSerialnumber(map.get(i).get(0).toString().trim());
                productionDefectsDO.setDocumentnumber(map.get(i).get(1).toString().trim());
                productionDefectsDO.setProcessstatus(map.get(i).get(2).toString().trim());
                productionDefectsDO.setProcessstartdate(map.get(i).get(3).toString().trim());
                productionDefectsDO.setCurrentsession(map.get(i).get(4).toString().trim());
                productionDefectsDO.setCurrentexecutor(map.get(i).get(5).toString().trim());
                productionDefectsDO.setProblemraiser(map.get(i).get(6).toString().trim());
                productionDefectsDO.setQuestiontitle(map.get(i).get(7).toString().trim());
                productionDefectsDO.setQuestionnumber(map.get(i).get(8).toString().trim());
                productionDefectsDO.setQuestiondetails(map.get(i).get(9).toString().trim());
                productionDefectsDO.setSolution(map.get(i).get(10).toString().trim());
                productionDefectsDO.setProblemattributiondept(map.get(i).get(11).toString().trim());
                productionDefectsDO.setPersonincharge(map.get(i).get(12).toString().trim());
                productionDefectsDO.setQuestiontype(map.get(i).get(13).toString().trim());
                productionDefectsDO.setIdentifytheproblem(map.get(i).get(14).toString().trim());
                productionDefectsDO.setDevelopmentleader(map.get(i).get(15).toString().trim());
                productionDefectsDO.setUpdatetype(map.get(i).get(16).toString().trim());
                productionDefectsDOLinkedList.add(productionDefectsDO);
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
        productionDefectsDOLinkedList.forEach(m -> {
            ProductionDefectsDO productionDefectsDO = new ProductionDefectsDO();
            productionDefectsDO.setSerialnumber(m.getSerialnumber());
            List<ProductionDefectsDO> productionDefectsDOS = productionDefectsDao.find(productionDefectsDO);
            if (JudgeUtils.isEmpty(productionDefectsDOS)) {
                productionDefectsDao.insert(m);
            } else {
                m.setId(productionDefectsDOS.get(0).getId());
                productionDefectsDao.update(m);
            }
        });
    }

    @Override
    public void smokeTestRegistration(SmokeTestRegistrationBO smokeTestRegistrationBO) {
        SmokeTestRegistrationDO smokeTestRegistrationDO = BeanUtils.copyPropertiesReturnDest(new SmokeTestRegistrationDO(), smokeTestRegistrationBO);
        smokeTestRegistrationDao.insert(smokeTestRegistrationDO);
        SmokeTestFailedCountDO smokeTestFailedCountDO = new SmokeTestFailedCountDO();
        smokeTestFailedCountDO.setJiraKey(smokeTestRegistrationBO.getJiraKey());
        List<SmokeTestFailedCountDO> smokeTestFailedCountDOS = smokeTestFailedCountDao.find(smokeTestFailedCountDO);
        if (JudgeUtils.isEmpty(smokeTestFailedCountDOS)) {
            smokeTestFailedCountDO.setJiraKey(smokeTestRegistrationBO.getJiraKey());
            smokeTestFailedCountDO.setCount(1);
            smokeTestFailedCountDO.setReqNo(smokeTestRegistrationBO.getReqNo());
            smokeTestFailedCountDO.setDepartment(smokeTestRegistrationBO.getDepartment());
            smokeTestFailedCountDao.insert(smokeTestFailedCountDO);
        } else {
            smokeTestFailedCountDOS.get(0).setCount(smokeTestFailedCountDOS.get(0).getCount() + 1);
            smokeTestFailedCountDao.update(smokeTestFailedCountDOS.get(0));
        }
    }

    /**
     * 需求累计投入资源
     */
    @Override
    public void demandInputResourceStatistics() {
        WorkingHoursDO workingHoursDO = new WorkingHoursDO();
        //查找统计表示为N的数据
        workingHoursDO.setRegisterflag("N");
        List<WorkingHoursDO> workingHoursDOS = workingHoursDao.find(workingHoursDO);
        workingHoursDOS.forEach(m -> {
            //没用epic编号的则不统计
            if (JudgeUtils.isBlank(m.getEpickey())) {
                m.setRegisterflag("Y");
                workingHoursDao.update(m);
                return;
            } else {
                DemandResourceInvestedDO demandResourceInvestedDO = new DemandResourceInvestedDO();
                demandResourceInvestedDO.setValueType("工时");
                demandResourceInvestedDO.setEpicKey(m.getEpickey());
                demandResourceInvestedDO.setDepartment(m.getDevpLeadDept());
                List<DemandResourceInvestedDO> demandResourceInvestedDOS = demandResourceInvestedDao.find(demandResourceInvestedDO);
                if (JudgeUtils.isNotEmpty(demandResourceInvestedDOS)) {
                    String value1 = demandResourceInvestedDOS.get(0).getValue();
                    String value2 = m.getTimespnet();
                    int parseInt = Integer.parseInt(value1) + Integer.parseInt(value2);
                    String value = String.valueOf(parseInt);
                    demandResourceInvestedDOS.get(0).setValue(value);
                    demandResourceInvestedDao.update(demandResourceInvestedDOS.get(0));
                } else {
                    demandResourceInvestedDO.setValue(m.getTimespnet());
                    demandResourceInvestedDao.insert(demandResourceInvestedDO);
                }
                //登记完成后修改状态
                m.setRegisterflag("Y");
                workingHoursDao.update(m);
            }

        });

    }

    @Override
    public TimeAxisDataBO getTimeAxisData(String reqInnerSeq) {
        String[] timeAxisData = null;
        int position =0;
        DemandBO demandBO = findById(reqInnerSeq);
        if (Integer.parseInt(demandBO.getPreCurPeriod()) < 30) {
            timeAxisData = this.getTimeAxisData(0);
            position=0;
        } else if (Integer.parseInt(demandBO.getPreCurPeriod()) < 120) {
            timeAxisData = this.getTimeAxisData(1);
            position=1;
        } else if (Integer.parseInt(demandBO.getPreCurPeriod()) == 120) {
            timeAxisData = this.getTimeAxisData(2);
            position=2;
        } else if (Integer.parseInt(demandBO.getPreCurPeriod()) < 160) {
            timeAxisData = this.getTimeAxisData(3);
            position=3;
        } else if (Integer.parseInt(demandBO.getPreCurPeriod()) < 180) {
            timeAxisData = this.getTimeAxisData(4);
            position=4;
        } else if (Integer.parseInt(demandBO.getPreCurPeriod()) == 180) {
            timeAxisData = this.getTimeAxisData(5);
            position=5;
        }


        TimeAxisDataBO timeAxisDataBO = new TimeAxisDataBO();
        timeAxisDataBO.setxAxisDate(timeAxisData);
        timeAxisDataBO.setPosition(position);

        //定稿时间
        timeAxisDataBO.setPrdFinshTm(demandBO.getPrdFinshTm());
        //uat更新时间
        timeAxisDataBO.setUatUpdateTm(demandBO.getUatUpdateTm());
        //测试时间
        timeAxisDataBO.setTestFinshTm(demandBO.getTestFinshTm());
        //预投产时间
        timeAxisDataBO.setPreTm(demandBO.getPreTm());
        //投产时间
        timeAxisDataBO.setExpPrdReleaseTm(demandBO.getExpPrdReleaseTm());
        //查询时间
        String selectTime = DateUtil.date2String(new Date(), "yyyy-MM-dd");

        String req_peroid = dictionaryService.findFieldValue("REQ_PEROID", demandBO.getPreCurPeriod());
        timeAxisDataBO.setPreCurPeriod(req_peroid);
        timeAxisDataBO.setSelectTime(selectTime);
        return timeAxisDataBO;
    }

    @Override
    public void defectMonthlyDownload(HttpServletResponse response,String startDateTime,String endDateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse=new Date();
        try {
            parse= sdf.parse(endDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(parse);
        c.add(Calendar.DATE, + 1);
        Date time = c.getTime();
        String preDay = sdf.format(time);

        DefectDetailsDO defectDetailsDO = new DefectDetailsDO();

        defectDetailsDO.setStartTime(startDateTime);
        defectDetailsDO.setEndTime(preDay);
        List<DefectDetailsDO> defectDetailsList = defectDetailsDao.findByTime(defectDetailsDO);

        HashMap<String,DefectMonthlyBO> defectMonthlyMap = new HashMap<>();
        ArrayList<DefectMonthlyBO> defectMonthlyList = new ArrayList<>();
        for(int i=0;i<defectDetailsList.size();i++){
            if(JudgeUtils.isNotBlank( defectDetailsList.get(i).getEpicKey())){
                if(defectDetailsList.get(i).getEpicKey().equals("CMPAY-2802")){
                    defectDetailsList.get(i).setEpicKey("CMPAY-2058");
                }
                if(defectDetailsList.get(i).getEpicKey().equals("CMPAY-1040")){
                    defectDetailsList.get(i).setEpicKey("CMPAY-999");
                }
                // 无效问题在缺陷月报报表不统计
                if(WUXIAOWENTI.equals(defectDetailsList.get(i).getDefectType())){
                    continue;
                }
                DemandJiraDO demandJiraDO = new DemandJiraDO();
                demandJiraDO.setJiraKey(defectDetailsList.get(i).getEpicKey());
                // 根据jiraKey获取内部编号
                List<DemandJiraDO> demandJiraDos = demandJiraDao.find(demandJiraDO);
                    // 根据内部编号获取 需求名及需求编号
                    if(demandJiraDos!=null && demandJiraDos.size()!=0){
                        if(JudgeUtils.isNull(defectMonthlyMap.get(defectDetailsList.get(i).getEpicKey()))){
                            DemandDO demandDO = demandDao.get(demandJiraDos.get(demandJiraDos.size()-1).getReqInnerSeq());
                            DefectMonthlyBO defectMonthlyBO = new DefectMonthlyBO();
                            defectMonthlyBO.setReqNo(demandDO.getReqNo());
                            defectMonthlyBO.setReqNm(demandDO.getReqNm());
                            defectMonthlyBO.setReqPrdLine(demandDO.getReqPrdLine());
                            defectMonthlyBO.setTotalWorkload(demandDO.getTotalWorkload());
                            defectMonthlyBO.setReqMnger(demandDO.getReqMnger());
                            defectMonthlyBO.setReqMngerDept(demandDO.getReqProDept());
                            defectMonthlyBO.setDevpResMng(demandDO.getDevpResMng());
                            defectMonthlyBO.setDevpLeadDept(demandDO.getDevpLeadDept());
                            defectMonthlyBO.setDefectsNumber(1);
                            defectMonthlyBO.setDefectRate(String.format("%.2f", (float) defectMonthlyBO.getDefectsNumber() / (float) defectMonthlyBO.getTotalWorkload() * 100)+"%");
                            defectMonthlyMap.put(defectDetailsList.get(i).getEpicKey(),defectMonthlyBO);
                            if(JudgeUtils.isNotBlank(defectDetailsList.get(i).getSecurityLevel())){
                                if(defectDetailsList.get(i).getSecurityLevel().equals("严重")){
                                    defectMonthlyBO.setSeriousDefectsNumber(defectMonthlyBO.getSeriousDefectsNumber()+1);
                                }else if(defectDetailsList.get(i).getSecurityLevel().equals("一般")){
                                    defectMonthlyBO.setOrdinaryDefectsNumber(defectMonthlyBO.getOrdinaryDefectsNumber()+1);
                                }else if(defectDetailsList.get(i).getSecurityLevel().equals("轻微")){
                                    defectMonthlyBO.setMinorDefectsNumber(defectMonthlyBO.getMinorDefectsNumber()+1);
                                }else if(defectDetailsList.get(i).getSecurityLevel().equals("致命")){
                                    defectMonthlyBO.setFatalDefectsNumber(defectMonthlyBO.getFatalDefectsNumber()+1);
                                }
                            }

                        }else{
                            DefectMonthlyBO defectMonthlyBO = defectMonthlyMap.get(defectDetailsList.get(i).getEpicKey());
                            defectMonthlyBO.setDefectsNumber(defectMonthlyBO.getDefectsNumber()+1);
                            defectMonthlyBO.setDefectRate(String.format("%.2f", (float) defectMonthlyBO.getDefectsNumber() / (float) defectMonthlyBO.getTotalWorkload() * 100)+"%");
                            if(JudgeUtils.isNotBlank(defectDetailsList.get(i).getSecurityLevel())){
                                if(defectDetailsList.get(i).getSecurityLevel().equals("严重")){
                                    defectMonthlyBO.setSeriousDefectsNumber(defectMonthlyBO.getSeriousDefectsNumber()+1);
                                }else if(defectDetailsList.get(i).getSecurityLevel().equals("一般")){
                                    defectMonthlyBO.setOrdinaryDefectsNumber(defectMonthlyBO.getOrdinaryDefectsNumber()+1);
                                }else if(defectDetailsList.get(i).getSecurityLevel().equals("轻微")){
                                    defectMonthlyBO.setMinorDefectsNumber(defectMonthlyBO.getMinorDefectsNumber()+1);
                                }else if(defectDetailsList.get(i).getSecurityLevel().equals("致命")){
                                    defectMonthlyBO.setFatalDefectsNumber(defectMonthlyBO.getFatalDefectsNumber()+1);
                                }
                            }
                            defectMonthlyMap.put(defectDetailsList.get(i).getEpicKey(),defectMonthlyBO);
                        }

                    }else{
                        System.err.println(2);
                    }

            }else{
                System.err.println(1);
            }
        }

        for (Map.Entry<String, DefectMonthlyBO> entry : defectMonthlyMap.entrySet()) {
            DefectMonthlyBO defectMonthlyBO = entry.getValue();

            //测试通过率 数据来源暂时无
            //defectMonthlyBO.setExecutionCaseNumber(Integer.parseInt(jiraBasicInfoDO.getTestCaseNumber()));
          /*
            defectMonthlyBO.setSuccessCaseNumber(defectMonthlyBO.getExecutionCaseNumber()-defectMonthlyBO.getDefectsNumber());
            defectMonthlyBO.setTestPassRate(String.format("%.2f", (float) defectMonthlyBO.getSuccessCaseNumber() / (float) defectMonthlyBO.getExecutionCaseNumber() * 100)+"%");
         */

            //产品线转换
            String req_peroid = dictionaryService.findFieldValue("PRD_LINE", defectMonthlyBO.getReqPrdLine());
            defectMonthlyBO.setReqPrdLine(req_peroid);
            defectMonthlyList.add(defectMonthlyBO);

        }

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DefectMonthlyBO.class, defectMonthlyList);
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // 判断数据
            if (workbook == null) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
            // 设置excel的文件名称
            String excelName = "reqTask_" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }

    @Override
    public List<DefectMonthlyBO> getDefectMonthlyReport(String defectStartTime, String defectEndTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parse=new Date();
        try {
            parse= sdf.parse(defectEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(parse);
        c.add(Calendar.DATE, + 1);
        Date time = c.getTime();
        String preDay = sdf.format(time);

        DefectDetailsDO defectDetailsDO = new DefectDetailsDO();

        defectDetailsDO.setStartTime(defectStartTime);
        defectDetailsDO.setEndTime(preDay);
        List<DefectDetailsDO> defectDetailsList = defectDetailsDao.findByTime(defectDetailsDO);
        HashMap<String,DefectMonthlyBO> defectMonthlyMap = new HashMap<>();
        List<DefectMonthlyBO> defectMonthlyList = new ArrayList<>();
        for(int i=0;i<defectDetailsList.size();i++){
            if(JudgeUtils.isNotBlank( defectDetailsList.get(i).getEpicKey())){
                if(defectDetailsList.get(i).getEpicKey().equals("CMPAY-2802")){
                    defectDetailsList.get(i).setEpicKey("CMPAY-2058");
                }
                if(defectDetailsList.get(i).getEpicKey().equals("CMPAY-1040")){
                    defectDetailsList.get(i).setEpicKey("CMPAY-999");
                }
                // 无效问题在缺陷月报报表不统计
                if(WUXIAOWENTI.equals(defectDetailsList.get(i).getDefectType())){
                    continue;
                }

                DemandJiraDO demandJiraDO = new DemandJiraDO();
                demandJiraDO.setJiraKey(defectDetailsList.get(i).getEpicKey());
                // 根据jiraKey获取内部编号
                List<DemandJiraDO> demandJiraDos = demandJiraDao.find(demandJiraDO);
                // 根据内部编号获取 需求名及需求编号

                if(demandJiraDos!=null && demandJiraDos.size()!=0){
                    if(JudgeUtils.isNull(defectMonthlyMap.get(defectDetailsList.get(i).getEpicKey()))){
                        DemandDO demandDO = demandDao.get(demandJiraDos.get(demandJiraDos.size()-1).getReqInnerSeq());
                        DefectMonthlyBO defectMonthlyBO = new DefectMonthlyBO();
                        defectMonthlyBO.setReqNo(demandDO.getReqNo());
                        defectMonthlyBO.setReqNm(demandDO.getReqNm());
                        defectMonthlyBO.setReqPrdLine(demandDO.getReqPrdLine());
                        defectMonthlyBO.setTotalWorkload(demandDO.getTotalWorkload());
                        defectMonthlyBO.setReqMnger(demandDO.getReqMnger());
                        defectMonthlyBO.setReqMngerDept(demandDO.getReqProDept());
                        defectMonthlyBO.setDevpResMng(demandDO.getDevpResMng());
                        defectMonthlyBO.setDevpLeadDept(demandDO.getDevpLeadDept());
                        defectMonthlyBO.setDefectsNumber(1);
                        defectMonthlyBO.setDefectRate(String.format("%.2f", (float) defectMonthlyBO.getDefectsNumber() / (float) defectMonthlyBO.getTotalWorkload() * 100)+"%");
                        defectMonthlyMap.put(defectDetailsList.get(i).getEpicKey(),defectMonthlyBO);
                        if(JudgeUtils.isNotBlank(defectDetailsList.get(i).getSecurityLevel())){
                            if(defectDetailsList.get(i).getSecurityLevel().equals("严重")){
                                defectMonthlyBO.setSeriousDefectsNumber(defectMonthlyBO.getSeriousDefectsNumber()+1);
                            }else if(defectDetailsList.get(i).getSecurityLevel().equals("一般")){
                                defectMonthlyBO.setOrdinaryDefectsNumber(defectMonthlyBO.getOrdinaryDefectsNumber()+1);
                            }else if(defectDetailsList.get(i).getSecurityLevel().equals("轻微")){
                                defectMonthlyBO.setMinorDefectsNumber(defectMonthlyBO.getMinorDefectsNumber()+1);
                            }else if(defectDetailsList.get(i).getSecurityLevel().equals("致命")){
                                defectMonthlyBO.setFatalDefectsNumber(defectMonthlyBO.getFatalDefectsNumber()+1);
                            }
                        }

                    }else{
                        DefectMonthlyBO defectMonthlyBO = defectMonthlyMap.get(defectDetailsList.get(i).getEpicKey());
                        defectMonthlyBO.setDefectsNumber(defectMonthlyBO.getDefectsNumber()+1);
                        defectMonthlyBO.setDefectRate(String.format("%.2f", (float) defectMonthlyBO.getDefectsNumber() / (float) defectMonthlyBO.getTotalWorkload() * 100)+"%");
                        if(JudgeUtils.isNotBlank(defectDetailsList.get(i).getSecurityLevel())){
                            if(defectDetailsList.get(i).getSecurityLevel().equals("严重")){
                                defectMonthlyBO.setSeriousDefectsNumber(defectMonthlyBO.getSeriousDefectsNumber()+1);
                            }else if(defectDetailsList.get(i).getSecurityLevel().equals("一般")){
                                defectMonthlyBO.setOrdinaryDefectsNumber(defectMonthlyBO.getOrdinaryDefectsNumber()+1);
                            }else if(defectDetailsList.get(i).getSecurityLevel().equals("轻微")){
                                defectMonthlyBO.setMinorDefectsNumber(defectMonthlyBO.getMinorDefectsNumber()+1);
                            }else if(defectDetailsList.get(i).getSecurityLevel().equals("致命")){
                                defectMonthlyBO.setFatalDefectsNumber(defectMonthlyBO.getFatalDefectsNumber()+1);
                            }
                        }
                        defectMonthlyMap.put(defectDetailsList.get(i).getEpicKey(),defectMonthlyBO);
                    }

                }else{
                    System.err.println(defectDetailsList.get(i).getEpicKey());
                }
            }

        }

        for (Map.Entry<String, DefectMonthlyBO> entry : defectMonthlyMap.entrySet()) {
            DefectMonthlyBO defectMonthlyBO = entry.getValue();
            String key = entry.getKey();
            //产品线转换
            String req_peroid = dictionaryService.findFieldValue("PRD_LINE", defectMonthlyBO.getReqPrdLine());
            defectMonthlyBO.setReqPrdLine(req_peroid);



            //测试通过率 数据来源暂时无
            //defectMonthlyBO.setExecutionCaseNumber(Integer.parseInt(jiraBasicInfoDO.getTestCaseNumber()));
          /*
            defectMonthlyBO.setSuccessCaseNumber(defectMonthlyBO.getExecutionCaseNumber()-defectMonthlyBO.getDefectsNumber());
            defectMonthlyBO.setTestPassRate(String.format("%.2f", (float) defectMonthlyBO.getSuccessCaseNumber() / (float) defectMonthlyBO.getExecutionCaseNumber() * 100)+"%");
         */

            defectMonthlyList.add(defectMonthlyBO);

        }

        return defectMonthlyList;
    }

    public String[] getTimeAxisData(int position) {
        String[] strings = new String[6];
        String[] date = {"需求定稿时间", "UAT更新时间", "测试完成时间", "预投产时间", "投产完成时间"};
        strings[position] = "";
        for (int j = 0,k=0; j < strings.length ; j++,k++) {
            if ("".equals(strings[j])) {
                k--;
                continue;
            }else{
                strings[j]=date[k];
            }
        }
        return strings;
    }

    private PageInfo<DemandNameChangeBO> getDemandnNameChange(DemandNameChangeBO demandNameChangeBO) {
        DemandNameChangeDO demandNameChangeDO = new DemandNameChangeDO();
        BeanConvertUtils.convert(demandNameChangeDO, demandNameChangeBO);
        PageInfo<DemandNameChangeBO> pageInfo = PageUtils.pageQueryWithCount(demandNameChangeBO.getPageNum(), demandNameChangeBO.getPageSize(),
                () -> BeanConvertUtils.convertList(iDemandNameChangeExtDao.findList(demandNameChangeDO), DemandNameChangeBO.class));
        return pageInfo;
    }




    /**
     * 补充测试子任务
     */
    public void  supplementaryTestSubtask() {
        DemandDO demandDO1 = new DemandDO();
        demandDO1.setReqImplMon("2020-08");
        List<DemandDO> demandDOS1 = demandDao.find(demandDO1);

        System.err.println(demandDOS1.size());

        for (int i = 0; i < demandDOS1.size(); i++) {
            System.err.println(demandDOS1.get(i).getReqInnerSeq());
            DemandJiraDO demandJiraDO = new DemandJiraDO();
            demandJiraDO.setReqInnerSeq(demandDOS1.get(i).getReqInnerSeq());
            List<DemandJiraDO> demandJiraDOS = demandJiraDao.find(demandJiraDO);
            if (JudgeUtils.isEmpty(demandJiraDOS)) {
                continue;
            }
            String jiraKey = demandJiraDOS.get(0).getJiraKey();
            System.err.println(jiraKey);
            DemandJiraDevelopMasterTaskDO demandJiraDevelopMasterTaskDO = new DemandJiraDevelopMasterTaskDO();
            demandJiraDevelopMasterTaskDO.setRelevanceEpic(jiraKey);
            demandJiraDevelopMasterTaskDO.setIssueType("测试主任务");
            List<DemandJiraDevelopMasterTaskDO> demandJiraDevelopMasterTaskDOS = demandJiraDevelopMasterTaskDao.find(demandJiraDevelopMasterTaskDO);
            System.err.println(demandJiraDevelopMasterTaskDOS.size());
            if (JudgeUtils.isEmpty(demandJiraDevelopMasterTaskDOS)) {
                continue;
            }
            System.err.println(demandJiraDevelopMasterTaskDOS.get(0).getJiraKey());
            JiraTaskBodyBO jiraTaskBodyBO = new JiraTaskBodyBO();
            try {
                jiraTaskBodyBO = JiraUtil.GetIssue(demandJiraDevelopMasterTaskDOS.get(0).getJiraKey());
            } catch (Exception e) {
                continue;
            }

            DemandJiraSubtaskDO demandJiraSubtaskDO1 = new DemandJiraSubtaskDO();
            demandJiraSubtaskDO1.setParenttaskkey(demandJiraDevelopMasterTaskDOS.get(0).getJiraKey());
            List<DemandJiraSubtaskDO> demandJiraSubtaskDOS = demandJiraSubtaskDao.find(demandJiraSubtaskDO1);
            if (JudgeUtils.isEmpty(demandJiraSubtaskDOS)) {

                CreateIssueTestSubtaskRequestBO createIssueTestSubtaskRequestBO = new CreateIssueTestSubtaskRequestBO();
                createIssueTestSubtaskRequestBO.setIssueType(10103);
                if (LemonUtils.getEnv().equals(Env.SIT)) {
                    createIssueTestSubtaskRequestBO.setProject(10100);
                } else {
                    createIssueTestSubtaskRequestBO.setProject(10106);
                }
                createIssueTestSubtaskRequestBO.setParentKey(demandJiraDevelopMasterTaskDOS.get(0).getJiraKey());
                UserDO userDO = new UserDO();
                userDO.setFullname(jiraTaskBodyBO.getAssignee());
                List<UserDO> userDOS = iUserDao.find(userDO);
                createIssueTestSubtaskRequestBO.setAssignee(userDOS.get(0).getUsername());
                String selectTime = DateUtil.date2String(new Date(), "yyyy-MM-dd");
                createIssueTestSubtaskRequestBO.setStartTime(selectTime);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, +7);
                Date time = c.getTime();
                String preDay = sdf.format(time);
                createIssueTestSubtaskRequestBO.setEndTime(preDay);

                createIssueTestSubtaskRequestBO.setSummary("【测试案例编写】" + demandDOS1.get(i).getReqNm());
                createIssueTestSubtaskRequestBO.setDescription("测试案例编写");
                Response response1 = JiraUtil.CreateIssue(createIssueTestSubtaskRequestBO);
                System.err.println(response1.getStatusCode());
                System.err.println(response1.prettyPrint());
                if (response1.getStatusCode() == 201) {
                    CreateIssueResponseBO as = response1.getBody().as(CreateIssueResponseBO.class);
                    DemandJiraSubtaskDO demandJiraSubtaskDO = new DemandJiraSubtaskDO();
                    demandJiraSubtaskDO.setSubtaskkey(as.getKey());
                    demandJiraSubtaskDO.setParenttaskkey(createIssueTestSubtaskRequestBO.getParentKey());
                    demandJiraSubtaskDO.setSubtaskname(createIssueTestSubtaskRequestBO.getSummary());
                    demandJiraSubtaskDao.insert(demandJiraSubtaskDO);
                }


                createIssueTestSubtaskRequestBO.setSummary("【测试案例评审】" + demandDOS1.get(i).getReqNm());
                createIssueTestSubtaskRequestBO.setDescription("测试案例评审");
                response1 = JiraUtil.CreateIssue(createIssueTestSubtaskRequestBO);
                if (response1.getStatusCode() == 201) {
                    CreateIssueResponseBO as = response1.getBody().as(CreateIssueResponseBO.class);
                    DemandJiraSubtaskDO demandJiraSubtaskDO = new DemandJiraSubtaskDO();
                    demandJiraSubtaskDO.setSubtaskkey(as.getKey());
                    demandJiraSubtaskDO.setParenttaskkey(createIssueTestSubtaskRequestBO.getParentKey());
                    demandJiraSubtaskDO.setSubtaskname(createIssueTestSubtaskRequestBO.getSummary());
                    demandJiraSubtaskDao.insert(demandJiraSubtaskDO);
                }

                createIssueTestSubtaskRequestBO.setSummary("【测试案例执行】" + demandDOS1.get(i).getReqNm());
                createIssueTestSubtaskRequestBO.setDescription("测试用例执行");
                response1 = JiraUtil.CreateIssue(createIssueTestSubtaskRequestBO);
                if (response1.getStatusCode() == 201) {
                    CreateIssueResponseBO as = response1.getBody().as(CreateIssueResponseBO.class);
                    DemandJiraSubtaskDO demandJiraSubtaskDO = new DemandJiraSubtaskDO();
                    demandJiraSubtaskDO.setSubtaskkey(as.getKey());
                    demandJiraSubtaskDO.setParenttaskkey(createIssueTestSubtaskRequestBO.getParentKey());
                    demandJiraSubtaskDO.setSubtaskname(createIssueTestSubtaskRequestBO.getSummary());
                    demandJiraSubtaskDao.insert(demandJiraSubtaskDO);
                }
            }

        }
    }
    @Override
    public File getReportForm11(String displayname,String date1,String date2){
        if (org.apache.commons.lang3.StringUtils.isBlank(date1) && org.apache.commons.lang3.StringUtils.isBlank(date2)) {
            MsgEnum.ERROR_CUSTOM.setMsgInfo("");
            MsgEnum.ERROR_CUSTOM.setMsgInfo("请选择日期查询条件：如周、月!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        //设置查询参数
        String[] raqArgs = new String[2];
        raqArgs[0] = displayname;
        String reportId = "RPX-Z-2011_HBST";
        // 查询周
        if (org.apache.commons.lang3.StringUtils.isNotBlank(date1) && org.apache.commons.lang3.StringUtils.isBlank(date2)) {
            reportId = "RPX-Z-2011_HBST";
            raqArgs[1] = date1;
        }
        // 查询月
        if (org.apache.commons.lang3.StringUtils.isNotBlank(date2) && org.apache.commons.lang3.StringUtils.isBlank(date1)) {
            reportId = "RPX-Z-2012_HBST";
            raqArgs[1] = date2;
        }
        GenerateReportBO generateReportBO = new GenerateReportBO();
        generateReportBO.setReportId(reportId);
        generateReportBO.setRaqArgs(raqArgs);
        generateReportBO.setReportStyle("xlsx");
        return genRptService.genRaqRpt(generateReportBO);
    }

    @Override
    public DemandEaseDevelopmentRspBO easeDevelopmentfindList(DemandEaseDevelopmentBO demandEaseDevelopmentBO){
        PageInfo<DemandEaseDevelopmentBO> pageInfo = getPageInfo1(demandEaseDevelopmentBO);
        List<DemandEaseDevelopmentBO> easeDevelopmentBOList = BeanConvertUtils.convertList(pageInfo.getList(), DemandEaseDevelopmentBO.class);
        DemandEaseDevelopmentRspBO easeDevelopmentRspBO = new DemandEaseDevelopmentRspBO();
        easeDevelopmentRspBO.setDemandEaseDevelopmentBOList(easeDevelopmentBOList);
        easeDevelopmentRspBO.setPageInfo(pageInfo);
        return easeDevelopmentRspBO;
    }
    private PageInfo<DemandEaseDevelopmentBO>  getPageInfo1(DemandEaseDevelopmentBO demandEaseDevelopmentBO) {
        DemandEaseDevelopmentDO easeDevelopmentDO = new DemandEaseDevelopmentDO();
        BeanConvertUtils.convert(easeDevelopmentDO, demandEaseDevelopmentBO);
        PageInfo<DemandEaseDevelopmentBO> pageInfo = PageUtils.pageQueryWithCount(demandEaseDevelopmentBO.getPageNum(), demandEaseDevelopmentBO.getPageSize(),
                () -> BeanConvertUtils.convertList(easeDevelopmentExtDao.findList(easeDevelopmentDO), DemandEaseDevelopmentBO.class));
        return pageInfo;
    }
    @Override
    public void easeDevelopmentDown(MultipartFile file){
        File f = null;
        LinkedList<DemandEaseDevelopmentDO> easeDevelopmentDOLinkedList = new LinkedList<>();
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
                DemandEaseDevelopmentDO easeDevelopmentDO = new DemandEaseDevelopmentDO();
                easeDevelopmentDO.setDocumentnumber(map.get(i).get(0).toString().trim());
                easeDevelopmentDO.setProcessstartdate(map.get(i).get(1).toString().trim());
                easeDevelopmentDO.setDevelopmentowner(map.get(i).get(2).toString().trim());
                easeDevelopmentDO.setSupportingmanufacturers(map.get(i).get(3).toString().trim());
                easeDevelopmentDO.setSupportingmanufacturerproducts(map.get(i).get(4).toString().trim());
                easeDevelopmentDO.setCuttype(map.get(i).get(5).toString().trim());
                easeDevelopmentDO.setDemandtheme(map.get(i).get(6).toString().trim());
                easeDevelopmentDO.setRequirementdescription(map.get(i).get(7).toString().trim());
                easeDevelopmentDO.setDevelopmentworkloadassess(map.get(i).get(8).toString().trim());
                easeDevelopmentDO.setDevelopmentworkload(map.get(i).get(9).toString().trim());
                easeDevelopmentDO.setCommissioningdate(map.get(i).get(10).toString().trim());
                easeDevelopmentDO.setAcceptor(map.get(i).get(11).toString().trim());
                easeDevelopmentDO.setAcceptancedate(map.get(i).get(12).toString().trim());
                easeDevelopmentDO.setCostdepartment(map.get(i).get(13).toString().trim());
                easeDevelopmentDO.setSecondlevelorganization(map.get(i).get(14).toString().trim());
                easeDevelopmentDO.setRemark(map.get(i).get(15).toString().trim());
                easeDevelopmentDOLinkedList.add(easeDevelopmentDO);
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
        easeDevelopmentDOLinkedList.forEach(m -> {
            DemandEaseDevelopmentDO supportWorkloadDO1 = new DemandEaseDevelopmentDO();
            supportWorkloadDO1.setDocumentnumber(m.getDocumentnumber());
            List<DemandEaseDevelopmentDO> productionDefectsDOS = easeDevelopmentExtDao.find(supportWorkloadDO1);
            if (JudgeUtils.isEmpty(productionDefectsDOS)) {
                easeDevelopmentExtDao.insert(m);
            } else {
                easeDevelopmentExtDao.update(m);
            }
        });
    }
    @Override
    public void getDownload(HttpServletResponse response, DemandEaseDevelopmentBO demandEaseDevelopmentBO){
        DemandEaseDevelopmentDO easeDevelopmentDO = new DemandEaseDevelopmentDO();
        BeanConvertUtils.convert(easeDevelopmentDO, demandEaseDevelopmentBO);
        List<DemandEaseDevelopmentDO> demandDOList = easeDevelopmentExtDao.findList(easeDevelopmentDO);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandEaseDevelopmentDO.class, demandDOList);
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // 判断数据
            if (workbook == null) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
            // 设置excel的文件名称
            String excelName = "easeDevelopmentDO" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }
    @Override
    public void easeDevelopmentWorkloadCountForDevp(HttpServletRequest request, HttpServletResponse response, DemandEaseDevelopmentBO demandEaseDevelopmentBO ){
        DemandEaseDevelopmentDO easeDevelopmentDO = new DemandEaseDevelopmentDO();
        BeanConvertUtils.convert(easeDevelopmentDO, demandEaseDevelopmentBO);
        // 获取一级团队支撑工作量汇总数据
        List<DemandEaseDevelopmentDO>  mon_input_workload_list = easeDevelopmentExtDao.easeDevelopmentWorkloadCountForDevp(easeDevelopmentDO);
        if(mon_input_workload_list == null || mon_input_workload_list.size()<=0){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("一级团队支撑工作量均为空，请先导入数据!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        //获取所有的一级团队
        List<String> firstLevelOrganizationList = iOrganizationStructureDao.findFirstLevelOrganization(new OrganizationStructureDO());
        List<DemandEaseDevelopment2DO> DevpWorkLoadList = new ArrayList<>();
        BigDecimal sum = new BigDecimal("0");
        for(int i=0;i<firstLevelOrganizationList.size();i++){
            DemandEaseDevelopment2DO demandEaseDevelopment2DO = new DemandEaseDevelopment2DO();
            // 一级团队赋值
            demandEaseDevelopment2DO.setSecondlevelorganization(firstLevelOrganizationList.get(i));
            demandEaseDevelopment2DO.setDevelopmentworkload("0");
            // 判断一级团队是否存在支撑工作量
            for(int j=0;j<mon_input_workload_list.size();j++){
                if(firstLevelOrganizationList.get(i).equals(mon_input_workload_list.get(j).getFirstLevelOrganization())){
                    demandEaseDevelopment2DO.setDevelopmentworkload(mon_input_workload_list.get(j).getDevelopmentworkload());
                }
            }
            sum = sum.add(new BigDecimal(demandEaseDevelopment2DO.getDevelopmentworkload()));
            DevpWorkLoadList.add(demandEaseDevelopment2DO);
        }
        // 汇总数据
        DemandEaseDevelopment2DO supportWorkload2DO = new DemandEaseDevelopment2DO();
        supportWorkload2DO.setSecondlevelorganization("一级团队汇总");
        supportWorkload2DO.setDevelopmentworkload(sum+"");
        DevpWorkLoadList.add(supportWorkload2DO);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandEaseDevelopment2DO.class, DevpWorkLoadList);
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
    public void easeDevelopmentWorkloadCountForDevp2(HttpServletRequest request, HttpServletResponse response, DemandEaseDevelopmentBO demandEaseDevelopmentBO){
        DemandEaseDevelopmentDO easeDevelopmentDO = new DemandEaseDevelopmentDO();
        BeanConvertUtils.convert(easeDevelopmentDO, demandEaseDevelopmentBO);
        // 获取二级团队支撑工作量汇总数据
        List<DemandEaseDevelopmentDO>  mon_input_workload_list = easeDevelopmentExtDao.easeDevelopmentWorkloadCountForDevp2(easeDevelopmentDO);
        if(mon_input_workload_list == null || mon_input_workload_list.size()<=0){
            MsgEnum.ERROR_CUSTOM.setMsgInfo("二级团队支撑工作量均为空，请先导入数据!");
            BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
        }
        //获取所有的二级团队
        List<OrganizationStructureDO> secondlevelorganizationList = iOrganizationStructureDao.find(new OrganizationStructureDO());
        List<DemandEaseDevelopment2DO> DevpWorkLoadList = new ArrayList<>();
        BigDecimal sum = new BigDecimal("0");
        for(int i=0;i<secondlevelorganizationList.size();i++){
            DemandEaseDevelopment2DO supportWorkload2DO = new DemandEaseDevelopment2DO();
            // 一级团队赋值
            supportWorkload2DO.setSecondlevelorganization(secondlevelorganizationList.get(i).getSecondlevelorganization());
            supportWorkload2DO.setDevelopmentworkload("0");
            // 判断一级团队是否存在支撑工作量
            for(int j=0;j<mon_input_workload_list.size();j++){
                if(secondlevelorganizationList.get(i).getSecondlevelorganization().equals(mon_input_workload_list.get(j).getSecondlevelorganization())){
                    supportWorkload2DO.setDevelopmentworkload(mon_input_workload_list.get(j).getDevelopmentworkload());
                }
            }
            sum = sum.add(new BigDecimal(supportWorkload2DO.getDevelopmentworkload()));
            DevpWorkLoadList.add(supportWorkload2DO);
        }
        // 汇总数据
        DemandEaseDevelopment2DO supportWorkload2DO = new DemandEaseDevelopment2DO();
        supportWorkload2DO.setSecondlevelorganization("二级团队汇总");
        supportWorkload2DO.setDevelopmentworkload(sum+"");
        DevpWorkLoadList.add(supportWorkload2DO);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), DemandEaseDevelopment2DO.class, DevpWorkLoadList);
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
