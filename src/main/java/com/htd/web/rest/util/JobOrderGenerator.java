package com.htd.web.rest.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.htd.domain.ShopOrder;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.joda.time.LocalDate;

import com.htd.web.rest.util.GenerateJobOrderTemplate;

@Stateless
public class JobOrderGenerator {

    private OutputStream out;

    private int sheetNumber = 0;
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private String customerName;

    @Inject
    HttpServletResponse response;
    @Inject
    GenerateJobOrderTemplate generateTemplate;

    public JobOrderGenerator(List<ShopOrder> shopOrder, HttpServletResponse response)
        throws InvalidFormatException, IOException {

        this.response = response;
        createWorkBook();
        createJobOrder(shopOrder);
        createFile();
    }

    private void createWorkBook() {
        workbook = new HSSFWorkbook();
    }

    private void createSheet(){

        String sheetName = sheetNumber + 1 + "-" + customerName;
        sheet = workbook.createSheet(sheetName);
        generatShopOrderTemplate();
    }

    private void generatShopOrderTemplate(){
        generateTemplate = new GenerateJobOrderTemplate();
        generateTemplate.applyTemplate(workbook, sheet);
    }

    private void createJobOrder(List<ShopOrder> order) throws InvalidFormatException,
        IOException {

        //get current date time with Date()
        Date todaysDate = new Date();

        for(ShopOrder shopOrder: order){

            customerName = shopOrder.getCustomer_name(); //set customerName to name the sheet
            createSheet();

            writeToSpecificCell(1, 1, sheetNumber, shopOrder.getPo_number()); // Po Number
            writeToSpecificCell(6, 3, sheetNumber, shopOrder.getPart_number()); // Part Number

            LocalDate date = shopOrder.getPo_due_date();
            String dateToString = date.toString();
            writeToSpecificCell(5, 7, sheetNumber, dateToString); // Due_Date

            writeToSpecificCell(2, 1, sheetNumber, todaysDate.toString());//todays date
            writeToSpecificCell(6, 7, sheetNumber,
                Integer.toString(shopOrder.getPart_quantity())); // Part Quantity
            //writeToSpecificCell(1,2,sheetNumber, shopOrder.getMaterial); //Material
            writeToSpecificCell(7, 3, sheetNumber, shopOrder.getPart_decription()); // Part Description
            writeToSpecificCell(5,3,sheetNumber, shopOrder.getCustomer_name()); //Customer
            writeToSpecificCell(10, 1, sheetNumber, shopOrder.getMachine_number()); // Machine
            writeToSpecificCell(7, 7, sheetNumber, shopOrder.getMaterial_size()); // Material Size

            sheetNumber++;
        }

    }

    public void writeToSpecificCell(int rowNumber, int cellNumber, int sheetNumber,
                                    String value) throws InvalidFormatException {
        System.out.println("The value being passed is: "+value);
        try {

            sheet = workbook.getSheetAt(sheetNumber);

            HSSFRow row = sheet.getRow(rowNumber);
            HSSFCell cell = row.createCell(cellNumber);

            if (cell == null) {
                cell = row.createCell(cellNumber);
            }
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(value);

        } catch (NullPointerException ex) {

            System.out.println("writeToSpecificCell class is returning null ");
            ex.getStackTrace();
        }

    }

    private void createFile(){

        // Set Excel File Name
        String fileName = "JobTicket.xls";
        // Set HTT
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename="
            + fileName);

        try {
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

}
