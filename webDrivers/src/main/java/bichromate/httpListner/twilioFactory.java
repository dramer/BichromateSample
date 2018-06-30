package bichromate.httpListner;

import com.twilio.Twilio;
//import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.verbs.Body;
import com.twilio.sdk.verbs.Redirect;

public class twilioFactory {
	

	// 
	// Find your Account Sid and Token at twilio.com/user/account
	//
	private static final String ACCOUNT_SID = "ACba359de1ffb8da045406b914928705b7";
	private static final String AUTH_TOKEN = "26f259e9c33701155d0c084d4beecbdf";
	private static final String FROM_PHONE_NUMBER =  "+18315349078";
	private static final String TO_PHONE_NUMBER =  "+14084803723"; 
	
	public String accountSID = null;
	  
	public twilioFactory(){ 
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	}//twilioFactory
	   
	protected void selfTest(){
		try{
			Message message = Message.creator(/* TO */ new PhoneNumber(TO_PHONE_NUMBER),new PhoneNumber(/* FROM */ FROM_PHONE_NUMBER), "twilioFactory:selfTest. Respond with a test name to run.").create();
			accountSID = new String(message.getSid());
			System.out.println(message.getSid());
		}catch (Exception e){
			System.out.println(e);
		}
		//
		//
		//
		getMessage();
	}//selfTest
	  
	public void getMessage(){

        // Get an object from its sid. If you do not have a sid,
        // check out the list resource examples on this page
		try{
			
				Message message = Message.fetcher(accountSID).fetch();
				System.out.println(message.getBody());
			
		}catch (Exception e){
			System.out.println(e);
		}
	}
	
	public void sendSMS(String toNumber, String message){
		  
		Message message1 = Message.creator(/* TO */ new PhoneNumber(toNumber),new PhoneNumber(/* FROM */ FROM_PHONE_NUMBER), message).create();

		System.out.println(message1.getSid());
		
	
	  }//sendSMS
	  
	  public static class Test
	  {
	 	public  static void main(final String[] args){
	 		
	 		twilioFactory tf = new twilioFactory();
	 		tf.selfTest();
	 		
	 	}//main
	 	
	 }//Test
}//twilioFactory


