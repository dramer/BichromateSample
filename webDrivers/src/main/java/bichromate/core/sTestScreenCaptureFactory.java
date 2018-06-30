/*
 * sTestScreenCaptureFactory.java	1.0 2013/01/23
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.axis.encoding.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.ScreenshotException;

import bichromate.sample.sampleWebDriverFactory;

/**
 * This class Demonstrates sTestScreenCaptureFactory().
 * <br>This class factory is used to take screen shots during a testNG test
 * <br>
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("unused")
public class sTestScreenCaptureFactory {
	private static final bichromateConstants bichConstants = new bichromateConstants();
	private ArrayList<String> fileNameList;
	private static ResourceBundle resources;
	private static WebDriver driver = null;
	private static String screenSavedDirectory = null;
	private String directory = null;
	private String filePath;
	private String fileName;
	private String timeStr;
	private String curTime;
	private Calendar calendar = null;
	private String remote = bichConstants.localWebDriver;
	private String currentRunName= "defaultRun";
	private sTestOSInformationFactory path = null;
	
	static {
		try {
			resources = ResourceBundle.getBundle("common.sTestScreenCaptureFactory", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestScreenCaptureFactory.properties not found: "+ mre);
			System.exit(0);
		}
	}
	
	/**
	 * This method demonstrates sTestScreenCaptureFactory().
	 * This class drives all oTest testNG tests. sTestWebDriveFactory encapsulates all other oTest factories. This method encapsulates all the oDesk. obo, mail_gui, and mobile page declarations.  
	 * @param myResources  use the Bichromate.properties file to pass along to all factories
	 */
	public sTestScreenCaptureFactory(ResourceBundle myResources){
		
		setupScreenCaptureFactory(myResources);
		
		
	}// sTestScreenCaptureFactory
	/**
	 * This method demonstrates sTestScreenCaptureFactory().
	 * This class drives all oTest testNG tests.
	 * <br> oTestWebDriveFactory encapsulates all other oTest factories.
	 * <br>This class encapsulates all the oDesk. obo, mail_gui, and mobile page declarations.  
	 */
	public sTestScreenCaptureFactory(){
		
		setupScreenCaptureFactory(resources);
		 
	}// sTestScreenCaptureFactory
	private void setupScreenCaptureFactory(ResourceBundle myResources){
		
		File ssd = null;
		
		path = new sTestOSInformationFactory();
		
		screenSavedDirectory = new String(path.setFilePath(myResources.getString("sTestScreenCaptureFactory.screenSavedDirectory")));
	
		this.fileNameList = new ArrayList<String>();
		calendar = Calendar.getInstance();
		directory = new String(path.workingDirectory()+path.fileSeperator()+screenSavedDirectory.trim());
		
		ssd = new File(directory);
		 
		 if (ssd.isDirectory()){
			 System.out.println("valid screen capture directory");
		 } else{
			 System.out.println("invalid screen capture directories");
		 }
		 //
		// Safe guard incase webdriver is never created
		//
		fileName = new String("No File Captured");
		
		 filePath = new String(directory+path.fileSeperator()+path.getCurrentDateAndUTCTime());
		
		
	}
	/**
	 * This function demonstrates setCurrentRunName().
	 * <br>provides more detail to the screen capture names
	 * <br>
	 * @param runName - name used to better define the screen captures 
	 */
	public void setCurrentRunName(String runName){
		this.currentRunName = new String(runName.trim());
	}
	/**
	 * This function demonstrates setCurrentWebDriver().
	 * <br>Preset the webDriver for the test run
	 * <br>
	 * @param drvr current webDriver 
	 */
	public void setCurrentWebDriver(WebDriver drvr){
		sTestScreenCaptureFactory.driver = drvr;
	}
	
	/**
	 * this method demonstrates createFileName().
	 * This private function return a String formatted HHmmss
	 * @param pageName page name is path of the file path
	 * @return fileName fully qualified file name 
	 */
	private String createFileName(String pageName){
		fileName = new String(filePath + path.fileSeperator()+pageName + "-"+path.getCurrentDateAndUTCTime() +".png"); 
		System.out.println("screen file name: " + fileName);
		return ( fileName);
		
	}
	/**
	 * this method demonstrates createTHFileName().
	 * This private function return a String formatted HHmmss
	 * @param pageName page name is path of the file path for the thumbnail
	 * @return fileName fully qualified file name 
	 */
	private String createTHFileName(String pageName){
		String fileName = new String(filePath +path.fileSeperator()+pageName +"-"+path.getCurrentDateAndUTCTime() +"-th.png"); 
		System.out.println("screen file name: " + fileName);
		return ( fileName);
		
	}
	/**
	 * This method demonstrates getCurrentRunName().
	 * Returns the currentRunName variable
	 * @return currentRunName String with current time appended.
	 */
	public String getCurrentRunName(){
		return currentRunName;

	}
	/**
	 * This method demonstrates getDirectory().
	 * Returns filePath variable
	 * @return filePath String with current filepath
	 */
	public String getDirectory(){
		return filePath;
	}
	/**
	 * This method demonstrates getfileName().
	 * returns fileName variable
	 * @return fileName String with current fileName
	 * @deprecated
	 */
	public String getfileName(){
		return fileName;
	}
	/**
	 * This method demonstrates getlastScreenCaptureFile().
	 * @return fileName   - fully qualified path and name of the last screen shot
	 */
	public String getLastScreenCaptureFileName(){
		return fileName;
	}
	/**
	 * This method demonstrates takeAppScreenShot().
	 * Does tha actual work of taking the screen shot when testing with Applications
	 * @param pageName name of the web page for the screen shot
	 */
	public void takeAppScreenShot(String pageName){
		
		if(null == driver) {
			 System.out.println("sTestScreenCaptureFactory:takeAppScreenShotscreen: driver = null");
			return;
		}
		try{
			File th = null;
        	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        	FileUtils.copyFile(scrFile, th = new File(fileName = createFileName(pageName)));
        	Thumbnails.of(th).size(80, 80).toFile(new File(createTHFileName(pageName)));
        	fileNameList.add(fileName);
        }catch(IOException exp){
        	System.out.println("sTestScreenCaptureFactory:takeAppScreenShot, File Not created " + exp);
        }
        System.out.println("sTestScreenCaptureFactory:takeAppScreenShotscreen, capture taken: "+pageName);
   
	}//takeAppScreenShot
	/**
	 * This method demonstrates takeScreenShot().
	 * Does tha actual work of taking the screen shot
	 * @param pageName name of the web page for the screen shot
	 */
	public void takeWebScreenShot(String pageName){
		if(null == driver) {
			 System.out.println("takeScreenShot: driver = null");
			return;
		}
		
		
        if(remote.equals(bichConstants.localWebDriver)){
			try{
				File th = null;
	        	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        	FileUtils.copyFile(scrFile, th = new File(fileName = createFileName(pageName)));
	        	 Thumbnails.of(th).size(80, 80).toFile(new File(createTHFileName(pageName)));
	        	fileNameList.add(fileName);
	        }catch(IOException exp){
	        	System.out.println("screebGrab: File Not created " + exp);
	        }
	        System.out.println("screen capture taken: "+pageName);
        }else  if(remote.equals(bichConstants.jbrowser)){
        	try{
				File th = null;
	        	File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	        	FileUtils.copyFile(scrFile, th = new File(fileName = createFileName(pageName)));
	        	 Thumbnails.of(th).size(80, 80).toFile(new File(createTHFileName(pageName)));
	        	fileNameList.add(fileName);
	        }catch(IOException exp){
	        	System.out.println("screebGrab: File Not created " + exp);
	        }
	        System.out.println("screen capture taken: "+pageName);
        }else  if(remote.toLowerCase().equals(bichConstants.seleniumGridWebDriver)){
        	try{
        		WebDriver myDriver = new Augmenter().augment(driver); 
        		String screenshotBase64 = ((TakesScreenshot)myDriver).getScreenshotAs(OutputType.BASE64);
        		fileName = createFileName(pageName);
        		File scrFile = new File(path.workingDirectory()+path.fileSeperator()+"temp.png");
        		
        		FileOutputStream fos = new FileOutputStream(scrFile);
        		byte[] decodedScreenshot = Base64.decode(screenshotBase64);
        		fos.write(decodedScreenshot);
        	    fos.close();
        	    FileUtils.copyFile(scrFile, new File(fileName = createFileName(pageName)));
        	    FileUtils.deleteQuietly(scrFile);
	        	fileNameList.add(fileName);
	        }catch(IOException  exp){
	        	System.out.println("screebGrab: IOERROR " + exp);
	        
	        }
	        System.out.println("screen capture taken: "+pageName);
        }
	}//takeScreenShot
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args)
    	{
			
			 ResourceBundle resources = null;
			try {
				
				 resources = ResourceBundle.getBundle("common.Bichromate", Locale.getDefault());
				} catch (MissingResourceException mre) {
					System.out.println("Bichromate.properties not found: "+ mre);
					System.exit(0);
				} 
			
			System.out.println("Starting screen capture test");
			sTestWebDriverFactory slwd = new sampleWebDriverFactory(resources);
			if(sTestWebDriverFactory.getSTestOSInformationFactory().getOSName().toLowerCase().contains(bichConstants.windows)) {
				WebDriver driver = slwd.createBrowserWebDriver(bichConstants.localWebDriver, "",Platform.WINDOWS,"7", bichConstants.firefox,"sTest Self Test - local Chrome web driver");
			}else if(sTestWebDriverFactory.getSTestOSInformationFactory().getOSName().toLowerCase().contains(bichConstants.mac)) {
				WebDriver driver = slwd.createBrowserWebDriver(bichConstants.localWebDriver, "",Platform.MAC, "7",bichConstants.chrome,"sTest Self Test - local Chrome web driver");
			} else if(sTestWebDriverFactory.getSTestOSInformationFactory().getOSName().toLowerCase().contains(bichConstants.linux)) {
				WebDriver driver = slwd.createBrowserWebDriver(bichConstants.localWebDriver, "",Platform.LINUX,"", bichConstants.chrome,"sTest Self Test - local Chrome web driver");
				
			}
			driver.get("http://www.Bichromate.org");
			//
			 // Will only wait 2 minutes if the page takes that long to laod. If the page 
			 // loads in 5 seconds then the wait continues
			 //
			 driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
			 try {
				Thread.sleep(15000); // 60000 is one minute in milliseconds
			} catch (InterruptedException e) {
				sTestWebDriverFactory.getLoggerFactory().enterInfoLog("Thread.sleep tossed Interrupt Exception");
				e.printStackTrace();
			}
			//
			// Take Screen shot
			//
			 sTestWebDriverFactory.getLoggerFactory().enterInfoLog("Taking screen shot");
			sTestWebDriverFactory.getSTestScreenCaptureFactory().takeWebScreenShot("sTestScreenCaptureFactory_Unit_Test");
			//
			// Close the web browser
			//
			sTestWebDriverFactory.getLoggerFactory().enterInfoLog("Driver quit");
			driver.close();
			driver.quit();
	        
    	}// main
	} //Test
} // eTestScreenCaptureFactory
