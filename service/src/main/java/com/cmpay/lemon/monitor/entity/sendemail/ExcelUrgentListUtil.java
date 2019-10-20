package com.cmpay.lemon.monitor.entity.sendemail;

import com.cmpay.lemon.monitor.bo.ProductionBO;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

public class ExcelUrgentListUtil {
	public  String createExcel(String path, List<ProductionBO> list, List<ProductionBO> listTotal) throws Exception {

		File file = new File(path);
		WritableWorkbook book = null;
		book = Workbook.createWorkbook(file);
		WritableSheet sheet = book.createSheet(file.getName(), 0);
		setHeader(sheet,list);
		String[] params = setBody(sheet, list); // 设置Excel内容主体信息
		setTotal(sheet, listTotal,params); // 设置Excel内容主体信息
		book.write();
		book.close();
		return file.getAbsolutePath();
	}
	
	private  void setHeader(WritableSheet sheet, List<ProductionBO> rowList) throws WriteException {
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
		ProductionBO msb = rowList.get(0);
		 sheet.mergeCells(0, 0, 5, 0); 
			addCell(sheet, 0, 0, "救火更新申请表", headerFormat,1000,5);
			addCell(sheet, 1,0, "更新标题", headerFormat,0,20);
			addCell(sheet, 2,0, "申请部门", headerFormat,0,20);
			addCell(sheet, 2,2, "申请人", headerFormat,0,20);
			addCell(sheet, 2,4, "联系方式", headerFormat,0,20);
			addCell(sheet, 3,0, "投产编号", headerFormat,0,20);
			addCell(sheet, 3,2, "复核人",headerFormat,0,20);
			addCell(sheet, 3,4, "联系方式",headerFormat,0,20);
			addCell(sheet, 4,0, "验证测试类型", headerFormat,0,20);
			addCell(sheet, 4,2, "验证人", headerFormat,0,20);
			addCell(sheet, 4,4, "联系方式", headerFormat,0,20);
			if(msb.getIsAdvanceProduction().endsWith("否")){
				sheet.mergeCells(0, 5, 0, 6); 
				addCell(sheet, 5,0, "不做预投产验证原因", headerFormat,0,20);
				sheet.mergeCells(0, 7, 0, 8); 
				addCell(sheet, 7,0, "不走投产日正常投产原因", headerFormat,0,20);
				addCell(sheet, 9,0, "当天不投产的影响", headerFormat,0,20);
				addCell(sheet, 11,0, "更新要求完成时间说明：", headerFormat,0,20);
				addCell(sheet, 12,0, "是否有回退方案：", headerFormat,0,20);
				addCell(sheet, 13,0, "如需提前至当日24点前更新，需补充填写以下内容：", headerFormat,0,20);
				addCell(sheet, 14,0, "提前实施原因 ：", headerFormat,0,20);
				sheet.mergeCells(0, 15,0 , 16);
				addCell(sheet, 15,0, "是否影响客户使用", headerFormat,0,20);
				addCell(sheet, 15,2, "如不影响客户使用，请简要描述原因", headerFormat,0,40);
				addCell(sheet, 16,2, "如影响客户使用，描述具体影响范围", headerFormat,0,40);
				addCell(sheet, 17,0,"更新时间及预计操作时长", headerFormat,0,20);
			}else{
			sheet.mergeCells(0, 5, 0, 6); 
			addCell(sheet, 5,0, "不走投产日正常投产原因", headerFormat,0,20);
			addCell(sheet, 7,0, "当天不投产的影响", headerFormat,0,20);
			addCell(sheet, 9,0, "更新要求完成时间说明：", headerFormat,0,20);
			addCell(sheet, 10,0, "是否有回退方案：", headerFormat,0,20);
			addCell(sheet, 11,0, "如需提前至当日24点前更新，需补充填写以下内容：", headerFormat,0,20);
			addCell(sheet, 12,0, "提前实施原因 ：", headerFormat,0,20);
			sheet.mergeCells(0, 13,0 , 14);
			addCell(sheet, 13,0, "是否影响客户使用", headerFormat,0,20);
			addCell(sheet, 13,2, "如不影响客户使用，请简要描述原因", headerFormat,0,40);
			addCell(sheet, 14,2, "如影响客户使用，描述具体影响范围", headerFormat,0,40);
			addCell(sheet, 15,0,"更新时间及预计操作时长", headerFormat,0,20);
			}

	}
	
