package bichromate.core;

import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * @author davidwramer
 * @version 1.0
 *
 */
public class sTestSlackFactory {
	private String slackServerURL = null;
	private String slackServerTestURL = null;
	
	protected static ResourceBundle resources;
	
	static {
		try {
			resources = ResourceBundle.getBundle("common.sTestSlackFactory", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestSlackFactory.properties not found: "+ mre);
			System.exit(0);
		}
	}
	
	public sTestSlackFactory(){
		
		slackServerURL =  new String(resources.getString("sTestSlackFactory.slackServerURL"));
		slackServerTestURL =  new String(resources.getString("sTestSlackFactory.slackServerTestURL"));
	}
	
	public sTestSlackFactory(ResourceBundle remoteResources){
		
		slackServerURL =  new String(remoteResources.getString("sTestSlackFactory.slackServerURL"));
	}
	/**
     * This method demonstrates sendSlackMessage().
     * This function is used to send a public messages to Slack. The URL has the room information and predefine in the properties file
     * @param message message to send to slack
     */
	
	public void sendSlackMessage(String message){
		
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		
	        try {
	        	
	            HttpPost httppost = new HttpPost(slackServerURL);
	           System.out.println("Server URL "+slackServerURL);
	           // httppost.setHeader("Content-Type", "text/x-gwt-rpc; charset=UTF-8");
	            httppost.addHeader("Content-type", "application/json");
	            
	            //
	   		 //  payload={"text": "This is a line of text in a channel.\nAnd this is another line of text."}
	   		 //
	
	            //
	            // Add the Json/xml code
	            //
	           System.out.println("Payload is "+ "payload={\"text\""+":"+"\""+message+"\"}");
	           // StringEntity params = new StringEntity(jsonobj.toString());
	           StringEntity params = new StringEntity("{\"text\" : \""+message+"\"}","UTF-8");
	          
	           params.setContentEncoding("UTF-8");
	           params.setContentType("application/json; charset=utf-8");
	            httppost.setEntity(params);
	            
	            //
	            // Execute the
	            //
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity resEntity = response.getEntity();
	            System.out.println(" @@@@ Http Post : Response " + response.toString());

	            System.out.println("----------------------------------------");
	            System.out.println(response.getStatusLine());
	            
	            if (resEntity != null) {
	                System.out.println("Response content length: " + resEntity.getContentLength());
	                System.out.println("Chunked?: " + resEntity.isChunked());
	            }
	            EntityUtils.consume(resEntity);
	            httpclient.close();
	        }catch(IOException e){
	        	System.out.println("IOException: " + e);
	        } finally {
	            // When HttpClient instance is no longer needed,
	            // shut down the connection manager to ensure
	            // immediate deallocation of all system resources
	            // no need to close 
	        	
	        }   
	}
	/**
     * This method demonstrates selfTest().
     * This method unit tests all the methods in sTestSlackFactory
     */
	private void selfTest(){
		slackServerURL = new String(slackServerTestURL);
		sendSlackMessage("Bichromate sTestSlackFactory Self Test");
	}
	//
	// Inner class for testing on the command line
	//
	public static class Test {
		public static void main(final String[] args){
			
			sTestSlackFactory slack = new sTestSlackFactory();
			if(null != slack)
				slack.selfTest();
			
		}// main
	}// Test
}// sTestSlackFactory
