package bichromate.mobile;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class androidTest {
	
	AndroidDriver <MobileElement> driver = null;
	
	public androidTest(){
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("browserName", "");   //Leave blank if automating an App
		capabilities.setCapability("platformVersion", "7.0"); 
		capabilities.setCapability("deviceName","Galaxy S8");
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("udid", "9887a83531485a4343");
		capabilities.setCapability("newCommandTimeout","1200");
		capabilities.setCapability("appPackage", "com.sec.android.app.popupcalculator");
		capabilities.setCapability("appActivity","com.sec.android.app.popupcalculator.Calculator"); 
	
	   try {
		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
	   } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	   }
	}//androidTest
	
	public void runSample(){
		if(null != driver){
			try{
				driver.findElementById("2").click();
				
				
				//driver.findElementById("+").click();
				driver.findElementByXPath("//android.widget.Button[@text='+']").click();
				
				driver.findElementById("4").click();
				
				// driver.findElementById("=").click();
				driver.findElementByXPath("//android.widget.Button[@text='=']").click();
				
				//driver.findElementById("EditText").click();
			
				
				MobileElement results=driver.findElementByClassName("android.widget.EditText");
				String resultString = new String(results.getText());
				
				//Check the calculated value on the edit box
				assert results.getText().equals("6"):"Actual value is : "+results.getText()+" did not match with expected value: 6";
				System.out.println("The final output should be 6: "+ resultString);
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
	 		androidTest at = new androidTest();
	 		
	 		if(null != at){
	 			at.runSample();
	 		}
	 		
	 		
	 	}//main
	 	
	 }//Test
	
	
}//androidTest


