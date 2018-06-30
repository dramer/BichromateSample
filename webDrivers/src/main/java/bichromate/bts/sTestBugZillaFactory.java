/*
 * sTestBugZillaFactory.java	1.0 2013/01/23
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
package bichromate.bts;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.j2bugzilla.base.BugzillaConnector;
import com.j2bugzilla.base.BugzillaException;
import com.j2bugzilla.base.ConnectionException;
import com.j2bugzilla.rpc.BugzillaVersion;
/**
 * This class Demonstrates oTestBugZillaFactory().
 * <br>This class factory is used access BugZilla DataBase. Once connected you can:
 * <br> update bugs.
 * <br> create new bugs.
 * <br>
 * <br> Test site:  https://landfill.bugzilla.org/
 * @author davidwramer
 * @version 1.0
 */
public class sTestBugZillaFactory {

	 private static ResourceBundle resources;
	 private String bugzillaServer;
	 private String login;
	 private String password;
	 private BugzillaConnector bugZillaConn = null;
	    
	    static
		{
			try
			{
				resources = ResourceBundle.getBundle("bts.sTestBugZillaFactory",Locale.getDefault());
			} catch (MissingResourceException mre) {
				System.out.println("sTestBugZillaFactory.properties not found: "+mre);
				System.exit(0);
			}
		}
	    sTestBugZillaFactory(ResourceBundle remoteResources){
	    	
	    	setup(remoteResources);
	    }
	    sTestBugZillaFactory(){
	    	setup(sTestBugZillaFactory.resources);
	    	
	    }
	    
	    private void setup(ResourceBundle myResources){
	    	bugzillaServer  = new String(myResources.getString("sTestBugZillaFactory.bugzillaServer"));
	    	login =   new String(myResources.getString("sTestBugZillaFactory.login"));
	    	password   = new String(myResources.getString("sTestBugZillaFactory.password"));
	    	bugZillaConn = new BugzillaConnector();
	    }// setup
	    /**
	     * This method Demonstrates authentication().
	     * <br>Authenticate against bugzilla server defined in properties file.
	     * <br> 
	     * <br>
	     * @param loginNew - login name to server defined in properties file
	     * @param passwordNew - login password for bugZilla server defined in properties file.
	     * @author DRamer
	     * @version 1.0
	     */
	    public void authentication(String loginNew, String passwordNew){
	    	try {
	    		if(bugZillaConn != null) bugZillaConn.connectTo(bugzillaServer, loginNew, passwordNew);
			} catch (ConnectionException e) {
				
				System.out.println("sTestBugZillaFactory : authentication failed " +e );
			}
	    }
	   
	    /**
	     * This method Demonstrates authentication().
	     * <br>Authenticate against bugzilla server, login name, and password,  defined in properties file.
	     * <br> 
	     * <br>
	     * @author DRamer
	     * @version 1.0
	     */
	    public void authentication(){
	    	
	    	try {
	    		bugZillaConn.connectTo(bugzillaServer, login, password);
			} catch (ConnectionException e) {
				System.out.println("sTestBugZillaFactory : authentication failed " +e );
			}
	    }
	    /**
	     * This method Demonstrates getBugZillaVersion().
	     * <br>Access the current version of bugZilla.
	     * <br> 
	     * <br>
	     * @return String - version of bugZilla running.
	     * @author DRamer
	     * @version 1.0
	     */
	    public String getBugZillaVersion(){
	    	BugzillaVersion versionCheck = new BugzillaVersion();
	    	try {
				if(bugZillaConn != null)bugZillaConn.executeMethod(versionCheck);
			} catch (BugzillaException e) {
				
				System.out.println("sTestBugZillaFactory : getBugZillaVersion failed " +e );
			}
	    	return versionCheck.getVersion();
	    }
	  	//
		// Inner class for testing on the command line
		//
		public static class Test
		{
			public static void main(final String[] args)
	    	{
				sTestBugZillaFactory bugZilla = new sTestBugZillaFactory();
				try{
					bugZilla.authentication("davidwramer","dramer32");
					System.out.println("Current version of bugzilla is: "+bugZilla.getBugZillaVersion());
				}catch(Exception e)	{
					 System.out.println("Authentication Exception: "+e);
				}
	    	}
		}
}
