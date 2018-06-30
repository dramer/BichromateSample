/*
 * webDriverLogFactory.java	1.0 2016/07/24
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
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JOptionPane;

import org.joda.time.DateTime;

/**
 * @author davidwramer
 * @version 1.0
 *
 */
@SuppressWarnings("unused")
public class webDriverLogFactory {
	
	private String hostNameAndIP = null;
	private Boolean logToFile = false;
	private String fileName = null;//DMSTester.log
	private String directory = null;//logFiles
	private sTestOSInformationFactory path = new sTestOSInformationFactory();
	private String logFileName = null;
	private Logger logger = null;
	private FileHandler fh = null;
	private int logRotationSize = 10485760;
	private int defaultLogSize = 10485760;
	private String logRotationString = null;
	
	private static ResourceBundle resources;
	    
	    static
		{
			try
			{
				resources = ResourceBundle.getBundle("common.webDriverLogFactory",Locale.getDefault());
			} catch (MissingResourceException mre) {
				System.out.println("webDriverLogFactory.properties not found: "+mre);
				System.exit(0);
			}
		}
   
	    /**
		 * Demonstrates logFileFactory().
		 * This class sets up the LogForDMSTester for all logging for DMSTester.
		 */
	    public webDriverLogFactory(){
	    	logFileFactorySetup(resources);
	    }
	    /**
		 * Demonstrates logFileFactory().
		 * This class sets up the LogForDMSTester for all logging for DMSTester.
		 * @param resources  resource bundle for all supporting properties
		 */
	    public webDriverLogFactory(ResourceBundle resources){
	    	logFileFactorySetup(resources);
	    }// logFileFactory
	    private void logFileFactorySetup(ResourceBundle myResources){
	    	File ssd = null;
	    	
	    	path = new sTestOSInformationFactory();
	    	
	    	hostNameAndIP = new String(path.getHostNameAndIP());
	    	
	    	fileName = new String(myResources.getString("webDriverLogFactory.fileName"));
	    	
	    	directory = new String(myResources.getString("webDriverLogFactory.directory"));
	    	
	    	logFileName = new String(path.buildWorkingDirectoryPath(directory) + path.fileSeperator()+fileName+getCurrentDate()+".log");
	    	
	    	logRotationString = new String(myResources.getString("webDriverLogFactory.logRotationString"));
	    	
	    	logRotationSize = Integer.parseInt(logRotationString);
	    	if(logRotationSize <=0){
	    		System.out.println("invalid log rotation, setting back to default 5 megs");
	    		logRotationSize = defaultLogSize;
	    	}
	    	
	    	
	    	//
	    	// Make sure the directory exists
	    	//
	    	String newLogFileDir = new String(path.workingDirectory() + path.fileSeperator() + directory);
	    	ssd = new File(newLogFileDir);
			 
			 if (ssd.isDirectory()){
				 System.out.println("valid webDriverLogFactory directory");
	
					
				 File[] contents = ssd.listFiles();
				    if (contents != null) {
				    	
				        for (File fa : contents) {
				        	
				        	if(fa.getName().equals(fileName+".log") && (fa.length()> logRotationSize)){
				        		System.out.println("renaming log file: "+ fa.getName());
				        		fa.renameTo(new File(path.buildWorkingDirectoryPath(directory) + path.fileSeperator()+fileName+getCurrentDate()+".log"));
				        	}
				        }
				    }
			 } else{
				 ssd.mkdir();
				 System.out.println("webDriverLogFactory directory created");
			 }
			 
			logFileName = new String(path.buildWorkingDirectoryPath(directory) + path.fileSeperator()+fileName+".log");
	    	//
	    	// Create the logger
	    	//
	    	logger = Logger.getLogger(fileName);
	    	 try {
				fh = new FileHandler(logFileName,true);// allow appends to the file
				logger.addHandler(fh);
		        logger.setUseParentHandlers(false);
		        SimpleFormatter formatter = new SimpleFormatter();  
		        fh.setFormatter(formatter);  
	    	 } catch (SecurityException e) {
				System.out.println("webDriverLogFactory:Security Exception"+ getCurrentDate()+" Failed to create log file handle ");
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("webDriverLogFactory:IOException"+ getCurrentDate()+" Failed to create log file handle ");
				e.printStackTrace();
			}
	 }//logFileFactorySetup
	 /**
     * Demonstrates setLogToFile().
     * This function tells the logger to send logs to file
     */
	public void setLogToFile(){
		logToFile = true;
	}
	 /**
	 * This method Demonstrates cancelLogToFile().
     * This function tells the logger to stop sending logs to file and output on stdout
     */
	public void cancelLogToFile(){
		logToFile = false;
	}
	 /**
     * This method Demonstrates enterSevereLog().
     * This function outputs log file information either to file or stdout
     * @param  info string of what needs to be posted to log file
     */
	public void enterSevereLog(String info){
		if(logToFile){	 
			
			logger.log(Level.SEVERE,hostNameAndIP+ " "+ info);  
			
		}else{	
			System.err.println("Log Level, Severe, logFileFactory:"+ getCurrentDate()+" "+info);
		}
	}//enterSevereLog
	 /**
     * This method Demonstrates enterInfoLog().
     * This function outputs log file information either to file or stdout
     * @param  info string of what needs to be posted to log file
     */
	public void enterInfoLog(String info){
		if(logToFile){	 
			
			logger.log(Level.INFO, hostNameAndIP+ " "+ info);  
			
		}else{	
			System.out.println("Log Level, Info, logFileFactory:"+ getCurrentDate()+" "+info);
		}
	}//enterInfoLog
	
