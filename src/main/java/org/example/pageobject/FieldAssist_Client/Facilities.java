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

public class Facilities {
    private final WindowsDriver<WindowsElement> FASession;
    private final WebDriverWait wait;
    private final Actions actions;
    Common common;

    public Facilities(WindowsDriver<WindowsElement> _FASession)
    {
        this.FASession = _FASession;
        this.wait = new WebDriverWait(FASession, 20);
        actions = new Actions(FASession);
        common = new Common(FASession);
    }

    private By Equip_Menu = By.name(("Equipment"));
    private By Facilities_Menu = By.name("Facilities");
    private By Fac_FacilitiesTab = By.name("AllWellsHSplt");
    private By Fac_WellsGrid = new MobileBy.ByAccessibilityId("AllWellsHSplt");
    private By Grid_DataTable = MobileBy.AccessibilityId("tableControl1");
    private By SplitPanel2 = new MobileBy.ByAccessibilityId("SplitPanel2");
    private By WFHistoryTabs2 = new MobileBy.ByAccessibilityId("WFHistoryTabs2");
    private By DetailsTab = By.name("HSpltBottomLeft");

    public void Navigate_to_Facilities_Screen() {
        FASession.findElement(Equip_Menu).click();
        FASession.findElement(Facilities_Menu).click();
    }

    public void Select_a_Well_from_FacilitiesScreen(String DwellName) {
        FASession.findElement(Fac_FacilitiesTab).isDisplayed();
        FASession.findElement(Fac_FacilitiesTab).click();
        var facilitiesWells = FASession.findElement(Fac_WellsGrid);
        var wellDataGrid = facilitiesWells.findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(wellDataGrid, DwellName);
    }

    public void Navigate_to_DetailsTab(){
        WindowsElement Fac_Panel2 = (WindowsElement) FASession.findElement(SplitPanel2).findElement(WFHistoryTabs2);
        WindowsElement Fac_DetailsTab = (WindowsElement) Fac_Panel2.findElement(DetailsTab);
        Fac_DetailsTab.isDisplayed();
        Fac_DetailsTab.click();
    }

    public void Validate_FA_Workflow_Created_Or_Not(String assmt, String subAssmt, String actn) {
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Navigate_to_DetailsTab();
        assert subAssmt != null;
        var workflowsGrid = FASession.findElementByName("Workflows");
        var workflowsGridTable = workflowsGrid.findElementByAccessibilityId("tableControl1");
        actions.moveToElement(workflowsGridTable, 15, 40).click().build().perform();
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
            TestCase.fail("Creation of Workflow is unsuccessful");
    }

    public void Verify_Workflow_is_Closed_Successfully() {
        //For validating the workflow is created or not the Workflows child grid should be displayed with StartDate as first column, Initial Assessment as second column and Action as third column.
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Navigate_to_DetailsTab();
        var workflowsGrid = FASession.findElementByName("Workflows");
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
}
