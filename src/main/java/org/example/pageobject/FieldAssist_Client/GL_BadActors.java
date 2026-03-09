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

import java.util.List;

public class GL_BadActors {
    private final WindowsDriver<WindowsElement> FASession;
    private final WebDriverWait wait;
    private final Actions actions;
    Common common;

    public GL_BadActors(WindowsDriver<WindowsElement> _FASession) {
        this.FASession = _FASession;
        this.wait = new WebDriverWait(FASession, 20);
        actions = new Actions(FASession);
        common = new Common(FASession);
    }

    private By GL_Menu = By.name("Gas Lift");
    private By GLBadActors_Menu = By.name("Bad Actors");
    private By GL_Exceptions_WellsGrid = By.name("Exceptions");
    private By GL_Rev_Def_WellsGrid = By.name("Reviewed / Deferred");
    private By Grid_DataTable = MobileBy.AccessibilityId("tableControl1");
    private By GLBadActors_WFGrid = MobileBy.AccessibilityId("HistWorkflowForWellGrid");

    public void Navigate_to_GL_BadActors_Screen() {
        FASession.findElement(GL_Menu).click();
        FASession.findElement(GLBadActors_Menu).click();
    }

    public void Select_Well_from_ExceptionsGrid(String DwellName){

        MobileElement fopWells = FASession.findElement(GL_Exceptions_WellsGrid);
        MobileElement wellDataGrid = fopWells.findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(wellDataGrid, DwellName);                                                                                                           //*******************---------------------------------------------------------------***************************
    }

    public void Validate_FA_GL_BadActors_Workflow_Created_Or_Not(String assmt, String subAssmt, String actn) {
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String WorkflowsGridName = "";
        List<WindowsElement> beamWFGrid = FASession.findElements(GLBadActors_WFGrid);

        if (!beamWFGrid.isEmpty() && beamWFGrid.getFirst().isDisplayed()) {
            WorkflowsGridName = beamWFGrid.getFirst().getAttribute("Name");
        }

        System.out.println("Workflows grid name collected is : " + WorkflowsGridName);
        assert subAssmt != null;
        var workflowsGrid = FASession.findElementByName(WorkflowsGridName);
        workflowsGrid.click();
        var workflowsGridTable = workflowsGrid.findElement(Grid_DataTable);
        actions.click(workflowsGridTable).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();

        workflowsGridTable.sendKeys(Keys.TAB);
        List<MobileElement> initialAssessmentCell = workflowsGridTable.findElementsByXPath("//*");
        String appSubAssessment = initialAssessmentCell.getLast().getAttribute("Value.Value");

        workflowsGridTable.sendKeys(Keys.TAB);
        List<MobileElement> wfActionCell = workflowsGridTable.findElementsByXPath("//*");
        String appWFAction = wfActionCell.getLast().getAttribute("Value.Value");
        if(subAssmt.equals(appSubAssessment) && actn.equals(appWFAction))
        {
            System.out.println("Created Workflow with Assessment -->  " +assmt+ "/" +appSubAssessment+ " with Action -->  " + appWFAction);
        }
        else {
            TestCase.fail("Creation or Validation of Workflow is unsuccessful");
        }
    }

    public void Verify_Workflow_is_Closed_Successfully() {
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String WorkflowsGridName = "";
        List<WindowsElement> beamWFGrid = FASession.findElements(GLBadActors_WFGrid);

        if (!beamWFGrid.isEmpty() && beamWFGrid.getFirst().isDisplayed()) {
            WorkflowsGridName = beamWFGrid.getFirst().getAttribute("Name");
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

    public void Select_Well_from_Reviewed_Tab(String DwellName){
        MobileElement reviewedGrid = FASession.findElement(GL_Rev_Def_WellsGrid);
        MobileElement wellDataGrid = reviewedGrid.findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(wellDataGrid, DwellName);
    }

    public void Verify_well_moved_to_ReviewedGrid(String DwellName){
        Select_Well_from_Reviewed_Tab(DwellName);
    }

    public void Verify_well_moved_to_ExceptionsGrid(String DWellName){
        Select_Well_from_ExceptionsGrid(DWellName);
    }
}
