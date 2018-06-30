/*
 * seaLightsFactory.java	1.0 2017/05/11
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
 * 
 * 
 * 
 */
package bichromate.sealights;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import bichromate.core.sTestOSInformationFactory;
import bichromate.graphs.sTestCPUGraph;

@SuppressWarnings("unused")

public class seaLightsFactory {
	private String pathToSessionId = null;
	private String pathToReportFile = null;
	private String buildListner = null;
	private String testListner = null;
	private String Accesstoken = null;
	private String login = null;
	
	private String password = null;
	private String enabled = null;
	private sTestOSInformationFactory path = null;
	private ProcessBuilder pb = null;
	private Process seaLightsProcess = null;
	protected static ResourceBundle resources;
	
	static {
		try {
			resources = ResourceBundle.getBundle("seaLights.seaLights", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("seaLights.properties not found: "+ mre);
			System.exit(0);
		}
	}
	/**
	   * This class Demonstrates seaLightsFactory().
	   * <br>
	   * <br>
	   * @author davidwramer
	   * @version 1.0
	   */
	public seaLightsFactory(){
		path = new sTestOSInformationFactory();
		
		buildListner = new String(resources.getString("seaLightsFactory.buildListner"));
		testListner = new String(resources.getString("seaLightsFactory.testListner"));
		Accesstoken = new String(resources.getString("seaLightsFactory.Accesstoken"));
		login = new String(resources.getString("seaLightsFactory.login"));
		password = new String(resources.getString("seaLightsFactory.password"));
		enabled = new String(resources.getString("seaLightsFactory.enabled"));
		
		
	}//seaLightsFactory
	 /**
	   * This class Demonstrates seaLightsFactory().
	   * <br>
	   * <br>
	   * @param myResources - resource bundle to pass. should be Bichromate.properties
	   * @author davidwramer
	   * @version 3.0
	   */
	public seaLightsFactory(ResourceBundle myResources){
		path = new sTestOSInformationFactory();
		
		buildListner = new String(myResources.getString("seaLightsFactory.buildListner"));
		testListner = new String(myResources.getString("seaLightsFactory.testListner"));
		Accesstoken = new String(myResources.getString("seaLightsFactory.Accesstoken"));
		login = new String(myResources.getString("seaLightsFactory.login"));
		password = new String(myResources.getString("seaLightsFactory.password"));
		enabled = new String(myResources.getString("seaLightsFactory.enabled"));
		
		
	}//seaLightsFactory
	 /**
	   * This method Demonstrates uploadReports().
	   * <br>This method starts the Sea Lights connection and should be called @beforeSute in the TestNG framework
	   * <br>
	   * @return String - status of Sea Lights connection
	   * @throws IOException - error in connection
	   * @author davidwramer
	   * @version 3.0
	   */
	public String startSeaLights() throws IOException{
		
		if(enabled.toLowerCase() == "false")
			return("Sea lights is not enabled, see seaLights properties file");
		
		// java -jar sl-test-listener.jar start -tokenfile "/path/to/tokenFile" -buildsessionidfile "/path/to/buildSessionId.txt" -testStage "Unit Test"
		
		
		
		// You just add more arguments to the ProcessBulider constructor: new ProcessBuilder("/path/to/java", "-jar", "your.jar", "arg1", "arg2"); � aioobe Feb 8 '11 at 17:57
		
		pb = new ProcessBuilder(System.getProperty("java.home"),"-jar", path.getSeaLightsDirectory()+testListner, "start","-token",Accesstoken,"-buildsessionidfile",pathToSessionId,"-testStage", "Regression Tests");
		pb.directory(new File(path.getSeaLightsWorkingDirectory()));
		seaLightsProcess = pb.start();
		return("Sea lights is enabled and seaLights process is running");
		
	}// startSeaLights
	 /**
	   * This method Demonstrates getSeaLightDirectory().
	   * <br>This method stops the sea Lights connection and should be called @afterSuite in the TestNG framework
	   * <br>
	   * @return String - status of Sea Lights connection
	   * @throws IOException - error in connection
	   * @author davidwramer
	   * @version 3.0
	   */
	public String  stopSeaLights() throws IOException{
		if(enabled.toLowerCase() == "false")
			return("Sea lights is not enabled, see seaLights properties file");
		seaLightsProcess.destroy();
		pb = null;
		seaLightsProcess = null;
		return("Sea lights process destroyed and stopped");
	}//stopSeaLights
	 /**
	   * This method Demonstrates uploadReports().
	   * <br>This method is called after all tests are run @AftetrSuite to upload the TestNG reports
	   * <br>
	   * @return workingDirectory of the working directory
	   * @throws IOException - error in connection
	   * @author davidwramer
	   * @version 3.0
	   */
	public String uploadReports()throws IOException{
		if(enabled.toLowerCase() == "false")
			return("Sea lights is not enabled, see seaLights properties file");
		
		pb = new ProcessBuilder(System.getProperty("java.home"), "-jar", "your.jar");
		pb = new ProcessBuilder(System.getProperty("java.home"),"-jar", path.getSeaLightsDirectory()+testListner, "uploadReports","-token",Accesstoken,"-buildsessionidfile",pathToSessionId,"-reportFile", pathToReportFile,"-source","Junit xml report");
		pb.directory(new File("preferred/working/directory"));
		seaLightsProcess = pb.start();
		/*
		for f in /path/to/junit_report_*.xml
		do
		    java -jar sl-test-listener.jar uploadReports -tokenfile "/path/to/tokenFile" -buildsessionidfile "/path/to/buildSessionId.txt" -reportFile "$f" -source "Junit xml report"
		done
		*/
		pb = new ProcessBuilder(System.getProperty("java.home"),"-jar", path.getSeaLightsDirectory()+testListner, "end","-token",Accesstoken,"-buildsessionidfile",pathToSessionId);
		seaLightsProcess = pb.start();
		
		
		seaLightsProcess.destroy();
		pb = null;
		seaLightsProcess = null;
		return("Sea lights reports uploaded and process destroyed and stopped");
		
	}//uploadReports
	
	 //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			System.out.println("Starting Sea Lights Factory test");
			seaLightsFactory slf = new seaLightsFactory();
			if(null != slf){
				try {
					slf.startSeaLights();
					slf.uploadReports();
					
					slf.stopSeaLights();
					
				} catch (IOException e) {
					
					System.out.println("seaLightsFactory tossed some IO Exception: " +e);
				}
				
				
				
				
			}
			
			
			System.out.println("Finished Sea Lights Factory test");
		} // end Main
	 } // end Inner class Test

}// seaLightsFactory
