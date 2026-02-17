package faClientTests;

import TestUtils.FABaseTest;
import org.example.pageobject.FieldAssist_Client.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;
import utils.FAUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static TestUtils.OABaseTest.OASession;


public class AddWellNoteTests extends FABaseTest {


    @AfterMethod(alwaysRun = true)
    public void CloseTabs(){
        if (OASession != null)
            OASession.close();
        boolean mainWindow = FASession.findElementByAccessibilityId("MapleMainForm").isEnabled();
        if (!mainWindow) {
            Actions actions = new Actions(FASession);
            String parentWinHandle = FASession.getWindowHandle();
            for (String handle : FASession.getWindowHandles()) {
                if (!handle.equals(parentWinHandle)) {
                    FASession.switchTo().window(handle);
                    System.out.println("Switched to new window: " + FASession.getTitle());
                }
            }

            actions.keyDown(Keys.ALT).sendKeys(Keys.F4).keyUp(Keys.ALT).build().perform();
            FASession.switchTo().window(parentWinHandle);
        }
        FASession.findElement(By.name("Windows")).click();
        FASession.findElement(By.name("Close All")).click();
    }


    @Test(dataProvider = "Beam")
    public void Add_a_WellNote_in_BeamWellsScreen(HashMap<String, String> input) {
        beamWells.Navigate_to_BeamWells_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_AddWellNote_Button();
        common.Enter_AddWellNote_Comment(input.get("WellName"), input.get("Comment"));
        common.Click_EditCommentDlg_OK_Button();
        beamWells.Verify_WellNote_is_added_or_not(input.get("Comment"));
    }

    @Test(dataProvider = "ESP")
    public void Add_a_WellNote_in_ESPWellsScreen(HashMap<String, String> input) {
        espWells.Navigate_to_ESP_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_AddWellNote_Button();
        common.Enter_AddWellNote_Comment(input.get("WellName"), input.get("Comment"));
        common.Click_EditCommentDlg_OK_Button();
        espWells.Verify_WellNote_is_added_or_not(input.get("Comment"));
    }

    @Test(dataProvider = "GasLift")
    public void Add_a_WellNote_in_GasLiftWellsScreen(HashMap<String, String> input) {
        glWells.Navigate_to_GasLift_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_AddWellNote_Button();
        common.Enter_AddWellNote_Comment(input.get("WellName"), input.get("Comment"));
        common.Click_EditCommentDlg_OK_Button();
        glWells.Verify_WellNote_is_added_or_not(input.get("Comment"));
    }


    @Test(dataProvider = "PAGL")
    public void Add_a_WellNote_in_PAGLWellsScreen(HashMap<String, String> input) {
        pagl.Navigate_to_PAGLWells_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_AddWellNote_Button();
        common.Enter_AddWellNote_Comment(input.get("WellName"), input.get("Comment"));
        common.Click_EditCommentDlg_OK_Button();
        pagl.Verify_WellNote_is_added_or_not(input.get("Comment"));
    }

    @Test(dataProvider = "Down")
    public void Add_a_WellNote_in_DownWellsScreen(HashMap<String,String> input) {
        downWells.Navigate_to_DownWells_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_AddWellNote_Button();
        common.Enter_AddWellNote_Comment(input.get("WellName"), input.get("Comment"));
        common.Click_EditCommentDlg_OK_Button();
        downWells.Verify_WellNote_is_added_or_not(input.get("Comment"));
    }

    @Test(dataProvider = "NF")
    public void Add_a_WellNote_in_NFWellsScreen(HashMap<String,String> input) {
        nfwells.Navigate_to_NFWells_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_AddWellNote_Button();
        common.Enter_AddWellNote_Comment(input.get("WellName"), input.get("Comment"));
        common.Click_EditCommentDlg_OK_Button();
        nfwells.Verify_WellNote_is_added_or_not(input.get("Comment"));
    }

    @DataProvider(name = "Beam")
    public Object[][] BeamWellNoteDataProvider() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir")+"//src//test//java//TestData//BW_AddWellNote.json");
        return new Object[][] {{data.get(0)}};
    }

    @DataProvider(name = "ESP")
    public Object[][] ESPWellNoteDataProvider() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir")+"//src//test//java//TestData//BW_AddWellNote.json");
        return new Object[][] {{data.get(1)}};
    }

    @DataProvider(name = "GasLift")
    public Object[][] GasLiftWellNoteDataProvider() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir")+"//src//test//java//TestData//BW_AddWellNote.json");
        return new Object[][] {{data.get(2)}};
    }

    @DataProvider(name = "PAGL")
    public Object[][] PAGLWellNoteDataProvider() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir")+"//src//test//java//TestData//BW_AddWellNote.json");
        return new Object[][] {{data.get(3)}};
    }

    @DataProvider(name = "Down")
    public Object[][] DownWellNoteDataProvider() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir")+"//src//test//java//TestData//BW_AddWellNote.json");
        return new Object[][] {{data.get(4)}};
    }

    @DataProvider(name = "NF")
    public Object[][] NFWellNoteDataProvider() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir")+"//src//test//java//TestData//BW_AddWellNote.json");
        return new Object[][] {{data.get(5)}};
    }

}
