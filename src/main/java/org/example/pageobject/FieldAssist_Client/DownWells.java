package org.example.pageobject.FieldAssist_Client;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

public class DownWells {
    private final WindowsDriver<WindowsElement> FASession;
    private final WebDriverWait wait;
    private final Actions actions;
    Common common;

    public DownWells(WindowsDriver<WindowsElement> _FASession) {
        this.FASession = _FASession;
        this.wait = new WebDriverWait(FASession, 20);
        actions = new Actions(FASession);
        common = new Common(FASession);
    }

    private By Jobs_Menu = By.name("Jobs");
    private By DW_Menu = By.name("Down Wells");
    private By DW_WellsTab = By.name("Down Wells");
    private By DW_WellsGrid = MobileBy.AccessibilityId("PSGrid");
    private By Grid_DataTable = MobileBy.AccessibilityId("tableControl1");
    private By WellMgmtChildTabs1 = MobileBy.AccessibilityId("WellMgmtChildTabs1");
    private By DW_Details_TabItems = By.xpath("//Tab[@AutomationId=\"WellMgmtChildTabs1\"]/TabItem");


    public void Navigate_to_DownWells_AllWells_Screen_and_Select_a_Well(String DwellName) {
        FASession.findElement(Jobs_Menu).click();
        FASession.findElement(DW_Menu).click();
        FASession.findElement(DW_WellsTab).isDisplayed();
        MobileElement downAllWells = FASession.findElement(DW_WellsGrid);
        try{
            Thread.sleep(5000);
        }
        catch (InterruptedException e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
        downAllWells.findElement(Grid_DataTable).isDisplayed();
        MobileElement wellDataGrid = downAllWells.findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(wellDataGrid,DwellName);
    }

    public void Select_WorkflowType(String WorkflowType ){
        List<WindowsElement> workflowType = FASession.findElementsByAccessibilityId("WorkflowTypeSelectorDlg");
        if (!workflowType.isEmpty()) {
            workflowType.getFirst().findElementByName(WorkflowType).click();
        }
    }

    public void Validate_FA_DownWell_Workflow_Created_Or_Not(String assmt, String subAssmt, String actn) {
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String WorkflowsGridName = "";
        FASession.findElement(By.name("WellMgmtJobsChildTabs")).click();
        List<WindowsElement> downWFGrid = FASession.findElementsByAccessibilityId("WellMgmtWFGridHSplt");

        if (!downWFGrid.isEmpty() && downWFGrid.getFirst().isDisplayed()) {
            WorkflowsGridName = downWFGrid.getFirst().getAttribute("Name");
        }

        System.out.println("Workflows grid name collected is : " + WorkflowsGridName);
        assert subAssmt != null;

        var workflowsGrid = FASession.findElementByName(WorkflowsGridName);
        MobileElement workflowsGridTable = workflowsGrid.findElementByAccessibilityId("HistWorkflowForWellGrid").findElement(Grid_DataTable);
        actions.moveByOffset(10,90).click().build().perform();
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

    public void Verify_Workflow_is_Closed_Successfully(){
        List<WindowsElement> tabItems = FASession.findElements(DW_Details_TabItems);
        int tabs_size = tabItems.size();
        for (int i = 0; i < tabs_size; i++) {
            List<WindowsElement> jobsGrid = FASession.findElementsByName("WellMgmtJobsChildTabs");
            if(!jobsGrid.isEmpty() && jobsGrid.getFirst().isDisplayed()){
                jobsGrid.getFirst().click();
                break;
            }
            else {
                actions.sendKeys(Keys.ARROW_LEFT).build().perform();
            }
        }

        String WorkflowsGridName = "";
        List<WindowsElement> downWFGrid = FASession.findElementsByAccessibilityId("WellMgmtWFGridHSplt");

        if (!downWFGrid.isEmpty() && downWFGrid.getFirst().isDisplayed()) {
            WorkflowsGridName = downWFGrid.getFirst().getAttribute("Name");
        }

        System.out.println("Workflows grid name collected is : " + WorkflowsGridName);
        var workflowsGrid = FASession.findElementByName(WorkflowsGridName);
        var workflowsGridTable = workflowsGrid.findElementByAccessibilityId("tableControl1");
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

    public void Verify_WellNote_is_added_or_not(String Comment){
        MobileElement beamAllWells = FASession.findElement(DW_WellsGrid);
        MobileElement wellDataGrid = beamAllWells.findElement(Grid_DataTable);
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

            List<WindowsElement> tabItems = FASession.findElements(By.xpath("//Tab[@AutomationId=\"WellMgmtChildTabs1\"]/TabItem"));
            for (WindowsElement tabItem : tabItems) {
                String tabName = tabItem.getText();
                if(tabName.equals("WellMgmtNotesGrid")) {
                    FASession.findElementByName(tabName).click();
                    MobileElement notesDataGrid = FASession.findElement(WellMgmtChildTabs1).findElement(Grid_DataTable);
                    actions.moveToElement(notesDataGrid, 15, 40).click().build().perform();
                    actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                    List<MobileElement> notesComment = FASession.findElement(WellMgmtChildTabs1).findElement(Grid_DataTable).findElementsByXPath("//*");
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

}
