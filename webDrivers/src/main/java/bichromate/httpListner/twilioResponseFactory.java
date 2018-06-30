package bichromate.httpListner;

import java.util.HashMap;
import java.util.Map;

import com.twilio.http.TwilioRestClient;
import com.twilio.jwt.client.OutgoingClientScope.Builder;

//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;

//import com.twilio.Twilio;
//import com.twilio.sdk.TwilioRestClient;
//import com.twilio.sdk.verbs.Redirect;
//import com.twilio.twiml.Body;
//import com.twilio.twiml.Message;
//import com.twilio.twiml.MessagingResponse;
//import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.Body;
import com.twilio.twiml.Message;
import com.twilio.twiml.Redirect;
import com.twilio.twiml.MessagingResponse;
import com.twilio.twiml.TwiMLException;

public class twilioResponseFactory {
	
	// 
		// Find your Account Sid and Token at twilio.com/user/account
		//
		private static final String ACCOUNT_SID = "ACba359de1ffb8da045406b914928705b7";
		private static final String AUTH_TOKEN = "26f259e9c33701155d0c084d4beecbdf";
		private static final String FROM_PHONE_NUMBER =  "+18315349078";
		private static final String TO_PHONE_NUMBER =  "+14084803723"; 

	public twilioResponseFactory(){ 
		//Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
	}//twilioFactory
	
	public void newMessage(){
		 Body body = new Body("Hello World!");
	        Message message = new Message.Builder().body(body).build();
	        Redirect redirect = new Redirect.Builder()
	            .url("https://demo.twilio.com/sms/welcome").build();
	        MessagingResponse response = new MessagingResponse.Builder()
	            .message(message).redirect(redirect).build();

	        try {
	            System.out.println(response.toXml());
	        } catch (TwiMLException e) {
	            e.printStackTrace();
	        }
	}
	 
	// 
	// SelfTest
	//
	public void AutoRespond() { 
		/*
			Builder myBuilder = new Builder("Hi");
	          TwilioRestClient client = new TwilioRestClient(myBuilder);
	          Map<String, String> params = new HashMap<String, String>();
	          String body = request.getParameter("Body");
	          if( body.equals("hello") ){
	        	  Message message3 = new Message ("Hi!");
	          }else if( body.equals("bye") ){
	        	  Message message3 = new Message ("bye");
	          }
	      */   
	  }
	// 
	// SelfTest
	//
	protected void selfTest(){
		AutoRespond();
	}
	
	// 
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public  static void main(final String[] args){
		
			twilioResponseFactory tf = new twilioResponseFactory();
			tf.selfTest();
		}
		
	}//main
	
}//Test
	
