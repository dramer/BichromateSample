/*
 * sTestTextBoxObject.java	1.0 2017/06/01
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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import bichromate.core.*;

public class sTestTextBoxObject extends sTestBaseObjectType {
	
	
	public sTestTextBoxObject(WebDriver driver,WebElement element, String PageObject,pageObjectModelLogFactory myLogger){ 
		myDriver = driver;
		this.element = element;	
		pageObjectName = new String(PageObject);
		myPOMLogger = myLogger;
	}
	
	/**
	 * This function demonstrates sTestTextBoxObjectClearText().
	 * <br>Function to clear the text into the textFiled
	 * <br> 
	 * @author davidwramer
	 * @version 3.0
	 */		
	public void sTestTextBoxObjectClearText(){
		try{
			
			if(null != element){
				element.clear();
				
			}else{
				myPOMLogger.enterSevereLog(pageObjectName+" : sTestTextBoxObjectClearText element not found ");
			}
		}catch (Exception e){
			myPOMLogger.enterSevereLog(pageObjectName+" : sTestTextBoxObjectClearText element not found ");
		}
	}//enterSearchTextBoxInfo 

	/**
	 * This function demonstrates sTestTextBoxObjectEnterText().
	 * <br>Function to enter text into the searchFiled
	 * <br> 
	 * @param data text that you wish put in search field 
	 * @author davidwramer
	 * @version 3.0
	 */		
	public void sTestTextBoxObjectEnterText(String data){
		try{
			
			if(null != element){
				element.clear();
				element.sendKeys(data);
			}else{
				myPOMLogger.enterSevereLog(pageObjectName+" : enterSearchTextBoxInfo element not found ");
			}
		}catch (Exception e){
			myPOMLogger.enterSevereLog(pageObjectName+" : enterSearchTextBoxInfo element not found ");
		}
	}//enterSearchTextBoxInfo 
	
}//sTestTextBoxObject



