package bichromate.confluence;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


/**
 * Demonstrates how to update a page using the Confluence 5.5 REST API.
 */
public class sTestConfluenceFactory {
    private static final String BASE_URL = "https://billfire.atlassian.net";
    private static final String USERNAME = "dramer";
    private static final String PASSWORD = "dramer32";
    private static final String ENCODING = "utf-8";

   public sTestConfluenceFactory(){
	   
   }

    @SuppressWarnings("deprecation")
	public void updateContent(String pageName, String parentPage) throws Exception
    {
        final long pageId = 98334;

        
        HttpClient client = HttpClientBuilder.create().build();

        // Get current page version
        String pageObj = null;
        HttpEntity pageEntity = null;
        try
        {
            HttpGet getPageRequest = new HttpGet(getContentRestUrl(pageId, new String[] {"body.storage", "version", "ancestors"}));
            HttpResponse getPageResponse = client.execute(getPageRequest);
            pageEntity = getPageResponse.getEntity();

            pageObj = IOUtils.toString(pageEntity.getContent());

            System.out.println("Get Page Request returned " + getPageResponse.getStatusLine().toString());
            System.out.println("");
            System.out.println(pageObj);
        }
        finally
        {
            if (pageEntity != null)
            {
                EntityUtils.consume(pageEntity);
            }
        }

        // Parse response into JSON
        JSONObject page = new JSONObject(pageObj);

        // Update page
        // The updated value must be Confluence Storage Format (https://confluence.atlassian.com/display/DOC/Confluence+Storage+Format), NOT HTML.
        page.getJSONObject("body").getJSONObject("storage").put("value", "hello, world");

        int currentVersion = page.getJSONObject("version").getInt("number");
        page.getJSONObject("version").put("number", currentVersion + 1);

        // Send update request
        HttpEntity putPageEntity = null;

        try
        {
            HttpPut putPageRequest = new HttpPut(getContentRestUrl(pageId, new String[]{}));

            StringEntity entity = new StringEntity(page.toString(), ContentType.APPLICATION_JSON);
            putPageRequest.setEntity(entity);

            HttpResponse putPageResponse = client.execute(putPageRequest);
            putPageEntity = putPageResponse.getEntity();

            System.out.println("Put Page Request returned " + putPageResponse.getStatusLine().toString());
            System.out.println("");
            System.out.println(IOUtils.toString(putPageEntity.getContent()));
        }
        finally
        {
            EntityUtils.consume(putPageEntity);
        }
    }
    private static String getContentRestUrl(final Long contentId, final String[] expansions) throws UnsupportedEncodingException
    {
        final String expand = URLEncoder.encode(StringUtils.join(expansions, ","), ENCODING);

        return String.format("%s/rest/api/content/%s?expand=%s&os_authType=basic&os_username=%s&os_password=%s", BASE_URL, contentId, expand, URLEncoder.encode(USERNAME, ENCODING), URLEncoder.encode(PASSWORD, ENCODING));
    }
    //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args)
    	{
			//sTestConfluenceFactory con = new sTestConfluenceFactory();
			try{
				
			}catch(Exception e)	{
				 System.out.println("Authentication Exception: "+e);
			}
    	}// main
	}//Test
}// sTestConfluenceFactory