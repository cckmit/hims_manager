package com.cmpay.lemon.monitor.service.impl.defects;


import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.ProductionDefectsBO;
import com.cmpay.lemon.monitor.bo.ProductionDefectsRspBO;
import com.cmpay.lemon.monitor.dao.IProductionDefectsExtDao;
import com.cmpay.lemon.monitor.entity.ProductionDefectsDO;
import com.cmpay.lemon.monitor.service.defects.DefectsService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefectsServiceImpl  implements DefectsService {
    @Autowired
    IProductionDefectsExtDao productionDefectsDao;

    @Override
    public ProductionDefectsRspBO findDefectAll(ProductionDefectsBO productionDefectsBO) {
        PageInfo<ProductionDefectsBO> pageInfo = getPageInfo(productionDefectsBO);
        List<ProductionDefectsBO> productionDefectsBOList = BeanConvertUtils.convertList(pageInfo.getList(), ProductionDefectsBO.class);

        ProductionDefectsRspBO productionDefectsRspBO = new ProductionDefectsRspBO();
        productionDefectsRspBO.setProductionDefectsBOList(productionDefectsBOList);
        productionDefectsRspBO.setPageInfo(pageInfo);
        return productionDefectsRspBO;
    }

    private PageInfo<ProductionDefectsBO> getPageInfo(ProductionDefectsBO productionDefectsBO) {
        ProductionDefectsDO productionDefectsDO = new ProductionDefectsDO();
        BeanConvertUtils.convert(productionDefectsDO, productionDefectsBO);
        PageInfo<ProductionDefectsBO> pageInfo = PageUtils.pageQueryWithCount(productionDefectsBO.getPageNum(), productionDefectsBO.getPageSize(),
                () -> BeanConvertUtils.convertList(productionDefectsDao.findAll(productionDefectsDO), ProductionDefectsBO.class));
        return pageInfo;
    }
}
