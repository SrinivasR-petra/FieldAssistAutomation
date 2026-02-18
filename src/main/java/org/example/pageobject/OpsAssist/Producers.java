package org.example.pageobject.OpsAssist;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.functions.ExpectedCondition;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


public class Producers {
    public WindowsDriver<WindowsElement> OASession;
    public WebDriverWait wait;

    public Producers(WindowsDriver<WindowsElement> OASession) {
        this.OASession = OASession;
        wait = new WebDriverWait(OASession, 20);
    }

    public void Navigate_to_Wells_Producers_Screen() {
        OASession.findElementByName("_tabWells").click();
        boolean grid_displayed = OASession.findElementByAccessibilityId("AllWellsGrid").isDisplayed();
        if (!grid_displayed) {
            OASession.findElementByName("Producers").click();
        }
        OASession.findElementByName("Wells").click();


    }

//    public void ScrollDown_To_An_Well_In_ProducersScreen(String Wellname) {
//        double scrollPosition = 0.0;
//        var allWellsGrid = OASession.findElementByAccessibilityId("AllWellsGrid");
//        while (scrollPosition < 100.00) {
//            List<MobileElement> well_row = allWellsGrid.findElementsByClassName("DataGridRow");
//            for (MobileElement well : well_row) {
//                String wellDisplayed = well.getAttribute("Name");
//                if (Objects.equals(wellDisplayed, Wellname))
//                    break;
//            }
//            well_row.getLast().click();
//            allWellsGrid.sendKeys(Keys.PAGE_DOWN);
//            var scrollPercent = allWellsGrid.getAttribute("Scroll.VerticalScrollPercent");
//            scrollPosition = Double.parseDouble(scrollPercent);
//        }
//        allWellsGrid.findElementByName(Wellname).click();
//    }

    public void Select_a_Well(String Wellname) {
        var allWellsGrid = OASession.findElementByAccessibilityId("AllWellsGrid");
        OASession.findElementByAccessibilityId("tbWellFilter").sendKeys(Wellname);
        wait.until((ExpectedCondition<Boolean>) d -> {
            try {
                System.out.println("Waiting started for well in AllWellGrid ....");
                var newText = d.findElement(new MobileBy.ByAccessibilityId("AllWellsGrid")).findElements(By.className("DataGridRow")).getFirst().findElements(By.className("DataGridCell")).getFirst();
                String gridWellName = newText.findElement(By.name(Wellname)).getAttribute("Name");
                System.out.println("Well Name captured from first row is : " + gridWellName);
                boolean wellVisibility = Objects.equals(Wellname, gridWellName);
                newText.findElement(By.name(Wellname)).click();
                newText = null;
                return wellVisibility;
            } catch (WebDriverException e) {
                // Element might temporarily disappear during refresh
                return false;
            }
        });

        OASession.findElementByAccessibilityId("tbWellFilter").clear();
    }

    public void Click_StartJob_Button_And_data_entry(String Wellname, String JobAssessment, String JobAction, String Comment) {
        wait.until(ExpectedConditions.elementToBeClickable(OASession.findElementByName("Start")));

        OASession.findElementByName("Start").click();
        String str1 = "Start Job for ";
        String startJob_dlg_title = str1.concat(Wellname);
        OASession.findElementByName(startJob_dlg_title).isDisplayed();

        var StartJobDlg = OASession.findElementByAccessibilityId("Self");
        StartJobDlg.findElementByName("Job Type").isDisplayed();
        StartJobDlg.findElementByAccessibilityId("SearchAssessmentComboBox").sendKeys(JobAssessment);
        Actions oa_actions = new Actions(OASession);
        oa_actions.sendKeys(Keys.ARROW_DOWN).build().perform();
        StartJobDlg.findElementByAccessibilityId("tbComment").sendKeys(Comment);
        oa_actions.sendKeys(Keys.TAB).sendKeys(JobAction).build().perform();
        //OASession.findElementByXPath("//Text[@Name=\"Action\"]/following-sibling::*[1]").sendKeys(JobAction);
        oa_actions.sendKeys(Keys.ENTER).build().perform();

    }

    public void Click_Cancel_Button() {
        OASession.findElementByName("Cancel").click();
    }

