package opsAssistTests;

import TestUtils.OABaseTest;
import org.example.pageobject.OpsAssist.Producers;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class OpsAssistTests extends OABaseTest {

   @AfterMethod
   public void CloseAdditionalWindows(){
       boolean mainWindow = OASession.findElementByAccessibilityId("MainForm").isEnabled();
       if (!mainWindow) {
           Actions actions = new Actions(OASession);
           String parentWinHandle = OASession.getWindowHandle();
           for (String handle : OASession.getWindowHandles()) {
               if (!handle.equals(parentWinHandle)) {
                   OASession.switchTo().window(handle);
                   System.out.println("Switched to new window: " + OASession.getTitle());
               }
           }

           actions.keyDown(Keys.ALT).sendKeys(Keys.F4).keyUp(Keys.ALT).build().perform();
           OASession.switchTo().window(parentWinHandle);
       }
   }
    @Test
    public void CleanUp_With_JobData_Workflow() {
        String wellname = "REEDY 194LS", JobType = "Clean Up", IniAssessment = "Fluid on Ground Tier 1: Priority Wells", Actns = ".Diagnose/Execute", Action_Change = ".Diagnose/Execute", Comment = "Testing", Comment_Change = "Testing Done";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.CreateSAP_Yes_No("Yes");
        producers.CleanUp_JobDataTab("Cleanup Crew");
        producers.Click_OK_Button();
        producers.OA_WFEdit(wellname,JobType,IniAssessment,Actns, Action_Change,Comment_Change);
        producers.CreateSAP_Yes_No("No");
        producers.CleanUp_JobDataTab("Backhoe");
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Completed");
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname,JobType,IniAssessment,Action_Change);
    }

    @Test
    public void CleanUp_With_SAP_Workflow() {
        String wellname = "SCHARB 32LS", JobType = "Clean Up", IniAssessment = "Cellar Pull Tier 1: Large (Approx 100% Full)", Actns = ".Diagnose/Execute", Action_Change = ".Diagnose/Execute", Comment = "Testing", Comment_Change = "Automation Testing";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.CreateSAP_Yes_No("Yes");
        producers.Click_OK_Button();
        producers.OA_WFEdit(wellname,JobType,IniAssessment,Actns, Action_Change,Comment_Change);
        producers.CreateSAP_Yes_No("No");
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Completed");
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname,JobType,IniAssessment, Action_Change);
    }

    @Test
    public void Optimization_With_JobData_Workflow() {
        String wellname = "SCHARB 31LS", JobType = "Optimization", IniAssessment = "Casing Blowdown Procedure", Actns = "Complete to Atmosphere", Comment = "Testing";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.Optimization_JobDataTab();
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname, JobType, IniAssessment, Actns);
    }

    @Test
    public void OtherWellTest_With_JobData_Workflow() {
        String wellname = "ARTEMIS 51LS", JobType = "Other", IniAssessment = "Well Test", Actns = ".Need Well Test", Action_Change = ".Needs Approval", Comment = "Testing", Comment_Change = "Automation Testing";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.Click_OK_Button();
        producers.OA_WFEdit(wellname, JobType, IniAssessment, Actns,Action_Change,Comment_Change);
        producers.OtherWellTest_JobDataTab(6,68,8);
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Completed");
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname, JobType, IniAssessment, Action_Change);

    }

    @Test
    public void OtherFluidShot_With_JobData_Workflow() {
        String wellname = "ARTEMIS 44LS", JobType = "Other", IniAssessment = "Fluid Shot", Actns = ".Need Fluid Shot", Action_Change = "Completed", Comment = "Testing", Comment_Change = "Automation Testing";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.Click_OK_Button();
        producers.OA_WFEdit(wellname,JobType,IniAssessment,Actns, Action_Change, Comment_Change);
        producers.OtherFluidShot_JobData("110", "EC Testing Comment");
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Completed");
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname,JobType,IniAssessment, Action_Change);
    }

    @Test
    public void OtherPressureReading_With_JobData_Workflow() {
        String wellname = "ARTEMIS 43LS", JobType = "Other", IniAssessment = "Pressure Reading", Actns = ".Need Pressure Reading", Action_Change = "Completed", Comment = "Testing", Comment_Change = "Automation Testing";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.Click_OK_Button();
        producers.OA_WFEdit(wellname, JobType,IniAssessment,Actns, Action_Change,Comment_Change);
        producers.OtherPressureReading_JobData("110","180","EC Testing Comment");
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Completed");
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname, JobType,IniAssessment, Action_Change);
    }



}
