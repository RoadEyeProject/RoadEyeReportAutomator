package com.roadeye;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

import org.json.JSONObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import redis.clients.jedis.Jedis;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.net.URL;

public class RedisQueueConsumer {

    private final String redisHost;
    private final int redisPort;
    private final String queueName;
    private AppiumDriver driver;

    public RedisQueueConsumer(String redisHost, int redisPort, String queueName) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.queueName = queueName;
    }

    public void start() {
        try (Jedis jedis = new Jedis(redisHost, redisPort)) {
            jedis.select(0);
            System.out.println("✅ Connected to Redis. PING: " + jedis.ping());

            while (true) {
                // BLPOP blocks until a message is available
                System.out.println("🕐 Waiting for events...");
                String message = jedis.blpop(0, queueName).get(1);
                System.out.println("Received message: " + message);

                // Process the message
                processMessage(message);
            }
        } catch (Exception e) {
            System.err.println("❌ Could not connect to Redis: " + e.getMessage());
        }
    }

    private void processMessage(String message) {
        try {
            JSONObject event = new JSONObject(message);
            String eventType = event.getString("eventType");
            System.out.println("Triggering '" + eventType + "' automation...");

            JSONObject location = event.getJSONObject("location");
            double latitude = location.getDouble("latitude");
            double longitude = location.getDouble("longitude");

            triggerAutomation(eventType, latitude, longitude);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    private void triggerAutomation(String eventType, double latitude, double longitude) {
        setupDriver(latitude, longitude);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        try {
            // Common initial steps for both event types
            WebElement getStartedButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/authWelcomeTopButtonText")));
            getStartedButton.click();

            WebElement continueAsGuestButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath(
                            "//android.widget.TextView[@resource-id='com.waze:id/dialog_item_text' and @text='Continue as guest']")));
            continueAsGuestButton.click();

            // WebElement noElectricPopup = wait.until(
            //         ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/button2")));
            // noElectricPopup.click();

            WebElement reportButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/mapReportButtonView")));
            reportButton.click();

            // Event-specific logic
            switch (eventType) {
                case "Road Construction":
                    WebElement hazardButton = wait.until(
                            ExpectedConditions.presenceOfElementLocated(
                                    AppiumBy.androidUIAutomator("new UiSelector().text(\"Hazard\")")));
                    hazardButton.click();

                    WebElement constructionButton = wait.until(
                            ExpectedConditions.presenceOfElementLocated(
                                    AppiumBy.androidUIAutomator("new UiSelector().text(\"Construction\")")));
                    constructionButton.click();
                    System.out.println("Clicked on 'Hazard -> Construction' button.");
                    break;

                case "Accident":
                    WebElement crashButton = wait.until(
                            ExpectedConditions.presenceOfElementLocated(
                                    AppiumBy.androidUIAutomator("new UiSelector().text(\"Crash\")")));
                    crashButton.click();

                    WebElement crashOnMySideButton = wait.until(
                            ExpectedConditions.presenceOfElementLocated(
                                    AppiumBy.androidUIAutomator("new UiSelector().text(\"Crash\")")));
                    crashOnMySideButton.click();
                    System.out.println("Clicked on 'Crash(Accident) -> Crash' button.");
                    break;

                case "Police Car":
                    WebElement policeButton = wait.until(
                            ExpectedConditions.presenceOfElementLocated(
                                    AppiumBy.androidUIAutomator("new UiSelector().text(\"Police\")")));
                    policeButton.click();
                    System.out.println("Clicked on 'Police Car' button.");

                    WebElement policeOnMySideButton = wait.until(
                            ExpectedConditions.presenceOfElementLocated(
                                    AppiumBy.androidUIAutomator("new UiSelector().text(\"Police\")")));
                    policeOnMySideButton.click();
                    System.out.println("Confirmed 'Police on my side'.");
                    break;

                default:
                    System.out.println("Unknown event type provided: " + eventType);
                    return; // Exit the method if unknown event type
            }
            
            WebElement confirmReportButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            AppiumBy.androidUIAutomator("new UiSelector().text(\"Report\")")));
            confirmReportButton.click();
            System.out.println("Sent report.");

            //This is to locate if the report confirmation text is present
            WebElement alerterTitle = wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            AppiumBy.id("com.waze:id/alerterTitle")));
            System.out.println("Report submitted successfully.");

        } catch (Exception e) {
            System.err.println("Error during automation for event type '" + eventType + "': " + e.getMessage());
        } finally {
            teardownDriver();
        }
    }

    private void setupDriver(double latitude, double longitude) {
        System.out.println("Setting up Appium driver...");
        grantMockLocationPermission(latitude, longitude);
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("deviceName", "galaxy");
            capabilities.setCapability("udid", "RF8M21KVM1K");
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion", "12");
            capabilities.setCapability("automationName", "UiAutomator2");
            capabilities.setCapability("appPackage", "com.waze");
            capabilities.setCapability("appActivity", "com.waze.FreeMapAppActivity");
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability("locationServicesEnabled", true);
            capabilities.setCapability("locationServicesAuthorized", true);
            // capabilities.setCapability("autoLaunch", false); // Prevent auto launching
            // Waze

            driver = new AppiumDriver(new URL("http://127.0.0.1:4723/"), capabilities);

        } catch (Exception e) {
            System.out.println("Error::::::::::\n");
            System.out.println(e);
            System.out.println("Error::::::::::\n");
            System.exit(1); // temp! kills the program so the loop will end
            throw new RuntimeException("Failed to initialize Appium driver", e);
        }
    }

    private void teardownDriver() {
        if (driver != null) {
            System.out.println("Quitting Appium driver...");
            driver.quit();
            System.out.println("Appium driver quit successfully.");
        }
    }

    private void grantMockLocationPermission(double latitude, double longitude) {
        try {
            String latStr = String.format("%.4f", latitude);
            String lonStr = String.format("%.4f", longitude);

            // Build adb command to launch GPS Joystick with the desired coordinates
            String startIntent = String.format(
                    "adb shell am start-foreground-service -a " + 
                    "theappninjas.gpsjoystick.TELEPORT --ef lat \"%s\" --ef lng \"%s\" --ef alt 0",
                    latStr, lonStr);
            executeCommand(startIntent);
            System.out.println("✅ Location spoofing initiated: " + latStr + ", " + lonStr);

        } catch (Exception e) {
            System.err.println("❌ Failed to set location: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void executeCommand(String command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command.split(" "));
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Log command output
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Command failed with exit code: " + exitCode);
        }
    }

}
