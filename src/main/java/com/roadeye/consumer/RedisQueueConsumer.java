package com.roadeye.consumer;
import java.time.Duration;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
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
        if (message.contains("\"eventType\":\"bad weather\"")) {
            System.out.println("Triggering 'Bad Weather' automation...");
            triggerBadWeatherAutomation();
        } else {
            System.out.println("Unknown event type. Skipping message.");
        }
    }

    private void triggerBadWeatherAutomation() {
        WebDriverWait wait = null;
        try {
            setupDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));

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


    private void setupDriver() {
        System.out.println("Setting up Appium driver...");
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

            driver = new AppiumDriver(new URL("http://127.0.0.1:4723/"), capabilities);
            System.out.println("Appium driver setup complete.");
        } catch (Exception e) {
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
}
