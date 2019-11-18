package com.cmpay.lemon.monitor.service.impl.error;

import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionRspBO;
import com.cmpay.lemon.monitor.bo.ErcdmgErrorComditionBO;
import com.cmpay.lemon.monitor.dao.IErcdmgErorDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.ErcdmgErrorComditionDO;
import com.cmpay.lemon.monitor.service.errror.ErrorService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 需求计划
 * @author: ty
 */
@Service
public class ErrorServiceImpl implements ErrorService {
    @Autowired
    private IErcdmgErorDao iErcdmgErorDao;
    @Override
    public ErcdmgErrorComditionRspBO searchErroeList (ErcdmgErrorComditionBO ercdmgErrorComditionBO){
        PageInfo<ErcdmgErrorComditionBO> pageInfo = getPageInfo(ercdmgErrorComditionBO);
        List<ErcdmgErrorComditionBO> productionBOList = BeanConvertUtils.convertList(pageInfo.getList(), ErcdmgErrorComditionBO.class);
        ErcdmgErrorComditionRspBO productionRspBO = new ErcdmgErrorComditionRspBO();
        productionRspBO.setErcdmgErrorComditionBOList(productionBOList);
        productionRspBO.setPageInfo(pageInfo);
        return  productionRspBO;
    }
    private PageInfo<ErcdmgErrorComditionBO> getPageInfo(ErcdmgErrorComditionBO demandBO) {
        ErcdmgErrorComditionDO demandDO = new ErcdmgErrorComditionDO();
        BeanConvertUtils.convert(demandDO, demandBO);
        System.err.println(demandDO);
        System.err.println(demandBO);
        PageInfo<ErcdmgErrorComditionBO> pageInfo = PageUtils.pageQueryWithCount(demandBO.getPageNum(), demandBO.getPageSize(),
                () -> BeanConvertUtils.convertList(iErcdmgErorDao.findErcdmgErrorList(demandDO), ErcdmgErrorComditionBO.class));
        return pageInfo;
    }
}
