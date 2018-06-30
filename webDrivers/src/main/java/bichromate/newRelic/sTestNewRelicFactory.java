/*
 * sTestKibanaFactory.java	1.0 2018/04/14
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
 * https://docs.newrelic.com/docs/servers/rest-api-examples-v2/server-api-examples/list-your-server-id-metric-data-v2
 * 
 */
package bichromate.newRelic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import bichromate.core.sTestHTTPFactory;
import bichromate.dataStore.sTestNewRelicServerInformation;
import bichromate.kibana.sTestKibanaFactory;
@SuppressWarnings("unused")
public class sTestNewRelicFactory {
	private static ResourceBundle resources;
	
	private String newRelicURL = null;
	private String newRelicKey = null;
	private List <sTestNewRelicServerInformation> serverList = null;
	
	static
	{
		try
		{
			resources = ResourceBundle.getBundle("common.sTestNewRelicFactory",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestNewRelicFactory.properties not found: "+mre);
			System.exit(0);
		}
	}
	
	/**
	 * This class Demonstrates sTestNewRelicFactory().
	 * <br>This class sets up the connection to NewRelic
	 * <br>
	 * @author davidwramer
	 * @version 1.0 
	 */
	public sTestNewRelicFactory(){
		setupsTestNewRelicFactory(resources);
	
	}// sTestNewRelicFactory
	/**
	 * This class Demonstrates sTestNewRelicFactory().
	 * <br>This class sets up the connection to NewRelic
	 * <br>
	 * @param remoteResources passed in resource bundle when this class is used within a jar file
	 * @author davidwramer
	 * @version 1.0 
	 */
	public sTestNewRelicFactory(ResourceBundle remoteResources){
		
		setupsTestNewRelicFactory(remoteResources);
	
	}// sTestNewRelicFactory
	/**
	 * This method Demonstrates setupsTestNewRelicFactory().
	 * <br>This method sets up the connection to NewRelic
	 * <br>
	 * @param remoteResources passed in resource bundle when this class is used within a jar file
	 * @author davidwramer
	 * @version 1.0 
	 */
	
	private void setupsTestNewRelicFactory(ResourceBundle remoteResources){
		newRelicURL = new String(remoteResources.getString("sTestNewRelicFactory.newRelicURL"));
		newRelicKey = new String(remoteResources.getString("sTestNewRelicFactory.newRelicKey"));
		/*
		curl -X GET 'https://api.newrelic.com/v2/servers.json' -H "X-Api-Key:${babb52b4386f29587fb433881cd0bb16}" -i
	     */ 
	}
	/**
	 * This method Demonstrates getNewRelicAPIInformation().
	 * <br>This method retrieves HTML help file for NewRelic API
	 * <br>
	 * @return String returns index.html for NewRelic API
	 * @author davidwramer
	 * @version 1.0 
	 */
	public String getNewRelicAPIInformation(){
		
		String results = null;
		sTestHTTPFactory httpFactory = new sTestHTTPFactory();
		if(null != httpFactory){
			
			
			results = new String(httpFactory.processHTTPRequest(newRelicURL,"GET","","", null));
			System.out.println(results);
		}
		
		return results;
	}
	/**
	 * This method Demonstrates parseServerResults().
	 * <br>This method parses the results from the getNewRelicServerList
	 * <br>
	 * @param String results - Results from the getNewRelicServerList
	 * @author davidwramer
	 * @version 1.0 
	 */
	private void parseServerResults(String results){
		
		List <String> serverID = new ArrayList<String>();
		List <String> accountID = new ArrayList<String>();
		List <String> serverName = new ArrayList<String>();
		List <String> hostName = new ArrayList<String>();
		List <String> healthStatus = new ArrayList<String>();
		List <String> reporting = new ArrayList<String>();
		List <String> lastReportedTime = new ArrayList<String>();
		int index = 0;
		
		index = results.indexOf(",\"name\":");
		
		while (index >= 0) {
			String name = new String((String) results.subSequence(index+9, results.indexOf(",",index+7)-1));
	    	System.out.println("Server Name "+ name);
	    	serverName.add(name);
		    //System.out.println(index);
		    index = results.indexOf(",\"name\":", index + 1);
		}
		index = results.indexOf("\"id\":");
		
		while (index >= 0) {
			String name = new String((String) results.subSequence(index+5, results.indexOf(",",index+5)));
		    System.out.println("Server ID "+ name);
		    serverID.add(name);
		    //System.out.println(index);
		    index = results.indexOf("\"id\":", index + 1);
		}
		index = results.indexOf("\"health_status\":");
		
		while (index >= 0) {
			String name = new String((String) results.subSequence(index+17, results.indexOf(",",index+16)-1));
			System.out.println("health status "+ name);
			healthStatus.add(name);
		    //System.out.println(index);
		    index = results.indexOf("\"health_status\":", index + 1);
		}
		index = results.indexOf("\"last_reported_at\":");
		
		while (index >= 0) {
			String name = new String((String) results.subSequence(index+20, results.indexOf(",",index+19)-1));
		    System.out.println("last reported time "+ name);
		    lastReportedTime.add(name);
		    //System.out.println(index);
		    index = results.indexOf("\"last_reported_at\":", index + 1);
		}
		index = results.indexOf("\"host\":");
		
		while (index >= 0) {
			String name = new String((String) results.subSequence(index+8, results.indexOf(",",index+7)-1));
	    	System.out.println("Host Names "+ name);
	    	hostName.add(name);
		    //System.out.println(index);
		    index = results.indexOf("\"host\":", index + 1);
		}
		index = results.indexOf("\"reporting\":");
		
		while (index >= 0) {
			String name = new String((String) results.subSequence(index+11, results.indexOf(",",index+11)));
	    	System.out.println("Reporting "+ name);
	    	reporting.add(name);
		    //System.out.println(index);
		    index = results.indexOf("\"reporting\":", index + 1); 
		}
	}//parseServerResults
	/**
	 * <br>
	 * @return String returns XML for all the servers being used
	 * @author davidwramer
	 * @version 1.0 
	 */
	public String getNewRelicServerList(){
		String results = null;
		sTestHTTPFactory httpFactory = new sTestHTTPFactory();
		if(null != httpFactory){
			HashMap<String,String> map = new HashMap<String, String>();
			
			map.put("Accept", "application/json");
			map.put("X-Api-Key", newRelicKey);
			
			// conn.setRequestMethod("GET");
 	     	// conn.setRequestProperty("Accept", "application/json");
 	     	//conn.setRequestProperty("content-type", "text/html");
 	     	
 	     	//conn.setRequestProperty("Accept-Charset", "UTF-8");
 	     	//conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
 	     	//conn.setRequestProperty("User-Agent", "");
        	//conn.setRequestProperty("X-Api-Key", "2a8d763bbeb790b93f488a44d9618b11c0ff13505292eb4");
			
			
			results = new String(httpFactory.processHTTPRequest(newRelicURL,"GET","","", map));
			if(null != results){
				System.out.println(results);
				parseServerResults(results);
			}
		}
		
		return results;
	}//getNewRelicServerList
	
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public static void main(final String[] args){
	 		
	 		
	 		sTestNewRelicFactory newRelic = new sTestNewRelicFactory();
	 		if(null != newRelic){
	 			String results = "failed";
	 			results = new String (newRelic.getNewRelicServerList());
	 		}
	 		
	 	}//main
	 }//Test

}
