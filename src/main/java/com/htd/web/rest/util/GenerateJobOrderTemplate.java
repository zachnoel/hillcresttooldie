package com.htd.web.rest.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

public class GenerateJobOrderTemplate {

	public static HSSFWorkbook applyTemplate(HSSFWorkbook workbook, HSSFSheet sheet) {
		//total rows to create (starts at 0)
		int totalRows = 26;//27 total rows
		sheet.setColumnWidth(1, 4300);
		sheet.setColumnWidth(8, 3000);

		//create sheet rows
		for(int i = 0; i < totalRows; i++){
			sheet.createRow(i);
		}

		//Fonts
        //Bold Header Font
        HSSFFont boldHeaderFont = workbook.createFont();
        boldHeaderFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldHeaderFont.setFontName("Broadway");
        boldHeaderFont.setItalic(true);
        boldHeaderFont.setFontHeightInPoints((short) 16);

        //Default Font
        HSSFFont defaultFont = workbook.createFont();
        defaultFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);

        //Bold Font
        HSSFFont boldFont = workbook.createFont();
        boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

      //Bold blue Font
        HSSFFont boldBlueFont = workbook.createFont();
        boldBlueFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldBlueFont.setColor(HSSFColor.BLUE.index);



      //Bold blue Tall Font
        HSSFFont boldBlueTallFont = workbook.createFont();
        boldBlueTallFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        boldBlueTallFont.setColor(HSSFColor.BLUE.index);
        boldBlueTallFont.setFontHeightInPoints((short) 14);
     //Styles
        //Bold Header
        HSSFCellStyle boldHeaderStyle = workbook.createCellStyle();
        boldHeaderStyle.setFont(boldHeaderFont);
        boldHeaderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //Center
        HSSFCellStyle centerStyle = workbook.createCellStyle();
        centerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //Bold
        HSSFCellStyle boldStyle = workbook.createCellStyle();
        boldStyle.setFont(boldFont);
        //Grey Background fill
        HSSFCellStyle greyFillStyle = workbook.createCellStyle();
        greyFillStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        greyFillStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        //Centered Bold Blue Style
        HSSFCellStyle centeredBoldBlueStyle = workbook.createCellStyle();
        centeredBoldBlueStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        centeredBoldBlueStyle.setFont(boldBlueFont);
      //Centered Bold Blue Tall Style
        HSSFCellStyle centeredBoldBlueTallStyle = workbook.createCellStyle();
        centeredBoldBlueTallStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        centeredBoldBlueTallStyle.setFont(boldBlueTallFont);


        //Merged Cells
        //Name and Address merged
        sheet.addMergedRegion(new CellRangeAddress(0,0,2,8));
        sheet.addMergedRegion(new CellRangeAddress(1,1,2,8));
        sheet.addMergedRegion(new CellRangeAddress(2,2,2,8));
        sheet.addMergedRegion(new CellRangeAddress(3,3,2,8));
        sheet.addMergedRegion(new CellRangeAddress(4,4,2,8));

        //Customer merged
        sheet.addMergedRegion(new CellRangeAddress(5,5,3,5));

        //Part Merged
        sheet.addMergedRegion(new CellRangeAddress(6,6,3,5));

		//Create template cells
        	//collumn 1
		    sheet.createRow(0).createCell(0).setCellValue("Job No.");
		    sheet.createRow(1).createCell(0).setCellValue("P.O. No.");
		    sheet.createRow(2).createCell(0).setCellValue("Date");

		    HSSFRow row;
		    HSSFCell cell;

		    //Name and Address Info
		    row = sheet.getRow(1);
		    cell = row.createCell(2);
		    cell.setCellValue("Hillcrest Tool & Die");
		    cell.setCellStyle(boldHeaderStyle);

		    row = sheet.getRow(2);
		    cell = row.createCell(2);
		    cell.setCellValue("807 Jones Rd, Paragould AR");
		    cell.setCellStyle(centerStyle);

		    row = sheet.getRow(3);
		    cell = row.createCell(2);
		    cell.setCellValue("(870)573-6881");
		    cell.setCellStyle(centerStyle);

		    //Grey Background Cells
		    row = sheet.getRow(0);
		    cell = row.createCell(2);
		    cell.setCellStyle(greyFillStyle);

		    row = sheet.getRow(4);
		    cell = row.createCell(2);
		    cell.setCellStyle(greyFillStyle);

		    row = sheet.getRow(8);
		    cell = row.createCell(0);
		    cell.setCellStyle(greyFillStyle);

		    row = sheet.getRow(8);
		    cell = row.createCell(1);
		    cell.setCellStyle(greyFillStyle);

		    //Customer centered
		    row = sheet.getRow(5);
		    cell = row.createCell(3);
		    cell.setCellStyle(centeredBoldBlueStyle);

		    //part centered
		    row = sheet.getRow(6);
		    cell = row.createCell(3);
		    cell.setCellStyle(centeredBoldBlueTallStyle);


	        sheet.getRow(5).createCell(2).setCellValue("Customer");
	        sheet.getRow(5).createCell(6).setCellValue("Due Date");
	        sheet.getRow(6).createCell(2).setCellValue("Part No.");
	        sheet.getRow(6).createCell(6).setCellValue("Quantity");
	        sheet.createRow(7).createCell(0).setCellValue("PARTIAL");
	        sheet.getRow(7).createCell(2).setCellValue("Part Description");
	        sheet.getRow(7).createCell(6).setCellValue("Material");
	        sheet.getRow(9).createCell(3).setCellValue("Initials");
	        sheet.getRow(9).createCell(4).setCellValue("Date");
	        sheet.getRow(9).createCell(5).setCellValue("Operation");
	        sheet.getRow(9).createCell(6).setCellValue("Qty");
	        sheet.getRow(9).createCell(7).setCellValue("Total");
	        sheet.getRow(9).createCell(8).setCellValue("Hours");
	        sheet.createRow(10).createCell(0).setCellValue("Machine");
	        sheet.getRow(10).createCell(1).setCellValue("LASER / PLASMA");
	        sheet.getRow(17).createCell(1).setCellValue("BEND / MILL");


	       return workbook;
	}

}
