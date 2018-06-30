/*
 * sTestCustomerAccountManagerFactory.java	1.0 2016/08/05
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import bichromate.dataStore.passwordStore;

/**
 * @author davidwramer
 * @version 1.0
 *
 */
public class sTestCustomerAccountManagerFactory {
	
	base64CoderFactory b64c = null;
	private boolean writeFileEncrypted = false;
	private String passwordFileName = null;
	private String passwordDirectory = null;
	private String passwordEncryption = null;
	private String isFileEncrypted = null;
	sTestOSInformationFactory osInfo = null;
	private List<passwordStore> logins = null;
	
	
	private static ResourceBundle resources;
		
		
		
	 static
	 {
			try
			{
				resources = ResourceBundle.getBundle("core.sTestCustomerAccountManagerFactory",Locale.getDefault());
			} catch (MissingResourceException mre) {
				System.out.println("sTestCustomerAccountManagerFactory.properties not found: "+mre);
				System.exit(0);
			}
	 }
	
	 /**
	 * This method demonstrates sTestPOMFactory().
	 * <br>  Page Object Model  generator. Reads in a properties file and generates the POM.java file 
	 * <br>  
	 * @param  outsideResources - Bichromare resources 
	 */
	public sTestCustomerAccountManagerFactory(ResourceBundle outsideResources){
	
		setup(outsideResources);
	}
	
	/**
	 * This method demonstrates sTestCustomerAccountManagerFactory().
	 * <br>  Page Object Model  generator. Reads in a properties file and generates the POM.java file 
	 * <br>    
	 */
	public sTestCustomerAccountManagerFactory(){
		
		setup(resources);
	}//sTestPOMFactory
	
	private void setup(ResourceBundle myResources){
	
		 osInfo = new sTestOSInformationFactory();
		
		 logins = new ArrayList<passwordStore>();
		 
		 b64c = new base64CoderFactory();
		 
		passwordFileName = new String(myResources.getString("sTestCustomerAccountManagerFactory.passwordFileName"));
		passwordDirectory = new String(myResources.getString("sTestCustomerAccountManagerFactory.passwordDirectory"));
		passwordEncryption = new String(myResources.getString("sTestCustomerAccountManagerFactory.passwordEncryption"));
		//
		//
		//
		loadLogins();
		//
		// Determine the state of the password file
		//
		if(isFileEncrypted.equalsIgnoreCase("true")){
			if(passwordEncryption.equalsIgnoreCase("false")){
				//
				//write out un-encrypted file.
				//
				writeFileEncrypted = false;
				writeNewPasswordFile(writeFileEncrypted);
			}
			if(passwordEncryption.equalsIgnoreCase("true")){
				//
				//write out encrypted file.
				//
				writeFileEncrypted = true;
	
			}
			
		}else if(isFileEncrypted.equalsIgnoreCase("false")){
			
			if(passwordEncryption.equalsIgnoreCase("true")){
				//
				//write out encrypted file.
				//
				writeFileEncrypted = true;
				writeNewPasswordFile(writeFileEncrypted);
				
			}
			if(passwordEncryption.equalsIgnoreCase("false")){
				writeFileEncrypted = false;
			}
			
		}else if(isFileEncrypted.equalsIgnoreCase("undefined")){
			if(passwordEncryption.equalsIgnoreCase("true")){
				//
				//write out encrypted file.
				//
				writeFileEncrypted = true;
				writeNewPasswordFile(writeFileEncrypted);
			} else if(passwordEncryption.equalsIgnoreCase("false")){
				//
				//write out un-encrypted file.
				//
				writeFileEncrypted = false;
				writeNewPasswordFile(writeFileEncrypted);
			}
			
		}else{
			System.err.println("File encryption is undefined");
			//
			//write out un-encrypted file.
			//
			writeFileEncrypted = false;
			writeNewPasswordFile(writeFileEncrypted);
		}
		
		
		
	}// setup
	
	private void loadLogins(){
		String loginFile = new String(osInfo.buildWorkingDirectoryPath(osInfo.setFilePath(passwordDirectory))+osInfo.fileSeperator()+passwordFileName);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(loginFile ));
			String line;
			
