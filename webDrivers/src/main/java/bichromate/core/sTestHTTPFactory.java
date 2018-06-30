/*
 * sTestHTTPFactory.java	1.0 2013/01/23
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


package bichromate.core;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * This class demonstrates oTestHTTPFactory().
 * <br>This class factory is used to test JSON APIS.
 * <br>
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("unused")
public class sTestHTTPFactory {
	
	private String selfTestURL = null;
	private String contentType=null;
	private String saveFilesAt = null;
	private String contentLength = null;
	private String reponseInformation = null;
	private String SaveResponseFilesAt = null;
	
   
   
    private CloseableHttpClient httpclient = null;
    private sTestOSInformationFactory path = new sTestOSInformationFactory();

    private static ResourceBundle resources;
    
    static
	{
		try
		{
			resources = ResourceBundle.getBundle("common.sTestHTTPFactory",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestHTTPFactory.properties not found: "+mre);
			System.exit(0);
		}
	}
    /**
	 * This class demonstrates oTestHTTPFactory().
	 * <br>This class sets up the HTTPFactory for making HTTP calls.
	 * <br>
	 */
    public sTestHTTPFactory(){
    	
    	path = new sTestOSInformationFactory();
    	
    	
	    //
    	// Directory where files will be stored
    	//
	    saveFilesAt = new String(resources.getString("sTestHTTPFactorty.SaveFilesAt"));
	    //
	    // Content type for Json request
	    //	    
	    contentType		= new String(resources.getString("sTestHTTPFactory.contentType"));	
    	// 
		// Use only for GET requests.
		//
    	selfTestURL = new String(resources.getString("sTestHTTPFactorty.selfTestURL"));
    	
    	
	}// sTestHTTPFactory
    
    /**
	 * This class Demonstrates oTestHTTPFactory().
	 * <br>This class sets up the HTTPFactory for making HTTP calls.
	 * <br>
	 * @param remoteResources passed in when this class is used within a jar file
	 */
    public sTestHTTPFactory(ResourceBundle remoteResources){
    	
    	path = new sTestOSInformationFactory();
    	
    	if(path != null){
	    	if(path.getOSName().equals("WINDOWS")){
	    		SaveResponseFilesAt = new String(resources.getString("sTestHTTPFactory.SaveWindowsResonsysFilesAt"));
	    	}else{
	    		SaveResponseFilesAt = new String(resources.getString("sTestHTTPFactory.SaveLinuxResonsysFilesAt"));
	    	}
    	}else{
    		SaveResponseFilesAt = new String("bad path");
    	}
    	
    	selfTestURL = new String(remoteResources.getString("selfTestURL"));
    	
	}// sTestHTTPFactory
   
    /**
     * This function Demonstrates getResponseInformation().
     * <br>This function is used to return the responseInformation
     * <br>
     * @return reponseInformation returns the param reponseInformation   
     */
	public String getResponseInformation(){
		return reponseInformation;
	}
	/**
     * This function Demonstrates getContentLength().
     * <br>This function is used to return the content length of a HTTP call
     * <br>
     * @return contentLength  lenght of the HTTP call 
     */
	public String getContentLength(){
		
		return contentLength;
	}
	
	/**
     * This function Demonstrates downloadFromUrl().
     * <br>This function is used to do a get from a URL
     * <br>
     * @param url to be used to download from
     * @param localFilename name of the file where the data is stored 
     * @throws IOException all I/O erros are caught
     */
	public void downloadFromUrl(URL url, String localFilename) throws IOException {
	    InputStream is = null;
	    FileOutputStream fos = null;

	    try {
	        URLConnection urlConn = url.openConnection();//connect

	        is = urlConn.getInputStream();               //get connection inputstream
	        fos = new FileOutputStream(localFilename);   //open outputstream to local file

	        byte[] buffer = new byte[8192];              //declare 4KB buffer
	        int len;

	        //while we have availble data, continue downloading and storing to local file
	        while ((len = is.read(buffer)) > 0) {  
	            fos.write(buffer, 0, len);
	        }
	    } finally {
	        try {
	            if (is != null) {
	                is.close();
	            }
	        } finally {
	            if (fos != null) {
	                fos.close();
	            }
	        }
	    }
	}// downloadFromUrl
	/**
     * This function Demonstrates processJsonRequest().
     * <br>This function is used to properly execute a Json request
     * <br>
     * @param body - POST/GET
     * @param login   login name
     * @param password password used by login
     * @param url to process
     * @throws IOException all I/O erros are caught
     * @throws JSONException all Json erros are caught
     */
	public void processPostJsonRequest(String body,String login,String password,String url) throws JSONException, IOException{
		
		
		URL object=new URL(url);
		
		Gson         gson          = new Gson();

		// HttpURLConnection con = (HttpURLConnection) object.openConnection();
		HttpPost     post = new HttpPost(url);
		// HttpGet		 get  = new HttpGet(url);
		HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead 
		
		
		String auth = login + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
		String authHeader = "Basic " + new String(encodedAuth);
		post.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
		
		post.setHeader("Content-type", "application/json");
		
		StringEntity postingString = new StringEntity(gson.toJson(body));
		post.setEntity(postingString);
		

		HttpResponse  response = httpClient.execute(post);
		
		int responseCode = response.getStatusLine(). getStatusCode();
		
		//display what returns the POST request

		StringBuilder sb = new StringBuilder();  
		
		if (responseCode == 200) {     
		    String line =  new String(response.toString());
		    System.out.println("POST Code: "+line);  
		   
		} else {
		    System.out.println("Resonse Code: "+responseCode);  
		}  
		
	}// processJsonRequest
	/**
     * This method Demonstrates escapeJsonRequest().
     * <br>This function is used to properly escape a Json request
     * <br>
     * @param jsonRequest  request to set all escape characters
     * @return escapedJsonRequest escaped jsonRequest  
     */
	public String escapeJsonRequest(String jsonRequest){
		String escapedJsonRequest = new String(jsonRequest.replace("\"","\\\"" ));
		System.out.println(" escaped Json string: " +escapedJsonRequest);
		return escapedJsonRequest;
	}
	/**
     * This function Demonstrates processPostRequest().
     * <br>This function is used to process public API post request
     * <br>
     * @param parameters params for the server
     * @param server  name of the server
     */
	public void processPostRequest(String parameters, String server) {

		 httpclient = HttpClientBuilder.create().build();
        try {
        	
            HttpPost httppost = new HttpPost(server);
           
            
            System.out.println("httppost created");
            
            //
            //This is where we set the header information
            //
            httppost.addHeader("Authorization", "authCode");	
            httppost.addHeader("Content-type", "application/json");
            if(httppost.containsHeader("Authorization")) System.out.println("Header set for: Authorization");
            if(httppost.containsHeader("Content-Type")) System.out.println("Header set for: Content-Type");
            System.out.println("httppost header Created");
            
            //
            // Add the Json/xml code
            //
            StringEntity params = new StringEntity(parameters); // parametes are name value pair
            params.setContentType("application/json");
            httppost.setEntity(params);
          
            
            System.out.println("httppost set entity complete");
            System.out.println("executing request " + httppost.getRequestLine());
            //
            // Execute the
            //
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            System.out.println("----------------------------------------");
            reponseInformation = new String(response.getStatusLine().toString());
            System.out.println(response.getStatusLine());
            
            if (resEntity != null) {
            	contentLength = new String("Response content length: " + resEntity.getContentLength());
                System.out.println("Response content length: " + resEntity.getContentLength());
                System.out.println("Chunked?: " + resEntity.isChunked());
            }
            EntityUtils.consume(resEntity);
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
     * This function Demonstrates processGetRequest().
     * <br>This function is used to process as straight forward get request
     * <br>
     * @param urlToRead  url to process
     * @return result information.
     */
	public String processGetRequest(String urlToRead) {

		URL url;
	      HttpURLConnection conn;
	      BufferedReader rd;
	      String line;
	      String result = "";
	      try {
	         url = new URL(urlToRead);
	         conn = (HttpURLConnection) url.openConnection();
	         conn.setRequestMethod("GET");
	         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         while ((line = rd.readLine()) != null) {
	            result += line;
	         }
	         rd.close();
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	      return result;
	}
	/**
     * This function Demonstrates processGetHTTPSRequest().
     * <br>This function is used to process as straight forward HTTPS get request
     * <br>
     * @param urlToRead  url to process
     * @param method POST, GET
     * @param login if needed, empty string otherwise
     * @param password if needed emty string otherwise 
     * @param requestProperties - name value pair for all the request properties for the connection
     * @return String - information.
     */
	public String processHTTPRequest(String urlToRead,String method,String login, String password,HashMap <String, String>requestProperties) {
		 InputStream inputStream = null;
		URL url;
	    HttpURLConnection conn;
	    BufferedReader rd;
	    String line;
	    String result = "";
	    try {
	    	url = new URL(urlToRead);
	        System.out.println("url: " + url);
	        conn = (HttpURLConnection) url.openConnection();
	         
	        if(null != conn){
	        	conn.setRequestMethod(method);
	        	//
	        	// Get all the keys if
	        	//
	        	if(null == requestProperties){
	        		System.out.println("processHTTPRequest: no hashMap entries");
	        		return result;
	        	}
	        	String[] keys = requestProperties.keySet().toArray(new String[requestProperties.size()]);
	        	for(int x = 0; x < keys.length;x++){
	        		String key = new String(keys[x]);
	        		String value = new String(requestProperties.get(keys[x]));
	        		System.out.println("conn.setRequestMethod: key: "+key+" value: "+value);
	        		conn.setRequestProperty(key,value );
	        	}
	        	if(null == login){
	        		System.out.println("processHTTPRequest: Login is null");
	        		return result;
	        	}
	        	if(null == password){
	        		System.out.println("processHTTPRequest: password is null");
	        		return result;
	        	}
	 	     	 if (login.length() > 0 && password.length() > 0) {
	                 String userPassword = login + ":" + password;
	                // @SuppressWarnings("restriction")
					// String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
	                 String encoding = DatatypeConverter.printBase64Binary(userPassword.getBytes());
	                 conn.setRequestProperty("Authorization", "Basic " + encoding);
	             }
	 	     	
	 	     	conn.connect();
	 	     	String contentEncoding = conn.getContentEncoding();
	 	       if (contentEncoding != null) {
	 	    	   if (contentEncoding.equalsIgnoreCase("gzip")) {
	 	    		   inputStream = new GZIPInputStream(conn.getInputStream());
	 	    	   } else if (contentEncoding.equalsIgnoreCase("deflate")) {
	 	    		   inputStream = new InflaterInputStream(conn.getInputStream(), new Inflater(true));
	 	    	   }
	 	       }else {
	 	         inputStream = conn.getInputStream();
	 	       }	
	 	       rd = new BufferedReader(new InputStreamReader(inputStream));
	 	       while ((line = rd.readLine()) != null) {
	 	    	   result += line;
	 	       }
	 	       rd.close();
	 	      conn.disconnect();
	         }else{
	        	 System.out.println("processGetHTTPRequest: Failed to createHttpsURLConnection");
	         }
	      } catch (IOException e) {
	    	  System.out.println("processGetHTTPRequest:IOException" +e);
	         e.printStackTrace();
	      }
	      return result;
	}
	
	/**
     * This method completes all unit tests oTestHTTPFactory.
     * <br>This function is used to selfTest oTestHTTPFactory by downloading all the team rooms for the credentials in the 
     * <br>  properties file
     */
	public void selfTest(){
		
		 String currentDate = new String(path.getCurrentDateAndUTCTime());
		 //
		 // Testing post request
		 // 
		 //
		 // Testing downloadFromUrl
		 //
		try{
			String filePath = new String(path.workingDirectory() + path.fileSeperator()+saveFilesAt+path.fileSeperator()+"mralexgrayrepos."+currentDate+".xml");
			System.out.println("file to download to is:" +filePath);
			System.out.println("Testing downloadFromURL");
			downloadFromUrl(new URL(selfTestURL),filePath );
			System.out.println("Downloaded: https://api.github.com/users/mralexgray/repos");
		}catch(IOException io){
			System.out.println("IOException" + io);
		}
	}
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			
			sTestHTTPFactory httpFactory = null;
			
			httpFactory = new sTestHTTPFactory();
			if(httpFactory != null){
				
				httpFactory.selfTest();
				
			}
			
		} // end Main
	 } // end Inner class Test
}// end oTestHTTPFactory
