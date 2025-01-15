package com.waze.tests;

import io.appium.java_client.AppiumBy;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AppTest extends TestBase {

    @Test
    public void emptyTest() {
        System.out.println("Starting test: emptyTest");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement continueButton = (WebElement) wait.until(
                ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("CALL_TO_ACTION_BAR_FIRST_ACTION"))
        );
        continueButton.click();
        System.out.println("Clicked on 'Continue' button.");
    }
}