package com.cmpay.lemon.monitor.entity.sendemail;


import com.cmpay.lemon.monitor.bo.ProductionBO;
import com.cmpay.lemon.monitor.utils.BaseUtil;

/**
 * Created by zouxin on 2018/8/24.
 */
public class EmailConfig {


    /**
     * 设置正常投产邮件内容
     *
     * @param productionBean ProductionBean
     * @return
     */
    public static String setProEmailContent(ProductionBO productionBean) {
        StringBuffer sb = new StringBuffer();
        sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
        sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>正常投产(非投产日)申请表</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>需求名称及内容简述</td><td colspan='5'>" + productionBean.getProNeed() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + productionBean.getApplicationDept() + "</td><td style='font-weight: bold;'>申请人</td><td>" + productionBean.getProApplicant() + "</td>");
        sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getApplicantTel() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>投产编号</td><td>" + productionBean.getProNumber() + "</td><td style='font-weight: bold;'>复核人</td><td>" + productionBean.getProChecker() + "</td>");
        sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getCheckerTel() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>验证测试类型</td><td>" + productionBean.getValidation() + "</td><td style='font-weight: bold;'>验证人</td><td>" + productionBean.getIdentifier() + "</td>");
        sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getIdentifierTel() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>计划投产日期</td><td>" + BaseUtil.transformDateToStr(productionBean.getProDate(), "yyyy-MM-dd") + "</td><td style='font-weight: bold;'>产品所属模块</td><td>" + productionBean.getProModule() + "</td>");
        sb.append("<td style='font-weight: bold;'>基地业务负责人</td><td>" + productionBean.getBusinessPrincipal() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>产品经理</td><td>" + productionBean.getProManager() + "</td><td style='font-weight: bold;'>是否涉及证书</td><td>" + productionBean.getIsRefCerificate() + "</td>");
        sb.append("<td style='font-weight: bold;'>开发负责人</td><td>" + productionBean.getDevelopmentLeader() + "</td></tr>");
        sb.append("<tr><td style='font-weight: bold;'>审批人</td><td>" + productionBean.getApprover() + "</td><td style='font-weight: bold;'>是否需要运维监控</td><td>" + productionBean.getProOperation() + "</td>");
        if (productionBean.getUpdateOperator() != null) {
            sb.append("<td style='font-weight: bold;'>版本更新操作人</td><td>" + productionBean.getUpdateOperator() + "</td></tr>");
        } else {
            sb.append("<td style='font-weight: bold;'>版本更新操作人</td><td>暂未录入</td></tr>");
        }
        if (productionBean.getIsAdvanceProduction().equals("否")) {
            sb.append("<tr><td style='font-weight: bold;'>不做预投产验证原因</td><td colspan='5'>" + productionBean.getNotAdvanceReason() + "</td></tr>");
        }
        sb.append("<tr><td style='font-weight: bold;'>不走正常投产原因</td><td colspan='5'>" + productionBean.getUnusualReasonPhrase() + "</td></tr></table>");
        return sb.toString();
    }


    /**
     * 设置救火更新邮件内容
     * @param productionBean
     * @return
     */
    public static String setFireEmailContent(ProductionBO productionBean,boolean flag) {
        //拼接邮件内容
        StringBuffer sb = new StringBuffer();
        String[] crMore_need = productionBean.getProNeed().split("-");
        String[] crMore_number = productionBean.getProNumber().split(";");;
        if(flag){
            sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
            sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>救火更新通知表</td></tr>");
            for(int i=0;i<crMore_need.length;i++){
                sb.append("<tr><td style='font-weight: bold;'>更新标题</td><td colspan='2'>" + crMore_need[i] + "</td><td style='font-weight: bold;'>投产编号</td><td colspan='2'>" + crMore_number[i] + "</td></tr>");
            }
            sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + productionBean.getApplicationDept() + "</td><td style='font-weight: bold;'>申请人</td><td>" + productionBean.getProApplicant() + "</td>");
            sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getApplicantTel() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>产品经理</td><td>" + productionBean.getProManager() + "</td><td style='font-weight: bold;'>复核人</td><td>" + productionBean.getProChecker() + "</td>");
            sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getCheckerTel() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>验证测试类型</td><td>" + productionBean.getValidation() + "</td><td style='font-weight: bold;'>验证人</td><td>" + productionBean.getIdentifier() + "</td>");
            sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getIdentifierTel() + "</td></tr>");
            if (productionBean.getIsAdvanceProduction().equals("否")) {
                sb.append("<tr><td style='font-weight: bold;'>不做预投产验证原因</td><td colspan='5'>" + productionBean.getNotAdvanceReason() + "</td></tr>");
            }
            sb.append("<tr><td style='font-weight: bold;'>不走正常投产原因</td><td colspan='5'>" + productionBean.getUrgentReasonPhrase() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>当天不投产的影响</td><td colspan='5'>" + productionBean.getNotProductionImpact() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>更新要求完成时间</td><td colspan='5'>" + productionBean.getCompletionUpdate() + "</td></tr>");
            sb.append("<tr><td colspan='6' style='font-weight: bold;'>如需提前至当日24点前更新，需补充填写以下内容：</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>提前实施原因</td><td colspan='5'>" + productionBean.getEarlyImplementation() + "</td></tr>");
            sb.append("<tr><td rowspan='2' style='font-weight: bold;' >是否影响客户使用</td><td  rowspan='2'>" + productionBean.getInfluenceUse() + "</td>");
            sb.append("<td style='font-weight: bold;' >如不影响客户使用，请简要描述原因</td><td colspan='3'>" + productionBean.getInfluenceUseReason() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;' >如影响客户使用，描述具体影响范围</td><td colspan='3'>" + productionBean.getInfluenceUseInf() + "</td></tr>");
            sb.append(" <tr><td style='font-weight: bold;'>更新时间及预计操作时长</td><td colspan='5'>" + productionBean.getOperatingTime() + "</td></tr></tr></table>");

        }else{
            sb.append("<table border='1' style='border-collapse: collapse;background-color: white; white-space: nowrap;'>");
            sb.append("<tr><td colspan='6' style='text-align: center;font-weight: bold;'>救火更新通知表</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>更新标题</td><td colspan='5'>" + productionBean.getProNeed() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>申请部门</td><td>" + productionBean.getApplicationDept() + "</td><td style='font-weight: bold;'>申请人</td><td>" + productionBean.getProApplicant() + "</td>");
            sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getApplicantTel() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>投产编号</td><td>" + productionBean.getProNumber() + "</td><td style='font-weight: bold;'>复核人</td><td>" + productionBean.getProChecker() + "</td>");
            sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getCheckerTel() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>验证测试类型</td><td>" + productionBean.getValidation() + "</td><td style='font-weight: bold;'>验证人</td><td>" + productionBean.getIdentifier() + "</td>");
            sb.append("<td style='font-weight: bold;'>联系方式</td><td>" + productionBean.getIdentifierTel() + "</td></tr>");
            if (productionBean.getIsAdvanceProduction().equals("否")) {
                sb.append("<tr><td style='font-weight: bold;'>不做预投产验证原因</td><td colspan='5'>" + productionBean.getNotAdvanceReason() + "</td></tr>");
            }
            sb.append("<tr><td style='font-weight: bold;'>不走正常投产原因</td><td colspan='5'>" + productionBean.getUrgentReasonPhrase() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>当天不投产的影响</td><td colspan='5'>" + productionBean.getNotProductionImpact() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>更新要求完成时间</td><td colspan='5'>" + productionBean.getCompletionUpdate() + "</td></tr>");
            sb.append("<tr><td colspan='6' style='font-weight: bold;'>如需提前至当日24点前更新，需补充填写以下内容：</td></tr>");
            sb.append("<tr><td style='font-weight: bold;'>提前实施原因</td><td colspan='5'>" + productionBean.getEarlyImplementation() + "</td></tr>");
            sb.append("<tr><td rowspan='2' style='font-weight: bold;' >是否影响客户使用</td><td  rowspan='2'>" + productionBean.getInfluenceUse() + "</td>");
            sb.append("<td style='font-weight: bold;' >如不影响客户使用，请简要描述原因</td><td colspan='3'>" + productionBean.getInfluenceUseReason() + "</td></tr>");
            sb.append("<tr><td style='font-weight: bold;' >如影响客户使用，描述具体影响范围</td><td colspan='3'>" + productionBean.getInfluenceUseInf() + "</td></tr>");
            sb.append(" <tr><td style='font-weight: bold;'>更新时间及预计操作时长</td><td colspan='5'>" + productionBean.getOperatingTime() + "</td></tr></table>");
        }

        return sb.toString();
    }

}
