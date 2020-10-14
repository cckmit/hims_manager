package com.cmpay.lemon.monitor.service.impl.defects;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.cmpay.lemon.common.exception.BusinessException;
import com.cmpay.lemon.common.utils.JudgeUtils;
import com.cmpay.lemon.framework.page.PageInfo;
import com.cmpay.lemon.framework.utils.PageUtils;
import com.cmpay.lemon.monitor.bo.*;
import com.cmpay.lemon.monitor.dao.*;
import com.cmpay.lemon.monitor.entity.*;
import com.cmpay.lemon.monitor.enums.MsgEnum;
import com.cmpay.lemon.monitor.service.defects.DefectsService;
import com.cmpay.lemon.monitor.utils.BeanConvertUtils;
import com.cmpay.lemon.monitor.utils.DateUtil;
import com.cmpay.lemon.monitor.utils.ReadExcelUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.beans.Transient;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Service
public class DefectsServiceImpl  implements DefectsService {
    @Autowired
    IProductionDefectsExtDao productionDefectsDao;
    @Autowired
    ISmokeTestFailedCountExtDao smokeTestFailedCountDao;
    @Autowired
    ISmokeTestRegistrationExtDao smokeTestRegistrationDao;
    @Autowired
    IZenQuestiontExtDao zenQuestiontExtDao;
    @Autowired
    private IUserExtDao iUserDao;
    @Autowired
    IDefectDetailsExtDao defectDetailsDao;

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
    public void getDownloadTest(HttpServletResponse response, SmokeTestRegistrationBO smokeTestRegistrationBO) {
        SmokeTestRegistrationDO smokeTestRegistrationDO = new SmokeTestRegistrationDO();
        BeanConvertUtils.convert(smokeTestRegistrationDO, smokeTestRegistrationBO);
        List<SmokeTestRegistrationDO> demandDOList = smokeTestRegistrationDao.findList(smokeTestRegistrationDO);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), SmokeTestRegistrationDO.class, demandDOList);
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void zennDataImport(MultipartFile file){
        File f = null;
        LinkedList<ZenQuestiontDO> zenQuestiontDOLinkedList = new LinkedList<>();
        try {
            //MultipartFile转file
            String originalFilename = file.getOriginalFilename();
            //获取后缀名
            String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if (suffix.equals("xls")) {
                suffix = ".xls";
            } else if (suffix.equals("xlsm") || suffix.equals("xlsx")) {
                suffix = ".xlsx";
            } else {
                MsgEnum.ERROR_CUSTOM.setMsgInfo("文件类型错误!");
                BusinessException.throwBusinessException(MsgEnum.ERROR_CUSTOM);
            }
            f = File.createTempFile("tmp", suffix);
            file.transferTo(f);
            String filepath = f.getPath();
            //excel转java类
            ReadExcelUtils excelReader = new ReadExcelUtils(filepath);
            Map<Integer, Map<Integer, Object>> map = excelReader.readExcelContent();
            for (int i = 1; i <= map.size(); i++) {
//                `bugNumber` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'BUG编号',
//                        `belongProducts` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '所属产品',
//                        `belongModule` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '所属模块',
//                        `belongProject` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '所属项目',
//                        `relatedDemand` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '相关需求',
//                        `relatedTask` varchar(2555) COLLATE utf8_bin DEFAULT NULL COMMENT '相关任务',
//                        `bugTitle` varchar(2555) COLLATE utf8_bin DEFAULT NULL COMMENT 'Bug标题',
//                        `keyword` varchar(2048) COLLATE utf8_bin DEFAULT NULL COMMENT '关键词',
//                        `severity` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '严重程度',
//                        `priority` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '优先级',
//                        `bugType` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Bug类型',
//                        `operatingSystem` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '操作系统',
//                        `browser` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '浏览器',
//                        `repeatSteps` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '重现步骤',
//                        `bugStatus` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'Bug状态',
//                        `expirationDate` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '截止日期',
//                        `activateNumber` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '激活次数',
//                        `whetherConfirm` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '是否确认',
//                        `carbonCopy` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '抄送给',
//                        `creator` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '由谁创建',
//                        `createdDate` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '创建日期',
//                        `affectsVersion` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '影响版本',
//                        `assigned` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '指派给',
//                        `assignedDate` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '指派日期',
//                        `solver` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '解决者',
//                        `solution` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '解决方案',
//                        `solveVersion` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '解决版本',
//                        `solveDate` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '解决日期',
//                        `shutPerson` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '由谁关闭',
//                        `shutDate` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '关闭日期',
//                        `repetitionId` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '重复ID',
//                        `relatedBug` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '相关Bug',
//                        `relatedCase` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '相关用例',
//                        `lastReviser` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '最后修改者',
//                        `changedDate` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '修改日期',
//                        `accessory` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '附件',
//                        `secondlevelorganization` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '二级主导部门',
                ZenQuestiontDO zenQuestiontDO = new ZenQuestiontDO();
                //因为纯数字的导入会变成double 多个.0
                if (!JudgeUtils.isEmpty(map.get(i).get(0).toString().trim())) {
                    zenQuestiontDO.setBugnumber((int)Double.parseDouble(map.get(i).get(0).toString().trim())+"");
                }
                zenQuestiontDO.setBelongproducts(map.get(i).get(1).toString().trim());
                zenQuestiontDO.setBelongmodule(map.get(i).get(2).toString().trim());
                zenQuestiontDO.setBelongproject(map.get(i).get(3).toString().trim());
                zenQuestiontDO.setRelateddemand(map.get(i).get(4).toString().trim());
                zenQuestiontDO.setRelatedtask(map.get(i).get(5).toString().trim());
                zenQuestiontDO.setBugtitle(map.get(i).get(6).toString().trim());
                zenQuestiontDO.setKeyword(map.get(i).get(7).toString().trim());
                if (!JudgeUtils.isEmpty(map.get(i).get(8).toString().trim())) {
                    zenQuestiontDO.setSeverity((int)Double.parseDouble(map.get(i).get(8).toString().trim())+"");
                }
                if (!JudgeUtils.isEmpty(map.get(i).get(9).toString().trim())) {
                    zenQuestiontDO.setPriority((int)Double.parseDouble(map.get(i).get(9).toString().trim())+"");
                }
                zenQuestiontDO.setBugtype(map.get(i).get(10).toString().trim());
                zenQuestiontDO.setOperatingsystem(map.get(i).get(11).toString().trim());
                zenQuestiontDO.setBrowser(map.get(i).get(12).toString().trim());
                zenQuestiontDO.setRepeatsteps(map.get(i).get(13).toString().trim());
                zenQuestiontDO.setBugstatus(map.get(i).get(14).toString().trim());
                //截至日期
                if (map.get(i).get(15) instanceof String) {
                    zenQuestiontDO.setExpirationdate(map.get(i).get(15).toString().trim());
                }
                if (map.get(i).get(15) instanceof Date) {
                    Date date = (Date)map.get(i).get(15);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dt = simpleDateFormat.format(date);
                    zenQuestiontDO.setExpirationdate(dt.trim());
                }
                if (!JudgeUtils.isEmpty(map.get(i).get(16).toString().trim())) {
                    zenQuestiontDO.setActivatenumber((int)Double.parseDouble(map.get(i).get(16).toString().trim())+"");
                }
                zenQuestiontDO.setWhetherconfirm(map.get(i).get(17).toString().trim());
                zenQuestiontDO.setCarboncopy(map.get(i).get(18).toString().trim());
                zenQuestiontDO.setCreator(map.get(i).get(19).toString().trim());
                //创建日期
                if (map.get(i).get(20) instanceof String) {
                    zenQuestiontDO.setCreateddate(map.get(i).get(20).toString().trim());
                }
                if (map.get(i).get(20) instanceof Date) {
                    Date date = (Date)map.get(i).get(20);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dt = simpleDateFormat.format(date);
                    zenQuestiontDO.setCreateddate(dt.trim());
                }
                zenQuestiontDO.setAffectsversion(map.get(i).get(21).toString().trim());
                zenQuestiontDO.setAssigned(map.get(i).get(22).toString().trim());
                //指派日期
                if (map.get(i).get(23) instanceof String) {
                    zenQuestiontDO.setAssigneddate(map.get(i).get(23).toString().trim());
                }
                if (map.get(i).get(23) instanceof Date) {
                    Date date = (Date)map.get(i).get(23);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dt = simpleDateFormat.format(date);
                    zenQuestiontDO.setAssigneddate(dt.trim());
                }
                zenQuestiontDO.setSolver(map.get(i).get(24).toString().trim());
                zenQuestiontDO.setSolution(map.get(i).get(25).toString().trim());
                zenQuestiontDO.setSolveversion(map.get(i).get(26).toString().trim());
                //解决日期
                if (map.get(i).get(27) instanceof String) {
                    zenQuestiontDO.setSolvedate(map.get(i).get(27).toString().trim());
                }
                if (map.get(i).get(27) instanceof Date) {
                    Date date = (Date)map.get(i).get(27);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dt = simpleDateFormat.format(date);
                    zenQuestiontDO.setSolvedate(dt.trim());
                }
                zenQuestiontDO.setShutperson(map.get(i).get(28).toString().trim());
                //关闭日期
                if (map.get(i).get(29) instanceof String) {
                    zenQuestiontDO.setShutdate(map.get(i).get(29).toString().trim());
                }
                if (map.get(i).get(29) instanceof Date) {
                    Date date = (Date)map.get(i).get(29);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dt = simpleDateFormat.format(date);
                    zenQuestiontDO.setShutdate(dt.trim());
                }
                zenQuestiontDO.setRepetitionid(map.get(i).get(30).toString().trim());
                zenQuestiontDO.setRelatedbug(map.get(i).get(31).toString().trim());
                zenQuestiontDO.setRelatedcase(map.get(i).get(32).toString().trim());
                zenQuestiontDO.setLastreviser(map.get(i).get(33).toString().trim());
                //修改日期
                if (map.get(i).get(34) instanceof String) {
                    zenQuestiontDO.setChangeddate(map.get(i).get(34).toString().trim());
                }
                if (map.get(i).get(34) instanceof Date) {
                    Date date = (Date)map.get(i).get(34);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String dt = simpleDateFormat.format(date);
                    zenQuestiontDO.setChangeddate(dt.trim());
                }
                zenQuestiontDO.setAccessory(map.get(i).get(35).toString().trim());
                //二级主导部门
                //根据解决人查询部门，如果不存在则没有部门。
                // 解决不为空，则根据姓名查询部门
                if(JudgeUtils.isNotBlank(zenQuestiontDO.getSolver())){
                    UserDO userDO = new UserDO();
                    userDO.setFullname(zenQuestiontDO.getSolver());
                    List<UserDO> userDOS = iUserDao.find(userDO);
                    if(!userDOS.isEmpty()){
                        zenQuestiontDO.setSecondlevelorganization(userDOS.get(0).getDepartment());
                    }else{
                        zenQuestiontDO.setSecondlevelorganization("");
                    }
                }
                zenQuestiontDOLinkedList.add(zenQuestiontDO);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        } catch (Exception e) {
            e.printStackTrace();
            BusinessException.throwBusinessException(MsgEnum.BATCH_IMPORT_FAILED);
        } finally {
            f.delete();
        }
        zenQuestiontDOLinkedList.forEach(m -> {
            ZenQuestiontDO zenQuestiontDO1 = new ZenQuestiontDO();
            //工具主键查询是否存在
            zenQuestiontDO1.setBugnumber(m.getBugnumber());
            List<ZenQuestiontDO> zenQuestiontDOList = zenQuestiontExtDao.find(zenQuestiontDO1);
            if (JudgeUtils.isEmpty(zenQuestiontDOList)) {
                zenQuestiontExtDao.insert(m);
            } else {
                zenQuestiontExtDao.update(m);
            }
            //有部门的才添加缺陷
            if(m.getSecondlevelorganization()!=null && m.getSecondlevelorganization()!=""){
                //无效问题不添加
                if(!"不予解决".equals(m.getSolution()) && !"设计如此".equals(m.getSolution())
                        && !"外部原因".equals(m.getSolution()) && !"重复BUG".equals(m.getSolution()) ){
                    DefectDetailsDO defectDetailsDO = new DefectDetailsDO();
                    defectDetailsDO.setJireKey(m.getBugnumber());
                    defectDetailsDO.setEpicKey(m.getBugnumber());
                    defectDetailsDO.setDefectName(m.getBugtitle());
                    defectDetailsDO.setDefectsDepartment(m.getSecondlevelorganization());
                    defectDetailsDO.setRegistrationDate(m.getCreateddate()+" 00:00:00");
                    defectDetailsDO.setTestNumber(0);
                    defectDetailsDO.setDefectDetails(m.getRepeatsteps());
                    defectDetailsDO.setDefectRegistrant(m.getCreator());
                    defectDetailsDO.setAssignee(m.getSolver());
                    if("已解决".equals(m.getBugstatus()) || "已关闭".equals(m.getBugstatus()) ){
                        defectDetailsDO.setDefectStatus("关闭");
                    }else{
                        defectDetailsDO.setDefectStatus("待处理");
                    }
                    DefectDetailsDO defectDetailsDO1 = defectDetailsDao.get(defectDetailsDO.getJireKey());
                    if (JudgeUtils.isNull(defectDetailsDO1)) {
                        defectDetailsDao.insert(defectDetailsDO);
                    } else {
                        defectDetailsDao.update(defectDetailsDO);
                    }
                }
            }
        });
    }

    @Override
    public ZenQuestiontRspBO zenQuestiontFindList(ZenQuestiontBO zenQuestiontBO){
        PageInfo<ZenQuestiontBO> pageInfo = getPageInfo1(zenQuestiontBO);
        List<ZenQuestiontBO> zenQuestiontBOList = BeanConvertUtils.convertList(pageInfo.getList(), ZenQuestiontBO.class);
        ZenQuestiontRspBO zenQuestiontRspBO = new ZenQuestiontRspBO();
        zenQuestiontRspBO.setZenQuestiontBOList(zenQuestiontBOList);
        zenQuestiontRspBO.setPageInfo(pageInfo);
        return zenQuestiontRspBO;
    }
    private PageInfo<ZenQuestiontBO>  getPageInfo1(ZenQuestiontBO zenQuestiontBO) {
        ZenQuestiontDO zenQuestiontDO = new ZenQuestiontDO();
        BeanConvertUtils.convert(zenQuestiontDO, zenQuestiontBO);
        PageInfo<ZenQuestiontBO> pageInfo = PageUtils.pageQueryWithCount(zenQuestiontBO.getPageNum(), zenQuestiontBO.getPageSize(),
                () -> BeanConvertUtils.convertList(zenQuestiontExtDao.findList(zenQuestiontDO), ZenQuestiontBO.class));
        return pageInfo;
    }

    @Override
    public void downloadZenQuestiont(HttpServletResponse response,ZenQuestiontBO zenQuestiontBO){
        ZenQuestiontDO zenQuestiontDO = new ZenQuestiontDO();
        BeanConvertUtils.convert(zenQuestiontDO, zenQuestiontBO);
        List<ZenQuestiontDO> demandDOList = zenQuestiontExtDao.findList(zenQuestiontDO);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), ZenQuestiontDO.class, demandDOList);
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
