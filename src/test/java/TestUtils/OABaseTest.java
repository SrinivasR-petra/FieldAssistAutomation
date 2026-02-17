package TestUtils;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.example.pageobject.OpsAssist.Producers;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.FAUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static TestUtils.FABaseTest.*;

public class OABaseTest extends FAUtils {
    public static WindowsDriver<WindowsElement> OASession = null;
    private static String winAppDriverUrl, field;
    public static Producers producers;

    @BeforeSuite(alwaysRun = true)
    public static void setUp() throws IOException, URISyntaxException, InterruptedException {

        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//resources//data.properties");
        prop.load(fis);
        String  ipAddress = System.getProperty("ipAddress") != null ? System.getProperty("ipAddress") : prop.getProperty("ipAddress");
        String  port = System.getProperty("port") != null ? System.getProperty("port") : prop.getProperty("port");
        field = System.getProperty("field") != null ? System.getProperty("field") : prop.getProperty("field");

        String winAppDriverLocation = prop.getProperty("winAppDriverLocation");
        winAppDriverUrl= startWinAppDriverApp(winAppDriverLocation, ipAddress, Integer.parseInt(port));
        OALaunch();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        if (OASession != null)
            OASession.close();
        startWinAppDriver.destroy();
    }

    public static void OALaunch() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "\\\\MCBU-Current\\Upgrader\\OpsClient\\PSC.FieldAssist.OpsAssistUpgraderApp.exe");
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("automationName", "windows");
            WindowsDriver<WindowsElement> OA_launchSession = new WindowsDriver<>(new URI(winAppDriverUrl).toURL(), capabilities);
            Thread.sleep(5000);

            OA_launchSession.quit();
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("app", "Root");
            WindowsDriver<WindowsElement> OA_rootSession = new WindowsDriver<>(new URI(winAppDriverUrl).toURL(), cap);
            Assert.assertNotNull(OA_rootSession);
            //OA_rootSession.getKeyboard().pressKey("OpenQA.Selenium.Keys.Command + \"a\" + OpenQA.Selenium.Keys.Command");
            Thread.sleep(2000);
            WindowsElement FieldSel = OA_rootSession.findElementByName("Field Selection");
            FieldSel.isDisplayed();
            FieldSel.findElementByName(field).click();
            Thread.sleep(3000);
            WindowsElement OAWebElement = OA_rootSession.findElementByName("OpsAssist - mcbu-current - Odessa");
            String OAWinHandleStr = OAWebElement.getAttribute("NativeWindowHandle");
            int OAWinHandleInt = Integer.parseInt(OAWinHandleStr);
            String OAWinHandleHex = Integer.toHexString(OAWinHandleInt);

// You attach to the already running application
            DesiredCapabilities OACapabilities = new DesiredCapabilities();
            OACapabilities.setCapability("platformName", "Windows");
// You set the Handle as one of the capabilities
            OACapabilities.setCapability("appTopLevelWindow", OAWinHandleHex);

// My Application Session
            OASession = new WindowsDriver<>(new URI(winAppDriverUrl).toURL(), OACapabilities);

        } catch (Exception e) {
            e.printStackTrace();
        }
        OASession.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        OASession.manage().window().maximize();
        producers = new Producers(OASession);
    }

}
