package com.cmpay.lemon.monitor.utils;

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

public class SendExcelOperationResultProblemUtil {
	public  String createExcel(String path, List<ProductionDO> list, List<ProductionDO> listTotal, List<List<ProblemDO>> proBeanList, String userName) throws Exception {

		File file = new File(path);
		WritableWorkbook book = null;
		book = Workbook.createWorkbook(file);
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		setHeader(sheet,list);
		String[] params = setBody(sheet, list,proBeanList,userName); // 设置Excel内容主体信息
		setTotal(sheet, listTotal,params); // 设置Excel内容主体信息
		book.write();
		book.close();
		return file.getAbsolutePath();
	}
	
	private  void setHeader(WritableSheet sheet, List<ProductionDO> rowList) throws WriteException {
		String[] headerNames = new String[]{
				"投产编号","产品名称","需求名称及内容简述","基地负责人","产品经理","生产验证方式","验证结果"};
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
		addCell(sheet, 0, 0, "每周投产通报", headerFormat,550,5);
		addCell(sheet, 1, 0, "投产总协调人", headerFormat,550,5);
		addCell(sheet, 1, 3, "日期", headerFormat,550,5);
		addCell(sheet, 2, 0, "本次投产CR总数", headerFormat,550,10);
		addCell(sheet, 2, 3, "本次投产PLOG总数", headerFormat,550,20);
		addCell(sheet, 3, 0, "本次投产涉及的产品/模块", headerFormat,550,20);
		 sheet.mergeCells(0, 4, 6, 4); 
		addCell(sheet, 4, 0, "投产清单", headerFormat,550,5);
		for(int i=5,len=headerNames.length;i<len+5;i++) {
			addCell(sheet, 5, i-5, headerNames[i-5], headerFormat,550,5);
		}
		int len=rowList.size()+5;
		sheet.mergeCells(0, len+1, 6, len+1); 
		addCell(sheet, len+1, 0, "问题汇报纪录", headerFormat,550,5);
		sheet.mergeCells(0, len+9, 6, len+9); 
		addCell(sheet, len+9, 0, "跟进事项", headerFormat,550,5);
		sheet.mergeCells(0, len+10, 1, len+10); 
		addCell(sheet, len+10, 0, "投产编号", headerFormat,550,5);
		sheet.mergeCells(2, len+10, 4, len+10); 
		addCell(sheet, len+10, 2, "需求名称及内容简述", headerFormat,550,5);
		addCell(sheet, len+10, 5, "生产验证方式", headerFormat,550,5);
		addCell(sheet, len+10, 6, "跟进人", headerFormat,550,5);
	}
	
	private  String[] setBody(WritableSheet sheet, List<ProductionDO> rowList, List<List<ProblemDO>> proBeanList, String userName) throws Exception {
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
			if(bean.getProNumber().contains("CR")){
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
		addCell(sheet, 2, 1, countCR+"", bodyFormat,0,20);
		sheet.mergeCells(4, 2, 6, 2); 
		addCell(sheet, 2, 4, countP+"", bodyFormat,0,20);
		StringBuffer sb=new StringBuffer();
		List<String> modules=new ArrayList<String>();
		
		for(int i=0;i<rowList.size();i++){
			String module=rowList.get(i).getProModule();
			boolean isRe=false;
			for(int j=0;j<modules.size();j++){
				if(modules.get(j).equals(module)){
					isRe=true;
					break;
				}
			}
			if(!isRe){
				modules.add(module);
			}
		}
		for(int j=0;j<modules.size();j++){
			if(j==modules.size()-1){
				sb.append(modules.get(j));
			}else{
			sb.append(modules.get(j)+"、");
			}
		}
		
		sheet.mergeCells(1, 3, 6, 3); 
		addCell(sheet, 3, 1, sb.toString(), bodyFormat,0,20);
		
		for (int i = 5; i < rowList.size()+5; i++) {
			msb = rowList.get(i-5);
			int k = -1;
			
			//投产编号
			addCell(sheet, i+1, ++k, msb.getProNumber(), bodyFormat,0,20);
			//产品名称
			addCell(sheet, i+1, ++k, msb.getProModule(), bodyFormat,0,15);
			//需求名称及内容简述
			addCell(sheet, i+1, ++k, msb.getProNeed(), bodyFormat,0,50);
			//基地负责人
			addCell(sheet, i+1, ++k, msb.getBasePrincipal(), bodyFormat,0,15);
			//产品经理
			addCell(sheet, i+1, ++k, msb.getProManager(), bodyFormat,0,15);
			//生产验证方式
			addCell(sheet, i+1, ++k, msb.getValidation(), bodyFormat,0,20);
			//验证结果
			if(msb.getValidation().equals("当晚验证")){
				if(msb.getProStatus().equals("投产验证完成")){
					addCell(sheet, i+1, ++k,"验证通过", bodyFormat,0,20);
				}else{
					addCell(sheet, i+1, ++k,"验证未通过", bodyFormat,0,20);
				}
			}
			if(msb.getValidation().equals("隔日验证")){
				if(msb.getProStatus().equals("投产验证完成")){
					addCell(sheet, i+1, ++k,"验证通过", bodyFormat,0,20);
				}else{
					addCell(sheet, i+1, ++k,"验证未通过", bodyFormat,0,20);
				}
			}
			if(msb.getValidation().equals("待业务触发验证")){
			addCell(sheet, i+1, ++k, "待业务触发验证", bodyFormat,0,20);
			}
		}
		int len=rowList.size()+5;
		sheet.mergeCells(0, len+2, 6, len+8); 
		StringBuffer stb=new StringBuffer();
		int rowNum=1;
		for(int i=0;i<proBeanList.size();i++){
			List<ProblemDO> list=proBeanList.get(i);
			for(int j=0;j<list.size();j++){
			
				if(list.get(j).getProblemDetail()!=null && !list.get(j).getProblemDetail().equals("")){
				stb.append(rowNum+"、"+list.get(j).getProblemDetail()+"。\r\n");
				}
				rowNum++;
			}
		}
		addCell(sheet, len+2, 0, stb.toString(), bodyFormat,0,20);
		
		int row=len+10;
		for(int i=len+10;i<rowList.size()+len+10;i++){
			msb = rowList.get(i-(len+10));
			if(msb.getProStatus().equals("部署完成待验证")){
			//投产编号
			sheet.mergeCells(0, row+1, 1, row+1);
			addCell(sheet, row+1, 0, msb.getProNumber(), bodyFormat,0,20);
			//需求名称及内容简述
			sheet.mergeCells(2, row+1, 4, row+1); 
			addCell(sheet, row+1, 2, msb.getProNeed(), bodyFormat,0,50);
			//产品模块
			addCell(sheet, row+1, 5, msb.getValidation(), bodyFormat,0,15);
			//验证人
			addCell(sheet, row+1, 6, msb.getIdentifier(), bodyFormat,0,15);
			row++;
			}
		}
		if(row==len+10){
			sheet.mergeCells(0, row+1, 6, row+1);
			addCell(sheet, row+1, 0, "", bodyFormat,0,15);
			row++;
		}
		sheet.mergeCells(0, row+1, 6, row+1); 
		addCell(sheet, row+1, 0,"附注:此文档发送对象：产品开发、产品支持中心总经理室成员、所有部门经理、问题提出人、问题处理人、跟进项跟进人等干系人。", bodyFormat,0,15);
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
