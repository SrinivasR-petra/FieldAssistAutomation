package TestUtils;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.example.pageobject.FieldAssist_Client.*;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
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
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class FABaseTest extends FAUtils {
    public static WindowsDriver<WindowsElement> FASession = null;
    public static String winAppDriverUrl;
    public static DownWells downWells;
    public static BeamWells beamWells;
    public static FOP fop;
    public static ESP espWells;
    public static GasLift glWells;
    public static Common common;
    public static PAGL pagl;
    public static NFWells nfwells;

    @BeforeSuite(alwaysRun = true)
    public static void setUp() throws IOException {
        // startWinAppDriverApp() method will start the Win App Driver application
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//resources//data.properties");
        prop.load(fis);
        String ipAddress = prop.getProperty("ipAddress");
        String port = prop.getProperty("port");
        String field = prop.getProperty("field");
        String winAppDriverLocation = prop.getProperty("winAppDriverLocation");
        winAppDriverUrl= startWinAppDriverApp(winAppDriverLocation, ipAddress, Integer.parseInt(port));
        try {
            WindowsDriver<WindowsElement> launchSession = getWindowsElementWindowsDriver();
            launchSession.quit();
            DesiredCapabilities cap = new DesiredCapabilities();
            cap.setCapability("app", "Root");
            WindowsDriver<WindowsElement> rootSession = new WindowsDriver<>(new URI(winAppDriverUrl).toURL(), cap);
            Assert.assertNotNull(rootSession);
            //rootSession.getKeyboard().pressKey("OpenQA.Selenium.Keys.Command + \"a\" + OpenQA.Selenium.Keys.Command");
            WindowsElement FAWebElement = rootSession.findElementByName("Petrasoft FieldAssist - mcbu-current - ");
            String FAWinHandleStr = FAWebElement.getAttribute("NativeWindowHandle");
            int FAWinHandleInt = Integer.parseInt(FAWinHandleStr);
            String FAWinHandleHex = Integer.toHexString(FAWinHandleInt);

            // You attach to the already running application
            DesiredCapabilities FACapabilities = new DesiredCapabilities();
            FACapabilities.setCapability("platformName", "Windows");

            // You set the Handle as one of the capabilities
            FACapabilities.setCapability("appTopLevelWindow", FAWinHandleHex);

            // My Application Session
            FASession = new WindowsDriver<>(new URI(winAppDriverUrl).toURL(), FACapabilities);

        } catch (Exception e) {
            e.printStackTrace();
        }


        downWells = new DownWells(FASession);
        beamWells = new BeamWells(FASession);
        fop = new FOP(FASession);
        espWells = new ESP(FASession);
        glWells = new GasLift(FASession);
        common = new Common(FASession);
        pagl = new PAGL(FASession);
        nfwells = new NFWells(FASession);

        List<WindowsElement> restoreDlg = FASession.findElementsByAccessibilityId("RestoreScreensSelectorDlg");
        if (!restoreDlg.isEmpty() && restoreDlg.getFirst().isDisplayed()) {
            FASession.findElementByAccessibilityId("btnUncheckAll").click();
            FASession.findElementByAccessibilityId("_btnOK").click();
        }

        FASession.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private static WindowsDriver<WindowsElement> getWindowsElementWindowsDriver() throws MalformedURLException, URISyntaxException, InterruptedException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", "\\\\MCBU-Current\\Upgrader\\Client\\PSC.FieldAssist.DesktopUpgraderApp.exe");
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("automationName", "windows");
        WindowsDriver<WindowsElement> launchSession = new WindowsDriver<>(new URI(winAppDriverUrl).toURL(), capabilities);
        Thread.sleep(5000);
        return launchSession;
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        if (FASession != null)
            FASession.close();
        startWinAppDriver.destroy();
    }
}
