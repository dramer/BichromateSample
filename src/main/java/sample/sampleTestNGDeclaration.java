/*
 * billFireSampleTestNGDeclaration.java	1.0 2016/07/14
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

package sample;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
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


import bichromate.core.sTestOSInformationFactory;
import bichromate.core.sTestSpreadsheetFactory;
import bichromate.core.testExecutionLogFactory;

@SuppressWarnings("unused")
public class sampleTestNGDeclaration {

	 protected ExtentReports extent;
	 protected ExtentTest test;
	    
	 protected String filePath = "reports\\Extent.html"; 
	 
	private static ResourceBundle resources; // Access to the properties file for releaseNightTest.
	
	
	protected WebDriver driver = null; // Selenium web driver to drive the testing
	//
	// Specific WebDriverFactory for Client. syscoAccountCenterWebDriverFactory contains all the page object models for the site
	//
	protected sampleWebDriverFactory webDriver = null;
	
	//
	// Information about calling Jira
	//
	protected String jiraTestID = "none";
	protected String jiraTestCycleToExecute = "none";
	protected String testName = "none";
	
	//
	// DataProvider Information
	//
	protected String testRunTableLabel = null;
	protected String testRunXLS = null;
	protected String testRunWorkSheet = null;
	private String[][] data = null;
	private sTestOSInformationFactory path = null;
	private testExecutionLogFactory testLogger = null;
	
	public sampleTestNGDeclaration(){
		
		path = new sTestOSInformationFactory();
		testLogger = new testExecutionLogFactory();
		if(null != path)
			filePath = new String(path.setFilePath(filePath));
		
	}// billFireBaseTestNGDeclaration
	
	
	/************************************************************
	   * <br>BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {test run} AfterMethod, AfterClass, AfterTest, 
	   * <br>(Called last after all tests run) AfterSuite
	   * 
	   * <br>@BeforeClass
	   * 
	   * <br>Not used for this test
	   * @author David Ramer
	   * @version 1.0  
	   */
	  @BeforeClass
	  public void beforeClass() {
		  
		  Reporter.log("BeforeClass complete...",true);
	  }// beforeClass
	  /**
	   * BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run} AfterMethod, AfterClass, AfterTest, 
	   * (Called last after all tests run) AfterSuite
	   *
	   *  <br>@BeforeMethod
	   * 
	   *  WebDriver.checkRemoteSauceConnection() is called to setup Sauce lab tunnel if needed for the test 
	   * @author David Ramer
	   * @version 1.0  
	   */
	  @BeforeMethod 
	  public void beforeMethod() {
		  Reporter.log("Before Method...",true);
		  
		  if(null == webDriver) 
			   webDriver = new sampleWebDriverFactory();
		  
		  Reporter.log("syscoAccountCenterWebDriverFactory  created",true);
		 
		 
	  }//beforeMethod
	  /**
	   *<br>BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run}  AfterMethod, AfterClass, AfterTest, 
	   *<br>(Called last after all tests run) AfterSuite
	   * 
	   *<br>@AfterMethod
	   *  
	   *<br>Close the web application
	   *<br>Zip all screen shots
	   *<br>driver = null;
	   * @author David Ramer
	   * @version 1.0  
	   */
	  @AfterMethod
	  public void afterMethod(ITestResult result) {
		  
		  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		  Date date = new Date();
		  String currentDate = new String(dateFormat.format(date));
		  
		  if (result.getStatus() == ITestResult.FAILURE) {
			  //
			  //Extend Reports
			  //
			  test.log(LogStatus.FAIL, result.getThrowable());
			  //
			  // Screen shot the error page
			  //
			  webDriver.getSTestScreenCaptureFactory().takeScreenShot(result.getInstanceName()+"-Failed-");
			     //
			     // Send error to testNG Reporter
			     //
			     Reporter.log("Following test failed "+result.getInstanceName(),true);
			     Reporter.log("See Screen Capture for more details: "+ webDriver.getSTestScreenCaptureFactory().getLastScreenCaptureFileName(),true);
			     String pathToImage = new String( webDriver.getSTestScreenCaptureFactory().getLastScreenCaptureFileName());
			      test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(pathToImage));
			     //
			     // Send error to console
			     //
			     System.err.println("Following test failed "+result.getInstanceName());
			     System.err.println("See Screen Capture for more details: "+pathToImage);
			     //
			     // Send error to HipChat
			     //
			     webDriver.getSTestHipChatFactory().sendErrorMessage("Following test failed "+result.getInstanceName());
			     webDriver.getSTestHipChatFactory().sendErrorMessage("See Screen Capture for more details: "+result.getInstanceName()+"-Failed-"+currentDate+".jpg");
			     //
			     // Send error to Logger
			     //
			     webDriver.getLogFactory().enterInfoLog("Following test failed "+result.getInstanceName());
			     webDriver.getLogFactory().enterInfoLog("See Screen Capture for more details: "+result.getInstanceName()+"-Failed-"+currentDate+".jpg");
			     //
			     // Log all test results to the Bichromate testExecutionFactory
			     //
			     if(null != testLogger){
			    	 testLogger.enterLog("Following test failed "+result.getInstanceName());
			    	 testLogger.enterLog("See Screen Capture for more details: "+result.getInstanceName()+"-Failed-"+currentDate+".jpg");
			     }
			  } else if (result.getStatus() == ITestResult.SUCCESS) {
				  //
				  //  test.log not logged for passed test. that is done at the end of the test
				  //
				  //
				  // Send pass message to testNG Reporter
				  //
				  Reporter.log("Following test passed "+result.getInstanceName(),true);
				  //
				  // Send pass message to HipChat
				  //
				  webDriver.getSTestHipChatFactory().sendPassMessage("Following test passed "+result.getInstanceName());
				  //
				  // Send pass message to Bichromate logger
				  //
				  webDriver.getLogFactory().enterInfoLog("Following test passed "+result.getInstanceName());
				  //
				  // Log all test results to the Bichromate testExecutionFactory
				  //
				  if(null != testLogger)
				    	 testLogger.enterLog("Following test passed "+result.getInstanceName());
				  //
				  // send pass message to console
				  //
				  System.out.println("Following test PASSED "+result.getInstanceName());
				  
			  }else if (result.getStatus() == ITestResult.SKIP) {
				  //
				  // Extend Report
				  //
				  test.log(LogStatus.SKIP, "Test skipped " + result.getThrowable());
				  //
				  // Send SKIP message to console
				  //
				  System.out.println("Following test was SKIPPED "+result.getInstanceName());
				  //
				  // Send pass message to Bichromate logger
				  //
				  webDriver.getLogFactory().enterInfoLog("Following test was SKIPPED "+result.getInstanceName());
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
				  webDriver.getSTestHipChatFactory().sendPassMessage("Following test was SKIPPED "+result.getInstanceName());
				  
			  } else if (result.getStatus() == ITestResult.SUCCESS_PERCENTAGE_FAILURE) {
				  //
				  // Send message to console
				  //
				  System.out.println("Following test SUCCESS_PERCENTAGE_FAILURE "+result.getInstanceName());
				  //
				  // send message to HipChat
				  //
				  webDriver.getSTestHipChatFactory().sendPassMessage("Following test SUCCESS_PERCENTAGE_FAILURE "+result.getInstanceName());
				  //
				  // Send pass message to Bichromate logger
				  //
				  webDriver.getLogFactory().enterInfoLog("Following test SUCCESS_PERCENTAGE_FAILURE "+result.getInstanceName());
				  //
				  // Log all test results to the Bichromate testExecutionFactory
				  //
				  if(null != testLogger)
				    	 testLogger.enterLog("Following test SUCCESS_PERCENTAGE_FAILURE "+result.getInstanceName());
				  //
				  // Send pass message to testNG Reporter
				  //
				  Reporter.log("Following test SUCCESS_PERCENTAGE_FAILURE "+result.getInstanceName(),true);
			  }
		  //
		  // Flush Extend Reports
		  //
		  extent.endTest(test);        
	      extent.flush();
	      
		  //
		  // Close Web Page
		  //
		  Reporter.log("After  Method...",true);
		  Reporter.log("After Method, closed the browser page",true);
		  if(null != driver) driver.quit();
		  	driver = null;
		  //
		  //reset Jira
		  //
		  jiraTestID = new String("none");
		  //
		  // stop video
		  //
		  webDriver.getSTestVideoCaptureFactory().stopRecording();
		  //
		  // Clean all pageObjects
		  //
		  Reporter.log("After Method, Close open page objects",true);
		  webDriver.cleanWebDriver(); // sets all page objects to null
		 //  webDriver = null;
		  Reporter.log("After Method, closed the browser page",true);
	  }// afterMethod
	  
	  /*********************************************
	   * BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run} AfterMethod, AfterClass, AfterTest, 
	   * <br>(Called last after all tests run) AfterSuite
	   * 
	   * <br> No actions in AfterClass
	   *
	   * @author David Ramer
	   * @version 1.0  
	   */
	  @AfterClass
	  public void afterClass(){
		  
		  Reporter.log("After Class...",true);
	  }
	  /**
	   * BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run} AfterMethod, AfterClass, AfterTest, 
	   * <br>Called last after all tests run) AfterSuite
	   * 
	   *  <br>No actions in @AfterTest
	   *
	   * @author David Ramer
	   * @version 1.0  
	   */
	  @AfterTest 
	  public void afterTest() {
		  Reporter.log("After Test:....",true);
	  }
	  /**
	   *BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run} AfterMethod, AfterClass, AfterTest, 
	   *<br>(Called last after all tests run) AfterSuite
	   *<br>@AfterSuite
	   *<br>Close Saucelabs connection.
	   * @author David Ramer
	   * @version 1.0  
	   */
	  @AfterSuite 
	  public void afterSuite() {
		  Reporter.log("After Suite...",true);
		  extent.close();
		  
		  //
		  // close sauceConnection
		  //
		  if(null != webDriver) webDriver.closeSauceConnection();
		  //
		  // Create History report
		  //
		  if(null != testLogger){
		    	 testLogger.buildExecutionHistoryReport();
		    	 testLogger.closeLogFileHandle();
		    	 testLogger = null;
		  }
		  
	  }
	  
	  /**
	   *BeforeSuite, BeforeTest, BeforeClass, DataProvider, BeforeMethod {Test Run} AfterMethod, AfterClass, AfterTest, 
	   *<br>(Called last after all tests run) AfterSuite
	   * 
	   *<br>@@BeforeSuite
	   *  
	   *<br>Open Saucelabs connection.
	   *  
	   * @author David Ramer
	   * @version 1.0  
	   */
	  @BeforeSuite
	  public void beforeSuite() {
		  //
			// Remove all the debug info
			//
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog"); 
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");	
			
		  Reporter.log("Before Suite",true);
		 
	  }
	  
	  /** 
	   * This method setupDataProvider() is the testNG way to create Data Driven Tests
	   * 
	   * xlfile for the data
	   * xlsheet from the xlfile
	   * xltable from the xlsheet
	   * @return data The 2 dimensional array consisting of the data from the spreadsheet
	   * @author davidwramer
	   * @version 1.0
	   */
	  @DataProvider (name = "billFireBaseTestNGDeclarationXLSDataProvider")
	  public Object[][] setupDataProviderr() {
		  
		  sTestSpreadsheetFactory xlFactory = new sTestSpreadsheetFactory();
		  Reporter.log("Getting test run data from: "+ testRunXLS+" using label: "+ testRunTableLabel ,true);
		  data = xlFactory.getTableArray(testRunXLS, testRunWorkSheet, testRunTableLabel);
		  Reporter.log("Loaded data from spreadsheet complete" ,true);
		  
		 
		  return (data);
	  } // billFireBaseTestNGDeclarationXLSDataProvider
	  
	  

}//billFireBaseTestNGDeclaration