	private  String[] setBody(WritableSheet sheet, List<ProductionBO> rowList) throws Exception {
		WritableCellFormat bodyFormat = new WritableCellFormat();
		bodyFormat.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormat.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormat.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		WritableCellFormat bodyFormatLeft = new WritableCellFormat();
		bodyFormatLeft.setAlignment(Alignment.LEFT); // 水平居中对齐
		bodyFormatLeft.setVerticalAlignment(VerticalAlignment.CENTRE); // 竖直方向居中对齐
		bodyFormatLeft.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
		ProductionBO msb = rowList.get(0);
		int totalRow = 0;
		BigDecimal totalWorkloadPoint = new BigDecimal(0.0);
		BigDecimal currentWorkloadPoint = new BigDecimal(0.0);
		String [] params = new String[3];
		
		
		sheet.mergeCells(1, 1, 5, 1); 
		addCell(sheet, 1,1, msb.getProNeed(), bodyFormat,0,20);
		
		addCell(sheet, 2,1, msb.getApplicationDept(), bodyFormat,0,20);
		
		addCell(sheet, 2,3, msb.getProApplicant(), bodyFormat,0,20);
		
		addCell(sheet, 2,5, msb.getApplicantTel(), bodyFormat,0,20);
		
		addCell(sheet, 3,1, msb.getProNumber(), bodyFormat,0,20);
		
		addCell(sheet, 3,3, msb.getProChecker(), bodyFormat,0,20);
		
		addCell(sheet, 3,5, msb.getCheckerTel(), bodyFormat,0,20);
		
		addCell(sheet, 4,1, msb.getValidation(), bodyFormat,0,20);
		
		addCell(sheet, 4,3, msb.getIdentifier(), bodyFormat,0,20);
		
		addCell(sheet, 4,5, msb.getIdentifierTel(), bodyFormat,0,20);

		if(msb.getIsAdvanceProduction().equals("否")){
			sheet.mergeCells(1, 5, 5, 6); 
			addCell(sheet, 5,1,msb.getNotAdvanceReason(), bodyFormat,0,20);
			
			sheet.mergeCells(1, 7, 5, 8); 
			addCell(sheet, 7,1,msb.getUrgentReasonPhrase(), bodyFormat,0,20);
			sheet.mergeCells(0, 9, 0, 10); 
			
			sheet.mergeCells(1, 9, 5, 10); 
			addCell(sheet, 9,1,msb.getNotProductionImpact(), bodyFormat,0,20);
			
			sheet.mergeCells(1, 11, 5, 11); 
			addCell(sheet, 11,1,msb.getCompletionUpdate(), bodyFormat,0,20);
			sheet.mergeCells(1, 12, 5, 12);
			addCell(sheet, 12,1,msb.getIsFallback(), bodyFormat,0,20);
			sheet.mergeCells(0, 13, 5, 13);
			
			sheet.mergeCells(1, 14, 5, 14);
			addCell(sheet, 14,1, msb.getEarlyImplementation(), bodyFormat,0,20);
			
			sheet.mergeCells(1, 15,1, 16);
			addCell(sheet, 15,1, msb.getInfluenceUse(), bodyFormat,0,20);
			
			sheet.mergeCells(3, 15,5, 15);
			addCell(sheet, 15,3,  msb.getInfluenceUseReason(), bodyFormat,0,20);
			
			sheet.mergeCells(3, 16,5, 16);
			addCell(sheet, 16,3, msb.getInfluenceUseInf(), bodyFormat,0,20);
			
			sheet.mergeCells(1, 17,5, 17);
			addCell(sheet, 17,1,msb.getOperatingTime(), bodyFormat,0,20);
			
		}else{
		sheet.mergeCells(1, 5, 5, 6); 
		addCell(sheet, 5,1,msb.getUrgentReasonPhrase(), bodyFormat,0,20);
		sheet.mergeCells(0, 7, 0, 8); 
		
		sheet.mergeCells(1, 7, 5, 8); 
		addCell(sheet, 7,1,msb.getNotProductionImpact(), bodyFormat,0,20);
		
		sheet.mergeCells(1, 9, 5, 9); 
		addCell(sheet, 9,1,msb.getCompletionUpdate(), bodyFormat,0,20);

		sheet.mergeCells(1, 10, 5, 10);
		addCell(sheet, 10,1, msb.getIsFallback(), bodyFormat,0,20);
		sheet.mergeCells(0, 11, 5, 11);
		
		sheet.mergeCells(1, 12, 5, 12);
		addCell(sheet, 12,1, msb.getEarlyImplementation(), bodyFormat,0,20);
		
		sheet.mergeCells(1, 13,1, 14);
		addCell(sheet, 13,1, msb.getInfluenceUse(), bodyFormat,0,20);
		
		sheet.mergeCells(3, 13,5, 13);
		addCell(sheet, 13,3,  msb.getInfluenceUseReason(), bodyFormat,0,20);
		
		sheet.mergeCells(3, 14,5, 14);
		addCell(sheet, 14,3, msb.getInfluenceUseInf(), bodyFormat,0,20);
		
		sheet.mergeCells(1, 15,5, 15);
		addCell(sheet, 15,1,msb.getOperatingTime(), bodyFormat,0,20);
		}
		params[0]=String.valueOf(totalRow);
		params[1]=String.valueOf(totalWorkloadPoint);
		params[2]=String.valueOf(currentWorkloadPoint);
		return params;
	}
	
	private  void setTotal(WritableSheet sheet, List<ProductionBO> rowList, String[] params) throws Exception {
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
