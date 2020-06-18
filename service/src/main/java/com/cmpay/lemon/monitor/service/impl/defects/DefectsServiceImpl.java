package com.cmpay.lemon.monitor.service.impl.defects;


import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.ProductionDefectsBO;
import com.cmpay.lemon.monitor.bo.ProductionDefectsRspBO;
import com.cmpay.lemon.monitor.bo.SmokeTestRegistrationBO;
import com.cmpay.lemon.monitor.bo.SmokeTestRegistrationRspBO;
import com.cmpay.lemon.monitor.dao.IProductionDefectsExtDao;
import com.cmpay.lemon.monitor.dao.ISmokeTestFailedCountDao;
import com.cmpay.lemon.monitor.dao.ISmokeTestRegistrationExtDao;
import com.cmpay.lemon.monitor.entity.ProductionDefectsDO;
import com.cmpay.lemon.monitor.entity.SmokeTestRegistrationDO;
import com.cmpay.lemon.monitor.service.defects.DefectsService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefectsServiceImpl  implements DefectsService {
    @Autowired
    IProductionDefectsExtDao productionDefectsDao;
    @Autowired
    ISmokeTestFailedCountDao smokeTestFailedCountDao;
    @Autowired
    ISmokeTestRegistrationExtDao smokeTestRegistrationDao;

    @Override
    public ProductionDefectsRspBO findDefectAll(ProductionDefectsBO productionDefectsBO) {
        PageInfo<ProductionDefectsBO> pageInfo = getProductionDefectsPageInfo(productionDefectsBO);
        List<ProductionDefectsBO> productionDefectsBOList = BeanConvertUtils.convertList(pageInfo.getList(), ProductionDefectsBO.class);

        ProductionDefectsRspBO productionDefectsRspBO = new ProductionDefectsRspBO();
        productionDefectsRspBO.setProductionDefectsBOList(productionDefectsBOList);
        productionDefectsRspBO.setPageInfo(pageInfo);
        return productionDefectsRspBO;
    }

    @Override
    public SmokeTestRegistrationRspBO smokeTestFailedQuery(SmokeTestRegistrationBO smokeTestRegistrationBO) {
        PageInfo<SmokeTestRegistrationBO> pageInfo = getProductionDefectsPageInfo2(smokeTestRegistrationBO);
        List<SmokeTestRegistrationBO> productionDefectsBOList = BeanConvertUtils.convertList(pageInfo.getList(), SmokeTestRegistrationBO.class);

        SmokeTestRegistrationRspBO smokeTestRegistrationRspBO = new SmokeTestRegistrationRspBO();
        smokeTestRegistrationRspBO.setSmokeTestRegistrationBOList(productionDefectsBOList);
        smokeTestRegistrationRspBO.setPageInfo(pageInfo);
        return smokeTestRegistrationRspBO;
    }

    private PageInfo<ProductionDefectsBO> getProductionDefectsPageInfo(ProductionDefectsBO productionDefectsBO) {
        ProductionDefectsDO productionDefectsDO = new ProductionDefectsDO();
        BeanConvertUtils.convert(productionDefectsDO, productionDefectsBO);
        PageInfo<ProductionDefectsBO> pageInfo = PageUtils.pageQueryWithCount(productionDefectsBO.getPageNum(), productionDefectsBO.getPageSize(),
                () -> BeanConvertUtils.convertList(productionDefectsDao.findList(productionDefectsDO), ProductionDefectsBO.class));
        return pageInfo;
    }

    private PageInfo<SmokeTestRegistrationBO> getProductionDefectsPageInfo2(SmokeTestRegistrationBO smokeTestRegistrationBO) {
        SmokeTestRegistrationDO smokeTestRegistrationDO = new SmokeTestRegistrationDO();
        BeanConvertUtils.convert(smokeTestRegistrationDO, smokeTestRegistrationBO);
        PageInfo<SmokeTestRegistrationBO> pageInfo = PageUtils.pageQueryWithCount(smokeTestRegistrationBO.getPageNum(), smokeTestRegistrationBO.getPageSize(),
                () -> BeanConvertUtils.convertList(smokeTestRegistrationDao.findList(smokeTestRegistrationDO), SmokeTestRegistrationBO.class));
        return pageInfo;
    }
}