	 /**
     * This method Demonstrates enterWarningLog().
     * This function outputs log file information either to file or stdout
     * @param  info string of what needs to be posted to log file
     */
	public void enterWarningLog(String info){
		if(logToFile){	 
			
			logger.log(Level.WARNING, hostNameAndIP+ " "+info);  
			
		}else{	
			System.out.println("Log Level, Warning, logFileFactory:"+ getCurrentDate()+" "+info);
		}
	}//enterWarningLog 
	/**
    * This method Demonstrates enterConfigLog().
    * This function outputs log file information either to file or stdout
    * @param  info string of what needs to be posted to log file
    */
	public void enterConfigLog(String info){
		if(logToFile){	 
			
			logger.log(Level.CONFIG, hostNameAndIP+ " "+info);  
			
		}else{	
			System.out.println("Log Level, Config, logFileFactory:"+ getCurrentDate()+" "+info);
		}
	}//enterWarningLog
	
	 /**
     * This method Demonstrates enterLog().
     * This function outputs log file information either to file or stdout
     * @param  info string of what needs to be posted to log file
     * @deprecated
     */
	public void enterLog(String info){
		if(logToFile){	 
			
			logger.info(hostNameAndIP+ " "+info);  
			
		}else{	
			System.out.println("logFileFactory:"+ getCurrentDate()+" "+info);
		}
	}//enterLog
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
     * This method Demonstrates getCurrentDate().
     * This function returns the current date in the following format yyyy-MM-dd-HH-mm-ss properties file
     * @return String  date  yyyy-MM-dd-HH-mm-ss
     */
	public String getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		String currentDate = new String(dateFormat.format(date));
		return currentDate;
		
	}//getCurrentDate
	/**
     * This method Demonstrates selfTest().
     * This function is used to selfTest LogForDMSTester.  
     */
	public void selfTest(){
		
		// String currentDate = new String(getCurrentDate());
		 //
		 // test stdout
		 //
		enterInfoLog("output to stdoout line 1");
		 //
		 //
		 //
		 setLogToFile();
		 enterInfoLog("output to log file line 1");
		 enterInfoLog("output to log file line 2");
		 enterInfoLog("output to log file line 3");
		 enterInfoLog("output to log file line 4");
		 cancelLogToFile();
		 enterInfoLog("output to stdoout line 2");
		 System.out.println("log entries: "+returnLogFileInfo()); 
	
	}//selfTest
	
	//
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			webDriverLogFactory logger = new webDriverLogFactory();
			if(logger != null){
				// String test1= JOptionPane.showInputDialog("Enter Log File directory");
				logger.selfTest();
				
			}
			
		} // end Main
	 } // end Inner class Test
}// LogForDMSTester
