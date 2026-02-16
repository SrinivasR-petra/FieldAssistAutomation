package utils;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.apache.commons.io.FileUtils;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public abstract class FAUtils {
    public static Process startWinAppDriver = null;

    public static String startWinAppDriverApp(String command, String ipAddress, int port) throws IOException {
        //String command = "C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe";
        ProcessBuilder builder = new ProcessBuilder(command).inheritIO();
        startWinAppDriver = builder.start();
        // Build WinAppDriver URL
        String winAppDriverUrl = String.format("http://%s:%s", ipAddress, port);
        return winAppDriverUrl;
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


