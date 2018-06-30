/*
 * sTestHipChatFactory.java	1.0 2013/01/23
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

import java.io.IOException;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import io.evanwong.oss.hipchat.v2.HipChatClient;
import io.evanwong.oss.hipchat.v2.commons.NoContent;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;
import io.evanwong.oss.hipchat.v2.rooms.SendRoomNotificationRequestBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;



/**
 * This class Demonstrates oTestHipChatFactory().
 * <br>This class factory is used to access HipChat and send AT messages to any team
 * <br>
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("unused")
public class sTestHipChatFactory {

	private static ResourceBundle resources;
	private String resultsRoom = null;
	
	//
	// access information
	//
	private String login = null;
	private String password = null;
	private String accessKey = null;
	
			
	private String askQARoom = null;
	private String QAResultsRoom = null;


	static
	{
		try
		{
			resources = ResourceBundle.getBundle("common.sTestHipChatFactory",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestHipChatFactory.properties not found: "+mre);
			System.exit(0);
		}
	}
	/**
	 * This class Demonstrates sTestHipChatFactory().
	 * <br>This method sets up the hipchat connection and defines all the rooms.
	 * <br>
	 * @author davidwramer
	 * @version 1.0 
	 */
	public sTestHipChatFactory(){
		setupHipChat(resources);
	
	}// sTestHipChatFactory
	/**
	 * This class Demonstrates sTestHipChatFactory().
	 * <br>This method sets up the hipchat connection and defines all the rooms.
	 * <br>
	 * @param remoteResources passed in resource bundle when this class is used within a jar file
	 * @author davidwramer
	 * @version 1.0 
	 */
	public sTestHipChatFactory(ResourceBundle remoteResources){
		
		setupHipChat(remoteResources);
	
	}// sTestHipChatFactory
	private void setupHipChat(ResourceBundle remoteResources){
		
		login = new String(remoteResources.getString("sTestHipChatFactory.login"));
		password = new String(remoteResources.getString("sTestHipChatFactory.password"));
		accessKey = new String(remoteResources.getString("sTestHipChatFactory.accessKey"));
		askQARoom = new String(remoteResources.getString("sTestHipChatFactory.askQARoom"));
		QAResultsRoom = new String(remoteResources.getString("sTestHipChatFactory.QAResultsRoom"));
		
	}
	/**
	 * This method Demonstrates setQAResultsRoom().
	 * <br>This method is a way to override the QAResultsRoom properties file setting
	 * <br>
	 * @param roomName - new name of the hipchat room to send results to.
	 * @author davidwramer
	 * @version 1.0 
	 */
	public void setQAResultsRoom(String roomName){
		resultsRoom = new String(QAResultsRoom);
		QAResultsRoom = new  String(roomName);
	}
	/**
	 * This method Demonstrates resetQAResultsRoom().
	 * <br>This method is a way to reset the overridden QAResultsRoom properties file setting
	 * @author davidwramer
	 * @version 1.0 
	 */
	public void resetQAResultsRoom(){
		if(null != resultsRoom)
			QAResultsRoom = new  String(resultsRoom);
	}
	/**
	 * This method Demonstrates sendPassMessage().
	 * <br>This method is used to post a message to the Hipchat room that is defined by the following property QAResultsRoom
	 * <br>
	 * @param message to send to the QAResultsRoom in HipChat. QAResultsRoom is defined in the properties file
	 * @author davidwramer
	 * @version 1.0 
	 */
	public void sendPassMessage(String message){
		
		if(accessKey.isEmpty()){
			 System.err.println("accessKey is not setup. See sTestHipChatFactory.properties");
			 return;
		}
		
		if(QAResultsRoom.isEmpty()){
			 System.err.println("QAResultsRoom is not setup. See sTestHipChatFactory.properties");
			 return;
		}
		
		HipChatClient client = new HipChatClient(accessKey);
		SendRoomNotificationRequestBuilder builder = client.prepareSendRoomNotificationRequestBuilder(QAResultsRoom, message);
		Future<NoContent> future = builder.setColor(MessageColor.GREEN).setNotify(true).build().execute();

		
		//optional... if you want/need to inspect the result:
		try {
			NoContent noContent = future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.close();
	}// sendPassMessage
	
	
	
	/**
	 * This method Demonstrates sendErrorMessage().
	 * <br>This method is used to send a error message with the color RED
	 * <br> You must call configureHipChat before this call.
	 * <br>
	 * @param message - text message. You can use 
	 * @author davidwramer
	 * @version 1.0 
	 */
	public void sendErrorMessage(String message){
		
		if(accessKey.isEmpty()){
			 System.err.println("accessKey is not setup. See sTestHipChatFactory.properties");
			 return;
		}
		
		if(QAResultsRoom.isEmpty()){
			 System.err.println("QAResultsRoom is not setup. See sTestHipChatFactory.properties");
			 return;
		}
		
		HipChatClient client = new HipChatClient(accessKey);
		SendRoomNotificationRequestBuilder builder = client.prepareSendRoomNotificationRequestBuilder(QAResultsRoom, message);
		Future<NoContent> future = builder.setColor(MessageColor.RED).setNotify(true).build().execute();
		//optional... if you want/need to inspect the result:
		try {
			NoContent noContent = future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.close();
		
	}//sendErrorMessage
	/**
	 * This method Demonstrates sendWarningMessage().
	 * <br>This method is used to send a generic message with the yellow color.
	 * <br> You must call configureHipChat before this call.
	 * <br>
	 * @param message - text message. You can use
	 * @author davidwramer
	 * @version 1.0 
	 */
	public void sendWarningMessage(String message){
		
		if(accessKey.isEmpty()){
			 System.err.println("accessKey is not setup. See sTestHipChatFactory.properties");
			 return;
		}
		
		if(QAResultsRoom.isEmpty()){
			 System.err.println("QAResultsRoom is not setup. See sTestHipChatFactory.properties");
			 return;
		}
		
		HipChatClient client = new HipChatClient(accessKey);
		SendRoomNotificationRequestBuilder builder = client.prepareSendRoomNotificationRequestBuilder(QAResultsRoom, message);
		Future<NoContent> future = builder.setColor(MessageColor.YELLOW).setNotify(true).build().execute();
		//optional... if you want/need to inspect the result:
		try {
			NoContent noContent = future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.close();
		
	}//sendWarningMessage
	
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			Date date = new Date();
			String currentDate = new String(dateFormat.format(date));
			
			sTestHipChatFactory hipChat = new sTestHipChatFactory();
		
			hipChat.sendPassMessage("Bichromate:sTestHipChatFactory:sendPassMessage unit test passed");
			hipChat.sendWarningMessage("Bichromate:sTestHipChatFactory:sendWarningMessage Unit Test passed");
			hipChat.sendErrorMessage("Bichromate:sTestHipChatFactory:sendErrorMessage Unit Test passed");
			
		}// main
	}//Test
}
