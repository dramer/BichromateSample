/*
 * sTestTableObject.java	1.0 2017/06/01
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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import bichromate.core.pageObjectModelLogFactory;


public class sTestTableObject extends sTestBaseObjectType {
	

	public sTestTableObject(WebDriver driver,WebElement element, String PageObject,pageObjectModelLogFactory myLogger){ 
		myDriver = driver;
		this.element = element;
		pageObjectName = new String(PageObject);
		myPOMLogger = myLogger;
		
	
	}//sTestButtonObject
	
	@SuppressWarnings("unused")
	public void parseTheTable(){
		try{
			WebElement table = element;
			
			
			if(null == table){
				myPOMLogger.enterSevereLog("sTestTableObject:pasrseTheTable never found the table");
				return;
			}
			
			List<WebElement> tableRows = table.findElements(By.xpath("thead"));
			
		        
			myPOMLogger.enterInfoLog("NUMBER OF ROWS IN THIS TABLE = "+tableRows.size());
	        //
			// Invoice table has 2 thead elements we start at the second one
			//
		    for(int cnt = 1; cnt < tableRows.size();cnt++) { 
		    	 
		    	WebElement trElement = tableRows.get(cnt);
		    	
		    	List<WebElement> td_collection = trElement.findElements(By.tagName("td"));
		    	
		    	int rowCnt = 0;
		    	for(WebElement tdElement : td_collection){
		    		
		    	}
		    }
		}catch(Exception e){
			
		}
	}// parseTheTable
	/**
	 * This method demonstrates CountRows
	 * <br>verify the search button is enabled 
	 * <br>  
	 * @return int - number of rows
	 * @author dramer
	 * @version 3.0
	 */
	public int CountRows(){
		
		WebElement table = element;
		
		
		if(null == table){
			myPOMLogger.enterSevereLog("sTestTableObject:pasrseTheTable never found the table");
			return 0;
		}
		
		List<WebElement> tableRows = table.findElements(By.xpath("thead"));
		
	        
		return(tableRows.size());
	}//CountRows
	
}//sTestTableObject
