
package bichromate.core;

import com.relevantcodes.extentreports.ExtentReports;

public class stestExtendReportFactory {
	
    private static ExtentReports extent;
    
    public synchronized static ExtentReports getReporter(String filePath) {
        if (extent == null) {
            extent = new ExtentReports(filePath, true);
            
            extent
                .addSystemInfo("Host Name", "Bichromate")
                .addSystemInfo("Environment", "QA");
        }
        
        return extent;
    }
}