/*
 * sTestJiraFactory.java	1.0 2013/01/23
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
 * ZAPI HELP
 * http://docs.getzephyr.apiary.io/#reference/cycleresource/get-default-issue-type
 * 
 * HELP
 * https://docs.atlassian.com/jira/REST/7.0-SNAPSHOT/#api/2/issue-addComment
 * 
 * 
 */
package bichromate.core;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.PatternSyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * @author davidwramer
 * @version 1.0
 *
 */
@SuppressWarnings("all")
public class sTestJiraFactory {
	
	static String testCasePassed = "passed";
	static String testCaseFailed = "failed";
	static String testCaseInProgress = "wip";
	static String testCaseBlocked = "blocked";
	
	
	private String JiraURL = null;
	private String loginName = null;
	private String loginPassword =  null;
	private String updateCommentURL = null;
	private String updateCommentURLPattern = null;
	@SuppressWarnings("unused")
	private String testJiraIssueID = null;
	private String createTestCaseURL = null;
	private String addTestStepToATestURL = null;
	@SuppressWarnings("unused")
	private String updateTestCaseStatusURL = null;
	private String createTestCasePayLoad = null;
	private String getAllProjects = null;
	
	protected static ResourceBundle resources;
	
	//
	// APIS to set test case execution from a test cycle
	//
	private String getIssueID = null; 				// /rest/api/2/issue/%s
	private String getExecutionInformation = null; 	// /rest/zapi/latest/execution?issueId=%s
	private String putExecutionStatus = null; 		// /rest/zapi/latest/execution/6728/execute?
	
	

