package com.cmpay.lemon.monitor.service.impl.production;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.BeanUtils;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.common.utils.StringUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.security.SecurityUtils;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.DemandBO;
import com.cmpay.lemon.monitor.bo.DemandRspBO;
import com.cmpay.lemon.monitor.bo.ProductionBO;
import com.cmpay.lemon.monitor.bo.ProductionRspBO;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.demand.ReqTaskService;
import com.cmpay.lemon.monitor.service.jira.JiraOperationService;
import com.cmpay.lemon.monitor.service.production.OperationProductionService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import com.cmpay.lemon.monitor.utils.ReadExcelUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.cmpay.lemon.monitor.constant.MonitorConstants.FILE;
import static com.cmpay.lemon.monitor.utils.FileUtils.importExcel;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

/**
 * @author: zhou_xiong
 */
@Service
public class OperationProductionServiceImpl implements OperationProductionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationProductionServiceImpl.class);
    @Autowired
    private IOperationProductionDao IOperationProductionDao;

    @Override
    public ProductionRspBO find(ProductionBO productionBO) {
        PageInfo<ProductionBO> pageInfo = getPageInfo(productionBO);
        List<ProductionBO> productionBOList = BeanConvertUtils.convertList(pageInfo.getList(), ProductionBO.class);
        ProductionRspBO productionRspBO = new ProductionRspBO();
        productionRspBO.setProductionList(productionBOList);
        productionRspBO.setPageInfo(pageInfo);
        return productionRspBO;
    }

    private PageInfo<ProductionBO> getPageInfo(ProductionBO productionBO) {
        ProductionDO productionDO = new ProductionDO();
        BeanConvertUtils.convert(productionDO, productionBO);
        PageInfo<ProductionBO> pageInfo = PageUtils.pageQueryWithCount(productionBO.getPageNum(), productionBO.getPageSize(),
                () -> BeanConvertUtils.convertList(IOperationProductionDao.findPageBreakByCondition(productionDO), ProductionBO.class));
        return pageInfo;
    }
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response, ProductionBO productionBO){
        ProductionDO productionDO = new ProductionDO();
        BeanConvertUtils.convert(productionDO, productionBO);
        List<ProductionDO> list = IOperationProductionDao.findExportExcelListByDate(productionDO);
        System.err.println(list.size());
        list.forEach(m ->{
            System.err.println(m);
        });
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), ProductionDO.class, list);
        try (OutputStream output = response.getOutputStream();
             BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output)) {
            // 判断数据
            if (workbook == null) {
                BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
            }
            // 设置excel的文件名称
            String excelName = "base_" + DateUtil.date2String(new Date(), "yyyyMMddHHmmss") + ".xls";
            response.setHeader(CONTENT_DISPOSITION, "attchement;filename=" + excelName);
            response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, CONTENT_DISPOSITION);
            workbook.write(bufferedOutPut);
            bufferedOutPut.flush();
        } catch (IOException e) {
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        }
    }
    @Override
    public void updateAllProduction(HttpServletRequest request, HttpServletResponse response, String taskIdStr){
//        //UserPrincipal currentUser = (UserPrincipal) SecurityUtils.getSubject().getPrincipal();
//        //获取登录用户名
//        String currentUser =  SecurityUtils.getLoginName();
//        //生成流水记录
//        //ScheduleBean scheduleBean=new ScheduleBean(currentUser);
//        String[] pro_number_list=taskIdStr.split("~");
//        if(pro_number_list[0].equals("1")){
//            //return ajaxDoneError("请填写进行此操作原因");
//        }
//        if(pro_number_list[0].equals("ytc")){
//            if(pro_number_list.length==1){
//                //return ajaxDoneError("请选择投产进行操作!");
//            }
//            for(int i=2;i<pro_number_list.length;i++){
//                ProductionDO beanCheck=IOperationProductionDao.findProductionBean(pro_number_list[i]);
//                if(!(beanCheck.getProStatus().equals("投产提出") || (beanCheck.getProStatus().equals("投产待部署") && beanCheck.getProType().equals("救火更新")))){
//                    //return ajaxDoneError("当前投产状态的投产信息不可修改!");
//                }
//                if(beanCheck.getProductionDeploymentResult().equals("已部署")){
//                    //return ajaxDoneError("当前投产预投产已部署，不可重复操作!");
//                }
//                ProductionDO bean=IOperationProductionDao.findProductionBean(pro_number_list[i]);
//                bean.setProductionDeploymentResult("已部署");
//                operationProductionServiceMgr.updateProductionBean(bean);
//                ScheduleBean sBean=new ScheduleBean();
//                sBean.setPre_operation(bean.getPro_status());
//                ScheduleBean schedule=new ScheduleBean(bean.getPro_number(), currentUser.getUserName(), "预投产已部署", sBean.getPre_operation(), sBean.getPre_operation(), "预投产已提前部署");
//                operationProductionServiceMgr.addScheduleBean(schedule);
//            }
//            return ajaxDoneSuccess("预投产部署状态修改成功");
//        }
    }

}
