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
package sample;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.*;

import com.relevantcodes.extentreports.LogStatus;

import bichromate.core.sTestBaseTestNGDeclaration;
import bichromate.core.sTestOSInformationFactory;
import bichromate.core.sTestWebDriverFactory;


@SuppressWarnings("unused")
public class sampleTest  extends sTestBaseTestNGDeclaration{
	
	private String filePath = "reports\\Extent.html"; 
	private sampleWebDriverFactory sampleWebDriver = null;
	
	public sampleTest(){
		testRunXLS = "sTestSpreadSheetFactorySelfTest.xls";
		testRunWorkSheet = "sTestSpreadSheetFactorySelfTest";
		testRunTableLabel = "sampleTest";
		
		//
		// Create the webDriverFactory that creates all selenium webdrivers
		//
		sampleWebDriver = new sampleWebDriverFactory();
		//
		// This sets the coreWebDriverFactory for Bichromate
		//
		coreWebDriverFactory = sampleWebDriver;
		
		sTestOSInformationFactory path = new sTestOSInformationFactory();
		filePath = new String(path.setFilePath(filePath));
		
		extent = extendManager.getReporter(filePath);
		
		if(null == sampleWebDriver){
			System.out.println("sTestWebDriverFactory not created: ");
			System.exit(0);
		}
		//
		// Remove all the debug info
		//
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog"); 
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");	
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
  @Test(enabled=true, dataProvider = "sTestBaseTestNGDeclarationDataProvider", groups = {"sampleTest"},description = "TestNG test to validate Bichromate")
  public void yahooTest(String testCount, String runTest, String browser , String platform, String osVersion,String version, String remote, 
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
	
	if(sampleWebDriver == null){
		throw new SkipException("No oDeskWebDriver created");
	}
	
	webDriver = sampleWebDriver.createWebDriver(remote, version, platform, osVersion, browser,"yahooTest");
	//
	// If the webDriver is not created
	//
	if(webDriver == null){
		throw new SkipException("No Web Driver");
	}
	//
	// Adding extend Reports
	//
	test = extent.startTest("yahooTest");
   
	
	Reporter.log("loginTest created web driver",true);
	testName = new String("Start the yahooTest on " +platform+" and "+browser+" browser version "+version);
	Reporter.log("Start the yahoo Test on " +platform+" and "+browser+" browser version "+version,true);
	Reporter.log("Open Web Page: " + baseURL,true);
	webDriver.get(baseURL);
	Reporter.log("open "+baseURL+" using "+browser,true);
	//
	// Make sure we see the landing page
	//
	Reporter.log("yahoo is open and waiting for landing page");
	sampleWebDriver.sTestThreadWait(3000);
	Reporter.log("Enter search Text for Red Sox");
	sampleWebDriver.getSamplePageDeclaration().enterSearchTextBoxInfo("Red sox");
	Reporter.log("Click search");
	sampleWebDriver.getSamplePageDeclaration().clickSearchButton();
	sampleWebDriver.sTestThreadWait(3000);
	Reporter.log("Searching....");
	sampleWebDriver.sTestThreadWait(3000);
	Reporter.log("SampleTest passed");
	 //
	 // All Tests Passed
	 //
	//Reporter.log("sauceLab results: "+ webDriver.obtainTestStatusInformations(),true);
	//
	// Update Jira if the test has a Jira ID
	//
	 if(!jiraTestID.equals("none")){
		 sampleWebDriverFactory.getJiraFactory().updateJiraTestComment(jiraTestID,"Title","ReleaseNightTest-Login Passed","David Ramer");
		  jiraTestID = "none";
	 }
	 //
	 //Extend Reports
	 //
	 test.log(LogStatus.PASS, "Passed yahooTest");
	 Assert.assertEquals(test.getRunStatus(), LogStatus.PASS);
  }// @yahooTest
  
 
}// sampleTest
