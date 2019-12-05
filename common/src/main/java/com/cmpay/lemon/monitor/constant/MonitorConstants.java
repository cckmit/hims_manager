package com.cmpay.lemon.monitor.constant;

/**
 * @author: zhou_xiong
 */
public class MonitorConstants {
    public static final String BASE_URI = "/v1/monitoringui";
    /**
     * 系统用户请求uri
     */
    public static final String SYSTEM_USER_PATH = BASE_URI + "/user";
    /**
     * 系统菜单请求uri
     */
    public static final String SYSTEM_MENU_PATH = BASE_URI + "/menu";
    /**
     * 系统角色请求uri
     */
    public static final String SYSTEM_ROLE_PATH = BASE_URI + "/role";
    /**
     * 系统日志请求uri
     */
    public static final String SYSTEM_LOG_PATH=BASE_URI + "/log";
    /**
     * 中台管理请求uri
     */
    public static final String CENTER_PATH = BASE_URI + "/center";
    /**
     * 日志类型请求uri
     */
    public static final String LOG_TYPE = BASE_URI + "/logtype";
    /**
     * 普通检索uri
     */
    public static final String ORDINARY_PATH = BASE_URI + "/ordinary";
    /**
     * 高级检索uri
     */
    public static final String ADVANCE_PATH = BASE_URI + "/advance";
    /**
     * txnplte日志检索uri
     */
    public static final String TXNPLTELOG_PATH = BASE_URI + "/txnplteLog";
    /**
     * 需求管理uri
     */
    public static final String REQTASK_PATH = BASE_URI + "/task";
    /**
     * 需求管理uri
     */
    public static final String REQINDEX_PATH = BASE_URI + "/index2";
    /**
     * 需求计划uri
     */
    public static final String REQPLAN_PATH = BASE_URI + "/plan";
    /**
     * 需求周反馈uri
     */
    public static final String REQWEEK_PATH = BASE_URI + "/week";
    /**
     * 需求月反馈uri
     */
    public static final String REQMONTH_PATH = BASE_URI + "/month";
    /**
     * 工作量管理uri
     */
    public static final String REQWORK_PATH = BASE_URI + "/workload";
    /**
     * 错误码管理uri
     */
    public static final String ERROR_PATH = BASE_URI + "/error";
    /**
     * 错误码更新管理uri
     */
    public static final String UPDMGN_PATH = BASE_URI + "/updmgn";


    /**
     * 投产查询管理uri
     */
    public static final String PRODUCTION_PATH = BASE_URI + "/production";

    public static final String REPORTFORM_PATH=BASE_URI + "/reportform";
    /**
     * 系统操作URL
     */
    public static final String SYSTEMOPERATION_PATH=BASE_URI + "/systemOperation";

    /**
     * 投产时间管理Uri
     */
    public static final String PRODUCTTIME=BASE_URI + "/productTime";
    /**
     * 字典uri
     */
    public static final String DICTIONARY_PATH = BASE_URI + "/dic";

    /**
     * 超级管理员ID
     */
    public static final int SUPER_ADMIN = 1;
    /**
     * 系统菜单最大ID
     */
    public static final int SYSTEM_MENU_MAX = 16;
    /**
     * 公共序列号
     */
    public static final String PUBLIC_SEQUENCE_NAME = "SYS:PUB:SEQ";
    public static final String TYPE = "doc";
    public static final String TIME_FILED = "@timestamp";
    public static final String SEPARATOR = "&";
    public static final String ALL = "整体";
    public static final String FILE = "file";
}
