/*
 * sTestWriteSpreadsheetFactory.java	1.0 2013/01/23
 *
 * Copyright (c) 2001 by David Ramer, Inc. All Rights Reserved.
 *
 * David Ramer grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to David Ramer.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. David Ramer AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL David Ramer OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF DRamer HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 * 
 * 
 * This code was adapted from:  by Lars Vogel
 * 
 *    http://www.vogella.com/
 * 
 */
package bichromate.core;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;





import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
/**
 * This class Demonstrates writeXLSheet().
 * <br>This class is used to write out XLS files
 * <br>
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("unused")
public class sTestWriteSpreadsheetFactory {
	private WritableCellFormat timesBoldUnderline;
	private WritableWorkbook workbook = null;
	private WritableCellFormat times;
	private String inputFile;
	private int sheetCnt = 0;
	
	/**
	 * method Demonstrates setOutputFile().
	 * This method is used to write out XLS files
	 * @param inputFile  name of the input file
	 * @param makeFileCopy boolean to create copy
	 */
  	public sTestWriteSpreadsheetFactory(String inputFile,boolean makeFileCopy) {
  		
  		Date date = new Date();
  		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String currentDate = new String(dateFormat.format(date));
		
  		//
  		// Make a backup name of the file
  		//
  		// C:\Users\dramer\workspace\oDeskTestNGJavaFramework\data\testSetup.xls
  		if(makeFileCopy){
  			inputFile = new String (inputFile.substring(0,inputFile.lastIndexOf(".")));
  			inputFile = new String(inputFile.concat("-iTestSaved-"+currentDate+".xls"));
  		}
  		this.inputFile = inputFile;
  		
  		File file = new File(inputFile);
  		//
  		// Setup the Cell format
  		//
  		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
 	    // Define the cell format
 	    times = new WritableCellFormat(times10pt);
 	    // Lets automatically wrap the cells
 	    try {
			times.setWrap(false);
		} catch (WriteException e1) {
			System.out.println("ERROR Cell Format not created:"+ e1);
			e1.printStackTrace();
		}
  		
  	    WorkbookSettings wbSettings = new WorkbookSettings();

  	    wbSettings.setLocale(new Locale("en", "EN"));

  	    try {
			workbook = Workbook.createWorkbook(file, wbSettings);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  	}
	/**
	 * method Demonstrates write().
	 * This function writes out the spreadsheet to a file
	 * @throws IOException any io exception when writing out the spreadsheet
	 * @throws WriteException write exception when writing out the spreadsheet
	 */
  	public void writeOutWorkBook() throws IOException, WriteException {
    	//
  		// Write the file and close it
  		//
    	workbook.write();
    	workbook.close();
  }
    /**
  	 * method Demonstrates createWorkBook().
  	 * This function creates a new Writable work sheet
  	 * @param workBookName name of the work book to create
  	 */
  	public void createWorkBook(String workBookName)  {
  		if(workbook !=null){
  			workbook.createSheet(workBookName, sheetCnt++);
  		}else{
  			System.out.println("ERROR: uninitialized workbook");
  		}
  	
  	}//createWorkBook
  	

  	 /**
  	 * method Demonstrates fillWorkSheet().
  	 * This function fills the given worksheet with data
  	 * @param String workBookName
  	 * @param 
  	 * @return None.
  	 * @exception None.
  	 * @throws WriteException
  	 */
/*  	
  	public void fillWorkSheet( int workSheetNum, iTestSpreadsheetPanel workSheet){
  		AbstractTableModel dataModel = null;
  		int rowCnt,colCnt = 0;
  		
  		WritableSheet excelSheet = workbook.getSheet(workSheetNum);
 
  		dataModel = workSheet.getDataModel();
  		rowCnt = dataModel.getRowCount();
  		colCnt = dataModel.getColumnCount();
  		
  		for(int row = 0; row < rowCnt; row++){
  			for(int col = 0; col <colCnt;col++){
  				String data = (String) dataModel.getValueAt(row,col);
  		  		try {
					if(!data.equals("")) addStringCell(excelSheet, col,row,data);
				} catch (RowsExceededException e) {
					System.out.println("ROW Exceeded error:"+ e);
					e.printStackTrace();
				} catch (WriteException e) {
					System.out.println("WriteException error:"+ e);
					e.printStackTrace();
				} 
  			}
  		}
  	}//fillWorkSheet
  	
  	*/
  	
  	
  	/**
	 * method Demonstrates createLabel().
	 * This function writes out the spreadsheet to a file
	 * @param sheet sheet to create lables in
	 * @throws WriteException any write error 
	 */
  	
	private void createLabel(WritableSheet sheet) throws WriteException {
	    // Lets create a times font
	    WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
	    // Define the cell format
	    times = new WritableCellFormat(times10pt);
	    // Lets automatically wrap the cells
	    times.setWrap(true);
	
	    // create create a bold font with underlines
	    WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
	        UnderlineStyle.SINGLE);
	    timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
	    // Lets automatically wrap the cells
	    timesBoldUnderline.setWrap(true);
	
	    CellView cv = new CellView();
	    cv.setFormat(times);
	    cv.setFormat(timesBoldUnderline);
	    cv.setAutosize(true);
	
	    // Write a few headers
	    addCaption(sheet, 0, 0, "Header 1");
	    addCaption(sheet, 1, 0, "This is another header");
  	}//createLabel
  	 
  	/**
  	 * method Demonstrates addCaption().
	 * This function adds a caption to the column
	 * @param sheet name of the sheet to add caption
	 * @param column  column to extract the data
	 * @param row  the row of data
	 * @param labelName name of the column name
	 * @throws WriteException  any write excpetion to the file
	 * @throws RowsExceededException  row out of bounds check
	 */
	 private void addCaption(WritableSheet sheet, int column, int row, String labelName) throws RowsExceededException, WriteException {
	    Label label;
	    label = new Label(column, row, labelName, timesBoldUnderline);
	    sheet.addCell(label);
	  }
	  /**
	   * method Demonstrates addLabel().
	   * This function adds a String to the worksheet
	   * @param sheet  sheet name
	   * @param column  column name
	   * @param row   row of data 
	   * @param s  label name
	   * @throws WriteException any write exception to the file
	   * @throws RowsExceededException if rows are exceeded 
	   */
	 private void addStringCell(WritableSheet sheet, int column, int row, String s) throws WriteException, RowsExceededException {
		 	Label label;
 			label = new Label(column, row, s, times);
 			sheet.addCell(label);
 	}
  	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public static void main(final String[] args){
	 	
	 		
	 		sTestWriteSpreadsheetFactory test = null;
	 		
	 		System.out.println("Java version: "+System.getProperty("java.runtime.version"));
	 		System.out.println("bitness of the JVM (32 or 64): " + System.getProperty("sun.arch.data.model"));
	 		
			System.out.println("Class Path " +System.getProperty("java.class.path"));
			
			
			
			sTestOSInformationFactory path = new sTestOSInformationFactory();
			if(path != null){
				String pathToXLSFile = new String(path.setFilePath(path.workingDirectory()+"/data/eTestSpreadSheetFactorySelfTest.xls"));
			
				System.out.println("Data Directory: " +pathToXLSFile);
			
				test = new sTestWriteSpreadsheetFactory(pathToXLSFile,false);
			
			
			    try {
					test.writeOutWorkBook();
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}// 
	 		
	 	}// main
	 }// Test
} // sTestWriteSpreadsheetFactory