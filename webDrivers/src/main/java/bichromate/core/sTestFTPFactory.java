/*
 * sTestFTPFactory.java	1.0 2013/01/24
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
 
/**
 * A program that demonstrates how to upload files from local computer
 * to a remote FTP server using Apache Commons Net API.
 * @author www.codejava.net/Dwr
 */
public class sTestFTPFactory {
	
	
private static ResourceBundle resources;
    
	private String server = null;
	private int port = 22;
	private String login = null;
	private String password = null;
	@SuppressWarnings("unused")
	private String FTPDirectory = null;
	private sTestOSInformationFactory osInfo = null;
	private  FTPClient ftpClient = null;

    static
	{
		try
		{
			resources = ResourceBundle.getBundle("common.sTestFTPFactory",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestFTPFactory.properties not found: "+mre);
			System.exit(0);
		}
	}
    public sTestFTPFactory(ResourceBundle myResources) {
    	
    	setupFTP(myResources);
    	
    }
   public sTestFTPFactory() {
	   setupFTP(resources);
   }
   private void setupFTP(ResourceBundle myResourceBundle){
	    osInfo = new sTestOSInformationFactory();
	   
        //
    	// FTP Server
    	//
	    server = new String(myResourceBundle.getString("sTestFTPFactory.server"));
	    //
    	// FTP Server port
    	//
	    port = Integer.parseInt(new String(myResourceBundle.getString("sTestFTPFactory.port")));
	    //
    	// FTP login
    	//
	    login = new String(myResourceBundle.getString("sTestFTPFactory.login"));
	    //
    	// FTP password
    	//
	    password = new String(myResourceBundle.getString("sTestFTPFactory.password"));
	    //
	    // FTP directory to store files
	    //
	    FTPDirectory = new String(myResourceBundle.getString("sTestFTPFactory.FTPDirectory"));
	    //
	    // create FTP client
	    //
	    if(password.length() !=0 || login.length() !=0 || server.length() !=0){
	    	ftpClient = new FTPClient();
	    }else{
	    	System.err.println("ftpClient was not created");
	    }  
   }//setupFTP
   /**
 	 * This method Demonstrates deletePublishedFileOnWeb().
 	 * <br>Use this method to delete a published file from webserver
 	 * <br>sTestFTPFactory contains all the server setup info
 	 * <br> 
 	 * @param fileNameOnServer - filename to delete, qualified path(from root) and name of the file
 	 * @return boolean - true if file was deleted
 	 * @author www.codejava.net
 	 */
  public boolean deletePublishedFileOnWeb(String fileNameOnServer){
	  boolean deleted = true;
	  
	  if(null == ftpClient){
  		System.err.println("ftpClient was not created");
  		return false;
	  }
      	
      try {

          ftpClient.connect(server, port);
          ftpClient.login(login, password);
          ftpClient.enterLocalPassiveMode();
          if(!ftpClient.deleteFile(fileNameOnServer)){
        	  System.err.println("failed to delete file: "+ fileNameOnServer);
        	  deleted = false;
          }
      } catch (IOException ex) {
          System.out.println("Error: " + ex.getMessage());
          ex.printStackTrace();
      } finally {
          try {
              if (ftpClient.isConnected()) {
                  ftpClient.logout();
                  ftpClient.disconnect();
              }
          } catch (IOException ex) {
              ex.printStackTrace();
          }
      }
	  return deleted;
  }//deletePublishedFileOnWeb
   /**
  	 * This method Demonstrates publishFileToWeb().
  	 * <br>Use this method to publish Bichromate reports to the webserver
  	 * <br>sTestFTPFactory contains all the server setup info
  	 * <br> The fileName param will be uploaded to the server and replace the file if the file already exists on the server
  	 * @param fileToPublish - fully qualified fileName including path and file name
  	 * @param fileNameOnFTPServer - file name on the ftp server.
  	 * @return boolean true if file was published to web
  	 * @author www.codejava.net
  	 */
   public boolean publishFileToWeb(String fileToPublish, String fileNameOnFTPServer){
	   boolean done = false;
	   
    	if(null == ftpClient){
    		System.err.println("ftpClient was not created");
    		return done;
    	}
        try {
 
            ftpClient.connect(server, port);
            ftpClient.login(login, password); // login for FTP (at godaddy) is specific to a FTP directory. No need to include the entire path.
            ftpClient.enterLocalPassiveMode();
 
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE, FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            // APPROACH #1: uploads first file using an InputStream
            File firstLocalFile = new File(fileToPublish);
 
            
            // InputStream inputStream = new FileInputStream(firstLocalFile);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(firstLocalFile));
 
            System.out.println("Start uploading first file");
            done = ftpClient.storeFile(fileNameOnFTPServer, bis);
           
            
            // inputStream.close();
            bis.close();
            if (done) {
                System.out.println("The first file is uploaded successfully.");
            }else{
            	System.out.println("Failed to upload file.");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return done;
    }// publishFileToWeb
   /**
 	 * This method Demonstrates selfTest().
 	 * <br>This was tested using GoDaddy.com site. So specific rules apply to what directory your login has access to.
 	 * @author DavidWRamer
 	 */
   public void selfTest(){
	   
	   if(publishFileToWeb(osInfo.getReportsDirectory()+"Extent.html","Extend.html"))
		   System.out.println("sTestFTPFactory selfTest Passed uploading new file");
	   else
		   System.out.println("sTestFTPFactory selfTest failed to upload Extent.html");
	   //
	   // Delete the file
	   //
	   if(deletePublishedFileOnWeb("Extend.html"))
		   System.out.println("sTestFTPFactory selfTest Passed deleting new file on FTP site");
	   else
		   System.out.println("sTestFTPFactory selfTest failed to upload Extent.html");
	   
	   
   }//selfTest
    
    //
	// Inner class for testing on the command line
	//
   /**
	 * This method Demonstrates Test().
	 * <br>Inner class to unit test sTestFTPFactory
	 * @author DavidWRamer
	 */
	public static class Test
	{
		public static void main(final String[] args){
			
			
			sTestFTPFactory FTPFactory = null;
			
			FTPFactory = new sTestFTPFactory();
			if(FTPFactory != null){
				
				FTPFactory.selfTest();
				
			}
			
		} // end Main
	 } // end Inner class Test
 
}//sTestFTPFactory