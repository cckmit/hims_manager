package com.cmpay.lemon.monitor.enums;

import com.cmpay.lemon.common.AlertCapable;
import com.cmpay.lemon.common.utils.StringUtils;

/**
 * @author: zhou_xiong
 */
public enum MsgEnum implements AlertCapable {
    SUCCESS("MON00000", "交易成功"),
    LOGIN_DATA_BIND_FAILED("MON00001", "登录数据解析失败"),
    LOGIN_ACCOUNT_OR_PASSWORD_ERROR("MON00002", "账户或密码错误"),
    LOGIN_SESSION_EXPIRE("MON00003", "session已失效，请重新登录"),
    LOGIN_FAILED("MON00004", "登录失败"),
    SUPER_ADMIN_CANNNOT_DELETE("MON00005", "超级管理员不能被删除"),
    ORGIN_PASSWORD_NOT_CORRECT("MON00006", "原密码错误"),
    USER_DISABLED("MON00007", "该用户已被禁用,不可登陆"),

    DB_UPDATE_FAILED("MON00100", "数据更新失败"),
    DB_DELETE_FAILED("MON00101", "数据删除失败"),
    DB_INSERT_FAILED("MON00102", "数据新增失败"),
    DB_FIND_FAILED("MON00103", "数据查询失败"),
    DB_CANNOT_DELETE("MON00104", "整体项不允许删除"),
    ERROR_REQ_NO("MON00105", "需求任务提交失败:需求编号有误！"),
    NON_UNIQUE("MON00106", "需求任务提交失败,已存在需求名或需求编号相同的记录！"),
    NULL_REMARK("MON00107", "需求任务提交失败:需求状态为取消或暂停时，月初备注信息不能为空！"),
    ERROR_DEVP("MON00108", "需求任务提交失败:配合部门不能和主导部门相同！"),
    ERROR_IMPORT("MON00109", "需求任务导入失败:"),

    MENU_NAME_CANNOT_NULL("MON00200", "菜单名称不能为空"),
    PARENT_MENU_CANNOT_NULL("MON00201", "上级菜单不能为空"),
    PARENT_MENU_MUSTBE_CATALOG("MON00203", "上级菜单只能为目录类型"),
    PARENT_MENU_MUSTBE_MENU("MON00204", "上级菜单只能为菜单类型"),
    SYSTEM_MENU_CANNOT_DELETE("MON00205", "系统菜单不允许删除"),
    DELETE_SUBMENU_OR_BUTTON_FIRST("MON00206", "请先删除子菜单或按钮"),

    FILE_NAME_NOT_EXIST("MON00304", "文件名不存在"),
    WRITE_FILE_ERROR("MON00306", "读写时异常"),
    BATCH_IMPORT_FAILED("MON00307", "批量导入失败,请稍后再试"),

    SEARCH_FAILED("MON00401", "搜索日志失败,请稍后再试"),
    REQUESTID_IS_BLANK("MON00402", "日志号为空"),

    ERROR_SENDT0_ISBLANK("MON00501", "项目启动失败，收件人必填，多个“;”分割!"),
    ERROR_PLAN_NULL("MON00502", "项目启动失败，找不到该需求对应信息!"),
    ERROR_REQNO_REQNM_ISBLANK("MON00503", "项目启动失败，需求编号和需求名称不能为空!"),
    ERROR_NOT_PROJECTMNG("MON00504", "项目启动失败，只能有项目经理进行项目启动"),
    ERROR_NOT_FINISHINFO("MON00505", "项目启动失败:人员或时间配置不完善"),
    ERROR_NOT_SVN("MON00506", "项目启动失败:"),
    ERROR_NOT_SVNBULID("MON00507", "项目启动失败，SVN项目建立失败："),
    ERROR_MAIL_FAIL("MON00508", "项目启动失败，SVN项目建立成功，启动邮件发送失败："),
    ERROR_NOT_PRIVILEGE("MON00509", "无权限使用该功能"),
    ERROR_FAIL_CHANGE("MON00510", "存量需求转存失败:");

    private String msgCd;
    private String msgInfo;

    MsgEnum(String msgCd, String msgInfo) {
        this.msgCd = msgCd;
        this.msgInfo = msgInfo;
    }

    public void setMsgCd(String msgCd) {
        this.msgCd = msgCd;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }

    @Override
    public String getMsgCd() {
        return msgCd;
    }

    @Override
    public String getMsgInfo() {
        return msgInfo;
    }

    public static MsgEnum getEnum(String msgCd) {
        for (MsgEnum m : MsgEnum.values()) {
            if (StringUtils.equals( m.msgCd, msgCd )) {
                return m;
            }
        }
        return null;
    }
}
