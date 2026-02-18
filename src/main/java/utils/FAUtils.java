package utils;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.apache.commons.io.FileUtils;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public abstract class FAUtils {
    public static Process startWinAppDriver = null;
    public static String ipAddress, port, field, winAppDriverUrl;

    public static void startWinAppDriverApp() throws IOException {
        //String command = "C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe";
        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//src//main//resources//data.properties");
        prop.load(fis);

        ipAddress = System.getProperty("ipAddress") != null ? System.getProperty("ipAddress") : prop.getProperty("ipAddress");
        port = System.getProperty("port") != null ? System.getProperty("port") : prop.getProperty("port");
        field = System.getProperty("field") != null ? System.getProperty("field") : prop.getProperty("field");
        String winAppDriverLocation = prop.getProperty("winAppDriverLocation");
        winAppDriverUrl= String.format("http://%s:%s", ipAddress, Integer.parseInt(port));
        ProcessBuilder builder = new ProcessBuilder(winAppDriverLocation).inheritIO();
        startWinAppDriver = builder.start();

    }

    public static List<HashMap<String, String>> getJsonData(String jsonFilePath) throws IOException {
        String jsonContent = FileUtils.readFileToString(new File(jsonFilePath), StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        List<HashMap<String, String>> data = objectMapper.readValue(jsonContent, new TypeReference<>() {
        });
        return data;
    }

    public static void Switch_to_ParentWindow(WindowsDriver<WindowsElement> driver){
        String parentWindow = driver.getWindowHandle();
        driver.switchTo().window(parentWindow);
    }
}


