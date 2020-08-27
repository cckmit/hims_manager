package com.cmpay.lemon.monitor.service.reportForm;


import com.cmpay.lemon.monitor.bo.GenerateReportBO;

import java.io.File;
import java.util.List;

/**
 * Created on 2020/6/29
 *
 * @author: hu_hx
 */
public interface GenRptService {
    /**
     * 生成查询报表
     * @param generateReportBO
     * @return
     */
    File genRaqRpt(GenerateReportBO generateReportBO);

    /**
     * 参数分割
     * @param string
     * @return
     */
    String splitString(List<String> string);
}
