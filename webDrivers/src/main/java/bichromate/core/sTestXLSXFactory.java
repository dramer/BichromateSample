package bichromate.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class sTestXLSXFactory {
	
	//private sTestOSInformationFactory path = null;
	
	public sTestXLSXFactory(){
		//path = new sTestOSInformationFactory();
		
	}//sTestXLSXFactory

	/**
	 * This method Demonstrates sTestXLSXFactory().
	 * <br>Get test data from spreadsheet. Data is stored (text only) in worksheets.
	 * <br>
	 * @param xlsxFileToRead - name of the xls file
	 * @param sheetName - worksheet name
	 * @return testData 2 dimensional text array of the test data
	 * @throws IOException - failed to open spreadsheet 
	 */
	public String[][] getTableArray(String xlsxFileToRead, String sheetName) throws IOException{
		
		String[][] testData=null;
		int rowI = 0;
		int colI = 0;
		File myFile = new File(xlsxFileToRead); 
		FileInputStream fis = new FileInputStream(myFile); // Finds the workbook instance for XLSX file 
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); // Return first sheet from the XLSX workbook 
		XSSFSheet mySheet = myWorkBook.getSheet(sheetName); // Get iterator to all the rows in current sheet 
		Iterator<Row> rowIterator = mySheet.iterator(); // Traversing over each row of XLSX file
		
		
		int rows = mySheet.getPhysicalNumberOfRows();
		
		
		testData=new String[rows][0]; // Don't know the columns yet
		
		while (rowIterator.hasNext() && rowI < rows) { 
			Row row = rowIterator.next(); // For each row, iterate through each columns 
			Iterator<Cell> cellIterator = row.cellIterator();
			int colCount = row.getPhysicalNumberOfCells();
			testData[rowI] = new String[colCount];
			colI = 0;
			while (cellIterator.hasNext() && colI < colCount) { 
				Cell cell = cellIterator.next();
				switch (cell.getCellType()) { 
					case Cell.CELL_TYPE_STRING: 
						testData[rowI][colI++] = new String(cell.getStringCellValue()); 
					break; 
					default : 
						testData[rowI][colI++] = new String("This cell is not text"); 
					break;
				} //switch
			}// //switch
			rowI++;
		}//while
		myWorkBook.close();
		return(testData);
	}//getTableArray
	/**
	 * This method Demonstrates readXLSX().
	 * <br>Get test data from spreadsheet. Data is stored (text only) in worksheets.
	 * <br>
	 * @param xlsxFileToRead - name of the xls file
	 * @throws IOException - failed to open spreadsheet 
	 */
	private void readXLSX(String xlsxFileToRead) throws IOException{
		XSSFSheet mySheet = null;
	
		File myFile = new File(xlsxFileToRead); 
		FileInputStream fis = new FileInputStream(myFile); // Finds the workbook instance for XLSX file 
		XSSFWorkbook myWorkBook = new XSSFWorkbook (fis); // Return first sheet from the XLSX workbook 
		int numberOfSheet = myWorkBook.getNumberOfSheets();
		for(int sheetCount = 0; sheetCount < numberOfSheet; sheetCount++ ){
			mySheet = myWorkBook.getSheetAt(sheetCount); // Get iterator to all the rows in current sheet 
			Iterator<Row> rowIterator = mySheet.iterator(); // Traversing over each row of XLSX file 
			System.out.println("Sheet Name " +mySheet.getSheetName()); 
			System.out.println("Sheet Number of Rows " +mySheet.getPhysicalNumberOfRows()); 
			while (rowIterator.hasNext()) { 
				Row row = rowIterator.next(); // For each row, iterate through each columns 
				Iterator<Cell> cellIterator = row.cellIterator(); 
				while (cellIterator.hasNext()) { 
					Cell cell = cellIterator.next(); 
					switch (cell.getCellType()) { 
						case Cell.CELL_TYPE_STRING: 
							System.out.print(" String Cell "+cell.getStringCellValue() + "\t"); 
							break; 
						default : 
							System.out.println("\nUnknow Cell Type? Spreadsheet must be saved as all Text " ); 
							return;
					} 
				} 
				System.out.println(""); 
			}
		}
		myWorkBook.close();
		
	}//readXLSX
	/**
	 * This method Demonstrates writeXLSX().
	 * <br>Get test data from spreadsheet. Data is stored (text only) in worksheets.
	 * <br>
	 * @param xlsxFileToWrite - name of the xls file
	 * @throws IOException - failed to open spreadsheet 
	 */
	@SuppressWarnings("unused")
	private void writeXLSX(String xlsxFileToWrite) throws IOException{
		
		XSSFWorkbook myWorkBook = new XSSFWorkbook (xlsxFileToWrite); // Return first sheet from the XLSX workbook 
		XSSFSheet mySheet = myWorkBook.getSheetAt(0); // Get iterator to all the rows in current sheet 
		
		Map<String, Object[]> data = new HashMap<String, Object[]>(); 
			data.put("7", new Object[] {7d, "Sonya", "75K", "SALES", "Rupert"}); 
			data.put("8", new Object[] {8d, "Kris", "85K", "SALES", "Rupert"}); 
			data.put("9", new Object[] {9d, "Dave", "90K", "SALES", "Rupert"}); 
			
			// Set to Iterate and add rows into XLS file 
			Set<String> newRows = data.keySet(); // get the last row number to append new data 
			int rownum = mySheet.getLastRowNum(); 
			for (String key : newRows) { // Creating a new Row in existing XLSX sheet 
				Row row = mySheet.createRow(rownum++); 
				Object [] objArr = data.get(key); 
				int cellnum = 0; 
				for (Object obj : objArr) { 
					Cell cell = row.createCell(cellnum++); 
					if (obj instanceof String) { 
						cell.setCellValue((String) obj); 
					} else if (obj instanceof Boolean) { 
						cell.setCellValue((Boolean) obj); 
					}else if (obj instanceof Date) { 
						cell.setCellValue((Date) obj); 
					} else if (obj instanceof Double) { 
						cell.setCellValue((Double) obj); 
					} 
				} 
			} // open an OutputStream to save written data into XLSX file 
			FileOutputStream os = new FileOutputStream(xlsxFileToWrite); 
			myWorkBook.write(os); 
			System.out.println("Writing on XLSX file Finished ...");
			myWorkBook.close();
			
	}//writeXLSX
	/**
	 * This method Demonstrates selfTest().
	 * <br>Get test data from spreadsheet. Data is stored (text only) in worksheets.
	 * <br>
	 * @param xlsxFileToRead - name of the xls file
	 * @param sheetName - Name of the sheet to open
	 * @throws IOException - failed to open spreadsheet 
	 */
	private void selfTest(String xlsxFileToRead,String sheetName) throws IOException{
		String[][] tabArray=null;
		
		System.out.println("selfTest...");
		
		tabArray = getTableArray(xlsxFileToRead, sheetName);
		
		for (int row = 0; row < tabArray.length; row++) {
			for (int col = 0; col < tabArray[row].length; col++) { 
				System.out.print(tabArray[row][col] + "\t"); 
			} 
			System.out.println(); 
		}
	}

	
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args)
    	{
			sTestXLSXFactory myXLSXReader = new sTestXLSXFactory();
			sTestOSInformationFactory path = new sTestOSInformationFactory();
			String pathToXLSFile = new String(path.setFilePath(path.workingDirectory()+path.fileSeperator()+"data"+path.fileSeperator()+"sTestReadSpreadsheetFactorySelfTest.xlsx"));
			try {
				myXLSXReader.readXLSX(pathToXLSFile);
				myXLSXReader.selfTest(pathToXLSFile,"Sheet2");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
}
