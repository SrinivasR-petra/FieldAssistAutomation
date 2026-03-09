package opsAssistTests;

import TestUtils.OABaseTest;
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
        producers.Navigate_to_MyJobs_tab();
        producers.Navigate_to_MyJobs_Inbox();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname,JobType,IniAssessment,Actns);
        producers.Click_EditJob_button();
        producers.CreateSAP_Yes_No("No");
        producers.Change_Action_in_EditJobDialog(Action_Change);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
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
        producers.Navigate_to_MyJobs_tab();
        producers.Navigate_to_MyJobs_Inbox();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname,JobType,IniAssessment,Actns);
        producers.Click_EditJob_button();
        producers.CreateSAP_Yes_No("No");
        producers.Change_Action_in_EditJobDialog(Action_Change);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Completed");
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname,JobType,IniAssessment, Action_Change);
    }

    @Test
    public void Optimization_With_JobData_Workflow() {
        String wellname = "SCHARB 31LS", JobType = "Optimization", IniAssessment = "Casing Blowdown Procedure", Actns = ".Evaluate/Execute", Action_Change = "Complete to Atmosphere", Comment = "Testing", Comment_Change = "Automation Testing";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.Click_OK_Button();
        producers.Navigate_to_MyJobs_tab();
        producers.Navigate_to_MyJobs_Inbox();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname,JobType,IniAssessment,Actns);
        producers.Click_EditJob_button();
        producers.Change_Action_in_EditJobDialog(Action_Change);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
        producers.Optimization_JobDataTab();
        producers.Completed_RadioButton_should_be_selected();
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname, JobType, IniAssessment, Action_Change);
    }

    @Test
    public void OtherWellTest_With_JobData_Workflow() {
        String wellname = "ARTEMIS 51LS", JobType = "Other", IniAssessment = "Well Test", Actns = ".Need Well Test", Action_Change = ".Needs Approval", Comment = "Testing", Comment_Change = "Automation Testing";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.Click_OK_Button();
        producers.Navigate_to_MyJobs_tab();
        producers.Navigate_to_MyJobs_Inbox();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname,JobType,IniAssessment,Actns);
        producers.Click_EditJob_button();
        producers.Change_Action_in_EditJobDialog(Action_Change);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
        producers.OtherWellTest_JobDataTab(6,68,8);
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Completed");
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname, JobType, IniAssessment, Action_Change);
    }

    @Test
    public void OtherWellTest_With_CancelOrCompletedAction() {
        String wellname = "ARTEMIS 51LS", JobType = "Other", IniAssessment = "Well Test", Actns = ".Need Well Test", Action_Change = "Cancel", Comment = "Testing", Comment_Change = "Automation Testing";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.Click_OK_Button();
        producers.Navigate_to_MyJobs_tab();
        producers.Navigate_to_MyJobs_Inbox();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname,JobType,IniAssessment,Actns);
        producers.Click_EditJob_button();
        producers.Change_Action_in_EditJobDialog(Action_Change);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
        producers.Completed_RadioButton_should_be_selected();
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
        producers.Navigate_to_MyJobs_tab();
        producers.Navigate_to_MyJobs_Inbox();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname,JobType,IniAssessment,Actns);
        producers.Click_EditJob_button();
        producers.Change_Action_in_EditJobDialog(Action_Change);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
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
        producers.Navigate_to_MyJobs_tab();
        producers.Navigate_to_MyJobs_Inbox();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname,JobType,IniAssessment,Actns);
        producers.Click_EditJob_button();
        producers.Change_Action_in_EditJobDialog(Action_Change);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
        producers.OtherPressureReading_JobData("110","180","EC Testing Comment");
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Completed");
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname, JobType,IniAssessment, Action_Change);
    }

    @Test
    public void ESPTroubleshooting_Workflow(){
        String wellname = "CMC PORTERHOUSE B 1062LS", JobType = "ESP Troubleshooting", IniAssessment = "Bad Gauge Reading/Scaling", Actns = "Archestra Setting Issue", Action_Change1 = "Bad Surface Choke",Action_Change2 = "Completed", Comment = "Testing", Comment_Change = "Automation Testing";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.AssignTo_an_User_or_Group("Group", "Odessa FS");
        producers.CreateSAP_Yes_No("Yes");
        producers.Click_OK_Button();
        producers.Navigate_to_MyJobs_tab();
        producers.Navigate_to_MyJobs_Inbox();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname,JobType,IniAssessment,Actns);
        producers.Click_EditJob_button();
        producers.Change_Action_in_EditJobDialog(Action_Change1);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
        producers.Select_Radio_Button_Fron_Edit_Job_Dialog("Edit");
        producers.AssignTo_an_User_or_Group("Group", "Odessa FS");
        producers.CreateSAP_Yes_No("No");
        producers.Click_OK_Button();
        producers.Navigate_to_MyJobs_tab();
        producers.Navigate_to_MyJobs_Inbox();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname,JobType,IniAssessment,Action_Change1);
        producers.Click_EditJob_button();
        producers.Change_Action_in_EditJobDialog(Action_Change2);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
        producers.Completed_RadioButton_should_be_selected();
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname, JobType,IniAssessment, Action_Change2);
    }

    @Test
    public void Workflow_with_NextJobAction(){
        String wellname = "CMC PORTERHOUSE B 1062LS", JobType = "ESP Troubleshooting", IniAssessment = "Control Mode Change", Actns = ".Execute", Action_Change="Subsurface Maintenance", NextAction = "Hole in Tubing", Action_Change2 = ".Diagnose/Execute", Action_Change3 = "Completed", Comment = "Testing", Comment_Change = "Automation Testing";
        producers.Navigate_to_Wells_Producers_Screen();
        producers.Select_a_Well(wellname);
        producers.Click_StartJob_Button_And_data_entry(wellname, IniAssessment, Actns, Comment);
        producers.AssignTo_an_User_or_Group("Group", "Odessa FS");
        producers.CreateSAP_Yes_No("Yes");
        producers.Click_OK_Button();
        producers.Navigate_to_MyJobs_tab();
        producers.Navigate_to_MyJobs_Inbox();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname,JobType,IniAssessment,Actns);
        producers.Click_EditJob_button();
        producers.Change_Action_in_EditJobDialog(Action_Change);
        producers.Select_NextJobAction(NextAction);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
        producers.AssignTo_an_User_or_Group("Group", "Odessa FS");
        producers.Completed_RadioButton_should_be_selected();
        producers.Verify_the_NextAction_Job_Info_is_Displayed_at_bottom(Action_Change,NextAction);
        producers.Click_OK_Button();
        producers.Verify_workflow_is_created_and_displayed_in_inbox(wellname, Action_Change, NextAction, Action_Change2);
        producers.Click_EditJob_button();
        producers.Change_Action_in_EditJobDialog(Action_Change3);
        producers.Enter_Comment_in_EditJobDialog(Comment_Change);
        producers.Completed_RadioButton_should_be_selected();
        producers.Click_OK_Button();
        producers.Verify_the_Workflow_is_closed(wellname,Action_Change,NextAction,Action_Change3);
    }

}