	static {
		try {
			resources = ResourceBundle.getBundle("common.sTestJiraFactory", Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestJiraFactory.properties not found: "+ mre);
			System.exit(0);
		}
	}
	
	
	/**
	 * sTestJiraFactory() constructor
	 * @see sTestWebDriverFactory
	 */
	public sTestJiraFactory() {
		JiraURL =  new String(resources.getString("sTestJiraFactory.JiraURL"));
		JiraURL =  new String(resources.getString("sTestJiraFactory.JiraURL"));
		loginName =  new String(resources.getString("sTestJiraFactory.loginName"));
		loginPassword =  new String(resources.getString("sTestJiraFactory.loginPassword"));
		updateCommentURL = new String(resources.getString("sTestJiraFactory.updateCommentURL"));
		updateCommentURLPattern  = new String(resources.getString("sTestJiraFactory.updateCommentURLPattern"));
		testJiraIssueID = new String(resources.getString("sTestJiraFactory.testJiraIssueID"));
		createTestCaseURL = new String(resources.getString("sTestJiraFactory.createTestCaseURL"));
		createTestCasePayLoad = new String(resources.getString("sTestJiraFactory.createTestCasePayLoad"));
		getAllProjects = new String(resources.getString("sTestJiraFactory.getAllProjects"));
		addTestStepToATestURL = new String(resources.getString("sTestJiraFactory.addTestStepToATestURL"));
		updateTestCaseStatusURL  = new String(resources.getString("sTestJiraFactory.updateTestCaseStatusURL"));
		
		
		//
		// set execution Status for a test in testing cycle
		//
		getIssueID = new String(resources.getString("sTestJiraFactory.getIssueID"));; 				// /rest/api/2/issue/%s
		getExecutionInformation = new String(resources.getString("sTestJiraFactory.getExecutionInformation"));; 	// /rest/zapi/latest/execution?issueId=%s
		putExecutionStatus = new String(resources.getString("sTestJiraFactory.putExecutionStatus"));; 		// /rest/zapi/latest/execution/6728/execute?
		
	}
	/**
	 * sTestJiraFactory() constructor
	 * @param remoteResources pass in global resource when this class is called from within sTestWebDriverFactory
	 * @see sTestWebDriverFactory
	 */
	public sTestJiraFactory( ResourceBundle remoteResources) {
		JiraURL =  new String(remoteResources.getString("sTestJiraFactory.JiraURL"));
		loginName =  new String(remoteResources.getString("sTestJiraFactory.loginName"));
		loginPassword =  new String(remoteResources.getString("sTestJiraFactory.loginPassword"));
		updateCommentURL = new String(remoteResources.getString("sTestJiraFactory.updateCommentURL"));
		testJiraIssueID = new String(remoteResources.getString("sTestJiraFactory.testJiraIssueID"));
		createTestCaseURL = new String(remoteResources.getString("sTestJiraFactory.createTestCaseURL"));
		createTestCasePayLoad = new String(remoteResources.getString("sTestJiraFactory.createTestCasePayLoad"));
		getAllProjects = new String(remoteResources.getString("sTestJiraFactory.getAllProjects"));
		addTestStepToATestURL = new String(remoteResources.getString("sTestJiraFactory.addTestStepToATestURL"));
		updateTestCaseStatusURL  = new String(remoteResources.getString("sTestJiraFactory.updateTestCaseStatusURL"));
		//
		// set execution Status for a test in testing cycle
		//
		getIssueID = new String(remoteResources.getString("sTestJiraFactory.getIssueID"));				// /rest/api/2/issue/%s
		getExecutionInformation = new String(remoteResources.getString("sTestJiraFactory.getExecutionInformation"));	// /rest/zapi/latest/execution?issueId=%s
		putExecutionStatus = new String(remoteResources.getString("sTestJiraFactory.putExecutionStatus")); 		// /rest/zapi/latest/execution/6728/execute?
		
		
		
		
	}
	/**
     * This method Demonstrates getAllProjects().
     * <br>Returns all projects which are visible for the currently logged in user. If no user is logged in, 
     * <br>it returns the list of projects that are visible when using anonymous access. Request
     * <br> Following URL for help:  https://docs.atlassian.com/jira/REST/7.0-SNAPSHOT/#api/2/issue-addComment
     */
	@SuppressWarnings("all")
	private void getAllProjects(){
		 CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		    //
		    // Creating a test case does not require a string replacement in the URL
		    //
		    // String NewUpdateCommentURL = new String(updateCommentURL.replaceAll(updateCommentURLPattern,issueID));
			
	        try {
	        	
	        	HttpGet httpget = new HttpGet(JiraURL+getAllProjects);
	            System.out.println("Server URL "+JiraURL+getAllProjects);
	            httpget.addHeader("Content-type", "application/json");
	           
	            //
	            // Authentication
	            //
	            String userpass = loginName + ":" + loginPassword;
	  		  	
				String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
	  		  	httpget.addHeader("Authorization", basicAuth);
	           
	            
	            //
	            // Execute the JSON request to JIRA
	            //
	            HttpResponse response = httpclient.execute(httpget);
	            HttpEntity resEntity = response.getEntity();
	            System.out.println(" @@@@ Http Post : Response " + response.toString());

	            System.out.println("----------------------------------------");
	            String responseString = new BasicResponseHandler().handleResponse(response);
	            System.out.println(responseString);
	            
	            if (resEntity != null) {
	                System.out.println("Response content length: " + resEntity.getContentLength());
	                System.out.println("ID: " + resEntity.toString());
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
	        }   
	}// getAllProjects
	/**
     * This method Demonstrates getSpecificExecutionID().
     * <br>Pass in the jira query from the test suite and the Specific test case Key, and returned the executionId for the test key
     * @param testCycleName - Test case key QATEST-1234
     * @param testCaseID -  passed/failed/wip/blocked
     * @param status - status of the test
     */
	public void setTestCaseExecutionStatus(String testCycleName,String testCaseID, String status){
		StringEntity params = null;
		String executionID = null;
		String issueID = null;
		String[] splitData = null;
		String zapiURL = null;
		HttpPut putRequest = null;
		HttpGet getRequest = null;
		HttpResponse response = null;
		String responseString = null;
		String userpass = loginName + ":" + loginPassword;
		//
		// Three calls to set the test case ID within the correct test cycle
		// GET /rest/api/2/issue/"test case id"
		// EXAMPLE:   http://jira.3pd.com:8080/rest/api/2/issue/QATEST-2895
		// Property File entry: getIssueID
		//
		// This returns all the information for the jira issue. You want the id EXAMPLE:  "id": "72119",
		//
		// The next call returns all the test cycles where the issue ID shows up
		//
		//  You want to find the  "id": 6728, that matches the testCycleName from the param above  "cycleName": "Automation Testing One",
		//
		//
		// GET rest/zapi/latest/execution?isssueid=
		// EXAMPLE:  http://jira.3pd.com:8080/rest/zapi/latest/execution?issueId=72119
		// Property File entry: getExecutionInformation
		// 
		// The last call sets the status
		// PUT /rest/zapi/latest/execution/6728/execute?
		// payload = {"executions": ["6728"],"status": "3"}
		// EXAMPLE:  http://jira.3pd.com:8080/rest/zapi/latest/execution/6728/execute?
		// Property File entry: putExecutionStatus
		//
		
		//
		//
		//
		CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        
		try {
        	//
    		// STEP 1  Get the Issue ID
    		//
        	zapiURL = new String(String.format(getIssueID,testCaseID));
        	getRequest = new HttpGet(JiraURL+zapiURL);
            System.out.println("Server URL "+JiraURL+zapiURL);
            getRequest.addHeader("Content-type", "application/json");
            //
            // Authentication
            //
           
  		  	
  		  	String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
  		  	getRequest.addHeader("Authorization", basicAuth);
  		  	//
            // Execute the JSON request to JIRA
            //
            response = httpclient.execute(getRequest);
            HttpEntity resEntity = response.getEntity();
            System.out.println(" @@@@ Http Post : Response " + response.toString());

            System.out.println("----------------------------------------");
            responseString = new BasicResponseHandler().handleResponse(response);
            System.out.println(responseString);
            //
            // Simple way to split up the response to find the testkey and executionID
            //
            splitData = responseString.split(",");
            for(int count = 0; count < splitData.length;count++){
            	String entity = new String(splitData[count].toString());
            	if(entity.contains("\"id\"")){ // "id": "72119",
            		issueID = new String(entity.substring(5));
            		issueID = issueID.substring(1, issueID.length()-1); // Strip double quotes
            		break;
            	}
            }
            if(null == issueID){
            	EntityUtils.consume(resEntity);
                httpclient.close();
            	return;
            }
  		  	//
  		  	// Step 2 get the execution ID
  		  	//
  		  	
        	zapiURL = new String(String.format(getExecutionInformation,issueID));
        	getRequest = new HttpGet(JiraURL+zapiURL);
            System.out.println("Server URL "+JiraURL+zapiURL);
            getRequest.addHeader("Content-type", "application/json");
            //
            // Authentication
            //
           
  		  	
  		  	String basicAuth1 = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
  		  	getRequest.addHeader("Authorization", basicAuth1);
  		  	//
            // Execute the JSON request to JIRA
            //
            response = httpclient.execute(getRequest);
            resEntity = response.getEntity();
            System.out.println(" @@@@ Http Post : Response " + response.toString());

            System.out.println("----------------------------------------");
            responseString = new BasicResponseHandler().handleResponse(response);
            System.out.println(responseString);
            //
            // Simple way to split up the response to find the testkey and executionID
            //
            Boolean enteredExecutions = false;
            splitData = responseString.split(",");
            for(int count = 0; count < splitData.length;count++){
            	String entity = new String(splitData[count].toString());
            	
            	if(entity.contains("executions")) enteredExecutions = true;
            	
            	if(enteredExecutions){
            	
	            	if(entity.contains("\"id\"")){ // "id": "72119",
	            		executionID = new String(entity.substring(entity.length()-4));
	            		// executionID = executionID.substring(1, executionID.length()-1); // Strip double quotes
	            	}
	            	//
	            	// match the cycle name and we are done  
	            	// "cycleName": "Automation Testing",
	            	//
	            	if(entity.contains("\"cycleName\"")){ // "id": "72119",
	            		String cycleName = new String(entity.substring(12));
	            		cycleName = cycleName.substring(1, cycleName.length()-1); // Strip double quotes
	            		if(cycleName.equalsIgnoreCase(testCycleName)){
	            			break; // no more need to search for ID
	            		}else{
	            			executionID = null;
	            		}
	            	}
            	}
            }
            //
            // No matching cycle
            //
            if(null == executionID){
            	EntityUtils.consume(resEntity);
                httpclient.close();
            	return;
            }
  		  	//
  		  	// Step 3 Set the Execution Status
  		  	//
            zapiURL = new String(String.format(putExecutionStatus,executionID));
        	putRequest = new HttpPut(JiraURL+zapiURL);
            System.out.println("Server URL "+JiraURL+zapiURL);
            putRequest.addHeader("Content-type", "application/json");
            //
            // Authentication
            //
           
  	
  		  	String basicAuth2 = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
  		  	putRequest.addHeader("Authorization", basicAuth2);
            
            
            
  		  	if(sTestJiraFactory.testCasePassed.equals(status)){
  		  		System.out.println("Payload is "+ "{\"executions\":\""+ executionID+"\"],\"status\":\"2\"}");
  		  		params = new StringEntity("{\"executions\":[\""+executionID+"\"],\"status\":\"1\"}","UTF-8");
  		  	}else if(sTestJiraFactory.testCaseFailed.equals(status)){
  		  		System.out.println("Payload is "+ "{\"executions\":[\""+ executionID+"\"],\"status\":\"2\"}");
  		  		params = new StringEntity("{\"executions\":[\""+executionID+"\"],\"status\":\"2\"}","UTF-8");
  		  	}else if(sTestJiraFactory.testCaseInProgress.equals(status)){
  		  	System.out.println("Payload is "+ "{\"executions\":[\""+ executionID+"\"],\"status\":\"3\"}");
  		  		params = new StringEntity("{\"executions\":[\""+executionID+"\"],\"status\":\"3\"}","UTF-8");
  		  	}else 
  		  	if(sTestJiraFactory.testCaseBlocked.equals(status)){
  		  	System.out.println("Payload is "+ "{\"executions\":[\""+ executionID+"\"],\"status\":\"4\"}");
  		  		params = new StringEntity("{\"executions\":[\""+executionID+"\"],\"status\":\"4\"}","UTF-8");
  		  	}else {
  		  		System.out.println("Invalid Status");
  		  		httpclient.close();
  		  		return;
  		  	}	
  		  	//
  		  	// Process the request
  		  	//
		  	params.setContentEncoding("UTF-8");
		  	params.setContentType("application/json; charset=utf-8");
		  	putRequest.setEntity(params);
  		  	//
            // Execute the JSON request to JIRA
            //
            response = httpclient.execute(putRequest);
            resEntity = response.getEntity();
            System.out.println(" @@@@ Http Post : Response " + response.toString());
            
            if (resEntity != null) {
                System.out.println("Response content length: " + resEntity.getContentLength());
                System.out.println("ID: " + resEntity.toString());
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
        }   
		
	}// setTestCaseExecutionStatus
	
	/**
     * This method Demonstrates updateJiraTestComment().
     * <br>When the automation test completes, update the test status PASS/FAIL from within a test suite.
     * <br> Following URL for help:  https://docs.atlassian.com/jira/REST/7.0-SNAPSHOT/#api/2/issue-addComment
     * @param jiraTestID  test ID to be used
     * @param title  comment  comment to be added to the issue
     * @param comment comment to add
     * @param owner who is making the change
     */
	public void updateJiraTestComment(String jiraTestID,String title,String comment,String owner){
		updateJiraComment(jiraTestID,title +comment+owner);
	}//updateJiraTestComment
	
	/**
     * This method Demonstrates createTestCaseWithSteps().
     * <br>Creates a new test case in the provided project
     * <br> Following URL for help:  https://docs.atlassian.com/jira/REST/7.0-SNAPSHOT/#api/2/issue-addComment
     * @param projectKey key to the project in the jira repository
	 * @param  summary  summary of the change
     * @param  description  detail
     */
	public void createTestCaseWithSteps(String projectKey,String summary, String description){
		 CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		    //
		    // Creating a test case does not require a string replacement in the URL
		    //
		    // String NewUpdateCommentURL = new String(updateCommentURL.replaceAll(updateCommentURLPattern,issueID));
			
	        try {
	        	
	            HttpPost httppost = new HttpPost(JiraURL+createTestCaseURL);
	            System.out.println("Server URL "+JiraURL+createTestCaseURL);
	            httppost.addHeader("Content-type", "application/json");
	           
	            //
	            // Authentication
	            //
	            String userpass = loginName + ":" + loginPassword;
				String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
	  		  	httppost.addHeader("Authorization", basicAuth);
	            //
	  		  	// payload
	  		  	//
	  		    // {"fields": {"project": {"key": issueProjectKey},"summary": issueSummary,"description": issueDescription,"issuetype": {"name": issueTypeName}}}

	  		  	
	  		  	
	  		  	System.out.println("Payload is: "+ "{\"fields\":{\"project\":{\"key\":\""+ projectKey +"\"},\"summary\":\""+summary+"\",\"description\":\""+description+"\",\"issuetype\":{\"name\":\"Test\"}}}");
	  		  	// StringEntity params = new StringEntity("{\"fields\":{\"project\":{\"key\":\""+ projectKey +"\"},\"summary\":\""+summary+"\",\"description\":\""+description+"\",\"issuetype\":{\"name\":\"Test\"}}}","UTF-8");
	  		  	//StringEntity params = new StringEntity("{\"fields\":{\"project\":{\"key\":\""+ projectKey +"\"},\"summary\":\""+summary+"\",\"issuetype\":{\"name\":\"Test\"}}}","UTF-8");
	  		  	String payload;
	  		  	payload = String.format(createTestCasePayLoad,projectKey, summary);
	  		  	StringEntity params = new StringEntity(payload,"UTF-8");
	  		  	params.setContentType("application/json; charset=utf-8");
	            httppost.setEntity(params);
	            
	            //
	            // Execute the JSON request to JIRA
	            //
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity resEntity = response.getEntity();
	            System.out.println(" @@@@ Http Post : Response " + response.toString());
	            
	            if (resEntity != null) {
	                System.out.println("Response content length: " + resEntity.getContentLength());
	                System.out.println("ID: " + resEntity.toString());
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
	        }   
		
	}//createTestCaseWithSteps 
	
	/**
     * This method Demonstrates updateTestStatusFromTestSuite().
     * <br>When the automation test completes, update the test status PASS/FAIL from within a test suite.
     * <br> Following URL for help:  https://docs.atlassian.com/jira/REST/7.0-SNAPSHOT/#api/2/issue-addComment
     * @param testID  Jira ID
     * @param StepInfo comment  comment to be added to the issue
     * @param testData data for the step
     * @param results expected results
     */
	public void addTestStepsToATest(String testID, String StepInfo, String testData, String results){
		
		 CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		
		 try {
	        	
	            HttpPost httppost = new HttpPost(JiraURL+addTestStepToATestURL);
	            System.out.println("Server URL "+JiraURL+addTestStepToATestURL);
	            httppost.addHeader("Content-type", "application/json");
	           
	            //
	            // Authentication
	            //
	            String userpass = loginName + ":" + loginPassword;
				String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
	  		  	httppost.addHeader("Authorization", basicAuth);
	            //
	  		  	// payload
	  		  	//
	  		  	
	  		  	
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
	        	
	        }   
	  		  	
	  		  	
	}// addTestStepsToATest
	
	/**
     * This method Demonstrates updateJiraComment().
     * <br>Updates the Jira ID with the supplied comment
     * <br> Following URL for help:  https://docs.atlassian.com/jira/REST/7.0-SNAPSHOT/#api/2/issue-addComment
     * @param issueID  Jira ID
     * @param comment  comment to be added to the issue
     * @exception PatternSyntaxException if url can't be completed due replaceAll call
     */
	public void updateJiraComment(String issueID, String comment) throws PatternSyntaxException{
		   
		    CloseableHttpClient httpclient = HttpClientBuilder.create().build();
		    //
		    // Need to find {issueIdOrkey} /rest/api/2/issue/{issueIdOrKey}/comment
		    // and replace with issueID
		    //
		    String NewUpdateCommentURL = new String(updateCommentURL.replaceAll(updateCommentURLPattern,issueID));
			
	        try {
	        	
	            HttpPost httppost = new HttpPost(JiraURL+NewUpdateCommentURL);
	            System.out.println("Server URL "+JiraURL+NewUpdateCommentURL);
	            httppost.addHeader("Content-type", "application/json");
	           
	            //
	            // Authentication
	            //
	            String userpass = loginName + ":" + loginPassword;
				String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
	  		  	httppost.addHeader("Authorization", basicAuth);
	            //
	  		  	// payload
	  		  	//
	  		  	System.out.println("Payload is "+ "{\"body\""+":"+"\""+comment+"\"}");
	  		  	StringEntity params = new StringEntity("{\"body\" : \""+comment+"\"}","UTF-8");
	          
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
	}// updateJiraComment
	
	/**
     * This method Demonstrates selfTest().
     * <br>This function is used to selfTest oTestHTTPFactory by downloading all the team rooms for the credentials in the 
     * <br>  properties file
     */
	@SuppressWarnings("unused")
	public void selfTest(){
		
		String testCycleName = new String("Automation Testing");
		String testCaseID = new String("QATEST-2899");
		System.out.println("adding comment to "+ testCaseID);
		updateJiraTestComment(testCaseID,"Bichromte:STestJiraFactory:updateJiraComment","Bichromate unit tests,Test Cycle Automated Testing setting test case to pass ","dramer");
		// System.out.println("changing testing status of "+ testCaseID +", in "+ testCycleName+" testing cycle");
		// setTestCaseExecutionStatus(testCycleName,testCaseID, sTestJiraFactory.testCasePassed);
			
		 
	} //selfTest
	
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			
			sTestJiraFactory jiraFactory = null;
			
			jiraFactory = new sTestJiraFactory();
			if(jiraFactory != null){
				
				jiraFactory.selfTest();
				
			}
			
		} // end Main
	 } // end Inner class Test
}
