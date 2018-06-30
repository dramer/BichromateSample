package bichromate.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bichromate.dataStore.sTestResourceStore;



public class sTestResourceStoreManager {
	
	public List<sTestResourceStore> resourceList = new ArrayList<sTestResourceStore>();
	private sTestOSInformationFactory osInfo = new sTestOSInformationFactory();
	
	public sTestResourceStoreManager(){
		
		
	}//bichromateHTTPListnerFactory
	
	public void displayAllProperties(){
		sTestResourceStore rowOfData = null;
		for(int x = 0; x < resourceList.size();x++){
			
			rowOfData = resourceList.get(x);
			if(rowOfData.isComment){
				System.out.println(rowOfData.comment);
			}else{
				System.out.println(rowOfData.propertyName+"="+rowOfData.propertyValue);
			}
		}
		
	}// displayAllProperties
	
	public void readPropertiesFile(String fileName) throws IOException{
		sTestResourceStore rowOfData = null;
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(osInfo.getResourceDirectory()+fileName));
			try {
			   // StringBuilder sb = new StringBuilder();
			    String line = br.readLine();
			    
			    while (line != null) {
			    	rowOfData = new sTestResourceStore();
			    	
			        
			    	if(line.length() > 0){  // handle blank lines
			    		String comment = new String(line.substring(0,1));
			    	
				        if(comment.trim().contains("#")){ // comment
				        	rowOfData.comment = new String(line);
				        	rowOfData.isComment = true;
				        	
				        }else{ // properties file entry
				        	try{
				        		rowOfData.propertyName = new String(line.substring(0, line.indexOf("=")));
				        		//int ll = line.length();
				        		//int equalIndex = line.indexOf("=");
				        		if(line.length() > (line.indexOf("="))){ // maybe no property value set
				        			rowOfData.propertyValue = new String(line.substring(line.indexOf("=")+1));
				        		}else{
				        			rowOfData.propertyValue = new String("");
				        		}
				        	}catch(IndexOutOfBoundsException e){
				        	
				        		System.out.println("Error reading properties file");
				        	}
				        	
				        }
			    	}else{
			    		rowOfData.comment = new String("");
			        	rowOfData.isComment = true;
			    	}
			        
			        resourceList.add(rowOfData);
			        line = br.readLine();
			    }
			} finally {
			    br.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//
	// Inner class for testing on the command line
	//
	 public static class Test
	 {
	 	public  static void main(final String[] args){
	 		sTestResourceStoreManager rsm = new sTestResourceStoreManager();
	 		if(null != rsm)
				try {
					rsm.readPropertiesFile("Bichromate.properties");
					rsm.displayAllProperties();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 		
	 		
	 	}//main
	 	
	 }//Test

}// sTestResourceStoreManager
