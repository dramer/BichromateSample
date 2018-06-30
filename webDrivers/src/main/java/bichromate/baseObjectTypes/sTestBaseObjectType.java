/*
 * sTestBaseObjectType.java	1.0 2017/06/01
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
package bichromate.baseObjectTypes;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import bichromate.core.pageObjectModelLogFactory;


public class sTestBaseObjectType {
	protected WebDriver myDriver = null;
	protected pageObjectModelLogFactory myPOMLogger = null;
	protected WebElement element = null;
	protected String pageObjectName = null;
	
	public sTestBaseObjectType(){ 
		
	}
	
	/**
	 * This method demonstrates isEnabled().
	 * <br>verify the search button is enabled 
	 * <br>  
	 * @return boolean - true if element is enabled
	 * @author dramer
	 * @version 3.0
	 */
	public boolean IsEnabled(){
		try{
			if(null != element){
				if(element.isEnabled()){
					return true;
				}else{
					return false;
				}
			}else{
				myPOMLogger.enterSevereLog(pageObjectName +" : IsEnabled element not found ");
			}
		}catch (Exception e){
			myPOMLogger.enterSevereLog(pageObjectName +" : IsEnabled element not found ");
		}
		return false;
	}//sTestButtonObjectEnabled
	
	/**
	 * This method demonstrates sTestButtonObjectDisplayed().
	 * <br>verify the search button is displayed
	 * <br>  
	 * @return boolean - true if element is displayed
	 * @author davidwramer
	 * @version 3.0
	 */
	public boolean IsDisplayed() {

		try{
			
			if(null != element){
				if(element.isDisplayed()){
					return true;
				}else{
					return false;
				}
			}else{
				myPOMLogger.enterSevereLog(pageObjectName +" : IsDisplayed element not found ");
			}
		}catch (Exception e){
			myPOMLogger.enterSevereLog(pageObjectName +" : IsDisplayed element not found ");
		}
		return false;
	}//IsDisplayed
	
	/**
	 * This method demonstrates IsSelected().
	 * <br>verify the search button is displayed
	 * <br>  
	 * @return boolean - true if element is selected
	 * @author dramer
	 * @version 3.0
	 */
	public boolean IsSelected() {

		try{
			
			if(null != element){
				if(element.isSelected()){
					return true;
				}else{
					return false;
				}
			}else{
				myPOMLogger.enterSevereLog(pageObjectName +" : IsSelected element not found ");
			}
		}catch (Exception e){
			myPOMLogger.enterSevereLog(pageObjectName +" : IsSelected element not found ");
		}
		return false;
	}//IsSelected
	
	
	/**
	 * This method demonstrates GetTagName().
	 * <br>return the tagName of the element
	 * <br>
	 * @return String tagName  
	 * @author dramer
	 * @version 3.0
	 */
	public String GetTagName() {
		String tagName = null;
		try{
			
			if(null != element){
				
					tagName = new String(element.getTagName());
			}else{
				myPOMLogger.enterSevereLog(pageObjectName +" : IsSelected element not found ");
			}
		}catch (Exception e){
			myPOMLogger.enterSevereLog(pageObjectName +" : IsSelected element not found ");
		}
		return tagName;
	}//GetTagName
	
	
	/**
	 * This method demonstrates GetText().
	 * <br>return the text of the element
	 * <br>
	 * @return String - textName 
	 * @author davidwramer
	 * @version 3.0
	 */
	public String GetText() {
		String textName = null;
		try{
			
			if(null != element){
				
				textName = new String(element.getText());
			}else{
				myPOMLogger.enterSevereLog(pageObjectName +" : IsSelected element not found ");
			}
		}catch (Exception e){
			myPOMLogger.enterSevereLog(pageObjectName +" : IsSelected element not found ");
		}
		return textName;
	}//GetText

	public void performAction(WebElement element, String whatAction){
		if(null != myDriver){
			Actions action = new Actions(myDriver);
			 
			if(whatAction.equals("doubleClick")){
				  //Double click
				  action.doubleClick(element).perform();
			}else if(whatAction.equals("moveToElement")){
			  //Mouse over
			  action.moveToElement(element).perform();
			}else if(whatAction.equals("rightClick")){
				  //Right Click
				  action.contextClick(element).perform();
			}
		}
	}
	/**
	 * waitUnitlVisible()
	 * <br> This function waits until the visibility of an element
	 * @param time - time in seconds to wait
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	public void waitUntilVisible(int time){
		WebDriverWait wait;
		wait =  new WebDriverWait(myDriver, time);
		wait.until(ExpectedConditions.visibilityOf(element));
		
	}//waitUntilVisible
	/**
	 * fluentWait()
	 * <br> This function is used to track AJAX functions that build pages, and you have to wait for the element to be visible
	 * @param polling - time in seconds
	 * @param timeOut - time in seconds
	 * @see RemoteWebDriver
	 */
	public void fluentWait(int polling,int timeOut ){
		
		@SuppressWarnings("deprecation")
		Wait<WebDriver> wait = new FluentWait<WebDriver>(myDriver)

			       .withTimeout(polling, TimeUnit.SECONDS)

			       .pollingEvery(timeOut, TimeUnit.SECONDS)

			       .ignoring(NoSuchElementException.class);

			 
		if(null != wait){
			wait.until(ExpectedConditions.visibilityOf(element));
		}else{
			myPOMLogger.enterSevereLog(pageObjectName +" : fluentWait failed to initialize ");
		}

	}//fluentWait
	
	/**
	 * waitUntilPrecenseOfElementLocated()
	 * <br> This function waits until the presence Of Element Located
	 * @param time - wait time
	 * @see RemoteWebDriver
	 * @author DRamer
	 * @version 1.0
	 */
	public void waitUntilPrecenseOfElementClickable(int time){
		WebDriverWait wait;
		
		wait =  new WebDriverWait(myDriver, time);
		wait.until(ExpectedConditions.elementToBeClickable(element));
		
	}//waitUntilPrecenseOfElementLocated
	
}//sTestBaseObjectType
