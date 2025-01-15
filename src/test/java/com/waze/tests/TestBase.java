package com.waze.tests;

import io.appium.java_client.AppiumDriver;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class TestBase {

    protected AppiumDriver driver;

    @Before
    public void setUp() {
        System.out.println("Initializing Appium Driver...");
        try {
            DesiredCapabilities cap = getCap();
            URL url = new URL("http://127.0.0.1:4723/");
            driver = new AppiumDriver(url, cap);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Appium Server URL is invalid!");
        }
        System.out.println("Appium Driver initialized.");
    }

    @After
    public void tearDown() {
        System.out.println("Closing Appium Driver...");
        if (driver != null) {
            driver.quit();
        }
        System.out.println("Appium Driver closed.");
    }

    @NotNull
    private DesiredCapabilities getCap() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("deviceName", "pixel_9_emulator");
        cap.setCapability("udid", "emulator-5554");
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "15");
        cap.setCapability("automationName", "uiAutomator2");
        cap.setCapability("appPackage", "com.waze");
        cap.setCapability("appActivity", "com.waze.FreeMapAppActivity");
        return cap;
    }
}

