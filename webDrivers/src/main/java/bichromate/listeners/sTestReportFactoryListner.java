/*
 * sTestReportFactoryListner.java	1.0 2013/01/23
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
 * Information:
 * 	Report Factory works off of the TestNG test-output\emailable-report.html
 * 
 *  The idea is after the testNG test is complete. TestNG writes out the emailable-report.
 * 	reportFactory reads the emailable-report.html and updates it with sTest information.
 * 
 */

package bichromate.listeners;

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

import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
// import java.util.regex.Matcher;

import java.util.regex.PatternSyntaxException;

import org.apache.commons.io.FileUtils;
import org.testng.ISuite;
import org.testng.reporters.EmailableReporter;
import org.testng.xml.XmlSuite;

import bichromate.core.sTestEmailFactory;


/**
 * This class Demonstrates reportFactory().
 * <br>This class overrides testNG EmailableReporter to generate Bichromate specific reports
 * <br>
 * @author davidwramer
 * @version 1.0
 */
public class sTestReportFactoryListner extends EmailableReporter{
	
	private static ResourceBundle resources;
	private ResourceBundle currentBundle = null;
	private String htmlPayLoad = null;
	//private String templates = null;
	private String reportFileName = null;
	private String header = null;
	private String find = null;
	
	
	static
	{
		try
		{
			resources = ResourceBundle.getBundle("common.sTestReportFactory",Locale.getDefault());
		} catch (MissingResourceException mre) {
			System.out.println("sTestReportFactory.properties not found: "+mre);
			System.exit(0);
		}
	}
	public sTestReportFactoryListner(){
		currentBundle = resources;
		System.out.println("Creating Local Report Factory Listner");
		
	}
	public sTestReportFactoryListner(ResourceBundle myResources){
		currentBundle = myResources;
		System.out.println("Creating Thru Bichromate Report Factory Listner");
		
	}
	public String getLastReportFileName(){
		return reportFileName;
	}
	public void writeOutNewReport(String newFileName,boolean needData){
		
		reportFileName = new String(newFileName);
		try{
			if(needData)getTestNGResult();
			File out = new File("test-output/"+newFileName);
			FileUtils.writeStringToFile(out, htmlPayLoad);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException io){
			io.printStackTrace();
		}
	}
	public void  updateAllColumnNames(String[] params){
		//
		// Here is where we will change the parameters to correct data provider names
		//
		int paramNum = 1;
		for(int cnt =0; cnt < params.length;cnt++){
			String find = new String("Parameter #"+paramNum+"<");
			String replace = new String(params[cnt]+"<");
			htmlPayLoad = new String(htmlPayLoad.replaceAll(find, replace));
			paramNum++;
			// <th>Parameter #1</th>
		}
	}
	public void insertHeaderTemplate(String templateName){
	
		header  = new String(currentBundle.getString(templateName));
		find = new String(currentBundle.getString(templateName+"Replace"));
		htmlPayLoad = new String(htmlPayLoad.replaceAll(find, header));
	}
	
