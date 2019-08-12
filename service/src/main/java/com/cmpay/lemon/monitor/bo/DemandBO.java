package com.cmpay.lemon.monitor.bo;

import java.util.Date;

/**
 * @author: zhou_xiong
 */
public class DemandBO {
    private String req_inner_seq;
    private String req_prd_line;
    private String req_type;
    private String req_sts;
    private String req_no;
    private String req_nm;
    private String req_desc;
    private int input_res;
    private int dev_cycle;
    private double exp_input;
    private String prd_finsh_tm;
    private String act_prd_finsh_tm;
    private String pre_cur_period;
    private String cur_mon_target;
    private String exp_prd_release_tm;
    private String devp_lead_dept;
    private String devp_coor_dept;
    private String req_pro_dept;
    private String req_proposer;
    private String req_mnger;
    private String devp_res_mng;
    private String req_start_mon;
    private String req_impl_mon;
    private String project_mng;
    private String product_mng;
    private String is_cut;
    private String pre_mon_period;
    private String qa_mng;
    private String config_mng;
    private String mon_remark;
    private String devp_eng;
    private String front_eng;
    private String test_eng;
    private String uat_update_tm;
    private String act_uat_update_tm;
    private String pre_tm;
    private String test_finsh_tm;
    private String act_test_finsh_tm;
    private String risk_solution;
    private String risk_feedback_tm;
    private String end_mon_remark;
    private String end_feedback_tm;
    private int total_workload;
    //已录入总工作量
    private int input_workload;
    //上月录入
    private int last_input_workload;
    //剩余录入工作量
    private int remain_workload;
    //本月录入工作量
    private int mon_input_workload;
    //	主导部门工作量占比
    private String lead_dept_pro;
    //	配合部门工作量占比
    private String coor_dept_pro;
    //	主导部门工作量
    private String lead_dept_workload;
    //	配合部门工作量
    private String coor_dept_workload;
    //是否建立svn目录
    private String is_svn_build;

    private String pro_id;
    private String project_start_tm;
    private String act_prd_upload_tm;
    private String act_workload_upload_tm;
    private String act_sit_upload_tm;
    private String act_test_cases_upload_tm;
    private String act_uat_upload_tm;
    private String act_pre_upload_tm;
    private String act_production_upload_tm;

    private Date creat_time;
    private Date update_time;
    private String creat_user;
    private String update_user;
    private String req_abnor_type;

    /**
     * 页数
     */
    private int pageNum;
    /**
     * 页面大小
     */
    private int pageSize;

    public DemandBO() {
    }

