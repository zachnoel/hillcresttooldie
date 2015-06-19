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
import java.io.InputStream;
import java.util.List;

public class JobOrderGenerator {

    private InputStream inputStream = this.getClass().getResourceAsStream("/resources/Shop-Order.xlsx");


    private int sheetNumber = 0;

    public JobOrderGenerator(List<ShopOrder> shopOrder) throws InvalidFormatException, IOException {

        if (inputStream == null) System.out.println("Inputstream is null");

        createJobOrder(shopOrder);

    }

    void createJobOrder(List<ShopOrder> shopOrder) throws InvalidFormatException, IOException {


        for (ShopOrder shopOrder1 : shopOrder) {
            System.out.println("Inside createJobOrder " + shopOrder1.getPo_number());
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
        System.out.println("Inside writeToSpecificCell before try statement " + cellNumber + " " + sheetNumber + " " + value);


        try {
            System.out.println("Inside writeToSpecificCell at the beginning of try statement");
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(sheetNumber);

            XSSFRow row = sheet.getRow(rowNumber);
            XSSFCell cell = row.createCell(cellNumber);

            if (cell == null) {
                cell = row.createCell(cellNumber);
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(value);
            System.out.println("Inside writeToSpecificCell at the end of try statement");
            workbook.close();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (NullPointerException ex) {

            System.out.println("writeToSpecificCell class is returning null ");
            ex.getStackTrace();
        }


    }
}
