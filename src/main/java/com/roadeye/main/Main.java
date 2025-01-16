package com.roadeye.main;

import com.roadeye.automation.AutomationService;

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {

        String serverUrl = "http://127.0.0.1:4723/";
        String deviceName = "pixel_9_emulator";
        String udid = "emulator-5554";
        String platformVersion = "15";

        ArrayList<String> reportTypes = new ArrayList<>();
        reportTypes.add("bad weather");
        // Initialize the AutomationService
        AutomationService service = new AutomationService(serverUrl, deviceName, udid, platformVersion);

        // Perform your automation tasks and teardown
        service.executeReports(reportTypes);
        service.teardown();
    }
}
