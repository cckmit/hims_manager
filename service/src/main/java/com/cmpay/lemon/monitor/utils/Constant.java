package com.cmpay.lemon.monitor.utils;

/**
 * Created by zouxin on 2018/8/30.
 */
public interface Constant {
    // 类型
    /**
     * @msg 产品名称类型
     */
    String DIC_PRODUCT_TYPE_NAME = "PRODUCT_TYPE_NAME";
    /**
     * @msg 周期分类类型
     */
    String DIC_CYCLE_CATEGORY = "CYCLE_CATEGORY";
    /**
     * @msg 审核状态类型
     */
    String DIC_AUDIT_FLAG = "AUDIT_FLAG";
    /**
     * @msg 需求来源类型
     */
    String DIC_DEMAND_SOURCES = "DEMAND_SOURCES";
    /**
     * @msg 任务状态类型
     */
    String DIC_MISSION_FLAG = "MISSION_FLAG";
    /**
     * @msg 产品归属线类型
     */
    String DIC_PRODUCT_LINE = "PRODUCT_LINE";
    /**
     * @msg 需求名称类型
     */
    String DIC_REQ_TYPE = "REQ_TYPE";

    // 用户角色
    /**
     * @msg 中心经理角色
     */
    String USER_ROLE_NAME_CENTER = "中心经理";
    /**
     * @msg 产品经理角色
     */
    String USER_ROLE_NAME_PRODUCT = "产品经理";
    /**
     * @msg 部门经理角色
     */
    String USER_ROLE_NAME_DEPARTMENT = "部门经理";
    /**
     * @msg 中心经理角色标示
     */
    String USER_ROLE_ID_CENTER = "201401010001";
    /**
     * @msg 产品经理角色标示
     */
    String USER_ROLE_ID_PRODUCT = "201401010002";
    /**
     * @msg 部门经理角色标示
     */
    String USER_ROLE_ID_DEPARTMENT = "201401010003";

    // Dept 部门标示
    /**
     * @msg 产品研究部
     */
    String DEPT_PRODUCT_RESEARCH = "002005";


    //审核管理常量
    /**
     * @msg 待审核
     */
    String AUDIT_FLAG_ORG = "01";//待审核
    /**
     * @msg 通过
     */
    String AUDIT_FLAG_SUCCESS = "02";//通过
    /**
     * @msg 拒绝
     */
    String AUDI_FLAG_FAIL = "03";//拒绝
    /**
     * @msg 退回
     */
    String AUDIT_FLAG_BACK = "04";//退回

    // 任务类型 日志记录
    /**
     * @msg 流程
     */
    String FEEDBACK_CATEGORY_PRCS = "03";


    // 文件相关参数
    /**
     * @msg 任务Excel 导出类型
     */
    String FILE_EXPORT_TASK_TYPE = "excel.import.match.header";
    String FILE_EXPORT_TASK_SHEET_NAME = "产品开发工作计划";
    String FILE_EXPORT_TASK_DOC_NAME = "任务模版";
    String FILE_EXPORT_TASK_HEADER = "和包平台应用软件优化提升项目支撑伙伴2018年11月考核与非考核任务汇总表（考核项勿删勿改名称）";
    
    /**
     * @msg 导出路径
     */
    String IMPORT_PATH = "/home/devadm/temp/import/";
    /**
     * @msg 项目文档路径
     */
    String PROJECTDOC_PATH = "/home/devadm/temp/Projectdoc/";
}
