/*
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


package sample;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.openqa.selenium.remote.RemoteWebDriver;

import bichromate.core.sTestWebDriverFactory;
import pageDeclarations.sample.samplePageDeclaration;

public class sampleWebDriverFactory extends sTestWebDriverFactory{

	private samplePageDeclaration spd = null;
	static {
		try {
			resources = ResourceBundle.getBundle("common.Bichromate", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestWebDriverFactory.properties not found: "+ mre);
			System.exit(0);
		}
	}
	
	public sampleWebDriverFactory(ResourceBundle remoteResources) {
		super(remoteResources);
		// TODO Auto-generated constructor stub
	}
	//
	// Site Pages are added here
	//
	
	
	public sampleWebDriverFactory(){
		super(resources);
		
		
	}
	public samplePageDeclaration getSamplePageDeclaration(){
		if(null == spd){
			spd = new samplePageDeclaration(driver);
		}
		return spd;
		
	}
	
	public void cleanWebDriver() {
		spd = null;
	}
	
	public RemoteWebDriver createWebDriver(String remote, String version,
			String platform, String browser, String setupString) {
		//
		// Closes all the page declarations
		//
		cleanWebDriver();
		return (super.createBrowserWebDriver(remote, version, platform, browser,
				setupString));
	}
}
