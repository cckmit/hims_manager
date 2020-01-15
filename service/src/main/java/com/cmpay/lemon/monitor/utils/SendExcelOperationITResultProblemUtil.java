package com.cmpay.lemon.monitor.utils;

import com.cmpay.lemon.monitor.bo.ITProductionBO;
import com.cmpay.lemon.monitor.entity.ProblemDO;
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
import java.util.Date;
import java.util.List;

/**
 * IT中心每周投产日情况通报
 */
public class SendExcelOperationITResultProblemUtil {
	public  String createExcel(String path, List<ProductionDO> list, List<ProductionDO> listTotal, ITProductionBO itProductionBO, String userName) throws Exception {

		File file = new File(path);
		WritableWorkbook book = null;
		book = Workbook.createWorkbook(file);
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		setHeader(sheet,list);
		String[] params = setBody(sheet, list,itProductionBO,userName); // 设置Excel内容主体信息
		setTotal(sheet, listTotal,params); // 设置Excel内容主体信息
		book.write();
		book.close();
		return file.getAbsolutePath();
	}
	
	private  void setHeader(WritableSheet sheet, List<ProductionDO> rowList) throws WriteException {
		String[] headerNames = new String[]{
				"CR/PLOG编号","问题描述","解决情况描述","解决时间"};
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
		

		
		sheet.mergeCells(0, 0, 6, 0);
		addCell(sheet, 0, 0, "IT中心每周投产日情况通报", headerFormat,550,5);
		addCell(sheet, 1, 0, "版本组主责人员", headerFormat,550,5);
		addCell(sheet, 1, 3, "日期", headerFormat,550,5);
		addCell(sheet, 2, 0, "更新开始时间", headerFormat,550,10);
		addCell(sheet, 2, 3, "离开机房时间", headerFormat,550,20);
		addCell(sheet, 3, 0, "投产支持主要人员", headerFormat,550,20);
		addCell(sheet, 4, 0, "本次投产CR及PLOG总数", headerFormat,550,20);
		addCell(sheet, 5, 0, "Weblogic重启情况", headerFormat,550,20);
		addCell(sheet, 6, 0, "服务台拨测人员", headerFormat,550,5);
		addCell(sheet, 6, 3, "拨测结果", headerFormat,550,5);
		addCell(sheet, 7, 0, "告警监控人员", headerFormat,550,10);
		addCell(sheet, 7, 3, "投产后告警情况（网管、应用监控）", headerFormat,550,20);

		sheet.mergeCells(0, 8, 6, 8);
		addCell(sheet, 8, 0, "问题清单", headerFormat,550,5);
		sheet.mergeCells(0, 9, 1, 9);
		addCell(sheet, 9, 0, "CR/PLOG编号", headerFormat,550,5);
		sheet.mergeCells(2, 9, 4, 9);
		addCell(sheet, 9, 2, "问题描述", headerFormat,550,5);
		addCell(sheet, 9, 5, "解决情况描述", headerFormat,550,5);
		addCell(sheet, 9, 6, "解决时间", headerFormat,550,5);
		int len=rowList.size()+9;
		sheet.mergeCells(0, len+1, 6, len+1);
		addCell(sheet, len+1, 0, "本次投产总结及遗留事项", headerFormat,550,5);
		sheet.mergeCells(0, len+10, 1, len+10);
		addCell(sheet, len+10, 0, "投产编号", headerFormat,550,5);
		sheet.mergeCells(2, len+10, 3, len+10);
		addCell(sheet, len+10, 2, "需求名称及内容简述", headerFormat,550,5);
		addCell(sheet, len+10, 4, "开发负责人", headerFormat,550,5);
	}
	
	private  String[] setBody(WritableSheet sheet, List<ProductionDO> rowList, ITProductionBO itProductionBO, String userName) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
		bodyFormat.setWrap(true);
		bodyFormat.setAlignment(Alignment.LEFT); // 水平居中对齐
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
		