    public DemandBO(String req_inner_seq, String req_prd_line, String req_type, String req_sts, String req_no, String req_nm, String req_desc, int input_res, int dev_cycle, double exp_input, String prd_finsh_tm, String act_prd_finsh_tm, String pre_cur_period, String cur_mon_target, String exp_prd_release_tm, String devp_lead_dept, String devp_coor_dept, String req_pro_dept, String req_proposer, String req_mnger, String devp_res_mng, String req_start_mon, String req_impl_mon, String project_mng, String product_mng, String is_cut, String pre_mon_period, String qa_mng, String config_mng, String mon_remark, String devp_eng, String front_eng, String test_eng, String uat_update_tm, String act_uat_update_tm, String pre_tm, String test_finsh_tm, String act_test_finsh_tm, String risk_solution, String risk_feedback_tm, String end_mon_remark, String end_feedback_tm, int total_workload, int input_workload, int last_input_workload, int remain_workload, int mon_input_workload, String lead_dept_pro, String coor_dept_pro, String lead_dept_workload, String coor_dept_workload, String is_svn_build, Date creat_time, Date update_time, String creat_user, String update_user, String req_abnor_type) {
        this.req_inner_seq = req_inner_seq;
        this.req_prd_line = req_prd_line;
        this.req_type = req_type;
        this.req_sts = req_sts;
        this.req_no = req_no;
        this.req_nm = req_nm;
        this.req_desc = req_desc;
        this.input_res = input_res;
        this.dev_cycle = dev_cycle;
        this.exp_input = exp_input;
        this.prd_finsh_tm = prd_finsh_tm;
        this.act_prd_finsh_tm = act_prd_finsh_tm;
        this.pre_cur_period = pre_cur_period;
        this.cur_mon_target = cur_mon_target;
        this.exp_prd_release_tm = exp_prd_release_tm;
        this.devp_lead_dept = devp_lead_dept;
        this.devp_coor_dept = devp_coor_dept;
        this.req_pro_dept = req_pro_dept;
        this.req_proposer = req_proposer;
        this.req_mnger = req_mnger;
        this.devp_res_mng = devp_res_mng;
        this.req_start_mon = req_start_mon;
        this.req_impl_mon = req_impl_mon;
        this.project_mng = project_mng;
        this.product_mng = product_mng;
        this.is_cut = is_cut;
        this.pre_mon_period = pre_mon_period;
        this.qa_mng = qa_mng;
        this.config_mng = config_mng;
        this.mon_remark = mon_remark;
        this.devp_eng = devp_eng;
        this.front_eng = front_eng;
        this.test_eng = test_eng;
        this.uat_update_tm = uat_update_tm;
        this.act_uat_update_tm = act_uat_update_tm;
        this.pre_tm = pre_tm;
        this.test_finsh_tm = test_finsh_tm;
        this.act_test_finsh_tm = act_test_finsh_tm;
        this.risk_solution = risk_solution;
        this.risk_feedback_tm = risk_feedback_tm;
        this.end_mon_remark = end_mon_remark;
        this.end_feedback_tm = end_feedback_tm;
        this.total_workload = total_workload;
        this.input_workload = input_workload;
        this.last_input_workload = last_input_workload;
        this.remain_workload = remain_workload;
        this.mon_input_workload = mon_input_workload;
        this.lead_dept_pro = lead_dept_pro;
        this.coor_dept_pro = coor_dept_pro;
        this.lead_dept_workload = lead_dept_workload;
        this.coor_dept_workload = coor_dept_workload;
        this.is_svn_build = is_svn_build;
        this.creat_time = creat_time;
        this.update_time = update_time;
        this.creat_user = creat_user;
        this.update_user = update_user;
        this.req_abnor_type = req_abnor_type;
    }

    public DemandBO(String req_inner_seq, String req_nm, String req_no, String devp_lead_dept, String devp_coor_dept,
                        String project_mng, String devp_eng, String front_eng, String product_mng, String test_eng,
                        String prd_finsh_tm, String uat_update_tm, String test_finsh_tm, String exp_prd_release_tm, String req_prd_line,
                        String pre_cur_period, String req_sts, String cur_mon_target) {
        super();
        this.req_inner_seq = req_inner_seq;
        this.req_nm = req_nm;
        this.req_no = req_no;
        this.devp_lead_dept = devp_lead_dept;
        this.devp_coor_dept = devp_coor_dept;
        this.project_mng = project_mng;
        this.devp_eng = devp_eng;
        this.front_eng = front_eng;
        this.product_mng = product_mng;
        this.test_eng = test_eng;
        this.prd_finsh_tm = prd_finsh_tm;
        this.uat_update_tm = uat_update_tm;
        this.test_finsh_tm = test_finsh_tm;
        this.req_prd_line = req_prd_line;
        this.pre_cur_period = pre_cur_period;
        this.req_sts = req_sts;
        this.cur_mon_target = cur_mon_target;
        this.exp_prd_release_tm=exp_prd_release_tm;
    }

    public int getTotal_workload() {
        return total_workload;
    }
    public void setTotal_workload(int total_workload) {
        this.total_workload = total_workload;
    }
    public String getProject_start_tm() {
        return project_start_tm;
    }
    public void setProject_start_tm(String project_start_tm) {
        this.project_start_tm = project_start_tm;
    }

