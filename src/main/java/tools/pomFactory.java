/*
 * pomFactory.java	1.0 2018/08/27
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
package tools;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFileChooser;
//import javax.swing.JOptionPane;

import bichromate.core.sTestSpreadsheetFactory;
import bichromate.tools.sTestDOMParser;
import bichromate.tools.sTestPOMFactory;

public class pomFactory {

	sTestPOMFactory pom = null;
	sTestDOMParser dom = null;
	sTestSpreadsheetFactory ssf = null;
	
	public pomFactory(){
		 pom = new sTestPOMFactory();
		 dom = new sTestDOMParser();
		 ssf = new sTestSpreadsheetFactory();
	}
	public void buildPOM(String webPage){
		
		try {
			dom.parseURL(new URL(webPage));
			 // prompt the user to enter their name
			JFileChooser fileChooser = new JFileChooser();
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			  File file = fileChooser.getSelectedFile();
			  String name = new String(file.getName());
			  name = new String(name.replaceFirst(".properties",""));
			  pom.createPOM(file,name);
			  System.out.println("pom file to create: "+name );
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args)
    	{
			pomFactory pf = new pomFactory();
			pf.buildPOM("http://www.bichromate.org/seleniumTestPage.html");
			
    	}
	}

}// pomFactory