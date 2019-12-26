package com.cmpay.lemon.monitor.utils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.cmpay.lemon.monitor.entity.ProductionDO;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class SendExcelOperationResultProductionUtil {
	public  String createExcel(String path, List<ProductionDO> list, List<ProductionDO> listTotal) throws Exception {

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
	
	private  void setHeader(WritableSheet sheet) throws WriteException {
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
		headerFormat.setFont(new WritableFont(WritableFont.COURIER,11,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLACK));
		//设置背景颜色
		headerFormat.setBackground(Colour.GREY_25_PERCENT);
		
		for(int i=0,len=headerNames.length;i<len;i++) {
			addCell(sheet, 0, i, headerNames[i], headerFormat,550,5);
		}
	}
	
	private  String[] setBody(WritableSheet sheet, List<ProductionDO> rowList) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
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
		for (int i = 0; i < rowList.size(); i++) {
			msb = rowList.get(i);
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
			if(msb.getProStatus().equals("投产验证完成")){
				addCell(sheet, i+1, ++k,"验证通过", bodyFormat,0,20);
			}else{
				addCell(sheet, i+1, ++k,"验证未完成", bodyFormat,0,20);
			}
		}
		params[0]=String.valueOf(totalRow);
		params[1]=String.valueOf(totalWorkloadPoint);
		params[2]=String.valueOf(currentWorkloadPoint);
		return params;
	}
	
	private  void setTotal(WritableSheet sheet, List<ProductionDO> rowList,String[] params) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
		bodyFormat.setAlignment(Alignment.CENTRE); // 水平居中对齐
		bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		
		WritableCellFormat bodyFormatLeft = new WritableCellFormat();
		bodyFormatLeft.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormatLeft.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormatLeft.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		}
	
	private static void addCell(WritableSheet sheet,int row,int column,String data,WritableCellFormat format,int rowWidth,int columnWidth) throws WriteException {
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