    public int getInput_workload() {
        return input_workload;
    }
    public String getIs_svn_build() {
        return is_svn_build;
    }
    public void setIs_svn_build(String is_svn_build) {
        this.is_svn_build = is_svn_build;
    }
    public void setInput_workload(int input_workload) {
        this.input_workload = input_workload;
    }
    public int getRemain_workload() {
        return remain_workload;
    }
    public void setRemain_workload(int remain_workload) {
        this.remain_workload = remain_workload;
    }
    public int getMon_input_workload() {
        return mon_input_workload;
    }
    public void setMon_input_workload(int mon_input_workload) {
        this.mon_input_workload = mon_input_workload;
    }
    public String getLead_dept_pro() {
        return lead_dept_pro;
    }
    public void setLead_dept_pro(String lead_dept_pro) {
        this.lead_dept_pro = lead_dept_pro;
    }
    public String getCoor_dept_pro() {
        return coor_dept_pro;
    }
    public void setCoor_dept_pro(String coor_dept_pro) {
        this.coor_dept_pro = coor_dept_pro;
    }
    public String getLead_dept_workload() {
        return lead_dept_workload;
    }
    public void setLead_dept_workload(String lead_dept_workload) {
        this.lead_dept_workload = lead_dept_workload;
    }
    public String getCoor_dept_workload() {
        return coor_dept_workload;
    }
    public void setCoor_dept_workload(String coor_dept_workload) {
        this.coor_dept_workload = coor_dept_workload;
    }
    public String getReq_inner_seq() {
        return req_inner_seq;
    }
    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getAct_prd_upload_tm() {
        return act_prd_upload_tm;
    }

    public void setAct_prd_upload_tm(String act_prd_upload_tm) {
        this.act_prd_upload_tm = act_prd_upload_tm;
    }

    public String getAct_workload_upload_tm() {
        return act_workload_upload_tm;
    }

    public void setAct_workload_upload_tm(String act_workload_upload_tm) {
        this.act_workload_upload_tm = act_workload_upload_tm;
    }

    public String getAct_sit_upload_tm() {
        return act_sit_upload_tm;
    }

    public void setAct_sit_upload_tm(String act_sit_upload_tm) {
        this.act_sit_upload_tm = act_sit_upload_tm;
    }

    public String getAct_test_cases_upload_tm() {
        return act_test_cases_upload_tm;
    }

    public void setAct_test_cases_upload_tm(String act_test_cases_upload_tm) {
        this.act_test_cases_upload_tm = act_test_cases_upload_tm;
    }

    public String getAct_uat_upload_tm() {
        return act_uat_upload_tm;
    }

    public void setAct_uat_upload_tm(String act_uat_upload_tm) {
        this.act_uat_upload_tm = act_uat_upload_tm;
    }

    public String getAct_pre_upload_tm() {
        return act_pre_upload_tm;
    }

    public void setAct_pre_upload_tm(String act_pre_upload_tm) {
        this.act_pre_upload_tm = act_pre_upload_tm;
    }

    public String getAct_production_upload_tm() {
        return act_production_upload_tm;
    }

    public void setAct_production_upload_tm(String act_production_upload_tm) {
        this.act_production_upload_tm = act_production_upload_tm;
    }

