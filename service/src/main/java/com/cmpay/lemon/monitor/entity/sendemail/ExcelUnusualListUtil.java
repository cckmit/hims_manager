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
import java.text.SimpleDateFormat;
import java.util.List;

public class ExcelUnusualListUtil {
	public  String createExcel(String path,List<ProductionBO> list, List<ProductionBO> listTotal) throws Exception {

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
			addCell(sheet, 0, 0, "正常投产(非投产日)申请表", headerFormat,1000,5);
			addCell(sheet, 1,0, "需求名称及内容简述", headerFormat,0,20);
			addCell(sheet, 2,0, "申请部门", headerFormat,0,20);
			addCell(sheet, 2,2, "申请人", headerFormat,0,20);
			addCell(sheet, 2,4, "联系方式", headerFormat,0,20);
			addCell(sheet, 3,0, "投产编号", headerFormat,0,20);
			addCell(sheet, 3,2, "复核人",headerFormat,0,20);
			addCell(sheet, 3,4, "联系方式",headerFormat,0,20);
			addCell(sheet, 4,0, "验证测试类型", headerFormat,0,20);
			addCell(sheet, 4,2, "验证人", headerFormat,0,20);
			addCell(sheet, 4,4, "联系方式", headerFormat,0,20);
			addCell(sheet, 5,0, "计划投产日期", headerFormat,0,20);
			addCell(sheet, 5,2, "产品所属模块", headerFormat,0,20);
			addCell(sheet, 5,4, "基地业务负责人", headerFormat,0,20);
			addCell(sheet, 6,0, "产品经理", headerFormat,0,20);
			addCell(sheet, 6,2, "是否涉及证书", headerFormat,0,20);
			addCell(sheet, 6,4, "开发负责人", headerFormat,0,20);
			addCell(sheet, 7,0, "审批人", headerFormat,0,20);
			addCell(sheet, 7,2, "是否需要运维监控", headerFormat,0,20);
			addCell(sheet, 7,4, "版本更新操作人", headerFormat,0,20);
			addCell(sheet, 8,0, "是否有回退方案", headerFormat,0,40);
			sheet.mergeCells(0, 9, 0, 10);
			addCell(sheet, 9,0, "不走投产日正常投产原因", headerFormat,0,40);
			if(msb.getIsAdvanceProduction().equals("否")){
				sheet.mergeCells(0, 11, 0, 12);
				addCell(sheet, 11,0, "不做预投产验证原因", headerFormat,0,40);
				sheet.mergeCells(0, 13, 0, 14);
				addCell(sheet, 13,0, "备注 (影响范围,其它补充说明)", headerFormat,0,40);
			}else{
				sheet.mergeCells(0, 11, 0, 12);
			addCell(sheet, 11,0, "备注 (影响范围,其它补充说明)", headerFormat,0,40);
			}
			//addCell(sheet, 7,0, "当天不投产的影响", headerFormat,0,20);
			

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
		
		addCell(sheet, 5,3,msb.getProModule(), bodyFormat,0,20);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(msb.getProDate()!=null)
		addCell(sheet, 5,1,sdf.format(msb.getProDate()), bodyFormat,0,20);
		
		addCell(sheet, 5,5,msb.getBusinessPrincipal(), bodyFormat,0,20);
		
		addCell(sheet, 6,1,msb.getProManager(), bodyFormat,0,20);
		
		addCell(sheet, 6,3,msb.getIsRefCerificate(), bodyFormat,0,20);
		
		addCell(sheet, 6,5,msb.getDevelopmentLeader(), bodyFormat,0,20);

		addCell(sheet, 7,1,msb.getApprover(), bodyFormat,0,20);
		
		addCell(sheet, 7,3,msb.getProOperation(), bodyFormat,0,20);
		addCell(sheet, 7,5,msb.getUpdateOperator(), bodyFormat,0,20);
		addCell(sheet, 8,1,msb.getIsFallback(), bodyFormat,0,40);
		sheet.mergeCells(1, 9, 5, 10);
		addCell(sheet, 9,1,msb.getUnusualReasonPhrase(), bodyFormat,0,20);

		if(msb.getIsAdvanceProduction().equals("否")){
			sheet.mergeCells(1, 11, 5, 12);
			addCell(sheet, 11,1,msb.getNotAdvanceReason(), bodyFormat,0,20);
			sheet.mergeCells(1, 13, 5, 14);
			addCell(sheet, 13,1,msb.getRemark(), bodyFormat,0,20);
		}else{
			sheet.mergeCells(1, 11, 5, 12);
			addCell(sheet, 11,1,msb.getRemark(), bodyFormat,0,20);
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
