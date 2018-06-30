/*
 * testExecutionLogFactory.java	1.0 2016/07/18
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.text.DecimalFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


import bichromate.dataStore.testHistory;

/**
 * @author davidwramer
 * @version 1.0
 *
 */
// @SuppressWarnings("all")

public class testExecutionLogFactory {
	
	@SuppressWarnings("unused")
	private String hostNameAndIP = null;
	private String fileName = null;
	private String directory = null;//logFiles
	private sTestOSInformationFactory path = null;
	private String logFileName = null;
	private Logger logger = null;
	private FileHandler fh = null;

	private String logRotationString = null;
	private int logRotationSize = 10485760;
	private int defaultLogSize = 10485760;
	
	private SimpleFormatter formatter = null;
	private String startOfCellEntry = "<!-- Enter rows here -->";
	private String reportCellEntryTR = "<tr>"; 
	private String reportCellEntryTHTestName = "<td>%s</td>";
	private String reportCellEntryTHFirstDate = "<td>%s</td>";
	private String reportCellEntryTHLastDate = "<td>%s</td>"; 
	private String reportCellEntryTHTotalPassed = "<td>%s</td>"; 
	private String reportCellEntryTHTotalFailed = "<td>%s</td>"; 
	private String percentPassed = "<td>%s </th>";
	private String percentPassedWarning = "<td><font color=\"yellow\">%s </td> ";
	private String percentPassedFailed = "<td><font color=\"red\">%s </td>"; 
	
	private String endColumn = "<td> </td>";
	private String endTableRow = "</tr>";  
	
	private List<testHistory> tests = new ArrayList<testHistory>();
	
	private static ResourceBundle resources;
    
