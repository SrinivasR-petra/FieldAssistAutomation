package org.example.pageobject.FieldAssist_Client;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import junit.framework.TestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.example.pageobject.FieldAssist_Client.Common.isDuplicate;
import static org.example.pageobject.FieldAssist_Client.Common.seenValues;

public class FieldDataCapture {
    private final WindowsDriver<WindowsElement> FASession;
    private final WebDriverWait wait;
    private final Actions actions;
    Common common;

    public FieldDataCapture(WindowsDriver<WindowsElement> _FASession)
    {
        this.FASession = _FASession;
        this.wait = new WebDriverWait(FASession, 20);
        actions = new Actions(FASession);
        common = new Common(FASession);
    }

    //Common locators
    private final By Equip_Menu = By.name("Equipment");
    private final By FDC_Menu = By.name("Field Data Capture");
    private final By FDC_CollectionPointsTab = By.name("tpaFacilities");
    private final By FDC_CollectionPointsGrid = new MobileBy.ByAccessibilityId("tpaFacilities");
    private final By FDC_BeginDate = new MobileBy.ByAccessibilityId("dtpBeginDate");
    private final By FDC_EndDate = new MobileBy.ByAccessibilityId("dtpEndDate");
    private final By Grid_DataTable = MobileBy.AccessibilityId("tableControl1");
    private final By FDC_DateForAdd = new MobileBy.ByAccessibilityId("dtpForAdd");
    private final By FDC_Export_PendingRadioBtn = new MobileBy.ByAccessibilityId("rbPending");
    private final By FDC_ConfirmationDlg = By.name("Confirmation");
    private final By FDC_ConfirmationDlg_OKBtn = By.name("OK");
    private final By FDC_RevertBtn = new MobileBy.ByAccessibilityId("btnRevertSelected");

    //Tank Level Tab locators
    private final By FDC_TankLevelTab = By.name("tpaTankLevels");
    private final By FDC_TankLevelGrid = new MobileBy.ByAccessibilityId("tpaTankLevels");
    private final By FDC_TankLevelDataGrid = new MobileBy.ByAccessibilityId("splitContainerAdvTankLevelBottom");
    private final By FDC_TankLevelExportStatusGrid = new MobileBy.ByAccessibilityId("scaTLDExportStatusMain");

    //Oil/Water/Gas Meters Tab locators
    private final By FDC_MetersTab = By.name("tpaOWGMeters");
    private final By FDC_StreamsGrid = new MobileBy.ByAccessibilityId("emgcStreams");
    private final By FDC_StreamsDataGrid = new MobileBy.ByAccessibilityId("emgcStreamsData");
    private final By FDC_ExportStatusGrid = new MobileBy.ByAccessibilityId("emgcExportStatus");

    //Tank Tickets Tab locators
    private final By FDC_TankTicketsTab = By.name("tpaTankTickets");
    private final By FDC_TankStreamsGrid = new MobileBy.ByAccessibilityId("emgcTankTicketStreams");
    private final By FDC_TankTicketsGrid = new MobileBy.ByAccessibilityId("emgcTankTickets");
    private final By FDC_TankTicketsDataGrid = new MobileBy.ByAccessibilityId("emgcTankTicketsData");

    //LACT Tickets Tab locators
    private final By FDC_LACTTicketsTab = By.name("tpaLactTickets");
    private final By FDC_LACTStreamsGrid = new MobileBy.ByAccessibilityId("emgcLACTTicketStreams");
    private final By FDC_LACTTicketsGrid = new MobileBy.ByAccessibilityId("emgcLACTTickets");


    private double safeParse(String value) {
        if (value == null) return 0.0;
        value = value.trim();
        if (value.isEmpty()) return 0.0;
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void Navigate_to_FieldDataCaptureScreen(){
        FASession.findElement(Equip_Menu).click();
        FASession.findElement(FDC_Menu).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(FDC_CollectionPointsTab));
    }

    public void Select_BeginDate( ){
        LocalDate beginDate = LocalDate.now().minusMonths(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String coll_beginDate = beginDate.format(formatter);
        FASession.findElement(FDC_BeginDate).sendKeys(coll_beginDate);
    }

    public void Select_EndDate(){
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String coll_endDate = today.format(formatter);
        FASession.findElement(FDC_EndDate).sendKeys(coll_endDate);
        FASession.findElement(FDC_EndDate).click();
        FASession.findElement(FDC_EndDate).sendKeys(Keys.ALT,Keys.ARROW_DOWN);
        FASession.findElement(By.name("Today Button")).click();
    }

    public void Validate_AddDate_is_one_day_lessthan_EndDate(){

        String endDateVal = FASession.findElement(FDC_EndDate).getAttribute("Value.Value");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate endDate = LocalDate.parse(endDateVal, formatter);
        String dateForAddVal = FASession.findElement(FDC_DateForAdd).getAttribute("Value.Value");
        LocalDate dateForAdd = LocalDate.parse(dateForAddVal, formatter);

        if(dateForAdd.equals(endDate.minusDays(1))){
            System.out.println("Date for Add is one day less than the End date");
        }
        else TestCase.fail("Date for Add is not one day less than End date");

    }

    public void Select_a_CollectionPoint(String CollectionPoint){
        FASession.findElement(FDC_CollectionPointsTab).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(FDC_CollectionPointsGrid));
        MobileElement collectionPointGrid = FASession.findElement(FDC_CollectionPointsGrid);
        MobileElement collectionDataGrid = collectionPointGrid.findElement(Grid_DataTable);
        actions.moveToElement(collectionDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
        //actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        String appCollectionPoint;
        while (true) {
            List<MobileElement> wName = collectionDataGrid.findElementsByXPath("//*");
            appCollectionPoint = wName.getLast().getAttribute("Value.Value");
            int wnsize = appCollectionPoint.getBytes().length;
            if (appCollectionPoint.equals(CollectionPoint)) {
                System.out.println("Collection Point selected is : " + appCollectionPoint);
                seenValues.clear();
                break;
            }
            else {
                for (int i = 0; i < wnsize; i++) {collectionDataGrid.sendKeys(Keys.ARROW_RIGHT);}

                collectionDataGrid.sendKeys(Keys.ARROW_DOWN);
                collectionDataGrid.sendKeys(Keys.F2);
            }
            if (isDuplicate(appCollectionPoint)) {
                TestCase.fail("Error : Unable to find the provided Collection Point");
                break;
            }
        }
    }

    public void Navigate_to_TankLevel_tab(){
        FASession.findElement(FDC_TankLevelTab).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(FDC_TankLevelGrid));
    }

    public void Select_a_Tank_using_TankCode(String TankCode){
        MobileElement tankLevelGrid = FASession.findElement(FDC_TankLevelGrid).findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(tankLevelGrid,TankCode);
    }

    public void Click_Add_button(){
        FASession.findElement(new MobileBy.ByAccessibilityId("btnAdd")).click();
    }

    public void Click_Save_button(){
        FASession.findElement(new MobileBy.ByAccessibilityId("btnSave")).click();
    }

    public void Click_Cancel_button(){
        FASession.findElement(new MobileBy.ByAccessibilityId("btnCancel")).click();
    }

    public void Click_Yes_in_Continue_dialog(){
        WebElement continueDlg = FASession.findElement(By.name("Continue?"));
        continueDlg.findElement(By.name("Yes")).click();
    }

    public void Click_No_in_Continue_dialog(){
        WebElement continueDlg = FASession.findElement(By.name("Continue?"));
        continueDlg.findElement(By.name("No")).click();
    }

    public void Click_Yes_in_Confirmation_dialog(){
        FASession.findElement(FDC_ConfirmationDlg).findElement(FDC_ConfirmationDlg_OKBtn).click();
    }

    public void Click_Revert_button(){
        FASession.findElement(FDC_RevertBtn).click();
    }

