package dataDrivenTests;

import TestUtils.OABaseTest;
import io.appium.java_client.MobileElement;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.pageobject.OpsAssist.Producers;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class OA_DataDrivenTests extends OABaseTest {
    @Test(priority = 2)
    public void Verify_workflows_Completed_in_Closed_tab() throws IOException {
        File src = new File(".\\Datafiles\\Demo.xlsx");
        XSSFWorkbook workbook;
        XSSFSheet sheet = null;
        try {
            workbook = new XSSFWorkbook(src);
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        int num_of_sheets = workbook.getNumberOfSheets();
        OASession.findElementByName("_tabSchedule").click();
        WebDriverWait wait = new WebDriverWait(OASession, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.name("Closed")));
        OASession.findElementByName("Closed").click();
        for (int sheet_count = 1; sheet_count <= num_of_sheets; sheet_count++) {
            sheet = workbook.getSheetAt(sheet_count - 1);

//            if ((sheet_count == 1)) {
//                sheet = workbook.getSheet("Gas Lift");
//            } else if (sheet_count == 2) {
//                sheet = workbook.getSheet("ESP");
//            }
            for (int counter = 1; counter <= sheet.getLastRowNum(); counter++) {

                XSSFCell cell = sheet.getRow(counter).getCell(0);
                String WName = null;
                if (cell.getCellType() == CellType.STRING) {
                    WName = cell.getStringCellValue();
                }
                XSSFCell cell1 = sheet.getRow(counter).getCell(1);
                String JobType = null;
                if (cell1.getCellType() == CellType.STRING) {
                    JobType = cell1.getStringCellValue();
                }
                XSSFCell cell2 = sheet.getRow(counter).getCell(2);
                String Assmt = null;
                if (cell2.getCellType() == CellType.STRING) {
                    Assmt = cell2.getStringCellValue();
                }
                XSSFCell cell3 = sheet.getRow(counter).getCell(7);
                String Acts = null;
                if (cell3.getCellType() == CellType.STRING) {
                    Acts = cell3.getStringCellValue();
                }

                var Results_Closed = OASession.findElementByAccessibilityId("gridClosed");
                var DGV_Cells = Results_Closed.findElementsByClassName("DataGridRow");
                System.out.println("No. of workflows displayed in the grid are : " + DGV_Cells.size());
                for (MobileElement selWF : DGV_Cells) {
                    List<MobileElement> dataGridCell = selWF.findElementsByClassName("DataGridCell");
                    String cellWName = dataGridCell.getFirst().getText();
                    //System.out.println("Selected row's wellname is :  "+cellWName);
                    String cellJType = dataGridCell.get(11).getText();
                    //System.out.println("Selected row's Job type is :  "+cellJType);
                    String cellIniAssmt = dataGridCell.get(12).getText();
                    //System.out.println("Selected row's Initial Assessment is :  "+cellIniAssmt);
                    String cellAct = dataGridCell.get(13).getText();
                    //System.out.println("Selected row's Action is :  "+cellAct);
                    if (Objects.equals(cellWName, WName) && Objects.equals(cellJType, JobType) && Objects.equals(cellIniAssmt, Assmt) && Objects.equals(cellAct, Acts)) {
                        selWF.findElementByName(WName).click();
                        System.out.println("Well Name: " + WName + " Assessment: " + JobType + " Sub-Assmt: " + Assmt + " Changed Action: " + Acts);
                        break;
                    }
                }
            }
            workbook.close();
        }
    }

    @Test(priority = 1)
    public void WorkflowEditing() throws IOException, InterruptedException {
        Producers producers = new Producers(OASession);
        File src = new File(".\\Datafiles\\Demo.xlsx");
        XSSFWorkbook workbook;
        XSSFSheet sheet = null;
        try {
            workbook = new XSSFWorkbook(src);
        } catch (InvalidFormatException | IOException e) {
            throw new RuntimeException(e);
        }
        int num_of_sheets = workbook.getNumberOfSheets();
        for (int sheet_count = 1; sheet_count <= num_of_sheets; sheet_count++) {
            sheet = workbook.getSheetAt(sheet_count - 1);
//            if ((sheet_count==1)) {
//                sheet = workbook.getSheet("Gas Lift");
//            } else if (sheet_count==2) {
//                sheet = workbook.getSheet("ESP");
//            }
            for (int counter = 1; counter <= sheet.getLastRowNum(); counter++) {

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
                XSSFCell cell5 = sheet.getRow(counter).getCell(4);
                String actn = null;
                if (cell5.getCellType() == CellType.STRING) {
                    actn = cell5.getStringCellValue();
                }
                XSSFCell cell3 = sheet.getRow(counter).getCell(7);
                String chng_Actn = null;
                if (cell3.getCellType() == CellType.STRING) {
                    chng_Actn = cell3.getStringCellValue();
                }
                XSSFCell cell4 = sheet.getRow(counter).getCell(8);
                String editCmnt = null;
                if (cell4.getCellType() == CellType.STRING) {
                    editCmnt = cell4.getStringCellValue();
                }

                System.out.println("Data acquired from spreadsheet:");
                System.out.println("Well Name:" + DwellName + " Assessment: " + assmt + " Sub-Assmt: " + subAssmt + "Changed Comment: " + editCmnt);
                producers.OA_WFEdit(DwellName, assmt, subAssmt, actn, chng_Actn, editCmnt);
            }
            workbook.close();
        }
    }


}
