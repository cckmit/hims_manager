package com.cmpay.lemon.monitor.utils;

import com.cmpay.lemon.monitor.entity.ScheduleDO;
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
import java.util.List;

public class ExcelOperationDetailUtil {
	public  String createExcel(String path, List<ScheduleDO> list, List<ScheduleDO> listTotal) throws Exception {

		File file = new File(path);
		WritableWorkbook book = null;
		book = Workbook.createWorkbook(file);
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		setHeader(sheet);
		String[] params = setBody(sheet, list); // 设置Excel内容主体信息
		setTotal(sheet, listTotal,params); // 设置Excel内容主体信息
		book.write();
		book.close();
		return file.getAbsolutePath();
	}
	
	public  String createCzlcExcel(String path,List<ScheduleDO> list, List<ScheduleDO> listTotal) throws Exception {

		File file = new File(path);
		WritableWorkbook book = null;
		book = Workbook.createWorkbook(file);
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		setCzlxHeader(sheet);
		String[] params = setCzlcBody(sheet, list); // 设置Excel内容主体信息
		setTotal(sheet, listTotal,params); // 设置Excel内容主体信息
		book.write();
		book.close();
		return file.getAbsolutePath();
	}
	private  void setCzlxHeader(WritableSheet sheet) throws WriteException {
		String[] headerNames = new String[]{
				"序号","操作申请内容","提出日期","是否涉及SQL ","系统操作类型","操作申请状态","申请部门"
				,"申请人","验证人","开发负责人","SVN表名称","系统操作原因描述","影响分析描述"};
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
	
	private  void setHeader(WritableSheet sheet) throws WriteException {
		String[] headerNames = new String[]{
				"序号","投产编号","操作类型","操作时间 ","操作人员","操作前投产状态","操作后投产状态","操作类型变更原因"};
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
	
	private  String[] setCzlcBody(WritableSheet sheet, List<ScheduleDO> rowList) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
		bodyFormat.setAlignment(Alignment.CENTRE); // 水平居中对齐
		bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		WritableCellFormat bodyFormatLeft = new WritableCellFormat();
		bodyFormatLeft.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormatLeft.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormatLeft.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		ScheduleDO msb = null;
		int totalRow = 0;
		BigDecimal totalWorkloadPoint = new BigDecimal(0.0);
		BigDecimal currentWorkloadPoint = new BigDecimal(0.0);
		String [] params = new String[3];
		for (int i = 0; i < rowList.size(); i++) {
			msb = rowList.get(i);
			int k = -1;
			//序号
			addCell(sheet, i+1, ++k,(i+1)+"", bodyFormat,0,20);
			//操作申请内容 oper_request_content
			addCell(sheet, i+1, ++k, msb.getOperRequestContent(), bodyFormat,0,20);
			//提出日期
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(msb.getProposeDate()!=null)
//			addCell(sheet, i+1, ++k, sdf.format(msb.getPropose_date()), bodyFormat,0,15);
			addCell(sheet, i+1, ++k, msb.getProposeDate(), bodyFormat,0,15);
			//是否涉及SQL
			addCell(sheet, i+1, ++k, msb.getIsRefSql(), bodyFormat,0,20);
			//系统操作类型
			addCell(sheet, i+1, ++k, msb.getSysOperType(), bodyFormat,0,15);
			//操作申请状态
			addCell(sheet, i+1, ++k, msb.getOperStatus(), bodyFormat,0,15);
			//申请部门
			addCell(sheet, i+1, ++k, msb.getDevelopmentLeader(), bodyFormat,0,15);
			//申请人
			addCell(sheet, i+1, ++k, msb.getOperApplicant(), bodyFormat,0,20);
			//验证人
			addCell(sheet, i+1, ++k, msb.getIdentifier(), bodyFormat,0,20);
           //开发负责人
			addCell(sheet, i+1, ++k, msb.getDevelopmentLeader(), bodyFormat,0,20);
           //SVN表名称
			addCell(sheet, i+1, ++k, msb.getSvntabName(), bodyFormat,0,20);
			
			//系统操作原因描述  
			addCell(sheet, i+1, ++k, msb.getOperApplicationReason(), bodyFormat,0,20);
			//影响分析描述
			addCell(sheet, i+1, ++k, msb.getAnalysis(), bodyFormat,0,20);
			
			
		}
		params[0]=String.valueOf(totalRow);
		params[1]=String.valueOf(totalWorkloadPoint);
		params[2]=String.valueOf(currentWorkloadPoint);
		return params;
	}
	
	
	private  String[] setBody(WritableSheet sheet, List<ScheduleDO> rowList) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
		bodyFormat.setAlignment(Alignment.CENTRE); // 水平居中对齐
		bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		WritableCellFormat bodyFormatLeft = new WritableCellFormat();
		bodyFormatLeft.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormatLeft.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormatLeft.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);		
		ScheduleDO msb = null;
		int totalRow = 0;
		BigDecimal totalWorkloadPoint = new BigDecimal(0.0);
		BigDecimal currentWorkloadPoint = new BigDecimal(0.0);
		String [] params = new String[3];
		for (int i = 0; i < rowList.size(); i++) {
			msb = rowList.get(i);
			int k = -1;
			
			//序号
			addCell(sheet, i+1, ++k,(i+1)+"", bodyFormat,0,20);
			//投产编号
			addCell(sheet, i+1, ++k, msb.getProNumber(), bodyFormat,0,20);
			//操作类型
			addCell(sheet, i+1, ++k, msb.getOperationType(), bodyFormat,0,15);
			//操作时间
			addCell(sheet, i+1, ++k, msb.getScheduleTime().toString(), bodyFormat,0,15);
			// 日期转换
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			if(msb.getScheduleTime()!=null)
//			addCell(sheet, i+1, ++k, sdf.format(msb.getScheduleTime()), bodyFormat,0,20);
//			//操作人员
			addCell(sheet, i+1, ++k, msb.getProOperator(), bodyFormat,0,15);
			//操作前投产状态
			addCell(sheet, i+1, ++k, msb.getPreOperation(), bodyFormat,0,15);
			//操作后投产状态
			addCell(sheet, i+1, ++k, msb.getAfterOperation(), bodyFormat,0,15);
			//操作类型变更原因
			addCell(sheet, i+1, ++k, msb.getOperationReason(), bodyFormat,0,20);
		}
		params[0]=String.valueOf(totalRow);
		params[1]=String.valueOf(totalWorkloadPoint);
		params[2]=String.valueOf(currentWorkloadPoint);
		return params;
	}
	
	private  void setTotal(WritableSheet sheet, List<ScheduleDO> rowList, String[] params) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
		bodyFormat.setAlignment(Alignment.CENTRE); // 水平居中对齐
		bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		
		WritableCellFormat bodyFormatLeft = new WritableCellFormat();
		bodyFormatLeft.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormatLeft.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormatLeft.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
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
