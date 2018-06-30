package bichromate.dataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class sTestGitLogData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String repoName = null;
	public String  jiraID = null;
	public Boolean isBug = false;
	public String  gitComment = null;
	public String gitOwner = null;
	public List<sTestGitFileList> gitFileList = new ArrayList<sTestGitFileList>();

}
