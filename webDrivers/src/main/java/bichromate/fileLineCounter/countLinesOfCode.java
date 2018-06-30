/*
 * countLinesOfCode.java	1.0 2017/03/04
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


package bichromate.fileLineCounter;

import java.io.File;
import java.util.List;

import bichromate.core.sTestOSInformationFactory;
import bichromate.tools.sTestZipCodeAPI;

@SuppressWarnings("unused")
public class countLinesOfCode {
	 private List<fileCountData> fileList;
	private sTestOSInformationFactory path = null;
	private fileCountData textData = null;
	private fileCountData javaData = null;
	private fileCountData propertiesData = null;
	private fileCountData cppData = null;
	private fileCountData htmlData = null;
	private fileCountData cssData = null;
	private fileCountData xmlData = null;
	private fileCountData hData = null;
	private fileCountData cData = null;
	private fileCountData unknownData = null;
	private fileCountData logData = null;
	private fileCountData csData = null;
	private fileCountData dllData = null;
	private int totalFiles;
	
	
	 /**
	  * This class Demonstrates countLinesOfCode().
	  * <br> The constructor sets up the countLinesOfCode
	  * <br>
	  */ 
	 public countLinesOfCode(){
		 
		
		 
		 path = new sTestOSInformationFactory();
		 textData = new fileCountData();
		 javaData = new fileCountData();
		 propertiesData = new fileCountData();
		 cppData = new fileCountData();
		 htmlData = new fileCountData();
		 cssData = new fileCountData();
		 xmlData = new fileCountData();
		 hData = new fileCountData();
		 cData = new fileCountData();
		 unknownData = new fileCountData();
		 logData = new fileCountData();
		 csData = new fileCountData();
		 dllData = new fileCountData();
		 totalFiles = 0;
	
	
	 }//countLinesOfCode
	 /**
	  * This class Demonstrates countLinesOfCode().
	  * <br> The constructor sets up the countLinesOfCode
	  * <br>
	  */ 
	 private void countTheLinesOfCode(fileCountData fcd){
		 
		
		 //
		 // sort the type of file
		 //
		 if(fcd.fileName.endsWith(".txt")){
			 
		 }else if(fcd.fileName.endsWith(".java")){
			 javaData.count++;
			 
		 }else if(fcd.fileName.endsWith(".properties")){
			 propertiesData.count++;
		 }else if(fcd.fileName.endsWith(".prop")){
			 propertiesData.count++;
			 
		 }else if(fcd.fileName.endsWith(".cpp")){
			 
		 } else if(fcd.fileName.endsWith(".html")){
			 htmlData.count++;
			 
		 } else if(fcd.fileName.endsWith(".cs")){
			 csData.count++;
		 } else if(fcd.fileName.endsWith(".dll")){
			 dllData.count++;
			 
		 } else if(fcd.fileName.endsWith(".css")){
			 
			 cssData.count++;
			 
		 }else if(fcd.fileName.endsWith(".log")){
			 logData.count++;
			 
		 } else if(fcd.fileName.endsWith(".xml")){
			 
			 xmlData.count++;
			 
		 }else if(fcd.fileName.endsWith(".h")){
			 
			 hData.count++;
		 } else if(fcd.fileName.endsWith(".c")){
			 cData.count++;
			 
		 }else{
			 // unKnown
			 unknownData.count++;
		 }
		 
		 
	 }//countLinesOfCode
	 private void showResults(){
		 
		 System.out.println("java File Count: "+javaData.count);
		 System.out.println("CS File Count: "+csData.count);
		 System.out.println("unknown File Count: "+unknownData.count);
		 System.out.println(".h File Count: "+hData.count);
		 System.out.println("xml File Count: "+xmlData.count);
		 System.out.println("dll File Count: "+dllData.count);
		 System.out.println(".c File Count: "+cData.count);
		 System.out.println("------------------------------");
		 System.out.println("Total File Count : "+totalFiles);
		 
	 }
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
			String newFile = new String(node.getAbsoluteFile().toString());
			fileCountData fcd = new fileCountData();
			fcd.fileName = new String(newFile); 
			countTheLinesOfCode(fcd);
			// fileList.add(fcd);
			totalFiles++;
			
		}
		//
		// Recurse down into directories
		//
		if(node.isDirectory()){
			String[] subNode = node.list();
			for(String filename : subNode){
				generateFileList(new File(node, filename));
			}
		}
		 
	 
	 }//generateFileList
	/**
	 * This method test() tests all the git repo comments. run this test to ensure countLinesOfCode is working as expected
	 * @param directory - pass in the directory where the comment files are stored
	 */
	public void test(String directory){
		
		 File zipDir = new File(directory);
		 System.out.println("Start to read thru all Files");
		 generateFileList(zipDir);
		 System.out.println("Finished reading thru all Files");
		 showResults();
		 
		
	}// Test
	
	
	 //
		// Inner class for testing on the command line
		//
		public static class Test
		{
			public static void main(final String[] args){
				
				countLinesOfCode lineCount = new countLinesOfCode();
				if(null != lineCount){
					lineCount.test("C:\\Users\\DRamer\\workspace\\lastmile-xpo-framework");
				}
				
				
			} // end Main
		 } // end Inner class Test
	
}//countLinesOfCode
