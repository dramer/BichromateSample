/*
 * sTestSpreadsheetFactory.java	1.0 2013/01/23
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
 */
package bichromate.core;


import java.io.File; 
//import java.io.IOException;
import java.lang.reflect.Field;





import jxl.*;   // file:///C:/Program%20Files/eclipse/jexcelapi/tutorial.html
/**
 * This method demonstrates spreadsheetFactory().
 * <br>spreadSheetFactory is used to gain access to data driven tests. The spreadsheet
 * <br> contains workbooks that contain tables that you define.
 * <br>
 * @author davidwramer
 * @version 1.0
 */
public class sTestSpreadsheetFactory {

	
	private static String xmlPath;
	String osName = null;
	String pathSeperator = null;
	sTestOSInformationFactory path = null;
	
	
	public sTestSpreadsheetFactory(){
		
		path = new sTestOSInformationFactory();
		if(path != null){
			xmlPath = new String(path.workingDirectory()+path.fileSeperator()+"data"+path.fileSeperator());
		} else{
			xmlPath = new String("Error Finding spreadsheet");
		}
	}//sTestSpreadsheetFactory
	
	//
	// Get test data from spreadsheet. This uses 2 tags to find  the data.  The first tag marks the 
	// start of the data, the second tag marks the end of the data.  the tag names are the same
	//
	/**
	 * This method Demonstrates spreadsheetFactory().
	 * <br>Get test data from spreadsheet. This uses 2 tags to find  the data.  The first tag marks the 
	 * <br> start of the data, the second tag marks the end of the data.  the tag names are the same
	 * <br>
	 * @param xlFileName - name of the xls file
	 * @param sheetName - worksheet name
	 * @param tableName - table within the work sheet
	 * @return tabArray 2 dimensional array of the test data
	 */
	public String[][] getTableArray(String xlFileName, String sheetName, String tableName){
        String[][] tabArray=null;
        try{
            Workbook workbook = Workbook.getWorkbook(new File(xmlPath+xlFileName));
            System.out.println("\nFound The workbook: "+xlFileName);
            Sheet sheet = workbook.getSheet(sheetName);
            System.out.println("\nFound The sheet: " + sheetName);
            int startRow,startCol, endRow, endCol,ci,cj;
            System.out.println("\nLooking for tableName: "+tableName);
            Cell tableStart=sheet.findCell(tableName);
            System.out.println("\nFound The tableName: "+tableName);
            startRow=tableStart.getRow();
            startCol=tableStart.getColumn();

            Cell tableEnd= sheet.findCell(tableName, startCol+1,startRow+1, 100, 64000,  false);                               

            endRow=tableEnd.getRow();
            endCol=tableEnd.getColumn();
            System.out.println("startRow="+startRow+", endRow="+endRow+", " +
                    "startCol="+startCol+", endCol="+endCol);
            tabArray=new String[endRow-startRow-1][endCol-startCol-1];
            ci=0;

            for (int i=startRow+1;i<endRow;i++,ci++){
                cj=0;
                for (int j=startCol+1;j<endCol;j++,cj++){
                    tabArray[ci][cj]=sheet.getCell(j,i).getContents();
                }
            }
        }
        catch (Exception e)    {
        	System.out.println("Exception: "+ e);
            System.out.println("error in getTableArray()");
        }

        return(tabArray);
    }// getTableArray
	/**
	 * This class Demonstrates spreadsheetFactory().
	 * <br>Get test data from spreadsheet. This uses 2 tags to find  the data.  The first tag marks the 
	 * <br> start of the data, the second tag marks the end of the data.  the tag names are the same
	 * <br>
	 * @param fileToOpen - file to the xls data
	 * @param xlFileName - name of the xls file
	 * @param sheetName - worksheet name
	 * @param tableName - table within the work sheet
	 * @return tabArray 2 dimensional array of the test data
	 */
	public String[][] getTableArray( File fileToOpen, String xlFileName, String sheetName, String tableName){
        String[][] tabArray=null;
        try{
            Workbook workbook = Workbook.getWorkbook(fileToOpen);
            System.out.println("\nFound The workbook: "+xlFileName);
            Sheet sheet = workbook.getSheet(sheetName);
            System.out.println("\nFound The sheet: " + sheetName);
            int startRow,startCol, endRow, endCol,ci,cj;
            System.out.println("\nLooking for tableName: "+tableName);
            Cell tableStart=sheet.findCell(tableName);
            System.out.println("\nFound The tableName: "+tableName);
            startRow=tableStart.getRow();
            startCol=tableStart.getColumn();

            Cell tableEnd= sheet.findCell(tableName, startCol+1,startRow+1, 100, 64000,  false);                               

            endRow=tableEnd.getRow();
            endCol=tableEnd.getColumn();
            System.out.println("startRow="+startRow+", endRow="+endRow+", " +
                    "startCol="+startCol+", endCol="+endCol);
            tabArray=new String[endRow-startRow-1][endCol-startCol-1];
            ci=0;

            for (int i=startRow+1;i<endRow;i++,ci++){
                cj=0;
                for (int j=startCol+1;j<endCol;j++,cj++){
                    tabArray[ci][cj]=sheet.getCell(j,i).getContents();
                }
            }
        }
        catch (Exception e)    {
        	System.out.println("Exception: "+ e);
            System.out.println("error in getTableArray()");
        }

        return(tabArray);
    }
	public void showObjectElements(){
		
		Field[] list = this.getClass().getDeclaredFields();
		
		System.out.println("Class has the following fields:\n");
		
		if(list.length == 0) System.out.println("No items in the list\n");
		
		for(int x = 0; x < list.length;x++){
			System.out.println("name: "+list[x].getName() + "\n");
		}
		
		
	}
	//
	// selfTest
	//
	public void selfTest(){
		
		String xlsPath = new String("sTestSpreadSheetFactorySelfTest.xls");
		String sheetName = new String("sTestSpreadSheetFactorySelfTest");
		String tableName = new String("sampleTest");
	
		String [][] myData = null;
		
		myData = getTableArray(xlsPath,sheetName,tableName);
		//
		// Display all the data
		//
		if(myData != null)
		{
			System.out.println("     PATH: "+ xlsPath+"\n");
			System.out.println("sheetName: "+ sheetName+"\n");
			System.out.println("tableName: "+ tableName+"\n");
			System.out.println("THE DATA ===================>\n");
			 for (int row = 0; row < myData.length; row++) 
			 {
		            for (int col = 0; col < myData[row].length; col++) {
		            	System.out.println( myData[row][col] + "\n");
		            }
		        }
			 System.out.println("spreadSheetFactory.selfTest: Passed");
				
		}else{
			System.err.println("spreadSheetFactory.selfTest: failed");
			System.out.println("spreadsheetFactory assumes:  \\data\\ spreadsheetname.xls");
		}
	}// self Test
	
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args)
    	{
		sTestSpreadsheetFactory xlFactory = new sTestSpreadsheetFactory();
			xlFactory.selfTest();
    	}
	}
	
	
	
}
