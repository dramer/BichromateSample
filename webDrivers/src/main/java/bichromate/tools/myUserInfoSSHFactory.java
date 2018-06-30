/*
 * myUserInfoSSHFactory.java	1.0 2013/01/24
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

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;
/**
 * This class Demonstrates myUserInfoSSHFactory().
 * <br> myUserInfoSSHFactory is used to interact with the user when making SSH connection that has
 * <br> passkey authentication
 * <br>  
 * <br>
 * @author davidwramer
 * @version 1.0
 */
public  class myUserInfoSSHFactory implements UserInfo, UIKeyboardInteractive{
	private boolean silentMode = true;
	private String myPassword = null;
	final GridBagConstraints gbc = new GridBagConstraints(0,0,1,1,1,1,
            GridBagConstraints.NORTHWEST,
            GridBagConstraints.NONE,
            new Insets(0,0,0,0),0,0);
	private Container panel;
	String passphrase;
	JTextField passphraseField=(JTextField)new JPasswordField(20);
	
	/**
	 * This class Demonstrates myUserInfoSSHFactory().
	 * <br> myUserInfoSSHFactory is used to interact with the user when making SSH connection that has
	 * <br> passkey authentication
	 * <br>  
	 * <br>
	 * @param password . passed in when no user interaction needed
	 */
	public myUserInfoSSHFactory(String password){
		myPassword = password;
		silentMode = true;
	}
	/**
	 * This class Demonstrates myUserInfoSSHFactory().
	 * <br> myUserInfoSSHFactory is used to interact with the user when making SSH connection that has
	 * <br> passkey authentication, This constructor is used when user interaction is required
	 * <br>  
	 * <br>
	 */
	public myUserInfoSSHFactory(){
		
	}
	/**
	 * This function Demonstrates getPassword().
	 * <br> myUserInfoSSHFactory is used to interact with the user when making SSH connection that has
	 * <br>  
	 * <br>
	 * @return String password that was set in the constructor
	 */
	public String getPassword(){ return myPassword; }
	/**
	 * This function Demonstrates promptYesNo().
	 * <br> Function used when prompting user interaction to connect to specific server
	 * <br>  
	 * <br>
	 * @param str - Server information that is being connected to
	 * @return boolean  true if connection should continue, false otherwise
	 */
	public boolean promptYesNo(String str){
		if(silentMode){
			return true;
		}else{
			Object[] options={ "yes", "no" };
		    int foo=JOptionPane.showOptionDialog(null, str,"Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		    return foo==0;
		}
	}// promptYesNo()
		      
	
	/**
	 * This function Demonstrates getPassphrase().
	 * <br> Function returns the passphrase for SSH authentication of key file
	 * <br>  
	 * <br>
	 * @return String password
	 */	     
	public String getPassphrase(){ 
		return passphrase; 
	}
	/**
	 * This function Demonstrates promptPassphrase().
	 * <br> Function used prompted user for SSH key passphrase
	 * <br>  
	 * <br>
	 * @param message - message to be added to the dialog
	 * @return boolean true if passphrase was entered
	 */	     	
	public boolean promptPassphrase(String message){
		
		if(silentMode){
			passphrase = getPassword();
			return true;
		}else{
			
		
			Object[] ob={passphraseField};
		    int result= JOptionPane.showConfirmDialog(null, ob, message,JOptionPane.OK_CANCEL_OPTION);
		    if(result==JOptionPane.OK_OPTION){
		    	passphrase=passphraseField.getText();
		        return true;
		    }else{ 
		    	return false; 
		    }
		}
	} // promptPassphrase()
	/**
	 * This function Demonstrates promptPassword().
	 * <br> Function used prompted user for SSH key passphrase
	 * <br>  
	 * <br>
	 * @param message - message to be added to the dialog
	 * @return boolean if passphrase was entered
	 */	     	
	public boolean promptPassword(String message){ 
		return true; 
	}
	/**
	 * This function Demonstrates showMessage().
	 * <br> Function used prompted user for SSH key passphrase
	 * <br>  
	 * <br>
	 * @param message - message to be added to the dialog
	 */	     	
	public void showMessage(String message){
		JOptionPane.showMessageDialog(null, message);
	}
	/**
	 * This function Demonstrates promptKeyboardInteractive().
	 * <br> Function used prompted user for SSH key passphrase
	 * <br>  
	 * <br>
	 * @param destination - destination information
	 * @param name - name
	 * @param instruction - instruction
	 * @param prompt - prompt
	 * @param echo - echo
	 * @return String [] - interactions
	 */	     	
	
	public String[] promptKeyboardInteractive(String destination,String name,String instruction,String[] prompt,boolean[] echo){
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
 
		gbc.weightx = 1.0;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.gridx = 0;
		panel.add(new JLabel(instruction), gbc);
		gbc.gridy++;
 
		gbc.gridwidth = GridBagConstraints.RELATIVE;
	     
	    JTextField[] texts=new JTextField[prompt.length];
	    for(int i=0; i<prompt.length; i++){
	    	gbc.fill = GridBagConstraints.NONE;
	        gbc.gridx = 0;
	        gbc.weightx = 1;
	        panel.add(new JLabel(prompt[i]),gbc);
	 
	        gbc.gridx = 1;
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        gbc.weighty = 1;
	        if(echo[i]){
	          texts[i]=new JTextField(20);
	        }
	        else{
	          texts[i]=new JPasswordField(20);
	        }
            panel.add(texts[i], gbc);
            gbc.gridy++;
	    }
	     
	    if(JOptionPane.showConfirmDialog(null, panel, destination+": "+name,JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE)==JOptionPane.OK_OPTION){
	        String[] response=new String[prompt.length];
	        for(int i=0; i<prompt.length; i++){
	          response[i]=texts[i].getText();
	        }
	    	return response;
	    }else{
	    	return null;  // cancel
	    }
	}//promptKeyboardInteractive
}//myUserInfoSSHFactory


