package bichromate.dataStore;

import java.time.LocalDate;
/**
 * testHistory is a data store used by testExecutionFactory to store execution history from the testExecution log file.
 * 
 * @author davidwramer
 * @version 1.0
 */
public class testHistory {
	
	public String testName;
	public LocalDate firstTestDate;
	public LocalDate lastTestDate;
	public String version;
	public int totalPassed = 0;
	public int totalFailed = 0;
	public int totalSkipped = 0;
	public int totalOther = 0;
	public float percentagePassed = 0;
	

}//testHistory
