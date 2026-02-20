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
import utils.FAUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NFWells {
    private final WindowsDriver<WindowsElement> FASession;
    private final WebDriverWait wait;
    private final Actions actions;
    Common common;

    public NFWells(WindowsDriver<WindowsElement> _FASession) {
        this.FASession = _FASession;
        this.wait = new WebDriverWait(FASession, 20);
        actions = new Actions(FASession);
        common = new Common(FASession);
    }


    private By NF_Menu = By.name("NF");
    private By NFWells_Menu = By.name("NF Wells");
    private By NF_AllWellsTab = By.name("BeamStatAllWellsHSplt");
    private By NF_AllWells_WellsGrid = MobileBy.AccessibilityId("FlowingWellsGrid");
    private By Grid_DataTable = MobileBy.AccessibilityId("tableControl1");
    private By NF_Notes_n_Alarms_Grid = MobileBy.AccessibilityId("BeamStatAlarmTabs");
    private By NF_WFGrid = MobileBy.AccessibilityId("HistWorkflowForWellGrid");

    public void Navigate_to_NFWells_AllWells_Screen_and_Select_a_Well(String DwellName) {
        FASession.findElement(NF_Menu).click();
        FASession.findElement(NFWells_Menu).click();
        FASession.findElement(NF_AllWellsTab).isDisplayed();
        FASession.findElement(NF_AllWellsTab).click();
        MobileElement nfAllWells = FASession.findElement(NF_AllWells_WellsGrid);
        MobileElement wellDataGrid = nfAllWells.findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(wellDataGrid,DwellName);
    }

    public void Validate_FA_NFWell_Workflow_Created_Or_Not(String assmt, String subAssmt, String actn) {
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String WorkflowsGridName = "";
        List<WindowsElement> nfWFGrid = FASession.findElements(NF_WFGrid);

        if (!nfWFGrid.isEmpty() && nfWFGrid.getFirst().isDisplayed()) {
            WorkflowsGridName = nfWFGrid.getFirst().getAttribute("Name");
        }

        System.out.println("Workflows grid name collected is : " + WorkflowsGridName);
        assert subAssmt != null;
        var workflowsGrid = FASession.findElementByName(WorkflowsGridName);
        //workflowsGrid.click();
        var workflowsGridTable = workflowsGrid.findElementByAccessibilityId("tableControl1");
        //actions.click(workflowsGridTable).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        actions.moveToElement(workflowsGridTable, 10, 40).click().sendKeys(Keys.TAB).sendKeys(Keys.F2).build().perform();
        //workflowsGridTable.sendKeys(Keys.TAB);
        List<MobileElement> initialAssessmentCell = workflowsGridTable.findElementsByXPath("//*");
        String appSubAssessment = initialAssessmentCell.getLast().getAttribute("Value.Value");

        workflowsGridTable.sendKeys(Keys.TAB);
        workflowsGridTable.sendKeys(Keys.F2);
        List<MobileElement> wfActionCell = workflowsGridTable.findElementsByXPath("//*");
        String appWFAction = wfActionCell.getLast().getAttribute("Value.Value");
        if(subAssmt.equals(appSubAssessment) && actn.equals(appWFAction))
        {
            System.out.println("Created Workflow with Assessment -->  " +assmt+ "/" +appSubAssessment+ " with Action -->  " + appWFAction);
        }
        else
            System.out.println("Creation of Workflow is unsuccessful");
    }

    public void Verify_Workflow_is_Closed_Successfully() {
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String WorkflowsGridName = "";
        List<WindowsElement> nfWFGrid = FASession.findElements(NF_WFGrid);

        if (!nfWFGrid.isEmpty() && nfWFGrid.getFirst().isDisplayed()) {
            WorkflowsGridName = nfWFGrid.getFirst().getAttribute("Name");
        }

        System.out.println("Workflows grid name collected is : " + WorkflowsGridName);
        var workflowsGrid = FASession.findElementByName(WorkflowsGridName);
        var workflowsGridTable = workflowsGrid.findElement(Grid_DataTable);
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

    public void Verify_WellNote_is_added_or_not(String Comment){
        MobileElement nfAllWells = FASession.findElement(NF_AllWells_WellsGrid);
        MobileElement wellDataGrid = nfAllWells.findElement(Grid_DataTable);
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

            List<WindowsElement> tabItems = FASession.findElements(By.xpath("//Tab[@AutomationId=\"BeamStatAlarmTabs\"]/TabItem"));
            for (WindowsElement tabItem : tabItems) {
                String tabName = tabItem.getText();
                if(tabName.equals("ALSExcLowisNotesGrid")) {
                    FASession.findElementByName(tabName).click();
                    MobileElement notesDataGrid = FASession.findElement(NF_Notes_n_Alarms_Grid).findElement(Grid_DataTable);
                    actions.moveToElement(notesDataGrid, 15, 40).click().build().perform();
                    actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                    List<MobileElement> notesComment = FASession.findElement(NF_Notes_n_Alarms_Grid).findElement(Grid_DataTable).findElementsByXPath("//*");
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
