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
 * Parameter	                           Description
 * client.transport.ignore_cluster_name   Set to true to ignore cluster name validation of connected nodes. (since 0.19.4)
 * client.transport.ping_timeout          The time to wait for a ping response from a node. Defaults to 5s.
 * client.transport.nodes_sampler_interval How often to sample / ping the nodes listed and connected. Defaults to 5s.
 * 
 * 
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.2/transport-client.html
 * 
 */
package bichromate.kibana;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;


public class sTestKibanaFactory {
	
	private static ResourceBundle resources;
	private TransportClient client = null;
	private String serverURL = null;
	private String port = null;
	private String clusterName = null;
	private String totalNodes = null;
	private int totalNodeCount = 0;
	private boolean setupComplete = false;
	
	
	
	static
	{
		try
		{
			resources = ResourceBundle.getBundle("common.sTestKibanaFactory",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestKibanaFactory.properties not found: "+mre);
			System.exit(0);
		}
	}
	/**
	 * This class Demonstrates sTestKibanaFactory().
	 * <br>This class sets up the connection to Kibana
	 * <br>
	 * @author davidwramer
	 * @version 1.0 
	 */
	public sTestKibanaFactory(){
		setupsTestKibanaFactory(resources);
	
	}// sTestHipChatFactory
	/**
	 * This class Demonstrates sTestKibanaFactory().
	 * <br>This class sets up the connection to Kibana
	 * <br>
	 * @param remoteResources passed in resource bundle when this class is used within a jar file
	 * @author davidwramer
	 * @version 1.0 
	 */
	public sTestKibanaFactory(ResourceBundle remoteResources){
		
		setupsTestKibanaFactory(remoteResources);
	
	}// sTestHipChatFactory
	/**
	 * This function Demonstrates setupsTestKibanaFactory().
	 * <br>This method sets up the connection to Kibana
	 * <br>
	 * @param remoteResources passed in resource bundle when this class is used within a jar file
	 * @author davidwramer
	 * @version 1.0 
	 */
	private void setupsTestKibanaFactory(ResourceBundle remoteResources){
		// Get Properties info for Server and port
		totalNodes = new String(remoteResources.getString("sTestKibanaFactory.totalNodes"));
		totalNodeCount = Integer.valueOf(totalNodes);
		clusterName =  new String(remoteResources.getString("sTestKibanaFactory.clusterName"));
		serverURL = new String(remoteResources.getString("sTestKibanaFactory.serverURL"));
		port = new String(remoteResources.getString("sTestKibanaFactory.port"));
		
		resources = remoteResources;
		
		setupComplete = false;
		
	}//setupsTestKibanaFactory
	
	@SuppressWarnings("resource")
	private void makeKibanaConnection(){
		
		if(!setupComplete) {
			
			// on startup

				try {
					
					Settings settings = Settings.builder().put("cluster.name", clusterName).build();
					
					//
					// Skip making connection if the stotal node count is 0 or the serverURL is empty
					//
					if(totalNodeCount !=0 || !serverURL.isEmpty()){
						client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(serverURL), Integer.valueOf(port)));
						
						while(--totalNodeCount >= 1 && (null != client)){
							try{
								String serverURL = new String(resources.getString("sTestKibanaFactory.serverURL"+totalNodeCount));
								client.addTransportAddress(new TransportAddress(InetAddress.getByName(serverURL), Integer.valueOf(port)));
							}catch(MissingResourceException mre){
								System.err.println("sTestKibanaFactory:setupsTestKibanaFactory, failed to find node IP address");
							}
						}
					       
					}
				} catch (NumberFormatException | UnknownHostException e) {
					 System.err.println("sTestKibanaFactory:setupsTestKibanaFactory, failed to create the TransportClient");
			
					e.printStackTrace();
					client = null;
				}
				setupComplete = true;
		}// if setupup
		
	}
	/**
	 * This function Demonstrates closeKibanaConnection().
	 * <br>This method closes the connection to Kibana
	 * <br>
	 * @author davidwramer
	 * @version 1.0 
	 */
	public void closeKibanaConnection(){
		if(null != client)
			client.close();
		System.out.println("sTestKibanaFactory:closeKibanaConnection: Closed Connection");
	}
	/**
	 * This function Demonstrates searchAllKibana().
	 * <br>This method is the basic search all for the Kibana connection
	 * <br>
	 * @return String - results of the search
	 * @author davidwramer
	 * @version 1.0 
	 */
	public String searchAllKibana(){
		makeKibanaConnection();
		
		if(null == client) {
			return new String("sTestKibanaFactory:searchAllKibana, connection not esablished");
		}
		
		String results = "Failed to find any information";
		try{
			
			SearchResponse response = client.prepareSearch().get();
			results = new String(response.toString());
		}catch (Exception e){
			System.err.println("sTestKibanaFactory:searchAllKibana, failed to find any results:"+e);
		}
		return (results);
	}
	/**
	 * This function Demonstrates searchAllKibana().
	 * <br>This method is the basic search all for the Kibana connection
	 * <br>
	 * @param index1 - search index 1
	 * @param index2 - search index 2
	 * @param type1 - type of search
	 * @param type2 - type of search
	 * @return String - results set
	 * @author davidwramer
	 * @version 1.0 
	 */
	public String searchKibana(String index1, String index2, String type1, String type2){
		
		makeKibanaConnection();
		if(null == client) {
			return new String("sTestKibanaFactory:searchKibana, connection not esablished");
		}
		
		SearchResponse response = client.prepareSearch(index1, index2)
		        .setTypes(type1, type2)
		        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.termQuery("multi", "test"))                 // Query
		        .setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
		        .setFrom(0).setSize(60).setExplain(true)
		        .get();
		
		return (response.toString());
		
	}
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public static void main(final String[] args){
	 		
	 		
	 		sTestKibanaFactory kibana = new sTestKibanaFactory();
	 		if(null != kibana){
	 			System.out.println("Search Results: "+kibana.searchAllKibana());
	 			System.out.println("-------------------------------------------------");
	 			kibana.closeKibanaConnection();
	 		}
	 		
	 	}//main
	 }//Test
	

}// sTestKibanaFactory
