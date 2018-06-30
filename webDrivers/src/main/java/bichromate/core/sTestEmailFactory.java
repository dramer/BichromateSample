/*
 * oTestEmailFactory.java	1.0 2013/01/23
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



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

 
public class sTestEmailFactory {
	
	public String mailFrom, mailTo, mailTitle, mailText, htmlPayLoad;	
	
	public void configureMail(String mf, String mt, String mtitle, String mtext) {
		mailFrom = new String(mf);
		mailTo = new String(mt);
		mailTitle = new String (mtitle);
		mailText = new String (mtext);
		System.out.println("Registered email target: " + mailTo);
		System.out.println("Registered sender: " + mailFrom);
	}
	//
	// Fix all the parameter table titles with the correct name
	//
	private void getTestNGResult(String[] params) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File("test-output/emailable-report.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			int paramNum = 1;
			for(int cnt = 0; cnt < params.length;cnt++){
				
				String find = new String("Parameter #"+paramNum+"<");
				String replace = new String(params[cnt]+"<");
				htmlPayLoad = new String(htmlPayLoad.replaceAll(find, replace));
				paramNum++;
				// <th>Parameter #1</th>
			}
			
		} finally { 
			
			stream.close(); 
		}
	}
	private void getTestNGResult() throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File("test-output/emailable-report.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	private void getTestNGResult(String reportFileName) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File("test-output/"+reportFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	public void getSkillsTestResult(boolean passed) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File(passed?"test-output/skills_passed.html":"test-output/skills_failed.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	public void getContractorOnBoardingTestResult(boolean passed) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File(passed?"test-output/ContractorOnBoarding_passed.html":"test-output/ContractorOnBoarding_failed.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	public void getContractorAccountActivityTestResult(boolean passed) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File(passed?"test-output/ContractorAccountActivity_passed.html":"test-output/ContractorAccountActivity_failed.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	public void getContractorOnboardingTestResult(boolean passed) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File(passed?"test-output/ContractorOnBoarding_passed.html":"test-output/ContractorOnBoarding_failed.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	public void getQTAllocationTestResult(boolean passed) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File(passed?"test-output/QTAllocation_passed.html":"test-output/QTAllocation_failed.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	public void getTalentTestResult(boolean passed) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File(passed?"test-output/ContractorTalent_passed.html":"test-output/ContractorTalent_failed.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	public void getContactListTestResult(boolean passed) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File(passed?"test-output/ContactData_passed.html":"test-output/ContactData_failed.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	public void getClientAccountActivityTestResult(boolean passed) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File(passed?"test-output/ClientAccountActivity_passed.html":"test-output/ClientAccountActivity_failed.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	public void getOnboardingTestResult(boolean passed) throws IOException {
		FileInputStream stream = null;
		try { // testNG stores results in the ./test-output directory by default
			stream = new FileInputStream(new File(passed?"test-output/ClientOnboarding_passed.html":"test-output/ClientOnboarding_failed.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		try { 
			FileChannel fc = stream.getChannel(); 
			MappedByteBuffer bb = fc.map( FileChannel.MapMode.READ_ONLY, 0, fc.size() ); 
			/* Instead of using default, pass in a decoder. */ 
			htmlPayLoad = Charset.defaultCharset().decode(bb).toString();
			//
			// Here is where we will change the parameters to correct data provider names
			//
			
			
		} finally { 
			stream.close(); 
		}
	}
	public void sendTestResultsAsAttachment(String attachment) throws IOException {
		sTestMessageFactory gmailer = new sTestMessageFactory();
		gmailer.sendTestResultAsAttachment(mailFrom, mailTo, mailTitle, mailText, htmlPayLoad,attachment );
	}
	public void sendTestNGAsAttachment(String attachment) throws IOException {
		sTestMessageFactory gmailer = new sTestMessageFactory();
		gmailer.sendTestResultAsAttachment(mailFrom, mailTo, mailTitle, mailText, htmlPayLoad,attachment );
	}
	public void sendNewTestResult(String reportFile, String attachment) throws IOException {
		getTestNGResult(reportFile);
		sTestMessageFactory gmailer = new sTestMessageFactory();
		gmailer.sendTestNGResult(mailFrom, mailTo, mailTitle, mailText, htmlPayLoad,attachment );
	}
	public void sendNewTestResult(String reportFile, String attachment,String attachment2) throws IOException {
		getTestNGResult(reportFile);
		sTestMessageFactory gmailer = new sTestMessageFactory();
		gmailer.sendTestNGResult(mailFrom, mailTo, mailTitle, mailText, htmlPayLoad,attachment,attachment2 );
	}
	public void sendNewTestResult(String reportFile) throws IOException {
		getTestNGResult(reportFile);
		sTestMessageFactory gmailer = new sTestMessageFactory();
		gmailer.sendTestNGResult(mailFrom, mailTo, mailTitle, mailText, htmlPayLoad );
	}
	
	public void sendTestNGResult() throws IOException {
		getTestNGResult();
		sTestMessageFactory gmailer = new sTestMessageFactory();
		gmailer.sendTestNGResult(mailFrom, mailTo, mailTitle, mailText, htmlPayLoad );
	}
	public void sendTestNGResult(String[] params) throws IOException {
		getTestNGResult(params);
		sTestMessageFactory gmailer = new sTestMessageFactory();
		gmailer.sendTestNGResult(mailFrom, mailTo, mailTitle, mailText, htmlPayLoad );
	}
	public void sendRegularEmail() throws IOException {
		sTestMessageFactory gmailer = new sTestMessageFactory();
		gmailer.sendRegularEmail(mailFrom, mailTo, mailTitle, mailText );
	}
	public void sendEmailWithAttachment(String mailFrom, String mailTo,String mailTitle,String mailText, String fileName) throws IOException {
		sTestMessageFactory gmailer = new sTestMessageFactory();
		gmailer.sendEmailWithAttachment(mailFrom, mailTo, mailTitle, mailText,fileName );
	}
	 //
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public static void main(final String[] args){
	 		
	 		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	 		Date date = new Date();
	 		 
	 		sTestEmailFactory ef = new sTestEmailFactory();
	 		ef.configureMail("davidwramertesting@gmail.com", "davidwramer@yahoo.com", "sTestEmailFactory Unit Test", dateFormat.format(date));
	 		try{
	 			//
	 			// Takes the html file and fills in the htmlPayLoad
	 			// Fixes the htlm to add  src= CID:odesk-1
	 			// and attaches the image to the email
	 			//
	 			// ef.sendNewTestResult("emailable-report.html","test-output/oDesk.JPG"); 
	 			//
	 			// Takes the attachment and fills in the htmlPayload
	 			//
	 			// ef.sendNewTestResult("oDeskLoginTest2013-03-01-10-58-53.html");
	 			//
	 			// ?????????????????
	 			//
	 			// ef.sendTestNGAsAttachment("oDeskLoginTest2013-03-01-10-58-53.html");
	 			//
	 			// Test gmailPlugin
	 			//
	 			// ef.sendRegularEmail();
				ef.sendEmailWithAttachment("davidwramertesting@gmail.com", "davidwramer@yahoo.com", "sTestEmailFactory Unit Test",
	 					                "This message says play Destiny", "destiny.jpg");
	 			//
	 			//
	 		}catch(IOException e){
	 			System.out.println("error getting test results");
	 		}
	 		
	 	} // main
	 }// Test
	
}// emailFactory