package org.example.pageobject.FieldAssist_Client;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class PAGL {
    private final WindowsDriver<WindowsElement> FASession;
    private final WebDriverWait wait;
    private final Actions actions;
    Common common;

    public PAGL(WindowsDriver<WindowsElement> _FASession) {
        this.FASession = _FASession;
        this.wait = new WebDriverWait(FASession, 20);
        actions = new Actions(FASession);
        common = new Common(FASession);
    }

    private By PAGL_Menu = By.name("PAGL");
    private By PAGLWells_Menu = By.name("PAGL Wells");
    private By PAGL_AllWellsTab = By.name("GLWellsHSpltWells");
    private By PAGL_AllWells_WellsGrid = MobileBy.AccessibilityId("GLWellsHSpltWells");
    private By Grid_DataTable = MobileBy.AccessibilityId("tableControl1");
    private By PAGLWellsSubDetailsTab2 = MobileBy.AccessibilityId("AlarmsTabs");

    public void Navigate_to_PAGLWells__Screen() {
        FASession.findElement(PAGL_Menu).click();
        FASession.findElement(PAGLWells_Menu).click();
    }

    public void Navigate_to_PAGLWells_AllWells_Screen_and_Select_a_Well(String DwellName) {
        Navigate_to_PAGLWells__Screen();
        FASession.findElement(PAGL_AllWellsTab).isDisplayed();
        FASession.findElement(PAGL_AllWellsTab).click();
        MobileElement beamAllWells = FASession.findElement(PAGL_AllWells_WellsGrid);
        MobileElement wellDataGrid = beamAllWells.findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(wellDataGrid,DwellName);
    }

    public void Validate_FA_Workflow_Created_Or_Not(String assmt, String subAssmt, String actn) {
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        assert subAssmt != null;
        var workflowsGrid = FASession.findElementByName("Workflows");
        workflowsGrid.click();
        var workflowsGridTable = workflowsGrid.findElementByAccessibilityId("tableControl1");
        Actions actions = new Actions(FASession);
        actions.click(workflowsGridTable).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();

        actions.sendKeys(Keys.TAB).sendKeys(Keys.F2).build().perform();
        List<MobileElement> initialAssessmentCell = workflowsGridTable.findElementsByXPath("//*");
        String appSubAssessment = initialAssessmentCell.getLast().getAttribute("Value.Value");

        actions.sendKeys(Keys.TAB).sendKeys(Keys.F2).build().perform();
        List<MobileElement> wfActionCell = workflowsGridTable.findElementsByXPath("//*");
        String appWFAction = wfActionCell.getLast().getAttribute("Value.Value");
        if(subAssmt.equals(appSubAssessment) && actn.equals(appWFAction))
        {
            System.out.println("Created Workflow with Assessment -->  " +assmt+ "/" +appSubAssessment+ " with Action -->  " + appWFAction);
        }
        else
            System.out.println("Creation of Workflow is unsuccessful");
    }

    public void Verify_WellNote_is_added_or_not(String Comment){
        MobileElement paglAllWells = FASession.findElement(PAGL_AllWells_WellsGrid);
        MobileElement wellDataGrid = paglAllWells.findElement(Grid_DataTable);
        String appWellNote;
        while (true) {
            List<MobileElement> wNote = wellDataGrid.findElementsByXPath("//*");
            appWellNote = wNote.getLast().getAttribute("Value.Value");
            System.out.println("Info grabbed from selected column is : " + appWellNote);
            if (appWellNote == null) {
                appWellNote = "xyz";
            }
            if (appWellNote.equals(Comment))
                break;
            else {
                wellDataGrid.sendKeys(Keys.TAB);
                wellDataGrid.sendKeys(Keys.F2);
            }
        }
        while (true) {
            List<MobileElement> tabItems = FASession.findElement(PAGLWellsSubDetailsTab2).findElementsByXPath("//TabItem");
            for (MobileElement tabItem : tabItems) {
                String tabName = tabItem.getText();
                if(tabName.equals("ALSExcLowisNotesGrid")) {
                    FASession.findElementByName(tabName).click();
                    MobileElement notesDataGrid = FASession.findElement(PAGLWellsSubDetailsTab2).findElement(Grid_DataTable);
                    actions.moveToElement(notesDataGrid, 15, 40).click().build().perform();
                    actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
//                    FASession.findElement(PAGLWellsSubDetailsTab2).findElement(Grid_DataTable).click();
//                    actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.TAB).build().perform();
                    List<MobileElement> notesComment = FASession.findElement(PAGLWellsSubDetailsTab2).findElement(Grid_DataTable).findElementsByXPath("//*");
                    String gridComment = notesComment.getLast().getAttribute("Value.Value");
                    assert gridComment.equals(Comment);
                    break;
                }
                else{
                    FASession.findElementByName(tabName).click();
                    actions.sendKeys(Keys.ARROW_RIGHT).build().perform();
                }
            }
            break;
        }
    }

    /* ===================================Exceptions Screen ===================================================*/

    // Exception Screen locators and Methods.
    private By ExceptionsTab = By.name("GLWellsMainExcTabs");
    private By Exc_CurrentTab = By.name("GLWellsExcCurrHSplt");
    private By Exc_CurrentGrid = MobileBy.AccessibilityId("GLWellsExcCurrHSplt");
    private By Exc_DeferredTab = By.name("GLWellsExcDefHSpltTab");
    private By Exc_DeferredGrid = MobileBy.AccessibilityId("GLWellsExcDefHSpltTab");



    public void Navigate_to_PAGL_ExcCurrentGrid_and_Select_a_Well(String DWellName) {
        FASession.findElement(ExceptionsTab).isDisplayed();
        FASession.findElement(ExceptionsTab).click();
        FASession.findElement(Exc_CurrentTab).click();
        MobileElement excCurrentWells = FASession.findElement(Exc_CurrentGrid);
        MobileElement wellDataGrid = excCurrentWells.findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(wellDataGrid, DWellName);
    }

    public void Navigate_to_ExcDeferredTab_and_Select_a_Well(String DWellName){
        FASession.findElement(ExceptionsTab).isDisplayed();
        FASession.findElement(ExceptionsTab).click();
        FASession.findElement(Exc_DeferredTab).isDisplayed();
        FASession.findElement(Exc_DeferredTab).click();
        MobileElement excDefWells = FASession.findElement(Exc_DeferredGrid);
        MobileElement wellDataGrid = excDefWells.findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(wellDataGrid, DWellName);
    }

    public void Verify_Defer_Workflow_is_Created_or_Not(String Category){
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var workflowsGrid = FASession.findElementByName("Workflows");
        workflowsGrid.click();
        var workflowsGridTable = workflowsGrid.findElementByAccessibilityId("tableControl1");
        Actions actions = new Actions(FASession);
        actions.click(workflowsGridTable).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();

        workflowsGridTable.sendKeys(Keys.TAB);
        List<MobileElement> initialAssessmentCell = workflowsGridTable.findElementsByXPath("//*");
        String appSubAssessment = initialAssessmentCell.getLast().getAttribute("Value.Value");

        workflowsGridTable.sendKeys(Keys.TAB);
        List<MobileElement> wfActionCell = workflowsGridTable.findElementsByXPath("//*");
        String appWFAction = wfActionCell.getLast().getAttribute("Value.Value");

        if(Category.equals(appSubAssessment) && "Defer".equals(appWFAction))
        {
            System.out.println("Created Workflow with Assessment -->  FOP - ALS FOP " +appSubAssessment);
        }
        else
            System.out.println("Creation of Workflow is unsuccessful");
    }

    public void Verify_Workflow_is_Undeferred(String DWellName){

        FASession.findElement(ExceptionsTab).isDisplayed();
        FASession.findElement(ExceptionsTab).click();
        FASession.findElement(Exc_CurrentTab).click();
        FASession.findElementByName("toolStripButton_Refresh").click();
        MobileElement excCurrentWells = FASession.findElement(Exc_CurrentGrid);
        MobileElement wellDataGrid = excCurrentWells.findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(wellDataGrid, DWellName);
        var workflowsGrid = FASession.findElementByName("Workflows");
        var workflowsGridTable = workflowsGrid.findElementByAccessibilityId("tableControl1");
        actions.moveToElement(workflowsGrid, 15, 40).click().build().perform();

        while (true) {
            workflowsGridTable.sendKeys(Keys.TAB);
            List<MobileElement> finalAssessment = workflowsGridTable.findElementsByXPath("//*");
            String finalResolution = finalAssessment.getLast().getAttribute("Value.Value");
            if(finalResolution.equals("Successfully Completed")){
                System.out.println("Workflow complete with Final Assessment : "+finalResolution);
                break;
            }
        }
    }

    public void Verify_Workflow_is_Closed_Successfully() {
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        var workflowsGrid = FASession.findElementByName("Workflows");
        var workflowsGridTable = workflowsGrid.findElementByAccessibilityId("tableControl1");
        //actions.click(workflowsGridTable).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        actions.moveToElement(workflowsGridTable, 15, 40).click().build().perform();
        while (true) {
            workflowsGridTable.sendKeys(Keys.TAB);
            workflowsGridTable.sendKeys(Keys.F2);
            List<MobileElement> finalAssessment = workflowsGridTable.findElementsByXPath("//*");
            String finalResolution = finalAssessment.getLast().getAttribute("Value.Value");
            if(finalResolution.equals("Successfully Completed")){
                System.out.println("Workflow completed with Final Assessment : "+finalResolution);
                break;
            }
        }
    }
}
