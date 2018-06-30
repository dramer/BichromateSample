package bichromate.DBAccess;


//Use the JDBC driver  
import java.sql.*;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.zip.ZipException;  

@SuppressWarnings("unused")
public class sTestMicrosoftSQLConnector {
	
	private static ResourceBundle resources;
	private String sqlServerURL = null;
	
	private String databaseName =  null;
	private String user =  null;
	private String password = null;
	private String encrypt =  null;
	private String trustServerCertificate = null;
	private Connection connection = null;
	private  String connectionString = null;
	
	
	 static
	 {
			try
			{
				resources = ResourceBundle.getBundle("common.sTestMicrosoftSQLConnector",Locale.getDefault());
			} catch (MissingResourceException mre) {
				System.out.println("sTestMicrosoftSQLConnector.properties not found: "+mre);
				System.exit(0);
			}
	 }
	
	 /**
	  * This class Demonstrates sTestMicrosoftSQLConnector().
	  * <br>This class constructor looks for the resource bundle in the common directory of the resource folder. Should only be used for testing
	  * <br> The constructor sets up the zip directories
	  * <br>
	  */ 
	 public sTestMicrosoftSQLConnector(){
		 
		 setup(resources);
		 
	 }//sTestMicrosoftSQLConnector
	 public sTestMicrosoftSQLConnector(ResourceBundle remoteResources){
		 setup(remoteResources);
		 
	 }//sTestMicrosoftSQLConnector
	 private void setup(ResourceBundle remoteResources){
		 
		 sqlServerURL = new String(remoteResources.getString("sTestMicrosoftSQLConnector.zipDirectory"));
		 databaseName =  new String( remoteResources.getString("sTestMicrosoftSQLConnector.zipDirectory"));
		 user =   new String( remoteResources.getString("sTestMicrosoftSQLConnector.zipDirectory"));
		 password =  new String( remoteResources.getString("sTestMicrosoftSQLConnector.zipDirectory"));
		 encrypt =   new String( remoteResources.getString("sTestMicrosoftSQLConnector.zipDirectory"));
		 trustServerCertificate =  new String( remoteResources.getString("sTestMicrosoftSQLConnector.zipDirectory"));
		 
		// Server=3P-ATL-DB-01\VPRODUCTION01;Database=FC_MDT;User Id=USERNAME;Password=PASSWORD;

		// Server=3P-ATL-DB-01.sta.3pdelivery.com\VPRODUCTION01;Database=FC_MDT;User Id=USERNAME;Password=PASSWORD;

		 connectionString =  
	                "jdbc:sqlserver://3P-ATL-DB-01/VPRODUCTION01:1433;"  
	                + "database=FC_MDT;"  
	                + "user=USERNAME;"  
	                + "password=PASSWORD;"  
	                + "encrypt=true;"  
	                + "trustServerCertificate=false;"  
	                + "hostNameInCertificate=*.database.windows.net;"  
	                + "loginTimeout=30;";
	                
		 /*
		 
		 connectionString =  
	                "jdbc:sqlserver://yourserver.database.windows.net:1433;"  
	                + "database=AdventureWorks;"  
	                + "user=yourusername@yourserver;"  
	                + "password=yourpassword;"  
	                + "encrypt=true;"  
	                + "trustServerCertificate=false;"  
	                + "hostNameInCertificate=*.database.windows.net;"  
	                + "loginTimeout=30;";
	                */
		 
	 }//setup
	 private void makeConnection(){
		 try {  
             connection = DriverManager.getConnection(connectionString);  

         }  
         catch (Exception e) {  
             e.printStackTrace();  
         }  
         finally {  
             if (connection != null) try { connection.close(); } catch(Exception e) {}  
         }  
	 }//makeConnection
	 
	 private void selfTest(){
		
		 

	            

	            
		
	}
	 //
	 // Inner class for testing on the command line
	 //
	 public static class Test
	 {
		 public static void main(String[] args) throws ZipException {
			 
			 
			 sTestMicrosoftSQLConnector connector = new sTestMicrosoftSQLConnector();
			 connector.selfTest();
			 
		 }//main
	 }//Test

}//sTestMicrosoftSQLConnector
