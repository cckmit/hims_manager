package com.cmpay.lemon.monitor.service.impl.systemOperation;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.dao.IOperationApplicationDao;
import com.cmpay.lemon.monitor.dao.IOperationProductionDao;
import com.cmpay.lemon.monitor.dao.IProductionPicDao;
import com.cmpay.lemon.monitor.entity.Constant;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.entity.sendemail.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.productTime.ProductTimeService;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.service.systemOperation.OperationApplicationService;
import com.cmpay.lemon.monitor.utils.*;
import com.jcraft.jsch.*;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * 投产管理：查询及状态变更
 */
@Service
public class OperationApplicationServiceImpl implements OperationApplicationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationApplicationServiceImpl.class);
    @Autowired
    private IOperationApplicationDao operationApplicationDao;

    /**
     * 系统操作分页查询
     * @param productionBO
     * @return
     */
    @Override
    public OperationApplicationRspBO find(OperationApplicationBO productionBO) {
        PageInfo<OperationApplicationBO> pageInfo = getPageInfo(productionBO);
        List<OperationApplicationBO> productionBOList = BeanConvertUtils.convertList(pageInfo.getList(), OperationApplicationBO.class);
        OperationApplicationRspBO productionRspBO = new OperationApplicationRspBO();
        productionRspBO.setProductionList(productionBOList);
        productionRspBO.setPageInfo(pageInfo);
        return productionRspBO;
    }

    private PageInfo<OperationApplicationBO> getPageInfo(OperationApplicationBO productionBO) {
        OperationApplicationDO productionDO = new OperationApplicationDO();
        BeanConvertUtils.convert(productionDO, productionBO);
        PageInfo<OperationApplicationBO> pageInfo = PageUtils.pageQueryWithCount(productionBO.getPageNum(), productionBO.getPageSize(),
                () -> BeanConvertUtils.convertList(operationApplicationDao.findPageBreakByCondition(productionDO), OperationApplicationBO.class));
        return pageInfo;
    }
}
