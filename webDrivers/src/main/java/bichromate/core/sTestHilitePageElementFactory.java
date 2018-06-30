/*
 * sTestHilitePageElementFactory.java	1.0 2013/01/23
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
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;



import bichromate.core.sTestOSInformationFactory;
import bichromate.sample.sampleWebDriverFactory;


/**
 * This class Demonstrates sTestHilitePageElementFactory().
 * <br> hilite element uses java script to draw a rectangle around the element 
 * <br>
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("unused")
public class sTestHilitePageElementFactory {
	
	private static final bichromateConstants bichConstants = new bichromateConstants();
	
	
	/**
	 * This method Demonstrates unHiLiteElement().
	 * <br> unHiLiteElement  removes the hilite around the element 
	 * <br>
	 * @param driver  webDriver
	 * @param element  element to unhilite
	 * @param color - color to unhilite
	 * @see WebDriver
	 * @see WebElement
	 */
	public void unHiLiteElement(WebDriver driver, WebElement element, String color) {
		   
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.backgroundColor='"+color+"'",element);
	      
	}
	/**
	 * This method Demonstrates hiLiteElement().
	 * <br> unHiLiteElement  removes the hilite around the element 
	 * <br>
	 * @param driver  webDriver
	 * @param element element to unhilite
	 * @param color to hilite use redHilite = "red", yellowHilite = "yellow", greenHilite = "green", blackHilite = "black"
	 * @see WebDriver
	 * @see WebElement
	 */
	public void hiLiteElement(WebDriver driver, WebElement element,String color) {
	   
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.backgroundColor='"+color+"'",element);
	      
	}
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
			
			sTestWebDriverFactory slwd = new sampleWebDriverFactory(resources);
			WebDriver driver = slwd.createBrowserWebDriver(bichConstants.localWebDriver, "",Platform.WINDOWS,"7", bichConstants.firefox,"sTest Self Test - local FireFix web driver");
			//
			// Test the landing page code
			//
			sTestScrollIntoViewFactory siv = new sTestScrollIntoViewFactory();
			sTestHilitePageElementFactory helem = new sTestHilitePageElementFactory();
			
			driver.get("http://www.google.com");
			 
			 //
			 // Will only wait 2 minutes if the page takes that long to load. If the page 
			 // loads in 5 seconds then the wait continues
			 //
			driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
			
			//
			// This is the API Center link at the bottom of the page
			//
			WebElement element = driver.findElement(By.id("lst-ib"));
			 String bgcolor  = element.getCssValue("backgroundColor");
			if(element !=null){
				if(siv.scrollVerticalElementIntoView(driver, element)){
					helem.hiLiteElement(driver, element,bichConstants.redHilite);
					System.out.println("highlight Test Passed");
				}else{
					System.out.println("highlight Test Failed");
				}
			}
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//
			// set the background back
			//
			helem.unHiLiteElement(driver, element,bgcolor);
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			driver.close();
			driver.quit();
    	}// main
	}// Test
} //eTestHilitePageElementFactory
