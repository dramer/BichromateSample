/*
 * sTestBaseTestNGDeclaration.java	1.0 2013/01/24
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

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import bichromate.httpListner.sTestHTTPListnerFactory;
import bichromate.sample.sampleWebDriverFactory;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
/**
 * @author davidwramer
 * @version 1.0
 *
 */
@SuppressWarnings("unused")
public class sTestBaseTestNGDeclaration {

	//
	// Bichrmate HTTP Server/Listener
	//
	
	protected ExtentReports extent;
	protected ExtentTest test;
	private testExecutionLogFactory testLogger = null;
	private String filePath = "reports\\Extent.html"; 
	
	private static ResourceBundle resources; // Access to the properties file for releaseNightTest.
	protected WebDriver webDriver = null; // Selenium web driver to drive the testing
	protected WiniumDriver winiumDriver = null; // Selenium web driver to drive the testing
	protected AndroidDriver <MobileElement> androidDriver = null;
	protected IOSDriver <MobileElement> iosDriver = null;
	//
	// webDriverFactory
	//
	protected sTestWebDriverFactory coreWebDriverFactory = null;
	
	
	//
	// Information about calling Jira
	//
	protected String jiraTestID = "none";
	protected String jiraTestCycleToExecute = "none";
	protected String testName = "none";
	//
	// Server IP to gather log files
	//
	protected String logFileIP = "0.0.0.0";
	//
	// DB Information
	//
	protected String dbServerName = null;
	protected String dbName = null;
	//
	// SSH tunnel Information
	//
	protected String sshServer = null;
	//
	// DataProvider Information
	//
	protected String testRunTableLabel = null;
	protected String testRunXLS = null;
	protected String testRunWorkSheet = null;
	private String[][] data = null;
	private boolean httpServerNotRunning = true;
	
	protected sTestOSInformationFactory path = null;
	
	
	
	
	
	public sTestBaseTestNGDeclaration(){
		
		path = new sTestOSInformationFactory();
		filePath = new String(path.setFilePath(filePath));
		//
		// parametes coming from CI environment
		//
		String spreadsheetProperty = System.getProperty("Bichromate.spreadsheet");
        if (spreadsheetProperty  == null) {
        	testRunXLS = "sTestSpreadSheetFactorySelfTest.xls";
         }else{
            testRunXLS = spreadsheetProperty;
         }
        String workSheetProperty = System.getProperty("Bichromate.workSheet");
        if (workSheetProperty  == null) {
        	testRunWorkSheet = "sTestSpreadSheetFactorySelfTest";
         }else{
        	 testRunWorkSheet = workSheetProperty;
         }
        
        String tableLabelProperty = System.getProperty("Bichromate.tableLabel");
        if (tableLabelProperty  == null) {
        	testRunTableLabel = "sampleTest";
         }else{
        	 testRunTableLabel = tableLabelProperty;
         }
        String logFileIPProperty = System.getProperty("Bichromate.logFileIP");
        if (logFileIPProperty  == null) {
        	logFileIP = "0.0.0.0";
        }else{
        	logFileIP = logFileIPProperty;
        }
        String dbServerNameProperty = System.getProperty("Bichromate.dbServerName");
        if (logFileIPProperty  != null) {
        	dbServerName = new String(dbServerNameProperty);
        }
        String dbNameProperty = System.getProperty("Bichromate.dbName");
        if (logFileIPProperty  != null) {
        	dbName = new String(dbServerNameProperty);
        } 
        String sshServerNameProperty = System.getProperty("Bichromate.sshServer");
        if (sshServerNameProperty  != null) {
        	sshServer = new String(sshServerNameProperty);
        }
       
        
	}//sTestBaseTestNGDeclaration
	
