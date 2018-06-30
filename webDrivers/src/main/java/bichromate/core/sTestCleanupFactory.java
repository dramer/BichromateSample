/*
 *
 * Copyright (c) 2016 by David Ramer, Inc. All Rights Reserved.
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;

/**
 * class sTestCleanupFactory() constructor
 * constructor for the sTestCleanupFactory Class is used to clean-up all files created after tests are run
 */
@SuppressWarnings("unused")
public class sTestCleanupFactory {
	
	private static final String cleanReportsFilesSchedule = null;


	protected static ResourceBundle resources;
	
	
	private String cleanLogFiles = null; 
	private String cleanLogFilesSchedule = null; 
	private String cleanReports = null; 
	private String cleanReportsSchedule = null; 
	private String cleanScreenCapture = null; 
	private String cleanScreenCaptureSchedule = null; 
	private String holidays = null;
	private List<Date> holidayDates = null;
	private LocalDate today = null;
	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	private boolean holidayDatesSetup = false;
	private int executeCleanup = 0;
	private sTestOSInformationFactory osInfo = null;
	
	
	// The returned value (0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday)
	
	
	static {
		try {
			resources = ResourceBundle.getBundle("core.sTestCleanupFactory", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestCleanupFactory.properties not found: "+ mre);
			System.exit(0);
		}
		
	}
	/**
	 * This method sTestCleanupFactory() constructor
	 *constructor for the sTestCleanupFactory Class is used to clean-up all files created after tests are run
	 * @param myResources to initialize all Factories, you can pass in Bichromate.properties
	 */
	public sTestCleanupFactory(ResourceBundle myResources) {
		
		sTestCleanupFactorySetup(myResources);
	}// sTestCleanupFactory
	
	/**
	 * This method sTestCleanupFactory() constructor
	 *constructor for the sTestCleanupFactory Class is used to clean-up all files created after tests are run
	 */
	public sTestCleanupFactory() {
		sTestCleanupFactorySetup(resources);
		
	}// sTestCleanupFactory
	private void sTestCleanupFactorySetup(ResourceBundle myResources){
		//
		// setup STestOSInformation
		//
		osInfo = new sTestOSInformationFactory();
		
		
		cleanLogFiles = new String(myResources.getString("sTestCleanupFactory.cleanLogFiles"));
		cleanLogFilesSchedule = new String(myResources.getString("sTestCleanupFactory.cleanLogFilesSchedule")); 
		cleanReports = new String(myResources.getString("sTestCleanupFactory.cleanReports")); 
		cleanReportsSchedule = new String(myResources.getString("sTestCleanupFactory.cleanReportsSchedule"));
		cleanScreenCapture = new String(myResources.getString("sTestCleanupFactory.cleanScreenCapture")); 
		cleanScreenCaptureSchedule = new String(myResources.getString("sTestCleanupFactory.cleanScreenCaptureSchedule")); 
		holidays = new String(myResources.getString("sTestCleanupFactory.holidays")); 	
	
		//
		// setup Holiday dates
		//
		setupHolidayDates();
		
	}//sTestCleanupFactorySetup
	
	private boolean setupHolidayDates(){
		holidayDates = new ArrayList<Date>();
		
		//today = new Date();
		today = LocalDate.now();
		
		ArrayList <String> aList= new ArrayList<String>(Arrays.asList(holidays.split(",")));
		
		for(int i=0;i<aList.size();i++){
			try {
				
				Date date = new SimpleDateFormat("MM/dd/yyyy").parse(aList.get(i));
				holidayDates.add(date);
			} catch (ParseException e) {
				System.out.println("sTestCleanupFactory.sTestCleanupFactory failed to parse Holiday dates: "+holidays);
				return false;
			}
		}
		holidayDatesSetup = true;
		return holidayDatesSetup;
	}// setupHolidayDates
	/**
	 * This method executeCleanUp() 
	 * Method to cleanup the reports,browserdownload,screencapture folders
	 */
	public void executeCleanUp(){
		boolean doCleanup = false;
		//
		// Clean Log Files
		//
		if(executeCleanup == cleanLogFiles.compareToIgnoreCase("true")){
			System.out.println("cleanLogFiles cleanup starting");
			
			if(executeCleanup == cleanLogFilesSchedule.compareToIgnoreCase("all")){
				System.out.println("cleanLogFiles cleanup for all days");
				doCleanup = true;
			}else
			if(executeCleanup == cleanLogFilesSchedule.compareToIgnoreCase("weekend")&& isWeekend(today)){
				
				System.out.println("cleanLogFiles cleanup on weekends");
				doCleanup = true;
			}else if(executeCleanup == cleanLogFilesSchedule.compareToIgnoreCase("holiday")&& isHoliday(today)){
				
				System.out.println("cleanLogFiles cleanup holiday schedule");
				doCleanup = true;
			}else if(executeCleanup == cleanLogFilesSchedule.compareToIgnoreCase("m-f") && !isWeekend(today)){
				System.out.println("cleanLogFiles cleanup for M-F");
				doCleanup = true;
				
			}else if(parseForIndividualDays(cleanLogFilesSchedule)){
				System.out.println("cleanLogFiles cleanup on: "+today.toString());
				doCleanup = true;
			}
			if(doCleanup){
				cleanLogFiles();
			}
			
		}
		doCleanup = false;
		//
		// Clean Report Files
		//
		if(executeCleanup == cleanReports.compareToIgnoreCase("true")){
			System.out.println("cleanReports cleanup starting");
			if(executeCleanup == cleanReports.compareToIgnoreCase("all")){
				System.out.println("cleanReports cleanup for all days");
				doCleanup = true;
			}else if(executeCleanup == cleanReportsSchedule.compareToIgnoreCase("weekend")&& isWeekend(today)){
				
				System.out.println("cleanReports cleanup on weekends");
				doCleanup = true;
			}else if(executeCleanup == cleanReportsSchedule.compareToIgnoreCase("holiday")&& isHoliday(today)){
				
				System.out.println("cleanReports cleanup holiday schedule");
				doCleanup = true;
			}else if(executeCleanup == cleanReportsSchedule.compareToIgnoreCase("m-f") && !isWeekend(today)){
				System.out.println("cleanReports cleanup for M-F");
				doCleanup = true;
			}else if(parseForIndividualDays(cleanReportsSchedule)){
				System.out.println("cleanReports cleanup on: "+today.toString());
				doCleanup = true;
			}
			if(doCleanup){
				cleanReportFiles();
			}
		}
		doCleanup = false;
		//
		// Clean Screen Capture Files
		//
		if(executeCleanup == cleanScreenCapture.compareToIgnoreCase("true")){
			System.out.println("cleanScreenCapture cleanup starting");
			if(executeCleanup == cleanScreenCapture.compareToIgnoreCase("all")){
				System.out.println("cleanScreenCapture cleanup for all days");
				doCleanup = true;
			}
			if(executeCleanup == cleanScreenCapture.compareToIgnoreCase("weekend")&& isWeekend(today)){
				
				System.out.println("cleanScreenCapture cleanup on weekends");
				doCleanup = true;
			}if(executeCleanup == cleanScreenCaptureSchedule.compareToIgnoreCase("weekend")&& isWeekend(today)){
				
				System.out.println("cleanScreenCapture cleanup on weekends");
				doCleanup = true;
			}else if(executeCleanup == cleanScreenCaptureSchedule.compareToIgnoreCase("holiday")&& isHoliday(today)){
				
				System.out.println("cleanScreenCapture cleanup holiday schedule");
				doCleanup = true;
			}else if(executeCleanup == cleanScreenCaptureSchedule.compareToIgnoreCase("m-f") && !isWeekend(today)){
				System.out.println("cleanScreenCapture cleanup for M-F");
				doCleanup = true;
			}else if(parseForIndividualDays(cleanScreenCaptureSchedule)){
				System.out.println("cleanScreenCapture cleanup on: "+today.toString());
				doCleanup = true;
			}
			if(doCleanup){
				cleanScreenCaptureFiles();
			}
		}
		
		
	}//executeCleanUp
	private void cleanReportFiles(){
		
		String logFileDirectory = new String(osInfo.getReportsDirectory());
		File dir = new File(logFileDirectory);
		 for (File file: dir.listFiles()) {
		        if (!file.isDirectory()&& file.getName().startsWith("reports-20")) {
		        	file.delete();
		        }
		 }
		
	}//cleanLogFiles
	private void cleanLogFiles(){
		
		String logFileDirectory = new String(osInfo.getBrowserDownloadDirectory());
		File dir = new File(logFileDirectory);
		 for (File file: dir.listFiles()) {
	        if (!file.isDirectory()&& file.getName().contains(".log")) {
	        	file.delete();
	        }
	    }
	}//cleanLogFiles
	
	private void cleanScreenCaptureFiles(){
		String screenCaptureDirectory = new String(osInfo.getScreenCaptureDirectory());
		File dir = new File(screenCaptureDirectory);
		 for (File file: dir.listFiles()) {
	        if (file.isDirectory()&& file.getName().startsWith("20")) {
	        	//file.delete();
	        	try {
					FileUtils.cleanDirectory(file);
					file.delete();
				} catch (IOException e) {
				
					e.printStackTrace();
				} 
	        }
	    }
	}//cleanScreenCaptureFiles
	
	/**
	   * This method checks whether the given date object is representing a date at
	   * the weekend (Saturday or Sunday)
	   * 
	   * @param date
	   *          Date to check, cannot be null
	   * @return TRUE is Saturday or Sunday
	   */
	  private boolean isWeekend(LocalDate date) {
	    DayOfWeek dayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
	    switch (dayOfWeek) {
	    case SATURDAY:
	    case SUNDAY:
	      return true;
	    default:
	      return false;
	    }
	  }// isWeekend
	  /**
	   * This method checks whether the given date matches the Holiday dates provided in the properties file
	  
	   * 
	   * @param LocalDate lDate - today
	   *          
	   * @return TRUE matches holiday dates
	   */
	  private boolean isHoliday(LocalDate lDate){
		  Date date = Date.from(lDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		  for(int x = 0; x < holidayDates.size();x++){
				Date hDate = holidayDates.get(x);
				if(hDate.getTime() == date.getTime())
					return true;
			}
		  
		  return false;
	  }//isHoliday
	  private boolean parseForIndividualDays(String daysToProcess){
		  
		  if(null == daysToProcess){
			  System.out.println("sTestCleanupFactory:parseForIndividualDays failed ");
			  return false;
		  }
		  boolean parsedIndividualDays = false;
		  DayOfWeek dayOfWeek = DayOfWeek.of(today.get(ChronoField.DAY_OF_WEEK));
		  ArrayList <String> aList= new ArrayList<String>(Arrays.asList(daysToProcess.split(",")));
		  for(int i=0;i<aList.size();i++){
			
			  switch (dayOfWeek) {
			  	case MONDAY:
			  		if(aList.get(i).equalsIgnoreCase("mon")){
			  			return true;
			  		}
			  		break;
			  	case TUESDAY:
			  		if(aList.get(i).equalsIgnoreCase("tue")){
			  			return true;
			  		}
			  		break;
			  	case WEDNESDAY:
			  		if(aList.get(i).equalsIgnoreCase("wed")){
			  			return true;
			  		}
			  		break;
			  	case THURSDAY:
			  		if(aList.get(i).equalsIgnoreCase("thu")){
			  			return true;
			  		}
			  		break;
			  	case FRIDAY:
			  		if(aList.get(i).equalsIgnoreCase("fri")){
			  			return true;
			  		}
			  		break;
			  	case SATURDAY:
			  		if(aList.get(i).equalsIgnoreCase("sat")){
			  			return true;
			  		}
			  		break;
			    case SUNDAY:
			    	if(aList.get(i).equalsIgnoreCase("sun")){
			  			return true;
			  		}
			    	break;
			    default:
			      break; 
			  }
		  }
		  return parsedIndividualDays;
	  }//parseForIndividualDays
	  
	/**
	 * method selfTest()
	 * base selfTest that calls the appropriate test for the current running OS
	 */
	public void selfTest(){
		if(holidayDatesSetup){
			Date date = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
			System.out.println("Today's Date: "+date);
			System.out.println("Holiday Schedule is as follows: ");
			for(int x = 0; x < holidayDates.size();x++){
				System.out.println("	Date: "+holidayDates.get(x));
			}
			
		}else{
			System.out.println("Holiday dates not setup");
		}
		executeCleanUp();
	}//selfTest
	//
	// Inner class for testing on the command line
	//
	public static class Test {
		public static void main(final String[] args){
			
			sTestCleanupFactory cuf = new sTestCleanupFactory();
			if(null != cuf)
				cuf.selfTest();
			
		}// main
	}// Test
}//sTestCleanupFactory
