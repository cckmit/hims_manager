package com.cmpay.lemon.monitor.utils;

import com.cmpay.lemon.monitor.entity.OperationApplicationDO;
import com.cmpay.lemon.monitor.entity.ProductionDO;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SendExcelProductionVerificationIsNotTimely {
	public  String createExcel(String path, List<ProductionDO> list, List<ProductionDO> listTotal, List<OperationApplicationDO> listTwo) throws Exception {

		File file = new File(path);
		WritableWorkbook book = null;
		book = Workbook.createWorkbook(file);
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		setHeader(sheet);
		String[] params = setBody(sheet, list,listTwo); // 设置Excel内容主体信息
		setTotal(sheet, listTotal,params); // 设置Excel内容主体信息
		book.write();
		book.close();
		return file.getAbsolutePath();
	}
	
	private  void setHeader(WritableSheet sheet) throws WriteException {
		String[] headerNames = new String[]{
				"投产编号/系统操作编号","投产/操作内容简述","投产/操作类型","投产/操作日期","申请部门","验证人","当前状态","已投产/操作天数"
			/*	"投产编号","需求名称及内容简述","投产类型","计划投产日期","申请部门","投产申请人","申请人联系方式",
				"产品所属模块","业务需求提出人","基地负责人","产品经理","投产状态","是否更新数据库数据","是否更新数据库（表）结构（包含DDL语句）","投产后是否需要运维监控",
				"是否涉及证书","是否预投产验证","不能预投产验证原因","预投产验证结果","验证人 ","验证人联系方式"
				,"验证复核人","验证复核人联系方式","生产验证方式","开发负责人","审批人 ","版本更新操作人"
				,"备注 (影响范围,其它补充说明)","不能走正常投产原因","当天不投产的影响"*/};
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < headerNames.length; i++) {
			list.add(headerNames[i]);
		}
		WritableCellFormat headerFormat = new WritableCellFormat();
		//水平居中对齐
		headerFormat.setAlignment(Alignment.CENTRE);
		//竖直方向居中对齐
		headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		//设置边框
		headerFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		//设置字体
		headerFormat.setFont(new WritableFont(WritableFont.COURIER,11, WritableFont.BOLD,false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
		//设置背景颜色
		headerFormat.setBackground(Colour.GREY_25_PERCENT);
		
		for(int i=0,len=headerNames.length;i<len;i++) {
			addCell(sheet, 0, i, headerNames[i], headerFormat,550,5);
		}
	}
	
	private  String[] setBody(WritableSheet sheet, List<ProductionDO> rowList,List<OperationApplicationDO> listTwo) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
		bodyFormat.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		WritableCellFormat bodyFormatLeft = new WritableCellFormat();
		bodyFormatLeft.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormatLeft.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormatLeft.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);		
		ProductionDO msb = null;
		OperationApplicationDO msb2=null;
		int totalRow = 0;
		BigDecimal totalWorkloadPoint = new BigDecimal(0.0);
		BigDecimal currentWorkloadPoint = new BigDecimal(0.0);
		String [] params = new String[3];

		for (int i = 0; i < rowList.size(); i++) {
			msb = rowList.get(i);
			int k = -1;
			
			//投产编号
			addCell(sheet, i+1, ++k, msb.getProNumber(), bodyFormat,0,20);
			//需求名称及内容简述
			addCell(sheet, i+1, ++k, msb.getProNeed(), bodyFormat,0,50);
			//投产类型
			addCell(sheet, i+1, ++k, msb.getProType(), bodyFormat,0,15);
			//计划投产日期
			// 日期转换
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(msb.getProDate()!=null)
			addCell(sheet, i+1, ++k, sdf.format(msb.getProDate()), bodyFormat,0,20);
			//申请部门
			addCell(sheet, i+1, ++k, msb.getApplicationDept(), bodyFormat,0,15);

			//产品经理
			addCell(sheet, i+1, ++k, msb.getIdentifier(), bodyFormat,0,20);
			//当前需求状态
			addCell(sheet, i+1, ++k, 	msb.getProStatus(), bodyFormat,0,20);
			//已投产天数
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(sdf.parse(sdf.format(new Date())));
			c2.setTime(sdf.parse(sdf.format(msb.getProDate())));
			long day = (sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(msb.getProDate())).getTime()) / (24 * 60 * 60 * 1000);
			addCell(sheet, i+1, ++k, String.valueOf(day), bodyFormat,0,20);
		}
        for (int i = rowList.size(); i < listTwo.size()+rowList.size(); i++) {
            int k = -1;
			msb2 = listTwo.get(i- rowList.size());
			//投产编号
			addCell(sheet, i+1, ++k, msb2.getOperNumber(), bodyFormat,0,20);
			//需求名称及内容简述
			addCell(sheet, i+1, ++k, msb2.getOperRequestContent(), bodyFormat,0,50);
			//投产类型
			addCell(sheet, i+1, ++k, msb2.getSysOperType(), bodyFormat,0,15);
			//计划投产日期
			// 日期转换
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(msb2.getProposeDate()!=null)
				addCell(sheet, i+1, ++k, sdf.format(msb2.getProposeDate()), bodyFormat,0,20);
			//申请部门
			addCell(sheet, i+1, ++k, msb2.getApplicationSector(), bodyFormat,0,15);

			//产品经理
			addCell(sheet, i+1, ++k, msb2.getIdentifier(), bodyFormat,0,20);
			//产品投产状态
			addCell(sheet, i+1, ++k, msb2.getOperStatus(), bodyFormat,0,15);
			//已投产天数
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(sdf.parse(sdf.format(new Date())));
			c2.setTime(sdf.parse(sdf.format(msb2.getProposeDate())));
			long day = (sdf.parse(sdf.format(new Date())).getTime() - sdf.parse(sdf.format(msb2.getProposeDate())).getTime()) / (24 * 60 * 60 * 1000);
			addCell(sheet, i+1, ++k, String.valueOf(day), bodyFormat,0,20);
		}


		params[0]=String.valueOf(totalRow);
		params[1]=String.valueOf(totalWorkloadPoint);
		params[2]=String.valueOf(currentWorkloadPoint);
		return params;
	}
	public  String createCzlcExcel_1(String path,List<ProductionDO> list, List<ProductionDO> listTotal) throws Exception {

		File file = new File(path);
		WritableWorkbook book = null;
		book = Workbook.createWorkbook(file);
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		setCzlxHeader_1(sheet);
		String[] params = setCzlcBody_1(sheet, list); // 设置Excel内容主体信息
		setTotal(sheet, listTotal,params); // 设置Excel内容主体信息
		book.write();
		book.close();
		return file.getAbsolutePath();
	}
	private  void setTotal(WritableSheet sheet, List<ProductionDO> rowList, String[] params) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
		bodyFormat.setAlignment(Alignment.CENTRE); // 水平居中对齐
		bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		
		WritableCellFormat bodyFormatLeft = new WritableCellFormat();
		bodyFormatLeft.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormatLeft.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormatLeft.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		}
	
	private  String[] setCzlcBody_1(WritableSheet sheet, List<ProductionDO> rowList) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
		bodyFormat.setAlignment(Alignment.CENTRE); // 水平居中对齐
		bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		WritableCellFormat bodyFormatLeft = new WritableCellFormat();
		bodyFormatLeft.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormatLeft.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormatLeft.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);		
		ProductionDO msb = null;
		int totalRow = 0;
		BigDecimal totalWorkloadPoint = new BigDecimal(0.0);
		BigDecimal currentWorkloadPoint = new BigDecimal(0.0);
		String [] params = new String[3];
		for (int i = 0; i < rowList.size(); i++) {
			msb = rowList.get(i);
			int k = -1;
			
			//投产编号
			addCell(sheet, i+1, ++k, msb.getProNumber(), bodyFormat,0,20);
			//需求名称及内容简述
			addCell(sheet, i+1, ++k, msb.getProNeed(), bodyFormat,0,50);
			//投产类型
			addCell(sheet, i+1, ++k, msb.getProType(), bodyFormat,0,15);
			//计划投产日期
			// 日期转换
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(msb.getProDate()!=null)
			addCell(sheet, i+1, ++k, sdf.format(msb.getProDate()), bodyFormat,0,20);
			//申请部门
			addCell(sheet, i+1, ++k, msb.getApplicationDept(), bodyFormat,0,15);
			//投产申请人
			addCell(sheet, i+1, ++k, msb.getProApplicant(), bodyFormat,0,10);
			//申请人联系方式
			addCell(sheet, i+1, ++k, msb.getApplicantTel(), bodyFormat,0,20);
			//产品所属模块
			addCell(sheet, i+1, ++k, msb.getProModule(), bodyFormat,0,20);
			//业务需求提出人
			addCell(sheet, i+1, ++k, msb.getBusinessPrincipal(), bodyFormat,0,20);
			//基地负责人
			addCell(sheet, i+1, ++k, msb.getBasePrincipal(), bodyFormat,0,20);
			//产品经理
			addCell(sheet, i+1, ++k, msb.getProManager(), bodyFormat,0,20);
			//投产状态
			addCell(sheet, i+1, ++k, msb.getProStatus(), bodyFormat,0,20);
			//是否更新数据库数据
			addCell(sheet, i+1, ++k, msb.getIsUpDatabase(), bodyFormat,0,20);
			//是否更新数据库（表）结构（包含DDL语句）
			addCell(sheet, i+1, ++k, msb.getIsUpStructure(), bodyFormat,0,20);
			//投产后是否需要运维监控
			addCell(sheet, i+1, ++k, msb.getProOperation(), bodyFormat,0,20);
			//是否涉及证书
			addCell(sheet, i+1, ++k, msb.getIsRefCerificate(), bodyFormat,0,20);
			//是否预投产验证
			addCell(sheet, i+1, ++k, msb.getIsAdvanceProduction(), bodyFormat,0,20);
			//不能预投产验证原因
			if(msb.getNotAdvanceReason()!=null){
			addCell(sheet, i+1, ++k, msb.getNotAdvanceReason(), bodyFormat,0,20);
			}else{
				addCell(sheet, i+1, ++k,"", bodyFormat,0,20);
			}
			//预投产验证结果
			if(msb.getProAdvanceResult()!=null){
				addCell(sheet, i+1, ++k, msb.getProAdvanceResult(), bodyFormat,0,20);
				}else{
					addCell(sheet, i+1, ++k,"", bodyFormat,0,20);
				}
			//验证人 
			addCell(sheet, i+1, ++k, msb.getIdentifier(), bodyFormat,0,20);
			//验证人联系方式
			addCell(sheet, i+1, ++k, msb.getIdentifierTel(), bodyFormat,0,20);
			//验证复核人
			addCell(sheet, i+1, ++k, msb.getProChecker(), bodyFormat,0,20);
			//验证复核人联系方式
			addCell(sheet, i+1, ++k, msb.getCheckerTel(), bodyFormat,0,20);
			//生产验证方式
			addCell(sheet, i+1, ++k, msb.getValidation(), bodyFormat,0,20);
			//开发负责人
			addCell(sheet, i+1, ++k, msb.getDevelopmentLeader(), bodyFormat,0,20);
			//审批人 
			addCell(sheet, i+1, ++k, msb.getApprover(), bodyFormat,0,20);
			//版本更新操作人
			addCell(sheet, i+1, ++k, msb.getUpdateOperator(), bodyFormat,0,20);
			//备注
			addCell(sheet, i+1, ++k, msb.getRemark(), bodyFormat,0,20);
			//不能走正常投产原因
			addCell(sheet, i+1, ++k, msb.getUrgentReasonPhrase(), bodyFormat,0,20);
			//当天不投产的影响
			addCell(sheet, i+1, ++k, msb.getNotProductionImpact(), bodyFormat,0,20);
		}
		params[0]=String.valueOf(totalRow);
		params[1]=String.valueOf(totalWorkloadPoint);
		params[2]=String.valueOf(currentWorkloadPoint);
		return params;
	}
	private  void setCzlxHeader_1(WritableSheet sheet) throws WriteException {
		String[] headerNames = new String[]{
				"投产编号","需求名称及内容简述","投产类型","计划投产日期","申请部门","投产申请人","申请人联系方式",
				"产品所属模块","业务需求提出人","基地负责人","产品经理","投产状态","是否更新数据库数据","是否更新数据库（表）结构（包含DDL语句）","投产后是否需要运维监控",
				"是否涉及证书","是否预投产验证","不能预投产验证原因","预投产验证结果","验证人 ","验证人联系方式"
				,"验证复核人","验证复核人联系方式","生产验证方式","开发负责人","审批人 ","版本更新操作人"
				,"备注 (影响范围,其它补充说明)","不能走正常投产原因","当天不投产的影响"};
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < headerNames.length; i++) {
			list.add(headerNames[i]);
		}
		WritableCellFormat headerFormat = new WritableCellFormat();
		//水平居中对齐
		headerFormat.setAlignment(Alignment.CENTRE);
		//竖直方向居中对齐
		headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
		//设置边框
		headerFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		//设置字体
		headerFormat.setFont(new WritableFont(WritableFont.COURIER,11, WritableFont.BOLD,false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK));
		//设置背景颜色
		headerFormat.setBackground(Colour.GREY_25_PERCENT);
		
		for(int i=0,len=headerNames.length;i<len;i++) {
			addCell(sheet, 0, i, headerNames[i], headerFormat,550,5);
		}
	}
	
	
	private static void addCell(WritableSheet sheet, int row, int column, String data, WritableCellFormat format, int rowWidth, int columnWidth) throws WriteException {
		Label label = new Label(column,row,data,format);
		if (rowWidth==0) {
			rowWidth = 350;
		}
		if (columnWidth==0) {
			columnWidth = 20;
		}
		sheet.addCell(label);
		sheet.setRowView(row, rowWidth);
		sheet.setColumnView(column,columnWidth);
	}
}
