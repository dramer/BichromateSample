package sample;

import com.relevantcodes.extentreports.ExtentReports;

public class extendManager {
	
    private static ExtentReports extent;
    
    public synchronized static ExtentReports getReporter(String filePath) {
        if (extent == null) {
            extent = new ExtentReports(filePath, true);
            
            extent
                .addSystemInfo("Host Name", "BillFire QA")
                .addSystemInfo("Environment", "QA");
        }
        
        return extent;
    }
}