    public void Click_OK_Button() {
        OASession.findElementByName("Ok").click();
    }

    public void Select_Radio_Button_Fron_Edit_Job_Dialog(String RadioButton) {
        OASession.findElementByName(RadioButton).click();
    }

    public void CreateSAP_Yes_No(String SAP_Option) {
        Actions oa_actions = new Actions(OASession);
        var SAPDisplayed = OASession.findElementByName("Create SAP Job?").isDisplayed();
        if (SAPDisplayed) {
            if (SAP_Option.equals("Yes")) {
                OASession.findElementByName("Yes").click();
                OASession.findElementByName("This will create a job in SAP").isDisplayed();
                var display = OASession.findElementByName("SAP").isDisplayed();
                Assert.assertTrue(display);
                OASession.findElementByName("SAP").click();
                var sapTab = OASession.findElementByAccessibilityId("dlgTabs");
                oa_actions.sendKeys("SAP Job").build().perform();   // SAP Description
                oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("0008").sendKeys(Keys.ENTER).build().perform(); // Detection Method
                oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("5").sendKeys(Keys.ENTER).build().perform(); // Priority
                List<MobileElement> childEquip = sapTab.findElementsByName("Child Equipment");
                if (!childEquip.isEmpty() && childEquip.getFirst().isDisplayed()) {
                    oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform(); // Child Equipment
                }else oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("FFCA").sendKeys(Keys.ENTER).build().perform(); // Notification Group
                oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("F047").sendKeys(Keys.ENTER).build().perform(); // Notification Code
                oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("5004").sendKeys(Keys.ENTER).build().perform(); // Work Center
                oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("1").sendKeys(Keys.ENTER).build().perform(); // Production Effect

            } else if (SAP_Option.equals("No")) {
                OASession.findElementByName("No").click();
            }
        }
    }