    static
	{
		try
		{
			resources = ResourceBundle.getBundle("common.testExecutionLogFactory",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("testExecutionLogFactory.properties not found: "+mre);
			System.exit(0);
		}
	}
    public testExecutionLogFactory(){
    	setupExecutionFactory(resources);
    }//testExecutionLogFactory

    public testExecutionLogFactory(ResourceBundle myResources){
    	setupExecutionFactory(myResources);
    }//testExecutionLogFactory
   
    private void setupExecutionFactory(ResourceBundle myResources){
    	
    	
    	path = new sTestOSInformationFactory();
    	
    	hostNameAndIP = new String(path.getHostNameAndIP());
    	
    	percentPassedWarning = new String(myResources.getString("testExecutionLogFactory.percentPassedWarning"));
    	percentPassedFailed = new String(myResources.getString("testExecutionLogFactory.percentPassedFailed"));
    	
    	fileName = new String(myResources.getString("testExecutionLogFactory.fileName"));
    	
    	directory = new String(path.setFilePath(myResources.getString("testExecutionLogFactory.directory")));
    	

    	logRotationString = new String(myResources.getString("testExecutionLogFactory.logRotationString"));
    	
    	logRotationSize = Integer.parseInt(logRotationString);
    	
    	if(logRotationSize <=0){
    		System.out.println("testExecutionLogFactory: invalid log rotation, setting back to default 5 megs");
    		logRotationSize = defaultLogSize;
    	}
		 //
		 // Always append to the log file
		 //
		logFileName = new String(path.buildWorkingDirectoryPath(directory) + path.fileSeperator()+fileName+".log");
    	
    }// setupExecutionFactory
    /**
     * This method Demonstrates closeLogFileHandle().
     * This function closes the log file handle
     */
    public void closeLogFileHandle(){
    	try{
    		if(null != fh)
    			fh.close();
    	}catch (Exception e){
    		System.out.println("testExecutionLogFactory : closeLogFileHandle Error in closing file: "+e);
    	}
    }
  
	 /**
     * This method Demonstrates buildExecutionHistoryReport().
     * This method reads the execution log file and calculates the history of all runs and outputs to the report.html file.
     */
	public synchronized void buildExecutionHistoryReport(){
		PrintWriter writer = null;;
		BufferedReader br = null;
		LocalDate date = null; //date of the entry to be recorded
		String dateString;
		int passed = 0;
		int failed = 0;
		int skipped = 0;
		String version = "unknown";
		String testName = "unKnown";
		//
		// Read all the rows of the log file
		//
		String logIno = returnLogFileInfo();
		//
		// String is lf CR seperated
		//
		String lines[] = logIno.split("\\r?\\n");
		
		for(int x = 0; x <lines.length; x++){
			if(lines[x].contains("INFO")){
				passed = 0;
				failed = 0;
				skipped = 0;
				
				
				if(lines[x].contains("SKIPPED")){
					skipped = 1;
					int testStart = lines[x].indexOf("SKIPPED")+8;
					
				
					testName = new String(lines[x].substring(testStart).trim());
				}
				if(lines[x].contains("passed")){
					passed = 1;
				// capture the test name
				//
					int testStart = lines[x].indexOf("passed")+6;
					testName = new String(lines[x].substring(testStart).trim());
				}
				if(lines[x].contains("failed")){
						failed = 1;
					int testStart = lines[x].indexOf("failed")+6;
					testName = new String(lines[x].substring(testStart).trim());
				}
				
				addTestInfo(passed,failed,skipped,version,testName,date);
				
			}else if(lines[x].contains("bichromate.core.")){// DATE ENTRY
				//
				//Deal with dates  Jul 19, 2016 7:11:07
				//
				
				dateString = new String (lines[x].toString());
				//
				// Trim the date string
				//
				int point = dateString.indexOf("bichromate");
				dateString = new String(dateString.substring(0, point-4));
				dateString.trim();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss", Locale.ENGLISH);
				try{
				date = LocalDate.parse(dateString, formatter);	
				}catch(DateTimeParseException e){
					System.out.println("Failed to parse date: " + dateString);
					System.out.println("Stack Trace: "  + e);
				}
				// System.out.println(date); // Jul 19, 2016 7:11:07
				
			}else if(lines[x].contains("Version")){
				version = new String(lines[x].trim());
			}
			
			
		}//for each log record
		
		DecimalFormat df = new DecimalFormat("##.#");
		
		/*
		for(int x = 0; x < tests.size(); x++){
			testHistory th = tests.get(x);
			System.out.println("Test Name "+th.testName);
			System.out.println("             Tested = "+th.version);
			System.out.println("    First Test Date = "+th.firstTestDate);
			System.out.println("     Last Test Date = "+th.lastTestDate);
			System.out.println(" Total Tests Passed = "+th.totalPassed);
			System.out.println(" Total Tests Failed = "+th.totalFailed);
			System.out.println("Total Tests skipped = "+th.totalSkipped);
			System.out.println("Percent passed = "+df.format(th.percentagePassed)+"%");
		}
		*/
		
		//
		// Now add information to the report.html
		//
	    // Open report.html
		// read from report.html
		// write to report-data.html
		// inject into the table the stats
		//
		String reportTemplate = new String(path.buildWorkingDirectoryPath("reports") + path.fileSeperator()+ path.fileSeperator()+"reports.html");
		String newReport = new String(path.buildWorkingDirectoryPath("reports") + path.fileSeperator()+ path.fileSeperator()+"reports-"+path.getCurrentDateAndUTCTime()+".html");
		

		try {
			writer = new PrintWriter(newReport, "UTF-8");
			br = new BufferedReader(new FileReader(reportTemplate));
			String line;
			while ((line = br.readLine()) != null) {
				//
				// when you get to the table entries, insert new tabel cells
				//
				if(line.equals(startOfCellEntry)){
					for(int x = 0; x < tests.size(); x++){
						String result = null;
						testHistory th = tests.get(x);
						
						writer.println(reportCellEntryTR);
						
						//
						// Test Name
						//
						result = String.format(reportCellEntryTHTestName,th.testName);
						writer.println(result);
						//
						//First Test Date
						//
						result = String.format(reportCellEntryTHFirstDate,th.firstTestDate);
						writer.println(result);
						//
						//Last Test Date
						//
						result = String.format(reportCellEntryTHLastDate,th.lastTestDate);
						writer.println(result);
						//
						//passed
						//
						result = String.format(reportCellEntryTHTotalPassed,th.totalPassed);
						writer.println(result);
						//
						//failed
						//
						result = String.format(reportCellEntryTHTotalFailed,th.totalFailed);
						writer.println(result);
						//
						//% passed
						//
						if(th.percentagePassed < 90.0){
							result = String.format(percentPassedFailed,df.format(th.percentagePassed));
						}else if(th.percentagePassed < 99.9){
							result = String.format(percentPassedWarning,df.format(th.percentagePassed));
						}else{
							result = String.format(percentPassed,df.format(th.percentagePassed));
						}
						writer.println(result);
						
					
						writer.println(endColumn);
						writer.println(endTableRow);
					}
				}
				//
				// add the signature line
				// <!-- Sign and date the page, it's only polite! -->
				if(line.equals("<!-- Sign and date the page, it's only polite! -->")){
					writer.println(line);
					  String result = String.format("<address>Created %s<br>Powered By Bichromate.</address>",path.getCurrentDateAndUTCTime());
					  writer.println(result);
				}
				writer.println(line);
			}
			
		} catch (IOException e) {
			System.out.println("testExecutionLogFactory: buildExecutionHistoryReport: Some error writing reports");
		}finally{
			try {
				if(null != br )
					br.close();
				if(null != writer)
					writer.close();
				if(null != fh)
					fh.close();
			} catch (IOException e) {
				System.out.println("testExecutionLogFactory : buildExecutionHistoryReport failed to close the report writer");
				e.printStackTrace();
			}
			
		}
	}// buildExecutionHistoryReport
	
	
	private void addTestInfo(int passed,int failed,int skipped,String version,String testName,LocalDate date){
		
		for(int x = 0; x < tests.size() ;x++){
			testHistory testInfo = tests.get(x);
			if(testInfo.testName.equals(testName)){
				if(failed > 0) testInfo.totalFailed ++;
				if(skipped >0) testInfo.totalSkipped++;
				if(passed >0) testInfo.totalPassed++;
				if(null == testInfo.version) testInfo.version = new String(version);
				testInfo.testName = new String(testName);
				//
				// Calculate % passed
				//
				if(testInfo.totalPassed > 0){
					testInfo.percentagePassed = (float) (testInfo.totalPassed *100)/(testInfo.totalPassed + testInfo.totalFailed);
				}else{
					testInfo.percentagePassed = 0;
				}
				//
				// set the dates
				//
				if(testInfo.firstTestDate.compareTo(date) > 0){
					testInfo.firstTestDate = date;
				}
				if(testInfo.lastTestDate.compareTo(date) < 0){
					testInfo.lastTestDate = date;
				}
				// tests.remove(x);// remove the old add the new
				// tests.add(testInfo);
				return;
			}
		}
		//
		// add a new item to the list
		//
		testHistory singleTestInfo =  new testHistory();
		singleTestInfo.testName = new String(testName);
		singleTestInfo.totalFailed = failed;
		singleTestInfo.totalPassed = passed;
		singleTestInfo.totalSkipped = skipped;
		singleTestInfo.firstTestDate = date;
		singleTestInfo.lastTestDate = date;
		if(null != version)singleTestInfo.version = new String(version);
		tests.add(singleTestInfo);
		
	}//addTestInfo
	
	 /**
     * This method Demonstrates enterLog().
     * This function outputs log file information either to file or stdout. Because TestNG can runs tests in parallel this must be thread safe
     * @param  info string of what needs to be posted to log file
     */
	public synchronized void enterLog(String info){
		if(openTestExecutionLogFile()){
			logger.info(info);
			closeLogFileHandle();
		}
	}//enterLog
	
	
	
	private boolean openTestExecutionLogFile(){
		boolean openTestLogger = false;
		//
		// Check to see if log files needs to be rotated
		//
		checkLogRotation();
		
		
		if(null== fileName && null == logFileName){
			System.out.println("testExecutionLogFactory:openTestExecutionLogFile fileName and logFileName not setup ");
			return false;
		}
		//
    	// Create the logger
    	//
    	logger = Logger.getLogger(fileName);
    	 try {
    		 fh = new FileHandler(logFileName,true); // true param means to append
			logger.addHandler(fh);
	        logger.setUseParentHandlers(false);
	        formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
	        openTestLogger = true;
    	 } catch (SecurityException e) {
			System.out.println("testExecutionLogFactory:Security Exception"+ path.getCurrentDateAndUTCTime()+" Failed to create log file handle ");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("testExecutionLogFactory:IOException"+ path.getCurrentDateAndUTCTime()+" Failed to create log file handle ");
			e.printStackTrace();
		}
    	 return openTestLogger;
	}
	private void checkLogRotation(){
		File ssd = null;
		//
    	// Make sure the directory exists
    	//
    	String newLogFileDir = new String(path.workingDirectory() + path.fileSeperator() + directory);
    	ssd = new File(newLogFileDir);
		 
		 if (ssd.isDirectory()){
			 System.out.println("valid test execution log file directory");
			 
			 File[] contents = ssd.listFiles();
			    if (contents != null) {
			    	
			        for (File fa : contents) {
			        	
			        	if(fa.getName().equals(fileName+".log") && (fa.length()> logRotationSize)){
			        		System.out.println("testExecutionLogFactory: renaming log file: "+ fa.getName());
			        		fa.renameTo(new File(path.buildWorkingDirectoryPath(directory) + path.fileSeperator()+fileName+".log"));
			        	}
			        }
			    }
		 } else{
			 ssd.mkdir();
			 System.out.println("test execution log file directory created");
		 }
	}
	 /**
     * This method Demonstrates returnLogFileInfo().
     * returns a String of the log file
     * @return logFileInfo  String of all the log data
     */
	public String returnLogFileInfo(){
		byte[] encoded;
		String logFileInfo = new String("No Data Read");
		try {
			encoded = Files.readAllBytes(Paths.get(logFileName));
			logFileInfo = new String(encoded, Charset.defaultCharset());
		} catch (IOException e) {
			System.out.println("Error reading log info");
			e.printStackTrace();
		}
		  return logFileInfo;
	}// returnLogFileInfo
	/**
     * This method Demonstrates selfTest().
     * This function is used to selfTest LogForDMSTester.  
     */
	public void selfTest(){
		
		 enterLog("selfTest: output to log file line 2");
		 enterLog("selfTest: output to log file line 3");
		 enterLog("selfTest: output to log file line 4");
		 System.out.println("testExecutionLogFactory entries: "+returnLogFileInfo()); 
		 //
		 // 
		 //
		 buildExecutionHistoryReport();
		 
	
	}//selfTest
	
	//
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			testExecutionLogFactory logger = new testExecutionLogFactory();
			if(null != logger) logger.selfTest();
		} // end Main
	 } // end Inner class Test
	
}//testExecutionLogFactory
