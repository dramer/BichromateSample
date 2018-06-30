/*
 * sTestScrollIntoViewFactory.java	1.0 2013/01/23
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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import bichromate.sample.sampleWebDriverFactory;




/**
 * This method demonstrates scrollIntoView().
 * <br>This class is used to scroll web elements into view.
 * <br>
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("unused")
public class sTestScrollIntoViewFactory {
	private static final bichromateConstants bichConstants = new bichromateConstants();
	
	WebDriver driver = null;
	WebElement element = null;
	By byElement = null;

	/**
	 * This method demonstrates scrollElementIntoView().
	 * <br>This class is used to scroll vertically web elements into view.
	 * <br>
	 * @param drvr  WebDriver
	 * @param elmt WebElement
	 * @return boolean - true if scrollintoview found the element in view
	 * @deprecated
	 */
	public boolean scrollElementIntoView(WebDriver drvr, WebElement elmt){
		this.driver = drvr;
		this.element = elmt;
		int scrollAmount = 200;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int second = 0;; second++) {
            if(second >=60){
                break;
            }
                
            	js.executeScript("window.scrollBy(0,"+scrollAmount+")", "");
            	scrollAmount +=200;
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
                if(element.isDisplayed())break;
            }
		if(element.isDisplayed()) return true;
		return false;
	}// scrollIntoViewFactory
	/**
	 * This method demonstrates scrollVerticalElementIntoView().
	 * <br>This class is used to scroll vertically.
	 * <br>
	 * @param drvr  WebDriver
	 * @param pixels - offset by number of pixels
	 */
	public void scrollVerticalElementIntoView(WebDriver drvr, int pixels){
		this.driver = drvr;
		JavascriptExecutor js = (JavascriptExecutor) driver;  
        if(null != js) js.executeScript("window.scrollBy(0,"+pixels+")", "");   
	}// scrollVerticalElementIntoView
	/**
	 * This method demonstrates scrollVerticalElementIntoView().
	 * <br>This class is used to scroll vertically.
	 * <br>
	 * @param drvr  WebDriver
	 * @param pixels - pixels to offset by
	 */
	public void scrollHorizontallyElementIntoView(WebDriver drvr, int pixels){
		this.driver = drvr;
		JavascriptExecutor js = (JavascriptExecutor) driver;  
        if(null != js) js.executeScript("window.scrollBy("+pixels+",0)", "");
	}// scrollHorizontallyElementIntoView
	/**
	 * This method demonstrates scrollVerticalElementIntoView().
	 * <br>This class is used to scroll vertically web elements into view.
	 * <br>
	 * @param drvr  WebDriver
	 * @param elmt WebElement
	 * @return boolean - if element was scrolled into view
	 */
	public boolean scrollVerticalElementIntoView(WebDriver drvr, WebElement elmt){
		this.driver = drvr;
		this.element = elmt;
		WebDriverWait wait = new WebDriverWait(driver,3);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for (int second = 0;; second++) {
            if(second >=60){
                break;
            }
                
            	js.executeScript("window.scrollBy(0,200)", "");
            	//
            	// Check the element 
            	//
            	try{
            		wait.until(ExpectedConditions.visibilityOf(element));
            		
            		return true;
            		
            	}catch (TimeoutException te){
            		// element not found scroll again
         
            	}
        }//Scroll Count
		
		//
    	// Check the element 
    	//
		try{
    		wait.until(ExpectedConditions.visibilityOf(element));
    		return true;
    		
    	}catch (TimeoutException te){
    		// element not found and at end of scrolling
    		return false;
    	}
		
        
	}// scrollVerticalElementIntoView
	
	/**
	 * This method demonstrates scrollHorizontallyElementIntoView().
	 * <br>This class is used to scroll horizontally web elements into view.
	 * <br>
	 * @param drvr  WebDriver
	 * @param elem WebElement
	 * @return boolean - true if element scrolled into view
	 */
	@SuppressWarnings("deprecation")
	public boolean scrollHorizontallyElementIntoView(WebDriver drvr, WebElement elem){
		this.driver = drvr;
		this.element = elem;
		
		WebDriverWait wait = new WebDriverWait(driver,3);
		wait.withTimeout(60, TimeUnit.SECONDS);
		wait.pollingEvery(5, TimeUnit.SECONDS);
		wait.ignoring(NoSuchElementException.class);
		
		JavascriptExecutor js = (JavascriptExecutor) driver;
		//
		// Scroll the page Left
		for (int scrollCount = 0;; scrollCount++) {
            if(scrollCount >=60){
                break;
            }
                //
            	// Scroll window left 200 pixels
            	//
            	js.executeScript("window.scrollBy(200,0)", "");
            	
            	//
            	// Check the element 
            	//
            	try{
            		wait.until(ExpectedConditions.visibilityOf(element));
            		return true;
        
            	}catch (TimeoutException te){
            		// element not found scroll again
         
            	}
        }//Scroll Count
		//
    	// Check the element 
    	//
		try{
			wait.until(ExpectedConditions.visibilityOf(element));
    		return true;
    		
    	}catch (TimeoutException te){
    		// element not found and at end of scrolling
    		return false;
    	}
        
	}// scrollVerticalElementIntoView
	
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args)
    	{
			ResourceBundle resources = null;
			sTestOSInformationFactory	path = new sTestOSInformationFactory();
			WebDriver driver = null;
			 try {
				
				 resources = ResourceBundle.getBundle("common.Bichromate", Locale.getDefault());
				} catch (MissingResourceException mre) {
					System.out.println("Bichromate.properties not found: "+ mre);
					System.exit(0);
				} 
			 
			 
			sTestWebDriverFactory slwd = new sampleWebDriverFactory(resources);
			if(path.getOSName().toLowerCase().contains(bichConstants.windows)) {
				driver = slwd.createBrowserWebDriver(bichConstants.localWebDriver, "",Platform.WINDOWS, "7",bichConstants.firefox,"sTest Self Test - local FireFix web driver");
			}else{
				driver = slwd.createBrowserWebDriver(bichConstants.localWebDriver, "",Platform.MAC,"7", bichConstants.firefox,"sTest Self Test - local FireFix web driver");
			}
			processPage(driver,slwd);
			
    	}
		private static void processPage(WebDriver drvr,sTestWebDriverFactory slwd){
			WebDriver driver = drvr;
					
			System.out.println("Testing the scroll into view code");
			 
			 //
			 // Test the landing page code
			 //
			sTestScrollIntoViewFactory siv = new sTestScrollIntoViewFactory();
			
			 driver.get("http://www.amazon.com");
			
			 //
			 // Will only wait 2 minutes if the page takes that long to load. If the page 
			 // loads in 5 seconds then the wait continues
			 //
			 driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
			
			 //
			 // This is the API Center link at the bottom of the page
			 //
			 WebElement element = driver.findElement(By.xpath("//a[contains(text(),'Conditions of Use')]"));
			 if(null != element){
				if(siv.scrollVerticalElementIntoView(driver, element)){
					if(element.isDisplayed()){
						System.out.println("scrollVerticalElementIntoView Test Passed");
					}else{
						System.out.println("scrollVerticalElementIntoView Test Failed");
					}
					 //
					 // Will only wait 2 minutes if the page takes that long to load. If the page 
					 // loads in 5 seconds then the wait continues
					 //
					 driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
				}else{
					System.out.println("scrollVerticalElementIntoView Test Failed");
				}
			 }else{
				 System.out.println("scrollVerticalElementIntoView failed to find element in the DOM");
			 }
			
			 //
			 // test horizontal scroll
			 //
			drvr.manage().window().setSize(new Dimension(400,780));
			 //
			 // This is the API Center link at the bottom of the page
			 //
			WebElement elementH = driver.findElement(By.xpath("//a[@id='nav-cart']/span[2]"));
			//
			// was the element found in the DOM?
			//
			if(null != elementH){
				if(siv.scrollHorizontallyElementIntoView(driver, elementH)){
					System.out.println("scrollVerticalElementIntoView Test Passed");
				}else{
					System.out.println("scrollVerticalElementIntoView Test Failed");
				}
				 //
				 // Will only wait 2 minutes if the page takes that long to load. If the page 
				 // loads in 5 seconds then the wait continues
				 //
				 driver.manage().timeouts().implicitlyWait(2, TimeUnit.MINUTES);
			}else{
				 System.out.println("scrollHorizontallyElementIntoView failed to find element in the DOM");
			 }
			driver.close();
			driver.quit();
			
    	}
	}
}

