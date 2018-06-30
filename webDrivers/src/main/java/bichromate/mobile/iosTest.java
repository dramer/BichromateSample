package bichromate.mobile;


import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.MobileElement;

import io.appium.java_client.ios.IOSDriver;

public class iosTest {
	
	IOSDriver <MobileElement> driver = null;
	
	public iosTest(){
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", "Safari");   //Leave blank if automating an App
		capabilities.setCapability("platformVersion", "10.2.1"); 
		capabilities.setCapability("deviceName","iPhone");
		capabilities.setCapability("platformName","ios");
		capabilities.setCapability("udid", "72b06c89fc9b14be4d627cb7bd764e9c07521b7d");
		capabilities.setCapability("newCommandTimeout","1200");
		capabilities.setCapability("version", "10.2.1");
		capabilities.setCapability("automationName","XCUITest");
		
	
	   try {
		driver = new IOSDriver<MobileElement>(new URL("http://127.0.0.1:4736/wd/hub"), capabilities);
	   } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	}//androidTest
	
	public void runSample(){
		if(null != driver){
			try{
				
			}catch(Exception e){
				e.printStackTrace();
			}
				
		   
		   driver.quit();
		}
	}

	
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public  static void main(final String[] args){
	 		iosTest at = new iosTest();
	 		
	 		if(null != at){
	 			at.runSample();
	 		}
	 		
	 		
	 	}//main
	 	
	 }//Test
	
	
}//iosTest


