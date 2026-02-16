package dataDrivenTests;

import TestUtils.FABaseTest;
import io.appium.java_client.MobileElement;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.pageobject.FieldAssist_Client.BeamWells;
import org.example.pageobject.FieldAssist_Client.ESP;
import org.example.pageobject.FieldAssist_Client.GasLift;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FA_DataDrivenTests extends FABaseTest {

    private BeamWells beamWells;
    private ESP espWells;
    private GasLift glWells;
    @BeforeMethod
    public void setupPageObject(){
        beamWells = new BeamWells(FASession);
        espWells = new ESP(FASession);
        glWells = new GasLift(FASession);
    }

    @Test(priority = 2)
    public void ESPWellsWF() throws IOException {
        File src = new File(".\\Datafiles\\Demo.xlsx");
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(src);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        XSSFSheet sheet = workbook.getSheet("ESP");
        FASession.findElement(By.name("ESP")).click();
        FASession.findElement(By.name("ESP Wells")).click();
        FASession.findElement(By.name("ESPWellsHSpltWells")).isDisplayed();

        var wellDataGrid = FASession.findElementByAccessibilityId("tableControl1");
        Actions actions = new Actions(FASession);
        //actions.click(wellDataGrid).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.F2).build().perform();
        actions.moveToElement(wellDataGrid, 5, 5).click().build().perform();

        for(int counter = 1; counter <= sheet.getLastRowNum(); counter++) {

            XSSFCell cell = sheet.getRow(counter).getCell(0);
            String DwellName = null;
            if (cell.getCellType() == CellType.STRING) {
                DwellName = cell.getStringCellValue();
            }
            XSSFCell cell1 = sheet.getRow(counter).getCell(1);
            String assmt = null;
            if (cell1.getCellType() == CellType.STRING) {
                assmt = cell1.getStringCellValue();
            }
            XSSFCell cell2 = sheet.getRow(counter).getCell(2);
            String subAssmt = null;
            if (cell2.getCellType() == CellType.STRING) {
                subAssmt = cell2.getStringCellValue();
            }
            XSSFCell cell3 = sheet.getRow(counter).getCell(3);
            String userGroup = null;
            if (cell3.getCellType() == CellType.STRING) {
                userGroup = cell3.getStringCellValue();
            }
            XSSFCell cell4 = sheet.getRow(counter).getCell(4);
            String actn = null;
            if (cell4.getCellType() == CellType.STRING) {
                actn = cell4.getStringCellValue();
            }
            XSSFCell cell5 = sheet.getRow(counter).getCell(5);
            String eam = null;
            if (cell5.getCellType() == CellType.STRING) {
                eam = cell5.getStringCellValue();
            }
            XSSFCell cell6 = sheet.getRow(counter).getCell(6);
            String cmnt = null;
            if (cell6.getCellType() == CellType.STRING) {
                cmnt = cell6.getStringCellValue();
            }

            //Verifying the well names in application to select the one provided in data sheet.
            String appWellName;
            while (true) {
                List<MobileElement> wName = wellDataGrid.findElementsByXPath("//*");
                appWellName = wName.getLast().getAttribute("Value.Value");
                //System.out.println("Well selected is : " + appWellName);
                if (appWellName.equals(DwellName))
                    break;
                else {
                    wellDataGrid.sendKeys(Keys.ARROW_DOWN);
                    wellDataGrid.sendKeys(Keys.F2);
                }
            }

            //Launching the Start Workflow dialog and filling the details for creating a workflow.
            FASession.findElement(By.name("Start Workflow")).click();
            FASession.findElementByAccessibilityId("CreateWorkflowDlgOpsComm").isDisplayed();
            FASession.findElementByAccessibilityId("_comboInitialAssessmentCategories").sendKeys(assmt);
            actions.sendKeys(Keys.TAB).build().perform();
            FASession.findElementByAccessibilityId("_comboInitialAssessment").sendKeys(subAssmt);
            actions.sendKeys(Keys.TAB).build().perform();
            FASession.findElementByAccessibilityId("comboAssignToGroup").sendKeys(userGroup);
            actions.sendKeys(Keys.TAB).build().perform();
            FASession.findElementByAccessibilityId("_comboActions").sendKeys(actn);
            actions.sendKeys(Keys.TAB).build().perform();

            assert eam != null;
            if (eam.equals("Yes")) {
                FASession.findElement(By.name("Create EAM")).click();
            }

            FASession.findElementByAccessibilityId("_textComment").sendKeys(cmnt);
            FASession.findElementByName("OK").click();
            //FASession.findElementByName("Cancel").click();
            //System.out.println("Workflow created : " + DwellName + " -- " + assmt + " -- " + subAssmt + " -- " + actn + " -- " + cmnt);
            String WorkflowsGridName = "Workflows";
            assert subAssmt != null;
            espWells.Validate_FA_Workflow_Created_Or_Not(WorkflowsGridName, subAssmt, actn);
            wellDataGrid.sendKeys(Keys.ARROW_DOWN);
            wellDataGrid.sendKeys(Keys.F2);
        }
        workbook.close();
    }

    @Test (priority = 3)
    public void GasLiftWellsWF() throws IOException, InterruptedException {

        BeamWells faClient = new BeamWells(FASession);
        File src = new File(".\\Datafiles\\Demo.xlsx");
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(src);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        XSSFSheet sheet = workbook.getSheet("Gas Lift");
        FASession.findElement(By.name("Gas Lift")).click();
        FASession.findElement(By.name("Gas Lift Wells")).click();
        var glAllWells = FASession.findElement(By.name("GLWellsHSpltWells"));
        glAllWells.isDisplayed();
        var wellDataGrid = FASession.findElementByAccessibilityId("tableControl1");
        Actions actions = new Actions(FASession);
        actions.moveToElement(wellDataGrid, 5, 5).click().build().perform();
        // This for loop will collect data from data-sheet and stores them into the variables provided accordingly

        for(int counter = 1; counter <= sheet.getLastRowNum(); counter++) {
            // actions.click(wellDataGrid).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.F2).build().perform();
            XSSFCell cell = sheet.getRow(counter).getCell(0);
            String DwellName = null;
            if (cell.getCellType() == CellType.STRING) {
                DwellName = cell.getStringCellValue();
            }
            XSSFCell cell1 = sheet.getRow(counter).getCell(1);
            String assmt = null;
            if (cell1.getCellType() == CellType.STRING) {
                assmt = cell1.getStringCellValue();
            }
            XSSFCell cell2 = sheet.getRow(counter).getCell(2);
            String subAssmt = null;
            if (cell2.getCellType() == CellType.STRING) {
                subAssmt = cell2.getStringCellValue();
            }
            XSSFCell cell3 = sheet.getRow(counter).getCell(3);
            String userGroup = null;
            if (cell3.getCellType() == CellType.STRING) {
                userGroup = cell3.getStringCellValue();
            }
            XSSFCell cell4 = sheet.getRow(counter).getCell(4);
            String actn = null;
            if (cell4.getCellType() == CellType.STRING) {
                actn = cell4.getStringCellValue();
            }
            XSSFCell cell5 = sheet.getRow(counter).getCell(5);
            String eam = null;
            if (cell5.getCellType() == CellType.STRING) {
                eam = cell5.getStringCellValue();
            }
            XSSFCell cell6 = sheet.getRow(counter).getCell(6);
            String cmnt = null;
            if (cell6.getCellType() == CellType.STRING) {
                cmnt = cell6.getStringCellValue();
            }

            //Verifying the well names in application to select the one provided in data sheet.
            String appWellName;                                                                                         //*******************---------------------------------------------------------------********************
            while (true) {                                                                                              //
                List<MobileElement> wName = wellDataGrid.findElementsByXPath("//*");                              //
                appWellName = wName.getLast().getAttribute("Value.Value");                                        //
                System.out.println("Well selected is : " + appWellName);                                                //  This block will compare the Wellname collected from Application and Wellname collected from data-sheet
                if (appWellName.equals(DwellName))                                                                      //              If both are equal then it'll break the loop
                    break;                                                                                              //              And continue for clicking Start Workflow button and further process.
                else {                                                                                                  //      If the wellname's are not equal then it'll press the down arrow from the currently selected well
                    wellDataGrid.sendKeys(Keys.ARROW_DOWN);                                                             //                      and verifies that wellname against the wellname collected from data-sheet.
                    wellDataGrid.sendKeys(Keys.F2);                                                                     //
                }                                                                                                       //
            }                                                                                                           //*******************---------------------------------------------------------------********************

            //Launching the Start Workflow dialog and fill details in the data fields for creating a workflow.

            FASession.findElement(By.name("Start Workflow")).click();                                                   // Click's the Start Workflow button.
            FASession.findElementByAccessibilityId("CreateWorkflowDlgOpsComm").isDisplayed();                     // Verifies whether Start Workflow dialog is displayed or not.
            FASession.findElementByAccessibilityId("_comboInitialAssessmentCategories").sendKeys(assmt);          // This will enter/select the Job Type or Initial Assessment data which is collected from data-sheet.
            actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.
            FASession.findElementByAccessibilityId("_comboInitialAssessment").sendKeys(subAssmt);                 // This will enter/select the Sub-Assessment data which is collected from data-sheet.
            actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.
            FASession.findElementByAccessibilityId("comboAssignToGroup").sendKeys(userGroup);                     // This will enter/select the Assign To data which is collected from data-sheet.
            actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.
            FASession.findElementByAccessibilityId("_comboActions").sendKeys(actn);                               // This will enter/select the Action data which is collected from data-sheet.
            actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.

            assert eam != null;                                                                                         //
            if (eam.equals("Yes")) {                                                                                    // This block will un-tick the Create EAM checkbox if it receives "yes" from the data-sheet.
                FASession.findElement(By.name("Create EAM")).click();                                                   // Because India team couldn't create EAM jobs and verify them that is why we are unchecking the Create EAM checkbox.
            }                                                                                                           //

            FASession.findElementByAccessibilityId("_textComment").sendKeys(cmnt);                                // Enters the comment into the Start Workflow comment field which is collected from data-sheet
            FASession.findElementByName("OK").click();                                                            // Clicks the OK button from the Start Workflow dialog and creates the workflow with entered information.
            //FASession.findElementByName("Cancel").click();
            //System.out.println("Workflow created : " + DwellName + " -- " + assmt + " -- " + subAssmt + " -- " + actn + " -- " + cmnt);

            String WorkflowsGridName = "Workflows";
            assert subAssmt != null;
            glWells.Validate_FA_Workflow_Created_Or_Not(WorkflowsGridName, subAssmt, actn);                                     // Validating the above created workflow is created and displaying in the Workflows grid or not using its Initial Assessment and Action details.
        }
        workbook.close();

    }

    @Test (priority = 1)
    public void BeamWellsWF() throws IOException {

        BeamWells faClient = new BeamWells(FASession);
        File src = new File(".\\Datafiles\\Demo.xlsx");
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(src);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        XSSFSheet sheet = workbook.getSheet("Beam Wells");
        FASession.findElement(By.name("Rod Pump")).click();
        FASession.findElement(By.name("Beam Wells")).click();
        FASession.findElement(By.name("BeamStatAllWellsHSplt")).isDisplayed();
        var beamAllWells = FASession.findElementByAccessibilityId("BeamStatAllWellsHSplt");
        var wellDataGrid = beamAllWells.findElementByAccessibilityId("tableControl1");
        Actions actions = new Actions(FASession);
        actions.moveToElement(wellDataGrid, 5, 5).click().build().perform();

        // This for loop will collect data from data-sheet and stores them into the variables provided accordingly

        for(int counter = 1; counter <= sheet.getLastRowNum(); counter++) {
            //actions.click(wellDataGrid).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_UP).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.F2).build().perform();

            XSSFCell cell = sheet.getRow(counter).getCell(0);
            String DwellName = null;
            if (cell.getCellType() == CellType.STRING) {
                DwellName = cell.getStringCellValue();
            }
            XSSFCell cell1 = sheet.getRow(counter).getCell(1);
            String assmt = null;
            if (cell1.getCellType() == CellType.STRING) {
                assmt = cell1.getStringCellValue();
            }
            XSSFCell cell2 = sheet.getRow(counter).getCell(2);
            String subAssmt = null;
            if (cell2.getCellType() == CellType.STRING) {
                subAssmt = cell2.getStringCellValue();
            }
            XSSFCell cell3 = sheet.getRow(counter).getCell(3);
            String userGroup = null;
            if (cell3.getCellType() == CellType.STRING) {
                userGroup = cell3.getStringCellValue();
            }
            XSSFCell cell4 = sheet.getRow(counter).getCell(4);
            String actn = null;
            if (cell4.getCellType() == CellType.STRING) {
                actn = cell4.getStringCellValue();
            }
            XSSFCell cell5 = sheet.getRow(counter).getCell(5);
            String eam = null;
            if (cell5.getCellType() == CellType.STRING) {
                eam = cell5.getStringCellValue();
            }
            XSSFCell cell6 = sheet.getRow(counter).getCell(6);
            String cmnt = null;
            if (cell6.getCellType() == CellType.STRING) {
                cmnt = cell6.getStringCellValue();
            }

            //Verifying the well names in application to select the one provided in data sheet.
            String appWellName;                                                                                         //*******************---------------------------------------------------------------********************
            while (true) {                                                                                              //
                List<MobileElement> wName = wellDataGrid.findElementsByXPath("//*");                              //
                appWellName = wName.getLast().getAttribute("Value.Value");                                        //
                System.out.println("Well selected is : " + appWellName);                                                //  This block will compare the Wellname collected from Application and Wellname collected from data-sheet
                if (appWellName.equals(DwellName))                                                                      //              If both are equal then it'll break the loop
                    break;                                                                                              //              And continue for clicking Start Workflow button and further process.
                else {                                                                                                  //      If the wellname's are not equal then it'll press the down arrow from the currently selected well
                    wellDataGrid.sendKeys(Keys.ARROW_DOWN);                                                             //                      and verifies that wellname against the wellname collected from data-sheet.
                    wellDataGrid.sendKeys(Keys.F2);                                                                     //
                }                                                                                                       //
            }                                                                                                           //*******************---------------------------------------------------------------********************

            //Launching the Start Workflow dialog and fill details in the data fields for creating a workflow.

            FASession.findElement(By.name("Start Workflow")).click();                                                   // Click's the Start Workflow button.
            var workflowType = FASession.findElementByAccessibilityId("WorkflowTypeSelectorDlg");
            workflowType.findElementByName("Operations").click();
            FASession.findElementByAccessibilityId("CreateWorkflowDlgOpsComm").isDisplayed();                     // Verifies whether Start Workflow dialog is displayed or not.
            FASession.findElementByAccessibilityId("_comboInitialAssessmentCategories").sendKeys(assmt);          // This will enter/select the Job Type or Initial Assessment data which is collected from data-sheet.
            actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.
            FASession.findElementByAccessibilityId("_comboInitialAssessment").sendKeys(subAssmt);                 // This will enter/select the Sub-Assessment data which is collected from data-sheet.
            actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.
            FASession.findElementByAccessibilityId("comboAssignToGroup").sendKeys(userGroup);                     // This will enter/select the Assign To data which is collected from data-sheet.
            actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.
            FASession.findElementByAccessibilityId("_comboActions").sendKeys(actn);                               // This will enter/select the Action data which is collected from data-sheet.
            actions.sendKeys(Keys.TAB).build().perform();                                                               // This line will move the cursor to next field replicating the TAB key pressing from Keyboard.

            assert eam != null;                                                                                         //
            if (eam.equals("Yes")) {                                                                                    // This block will un-tick the Create EAM checkbox if it receives "yes" from the data-sheet.
                FASession.findElement(By.name("Create EAM")).click();                                                   // Because India team couldn't create EAM jobs and verify them that is why we are unchecking the Create EAM checkbox.
            }                                                                                                           //

            FASession.findElementByAccessibilityId("_textComment").sendKeys(cmnt);                                // Enters the comment into the Start Workflow comment field which is collected from data-sheet
            FASession.findElementByName("OK").click();                                                            // Clicks the OK button from the Start Workflow dialog and creates the workflow with entered information.
            //FASession.findElementByName("Cancel").click();
            //System.out.println("Workflow created : " + DwellName + " -- " + assmt + " -- " + subAssmt + " -- " + actn + " -- " + cmnt);
            String WorkflowsGridName = FASession.findElementByAccessibilityId("BeamStatWFGridHSplt").getAttribute("Name");
            System.out.println("Extracted name of the Workflows grid is  :  "+WorkflowsGridName);
            assert subAssmt != null;
            beamWells.Validate_FA_BeamWell_Workflow_Created_Or_Not(WorkflowsGridName, subAssmt, actn);                                                         // Validating the above created workflow is created and displaying in the Workflows grid or not using its Initial Assessment and Action details.

            //wellDataGrid.click();
        }
        workbook.close();
    }


}
