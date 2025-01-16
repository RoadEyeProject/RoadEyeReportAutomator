package com.waze.tests;

import io.appium.java_client.AppiumBy;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static java.lang.Thread.sleep;

public class AppTest extends TestBase {

    @Test
    public void ReportPrep() throws InterruptedException {
        System.out.println("Starting report: ReportPrep");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement authWelcomeTopButton = (WebElement) wait.until(
                ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/authWelcomeTopButtonText")));
        authWelcomeTopButton.click();
        System.out.println("Clicked on 'Get started' button.");

        WebElement guestButton = (WebElement) wait.until(
                ExpectedConditions.presenceOfElementLocated(AppiumBy.xpath("//android.widget.TextView[@resource-id=\"com.waze:id/dialog_item_text\" and @text=\"Continue as guest\"]")));
        guestButton.click();
        System.out.println("Clicked on 'Continue as guest' button.");

        WebElement noElectricButton = (WebElement) wait.until(
                ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/button2")));
        noElectricButton.click();
        System.out.println("Closed electric vehicle pop up");

        WebElement mapReportButton = (WebElement) wait.until(
                ExpectedConditions.presenceOfElementLocated(AppiumBy.id("com.waze:id/mainReportButton")));
        mapReportButton.click();
        System.out.println("Clicked on yellow report button button.");

        WebElement badWeatherSubjectButton = (WebElement) wait.until(
                ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("REPORT_MENU_BOTTOM_SHEET_INDEXED_ITEM7")));
        badWeatherSubjectButton.click();
        System.out.println("Clicked on bad weather button");
        //bad weather is already selected after selecting a report category, so there is no need to press
        //the specific report, it is already selected.
        WebElement reportButton = (WebElement) wait.until(
                ExpectedConditions.presenceOfElementLocated(AppiumBy.accessibilityId("REPORT_MENU_BOTTOM_SHEET_PRIMARY_BUTTON")));
        reportButton.click();
        System.out.println("Clicked on report button");

        //asserting the report was sent by asserting feedback from the app in the form of an alert at the bottom
        System.out.println("Checking for 'Thanks' alert...");
        WebElement thanksAlert = wait.until(
                ExpectedConditions.visibilityOfElementLocated(AppiumBy.id("com.waze:id/bottomAlerterView"))
        );
        // Assert that the alert is displayed
        System.out.println("Verified 'Thanks' alert is displayed.");
    }
}