			isFileEncrypted = new String("undefined");
			boolean fileEncryptionValidated = false;
			while ((line = br.readLine()) != null) {
				passwordStore pass = new passwordStore();
				if(line.contains("passwords.encrypted")){
					//
					// Check encryption state
					//
					if(line.contains("passwords.encrypted=no")){
						isFileEncrypted = new String ("false");
						System.out.println("file not encrypted");
						fileEncryptionValidated= true;
					}else if(line.contains("passwords.encrypted=yes")){
						isFileEncrypted = new String ("true");
						System.out.println("file is encrypted");
						fileEncryptionValidated= true;
					}else{
						isFileEncrypted = new String ("undefined");
						System.out.println("encyrption no defined, can't read file");
					}
				}
				if(line.contains("login=")&& fileEncryptionValidated){
					int offset = 6;
					String loginPassword = new String(line.substring(offset));
					
					List<String> list = new ArrayList<String>(Arrays.asList(loginPassword.split(",")));
				
					if(list.size() == 2){
						pass.login = new String(list.get(0).trim());
						try{
							if(isFileEncrypted.equalsIgnoreCase("true")){
								pass.encryptedPassword = new String(list.get(1).trim());
								pass.password = new String(base64CoderFactory.decode(pass.encryptedPassword));
							}else{
								pass.password = new String(list.get(1).trim());
								pass.encryptedPassword = new String(base64CoderFactory.encodeString(pass.password));
							}
							logins.add(pass);
						}catch(IllegalArgumentException iae){
							System.out.println("login: "+pass.login + " illegal encryption");
						}
						
					}
					
				}
			}
			br.close();
		} catch (FileNotFoundException file) {
			System.out.println("passwords FileNotFoundException: "+file );
			
		} catch (IOException io) {
			System.out.println("passwords File IOException: "+io );
		} finally{
			System.out.println("passwords file read");
		}
	}//loadLogins
	/**
	 * This method demonstrates storeNewPassword().
	 * <br>  If a test changes a password, that password can be stored back to the password.log 
	 * <br>
	 * @param account - account name to find the password
	 * @param newPassword - new password of the account    
	 */
	public void storeNewPassword(String account,String newPassword){
		
		//
		// Store the new password in the in memory list
		//
		for(int x = 0; x < logins.size(); x++){
			passwordStore pass = logins.get(x);
			passwordStore newPass = new passwordStore();
			if(pass.login.equals(account)){
				newPass.password = new String(newPassword);
				newPass.login = new String(account);
				newPass.encryptedPassword = new String(base64CoderFactory.encodeString(pass.password));
				logins.remove(x);
				logins.add(newPass);
				break;
			}	
		}
		//
		// write out the new file
		//
		writeNewPasswordFile(writeFileEncrypted);
		
		
	}// storeNewPassword
	private void writeNewPasswordFile(boolean encrypted){
		//
		// rename the password.log file
		//
		String loginFile = new String(osInfo.buildWorkingDirectoryPath(osInfo.setFilePath(passwordDirectory))+osInfo.fileSeperator()+passwordFileName);
		File file = new File(loginFile);
		File file2 = new File(loginFile+".old");

		if (file2.exists()){
			System.out.println("found old password.log.old and deleting it");
			file2.delete();
		}
		if(file.renameTo(file2)){
			BufferedReader br = null;
			PrintWriter passFile = null;
			try {
				br = new BufferedReader(new FileReader(loginFile+".old" ));
				String line;
				//
				// Check to see if the file already exists
				//
				File f = new File(loginFile);
				if(f.exists()){
					System.out.println("renamed passwords.log exists, failed to save new password");
					br.close();
					return;
				}
				
				passFile = new PrintWriter(loginFile, "UTF-8");
				boolean listWritten = false;
				while ((line = br.readLine()) != null) {
					String newLine = new String(line);
					if(newLine.contains("# Created:")){
						newLine = new String("# Created: "+ osInfo.getCurrentDateAndUTCTime());
					}
					if(newLine.contains("passwords.encrypted=")){
						if(encrypted){
							newLine = new String("passwords.encrypted=yes");
						}else{
							newLine = new String("passwords.encrypted=no");
						}
					}
					if(newLine.contains("login=")){
						if(!listWritten){
							for(int x = 0; x < logins.size(); x++){
								passwordStore pass = logins.get(x);
								if(null != pass){
									if(encrypted){
										newLine = new String("login="+pass.login+","+ pass.encryptedPassword);
									}else{
										newLine = new String("login="+pass.login+","+ pass.password);
									}
									passFile.write(newLine);
									passFile.write("\r\n");
								}
								listWritten = true;
							}
							newLine = new String("");
						}else{
							newLine = new String("");
						}
					}
					passFile.write(newLine);
					passFile.write("\r\n");
				}
				passFile.flush();
				passFile.close();
				br.close();
			} catch (FileNotFoundException fnfe) {
				System.out.println("passwords FileNotFoundException: "+fnfe );
				
			} catch (IOException io) {
				System.out.println("passwords File IOException: "+io );
			} finally{
				System.out.println("passwords file saved,deleting password.log.old");
				File oldLog = new File(loginFile+".old");
				if (oldLog.exists()){
					System.out.println("Delete old password.log.old");
					oldLog.delete();
				}
			}
		}
	
	}// writeNewPasswordFile
	/**
	 * This method demonstrates getPasswordForAccount().
	 * <br>  All customer passwords are stored in the password.log file. 
	 * <br>
	 * @param account - account name to find the password
	 * @return password - password of the account passed in    
	 */
	public String getPasswordForAccount(String account){
		String password = null;
		for(int x = 0; x < logins.size(); x++){
			passwordStore pass = logins.get(x);
			if(pass.login.equals(account)){
				password = new String(pass.password);
				break;
			}	
		}
		return password;
	}
	private void selfTest(){
		//
		// Check the reading in of the file and storing the data
		//
		for(int x = 0; x < logins.size();x++){
			passwordStore pass = logins.get(x);
			System.out.println("login: "+pass.login+"  password: "+pass.password+ " Encryption: "+pass.encryptedPassword);
			
		}
		//
		// Check getting the password
		//
		// login=frank , frank1
		System.out.println("Testing the password for frank should be frankX");
		System.out.println("frank's password is " +getPasswordForAccount("frank"));
		//
		// Store Franks password
		//
		System.out.println("Storing a new password for frank");
		storeNewPassword("frank","frank4");
	}//selfTest
	
	 //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			sTestCustomerAccountManagerFactory pom = null;
			
			pom = new sTestCustomerAccountManagerFactory();
			if(pom != null){
				pom.selfTest();
			}
			
		} // end Main
	 } // end Inner class Test

}// sTestCustomerAccountManagerFactory