	/************************************************************
	   * <br>BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {test run} AfterMethod, AfterClass, AfterTest, 
	   * <br>(Called last after all tests run) AfterSuite
	   * 
	   * <br>@BeforeClass
	   * 
	   * <br>Not used for this test  
	   */
	  @BeforeClass(alwaysRun = true)
	  public void beforeClass() {
		  
		  Reporter.log("BeforeClass complete...",true);
	  }
	  /**
	   * BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run} AfterMethod, AfterClass, AfterTest, 
	   * (Called last after all tests run) AfterSuite
	   *
	   *  <br>@BeforeMethod
	   * 
	   *  WebDriver.checkRemoteSauceConnection() is called to setup Sauce lab tunnel if needed for the test 
	   */
	  @BeforeMethod(alwaysRun = true)
	  public void beforeMethod() {
		  Reporter.log("Before Method...",true);
		  Reporter.log("sTestWebDriverFactory  created",true);
		  Reporter.log("Before Method...",true);
		  Reporter.log("Test Started at: "+path.getCurrentDateAndUTCTime() ,true);
	  }
	  /**
	   *<br>BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run}  AfterMethod, AfterClass, AfterTest, 
	   *<br>(Called last after all tests run) AfterSuite
	   * 
	   *<br>@AfterMethod
	   *  
	   *<br>Close the web application
	   *<br>Zip all screen shots
	   *<br>driver = null;
	   *@param result -ITestResults
	   */
	  @SuppressWarnings("deprecation")
	@AfterMethod(alwaysRun = true)
	  public void afterMethod(ITestResult result) {
		  
		  
		  String currentDate = new String(path.getCurrentDateAndUTCTime());
		  Reporter.log("Test Completed at: "+currentDate,true);
		  if (result.getStatus() == ITestResult.FAILURE) {
			  //
			  //Extend Reports
			  //
			  if(null != test)
				  test.log(LogStatus.FAIL, result.getThrowable());
			  if(null != coreWebDriverFactory)
				  sTestWebDriverFactory.getSTestScreenCaptureFactory().takeWebScreenShot(result.getInstanceName()+"-Failed-");
		      //
		      // Send error to testNG Reporter
		      //
		      Reporter.log("Following test failed "+result.getInstanceName(),true);
		      Reporter.log("See Screen Capture for more details: "+sTestWebDriverFactory.getSTestScreenCaptureFactory().getfileName(),true);
		      //
		      //
		      if(null != coreWebDriverFactory){
		    	  String pathToImage = new String( sTestWebDriverFactory.getSTestScreenCaptureFactory().getfileName());
		    	  if(null != test)
		    		  test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(pathToImage));
		      }
		      //
		      // Send error to console
		      //
		      System.err.println("Following test failed "+result.getInstanceName());
		      System.err.println("See Screen Capture for more details: "+result.getInstanceName()+"-Failed-"+currentDate+".jpg");
		      //
		      // Send error to HipChat
		      //
		      if(null != coreWebDriverFactory){
			      sTestWebDriverFactory.getSTestHipChatFactory().sendErrorMessage("Following test failed "+result.getInstanceName());
			      sTestWebDriverFactory.getSTestHipChatFactory().sendErrorMessage("See Screen Capture for more details: "+result.getInstanceName()+"-Failed-"+currentDate+".jpg");
			      //
			      // Send error to Logger
			      //
			      sTestWebDriverFactory.getLogFactory().enterLog("Following test failed "+result.getInstanceName());
			      sTestWebDriverFactory.getLogFactory().enterLog("See Screen Capture for more details: "+result.getInstanceName()+"-Failed-"+currentDate+".jpg");
			      //
			      // Log all test results to the Bichromate testExecutionFactory
			      //
			      if(null != testLogger){
			    	  testLogger.enterLog("Following test failed "+result.getInstanceName());
			    	  testLogger.enterLog("See Screen Capture for more details: "+result.getInstanceName()+"-Failed-"+currentDate+".jpg");
			      }
		      }
		  } else if (result.getStatus() == ITestResult.SUCCESS) {
			  //
			  // Extend Reports
			  //
			  if(null != test)
				  test.log(LogStatus.PASS, "Test passed");
			  //
			  // Send pass message to testNG Reporter
			  //
			  Reporter.log("Following test passed "+result.getInstanceName(),true);
			  //
			  // Send pass message to HipChat
			  //
			  if(null != coreWebDriverFactory){
				  sTestWebDriverFactory.getSTestHipChatFactory().sendPassMessage("Following test passed "+result.getInstanceName());
				  //
				  // Send pass message to Bichromate logger
				  //
				  sTestWebDriverFactory.getLogFactory().enterLog("Following test passed "+result.getInstanceName());
				  //
				  // Log all test results to the Bichromate testExecutionFactory
				  //
				  
				  if(null != testLogger)
			    	  testLogger.enterLog("Following test passed "+result.getInstanceName());
				  //
				  // send pass message to console
				  //
			  }
			  System.out.println("Following test PASSED "+result.getInstanceName());
			  
		  }else if (result.getStatus() == ITestResult.SKIP) {
			  //
			  //
			  //
			  if(null != test)
				  test.log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
			  
			  //
			  // Send SKIP message to console
			  //
			  System.out.println("Following test was SKIPPED "+result.getInstanceName());
			  //
			  // Send pass message to Bichromate logger
			  //
			  if(null != coreWebDriverFactory){
				  sTestWebDriverFactory.getLogFactory().enterLog("Following test was SKIPPED "+result.getInstanceName());
				  
				  //
				  // Log all test results to the Bichromate testExecutionFactory
				  //
				  if(null != testLogger)
			    	  testLogger.enterLog("Following test was SKIPPED "+result.getInstanceName());
				  //
				  // Send pass message to testNG Reporter
				  //
				  Reporter.log("Following test was SKIPPED "+result.getInstanceName(),true);
				  //
				  // Send pass message to HipChat
				  //
				  sTestWebDriverFactory.getSTestHipChatFactory().sendPassMessage("Following test was SKIPPED "+result.getInstanceName());
			  }
		  } else if (result.getStatus() == ITestResult.SUCCESS_PERCENTAGE_FAILURE) {
			  //
			  // Send message to console
			  //
			  System.out.println("Following test SUCCESS_PERCENTAGE_FAILURE "+result.getInstanceName());
			  //
			  // send message to HipChat
			  //
			  if(null != coreWebDriverFactory){
				  sTestWebDriverFactory.getSTestHipChatFactory().sendPassMessage("Following test SUCCESS_PERCENTAGE_FAILURE "+result.getInstanceName());
				  //
				  // Send pass message to Bichromate logger
				  //
				  sTestWebDriverFactory.getLogFactory().enterLog("Following test SUCCESS_PERCENTAGE_FAILURE "+result.getInstanceName());
				  //
				  // Log all test results to the Bichromate testExecutionFactory
				  //
				  if(null != testLogger)
			    	  testLogger.enterLog("Following test SUCCESS_PERCENTAGE_FAILURE "+result.getInstanceName());
				  //
				  // Send pass message to testNG Reporter
				  //
			  }
			  Reporter.log("Following test SUCCESS_PERCENTAGE_FAILURE "+result.getInstanceName(),true);
		  }
		  //
		  // Flush Extend Reports
		  //
		  if(null != extent){
			  extent.endTest(test);        
			  extent.flush();
		  }
		  //
		  // Close Web Page
		  //
		  if(null != webDriver) webDriver.quit();
		  webDriver = null;
		  //
		  // Close App driver
		  //
		  if(null != winiumDriver) winiumDriver.quit();
		  winiumDriver = null;
		  