    public void OtherWellTest_JobDataTab(float Oil, float Water, float Gas) {
        var JData = OASession.findElementByName("Job Data");
        var JDGrid = OASession.findElementByAccessibilityId("dlgTabs");
        boolean JD_Status = JData.isDisplayed();
        float MinimumOil = 0, MaximumOil = 0, MinimumWater = 0, MaximumWater = 0, MinimumGas = 0, MaximumGas = 0;
        if (JD_Status) {
            Actions jd_actions = new Actions(OASession);
            JData.click();
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDate = today.format(formatter);
            JDGrid.findElementByAccessibilityId("PART_TextBox").sendKeys(currentDate);
            jd_actions.sendKeys(Keys.TAB).sendKeys("01:00 AM").build().perform();
            JDGrid.findElementByName("Hrs").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("8").build().perform();
            jd_actions.sendKeys(Keys.TAB).sendKeys("30").build().perform();
            JDGrid.findElementByName("Oil / Condensate").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("" + Oil).build().perform();

            List<MobileElement> DataRows = JDGrid.findElementsByClassName("DataGridRow");
            for (MobileElement DataRow : DataRows) {
                List<MobileElement> DataGridCells = DataRow.findElementsByClassName("DataGridCell");
                for (MobileElement DataGridCell : DataGridCells) {
                    var DataCellName = DataGridCell.getAttribute("Name");
                    if (DataCellName.equals("Oil / Condensate")) {
                        var DataColumn = DataRow.findElementByName("Item: PSC.OpsAssist.Operators.Data.OpsWorkflowJobData, Column Display Index: 2");
                        List<MobileElement> CellValue = DataColumn.findElementsByClassName("TextBlock");
                        var oilMin = CellValue.get(1).getAttribute("Name");
                        MinimumOil = Float.parseFloat(oilMin);
                        var oilMax = CellValue.get(3).getAttribute("Name");
                        MaximumOil = Float.parseFloat(oilMax);
                        break;
                    }
                }
            }

            JDGrid.findElementByName("Water").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("" + Water).build().perform();

            List<MobileElement> WaterDataRows = JDGrid.findElementsByClassName("DataGridRow");
            for (MobileElement WDataRow : WaterDataRows) {
                List<MobileElement> DataGridCells = WDataRow.findElementsByClassName("DataGridCell");
                for (MobileElement DataGridCell : DataGridCells) {
                    var DataCellName = DataGridCell.getAttribute("Name");
                    if (DataCellName.equals("Water")) {
                        var DataColumn = WDataRow.findElementByName("Item: PSC.OpsAssist.Operators.Data.OpsWorkflowJobData, Column Display Index: 2");
                        List<MobileElement> CellValue = DataColumn.findElementsByClassName("TextBlock");
                        var waterMin = CellValue.get(1).getAttribute("Name");
                        MinimumWater = Float.parseFloat(waterMin);
                        var waterMax = CellValue.get(3).getAttribute("Name");
                        MaximumWater = Float.parseFloat(waterMax);
                        break;
                    }
                }
            }

            JDGrid.findElementByName("Gas").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("" + Gas).build().perform();

            List<MobileElement> GasDataRows = JDGrid.findElementsByClassName("DataGridRow");
            for (MobileElement GDataRow : GasDataRows) {
                List<MobileElement> DataGridCells = GDataRow.findElementsByClassName("DataGridCell");
                for (MobileElement DataGridCell : DataGridCells) {
                    var DataCellName = DataGridCell.getAttribute("Name");
                    if (DataCellName.equals("Gas")) {
                        var DataColumn = GDataRow.findElementByName("Item: PSC.OpsAssist.Operators.Data.OpsWorkflowJobData, Column Display Index: 2");
                        List<MobileElement> CellValue = DataColumn.findElementsByClassName("TextBlock");
                        var gasMin = CellValue.get(1).getAttribute("Name");
                        MinimumGas = Float.parseFloat(gasMin);
                        var gasMax = CellValue.get(3).getAttribute("Name");
                        MaximumGas = Float.parseFloat(gasMax);
                        break;
                    }
                }
            }

            JDGrid.findElementByName("Accepted?").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("Accept, In Allocation").build().perform();

            if ((Float.compare(Oil, MinimumOil) < 0) || (Float.compare(MaximumOil, Oil) < 0)) {
                System.out.println("Given Oil value is out of the bounds");
            } else System.out.println("Given Oil value is with-in the bounds");

            if ((Float.compare(Water, MinimumWater) < 0) || (Float.compare(MaximumWater, Water) < 0)) {
                System.out.println("Given Water value is out of the bounds");
            } else System.out.println("Given Water value is with-in the bounds");
            if ((Float.compare(Gas, MinimumGas) < 0) || (Float.compare(MaximumGas, Gas) < 0)) {
                System.out.println("Given Gas value is out of the bounds");
            } else System.out.println("Given Gas value is with-in the bounds");
        }
    }

    public void CleanUp_JobDataTab(String Req_Resource) {
        Actions jd_actions = new Actions(OASession);
        OASession.findElementByName("Job Data").click();
        OASession.findElementByName("Required Resource").click();
        jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys(Req_Resource).sendKeys(Keys.ENTER).build().perform();
    }

    public void Optimization_JobDataTab() {
        var JData = OASession.findElementByName("Job Data");

        boolean JD_Status = JData.isDisplayed();
        if (JD_Status) {
            Actions jd_actions = new Actions(OASession);
            JData.click();
            var JDGrid = OASession.findElementByClassName("DataGrid");
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDate = today.format(formatter);
            JDGrid.findElementByAccessibilityId("PART_TextBox").sendKeys(currentDate);
            jd_actions.sendKeys(Keys.TAB).sendKeys("01:00 AM").build().perform();
            JDGrid.findElementByName("Hrs").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("8").build().perform();
            jd_actions.sendKeys(Keys.TAB).sendKeys("30").build().perform();
            JDGrid.findElementByName("Well or Packer Depth (ft)").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("800").build().perform();
            JDGrid.findElementByName("Tubing OD (in)").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("6").build().perform();
            JDGrid.findElementByName("Casing ID (in)").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("4").build().perform();
            JDGrid.findElementByName("Starting Pressure (psig)").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("200").build().perform();
            JDGrid.findElementByName("Ending Pressure (psig)").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("220").build().perform();
        }
    }

