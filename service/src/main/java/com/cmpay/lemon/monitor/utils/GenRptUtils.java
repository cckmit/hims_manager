package com.cmpay.lemon.monitor.utils;


import com.cmpay.lemon.monitor.bo.GenerateReportBO;
import com.runqian.report4.ide.GroupEngine;
import com.runqian.report4.model.ReportDefine;
import com.runqian.report4.model.engine.ExtCellSet;
import com.runqian.report4.usermodel.*;
import com.runqian.report4.util.ReportUtils;
import com.runqian.report4.view.ReportExporter;
import com.runqian.report4.view.exceloxml.ExcelOxmlReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created on 9:34 2019/5/15
 *
 * @author wulan
 *         <p>
 *         descriptionc 快逸报表生成
 */
@Service
public class GenRptUtils {

    private static final String XLSX ="xlsx" ;
    private static final String XLS="xls" ;
    private static final String PDF = "pdf";
    private static Logger logger = LoggerFactory.getLogger(GenRptUtils.class);

    /**
     * 生成单个报表
     *
     * @param generateReportBO
     * @return
     */
    public static boolean genRaqRpt(GenerateReportBO generateReportBO) {
        try {
            Context cxt = new Context();
            // 授权文件
            ExtCellSet.setLicenseFileName(generateReportBO.getLicUrl());
            cxt.setConnection(generateReportBO.getDataSourceName(),
                    getConnection(generateReportBO.getDataBaseDriver(), generateReportBO.getDataBaseUrl(),
                            generateReportBO.getDataBaseUserName(), generateReportBO.getUserPassword()));
            ReportDefine rd = (ReportDefine) ReportUtils.read(generateReportBO.getRaqPath());
            String[] array = generateReportBO.getParamArray();
            // 绑定raq文件中的参数
            ParamMetaData pmd = rd.getParamMetaData();
            if (pmd != null) {
                for (int j = 0, count = pmd.getParamCount(); j < count; j++) {
                    String name = pmd.getParam(j).getParamName();
                    cxt.setParamValue(name, array[j]);
                }
            }
            // 创建报表引擎
            Engine engine = new Engine(rd, cxt);
            // 计算报表
            IReport iReport = engine.calc();

            generateRpt(generateReportBO.getFileSavePath(), iReport, generateReportBO.getReportStyle());
        } catch (Exception e) {
            logger.error("genRaqRpt Error:",e);
            return false;
        }
        return true;
    }

    /**
     * 生成多个sheet的报表组文件
     *
     * @param generateReportBO
     * @return
     */
    public static boolean genRpgRpt(GenerateReportBO generateReportBO) {
        try {
            Context cxt = new Context();
            cxt.setConnection(generateReportBO.getDataSourceName(),
                    getConnection(generateReportBO.getDataBaseDriver(), generateReportBO.getDataBaseUrl(),
                            generateReportBO.getDataBaseUserName(), generateReportBO.getUserPassword()));
            HashMap<String, String> map = new HashMap<>();
            ReportGroup rg = ReportGroup.read(generateReportBO.getRaqPath());
            ParamMetaData rgParam = rg.getParamMetaData();
            List<String> paName = new ArrayList<String>();
            if (rgParam != null) {
                int rgParamNumber = rgParam.getParamCount();
                String rgParamName;
                for (int j = 0; j < rgParamNumber; j++) {
                    rgParamName = rgParam.getParam(j).getParamName();
                    paName.add(rgParamName);
                }
                String[] array = generateReportBO.getParamArray();
                for (int k = 0; k < array.length; k++) {
                    for (int j = 0; j < paName.size(); j++) {
                        if (k == j) {
                            map.put(paName.get(j), array[k]);
                        }
                    }
                }
                for (int j = 0; j < rgParamNumber; j++) {
                    rgParamName = rgParam.getParam(j).getParamName();
                    for (Iterator it = map.keySet().iterator(); it.hasNext(); ) {
                        String key = (String) it.next();
                        // 遍历hashmap，将对应的参数写入报表组对象
                        if (rgParamName.equals(key)) {
                            rgParam.getParam(j).setValue(map.get(key));
                        }
                    }
                }
            }
            cxt.setParamMap(map);
            // 计算报表组
            GroupEngine gEngine = new GroupEngine(rg, cxt);
            // 获取子报表数量
            int num = rg.getItemCount();
            String reportStyle = generateReportBO.getReportStyle();
            if (reportStyle != null && (XLSX.equals( reportStyle ) || XLS.equals( reportStyle ))) {
                ExcelOxmlReport er = new ExcelOxmlReport();
                for (int j = 0; j < num; j++) {
                    IReport r = gEngine.getReport(j);
                    er.export(gEngine.getTitle(j), r);
                }
                er.saveTo(generateReportBO.getFileSavePath());
                logger.debug("文件输出结束。。。");

            } else {
                logger.debug("暂不支持生成该格式的文件！");
            }
        } catch (Exception e) {
            logger.error("报表文件生成失败：{}",e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 生成报表
     *
     * @param path    报表存储路径
     * @param iReport 预处理报表
     * @param rptTyp  报表类型
     * @throws Throwable
     */
    public static boolean generateRpt(String path, IReport iReport, String rptTyp) {
        try {
            if (rptTyp != null && XLSX.equals( rptTyp )) {
                // xlsx格式报表
                ReportExporter re = new ReportExporter(path, ReportExporter.EXPORT_EXCEL_OPENXML);
                // 导出报表数据到excel文件
                re.export(iReport);
            } else if (rptTyp != null && PDF.equals( rptTyp )) {
                // pdf格式报表
                ReportExporter re = new ReportExporter(path, ReportExporter.EXPORT_PDF);
                // 导出报表数据到pdf文件
                re.export(iReport);

            } else if (rptTyp != null && XLS.equals( rptTyp )) {
                // 03版excel
                ReportExporter re = new ReportExporter(path, ReportExporter.EXPORT_EXCEL);
                re.export(iReport);
            }
        } catch (Throwable throwable) {
            logger.error("generateRpt error =============",throwable);
            return false;
        }
        return true;
    }

    public static Connection getConnection(String driver, String jdbcUrl, String username, String password) throws Exception {
        Class.forName(driver);
        return DriverManager.getConnection(jdbcUrl, username, password);

    }

}
