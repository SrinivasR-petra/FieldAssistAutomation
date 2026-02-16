package TestUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReporterNG {
    static ExtentReports extent;

    public static ExtentReports getReporterObject(){
        String reportPath = System.getProperty("user.dir") + "//reports//index.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(reportPath);
        reporter.config().setDocumentTitle("Test Report");
        reporter.config().setReportName("FA Test Results");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        return extent;
    }
}
