package com.htd.domain;

import com.htd.domain.ShopOrder;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JobOrderGenerator {

    private File file = new File("/Shop-Order.xlsx");
    private int sheetNumber = 0;

    public JobOrderGenerator(List<ShopOrder> shopOrder) throws InvalidFormatException, IOException {

        for (ShopOrder shopOrder1 : shopOrder) {

            writeToSpecificCell(2, 1, sheetNumber, shopOrder1.getPo_number()); //Po Number
            writeToSpecificCell(7, 3, sheetNumber, shopOrder1.getPo_number()); //Part Number
            LocalDate date = shopOrder1.getPo_due_date();
            String dateToString = date.toString();
            writeToSpecificCell(1, 2, sheetNumber, dateToString); //Due_Date
            writeToSpecificCell(7, 5, sheetNumber, Integer.toString(shopOrder1.getPart_quantity())); //Quantity
            //writeToSpecificCell(1,2,sheetNumber, shopOrder.get); //Material
            writeToSpecificCell(8, 3, sheetNumber, shopOrder1.getPart_decription()); //Part Description
            //writeToSpecificCell(1,2,sheetNumber, shopOrder.getCustomer()); //Customer
            writeToSpecificCell(10, 1, sheetNumber, shopOrder1.getMachine_number()); //Machine

            sheetNumber++;

        }
    }

    void writeToSpecificCell(int rowNumber, int cellNumber, int sheetNumber, String value) throws InvalidFormatException, IOException {

        if(file.exists()){
            System.out.println("Was was found");
        } else {
            System.out.println("File was NOT found");
        }

        try {

            XSSFWorkbook workbook = new XSSFWorkbook(file);

            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            XSSFRow row = sheet.getRow(rowNumber);
            XSSFCell cell = row.createCell(cellNumber);

            if (cell == null) {
                cell = row.createCell(cellNumber);
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(value);

            workbook.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public File getShopOrderFile(){
        return file;
    }

}
