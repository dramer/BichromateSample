/*
 * sTestCronJob.java	1.0 2016/08/23
 *
 * Copyright (c) 20016 by David Ramer, Inc. All Rights Reserved.
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
package bichromate.tools;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import bichromate.core.sTestOSInformationFactory;
import bichromate.dataStore.sTestCronData;
import bichromate.httpListner.twilioFactory;
import bichromate.httpListner.twilioResponseFactory;



/**
 * This class Demonstrates oTestCronJob().
 * <br>This class factory is used to run oTests via a cron process
 * <br>
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("unused")
public class sTestCronJob extends Thread {
	private static ResourceBundle resources;
	private Logger logger = null;
	private FileHandler fh = null;
	private SimpleFormatter formatter = null;
	private String cronXLS;
	private String cronTable;
	private String cronWorkSheet;
	private String sTestCronLoggingFile;
	private String sTestCronLoggingDirectory;
	private String registeredSMSNumber;
	private twilioFactory tFactory = null;
	private twilioResponseFactory tRFactory = null;
	private sTestCronData cronData;
	private String currentTime;
	private String today;
	private String fullDate;
	private String dayOfWeek;
	private sTestOSInformationFactory path = null;
	private ArrayList<sTestCronData> cronDataList = new ArrayList<sTestCronData>();
	private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
	private DateFormat fullDateFormat = new SimpleDateFormat("YYYY-MM-DD-HH-MM");
	private DateFormat day = new SimpleDateFormat("EEEE");
	private Date date = new Date();
	private Calendar cal = Calendar.getInstance();
	private  String[][] data = null;
	private cronJobCallBack cronCallBack = null;
	private JTextPane  cronOutPut = null;
	private boolean running = true;
	
	static
	{
		try
		{
			resources = ResourceBundle.getBundle("tools.sTestCronJob",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestCronJob.properties not found: "+mre);
			System.exit(0);
		}
	}
	
	public sTestCronJob(JTextPane outPutPane,String[][] myData){
	
			cronOutPut = outPutPane;
			data = myData;
			setup();
			
	}//sTestCronJob
	
	public sTestCronJob(){
		cronOutPut = null;
		setup();
		
    } // sTestCronJob
	private void setup(){
		File ssd = null;
		
		path = new sTestOSInformationFactory();
		
		tFactory = new twilioFactory();
		tRFactory = new twilioResponseFactory();
		
		cronCallBack = new cronJobCallBack();
		
		cronTable  = new String(resources.getString("sTestCronJob.cronTable"));
		cronXLS  = new String(resources.getString("sTestCronJob.cronXLS"));
		cronWorkSheet = new String(resources.getString("sTestCronJob.cronWorkSheet"));
		sTestCronLoggingFile = new String(resources.getString("sTestCronJob.sTestCronLoggingFile"));
		sTestCronLoggingDirectory   = new String(resources.getString("sTestCronJob.sTestCronLoggingDirectory"));
		registeredSMSNumber = new String(resources.getString("sTestCronJob.registeredSMSNumber"));
		
		date = new Date();
		currentTime = new String(dateFormat.format(date));
		fullDate = new String(fullDateFormat.format(date));
		today = new String(day.format(date));
		
		//
    	// Make sure the directory exists
    	//
    	String newLogFileDir = new String(path.workingDirectory() + path.fileSeperator() + sTestCronLoggingDirectory);
    	ssd = new File(newLogFileDir);
		 
		 if (ssd.isDirectory()){
			 System.out.println("valid cron log directory");
			 
		 } else{
			 ssd.mkdir();
			 System.out.println("cron log file directory created");
		 }
		 String logFileName = new String(path.buildWorkingDirectoryPath(sTestCronLoggingDirectory) + path.fileSeperator()+sTestCronLoggingFile+".log");
		 logger = Logger.getLogger(logFileName);
    	 try {
    		fh = new FileHandler(logFileName,true); // true param means to append
			logger.addHandler(fh);
	        logger.setUseParentHandlers(false);
	        formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);
	        
	    	loadCronDataList();
	    	appendToPane("MSG: Cron Data loaded from Spreadsheet", Color.GREEN);
    	 } catch (SecurityException e) {
			System.out.println("sTestCronJob:Security Exception"+ getCurrentDate()+" Failed to create log file handle ");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("sTestCronJob:IOException"+ getCurrentDate()+" Failed to create log file handle ");
			e.printStackTrace();
		}  
	}//setup
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
	public healthCheckCallBack getCallBack(){
		return cronCallBack;
	}
	public void setRunning(boolean onOff){
		running = onOff;
	}
	public void stopCron(){
		running = false;
	}
	public void run()
    {

		
        while (running){
        	
        	//
        	// process the data and run tests when time matches
        	//
        	synchronized(this){
        		date = new Date();
        		dayOfWeek = new String(day.format(date));
        		currentTime = new String(dateFormat.format(date));
        		checkForSMS();
            	anyTestsToRun(currentTime,dayOfWeek);
            	if(!dayOfWeek.equals(today)){
            		today = new String(dayOfWeek);
            		resetAllDailyRuns();
            	}
        	}
	        try {
	            Thread.sleep(10000);
	          
	        } catch(InterruptedException e) {
	        	
	        }
        }

    }//run
	private void checkForSMS(){
		System.out.println("Checking for any SMS tests to run");
		
	}
	private void resetAllDailyRuns(){
		logger.info("MSG: Resetting Daily Runs at: "+currentTime);
		for(int x = 0; x < cronDataList.size();x++){
			sTestCronData otcd = (sTestCronData)cronDataList.get(x);
			if(otcd.runToday){
				logger.info("MSG: Resetting daily run for: " +otcd.test);
				appendToPane("MSG: Resetting daily run for: " +otcd.test, Color.GREEN);
			}
			otcd.runToday = false;
		}
		
	}
	private void anyTestsToRun(String currentTime,String currentDay){
		
		System.out.println("Looking for Tests to run: "+ currentDay+": "+currentTime);
		logger.info("MSG: Looking for Tests to run: "+ currentDay+": "+currentTime);
		appendToPane("MSG: Looking for Tests to run: "+ currentDay+": "+currentTime, Color.GREEN);
		cronCallBack.callback_method("Looking for Tests to run: "+ currentDay+": "+currentTime);
		for(int x = 0; x < cronDataList.size();x++){
			sTestCronData otcd = (sTestCronData)cronDataList.get(x);
			//
			// Support for Daily runs
			//
			if(otcd.day.equals("Daily") && !otcd.testRunning && !otcd.runToday){
				if((otcd.timeToRun.equals(currentTime)) && !otcd.testRunning){
					System.out.println("Found matching Time for: "+otcd.test);
					logger.info("MSG: Found matching Time for: "+otcd.test);
					logger.info("MSG: Execute: " +otcd.test);
					logger.info("Start test: "+ otcd.test);
					appendToPane("Start test: "+ otcd.test, Color.GREEN);
					otcd.executeTest();	
				}// match time
			}//daily Run
			//
			// Support for Hourly
			//
			if(otcd.day.equals("Hourly") && !otcd.testRunning){
				//
				// extract the hours out of the string
				//
				String[] s_tokens = currentTime.split(":");
				
				String hour = null;
				//String minutes = null;
				hour = new String (s_tokens[0]);
				//minutes = new String (s_tokens[1]);
				String timeToRunTest = new String(hour+otcd.timeToRun);
				if(timeToRunTest.equals(currentTime)){
					logger.info("MSG: Found matching Time for: "+otcd.test);
					logger.info("MSG: Execute: " +otcd.test);
					logger.info("Start test: "+ otcd.test);
					appendToPane("Start test: "+ otcd.test, Color.GREEN);
					otcd.executeTest();
				}
			}
			//
			// Support for Week day Monday ~ Sunday
			//
			if(currentDay.toLowerCase().equals(otcd.day.toLowerCase()) && !otcd.testRunning){
				if((otcd.timeToRun.equals(currentTime)) && !otcd.testRunning){
					System.out.println("Found matching Time for: "+otcd.test);
					logger.info("MSG: Found matching Time for: "+otcd.test);
					logger.info("MSG: Execute: " +otcd.test);
					logger.info("Start test: "+ otcd.test);
					appendToPane("Start test: "+ otcd.test, Color.GREEN);
					otcd.executeTest();
				}// if time to run test
			}// correct day of week
		}// for loop thru all tests
	}// anyTestsToRun
	
	 private void loadCronDataList(){
		 
		 if(null == data)
			 return;
		 
		  for(int cnt = 0; cnt < data.length;cnt++){
			  sTestCronData cronData = new sTestCronData();
			  cronData.title = new String(data[cnt][0]);
			  cronData.description  = new String(data[cnt][1]);
			  cronData.test  = new String(data[cnt][2]);
			  cronData.day  = new String(data[cnt][3]);
			  cronData.timeToRun  = new String(data[cnt][4]);
			  cronDataList.add(cronData);
			  tFactory.sendSMS("4084803723", cronData.title + "Has been scheduled to run: " + cronData.day+ " at: " + cronData.timeToRun);
			  logger.info("MSG: sTest loaded: "+ cronData.test);
			  appendToPane("MSG: sTest loaded: "+ cronData.test, Color.GREEN);
		  }
		  logger.info("MSG: All Cron Data loaded");
		  appendToPane("MSG: All Cron Data loaded", Color.GREEN);
	 }
	 /**
		 * This function appendToPane().
		 * <br>This function appends text, and text color to the JTextPane
		 * <br> 
		 * <br>
		 * @param msg
		 * @param c
		 * @return None.
		 * @exception None.
		 * @see sTestTestNGAutomationFactory
		 */
		private void appendToPane(String msg, Color c)
	    {
			if(null != cronOutPut){
				StyleContext sc = StyleContext.getDefaultStyleContext();
				AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

				aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
				aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

				int len = cronOutPut.getDocument().getLength();
				cronOutPut.setCaretPosition(len);
				cronOutPut.setCharacterAttributes(aset, false);
				cronOutPut.replaceSelection(msg+"\n");
			}
	    }
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			//Thread myHealthCheck = new sTestHealthCheck();
			
			Thread myCronJob = new sTestCronJob();
			//healthCheckCallBack cb = ((sTestCronJob) myCronJob).getCallBack();
			//((sTestHealthCheck) myHealthCheck).registerCallBack(cb);
			
			myCronJob.start();
			//myHealthCheck.start();
		}
	}
}