    public void Enter_TankLevelData(String LiquidDip, String WaterDip, String Comments){
        WebElement tankLvlDataGrid = FASession.findElement(FDC_TankLevelDataGrid).findElement(Grid_DataTable);
        actions.moveToElement(tankLvlDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_DOWN).keyUp(Keys.CONTROL).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(LiquidDip);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(WaterDip);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Comments);
        actions.sendKeys(Keys.TAB).build().perform();
    }

    public void Validate_the_status_of_Added_TankLevelData(String TankCode, String LiquidDip, String WaterDip, String Comments){
        MobileElement exportStatusGrid = FASession.findElement(FDC_TankLevelExportStatusGrid);
        exportStatusGrid.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        String TankName, appLiqDip, appWaterDip, appComments, appType;
        while (true) {
            List<MobileElement> tName = exportStatusDataGrid.findElementsByXPath("//*");
            TankName = tName.getLast().getAttribute("Value.Value");
            int tnsize = TankName.getBytes().length;
            for (int i = 0; i < tnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> LiqDip = exportStatusDataGrid.findElementsByXPath("//*");
            appLiqDip = LiqDip.getLast().getAttribute("Value.Value");
            int ldsize = appLiqDip.getBytes().length;
            for (int i = 0; i < ldsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> WatDip = exportStatusDataGrid.findElementsByXPath("//*");
            appWaterDip = WatDip.getLast().getAttribute("Value.Value");
            int wdsize = appWaterDip.getBytes().length;
            for (int i = 0; i < wdsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appComments = Cmnt.getLast().getAttribute("Value.Value");
            int cmtsize = appComments.getBytes().length;
            for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            double lDip = Double.parseDouble(LiquidDip);
            String expLiquidDip = String.format("%.5f", lDip);
            double wDip = Double.parseDouble(WaterDip);
            String expWaterDip = String.format("%.5f", wDip);

            double appLiq = Double.parseDouble(appLiqDip);
            double appWat = Double.parseDouble(appWaterDip);

            double expLiq = Double.parseDouble(expLiquidDip);
            double expWat = Double.parseDouble(expWaterDip);

            boolean dipsMatch = (Double.compare(appLiq, expLiq) == 0) &&
                    (Double.compare(appWat, expWat) == 0);

            if (TankName.contains(TankCode))
            {
                if (dipsMatch)
                {
                    if (appComments.equals(Comments))
                    {
                        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                        List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                        appType = type.getLast().getAttribute("Value.Value");

                        if(appType.equals("Add"))
                        {
                            seenValues.clear();
                            break;
                        }
                        else{
                            TestCase.fail("Selected Tank Level Data row is not having the Add Type");
                        }
                    }
                }
            }
            if(isDuplicate(TankName)){
                System.out.println("Data Rows Ended");
                break;
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.F2).build().perform();
            for (int i = 0; i < tnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
    }

    public void Validate_the_status_of_Edited_TankLevelData(String TankCode, String LiquidDip, String WaterDip, String Comments) {

        MobileElement exportStatusGrid = FASession.findElement(FDC_TankLevelExportStatusGrid);
        exportStatusGrid.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        String TankName, appLiqDip, appWaterDip, appComments, appType;
        while (true) {
            List<MobileElement> tName = exportStatusDataGrid.findElementsByXPath("//*");
            TankName = tName.getLast().getAttribute("Value.Value");
            int tnsize = TankName.getBytes().length;
            for (int i = 0; i < tnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> LiqDip = exportStatusDataGrid.findElementsByXPath("//*");
            appLiqDip = LiqDip.getLast().getAttribute("Value.Value");
            int ldsize = appLiqDip.getBytes().length;
            for (int i = 0; i < ldsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> WatDip = exportStatusDataGrid.findElementsByXPath("//*");
            appWaterDip = WatDip.getLast().getAttribute("Value.Value");
            int wdsize = appWaterDip.getBytes().length;
            for (int i = 0; i < wdsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appComments = Cmnt.getLast().getAttribute("Value.Value");
            int cmtsize = appComments.getBytes().length;
            for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            double lDip = Double.parseDouble(LiquidDip);
            String expLiquidDip = String.format("%.5f", lDip);
            double wDip = Double.parseDouble(WaterDip);
            String expWaterDip = String.format("%.5f", wDip);

            double appLiq = Double.parseDouble(appLiqDip);
            double appWat = Double.parseDouble(appWaterDip);

            double expLiq = Double.parseDouble(expLiquidDip);
            double expWat = Double.parseDouble(expWaterDip);

            boolean dipsMatch = (Double.compare(appLiq, expLiq) == 0) &&
                    (Double.compare(appWat, expWat) == 0);

            if (TankName.contains(TankCode))
            {
                if (dipsMatch)
                {
                    if (appComments.equals(Comments))
                    {
                        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                        List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                        appType = type.getLast().getAttribute("Value.Value");

                        if(appType.equals("Change"))
                        {
                            seenValues.clear();
                            break;
                        }
                        else{
                            TestCase.fail("Selected Tank Level Data row is not having the Change Type");
                        }
                    }
                }
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.F2).build().perform();
            for (int i = 0; i < tnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
    }

    public void Validate_the_TankLevelData_is_not_added(String TankCode, String LiquidDip, String WaterDip, String Comments){
        MobileElement exportStatusGrid = FASession.findElement(FDC_TankLevelExportStatusGrid);
        exportStatusGrid.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        String TankName, appLiqDip, appWaterDip, appComments;
        while (true) {
            List<MobileElement> tName = exportStatusDataGrid.findElementsByXPath("//*");
            System.out.println("Length of the Export Status grid is : "+tName.size());
            if(tName.isEmpty() || tName.size() == 1){break;}
            TankName = tName.getLast().getAttribute("Value.Value");
            int tnsize = TankName.getBytes().length;
            for (int i = 0; i < tnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> LiqDip = exportStatusDataGrid.findElementsByXPath("//*");
            appLiqDip = LiqDip.getLast().getAttribute("Value.Value");
            int ldsize = appLiqDip.getBytes().length;
            for (int i = 0; i < ldsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> WatDip = exportStatusDataGrid.findElementsByXPath("//*");
            appWaterDip = WatDip.getLast().getAttribute("Value.Value");
            int wdsize = appWaterDip.getBytes().length;
            for (int i = 0; i < wdsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appComments = Cmnt.getLast().getAttribute("Value.Value");

            double lDip = Double.parseDouble(LiquidDip);
            String fLiquidDip = String.format("%.5f", lDip);
            double wDip = Double.parseDouble(WaterDip);
            String fWaterDip = String.format("%.5f", wDip);

            if (!(TankName.contains(TankCode) && appLiqDip.equals(fLiquidDip) && appWaterDip.equals(fWaterDip) && appComments.equals(Comments))){
                seenValues.clear();
                break;
            }
            else {
                actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.F2).build().perform();
                for (int i = 0; i < tnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
                exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
                exportStatusDataGrid.sendKeys(Keys.F2);
            }
        }
    }

    public void Navigate_to_Oil_Water_Gas_Meters_tab(){
        FASession.findElement(FDC_MetersTab).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(FDC_StreamsGrid));
    }

    public void Select_a_Stream_using_Stream_Name(String StreamName){
        MobileElement streamGrid = FASession.findElement(FDC_StreamsGrid).findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(streamGrid,StreamName);
    }

    public void Enter_StreamsData(String Override_Opening, String Closing, String Comments){
        WebElement streamsDataGrid = FASession.findElement(FDC_StreamsDataGrid).findElement(Grid_DataTable);
        actions.moveToElement(streamsDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_DOWN).keyUp(Keys.CONTROL).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Override_Opening);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Closing);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Comments);
        actions.sendKeys(Keys.TAB).build().perform();
    }

    public void Validate_the_status_of_Added_StreamsData(String StreamName, String Opening_Override, String Closing, String Comments){
        MobileElement exportStatusGrid = FASession.findElement(FDC_ExportStatusGrid);
        FASession.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        String appStreamName, appOpening, appClosing, appComments, appType;
        while (true) {
            List<MobileElement> sName = exportStatusDataGrid.findElementsByXPath("//*");
            appStreamName = sName.getLast().getAttribute("Value.Value");
            int snsize = appStreamName.getBytes().length;
            for (int i = 0; i < snsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Opening = exportStatusDataGrid.findElementsByXPath("//*");
            appOpening = Opening.getLast().getAttribute("Value.Value");
            int oosize = appOpening.getBytes().length;
            for (int i = 0; i < oosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Close = exportStatusDataGrid.findElementsByXPath("//*");
            appClosing = Close.getLast().getAttribute("Value.Value");
            int csize = appClosing.getBytes().length;
            for (int i = 0; i < csize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appComments = Cmnt.getLast().getAttribute("Value.Value");
            int cmtsize = appComments.getBytes().length;
            for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            double oOride = Double.parseDouble(Opening_Override);
            String expOverride = String.format("%.5f", oOride);
            double closeInfo = Double.parseDouble(Closing);
            String expWaterDip = String.format("%.5f", closeInfo);

            double appOpen = Double.parseDouble(appOpening);
            double appClose = Double.parseDouble(appClosing);

            double expOpenOverride = Double.parseDouble(expOverride);
            double expClosing = Double.parseDouble(expWaterDip);

            boolean dipsMatch = (Double.compare(appOpen, expOpenOverride) == 0) &&
                    (Double.compare(appClose, expClosing) == 0);

            if (appStreamName.equals(StreamName))
            {
                if (dipsMatch)
                {
                    if (appComments.equals(Comments))
                    {
                        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                        List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                        appType = type.getLast().getAttribute("Value.Value");

                        if(appType.equals("Add"))
                        {
                            seenValues.clear();
                            break;
                        }
                        else{
                            TestCase.fail("Selected Streams Data row is not having the Add Type");
                        }
                    }
                }
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.F2).build().perform();
            for (int i = 0; i < snsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
    }

    public void Validate_the_status_of_Edited_StreamsData(String StreamName, String Opening_Override, String Closing, String Comments){
        MobileElement exportStatusGrid = FASession.findElement(FDC_ExportStatusGrid);
        FASession.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        String appStreamName, appOpening, appClosing, appComments, appType;
        while (true) {
            List<MobileElement> sName = exportStatusDataGrid.findElementsByXPath("//*");
            appStreamName = sName.getLast().getAttribute("Value.Value");
            int snsize = appStreamName.getBytes().length;
            for (int i = 0; i < snsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Opening = exportStatusDataGrid.findElementsByXPath("//*");
            appOpening = Opening.getLast().getAttribute("Value.Value");
            int oosize = appOpening.getBytes().length;
            for (int i = 0; i < oosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Close = exportStatusDataGrid.findElementsByXPath("//*");
            appClosing = Close.getLast().getAttribute("Value.Value");
            int csize = appClosing.getBytes().length;
            for (int i = 0; i < csize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appComments = Cmnt.getLast().getAttribute("Value.Value");
            int cmtsize = appComments.getBytes().length;
            for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            double oOride = Double.parseDouble(Opening_Override);
            String expOverride = String.format("%.5f", oOride);
            double closeInfo = Double.parseDouble(Closing);
            String expWaterDip = String.format("%.5f", closeInfo);

            double appOpen = Double.parseDouble(appOpening);
            double appClose = Double.parseDouble(appClosing);

            double expOpenOverride = Double.parseDouble(expOverride);
            double expClosing = Double.parseDouble(expWaterDip);

            boolean dipsMatch = (Double.compare(appOpen, expOpenOverride) == 0) &&
                    (Double.compare(appClose, expClosing) == 0);

            if (appStreamName.equals(StreamName))
            {
                if (dipsMatch)
                {
                    if (appComments.equals(Comments))
                    {
                        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                        List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                        appType = type.getLast().getAttribute("Value.Value");

                        if(appType.equals("Change"))
                        {
                            seenValues.clear();
                            break;
                        }
                        else{
                            TestCase.fail("Selected Streams Data row is not having the Change Type");
                        }
                    }
                }
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.F2).build().perform();
            for (int i = 0; i < snsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
    }

    public void Validate_the_StreamsData_is_not_added(String StreamName, String Opening_Override, String Closing, String Comments){
        MobileElement exportStatusGrid = FASession.findElement(FDC_ExportStatusGrid);
        FASession.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        String appStreamName, appOpening, appClosing, appComments, appType;
        while (true) {
            List<MobileElement> sName = exportStatusDataGrid.findElementsByXPath("//*");
            if(sName.isEmpty() || sName.size()==1){break;}
            appStreamName = sName.getLast().getAttribute("Value.Value");
            int snsize = appStreamName.getBytes().length;
            for (int i = 0; i < snsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Opening = exportStatusDataGrid.findElementsByXPath("//*");
            appOpening = Opening.getLast().getAttribute("Value.Value");
            int oosize = appOpening.getBytes().length;
            for (int i = 0; i < oosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Close = exportStatusDataGrid.findElementsByXPath("//*");
            appClosing = Close.getLast().getAttribute("Value.Value");
            int csize = appClosing.getBytes().length;
            for (int i = 0; i < csize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appComments = Cmnt.getLast().getAttribute("Value.Value");
            int cmtsize = appComments.getBytes().length;
            for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            double oOride = Double.parseDouble(Opening_Override);
            String expOverride = String.format("%.5f", oOride);
            double closeInfo = Double.parseDouble(Closing);
            String expWaterDip = String.format("%.5f", closeInfo);

            double appOpen = Double.parseDouble(appOpening);
            double appClose = Double.parseDouble(appClosing);

            double expOpenOverride = Double.parseDouble(expOverride);
            double expClosing = Double.parseDouble(expWaterDip);

            boolean dipsMatch = (Double.compare(appOpen, expOpenOverride) == 0) &&
                    (Double.compare(appClose, expClosing) == 0);

            if (!(appStreamName.contains(StreamName) && dipsMatch && appComments.equals(Comments)))
            {
                seenValues.clear();
                break;
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.F2).build().perform();
            for (int i = 0; i < snsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
    }

    public void Navigate_to_TankTickets_tab(){
        FASession.findElement(FDC_TankTicketsTab).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(FDC_TankStreamsGrid));
    }

    public void Select_a_TankStream_using_StreamName(String StreamName){
        MobileElement tankStreamGrid = FASession.findElement(FDC_TankStreamsGrid).findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(tankStreamGrid,StreamName);
    }

    public void Enter_TankTickets_Info(String Ticket_Number, String Shipper, String Hauler,String Comments){
        WebElement tankTicketsGrid = FASession.findElement(FDC_TankTicketsGrid).findElement(Grid_DataTable);
        actions.moveToElement(tankTicketsGrid, 5, 30).click().build().perform();
        actions.sendKeys(Keys.TAB).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Ticket_Number);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Shipper);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Hauler);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Comments);
        actions.sendKeys(Keys.TAB).build().perform();
    }

    public void Enter_TankTicketsData_Info(String OpeningLiquidDip, String OpeningTemp, String Seal1_Off, String ClosingLiquidDip, String ClosingGravity, String ClosingObservedTemp, String ClosingTemp, String BSWVol, String Seal1_On){
        WebElement streamsDataGrid = FASession.findElement(FDC_TankTicketsDataGrid).findElement(Grid_DataTable);
        actions.moveToElement(streamsDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(OpeningLiquidDip);
        actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(OpeningTemp);
        actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Seal1_Off);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(ClosingLiquidDip);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(ClosingGravity);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(ClosingObservedTemp);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(ClosingTemp);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(BSWVol);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Seal1_On);
        actions.sendKeys(Keys.TAB).build().perform();
    }

    public void Validate_the_status_of_Added_TankTickets(String Ticket_Number, String Shipper, String Hauler, String Comments, String OpeningLiquidDip, String OpeningTemp, String Seal1_Off, String ClosingLiquidDip, String ClosingGravity, String ClosingObservedTemp, String ClosingTemp, String Seal1_On){
        MobileElement exportStatusGrid = FASession.findElement(FDC_ExportStatusGrid);
        FASession.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
        String appTicketNumber, appShipper, appHauler, appcomments, appType, appIsOpen, appLiquidDip, appTemp, appObservedTemp, appSeal_Off, appObservedGravity, appSeal_On;
        while (true) {
            List<MobileElement> tNum = exportStatusDataGrid.findElementsByXPath("//*");
            appTicketNumber = tNum.getLast().getAttribute("Value.Value");
            int tnsize = 0;
            if (appTicketNumber != null){
                tnsize = appTicketNumber.getBytes().length;
                for (int i = 0; i < tnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> shipperList = exportStatusDataGrid.findElementsByXPath("//*");
            appShipper = shipperList.getLast().getAttribute("Value.Value");
            if (appShipper != null) {
                int shipsize = appShipper.getBytes().length;
                for (int i = 0; i < shipsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> haulerList = exportStatusDataGrid.findElementsByXPath("//*");
            appHauler = haulerList.getLast().getAttribute("Value.Value");
            if (appHauler != null) {
                int hsize = appHauler.getBytes().length;
                for (int i = 0; i < hsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appcomments = Cmnt.getLast().getAttribute("Value.Value");
            if (appcomments != null) {
                int cmtsize = appcomments.getBytes().length;
                for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> isOpenList = exportStatusDataGrid.findElementsByXPath("//*");
            appIsOpen = isOpenList.getLast().getAttribute("Value.Value");
            if (appIsOpen != null) {
                int iosize = appIsOpen.getBytes().length;
                for (int i = 0; i < iosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> liqDipList = exportStatusDataGrid.findElementsByXPath("//*");
            appLiquidDip = liqDipList.getLast().getAttribute("Value.Value");
            if (appLiquidDip != null) {
                int ldsize = appLiquidDip.getBytes().length;
                for (int i = 0; i < ldsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> tempList = exportStatusDataGrid.findElementsByXPath("//*");
            appTemp = tempList.getLast().getAttribute("Value.Value");
            if (appTemp != null) {
                int tempsize = appTemp.getBytes().length;
                for (int i = 0; i < tempsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> obsTempList = exportStatusDataGrid.findElementsByXPath("//*");
            appObservedTemp = obsTempList.getLast().getAttribute("Value.Value");
            if (appObservedTemp != null) {
                int obsTempsize = appObservedTemp.getBytes().length;
                for (int i = 0; i < obsTempsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> gravityList = exportStatusDataGrid.findElementsByXPath("//*");
            appObservedGravity = gravityList.getLast().getAttribute("Value.Value");
            if (appObservedGravity != null) {
                int gsize = appObservedGravity.getBytes().length;
                for (int i = 0; i < gsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> sOnList = exportStatusDataGrid.findElementsByXPath("//*");
            appSeal_On = sOnList.getLast().getAttribute("Value.Value");
            if (appSeal_On != null) {
                int sOnsize = appSeal_On.getBytes().length;
                for (int i = 0; i < sOnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> sOffList = exportStatusDataGrid.findElementsByXPath("//*");
            appSeal_Off = sOffList.getLast().getAttribute("Value.Value");
            if (appSeal_Off != null) {
                int sOffsize = appSeal_Off.getBytes().length;
                for (int i = 0; i < sOffsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            double openLDip = Double.parseDouble(OpeningLiquidDip);
            String expOpenLDip = String.format("%.5f", openLDip);
            double openTemp = Double.parseDouble(OpeningTemp);
            String expOpenTemp = String.format("%.5f", openTemp);
            double sealOff = Double.parseDouble(Seal1_Off);
            String expSealOff = String.format("%.5f", sealOff);
            double sealOn = Double.parseDouble(Seal1_On);
            String expSealOn = String.format("%.5f", sealOn);
            double closeLDip = Double.parseDouble(ClosingLiquidDip);
            String expCloseLDip = String.format("%.5f", closeLDip);
            double closeGravity = Double.parseDouble(ClosingGravity);
            String expCloseGravity = String.format("%.5f", closeGravity);
            double closeObsTemp = Double.parseDouble(ClosingObservedTemp);
            String expCloseObsTemp = String.format("%.5f", closeObsTemp);
            double closeTemp = Double.parseDouble(ClosingTemp);
            String expCloseTemp = String.format("%.5f", closeTemp);

            double appLDip = safeParse(appLiquidDip);
            double appTempDouble = safeParse(appTemp);
            double appObsTempDouble = safeParse(appObservedTemp);
            double appSealOff = safeParse(appSeal_Off);
            double appSealOn = safeParse(appSeal_On);
            double appGrav = safeParse(appObservedGravity);

            double expOpenLiquidDip = Double.parseDouble(expOpenLDip);
            double expOpeningTemp = Double.parseDouble(expOpenTemp);
            double expSeal1Off = Double.parseDouble(expSealOff);
            double expSeal1On = Double.parseDouble(expSealOn);
            double expClosingLiquidDip = Double.parseDouble(expCloseLDip);
            double expClosingTemp = Double.parseDouble(expCloseTemp);
            double expClosingObsTemp = Double.parseDouble(expCloseObsTemp);
            double expClosingGravity = Double.parseDouble(expCloseGravity);

            boolean dipsMatch = false;


            if (appIsOpen != null) {
                if(appIsOpen.equals("True")){
                    dipsMatch = (Double.compare(appLDip, expOpenLiquidDip) == 0) &&
                            (Double.compare(appTempDouble, expOpeningTemp) == 0) &&
                            (Double.compare(appSealOff, expSeal1Off) == 0);
                } else if (appIsOpen.equals("False")) {
                    dipsMatch = (Double.compare(appLDip, expClosingLiquidDip) == 0) &&
                            (Double.compare(appObsTempDouble, expClosingObsTemp) == 0) &&
                            (Double.compare(appTempDouble, expClosingTemp) == 0) &&
                            (Double.compare(appGrav, expClosingGravity) == 0) &&
                            (Double.compare(appSealOn, expSeal1On) == 0);
                }
            }

            if (appTicketNumber == null)
            {
                if (dipsMatch)
                {
                        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                        List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                        appType = type.getLast().getAttribute("Value.Value");

                        if(appType.equals("Add"))
                        {
                            seenValues.clear();
                        }
                        else{
                            TestCase.fail("Selected Tank Tickets Data row is not having the Add Type");
                        }
                }
                else{
                    System.out.println("Entered Data and Application displayed data are not matching.....");
                }
            } else {
                if (appTicketNumber.equals(Ticket_Number)) {
                    assert appShipper != null;
                    if ((appShipper.equals(Shipper))) {
                        assert appHauler != null;
                        if ((appHauler.equals(Hauler))) {
                            assert appcomments != null;
                            if (appcomments.equals(Comments)) {
                                actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                                List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                                appType = type.getLast().getAttribute("Value.Value");

                                if (appType.equals("Add")) {
                                    seenValues.clear();
                                    break;
                                } else {
                                    TestCase.fail("Selected Tank Tickets Data row is not having the Add Type");
                                }
                            }
                        }
                    }
                }
            }

            if (appTicketNumber != null) {
                if(isDuplicate(appTicketNumber)){
                    TestCase.fail("Error : Data Rows ended");
                    break;
                }
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();

            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
    }

    public void Edit_TankTickets_Info(String Ticket_Number, String Shipper, String Hauler,String Comments){
        WebElement tankTicketsGrid = FASession.findElement(FDC_TankTicketsGrid).findElement(Grid_DataTable);
        actions.moveToElement(tankTicketsGrid, 5, 10).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_DOWN).keyUp(Keys.CONTROL).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Ticket_Number);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Shipper);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Hauler);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Comments);
        actions.sendKeys(Keys.TAB).build().perform();
    }

    public void Edit_TankTicketsData_Info(String OpeningLiquidDip, String OpeningTemp, String Seal1_Off, String ClosingLiquidDip, String ClosingGravity, String ClosingObservedTemp, String ClosingTemp, String BSWVol, String Seal1_On){
        WebElement streamsDataGrid = FASession.findElement(FDC_TankTicketsDataGrid).findElement(Grid_DataTable);
        actions.moveToElement(streamsDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(ClosingLiquidDip);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(ClosingGravity);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(ClosingObservedTemp);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(ClosingTemp);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(BSWVol);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Seal1_On);
        actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(OpeningLiquidDip);
        actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(OpeningTemp);
        actions.sendKeys(Keys.TAB).sendKeys(Keys.TAB).sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Seal1_Off);
        actions.sendKeys(Keys.TAB).build().perform();
    }

    public void Validate_the_status_of_Edited_TankTickets(String Ticket_Number, String Shipper, String Hauler, String Comments, String OpeningLiquidDip, String OpeningTemp, String Seal1_Off, String ClosingLiquidDip, String ClosingGravity, String ClosingObservedTemp, String ClosingTemp, String Seal1_On){
        MobileElement exportStatusGrid = FASession.findElement(FDC_ExportStatusGrid);
        FASession.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
        String appTicketNumber, appShipper, appHauler, appcomments, appType, appIsOpen, appLiquidDip, appTemp, appObservedTemp, appSeal_Off, appObservedGravity, appSeal_On;
        while (true) {
            List<MobileElement> tNum = exportStatusDataGrid.findElementsByXPath("//*");
            appTicketNumber = tNum.getLast().getAttribute("Value.Value");
            int tnsize = 0;
            if (appTicketNumber != null){
                tnsize = appTicketNumber.getBytes().length;
                for (int i = 0; i < tnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> shipperList = exportStatusDataGrid.findElementsByXPath("//*");
            appShipper = shipperList.getLast().getAttribute("Value.Value");
            if (appShipper != null) {
                int shipsize = appShipper.getBytes().length;
                for (int i = 0; i < shipsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> haulerList = exportStatusDataGrid.findElementsByXPath("//*");
            appHauler = haulerList.getLast().getAttribute("Value.Value");
            if (appHauler != null) {
                int hsize = appHauler.getBytes().length;
                for (int i = 0; i < hsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appcomments = Cmnt.getLast().getAttribute("Value.Value");
            if (appcomments != null) {
                int cmtsize = appcomments.getBytes().length;
                for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> isOpenList = exportStatusDataGrid.findElementsByXPath("//*");
            appIsOpen = isOpenList.getLast().getAttribute("Value.Value");
            if (appIsOpen != null) {
                int iosize = appIsOpen.getBytes().length;
                for (int i = 0; i < iosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> liqDipList = exportStatusDataGrid.findElementsByXPath("//*");
            appLiquidDip = liqDipList.getLast().getAttribute("Value.Value");
            if (appLiquidDip != null) {
                int ldsize = appLiquidDip.getBytes().length;
                for (int i = 0; i < ldsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> tempList = exportStatusDataGrid.findElementsByXPath("//*");
            appTemp = tempList.getLast().getAttribute("Value.Value");
            if (appTemp != null) {
                int tempsize = appTemp.getBytes().length;
                for (int i = 0; i < tempsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> obsTempList = exportStatusDataGrid.findElementsByXPath("//*");
            appObservedTemp = obsTempList.getLast().getAttribute("Value.Value");
            if (appObservedTemp != null) {
                int obsTempsize = appObservedTemp.getBytes().length;
                for (int i = 0; i < obsTempsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> gravityList = exportStatusDataGrid.findElementsByXPath("//*");
            appObservedGravity = gravityList.getLast().getAttribute("Value.Value");
            if (appObservedGravity != null) {
                int gsize = appObservedGravity.getBytes().length;
                for (int i = 0; i < gsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> sOnList = exportStatusDataGrid.findElementsByXPath("//*");
            appSeal_On = sOnList.getLast().getAttribute("Value.Value");
            if (appSeal_On != null) {
                int sOnsize = appSeal_On.getBytes().length;
                for (int i = 0; i < sOnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> sOffList = exportStatusDataGrid.findElementsByXPath("//*");
            appSeal_Off = sOffList.getLast().getAttribute("Value.Value");
            if (appSeal_Off != null) {
                int sOffsize = appSeal_Off.getBytes().length;
                for (int i = 0; i < sOffsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            double openLDip = Double.parseDouble(OpeningLiquidDip);
            String expOpenLDip = String.format("%.5f", openLDip);
            double openTemp = Double.parseDouble(OpeningTemp);
            String expOpenTemp = String.format("%.5f", openTemp);
            double sealOff = Double.parseDouble(Seal1_Off);
            String expSealOff = String.format("%.5f", sealOff);
            double sealOn = Double.parseDouble(Seal1_On);
            String expSealOn = String.format("%.5f", sealOn);
            double closeLDip = Double.parseDouble(ClosingLiquidDip);
            String expCloseLDip = String.format("%.5f", closeLDip);
            double closeGravity = Double.parseDouble(ClosingGravity);
            String expCloseGravity = String.format("%.5f", closeGravity);
            double closeObsTemp = Double.parseDouble(ClosingObservedTemp);
            String expCloseObsTemp = String.format("%.5f", closeObsTemp);
            double closeTemp = Double.parseDouble(ClosingTemp);
            String expCloseTemp = String.format("%.5f", closeTemp);

            double appLDip = safeParse(appLiquidDip);
            double appTempDouble = safeParse(appTemp);
            double appObsTempDouble = safeParse(appObservedTemp);
            double appSealOff = safeParse(appSeal_Off);
            double appSealOn = safeParse(appSeal_On);
            double appGrav = safeParse(appObservedGravity);

            double expOpenLiquidDip = Double.parseDouble(expOpenLDip);
            double expOpeningTemp = Double.parseDouble(expOpenTemp);
            double expSeal1Off = Double.parseDouble(expSealOff);
            double expSeal1On = Double.parseDouble(expSealOn);
            double expClosingLiquidDip = Double.parseDouble(expCloseLDip);
            double expClosingTemp = Double.parseDouble(expCloseTemp);
            double expClosingObsTemp = Double.parseDouble(expCloseObsTemp);
            double expClosingGravity = Double.parseDouble(expCloseGravity);

            boolean dipsMatch = false;


            if (appIsOpen != null) {
                if(appIsOpen.equals("True")){
                    dipsMatch = (Double.compare(appLDip, expOpenLiquidDip) == 0) &&
                            (Double.compare(appTempDouble, expOpeningTemp) == 0) &&
                            (Double.compare(appSealOff, expSeal1Off) == 0);
                } else if (appIsOpen.equals("False")) {
                    dipsMatch = (Double.compare(appLDip, expClosingLiquidDip) == 0) &&
                            (Double.compare(appObsTempDouble, expClosingObsTemp) == 0) &&
                            (Double.compare(appTempDouble, expClosingTemp) == 0) &&
                            (Double.compare(appGrav, expClosingGravity) == 0) &&
                            (Double.compare(appSealOn, expSeal1On) == 0);
                }
            }

            if (appTicketNumber == null)
            {
                if (dipsMatch)
                {
                    actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                    List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                    appType = type.getLast().getAttribute("Value.Value");

                    if(appType.equals("Change"))
                    {
                        seenValues.clear();
                    }
                    else{
                        TestCase.fail("Selected Tank Tickets Data row is not having the Change Type");
                    }

                }
                else{
                    System.out.println("Entered Data and the Application Data are not matching.....");
                }
            } else {
                if (appTicketNumber.equals(Ticket_Number)) {
                    assert appShipper != null;
                    if ((appShipper.equals(Shipper))) {
                        assert appHauler != null;
                        if ((appHauler.equals(Hauler))) {
                            assert appcomments != null;
                            if (appcomments.equals(Comments)) {
                                actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                                List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                                appType = type.getLast().getAttribute("Value.Value");

                                if (appType.equals("Change")) {
                                    seenValues.clear();
                                    break;
                                } else {
                                    TestCase.fail("Selected Tank Tickets Data row is not having the Change Type");
                                }
                            }
                        }
                    }
                }
            }

            if (appTicketNumber != null) {
                if(isDuplicate(appTicketNumber)){
                    TestCase.fail("Error : Data Rows ended");
                    break;
                }
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();

            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
    }

    public void Validate_the_TankTicket_is_not_added(String Ticket_Number, String Shipper, String Hauler, String Comments, String OpeningLiquidDip, String OpeningTemp, String Seal1_Off, String ClosingLiquidDip, String ClosingGravity, String ClosingObservedTemp, String ClosingTemp, String Seal1_On){
        MobileElement exportStatusGrid = FASession.findElement(FDC_ExportStatusGrid);
        FASession.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
        String appTicketNumber, appShipper, appHauler, appcomments, appType, appIsOpen, appLiquidDip, appTemp, appObservedTemp, appSeal_Off, appObservedGravity, appSeal_On;
        while (true) {
            List<MobileElement> tNum = exportStatusDataGrid.findElementsByXPath("//*");
            appTicketNumber = tNum.getLast().getAttribute("Value.Value");
            int tnsize = 0;
            if (appTicketNumber != null){
                tnsize = appTicketNumber.getBytes().length;
                for (int i = 0; i < tnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> shipperList = exportStatusDataGrid.findElementsByXPath("//*");
            appShipper = shipperList.getLast().getAttribute("Value.Value");
            if (appShipper != null) {
                int shipsize = appShipper.getBytes().length;
                for (int i = 0; i < shipsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> haulerList = exportStatusDataGrid.findElementsByXPath("//*");
            appHauler = haulerList.getLast().getAttribute("Value.Value");
            if (appHauler != null) {
                int hsize = appHauler.getBytes().length;
                for (int i = 0; i < hsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appcomments = Cmnt.getLast().getAttribute("Value.Value");
            if (appcomments != null) {
                int cmtsize = appcomments.getBytes().length;
                for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> isOpenList = exportStatusDataGrid.findElementsByXPath("//*");
            appIsOpen = isOpenList.getLast().getAttribute("Value.Value");
            if (appIsOpen != null) {
                int iosize = appIsOpen.getBytes().length;
                for (int i = 0; i < iosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> liqDipList = exportStatusDataGrid.findElementsByXPath("//*");
            appLiquidDip = liqDipList.getLast().getAttribute("Value.Value");
            if (appLiquidDip != null) {
                int ldsize = appLiquidDip.getBytes().length;
                for (int i = 0; i < ldsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> tempList = exportStatusDataGrid.findElementsByXPath("//*");
            appTemp = tempList.getLast().getAttribute("Value.Value");
            if (appTemp != null) {
                int tempsize = appTemp.getBytes().length;
                for (int i = 0; i < tempsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> obsTempList = exportStatusDataGrid.findElementsByXPath("//*");
            appObservedTemp = obsTempList.getLast().getAttribute("Value.Value");
            if (appObservedTemp != null) {
                int obsTempsize = appObservedTemp.getBytes().length;
                for (int i = 0; i < obsTempsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> gravityList = exportStatusDataGrid.findElementsByXPath("//*");
            appObservedGravity = gravityList.getLast().getAttribute("Value.Value");
            if (appObservedGravity != null) {
                int gsize = appObservedGravity.getBytes().length;
                for (int i = 0; i < gsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> sOnList = exportStatusDataGrid.findElementsByXPath("//*");
            appSeal_On = sOnList.getLast().getAttribute("Value.Value");
            if (appSeal_On != null) {
                int sOnsize = appSeal_On.getBytes().length;
                for (int i = 0; i < sOnsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> sOffList = exportStatusDataGrid.findElementsByXPath("//*");
            appSeal_Off = sOffList.getLast().getAttribute("Value.Value");
            if (appSeal_Off != null) {
                int sOffsize = appSeal_Off.getBytes().length;
                for (int i = 0; i < sOffsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            double openLDip = Double.parseDouble(OpeningLiquidDip);
            String expOpenLDip = String.format("%.5f", openLDip);
            double openTemp = Double.parseDouble(OpeningTemp);
            String expOpenTemp = String.format("%.5f", openTemp);
            double sealOff = Double.parseDouble(Seal1_Off);
            String expSealOff = String.format("%.5f", sealOff);
            double sealOn = Double.parseDouble(Seal1_On);
            String expSealOn = String.format("%.5f", sealOn);
            double closeLDip = Double.parseDouble(ClosingLiquidDip);
            String expCloseLDip = String.format("%.5f", closeLDip);
            double closeGravity = Double.parseDouble(ClosingGravity);
            String expCloseGravity = String.format("%.5f", closeGravity);
            double closeObsTemp = Double.parseDouble(ClosingObservedTemp);
            String expCloseObsTemp = String.format("%.5f", closeObsTemp);
            double closeTemp = Double.parseDouble(ClosingTemp);
            String expCloseTemp = String.format("%.5f", closeTemp);

            double appLDip = safeParse(appLiquidDip);
            double appTempDouble = safeParse(appTemp);
            double appObsTempDouble = safeParse(appObservedTemp);
            double appSealOff = safeParse(appSeal_Off);
            double appSealOn = safeParse(appSeal_On);
            double appGrav = safeParse(appObservedGravity);

            double expOpenLiquidDip = Double.parseDouble(expOpenLDip);
            double expOpeningTemp = Double.parseDouble(expOpenTemp);
            double expSeal1Off = Double.parseDouble(expSealOff);
            double expSeal1On = Double.parseDouble(expSealOn);
            double expClosingLiquidDip = Double.parseDouble(expCloseLDip);
            double expClosingTemp = Double.parseDouble(expCloseTemp);
            double expClosingObsTemp = Double.parseDouble(expCloseObsTemp);
            double expClosingGravity = Double.parseDouble(expCloseGravity);

            boolean dipsMatch = false;


            if (appIsOpen != null) {
                if(appIsOpen.equals("True")){
                    dipsMatch = (Double.compare(appLDip, expOpenLiquidDip) == 0) &&
                            (Double.compare(appTempDouble, expOpeningTemp) == 0) &&
                            (Double.compare(appSealOff, expSeal1Off) == 0);
                } else if (appIsOpen.equals("False")) {
                    dipsMatch = (Double.compare(appLDip, expClosingLiquidDip) == 0) &&
                            (Double.compare(appObsTempDouble, expClosingObsTemp) == 0) &&
                            (Double.compare(appTempDouble, expClosingTemp) == 0) &&
                            (Double.compare(appGrav, expClosingGravity) == 0) &&
                            (Double.compare(appSealOn, expSeal1On) == 0);
                }
            }

            if (appTicketNumber == null)
            {
                if (dipsMatch)
                {
                        TestCase.fail("Cancelled Tank Ticket info is displayed in the Export Status");
                }
                else{
                    System.out.println("Cancelled Tank Ticket Data is not added");
                }
            } else {
                if (appTicketNumber.equals(Ticket_Number)) {
                    assert appShipper != null;
                    if ((appShipper.equals(Shipper))) {
                        assert appHauler != null;
                        if ((appHauler.equals(Hauler))) {
                            assert appcomments != null;
                            if (appcomments.equals(Comments)) {

                                    TestCase.fail("Cancelled Tank Ticket info is displayed in the Export Status");
                            }
                        }
                    }
                }
                else System.out.println("Cancelled Tank Ticket is not added");
            }

            if (appTicketNumber != null) {
                if(isDuplicate(appTicketNumber)){
                    System.out.println("Data Rows ended");
                    break;
                }
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();

            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
    }

    public void Navigate_to_LACT_Tickets_Tab(){
        FASession.findElement(FDC_LACTTicketsTab).click();
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(FDC_LACTTicketsGrid));
    }

    public void Select_a_LACTStream_using_StreamName(String StreamName){
        MobileElement lactStreamGrid = FASession.findElement(FDC_LACTStreamsGrid).findElement(Grid_DataTable);
        common.WellSelectionFromWellsDataGrid(lactStreamGrid,StreamName);
    }

    public void Enter_LACTTickets_Info(String Ticket_Number, String Totalizer_Override, String Totalizer_Closing, String Avg_Temp, String Run_Temp, String BSW_Vol, String Corrected_Gravity, String Meter_Factor_Override, String Pressure_Factor_Override, String Comments){
        WebElement lactTicketsGrid = FASession.findElement(FDC_LACTTicketsGrid).findElement(Grid_DataTable);
        actions.moveToElement(lactTicketsGrid, 5, 10).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_DOWN).keyUp(Keys.CONTROL).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        actions.sendKeys(Keys.TAB).build().perform();
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
//        String lactEndDate = today.format(formatter);
//        actions.keyDown(Keys.ALT).sendKeys(Keys.ARROW_DOWN).keyUp(Keys.ALT).build().perform();
//        String dtBtn = "Today: "+lactEndDate;
//        FASession.findElement(By.name(dtBtn)).click();
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Ticket_Number);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Totalizer_Override);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Totalizer_Closing);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Avg_Temp);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Run_Temp);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(BSW_Vol);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Corrected_Gravity);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Meter_Factor_Override);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Pressure_Factor_Override);
        actions.sendKeys(Keys.TAB).build().perform();
        actions.sendKeys(Comments);
        actions.sendKeys(Keys.TAB).build().perform();
    }

    public void Validate_the_status_of_Added_LACTTickets_Data(String Ticket_Number, String Totalizer_Override, String Totalizer_Closing, String Avg_Temp, String Run_Temp, String BSW_Vol, String Corrected_Gravity, String Meter_Factor_Override, String Pressure_Factor_Override, String Comments){
        MobileElement exportStatusGrid = FASession.findElement(FDC_ExportStatusGrid);
        FASession.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
        String appTicketNumber, appTotalOverride, appTotalClosing, appCorrectedGravity, appAvgTemp, appRunTemp, appBSWVol, appMFOverride, appPFOverride, appComments, appType;
        while (true) {
            List<MobileElement> tNum = exportStatusDataGrid.findElementsByXPath("//*");
            appTicketNumber = tNum.getLast().getAttribute("Value.Value");
            int tNumSize = 0;
            if (appTicketNumber != null) {
                tNumSize = appTicketNumber.getBytes().length;
                for (int i = 0; i < tNumSize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> tOverride = exportStatusDataGrid.findElementsByXPath("//*");
            appTotalOverride = tOverride.getLast().getAttribute("Value.Value");
            if (appTotalOverride != null) {
                int tosize = appTotalOverride.getBytes().length;
                for (int i = 0; i < tosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> tClosing = exportStatusDataGrid.findElementsByXPath("//*");
            appTotalClosing = tClosing.getLast().getAttribute("Value.Value");
            if (appTotalClosing != null) {
                int tcsize = appTotalClosing.getBytes().length;
                for (int i = 0; i < tcsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> cGravity = exportStatusDataGrid.findElementsByXPath("//*");
            appCorrectedGravity = cGravity.getLast().getAttribute("Value.Value");
            if (appCorrectedGravity != null) {
                int cgsize = appCorrectedGravity.getBytes().length;
                for (int i = 0; i < cgsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> aTemp = exportStatusDataGrid.findElementsByXPath("//*");
            appAvgTemp = aTemp.getLast().getAttribute("Value.Value");
            if (appAvgTemp != null) {
                int atsize = appAvgTemp.getBytes().length;
                for (int i = 0; i < atsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> rTemp = exportStatusDataGrid.findElementsByXPath("//*");
            appRunTemp = rTemp.getLast().getAttribute("Value.Value");
            if (appRunTemp != null) {
                int rtsize = appRunTemp.getBytes().length;
                for (int i = 0; i < rtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> bVol = exportStatusDataGrid.findElementsByXPath("//*");
            appBSWVol = bVol.getLast().getAttribute("Value.Value");
            if (appBSWVol != null) {
                int bvsize = appBSWVol.getBytes().length;
                for (int i = 0; i < bvsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> mfOverride = exportStatusDataGrid.findElementsByXPath("//*");
            appMFOverride = mfOverride.getLast().getAttribute("Value.Value");
            if (appMFOverride != null) {
                int mfosize = appMFOverride.getBytes().length;
                for (int i = 0; i < mfosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> pfOverride = exportStatusDataGrid.findElementsByXPath("//*");
            appPFOverride = pfOverride.getLast().getAttribute("Value.Value");
            if (appPFOverride != null) {
                int pfosize = appPFOverride.getBytes().length;
                for (int i = 0; i < pfosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appComments = Cmnt.getLast().getAttribute("Value.Value");
            int cmtsize = appComments.getBytes().length;
            for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            double expTotalOverride = Double.parseDouble(Totalizer_Override);
            double expTotalClosing = Double.parseDouble(Totalizer_Closing);
            double expAvgTemp = Double.parseDouble(Avg_Temp);
            double expRunTemp = Double.parseDouble(Run_Temp);
            double expBSWVol = Double.parseDouble(BSW_Vol);
            double expCorrectedGravity = Double.parseDouble(Corrected_Gravity);
            double expMFOverride = Double.parseDouble(Meter_Factor_Override);
            double expPFOverride = Double.parseDouble(Pressure_Factor_Override);

            assert appTotalOverride != null;
            double actTotalOverride = Double.parseDouble(appTotalOverride);
            assert appTotalClosing != null;
            double actTotalClosing = Double.parseDouble(appTotalClosing);
            assert appAvgTemp != null;
            double actAvgTemp = Double.parseDouble(appAvgTemp);
            assert appRunTemp != null;
            double actRunTemp = Double.parseDouble(appRunTemp);
            assert appBSWVol != null;
            double actBSWVol = Double.parseDouble(appBSWVol);
            assert appCorrectedGravity != null;
            double actCorrectedGravity = Double.parseDouble(appCorrectedGravity);
            assert appMFOverride != null;
            double actMFOverride = Double.parseDouble(appMFOverride);
            assert appPFOverride != null;
            double actPFOverride = Double.parseDouble(appPFOverride);

            boolean dipsMatch = (Double.compare(expTotalOverride, actTotalOverride) == 0) &&
                            (Double.compare(expTotalClosing, actTotalClosing) == 0) &&
                            (Double.compare(expCorrectedGravity, actCorrectedGravity) == 0)  &&
                            (Double.compare(expAvgTemp, actAvgTemp) == 0 ) &&
                            (Double.compare(expRunTemp, actRunTemp) == 0)  &&
                            (Double.compare(expBSWVol, actBSWVol) == 0)  &&
                            (Double.compare(expMFOverride, actMFOverride) == 0) &&
                            (Double.compare(expPFOverride, actPFOverride) == 0) ;

            assert appTicketNumber != null;
            if (appTicketNumber.equals(Ticket_Number))
            {
                if (dipsMatch)
                {
                    if (appComments.equals(Comments))
                    {
                        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                        List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                        appType = type.getLast().getAttribute("Value.Value");

                        if(appType.equals("Add"))
                        {
                            seenValues.clear();
                            break;
                        }
                        else{
                            TestCase.fail("Selected LACT Tickets Data row is not having the Add Type");
                        }
                    }
                }
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            for (int i = 0; i < tNumSize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
    }

    public void Validate_the_status_of_Edited_LACTTickets_Data(String Ticket_Number, String Totalizer_Override, String Totalizer_Closing, String Avg_Temp, String Run_Temp, String BSW_Vol, String Corrected_Gravity, String Meter_Factor_Override, String Pressure_Factor_Override, String Comments){
        MobileElement exportStatusGrid = FASession.findElement(FDC_ExportStatusGrid);
        FASession.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
        String appTicketNumber, appTotalOverride, appTotalClosing, appCorrectedGravity, appAvgTemp, appRunTemp, appBSWVol, appMFOverride, appPFOverride, appComments, appType;
        while (true) {
            List<MobileElement> tNum = exportStatusDataGrid.findElementsByXPath("//*");
            appTicketNumber = tNum.getLast().getAttribute("Value.Value");
            int tNumSize = 0;
            if (appTicketNumber != null) {
                tNumSize = appTicketNumber.getBytes().length;
                for (int i = 0; i < tNumSize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> tOverride = exportStatusDataGrid.findElementsByXPath("//*");
            appTotalOverride = tOverride.getLast().getAttribute("Value.Value");
            if (appTotalOverride != null) {
                int tosize = appTotalOverride.getBytes().length;
                for (int i = 0; i < tosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> tClosing = exportStatusDataGrid.findElementsByXPath("//*");
            appTotalClosing = tClosing.getLast().getAttribute("Value.Value");
            if (appTotalClosing != null) {
                int tcsize = appTotalClosing.getBytes().length;
                for (int i = 0; i < tcsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> cGravity = exportStatusDataGrid.findElementsByXPath("//*");
            appCorrectedGravity = cGravity.getLast().getAttribute("Value.Value");
            if (appCorrectedGravity != null) {
                int cgsize = appCorrectedGravity.getBytes().length;
                for (int i = 0; i < cgsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> aTemp = exportStatusDataGrid.findElementsByXPath("//*");
            appAvgTemp = aTemp.getLast().getAttribute("Value.Value");
            if (appAvgTemp != null) {
                int atsize = appAvgTemp.getBytes().length;
                for (int i = 0; i < atsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> rTemp = exportStatusDataGrid.findElementsByXPath("//*");
            appRunTemp = rTemp.getLast().getAttribute("Value.Value");
            if (appRunTemp != null) {
                int rtsize = appRunTemp.getBytes().length;
                for (int i = 0; i < rtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> bVol = exportStatusDataGrid.findElementsByXPath("//*");
            appBSWVol = bVol.getLast().getAttribute("Value.Value");
            if (appBSWVol != null) {
                int bvsize = appBSWVol.getBytes().length;
                for (int i = 0; i < bvsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> mfOverride = exportStatusDataGrid.findElementsByXPath("//*");
            appMFOverride = mfOverride.getLast().getAttribute("Value.Value");
            if (appMFOverride != null) {
                int mfosize = appMFOverride.getBytes().length;
                for (int i = 0; i < mfosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> pfOverride = exportStatusDataGrid.findElementsByXPath("//*");
            appPFOverride = pfOverride.getLast().getAttribute("Value.Value");
            if (appPFOverride != null) {
                int pfosize = appPFOverride.getBytes().length;
                for (int i = 0; i < pfosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appComments = Cmnt.getLast().getAttribute("Value.Value");
            int cmtsize = appComments.getBytes().length;
            for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            double expTotalOverride = Double.parseDouble(Totalizer_Override);
            double expTotalClosing = Double.parseDouble(Totalizer_Closing);
            double expAvgTemp = Double.parseDouble(Avg_Temp);
            double expRunTemp = Double.parseDouble(Run_Temp);
            double expBSWVol = Double.parseDouble(BSW_Vol);
            double expCorrectedGravity = Double.parseDouble(Corrected_Gravity);
            double expMFOverride = Double.parseDouble(Meter_Factor_Override);
            double expPFOverride = Double.parseDouble(Pressure_Factor_Override);

            assert appTotalOverride != null;
            double actTotalOverride = Double.parseDouble(appTotalOverride);
            assert appTotalClosing != null;
            double actTotalClosing = Double.parseDouble(appTotalClosing);
            assert appAvgTemp != null;
            double actAvgTemp = Double.parseDouble(appAvgTemp);
            assert appRunTemp != null;
            double actRunTemp = Double.parseDouble(appRunTemp);
            assert appBSWVol != null;
            double actBSWVol = Double.parseDouble(appBSWVol);
            assert appCorrectedGravity != null;
            double actCorrectedGravity = Double.parseDouble(appCorrectedGravity);
            assert appMFOverride != null;
            double actMFOverride = Double.parseDouble(appMFOverride);
            assert appPFOverride != null;
            double actPFOverride = Double.parseDouble(appPFOverride);

            boolean dipsMatch = (Double.compare(expTotalOverride, actTotalOverride) == 0) &&
                    (Double.compare(expTotalClosing, actTotalClosing) == 0) &&
                    (Double.compare(expCorrectedGravity, actCorrectedGravity) == 0)  &&
                    (Double.compare(expAvgTemp, actAvgTemp) == 0 ) &&
                    (Double.compare(expRunTemp, actRunTemp) == 0)  &&
                    (Double.compare(expBSWVol, actBSWVol) == 0)  &&
                    (Double.compare(expMFOverride, actMFOverride) == 0) &&
                    (Double.compare(expPFOverride, actPFOverride) == 0) ;

            assert appTicketNumber != null;
            if (appTicketNumber.equals(Ticket_Number))
            {
                if (dipsMatch)
                {
                    if (appComments.equals(Comments))
                    {
                        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                        List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                        appType = type.getLast().getAttribute("Value.Value");

                        if(appType.equals("Change"))
                        {
                            seenValues.clear();
                            break;
                        }
                        else{
                            TestCase.fail("Selected LACT Tickets Data row is not having the Change Type");
                        }
                    }
                }
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            for (int i = 0; i < tNumSize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
    }

    public void Validate_the_LACTTicket_is_not_added(String Ticket_Number, String Totalizer_Override, String Totalizer_Closing, String Avg_Temp, String Run_Temp, String BSW_Vol, String Corrected_Gravity, String Meter_Factor_Override, String Pressure_Factor_Override, String Comments){
        MobileElement exportStatusGrid = FASession.findElement(FDC_ExportStatusGrid);
        FASession.findElement(FDC_Export_PendingRadioBtn).click();
        MobileElement exportStatusDataGrid = exportStatusGrid.findElement(Grid_DataTable);

        actions.moveToElement(exportStatusDataGrid, 5, 5).click().build().perform();
        actions.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.F2).build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).build().perform();
        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
        String appTicketNumber, appTotalOverride, appTotalClosing, appCorrectedGravity, appAvgTemp, appRunTemp, appBSWVol, appMFOverride, appPFOverride, appComments, appType = null;
        while (true) {
            List<MobileElement> tNum = exportStatusDataGrid.findElementsByXPath("//*");
            appTicketNumber = tNum.getLast().getAttribute("Value.Value");
            int tNumSize = 0;
            if (appTicketNumber != null) {
                tNumSize = appTicketNumber.getBytes().length;
                for (int i = 0; i < tNumSize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> tOverride = exportStatusDataGrid.findElementsByXPath("//*");
            appTotalOverride = tOverride.getLast().getAttribute("Value.Value");
            if (appTotalOverride != null) {
                int tosize = appTotalOverride.getBytes().length;
                for (int i = 0; i < tosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> tClosing = exportStatusDataGrid.findElementsByXPath("//*");
            appTotalClosing = tClosing.getLast().getAttribute("Value.Value");
            if (appTotalClosing != null) {
                int tcsize = appTotalClosing.getBytes().length;
                for (int i = 0; i < tcsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> cGravity = exportStatusDataGrid.findElementsByXPath("//*");
            appCorrectedGravity = cGravity.getLast().getAttribute("Value.Value");
            if (appCorrectedGravity != null) {
                int cgsize = appCorrectedGravity.getBytes().length;
                for (int i = 0; i < cgsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> aTemp = exportStatusDataGrid.findElementsByXPath("//*");
            appAvgTemp = aTemp.getLast().getAttribute("Value.Value");
            if (appAvgTemp != null) {
                int atsize = appAvgTemp.getBytes().length;
                for (int i = 0; i < atsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> rTemp = exportStatusDataGrid.findElementsByXPath("//*");
            appRunTemp = rTemp.getLast().getAttribute("Value.Value");
            if (appRunTemp != null) {
                int rtsize = appRunTemp.getBytes().length;
                for (int i = 0; i < rtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> bVol = exportStatusDataGrid.findElementsByXPath("//*");
            appBSWVol = bVol.getLast().getAttribute("Value.Value");
            if (appBSWVol != null) {
                int bvsize = appBSWVol.getBytes().length;
                for (int i = 0; i < bvsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> mfOverride = exportStatusDataGrid.findElementsByXPath("//*");
            appMFOverride = mfOverride.getLast().getAttribute("Value.Value");
            if (appMFOverride != null) {
                int mfosize = appMFOverride.getBytes().length;
                for (int i = 0; i < mfosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> pfOverride = exportStatusDataGrid.findElementsByXPath("//*");
            appPFOverride = pfOverride.getLast().getAttribute("Value.Value");
            if (appPFOverride != null) {
                int pfosize = appPFOverride.getBytes().length;
                for (int i = 0; i < pfosize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            }

            actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
            List<MobileElement> Cmnt = exportStatusDataGrid.findElementsByXPath("//*");
            appComments = Cmnt.getLast().getAttribute("Value.Value");
            int cmtsize = appComments.getBytes().length;
            for (int i = 0; i < cmtsize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}

            double expTotalOverride = Double.parseDouble(Totalizer_Override);
            double expTotalClosing = Double.parseDouble(Totalizer_Closing);
            double expAvgTemp = Double.parseDouble(Avg_Temp);
            double expRunTemp = Double.parseDouble(Run_Temp);
            double expBSWVol = Double.parseDouble(BSW_Vol);
            double expCorrectedGravity = Double.parseDouble(Corrected_Gravity);
            double expMFOverride = Double.parseDouble(Meter_Factor_Override);
            double expPFOverride = Double.parseDouble(Pressure_Factor_Override);

            assert appTotalOverride != null;
            double actTotalOverride = Double.parseDouble(appTotalOverride);
            assert appTotalClosing != null;
            double actTotalClosing = Double.parseDouble(appTotalClosing);
            assert appAvgTemp != null;
            double actAvgTemp = Double.parseDouble(appAvgTemp);
            assert appRunTemp != null;
            double actRunTemp = Double.parseDouble(appRunTemp);
            assert appBSWVol != null;
            double actBSWVol = Double.parseDouble(appBSWVol);
            assert appCorrectedGravity != null;
            double actCorrectedGravity = Double.parseDouble(appCorrectedGravity);
            assert appMFOverride != null;
            double actMFOverride = Double.parseDouble(appMFOverride);
            assert appPFOverride != null;
            double actPFOverride = Double.parseDouble(appPFOverride);

            boolean dipsMatch = (Double.compare(expTotalOverride, actTotalOverride) == 0) &&
                    (Double.compare(expTotalClosing, actTotalClosing) == 0) &&
                    (Double.compare(expCorrectedGravity, actCorrectedGravity) == 0)  &&
                    (Double.compare(expAvgTemp, actAvgTemp) == 0 ) &&
                    (Double.compare(expRunTemp, actRunTemp) == 0)  &&
                    (Double.compare(expBSWVol, actBSWVol) == 0)  &&
                    (Double.compare(expMFOverride, actMFOverride) == 0) &&
                    (Double.compare(expPFOverride, actPFOverride) == 0) ;

            assert appTicketNumber != null;
            if (appTicketNumber.equals(Ticket_Number))
            {
                if (dipsMatch)
                {
                    if (appComments.equals(Comments))
                    {
                        actions.sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.F2).build().perform();
                        List<MobileElement> type = exportStatusDataGrid.findElementsByXPath("//*");
                        appType = type.getLast().getAttribute("Value.Value");

                        if(appType.equals("Change") || appType.equals("Add"))
                        {
                            seenValues.clear();
                            TestCase.fail("Cancelled LACT Ticket row is displayed in the Export Status grid");
                            break;
                        }

                    }
                }
            }
            if(isDuplicate(appTicketNumber)){
                System.out.println("Data Rows ended");
                break;
            }
            actions.keyDown(Keys.CONTROL).sendKeys(Keys.ARROW_LEFT).keyUp(Keys.CONTROL).sendKeys(Keys.ARROW_RIGHT).sendKeys(Keys.ARROW_RIGHT).build().perform();
            //for (int i = 0; i < tNumSize; i++) {exportStatusDataGrid.sendKeys(Keys.ARROW_RIGHT);}
            exportStatusDataGrid.sendKeys(Keys.ARROW_DOWN);
            exportStatusDataGrid.sendKeys(Keys.F2);
        }
        if(appType == null){
            System.out.println("Pass : Cancelled Data row is not added into the Export Status grid...");
        }
    }
}
