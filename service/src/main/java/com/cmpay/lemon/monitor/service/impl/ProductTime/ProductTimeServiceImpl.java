package com.cmpay.lemon.monitor.service.impl.ProductTime;

import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.monitor.bo.ProductionTimeBO;
import com.cmpay.lemon.monitor.dao.IProductionTimeDao;
import com.cmpay.lemon.monitor.entity.ProductionTimeD0;
import com.cmpay.lemon.monitor.service.productTime.ProductTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ProductTimeServiceImpl  implements ProductTimeService {
    @Autowired
    IProductionTimeDao productionTimeDao ;

    // 查询所有投产时间设置的详细信息
    @Override
    public List<ProductionTimeBO> productTimeList() {
        List<ProductionTimeBO> productionTimeBOS = new LinkedList<>();
        List<ProductionTimeD0> productTimeDOList = productionTimeDao.findProductTimeList();
        productTimeDOList.forEach(m->{
            productionTimeBOS.add(BeanUtils.copyPropertiesReturnDest(new ProductionTimeBO(), m));
        });
        return productionTimeBOS;
    }

    // 通过标示查询投产时间信息
    @Override
    public String findProductTimeByID(Integer id) {
        return productionTimeDao.findProductTimeByID(id);
    }

    // 通过标示查询投产时间详细信息
    @Override
    public ProductionTimeBO findProductTimeBeanByID(Integer id){
        ProductionTimeD0 productTimeDO = productionTimeDao.findProductTimeBeanByID(id);
        ProductionTimeBO productionTimeBO = BeanUtils.copyPropertiesReturnDest(new ProductionTimeBO(), productTimeDO);
        return productionTimeBO;
    }

    // 更新投产时间信息
    @Override
    public void updateProductTime(ProductionTimeBO productTimeBO) {
        productionTimeDao.update(BeanUtils.copyPropertiesReturnDest(new ProductionTimeD0(), productTimeBO));

    }

}