    public void OtherFluidShot_JobData(String FluidLevel, String EC_Comment) {
        var JData = OASession.findElementByName("Job Data");
        var JDGrid = OASession.findElementByAccessibilityId("dlgTabs");
        boolean JD_Status = JData.isDisplayed();
        if (JD_Status) {
            Actions jd_actions = new Actions(OASession);
            JData.click();
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDate = today.format(formatter);
            JDGrid.findElementByAccessibilityId("PART_TextBox").sendKeys(currentDate);
            JDGrid.findElementByName("Fluid Level From Surface").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys(FluidLevel).build().perform();
            JDGrid.findElementByName("EC Comment").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys("EC_Comment").build().perform();
        }
    }

    public void OtherPressureReading_JobData(String Bradenhead_Pressure, String Intermediate_Pressure, String EC_Comment) {
        var JData = OASession.findElementByName("Job Data");
        boolean JD_Status = JData.isDisplayed();
        if (JD_Status) {
            Actions jd_actions = new Actions(OASession);
            JData.click();
            var JDGrid = OASession.findElementByClassName("DataGrid");
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            String currentDate = today.format(formatter);
            JDGrid.findElementByAccessibilityId("PART_TextBox").sendKeys(currentDate);
            jd_actions.sendKeys(Keys.TAB).sendKeys("01:00 AM").build().perform();
            JDGrid.findElementByName("Bradenhead Pressure").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys(Bradenhead_Pressure).build().perform();
            JDGrid.findElementByName("1st Intermediate Pressure").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys(Intermediate_Pressure).build().perform();
            JDGrid.findElementByName("EC Comment").click();
            jd_actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys(EC_Comment).build().perform();

        }
    }