    public void setReq_inner_seq(String req_inner_seq) {
        this.req_inner_seq = req_inner_seq;
    }
    public String getReq_prd_line() {
        return req_prd_line;
    }
    public void setReq_prd_line(String req_prd_line) {
        this.req_prd_line = req_prd_line;
    }
    public String getReq_type() {
        return req_type;
    }
    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }
    public String getReq_sts() {
        return req_sts;
    }
    public void setReq_sts(String req_sts) {
        this.req_sts = req_sts;
    }
    public String getReq_no() {
        return req_no;
    }
    public void setReq_no(String req_no) {
        this.req_no = req_no;
    }
    public String getReq_nm() {
        return req_nm;
    }
    public void setReq_nm(String req_nm) {
        this.req_nm = req_nm;
    }
    public String getReq_desc() {
        return req_desc;
    }
    public void setReq_desc(String req_desc) {
        this.req_desc = req_desc;
    }
    public int getInput_res() {
        return input_res;
    }
    public void setInput_res(int input_res) {
        this.input_res = input_res;
    }
    public int getDev_cycle() {
        return dev_cycle;
    }
    public void setDev_cycle(int dev_cycle) {
        this.dev_cycle = dev_cycle;
    }
    public double getExp_input() {
        return exp_input;
    }
    public void setExp_input(double exp_input) {
        this.exp_input = exp_input;
    }
    public String getPrd_finsh_tm() {
        return prd_finsh_tm;
    }
    public void setPrd_finsh_tm(String prd_finsh_tm) {
        this.prd_finsh_tm = prd_finsh_tm;
    }
    public String getAct_prd_finsh_tm() {
        return act_prd_finsh_tm;
    }
    public void setAct_prd_finsh_tm(String act_prd_finsh_tm) {
        this.act_prd_finsh_tm = act_prd_finsh_tm;
    }
    public String getPre_cur_period() {
        return pre_cur_period;
    }
    public void setPre_cur_period(String pre_cur_period) {
        this.pre_cur_period = pre_cur_period;
    }
    public String getCur_mon_target() {
        return cur_mon_target;
    }
    public void setCur_mon_target(String cur_mon_target) {
        this.cur_mon_target = cur_mon_target;
    }
    public String getExp_prd_release_tm() {
        return exp_prd_release_tm;
    }
    public void setExp_prd_release_tm(String exp_prd_release_tm) {
        this.exp_prd_release_tm = exp_prd_release_tm;
    }
    public String getDevp_lead_dept() {
        return devp_lead_dept;
    }
    public void setDevp_lead_dept(String devp_lead_dept) {
        this.devp_lead_dept = devp_lead_dept;
    }
    public String getDevp_coor_dept() {
        return devp_coor_dept;
    }
    public void setDevp_coor_dept(String devp_coor_dept) {
        this.devp_coor_dept = devp_coor_dept;
    }
    public String getReq_pro_dept() {
        return req_pro_dept;
    }
    public void setReq_pro_dept(String req_pro_dept) {
        this.req_pro_dept = req_pro_dept;
    }
    public String getReq_proposer() {
        return req_proposer;
    }
    public void setReq_proposer(String req_proposer) {
        this.req_proposer = req_proposer;
    }
    public String getReq_mnger() {
        return req_mnger;
    }
    public void setReq_mnger(String req_mnger) {
        this.req_mnger = req_mnger;
    }
    public String getDevp_res_mng() {
        return devp_res_mng;
    }
    public void setDevp_res_mng(String devp_res_mng) {
        this.devp_res_mng = devp_res_mng;
    }
    public String getReq_start_mon() {
        return req_start_mon;
    }
    public void setReq_start_mon(String req_start_mon) {
        this.req_start_mon = req_start_mon;
    }
    public String getReq_impl_mon() {
        return req_impl_mon;
    }
    public void setReq_impl_mon(String req_impl_mon) {
        this.req_impl_mon = req_impl_mon;
    }
    public String getProject_mng() {
        return project_mng;
    }
    public void setProject_mng(String project_mng) {
        this.project_mng = project_mng;
    }
    public String getProduct_mng() {
        return product_mng;
    }
    public void setProduct_mng(String product_mng) {
        this.product_mng = product_mng;
    }
    public String getIs_cut() {
        return is_cut;
    }
    public void setIs_cut(String is_cut) {
        this.is_cut = is_cut;
    }
    public String getPre_mon_period() {
        return pre_mon_period;
    }
    public void setPre_mon_period(String pre_mon_period) {
        this.pre_mon_period = pre_mon_period;
    }
    public String getQa_mng() {
        return qa_mng;
    }
    public void setQa_mng(String qa_mng) {
        this.qa_mng = qa_mng;
    }
    public String getConfig_mng() {
        return config_mng;
    }
    public void setConfig_mng(String config_mng) {
        this.config_mng = config_mng;
    }
    public String getMon_remark() {
        return mon_remark;
    }
    public void setMon_remark(String mon_remark) {
        this.mon_remark = mon_remark;
    }
    public String getDevp_eng() {
        return devp_eng;
    }
    public void setDevp_eng(String devp_eng) {
        this.devp_eng = devp_eng;
    }
    public String getFront_eng() {
        return front_eng;
    }
    public void setFront_eng(String front_eng) {
        this.front_eng = front_eng;
    }
    public String getTest_eng() {
        return test_eng;
    }
    public void setTest_eng(String test_eng) {
        this.test_eng = test_eng;
    }
    public String getUat_update_tm() {
        return uat_update_tm;
    }
    public void setUat_update_tm(String uat_update_tm) {
        this.uat_update_tm = uat_update_tm;
    }
    public String getAct_uat_update_tm() {
        return act_uat_update_tm;
    }
    public void setAct_uat_update_tm(String act_uat_update_tm) {
        this.act_uat_update_tm = act_uat_update_tm;
    }
    public String getPre_tm() {
        return pre_tm;
    }
    public void setPre_tm(String pre_tm) {
        this.pre_tm = pre_tm;
    }
    public String getTest_finsh_tm() {
        return test_finsh_tm;
    }
    public void setTest_finsh_tm(String test_finsh_tm) {
        this.test_finsh_tm = test_finsh_tm;
    }
    public String getAct_test_finsh_tm() {
        return act_test_finsh_tm;
    }
    public void setAct_test_finsh_tm(String act_test_finsh_tm) {
        this.act_test_finsh_tm = act_test_finsh_tm;
    }
    public String getRisk_solution() {
        return risk_solution;
    }
    public void setRisk_solution(String risk_solution) {
        this.risk_solution = risk_solution;
    }
    public String getRisk_feedback_tm() {
        return risk_feedback_tm;
    }
    public void setRisk_feedback_tm(String risk_feedback_tm) {
        this.risk_feedback_tm = risk_feedback_tm;
    }
    public String getEnd_mon_remark() {
        return end_mon_remark;
    }
    public void setEnd_mon_remark(String end_mon_remark) {
        this.end_mon_remark = end_mon_remark;
    }
    public String getEnd_feedback_tm() {
        return end_feedback_tm;
    }
    public void setEnd_feedback_tm(String end_feedback_tm) {
        this.end_feedback_tm = end_feedback_tm;
    }
    public Date getCreat_time() {
        return creat_time;
    }
    public void setCreat_time(Date creat_time) {
        this.creat_time = creat_time;
    }
    public Date getUpdate_time() {
        return update_time;
    }
    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
    public String getCreat_user() {
        return creat_user;
    }
    public void setCreat_user(String creat_user) {
        this.creat_user = creat_user;
    }
    public String getUpdate_user() {
        return update_user;
    }
    public void setUpdate_user(String update_user) {
        this.update_user = update_user;
    }

    public String getReq_abnor_type() {
        return req_abnor_type;
    }

    public void setReq_abnor_type(String req_abnor_type) {
        this.req_abnor_type = req_abnor_type;
    }

    public int getLast_input_workload() {
        return last_input_workload;
    }
    public void setLast_input_workload(int last_input_workload) {
        this.last_input_workload = last_input_workload;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "DemandBO{" +
                "req_inner_seq='" + req_inner_seq + '\'' +
                ", req_prd_line='" + req_prd_line + '\'' +
                ", req_type='" + req_type + '\'' +
                ", req_sts='" + req_sts + '\'' +
                ", req_no='" + req_no + '\'' +
                ", req_nm='" + req_nm + '\'' +
                ", req_desc='" + req_desc + '\'' +
                ", input_res=" + input_res +
                ", dev_cycle=" + dev_cycle +
                ", exp_input=" + exp_input +
                ", prd_finsh_tm='" + prd_finsh_tm + '\'' +
                ", act_prd_finsh_tm='" + act_prd_finsh_tm + '\'' +
                ", pre_cur_period='" + pre_cur_period + '\'' +
                ", cur_mon_target='" + cur_mon_target + '\'' +
                ", exp_prd_release_tm='" + exp_prd_release_tm + '\'' +
                ", devp_lead_dept='" + devp_lead_dept + '\'' +
                ", devp_coor_dept='" + devp_coor_dept + '\'' +
                ", req_pro_dept='" + req_pro_dept + '\'' +
                ", req_proposer='" + req_proposer + '\'' +
                ", req_mnger='" + req_mnger + '\'' +
                ", devp_res_mng='" + devp_res_mng + '\'' +
                ", req_start_mon='" + req_start_mon + '\'' +
                ", req_impl_mon='" + req_impl_mon + '\'' +
                ", project_mng='" + project_mng + '\'' +
                ", product_mng='" + product_mng + '\'' +
                ", is_cut='" + is_cut + '\'' +
                ", pre_mon_period='" + pre_mon_period + '\'' +
                ", qa_mng='" + qa_mng + '\'' +
                ", config_mng='" + config_mng + '\'' +
                ", mon_remark='" + mon_remark + '\'' +
                ", devp_eng='" + devp_eng + '\'' +
                ", front_eng='" + front_eng + '\'' +
                ", test_eng='" + test_eng + '\'' +
                ", uat_update_tm='" + uat_update_tm + '\'' +
                ", act_uat_update_tm='" + act_uat_update_tm + '\'' +
                ", pre_tm='" + pre_tm + '\'' +
                ", test_finsh_tm='" + test_finsh_tm + '\'' +
                ", act_test_finsh_tm='" + act_test_finsh_tm + '\'' +
                ", risk_solution='" + risk_solution + '\'' +
                ", risk_feedback_tm='" + risk_feedback_tm + '\'' +
                ", end_mon_remark='" + end_mon_remark + '\'' +
                ", end_feedback_tm='" + end_feedback_tm + '\'' +
                ", total_workload=" + total_workload +
                ", input_workload=" + input_workload +
                ", last_input_workload=" + last_input_workload +
                ", remain_workload=" + remain_workload +
                ", mon_input_workload=" + mon_input_workload +
                ", lead_dept_pro='" + lead_dept_pro + '\'' +
                ", coor_dept_pro='" + coor_dept_pro + '\'' +
                ", lead_dept_workload='" + lead_dept_workload + '\'' +
                ", coor_dept_workload='" + coor_dept_workload + '\'' +
                ", is_svn_build='" + is_svn_build + '\'' +
                ", pro_id='" + pro_id + '\'' +
                ", project_start_tm='" + project_start_tm + '\'' +
                ", act_prd_upload_tm='" + act_prd_upload_tm + '\'' +
                ", act_workload_upload_tm='" + act_workload_upload_tm + '\'' +
                ", act_sit_upload_tm='" + act_sit_upload_tm + '\'' +
                ", act_test_cases_upload_tm='" + act_test_cases_upload_tm + '\'' +
                ", act_uat_upload_tm='" + act_uat_upload_tm + '\'' +
                ", act_pre_upload_tm='" + act_pre_upload_tm + '\'' +
                ", act_production_upload_tm='" + act_production_upload_tm + '\'' +
                ", creat_time=" + creat_time +
                ", update_time=" + update_time +
                ", creat_user='" + creat_user + '\'' +
                ", update_user='" + update_user + '\'' +
                ", req_abnor_type='" + req_abnor_type + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
