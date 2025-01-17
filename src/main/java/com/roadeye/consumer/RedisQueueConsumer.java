package com.roadeye.consumer;
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
            System.out.println("Connected to Redis. Listening for messages...");

            while (true) {
                // BLPOP blocks until a message is available
                String message = jedis.blpop(0, queueName).get(1);
                System.out.println("Received message: " + message);

                // Process the message
                processMessage(message);
            }
        } catch (Exception e) {
            System.err.println("Error while listening to Redis queue: " + e.getMessage());
        }
    }

    private void processMessage(String message) {
        try {
            JSONObject event = new JSONObject(message);
            if (event.getString("eventType").equals("bad weather")) {
                System.out.println("Triggering 'Bad Weather' automation...");

                // Extract coordinates
                JSONObject location = event.getJSONObject("location");
                double latitude = location.getDouble("latitude");
                double longitude = location.getDouble("longitude");

                triggerBadWeatherAutomation(latitude, longitude);
            } else {
                System.out.println("Unknown event type. Skipping message.");
            }
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    private void triggerBadWeatherAutomation(double latitude, double longitude) {
        setupDriver(latitude, longitude);
        WebDriverWait wait = null;
        try {
            wait = new WebDriverWait(driver, Duration.ofSeconds(40));

            // Perform Waze automation steps with waits
            WebElement getStartedButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/authWelcomeTopButtonText")));
            getStartedButton.click();
            System.out.println("Clicked on 'Get started' button.");

            WebElement continueAsGuestButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath(
                            "//android.widget.TextView[@resource-id='com.waze:id/dialog_item_text' and @text='Continue as guest']")));
            continueAsGuestButton.click();
            System.out.println("Clicked on 'Continue as guest' button.");

            WebElement noElectricPopup = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/button2")));
            noElectricPopup.click();
            System.out.println("Closed electric vehicle pop-up.");

            WebElement reportButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/mainReportButton")));
            reportButton.click();
            System.out.println("Clicked on yellow report button.");

            WebElement badWeatherButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("REPORT_MENU_BOTTOM_SHEET_INDEXED_ITEM7")));
            badWeatherButton.click();
            System.out.println("Clicked on bad weather button.");

            WebElement submitButton = wait.until(
                    ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("REPORT_MENU_BOTTOM_SHEET_PRIMARY_BUTTON")));
            submitButton.click();
            System.out.println("Clicked on report button.");
        } catch (Exception e) {
            System.err.println("Error during 'Bad Weather' automation: " + e.getMessage());
        } finally {
            teardownDriver();
        }
    }


    private void setupDriver(double latitude, double longitude) {
        System.out.println("Setting up Appium driver...");

        grantMockLocationPermission(latitude, longitude);
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("deviceName", "pixel_9_emulator");
            capabilities.setCapability("udid", "emulator-5554");
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion", "15");
            capabilities.setCapability("automationName", "UiAutomator2");
            capabilities.setCapability("appPackage", "com.waze");
            capabilities.setCapability("appActivity", "com.waze.FreeMapAppActivity");
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability("locationServicesEnabled", true);
            capabilities.setCapability("locationServicesAuthorized", true);

            driver = new AppiumDriver(new URL("http://127.0.0.1:4723/"), capabilities);


        } catch (Exception e) {
            System.exit(1); //temp! kills the program so the loop will end
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
            // Set the GPS location using adb emu geo fix
            String locationCommand = String.format("adb emu geo fix %f %f", longitude, latitude);
            executeCommand(locationCommand);
            System.out.println("Set GPS location to: Latitude = " + latitude + ", Longitude = " + longitude);
        } catch (Exception e) {
            System.err.println("Failed to grant permissions or set location: " + e.getMessage());
            e.printStackTrace();
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
