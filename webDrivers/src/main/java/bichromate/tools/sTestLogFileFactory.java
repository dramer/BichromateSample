/*
 * sTestLogFileFactory.java	1.0 2016/09/01
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

package bichromate.tools;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import com.jcraft.jsch.*;

import bichromate.core.sTestOSInformationFactory;

import javax.swing.*;
/**
 * 
 * 
 * 
 * BEFOERE YOU USE, PLEASE READ
 * 	Thank you for downloading the Unlimited Strength Java(TM) Cryptography
 *  Extension (JCE) Policy Files for the Java(TM) Platform, Standard
 *  Edition Development Kit, v6.

 * 	Due to import control restrictions, the version of JCE policy files that
 *	are bundled in the JDK(TM) 6 environment allow "strong" but limited
 *	cryptography to be used. This download bundle (the one including this
 * 	README file) provides "unlimited strength" policy files which contain
 *	no restrictions on cryptographic strengths.

 *	Please note that this download file does NOT contain any encryption
 *	functionality since such functionality is supported in Sun's JDK 6.
 *	Thus, this installation applies only to Sun's JDK 6, and assumes
 *	that the JDK 6 is already installed.
 * 
 * 
 * @author davidwramer
 *
 */
@SuppressWarnings("unused")
public class sTestLogFileFactory {
	private String securityDirectoryForPubKey = null;
	private String pubKeyName = null;
	private String myFile;
	private String myServer;
	private static ResourceBundle resources;
	private String pubKeyFile= null;
	private String fileLimitString = null;
	private long fileLimit = 1073741824;
	private String downLoadFileName = null;
	private String numberOfLogs = null;
	private long numOfLogs = 0;
	private String numberOfServers = null;
	private long numOfServers = 0;
	private String userName = null;
	private String startingDirectory = null;
	private String downloadDirectory = null;
	private String downloadFileNameAndPath = null;
   
    public static String promptedfile,promptedServer;
   
    Vector <String>serverSelection = null;
    Vector <String>fileSelection = null;
    
    private sTestOSInformationFactory path = null;
    
    public static String dbName = "Select server...";
    
    ///var/log/httpd/ngs/tomcat
    //all
    //audit
    //cron
    //dbpool
    //emails
    //errors
    //dbpools
    
    //
    //
    // servers
    // 52.36.81.81        BroadlineTestApp        database host: testdb.broadline.billfire.com
    // 52.36.40.200       BroadlineTrainApp      database host: localhost
    // 52.40.236.229      PfgTestApp             database host: testdb.pfg.billfire.com
    // 52.10.82.28 
	
