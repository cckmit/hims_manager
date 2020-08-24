package com.cmpay.lemon.monitor.service.impl.defects;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.dao.IProductionDefectsExtDao;
import com.cmpay.lemon.monitor.dao.ISmokeTestFailedCountDao;
import com.cmpay.lemon.monitor.dao.ISmokeTestRegistrationExtDao;
import com.cmpay.lemon.monitor.entity.DemandDO;
import com.cmpay.lemon.monitor.entity.DemandStateHistoryDO;
import com.cmpay.lemon.monitor.entity.ProductionDefectsDO;
import com.cmpay.lemon.monitor.entity.SmokeTestRegistrationDO;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.defects.DefectsService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

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

    //导出
    @Override
    public void getDownload(HttpServletResponse response, ProductionDefectsBO productionDefectsBO) {
        ProductionDefectsDO productionDefectsDO = new ProductionDefectsDO();
        BeanConvertUtils.convert(productionDefectsDO, productionDefectsBO);
        List<ProductionDefectsDO> demandDOList = productionDefectsDao.findList(productionDefectsDO);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), ProductionDefectsDO.class, demandDOList);
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // 判断数据
            if (workbook == null) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
            // 设置excel的文件名称
            String excelName = "productionDefectsDO_" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }
}
