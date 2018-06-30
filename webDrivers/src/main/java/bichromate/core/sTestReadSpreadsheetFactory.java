/*
 * sTestReadSpreadsheetFactory.java	1.0 2013/01/23
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
 * This code was adapted from:  Lars Vogel
 *    http://www.vogella.com/
 * 
 */
package bichromate.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.swing.table.AbstractTableModel;

import bichromate.core.sTestOSInformationFactory;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;


/**
 * This class Demonstrates ReadXLSheet().
 * This class is used to read in the TestNG test data from a XLS file. 
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("unused")
public class sTestReadSpreadsheetFactory {
	private FileInputStream fs = null;
	private String fileName = null;
	private WorkbookSettings ws = null;
	private Workbook workbook = null;
	private int totalSheet = 0;
	/**
	 * This class Constructor for ReadXLSheet().
	 * The constructor opens the   XLS file for reading.
	 * @param XLSFile  Path to the XLS File
	 */
	public sTestReadSpreadsheetFactory(String XLSFile){
		
		ws = new WorkbookSettings();
		ws.setLocale(new Locale("en", "EN"));
		
		fileName = new String(XLSFile);
		
		try {
			fs = new FileInputStream(new File(fileName));
			workbook = Workbook.getWorkbook(fs, ws);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * This function for getHeadingFromXlsFile().
	 * The Function reads all the column headers in the sheet
	 * @param sheet name of the sheet to open
	 */
	public void getHeadingFromXlsFile(Sheet sheet) {
		int columnCount = sheet.getColumns();
		for (int i = 0; i < columnCount; i++) {
			System.out.println(sheet.getCell(i, 0).getContents());
		}
	}
	/**
	 * This function for getSheetName().
	 * Returns the name of the sheet
	 * @param sheetNumber returns the name of the sheet from the number offset
	 * @return sheetName name of the sheet 
	 */
	public String getSheetName(int sheetNumber){
		return(workbook.getSheet(sheetNumber).getName());	
	}
	/**
	 * This function for totalWorkSheets().
	 * Returns the number of sheets in the XLS file.
	 * @return totalSheets - Total sheets in the file 
	 */
	public int totalWorkSheets(){
		totalSheet = workbook.getNumberOfSheets();
		return (totalSheet);
	}
	/**
	 * This function for getSheetData().
	 * This function reads in all the data for a given worksheet
	 * @param sheet - Sheet number
	 * @param dataModel - Array to fill with data
	 */
	public void getSheetData(int sheet,AbstractTableModel dataModel) {
		
		Sheet s = null;
		Cell rowData[] = null;
		int rowCount = '0';
		// int columnCount = '0';	
 
		//
		// Getting Default Sheet i.e. 0
		//
		s = workbook.getSheet(sheet);
		rowCount = s.getRows();
		//columnCount = s.getColumns();
		int dataModelColCount = dataModel.getColumnCount();
		int dataModelRowCount = dataModel.getRowCount();
 
		//
		// Reading Individual Row Content
		//
		for (int rows = 0; rows < rowCount; rows++) {
			//Get Individual Row
			rowData = s.getRow(rows);
			int rowLength = rowData.length;
			if(rowLength > 0){ //row could be empty
				if(rowCount < (dataModelRowCount-1)){
					for (int cols = 0; cols < rowLength; cols++) {
						if(cols < (dataModelColCount))
							if(rowData[cols]!=null){
								if (rowData[cols].getContents().length() != 0) { 
									String data  = new String(rowData[cols].getContents());
									dataModel.setValueAt(data, rows, cols);
								}//rowData has data
							} // col data not empty
					}// cols
				}// rows count
				
			}//RowData Length
		}
	}//getSheetData
	
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public static void main(final String[] args){
	 		
	 		System.out.println("Java version: "+System.getProperty("java.runtime.version"));
	 		System.out.println("bitness of the JVM (32 or 64): " + System.getProperty("sun.arch.data.model"));
	 		
			System.out.println("Class Path " +System.getProperty("java.class.path"));
			
			// /Users/davidwramer/Documents/workspace/eTest/data/eTestSpreadSheetFactorySelfTest.xls
			// can we open this file and read it line for line.
			sTestOSInformationFactory path = new sTestOSInformationFactory();
			
			if(path != null){
				String pathToXLSFile = new String(path.setFilePath(path.workingDirectory()+path.fileSeperator()+"data"+path.fileSeperator()+"sTestSpreadSheetFactorySelfTest.xls"));
			
				System.out.println("Data Directory: " +pathToXLSFile);
				sTestReadSpreadsheetFactory readXLS = new sTestReadSpreadsheetFactory(pathToXLSFile);
				
				if(readXLS !=null) 
					System.out.println("Total Work Sheets = " +readXLS.totalWorkSheets());
				
				/**************	
				String line;
				
			    InputStream fis;
				try {
					fis = new FileInputStream(pathToXLSFile);
					 InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
					 BufferedReader br = new BufferedReader(isr);
					  while ((line = br.readLine()) != null) {
					    	System.out.println("line of data: " +line);
					    }
					  br.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   *****************/
			}
			
	 		
	 	}// main
	 }// Test
	
}// sTestReadSpreadsheetFactory
