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

import static TestUtils.OABaseTest.*;


public class WorkflowTests extends FABaseTest {


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

    @Test (dataProvider = "Beam_WF")
    public void Create_a_BeamWell_Workflow(HashMap<String,String> input) {
        beamWells.Navigate_to_BeamWells_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_StartWorkflow_Button();
        beamWells.Select_WorkflowType("Operations");
        common.Start_WorkflowDlg_DataFilling(input.get("assmt"), input.get("subAssmt"),  input.get("userGroup"), input.get("actn"), input.get("SAP"), input.get("cmnt"));
        common.Click_OK_Button();
        beamWells.Validate_FA_BeamWell_Workflow_Created_Or_Not(input.get("assmt"), input.get("subAssmt"), input.get("actn"));
        OALaunch();
        producers.OA_WFEdit(input.get("WellName"), input.get("assmt"), input.get("subAssmt"), input.get("actn"), input.get("change_actn"), input.get("cmnt") );
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Completed");
        producers.Tick_All_Tasks_As_Done_In_Alarms_TaskListTab();
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(input.get("WellName"), input.get("assmt"), input.get("subAssmt"), input.get("change_actn"));
        Switch_to_ParentWindow(FASession);
        beamWells.Verify_Workflow_is_Closed_Successfully();
    }

    @Test(dataProvider = "ESP_WF")
    public void Create_a_ESPWell_Workflow(HashMap<String,String> input) {
        espWells.Navigate_to_ESP_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_StartWorkflow_Button();
        common.Start_WorkflowDlg_DataFilling(input.get("assmt"), input.get("subAssmt") ,  input.get("userGroup"), input.get("actn"), input.get("SAP"), input.get("cmnt"));
        common.Click_OK_Button();
        espWells.Validate_FA_Workflow_Created_Or_Not(input.get("assmt"), input.get("subAssmt"), input.get("actn"));
        OALaunch();
        producers.OA_WFEdit(input.get("WellName"), input.get("assmt"), input.get("subAssmt"), input.get("actn"), input.get("change_actn"), input.get("cmnt") );
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Completed");
        producers.Tick_All_Tasks_As_Done_In_Alarms_TaskListTab();
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(input.get("WellName"), input.get("assmt"), input.get("subAssmt"), input.get("change_actn"));
        Switch_to_ParentWindow(FASession);
        espWells.Verify_Workflow_is_Closed_Successfully();
    }

    @Test(dataProvider = "GL_WF")
    public void Create_a_GasLiftWell_Workflow(HashMap<String,String> input) {
        glWells.Navigate_to_GasLift_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_StartWorkflow_Button();
        common.Start_WorkflowDlg_DataFilling(input.get("assmt"), input.get("subAssmt") ,  input.get("userGroup"), input.get("actn"), input.get("SAP"), input.get("cmnt"));
        //common.Click_OK_Button();
        glWells.Validate_FA_Workflow_Created_Or_Not(input.get("assmt"), input.get("subAssmt"), input.get("actn"));
    }

    @Test (dataProvider = "PAGL_WF", groups = {"Smoke"})
    //Template Id = 10821, 10824
    public void Create_a_PAGLWell_SSPS_Workflow(HashMap<String,String> input) {
        pagl.Navigate_to_PAGLWells_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_StartWorkflow_Button();
        common.Start_WorkflowDlg_DataFilling(input.get("assmt"), input.get("subAssmt") ,  input.get("userGroup"), input.get("actn"), input.get("SAP"), input.get("cmnt"));
        common.Click_OK_Button();
        pagl.Validate_FA_Workflow_Created_Or_Not(input.get("assmt"), input.get("subAssmt"), input.get("actn"));
    }

    @Test (dataProvider = "PAGL_SAPWF", groups = {"Smoke"})
    //Template Id = 10823
    public void Create_a_PAGLWell_SurfaceMaintenance_Workflow_With_SAP(HashMap<String,String> input) {
        pagl.Navigate_to_PAGLWells_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_StartWorkflow_Button();
        common.Start_WorkflowDlg_DataFilling(input.get("assmt"), input.get("subAssmt") ,  input.get("userGroup"), input.get("actn"), input.get("SAP"), input.get("cmnt"));
        common.Navigate_to_SAPTab_and_Enter_Details();
        common.Click_OK_Button();
        pagl.Validate_FA_Workflow_Created_Or_Not(input.get("assmt"), input.get("subAssmt"), input.get("actn"));
    }

    @Test (dataProvider = "Down_WF", groups = {"Smoke"})
    //Template ID = 10768
    public void Create_a_DownWell_Workflow(HashMap<String,String> input) {
        downWells.Navigate_to_DownWells_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_StartWorkflow_Button();
        downWells.Select_WorkflowType("Operations");
        common.Start_WorkflowDlg_DataFilling(input.get("assmt"), input.get("subAssmt") ,  input.get("userGroup"), input.get("actn"), input.get("SAP"), input.get("cmnt"));
        common.Click_OK_Button();
        downWells.Validate_FA_DownWell_Workflow_Created_Or_Not(input.get("assmt"), input.get("subAssmt"), input.get("actn"));
    }

