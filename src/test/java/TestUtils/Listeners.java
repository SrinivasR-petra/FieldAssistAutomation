package TestUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static TestUtils.FABaseTest.FASession;

public class Listeners implements ITestListener
{
    ExtentTest test;
    ExtentReports extent = ExtentReporterNG.getReporterObject();

    @Override
    public void onTestStart(ITestResult result) {

        test = extent.createTest(result.getMethod().getMethodName()).assignAuthor("Srinivas Reddy");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.fail(result.getThrowable());
        File srcFile = FASession.getScreenshotAs(OutputType.FILE);
        File destFile = new File(System.getProperty("user.dir") + "//reports//" + result.getName() + ".png");

        try {
            FileHandler.copy(srcFile, destFile);
            System.out.println("Screenshot saved: " + destFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onFinish(ITestContext context)
    {
        extent.flush();
    }

}
