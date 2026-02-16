package org.example.pageobject.FieldAssist_Client;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import junit.framework.TestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Common {
    private final WindowsDriver<WindowsElement> FASession;
    private final WebDriverWait wait;
    private final Actions actions;

    public Common(WindowsDriver<WindowsElement> _FASession) {
        this.FASession = _FASession;
        this.wait = new WebDriverWait(FASession, 20);
        actions = new Actions(FASession);
    }

    private final By OK_Btn = By.name("OK");
    private final By Cancel_Btn = By.name("Cancel");
    private By Grid_DataTable = MobileBy.AccessibilityId("tableControl1");

    // Start Workflow dialog locators.
    private final By StartWorkflow_Btn = By.name("Start Workflow");
    private final By StartWorkflow_Dlg = MobileBy.AccessibilityId("CreateWorkflowDlgOpsComm");
    private final By Assessments_Category = MobileBy.AccessibilityId("_comboInitialAssessmentCategories");
    private final By Initial_Assessments = MobileBy.AccessibilityId("_comboInitialAssessment");
    private final By AssignToGroup = MobileBy.AccessibilityId("comboAssignToGroup");
    private final By WF_Actions = MobileBy.AccessibilityId("_comboActions");
    private final By SAP_Checkbox = MobileBy.AccessibilityId("_checkBoxCreateD7i");
    private final By CommentsBox = MobileBy.AccessibilityId("_textComment");

    // Clean-Up Data tab locators.
    private By CleanUp_DataTab = By.name("tpaJobData");
    private By CleanUp_DataGrid = MobileBy.AccessibilityId("ggcJobData");
    private By EditSelectedRow_Btn = MobileBy.AccessibilityId("btnEditSelectedJobSpecificData");
    private By EditJobDataEntry_Dlg = MobileBy.AccessibilityId("WorkflowEditJobSpecificDataDlg");
    private By ReqResource = MobileBy.AccessibilityId("cbFromValue");

    // Add WellNote locators.
    private final By AddWellNote_Btn = By.name("Add Well Note");
    private final By EditComment_Dlg = MobileBy.AccessibilityId("CreateLowisWellInfoWorkflow");
    private final By EditCommentDlg_Comment = MobileBy.AccessibilityId("tbComment");
    private final By EditCommentDlg_OkBtn = MobileBy.AccessibilityId("btnOK");

    // SAP tab locators.
    private final By SAP_Tab = By.name("tpaSAP");
    private final By SAP_Description = MobileBy.AccessibilityId("tb_Description");
    //private By SAP_Location = MobileBy.AccessibilityId("tb_Location");
    //private By SAP_RegulationName = MobileBy.AccessibilityId("combo_RegulationName");
    private final By SAP_DetectionMethod = MobileBy.AccessibilityId("combo_Type");
    private final By SAP_Priority = MobileBy.AccessibilityId("combo_Priority");
    private final By SAP_Effect = MobileBy.AccessibilityId("combo_Effect");
    //private By SAP_ChildEquipment = MobileBy.AccessibilityId("combo_ChildEquipments");
    //private By SAP_NotificationGroup = MobileBy.AccessibilityId("combo_Class");
    private final By SAP_NotificationCode = MobileBy.AccessibilityId("combo_ProblemCode");
    //private By SAP_WorkCenter = MobileBy.AccessibilityId("combo_Subclass");
    //private By SAP_AffectedProductionBOE = MobileBy.AccessibilityId("_textBoxAffectedProduction");
    private final By SAP_EquipDown = MobileBy.AccessibilityId("_comboEquipmentDown");
    //private By SAP_DateTimePicker = MobileBy.AccessibilityId("_dtpWellStatusChange");

    // Defer Dialog locators.
    private By DeferDlg = MobileBy.AccessibilityId("DeferWellDlg");
    private By Defer_Category = MobileBy.AccessibilityId("_comboInitialAssessment");
    private By Defer_SubCategory = MobileBy.AccessibilityId("_comboSubCategory");
    private By Defer_Days = MobileBy.AccessibilityId("_numericUpDownDays");
    private By Defer_Comment = MobileBy.AccessibilityId("_textComment");
    private By Defer_OKBtn = MobileBy.AccessibilityId("_btnOk");
    private By UndeferDlg = MobileBy.AccessibilityId("UndeferWellDlg");
    private By DeferBtn = By.name("Defer");
    private By UnDeferBtn = By.name("Undefer");

    private static Set<String> seenValues = new HashSet<>();

    private static boolean isDuplicate(String value) {
        if (value.isEmpty()) return false; // Ignore empty values
        return !seenValues.add(value); // add() returns false if already exists
    }

    public void WellSelectionFromWellsDataGrid(MobileElement wellDataGrid, String DWellName) {
        actions.moveToElement(wellDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        String appWellName;                                                                                         //*******************---------------------------------------------------------------**************************
        while (true) {                                                                                              //
            List<MobileElement> wName = wellDataGrid.findElementsByXPath("//*");                              //
            appWellName = wName.getLast().getAttribute("Value.Value");
            int wnsize = appWellName.getBytes().length;                                                             //  This block will compare the Wellname collected from Application and Wellname collected from data-sheet
            if (appWellName.equals(DWellName)) {                                                                    //              If both are equal then it'll break the loop
                System.out.println("Well selected is : " + appWellName);
                seenValues.clear();                                                                                 //
                break;                                                                                              //
            }                                                                                                       //              And continue for clicking Start Workflow button and further process.
            else {                                                                                                  //      If the wellname's are not equal then it'll press the down arrow from the currently selected well
                for (int i = 0; i < wnsize; i++) {wellDataGrid.sendKeys(Keys.ARROW_RIGHT);}

                wellDataGrid.sendKeys(Keys.ARROW_DOWN);                                                             //                      and verifies that wellname against the wellname collected from data-sheet.
                wellDataGrid.sendKeys(Keys.F2);                                                                     //
            }                                                                                                       //
            if (isDuplicate(appWellName)) {                                                                         //
                TestCase.fail("Error : Unable to find the provided Wellname");                                      //
                break;                                                                                              //
            }                                                                                                       //
        }
    }

    public void Click_Defer_Button(){
        MobileElement Defer_Btn = FASession.findElement(DeferBtn);
        wait.until((ExpectedCondition<Boolean>) wd -> {
            return wd.findElement(DeferBtn).isEnabled();
        });
        // Clicks the Cancel button from the Start Workflow dialog.
        Defer_Btn.click();
    }

    public void Click_UnDefer_Button(){
        MobileElement UnDefer_Btn = FASession.findElement(UnDeferBtn);
        wait.until((ExpectedCondition<Boolean>) wd -> {
            return wd.findElement(UnDeferBtn).isEnabled();
        });
        // Clicks the Cancel button from the Start Workflow dialog.
        UnDefer_Btn.click();
    }

    public void DeferDlg_details_entry(String Category, String SubCategory, String Days, String Comment) {
        FASession.findElement(DeferDlg).isDisplayed();
        FASession.findElement(Defer_Category).sendKeys(Category);
        FASession.findElement(Defer_SubCategory).sendKeys(SubCategory);
        actions.sendKeys(Keys.TAB).keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).build().perform();
        FASession.findElement(Defer_Days).clear();
        FASession.findElement(Defer_Days).sendKeys(Days);
        FASession.findElement(Defer_Comment).sendKeys(Comment);
    }

    public void UnDeferDlg_Comment_entry(String Comment){
        FASession.findElement(UndeferDlg).isDisplayed();
        FASession.findElement(Defer_Comment).sendKeys(Comment);
    }

    public void Click_DeferDlg_OKBtn() {
        MobileElement OK_Btn1 = FASession.findElement(Defer_OKBtn);
        wait.until((ExpectedCondition<Boolean>) wd -> {
            return wd.findElement(Defer_OKBtn).isEnabled();
        });
        OK_Btn1.click();
    }

    public void Navigate_to_SAPTab_and_Enter_Details(){
        FASession.findElement(SAP_Tab).isDisplayed();
        FASession.findElement(SAP_Tab).click();
        FASession.findElement(SAP_Description).sendKeys("SAP Job");
        FASession.findElement(SAP_DetectionMethod).sendKeys("0008");
        actions.sendKeys(Keys.ENTER).build().perform();
        FASession.findElement(SAP_Priority).sendKeys("5");
        actions.sendKeys(Keys.ENTER).build().perform();
        FASession.findElement(SAP_Effect).sendKeys("1");
        actions.sendKeys(Keys.ENTER).build().perform();
        FASession.findElement(SAP_NotificationCode).sendKeys("F071");
        actions.sendKeys(Keys.ENTER).build().perform();
        FASession.findElement(SAP_EquipDown).sendKeys("Yes");
        actions.sendKeys(Keys.ENTER).sendKeys(Keys.TAB).sendKeys(Keys.SPACE).build().perform();
    }

    public void Click_OK_Button() {
        MobileElement OK_Btn1 = FASession.findElement(OK_Btn);
        wait.until((ExpectedCondition<Boolean>) wd -> {
            return wd.findElement(OK_Btn).isEnabled();
        });
        OK_Btn1.click();
    }

    public void Click_Cancel_Button(){
        MobileElement Cancel_Btn1 = FASession.findElement(Cancel_Btn);
        wait.until((ExpectedCondition<Boolean>) wd -> {
            return wd.findElement(Cancel_Btn).isEnabled();
        });
        // Clicks the Cancel button from the Start Workflow dialog.
        Cancel_Btn1.click();
    }

    public void Click_EditCommentDlg_OK_Button() {
        MobileElement OK_Btn1 = FASession.findElement(EditCommentDlg_OkBtn);
        wait.until((ExpectedCondition<Boolean>) wd -> {
            return wd.findElement(EditCommentDlg_OkBtn).isEnabled();
        });
        OK_Btn1.click();
    }

    public void Click_AddWellNote_Button(){
        MobileElement WellNote_Btn1 = FASession.findElement(AddWellNote_Btn);
        wait.until((ExpectedCondition<Boolean>) wd -> {
            return wd.findElement(AddWellNote_Btn).isEnabled();
        });
        WellNote_Btn1.click();
    }

    public void Click_StartWorkflow_Button(){
        FASession.findElement(StartWorkflow_Btn).click();                                                   // Click's the Start Workflow button.
    }

    public void Start_WorkflowDlg_DataFilling(String assmt, String subAssmt, String userGroup, String actn, String SAP, String cmnt){
        FASession.findElement(StartWorkflow_Dlg).isDisplayed();                     // Verifies whether Start Workflow dialog is displayed or not.
        FASession.findElement(Assessments_Category).sendKeys(assmt);          // This will enter/select the Job Type or Initial Assessment data which is collected from data-sheet.
        actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.
        FASession.findElement(Initial_Assessments).sendKeys(subAssmt);                 // This will enter/select the Sub-Assessment data which is collected from data-sheet.
        actions.sendKeys(Keys.ARROW_DOWN).click().build().perform();
        //actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.
        FASession.findElement(AssignToGroup).sendKeys(userGroup);                     // This will enter/select the Assign To data which is collected from data-sheet.
        actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.
        FASession.findElement(WF_Actions).sendKeys(actn);                               // This will enter/select the Action data which is collected from data-sheet.
        actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // This block will tick the Create EAM checkbox if it receives "Yes" and untick if receives "No".
        // Because India team couldn't create EAM jobs and verify them that is why we are unchecking the Create EAM checkbox.
        List<WindowsElement> SAP_Loc = FASession.findElements(SAP_Checkbox);
        if (!SAP_Loc.isEmpty()) {
            assert SAP != null;
            if (SAP.equals("Yes")) {
                String EAM_State = SAP_Loc.getFirst().getAttribute("Toggle.ToggleState");
                if(EAM_State.equals("0")){
                    SAP_Loc.getFirst().click();
                }

            } else if (SAP.equals("No")) {
                String EAM_State = SAP_Loc.getFirst().getAttribute("Toggle.ToggleState");
                if(EAM_State.equals("1")){
                    SAP_Loc.getFirst().click();
                }
            }
        }
        FASession.findElement(CommentsBox).sendKeys(cmnt);                                // Enters the comment into the Start Workflow comment field which is collected from data-sheet
    }

    public void CleanUp_DataTab_Details_Filling(String Resource){
        FASession.findElement(CleanUp_DataTab).click();
        FASession.findElement(CleanUp_DataGrid).isDisplayed();
        MobileElement dataTab = FASession.findElement(CleanUp_DataGrid);
        MobileElement jobDataGrid = dataTab.findElement(Grid_DataTable);
        actions.moveToElement(jobDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).build().perform();
        FASession.findElement(EditSelectedRow_Btn).isEnabled();
        FASession.findElement(EditSelectedRow_Btn).click();
        FASession.findElement(EditJobDataEntry_Dlg).isDisplayed();
        FASession.findElement(ReqResource).sendKeys(Resource);
        FASession.findElement(By.name("OK")).click();
    }

    public void Enter_AddWellNote_Comment(String Wellname, String Comment){
        wait.until((ExpectedCondition<Boolean>) wd -> {
            return wd.findElement(EditComment_Dlg).isDisplayed();
        });
        String dlgWellname = FASession.findElement(EditComment_Dlg).findElement(new MobileBy.ByAccessibilityId("labelWellname")).getAttribute("Name");
        assert dlgWellname.equals(Wellname);
        String cbComment = FASession.findElement(EditComment_Dlg).findElement(new MobileBy.ByAccessibilityId("cbComment")).getAttribute("Toggle.ToggleState");
        if(cbComment.equals("0")){
            FASession.findElement(new MobileBy.ByAccessibilityId("cbComment")).click();
        }
        FASession.findElement(EditCommentDlg_Comment).clear();
        FASession.findElement(EditCommentDlg_Comment).sendKeys(Comment);
    }



}
