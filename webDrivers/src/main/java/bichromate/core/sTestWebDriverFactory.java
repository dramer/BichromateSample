/*
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
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.regex.Pattern;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import org.apache.commons.lang.time.StopWatch;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.sikuli.webdriver.SikuliFirefoxDriver;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.google.common.util.concurrent.FluentFuture;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import org.testng.Reporter;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.saucelabs.ci.sauceconnect.SauceConnectTwoManager;
import com.saucelabs.saucerest.SauceREST;

import bichromate.DBAccess.sTestMySQLConnector;
import bichromate.dataStore.sTestTestDataStorage;
import bichromate.kibana.sTestKibanaFactory;
import bichromate.newRelic.sTestNewRelicFactory;
import bichromate.tools.sTestLogFileFactory;
import bichromate.tools.sTestZipCodeAPI;





/** 
 * Sub-class from sTestWebDriverFactory to include defined page objects to be used in creating testNG tests. 
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("unused")
public abstract class sTestWebDriverFactory {
	
	
	private static final bichromateConstants bichConstants = new bichromateConstants();
	
	//
	// Used for building Winium web driver
	//
	 private String WINIUM_PATH = "http://localhost:9999";
    //
    // WebDriver directory
    //
    private String winChromeWebDriverDirectory = null;
    private String macChromeWebDriverDirectory = null;
    private String linuxChromeWebDriverDirectory = null; ///home/dramer/workspace/Bichromate/webDrivers/chromedriver_linux
    private String linux64ChromeWebDriverDirectory = null;
    private String winIEWebDriverDirectory = null;
    private String macIEWebDriverDirectory = null;
    
    //
    // Architect Type
    //
    boolean is64bit = false;
	
	//
	// saucelabs
	//
	private SauceConnectTwoManager sauceConnectManager = null;
	private String sauceConnectLoginName = null;
	private String sauceConnectAccessKey = null;
	//
	// Browser Stack
	//
	private String browserStackLoginName = null;
	private String browserStackKey = null;
	private String browserStackURL =null;
	//
	// Selenium items
	//
	//protected WebDriverWait wait;
	protected org.openqa.selenium.support.ui.FluentWait <RemoteWebDriver>  fluentWait;
	protected RemoteWebDriver driver;
	protected AndroidDriver <MobileElement> androidDriver = null;
	protected IOSDriver <MobileElement> iosDriver = null;
	protected WebDriver localDriver;
	private DesiredCapabilities proxyCap = null;
	//
	// Mobile items
	//
	public static final String portrait = "PORTRAIT";
	public static final String landscape = "LANDSCAPE";
	//
	// local variables
	//
	private String proxy =  null; 
	
	protected static ResourceBundle resources;
	private Executor executor = null;
	private String osName = null;
	private boolean failProxyToLocalWebDriver = false;
	private String methodName = null;
	private String remoteWebDriverLogging = "false";
	private String enableVideo = "false";
	private String browserStartPage = "http://www.google.com";
	private String fileDownloadDirectory = null;
	//
	// Stop Watch for page load times
	//
	private StopWatch pageLoad = null; //new StopWatch();
	//
	// FireFox plugins
	//
	private String firebug_xpi = null;
	private String netexport_xpi = null;
	//
	// Local Factories
	//
	private static sTestJiraFactory jiraFactory = null;
	private static sTestOSInformationFactory osInfo =  null;
	private static webDriverLogFactory WEBDRIVERLOGGER = new webDriverLogFactory();
	private static sTestScrollIntoViewFactory siv = null;
	private static sTestHilitePageElementFactory helem = null;
	private static sTestScreenCaptureFactory screencapture = null;
	private static sTestZipFileFactory zipFile = null;
	private static sTestSlackFactory slackFactory = null;
	private static sTestHipChatFactory hipChat = null;
	private static sTestVideoCaptureFactory vidCapture = null;
	private static sTestMySQLConnector mySQLConnection = null;
	private static sTestFTPFactory  ftp = null;
	private static sTestCustomerAccountManagerFactory passwordStore = null;
	private static sTestLogFileFactory log = null;
	private static sTestCleanupFactory cuf = null;
	private static sTestZipCodeAPI zipCodeAPI = null;
	private static sTestNewRelicFactory newRelic = null;
	private static sTestKibanaFactory kibana = null;
	private static sTestTestDataStorage testStorage = null;
	
	static {
		try {
			resources = ResourceBundle.getBundle("common.sTestWebDriverFactory", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestWebDriverFactory.properties not found: "+ mre);
			System.exit(0);
		}
		
	}
    private String win32GeckoWebDriverDirectory = "geckodriver_win32";
    private String win64GeckoWebDriverDirectory = "geckodriver_win64";
    private String macGeckoWebDriverDirectory = "geckodriver_mac";
    private String linuxGeckoWebDriverDirectory = "geckodriver_linux";
	/**
	 * method sTestWebDriverFactory() constructor
	 * constructor for the sTestWebDriverFactory This is the heart of Bichromate
	 */
	protected sTestWebDriverFactory() {
		setupWebDriverFactory(resources);
		
	}// eTestWebDrierFactory
	/**
	 * This method sTestWebDriverFactory() constructor
	 * Constructor for the sTestWebDriverFactory This is the heart of Bichromate
	 * @param remoteResources to initialize all Factories, you can pass in Bichromate.properties
	 */
	public sTestWebDriverFactory(ResourceBundle remoteResources) {
		setupWebDriverFactory(remoteResources);
		
	}// eTestWebDrierFactory
	/**
	 * This method setupWebDriverFactory() constructor
	 * Used to setup all the factories used in sTestWebDriverFactory
	 * @param myResources to initialize all Factories, you can pass in Bichromate.properties
	 */
	private void setupWebDriverFactory(ResourceBundle myResources){
		//
		//Architect Type use for Linux or Windows. MAC is 64 but
		//
		if( System.getProperty("os.arch").contains("64")){
			is64bit = true;
		}
		//
		// Create Local Factories
		//
		WEBDRIVERLOGGER.setLogToFile();
		WEBDRIVERLOGGER.enterInfoLog("logFileFactory created");
		osInfo =  new sTestOSInformationFactory();
		WEBDRIVERLOGGER.enterInfoLog("sTestOSInformationFactory created");
		siv = new sTestScrollIntoViewFactory();
		WEBDRIVERLOGGER.enterInfoLog("scrollIntoViewFactory created");
		helem = new sTestHilitePageElementFactory();
		WEBDRIVERLOGGER.enterInfoLog("sTestHilitePageElementFactory created");
		jiraFactory = new sTestJiraFactory(myResources);
		WEBDRIVERLOGGER.enterInfoLog("sTestJiraFactory created");
		screencapture = new sTestScreenCaptureFactory();
		WEBDRIVERLOGGER.enterInfoLog("sTestScreenCaptureFactory created");
		zipFile = new sTestZipFileFactory(myResources);
		WEBDRIVERLOGGER.enterInfoLog("sTestZipFileFactory created");
		slackFactory = new sTestSlackFactory(myResources);
		WEBDRIVERLOGGER.enterInfoLog("sTestSlackFactory created");
		hipChat = new sTestHipChatFactory(myResources);
		WEBDRIVERLOGGER.enterInfoLog("sTestHipChatFactory created");
		ftp = new sTestFTPFactory();
		WEBDRIVERLOGGER.enterInfoLog("sTestFTPFactory created");
		passwordStore = new sTestCustomerAccountManagerFactory(myResources);
		WEBDRIVERLOGGER.enterInfoLog("sTestCustomerAccountManagerFactory created");
		log = new sTestLogFileFactory(myResources);
		WEBDRIVERLOGGER.enterInfoLog("sTestLogFileFactory created");
		mySQLConnection = new sTestMySQLConnector(myResources);
		WEBDRIVERLOGGER.enterInfoLog("sTestMySQLConnector created");
		cuf = new sTestCleanupFactory(myResources);
		WEBDRIVERLOGGER.enterInfoLog("sTestCleanupFactory created");
		newRelic = new sTestNewRelicFactory(myResources);
		WEBDRIVERLOGGER.enterInfoLog("sTestNewRelicFactory created");
		kibana = new sTestKibanaFactory(myResources);
		WEBDRIVERLOGGER.enterInfoLog("sTestKibanaFactory created");
		 testStorage = new sTestTestDataStorage();
		 WEBDRIVERLOGGER.enterInfoLog("sTestTestDataStorage created");
		//
		// SauceLab Connection Information
		//
		sauceConnectManager = new SauceConnectTwoManager();
		sauceConnectLoginName =  new String(myResources.getString("sTestWebDriverFactory.sauceConnectLoginName"));
		sauceConnectAccessKey =  new String(myResources.getString("sTestWebDriverFactory.sauceConnectAccessCode"));
		WEBDRIVERLOGGER.enterInfoLog("sauceConnectManager configured");
		//
		// Browser Stack
		//
		browserStackLoginName = new String(myResources.getString("sTestWebDriverFactory.browserStackLoginName"));
		browserStackKey = new String(myResources.getString("sTestWebDriverFactory.browserStackKey"));
		browserStackURL = new String(myResources.getString("sTestWebDriverFactory.browserStackURL"));
		//
		// WebDriver directories are assumed to be under the webDrivers Directory
		//
		winChromeWebDriverDirectory =  new String(myResources.getString("sTestWebDriverFactory.winChromeWebDriverDirectory"));
		macChromeWebDriverDirectory =  new String(myResources.getString("sTestWebDriverFactory.macChromeWebDriverDirectory"));
		linuxChromeWebDriverDirectory = new String(myResources.getString("sTestWebDriverFactory.linuxChromeWebDriverDirectory"));
		linux64ChromeWebDriverDirectory = new String(myResources.getString("sTestWebDriverFactory.linux64ChromeWebDriverDirectory"));
		winIEWebDriverDirectory =  new String(myResources.getString("sTestWebDriverFactory.winIEWebDriverDirectory"));
		macIEWebDriverDirectory =  new String(myResources.getString("sTestWebDriverFactory.macIEWebDriverDirectory"));
		WEBDRIVERLOGGER.enterInfoLog("webdrivers setup");
		//
		// Selenium Grid IP
		//
		proxy =  new String(myResources.getString("sTestWebDriverFactory.ProxyIP-1"));
		//
		// FireFox Plugins
		//
		firebug_xpi  =  new String(myResources.getString("sTestWebDriverFactory.firebug_xpi"));
		netexport_xpi =  new String(myResources.getString("sTestWebDriverFactory.netexport_xpi"));
		//
		// logging for webDriver
		//
		remoteWebDriverLogging =  new String(myResources.getString("sTestWebDriverFactory.remoteWebDriverLogging"));
		//
		// Enable recording
		//
		enableVideo =  new String(myResources.getString("sTestWebDriverFactory.enableVideo"));
		//
		//
		//
		browserStartPage =  new String(myResources.getString("sTestWebDriverFactory.browserStartPage"));
		fileDownloadDirectory =  new String(myResources.getString("sTestWebDriverFactory.fileDownloadDirectory"));
		//
		// Create Stop watch
		//
		pageLoad = new StopWatch();
		//
		// Setup FireFox gecko Driver
		//
		if(osInfo.getOSName().toLowerCase().contains(bichConstants.windows)) {
        	
        	String arch = new String(osInfo.isRunningWin64()?win64GeckoWebDriverDirectory:win32GeckoWebDriverDirectory);
        	
			System.setProperty("webdriver.gecko.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+arch+osInfo.fileSeperator()+"geckodriver.exe");
			
		}else if(osInfo.getOSName().toLowerCase().contains(bichConstants.mac)) {
			System.setProperty("webdriver.gecko.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+macGeckoWebDriverDirectory+osInfo.fileSeperator()+ "geckodriver");
		}else if(osInfo.getOSName().toLowerCase().contains(bichConstants.linux)) {
			System.setProperty("webdriver.gecko.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+linuxGeckoWebDriverDirectory+osInfo.fileSeperator()+ "geckodriver");
			WEBDRIVERLOGGER.enterInfoLog("system.setproperty webdriver.gecko.driver for linux");
		}else{
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewFirefoxLocalDriver: Windows,Mac, Linux os not found for firefox webDriver");
		}
		//
		// Setup IE WebDriver
		if(osInfo.getOSName().toLowerCase().contains(bichConstants.windows)) {
			System.setProperty("webdriver.ie.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+winIEWebDriverDirectory+osInfo.fileSeperator()+"IEDriverServer.exe");
		
		}else if(osInfo.getOSName().toLowerCase().contains(bichConstants.mac)) {
			System.setProperty("webdriver.ie.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+macIEWebDriverDirectory+osInfo.fileSeperator()+ "IEDriverServer");
		} else{
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewIELocalDriver: failed to create webDriver");
		}
		//
		// Send the owner a message that the webdriver class is being used.
		//
		sTestMessageFactory sms = new sTestMessageFactory();
		// if(null != sms) 
			// sms.sendSMS("davidwramertesting@gmail.com","4084803723@vtext.com", "Bichromate in Use","Bichromate setupWebDriverFactory was called By: " +osInfo.getHostName()+" IP: "+osInfo.getIPAddress());

	}//setupWebDriverFactory
	
	/**
	 * sTestVideoCaptureFactory()
	 * If during a test you want to capture it as video
	 * @return sTestVideoCaptureFactory - returns the video factory
	 * @see sTestVideoCaptureFactory
	 * @author DRamer
	 * @version 1.0
	 */
	public sTestVideoCaptureFactory getSTestVideoCaptureFactory(){
		
		if(null == vidCapture){
			
			if(0 == enableVideo.compareToIgnoreCase("False")){
				WEBDRIVERLOGGER.enterInfoLog("sTestVideoCaptureFactory created and disabled");
				vidCapture = new sTestVideoCaptureFactory();
			}else{
				WEBDRIVERLOGGER.enterInfoLog("sTestVideoCaptureFactory created and enabled");
				vidCapture = new sTestVideoCaptureFactory(true);
			}
		}
		return vidCapture;
	}
	/**
	 * stopWatchStart()
	 * Used to calculate page load times
	 * @author DRamer
	 * @version 1.0
	 */
	public void stopWatchStart(){
		 pageLoad.start();
	}
	/**
	 * stopWatchStop()
	 * Used to calculate page load times
	 * @param url - page to load
	 * @return long - time of the test
	 * @author DRamer
	 * @version 1.0
	 */
	public long stopWatchStop(String url){
		 pageLoad.stop();
		 long pageLoadTime_ms = pageLoad.getTime();
	     long pageLoadTime_Seconds = pageLoadTime_ms / 1000;
	     WEBDRIVERLOGGER.enterInfoLog("Load Time for: "+url+" is: " + pageLoadTime_ms + " milliseconds");
	     return pageLoadTime_ms;
	}
	/**
	 * setFailOverToLocalWebDriver()
	 * If the remote webdriver can't be created then try to create a local webDriver
	 * @author DRamer
	 * @version 1.0
	 */
	public void setFailOverToLocalWebDriver(){
		failProxyToLocalWebDriver = true;
	}
	/**
	 * cancelFailOverToLocalWebDriver()
	 * If the remote webdriver can't be created then try to create a local webDriver
	 * @author DRamer
	 * @version 1.0
	 */
	public void cancelFailOverToLocalWebDriver(){
		failProxyToLocalWebDriver = false;
	}
	/**
	 * getsTestTestDataStorage() 
	 * @return sTestTestDataStorage - getsTestTestDataStorage that is used to store test data to be shared across tests 
	 * @see sTestTestDataStorage
	 * @author DRamer
	 * @version 1.0
	 */
	public static  sTestTestDataStorage getsTestTestDataStorage(){
		return testStorage;
	}
	/**
	 * getSTestLogFileFactory()
	 * Returns the getSTestLogFileFactory class. Used to get log files from the tested server. Server and log file info 
	 * <br> must be setup in the properties file.
	 * @see sTestLogFileFactory
	 * @return sTestLogFileFactory - log file factory
	 * @author DRamer
	 * @version 1.0
	 */
	public static sTestLogFileFactory getSTestLogFileFactory(){
		return log;
	}
	/**
	 * getSTestMySQLConnector()
	 * Returns the sTestMySQLConnector class. Used to clean up test files
	 * <br> must be setup in the properties file.
	 * @see sTestMySQLConnector
	 * @return sTestMySQLConnector  -SQL connection factory
	 * @author DRamer
	 * @version 1.0
	 */
	public  static sTestMySQLConnector getSTestMySQLConnector(){
		return mySQLConnection;
	}
	/**
	 * sTestKibanaFactory()
	 * Returns the sTestKibanaFactory class. Used to access ELK Stack.
	 * <br> must be setup in the properties file.
	 * @see sTestKibanaFactory
	 * @return sTestKibanaFactory - Kibana Factory
	 * @author DRamer
	 * @version 1.0
	 */
	public static sTestKibanaFactory getsTestKibanaFactory(){
	
		return kibana;
	}
	/**
	 * getsTestNewRelicFactory()
	 * Returns the sTestNewRelicFactory class. Used to access NewRelic.
	 * <br> must be setup in the properties file.
	 * @see sTestNewRelicFactory
	 * @return sTestNewRelicFactory - NewRelic Factory
	 * @author DRamer
	 * @version 1.0
	 */
	public static sTestNewRelicFactory getsTestNewRelicFactory(){
	
		return newRelic;
	}
	/**
	 * getSTestMySQLConnector()
	 * Returns the getsTestCleanupFactory class. Used to clean up test files
	 * <br> must be setup in the properties file.
	 * @see sTestMySQLConnector
	 * @return sTestMySQLConnector - SQL connection factory
	 * @author DRamer
	 * @version 1.0
	 */
	public static sTestCleanupFactory getsTestCleanupFactory(){
		
		return cuf;
	}
	/**
	 * getSTestCustomerAccountManagerFactory()
	 * @see sTestCustomerAccountManagerFactory
	 * @return passwordStore - Returns the sTestCustomerAccountManagerFactory class. Used to get passwords for accounts when logging in
	 * @author DRamer
	 * @version 1.0
	 */
	public static sTestCustomerAccountManagerFactory getSTestCustomerAccountManagerFactory(){
		return passwordStore;
	}
	/**
	 * getZipCodeFactory()
	 * @see sTestZipCodeAPI
	 * @return sTestZipCodeAPI - Returns the sTestZipCodeAPI class. Used to calculate and obtain zip information
	 * @author DRamer
	 * @version 1.0
	 */
	public static sTestZipCodeAPI getZipCodeFactory(){
		if(null == zipCodeAPI){
			zipCodeAPI = new sTestZipCodeAPI();
		}
		return zipCodeAPI;
	}
	/**
	 * getSTestFTPFactory()
	 * @see getSTestFTPFactory
	 * @return sTestFTPFactory - Returns the getSTestFTPFactory class. Used to post files to a FTP server that is setup in the properties file
	 * @author DRamer
	 * @version 1.0	
	 */
	public static sTestFTPFactory getSTestFTPFactory(){
		return ftp;
	}
	
	/**
	 * getSTestOSInformationFactory()
	 * @see getSTestOSInformationFactory
	 * @return osInfo - Returns the getSTestOSInformationFactory class
	 * @author DRamer
	 * @version 1.0	
	 */
	public static sTestOSInformationFactory getSTestOSInformationFactory(){
		return osInfo;
	}
	
	/**
	 * logFileFactory()
	 * Returns the webDriverLogFactory class
	 * @see webDriverLogFactory
	 * @return sTestScrollIntoViewFactory - webdriver logger
	 * @author DRamer
	 * @version 1.0	
	 */
	public static webDriverLogFactory getLogFactory(){
		return WEBDRIVERLOGGER;
	}
	
	/**
	 * getSTestHipChatFactory()
	 * @see sTestSlackFactory
	 * @return hipChat - Returns the sTestHipChatFactory class
	 * @author DRamer
	 * @version 1.0	
	 */
	public static sTestHipChatFactory getSTestHipChatFactory(){
		return hipChat;
	}
	/**
	 * sTestSlackFactory()
	 * @see sTestSlackFactory
	 * @return slackFactory - Returns the sTestSlackFactory class
	 * @author DRamer
	 * @version 1.0	
	 */
	public static sTestSlackFactory getSTestSlackFactory(){
		return slackFactory;
	}
	/**
	 * getSTestJiraFactory()
	 * Returns the jiraFactory class
	 * @see sTestJiraFactory
	 * @return jiraFactory factory for entering jira updates
	 * @author DRamer
	 * @version 1.0	
	 */
	public static sTestJiraFactory getZipFileFactory(){
		return jiraFactory;
	}
	/**
	 * getSTestJiraFactory()
	 * Return the sTestZipFileFactory class
	 * @return zipFile zip file factory for zipping up screen shots
	 * @see sTestZipFileFactory
	 * @author DRamer
	 * @version 1.0	
	 */
	public static sTestZipFileFactory getSTestJiraFactory(){
		return zipFile;
	}
	/**
	 * method getScreenGrabber()
	 * Returns the getScreenGrabber class
	 * @see sTestScreenCaptureFactory
	 * @return screencapture sTestScreenCaptureFactory to take screen shots
	 * @author DRamer
	 * @version 1.0	
	 */
	public static sTestScreenCaptureFactory getSTestScreenCaptureFactory(){
		return screencapture;
	}
	
	/**
	 * method getScrollIntoViewFactory()
	 * Returns the scrollIntoView class
	 * @see sTestScrollIntoViewFactory
	 * @return sTestScrollIntoViewFactory factory to scroll elements into view
	 * @author DRamer
	 * @version 1.0
	 */
	public static sTestScrollIntoViewFactory getScrollIntoViewFactory(){
		return siv;
	}
	/**
	 * method getSTestHilitePageElementFactory()
	 * Return the sTestHilitePageElementFactory
	 * @see sTestHilitePageElementFactory
	 * @return helem  hilite factory to hilite elements on a page
	 */
	public static sTestHilitePageElementFactory getSTestHilitePageElementFactory(){
		return helem;
	}
	/**
	 * method getJiraFactory()
	 * Returns the sTestJiraFactory class
	 * @see sTestJiraFactory
	 * @return jiraFactory  factory to enter jira updates
	 */
	public static sTestJiraFactory getJiraFactory(){
		return jiraFactory;
	}
	/**
	 * method getLoggerFactory()
	 * Returns the webDriverLogFactory
	 * @return logger   log file for entering log entries
	 * @see webDriverLogFactory
	 */
	public static webDriverLogFactory getLoggerFactory(){
		return WEBDRIVERLOGGER;
	}
	/**
	 * method getWebDriver()
	 * Returns the RemoteWebDriver
	 * @return driver  Selenium remote webDriver
	 * @see RemoteWebDriver 
	 */
	public RemoteWebDriver getWebDriver(){
		return driver;
	}// getWebDriver
	/**
	 * method getWebDriver()
	 * Returns the RemoteWebDriver
	 * @return driver  Selenium Andoroid Driver
	 * @see RemoteWebDriver 
	 */
	public RemoteWebDriver getAndroidDriver(){
		return androidDriver;
	}// getAndroidDriver
	/**
	 * method getIOSDriver()
	 * Returns the RemoteWebDriver
	 * @return driver  Selenium IOS Driver
	 * @see RemoteWebDriver 
	 */
	public RemoteWebDriver getIOSDriver(){
		return iosDriver;
	}// getIOSDriver
	/**
	 * method sTestThreadWait()
	 * used to stop the test thread for defined milliseconds
	 * @param milliseconds - 60000 is equal to one minute
	 */
	public void sTestThreadWait(int milliseconds){
		try {
			Thread.sleep(milliseconds); // 60000 is one minute in milliseconds
		} catch (InterruptedException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:sTestWait:InterruptedException: "+e.getMessage());
		}
	}//sTestThreadWait
	
	/**
	 * method buildPatternFromURL()
	 * Takes a string and turns it into a URL
	 * @param URL  - URL with embedded URL
	 * @return pattern  pattern builder
	 */
	private static Pattern buildPatternFromURL(String URL){
		
		final Pattern pattern = Pattern.compile(URL);
		
		return pattern;
	}// buildPatterFromURL
	/**
	 * method getProxyName() 
	 * Returns the name of the proxy for the Grid Console machine (hub)
	 * @return proxy - name of Selenium grid hub
	 */
	public String getProxyName(){
		return proxy;
	}// getProxyName

	/**
	 * createSilukiWebDriver() Main class that creates the createSilukiWebDriver
	 * <br>- This webDrive extends the FireFox webdriver that runs on local host
	 * <br>- See https://answers.launchpad.net/sikuli
	 * <br>- download jar file from http://www.java2s.com/Code/Jar/s/Downloadsikuliwebdriver101jar.htm
	 * @return SikuliFirefoxDriver webdriver to pinpoint map items
	 * @see SikuliFirefoxDriver
	 * @return driver SikuliFirefoxDriver for map usage
	 */
	
	private SikuliFirefoxDriver createSilukiWebDriver() {
		
		SikuliFirefoxDriver driver = new SikuliFirefoxDriver(); 
		return driver;
	}
	
	/**
	 * ceateJBrowserDriver() Main class that creates the ceateJBrowserDriver
	 * <br>- Headless webDriver for testing on machines that do not have any browsers installed
	 * <br>- See https://github.com/MachinePublishers/jBrowserDriver/
	 * <br>- download from https://github.com/MachinePublishers/jBrowserDriver/
	 * @return driver  remote web driver
	 * @see RemoteWebDriver
	 * @see JBrowserDriver
	 * @return RemoteWebDriver
	 */
	private RemoteWebDriver ceateJBrowserDriver(){
		 // 
		// You can optionally pass a Settings object here, constructed using Settings.Builder
		// Fixed from issue: https://github.com/MachinePublishers/jBrowserDriver/issues/130
		
	    driver = new JBrowserDriver(Settings.builder().ssl("compatible").build());
	    return driver;
	}
	/**
	 * method createFireFoxNetPanelWebDriver()
	 * CreateFireFoxNetPanelWebDriver creates a FireFox web driver with Firbug and NetPanel installed
	 * <br> and active. This web driver is used to test page load times with firerox
	 * @return driver  remote selenium web driver
	 * @see RemoteWebDriver
	 */
	private RemoteWebDriver createFireFoxNetPanelWebDriver() {
		
		FirefoxProfile profile = new FirefoxProfile();
		FirefoxOptions options = new FirefoxOptions();
		//
		// Need to download and store these plugins into a directory
		//
		
		//
		// FireFox Plugins
		//
        File firebug = new File(osInfo.workingDirectory()+osInfo.fileSeperator()+"plugins"+osInfo.fileSeperator()+firebug_xpi);
        File netExport = new File(osInfo.workingDirectory()+osInfo.fileSeperator()+"plugins"+osInfo.fileSeperator()+netexport_xpi);

        //try
        {
            profile.addExtension(firebug);
            profile.addExtension(netExport);
        }
       // catch (IOException e)
       // {
       // 	WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:createFireFoxNetPanelWebDriver:IOException: "+e.getMessage());
        //    return null;
       // }

        String domain = "extensions.firebug.";

        // Setting Firebug preferences
        profile.setPreference("app.update.enabled", false);
        profile.setPreference(domain + "currentVersion", "2.0");
        profile.setPreference(domain + "addonBarOpened", true);
        profile.setPreference(domain + "console.enableSites", true);
        profile.setPreference(domain + "script.enableSites", true);
        profile.setPreference(domain + "net.enableSites", true);
        profile.setPreference(domain + "previousPlacement", 1);
        profile.setPreference(domain + "allPagesActivation", "on");
        profile.setPreference(domain + "onByDefault", true);
        profile.setPreference(domain + "defaultPanelName", "net");

        // Setting netExport preferences

        profile.setPreference(domain + "netexport.alwaysEnableAutoExport", true);
        profile.setPreference(domain + "netexport.autoExportToFile", true);
        profile.setPreference(domain + "netexport.Automation", true);
        profile.setPreference(domain + "netexport.showPreview", true);
        profile.setPreference(domain + "netexport.defaultLogDir", osInfo.workingDirectory()+osInfo.fileSeperator()+"logFiles"+osInfo.fileSeperator()+"netExport");
        // this preference is not working"
        profile.setPreference(domain + "netexport.defaultFileName", "testHar"); 
        options.setProfile(profile);
		RemoteWebDriver driver = new FirefoxDriver(options);
        
        return driver;
	}// createFireFoxNetPanelWebDrier
	
	
	/**
	 * obtainTestStatusInformation()
	 * <br> This function when called will return the test status from saucelabs for the currently running test
	 * @return jobInfo with the status
	 */
	public String obtainTestStatusInformations() {

		
		/*
		 * to get your jobID: Selenium 1: String jobID =
		 * browser.getEval("selenium.sessionId"); Selenium 2: String jobID =
		 * ((RemoteWebDriver) driver).getSessionId().toString();
		 */

		SauceREST client = new SauceREST(sauceConnectLoginName, sauceConnectAccessKey);
		/*
		 * Using a map of udpates:
		 * (http://saucelabs.com/docs/sauce-ondemand#
		 * alternative-annotation-methods)
		 * 
		 * Map<String, Object> updates = new HashMap<String, Object>();
		 * updates.put("name", "this job has a name"); updates.put("passed",
		 * true); updates.put("build", "c234");
		 * client.updateJobInfo("<your-job-id>", updates);
		 */
		String jobID = ((RemoteWebDriver) driver).getSessionId().toString();
		String jobInfo = client.getJobInfo(jobID);
		WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:obtainTestStatusInformation:jobInfo: "+jobInfo);
		// client.jobPassed(jobID);
		// client.jobFailed("<your-job-id>");
		return new String(jobInfo);
		
	}// obtainTestStatusInformation
	
	/**
	 * method createWindowsAPPDriver() 
	 * Main method to create a windows Application driver using Winium.
	 * @param appExepath  - path to the winium driver
	 * @throws IOException - failed to create Winium driver
	 * @return WiniumDriver - windows driver
	 * @see WiniumDriver
	 * @author dramer
	 */
	public WiniumDriver createWindowsAPPDriver(String appExepath) throws IOException{
		
		WiniumDriver driver = null;
		DesktopOptions options;
		Runtime rt = null;
		 
		if(!osInfo.getOSName().toLowerCase().contains("windows")){
    		System.out.println("Calculator Application only runs on Windows" );
    		return driver;
    	}
    	
		
    	 options = new DesktopOptions();
         options.setApplicationPath(appExepath);
         driver = new WiniumDriver(new URL(WINIUM_PATH), options);
         
         return driver;
		
	}// createWindowsAPPDriver
	
	/**
	 * method createAndroidAppWebDriver() 
	 * Main method to create a AndroidDriver using http://127.0.0.1:4723/wd/hub
	 * @param caps - DesiredCapabilities
	 * @param testInformation - info about the test 
	 * @return AndroidDriver -android webdriver
	 * @see AndroidDriver
	 * @author dramer
	 * @version 1.0
	 */
	public AndroidDriver <MobileElement>  createAndroidAppWebDriver(DesiredCapabilities caps, String testInformation) {
		
		
		androidDriver = null;
		
		this.methodName = new String(testInformation);
		
		try {
			androidDriver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
		   } catch (MalformedURLException e) {
			   WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:createAndroidAppWebDriver: androidDriver not created");
			e.printStackTrace();
		   }
		
		return androidDriver; 
		
	}//createAndroidAppWebDriver
	/**
	 * method createAndroidAppWebDriver() 
	 * Main method to create a AndroidDriver 
	 * @param caps - DesiredCapabilities   
	 * @param appiumDriverURL - http://127.0.0.1:4723/wd/hub
	 * @param testInformation - Info about the test
	 * @return AndroidDriver -webdriver
	 * @see AndroidDriver 
	 * @author dramer
	 * @version 1.0
	 */
	public AndroidDriver <MobileElement>  createAndroidAppWebDriver(DesiredCapabilities caps, String appiumDriverURL,String testInformation) {
		
		
		androidDriver = null;
		
		this.methodName = new String(testInformation);
		
		try {
			// "http://127.0.0.1:4723/wd/hub"
			androidDriver = new AndroidDriver<MobileElement>(new URL(appiumDriverURL), caps);
		   } catch (MalformedURLException e) {
			   WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:createAndroidAppWebDriver: androidDriver not created");
			e.printStackTrace();
		   }
		
		return androidDriver; 
		
	}//createAndroidAppWebDriver
	
	/**
	 * createBrowserWebDriver() 
	 * Main class that creates the selenium web drivers, This is wrapper method that allows you to pass in a string for the platform.
	 * @param remote  - Local, proxy,saucelabs must use
	 * @param browserVersion = browser version only used when connecting to saucelabs
	 * @param platform - windows,Linux,Mac. Used when connecting to proxy, or browser stack, or saucelabs
	 * @param osVersion - Windows (7,8,8.1,10), MAC(OS_X), Linux not used leave blank
	 * @param browser - (Firefox,Chrome,IOS,Android,IE)
	 * @param setupString - Test description
	 * @return RemoteWebDriver - create a webdriver based on the inputs
	 * @see RemoteWebDriver - webdriver created based on input
	 * @author dramer
	 * @version 1.0
	 * 
	 */
	public RemoteWebDriver createBrowserWebDriver(String remote, String browserVersion, String platform, String osVersion,String browser, String setupString) {
		
		//
		// Make sure the OS and platform match
		// If the call is for a local webdriver then platform must equal osName
		//
		if(remote.contains(bichConstants.localWebDriver)){
			
			String osName = new String(osInfo.getOSName().toLowerCase());
			
			if(osName.toLowerCase().contains(bichConstants.linux)){
				platform = new String(bichConstants.linux).toUpperCase();	
			}else if(osName.toLowerCase().contains(bichConstants.mac)){
				platform = new String(bichConstants.mac).toUpperCase();
			}else if(osName.toLowerCase().contains(bichConstants.windows)){
				platform = new String(bichConstants.windows).toUpperCase();
			}
		}
		if(platform.contains(Platform.MAC.toString()))
			return createBrowserWebDriver(remote,browserVersion,Platform.MAC,osVersion,browser,setupString);
		if(platform.contains(Platform.WINDOWS.toString()))
			return createBrowserWebDriver(remote,browserVersion,Platform.WINDOWS,osVersion,browser,setupString);
		if(platform.contains(Platform.LINUX.toString()))
			return createBrowserWebDriver(remote,browserVersion,Platform.LINUX,osVersion,browser,setupString);
		
		
		WEBDRIVERLOGGER.enterInfoLog("Can't determine platform, trying to create local webDriver");
		
		if(osInfo.getOSName().toLowerCase().contains(bichConstants.linux))
			return createBrowserWebDriver(bichConstants.localWebDriver,browserVersion,Platform.LINUX,osVersion,browser,setupString);
		if(osInfo.getOSName().toLowerCase().contains(bichConstants.windows))
			return createBrowserWebDriver(bichConstants.localWebDriver,browserVersion,Platform.WINDOWS,osVersion,browser,setupString);
		if(osInfo.getOSName().toLowerCase().contains(bichConstants.mac))
			createBrowserWebDriver(bichConstants.localWebDriver,browserVersion,Platform.MAC,osVersion,browser,setupString);
		
		return null;
	}
	/**
	 * method dragAndDrop() 
	 * Drag and drop using Selenium Actions
	 * @param from = web element
	 * @param to = web element
	 * @see Actions
	 */
	public void dragAndDrop(WebElement from, WebElement to){

		(new Actions(driver)).dragAndDrop(from, to).perform();
		
		
	}//dragAndDrop
	/**
	 * method createBrowserWebDriver() 
	 * Main class that creates the selenium web driver
	 * @param remote  - Local, proxy,saucelabs must use
	 * @param browserVersion = browser version only used when connecting to saucelabs
	 * @param platform - Only used when connecting to proxy, saucelabs, browser stack
	 * @param osVersion - Windows(7,8,8.1,10) MAC (os_X) Linux not used
	 * @param browser - (Firefox,Chrome,IOS,Android,IE)
	 * @param setupString - Test description
	 * @return RemoteWebDriver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	public RemoteWebDriver createBrowserWebDriver(String remote, String browserVersion, Platform platform, String osVersion, String browser, String setupString) {
		
		
		driver = null;
		
		this.methodName = new String(setupString);
		
		//
		// Test on sauceLabs cloud servers
		//
		if (remote.equals(bichConstants.sauceLabWebDriver)) {
			
			
			
			if (browser.toLowerCase().contains(bichConstants.ipad))
				driver = buildNewIPadRemoteDriver(platform, browserVersion);
			if (browser.toLowerCase().contains(bichConstants.firefox))
				driver = buildNewFireFoxRemoteDriver(platform, browserVersion);
			if (browser.toLowerCase().contains(bichConstants.ie))
				driver = buildNewIERemoteDriver(platform, browserVersion);
			if (browser.toLowerCase().contains(bichConstants.chrome))
				driver = buildNewChromeRemoteDriver(platform, browserVersion);
			if (browser.toLowerCase().contains(bichConstants.safari))
				driver = buildNewSafariRemoteDriver(platform, browserVersion);
			
			
		} else if (remote.equals(bichConstants.seleniumGridWebDriver)) {
			//
			// Selenium Grid
			// To start the grid on windows: java -jar selenium-server-standalone-2.35.0.java -role hub
			// You have to start a node on the hub if you want to use the hub for testing.
			// To start nodes  java -jar selenium-server-standalone-2.35.0.jar -role node -hub http://<IP>:4444/grid/register
			// on Mac set the -cp (classpath) java -cp /home/downloads/selenium-server-standalone-2.35.0.jar -role node -hub http://<IP>:4444/grid/register
			// on mac make sure you down load the chrome exe for webdriver and put in same directory.
			// on export Ubuntu 12.04
			//	 ctrl +alt + F1 to open console
			//	 then enter "export DISPLAY=:0"  "DISPLAY" MUST BE CAPITALIZED!!!!!!
			//	 java -cp /home/<dir for standalone server>/selenium-server-standalone-2/35/0.jar -jar selenium-server-standalone-2.35.0.jar -role node -hub http://<IP of hub>:4444/grid/register
			// WINDOWS VISTA
			//
			if( platform.equals(Platform.VISTA)){
				
				if (browser.toLowerCase().contains(bichConstants.firefox))
					driver = buildNewFireFoxProxyDriver(proxy,Platform.VISTA,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.ie))
					driver = buildNewIEProxyDriver(proxy,Platform.VISTA,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.chrome))
					driver = buildNewChromeProxyDriver(proxy,Platform.VISTA,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.safari))
					driver = buildNewSafariProxyDriver(proxy,Platform.VISTA,browserVersion);
				//
				// Windows
				//
			}else if( platform.equals(Platform.WINDOWS)){
				
				if (browser.toLowerCase().contains(bichConstants.firefox))
					driver = buildNewFireFoxProxyDriver(proxy,Platform.WINDOWS,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.ie))
					driver = buildNewIEProxyDriver(proxy,Platform.WINDOWS,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.chrome))
					driver = buildNewChromeProxyDriver(proxy,Platform.WINDOWS,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.safari))
					driver = buildNewSafariProxyDriver(proxy,Platform.WINDOWS,browserVersion);
				//
				// Windows XP
				//
			}else if( platform.equals(Platform.XP)){
				
				if (browser.toLowerCase().contains(bichConstants.firefox))
					driver = buildNewFireFoxProxyDriver(proxy,Platform.XP,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.ie))
					driver = buildNewIEProxyDriver(proxy,Platform.XP,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.chrome))
					driver = buildNewChromeProxyDriver(proxy,Platform.XP,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.safari))
					driver = buildNewSafariProxyDriver(proxy,Platform.XP,browserVersion);
				
			}else if( platform.equals(Platform.EL_CAPITAN)){
				
				if (browser.toLowerCase().contains(bichConstants.firefox))
					driver = buildNewFireFoxProxyDriver(proxy,Platform.EL_CAPITAN,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.ie))
					driver = buildNewIEProxyDriver(proxy,Platform.EL_CAPITAN,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.chrome))
					driver = buildNewChromeProxyDriver(proxy,Platform.EL_CAPITAN,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.safari))
					driver = buildNewSafariProxyDriver(proxy,Platform.EL_CAPITAN,browserVersion);
				
			}else if( platform.equals(Platform.WIN8)){
				
				if (browser.toLowerCase().contains(bichConstants.firefox))
					driver = buildNewFireFoxProxyDriver(proxy,Platform.WIN8,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.ie))
					driver = buildNewIEProxyDriver(proxy,Platform.WIN8,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.chrome))
					driver = buildNewChromeProxyDriver(proxy,Platform.WIN8,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.safari))
					driver = buildNewSafariProxyDriver(proxy,Platform.WIN8,browserVersion);
				
			}else if( platform.equals(Platform.WIN10)){
				
				if (browser.toLowerCase().contains(bichConstants.firefox))
					driver = buildNewFireFoxProxyDriver(proxy,Platform.WIN10,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.ie))
					driver = buildNewIEProxyDriver(proxy,Platform.WIN10,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.chrome))
					driver = buildNewChromeProxyDriver(proxy,Platform.WIN10,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.safari))
					driver = buildNewSafariProxyDriver(proxy,Platform.WIN8,browserVersion);
				
			}else if( platform.equals(Platform.MAC)){
				
				if (browser.toLowerCase().contains(bichConstants.firefox))
					driver = buildNewFireFoxProxyDriver(proxy,Platform.MAC,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.ie))
					driver = buildNewIEProxyDriver(proxy,Platform.MAC,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.chrome))
					driver = buildNewChromeProxyDriver(proxy,Platform.MAC,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.safari))
					driver = buildNewSafariProxyDriver(proxy,Platform.MAC,browserVersion);
				//
				// Linux
				//
			}else if( platform.equals(Platform.LINUX)){
				
				if (browser.toLowerCase().contains(bichConstants.firefox))
					driver = buildNewFireFoxProxyDriver(proxy,Platform.LINUX,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.ie))
					driver = buildNewIEProxyDriver(proxy,Platform.LINUX,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.chrome))
					driver = buildNewChromeProxyDriver(proxy,Platform.LINUX,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.safari))
					driver = buildNewSafariProxyDriver(proxy,Platform.LINUX,browserVersion);
				//
				// None Specified
				//
			}else{// NO Proxy OS was specified in the XLS
				if (browser.toLowerCase().contains(bichConstants.firefox))
					driver = buildNewFireFoxProxyDriver(proxy,Platform.ANY,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.ie))
					driver = buildNewIEProxyDriver(proxy,Platform.ANY,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.chrome))
					driver = buildNewChromeProxyDriver(proxy,Platform.ANY,browserVersion);
				if (browser.toLowerCase().contains(bichConstants.safari))
					driver = buildNewSafariProxyDriver(proxy,Platform.ANY,browserVersion);
			}
			
			
		} else if (remote.toLowerCase().equals(bichConstants.localWebDriver)) {
			//
			// Local web Driver 
			//
		
			if (browser.toLowerCase().contains(bichConstants.firefox))
				driver = buildNewFireFoxLocalDriver();
			if (browser.toLowerCase().contains(bichConstants.ie))
				driver = buildNewIELocalDriver();
			if (browser.toLowerCase().contains(bichConstants.chrome))   
				driver = buildNewChromeLocalDriver();
			if (browser.toLowerCase().contains(bichConstants.safari))
				driver = buildNewSafariLocalDriver();
			if (browser.toLowerCase().contains(bichConstants.jbrowser))
				driver = ceateJBrowserDriver();
		}else if (remote.equals(bichConstants.browserStackWebDriver)) {
			driver = buildNewBrowserStackWebDriver(browserVersion, platform, osVersion,browser, setupString);
		}else{
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:createWebDriver:Info: "+"invalid remote setting, must define remote as: "+ bichConstants.localWebDriver +", "+bichConstants.browserStackWebDriver +", "+bichConstants.seleniumGridWebDriver +", or"+ bichConstants.sauceLabWebDriver);
			driver = null;
		}
		if(driver != null){
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}
		//
		// Setup the screen grabber
		//
		if(null != screencapture){
			screencapture.setCurrentWebDriver(driver);
			screencapture.setCurrentRunName(setupString);
		}
		return driver;

	}// createBrowserWebDrivers
	
	
	
	/**
	 * buildNewIELocalDriver()
	 * <br> This function sets up the IE local webDriver with the following capabilities: INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS
	 * <br> and IGNORE_ZOOM_SETTING
	 * @return RemoteWebDriver - new IE Driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	private RemoteWebDriver buildNewIELocalDriver() {

		DesiredCapabilities caps = DesiredCapabilities.internetExplorer();    
		
		caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
		caps.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING,true);
		caps.setCapability(InternetExplorerDriver.NATIVE_EVENTS,false);
		caps.setCapability(InternetExplorerDriver.UNEXPECTED_ALERT_BEHAVIOR, "accept");
		caps.setCapability("ignoreProtectedModeSettings", true);
		caps.setCapability("disable-popup-blocking", true);
		caps.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
		caps.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, browserStartPage);
		
		return (driver = new InternetExplorerDriver(caps));

	}// buildNewIELocalDriver
	/**
	 * method buildNewIELocalDriver()
	 * This function sets up the IE local webDriver.
	 * @param caps browser capabilities
	 * @return RemoteWebDriver - IELocalDriver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	public RemoteWebDriver buildNewIELocalDriver(DesiredCapabilities caps) {

		if(osInfo.getOSName().contains(bichConstants.windows)) {
			System.setProperty("webdriver.ie.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+winIEWebDriverDirectory+osInfo.fileSeperator()+"IEDriverServer.exe");
		}else if(osInfo.getOSName().contains(bichConstants.mac)) {
			System.setProperty("webdriver.ie.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+macIEWebDriverDirectory+osInfo.fileSeperator()+ "IEDriverServer");
		}else{
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewIELocalDriver: failed to create webDriver");
			return null;
		}
		return (driver = new InternetExplorerDriver(caps));

	}// buildNewIELocalDriver
	/**
	 * method buildNewSafariProxyDriver()
	 * This function setsup the selenium grid webdriver.
	 * @param proxyInfo - browser version
	 * @param os - OS to create
	 * @param browserVersion - version of the browser
	 * @return RemoteWebDriver Safari web Driver
	 * @see RemoteWebDriver
	 * @see Platform
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewSafariProxyDriver(String proxyInfo,Platform os, String browserVersion) {
		
		WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:buildNewSafariProxyDriver:Info: "+"Building "+os.name()+" Safari Web Driver for Selenium Grid");
		
		try {
			String proxyServer = "http://" + proxyInfo.trim()+ ":4444/wd/hub";
			DesiredCapabilities capabillities = DesiredCapabilities.safari();
			capabillities.setCapability("platform", os.name());
			capabillities.setCapability("name", methodName);
			driver = new RemoteWebDriver(new URL(proxyServer), capabillities);
		} catch (IOException e) {
			//
			// If the failover to local driver is true and the OS matches call local driver
			//
			if(failProxyToLocalWebDriver && osName.toLowerCase().contains(os.name())){
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewSafariProxyDriver:IOException: "+e.getMessage());
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewSafariProxyDriver:Info: "+"Proxy Failover to local Web driver");
				return  buildNewSafariLocalDriver();
			}else{
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewSafariProxyDriver: failed to create webDriver");
				return null;
			}
		}
		return (driver);
	}
	/**
	 * method buildNewIEProxyDriver()
	 * This function setsup the selenium grid webdriver.
	 * @param proxyInfo - browser version
	 * @param os - not used, default is linux
	 * @param browserVersion - version of the browser
	 * @return RemoteWebDriver IE proxy driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewIEProxyDriver(String proxyInfo,Platform os, String browserVersion) {
		
		System.out.println("Building "+os.name()+" Internet Explorer Web Driver for Selenium Grid");
		try {
			URL proxyServer = new URL("http://" + proxyInfo + ":4444/wd/hub");
			DesiredCapabilities capabillities = DesiredCapabilities.internetExplorer();
			capabillities.setCapability("platform", os.name());
			capabillities.setCapability("name", methodName);
			driver = new RemoteWebDriver(proxyServer, capabillities);
		} catch (IOException e) {
			//
			// If the failover to local driver is true and the OS matches call local driver
			//
			if(failProxyToLocalWebDriver && osName.toLowerCase().contains(os.name())){
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewIEProxyDriver:IOException: "+e.getMessage());
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewIEProxyDriver:Info: "+"Proxy Failover to local Web driver");
				return  buildNewIELocalDriver();
			}else{
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewIEProxyDriver: failed to create webDriver");
				return null;
			}
		}
		return (driver);
	} 
	/**
	 * buildNewSafariLocalDriver()
	 * <br> This function setsup local Safari webdriver
	 * @return RemoteWebDriver - Safari local driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	@SuppressWarnings("deprecation")
	private RemoteWebDriver buildNewSafariLocalDriver() {
		SafariOptions options = new SafariOptions();
		 options.useCleanSession(true); //if you wish safari to forget session everytime
		 
		return (driver = new SafariDriver(options));
	}
	/**
	 * buildNewSafariLocalDriver()
	 * <br> This function setsup local Safari webdriver
	 * @param options - Safari Options
	 * @return RemoteWebDriver - Safari local driver
	 * @see RemoteWebDriver
	 * @see SafariOptions
	 * @author DRamer
	 * @version 1.0
	 */
	public RemoteWebDriver buildNewSafariLocalDriver(SafariOptions options) {
		 
		return (driver = new SafariDriver(options));
	}
	/**
	 * buildNewFireFoxLocalDriver()
	 * <br> This function setsup local firefox webdriver
	 * @param options - Firefox Options
	 * @return RemoteWebDriver - FireFox local web driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 3.0
	 */

	public RemoteWebDriver buildNewFireFoxLocalDriver( FirefoxOptions options) {
		WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:buildNewFireFoxLocalDriver:Info: "+" Creating new driver");
		//
		// Logging is controlled by a properties file
		//
		if(remoteWebDriverLogging.equalsIgnoreCase("FALSE")){
		    options.setLogLevel(FirefoxDriverLogLevel.INFO);
		}else{
			 options.setLogLevel(FirefoxDriverLogLevel.DEBUG);
		}
	    driver = new FirefoxDriver(options);
		return (driver);
	}
	/**
	 * buildNewFireFoxLocalDriver()
	 * <br> This function setsup local firefox webdriver
	 * @return RemoteWebDriver - FireFox local web driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 3.0
	 */
	private RemoteWebDriver buildNewFireFoxLocalDriver() {
	
		WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:buildNewFireFoxLocalDriver:Info: "+" Creating new driver");
        
	
        FirefoxOptions options = new FirefoxOptions();
     
		
		if(remoteWebDriverLogging.equalsIgnoreCase("FALSE")){
		    options.setLogLevel(FirefoxDriverLogLevel.INFO);
		}else{
			 options.setLogLevel(FirefoxDriverLogLevel.DEBUG);
		}
	    driver = new FirefoxDriver(options);
		return (driver);
	}
	/**
	 * method buildNewFireFoxProxyDriver()
	 * This function setsup the selenium grid webdriver. If running on Ubuntu make sure you set export DISPLAY=:22
	 * @param proxyInfo - browser version
	 * @param os - windows, linux, mac
	 * @param browserVersion - version of the browser
	 * @return RemoteWebDriver - return FireFox proxy webdriver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewFireFoxProxyDriver(String proxyInfo,Platform os, String browserVersion) {
		System.out.println("Building "+os.name()+" Firefox Web Driver for Selenium Grid");
		try {
			String proxyServer = "http://" + proxyInfo.trim()+ ":4444/wd/hub";
			DesiredCapabilities capabillities = DesiredCapabilities.firefox();
			capabillities.setCapability("platform", os);
			capabillities.setCapability("name", methodName);
			//
			// Extra capabilities for download files
			//
			capabillities.setCapability("browser.download.folderList",2);
			capabillities.setCapability("browser.download.manager.showWhenStarting",false);
			capabillities.setCapability("browser.download.dir",osInfo.buildWorkingDirectoryPath(fileDownloadDirectory));
			capabillities.setCapability("browser.helperApps.neverAsk.saveToDisk","text/csv,application/vnd.ms-excel");
			capabillities.setCapability("browser.startup.homepage",browserStartPage);
			
			driver = new RemoteWebDriver(new URL(proxyServer), capabillities);
		} catch (IOException e) {
			//
			// If the failover to local driver is true and the OS matches call local driver
			//
			if(failProxyToLocalWebDriver && osName.toLowerCase().contains(os.name())){
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewFireFoxProxyDriver:IOException: "+e.getMessage());
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewFireFoxProxyDriver:Info: "+"Proxy Failover to local Web driver");
				return  buildNewFireFoxLocalDriver();
			}else{
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewFireFoxProxyDriver: failed to create Fire Fox proxy server");
				return null;
			}
		}
		return (driver);
	}
	/**
	 * method buildNewChromeLocalDriver()
	 * This function setsup the local chrome web driver.
	 * @return RemoteWebDriver - Chrome local Driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewChromeLocalDriver() {
		
		String osName = new String(osInfo.getOSName());
		
		WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:buildNewChromeLocalDriver:Info: "+" Creating new driver");
		
		if(osInfo.getOSName().toLowerCase().contains(bichConstants.windows)) {
			System.setProperty("webdriver.chrome.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+winChromeWebDriverDirectory+osInfo.fileSeperator()+"chromedriver.exe");
		}else if(osInfo.getOSName().toLowerCase().contains(bichConstants.mac)) {
			System.setProperty("webdriver.chrome.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+macChromeWebDriverDirectory+osInfo.fileSeperator()+ "chromedriver");
		}else if(osInfo.getOSName().toLowerCase().contains(bichConstants.linux)) {
			System.setProperty("webdriver.chrome.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+linuxChromeWebDriverDirectory+osInfo.fileSeperator()+ "chromedriver");
			WEBDRIVERLOGGER.enterInfoLog("system.setproperty webdriver.chrome.driver for linux");
		}else{
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewChromeLocalDriver: Windows,Mac, Linux os not found for chrome webDriver");
			return null; // can't create Chrome on Linux
		}
		
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", osInfo.buildWorkingDirectoryPath(fileDownloadDirectory));
		chromePrefs.put("session.startup_urls",Arrays.asList(browserStartPage));
		ChromeOptions options = new ChromeOptions();
		HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
		options.setExperimentalOption("prefs", chromePrefs);
		options.addArguments("--test-type");
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, options);
		
		return (driver = new ChromeDriver(/*cap*/));
	}
	/**
	 * method buildNewChromeLocalDriver()
	 * <br> This function setsup the local chrome web driver, with ChromeOptions passsed in
	 * @param options - ChromeOptions
	 * @return RemoteWebDriver - Chrome local Driver
	 * @see RemoteWebDriver
	 * @see ChromeOptions
	 * @author DRamer
	 * @version 1.0
	 */
	public RemoteWebDriver buildNewChromeLocalDriver(ChromeOptions options) {
		WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:buildNewChromeLocalDriver:Info: "+" Creating new driver with options");
		if(osInfo.getOSName().toLowerCase().contains(bichConstants.windows)) {
			System.setProperty("webdriver.chrome.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+winChromeWebDriverDirectory+osInfo.fileSeperator()+"chromedriver.exe");
			WEBDRIVERLOGGER.enterInfoLog("system.setproperty webdriver.chrome.driver for windows");
		}else if(osInfo.getOSName().toLowerCase().contains(bichConstants.mac)) {
			System.setProperty("webdriver.chrome.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+macChromeWebDriverDirectory+osInfo.fileSeperator()+ "chromedriver");
			WEBDRIVERLOGGER.enterInfoLog("system.setproperty webdriver.chrome.driver for mac");
		}else if(osInfo.getOSName().toLowerCase().contains(bichConstants.linux)) {
			
			if(is64bit){
				System.setProperty("webdriver.chrome.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+linux64ChromeWebDriverDirectory+osInfo.fileSeperator()+ "chromedriver");
				WEBDRIVERLOGGER.enterInfoLog("system.setproperty webdriver.chrome.driver for linux 64 bit");
			}else{
				System.setProperty("webdriver.chrome.driver", osInfo.getWebDriverDirectory()+osInfo.fileSeperator()+linuxChromeWebDriverDirectory+osInfo.fileSeperator()+ "chromedriver");
				WEBDRIVERLOGGER.enterInfoLog("system.setproperty webdriver.chrome.driver for linux");
				
				
			}
		}else{
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewChromeLocalDriver: Windows,Mac, Linux os not found for chrome webDriver");
			return null; // can't create Chrome on Linux
		}
		return this.driver = new ChromeDriver(options);
	}
	
	/**
	 * method buildNewChromeProxyDriver()
	 * This function setsup the selenium grid webdriver.
	 * @param proxyInfo - browser version
	 * @param os - Operating system to use when creating remote webDriver
	 * @param browserVersion - version of the browser
	 * @return RemoteWebDriver - return chrome proxy webdriver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewChromeProxyDriver(String proxyInfo,Platform os, String browserVersion) {
		
		
		WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:buildNewChromeProxyDriver:Info: "+"Building "+os.name()+" Chrome Web Driver for Selenium Grid");
		
		try {
			String proxyServer = "http://" + proxyInfo.trim()+ ":4444/wd/hub";
			if(os.name().equals("MAC")){
				System.setProperty("webdriver.chrome.driver","\\user\\downloads\\chromedriver");
			}
			DesiredCapabilities capabillities = DesiredCapabilities.chrome();
			capabillities.setCapability("platform", os.name());
			capabillities.setCapability("name", methodName);
			driver = new RemoteWebDriver(new URL(proxyServer), capabillities);
		} catch (IOException e) {
			//
			// If the failover to local driver is true and the OS matches call local driver
			//
			if(failProxyToLocalWebDriver && osName.toLowerCase().contains(os.name())){
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewChromeProxyDriver:IOException: "+e.getMessage());
				WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewChromeProxyDriver:Info: "+"Proxy Failover to local Web driver");
				return  buildNewChromeLocalDriver();
			}else{
				return null;
			}
		}
		return (driver);
	}
	/**
	 * method buildNewIERemoteDriver()
	 * This function setsup the saucelabs webdriver.
	 * @param platform - windows, mac, linux
	 * @param version - browser version
	 * @return RemoteWebDriver - IE remote web driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewIERemoteDriver(Platform platform, String version) {

		DesiredCapabilities capabillities = DesiredCapabilities.internetExplorer();
		capabillities.setCapability("platform", platform);
		capabillities.setCapability("version", version);
		capabillities.setCapability("name", methodName);
		try {
			this.driver = new RemoteWebDriver(new URL("http://" + sauceConnectLoginName
					+ ":" + sauceConnectAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
					capabillities);
		} catch (MalformedURLException e) {
			
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewIERemoteDriver:MalformedURLException: "+e.getMessage());
		}
		return driver;
	}
	/**
	 * method buildNewChromeRemoteDriver()
	 * This function setsup the saucelabs webdriver.
	 * @param platform - windows, mac, linux
	 * @param version - browser version
	 * @return RemoteWebDriver - Chrome remote web driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewChromeRemoteDriver(Platform platform,String version) {

		DesiredCapabilities capabillities = DesiredCapabilities.chrome();
		capabillities.setCapability("platform", platform);
		if (!version.toLowerCase().contains("none")) { // SauceLabs recommends
														// not to set the
														// browser version if on
														// MAC or Linux
			capabillities.setCapability("version", version);
		}
		capabillities.setCapability("name", methodName);
		try {
			this.driver = new RemoteWebDriver(new URL("http://" + sauceConnectLoginName
					+ ":" + sauceConnectAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
					capabillities);
		} catch (MalformedURLException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewChromeRemoteDriver:MalformedURLException: "+e.getMessage());
		}
		return driver;
	}
	/**
	 * method buildNewSafariRemoteDriver()
	 * This function setsup the saucelabs webdriver.
	 * @param platform - windows, mac,Linux
	 * @param version - browser version
	 * @return RemoteWebDriver - Safari remote web driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewSafariRemoteDriver(Platform platform,String version) {

		DesiredCapabilities capabillities = DesiredCapabilities.safari();
		capabillities.setCapability("platform", platform);
		if (!version.toLowerCase().contains("none")) { // SauceLabs recommends
														// not to set the
														// browser version if on
														// MAC or Linux
			capabillities.setCapability("version", version);
		}
		capabillities.setCapability("name", methodName);
		try {
			this.driver = new RemoteWebDriver(new URL("http://" + sauceConnectLoginName
					+ ":" + sauceConnectAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
					capabillities);
		} catch (MalformedURLException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewSafariRemoteDriver:MalformedURLException: "+e.getMessage());
		}
		return driver;
	}
	/**
	 * methodbuildNewIPadRemoteDriver()
	 * This function setsup the saucelabs webdriver.
	 * @param platform - must be mac
	 * @param version - browser version
	 * @return RemoteWebDriver - Ipad Remote webdriver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewIPadRemoteDriver(Platform platform,String version) {

		DesiredCapabilities capabillities = DesiredCapabilities.ipad();
		capabillities.setCapability("platform", platform);
		capabillities.setCapability("version", version);
		capabillities.setCapability("name", methodName);
		try {
			this.driver = new RemoteWebDriver(new URL("http://" + sauceConnectLoginName
					+ ":" + sauceConnectAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
					capabillities);
		} catch (MalformedURLException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewSafariRemoteDriver:MalformedURLException: "+e.getMessage());
		}
		return driver;
	}
	/**
	 * method buildNewIPhoneRemoteDriver()
	 * This function setsup the saucelabs webdriver.
	 * @param platform - must be mac
	 * @param version - browser version
	 * @return RemoteWebDriver - Iphone remote driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewIPhoneRemoteDriver(Platform platform,String version) {

		DesiredCapabilities capabillities = DesiredCapabilities.iphone();
		capabillities.setCapability("platform", platform/* "Mac 10.8" */);
		capabillities.setCapability("version", version /* "6" */);
		capabillities.setCapability("name", methodName);
		try {
			this.driver = new RemoteWebDriver(new URL("http://" + sauceConnectLoginName
					+ ":" + sauceConnectAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
					capabillities);
		} catch (MalformedURLException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewIPhoneRemoteDriver:MalformedURLException: "+e.getMessage());
		}
		return this.driver;
	}
	/**
	 * method buildNewFireFoxRemoteDriver()
	 * description This function setsup the saucelabs webdriver.
	 * @param platform - windows, linux, mac
	 * @param version - browser version
	 * @return RemoteWebDriver = FireFox remote web driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewFireFoxRemoteDriver(Platform platform,String version) {

		DesiredCapabilities capabillities = DesiredCapabilities.firefox();
		capabillities.setCapability("platform", platform);
		capabillities.setCapability("version", version);
		capabillities.setCapability("name", methodName);
		try {
			this.driver = new RemoteWebDriver(new URL("http://" + sauceConnectLoginName
					+ ":" + sauceConnectAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
					capabillities);
		} catch (MalformedURLException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewFireFoxRemoteDriver:MalformedURLException: "+e.getMessage());
		}
		return driver;
	}
	
	/**
	 * method buildNewIphoneRemoteDriver()
	 * This function setsup the saucelabs webdriver.
	 * @param platform - must be mac
	 * @param version - browser version
	 * @return RemoteWebDriver - Iphone remote web driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewIphoneRemoteDriver(String platform,String version) {

		DesiredCapabilities capabillities = DesiredCapabilities.iphone();
		capabillities.setCapability("platform", platform);
		capabillities.setCapability("version", version);
		capabillities.setCapability("name", methodName);
		try {
			this.driver = new RemoteWebDriver(new URL("http://" + sauceConnectLoginName
					+ ":" + sauceConnectAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
					capabillities);
		} catch (MalformedURLException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewIphoneRemoteDriver:MalformedURLException: "+e.getMessage());
		}
		return driver;
	}
	
	
	/**
	 * method buildNewPhantomRemoteDriver()
	 * This function setsup the phantomJS webdriver.
	 * @param platform - windows, mac, linux
	 * @param version - browser version
	 * @return RemoteWebDriver - returns PhantomJS remote webdriver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewPhantomRemoteDriver(String platform,String version) {

		@SuppressWarnings("deprecation")
		DesiredCapabilities capabillities = DesiredCapabilities.phantomjs();
		capabillities.setCapability("platform", platform/* "Mac 10.8" */);
		capabillities.setCapability("version", version /* "6" */);
		capabillities.setCapability("name", methodName);
		try {
			this.driver = new RemoteWebDriver(new URL("http://" + sauceConnectLoginName
					+ ":" + sauceConnectAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
					capabillities);
		} catch (MalformedURLException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewPhantomRemoteDriver:MalformedURLException: "+e.getMessage());
		}
		return driver;
	}
	/**
	 * method buildNewAndroidSauceConnectRemoteDriver()
	 * This function setsup the DesiredCapabilities for the saucelab web driver
	 * @param capabillities - browser capabilities
	 * @return AndroidDriver - android webdriver
	 * @see AndroidDriver
	 * @author DRamer
	 * @version 1.0
	 */
	public AndroidDriver<MobileElement> buildNewAndroidSauceConnectRemoteDriver(DesiredCapabilities capabillities) {

	
		try {
			androidDriver = new AndroidDriver<MobileElement>(new URL("http://" + sauceConnectLoginName
					+ ":" + sauceConnectAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
					capabillities);
		} catch (MalformedURLException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewAndroidSauceConnectRemoteDriver:MalformedURLException: "+e.getMessage());
		}
		return androidDriver;
	}
	/**
	 * method buildNewBrowserStackWebDriver()
	 * This function setsup the DesiredCapabilities for the browserStack cloud.
	 * See: https://www.browserstack.com
	 * @param BrowserVersion - browser version
	 * @param platform - WINDOWS, MAC, LINUX
	 * @param osVersion - string container Os version Windows(7,8,8.1,10) MAC (os X), LINUX not used
	 * @param browser - type of browser IE,FireFox,Chrome
	 * @param setupString - data about the test
	 * @return RemoteWebDriver - BrowserStack web driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewBrowserStackWebDriver(String BrowserVersion, Platform platform, String osVersion, String browser, String setupString){
		
		String browserStack = String.format(browserStackURL,browserStackLoginName, browserStackKey);
				
	
		// Windows: XP, 7, 8 and 8.1 
		// OS X: Snow Leopard, Lion, Mountain Lion, Mavericks, Yosemite, El Capitan
		
		DesiredCapabilities capabillities = new DesiredCapabilities();
		capabillities.setBrowserName(browser);
		capabillities.setCapability("browser", browser);
		capabillities.setCapability("browser_version", BrowserVersion);
		if(platform.toString().contains("WINDOWS")){
			capabillities.setCapability("os", "Windows");
			capabillities.setCapability("os_version",osVersion );
		}else if(platform.toString().contains("linux")){
			capabillities.setCapability("os", "LINUX");
		}else{
			capabillities.setCapability("os", "MAC");
			capabillities.setCapability("os_version",osVersion );
		}
		capabillities.setCapability("name", methodName);
		capabillities.setCapability("browserstack.debug", "true");
		
		
	
		try {
			driver = new RemoteWebDriver(new URL(browserStack), capabillities);
		} catch (MalformedURLException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewBrowserStackWebDriver: browser Stack URL error: "+browserStack);
			e.printStackTrace();
		}
		return driver;
	}// buildNewBrowserStackWebDriver
	
	/**
	 * method buildNewConfiguredRemoteDriver()
	 * This function setsup the DesiredCapabilities for the saucelab web driver
	 * @param browser - browser name
	 * @param browserVersion - browser version
	 * @param platformOS - platform (Mac, Windows, linux)
	 * @return RemoteWebDriver -configured remote Driver
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	private RemoteWebDriver buildNewConfiguredRemoteDriver(String browser,String browserVersion, String platformOS) {

		DesiredCapabilities capabillities = new DesiredCapabilities();
		capabillities.setBrowserName(browser);
		capabillities.setCapability("version", browserVersion);
		capabillities.setCapability("platform", Platform.valueOf(platformOS));
		capabillities.setCapability("name", methodName);
		try {
			this.driver = new RemoteWebDriver(new URL("http://" + sauceConnectLoginName
					+ ":" + sauceConnectAccessKey + "@ondemand.saucelabs.com:80/wd/hub"),
					capabillities);
		} catch (MalformedURLException e) {
			WEBDRIVERLOGGER.enterSevereLog("sTestWebDriverFactory:buildNewConfiguredRemoteDriver:MalformedURLException: "+e.getMessage());
		}
		return driver;
	}
	/**
	 * method cleanWebDriver()
	 * reset the page DOM for all pages. The base webDriverFactory has no pages to clean
	 * @author dramer
	 * @version 3.0
	 */ 
	public abstract void cleanWebDriver();//cleanWebDriver
	
	/**
	 * method startTest()
	 * startTest simply
	 * @param baseURL starting URL
	 * @return long - start time
	 */
	public long startTestTimer(String baseURL){
		stopWatchStart();
		driver.get(baseURL);
		return(stopWatchStop(baseURL));
	}
	
	/**
	 * method selfTestBrowser()
	 * selfTest, used to validate all aspects of the sTestWebDriverFactory
	 * @param browserName name of the browser to test
	 * @param testNumber sequential number for selfTest
	 */
	private void selfTestBrowser(String browserName,String testNumber){
		
		if (driver != null) {
			WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:selfTestWindows:Info: "+"Starting local web driver test: "+browserName);
			driver.get("http://www.bichromate.org");
			 //
			 // Will only wait 2 minutes if the page takes that long to load. If the page 
			 // loads in 5 seconds then the wait continues
			 //
			 driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		
			if(driver.getCurrentUrl().contains("http://www.bichromate.org")){
				WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:selfTestWindows:Info: "+"Test"+ testNumber +" passed: Create local web driver: "+browserName);
			}else{
				WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:selfTestWindows:Info: "+"Test "+ testNumber +" Failed: Local web driver no created: "+browserName);
			}
			System.out.println("selfTestBrowser Passed: "+browserName);
			driver.quit();
			driver = null;
			
		} else {
			WEBDRIVERLOGGER.enterInfoLog("sTestWebDriverFactory:selfTestWindows:Info: "+"Test "+ testNumber +" Failed: Local web driver no created:  "+browserName);
		}
	}
	/**
	 * method selfTestWindows()
	 * Unit Test for windows platform
	 */
	private void selfTestWindows(){
		String osVersion = new String("7");
		
		//
		// Testing Local Drivers windows Firefox
		//
		driver = createBrowserWebDriver(bichConstants.localWebDriver, "", Platform.WINDOWS, osVersion,bichConstants.firefox,"sTest Self Test - local FireFix web driver");
		driver.setLogLevel(Level.OFF);
		selfTestBrowser("firefox","1");
		//
		// Locak Chrome
		//
		driver = createBrowserWebDriver(bichConstants.localWebDriver, "", Platform.WINDOWS,osVersion, bichConstants.chrome,"sTest Self Test - local Chrome web driver");
		selfTestBrowser("chrome","2");
		//
		// Local IE
		//
		driver = createBrowserWebDriver(bichConstants.localWebDriver, "", Platform.WINDOWS,osVersion, bichConstants.ie,"sTest Self Test - local IE web driver");
		selfTestBrowser("ie","3");
		//
		// JBrowser
		//
		//driver = createBrowserWebDriver(bichConstants.localWebDriver, "", Platform.WINDOWS, osVersion, bichConstants.jbrowser,"sTest Self Test - local jBrowser web driver");
		//selfTestBrowser("jbrowser","4");
		//
		// Browser Stack
		//
		driver = createBrowserWebDriver(bichConstants.browserStackWebDriver, "58", Platform.WINDOWS, osVersion,bichConstants.firefox,"sTest BrowserStack - Browser Stack FireFox ");
		selfTestBrowser("BrowserStack WebDriver","5");
		//
		// Selenium Grid
		//
		driver = createBrowserWebDriver(bichConstants.seleniumGridWebDriver, "58", Platform.WINDOWS, osVersion,"fireFox","sTest Self Test - Selenium grid with Firefox");
		selfTestBrowser("Selenium Grid Windows","7");
		//
		// Net Panel webdriver to track page load times for Ajax
		//
		driver = createFireFoxNetPanelWebDriver();
		selfTestBrowser("firefox with NetPanel and Firebug","8");
		//
		// SauceLabs
		//
		driver = createBrowserWebDriver(bichConstants.sauceLabWebDriver, "58", Platform.WINDOWS, osVersion,"fireFox","sTest Self Test - SauceLabs with Firefox");
		selfTestBrowser("SauceLabs Windows","9");
		
	}// selfTestWindows
	/**
	 * method selfTestMac()
	 * unit test for mac platform
	 */
	private void selfTestMac(){
		String osVersion = new String("os_X");
		
		
		driver = createBrowserWebDriver(bichConstants.localWebDriver, "",  Platform.MAC, osVersion,bichConstants.firefox,"sTest Self Test Mac - local FireFix web driver");
		selfTestBrowser("firefox","1");
		driver = createBrowserWebDriver(bichConstants.localWebDriver, "", Platform.MAC, osVersion,bichConstants.chrome,"sTest Self Test Mac - local Chrome web driver");
		selfTestBrowser("chrome","2");
		driver = createBrowserWebDriver(bichConstants.localWebDriver, "", Platform.MAC, osVersion,bichConstants.safari,"sTest Self Test Mac - local Safari web driver");
		selfTestBrowser("Safari","3");
		
	}// selfTestMac
	
	/**
	 * method selfTestLinux()
	 * unit test for mac platform
	 */
	private void selfTestLinux(){

		
		//
		// Testing Local Drivers
		//
		driver = createBrowserWebDriver(bichConstants.localWebDriver, "", Platform.LINUX, "",bichConstants.chrome,"sTest Self Test Linux - local Chrome web driver");
		selfTestBrowser("Chrome","1");
		driver = createBrowserWebDriver(bichConstants.localWebDriver, "", Platform.LINUX, "",bichConstants.firefox,"sTest Self Test Linux - local FireFox web driver");
		selfTestBrowser("FireFox","2");
		
	}// selfTestMac
	/**
	 * method selfTest()
	 * base selfTest that calls the appropriate test for the current running OS
	 */
	public void selfTest(){
		WEBDRIVERLOGGER.setLogToFile();
		
		if(osInfo.getOSName().toLowerCase().contains(bichConstants.mac)){
			WEBDRIVERLOGGER.enterInfoLog("selfTtestMac being called");
			selfTestMac();
		} else if(osInfo.getOSName().toLowerCase().contains(bichConstants.windows)) {
			WEBDRIVERLOGGER.enterInfoLog("selfTtestWindows being called");
			selfTestWindows();
		}else if(osInfo.getOSName().toLowerCase().contains(bichConstants.linux)) {
			WEBDRIVERLOGGER.enterInfoLog("selfTtestLinux being called");
			selfTestLinux();
		}else{
			
		}
		
		//
		// Test remote browsers
		//
		
		
		//
		// Test proxy servers
		//
		
		
	}//selfTest
	
	//
	// Inner class for testing on the command line
	//
	public static class Test {
		public static void main(final String[] args){
			
			
				System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog"); 
				System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "warn");	
		 
			
			
			
			//sTestWebDriverFactory slwd = new sTestWebDriverFactory();
			//if(null != slwd)
				//slwd.selfTest();
			
		}// main
	}// Test
	
}// eTestWebDriverFactory
