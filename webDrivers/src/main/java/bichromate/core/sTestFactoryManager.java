package bichromate.core;

import java.util.ResourceBundle;

import bichromate.DBAccess.sTestMySQLConnector;
import bichromate.tools.sTestLogFileFactory;
import bichromate.tools.sTestZipCodeAPI;

public class sTestFactoryManager {
	protected static ResourceBundle resources;
	//
	// Local Factories
	//
	private sTestJiraFactory jiraFactory = null;
	private sTestOSInformationFactory osInfo =  null;
	
	@SuppressWarnings("unused")
	private sTestScrollIntoViewFactory siv = null;
	@SuppressWarnings("unused")
	private sTestHilitePageElementFactory helem = null;
	private sTestScreenCaptureFactory screencapture = null;
	private sTestZipFileFactory zipFile = null;
	private sTestSlackFactory slackFactory = null;
	private sTestHipChatFactory hipChat = null;
	@SuppressWarnings("unused")
	private sTestVideoCaptureFactory vidCapture = null;
	private sTestMySQLConnector mySQLConnection = null;
	private sTestFTPFactory  ftp = null;
	private sTestCustomerAccountManagerFactory passwordStore = null;
	@SuppressWarnings("unused")
	private sTestLogFileFactory log = null;
	private sTestCleanupFactory cuf = null;
	private sTestZipCodeAPI zipCodeAPI = null;
	
	public sTestFactoryManager(ResourceBundle myResources){
		
		resources = myResources;
		
		osInfo =  new sTestOSInformationFactory();
		
		siv = new sTestScrollIntoViewFactory();
		
		helem = new sTestHilitePageElementFactory();
		
		jiraFactory = new sTestJiraFactory(resources);
		
		screencapture = new sTestScreenCaptureFactory();
		
		zipFile = new sTestZipFileFactory(resources);
		
		slackFactory = new sTestSlackFactory(resources);
		
		hipChat = new sTestHipChatFactory(resources);
		
		ftp = new sTestFTPFactory();
		
		passwordStore = new sTestCustomerAccountManagerFactory(resources);
		
		log = new sTestLogFileFactory(resources);
		
		mySQLConnection = new sTestMySQLConnector(resources);
		
		cuf = new sTestCleanupFactory(resources);
		
		
	}//sTestFactoryManager
	
	/**
	 * getSTestMySQLConnector()
	 * Returns the sTestMySQLConnector class. Used to clean up test files
	 * <br> must be setup in the properties file.
	 * @see sTestMySQLConnector
	 * @return sTestMySQLConnector
	 */
	public sTestMySQLConnector getSTestMySQLConnector(){
		return mySQLConnection;
	}
	/**
	 * getSTestMySQLConnector()
	 * Returns the getsTestCleanupFactory class. Used to clean up test files
	 * <br> must be setup in the properties file.
	 * @see sTestMySQLConnector
	 * @return sTestMySQLConnector
	 */
	public sTestCleanupFactory getsTestCleanupFactory(){
		
		return cuf;
	}
	/**
	 * getSTestCustomerAccountManagerFactory()
	 * Returns the sTestCustomerAccountManagerFactory class. Used to get passwords for accounts when logging in
	 * @see sTestCustomerAccountManagerFactory
	 * @return passwordStore
	 */
	public sTestCustomerAccountManagerFactory getSTestCustomerAccountManagerFactory(){
		return passwordStore;
	}
	/**
	 * getZipCodeFactory()
	 * Returns the sTestZipCodeAPI class. Used to calculate and obtain zip information
	 * @see sTestZipCodeAPI
	 * @return sTestZipCodeAPI
	 */
	public sTestZipCodeAPI getZipCodeFactory(){
		if(null == zipCodeAPI){
			zipCodeAPI = new sTestZipCodeAPI();
		}
		return zipCodeAPI;
	}
	/**
	 * getSTestFTPFactory()
	 * Returns the getSTestFTPFactory class. Used to post files to a FTP server that is setup in the properties file
	 * @see getSTestFTPFactory
	 * @return osInfo
	 */
	public sTestFTPFactory getSTestFTPFactory(){
		return ftp;
	}
	
	/**
	 * getSTestOSInformationFactory()
	 * Returns the getSTestOSInformationFactory class
	 * @see getSTestOSInformationFactory
	 * @return osInfo
	 */
	public sTestOSInformationFactory getSTestOSInformationFactory(){
		return osInfo;
	}
	/**
	 * getSTestHipChatFactory()
	 * Returns the sTestHipChatFactory class
	 * @see sTestSlackFactory
	 * @return hipChat
	 */
	public sTestHipChatFactory getSTestHipChatFactory(){
		return hipChat;
	}
	/**
	 * sTestSlackFactory()
	 * Returns the sTestSlackFactory class
	 * @see sTestSlackFactory
	 * @return slackFactory
	 */
	public sTestSlackFactory getSTestSlackFactory(){
		return slackFactory;
	}
	/**
	 * getSTestJiraFactory()
	 * Returns the jiraFactory class
	 * @see sTestJiraFactory
	 * @return jiraFactory factory for entering jira updates
	 */
	public sTestJiraFactory getZipFileFactory(){
		return jiraFactory;
	}
	/**
	 * getSTestJiraFactory()
	 * Return the sTestZipFileFactory class
	 * @return zipFile zip file factory for zipping up screen shots
	 * @see sTestZipFileFactory
	 */
	public sTestZipFileFactory getSTestJiraFactory(){
		return zipFile;
	}
	/**
	 * method getScreenGrabber()
	 * Returns the getScreenGrabber class
	 * @see sTestScreenCaptureFactory
	 * @return screencapture sTestScreenCaptureFactory to take screen shots
	 */
	public sTestScreenCaptureFactory getSTestScreenCaptureFactory(){
		return screencapture;
	}

}// sTestFactoryManager
