package com.cmpay.lemon.monitor.service.impl.demand;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.ProjectStartBO;
import com.cmpay.lemon.monitor.dao.IDemandExtDao;
import com.cmpay.lemon.monitor.dao.IPlanDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.ProjectStartDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqPlanService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.cmpay.lemon.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 需求计划
 * @author: ty
 */
@Service
public class ReqPlanServiceImpl implements ReqPlanService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReqPlanServiceImpl.class);
    @Autowired
    private IDemandExtDao demandDao;
    @Autowired
    private IPlanDao planDao;

    /**
     * 自注入,解决getAppsByName中调用findAll的缓存不生效问题
     */
    @Autowired
    private ReqPlanService reqPlanService;

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
    public PageInfo<DemandBO> findDemand(DemandBO demandBO) {
        DemandDO demandDO = new DemandDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        PageInfo<DemandBO> pageInfo = PageUtils.pageQueryWithCount(demandBO.getPageNum(), demandBO.getPageSize(),
                () -> BeanConvertUtils.convertList(demandDao.find(demandDO), DemandBO.class));
        return pageInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void add(DemandBO demandBO) {
        try {
            if (!"30".equals(demandBO.getReq_sts()) && !"40".equals(demandBO.getReq_sts())) {
                //修改需求状态
                if ("10".equals(demandBO.getPre_cur_period())) {
                    //提出
                    demandBO.setReq_sts("10");
                } else if ("180".equals(demandBO.getPre_cur_period())) {
                    //完成
                    demandBO.setReq_sts("50");
                } else {
                    //进行中
                    demandBO.setReq_sts("20");
                }
            }
            demandDao.insert(BeanUtils.copyPropertiesReturnDest(new DemandDO(), demandBO));
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void delete(String req_inner_seq) {
        try {
            demandDao.delete(req_inner_seq);
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.DB_DELETE_FAILED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void update(DemandBO demandBO) {
        try {
            if (!"30".equals(demandBO.getReq_sts()) && !"40".equals(demandBO.getReq_sts())) {
                //修改需求状态
                if ("10".equals(demandBO.getPre_cur_period())) {
                    //提出
                    demandBO.setReq_sts("10");
                } else if ("180".equals(demandBO.getPre_cur_period())) {
                    //完成
                    demandBO.setReq_sts("50");
                } else {
                    //进行中
                    demandBO.setReq_sts("20");
                }
            }
            demandDao.update(BeanUtils.copyPropertiesReturnDest(new DemandDO(), demandBO));
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.DB_UPDATE_FAILED);
        }
    }

    @Override
    public List<DemandBO> findAll() {
        return BeanConvertUtils.convertList(demandDao.find(new DemandDO()), DemandBO.class);
    }

    /**
     * 根据内部编号查询项目启动信息
     */
    @Override
    public  ProjectStartBO goProjectStart(String req_inner_seq){
        DemandDO demandDO = demandDao.get(req_inner_seq);
        System.out.println("service："+demandDO.toString());
        ProjectStartDO projectStartDO = new ProjectStartDO();
        projectStartDO.setReq_nm(demandDO.getReq_nm());
        projectStartDO.setReq_no(demandDO.getReq_no());
        projectStartDO.setReq_inner_seq(demandDO.getReq_inner_seq());
        DemandDO demandDO2 =planDao.findBaseChargeEmailByName(req_inner_seq);
        System.out.println("获取邮件："+demandDO2.toString());
        return BeanUtils.copyPropertiesReturnDest(new ProjectStartBO(), projectStartDO);
    }

    /**
     * 获取邮箱
     */
    @Override
    public Map<String,String> getMailbox(String req_inner_seq){
        Map<String, String> resMap = new HashMap<>();
        DemandDO demandDO = new DemandDO();
        //发送的邮箱
        String proMemberEmail = "";
        //抄送的邮箱
        String testDevpEmail = "";
        //基地所有邮箱
        String jdEmail = "";
        //所有部门主管邮箱
        String devpEmail = "";
        //根据内部编号查询所有相关人员
        DemandDO bean = demandDao.get(req_inner_seq);
        //后台开发工程师
        String[] devp_eng = StringUtils.isBlank(bean.getDevp_eng()) ? null : bean.getDevp_eng().split("、");
        String devp_eng_email = "";
        //前端开发工程师
        String[] front_eng = StringUtils.isBlank(bean.getFront_eng()) ? null : bean.getFront_eng().split("、");
        String front_eng_email = "";
        //测试工程师
        String[] test_eng = StringUtils.isBlank(bean.getTest_eng()) ? null : bean.getTest_eng().split("、");
        String test_eng_email = "";
        //配置人员
        String[] config_mng = StringUtils.isBlank(bean.getConfig_mng()) ? null : bean.getConfig_mng().split("、");
        String config_mng_email = "";
        //查询项目成员邮箱(后台开发工程师、项目经理、产品经理、前端开发工程师、测试工程师、配置人员、QA人员)
        if (devp_eng != null) {
            if (devp_eng.length >= 2) {
                for (int i = 0; i < devp_eng.length; i++) {
                    demandDO = planDao.searchUserEmail(devp_eng[i]);
                    if (demandDO != null) {
                        devp_eng_email += demandDO.getMon_remark() + ";";
                    }
                }
            } else {
                demandDO = planDao.searchUserEmail(bean.getDevp_eng());
                if (demandDO != null) {
                    devp_eng_email += demandDO.getMon_remark() + ";";
                }
            }
        }

        if (front_eng != null) {
            if (front_eng.length >= 2) {
                for (int i = 0; i < front_eng.length; i++) {
                    demandDO = planDao.searchUserEmail(front_eng[i]);
                    if (demandDO != null) {
                        front_eng_email += demandDO.getMon_remark() + ";";
                    }
                }
            } else {
                demandDO = planDao.searchUserEmail(bean.getFront_eng());
                if (demandDO != null) {
                    front_eng_email += demandDO.getMon_remark() + ";";
                }
            }
        }

        if (test_eng != null) {
            if (test_eng.length >= 2) {
                for (int i = 0; i < test_eng.length; i++) {
                    demandDO = planDao.searchUserEmail(test_eng[i]);
                    if (demandDO != null) {
                        test_eng_email += demandDO.getMon_remark() + ";";
                    }
                }
            } else {
                demandDO = planDao.searchUserEmail(bean.getTest_eng());
                if (demandDO != null) {
                    test_eng_email += demandDO.getMon_remark() + ";";
                }
            }
        }

        if (config_mng != null) {
            if (config_mng.length >= 2) {
                for (int i = 0; i < config_mng.length; i++) {
                    demandDO = planDao.searchUserEmail(config_mng[i]);
                    if (demandDO != null) {
                        config_mng_email += demandDO.getMon_remark() + ";";
                    }
                }
            } else {
                demandDO = planDao.searchUserEmail(bean.getConfig_mng());
                if (demandDO != null) {
                    config_mng_email += demandDO.getMon_remark() + ";";
                }
            }
        }
        demandDO = planDao.searchOtherUserEmail(req_inner_seq);
        if (demandDO != null) {
            String proMemberEmailAll = devp_eng_email + front_eng_email + test_eng_email + config_mng_email + demandDO.getMon_remark() + ";";
            String[] proMemberEmailSplit = proMemberEmailAll.split(";");
            //去除重复的字符串
            List<String> list = new ArrayList<>();
            for (int i = 0; i < proMemberEmailSplit.length; i++) {
                if (!list.contains(proMemberEmailSplit[i])) {
                    list.add(proMemberEmailSplit[i]);
                }
            }
            for (int i = 0; i < list.size(); i++) {
                proMemberEmail += list.get(i) + ";";
            }
        }
        //查询基地邮箱(需求提出人，需求负责人,开发负责人)
        demandDO = planDao.findBaseChargeEmailByName(req_inner_seq);
        if (demandDO != null) {
            jdEmail = demandDO.getMon_remark() + ";";
        }
        //查询部门邮箱(主导部门和配合部门，去除配合部门测试部)
        demandDO = planDao.findDevpEmail(req_inner_seq);
        if (demandDO != null) {
            devpEmail = demandDO.getMon_remark() + ";";
        }
        //测试部邮箱(田群、吴暇、谭杰、张勇、左娟)
        /*MailGroupBean  mailBean = operationProductionMapper.findMailGroupBeanDetail("4");
        testDevpEmail += mailBean.getMail_user();*/
        resMap.put("proMemberEmail", proMemberEmail);
        //resMap.put("testDevpEmail", testDevpEmail);
        resMap.put("devpEmail", devpEmail);
        resMap.put("jdEmail", jdEmail);
        return resMap;
    }

}
