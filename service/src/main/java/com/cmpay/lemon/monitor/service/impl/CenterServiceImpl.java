package com.cmpay.lemon.monitor.service.impl;

import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.CenterBO;
import com.cmpay.lemon.monitor.dao.ICenterDao;
import com.cmpay.lemon.monitor.entity.CenterDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.CenterService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.ALL;

/**
 * @author: zhou_xiong
 */
@Service
public class CenterServiceImpl implements CenterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CenterServiceImpl.class);
    @Autowired
    private ICenterDao centerDao;
    /**
     * 自注入,解决getAppsByName中调用findAll的缓存不生效问题
     */
    @Autowired
    private CenterService centerService;

    @Override
    public CenterBO findById(Long centerId) {
        CenterDO centerDO = centerDao.get(centerId);
        if (JudgeUtils.isNull(centerDO)) {
            LOGGER.warn("id为[{}]的记录不存在", centerId);
            BusinessException.throwBusinessException(MsgEnum.DB_FIND_FAILED);
        }
        return BeanUtils.copyPropertiesReturnDest(new CenterBO(), centerDO);
    }

    @Override
    public PageInfo<CenterBO> findCenters(CenterBO centerBO) {
        CenterDO centerDO = new CenterDO();
        if (JudgeUtils.isBlank(centerBO.getCenterName())) {
            centerBO.setCenterName(null);
        }
        centerDO.setCenterName(centerBO.getCenterName());
        PageInfo<CenterBO> pageInfo = PageUtils.pageQueryWithCount(centerBO.getPageNum(), centerBO.getPageSize(),
                () -> BeanConvertUtils.convertList(centerDao.find(centerDO), CenterBO.class));
        return pageInfo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @CachePut(value = "common", cacheResolver = "jCacheCacheResolver")
    public void add(CenterBO centerBO) {
        try {
            centerDao.insert(BeanUtils.copyPropertiesReturnDest(new CenterDO(), centerBO));
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.DB_INSERT_FAILED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @CacheEvict(value = "common", cacheResolver = "jCacheCacheResolver")
    public void delete(Long id) {
        if (id == 1) {
            BusinessException.throwBusinessException(MsgEnum.DB_CANNOT_DELETE);
        }
        try {
            centerDao.delete(id);
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.DB_DELETE_FAILED);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    @CachePut(value = "common", cacheResolver = "jCacheCacheResolver")
    public void update(CenterBO centerBO) {
        try {
            centerDao.update(BeanUtils.copyPropertiesReturnDest(new CenterDO(), centerBO));
        } catch (Exception e) {
            BusinessException.throwBusinessException(MsgEnum.DB_UPDATE_FAILED);
        }
    }

    @Override
    @Cacheable(value = "common", cacheResolver = "jCacheCacheResolver")
    public List<CenterBO> findAll() {
        return BeanConvertUtils.convertList(centerDao.find(new CenterDO()), CenterBO.class);
    }

    @Override
    public List<String> getAppsByName(String centerName) {
        if (StringUtils.equals(centerName, ALL)) {
            List<String> apps = new ArrayList<>(32);
            centerService.findAll().stream().filter(c -> !StringUtils.equals(c.getCenterName(), ALL))
                    .map(bo -> StringUtils.split(bo.getApp(), ";")).map(Arrays::asList).forEach(apps::addAll);
            return apps;
        } else {
            return centerService.findAll().stream().filter(c -> StringUtils.equals(centerName, c.getCenterName()))
                    .map(centerBO -> StringUtils.split(centerBO.getApp(), ";")).map(Arrays::asList).findFirst().get();
        }
    }
}
