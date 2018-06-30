/*
 * sTestPOMFactory.java	1.0 2016/08/01
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


package bichromate.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import bichromate.core.sTestOSInformationFactory;

@SuppressWarnings("unused")
public class sTestPOMFactory {
	
	
	private final String BUTTON = "Button";
	private final String DROPDOWN = "DropDown";
	private final String TEXTFIELD = "TextField";
	private final String LINK = "Link";
	private final String ElementsStart = "# sTestPOMFactory Elements Start";
	
	private String pomFilePackage = null;
	private String templatesDirectory = null;
	private String propertiesFileDirectory = null;
	private String clickElementTemplate = null;
	private String constructorTemplate = null;
    private String copyrightTemplate = null;
	private String enterTextTemplate = null;
	private String importsTemplate = null;
	private String isDisplayedTemplate = null;
	private String isEnabledTemplate = null;
	private String propertiesFileExtension = null;
	private String pomFileDirectory = null;
	private String pomFileVersion = null;
	private String propertiesFileLocation = null;
	private PrintWriter writer = null;
	private List <String> elementList = null;
	
	
	
	private sTestOSInformationFactory path = null;
	
	
	 private static ResourceBundle resources;
	
	
	
	 static
	 {
			try
			{
				resources = ResourceBundle.getBundle("tools.sTestPOMFactory",Locale.getDefault());
			} catch (MissingResourceException mre) {
				System.out.println("sTestPOMFactory.properties not found: "+mre);
				System.exit(0);
			}
	 }
	
	 /**
	 * This method demonstrates sTestPOMFactory().
	 * <br>  Page Object Model  generator. Reads in a properties file and generates the POM.java file 
	 * <br>  
	 * @param   outsideResources - Bichromate resource bundle
	 */
	public sTestPOMFactory(ResourceBundle outsideResources){
	
		setup(outsideResources);
	}
	
	/**
	 * This method demonstrates sTestPOMFactory().
	 * <br>  Page Object Model  generator. Reads in a properties file and generates the POM.java file 
	 * <br>    
	 */
	public sTestPOMFactory(){
		
		setup(resources);
	}//sTestPOMFactory
	
	private void setup(ResourceBundle myResources){
		//
		// List of elements to process
		//
		elementList =  new ArrayList<String>();
		
		path = new sTestOSInformationFactory();
		//
		// properties file location
		//
		propertiesFileLocation = new String(myResources.getString("sTestPOMFactory.propertiesFileLocation"));
		//
		// package name
		//
		pomFilePackage = new String(myResources.getString("sTestPOMFactory.pomFilePackage"));
		//
		// Directory where all templates are stored
		//
		templatesDirectory = new String(myResources.getString("sTestPOMFactory.templatesDirectory"));
		//
		// properties file directory
		//
		propertiesFileDirectory = new String(myResources.getString("sTestPOMFactory.propertiesFileDirectory"));
		//
		//file extension for properties files
		//
		propertiesFileExtension  = new String(myResources.getString("sTestPOMFactory.propertiesFileExtension"));
		//
		// version of files we are creating
		//
		pomFileVersion =  new String(myResources.getString("sTestPOMFactory.pomFileVersion"));
		//
		// POM file directory
		//
		pomFileDirectory = new String(resources.getString("sTestPOMFactory.pomFileDirectory"));
		//
		// Templates
		//
		clickElementTemplate = new String(myResources.getString("sTestPOMFactory.clickElementTemplate"));
		constructorTemplate = new String(myResources.getString("sTestPOMFactory.constructorTemplate"));
	    copyrightTemplate = new String(myResources.getString("sTestPOMFactory.copyrightTemplate"));
		enterTextTemplate = new String(myResources.getString("sTestPOMFactory.enterTextTemplate"));
		importsTemplate = new String(myResources.getString("sTestPOMFactory.importsTemplate"));
		isDisplayedTemplate = new String(myResources.getString("sTestPOMFactory.isDisplayedTemplate"));
		isEnabledTemplate = new String(myResources.getString("sTestPOMFactory.isEnabledTemplate"));
	}// setup
	
	/**
	 * This method demonstrates createPOM().
	 * <br>  Page Object Model. Details in the pomCreator.properties file 
	 * <br>
	 * @param propertiesFileName    name of the properties file to create the POM File
	 */
	public void createPOM(String propertiesFileName){
		//
		// Ensure the properties file exists, we need to read all the page elements
		//
		if(!readInPropertiesFile(propertiesFileName)){
			System.err.println("Error reading properties file: "+propertiesFileName );
			return;
		}
		
		
		
		String newPOMFile = new String(path.buildWorkingDirectoryPath(path.setFilePath(pomFileDirectory))+path.fileSeperator()+propertiesFileName+".java");
		//
		// Check to see if the file already exists
		//
		File f = new File(newPOMFile);
		if(f.exists()){
			System.out.println("POM file already exisits: "+newPOMFile );
			return;
		}
		PrintWriter pomFile = null;
		try {
			pomFile = new PrintWriter(newPOMFile, "UTF-8");
			//
			// write out copyright
			//
			flushCopyright(pomFile,propertiesFileName+".java");
			//
			// write out the IMPORTS
			//
			flushImports(pomFile);
			//
			// flush out constructor
			//
			flushConstructor(pomFile,propertiesFileName);
			//
			// Create all the functions
			//
			flushMethods(pomFile, propertiesFileName);
					
			//
			// Close the class
			//
			pomFile.write("}// "+propertiesFileName+".java Created By Bichromate, "+getCurrentDate());
			pomFile.write("\r\n");
			//
			// Close the newly created POM File
			//
			pomFile.flush();
			pomFile.close();
		} catch (FileNotFoundException file) {
			System.out.println("FileNotFoundException: "+file );
		} catch (IOException io) {
			System.out.println("IOException: "+io );
		} finally{
			System.out.println("new POM file created");
		}
	}// createPOM
	private boolean readInPropertiesFile(String propertiesFileName){
		String proFileToprocess = new String(path.buildWorkingDirectoryPath(path.setFilePath(propertiesFileDirectory))+path.fileSeperator()+propertiesFileName+propertiesFileExtension);
		BufferedReader br = null;
		PrintWriter pomFile = null;
		try {
			br = new BufferedReader(new FileReader(proFileToprocess ));
			String line;
			boolean startOfElements = false;
			while ((line = br.readLine()) != null) {
				System.out.println("properties File Data: "+line );
				//
				// Save only elements we know how to process
				//
				if(startOfElements){
					
					if(line.contains("Field") && !line.contains("Type")){
						int endOfElement = line.indexOf("=");
						if(endOfElement > 1)
							elementList.add(line.substring(0, endOfElement));
					}else if(line.contains("Button")&& !line.contains("Type")){
						int endOfElement = line.indexOf("=");
						if(endOfElement > 1)
							elementList.add(line.substring(0, endOfElement));
					}else if(line.contains("Link")&& !line.contains("Type")){
						int endOfElement = line.indexOf("=");
						if(endOfElement > 1)
							elementList.add(line.substring(0, endOfElement));
					}
				}
				if(line.contains(ElementsStart)){
					startOfElements = true;
				}
			}
			
			
			br.close();
		} catch (FileNotFoundException file) {
			System.out.println("Properties FileNotFoundException: "+file );
			
			return false;
		} catch (IOException io) {
			System.out.println("Properties File IOException: "+io );
			
			return false;
		} finally{
			System.out.println("Properties file read");
		}
		
		return true;
	}
	/**
	 * This method demonstrates flushEnterTextMethod().
	 * <br>  Writes out the enter Text element Method 
	 * <br>
	 * @param pomFile  File to write all imports. Imports are taken from the imports template 
	 * @param propertiesFileName  name of the POM being created
	 * @param elementName  name of the element to check is Enabled  
	 * @throws FileNotFoundException - POM file not found
	 * @throws  IOException - POM File could not be opened 
	 */
	private void flushEnterTextMethod(PrintWriter pomFile, String propertiesFileName,String elementName) throws FileNotFoundException,IOException{
		
		//
		// Open copyright template and flush to new POM file
		//
		String enabledTmpl = new String(path.buildWorkingDirectoryPath(path.setFilePath(templatesDirectory))+path.fileSeperator()+enterTextTemplate);
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(enabledTmpl ));
		String line = null;
		while ((line = br.readLine()) != null) {
			String newLine = new String(line);
			if(newLine.contains("<-FUNCTION NAME->")){
				StringBuilder sb = new StringBuilder(elementName); 
				char c = sb.charAt(0);
				if (Character.isLowerCase(c))
					sb.setCharAt(0, Character.toUpperCase(c));
				newLine = new String(newLine.replace("<-FUNCTION NAME->", "click"+sb.toString()));
			}
			if(newLine.contains("<-ELEMENT NAME->")){
				newLine = new String(newLine.replace("<-ELEMENT NAME->", elementName));
			}
			if(newLine.contains("<-CLASS NAME->")){
				newLine = new String(newLine.replace("<-CLASS NAME->", propertiesFileName));
			}
			
			pomFile.write(newLine);
			pomFile.write("\r\n");
			
		}
		br.close();
		pomFile.write("");
		pomFile.write("\r\n");
		pomFile.write("");
		pomFile.write("\r\n");
		
	}//flushEnterTextMethod
	/**
	 * This method demonstrates flushClickMethod().
	 * <br>  Writes out the Click element Method 
	 * <br>
	 * @param pomFile  File to write all imports. Imports are taken from the imports template 
	 * @param propertiesFileName  name of the POM being created
	 * @param elementName  name of the element to check is Enabled   
	 * @throws FileNotFoundException - POM file not found
	 * @throws  IOException - POM File could not be opened 
	 * @author davidwramer
	 * @version 1.0
	 */
	private void flushClickMethod(PrintWriter pomFile, String propertiesFileName,String elementName) throws FileNotFoundException,IOException{
		
		// Open copyright template and flush to new POM file
		//
		String enabledTmpl = new String(path.buildWorkingDirectoryPath(path.setFilePath(templatesDirectory))+path.fileSeperator()+clickElementTemplate);
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(enabledTmpl ));
		String line = null;
		while ((line = br.readLine()) != null) {
			String newLine = new String(line);
			if(newLine.contains("<-FUNCTION NAME->")){
				StringBuilder sb = new StringBuilder(elementName); 
				char c = sb.charAt(0);
				if (Character.isLowerCase(c))
					sb.setCharAt(0, Character.toUpperCase(c));
				newLine = new String(newLine.replace("<-FUNCTION NAME->", "click"+sb.toString()));
			}
			if(newLine.contains("<-ELEMENT NAME->")){
				newLine = new String(newLine.replace("<-ELEMENT NAME->", elementName));
			}
			if(newLine.contains("<-CLASS NAME->")){
				newLine = new String(newLine.replace("<-CLASS NAME->", propertiesFileName));
			}
			
			pomFile.write(newLine);
			pomFile.write("\r\n");
			
		}
		br.close();
		pomFile.write("");
		pomFile.write("\r\n");
		pomFile.write("");
		pomFile.write("\r\n");
		
	}//flushIsDisplayedMethod
	/**
	 * This method demonstrates flushIsDisplayedMethod().
	 * <br>  Writes out the isDisplayed Method 
	 * <br>
	 * @param pomFile  File to write all imports. Imports are taken from the imports template 
	 * @param propertiesFileName  name of the POM being created
	 * @param elementName  name of the element to check is Enabled  
	 * @throws FileNotFoundException - POM file not found
	 * @throws  IOException - POM File could not be opened  
	 * @author davidwramer
	 * @version 1.0
	 */
	private void flushIsDisplayedMethod(PrintWriter pomFile, String propertiesFileName,String elementName) throws FileNotFoundException,IOException{
		
		//
		// Open copyright template and flush to new POM file
		//
		String enabledTmpl = new String(path.buildWorkingDirectoryPath(path.setFilePath(templatesDirectory))+path.fileSeperator()+isDisplayedTemplate);
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(enabledTmpl ));
		String line = null;
		while ((line = br.readLine()) != null) {
			String newLine = new String(line);
			
			if(newLine.contains("<-FUNCTION NAME->")){
				StringBuilder sb = new StringBuilder(elementName); 
				char c = sb.charAt(0);
				if (Character.isLowerCase(c))
					sb.setCharAt(0, Character.toUpperCase(c));
				newLine = new String(newLine.replace("<-FUNCTION NAME->", "is"+sb.toString()+"Displayed"));
			}
			if(newLine.contains("<-ELEMENT NAME->")){
				newLine = new String(newLine.replace("<-ELEMENT NAME->", elementName));
			}
			if(newLine.contains("<-CLASS NAME->")){
				newLine = new String(newLine.replace("<-CLASS NAME->", propertiesFileName));
			}
			
			pomFile.write(newLine);
			pomFile.write("\r\n");
			
		}
		br.close();
		pomFile.write("");
		pomFile.write("\r\n");
		pomFile.write("");
		pomFile.write("\r\n");
		
	}//flushIsDisplayedMethod
	/**
	 * This method demonstrates flushIsEnabledMethod().
	 * <br>  Writes out the isEnabled Method 
	 * <br>
	 * @param pomFile  File to write all imports. Imports are taken from the imports template 
	 * @param propertiesFileName  name of the POM being created
	 * @param elementName  name of the element to check is Enabled  
	 * @throws FileNotFoundException - POM file not found
	 * @throws  IOException - POM File could not be opened  
	 */
	private void flushIsEnabledMethod(PrintWriter pomFile, String propertiesFileName,String elementName) throws FileNotFoundException,IOException{
		
		//
		// Open copyright template and flush to new POM file
		//
		String enabledTmpl = new String(path.buildWorkingDirectoryPath(path.setFilePath(templatesDirectory))+path.fileSeperator()+isEnabledTemplate);
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(enabledTmpl ));
		String line = null;
		while ((line = br.readLine()) != null) {
			String newLine = new String(line);
			
			if(newLine.contains("<-FUNCTION NAME->")){
				StringBuilder sb = new StringBuilder(elementName); 
				char c = sb.charAt(0);
				if (Character.isLowerCase(c))
					sb.setCharAt(0, Character.toUpperCase(c));
				newLine = new String(newLine.replace("<-FUNCTION NAME->", "is"+sb.toString()+"Enabled"));
			}
			if(newLine.contains("<-ELEMENT NAME->")){
				newLine = new String(newLine.replace("<-ELEMENT NAME->", elementName));
			}
			if(newLine.contains("<-CLASS NAME->")){
				newLine = new String(newLine.replace("<-CLASS NAME->", propertiesFileName));
			}
			
			pomFile.write(newLine);
			pomFile.write("\r\n");
			
		}
		br.close();
		pomFile.write("");
		pomFile.write("\r\n");
		pomFile.write("");
		pomFile.write("\r\n");
		
	}//flushIsEnabledMethod
	
	/**
	 * This method demonstrates flushMethods().
	 * <br>  Write all the element functions 
	 * <br>
	 * @param pomFile  File to write all imports. Imports are taken from the imports template 
	 * @param propertiesFileName  name of the POM being created  
	 * @throws FileNotFoundException - POM file not found
	 * @throws  IOException - POM File could not be opened  
	 */
	private void flushMethods(PrintWriter pomFile, String propertiesFileName) throws FileNotFoundException,IOException{
		
		for(int x = 0; x < elementList.size(); x++){
			String elementName = new String(elementList.get(x));
			flushIsDisplayedMethod(pomFile,propertiesFileName, elementName);
			pomFile.write("");
			pomFile.write("\r\n");
			pomFile.write("");
			pomFile.write("\r\n");
			flushIsEnabledMethod(pomFile,propertiesFileName, elementName);
			pomFile.write("");
			pomFile.write("\r\n");
			pomFile.write("");
			pomFile.write("\r\n");
			if(elementName.contains(TEXTFIELD)){
				flushEnterTextMethod(pomFile,propertiesFileName, elementName);
				pomFile.write("");
				pomFile.write("\r\n");
				pomFile.write("");
				pomFile.write("\r\n");
			}
			if(elementName.contains(BUTTON)){
				flushClickMethod(pomFile,propertiesFileName, elementName);
				pomFile.write("");
				pomFile.write("\r\n");
				pomFile.write("");
				pomFile.write("\r\n");
			}
			
		}
		pomFile.write("");
		pomFile.write("\r\n");
		pomFile.write("");
		pomFile.write("\r\n");
		
		
	}// flushMethods
	/**
	 * This method demonstrates flushConstructor().
	 * <br>  Write to the new POM File 
	 * <br>
	 * @param pomFile  File to write all imports. Imports are taken from the imports template 
	 * @param propertiesFileName - name of the pom properties file
	 * @throws FileNotFoundException - POM file not found
	 * @throws  IOException - POM File could not be opened    
	 */
	private void flushConstructor(PrintWriter pomFile, String propertiesFileName) throws FileNotFoundException,IOException{
		
		//
		// Open copyright template and flush to new POM file
		//
		String constructorFile = new String(path.buildWorkingDirectoryPath(path.setFilePath(templatesDirectory))+path.fileSeperator()+constructorTemplate);
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(constructorFile ));
		String line = null;
		while ((line = br.readLine()) != null) {
			String newLine = new String(line);
			
			if(newLine.contains("<-FILE NAME->")){
				newLine = new String(newLine.replace("<-FILE NAME->", propertiesFileName));
			}
			if(newLine.contains("<PAGE DECLARATION DIRECTORY->")){
				newLine = new String(newLine.replace("<PAGE DECLARATION DIRECTORY->", propertiesFileLocation));
			}
			if(newLine.contains("<-ELEMENTS->")){
				for(int x = 0; x < elementList.size(); x++){
					String elementName = new String(elementList.get(x));
					System.out.println("element name to process: "+ elementName);
					pomFile.write ("private String "+elementName+" = null;");
					pomFile.write("\r\n");
				}
				pomFile.write("");
				pomFile.write("\r\n");
				pomFile.write("");
				pomFile.write("\r\n");
				//
				// lines have been written
				//
				newLine = null;
			}
			if(null != newLine){
				pomFile.write(newLine);
				pomFile.write("\r\n");
			}
		}
		
		pomFile.write ("");
		pomFile.write("\r\n");
		pomFile.write ("");
		pomFile.write("\r\n");
		br.close();
	}
	/**
	 * This method demonstrates flushImports().
	 * <br>  Write all imports to the new POM File 
	 * <br>
	 * @param pomFile  File to write all imports. Imports are taken from the imports template  
	 * @throws FileNotFoundException - POM file not found
	 * @throws  IOException - POM File could not be opened   
	 */
	private void flushImports(PrintWriter pomFile) throws FileNotFoundException,IOException{
		
		//
		// Open copyright template and flush to new POM file
		//
		String importsFile = new String(path.buildWorkingDirectoryPath(path.setFilePath(templatesDirectory))+path.fileSeperator()+importsTemplate);
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(importsFile ));
		String line = null;
		while ((line = br.readLine()) != null) {
		
			pomFile.write (line);
			pomFile.write("\r\n");
		}
		pomFile.write ("");
		pomFile.write("\r\n");
		pomFile.write ("");
		pomFile.write("\r\n");
		br.close();
		
	}//flushImports
	/**
	 * This method demonstrates flushCopyright().
	 * <br>  Write the copy right to the new POM File 
	 * <br>
	 * @param pomFile  File to write the copyright info. copyright is taken from the copyright template
	 * @param fileName    name of the mew POM File
	 * @throws FileNotFoundException - POM file not found
	 * @throws  IOException - POM File could not be opened 
	 */
	private void flushCopyright(PrintWriter pomFile,String filename) throws FileNotFoundException,IOException{
		//
		// Open copyright template and flush to new POM file
		//
		String copyright = new String(path.buildWorkingDirectoryPath(path.setFilePath(templatesDirectory))+path.fileSeperator()+copyrightTemplate);
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(copyright ));
		String line = null;
		while ((line = br.readLine()) != null) {
			if(line.contains(" * <-FILENAME->	<-VERSION-> <-DATE->")){
				
				pomFile.write (" * "+filename+ ", Version " +pomFileVersion +", Created "+ getCurrentDate() );
				pomFile.write("\r\n");
				
			}else{
				pomFile.write (line);
				pomFile.write("\r\n");
			}
			
		}
		pomFile.write ("");
		pomFile.write("\r\n");
		pomFile.write ("");
		pomFile.write("\r\n");
		pomFile.write("//TODO  enable the package");
		pomFile.write("\r\n");
		pomFile.write ("// package "+pomFilePackage);
		pomFile.write("\r\n");
		pomFile.write ("");
		pomFile.write("\r\n");
		pomFile.write ("");
		pomFile.write("\r\n");
		br.close();
		
	}//flushCopyright
	
	/**
     * This method Demonstrates getCurrentDate().
     * This function returns the current date in the following format yyyy-MM-dd-HH-mm-ss properties file
     * @return String  date  yyyy-MM-dd-HH-mm-ss
     */
	private String getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date date = new Date();
		String currentDate = new String(dateFormat.format(date));
		return currentDate;
		
	}//getCurrentDate
	
	
	/**
	 * This method demonstrates pomGenerator().
	 * <br>  Page Object Model 
	 * <br>    
	 */
	public void selfTest(){
		/*
		// a jframe here isn't strictly necessary, but it makes the example a little more real
	    JFrame frame = new JFrame("InputDialog Example #1");

	    // prompt the user to enter their name
	    String name = JOptionPane.showInputDialog(frame, "Properties File Name, no extension!!!");
	    */
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		  File file = fileChooser.getSelectedFile();
		  // load from file
		}
		/*
	    if(null != file.){
	    	System.out.println("pom file to create: "+name );
	    	createPOM(name);
	    }else{
	    	System.out.println("user cancelled");
	    }
	    */
	    
	}//selfTest
	/**
	 * This method demonstrates pomGenerator().
	 * <br>  Page Object Model 
	 * <br>
	 * @param propertiesFileName - name of the properties file    
	 */
	public void selfTest(String propertiesFileName){
		String name = new String(propertiesFileName);
	    System.out.println("pom file to create: "+name );
	    createPOM(name);
	   
	}//selfTest
	
	 //
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			
			sTestPOMFactory pom = null;
			
			pom = new sTestPOMFactory();
			if(pom != null){
				
				// pom.selfTest("pomCreator");
				pom.selfTest();
				
			}
			
		} // end Main
	 } // end Inner class Test
	
	
}//sTestPOMFactory
