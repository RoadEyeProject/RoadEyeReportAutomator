package com.roadeye.automation;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.time.Duration;
import java.util.List;

public class AutomationService {
    private final String appiumServerUrl;
    protected AppiumDriver driver;

    // Constructor to accept dynamic parameters
    public AutomationService(String serverUrl, String deviceName, String udid, String platformVersion) {
        this.appiumServerUrl = serverUrl;
        setup(deviceName, udid, platformVersion); // Dynamically initialize the driver
    }

    // Private method to initialize the Appium driver with dynamic capabilities
    private void setup(String deviceName, String udid, String platformVersion) {
        System.out.println("Setting up the Appium driver...");
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();

            // Populate capabilities dynamically
            capabilities.setCapability("deviceName", deviceName);
            capabilities.setCapability("udid", udid);
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion", platformVersion);
            capabilities.setCapability("automationName", "uiAutomator2");
            capabilities.setCapability("appPackage", "com.waze");
            capabilities.setCapability("appActivity", "com.waze.FreeMapAppActivity");
            capabilities.setCapability("autoGrantPermissions", true);

            // Initialize the Appium driver
            driver = new AppiumDriver(new URL(appiumServerUrl), capabilities);
            System.out.println("Driver initialized successfully for device: " + deviceName);
        } catch (Exception e) {
            System.err.println("Failed to initialize the Appium driver: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Teardown method to quit the driver
    public void teardown() {
        if (driver != null) {
            System.out.println("Quitting the Appium driver...");
            driver.quit();
            System.out.println("Driver quit successfully.");
        }
    }

    // Method to execute automations based on the provided report types
    public void executeReports(List<String> reportTypes) {
        System.out.println("Executing reports...");

        for (String report : reportTypes) {
            switch (report.toLowerCase()) {
                case "bad weather":
                    executeBadWeatherAutomation();
                    break;
                // Future automations can be added here as additional cases
                default:
                    System.out.println("No automation linked to report type: " + report);
            }
        }
    }

    // Automation logic for "Bad Weather"
    private void executeBadWeatherAutomation() {
        System.out.println("Executing 'Bad Weather' automation...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try {
            // Perform steps for "Bad Weather"
            WebElement authWelcomeTopButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/authWelcomeTopButtonText")));
            authWelcomeTopButton.click();
            System.out.println("Clicked on 'Get started' button.");

            WebElement guestButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.waze:id/dialog_item_text\" and @text=\"Continue as guest\"]")));
            guestButton.click();
            System.out.println("Clicked on 'Continue as guest' button.");

            WebElement noElectricButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/button2")));
            noElectricButton.click();
            System.out.println("Closed electric vehicle pop-up.");

            WebElement mapReportButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/mainReportButton")));
            mapReportButton.click();
            System.out.println("Clicked on yellow report button.");

            WebElement badWeatherSubjectButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("REPORT_MENU_BOTTOM_SHEET_INDEXED_ITEM7")));
            badWeatherSubjectButton.click();
            System.out.println("Clicked on bad weather button.");

            WebElement reportButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("REPORT_MENU_BOTTOM_SHEET_PRIMARY_BUTTON")));
            reportButton.click();
            System.out.println("Clicked on report button.");

            // Assert that the alert is displayed
            WebElement thanksAlert = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(AppiumBy.id("com.waze:id/bottomAlerterView")));
            System.out.println("Verified 'Thanks' alert is displayed.");
        } catch (Exception e) {
            System.err.println("Error during 'Bad Weather' automation: " + e.getMessage());
        }
    }
}
