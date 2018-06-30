package bichromate.winium;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;

import bichromate.core.sTestOSInformationFactory;
import bichromate.sample.pageDeclaration.sTestCalculatorStandardView;


public class sTestCalculatorApp {
	
	protected static ResourceBundle resources;
	//
	// Used for building Winium web driver
	//
	private String WINIUM_PATH = "http://localhost:9999";
	private sTestOSInformationFactory osInfo = null;
	private WiniumDriver driver = null;
	private DesktopOptions options;
    
    private static Runtime rt = null;
    private sTestCalculatorStandardView stanView = null;
    
    
    static {
		try {
			resources = ResourceBundle.getBundle("winium.sTestCalculatorApp", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestCalculatorApp.properties not found: "+ mre);
			System.exit(0);
		}
	}
    
    
    public sTestCalculatorApp(){
    	//
    	// setup the OSInformation factory
    	//
    	osInfo = new sTestOSInformationFactory();
    	//
    	// Launch the WINIUM SERVER FIRST
    	//
    	rt = Runtime.getRuntime();
    	try {
			rt.exec(osInfo.getEXEDirectory()+"Winium.Desktop.Driver.exe", null, new File(osInfo.getEXEDirectory()));
			 System.out.println("Calculator Application running thru Winium Driver" );
	    	 //
	    	 //
	    	 //
    		if(!osInfo.getOSName().toLowerCase().contains("windows")){
        		System.out.println("Calculator Application only runs on Windows" );
        		return ;
        	}
        	 options = new DesktopOptions();
             options.setApplicationPath(new String(resources.getString("sTestCalculatorApp.appPath")));
             driver = new WiniumDriver(new URL(WINIUM_PATH), options);
             
             wait(5000);
	             
		} catch (IOException e) {
			System.out.println("Failed to start the Calculator Application" );
		}
    	
    }// sTestCalculatorApp
    
    
    public sTestCalculatorStandardView getStandardView(){
    	
    	if(null == stanView){
    		stanView = new sTestCalculatorStandardView(driver);
    	}
    	
    	
    	return stanView;
    }
   
    public void setCalculatorView(String view){
    	
    	driver.findElementByName("View").click();
    	wait(5000);
    	driver.findElementByName(view).click();
    	wait(5000);
    	
    }//setCalculatorView
    
    public void sTestCalculatorAppStop() throws IOException {

    	driver.close();
    	driver.quit();
       
        System.out.println("Calculator Application closed" );
        if(null != rt)
        	rt.exec("taskkill /F /IM TRKPlusClient_QA.application");  
        
        driver = null;
    }
    private void selfTest(){
    	
    	setCalculatorView("Standard");
    	wait(5000);
    	sTestCalculatorStandardView myStanView = getStandardView();
    	if(5 == myStanView.add(2, 3)){
    		System.out.println("Calculator is working great" );
    	}
    	
    	try {
			sTestCalculatorAppStop();
		} catch (IOException e) {
			System.out.println("Failed to stop the Calculator Application" );
		}
    }

    private void wait(int seconds){
    	
    	try {

			//sleep 5 seconds
			Thread.sleep(seconds);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	
    }
   
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public  static void main(final String[] args){
	 	
	 		sTestCalculatorApp calc = new sTestCalculatorApp();
	 		System.out.println("Calculator Application Started" );
	 		if(null != calc){
	 			calc.selfTest();
	 			
	 		}
	 	}
	 }//Test
	 
	 
}// sTestCalculatorAppStart