		sheet.mergeCells(1, 1, 2, 1);
		addCell(sheet, 1, 1, userName, bodyFormat,0,20);
		int countCR=0;
		int countP=0;
		for (ProductionDO bean : rowList) {
			if(bean.getProNumber().contains("REQ")){
				countCR++;
			}
			if(bean.getProNumber().contains("P")){
				countP++;
			}
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sheet.mergeCells(4, 1, 6, 1);
		addCell(sheet, 1, 4, sdf.format(new Date()), bodyFormat,0,20);
		sheet.mergeCells(1, 2, 2, 2);
		addCell(sheet, 2, 1, itProductionBO.getStartTime(), bodyFormat,0,20);
		sheet.mergeCells(4, 2, 6, 2);
		addCell(sheet, 2, 4, itProductionBO.getEndTime(), bodyFormat,0,20);

		
		sheet.mergeCells(1, 3, 6, 3);
		addCell(sheet, 3, 1,itProductionBO.getSupportStaff(), bodyFormat,0,20);

		sheet.mergeCells(1, 4, 6, 4);
		addCell(sheet, 4, 1,"（清单请看附录1）", bodyFormat,0,20);
		sheet.mergeCells(1, 5, 6, 5);
		addCell(sheet, 5, 1,itProductionBO.getRestartTheSituation(), bodyFormat,0,20);

		sheet.mergeCells(1, 6, 2, 6);
		addCell(sheet, 6, 1, itProductionBO.getServiceDeskTester(), bodyFormat,0,20);
		sheet.mergeCells(4, 6, 6, 6);
		addCell(sheet, 6, 4, itProductionBO.getTestResult(), bodyFormat,0,20);

		sheet.mergeCells(1, 7, 2, 7);
		addCell(sheet, 7, 1, itProductionBO.getAlarmMonitor(), bodyFormat,0,20);
		sheet.mergeCells(4, 7, 6, 7);
		addCell(sheet, 7, 4, itProductionBO.getAlarmMonitoring(), bodyFormat,0,20);


		for (int i = 9; i < rowList.size()+9; i++) {
			msb = rowList.get(i-9);
			int k = -1;
			sheet.mergeCells(0, i+1, 1, i+1);
			addCell(sheet, i+1, 0, "", bodyFormat,0,20);
			//需求名称及内容简述
			sheet.mergeCells(2, i+1, 4, i+1);
			addCell(sheet, i+1, 2, "", bodyFormat,0,50);
			//产品模块
			addCell(sheet, i+1, 5, "", bodyFormat,0,15);
			//验证人
			addCell(sheet, i+1, 6, "", bodyFormat,0,15);
		}
		int len=rowList.size()+9;
		sheet.mergeCells(0, len+2, 6, len+8);
		addCell(sheet, len+2, 0, itProductionBO.getProductionOfTheSummary(), bodyFormat,0,20);


		int row=len+10;
		for(int i=len+10;i<rowList.size()+len+10;i++){
			msb = rowList.get(i-(len+10));
			System.err.println(msb);
			//投产编号
			sheet.mergeCells(0, row+1, 1, row+1);
			addCell(sheet, row+1, 0, msb.getProNumber(), bodyFormat,0,20);
			//需求名称及内容简述
			sheet.mergeCells(2, row+1, 3, row+1);
			addCell(sheet, row+1, 2, msb.getProNeed(), bodyFormat,0,50);
			//产品模块
			addCell(sheet, row+1, 4, msb.getDevelopmentLeader(), bodyFormat,0,15);
			row++;
		}

		params[0]=String.valueOf(totalRow);
		params[1]=String.valueOf(totalWorkloadPoint);
		params[2]=String.valueOf(currentWorkloadPoint);
		return params;
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
		if(label.getColumn()==0){
			sheet.setColumnView(0,30);
		}else if(label.getColumn()==3){
			sheet.setColumnView(3,25);
		}else{
		sheet.setColumnView(column,columnWidth);
		}
	}
}