    @Test (dataProvider = "FOP_SAPWF", groups = {"Smoke"})
    //Template ID = 10524
    public void Create_a_FOP_Workflow_With_SAP(HashMap<String,String> input){
        fop.Navigate_to_FOPWells_Screen();
        fop.Select_Well_from_ExceptionsGrid(input.get("WellName"));
        common.Click_StartWorkflow_Button();
        fop.Select_WorkflowType("Operations");
        common.Start_WorkflowDlg_DataFilling(input.get("assmt"), input.get("subAssmt") ,  input.get("userGroup"), input.get("actn"), input.get("SAP"), input.get("cmnt"));
        common.Navigate_to_SAPTab_and_Enter_Details();
        common.CleanUp_DataTab_Details_Filling("Backhoe");
        common.Click_OK_Button();
        fop.Validate_FA_FOPWell_Workflow_Created_Or_Not(input.get("assmt"), input.get("subAssmt"), input.get("actn"));
        fop.Verify_well_moved_to_ReviewedGrid(input.get("WellName"));
    }

    @Test (dataProvider = "NF_WF", groups = {"Smoke"})
    //Template Id = 10433
    public void Create_a_NFWell_SurfaceMaintenance_Workflow_Without_SAP(HashMap<String,String> input) {
        nfwells.Navigate_to_NFWells_AllWells_Screen_and_Select_a_Well(input.get("WellName"));
        common.Click_StartWorkflow_Button();
        common.Start_WorkflowDlg_DataFilling(input.get("assmt"), input.get("subAssmt") ,  input.get("userGroup"), input.get("actn"), input.get("SAP"), input.get("cmnt"));
        common.Click_OK_Button();
        nfwells.Validate_FA_NFWell_Workflow_Created_Or_Not(input.get("assmt"), input.get("subAssmt"), input.get("actn"));
    }

    @Test (dataProvider = "FOP_WF", groups = {"Smoke"})
    //Template ID = 10527
    public void Create_a_FOP_Workflow_Without_SAP(HashMap<String,String> input){
        fop.Navigate_to_FOPWells_Screen();
        fop.Select_Well_from_ExceptionsGrid(input.get("WellName"));
        common.Click_StartWorkflow_Button();
        fop.Select_WorkflowType("Operations");
        common.Start_WorkflowDlg_DataFilling(input.get("assmt"), input.get("subAssmt") ,  input.get("userGroup"), input.get("actn"), input.get("SAP"), input.get("cmnt"));
        common.CleanUp_DataTab_Details_Filling("Backhoe");
        common.Click_OK_Button();
        fop.Validate_FA_FOPWell_Workflow_Created_Or_Not(input.get("assmt"), input.get("subAssmt"), input.get("actn"));
        fop.Verify_well_moved_to_ReviewedGrid(input.get("WellName"));
    }



    @DataProvider(name = "Beam_WF")
    public Object[][] getBWData() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//BW_Workflow.json");
        return new Object[][]  {{data.get(0)},{data.get(1)},{data.get(2)}};
    }

    @DataProvider(name = "ESP_WF")
    public Object[][] getESPData() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//ESP_Workflow.json");
        return new Object[][]  {{data.get(0)},{data.get(1)},{data.get(2)}};
    }

    @DataProvider(name = "GL_WF")
    public Object[][] getGLData() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//GL_Workflow.json");
        return new Object[][]  {{data.get(0)}};
    }

    @DataProvider(name = "PAGL_WF")
    public Object[][] getPAGLData() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//PAGL_Workflow.json");
        return new Object[][]  {{data.get(0)},{data.get(1)}};
    }

    @DataProvider(name = "PAGL_SAPWF")
    public Object[][] getPAGLSAPData() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//PAGL_Workflow.json");
        return new Object[][]  {{data.get(2)}};
    }

    @DataProvider(name = "Down_WF")
    public Object[][] getDownData() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//Down_Workflow.json");
        return new Object[][]  {{data.get(0)}};
    }

    @DataProvider(name = "FOP_WF")
    public Object[][] getFOPData() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//FOP_Workflow.json");
        return new Object[][]  {{data.get(1)}};
    }

    @DataProvider(name = "FOP_SAPWF")
    public Object[][] getFOPSAPData() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//FOP_Workflow.json");
        return new Object[][]  {{data.get(0)}};
    }

    @DataProvider(name="NF_WF")
    public Object[][] getNFData() throws IOException {
        List<HashMap<String, String>> data = FAUtils.getJsonData(System.getProperty("user.dir") + "//src//test//java//TestData//NF_Workflow.json");
        return new Object[][]  {{data.get(0)}};
    }
}
