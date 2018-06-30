package bichromate.httpListner;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import bichromate.core.sTestOSInformationFactory;
import bichromate.core.sTestResourceStoreManager;
import bichromate.dataStore.sTestResourceStore;




@SuppressWarnings("restriction")
public class sTestHTTPListnerFactory implements Runnable {
	private HttpServer server = null;
	//private HttpsServer  sServer = null;
	//private SSLContext = null;
	private int mainPort = 8000;
	private static sTestResourceStoreManager rsm = null;
	
	public sTestHTTPListnerFactory(int port){
		mainPort = port;
		rsm = new sTestResourceStoreManager();
		if(rsm != null)
			try {
				rsm.readPropertiesFile("Bichromate.properties");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}//sTestHTTPListnerFactory
	public boolean startServer()throws IOException{
		
		server = HttpServer.create(new InetSocketAddress(mainPort), 0);
		
	    if(null != server){
	    	server.createContext("/test-output/screencapture", new MyHandlerScreenCapture());
	    	server.createContext("/", new MyHandlerRoot());
	    	server.createContext("/reports", new MyHandlerReports());
	    	server.createContext("/logs", new MyHandlerLogs());
	    	server.createContext("/properties", new MyHandlerProperties());
	    	server.createContext("/propertiesForm", new MyHandlerPropertiesForm());
	    	server.createContext("/logo.png", new MyHandlerImages());
	    	server.createContext("/ExtendReport", new MyHandlerExtend());
	    	server.createContext("/TestNGReports", new MyHandlerTestNGReports());
	    	
	    	server.start();
	    	
	    	System.out.println("bichromateHTTPListnerFactory:Test - is running");
 			System.out.println("bichromateHTTPListnerFactory:Test - Execute it and go to http://localhost:8000/reports?reportName=<reports-2017-06-22-22-19-46.html> and you'll see the following response:This is the response");
 			System.out.println("bichromateHTTPListnerFactory:Test - Execute it and go to http://localhost:8000/properties You will see all the properties for Bichromate");
 			System.out.println("bichromateHTTPListnerFactory:Test - Execute it and go to http://localhost:8000/propertiesForm You will see a form to edit Bichromate properties");
 			System.out.println("bichromateHTTPListnerFactory:Test - Execute it and go to http://localhost:8000/ExtendReport You will see the Extend Reports");
 			System.out.println("bichromateHTTPListnerFactory:Test - Execute it and go to http://localhost:8000/TestNGReports You will see the testNG Report");
 			System.out.println("bichromateHTTPListnerFactory:Test - Execute it and go to http://localhost:8000/ redirected to http://www/bichromate.org");
 			System.out.println("bichromateHTTPListnerFactory:Test - Execute it and go to http://localhost:8000/logs Displays the Bichromate Log Files");
	 	
	    	return true;
	    }else{
	    	System.out.println("bichromateHTTPListnerFactory:bichromateHTTPListnerFactory - Failed to createt");
	    }
		return false;
	}
	
	public void run() {
	       try {
			startServer();
		} catch (IOException e) {
			System.out.println("bichrmateHTTPListner:run -Failed to start the HTTPServer");
			e.printStackTrace();
		}
	}//run
	public void stopServer(){
		server.stop(0);
	}
	static class MyHandlerRoot implements HttpHandler {
		
        public void handle(HttpExchange t) throws IOException {
			int responseCode = 302;
			List<String> values = new ArrayList<String>();
			
            String response = "Redirecting to http://www.bichromate.org";
           
            String path = new String(t.getRequestURI().toString());
           
            if(path.contains("?")){ // generate 404 error page
            	 System.out.println("parameters: "+path);
            }
            /*
            
         // return some cookies so we can check getHeaderField(s)
            Headers respHeaders = t.getResponseHeaders();
            List<String> values = new ArrayList<>();
            values.add("ID=JOEBLOGGS; version=1; Path=" + URI_PATH);
            values.add("NEW_JSESSIONID=" + (SESSION_ID+1) + "; version=1; Path="
                       + URI_PATH +"; HttpOnly");
            values.add("NEW_CUSTOMER=WILE_E_COYOTE2; version=1; Path=" + URI_PATH);
            respHeaders.put("Set-Cookie", values);
            values = new ArrayList<>();
            values.add("COOKIE2_CUSTOMER=WILE_E_COYOTE2; version=1; Path="
                       + URI_PATH);
            respHeaders.put("Set-Cookie2", values);
            values.add("COOKIE2_JSESSIONID=" + (SESSION_ID+100)
                       + "; version=1; Path=" + URI_PATH +"; HttpOnly");
            respHeaders.put("Set-Cookie2", values);

            t.sendResponseHeaders(200, -1);
            t.close();
            
            */
           
	        Headers h = t.getResponseHeaders();
	        h.set("Content-Type","text/html");
	        h.set("location", "http://www.bichromate.org");
	        
            
	        t.sendResponseHeaders(responseCode, response.length());
	        OutputStream os = t.getResponseBody();
	        os.write(response.getBytes());
	        os.close();
        }//handle
	}//MyHandlerRoot
	static class MyHandlerLogs implements HttpHandler {
		
        public void handle(HttpExchange t) throws IOException {
        	int responseCode = 200;
            String response = "The following Report will be displayed: ";
           
            try{
            	response = new String(readBichromteLogFiles());
            }catch(IOException e){
            	System.out.println("Failed to read MyHandlerProperties");
            	response = new String("Bichromate Error: bichromateHTTPListnerFactory:MyHandlerLogs - Failed to read web driver log files");
            }
            
            Headers h = t.getResponseHeaders();
            h.set("Content-Type","text/html");
            
	    	  t.sendResponseHeaders(responseCode, response.length());
	          OutputStream os = t.getResponseBody();
	          os.write(response.getBytes());
	          os.close();
	
        }//handle
	}//MyHandlerLogs
	static class MyHandlerTestNGReports implements HttpHandler {
		
        public void handle(HttpExchange t) throws IOException {
        	int responseCode = 200;
            String response = "The following Report will be displayed: ";
           
            try{
            	response = new String(readTestNGReport());
            }catch(IOException e){
            	System.out.println("Failed to read emailable-report.html");
            	response = new String("Bichromate Error: bichromateHTTPListnerFactory:MyHandlerTestNGReports - Failed to read emailable-report.html");
            }
            
            Headers h = t.getResponseHeaders();
            h.set("Content-Type","text/html");
            
	    	  t.sendResponseHeaders(responseCode, response.length());
	          OutputStream os = t.getResponseBody();
	          os.write(response.getBytes());
	          os.close();
        }//handle
	}//MyHandlerTestNGReports
	static class MyHandlerExtend implements HttpHandler {
		
        public void handle(HttpExchange t) throws IOException {
        	int responseCode = 200;
            String response = "The following Report will be displayed: ";
           
            try{
            	response = new String(readExtendReport());
            }catch(IOException e){
            	System.out.println("Failed to read Extend.html");
            	response = new String("Bichromate Error: bichromateHTTPListnerFactory:MyHandlerExtend - Failed to read Extend.html");
            }
            
            Headers h = t.getResponseHeaders();
            h.set("Content-Type","text/html");
            
	    	  t.sendResponseHeaders(responseCode, response.length());
	          OutputStream os = t.getResponseBody();
	          os.write(response.getBytes());
	          os.close();
        }//handle
	}//MyHandlerExtend
	static class MyHandlerProperties implements HttpHandler {
		
        public void handle(HttpExchange t) throws IOException {
        	int responseCode = 200;
            String response = "The following Report will be displayed: ";
           
            try{
            	response = new String(readProperties());
            }catch(IOException e){
            	System.out.println("Failed to read MyHandlerProperties");
            	response = new String("Bichromate Error: bichromateHTTPListnerFactory:MyHandlerProperties - Failed to read Bichromate.properties");
            }
            
            Headers h = t.getResponseHeaders();
            h.set("Content-Type","text/html");
            
	    	  t.sendResponseHeaders(responseCode, response.length());
	          OutputStream os = t.getResponseBody();
	          os.write(response.getBytes());
	          os.close();
        }//handle
	}//MyHandlerProperties
	static class MyHandlerPropertiesForm implements HttpHandler {
		
        public void handle(HttpExchange t) throws IOException {
        	int responseCode = 200;
        	String propertiesDetail = null;
            String response = "The following Properties will be displayed: ";
           
            String path = new String(t.getRequestURI().toString());
            System.out.println("parameters: "+path);
            if(path.contains("?")){
            	propertiesDetail = new String(path.substring(path.indexOf("?")+1));
            	System.out.println(propertiesDetail);
            	try{
            		// parameters: /propertiesForm?Bichromate+Properties=testExecutionLogFactory.directory&BichromatePropertyInput=ddddd
            		// Bichromate+Properties=testExecutionLogFactory.directory&BichromatePropertyInput=ddddd
            		updatePropertiesFile(propertiesDetail);
            		response = new String(readPropertiesForm());
            	 }catch(IOException e){
                 	System.out.println("Failed to read BichrmateForms.html");
                 	response = new String("Bichromate Error: bichromateHTTPListnerFactory:MyHandlerPropertiesForm - Failed to read BichromateForms.html");
                 }
            	response = new String(response +"<h2>Configured Bichromate Properties</h2> <p class=\"dotted\"></p>");
            	// Strip the properties
            	// Update the properties
            	//
            }else{
            	response = new String(readPropertiesForm());
            }
            
            Headers h = t.getResponseHeaders();
            h.set("Content-Type","text/html");
            
	    	  t.sendResponseHeaders(responseCode, response.length());
	          OutputStream os = t.getResponseBody();
	          os.write(response.getBytes());
	          os.close();
        }//handle
	}//MyHandlerPropertiesForm
	
	static class MyHandlerReports implements HttpHandler {
		
       
        public void handle(HttpExchange t) throws IOException {
        	int responseCode = 200;
            String response = "The following Report will be displayed: ";
            String reportName = null;
            String path = new String(t.getRequestURI().toString());
            System.out.println("parameters: "+path);
            if(path.contains("reportName=")){
            	reportName = new String(path.substring(path.indexOf("=")+1));
            	System.out.println(reportName);
            	try{
            		response = new String(readReport(reportName));
            	}catch(IOException e){
            		System.out.println("Failed to read <xxxx>.html");
            		response = new String("Bichromate Error: bichromateHTTPListnerFactory:MyHandlerReports - Failed to read <xxx>.html");
            	}
            }else{
            	response = new String("Malformed URL");
            	responseCode = 204;
            }
            
            	
            Headers h = t.getResponseHeaders();
            h.set("Content-Type","text/html");
            
	    	  t.sendResponseHeaders(responseCode, response.length());
	          OutputStream os = t.getResponseBody();
	          os.write(response.getBytes());
	          os.close();
                    
            
   
        }
	}//MyHandlerReports
	
	static class MyHandlerScreenCapture implements HttpHandler {
		 
	        public void handle(HttpExchange t) throws IOException {
			sTestOSInformationFactory osInfo = new sTestOSInformationFactory();
	    	
	   	 Headers headers = t.getResponseHeaders();
	        headers.add("Content-Type", "image/png");
	         
	        File file = new File (osInfo.getWWWDirectory()+"logo.png");
	        byte[] bytes  = new byte [(int)file.length()];
	        System.out.println(file.getAbsolutePath());
	        System.out.println("length:" + file.length());
	         
	        FileInputStream fileInputStream = new FileInputStream(file);
	        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
	        bufferedInputStream.read(bytes, 0, bytes.length);
	
	        t.sendResponseHeaders(200, file.length());
	        OutputStream outputStream = t.getResponseBody();
	        outputStream.write(bytes, 0, bytes.length);
	        outputStream.close();
	        bufferedInputStream.close();
		 }
	}//MyHandlerScreenCapture
	
	static class MyHandlerImages implements HttpHandler {
        
        public void handle(HttpExchange t) throws IOException {
        	sTestOSInformationFactory osInfo = new sTestOSInformationFactory();
        	
        	 Headers headers = t.getResponseHeaders();
             headers.add("Content-Type", "image/png");
              
             File file = new File (osInfo.getWWWDirectory()+"logo.png");
             byte[] bytes  = new byte [(int)file.length()];
             System.out.println(file.getAbsolutePath());
             System.out.println("length:" + file.length());
              
             FileInputStream fileInputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
             bufferedInputStream.read(bytes, 0, bytes.length);
  
             t.sendResponseHeaders(200, file.length());
             OutputStream outputStream = t.getResponseBody();
             outputStream.write(bytes, 0, bytes.length);
             outputStream.close();
             bufferedInputStream.close();
        }
	}//MyHandlerImages
	private static String readPropertiesForm() throws IOException{
		
		String everything = null;
		
		sTestOSInformationFactory osInfo = new sTestOSInformationFactory();
		
		BufferedReader br = new BufferedReader(new FileReader(osInfo.getWWWDirectory()+"propertiesForm.html"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    sTestResourceStore rowOfData = null;
		    while (line != null) {
		        sb.append(line);
		        
		        if(line.contains("<select name=\"Bichromate Properties\">")){
		        	for(int x = 0; x < rsm.resourceList.size(); x++){
		        		rowOfData = rsm.resourceList.get(x);
		        		if(!rowOfData.isComment){
		        			sb.append("<option value=\""+rowOfData.propertyName+" \">"+ rowOfData.propertyName+"</option>");
		        		}
		        	}
		        }
		        
		        line = br.readLine();
		    }
		    everything = sb.toString();
		    
		} finally {
		    br.close();
		   
		}
		
		
		return everything;
	}//readPropertiesForm
	private static String readTestNGReport() throws IOException{
		
		String everything = null;
		
		sTestOSInformationFactory osInfo = new sTestOSInformationFactory();
		
		String fileNameAndPath = new String(osInfo.getTestOutputDirectory()+"emailable-report.html");
		
		BufferedReader br = new BufferedReader(new FileReader(fileNameAndPath));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        // sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();
		    
		} finally {
		    br.close();
		    
		}
		
		
		return everything;
	}//readReport
	private static String readReport(String report) throws IOException{
		
		String everything = null;
		
		sTestOSInformationFactory osInfo = new sTestOSInformationFactory();
		
		BufferedReader br = new BufferedReader(new FileReader(osInfo.getReportsDirectory()+report));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        // sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();
		    
		} finally {
		    br.close();
		    
		}
		
		
		return everything;
	}//readReport
private static String readExtendReport() throws IOException{
		
		String everything = null;
		
		sTestOSInformationFactory osInfo = new sTestOSInformationFactory();
		
		BufferedReader br = new BufferedReader(new FileReader(osInfo.getReportsDirectory()+"Extent.html"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        // sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();
		    
		} finally {
		    br.close();
		   
		}
		
		
		return everything;
	}//readExtendReport

	private static String readBichromteLogFiles() throws IOException{
		
		String everything = null;
		String htmlHeaderForProperties = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"> <html> <head><title>Bichromate Log Files</title></head><style>body {padding-left: 5em;font-family: Georgia, \"Times New Roman\", Times, serif;color: green;background-color: #EBEBE3 }  h1{font-family: Helvetica, Geneva, Arial, SunSans-Regular, sans-serif }address {margin-top: 1em;padding-top: 1em;border-top: thin dotted }th, td {border-bottom: 1px solid #ddd;}tr:nth-child(even) {background-color: #f2f2f2}</style><body><!-- Main content --><img src=\"logo.png\" alt=\"Bichromate logo\" style=\"width:900px;height:299px;\"><h1>Bichromate Log Files</h1>";
		String htmlFooterForProperties ="<address>Created DWR1234<br>Powered By Bichromate.</address></body></html>";
		
		everything = new String(htmlHeaderForProperties);
		
		sTestOSInformationFactory osInfo = new sTestOSInformationFactory();
		
		
		BufferedReader br = new BufferedReader(new FileReader(osInfo.getLogFileDirectory()+"webDriverLogFactory.log"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line+System.getProperty("line.separator"));
		        //sb.append("<br />");
		        //sb.append(System.getProperty("line.separator"));
		        line = br.readLine();
		    }
		    everything = new String(everything+ "<xmp>"+sb.toString()+"</xmp>");
		    
		} finally {
		    br.close();
		}
		String date = new String(osInfo.getCurrentDateAndTimeWithSystemTimeZone());
		htmlFooterForProperties = htmlFooterForProperties.replace("DWR1234",date );
		everything = new String(everything+htmlFooterForProperties);
		
		
		
		return everything;
	}//readBichromteLogFiles
	 
private static String readProperties() throws IOException{
		
		String everything = null;
		String htmlHeaderForProperties = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"> <html> <head><title>Bichromate Properties Report</title></head><style>body {padding-left: 5em;font-family: Georgia, \"Times New Roman\", Times, serif;color: green;background-color: #EBEBE3 }  h1{font-family: Helvetica, Geneva, Arial, SunSans-Regular, sans-serif }address {margin-top: 1em;padding-top: 1em;border-top: thin dotted }th, td {border-bottom: 1px solid #ddd;}tr:nth-child(even) {background-color: #f2f2f2}</style><body><!-- Main content --><img src=\"logo.png\" alt=\"Bichromate logo\" style=\"width:900px;height:299px;\"><h1>Bichromate Properties Report</h1>";
		String htmlFooterForProperties ="<address>Created DWR1234<br>Powered By Bichromate.</address></body></html>";
		
		everything = new String(htmlHeaderForProperties);
		
		sTestOSInformationFactory osInfo = new sTestOSInformationFactory();
		
		
		BufferedReader br = new BufferedReader(new FileReader(osInfo.getPropertiesFileDirectory()+"Bichromate.properties"));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        //sb.append("<br />");
		        sb.append(System.getProperty("line.separator"));
		        line = br.readLine();
		    }
		    everything = new String(everything+ "<xmp>"+sb.toString()+"</xmp>");
		    
		} finally {
		    br.close();
		}
		String date = new String(osInfo.getCurrentDateAndTimeWithSystemTimeZone());
		htmlFooterForProperties = htmlFooterForProperties.replace("DWR1234",date );
		everything = new String(everything+htmlFooterForProperties);
		return everything;
	}//readReport
	// parameters: /propertiesForm?Bichromate+Properties=testExecutionLogFactory.directory&BichromatePropertyInput=ddddd
	// Bichromate+Properties=testExecutionLogFactory.directory&BichromatePropertyInput=ddddd
	private static void updatePropertiesFile(String propertiesDetail){
		String setting = null;
		String propertySetting = null;
		
		propertySetting = new String(propertiesDetail.substring(propertiesDetail.indexOf("Input=")+6));
		setting = new String(propertiesDetail.substring(propertiesDetail.indexOf("Properties="),propertiesDetail.indexOf("&")-1));
		propertySetting.replace("+", " ");
		System.out.println("Update Setting: "+setting +"  New Setting: "+propertySetting);
		
	}//updatePropertiesFile
	
	
	
	@SuppressWarnings("unused")
	private static boolean availablePort(int port) {
	    System.out.println("--------------Testing port " + port);
	    Socket s = null;
	    try {
	        s = new Socket("localhost", port);

	        // If the code makes it this far without an exception it means
	        // something is using the port and has responded.
	        System.out.println("--------------Port " + port + " is not available");
	        return false;
	    } catch (IOException e) {
	        System.out.println("--------------Port " + port + " is available");
	        return true;
	    } finally {
	        if( s != null){
	            try {
	                s.close();
	            } catch (IOException e) {
	                //throw new RuntimeException("You should handle this error." , e);
	            }
	        }
	    }
	}//availablePort
	
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public  static void main(final String[] args){
	 		
			(new Thread(new sTestHTTPListnerFactory(8000))).start();
	 		
	 	}//main
	 	
	 }//Test
	
	

}//bichromateHTTPListnerFactory
