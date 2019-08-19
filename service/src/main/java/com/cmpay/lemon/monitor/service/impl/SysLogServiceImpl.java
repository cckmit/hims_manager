package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.SysLogBO;
import com.cmpay.lemon.monitor.bo.SysLogPageBO;
import com.cmpay.lemon.monitor.dao.ISysLogDao;
import com.cmpay.lemon.monitor.entity.SysLogDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SysLogService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 系统日志表 服务实现类
 * Created on 2018/12/24
 *
 * @author zhou_xiong
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS,rollbackFor = RuntimeException.class)
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private ISysLogDao sysLogDao;

    @Override
    public PageInfo<SysLogBO> pageQuery(SysLogPageBO sysLogPageBO) {
       return PageUtils.pageQueryWithCount(sysLogPageBO.getPageNum(), sysLogPageBO.getPageSize(), () -> {
            Date startTime = null;
            Date endTime = null;
            if (sysLogPageBO.getStartTime()!=0) {
                startTime = new Date(sysLogPageBO.getStartTime());
            }
            if (sysLogPageBO.getEndTime()!=0) {
                endTime = DateUtils.addDays(new Date(sysLogPageBO.getEndTime()), 1);
            }
            List<SysLogDO> sysLogDOList = sysLogDao.pageQuery(sysLogPageBO.getUserNo(), sysLogPageBO.getOperation(), sysLogPageBO.getRequestUri(), startTime, endTime);
            return BeanConvertUtils.convertList(sysLogDOList, SysLogBO.class);
        });
    }

    @Override
    public void insert(SysLogBO sysLogBO) {
        SysLogDO sysLogDO = BeanUtils.copyPropertiesReturnDest(new SysLogDO(), sysLogBO);
        int number = sysLogDao.insert(sysLogDO);
        if (number != 1) {
            BusinessException.throwBusinessException( MsgEnum.DB_INSERT_FAILED);
        }
    }
}
