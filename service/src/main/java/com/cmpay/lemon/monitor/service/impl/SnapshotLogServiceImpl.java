package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.SnapshotLogBO;
import com.cmpay.lemon.monitor.bo.SnapshotLogQueryBO;
import com.cmpay.lemon.monitor.dao.ISnapshotLogDao;
import com.cmpay.lemon.monitor.entity.SnapshotLogDO;
import com.cmpay.lemon.monitor.service.SnapshotLogService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtils;
import com.cmpay.lemon.monitor.utils.IdGen;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created on 2018/12/17
 *
 * @author: ou_yn
 */
@Service
@Transactional
public class SnapshotLogServiceImpl implements SnapshotLogService {
    @Autowired
    private ISnapshotLogDao snapshotLogDao;

    @Override
    public int insert(SnapshotLogBO snapshotLogBO) {
        SnapshotLogDO snapshotLogDO = new SnapshotLogDO();
        BeanUtils.copyProperties(snapshotLogDO, snapshotLogBO);
        snapshotLogDO.setCreateDate(new Date());
        snapshotLogDO.setId( IdGen.uuid());
        return snapshotLogDao.insert(snapshotLogDO);
    }

    @Override
    public PageInfo<SnapshotLogBO> getSnapshotHtmls(SnapshotLogQueryBO snapshotLogQueryBO) {
        return PageUtils.pageQueryWithCount(snapshotLogQueryBO.getPageNum(), snapshotLogQueryBO.getPageSize(), () -> {
            Date startTime = null;
            Date endTime = null;
            if (snapshotLogQueryBO.getStartTime()!=0) {
                startTime = new Date(snapshotLogQueryBO.getStartTime());
            }
            if (snapshotLogQueryBO.getEndTime()!=0) {
                endTime = DateUtils.addDays(new Date(snapshotLogQueryBO.getEndTime()), 1);
            }
            List<SnapshotLogDO> snapshotLogDOS = snapshotLogDao.querySnapshotList(snapshotLogQueryBO.getTitle(), startTime, endTime);
            return BeanConvertUtils.convertList(snapshotLogDOS, SnapshotLogBO.class);
        });
    }

}
