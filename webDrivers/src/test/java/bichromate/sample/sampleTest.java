/*
 * sampleTest.java	1.0 2016/03/15
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
package bichromate.sample;


import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.time.StopWatch;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.LogStatus;

import bichromate.core.bichromateConstants;
import bichromate.core.sTestBaseTestNGDeclaration;
import bichromate.core.sTestOSInformationFactory;
import bichromate.core.sTestWebDriverFactory;
import bichromate.sample.sampleWebDriverFactory;


@SuppressWarnings("all")
public class sampleTest  extends sTestBaseTestNGDeclaration{
	
	sampleWebDriverFactory localSampleWebDriverFactory = null;
	
	public sampleTest(){
		
		
		//
		// Create the webDriverFactory that creates all selenium webdrivers
		//
		coreWebDriverFactory = new sampleWebDriverFactory();
		
		localSampleWebDriverFactory = (sampleWebDriverFactory) coreWebDriverFactory;
		
		if(null == coreWebDriverFactory){
			System.out.println("coreWebDriverFactory not created: ");
			System.exit(0);
		}
	}
  /**
   * <br>Created by: David Ramer
   * <br>Date Created:  2/1/2013
   * <br>Updates: 
   *<br>
   * <br>Test Case: open yahoo.com and search for some data. This test validated the Bichromate core webdriver creation
   * <br>Priority High
   * <br>Setup: 
   * <br>Status: In-progress
   * <br>Automated: Yes
   * <br>Execution Time: 1 minute
   * <br>Setup: 
   * <br><b>Steps:</b>
   *    <br>Step 1: Launch web browser (FireFox,IE, Chrome)
   *   	<br>Step 2: Open yahoo.com
   *	<br>Step 3: Enter search terms
   *	<br>Step 4: click search or click enter
   *	<br>Step 5: Verify results
   * 	<br>Step 6: Close browser
   * <br><b>Expected Results:</b> 
   * <br> 1) yahoo.com page is opened
   * <br> 2) verify search field
   * <br> 3) click search and view results.
   * 
   * 
   * @param testCount - Jira ID
   * @param runTest - true/false to skip test
   * @param browser - firefox/chrome/IE
   * @param platform - Windows/Linux/Mac
   * @param version - Browser version
   * @param remote - yes/no/proxy
   * @param baseURL - URL to start the test with
   * @param description - description of the test
   * @param xlFile - Name of the XL file for extra data
   * @param xlSheet - work book name for extra data
   * @param xlTable - name of the table in the work book
   * @author David Ramer
   * @version 1.0 
   */
  @Test(enabled=true, dataProvider = "sTestBaseTestNGDeclarationDataProvider", groups = {"sampleTest"},testName="yahooTest", description = "TestNG test to validate Bichromate")
  public void yahooTest(String testCount, String runTest, String browser , String platform, String version, String remote, 
          String baseURL, String description,String xlFile, String xlSheet, String xlTable) {
	//
	// set Jira ID
	//
	jiraTestID = new String("none");
	//
	// Do we want to process this row of data?
	//	
	if(runTest.toLowerCase().contains("no")){
		throw new SkipException("Skip Test");
	}
	
	if(coreWebDriverFactory == null){
		throw new SkipException("No coreWebDriverFactory created");
	}
	
	webDriver = coreWebDriverFactory.createBrowserWebDriver(remote, version, platform, "7",browser,"yahooTest");
	
	coreWebDriverFactory.getSTestScreenCaptureFactory().setCurrentWebDriver(webDriver);
	
	if(webDriver == null){
		throw new SkipException("No Web Driver");
	}
	Reporter.log("yahooTest created web driver",true);
	testName = new String("Start the yahooTest on " +platform+" and "+browser+" browser version "+version);
	//
	// Adding extend Reports
	//
	test = extent.startTest("yahooTest");
	test.assignCategory("Regression");
	
	
	Reporter.log("Start the yahooTest on " +platform+" and "+browser+" browser version "+version,true);
	
	long responseTime = coreWebDriverFactory.startTestTimer(baseURL);
	Reporter.log("open "+baseURL+" using "+browser,true);
	Reporter.log("Load Time for: "+baseURL+" is: " + responseTime + " milliseconds",true);
	//
	// Make sure we see the landing page
	//
	
	String currentURL = new String(webDriver.getCurrentUrl());
	try{
		localSampleWebDriverFactory.getSamplePageDeclaration().verifyFoundPageByURL(10, localSampleWebDriverFactory.getSamplePageDeclaration().pageURL, currentURL);
		Reporter.log("yahoo is open and waiting for landing page");
	}catch(Exception e){
		Reporter.log("failed to find Yahoo");
		Assert.assertTrue(false);
	}
	coreWebDriverFactory.sTestThreadWait(3000);
	//
	// make sure the yhoo search box is present 
	//
	Assert.assertTrue(localSampleWebDriverFactory.getSamplePageDeclaration().getSearchButton().IsDisplayed());
	Assert.assertTrue(localSampleWebDriverFactory.getSamplePageDeclaration().getSearchField().IsDisplayed());
	Reporter.log("Yahoo search button and search text box displayed");
	
	localSampleWebDriverFactory.getSamplePageDeclaration().getSearchField().sTestTextBoxObjectEnterText("News");
	localSampleWebDriverFactory.getSamplePageDeclaration().getSearchButton().clicksTestButtonObject();
	Reporter.log("Searching....");
	coreWebDriverFactory.sTestThreadWait(3000);
	//
	//
	
	//
	// attache screen capture to test
	//
	coreWebDriverFactory.getSTestScreenCaptureFactory().takeWebScreenShot("yahooTest-passed-");
    String pathToImage = new String( coreWebDriverFactory.getSTestScreenCaptureFactory().getfileName());
    test.log(LogStatus.INFO, "Snapshot below: " + test.addScreenCapture(pathToImage));
	
	 //
	 // All Tests Passed
	 //
	 //Reporter.log("sauceLab results: "+ webDriver.obtainTestStatusInformations(),true);
	 //
	 // Update Jira if the test has a Jira ID
	 //
	 if(!jiraTestID.equals("none")){
		 coreWebDriverFactory.getJiraFactory().updateJiraTestComment(jiraTestID,"Title","ReleaseNightTest-Login Passed","David Ramer");
		  jiraTestID = "none";
	 }
	 //
	 // Adding extend Reports
	 //
	 test.log(LogStatus.PASS, "Passed yahooTest");
	 Assert.assertEquals(test.getRunStatus(), LogStatus.PASS);
  }// @yahooTest
  /**
   * <br>Created by: David Ramer
   * <br>Date Created:  6/1/2017
   * <br>Updates: 
   *<br>
   * <br>Test Case: https://www.w3schools.com/html/html_tables.asp parse the sample table on the page
   * <br>Priority High
   * <br>Setup: 
   * <br>Status: In-progress
   * <br>Automated: Yes
   * <br>Execution Time: 1 minute
   * <br>Setup: 
   * <br><b>Steps:</b>
   *    <br>Step 1: Launch web browser (FireFox,IE, Chrome)
   *   	<br>Step 2: Open yahoo.com
   *	<br>Step 3: Enter search terms
   *	<br>Step 4: click search or click enter
   *	<br>Step 5: Verify results
   * 	<br>Step 6: Close browser
   * <br><b>Expected Results:</b> 
   * <br> 1) yahoo.com page is opened
   * <br> 2) verify search field
   * <br> 3) click search and view results.
   * 
   * 
   * @param testCount - Jira ID
   * @param runTest - true/false to skip test
   * @param browser - firefox/chrome/IE
   * @param platform - Windows/Linux/Mac
   * @param version - Browser version
   * @param remote - yes/no/proxy
   * @param baseURL - URL to start the test with
   * @param description - description of the test
   * @param xlFile - Name of the XL file for extra data
   * @param xlSheet - work book name for extra data
   * @param xlTable - name of the table in the work book
   * @author David Ramer
   * @version 1.0 
   */
  @Test(enabled=false, groups = {"sampleTableTest"},testName="sampleTableTest", description = "TestNG test to validate Bichromate's table object")
  public void sampleTableTest() {
	//
	// set Jira ID
	//
	jiraTestID = new String("none");
	
	
	if(coreWebDriverFactory == null){
		throw new SkipException("No coreWebDriverFactory created");
	}
	sTestOSInformationFactory path = new sTestOSInformationFactory();
	bichromateConstants bc = new bichromateConstants();
	
	String remote = new String("local");
	String version = new String("");
	String platform = null;
	String browser = new String(bc.firefox);
	String baseURL = new String("https://www.w3schools.com/html/html_tables.asp");
	String osName = new String(path.getOSName().toLowerCase());
	
	if(null == path){
		throw new SkipException("No OSInformationFactory");
	}
	if(osName.contains(bc.windows)){
		//
		// Use OSInformationfactory to determine OS
		//
		webDriver = coreWebDriverFactory.createBrowserWebDriver("local", "",bc.windows,"",bc.chrome,"sampleTableTest");
		
		platform = new String(bc.mac);
	}else if(osName.contains(bc.mac)){
		//
		// Use OSInformationfactory to determine OS
		//
		webDriver = coreWebDriverFactory.createBrowserWebDriver("local", "", bc.mac,"",bc.chrome,"sampleTableTest");
		platform = new String(bc.windows);
	}else{
		throw new SkipException("No Driver created");
	}
		
	
	
	if(webDriver == null){
		throw new SkipException("No Web Driver");
	}
	Reporter.log("sampleTableTest created web driver",true);
	
	coreWebDriverFactory.getSTestScreenCaptureFactory().setCurrentWebDriver(webDriver);
	
	
	testName = new String("Start the sampleTableTest on " +platform+" and "+browser+" browser version "+version);
	//
	// Adding extend Reports
	//
	test = extent.startTest("sampleTableTest");
	test.assignCategory("Regression");
	
	
	Reporter.log("Start the sampleTableTest on " +platform+" and "+browser+" browser version "+version,true);
	
	long responseTime = coreWebDriverFactory.startTestTimer(baseURL);
	Reporter.log("open "+baseURL+" using "+browser,true);
	Reporter.log("Load Time for: "+baseURL+" is: " + responseTime + " milliseconds",true);
	//
	// Make sure we see the landing page
	//
	//
	// Make sure we see the landing page
	//
	
	String currentURL = new String(webDriver.getCurrentUrl());
	try{
		localSampleWebDriverFactory.getSampleTablePageDeclaration().verifyFoundPageByURL(10, localSampleWebDriverFactory.getSampleTablePageDeclaration().pageURL, currentURL);
		Reporter.log("W3C Table sample page is open and waiting for landing page");
	}catch(Exception e){
		Reporter.log("failed to find currentURL");
		Assert.assertTrue(false);
	}
	coreWebDriverFactory.sTestThreadWait(3000);
	//
	// Can we see the table
	//
	Assert.assertTrue(localSampleWebDriverFactory.getSampleTablePageDeclaration().geTable().IsDisplayed());
	Reporter.log("Number of rows in the table: "+localSampleWebDriverFactory.getSampleTablePageDeclaration().geTable().CountRows());
	//
	// Update Jira if the test has a Jira ID
	//
	if(!jiraTestID.equals("none")){
		coreWebDriverFactory.getJiraFactory().updateJiraTestComment(jiraTestID,"Title","sampleTableTest Passed","David Ramer");
		jiraTestID = "none";
	}
	//
	// Adding extend Reports
	//
	test.log(LogStatus.PASS, "Passed sampleTableTest");
	Assert.assertEquals(test.getRunStatus(), LogStatus.PASS);
	 
  }//SampleTableTest
 
}// sampleTest
