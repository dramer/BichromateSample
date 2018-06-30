/*
 * sTestTestDataStorage.java	1.0 2018/05/11
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
 */
package bichromate.dataStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import bichromate.core.sTestOSInformationFactory;


public class sTestTestDataStorage implements Serializable{
	
	static private HashMap<String,Object> map1 = null;
	
	
	/**
	 * This class Demonstrates sTestTestDataStorage().
	 * <br>This class allows tests to store test objects and retrieve them in a later test
	 * <br>
	 * @author davidwramer
	 * @version 1.0 
	 */
	public sTestTestDataStorage(){
		
		map1 = new HashMap<String, Object>();
		
	}// sTestNewRelicFactory
	/**
	 * This class Demonstrates  storeIfAbsentTestData
	 * <br>This function stores a name object pair if the key is not already stored
	 * <br>
	 * @param key - key of the object to retrieve.
	 * @param value - Object to store
	
	 * @author davidwramer
	 * @version 1.0 
	 */
	public void storeIfAbsentTestData(String key, Object value){
		
		if(null != value){
			map1.putIfAbsent(key, value);
		}else{
			System.out.println("sTestTestDataStorage:storeIfAbsentTestData Object was null and was not stored");
		}
		
	}//storeTestData
	/**
	 * This class Demonstrates retreiveTestData
	 * <br>This function retrieves a name object pair if the key is not already stored
	 * <br>
	 * @param key - key of the object to retrieve.
	 * @return data - retrieve the Object from the key passed in
	 * @author davidwramer
	 * @version 1.0 
	 */
	public Object retreiveTestData(String key){
		Object data = null;
		if(map1.containsKey(key)){
			data = map1.get(key);
		}else{
			System.out.println("sTestTestDataStorage:storeIfAbsentTestData Key was not found in the hash");
		}
		return data;
	}//storeTestData
	/**
	 * This class Demonstrates saveHashToFile
	 * <br>This function saves the hash map to file
	 * <br>
	 * @throws IOException - failed to open file to save
	 * @author davidwramer
	 * @version 1.0 
	 */
	public void saveHashToFile() throws IOException{
		sTestOSInformationFactory path = new sTestOSInformationFactory();
		
		FileOutputStream fileOutputStream = new FileOutputStream(path.getDataDirectory()+"hash.txt");
		ObjectOutputStream objectOutputStream= new ObjectOutputStream(fileOutputStream);

		objectOutputStream.writeObject(map1);
		objectOutputStream.close();
		
	}//saveHashToFile
	/**
	 * This class Demonstrates restoreHashFromFile
	 * <br>This function retrieves the hash from file
	 * <br>
	 * @author davidwramer
	 * @version 1.0 
	 * @throws ClassNotFoundException - failed to create Object input stream
	 * @throws IOException - failed to open hash.txt
	 */
	@SuppressWarnings("unchecked")
	public void restoreHashFromFile() throws IOException, ClassNotFoundException{
		
		sTestOSInformationFactory path = new sTestOSInformationFactory();
		
		FileInputStream fileInputStream  = new FileInputStream(path.getDataDirectory()+"hash.txt");
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

		map1 = (HashMap<String, Object>) objectInputStream.readObject();
		objectInputStream.close();
		//
		// Never keep the hash around
		//
		deleteTheHashFile();
		
		
	}//restoreHashFromFile
	/**
	 * This class Demonstrates clearTheHash
	 * <br>This function clears all the elements in the hash
	 * <br>
	 * @author davidwramer
	 * @version 1.0 
	 */
	public void clearTheHash(){
		map1.clear();
	}
	/**
	 * This class Demonstrates deleteTheHashFile
	 * <br>This function deletes the hash file
	 * <br>
	 * @author davidwramer
	 * @version 1.0 
	 */
	private void deleteTheHashFile(){
		
		sTestOSInformationFactory path = new sTestOSInformationFactory();
		
		try{
    		
    		File file = new File(path.getDataDirectory()+"hash.txt");
        	
    		if(file.delete()){
    			System.out.println("sTestTestDataStorage:deleteTheHash " + file.getName() + " is deleted!");
    		}else{
    			System.out.println("sTestTestDataStorage:deleteTheHash " +"Delete operation is failed.");
    		}
    	   
    	}catch(Exception e){
    		System.out.println("sTestTestDataStorage:deleteTheHash some I/O error");
    		e.printStackTrace();
    		
    	}
	}// deleteTheHash
	
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public static void main(final String[] args){
	 		
	 		sTestGitLogData git = new sTestGitLogData();
	 		sTestGitLogData git1 = new sTestGitLogData();
	 		git.repoName = new String("Test Repo Name");
	 		git1.jiraID = new String("JiraID-1");
	 		
	 		sTestTestDataStorage testData = new sTestTestDataStorage();
	 		if(null != testData){
	 			testData.storeIfAbsentTestData("testData", "Testing");
	 			System.out.println("Stored: key = TestData, Object = \"Testing\"");
	 			testData.storeIfAbsentTestData("testNumber", 35);
	 			System.out.println("Stored: key = TestNumber, Object =  35");
	 			testData.storeIfAbsentTestData("git", git);
	 			System.out.println("Stored: key = git, Object =  sTestTestDataStorage");
	 			testData.storeIfAbsentTestData("git1", git1);
	 			System.out.println("Stored: key = git1, Object =  sTestTestDataStorage");
	 			//
	 			// Save the hash to file
	 			//
	 			try {
					testData.saveHashToFile();
					System.out.println("Saved the hash to file");
				} catch (IOException e) {
					System.out.println("failed to save the hash to file");
					e.printStackTrace();
				}
	 			//
	 			// Delete all hash items
	 			//
	 			testData.clearTheHash();
	 			try {
					testData.restoreHashFromFile();
					//
		 			// Did the restore work?
		 			//
		 			System.out.println("Retreived: key = TestData, "+ (String)testData.retreiveTestData("testData"));
		 			System.out.println("Retreived: key = TestNumber, "+ (Integer)testData.retreiveTestData("testNumber"));
		 			sTestGitLogData gitReturn = (sTestGitLogData)testData.retreiveTestData("git");
		 			if(null != gitReturn)
		 				System.out.println("Retreived: key = git, "+ gitReturn.repoName);
		 			else
		 				System.out.println("sTestTestDataStorage:main failed to retieve the sTestGitLogData");
		 			
		 			
		 			sTestGitLogData gitReturn1 = (sTestGitLogData)testData.retreiveTestData("git1");
		 			if(null != gitReturn1)
		 				System.out.println("Retreived: key = git1, "+ gitReturn1.jiraID);
		 			else
		 				System.out.println("sTestTestDataStorage:main failed to retieve the sTestGitLogData1");
		 			
		 			System.out.println("sTestTestDataStorage test stored, string, integer, class object, and class object");
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 			
	 		}
	 		
	 	}//main
	 }//Test

}
