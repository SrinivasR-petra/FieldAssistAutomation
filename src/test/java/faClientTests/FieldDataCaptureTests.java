package faClientTests;

import TestUtils.FABaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static TestUtils.OABaseTest.OASession;

public class FieldDataCaptureTests extends FABaseTest {
    @AfterMethod(alwaysRun = true)
    public void CloseTabs(){
        try {
            if (OASession != null)
                OASession.close();
            Switch_to_ParentWindow(FASession);
            boolean mainWindow = FASession.findElementByAccessibilityId("MapleMainForm").isEnabled();
            if (!mainWindow) {
                Actions actions = new Actions(FASession);
                String parentWinHandle = FASession.getWindowHandle();
                for (String handle : FASession.getWindowHandles()) {
                    if (!handle.equals(parentWinHandle)) {
                        FASession.switchTo().window(handle);
                        System.out.println("Switched to new window: " + FASession.getTitle());
                    }
                }

                actions.keyDown(Keys.ALT).sendKeys(Keys.F4).keyUp(Keys.ALT).build().perform();
                FASession.switchTo().window(parentWinHandle);
            }
            FASession.findElement(By.name("Windows")).click();
            FASession.findElement(By.name("Close All")).click();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void SelectDateForAdd_should_be_one_day_minus_of_end_date(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Navigate_to_TankLevel_tab();
        fdc.Validate_AddDate_is_one_day_lessthan_EndDate();
    }

    @Test
    public void Add_TankLevelData_cancel(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_TankLevel_tab();
        fdc.Select_a_Tank_using_TankCode("8423290188");
        fdc.Click_Add_button();
        fdc.Enter_TankLevelData("48","36","Test Data");
        fdc.Click_Cancel_button();
        fdc.Click_Yes_in_Confirmation_dialog();
        fdc.Validate_the_TankLevelData_is_not_added("8423290188","50","30","Test Data");
    }

    @Test
    public void Add_TankLevelData_revert(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_TankLevel_tab();
        fdc.Select_a_Tank_using_TankCode("8423290188");
        fdc.Click_Add_button();
        fdc.Enter_TankLevelData("48","36","Test Data");
        fdc.Click_Revert_button();
        fdc.Click_Yes_in_Confirmation_dialog();
        fdc.Validate_the_TankLevelData_is_not_added("8423290188","50","30","Test Data");
    }

    @Test
    public void Add_TankLevelData_success(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_TankLevel_tab();
        fdc.Select_a_Tank_using_TankCode("8423290189");
        fdc.Click_Add_button();
        fdc.Enter_TankLevelData("50","30","Test Data");
        fdc.Click_Save_button();
        fdc.Click_Yes_in_Continue_dialog();
        fdc.Validate_the_status_of_Added_TankLevelData("8423290189","50","30","Test Data");
    }

    @Test
    public void Edit_TankLevelData_success(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_TankLevel_tab();
        fdc.Select_a_Tank_using_TankCode("8423290189");
        fdc.Enter_TankLevelData("48","18","Edit Test Data");
        fdc.Click_Save_button();
        fdc.Click_Yes_in_Continue_dialog();
        fdc.Validate_the_status_of_Edited_TankLevelData("8423290189","48","18","Edit Test Data");
    }

    @Test
    public void Add_Oil_Water_Gas_StreamsData_cancel(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_Oil_Water_Gas_Meters_tab();
        fdc.Select_a_Stream_using_Stream_Name("SOA SCHAR SE 0011 - 2423290242");
        fdc.Click_Add_button();
        fdc.Enter_StreamsData("0","250","Test Data");
        fdc.Click_Cancel_button();
        fdc.Click_Yes_in_Confirmation_dialog();
        fdc.Validate_the_StreamsData_is_not_added("SOA SCHAR SE 0011 - 2423290242","0","250","Test Data");
    }

    @Test
    public void Add_Oil_Water_Gas_StreamsData_revert(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_Oil_Water_Gas_Meters_tab();
        fdc.Select_a_Stream_using_Stream_Name("SOA SCHAR SE 0011 - 2423290242");
        fdc.Click_Add_button();
        fdc.Enter_StreamsData("0","250","Test Data");
        fdc.Click_Revert_button();
        fdc.Click_Yes_in_Confirmation_dialog();
        fdc.Validate_the_StreamsData_is_not_added("SOA SCHAR SE 0011 - 2423290242","0","250","Test Data");
    }

    @Test
    public void Add_Oil_Water_Gas_StreamsData_success(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_Oil_Water_Gas_Meters_tab();
        fdc.Select_a_Stream_using_Stream_Name("SOA SCHAR SE 0011 - 2423290242");
        fdc.Click_Add_button();
        fdc.Enter_StreamsData("0","120","Test");
        fdc.Click_Save_button();
        fdc.Click_Yes_in_Continue_dialog();
        fdc.Validate_the_status_of_Added_StreamsData("SOA SCHAR SE 0011 - 2423290242","0","120","Test");
    }

    @Test
    public void Edit_Oil_Water_Gas_StreamsData_success(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_Oil_Water_Gas_Meters_tab();
        fdc.Select_a_Stream_using_Stream_Name("SOA SCHAR SE 0011 - 2423290242");
        fdc.Enter_StreamsData("0","300","Update");
        fdc.Click_Save_button();
        fdc.Click_Yes_in_Continue_dialog();
        fdc.Validate_the_status_of_Edited_StreamsData("SOA SCHAR SE 0011 - 2423290242","0","300","Update");
    }

    @Test
    public void Add_TankTicketsData_cancel(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_TankTickets_tab();
        fdc.Select_a_TankStream_using_StreamName("BG 14 CTB OIL T-500D - 8423290190_TKT");
        fdc.Click_Add_button();
        fdc.Enter_TankTickets_Info("11111111","Enterprise Crude Oil, LLC", "Enterprise Crude Oil, LLC", "Sold from Oil Tank");
        fdc.Enter_TankTicketsData_Info("111", "11", "0", "22", "33", "88", "89", "1", "1234567");
        fdc.Click_Cancel_button();
        fdc.Click_Yes_in_Confirmation_dialog();
        fdc.Validate_the_TankTicket_is_not_added("11111111","Enterprise Crude Oil, LLC", "Enterprise Crude Oil, LLC", "Sold from Oil Tank","111", "11", "0", "22", "33", "88", "89", "1234567");
    }

    @Test
    public void Add_TankTicketsData_revert(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_TankTickets_tab();
        fdc.Select_a_TankStream_using_StreamName("BG 14 CTB OIL T-500D - 8423290190_TKT");
        fdc.Click_Add_button();
        fdc.Enter_TankTickets_Info("11111111","Enterprise Crude Oil, LLC", "Enterprise Crude Oil, LLC", "Sold from Oil Tank");
        fdc.Enter_TankTicketsData_Info("111", "11", "0", "22", "33", "88", "89", "1", "1234567");
        fdc.Click_Revert_button();
        fdc.Click_Yes_in_Confirmation_dialog();
        fdc.Validate_the_TankTicket_is_not_added("11111111","Enterprise Crude Oil, LLC", "Enterprise Crude Oil, LLC", "Sold from Oil Tank","111", "11", "0", "22", "33", "88", "89", "1234567");
    }

    @Test
    public void Add_TankTicketsData_success(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_TankTickets_tab();
        fdc.Select_a_TankStream_using_StreamName("BG 14 CTB OIL T-500D - 8423290190_TKT");
        fdc.Click_Add_button();
        fdc.Enter_TankTickets_Info("14502874","Enterprise Crude Oil, LLC", "Enterprise Crude Oil, LLC", "Sold from Oil Tank");
        fdc.Enter_TankTicketsData_Info("135", "86", "0", "65", "42", "86", "86", "0.60", "1777707");
        fdc.Click_Save_button();
        fdc.Click_Yes_in_Continue_dialog();
        fdc.Validate_the_status_of_Added_TankTickets("14502874","Enterprise Crude Oil, LLC", "Enterprise Crude Oil, LLC", "Sold from Oil Tank", "135", "86", "0", "65", "42", "86", "86",  "1777707");
    }

    @Test
    public void Edit_TankTicketsData_success(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_TankTickets_tab();
        fdc.Select_a_TankStream_using_StreamName("BG 14 CTB OIL T-500A - 8423290187_TKT");
        fdc.Edit_TankTickets_Info("14502874","Enterprise Crude Oil, LLC", "Enterprise Crude Oil, LLC", "Test Comment");
        fdc.Edit_TankTicketsData_Info("135", "85", "0", "65", "42", "86", "86", "0.65", "1777707");
        fdc.Click_Save_button();
        fdc.Click_Yes_in_Continue_dialog();
        fdc.Validate_the_status_of_Edited_TankTickets("14502874","Enterprise Crude Oil, LLC", "Enterprise Crude Oil, LLC", "Test Comment","135", "85", "0", "65", "0.65", "86", "86", "1777707");
    }

    @Test
    public void Add_LACTTicketsData_cancel(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_LACT_Tickets_Tab();
        fdc.Select_a_LACTStream_using_StreamName("BG 14 CTB LACT K-700B - 4423290027_TKT");
        fdc.Click_Add_button();
        fdc.Enter_LACTTickets_Info("12345678", "0", "4236965", "85", "98", "0", "39.50", "1", "0", "Test");
        fdc.Click_Cancel_button();
        fdc.Click_Yes_in_Confirmation_dialog();
        fdc.Validate_the_LACTTicket_is_not_added("12345678", "0", "4236965", "85", "98", "0", "39.50", "1", "0", "Test");
    }

    @Test
    public void Add_LACTTicketsData_revert(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_LACT_Tickets_Tab();
        fdc.Select_a_LACTStream_using_StreamName("BG 14 CTB LACT K-700B - 4423290027_TKT");
        fdc.Click_Add_button();
        fdc.Enter_LACTTickets_Info("12345678", "0", "4236965", "85", "98", "0", "39.50", "1", "0", "Test");
        fdc.Click_Revert_button();
        fdc.Click_Yes_in_Confirmation_dialog();
        fdc.Validate_the_LACTTicket_is_not_added("12345678", "0", "4236965", "85", "98", "0", "39.50", "1", "0", "Test");
    }

    @Test
    public void Add_LACTTicketsData_success(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_LACT_Tickets_Tab();
        fdc.Select_a_LACTStream_using_StreamName("BG 14 CTB LACT K-700B - 4423290027_TKT");
        fdc.Click_Add_button();
        fdc.Enter_LACTTickets_Info("27592267", "0", "4236965", "85", "98", "0", "39.50", "1", "0", "Test");
        fdc.Click_Save_button();
        fdc.Click_Yes_in_Continue_dialog();
        fdc.Validate_the_status_of_Added_LACTTickets_Data("27592267", "0", "4236965", "85", "98", "0", "39.50", "1", "0", "Test");
    }

    @Test
    public void Edit_LACTTicketsData_success(){
        fdc.Navigate_to_FieldDataCaptureScreen();
        fdc.Select_BeginDate();
        fdc.Select_EndDate();
        fdc.Select_a_CollectionPoint("BG 14 CTB");
        fdc.Navigate_to_LACT_Tickets_Tab();
        fdc.Select_a_LACTStream_using_StreamName("BG 14 CTB LACT K-700B - 4423290027_TKT");
        fdc.Enter_LACTTickets_Info("27592266", "1", "4236967", "87", "98", "0", "39.50", "1", "2", "Update Test");
        fdc.Click_Save_button();
        fdc.Click_Yes_in_Continue_dialog();
        fdc.Validate_the_status_of_Edited_LACTTickets_Data("27592266", "1", "4236967", "87", "98", "0", "39.50", "1", "2", "Update Test");
    }
}
