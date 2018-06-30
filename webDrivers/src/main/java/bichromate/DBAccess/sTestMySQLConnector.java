/*
 * sTestMySQLConnector.java	1.0 2016/08/24
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



package bichromate.DBAccess;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import com.mysql.cj.jdbc.MysqlDataSource;

import bichromate.core.sTestOSInformationFactory;


public class sTestMySQLConnector {
	
	
	private sTestOSInformationFactory path = null;
	private static Session session = null;
	private static ResourceBundle resources;
	private Connection connection = null;
	private String serverName = null;
	private String mydatabaseSchema = null;
	private String javaDriverName = null;
	private String username = null;
	private String password = null;
	private String useSSL = null;
	private String port = null;
	private String localQueryToExecute = null;
	private String sshQueryToExecute = null;
	private String sshUser = null;
	private String sshHost  = null;
	private String remoteDBHost = null;
	private String remotePortString  = null;
	private String localPortString  = null;
	private int remotePort = 0;
	private int localPort = 0;
	private String sshDBServerName  = null;
	private String sshDBLogin  = null;
	private String sshDBPassword = null;
	
	static
	{
		try
		{
			resources = ResourceBundle.getBundle("DBAccess.sTestMySQLConnector",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestMySQLConnector.properties not found: "+mre);
			System.exit(0);
		}
	}
	public sTestMySQLConnector(ResourceBundle myResources){
		
		setupParameters(myResources);
			
	 
	}// sTestMySQLConnector
	public sTestMySQLConnector(){
	
		setupParameters(resources);
			
	 
	}// sTestMySQLConnector
	private void setupParameters(ResourceBundle resources){
		
		
		//
		// Local Server information
		//
		serverName = new String(resources.getString("sTestMySQLConnector.serverName"));
		mydatabaseSchema = new String(resources.getString("sTestMySQLConnector.mydatabaseSchema"));
		username  = new String(resources.getString("sTestMySQLConnector.username"));
		password  = new String(resources.getString("sTestMySQLConnector.password"));
		port  = new String(resources.getString("sTestMySQLConnector.port"));
		//
		// SSH DB Connection information
		//
		sshUser = new String(resources.getString("sTestMySQLConnector.sshuser"));
		sshHost = new String(resources.getString("sTestMySQLConnector.sshHost"));
		remoteDBHost = new String(resources.getString("sTestMySQLConnector.remoteDBHost"));
		remotePortString = new String(resources.getString("sTestMySQLConnector.remotePort"));
		remotePort = Integer.parseInt(remotePortString);
		localPortString = new String(resources.getString("sTestMySQLConnector.localPort"));
		localPort = Integer.parseInt(localPortString);
		sshDBServerName = new String(resources.getString("sTestMySQLConnector.sshDBServerName"));
		sshDBLogin = new String(resources.getString("sTestMySQLConnector.SSHDBLogin"));
		sshDBPassword= new String(resources.getString("sTestMySQLConnector.SSHDBPassword"));
		//
		// test Queries to run
		//
		
		localQueryToExecute = new String(resources.getString("sTestMySQLConnector.localSampleTestQuery"));
		sshQueryToExecute = new String(resources.getString("sTestMySQLConnector.SampleSSHConnectionQuery"));
		//
		// SQL className
		//
		javaDriverName = new String(resources.getString("sTestMySQLConnector.driverName"));
		
		
		path = new sTestOSInformationFactory();
		
	}
	/**
	 * This method demonstrates makeLocalDBConnection(). Use the following parameters in the properties file
	 * <br> Makes a DB connection to a local DB instance.
	 * <br> sTestMySQLConnector.serverName
	 * <br> sTestMySQLConnector.mydatabaseSchema
	 * <br> sTestMySQLConnector.username
	 * <br> sTestMySQLConnector.password
	 * <br>
	 * @throws ClassNotFoundException - failed to load the SQL driver
	 * @throws SQLException - failed to make SQL connection
	 * @author davidwramer
	 * @version 1.0
	 */
	public void makeLocalDBConnection() throws ClassNotFoundException, SQLException{
			useSSL = "false";
		 Class.forName(javaDriverName);
		 
		 String url = "jdbc:mysql://" + serverName + ":"+port+"/" + mydatabaseSchema+"?useSSL="+useSSL; 
		 System.out.println("Connecting to: "+url);
		 //jdbc:mysql://localhost:3306/javabase
		  
		 connection = DriverManager.getConnection(url, username, password);
		 String schema = connection.getSchema();
		 System.out.println("Schema Name: "+schema);
		 String defaultCatalog = connection.getCatalog();
		 System.out.println("Default Catalog: "+defaultCatalog);
		 
	}//makeDBConnection
	/**
	 * This method demonstrates makeLocalDBConnection(). Use the following parameters in the properties file
	 * <br> Makes a DB connection to a local DB instance.
	 * <br> sTestMySQLConnector.serverName
	 * <br> sTestMySQLConnector.mydatabaseSchema
	 * <br> sTestMySQLConnector.username
	 * <br> sTestMySQLConnector.password
	 * <br>
	 * @throws ClassNotFoundException - failed to load the SQL driver
	 * @throws SQLException - failed to make SQL connection
	 * @return boolean - true if connection was made, false otherwise
	 * @author davidwramer
	 * @version 1.0
	 */
	public boolean makeDBConnection() throws ClassNotFoundException, SQLException{
		 
	       
	        String localSSHUrl = "localhost";
	        try {

	            //
	        	// mysql database connectivity
	        	// 
	            MysqlDataSource dataSource = new MysqlDataSource();
	            dataSource.setServerName(localSSHUrl);
	            dataSource.setPortNumber(localPort);
	            dataSource.setUser(sshDBLogin);
	            dataSource.setPassword(sshDBPassword);
	            dataSource.setDatabaseName(sshDBServerName);
	            dataSource.setLoginTimeout(20);
	           

	            connection = dataSource.getConnection();

	            System.out.print("Connection to server successful!:" + connection + "\n\n");
	            String schema = connection.getSchema();
	   		 	System.out.println("Schema Name: "+schema);
	   		 	String defaultCatalog = connection.getCatalog();
	   		 	System.out.println("Default Catalog: "+defaultCatalog);
	        } catch (Exception e) {
	            e.printStackTrace();
	            CloseSSHConnection();
	            return false;
	        }
		return true;
		 
	}//makeDBConnection
	private boolean makeDBConnection(String DBName) throws ClassNotFoundException, SQLException{
		 
	       
        String localSSHUrl = "localhost";
        try {

            //
        	// mysql database connectivity
        	// 
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(localSSHUrl);
            dataSource.setPortNumber(localPort);
            dataSource.setUser(sshDBLogin);
            dataSource.setPassword(sshDBPassword);
            dataSource.setDatabaseName(DBName);
            dataSource.setLoginTimeout(20);
           

            connection = dataSource.getConnection();

            System.out.print("Connection to server successful!:" + connection + "\n\n");
            String schema = connection.getSchema();
   		 	System.out.println("Schema Name: "+schema);
   		 	String defaultCatalog = connection.getCatalog();
   		 	System.out.println("Default Catalog: "+defaultCatalog);
        } catch (Exception e) {
            e.printStackTrace();
            CloseSSHConnection();
            return false;
        }
	return true;
	 
}//makeDBConnection
	/*
	 * 
	 * 
	 * http://stackoverflow.com/questions/1968293/connect-to-remote-mysql-database-through-ssh-using-java
	 * 
	 * 
	 * http://blog.trackets.com/2014/05/17/ssh-tunnel-local-and-remote-port-forwarding-explained-with-examples.html
	 * 
	 */
	private void connectSSH() throws SQLException {
     
       String SshKeyFilepath = path.getSecurityDirectory()+"id_rsa";
      
      

        try {
        	//
        	// Connect to the local machine inside the firewall to setup port forward
        	//
            java.util.Properties config = new java.util.Properties();
            JSch jsch = new JSch();
            session = jsch.getSession(sshUser, sshHost, 22);
            jsch.addIdentity(SshKeyFilepath);
            config.put("StrictHostKeyChecking", "no");
            config.put("ConnectionAttempts", "3");
            session.setConfig(config);
            session.connect();

            System.out.println("Connected to sshHost: "+sshHost);

            Class.forName(javaDriverName).newInstance();
            //
            // Port forward
            //
            int assinged_port = session.setPortForwardingL(localPort, remoteDBHost, remotePort);

            System.out.println("localhost:" + assinged_port + " -> " + remoteDBHost + ":" + remotePort);
            System.out.println("Port Forwarded");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//connectSSH
	private void connectSSH(String DBHost, String sshHostServer) throws SQLException {
	       // String sshHost = "52.36.81.81";
	       // String sshuser = "dramer";
	       String SshKeyFilepath = path.getSecurityDirectory()+"id_rsa";
	       // int localPort = 3307; // any free port can be used
	       // String remoteHost = "testdb.broadline.billfire.com";
	       // int remotePort = 3306;
	      

	        try {
	        	//
	        	// Connect to the local machine inside the firewall to setup port forward
	        	//
	            java.util.Properties config = new java.util.Properties();
	            JSch jsch = new JSch();
	            session = jsch.getSession(sshUser, sshHostServer, 22);
	            jsch.addIdentity(SshKeyFilepath);
	            config.put("StrictHostKeyChecking", "no");
	            config.put("ConnectionAttempts", "3");
	            session.setConfig(config);
	            session.connect();

	            System.out.println("Connected to sshHost: "+sshHost);

	            Class.forName(javaDriverName).newInstance();
	            //
	            // Port forward
	            //
	            int assinged_port = session.setPortForwardingL(localPort, DBHost, remotePort);

	            System.out.println("localhost:" + assinged_port + " -> " + DBHost + ":" + remotePort);
	            System.out.println("Port Forwarded");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }//connectSSH
	/**
	 * This method demonstrates makeLocalDBConnection(). Use the following parameters in the properties file
	 * <br> Makes a DB connection to a local DB instance.
	 * <br> sTestMySQLConnector.serverName
	 * <br> sTestMySQLConnector.mydatabaseSchema
	 * <br> sTestMySQLConnector.username
	 * <br> sTestMySQLConnector.password
	 * <br>
	 * @return boolean - true if DB connectio was made false otherwise
	 * @author davidwramer
	 * @version 1.0
	 */
	public boolean makeSSHTunnelDBConnection(){
		try{
			connectSSH();
			makeDBConnection();
		} catch (Exception e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}// makeSSHTunnelDBConnection
	
	/**
	 * This method demonstrates createDBConnection(). Use the following parameters in the properties file
	 * <br> Makes a DB connection to a local DB instance.
	 * <br> sTestMySQLConnector.serverName
	 * <br> sTestMySQLConnector.mydatabaseSchema
	 * <br> sTestMySQLConnector.username
	 * <br> sTestMySQLConnector.password
	 * <br>
	 * @return boolean true if DB connection was made, false otherwise
	 * @author davidwramer
	 * @version 1.0
	 */
	public boolean createDBConnection(){
		try{
			makeDBConnection();
		} catch (Exception e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}// createDBConnection
	
	/**
	 * This method demonstrates makeSSHTunnelDBConnection(). This method allows you to give the DBName and Server name to connect to. This method still uses the same
	 * <br> server to create the port forwarding.
	 * <br> Makes a DB connection to a local DB instance.
	 * <br> sTestMySQLConnector.serverName
	 * <br> sTestMySQLConnector.username
	 * <br> sTestMySQLConnector.password
	 * <br>
	 * @param DBName - Name of the DB to connect to
	 * @param DBHost - ServerName to connect to
	 * @param sshHost - ssh server
	 * @return boolean - true if connection was made, false otherwise
	 * @author davidwramer
	 * @version 1.0
	 */
	public boolean makeSSHTunnelDBConnection(String DBName,String DBHost,String sshHost){
		try{
			connectSSH(DBHost,sshHost);
			makeDBConnection(DBName);
		} catch (Exception e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}// makeSSHTunnelDBConnection
	/**
	 * This method demonstrates makeSSHTunnelDBConnection(). This method allows you to give the DBName where the DBHost,and SSHHost are pulled from the properties file
	 * <br> server to create the port forwarding.
	 * <br> Makes a DB connection to a local DB instance.
	 * <br> sTestMySQLConnector.serverName
	 * <br> sTestMySQLConnector.username
	 * <br> sTestMySQLConnector.password
	 * <br>
	 * @param DBName - Name of the DB to connect to
	 * @return boolean - true of connection was made, false otherwise
	 * @author davidwramer
	 * @version 1.0
	 */
	public boolean makeSSHTunnelDBConnection(String DBName){
		try{
			connectSSH();
			makeDBConnection(DBName);
		} catch (Exception e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}// makeSSHTunnelDBConnection
	/**
	 * This method demonstrates executeQuery(). Use this method to execute SQL querries on the DB
	 * <br> DB connection must be created first.
	 * <br> sTestMySQLConnector.serverName
	 * <br> sTestMySQLConnector.mydatabaseSchema
	 * <br> sTestMySQLConnector.username
	 * <br> sTestMySQLConnector.password
	 * <br>
	 * @param query - SQL to execute
	 * @return - ResultSet - date from the query
	 * @throws SQLException - malformed SQL
	 * @author davidwramer
	 * @version 1.0
	 */
	public ResultSet executeQuery(String query) throws SQLException{
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		return rs;
		
	}//executeQuery
	/**
	 * This method demonstrates closeConnections(). When done executing connections close the tunnel and DB connections
	 * <br>
	 * @author davidwramer
	 * @version 1.0
	 */
	public  void closeConnections() {
        CloseDataBaseConnection();
        CloseSSHConnection();
    }//closeConnections

    private  void CloseDataBaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Closing Database Connection");
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }//CloseDataBaseConnection

    private static void CloseSSHConnection() {
        if (session != null && session.isConnected()) {
            System.out.println("Closing SSH Connection");
            session.disconnect();
        }
    }//CloseSSHConnection
    /**
	 * This method demonstrates selfTest(). use this to test local and SSH DB connections
	 * <br>
	 * @param tunnelConnection - true if you want to test SSH connection, false otherwise to test local connection
	 * @return ResultSet - Results of the query 
	 * @author davidwramer
	 * @version 1.0
	 */
    public ResultSet selfTest(boolean tunnelConnection){
    	ResultSet rs = null;
    	try{
			//
			// SSH Tunnel Connection
			//
			if(tunnelConnection){
				makeSSHTunnelDBConnection();
				rs = executeQuery(sshQueryToExecute);
			}else{
				//
				// Local Host connection and Test
				//
				makeLocalDBConnection();
				rs = executeQuery(localQueryToExecute);
			}
		}catch(Exception e){
			System.out.println("some DB exception "+e);
		}
		return rs;
    }// selfTest
    
	 //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args)
    	{
			boolean sshConnection = true;
			sTestMySQLConnector myDB = new sTestMySQLConnector();
			ResultSet rs = myDB.selfTest(sshConnection);
			try{
				if(sshConnection){
					while (rs.next()) {
						   
						System.out.println("ClientId = "+ rs.getString(1));			    
					}
				}else{
					//
					// Local Host connection and Test
					//
				
					while (rs.next()) {
					   
						System.out.println("id:"+ rs.getString(1)+" Name: "+rs.getString(2)+" Country Code: "+rs.getString(3)+" District: "+rs.getString(4)+" Population: "+rs.getString(5));			    
					}
				}
			}catch(Exception e){
				System.out.println("some DB exception "+e);
			}
			myDB.closeConnections();
			
    	}//main
	}//Test
}