	public static String logFile = "Select log file...";
	
	
	
	
    static
    {
    	try
    	{
    		resources = ResourceBundle.getBundle("tools.sTestLogFileFactory",Locale.getDefault());
    	} catch (MissingResourceException mre) {
    		System.out.println("sTestLogFileFactory.properties not found: "+mre);
    		System.exit(0);
    	}
    }
    public sTestLogFileFactory(){
    	
   	 setup(resources);
   	 
   }//sTestLogFileFactory
   
    
    public sTestLogFileFactory(ResourceBundle myResources){
    	setup(myResources);
    	
    }//sTestLogFileFactory
    /**
  	 * This method Demonstrates setup().
  	 * read in all the parameters from the properties file.
  	 * @param myResources
  	 * @author davidwramer
  	 * @version 1.0
  	 */
   private void setup(ResourceBundle myResources){
	   downloadDirectory = new String(myResources.getString("sTestLogFileFactory.downLoadDirectory"));
	   securityDirectoryForPubKey = new String(myResources.getString("sTestLogFileFactory.securityDirectory"));
	   pubKeyName = new String(myResources.getString("sTestLogFileFactory.sshKeyName"));
	   downLoadFileName = new String(myResources.getString("sTestLogFileFactory.downLoadFileName"));
    	startingDirectory = new String(myResources.getString("sTestLogFileFactory.logStartingDirectory"));
    	fileLimitString = new String(myResources.getString("sTestLogFileFactory.fileLimit"));
    	fileLimit = Long.parseLong(fileLimitString.trim());
    	numberOfLogs = new String(myResources.getString("sTestLogFileFactory.numberOfLogs"));
    	numOfLogs = Long.parseLong(numberOfLogs.trim());
    	userName = new String(myResources.getString("sTestLogFileFactory.userName"));
    	numberOfServers = new String(myResources.getString("sTestLogFileFactory.numberOfServers"));
    	numOfServers = Long.parseLong(numberOfServers.trim());
    	serverSelection = new Vector<String>();
    	fileSelection = new Vector<String>();
    	 path = new sTestOSInformationFactory();
    	 pubKeyFile = new String(path.buildWorkingDirectoryPath(securityDirectoryForPubKey)+path.fileSeperator()+pubKeyName);
    	 //
      	// Now load all the server and log files
      	//
    	serverSelection.addElement(dbName);
    	 
      	for(int x = 1; x <= numOfServers; x++){
      		String serverToFetchFromPropertiesFile = new String("sTestLogFileFactory.server-"+x);
      		String serverName = new String(myResources.getString(serverToFetchFromPropertiesFile));
      		serverSelection.addElement(serverName);
      	}
      	//
     	// Now load all the servers and log files
     	//
      	fileSelection.addElement(logFile);
     	for(int x = 1; x <= numOfLogs; x++){
     		String logFileToFetchFromPropertiesFile = new String("sTestLogFileFactory.logFile-"+x);
     		String logName = new String(myResources.getString(logFileToFetchFromPropertiesFile));
     		fileSelection.addElement(logName);
     	} 
    }//setup
   /**
	 * This method Demonstrates getLogFileComboBox().
	 * This method returns the list of fileNames for the U/I
	 * @return Vector - fileSelection
	 */
   public  Vector <String> getLogFileComboBox(){
	   return fileSelection;
   }
   /**
 	 * This method Demonstrates getServerComboBox().
 	 * This method returns the list of serverNames for the U/I
 	 * @return Vector -serverSelection
 	 */
   public  Vector <String> getServerComboBox(){
	 return serverSelection;
   }
   /**
 	 * This method Demonstrates getStartingDirectory().
 	 * This method will return the starting directory for all log files.
 	 * @return String - startingDirectory
 	 */
   public String getStartingDirectory(){
	   return startingDirectory;
   }
   /**
  	 * This method Demonstrates getPrivateKeyFile().
  	 * This method will prompt the user to select the PPK file.
  	 * @author davidwramer
  	 * @version 1.0
  	 */
    private void getPrivateKeyFile(){
    	try{
    	      JFileChooser chooser = new JFileChooser();
    	      chooser.setDialogTitle("Choose your privatekey(ex. ~/.ssh/id_dsa)");
    	      chooser.setFileHidingEnabled(false);
    	      int returnVal = chooser.showOpenDialog(null);
    	      if(returnVal == JFileChooser.APPROVE_OPTION) {
    	        System.out.println("You chose "+ chooser.getSelectedFile().getAbsolutePath()+".");
    	        pubKeyFile = new String(chooser.getSelectedFile().getAbsolutePath());
    	      }
    	} catch(Exception e){
    		System.out.println(e);
    	}
    }//getPrivateKeyFile()
    /**
   	 * This method Demonstrates promptServerAndLogFile().
   	 * This method will prompt the user to select server and log file.
   	 * @return - boolean true of user selected option, false otherwise
   	 * @author davidwramer
  	 * @version 1.0
   	 */
    private boolean promptServerAndLogFile(){
    	  
    	JComboBox <String>serverSelectionBox = new JComboBox<String>(serverSelection);
    
    	JComboBox <String>fileSelectionBox = new JComboBox<String>(fileSelection);
    	
    	
		Object[] ob={"server: ",serverSelectionBox,"file: ",fileSelectionBox};
	    int result= JOptionPane.showConfirmDialog(null, ob, "Enter Server & FIle",JOptionPane.OK_CANCEL_OPTION);
	    if(result==JOptionPane.OK_OPTION){
	    	promptedServer = new String((String)serverSelectionBox.getSelectedItem());
	    	promptedfile = new String((String)fileSelectionBox.getSelectedItem());
	        return true;
	    }else{ 
	    	return false; 
	    }
	} // promptPassphrase()
    /**
	 * This method Demonstrates downloadLogFile().
	 * This method will prompt the user to find SSH key.
	 * @param server - name of the server
	 * @param pathAndfileName - fully qualified path to the file and name. EXAMPLE: /var/logs/log.log
	 * @param login - login name on the SSH server
	 * @author davidwramer
  	 * @version 1.0 
	 */
    public void downloadLogFile(final String server, final String pathAndfileName, String login){
    	downloadFileNameAndPath = null;
    	 JSch jsch = new JSch();
         Session session = null;
         System.out.println("Connecting to SFTP server...");
         System.out.println("Connecting to "+ server);
         try {
             session = jsch.getSession(login,server , 22);
             //
             // prompt for the pass key if not provided in properties file
             //
             if(pubKeyFile == null){
            	 getPrivateKeyFile();
             }
             jsch.addIdentity(pubKeyFile);
             UserInfo ui = new myUserInfoSSHFactory();
             session.setUserInfo(ui);
           
             session.connect();
             
             Channel channel = session.openChannel("sftp");
             Channel channelShell = session.openChannel("shell");
             
             channel.connect(3*1000);
             
             channelShell.connect();
             ChannelSftp sftpChannel = (ChannelSftp) channel;
             String getFilePath = new String(pathAndfileName);
             System.out.println("downloading:  "+ getFilePath);
            
             SftpATTRS myattr = sftpChannel.lstat(getFilePath); 
             long fileSize = myattr.getSize();
             // 
             // 1 gig limit
             //
             if(fileSize < 1073741824) {
				downloadFileNameAndPath = new String(path.workingDirectory() + path.fileSeperator() + downloadDirectory
						+ path.fileSeperator() + server + "-" + getCurrentDate() + "-" + downLoadFileName);
            	 sftpChannel.get(getFilePath,downloadFileNameAndPath);
             }else{
            	 System.out.println("File: "+ getFilePath +" is over the 1 gig limit for downloads"); 
             }
             sftpChannel.exit();
             session.disconnect();
             System.out.println("File downloaded: "+ downloadFileNameAndPath);
         } catch (JSchException e) {
        	 System.out.println("FAILED to Connect: "+ server);
         } catch (SftpException e) {
        	 System.out.println("FAILED to download from: "+ server);
         }
    }//downloadLogFile
    /**
   	 * This method Demonstrates downloadLogFile().
   	 * This method assumes SSH Key on the server.
   	 * @param server - name of the server
   	 * @param pathAndfileName - fully qualified path to the file and name. EXAMPLE: /var/logs/log.log
   	 * @param testName - name of the last test ran
   	 * @param login - login name on the SSH server
   	 * @author davidwramer
  	 * @version 1.0 
   	 */
       public void downloadLogFile(final String server, final String pathAndfileName, String testName,String login){
       	downloadFileNameAndPath = null;
       	 JSch jsch = new JSch();
            Session session = null;
            System.out.println("Connecting to SFTP server...");
            System.out.println("Connecting to "+ server);
            try {
                session = jsch.getSession(login,server , 22);
                //
                // prompt for the pass key if not provided in properties file
                //
                if(pubKeyFile == null){
               	 getPrivateKeyFile();
                }
                jsch.addIdentity(pubKeyFile);
                UserInfo ui = new myUserInfoSSHFactory();
                session.setUserInfo(ui);
              
                session.connect();
                
                Channel channel = session.openChannel("sftp");
                Channel channelShell = session.openChannel("shell");
                
                channel.connect(3*1000);
                
                channelShell.connect();
                ChannelSftp sftpChannel = (ChannelSftp) channel;
                String getFilePath = new String(pathAndfileName);
                System.out.println("downloading:  "+ getFilePath);
               
                SftpATTRS myattr = sftpChannel.lstat(getFilePath); 
                long fileSize = myattr.getSize();
                // 
                // 1 gig limit
                //
                if(fileSize < 1073741824) {
   				downloadFileNameAndPath = new String(path.workingDirectory() + path.fileSeperator() + downloadDirectory
   						+ path.fileSeperator() + server + "-" + getCurrentDate() + "-" + testName + ".log");
               	 sftpChannel.get(getFilePath,downloadFileNameAndPath);
                }else{
               	 System.out.println("File: "+ getFilePath +" is over the 1 gig limit for downloads"); 
                }
                sftpChannel.exit();
                session.disconnect();
                System.out.println("File downloaded: "+ downloadFileNameAndPath);
            } catch (JSchException e) {
           	 System.out.println("FAILED to Connect: "+ server);
            } catch (SftpException e) {
           	 System.out.println("FAILED to download from: "+ server);
            }
       }//downloadLogFile
    /**
   	 * This method Demonstrates getLastLogFile().
   	 * This method returns the full path and name to the last log file read.
   	 * @return String full path and file name of the last log read.
   	 */
    public String getLastLogFile(){
    	return downloadFileNameAndPath;
    }//getLastLogFile
    /**
	 * This method Demonstrates obtainLogFileData().
	 * This method returns a String of the log data. User needs to parse the data.
	 * @param fileNameAndPath - log file name and path
	 * @return String -return the fileNameAndPathParameter
	 */
    public String obtainLogFileData(String fileNameAndPath){
    	String logFileInfo = null;
    	if(null == fileNameAndPath)
    		return (logFileInfo = new String("NO FILE FOUND"));
    	byte[] encoded;
		
		try {
			encoded = Files.readAllBytes(Paths.get(fileNameAndPath));
			logFileInfo = new String(encoded, Charset.defaultCharset());
		} catch (IOException e) {
			System.out.println("Error reading log info");
			e.printStackTrace();
		}
		  return logFileInfo;
    	
   
    }//obtainLogFileData
	/**
	 * This method Demonstrates downloadLogFile().
	 * This method will prompt the user to select file, server, and SSH File.
	 */
    public void downloadLogFile(){
    	downloadFileNameAndPath = null;
    	//
    	// Prompt user for server and log file
    	//
    	promptServerAndLogFile();
    	
    	 JSch jsch = new JSch();
         Session session = null;
         System.out.println("Connecting to SFTP server...");
         System.out.println("Connecting to "+ promptedServer);
         try {
             session = jsch.getSession(userName,promptedServer , 22);
             //
             // if the pubkey is null promt for it
             //
             if(pubKeyFile == null){
            	 getPrivateKeyFile();
             }
             jsch.addIdentity(pubKeyFile/*, "passphrase"*/);
             //
             // UserInfo is used to silence the warning messages
             //
             UserInfo ui = new myUserInfoSSHFactory();
             session.setUserInfo(ui);
            
             session.connect();
             
             Channel channel = session.openChannel("sftp");
             channel.connect();
             ChannelSftp sftpChannel = (ChannelSftp) channel;
             String getFilePath = new String(startingDirectory+promptedfile);
             System.out.println("downloading:  "+ getFilePath);
             SftpATTRS myattr = sftpChannel.lstat(getFilePath); 
             long fileSize = myattr.getSize();
             // 
             // 1 gig limit
             //
             if(fileSize < 1073741824) {
            	 downloadFileNameAndPath = new String(path.workingDirectory()+path.fileSeperator()+downloadDirectory+path.fileSeperator()+ promptedServer+"-"+getCurrentDate()+"-"+downLoadFileName);
            	 sftpChannel.get(getFilePath, downloadFileNameAndPath);
             }else{
            	 System.out.println("File: "+ getFilePath +" is over the 1 gig limit for downloads"); 
             }
             sftpChannel.exit();
             session.disconnect();
             System.out.println("File downloaded: "+ downloadFileNameAndPath);
         } catch (JSchException e) {
        	 System.out.println("FAILED to Connect: "+ promptedServer);
         } catch (SftpException e) {
        	 System.out.println("FAILED to download from: "+ promptedServer);
         }
    }//downloadLogFile
    /**
     * This method Demonstrates getCurrentDate().
     * This function returns the current date in the following format yyyy-MM-dd-HH-mm-ss properties file
     * @return String  date  yyyy-MM-dd-HH-mm-ss
     */
	private String getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		String currentDate = new String(dateFormat.format(date));
		return currentDate;
	}//getCurrentDate
    //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args)
    	{
			sTestLogFileFactory log = new sTestLogFileFactory();
			try{
				System.out.println("Testing downloadLogFile()");
				// log.downloadLogFile();
				//System.out.println("Testing downloadLogFile(String, String, String)");
				log.downloadLogFile("52.36.81.81","/var/log/httpd/ngs/tomcat/errors/errors.log","dramer");
				System.out.print(log.obtainLogFileData(log.getLastLogFile()));
			}catch(Exception e){
				e.printStackTrace();
			}
			
    	}//main
	}//Test
 
}