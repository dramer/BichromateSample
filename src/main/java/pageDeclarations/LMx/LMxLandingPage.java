/*
 * LMxLandingPage.java	1.0 2013/01/24
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

package pageDeclarations.LMx;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.openqa.selenium.WebDriver;
import bichromate.core.sTestBasePageDeclaration;



/**
 * This class Demonstrates LMxLandingPage().
 * <br>changePasswordPage encapsulates all the Selenium elements on the loginPage. 
 * <br> When building new testNG tests, the tester will create a new sTestWebDriverFactory
 * <br> and create this page using the newly created  a new sTestWebDriverFactory
 * <br> EXAMPLE:  sTestWebDriverFactory driver = new sTestWebDriverFactory(remote,version,platform,browser,setupString)
 * <br> EXAMPLE: driver.getLoginPage().pageURL
 * @see sTestBasePageDeclaration
 * @author davidwramer
 * @version 1.0
 */
public class LMxLandingPage extends sTestBasePageDeclaration{
	
	
	static ResourceBundle resources;
	
	static
	{
		try
		{
			resources = ResourceBundle.getBundle("pageDeclarations.LMx.LMxLandingPage",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("samplePageDeclaration.properties not found: "+mre);
			System.exit(0);
		}
	}
	/**
	 * method This class Demonstrates samplePageDeclaration().
	 * The samplePageDeclaration uses yahoo.com landing page 
	 * @param driver webDriver
	 * @author davidwramer
	 * @version 1.0
	 */
	public LMxLandingPage(WebDriver driver){
		super(driver,resources);
		
		pageTitle = new String(resources.getString("pageTitle"));
		pageURL = new String(resources.getString("pageURL"));
		
		POMLOGGER.enterInfoLog("creating LMxLandingPage");
		
	}
	@Override
	public boolean isAjaxLoaded() {
		// Use this method to determine if ajax is loading on your page.
		// There is no easy way to determine if ajax has completed on the page
		// SamplePageDeclaration does not use ajax
		return true;
	}
}// samplePageDeclaration

