/*
 * oTestZipFactory.java	1.0 2013/01/23
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


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
/**
 * This class Demonstrates oTestZipFactory().
 * This class factory is used to zip all screen captures after a test has run.
 * @author davidwramer
 * @version 1.0
 */
@SuppressWarnings("all")
public class sTestZipFileFactory {

	 private List<String> fileList;
	 private String zipDirectory = null;
	 private String zipOutPutDirectory = null;
	 private static ResourceBundle resources;
	 private sTestOSInformationFactory path = null;

	 static
	 {
			try
			{
				resources = ResourceBundle.getBundle("common.sTestZipFileFactory",Locale.getDefault());
			} catch (MissingResourceException mre) {
				System.out.println("oTestZipFactory.properties not found: "+mre);
				System.exit(0);
			}
	 }
	 /**
	  * This class Demonstrates sTestZipFileFactory().
	  * <br>This class constructor looks for the resource bundle in the common directory of the resource folder. Should only be used for testing
	  * <br> The constructor sets up the zip directories
	  * <br>
	  */ 
	 public sTestZipFileFactory(){
		 
		 File zipDir = null;
		 File zipOutDir = null;
		 
		 path = new sTestOSInformationFactory();
		 
		 zipDirectory = new String(path.setFilePath(resources.getString("sTestZipFileFactory.zipDirectory")));
		 zipOutPutDirectory  = new String(path.setFilePath(resources.getString("sTestZipFileFactory.zipOutPutDirectory")));
		 fileList = new ArrayList<String>();
		 
		 zipDirectory = new String(path.workingDirectory()+path.fileSeperator()+zipDirectory);
		 zipOutPutDirectory = new String(path.workingDirectory()+path.fileSeperator()+zipOutPutDirectory);
		 
		 //
		 // Check if these are valid directories
		 //
		 zipDir = new File(zipDirectory);
		 zipOutDir = new File(zipOutPutDirectory);
		 
		 if (zipDir.isDirectory()){
			 System.out.println("valid zip directories");
		 } else{
			 if(zipDir.mkdir()){
				 System.out.println("created the input directory");
			 }else{
				 System.out.println("Failed to create the output directory");
				 System.exit(0);
			 }
		 }
		 //
		 // create the output directory if it does not exist
		 //
		 if (zipOutDir.isDirectory()){
				
			 System.out.println("valid zip output directory");
		 } else{
			 if(zipOutDir.mkdir()){
				 System.out.println("created the output directory");
			 }else{
				 System.out.println("Failed to create the output directory");
				 System.exit(0);
			 }
		 }
		 
	 }// oTestZipFactory
	 /**
	  * This class Demonstrates sTestZipFileFactory().
	  * <br>This class constructor looks for the resource bundle in the common directory of the resource folder. Should only be used for testing
	  * <br> The constructor sets up the zip directories
	  * <br>
	  */ 
	 private sTestZipFileFactory(String zipInDir,String ZipOutDir){
		 
		 File zipFile = null;
		 File zipOutFile = null;
		 
		 path = new sTestOSInformationFactory();
		 
		 zipDirectory = new String(path.setFilePath(zipInDir));
		 zipOutPutDirectory  = new String(path.setFilePath(ZipOutDir));
		 fileList = new ArrayList<String>();
		 
		 zipDirectory = new String(path.workingDirectory()+path.fileSeperator()+zipDirectory);
		 zipOutPutDirectory = new String(path.workingDirectory()+path.fileSeperator()+zipOutPutDirectory);
		 
		 //
		 // Check if these are valid directories
		 //
		 zipFile = new File(zipDirectory);
		 zipOutFile = new File(zipOutPutDirectory);
		 
		 if (zipFile.isDirectory()){
			 System.out.println("valid zip directories");
		 } else{
			 if(zipFile.mkdir()){
				 PrintWriter writer;
				try {
					writer = new PrintWriter(zipDirectory + path.fileSeperator()+  "zipFileUnitTest-1.txt", "UTF-8");
				
					writer.println("The first line");
					writer.println("The second line");
					writer.close();
					PrintWriter writer1 = new PrintWriter(zipDirectory + path.fileSeperator()+  "zipFileUnitTest.t-2xt", "UTF-8");
					writer1.println("The first line");
					writer1.println("The second line");
					writer1.close();
					System.out.println("created directory and added 2 files to zip");
				} catch (FileNotFoundException e) {
					System.out.println("failed to create unit test directory with 2 files ");
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					System.out.println("failed to create unit test directory with 2 files ");
					e.printStackTrace();
				}
			 }else{
				 System.out.println("Failed to create the output directory");
				 System.exit(0);
			 }
		 }
		 //
		 // create the output directory if it does not exist
		 //
		 if (zipOutFile.isDirectory()){
				
			 System.out.println("valid zip output directory");
		 } else{
			 if(zipOutFile.mkdir()){
				 System.out.println("created the output directory");
			 }else{
				 System.out.println("Failed to create the output directory");
				 System.exit(0);
			 }
		 }
		 
	 }// oTestZipFactory
	 /**
	  * method Demonstrates sTestZipFileFactory().
	  * This class constructor is passed a resource bundle and is used when using the Bichromate.jar file. The constructor sets up the zip directories
	  * @param remoteResources pass in the main Bichromate.properties file
	  */ 
	 public sTestZipFileFactory(ResourceBundle remoteResources){
		 File zipDir = null;
		 File zipOutDir = null;
		 
		 path = new sTestOSInformationFactory();
		 
		 zipDirectory = new String(path.setFilePath(remoteResources.getString("sTestZipFileFactory.zipDirectory")));
		 zipOutPutDirectory  = new String(path.setFilePath(remoteResources.getString("sTestZipFileFactory.zipOutPutDirectory")));
		 
		 zipDirectory = new String(path.workingDirectory()+path.fileSeperator()+zipDirectory);
		 zipOutPutDirectory = new String(path.workingDirectory()+path.fileSeperator()+zipOutPutDirectory);
		 
		 //
		 // Check if these are valid directories
		 //
		 zipDir = new File(zipDirectory);
		 zipOutDir = new File(zipOutPutDirectory);
		 
		 if (zipDir.isDirectory() && zipDir.exists() &&  zipOutDir.isDirectory() && zipOutDir.exists() ){
			 System.out.println("valid zip directories");
		 } else{
			 System.out.println("invalid zip directories");
		 }
		 
	 }// oTestZipFactory
	 /**
	  * method getZipDirectory().
	  * This class returns a String of the zipDirectory where files will be zipped into
	  * @return zipDirectory String of the zip directory.
	  */ 
	 public String getZipDirectory(){
		 return zipDirectory;
		
	 }
	 /**
	  * method Demonstrates deleteOldZipFiles().
	  * Delete all zip files from the zipOutPutDirectory
	  */ 
	 @SuppressWarnings("unused")
	private void deleteOldZipFiles(){
		 
		 File dir = new File(zipOutPutDirectory);
		 if(dir != null)
			 for(File file: dir.listFiles()) file.delete();
	 }
	 /**
	  * method Demonstrates getOutputDirectory().
	  * This class returns a String of where all the zip files will be stored
	  * @return zipOutPutDirectory to the output directory. Where the zip file will be stored
	  */ 
	 public String getOutputDirectory(){
		 
		 return zipOutPutDirectory;
	 }
	 /**
	  * method This class Demonstrates findZipFileToEmail().
	  * This class returns the name of the zip file to be included in email
	  * @return String to the zip file
	  */ 
	 public String findZipFileToEmail(){
		 
		 
		 File folder = new File(zipOutPutDirectory);
		 File[] listOfFiles = folder.listFiles();

		     for (int i = 0; i < listOfFiles.length; i++) {
		       if (listOfFiles[i].isFile()) {
		         System.out.println("File " + listOfFiles[i].getName());
		         return listOfFiles[i].getName();
		       } else if (listOfFiles[i].isDirectory()) {
		         System.out.println("Directory " + listOfFiles[i].getName());
		       }
		     }
		     return "empty";
	 }
	 /**
	  * method This class Demonstrates zipIt().
	  * This class builds the zip file from the predefined zip directory
	  * @param zipFile String of the zipFile name
	  */ 
	 public void zipIt(String zipFile){
	 
	     byte[] buffer = new byte[1024];
	     FileOutputStream fos = null;
	    ZipOutputStream zos = null;
	    
	    try{
	    	 
	    	fos = new FileOutputStream(zipFile);
	    	zos = new ZipOutputStream(fos);
	 
	    	System.out.println("sTestZipFileFactory:zipIt -  Output to Zip : " + zipFile);
	    	//
	    	// fileList is created by calling  myZip.generateFileList(zipDir);
	    	//
	    	for(String file : this.fileList){
	 
	    		System.out.println("sTestZipFileFactory:zipIt - File Added : " + file);
	    		ZipEntry ze= new ZipEntry(file);
	        	zos.putNextEntry(ze);
	 
	        	FileInputStream in = 
	                       new FileInputStream(zipDirectory + file);
	 
	        	int len;
	        	while ((len = in.read(buffer)) > 0) {
	        		zos.write(buffer, 0, len);
	        	}
	        	System.out.println("sTestZipFileFactory:zipIt - added: "+file);
	        	in.close();
	    	}
	    	System.out.println("sTestZipFileFactory:zipIt - Completed adding Files");
	    }catch(IOException ex){
	       ex.printStackTrace();
	       System.out.println("sTestZipFileFactory:zipIt - Exception in building ZipFile:"+ex);
	      
	    }
	     try {
			if(null != zos) {
				zos.closeEntry();
				zos.close();
			}
	    	if(null != fos) fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			 System.out.println("sTestZipFileFactory:zipIt - Exception in closing ZipFile:"+e);
		}
	    	//remember close it
	    	
	     System.out.println("sTestZipFileFactory:zipIt - Done zipping files");
	     
	 }//zipIt
	 /**
	  * method demonstrates zipFiles().
	  * This method builds a zip file from the director passed in and the zipFile passed in
	  * @param directory  - directory to zip
	  * @param zipFile - zip file name to be created.
	  */ 
	 public void zipFiles(String directory, String zipFile){
		 
		 //
		 // Build a list of all files to zip
		 //
		 //
		 // build a list of all files to be zipped
		 //
		 File zipDir = new File(directory);
		 generateFileList(zipDir);
		 System.out.println("Generated the list of files to zip");
		 
		 zipIt(getOutputDirectory()+path.fileSeperator()+zipFile);
		 
		 System.out.println("Generated the zip file");
		 
	 }//zipFiles
	 /**
	  * method demonstrates zigenerateFileListpIt().
	  * This class builds a list of all the files to zip
	  * @param node file where to look for zipFiles
	  */ 
	 public void generateFileList(File node){
	 
	    //
		// add files only
		// 
		if(node.isFile()){
			String newFile = new String(generateZipEntry(node.getAbsoluteFile().toString()));
			fileList.add(newFile);
		}
		//
		// Recurse down into directories
		//
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename));
			}
		}
	 
	 }//generateFileList
	 /**
	  * method Demonstrates generateZipEntry().
	  * This class Formats the file path for zip
	  * @param file file path
	  * @return String formatted for the file path
	  */ 
	 private String generateZipEntry(String file){
	    	return file.substring(zipDirectory.length(), file.length());
	 }
	 /**
	  * method This class Demonstrates extractFolder().
	  * Extract a zip File into the directory
	  * @param zipFile the zipFile
	  * @throws ZipException  any issue when compacting files
	  * @throws IOException any IO error trying to read files
	  */ 
	 public void extractFolder(String zipFile) throws ZipException, IOException 
	 {
		 
		 System.out.println(zipFile);
	     int BUFFER = 2048;
	     File file = new File(zipFile);


		ZipFile zip = new ZipFile(file);
	     String newPath = zipFile.substring(0, zipFile.length() - 4);

	     new File(newPath).mkdir();
	     Enumeration<?> zipFileEntries = zip.entries();

	     // Process each entry
	     while (zipFileEntries.hasMoreElements())
	     {
	         // grab a zip file entry
	         ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
	         String currentEntry = entry.getName();
	         File destFile = new File(newPath, currentEntry);
	         //destFile = new File(newPath, destFile.getName());
	         File destinationParent = destFile.getParentFile();

	         // create the parent directory structure if needed
	         destinationParent.mkdirs();

	         if (!entry.isDirectory())
	         {
	             BufferedInputStream is = new BufferedInputStream(zip
	             .getInputStream(entry));
	             int currentByte;
	             // establish buffer for writing file
	             byte data[] = new byte[BUFFER];

	             // write the current file to disk
	             FileOutputStream fos = new FileOutputStream(destFile);
	             BufferedOutputStream dest = new BufferedOutputStream(fos,
	             BUFFER);

	             // read and write until last byte is encountered
	             while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
	                 dest.write(data, 0, currentByte);
	             }
	             dest.flush();
	             dest.close();
	             is.close();
	         }

	         if (currentEntry.endsWith(".zip"))
	         {
	             // found a zip file, try to open
	             extractFolder(destFile.getAbsolutePath());
	         }
	     }
	     zip.close();
	 }// extractFolder
	 
	 //
	 // Inner class for testing on the command line
	 //
	 public static class Test
	 {
		 public static void main(String[] args) throws ZipException {
		
			//  sTestZipFileFactory myZip = new sTestZipFileFactory("Bichromate","zipTestOutput");
			 sTestZipFileFactory myZip = new sTestZipFileFactory();
			 
			 sTestOSInformationFactory path = new sTestOSInformationFactory();
			 
			 if(path != null && myZip != null){
				 System.out.println("Create Zip File");
				 //
				 // build a list of all files to be zipped
				 //
				 File zipDir = new File(myZip.getZipDirectory());
				 //
				 // Build a list of all files to zip
				 //
				 myZip.generateFileList(zipDir);
				 System.out.println("Generated the list of files to zip");
				 //
				 // Zip the files from the list
				 //
				 myZip.zipIt(myZip.getOutputDirectory()+path.fileSeperator()+"myzip.zip");
				 System.out.println("Generated the zip file");
				 try {
					 System.out.println("Extract the zip file");
					 myZip.extractFolder(myZip.getOutputDirectory()+path.fileSeperator()+"myzip.zip");
					 System.out.println("unzipped the file");
				 } catch (IOException e) {
					 e.printStackTrace();
					 System.out.println("some failure in Extracting the zip file");
				
				 }
			 }
		 }// main
	}// Test

}//sTestZipFileFactory
