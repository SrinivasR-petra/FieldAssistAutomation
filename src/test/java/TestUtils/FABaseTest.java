package TestUtils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.example.pageobject.FieldAssist_Client.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
import java.util.concurrent.TimeUnit;

public class FABaseTest extends FAUtils {
    public static WindowsDriver<WindowsElement> FASession = null;
    private static String field, winAppDriverUrl;
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

        String  ipAddress = System.getProperty("ipAddress") != null ? System.getProperty("ipAddress") : prop.getProperty("ipAddress");
        String  port = System.getProperty("port") != null ? System.getProperty("port") : prop.getProperty("port");
        field = System.getProperty("field") != null ? System.getProperty("field") : prop.getProperty("field");

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

        FASession.manage().window().maximize();

        List<WindowsElement> restoreDlg = FASession.findElementsByAccessibilityId("RestoreScreensSelectorDlg");
        if (!restoreDlg.isEmpty() && restoreDlg.getFirst().isDisplayed()) {
            FASession.findElementByAccessibilityId("btnUncheckAll").click();
            FASession.findElementByAccessibilityId("_btnOK").click();
        }
        FABaseTest.FieldSelection();
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

    public static void FieldSelection(){
        WindowsElement toolStrip = FASession.findElementByAccessibilityId("toolStrip1");
        WindowsElement fieldsBtn = (WindowsElement) toolStrip.findElement(By.name("Fields..."));
        if (fieldsBtn.isEnabled()) {
            fieldsBtn.click();
            WindowsElement selectFieldsWindow = FASession.findElementByAccessibilityId("FieldNavigationForm");
            selectFieldsWindow.findElementByName("Collapse All").click();
            WindowsElement fieldsPane = (WindowsElement) selectFieldsWindow.findElementByAccessibilityId("tableLayoutPanel1");
            List<MobileElement> mainTreeViewList = fieldsPane.findElementsByXPath("//Tree[@AutomationId='treeView']/TreeItem");
            for (MobileElement element : mainTreeViewList) {
                System.out.println("Field's main tree selected is : "+element.getAttribute("Name"));
                element.click();
                element.sendKeys(Keys.ARROW_RIGHT);
                String parentSelected = element.getAttribute("Name");
                try{
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                element.sendKeys(Keys.ARROW_DOWN);
                List<MobileElement> fieldsList = fieldsPane.findElementsByXPath("//TreeItem[@Name='"+parentSelected+"']/TreeItem");
                for (MobileElement field1 : fieldsList) {
                    System.out.println(field1.getAttribute("Name"));
                    String selectedField = field1.getAttribute("Name");
                    String selectionStatus = field1.getAttribute("HelpText");
                    if (selectedField.equals(field)) {
                        if (selectionStatus.equals("Unchecked")) {
                            System.out.println("Got Field name passed and it's status is Unchecked");
                            field1.click();
                            field1.sendKeys(Keys.SPACE);
                        }
                    } else {
                        if (selectionStatus.equals("Checked")) {
                            field1.click();
                            field1.sendKeys(Keys.SPACE);
                        }
                    }
                    field1.sendKeys(Keys.ARROW_DOWN);
                }
                parentSelected = null;
            }
            String numFieldsSelected = FASession.findElementByXPath("//StatusBar[@AutomationId = 'statusStrip1']/Text").getText();
            Assert.assertEquals(numFieldsSelected, "Num Fields Selected: 1");
            WindowsElement okBtn = FASession.findElementByAccessibilityId("btnOk");
            if (okBtn.isEnabled())
                okBtn.click();
            else FASession.findElementByAccessibilityId("btnCancel").click();
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws InterruptedException {
        Thread.sleep(2000);
        if (FASession != null)
            FASession.close();
        startWinAppDriver.destroy();
    }
}
