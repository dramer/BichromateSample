package bichromate.baseObjectTypes;



import org.openqa.selenium.WebDriver;

/*
 * sTestDropDownList.java	1.0 2017/06/01
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

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import bichromate.core.pageObjectModelLogFactory;

public class sTestDropDownList extends sTestBaseObjectType {
	
	
	public sTestDropDownList(WebDriver driver,WebElement element, String PageObject,pageObjectModelLogFactory myLogger){ 
		myDriver = driver;
		this.element = element;
		pageObjectName = new String(PageObject);
		myPOMLogger = myLogger;
	}//sTestButtonObject
	/**
	 * This method demonstrates selectDropDownElementByIndex().
	 * <br>Select drop down by index
	 * <br>
	 * @param index of the list item 
	 * @return boolean  if select was succesful
	 * @author dramer
	 * @version 3.0
	 */
	public boolean selectDropDownElementByIndex(int index){
		
		try{
			
			if(null != element){
				Select dropdown = new Select(element);
				dropdown.selectByIndex(1);
				return true;
			}else{
				myPOMLogger.enterSevereLog(pageObjectName +" : selectDropDownElement element not found ");
			}
		}catch (Exception e){
			myPOMLogger.enterSevereLog(pageObjectName +" : selectDropDownElement element not found ");
		}
		return false;
	}// selectDropDownElementByIndex
	/**
	 * This method demonstrates selectDropDownElementByValue().
	 * <br>Select drop down by Value
	 * <br>  
	 * @param value - String name in the list
	 * @return boolean - if selection was succeful
	 * @author dramer
	 * @version 3.0
	 */
	public boolean selectDropDownElementByValue(String value){
		
		try{
			
			if(null != element){
				Select dropdown = new Select(element);
				 dropdown.selectByValue(value);
				 return true;
			}else{
				myPOMLogger.enterSevereLog(pageObjectName +" : selectDropDownElement element not found ");
			}
		}catch (Exception e){
			myPOMLogger.enterSevereLog(pageObjectName +" : selectDropDownElement element not found ");
		}
		return false;
	}// selectDropDownElementByValue
	/**
	 * This method demonstrates selectDropDownElement().
	 * <br>verify the search button is displayed
	 * <br>  
	 * @param listItem - item to find in the list
	 * @return boolean - true if selection was done
	 * @author dramer
	 * @version 3.0
	 */
	public boolean selectDropDownByVisibleText(String listItem) {

		try{
			
			if(null != element){
				Select dropdown = new Select(element);
				dropdown.selectByVisibleText(listItem);
			}else{
				myPOMLogger.enterSevereLog(pageObjectName +" : selectDropDownElement element not found ");
			}
		}catch (Exception e){
			myPOMLogger.enterSevereLog(pageObjectName +" : selectDropDownElement element not found ");
		}
		return false;
		
	}//selectDropDownByVisibleText
	
	
}//sTestButtonObject
