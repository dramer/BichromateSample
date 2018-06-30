package bichromate.winium;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.winium.WiniumDriver;

import bichromate.core.sTestHilitePageElementFactory;
import bichromate.core.sTestScreenCaptureFactory;



//import org.openqa.selenium.winium.WiniumDriver;

public class sTestTruckPlus {

	  private WiniumDriver app;
	  private sTestReasonCode rc = null;
	  private sTestScreenCaptureFactory sc = null;
	  private sTestHilitePageElementFactory he = null;
	
	  public sTestTruckPlus(WiniumDriver app) {
	      this.app = app;
	      sc = new sTestScreenCaptureFactory();
	      he = new sTestHilitePageElementFactory();
	  }
	  public sTestHilitePageElementFactory getHiliteElementFactory(){
		  return he;
	  }
	 public sTestScreenCaptureFactory getScreenCaptureFactory(){
		 sc.setCurrentRunName("SampleTruckPlus");
		 sc.setCurrentWebDriver(app);
		 return sc;
	 }
	  public boolean clickMenuItem(String menuName){
		  
		  WebElement e = app.findElementByName(String.valueOf(menuName));
		  if(null != e){
				  e.click();
				  return true;
		  }else{
			  return false;
		  }
		  
	  }//clickMenuItem
	  public boolean clickCloseButton(){
		  
		  WebElement e = app.findElementByName("Close");
		  if(null != e){
				  e.click();
				  return true;
		  }else{
			  return false;
		  }
		  
	  }//clickCloseButton
	  public boolean clickMinimizeButton(){
		  
		  WebElement e = app.findElementByName("Minimize");
		  if(null != e){
				  e.click();
				  return true;
		  }else{
			  return false;
		  }
		  
	  }//clickCloseButton
 public boolean clickMaxinimizeButton(){
		  
		  WebElement e = app.findElementByName("Maximize");
		  if(null != e){
				  e.click();
				  return true;
		  }else{
			  return false;
		  }
		  
	  }//clickCloseButton
	  
	  public sTestReasonCode getReasonCode(){
		  if(null == rc){
			  rc = new sTestReasonCode(app);
		  }
		  return rc;
		  
	  }
}//sTestTruckPlus