    public void OA_WFEdit(String WName, String JobType, String IniAssessment, String Acts, String Chang_Acts, String Edit_Cmt) {
        Actions OA_actions = new Actions(OASession);
        OASession.findElementByName("_tabSchedule").click();
        wait.until((ExpectedCondition<Boolean>) d -> {
            try {
                return d.findElement(By.name("Inbox")).isEnabled();
            } catch (WebDriverException e) {
                // Element might temporarily disappear during refresh
                return false;
            }
        });
        OASession.findElementByName("Inbox").click();
        var Results_Inbox = OASession.findElementByAccessibilityId("gridInbox");
        var DGV_Headers = Results_Inbox.findElementByAccessibilityId("PART_ColumnHeadersPresenter");
        String Name_header = null, JType_header = null, Assmt_header = null, Actn_header = null;

        Name_header = DGV_Headers.findElementByName("Name").getAttribute("PositionInSet");
        JType_header = DGV_Headers.findElementByName("Job Type").getAttribute("PositionInSet");
        Assmt_header = DGV_Headers.findElementByName("Assessment").getAttribute("PositionInSet");
        Actn_header = DGV_Headers.findElementByName("Action").getAttribute("PositionInSet");

        int nameIndex = Integer.parseInt(Name_header) - 1;
        int jtpeIndex = Integer.parseInt(JType_header) - 1;
        int assmtIndex = Integer.parseInt(Assmt_header) - 1;
        int actnIndex = Integer.parseInt(Actn_header) - 1;

        JType_header = Integer.toString(jtpeIndex);
        Name_header = Integer.toString(nameIndex);
        Assmt_header = Integer.toString(assmtIndex);
        Actn_header = Integer.toString(actnIndex);

        wait.until((ExpectedCondition<Boolean>) d -> {
            try {
                System.out.println("Waiting started for well in Inbox ....");
                d.findElement(new MobileBy.ByAccessibilityId("gridInbox")).findElement(By.className("DataGridRow")).isDisplayed();
                var newText = d.findElement(new MobileBy.ByAccessibilityId("gridInbox")).findElements(By.className("DataGridRow")).getFirst().findElements(By.className("DataGridCell")).get(1);
                String gridWellName = newText.getAttribute("Name");
                System.out.println("Well Name captured from first row is : " + gridWellName);
                boolean wellVisibility = Objects.equals(WName, gridWellName);
                newText = null;
                return wellVisibility;
            } catch (WebDriverException e) {
                // Element might temporarily disappear during refresh
                return false;
            }
        });

        var DGV_Cells = Results_Inbox.findElementsByClassName("DataGridRow");
        for (MobileElement selWF : DGV_Cells) {
            String cellWname = null, cellJType = null, cellIniAssmt = null, cellAct = null;
            List<MobileElement> dataGridCell = selWF.findElementsByClassName("DataGridCell");
            for (MobileElement cell : dataGridCell) {
                var gridHeader = cell.getAttribute("GridItem.Column");

                if (Objects.equals(Name_header, gridHeader)) {
                    cellWname = cell.getAttribute("Name");

                }
                if (Objects.equals(JType_header, gridHeader)) {
                    cellJType = cell.getAttribute("Name");

                }
                if (Objects.equals(Assmt_header, gridHeader)) {
                    cellIniAssmt = cell.getAttribute("Name");

                }
                if (Objects.equals(Actn_header, gridHeader)) {
                    cellAct = cell.getAttribute("Name");

                }
            }
            if (Objects.equals(cellWname, WName) && Objects.equals(cellJType, JobType) && Objects.equals(cellIniAssmt, IniAssessment) && Objects.equals(cellAct, Acts)) {
                selWF.findElementByName(WName).click();
                System.out.println(" Well Name: " + WName + "\n" + " Assessment: " + JobType + "\n" + " Sub-Assmt: " + IniAssessment + "\n" + " Action: " + Acts);
                break;
            }

        }

        OASession.findElementByName("Edit").click();
        OASession.findElementByName("Comment...").click();
        OASession.findElementByAccessibilityId("tbComment").sendKeys(Edit_Cmt);
        OA_actions.sendKeys(Keys.TAB).build().perform();
        OA_actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Chang_Acts).build().perform();
    }

    public void Verify_the_Workflow_is_closed(String WName, String JobType, String IniAssessment, String Chang_Acts) {
        OASession.findElementByName("_tabSchedule").click();
        wait.until(ExpectedConditions.elementToBeClickable(By.name("Closed")));
        OASession.findElementByName("Closed").click();
        var Results_Closed = OASession.findElementByAccessibilityId("gridClosed");
        var DGV_Headers = Results_Closed.findElementByAccessibilityId("PART_ColumnHeadersPresenter");
        String Name_header = null, JType_header = null, Assmt_header = null, Actn_header = null;

        Name_header = DGV_Headers.findElementByName("Name").getAttribute("PositionInSet");
        JType_header = DGV_Headers.findElementByName("Job Type").getAttribute("PositionInSet");
        Assmt_header = DGV_Headers.findElementByName("Assessment").getAttribute("PositionInSet");
        Actn_header = DGV_Headers.findElementByName("Action").getAttribute("PositionInSet");

        int nameIndex = Integer.parseInt(Name_header) - 1;
        int jtpeIndex = Integer.parseInt(JType_header) - 1;
        int assmtIndex = Integer.parseInt(Assmt_header) - 1;
        int actnIndex = Integer.parseInt(Actn_header) - 1;

        JType_header = Integer.toString(jtpeIndex);
        Name_header = Integer.toString(nameIndex);
        Assmt_header = Integer.toString(assmtIndex);
        Actn_header = Integer.toString(actnIndex);


        wait.until((ExpectedCondition<Boolean>) d -> {
            try {
                System.out.println("Waiting started for well in Closed Tab....");
                //var newText = Results_Inbox.findElementsByClassName("DataGridRow").getFirst().findElementsByClassName("DataGridCell").getFirst().findElementByName(WName);
                var newText = d.findElement(new MobileBy.ByAccessibilityId("gridClosed")).findElements(By.className("DataGridRow")).getFirst().findElements(By.className("DataGridCell")).getFirst();
                String gridWellName = newText.getAttribute("Name");
                System.out.println("Well Name captured from first row is : " + gridWellName);
                boolean wellVisibility = Objects.equals(WName, gridWellName);
                newText = null;
                return wellVisibility;
            } catch (WebDriverException e) {
                // Element might temporarily disappear during refresh
                return false;
            }
        });

        var DGV_Cells = Results_Closed.findElementsByClassName("DataGridRow");
        for (MobileElement selWF : DGV_Cells) {
            String cellWname = null, cellJType = null, cellIniAssmt = null, cellAct = null;
            List<MobileElement> dataGridCell = selWF.findElementsByClassName("DataGridCell");
            for (MobileElement cell : dataGridCell) {
                var gridHeader = cell.getAttribute("GridItem.Column");

                if (Objects.equals(Name_header, gridHeader)) {
                    cellWname = cell.getAttribute("Name");
                    //System.out.println("Well Name captured from grid is : "+cellWname);

                }
                if (Objects.equals(JType_header, gridHeader)) {
                    cellJType = cell.getAttribute("Name");
                    //System.out.println("Job Type captured from grid is : "+cellJType);

                }
                if (Objects.equals(Assmt_header, gridHeader)) {
                    cellIniAssmt = cell.getAttribute("Name");
                    //System.out.println("Initial Assessment captured fom grid is : "+cellIniAssmt );

                }
                if (Objects.equals(Actn_header, gridHeader)) {
                    cellAct = cell.getAttribute("Name");
                    //System.out.println("Action captured from grid is : "+cellAct);

                }
            }
            if (Objects.equals(cellWname, WName) && Objects.equals(cellJType, JobType) && Objects.equals(cellIniAssmt, IniAssessment) && Objects.equals(cellAct, Chang_Acts)) {
                selWF.findElementByName(WName).click();
                System.out.println(" Well Name: " + WName + "\n" + " Assessment: " + JobType + "\n" + " Sub-Assmt: " + IniAssessment + "\n" + " Action: " + Chang_Acts + "\n" + "Workflow Closed Successfully");
                break;
            } else System.out.println("Couldn't find the Workflow !");

        }

    }
    public void CreateSAP(String SAP_Option, String WellType) {
        Actions oa_actions = new Actions(OASession);
        var SAPDisplayed = OASession.findElementByName("Create SAP Job?").isDisplayed();
        if (SAPDisplayed) {
            if (SAP_Option.equals("Yes")) {
                OASession.findElementByName("Yes").click();
                OASession.findElementByName("This will create a job in SAP").isDisplayed();
                var display = OASession.findElementByName("SAP").isDisplayed();
                Assert.assertTrue(display);
                OASession.findElementByName("SAP").click();
                var sapTab = OASession.findElementByAccessibilityId("dlgTabs");
                oa_actions.sendKeys("SAP Job").build().perform();   // SAP Description
                oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("0008").sendKeys(Keys.ENTER).build().perform(); // Detection Method
                oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("5").sendKeys(Keys.ENTER).build().perform(); // Priority
                List<MobileElement> childEquip = sapTab.findElementsByName("Child Equipment");
                if (!childEquip.isEmpty() && childEquip.getFirst().isDisplayed()) {
                    oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
                }else oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("FFCA").sendKeys(Keys.ENTER).build().perform();
                oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("F047").sendKeys(Keys.ENTER).build().perform(); // Notification Code
                oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("5004").sendKeys(Keys.ENTER).build().perform(); // Work Center
                oa_actions.sendKeys(Keys.TAB).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys("1").sendKeys(Keys.ENTER).build().perform(); // Production Effect

            } else if (SAP_Option.equals("No")) {
                OASession.findElementByName("No").click();
            }
        }
    }

    public void Tick_All_Tasks_As_Done_In_Alarms_TaskListTab(){
        List<MobileElement> taskLists = OASession.findElementByAccessibilityId("dlgTabs").findElementsByName("Task List");
        if (!taskLists.isEmpty()) {
            var taskListTab = OASession.findElementByAccessibilityId("dlgTabs").findElementByName("Task List");
            if (taskListTab.isDisplayed()) {
                taskListTab.click();
                var taskListGrid = OASession.findElementByAccessibilityId("dlgTabs").findElementByAccessibilityId("gridTasks");
                List<MobileElement> dataRows = taskListGrid.findElementsByClassName("DataGridRow");
                for (MobileElement datarow : dataRows) {
                    List<MobileElement> dataCols = datarow.findElementsByClassName("DataGridCell");
                    dataCols.get(1).click();
                    OASession.findElementByAccessibilityId("tbTaskComment").sendKeys("Testing TaskLists");
                }
            }
        }
        else System.out.println("No task lists tab is found!");
    }
}

