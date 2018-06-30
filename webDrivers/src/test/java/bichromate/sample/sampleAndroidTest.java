/*
 * sampleAndroidTest.java	1.0 2017/01/31
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


import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.*;
import bichromate.core.sTestBaseTestNGDeclaration;
import bichromate.sample.sampleWebDriverFactory;
import io.appium.java_client.MobileElement;


public class sampleAndroidTest extends sTestBaseTestNGDeclaration{
	
	
	public sampleAndroidTest(){
		
		
		//
		// Create the webDriverFactory that creates all selenium webdrivers
		//
		coreWebDriverFactory = new sampleWebDriverFactory();
		
		if(null == coreWebDriverFactory){
			System.out.println("sampleAndroidTest: sampleWebDriverFactory not created: ");
			System.exit(0);
		}
		//
		// Set the Data for the test
		//
		testRunWorkSheet = "mobile";
		testRunTableLabel = "mobileSampleTest";
	}//sampleAndroidTest
	 /**
	   * <br>Created by: David Ramer
	   * <br>Date Created:  6/23/2017
	   * <br>Updates: 
	   *<br>
	   * <br>Test Case: open yahoo.com and search for some data. This test validated the Bichromate core webdriver creation
	   * <br>Priority High
	   * <br>Setup: 
	   * <br>Status: In-progress
	   * <br>Automated: Yes
	   * <br>Execution Time: 3 minute
	   * <br>Setup: 
	   * <br><b>Steps:</b>
	   *    <br>Step 1: Launch The Calculator App on the Android Device
	   *   	<br>Step 2: click the #2 button
	   *	<br>Step 3: Click the + button
	   *	<br>Step 4: click the #6 button
	   *	<br>Step 5: click the = button
	   * 	<br>Step 6: Verify the results is 8
	   * <br><b>Expected Results:</b> 
	   * <br> 1) Calculator results shows 8
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
	@Test(enabled=true, dataProvider = "sTestBaseTestNGDeclarationDataProvider", groups = {"sampleTest"},testName="androidAppTest", description = "Sample Android App Test")
	
	public void androidTest(String testCount,String runTest, String platformName,String browserName, String platformVersion,String deviceName,
			                String udid,String newCommandTimeout,String appPackage,String appActivity) {
		//
		// set Jira ID
		//
		jiraTestID = new String("none");
	
	
		if(coreWebDriverFactory == null){
			throw new SkipException("No sampleWebDriver created");
		}
		//
		// Setup Capabilities for the Android Apps
		//
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", browserName);   //Leave blank if automating an App
		capabilities.setCapability("platformVersion", platformVersion); 
		capabilities.setCapability("deviceName",deviceName);
		capabilities.setCapability("platformName",platformName);
		capabilities.setCapability("udid", udid);
		capabilities.setCapability("newCommandTimeout",newCommandTimeout);
		capabilities.setCapability("appPackage", appPackage);
		capabilities.setCapability("appActivity",appActivity); 
		
	
		androidDriver = coreWebDriverFactory.createAndroidAppWebDriver(capabilities, "Sample Andorid App Test with the Calculator");
		if(androidDriver == null){
			throw new SkipException("No Android App Driver");
		}
		Reporter.log("yahooTest created web driver",true);
		testName = new String("Start the Android App Test on " +platformName);
		
		try{
			androidDriver.findElementById("2").click();
			
			
			//driver.findElementById("+").click();
			androidDriver.findElementByXPath("//android.widget.Button[@text='+']").click();
			
			androidDriver.findElementById("4").click();
			
			// driver.findElementById("=").click();
			androidDriver.findElementByXPath("//android.widget.Button[@text='=']").click();
			
			//driver.findElementById("EditText").click();
		
			
			MobileElement results=androidDriver.findElementByClassName("android.widget.EditText");
			String resultString = new String(results.getText());
			
			//Check the calculated value on the edit box
			assert results.getText().equals("6"):"Actual value is : "+results.getText()+" did not match with expected value: 6";
			System.out.println("The final output should be 6: "+ resultString);
		}catch(Exception e){
			assert(false); // Failed test with some setup
		}
	
		Reporter.log("Test Complete");
	

	}//androidAppTest

}//sampleAndroidTest
