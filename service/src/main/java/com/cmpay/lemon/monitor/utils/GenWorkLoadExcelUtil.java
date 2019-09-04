package com.cmpay.lemon.monitor.utils;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.List;
import java.util.Map;

public class GenWorkLoadExcelUtil {
	public static String  createXLSX(Map<String, Object> map, String tempPath, String srcPath) {
		String message="";
		FileOutputStream out = null;
		InputStream in = null;
		XSSFWorkbook wb = null;
		try {// excel模板路径
			File fi = new File(tempPath);
			in = new FileInputStream(fi);
			// 读取excel模板
			wb = new XSSFWorkbook(in);
			// 读取了模板内所有sheet内容
			XSSFSheet sheet = wb.getSheetAt(0);
			// 表头值设置
			String reqNo = (String) map.get("reqNo");
			String reqName = (String) map.get("reqName");
			sheet.getRow(1).getCell(3).setCellValue(reqNo);
			sheet.getRow(1).getCell(1).setCellValue(reqName);
			String prdLine = (String) map.get("prdLine");
			sheet.getRow(1).getCell(9).setCellValue(prdLine);
			String reqDesc = (String) map.get("reqDesc");
			sheet.getRow(2).getCell(1).setCellValue(reqDesc);
			// 构造数据
			List<Map<String, Object>> listMap = (List<Map<String, Object>>) map.get("dataList");
			sheet.getRow(2).getCell(9).setCellValue(listMap.size());
			map.put("dataList", listMap);

			XSSFCellStyle style1 = setStyle(wb);

			// 修改模板内容导出新模板
			int totalIndex = 6 + listMap.size();

			for (int i = 0; i < listMap.size(); i++) {
				Map<String, Object> rowMap = listMap.get(i);
				// 在相应的单元格进行赋值 第6行开始（下标0）
				int index = i + 7;
				XSSFRow row = sheet.createRow(i + 6);
				row.createCell(0).setCellValue((String) rowMap.get("bussNm"));
				row.createCell(1).setCellValue((String) rowMap.get("workLoadName"));
				row.createCell(2).setCellValue((String) rowMap.get("wordType"));
				row.createCell(3).setCellValue((String) rowMap.get("modifyType"));
				row.createCell(4).setCellValue((String) rowMap.get("comlexType"));
				row.createCell(5).setCellValue((String) rowMap.get("isConn"));
				row.createCell(6).setCellFormula("D" + index + "&E" + index + "&C" + index + "&F" + index + "");
				row.createCell(7).setCellFormula("VLOOKUP(G:G,工作量评估标准!$C:$I,7,FALSE)");
				row.createCell(8).setCellValue(Double.valueOf((String) rowMap.get("cellNum")).intValue());

				row.createCell(9).setCellFormula("VLOOKUP(G:G,工作量评估标准!$C:$I,2,FALSE)");

				if (index == 7) {
					row.createCell(10).setCellFormula("sum(M" + index + ":M" + totalIndex + ")");
				} else {
					row.createCell(10).setCellValue("");
				}
				row.createCell(12).setCellFormula("J" + index + "*I" + index);
				for (Cell cell : row) {
					cell.setCellStyle(style1);
				}
			}

			sheet.setColumnHidden(6, true);
			sheet.setColumnHidden(12, true);
			if(listMap.size()>1){
				sheet.addMergedRegion(new CellRangeAddress(6, 5 + listMap.size(), 10, 10));
			}

			//设置保留公式
//			sheet.setForceFormulaRecalculation(true);
			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);

			out = new FileOutputStream(srcPath);
			wb.write(out);
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
			message=e.getMessage();
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return message;
	}

	private static XSSFCellStyle setStyle(XSSFWorkbook wb) {
		XSSFCellStyle style1 = wb.createCellStyle();
		style1.setBorderTop(BorderStyle.THIN);
		style1.setBorderBottom(BorderStyle.THIN);
		style1.setBorderLeft(BorderStyle.THIN);
		style1.setBorderRight(BorderStyle.THIN);
		style1.setAlignment(HorizontalAlignment.LEFT);
		style1.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直
		XSSFFont font = wb.createFont();
		// 设置字体大小
		font.setFontHeightInPoints((short) 10);
		// 字体
		font.setFontName("宋体");
		style1.setFont(font);
		return style1;
	}

}