		  //
		  // Close Android App
		  //
		  if(null != androidDriver) androidDriver.quit();
		  androidDriver = null;
		  //
		  // Close IOS APP
		  //
		  if(null != iosDriver)iosDriver.quit();
		  iosDriver = null;
		  //
		  //erase all created page objects
		  //
		  if(null != coreWebDriverFactory)
			  coreWebDriverFactory.cleanWebDriver();
		  //
		  // reset Jira ID
		  //
		  jiraTestID = new String("none");
		 
		  
		  Reporter.log("After Method, closed the browser page",true);
	  }//afterMethod
	  
	  /*********************************************
	   * BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run} AfterMethod, AfterClass, AfterTest, 
	   * <br>(Called last after all tests run) AfterSuite
	   * 
	   * <br> No actions in AfterClass
	   * 
	   */
	  @AfterClass(alwaysRun = true)
	  public void afterClass(){
		  
		  Reporter.log("After Class...",true);
	  }//afterClass
	  /**
	   * BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run} AfterMethod, AfterClass, AfterTest, 
	   * <br>Called last after all tests run) AfterSuite
	   * 
	   *  <br>No actions in @AfterTest
	   * 
	   */
	  @AfterTest(alwaysRun = true) 
	  public void afterTest() {
		  Reporter.log("After Test:....",true);
	  }//afterTest
	  /**
	   *BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run} AfterMethod, AfterClass, AfterTest, 
	   *<br>(Called last after all tests run) AfterSuite
	   *<br>@AfterSuite
	   *<br>Close Saucelabs connection.
	   */
	  @AfterSuite(alwaysRun = true) 
	  public void afterSuite() {
		  Reporter.log("After Suite...",true);
		  extent.close();
		 
		  //
		  // Create Bichromate History report
		  //
		  if(null != testLogger){
	    	  testLogger.buildExecutionHistoryReport();
	    	  testLogger.closeLogFileHandle();
	    	  testLogger = null;
		  }
	  }//afterSuite
	  
	  /**
	   *BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run} AfterMethod, AfterClass, AfterTest, 
	   *<br>(Called last after all tests run) AfterSuite
	   * 
	   *<br>@@BeforeSuite
	   *  
	   *<br>Open Saucelabs connection.
	   *   
	   */
	  @BeforeSuite(alwaysRun = true)
	  public void beforeSuite() {
		  	//
			// Remove all the debug info
			//
		  	System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog"); 
			System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");	
			
			Reporter.log("Before Suite",true);
			
			//
			// Start the HTTPServer
			//
			if(httpServerNotRunning){
				(new Thread(new sTestHTTPListnerFactory(8000))).start();
				httpServerNotRunning = false;
			}
		  
			extent = stestExtendReportFactory.getReporter(filePath);
			//
			// Open the Test Execution Loggger
			//
			 testLogger = new testExecutionLogFactory();
	  }//beforeSuite
	  
	  
	  /** 
	   * This method setupDataProvider() is the testNG way to create Data Driven Tests
	   * 
	   * xlfile for the data
	   * xlsheet from the xlfile
	   * xltable from the xlsheet
	   * @return data The 2 dimensional array consisting of the data from the spreadsheet
	   */
	  @DataProvider (name = "sTestBaseTestNGDeclarationDataProvider")
	  public Object[][] setupDataProvider() {
		  
		  sTestSpreadsheetFactory xlFactory = new sTestSpreadsheetFactory();
		  Reporter.log("Getting test run data from: "+ testRunXLS+" using label: "+ testRunTableLabel ,true);
		  data = xlFactory.getTableArray(testRunXLS, testRunWorkSheet, testRunTableLabel);
		  Reporter.log("Loaded data from spreadsheet complete" ,true);
		  
		 
		  return (data);
	  } // eTestBaseTestNGDeclarationDataProvider
	  
	  
	  /** 
	   * This method setupDataProvider() is the testNG way to create Data Driven Tests.This data provider can be used with
	   * XLS windows 95 spreadsheets.
	   * 
	   * xlfile for the data
	   * xlsheet from the xlfile
	   * xltable from the xlsheet
	   * @return data The 2 dimensional array consisting of the data from the spreadsheet
	   */
	  @DataProvider (name = "sTestBaseTestNGDeclarationXLSDataProvider")
	  public Object[][] setupDataProviderr() {
		  
		  sTestSpreadsheetFactory xlFactory = new sTestSpreadsheetFactory();
		  Reporter.log("Getting test run data from: "+ testRunXLS+" using label: "+ testRunTableLabel ,true);
		  data = xlFactory.getTableArray(testRunXLS, testRunWorkSheet, testRunTableLabel);
		  Reporter.log("Loaded data from spreadsheet complete" ,true);
		  
		 
		  return (data);
	  } // sTestBaseTestNGDeclarationXLSDataProvider
	  
	  /** 
	   * This method setupDataProviderrr() is the testNG way to create Data Driven Tests. This data provider can be used with
	   * XLSX spreadsheets.
	   * 
	   * xlfile for the data
	   * xlsheet from the xlfile
	   * xltable from the xlsheet
	   * @return data The 2 dimensional array consisting of the data from the spreadsheet
	   */
	  @DataProvider (name = "sTestBaseTestNGDeclarationXLSXDataProvider")
	  public Object[][] setupDataProviderrr() {
		  
		  sTestXLSXFactory xlFactory = new sTestXLSXFactory();
		  Reporter.log("Getting test run data from: "+ testRunXLS+" using label: "+ testRunTableLabel ,true);
		  try {
			data = xlFactory.getTableArray(testRunXLS, testRunWorkSheet);
			Reporter.log("Loaded data from spreadsheet complete" ,true);
		} catch (IOException e) {
			Reporter.log("Failed to load data from spreadsheet" ,true);
			e.printStackTrace();
		}
		  return (data);
	  } // sTestBaseTestNGDeclarationXLSDataProvider
	  
	  
}//sTestBaseTestNGDeclaration
