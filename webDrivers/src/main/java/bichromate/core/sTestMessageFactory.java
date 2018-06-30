/*
 * sTestMessageFactory.java	1.0 2013/01/23
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
 * ZAPI HELP
 * http://docs.getzephyr.apiary.io/#reference/cycleresource/get-default-issue-type
 * 
 * HELP
 * https://docs.atlassian.com/jira/REST/7.0-SNAPSHOT/#api/2/issue-addComment
 * 
 * 
 */

package bichromate.core;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message; 
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
 
public final class sTestMessageFactory {	
 
	private static Properties gMailConfig = new Properties();
	
	private String emailAddress = null;
	private String emailPassword = null;
	private String portNumber = null;
	private String debugging = null;
	
	 private static ResourceBundle resources;
	    
	    static
		{
			try
			{
				resources = ResourceBundle.getBundle("core.sTestMessageFactory",Locale.getDefault());
			} catch (MissingResourceException mre) {
				System.out.println("sTestMessageFactory.properties not found: "+mre);
				System.exit(0);
			}
		}
 
	public sTestMessageFactory()
	{
		
		setupGMail(resources);
		
	}
	public sTestMessageFactory(ResourceBundle myResources)
	{
		
		setupGMail(myResources);
		
	}
	private void setupGMail(ResourceBundle myResources){
		
		//osInfo = new sTestOSInformationFactory();
		
		emailAddress = new String(myResources.getString("sTestMessageFactory.emailAddress"));
		emailPassword = new String(myResources.getString("sTestMessageFactory.password"));
		portNumber = new String(myResources.getString("sTestMessageFactory.portNumber"));
		debugging = new String(myResources.getString("sTestMessageFactory.debugging"));
		
		
		gMailConfig.put("mail.smtp.password", emailPassword);
		gMailConfig.put("mail.transport.protocol", "smtp");
		gMailConfig.put("mail.smtp.auth", "true"); 
		gMailConfig.put("mail.smtp.starttls.enable", "true"); 
		gMailConfig.put("mail.smtp.port", portNumber);
		gMailConfig.put("mail.smtp.host", "smtp.gmail.com"); 
		
        System.out.println("Loading gmailFactory class...");
		
	}
	public void sendEmailWithAttachment(String mFrom, String mTo, String mTitle, String mText, String fileNameInImageFolder ){
		
		Session sessionTLS = Session.getInstance( gMailConfig ); 
		sessionTLS.setDebug(true);
		MimeMessage messageTLS = new MimeMessage(sessionTLS); 
		try {
			messageTLS.setFrom( new InternetAddress( mFrom ) );
			MimeMultipart multipart = new MimeMultipart();
			messageTLS.setRecipients( Message.RecipientType.TO, InternetAddress.parse( mTo ) );
			messageTLS.setRecipients( Message.RecipientType.CC, InternetAddress.parse( emailAddress) );
			MimeBodyPart tmpBp1 = new MimeBodyPart(); // subject of the email
			MimeBodyPart tmpBp2 = new MimeBodyPart(); // Content of the email
			messageTLS.setSubject( mTitle); 
			tmpBp1.setContent( mText, "text/plain");
			multipart.addBodyPart(tmpBp1);
			// 
			// File to attach
			//
			DataSource source = new FileDataSource("images/"+fileNameInImageFolder);
			tmpBp2.setDataHandler(new DataHandler(source));
			tmpBp2.setFileName(fileNameInImageFolder);
	        multipart.addBodyPart(tmpBp2);
	       
	        //
	        // Put all the parts together and send the email
	        //
			messageTLS.setContent(multipart);			
		} catch (MessagingException e) {
			e.printStackTrace();
		} 		
		 
		Transport transportTLS;
		try {
			transportTLS = sessionTLS.getTransport();
			transportTLS.connect( "smtp.gmail.com" , 587, mFrom, gMailConfig.getProperty("mail.smtp.password") );
			transportTLS.sendMessage( messageTLS, messageTLS.getAllRecipients() ); 
			transportTLS.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException ex){
			System.err.println("Cannot send email. " + ex);
		}
	}
	public void sendTestResultAsAttachment(String mFrom, String mTo, String mTitle, String mText, String htmlPayload, String fileToAttach ){
		
		
		Session sessionTLS = Session.getInstance( gMailConfig ); 
		sessionTLS.setDebug(true);
		htmlPayload = new String(fixPayLoadToShowAttachments(htmlPayload));
		MimeMessage messageTLS = new MimeMessage(sessionTLS); 
		try {
			messageTLS.setFrom( new InternetAddress( mFrom ) );
			MimeMultipart multipart = new MimeMultipart();
			messageTLS.setRecipients( Message.RecipientType.TO, InternetAddress.parse( mTo ) );
			messageTLS.setRecipients( Message.RecipientType.CC, InternetAddress.parse( emailAddress) );
			MimeBodyPart tmpBp1 = new MimeBodyPart(); // subject of the email
			MimeBodyPart tmpBp2 = new MimeBodyPart(); // Content of the email
			MimeBodyPart tmpBp3 = new MimeBodyPart(); // Attachment file
			MimeBodyPart tmpBp4 = new MimeBodyPart(); // oDesk image
			MimeBodyPart tmpBp5 = new MimeBodyPart(); // passed or failed image
			messageTLS.setSubject( mTitle); 
			tmpBp1.setContent( mText, "text/plain");
			multipart.addBodyPart(tmpBp1);
			tmpBp2.setContent( htmlPayload, "text/html");
			multipart.addBodyPart(tmpBp2);
			// 
			// File to attach
			//
			DataSource source = new FileDataSource(fileToAttach);
			tmpBp3.setDataHandler(new DataHandler(source));
			tmpBp3.setFileName(fileToAttach);
	        multipart.addBodyPart(tmpBp3);
	        //
	        // embedded image to attach
	        //
	        source = new FileDataSource("test-output/oDesk.JPG");
			tmpBp4.setDataHandler(new DataHandler(source));
			tmpBp4.setFileName("test-output/oDesk.JPG");
			tmpBp4.setHeader("Content-ID","<odesk-1>");
	        multipart.addBodyPart(tmpBp4);
	        //
	        //
	       
	        //
	        // embedded pass/fail image to attach
	        //
	        if(htmlPayload.contains("Results Passed")){
	        	source = new FileDataSource("test-output/passed.png");
	        	tmpBp5.setDataHandler(new DataHandler(source));
	        	tmpBp5.setFileName("passed.png");
	        	tmpBp5.setHeader("Content-ID","<odesk-2>");
	        }else{
	        	source = new FileDataSource("test-output/failed.png");
	        	tmpBp5.setDataHandler(new DataHandler(source));
	        	tmpBp5.setFileName("failed.png");
	        	tmpBp5.setHeader("Content-ID","<odesk-2>");
	        }
	        multipart.addBodyPart(tmpBp5);
	        //
	        // Put all the parts together and send the email
	        //
			messageTLS.setContent(multipart);			
		} catch (MessagingException e) {
			e.printStackTrace();
		} 		
		 
		Transport transportTLS;
		try {
			transportTLS = sessionTLS.getTransport();
			transportTLS.connect( "smtp.gmail.com" , 587, mFrom, gMailConfig.getProperty("mail.smtp.password") );
			transportTLS.sendMessage( messageTLS, messageTLS.getAllRecipients() ); 
			transportTLS.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException ex){
			System.err.println("Cannot send email. " + ex);
		}
		
	}
	public void sendTestNGResult(String mFrom, String mTo, String mTitle, String mText, String htmlPayload ) 
	{
		Session sessionTLS = Session.getInstance( gMailConfig ); 
		sessionTLS.setDebug(true);
		 
		MimeMessage messageTLS = new MimeMessage(sessionTLS); 
		try {
			messageTLS.setFrom( new InternetAddress( mFrom ) );
			MimeMultipart multipart = new MimeMultipart();
			messageTLS.setRecipients( Message.RecipientType.TO, InternetAddress.parse( mTo ) );
			messageTLS.setRecipients( Message.RecipientType.CC, InternetAddress.parse( emailAddress ) );
			MimeBodyPart tmpBp1 = new MimeBodyPart();
			MimeBodyPart tmpBp2 = new MimeBodyPart();
			messageTLS.setSubject( mTitle); 
			tmpBp1.setContent( mText, "text/plain");
			multipart.addBodyPart(tmpBp1);
			tmpBp2.setContent( htmlPayload, "text/html");
			multipart.addBodyPart(tmpBp2);
			messageTLS.setContent(multipart);			
		} catch (MessagingException e) {
			e.printStackTrace();
		} 		
		 
		Transport transportTLS;
		try {
			transportTLS = sessionTLS.getTransport();
			transportTLS.connect( "smtp.gmail.com" , 587, mFrom, gMailConfig.getProperty("mail.smtp.password") );
			transportTLS.sendMessage( messageTLS, messageTLS.getAllRecipients() ); 
			transportTLS.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException ex){
			System.err.println("Cannot send email. " + ex);
		}
		
	}
	private String fixPayLoadToShowAttachments(String htmlPayload){
		
		String find = new String("<img src=\"oDesk.JPG\"");
		String find2 = new String("<img src=\"passed.png\"");
		String find3 = new String("<img src=\"failed.png\"");
		String replace = new String("<img src=\"cid:odesk-1\"");
		String replace2 = new String("<img src=\"cid:odesk-2\"");
		// <img src=\"cid:the-img-1\"/>
		htmlPayload = new String(htmlPayload.replaceAll(find, replace));
		htmlPayload = new String(htmlPayload.replaceAll(find2, replace2));
		htmlPayload = new String(htmlPayload.replaceAll(find3, replace2));
		return (htmlPayload);
		
	}
	public void sendTestNGResult(String mFrom, String mTo, String mTitle, String mText, String htmlPayload, String attachment) 
	{
		Session sessionTLS = Session.getInstance( gMailConfig ); 
		sessionTLS.setDebug(true);
		
		htmlPayload = new String(fixPayLoadToShowAttachments(htmlPayload));
		 
		MimeMessage messageTLS = new MimeMessage(sessionTLS); 
		try {
			messageTLS.setFrom( new InternetAddress( mFrom ) );
			MimeMultipart multipart = new MimeMultipart(/* "Related"*/); // Note the "Related" tells mimeMultipart that items are related
			messageTLS.setRecipients( Message.RecipientType.TO, InternetAddress.parse( mTo ) );
			messageTLS.setRecipients( Message.RecipientType.CC, InternetAddress.parse( emailAddress) );
			MimeBodyPart tmpBp1 = new MimeBodyPart();
			MimeBodyPart tmpBp2 = new MimeBodyPart();
			messageTLS.setSubject( mTitle); 
			tmpBp1.setContent( mText, "text/plain");
			multipart.addBodyPart(tmpBp1);
			tmpBp2.setContent( htmlPayload, "text/html");
			multipart.addBodyPart(tmpBp2);
			//
			// Add attachment file
			//
			// Part two is attachment
			MimeBodyPart imageBodyPart;
			
		    imageBodyPart = new MimeBodyPart();
		    DataSource source = new FileDataSource(attachment);
		    imageBodyPart.setDataHandler(new DataHandler(source));
		    imageBodyPart.setFileName(attachment);
		    imageBodyPart.setContentID("image/jpg");
		    imageBodyPart.setHeader("Content-ID","<odesk-1>");
		    multipart.addBodyPart(imageBodyPart);
		    //
			// Set content of entire msg
			//
			messageTLS.setContent(multipart);			
		} catch (MessagingException e) {
			e.printStackTrace();
		} 		
		 
		Transport transportTLS;
		try {
			transportTLS = sessionTLS.getTransport();
			transportTLS.connect( "smtp.gmail.com" , 587, mFrom, gMailConfig.getProperty("mail.smtp.password") );
			transportTLS.sendMessage( messageTLS, messageTLS.getAllRecipients() ); 
			transportTLS.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException ex){
			System.err.println("Cannot send email. " + ex);
		}
		
	}
	public void sendTestNGResult(String mFrom, String mTo, String mTitle, String mText, String htmlPayload, String attachment,String attachment2) 
	{
		Session sessionTLS = Session.getInstance( gMailConfig ); 
		sessionTLS.setDebug(true);
		
		htmlPayload = new String(fixPayLoadToShowAttachments(htmlPayload));
		 
		MimeMessage messageTLS = new MimeMessage(sessionTLS); 
		try {
			messageTLS.setFrom( new InternetAddress( mFrom ) );
			MimeMultipart multipart = new MimeMultipart(/* "Related"*/); // Note the "Related" tells mimeMultipart that items are related
			messageTLS.setRecipients( Message.RecipientType.TO, InternetAddress.parse( mTo ) );
			messageTLS.setRecipients( Message.RecipientType.CC, InternetAddress.parse( emailAddress) );
			MimeBodyPart tmpBp1 = new MimeBodyPart();
			MimeBodyPart tmpBp2 = new MimeBodyPart();
			messageTLS.setSubject( mTitle); 
			tmpBp1.setContent( mText, "text/plain");
			multipart.addBodyPart(tmpBp1);
			tmpBp2.setContent( htmlPayload, "text/html");
			multipart.addBodyPart(tmpBp2);
			//
			// Add attachment file
			//
			// Part two is attachment
			MimeBodyPart imageBodyPart;
			
		    imageBodyPart = new MimeBodyPart();
		    DataSource source = new FileDataSource(attachment);
		    imageBodyPart.setDataHandler(new DataHandler(source));
		    imageBodyPart.setFileName(attachment);
		    imageBodyPart.setContentID(".jpg");
		    imageBodyPart.setHeader("Content-ID","<odesk-1>");
		    multipart.addBodyPart(imageBodyPart);
		    //
		    // Part three is attachment2
		    //
		    MimeBodyPart zipBodyPart;
			
		    zipBodyPart = new MimeBodyPart();
		    DataSource source2 = new FileDataSource(attachment2);
		    zipBodyPart.setDataHandler(new DataHandler(source2));
		    zipBodyPart.setFileName(attachment2);
		    zipBodyPart.setContentID(".zip");
		    multipart.addBodyPart(zipBodyPart);
		    //
			// Set content of entire msg
			//
			messageTLS.setContent(multipart);			
		} catch (MessagingException e) {
			e.printStackTrace();
		} 		
		 
		Transport transportTLS;
		try {
			transportTLS = sessionTLS.getTransport();
			transportTLS.connect( "smtp.gmail.com" , 587, mFrom, gMailConfig.getProperty("mail.smtp.password") );
			transportTLS.sendMessage( messageTLS, messageTLS.getAllRecipients() ); 
			transportTLS.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException ex){
			System.err.println("Cannot send email. " + ex);
		}
		
	}
	public void sendTestNGAsAttachment(String mFrom, String mTo, String mTitle, String mText, String attachment) 
	{
		Session sessionTLS = Session.getInstance( gMailConfig ); 
		sessionTLS.setDebug(true);
		 
		MimeMessage messageTLS = new MimeMessage(sessionTLS); 
		try {
			messageTLS.setFrom( new InternetAddress( mFrom ) );
			MimeMultipart multipart = new MimeMultipart();
			messageTLS.setRecipients( Message.RecipientType.TO, InternetAddress.parse( mTo ) );
			messageTLS.setRecipients( Message.RecipientType.CC, InternetAddress.parse( emailAddress) );
			MimeBodyPart tmpBp1 = new MimeBodyPart();
			MimeBodyPart tmpBp2 = new MimeBodyPart();
			messageTLS.setSubject( mTitle); 
			tmpBp1.setContent( mText, "text/plain");
			multipart.addBodyPart(tmpBp1);
			tmpBp2.setContent( mTitle, "text/plain");
			multipart.addBodyPart(tmpBp2);
			//
			// Add attachment file
			//
			// Part two is attachment
			MimeBodyPart messageBodyPart;
			
		    messageBodyPart = new MimeBodyPart();
		    DataSource source = new FileDataSource(attachment);
		    messageBodyPart.setDataHandler(new DataHandler(source));
		    messageBodyPart.setFileName(attachment);
		    messageBodyPart.setContentID("image/jpg");
		    multipart.addBodyPart(messageBodyPart);
			//
			// Set content of entire msg
			//
			messageTLS.setContent(multipart);			
		} catch (MessagingException e) {
			e.printStackTrace();
		} 		
		 
		Transport transportTLS;
		try {
			transportTLS = sessionTLS.getTransport();
			transportTLS.connect( "smtp.gmail.com" , 587, mFrom, gMailConfig.getProperty("mail.smtp.password") );
			transportTLS.sendMessage( messageTLS, messageTLS.getAllRecipients() ); 
			transportTLS.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException ex){
			System.err.println("Cannot send email. " + ex);
		}
		
	}
	
	
	public void sendRegularEmail(String mFrom, String mTo, String mTitle, String mText ) 
	{
		Session sessionTLS = Session.getInstance( gMailConfig );
		
		if(debugging.toLowerCase().equals("true")){
			sessionTLS.setDebug(true);
		}else{
			sessionTLS.setDebug(false);
		}
		 
		MimeMessage messageTLS = new MimeMessage(sessionTLS); 
		try {
			messageTLS.setFrom( new InternetAddress( mFrom ) );
			MimeMultipart multipart = new MimeMultipart();
			messageTLS.setRecipients( Message.RecipientType.TO, InternetAddress.parse( mTo ) );
			MimeBodyPart tmpBp1 = new MimeBodyPart();
			messageTLS.setSubject( mTitle); 
			tmpBp1.setContent( mText, "text/plain");
			multipart.addBodyPart(tmpBp1);
			messageTLS.setContent(multipart);			
		} catch (MessagingException e) {
			e.printStackTrace();
		} 		
		 
		Transport transportTLS;
		try {
			transportTLS = sessionTLS.getTransport();
			String emailPassword = new String(gMailConfig.getProperty("mail.smtp.password"));
			String port = new String(gMailConfig.getProperty("mail.smtp.port"));
			
			transportTLS.connect( "smtp.gmail.com" , Integer.valueOf(port), mFrom,  emailPassword);
			transportTLS.sendMessage( messageTLS, messageTLS.getAllRecipients() ); 
			transportTLS.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException ex){
			System.err.println("Cannot send email. " + ex);
		}
		
	}//sendRegularEmail
	public void sendSMS(String mFrom, String mTo, String mTitle, String mText ) 
	{
		Session sessionTLS = Session.getInstance( gMailConfig ); 
		sessionTLS.setDebug(true);
		 
		MimeMessage messageTLS = new MimeMessage(sessionTLS); 
		try {
			messageTLS.setFrom( new InternetAddress( mFrom ) );
			MimeMultipart multipart = new MimeMultipart();
			messageTLS.setRecipients( Message.RecipientType.TO, InternetAddress.parse( mTo ) );
			MimeBodyPart tmpBp1 = new MimeBodyPart();
			messageTLS.setSubject( mTitle); 
			tmpBp1.setContent( mText, "text/plain");
			multipart.addBodyPart(tmpBp1);
			messageTLS.setContent(multipart);			
		} catch (MessagingException e) {
			e.printStackTrace();
		} 		
		 
		Transport transportTLS;
		try {
			String password = new String(gMailConfig.getProperty("mail.smtp.password"));
			transportTLS = sessionTLS.getTransport();
			transportTLS.connect( "smtp.gmail.com" , 587, mFrom, password );
			transportTLS.sendMessage( messageTLS, messageTLS.getAllRecipients() ); 
			transportTLS.close();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException ex){
			System.err.println("Cannot send email. " + ex);
			System.err.println("Cannot send email. " + ex);
			
			/*
			 * Head over to Account Security Settings (https://www.google.com/settings/security/lesssecureapps) 
			 * and enable "Access for less secure apps", this allows you to use the google 
			 * smtp for clients other than the official ones.
			 */
			
			
		}
		
	}//sendSMS
	
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public static void main(final String[] args){
	 	
	 		sTestMessageFactory sms = new sTestMessageFactory();
	 		sms.sendRegularEmail("davidwramertesting@gmail.com","davidwramer@yahoo.com", "Bichromate Unit Testing","Unit Testing gmailPlugin");
	 		sms.sendSMS("davidwramertesting@gmail.com","4084803723@vtext.com", "Bichromate Unit Test","Unit Testing gmailPlugin");

	 	} // main
	 }// Test
	
	
} // gmailPlugin
