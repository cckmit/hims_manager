package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.monitor.bo.LogTypeBO;
import com.cmpay.lemon.monitor.dao.ILogTypeDao;
import com.cmpay.lemon.monitor.entity.LogTypeDO;
import com.cmpay.lemon.monitor.service.LogTypeService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhou_xiong
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS,rollbackFor = RuntimeException.class)
public class LogTypeServiceImpl implements LogTypeService {
    @Autowired
    private ILogTypeDao logTypeDao;
    @Override
    public LogTypeBO findAll() {
        List<LogTypeDO> logTypeDOList = logTypeDao.find(new LogTypeDO());
        List<LogTypeBO> logTypeBOList = BeanConvertUtils.convertList(logTypeDOList, LogTypeBO.class);
        LogTypeBO logTypeBO = new LogTypeBO();
        logTypeBO.setLogTypeBOList(logTypeBOList);
        return logTypeBO;
    }
}
