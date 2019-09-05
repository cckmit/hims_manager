package com.cmpay.lemon.monitor.entity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.ArrayUtils;


/**
 * 常量类
 * 
 * @author bailin.qing
 * @date  Jan 10, 2014
 * @dep 高阳通联--开发一部
 */
public class Constant {
	
	/**
	 * 验证码类型
	 */
	public enum CaptchaType {

		/** 后台用户登录 */
		adminUserLogin,

		/** 其它 */
		other
	}
	/**
	 * 账号锁定类型
	 */
	public enum AccountLockType {

		/** 其他  */
		other,

		/** 管理员 */
		admin
	}	
	/** 自动解锁时间 */
	private static Integer accountLockTime = 10;
	/** 连续登录失败最大次数 */
	private static Integer accountLockCount = 90000;
	public static String captchaTypes[] = {"adminUserLogin","other"};  
	public static String accountLockTypes[] = {"admin","other"};
	
	/**审核管理常量*/
	public static final String AUDIT_FLAG_ORG = "01";//待审核
	public static final String AUDIT_FLAG_SUCCESS = "02";//通过
	public static final String AUDI_FLAG_FAIL = "03";//拒绝
	public static final String AUDIT_FLAG_BACK = "04";//退回
	
	/**需求类型*/
	public static final String CYCLE_CATEGORY_01 = "01";//考核任务
	public static final String CYCLE_CATEGORY_02 = "02";//非考核任务
	
	/**反馈类型*/
	public static final String FEEDBACK_TYPE_MONTH_GEGIN = "01";//月初反馈
	public static final String FEEDBACK_TYPE_MONTH_END = "02";//月末反馈
	public static final String FEEDBACK_TYPE_BSF = "03";//登记BSF
	public static final String FEEDBACK_TYPE_RMD = "04";//登记青铜器
	
	/**任务类型*/
	public static final String FEEDBACK_CATEGORY_MAIN = "01";//主任务
	public static final String FEEDBACK_CATEGORY_SUB = "02";//子任务
	public static final String FEEDBACK_CATEGORY_PRCS = "03";//流程
	public static final String FEEDBACK_CATEGORY_FUN = "04";//功能点管理
	
	/**反馈审核类型*/
	public static final String FEEDBACK_AUDIT_STATUS_01 = "01";//审核通过
	public static final String FEEDBACK_AUDIT_STATUS_02 = "02";//审核未通过
	
	/**任务类型*/
	public static final String TASK_TYPE_MAIN = "01";//主任务
	public static final String TASK_TYPE_SUB = "02";//子任务
	
	/**编码方式*/
	public static final String CHARSET_GB2312 = "gb2312";
	public static final String CHARSET_ISO8859 = "ISO-8859-1";
	
	/**角色名称*/
	public static final String USER_ROLE_NAME_CENTER = "中心经理";
	/**角色名称*/
	public static final String USER_ROLE_NAME_PRODUCT = "产品经理";
	/**角色名称*/
	public static final String USER_ROLE_NAME_DEPARTMENT = "部门经理";	
	
	/**需求类型*/
	public static final String REQ_TYPE_01 = "01";//开发类
	
	/**产品研究与分析*/
	public static final String SUP_WORK_TYPE1 = "45";
	/**联调测试*/
	public static final String SUP_WORK_TYPE2 = "46";
	/**数据变更*/
	public static final String SUP_WORK_TYPE3 = "47";
	/**用户体验*/
	public static final String SUP_WORK_TYPE4 = "48";
	/**技术研究*/
	public static final String SUP_WORK_TYPE5 = "49";
	
	/**部门id*/
	public static final String DEPT_ID = "002002";
	
	/**错误码管理*/
	public static final String EMAIL_NAME="code_review@hisuntech.com";
	public static final String EMAIL_PSWD="hisun@248!@#";
	public static final String VERSION="version_it@hisuntech.com";
	
	/**投产管理*/
	/*public static final String P_EMAIL_NAME="deliver_sys@hisuntech.com";
	public static final String P_EMAIL_PSWD="hisun@248!@#";*/
	public static final String P_EMAIL_NAME="code_review@hisuntech.com";
	public static final String P_EMAIL_PSWD="hisun@248!@#";
	
//	//产品组邮箱
//	public static final String PM_DEPT_MAIL="wu_lan1@hisuntech.com";
	
	/*public static final String EMAIL_NAME="code_review@hisuntech.com";
	public static final String EMAIL_PSWD="code_review##";
	public static final String VERSION="liu_chang@hisuntech.com";*/
	
	public static Integer getAccountLockTime() {
		return accountLockTime;
	}
	public static void setAccountLockTime(Integer accountLockTime) {
		Constant.accountLockTime = accountLockTime;
	} 
	public static Integer getAccountLockCount() {
		return accountLockCount;
	}
	public static void setAccountLockCount(Integer accountLockCount) {
		Constant.accountLockCount = accountLockCount;
	}  
	public static void main (String args[]) {
		
		
		String aStr []= {"abc", "uvw", "xyz", "123", "456" };
		System.out.println(DigestUtils.md5Hex("111111"));
		boolean tt = ArrayUtils.contains(Constant.captchaTypes, CaptchaType.adminUserLogin);
		boolean tt2 = ArrayUtils.contains(Constant.captchaTypes, CaptchaType.adminUserLogin);
	}
}

	

