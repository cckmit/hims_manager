package com.cmpay.lemon.monitor.service.impl.preproduction;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cmpay.lemon.common.Env;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.LemonUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.Constant;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.entity.sendemail.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.SystemUserService;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.preproduction.PreProductionService;
import com.cmpay.lemon.monitor.service.productTime.ProductTimeService;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.utils.*;
import com.cmpay.lemon.monitor.utils.wechatUtil.schedule.BoardcastScheduler;
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
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * 预投产管理：查询及状态变更
 */
@Service
public class PreProductionServiceImpl implements PreProductionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PreProductionServiceImpl.class);
    @Autowired
    private IPreproductionExtDao iPreproductionExtDao;
    @Autowired
    private IOperationApplicationDao operationApplicationDao;

    @Autowired
    private IProductionPicDao productionPicDao;
    @Autowired
    private ProductTimeService productTimeService;
    @Autowired
    private ReqTaskService reqTaskService;
    @Autowired
    IPermiUserDao permiUserDao;
    @Autowired
    ITPermiDeptDao permiDeptDao;
    @Autowired
    IDemandExtDao demandDao;

    @Autowired
    SystemUserService userService;

    @Override
    public PreproductionRspBO find(PreproductionBO productionBO) {
        PageInfo<PreproductionBO> pageInfo = getPageInfo(productionBO);
        System.err.println(pageInfo.getSize());
        List<PreproductionBO> productionBOList = BeanConvertUtils.convertList(pageInfo.getList(), PreproductionBO.class);
        PreproductionRspBO productionRspBO = new PreproductionRspBO();
        productionRspBO.setPreproductionBOList(productionBOList);
        productionRspBO.setPageInfo(pageInfo);
        return productionRspBO;
    }

    private PageInfo<PreproductionBO> getPageInfo(PreproductionBO productionBO) {
        PreproductionDO preproductionDO = new PreproductionDO();
        BeanConvertUtils.convert(preproductionDO, productionBO);
        System.err.println(preproductionDO.toString());
        System.err.println(iPreproductionExtDao.find(preproductionDO).size());
        PageInfo<PreproductionBO> pageInfo = PageUtils.pageQueryWithCount(productionBO.getPageNum(), productionBO.getPageSize(),
                () -> BeanConvertUtils.convertList(iPreproductionExtDao.find(preproductionDO), PreproductionBO.class));
        return pageInfo;
    }

}
