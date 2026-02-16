package org.example.pageobject.FieldAssist_Client;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import junit.framework.TestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeamWells {
    private final WindowsDriver<WindowsElement> FASession;
    private final WebDriverWait wait;
    private final Actions actions;
    Common common;

    public BeamWells(WindowsDriver<WindowsElement> _FASession) {
        this.FASession = _FASession;
        this.wait = new WebDriverWait(FASession, 20);
        actions = new Actions(FASession);
        common = new Common(FASession);
    }

    private By RodPump_Menu = By.name("Rod Pump");
    private By BW_Menu = By.name("Beam Wells");
    private By BW_AllWellsTab = By.name("BeamStatAllWellsHSplt");
    private By BW_AllWells_WellsGrid = MobileBy.AccessibilityId("BeamStatAllWellsHSplt");
    private By Grid_DataTable = MobileBy.AccessibilityId("tableControl1");
    private By BeamStatDetailsChildTabs = MobileBy.AccessibilityId("BeamStatDetailsChildTabs");
    private By BW_Details_TabItems = By.xpath("//Tab[@AutomationId=\"BeamStatDetailsChildTabs\"]/TabItem");


    public void Navigate_to_BeamWells_Screen() {
        FASession.findElement(RodPump_Menu).click();
        FASession.findElement(BW_Menu).click();
    }

    public void Navigate_to_BeamWells_AllWells_Screen_and_Select_a_Well(String DwellName) {
        Navigate_to_BeamWells_Screen();
        FASession.findElement(BW_AllWellsTab).isDisplayed();
        FASession.findElement(BW_AllWellsTab).click();
        MobileElement beamAllWells = FASession.findElement(BW_AllWells_WellsGrid);
        MobileElement wellDataGrid = beamAllWells.findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(wellDataGrid, DwellName);                                                                                                      //*******************---------------------------------------------------------------***************************
    }

    public void Select_WorkflowType(String WorkflowType ){
        var workflowType = FASession.findElementByAccessibilityId("WorkflowTypeSelectorDlg");
        workflowType.findElementByName(WorkflowType).click();
    }

    public void Validate_FA_BeamWell_Workflow_Created_Or_Not(String assmt, String subAssmt, String actn) {
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<WindowsElement> tabItems = FASession.findElements(BW_Details_TabItems);
        int tabs_size = tabItems.size();
        for (int i = 0; i < tabs_size; i++) {
            List<WindowsElement> jobsGrid = FASession.findElementsByName("BeamStatJobsTabs");
            if(!jobsGrid.isEmpty() && jobsGrid.getFirst().isDisplayed()){
                jobsGrid.getFirst().click();
                break;
            }
            else {
                actions.sendKeys(Keys.ARROW_LEFT).build().perform();
            }
        }

        String WorkflowsGridName = "";
        List<WindowsElement> beamWFGrid = FASession.findElementsByAccessibilityId("BeamStatWFGridHSplt");

        if (!beamWFGrid.isEmpty() && beamWFGrid.getFirst().isDisplayed()) {
            WorkflowsGridName = beamWFGrid.getFirst().getAttribute("Name");
        }

        System.out.println("Workflows grid name collected is : " + WorkflowsGridName);
        assert subAssmt != null;
        var workflowsGrid = FASession.findElementByName(WorkflowsGridName);
        var workflowsGridTable = workflowsGrid.findElementByAccessibilityId("tableControl1");
        //actions.click(workflowsGridTable).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        actions.moveToElement(workflowsGridTable, 15, 40).click().build().perform();
        workflowsGridTable.sendKeys(Keys.TAB);
        workflowsGridTable.sendKeys(Keys.F2);
        List<MobileElement> initialAssessmentCell = workflowsGridTable.findElementsByXPath("//*");
        String appSubAssessment = initialAssessmentCell.getLast().getAttribute("Value.Value");
        workflowsGridTable.sendKeys(Keys.TAB);
        workflowsGridTable.sendKeys(Keys.F2);
        List<MobileElement> wfActionCell = workflowsGridTable.findElementsByXPath("//*");
        String appWFAction = wfActionCell.getLast().getAttribute("Value.Value");
        System.out.println("Collected Sub Assessment is : "+appSubAssessment+" and WFAction is : " + appWFAction);
        if(subAssmt.equals(appSubAssessment) && actn.equals(appWFAction))
        {
            System.out.println("Created Workflow with Assessment -->  " +assmt+ "/" +appSubAssessment+ " with Action -->  " + appWFAction);
        }
        else {
            TestCase.fail("Creation of Workflow is unsuccessful");
        }
    }

    public void Verify_Workflow_is_Closed_Successfully(){
        //Navigate_to_BeamWells_AllWells_Screen_and_Select_a_Well(DwellName);
        List<WindowsElement> tabItems = FASession.findElements(BW_Details_TabItems);
        int tabs_size = tabItems.size();
        for (int i = 0; i < tabs_size; i++) {
            List<WindowsElement> jobsGrid = FASession.findElementsByName("BeamStatJobsTabs");
            if(!jobsGrid.isEmpty() && jobsGrid.getFirst().isDisplayed()){
                jobsGrid.getFirst().click();
                break;
            }
            else {
                actions.sendKeys(Keys.ARROW_LEFT).build().perform();
            }
        }

        String WorkflowsGridName = "";
        List<WindowsElement> beamWFGrid = FASession.findElementsByAccessibilityId("BeamStatWFGridHSplt");

        if (!beamWFGrid.isEmpty() && beamWFGrid.getFirst().isDisplayed()) {
            WorkflowsGridName = beamWFGrid.getFirst().getAttribute("Name");
        }

        System.out.println("Workflows grid name collected is : " + WorkflowsGridName);
        var workflowsGrid = FASession.findElementByName(WorkflowsGridName);
        var workflowsGridTable = workflowsGrid.findElementByAccessibilityId("tableControl1");
        actions.moveToElement(workflowsGridTable, 15, 40).click().build().perform();
        while (true) {
            workflowsGridTable.sendKeys(Keys.TAB);
            List<MobileElement> finalAssessment = workflowsGridTable.findElementsByXPath("//*");
            String finalResolution = finalAssessment.getLast().getAttribute("Value.Value");
            if(finalResolution.equals("Successfully Completed")){
                System.out.println("Workflow completed with Final Assessment : "+finalResolution);
                break;
            }
        }
    }

    public void Verify_WellNote_is_added_or_not(String Comment){
        MobileElement beamAllWells = FASession.findElement(BW_AllWells_WellsGrid);
        MobileElement wellDataGrid = beamAllWells.findElement(Grid_DataTable);
        String appWellNote;
        while (true) {
            List<MobileElement> wNote = wellDataGrid.findElementsByXPath("//*");
            appWellNote = wNote.getLast().getAttribute("Value.Value");

            if (appWellNote == null) {
                appWellNote = "xyz";
            }
            if (appWellNote.equals(Comment)) {
                System.out.println("Info grabbed from selected column is : " + appWellNote);
                break;
            }
            else {
                wellDataGrid.sendKeys(Keys.TAB);
                wellDataGrid.sendKeys(Keys.F2);
            }
        }
        while (true) {

            List<WindowsElement> tabItems = FASession.findElements(BW_Details_TabItems);
            for (WindowsElement tabItem : tabItems) {
                String tabName = tabItem.getText();
                if(tabName.equals("ALSExcLowisNotesGrid")) {
                    FASession.findElementByName(tabName).click();
                    MobileElement notesDataGrid = FASession.findElement(BeamStatDetailsChildTabs).findElement(Grid_DataTable);
                    actions.moveToElement(notesDataGrid, 15, 40).click().build().perform();
                    actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                    List<MobileElement> notesComment = FASession.findElement(BeamStatDetailsChildTabs).findElement(Grid_DataTable).findElementsByXPath("//*");
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
    private By ExceptionsTab = By.name("BeamStatMainExcTabs");
    private By Exc_CurrentTab = By.name("BeamStatExcCurrHSplt");
    private By Exc_CurrentGrid = MobileBy.AccessibilityId("BeamStatExcCurrHSplt");
    private By Exc_DeferredTab = By.name("BeamStatExcDefHSpltTab");
    private By Exc_DeferredGrid = MobileBy.AccessibilityId("BeamStatExcDefHSpltTab");



    public void Navigate_to_BW_ExcCurrentGrid_and_Select_a_Well(String DWellName) {
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
        List<WindowsElement> tabItems = FASession.findElements(BW_Details_TabItems);
        int tabs_size = tabItems.size();
        for (int i = 0; i < tabs_size; i++) {
            List<WindowsElement> jobsGrid = FASession.findElementsByName("BeamStatJobsTabs");
            if(!jobsGrid.isEmpty() && jobsGrid.getFirst().isDisplayed()){
                jobsGrid.getFirst().click();
                break;
            }
            else {
                actions.sendKeys(Keys.ARROW_LEFT).build().perform();
            }
        }

        String WorkflowsGridName = "";
        List<WindowsElement> beamWFGrid = FASession.findElementsByAccessibilityId("BeamStatWFGridHSplt");

        if (!beamWFGrid.isEmpty() && beamWFGrid.getFirst().isDisplayed()) {
            WorkflowsGridName = beamWFGrid.getFirst().getAttribute("Name");
        }

        System.out.println("Workflows grid name collected is : " + WorkflowsGridName);
        assert Category != null;
        var workflowsGrid = FASession.findElementByName(WorkflowsGridName);
        workflowsGrid.click();
        var workflowsGridTable = workflowsGrid.findElementByAccessibilityId("tableControl1");
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


}
