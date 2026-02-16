package faClientTests;

import TestUtils.FABaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utils.FAUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DeferAndUnDeferTests extends FABaseTest {

    @AfterMethod(alwaysRun = true)
    public void CloseTabs(){
        boolean mainWindow = FASession.findElementByAccessibilityId("MapleMainForm").isEnabled();
        if (!mainWindow) {
            Actions actions = new Actions(FASession);
            actions.keyDown(Keys.ALT).sendKeys(Keys.F4).keyUp(Keys.ALT).build().perform();
        }
        FASession.findElement(By.name("Windows")).click();
        FASession.findElement(By.name("Close All")).click();
    }

    @Test(dataProvider = "Beam")
    public void Defer_BeamExceptionsScreen(HashMap<String,String> input) {
        beamWells.Navigate_to_BeamWells_Screen();
        beamWells.Navigate_to_BW_ExcCurrentGrid_and_Select_a_Well(input.get("WellName"));
        common.Click_Defer_Button();
        common.DeferDlg_details_entry(input.get("Category"), input.get("SubCategory"), input.get("Days"),input.get("Comment"));
        common.Click_DeferDlg_OKBtn();
        beamWells.Navigate_to_ExcDeferredTab_and_Select_a_Well(input.get("WellName"));
        beamWells.Verify_Defer_Workflow_is_Created_or_Not(input.get("Category"));
    }

    @Test(dataProvider = "Beam", dependsOnMethods = "Defer_BeamExceptionsScreen")
    public void UnDefer_BeamExceptionsScreen(HashMap<String,String> input) {
        beamWells.Navigate_to_BeamWells_Screen();
        beamWells.Navigate_to_ExcDeferredTab_and_Select_a_Well(input.get("WellName"));
        beamWells.Verify_Defer_Workflow_is_Created_or_Not(input.get("Category"));
        common.Click_UnDefer_Button();
        common.UnDeferDlg_Comment_entry(input.get("Undef_Comment"));
        common.Click_DeferDlg_OKBtn();
        beamWells.Navigate_to_BW_ExcCurrentGrid_and_Select_a_Well(input.get("WellName"));  // Verifying that after un-defer the well is moved to Current tab or not.
    }

    @Test(dataProvider = "ESP")
    public void Defer_ESPExceptionsScreen(HashMap<String,String> input) {
        espWells.Navigate_to_ESPWells_Screen();
        espWells.Navigate_to_ESP_ExcCurrentGrid_and_Select_a_Well(input.get("WellName"));
        common.Click_Defer_Button();
        common.DeferDlg_details_entry(input.get("Category"), input.get("SubCategory"), input.get("Days"),input.get("Comment"));
        common.Click_DeferDlg_OKBtn();
        espWells.Navigate_to_ExcDeferredTab_and_Select_a_Well(input.get("WellName"));
        espWells.Verify_Defer_Workflow_is_Created_or_Not(input.get("Category"));
    }

    @Test(dataProvider = "ESP", dependsOnMethods = "Defer_ESPExceptionsScreen")
    public void UnDefer_ESPExceptionsScreen(HashMap<String,String> input) {
        espWells.Navigate_to_ESPWells_Screen();
        espWells.Navigate_to_ExcDeferredTab_and_Select_a_Well(input.get("WellName"));
        espWells.Verify_Defer_Workflow_is_Created_or_Not(input.get("Category"));
        common.Click_UnDefer_Button();
        common.UnDeferDlg_Comment_entry(input.get("Undef_Comment"));
        common.Click_DeferDlg_OKBtn();
        espWells.Navigate_to_ESP_ExcCurrentGrid_and_Select_a_Well(input.get("WellName"));  // Verifying that after un-defer the well is moved to Current tab or not.
    }

    @Test(dataProvider = "GasLift")
    public void Defer_GasLiftExceptionsScreen(HashMap<String,String> input) {
        glWells.Navigate_to_GasLiftWells_Screen();
        glWells.Navigate_to_GasLift_ExcCurrentGrid_and_Select_a_Well(input.get("WellName"));
        common.Click_Defer_Button();
        common.DeferDlg_details_entry(input.get("Category"), input.get("SubCategory"), input.get("Days"),input.get("Comment"));
        common.Click_DeferDlg_OKBtn();
        glWells.Navigate_to_ExcDeferredTab_and_Select_a_Well(input.get("WellName"));
        glWells.Verify_Defer_Workflow_is_Created_or_Not(input.get("Category"));
    }

    @Test(dataProvider = "GasLift", dependsOnMethods = "Defer_GasLiftExceptionsScreen")
    public void UnDefer_GasLiftExceptionsScreen(HashMap<String,String> input) {
        glWells.Navigate_to_GasLiftWells_Screen();
        glWells.Navigate_to_ExcDeferredTab_and_Select_a_Well(input.get("WellName"));
        glWells.Verify_Defer_Workflow_is_Created_or_Not(input.get("Category"));
        common.Click_UnDefer_Button();
        common.UnDeferDlg_Comment_entry(input.get("Undef_Comment"));
        common.Click_DeferDlg_OKBtn();
        glWells.Verify_Workflow_is_Undeferred(input.get("WellName"));  // Verifying that after un-defer the well is moved to Current tab or not.
    }

    @Test(dataProvider = "PAGL")
    public void Defer_PAGLExceptionsScreen(HashMap<String,String> input) {
        pagl.Navigate_to_PAGLWells__Screen();
        pagl.Navigate_to_PAGL_ExcCurrentGrid_and_Select_a_Well(input.get("WellName"));
        common.Click_Defer_Button();
        common.DeferDlg_details_entry(input.get("Category"), input.get("SubCategory"), input.get("Days"),input.get("Comment"));
        common.Click_DeferDlg_OKBtn();
        pagl.Navigate_to_ExcDeferredTab_and_Select_a_Well(input.get("WellName"));
        pagl.Verify_Defer_Workflow_is_Created_or_Not(input.get("Category"));
    }

    @Test(dataProvider = "PAGL", dependsOnMethods = "Defer_PAGLExceptionsScreen")
    public void UnDefer_PAGLExceptionsScreen(HashMap<String,String> input) {
        pagl.Navigate_to_PAGLWells__Screen();
        pagl.Navigate_to_ExcDeferredTab_and_Select_a_Well(input.get("WellName"));
        pagl.Verify_Defer_Workflow_is_Created_or_Not(input.get("Category"));
        common.Click_UnDefer_Button();
        common.UnDeferDlg_Comment_entry(input.get("Undef_Comment"));
        common.Click_DeferDlg_OKBtn();
        pagl.Verify_Workflow_is_Undeferred(input.get("WellName"));  // Verifying that after un-defer the well is moved to Current tab or not.
    }

    @Test(dataProvider = "FOP")
    public void Defer_a_well_in_FOPScreen(HashMap<String,String> input){
        String WellName = "MID AC AF 8HD";
        fop.Navigate_to_FOPWells_Screen();
        fop.Select_Well_from_ExceptionsGrid(input.get("WellName"));
        common.Click_Defer_Button();
        common.DeferDlg_details_entry(input.get("Category"), input.get("SubCategory"), input.get("Days"),input.get("Comment"));
        common.Click_DeferDlg_OKBtn();
        fop.Verify_well_moved_to_ReviewedGrid(input.get("WellName"));
    }

    @Test(dataProvider = "FOP", dependsOnMethods = "Defer_a_well_in_FOPScreen")
    public void UnDefer_a_well_in_FOPScreen(HashMap<String,String> input){
        String WellName = "MID AC AF 8HD";
        fop.Navigate_to_FOPWells_Screen();
        fop.Select_Well_from_Reviewed_Tab(input.get("WellName"));
        common.Click_UnDefer_Button();
        common.UnDeferDlg_Comment_entry("Defer Testing Completed");
        common.Click_DeferDlg_OKBtn();
        fop.Select_Well_from_ExceptionsGrid(input.get("WellName"));
    }

    @DataProvider(name = "Beam")
    public Object[][] Beam() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//Defer.json");
        return new Object[][]{{data.getFirst()}};
    }

    @DataProvider(name = "ESP")
    public Object[][] ESP() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//Defer.json");
        return new Object[][]{{data.get(1)}};
    }

    @DataProvider(name = "GasLift")
    public Object[][] GasLift() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//Defer.json");
        return new Object[][]{{data.get(2)}};
    }

    @DataProvider(name = "PAGL")
    public Object[][] PAGL() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//Defer.json");
        return new Object[][]{{data.get(3)}};
    }

    @DataProvider(name = "FOP")
    public Object[][] FOP() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//Defer.json");
        return new Object[][]{{data.get(4)}};
    }
}
