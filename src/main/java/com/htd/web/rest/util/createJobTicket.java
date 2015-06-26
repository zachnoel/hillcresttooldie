	package com.htd.web.rest.util;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.core.env.Environment;


public class createJobTicket {
	
		
	public static HSSFWorkbook getWorkbook(HSSFWorkbook workbook, Long id, Environment env){
	//Step 1: JDBC props
	final String DB_URL = env.getProperty("spring.datasource.url");
	final String USER = env.getProperty("spring.datasource.username");
	final String PASS = env.getProperty("spring.datasource.password");
	java.sql.Connection conn = null;
	java.sql.Statement stmt = null;
	 
	 
	 try{
	      //STEP 2: Register JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");
	      //STEP 3: Open a connection
	      conn = DriverManager.getConnection(DB_URL,USER,PASS);
	      //STEP 4: Execute a query
	      stmt = conn.createStatement();
	      String sql;
	      sql = "SELECT"
	    		  	+" hillcresttooldie.T_PO.id po_id,"
	    		  	+" hillcresttooldie.T_PO.DUE_DATE due_date,"
	    		  	+" hillcresttooldie.T_CUSTOMER.CUSTOMER_NAME customer_name,"
					+" hillcresttooldie.T_PART.ID part_id,"
					+" hillcresttooldie.T_PO_PART.PART_QUANTITY part_quantity,"
					+" hillcresttooldie.T_PART.PART_NUMBER part_number,"
					+" hillcresttooldie.T_PART.PART_DESCRIPTION part_description,"
					+" hillcresttooldie.T_PART.PLASMA_HRS_PER_PART plasma_hrs,"
					+" hillcresttooldie.T_PART.GRIND_HRS_PER_PART grind_hrs,"
					+" hillcresttooldie.T_PART.MILL_HRS_PER_PART mill_hrs,"
					+" hillcresttooldie.T_PART.BRAKEPRESS_HRS_PER_PART brakepress_hrs,"
					+" hillcresttooldie.T_PART.LASER_HRS_PER_PART laser_hrs,"
					+" hillcresttooldie.T_MATERIAL.MATERIAL_THICKNESS material_thickness"
					+" FROM hillcresttooldie.T_PO"
					+" join hillcresttooldie.T_PO_PART"
					+" on hillcresttooldie.T_PO_PART.PO_ID = hillcresttooldie.T_PO.ID"
					+" join hillcresttooldie.T_PART"
					+" on hillcresttooldie.T_PART.ID = hillcresttooldie.T_PO_PART.PART_ID"
					+" join hillcresttooldie.T_CUSTOMER"
					+" on hillcresttooldie.T_PO.CUSTOMER_ID = hillcresttooldie.T_CUSTOMER.ID"
					+" join hillcresttooldie.T_PART_MATERIAL"
					+" on hillcresttooldie.T_PART.ID = hillcresttooldie.T_PART_MATERIAL.PARTS_ID"
					+" join hillcresttooldie.T_MATERIAL"
					+" on hillcresttooldie.T_PART_MATERIAL.materials_id = hillcresttooldie.T_MATERIAL.id"
					+" where T_PO.id = '"+id+"'";
	      
	     ResultSet rs = stmt.executeQuery(sql);
	     
	     int index = 0;
	     
	     //STEP 5: Extract data from result set
	      while(rs.next()){
	    	 int po_id = rs.getInt("po_id");
	    	 String due_date = rs.getString("due_date");
	    	 String customer_name = rs.getString("customer_name");
	         int part_id  = rs.getInt("part_id");
	         int part_quantity = rs.getInt("part_quantity");
	         String part_number = rs.getString("part_number");
	         String part_description = rs.getString("part_description");
	         BigDecimal plasma_hrs = rs.getBigDecimal("plasma_hrs");
	         BigDecimal grind_hrs = rs.getBigDecimal("grind_hrs");
	         BigDecimal mill_hrs = rs.getBigDecimal("mill_hrs");
	         BigDecimal brakepress_hrs = rs.getBigDecimal("brakepress_hrs");
	         BigDecimal laser_hrs = rs.getBigDecimal("laser_hrs");
	         double material_thickness = rs.getDouble("material_thickness");
	         
	         
	         //Create the Sheet
	         String sheetName = index + "-" + customer_name + "-" + part_number;
	         HSSFSheet sheet = workbook.createSheet(sheetName);
	         //Apply Template to the sheet
	         generateTemplate.applyTemplate(workbook, sheet);
	         
	         
	         sheet.getRow(1).createCell(1).setCellValue(po_id);
	         sheet.getRow(5).getCell(3).setCellValue(customer_name);
	         sheet.getRow(5).createCell(8).setCellValue(due_date);
	         sheet.getRow(6).getCell(3).setCellValue(part_number);
	         sheet.getRow(6).createCell(8).setCellValue(part_quantity);
	         sheet.getRow(7).createCell(4).setCellValue(part_description);
	         sheet.getRow(7).createCell(8).setCellValue(material_thickness);
	         
	         
	         index++;
	      }
	      //STEP 6: Clean-up environment
	      rs.close();
	      stmt.close();
	      conn.close();
	      
	   }catch(SQLException se){
	     se.printStackTrace();
	   }catch(Exception e){
	      e.printStackTrace();
	   }finally{
	      try{
	         if(stmt!=null)
	            stmt.close();
	      }catch(SQLException se2){
	      }
	      try{
	         if(conn!=null)
	            conn.close();
	      }catch(SQLException se){
	         se.printStackTrace();
	      }//end finally try
	      
	      
	   }//end try
	return workbook;
	
	}
}
