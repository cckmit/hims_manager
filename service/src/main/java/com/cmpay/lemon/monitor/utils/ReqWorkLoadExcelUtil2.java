package com.cmpay.lemon.monitor.utils;

import com.cmpay.lemon.monitor.bo.DemandBO;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReqWorkLoadExcelUtil2 {

	public  String createExcel(String path, List list, List<DemandBO> listTotal, String[] headStr, String type) throws Exception {
		File file = new File(path);
		WritableWorkbook book = null;
		book = Workbook.createWorkbook(file);
		WritableSheet sheet = book.createSheet(file.getName(), 0);//创建sheet页
		setHeader(sheet,headStr);//设置表头
		setBody(sheet, list, type); // 设置Excel内容主体信息
		setTotal(sheet, listTotal); // 设置Excel内容主体信息
		book.write();
		book.close();
		return file.getAbsolutePath();
	}

	private  void setHeader(WritableSheet sheet,String[] headStr) throws WriteException {
		//表头修改补充
		String[] headerNames = headStr;
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

	private  String[] setBody(WritableSheet sheet, List rowList,String type) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
		bodyFormat.setAlignment(Alignment.CENTRE); // 水平居中对齐
		bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		WritableCellFormat bodyFormatLeft = new WritableCellFormat();
		bodyFormatLeft.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormatLeft.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormatLeft.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		DemandBO demand = null;
		int totalRow = 0;
		BigDecimal totalWorkloadPoint = new BigDecimal(0.0);
		BigDecimal currentWorkloadPoint = new BigDecimal(0.0);
		String [] params = new String[3];

		if(type.equals("3")) {
			goExportCountForDevp(sheet,rowList,bodyFormat);
		}
		params[0]=String.valueOf(totalRow);
		params[1]=String.valueOf(totalWorkloadPoint);
		params[2]=String.valueOf(currentWorkloadPoint);
		return params;
	}

	private  void setTotal(WritableSheet sheet, List<DemandBO> rowList) throws Exception {
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

	// 各部门工作量月统计汇总报表导出  goExportCountForDevp
	private static void goExportCountForDevp(WritableSheet sheet,List rowList,WritableCellFormat format) throws WriteException {
		int i = 0;
		int k = -1;
		DecimalFormat df = new DecimalFormat("0.00");
		//银行合作研发部
		addCell(sheet, i+1, ++k, rowList.get(0) == null ? "0" : df.format(rowList.get(0)) + "", format,0,20);
		//移动支付研发部
		addCell(sheet, i+1, ++k, rowList.get(1) == null ? "0" : df.format(rowList.get(1)) + "", format,0,20);
		//营销服务研发部
		addCell(sheet, i+1, ++k, rowList.get(2) == null ? "0" : df.format(rowList.get(2)) + "", format,0,20);
		//电商支付研发部
		addCell(sheet, i+1, ++k, rowList.get(3) == null ? "0" : df.format(rowList.get(3)) + "", format,0,20);
		//风控大数据研发部
		addCell(sheet, i+1, ++k, rowList.get(4) == null ? "0" : df.format(rowList.get(4)) + "", format,0,20);
		//互联网金融研发部
		addCell(sheet, i+1, ++k, rowList.get(5) == null ? "0" : df.format(rowList.get(5)) + "", format,0,20);
		//前端技术研发部
		addCell(sheet, i+1, ++k, rowList.get(6) == null ? "0" : df.format(rowList.get(6)) + "", format,0,20);
		//基础应用研发部
		addCell(sheet, i+1, ++k, rowList.get(7) == null ? "0" : df.format(rowList.get(7)) + "", format,0,20);
		//客户端研发部
		addCell(sheet, i+1, ++k, rowList.get(8) == null ? "0" : df.format(rowList.get(8)) + "", format,0,20);
		//公共缴费研发部
		addCell(sheet, i+1, ++k, rowList.get(9) == null ? "0" : df.format(rowList.get(9)) + "", format,0,20);
		//产品测试部
		addCell(sheet, i+1, ++k, rowList.get(10) == null ? "0" : df.format(rowList.get(10)) + "", format,0,20);
		//平台架构部
		addCell(sheet, i+1, ++k, rowList.get(11) == null ? "0" : df.format(rowList.get(11)) + "", format,0,20);
		//产品研究部
		addCell(sheet, i+1, ++k, rowList.get(12) == null ? "0" : df.format(rowList.get(12)) + "", format,0,20);
		//设计项目组
		addCell(sheet, i+1, ++k, rowList.get(13) == null ? "0" : df.format(rowList.get(13)) + "", format,0,20);
		//设计项目组
		addCell(sheet, i+1, ++k, rowList.get(14) == null ? "0" : df.format(rowList.get(14)) + "", format,0,20);
		//设计项目组
		addCell(sheet, i+1, ++k, rowList.get(15) == null ? "0" : df.format(rowList.get(15)) + "", format,0,20);
		//设计项目组
		addCell(sheet, i+1, ++k, rowList.get(16) == null ? "0" : df.format(rowList.get(16)) + "", format,0,20);
		//设计项目组
		addCell(sheet, i+1, ++k, rowList.get(17) == null ? "0" : df.format(rowList.get(17)) + "", format,0,20);
		//设计项目组
		addCell(sheet, i+1, ++k, rowList.get(18) == null ? "0" : df.format(rowList.get(18)) + "", format,0,20);
		//设计项目组
		addCell(sheet, i+1, ++k, rowList.get(19) == null ? "0" : df.format(rowList.get(19)) + "", format,0,20);
	}
}
