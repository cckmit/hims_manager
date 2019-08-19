package com.cmpay.lemon.monitor.service.impl.demand;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.dao.IDemandExtDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.cmpay.lemon.monitor.utils.FileUtils.importExcel;

/**
 * @author: zhou_xiong
 */
@Service
public class ReqTaskServiceImpl implements ReqTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReqTaskServiceImpl.class);
    @Autowired
    private IDemandExtDao demandDao;
    /**
     * 自注入,解决getAppsByName中调用findAll的缓存不生效问题
     */
    @Autowired
    private ReqTaskService reqTaskService;

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
    public PageInfo<DemandBO> find(DemandBO demandBO) {
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
            //校验数据
            checkReqTask(demandBO);

            // 判断需求编号或需求名称是否重复
            List<DemandBO> list = reqTaskService.getReqTaskByUK(demandBO);
            if (list.size() > 0) {
                BusinessException.throwBusinessException(MsgEnum.NON_UNIQUE);
            }

            //设置默认值
            demandBO.getReq_type();
            String month = DateUtil.date2String(new Date(), "yyyy-MM");
            demandBO.setReq_impl_mon(month);
            demandBO.setReq_start_mon(month);
            demandBO.setReq_inner_seq(reqTaskService.getNextInnerSeq());
            demandBO.setQa_mng("刘桂娟");
            demandBO.setConfig_mng("黄佳海");
            demandBO.setReq_abnor_type("01");

            setDefaultValue(demandBO);
            setDefaultUser(demandBO);
            setReqSts(demandBO);

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
    public void deleteBatch(List<String> ids) {
        try {
            ids.forEach(demandDao::delete);
        } catch (Exception e) {
            LOGGER.error("delete error:", e);
            BusinessException.throwBusinessException(MsgEnum.DB_DELETE_FAILED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void update(DemandBO demandBO) {
        try {
            //校验数据
            checkReqTask(demandBO);

            //设置默认值
            setDefaultValue(demandBO);
            setDefaultUser(demandBO);
            setReqSts(demandBO);

            demandDao.update(BeanUtils.copyPropertiesReturnDest(new DemandDO(), demandBO));
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.DB_UPDATE_FAILED);
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
        if (reqNo.length == 3){
            if ((("REQ".equals(reqNo[0]) || "REQJIRA".equals(reqNo[0]))
                    && reqNo[1].matches("^\\d{8}$") && reqNo[2].matches("^\\d{4}$"))){
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
        int inpoyRes = demandBO.getInput_res();
        int devCycle = demandBO.getDev_cycle();
        Double expInput = new BigDecimal(new Double(inpoyRes) * new Double(devCycle))
                .divide(new BigDecimal(String.valueOf(22)), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //Double expInput = (double) (inpoyRes * devCycle / 22);
        demandBO.setExp_input(expInput);
    }

    /**
     * 设置用户
     *
     * @param demandBO
     */
    public void setDefaultUser(DemandBO demandBO) {
        if (StringUtils.isBlank(demandBO.getCreat_user())){
            demandBO.setCreat_user(SecurityUtils.getLoginUserId());
            demandBO.setCreat_time(new Date());
        }
        demandBO.setUpdate_user(SecurityUtils.getLoginUserId());
        demandBO.setUpdate_time(new Date());
    }

    /**
     * 变更需求状态
     *
     * @param demandBO
     */
    public void setReqSts(DemandBO demandBO) {
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
    }

    /**
     * 变更需求状态
     *
     * @param demandBO
     */
    public void checkReqTask(DemandBO demandBO) {
        //判断编号是否规范
        String reqNo = demandBO.getReq_no();
        if (StringUtils.isNotBlank(reqNo) && !reqTaskService.checkNumber(reqNo)){
            BusinessException.throwBusinessException(MsgEnum.ERROR_REQ_NO);
        }

        //如果选择取消或暂停，月初备注不能为空
        if("30".equals(demandBO.getReq_sts()) && StringUtils.isBlank(demandBO.getMon_remark())
                ||"40".equals(demandBO.getReq_sts()) && StringUtils.isBlank(demandBO.getMon_remark())){
            BusinessException.throwBusinessException(MsgEnum.NULL_REMARK);
        }

        //主导部门和配合部门不能相同
        if(StringUtils.isNotBlank(demandBO.getDevp_coor_dept())){
            String[] devp_coor_dept=demandBO.getDevp_coor_dept().split(",");
            for (int  i = 0; i < devp_coor_dept.length;i++){
                if(devp_coor_dept[i].equals(demandBO.getDevp_lead_dept())){
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
            String maxInnerSeq = reqTask.getReq_inner_seq();
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
        return BeanUtils.copyPropertiesReturnDest(new DemandBO(), demandDao.getMaxInnerSeq());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void doBatchImport(MultipartFile file) {
        List<DemandDO> demandDOS = importExcel(file, 0, 1, DemandDO.class);
        demandDOS.forEach(m -> {
//            m.setCreateTime(DateTimeUtils.getCurrentLocalDateTime());
//            m.setUpdateTime(DateTimeUtils.getCurrentLocalDateTime());
            try {
                demandDao.insert(m);
            } catch (Exception e) {
                LOGGER.error("msgCode为[{}]的记录insert失败", m.getReq_nm(), e);
                BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
            }
        });
    }
}
