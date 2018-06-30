/*
 * pageObjectModelLogFactory.java	1.0 2016/07/24
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


import bichromate.dataStore.testHistory;


/**
 * @author davidwramer
 * @version 1.0
 *
 */
@SuppressWarnings("all")

public class pageObjectModelLogFactory {
	
	private String hostNameAndIP = null;
	private String fileName = null;
	private String directory = null;//logFiles
	private sTestOSInformationFactory path = null;
	private String logFileName = null;
	private Logger logger = null;
	private FileHandler fh = null;
	private int logRotation = 2;
	private String logRotationString = null;
	private int logRotationSize = 10485760;
	private int defaultLogSize = 10485760;
	private SimpleFormatter formatter = null;
	
	
	private List<testHistory> tests = new ArrayList<testHistory>();
	
	private static ResourceBundle resources;
    
    static
	{
		try
		{
			resources = ResourceBundle.getBundle("common.pageObjectModelLogFactory",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("pageObjectModelLogFactory.properties not found: "+mre);
			System.exit(0);
		}
	}
    public pageObjectModelLogFactory(){
    	setupExecutionFactory(resources);
    }//testExecutionLogFactory

    public pageObjectModelLogFactory(ResourceBundle myResources){
    	setupExecutionFactory(myResources);
    }//testExecutionLogFactory
    
    private void setupExecutionFactory(ResourceBundle myResources){
    	File ssd = null;
    	
    	path = new sTestOSInformationFactory();
    	
    	hostNameAndIP = new String(path.getHostNameAndIP());
    	
    	fileName = new String(myResources.getString("pageObjectModelLogFactory.fileName"));
    	
    	directory = new String(path.setFilePath(myResources.getString("pageObjectModelLogFactory.directory")));
    	
    	logRotationString = new String(path.setFilePath(myResources.getString("pageObjectModelLogFactory.logRotationString")));
    	
    	logRotationSize = Integer.parseInt(logRotationString);
    	
    	if(logRotationSize <=0){
    		System.out.println("pageObjectModelLogFactory: invalid log rotation, setting back to default 5 megs");
    		logRotationSize = defaultLogSize;
    	}
    	//
    	// Make sure the directory exists
    	//
    	String newLogFileDir = new String(path.workingDirectory() + path.fileSeperator() + directory);
    	ssd = new File(newLogFileDir);
		 
		 if (ssd.isDirectory()){
			 System.out.println("valid page ObjectModel log file directory");
			 
			 File[] contents = ssd.listFiles();
			    if (contents != null) {
			    	
			        for (File fa : contents) {
			        	
			        	if(fa.getName().equals(fileName+".log") && (fa.length()> logRotationSize)){
			        		System.out.println("pageObjectModelLogFactory: renaming log file: "+ fa.getName());
			        		fa.renameTo(new File(path.buildWorkingDirectoryPath(directory) + path.fileSeperator()+fileName+".log"));
			        	}
			        }
			    }
			 
			 
		 } else{
			 ssd.mkdir();
			 System.out.println("test execution log file directory created");
		 }
		 //
		 // Always append to the log file
		 //
		logFileName = new String(path.buildWorkingDirectoryPath(directory) + path.fileSeperator()+fileName+".log");
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
    	 } catch (SecurityException e) {
			System.out.println("pageObjectModelLogFactory:Security Exception"+ path.getCurrentDateAndUTCTime()+" Failed to create log file handle ");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("pageObjectModelLogFactory:IOException"+ path.getCurrentDateAndUTCTime()+" Failed to create log file handle ");
			e.printStackTrace();
		}
    	
    }
	
	 /**
     * This method Demonstrates enterSevereLog().
     * This function outputs log file information either to file or stdout
     * @param  info string of what needs to be posted to log file
     */
	public void enterSevereLog(String info){
		 
			
			logger.log(Level.SEVERE, hostNameAndIP+ " "+ info);  
			
		
	}//enterSevereLog
	 /**
     * This method Demonstrates enterInfoLog().
     * This function outputs log file information either to file or stdout
     * @param  info string of what needs to be posted to log file
     */
	public void enterInfoLog(String info){
		
			
			logger.log(Level.INFO,hostNameAndIP+ " "+ info);  
			
		
	}//enterInfoLog
	
	 /**
     * This method Demonstrates enterWarningLog().
     * This function outputs log file information either to file or stdout
     * @param  info string of what needs to be posted to log file
     */
	public void enterWarningLog(String info){
		
			
			logger.log(Level.WARNING, hostNameAndIP+ " "+ info);  
			
		
	}//enterWarningLog 
	/**
    * This method Demonstrates enterConfigLog().
    * This function outputs log file information either to file or stdout
    * @param  info string of what needs to be posted to log file
    */
	public void enterConfigLog(String info){
		
			
			logger.log(Level.CONFIG, hostNameAndIP+ " "+ info);  
			
		
	}//enterWarningLog
	 /**
     * This method Demonstrates returnLogFileInfo().
     * returns a String of the log file
     * @return logFileInfo  String of all the log data
     */
	public String returnLogFileInfo(){
		byte[] encoded;
		String logFileInfo = null;
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
     * @author davidwramer
     * @version 1.0
     */
	public void selfTest(){
		
		 enterInfoLog("output to log file line 2");
		 enterInfoLog("output to log file line 3");
		 enterInfoLog("output to log file line 4");
		 System.out.println("testExecutionLogFactory entries: "+returnLogFileInfo()); 
		 
		 
	
	}//selfTest
	
	//
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			pageObjectModelLogFactory logger = new pageObjectModelLogFactory();
			if(null != logger) logger.selfTest();
		} // end Main
	 } // end Inner class Test

	
	
}//testExecutionLogFactory
