package bichromate.sample;

import java.io.IOException;

import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import bichromate.core.sTestBaseTestNGDeclaration;
import bichromate.sample.pageDeclaration.sTestCalculatorStandardView;
import bichromate.winium.sTestCalculatorApp;

public class sampleCalculatorApplicationTest extends sTestBaseTestNGDeclaration{
	
	private sTestCalculatorApp calcApp = null;
	
	
	public sampleCalculatorApplicationTest(){
		
		
		//
		// Create the webDriverFactory that creates all selenium webdrivers
		//
		coreWebDriverFactory = new sampleWebDriverFactory();
		
		if(null == coreWebDriverFactory){
			System.out.println("sampleWebDriverFactory not created: ");
			System.exit(0);
		}
		
		//
		// Set the Data for the test
		//
		testRunWorkSheet = "Calculator";
		testRunTableLabel = "CalculatorTest";
		
	}//sampleCalculatorApplicationTest
	/**
	  * <br>Created by: David Ramer
	  * <br>Date Created:  7/26/2017
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
	  *    <br>Step 1: Launch the Calculator App
	  *   	<br>Step 2: enter 2 + 3 = 5
	  * <br><b>Expected Results:</b> 
	  * <br> 1) Calculator Results = 5
	  * 
	  * 
	  * @param String testCount - sequence number
	  * @param String runTest - true/false to skip test
	  * @param String view - Calculator view
	  *
	  * @author David Ramer
	  * @version 1.0 
	  */
	@Test(enabled=true, dataProvider = "sTestBaseTestNGDeclarationDataProvider", groups = {"calculatorAppTest"},testName="calculatorAppTest", description = "Testing the Calculator App on Windows")
	public void calculatorAppTest(String testCount, String runTest, String view) {
		//
		// set Jira ID
		//
		jiraTestID = new String("none");
		//
		// Adding extend Reports
		//
		test = extent.startTest("calculatorAppTest");
		test.assignCategory("Regression");
		//
		// Do we want to process this row of data?
		//	
		if(runTest.toLowerCase().contains("no")){
			throw new SkipException("Skip Test");
		}
		
		if(coreWebDriverFactory == null){
			throw new SkipException("No coreWebDriverFactory created");
		}
		//
		// Create the CalcApplication
		//
		calcApp = new sTestCalculatorApp();
		
		if(calcApp == null){
			throw new SkipException("No sTestCalculatorApp created");
		}
		
		calcApp.setCalculatorView(view);
		
    	
    	if(5 == calcApp.getStandardView().add(2, 3)){
    		System.out.println("Calculator is working great" );
    		test.log(LogStatus.PASS, "Passed Calculator windows Application Test");
    	}else{
    		System.out.println("Calculator failed" );
    		test.log(LogStatus.FAIL, "Failed Calculator windows Application Test");
    	}
    	
    	try {
    		calcApp.sTestCalculatorAppStop();
		} catch (IOException e) {
			System.out.println("Failed to stop the Calculator Application" );
		}
		
		//
		// All Tests Passed
		//
		//Reporter.log("sauceLab results: "+ webDriver.obtainTestStatusInformations(),true);
		//
		// Update Jira if the test has a Jira ID
		//
		if(!jiraTestID.equals("none")){
			coreWebDriverFactory.getJiraFactory().updateJiraTestComment(jiraTestID,"Title","Calculator test passed","David Ramer");
			jiraTestID = "none";
		}
		//
		// Adding extend Reports
		//
		Assert.assertEquals(test.getRunStatus(), LogStatus.PASS);
	}//calculatorAppTest

}