	public void getTestNGResult() throws IOException {
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
		} finally { 
			stream.close(); 
		}
	}// getTestNGResult
	@Override
	public void generateSuiteSummaryReport(List<ISuite>suites){
		super.generateSuiteSummaryReport(suites);
		System.out.println("sTestReportFactory was called to overwrite generateSuiteSummaryReport");
	}
	@Override
	public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir){
		super.generateReport(xml, suites, outdir);
		System.out.println("sTestReportFactory was called to overwrite generateReport");
		
		String data = new String(xml.toString());
		String suiteName = null;
		String className = null;
		String[] splits = data.split(":");
 		System.out.println("splits.size: " + splits.length);
 		if(splits.length < 10){
 			suiteName = new String ("unknown");
 			className = new String("unKnown");
 		}else{
 			suiteName = new String (splits[1]);
 			className = new String(splits[8]);
 			String[] classSplit = className.split("=");
 			className = new String(classSplit[1]);
 			String [] classSplit2 = className.split("\\]");
 			className = new String(classSplit2[0]);
 		}
		
		  String[] paramField = null;
		  paramField = new String[13];
		  paramField[0] = new String("testCount");
		  paramField[1] = new String("runTest");
		  paramField[2] = new String("Browser");
		  paramField[3] = new String("Platform");
		  paramField[4] = new String("Version");
		  paramField[5] = new String("Remote Test");
		  paramField[6] = new String("oDeskURL");
		  paramField[7] = new String("oboURL");
		  paramField[8] = new String("mail_guiURL");
		  paramField[9] = new String("Description");
		  paramField[10] = new String("xlFile");
		  paramField[11] = new String("xlSheet");
		  paramField[12] = new String("xlTable");
		  
		  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		  Date date = new Date();
		
		  try{
	 		getTestNGResult();
	 		updateAllColumnNames(paramField);
	 		insertHeaderTemplate("sTestReportFactory.headingOneTemplate");
	 		writeOutNewReport(suiteName+dateFormat.format(date)+".html",false);
	 		sTestEmailFactory ef = new sTestEmailFactory();
	 		ef.configureMail("davidwramertesting@gmail.com", "davidwramertesting@gmail.com", className + " Report Generated By ReportFactory", dateFormat.format(date));
	 		
	 		
	 		//
	 		// 8/13/2013  in future all tests should zip the screen shots and send in mail.
	 		// Zip file support has been added to oTestWebDriverFactory
	 		//
	 		//oTestZipFactory myZip = new oTestZipFactory();
	 		//String zipFile = new String(myZip.findZipFileToEmail());
	 		
	 		//if(zipFile.contains("empty")){
	 			ef.sendNewTestResult(getLastReportFileName(),"images/bichromate.JPG");
	 		//}else{
	 			// ef.sendNewTestResult(getLastReportFileName(),"test-output/oDesk.JPG","test-output/zipped/"+zipFile);
	 		//}
		  }catch(IOException e){
	 			e.printStackTrace();
	 	}
		
	}
	
	
	
	 //
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public static void main(final String[] args){
	 		
	 		// Matcher matcher = null;
	 		String data = new String("[[Suite: ReleaseNight [Test=default Test classes:[ XmlClass=com.bichromate.test.match] ");
	 
	 		try{
		 		String[] splits = data.split(":");
		 		System.out.println("splits.size: " + splits.length);
		 		String suiteName = new String (splits[1]);
		 		String className = new String(splits[2]);
		 		String[] classSplit = className.split("=");
		 		className = new String(classSplit[1]);
		 		String [] classSplit2 = className.split("\\]");
		 		className = new String(classSplit2[0]);
		 		String[] suiteSplit = suiteName.split("\\[");
		 		suiteName = new String(suiteSplit[0]);
		 		for(String asset: splits){
		 			System.out.println(asset);
		 		}
	 			
	 		}catch(PatternSyntaxException e){
	 			System.out.println("Failed to split string");
	 		}
	 		
	 		
	 		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	 		Date date = new Date();
	 		
	 		String[] paramField = null;
	 		  paramField = new String[13];
	 		  paramField[0] = new String("testCount");
	 		  paramField[1] = new String("runTest");
	 		  paramField[2] = new String("Browser");
	 		  paramField[3] = new String("Platform");
	 		  paramField[4] = new String("Version");
	 		  paramField[5] = new String("Remote Test");
	 		  paramField[6] = new String("oDeskURL");
	 		  paramField[7] = new String("oboURL");
	 		  paramField[8] = new String("mail_guiURL");
	 		  paramField[9] = new String("Description");
	 		  paramField[10] = new String("xlFile");
	 		  paramField[11] = new String("xlSheet");
	 		  paramField[12] = new String("xlTable");
	 		
	 		 
	 		 sTestReportFactoryListner rf = new sTestReportFactoryListner();
	 		try{
	 		rf.getTestNGResult();
	 		rf.updateAllColumnNames(paramField);
	 		rf.insertHeaderTemplate("sTestReportFactory.headingOneTemplate");
	 		rf.writeOutNewReport("sTestReportFactoryTest"+dateFormat.format(date)+".html",false);
	 		}catch(IOException e){
	 			e.printStackTrace();
	 		}
	 	} // main
	 }// Test

}//sTestReportFactoryListner
