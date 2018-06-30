/*
 * eTestOSInformationFactory.java	1.0 2016/04/01
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
 * 
 */
package bichromate.core;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import bichromate.system.systemInformation;

/**
 * This class Demonstrates sTestOSInformationFactory().
 * <br>This class is used to determine the correct file, URI, or URL on a given OS. 
 * <br> Pass in the file path name, and returns a properly formatted path for the given OS.
 * @author davidwramer
 */
public class sTestOSInformationFactory {

	private static String osName = null;
	private static String pathSeperator = null;
	private static String workingDirectory = null;
	private static String fileSeperator = null;
	private static Locale locale = null;
	private static String language = null;
	private static String country = null;
	//
	// Bichromate Factory
	//
	systemInformation sysInfo = null;
	
	public sTestOSInformationFactory(){
		
		
 		osName = new String(System.getProperty("os.name"));
 		pathSeperator = new String(System.getProperty("path.separator"));
 		workingDirectory = new String("could not find working directory");
 		fileSeperator = new String(System.getProperty("file.separator"));
 		locale = Locale.getDefault();
 		language = locale.getDisplayLanguage();
 		country = locale.getDisplayCountry();
 		
 		File x = new File(".");
 		
 		if(x != null){
	 		try {
				workingDirectory = new String(x.getCanonicalPath());
			} catch (IOException e) {
				System.out.println("sTestOSInformationFactory :sTestOSInformationFactory Exception in creating working Directory:"+ e );
			}
 		}
 		
	}// sTestOSInformationFactory()
	 /**
	   * This function Demonstrates getSystemInformation().
	   * <br>Used to get system information
	   * <br>
	   * @return systemInformation 
	   */
	public systemInformation getSystemInformation(){
		if(null == sysInfo)
			 sysInfo = new systemInformation();
		return sysInfo;
				
	}//getSystemInformation()
	 /**
	   * This function Demonstrates getLanguage().
	   * <br>Returns the language setting from Locale
	   * <br>
	   * @return String country 
	   */
	public String getLanguage(){
		return language;
	}
	 /**
	   * This function Demonstrates getCountry().
	   * <br>Returns the country setting from Locale
	   * <br>
	   * @return String country 
	   */
	public String getCountry(){
		return country;
	}
	 /**
	   * This function Demonstrates getHostNameAndIP().
	   * <br>Used to return hostname and IP
	   * <br>
	   * @return String HostName and IP 
	   */
	public  String getHostNameAndIP(){
		InetAddress addr;
		String hostNameAndIP = new String("HostNameAndIP-Unknown");
		try {
			addr = InetAddress.getLocalHost();
			 //Getting IPAddress of localhost - getHostAddress return IP Address
	        // in textual format
	        String ipAddress = addr.getHostAddress();
	        //Hostname
	        String hostname = addr.getHostName();
	        hostNameAndIP = new String(hostname+":"+ipAddress);
	      
	       
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
		return hostNameAndIP;
       
	}//getHostNameAndIP
	 /**
	   * This function Demonstrates pathSeperator().
	   * <br>This function returns the path separator for the given OS: System.getProperty("path.separator")
	   * <br>
	   * @return pathSeperator of the path separator for the current OS
	   */
	public String pathSeperator(){
		return pathSeperator;
				
	}//pathSeperator()
	
	 /**
	   * This function Demonstrates fileSeperator().
	   * <br>This function returns the file seperator for the given OS: System.getProperty("file.separator")
	   * <br>
	   * @return fileSeperator of the file seperator for the current OS
	   */
	public String fileSeperator(){
		return fileSeperator;
				
	}//fileSeperator()
	/**
	   * This function Demonstrates isWin32BitOS().
	   * <br>This function returns true if the windows environment is 64 bit
	   * <br>
	   * @return true if win OS is 64 bit
	   */
	public String getArchitecture(){
		
		return(System.getProperty("os.arch"));
	}
	
	/**
	   * This function Demonstrates isRunningWin64().
	   * <br>This function returns true if the windows environment is 64 bit
	   * <br>
	   * @return true if win OS is amd64
	   */
	public boolean isRunningWin64(){
		boolean arch64 = false;
		String archString = new String(System.getProperty("os.arch"));
		if( 0 == archString.compareTo("amd64"));
				arch64 = true;
		
			
			return(arch64);
	}//isRunningWin64
	/**
	   * This function Demonstrates getHostName().
	   * <br>This function returns the hostName
	   * <br>
	   * @return hostname  of the computer
	   */
	public String getHostName(){
		
		
		String hostname = "Unknown";
		
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			 if(null != addr){
			    	hostname = addr.getHostName();
			    	
			    }
		} catch (UnknownHostException e) {
			
		}
		return hostname;
	}//getHostName
	/**
	   * This function Demonstrates getIPAddress().
	   * <br>This function returns the hostName
	   * <br>
	   * @return ip  of the computer
	   */
	public String getIPAddress(){
		
		
		String ip = "0.0.0.0";
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			 if(null != addr){
			    	
			    	ip = addr.getHostAddress();
			    }
		} catch (UnknownHostException e) {
			
		}
		return ip;
	}//getIPAddress
	
	/**
	   * This function Demonstrates getOSName().
	   * <br>This function returns the path seperator for the given OS
	   * <br>
	   * @return osName of the path seperator for the current OS
	   */
	public String getOSName(){
		
		return osName;
	}
	 /**
	   * This method Demonstrates getResourceDirectory().
	   * <br>This method builds the working directory to the Bichromate.properties file in scr\main\resource\common
	   * <br>
	   * @return workingDirectory of the working directory
	   */
	public String getResourceDirectory(){
		return new String(workingDirectory +fileSeperator+ "src"+fileSeperator+ "main"+fileSeperator+ "resources"+fileSeperator+ "common"+fileSeperator);
	}//getResourceDirectory
	 /**
	   * This method Demonstrates getWorkingDirectory().
	   * <br>This method builds the working directory
	   * <br>
	   * @return workingDirectory 
	   */
	public String getWorkingDirectory(){
		return new String(workingDirectory +fileSeperator);
	}//getWorkingDirectory
	 /**
	   * This method Demonstrates getWebDriverDirectory().
	   * <br>This method builds the working directory to the webDrivers file
	   * <br>
	   * @return workingDirectory of the working directory
	   */
	public String getWebDriverDirectory(){
		return new String(workingDirectory +fileSeperator+ "webDrivers"+fileSeperator);
	}//getWebDriverFactory
	 /**
	   * This method Demonstrates getDictionaryDirectory().
	   * <br>This method builds the working directory to the jazzy dictionary files
	   * <br>
	   * @return workingDirectory of the working directory
	   */
	public String getDictionaryDirectory(){
		return new String(workingDirectory +fileSeperator+ "dictionary"+fileSeperator);
	}//getDictionaryDirectory
	 /**
	   * This method Demonstrates getDataDirectory().
	   * <br>This method builds the working directory to the data directory
	   * <br>
	   * @return String - path with seperator to data directory
	   */
	public String getDataDirectory(){
		return new String(workingDirectory +fileSeperator+ "data"+fileSeperator);
	}//getDataDirectory
	 /**
	   * This method Demonstrates getGitLogDirectory().
	   * <br>This method builds the working directory to the GitLog Directory
	   * <br>
	   * @return workingDirectory of the working directory
	   */
	public String getGitLogDirectory(){
		return new String(workingDirectory +fileSeperator+ "gitLogs"+fileSeperator);
	}//getGitLogDirectory
	/**
	   * This method Demonstrates getAudioDirectory().
	   * <br>This method builds the working directory to the audio file
	   * <br>
	   * @return workingDirectory of the working directory
	   */
	public String getAudioDirectory(){
		return new String(workingDirectory +fileSeperator+ "audio"+fileSeperator);
	}//getAudioDirectory
	/**
	   * This method Demonstrates getEXEDirectory().
	   * <br>This method builds the working directory to the exe folder
	   * <br>
	   * @return workingDirectory of the working directory
	   */
	public String getEXEDirectory(){
		return new String(workingDirectory +fileSeperator+ "exe"+fileSeperator);
	}//getAudioDirectory
	/**
	   * This method Demonstrates getTestOutputDirectory().
	   * <br>This method builds the working directory to the test-output directory
	   * <br>
	   * @return workingDirectory of the test-output directory
	   */
	public String getTestOutputDirectory(){
		return new String(workingDirectory +fileSeperator+ "test-output"+fileSeperator);
	}//getTestOutputDirectory
	
	 /**
	   * This method Demonstrates getPropertiesFileDirectory().
	   * <br>This method builds the working directory to the properties files
	   * <br>
	   * @return workingDirectory of the working directory
	   */
	public String getPropertiesFileDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "target"+fileSeperator+"classes"+fileSeperator+"common"+fileSeperator);
	}//getPropertiesFileDirectory
	
	 /**
	   * This method Demonstrates getImageDirectory().
	   * <br>This method builds the working directory to image directory
	   * <br>
	   * @return workingDirectory of the working directory
	   * @author davidwramer
	   * @version 1.0
	   */
	public String getImageDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "images"+fileSeperator);
	}//getImageDirectory
	 /**
	   * This method Demonstrates getLogFileDirectory().
	   * <br>This method builds the working directory to log file directory
	   * <br>
	   * @return workingDirectory of the working directory
	   * @author davidwramer
	   * @version 3.0
	   */
	public String getLogFileDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "logFiles"+fileSeperator);
	}//getLogFileDirectory
	 /**
	   * This method Demonstrates getSeaLightDirectory().
	   * <br>This method builds the working directory to image directory
	   * <br>
	   * @return workingDirectory of the working directory
	   * @author davidwramer
	   * @version 3.0
	   */
	public String getSeaLightsDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "seaLights"+fileSeperator);
	}//getSeaLightsDirectory
	 /**
	   * This method Demonstrates getSeaLightsWorkingDirectory().
	   * <br>This method builds the working directory for Sea Lights 
	   * <br>
	   * @return workingDirectory of the working directory
	   * @author davidwramer
	   * @version 3.0
	   */
	public String getSeaLightsWorkingDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "seaLights"+fileSeperator+"workingDirectory"+fileSeperator);
	}//getSeaLightsWorkingDirectory
	/**
     * This method Demonstrates getCurrentDateAndTimeWithSystemTimeZone().
     * This function returns the current date in the following format yyyy-MM-dd-HH-mm-ss properties file
     * @return String  date  yyyy-MM-dd-HH-mm-ss with the current system Time Zone.
     */
	public String getCurrentDateAndTimeWithSystemTimeZone(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		String currentDate = new String(dateFormat.format(date));
		return currentDate;
		
	}//getCurrentDateAndTimeWithSystemTimeZone
	
	/**
     * This method Demonstrates getCurrentDateAndUTCTime().
     * This function returns the current date in the following format yyyy-MM-dd-HH-mm-ss
     * @return String  date  yyyy-MM-dd-HH-mm-ss set to UTC Time.
     */
	public String getCurrentDateAndUTCTime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = new Date();
		String currentDate = new String(dateFormat.format(date));
		
		return currentDate;
		
	}//getCurrentDateAndUTCTime
	/**
	   * This method Demonstrates getScreenCaptureDirectory().
	   * <br>This method builds the working directory to screenCapture directory
	   * <br>
	   * @return workingDirectory where all screen captures are stored
	   * @author davidwramer
	   * @version 1.0
	   */
	public String getScreenCaptureDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "test-output"+fileSeperator+"screencapture"+fileSeperator);
	}//getScreenCaptureDirectory
	/**
	   * This method Demonstrates getReportsDirectory().
	   * <br>This method builds the working directory to reprots directory
	   * <br>
	   * @return workingDirectory where all reports files are stored
	   * @author davidwramer
	   * @version 1.0
	   */
	public String getReportsDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "reports"+fileSeperator);
	}//getReportsDirectory
	/**
	   * This method Demonstrates getWWWDirectory().
	   * <br>This method builds the working directory to the Bichromate Web Server WWW directory
	   * <br>
	   * @return workingDirectory where all www files are stored
	   * @author davidwramer
	   * @version 1.0
	   */
	public String getWWWDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "www"+fileSeperator);
	}//getReportsDirectory
	/**
	   * This method Demonstrates getBrowserDownloadDirectory().
	   * <br>This method builds the working directory to browserDownload directory
	   * <br>
	   * @return workingDirectory where all log files are stored
	   * @author davidwramer
	   * @version 1.0
	   */
	public String getBrowserDownloadDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "browserDownloads"+fileSeperator);
	}//getBrowserDownloadDirectory
	 /**
	   * This method Demonstrates getSecurityDirectory().
	   * <br>This method builds the working directory to the security directory
	   * <br>
	   * @return workingDirectory of the security directory
	   * @author davidwramer
	   * @version 1.0
	   */
	public String getSecurityDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "security"+fileSeperator);
	}//getImageDirectory 
	 /**
	   * This method Demonstrates getAnimationDirectory().
	   * <br>This method builds the working directory to the animation directory
	   * <br>
	   * @return workingDirectory of the working directory
	   * @author davidwramer
	   * @version 1.0
	   */
	public String getAnimationDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "images"+fileSeperator+"animations"+fileSeperator);
	}//getImageDirectory 
	/**
	   * This method Demonstrates getPropertiesFileDirectory().
	   * <br>This method builds the working directory to the properties files
	   * <br>
	   * @return workingDirectory of the working directory
	   * @author davidwramer
	   * @version 1.0
	   */
	public String getTestClassDirectory(){
		
		return new String(workingDirectory +fileSeperator+ "target"+fileSeperator+"test-classes");
	}//getTestClassDirectory 
	 /**
	   * This function Demonstrates workingDirectory().
	   * <br>This function returns the working directory
	   * <br>
	   * @return workingDirectory of the working directory
	   */
	public String workingDirectory(){
		return workingDirectory;
				
	}
	public String buildWorkingDirectoryPath(String directory){
		String workingDirectory = new String(workingDirectory());
		String normalizedDir = new String(setFilePath(directory));
	
		if(normalizedDir.startsWith(fileSeperator().toString(), 0)){
			workingDirectory = new String(workingDirectory +directory);
		}else{
			workingDirectory = new String(workingDirectory +fileSeperator()+directory);
		}
		return workingDirectory;
		
	}//buildWorkingDirectoryPath
	/**
	   * This function Demonstrates setFilePath().
	   * <br>This function takes a path and changes the \ or / based on specific OS
	   * <br> There is no need to change properties file path names when running on different OS.
	   * <br>
	   * @param path path to be checked
	   * @return newPath of the correct path for the current OS
	   */
	public  String setFilePath(String path){
	
		String  newPath = null;
		
		
		if(osName.toUpperCase().contains("MAC") || osName.toUpperCase().contains("LINUX")){
			
			if( path.contains("/")){
				newPath = new String(path);
			}else if( path.contains("\\")){
				newPath = new String(path.replace("\\", "/"));
			}else{
				newPath = new String(path);
			}
		}else { // Windows file format
			
			if( path.contains("\\")){
				newPath = new String(path);
			}else if( path.contains("/")){
				newPath = new String(path.replace("/", "\\"));
			}else{
				newPath = new String(path);
			}
			
		}	
		
 		return newPath;
	}// setFilePath

	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public  static void main(final String[] args){
	 		
	 		String newPath = null;
	 		
	 		sTestOSInformationFactory path = new sTestOSInformationFactory();
	 		
	 		// OS Info
	 		System.out.println("OS Information: " + path.getOSName());
	 		// working directory
	 		System.out.println("Working Directory: " + path.workingDirectory());
	 		// Web Driver Directory
	 		System.out.println("Web Driver Directory: " + path.getWebDriverDirectory());
	 		// path separator
	 		System.out.println("file Seperator: " + path.fileSeperator());
	 		// find architecture
	 		System.out.println("Architecture: " + path.getArchitecture());
	 		// find country
	 		System.out.println("Country: " + path.getCountry());
	 		// find architecture
	 		System.out.println("Language: " + path.getLanguage());
	 		// Testing MAC path Name
	 		
	 		System.out.println("Test Mac File Path");
	 		newPath = new String(path.setFilePath("/JARs/Sauce-Connect.jar"));
	 		System.out.println("Mac path in: /JARs/Sauce-Connect.jar " + "Returnd path should match (if Mac/linux os) : "+ newPath);
	 		
	 		//Testing windows path name to Mac
	 		
	 		System.out.println("Test Windows File Path to Mac File path");
	 		newPath = new String(path.setFilePath("\\JARs\\Sauce-Connect.jar"));
	 		System.out.println("Windows path in: \\JARs\\Sauce-Connect.jar " + "Returnd path should change to Mac (if Mac/Linux os): "+ newPath);
	 		
	 		
	 		//Testing invalid file path
	 		
	 		System.out.println("Test invalid File Path");
	 		newPath = new String(path.setFilePath(".JARs.Sauce-Connect.jar"));
	 		System.out.println("Invalid path in: .JARs.Sauce-Connect.jar " + "Returnd path should an Error: "+ newPath);
	 		
	 		
	 		//Testing invalid file path
	 		
	 		System.out.println("Test invalid File Path than contains both / and \\");
	 		newPath = new String(path.setFilePath("\\JARs/Sauce-Connect.jar"));
	 		System.out.println("Invalid path in: \\JARs/Sauce-Connect.jar " + "Returnd path should an Error: "+ newPath);
	 		
	 		
	 	}
	 }
	
	
}// sTestOSInformationFactory
