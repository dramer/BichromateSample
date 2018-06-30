/*
 * sTestGitLogFormatter.java	1.0 2017/07/01
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
package bichromate.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.ObjectId;

import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;

import org.eclipse.jgit.treewalk.TreeWalk;

import bichromate.core.sTestOSInformationFactory;
import bichromate.dataStore.sTestGitFileList;
import bichromate.dataStore.sTestGitLogData;

//
// C:\Users\DRamer\workspace\lastmile-dispatch-office>git log --name-only --since="2017-01-01" >lmDispatchOffice.log
//

public class sTestGitLogInformation {
	
	private sTestOSInformationFactory osInfo = new sTestOSInformationFactory();
	private List<sTestGitLogData> gitJiraFileList = new ArrayList<sTestGitLogData>();
	private List<jiraBugID> jiraBugIDList = new ArrayList<jiraBugID>();
	//private Repository repository = null;
	private String repoDirectory = null;
	
	sTestGitLogInformation(String repo){
		
		repoDirectory = new String(repo);	
		
	}//sTestGitLogInformation
	
	private String jiraIDPatternMatcher(String str){
		
		 // rpt-468:
	      String pattern = "(([a-zA-Z]+)-(\\d+))";

	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(str.trim());
	      if (m.find( )) {
	    	  String jiraID = new String(m.group());
	       return jiraID;
	      }
	      return null;
	}//jiraIDPatternMatcher
	
	private Boolean loadListOfBugs(){
		FileReader in = null;
		try{
			BufferedReader br = new BufferedReader(in = new FileReader(osInfo.getGitLogDirectory()+"currentBugs.txt"));
			String str;
			while ((str = br.readLine())!= null) {
			 
				jiraBugID newID = new jiraBugID();
				newID.id = new String(str.trim());
				jiraBugIDList.add(newID);
			} 
				in.close();
		} catch (IOException e) {
		        System.out.println("jira Bug list File Read Error");
		        return false;
		}
		return true;
	
	}//loadListOfBugs
	private Boolean isABug(String jiraID){
		Boolean isABug = false;
		for(int x = 0; x < jiraBugIDList.size(); x++){
			jiraBugID bug = jiraBugIDList.get(x);
			if(jiraID.contains(bug.id)){
				jiraBugID newBugInfo = new jiraBugID();
				newBugInfo.anyCheckin = true;
			
				newBugInfo.checkinCount = bug.checkinCount +1;
				newBugInfo.id = new String (jiraID);
				jiraBugIDList.remove(x);
				jiraBugIDList.add(newBugInfo);
				return true;
			}
		}
		return isABug;
	}
	private void setupGitRepository(){
		PrintWriter outFile = null;
		
		try {
			Git git;
			
			git = Git.init().setDirectory(new File(repoDirectory)).call();
			
			TreeWalk treeWalk = new TreeWalk( git.getRepository() );
			outFile = new PrintWriter(new FileWriter(osInfo.getGitLogDirectory()+"temp-"+osInfo.getCurrentDateAndUTCTime()+".log", true));
			
			treeWalk.reset();
			
				
			for (RevCommit commit : git.log().call()) {
				outFile.append(commit.getFullMessage());
				System.out.println(commit.getFullMessage());
				RevTree revTree = commit.getTree();
				ObjectId id = revTree.getId();
				if(null != id){
					treeWalk.reset(id);
					while( treeWalk.next() ) {
						String path = treeWalk.getPathString();
						outFile.append(path);
						System.out.println(path);
					}
					treeWalk.close();
				}
			}
				
		} catch (NoHeadException e) {
			System.out.println("Empty repo, nothing to log");
		} catch (IllegalStateException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (GitAPIException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
	
			if(outFile != null){
				outFile.close();
			}
		}
	
	}//setupGitRepository
	

	private void howManyBugs(){
		PrintWriter outFile = null;
		try {
			outFile = new PrintWriter(new FileWriter(osInfo.getGitLogDirectory()+"temp-"+osInfo.getCurrentDateAndUTCTime()+".log", true));
		
			int bugCount = 0;
			String repoName = "none";
			for(int x = 0; x < gitJiraFileList.size();x++){
				sTestGitLogData data = gitJiraFileList.get(x);
				repoName = new String(data.repoName);
				if(data.isBug){
					bugCount++;
					for(int y = 0; y < data.gitFileList.size(); y++){
						sTestGitFileList fl = data.gitFileList.get(y);
						System.out.println(fl.gitFileName+",bug,"+data.jiraID);
						outFile.append(fl.gitFileName+",bug,"+data.jiraID+"\n");
					}
					
				}else{
					for(int y = 0; y < data.gitFileList.size(); y++){
						sTestGitFileList fl = data.gitFileList.get(y);
						System.out.println(fl.gitFileName+",code,"+data.jiraID);
						outFile.append(fl.gitFileName+",code,"+data.jiraID+"\n");
					}
				}
				
			}
			System.out.println("Repo: "+ repoName);
			outFile.append("Repo: "+ repoName+"\n");
			System.out.println("Total checkins: "+gitJiraFileList.size());
			outFile.append("Total checkins: "+gitJiraFileList.size()+"\n");
			System.out.println("Total Bug Checkin is: "+bugCount);
			outFile.append("Total Bug Checkin is: "+bugCount+"\n");
			
			//
			// How many bugs map back to the bug list
			//
			int bugMatchCount = 0;
			for(int x = 0; x < jiraBugIDList.size(); x++){
				jiraBugID bug = jiraBugIDList.get(x);
				if(bug.anyCheckin){
					bugMatchCount++;
				}
			}
			
			System.out.println("Total Bugs: "+ jiraBugIDList.size());
			outFile.append("Total Bugs: "+ jiraBugIDList.size()+"\n");
			System.out.println("Total Bugs with Checkins: "+ bugMatchCount);
			outFile.append("Total Bugs with Checkins: "+ bugMatchCount+"\n");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		
		if(outFile != null){
			outFile.close();
		}
	}
		
	}
	public void formatGitLogFile(String fileName) throws FileNotFoundException{
		
		FileReader in = null;
		
		boolean dateSeen = false;
		boolean commitSeen = false;
		boolean setJiraID = false;
		
		try {
			
			BufferedReader br = new BufferedReader(in = new FileReader(osInfo.getGitLogDirectory()+fileName+".log"));
			
	        String str;
	        sTestGitLogData jiraData = null;
	        while ((str = br.readLine())!= null) {
	        	
	            if(str.length() != 0) {// blank line
	            	
	            	if(str.contains("commit")){ // Start a new row
	            		commitSeen = true;
	            		if(jiraData != null){
	            			jiraData.isBug = isABug(jiraData.jiraID);
	            			gitJiraFileList.add(jiraData);
	            			setJiraID = false;
	            		}
	            	}
	            	
	            	if(dateSeen && !commitSeen){ // Capture everything until commit	
	            		String jiraID = null;
	            		if(!setJiraID && (null != (jiraID = jiraIDPatternMatcher(str)))){
	            			jiraData = new sTestGitLogData();
	            			jiraData.repoName = new String(fileName+".log");
	            			jiraData.jiraID = new String(jiraID);
	            			setJiraID = true;
	            			
	            		}else{// add the files  lib/Configs/FlexAPI_InBound/OrderSchedulingConfig.xml
	            			if(jiraData != null && setJiraID){
	            				String trimmedString = new String(str.trim());
	            				int slashLocation = trimmedString.lastIndexOf("/");
	            				
	            				if(!(slashLocation < 0)){
	            					if(!(trimmedString.contains("/trunk@"))){
	            						String fileOnly = new String(trimmedString.substring(slashLocation+1));
	            						sTestGitFileList fileList = new sTestGitFileList();
	            						fileList.gitFileName = new String(fileOnly);
	            						jiraData.gitFileList.add(fileList);
	            					}
	            				}
	            			}
	            		}
	            	}
	            	
	            	if(str.contains("Date:")){
	            		dateSeen = true;
	            		commitSeen = false;
	            	
	            	}
	            	
	            	
	            }//str length
	        }//while
	       
			in.close();
	    } catch (IOException e) {
	        System.out.println("File Read Error");
	    }
		
	}//formatGitLogFile
	
	public void selfTestGit(){
		setupGitRepository();
		
	}
	public void selfTestGitLog(){
		
		try {
			if(!loadListOfBugs()){ // load all the bugs from the file
				
				System.out.println("Abort selfTestGitLog, failed to load bug list");
				return;
			}
			// formatGitLogFile("lmDispatchOffice");
			// formatGitLogFile("lmXPOFramework");
			// formatGitLogFile("mldb");
			// formatGitLogFile("lmDispatchOffice");
			formatGitLogFile("HLCheckListManagement");
			howManyBugs();
			
		} catch (FileNotFoundException e) {
			System.out.println("sTestGitLogFormatter:formatGitLogFile:  error file not found");
		}
		
	}//selfTest()
	
	private class jiraBugID {
		String id = null;
		Boolean anyCheckin = false;
		int checkinCount = 0;
	}
	
	//
	// Inner class for testing on the command line
	//
	public static class Test
	{
		public static void main(final String[] args){
			
			sTestOSInformationFactory path = new sTestOSInformationFactory();
			sTestGitLogInformation gitLogInfo = null;
			
			gitLogInfo = new sTestGitLogInformation(path.getWorkingDirectory());
			if(gitLogInfo != null){
				// gitLogInfo.selfTestGit();
				gitLogInfo.selfTestGitLog();
				
			}
			
		} // end Main
	 } // end Inner class Test

}// sTestGitLogInformation
