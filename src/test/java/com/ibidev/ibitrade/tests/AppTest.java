package com.ibidev.ibitrade.tests;
import org.junit.Test;

import java.awt.*;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;

public class AppTest extends TestBase {

    @Test
    public void emptyTest() throws InterruptedException {
    }


    @Test
    public void watchlistPage() throws MalformedURLException, InterruptedException {
        findEl("new UiSelector().resourceId(\"com.ibidev.ibitrade:id/watchlistsFragment\")").click();
        deletePreviousWatchLists();
        createNewWatchLists(10);
        addStocks();

    }
    private void createNewWatchLists(int amount) throws InterruptedException {
        findEl("new UiSelector().text(\"Create a New Watchlist\")").click();
        findEl("new UiSelector().className(\"android.widget.EditText\")").sendKeys("Automation");
        findEl("new UiSelector().className(\"android.widget.Button\")").click();
        Thread.sleep(2000);
        String watchlistName = "A";

        for (int i = 1; i < amount; i++) {
            //scrollToElementHorizontally("new UiSelector().className(\"android.widget.Button\").instance(2)");
            if(!isExist("new UiSelector().description(\"Add Watchlist\")")){
                scroll(findEl("new UiSelector().className(\"android.widget.HorizontalScrollView\")"), "left");
                Thread.sleep(500);
            }
            findEl("new UiSelector().description(\"Add Watchlist\")").click();
            findEl("new UiSelector().className(\"android.widget.EditText\")").sendKeys(watchlistName);
            findEl("new UiSelector().className(\"android.widget.Button\")").click();
            Thread.sleep(500);
            watchlistName = watchlistName.concat("A");
        }
    }
    private void deletePreviousWatchLists() throws InterruptedException {
        while (!findList("new UiSelector().className(\"android.widget.HorizontalScrollView\")").isEmpty()) {//remove all old watchLists
            findEl("new UiSelector().className(\"android.widget.ImageView\").instance(0)").click();
            Thread.sleep(500);
            findEl("new UiSelector().className(\"android.widget.Button\")").click();
            Thread.sleep(1500);
        }
    }
    private void addStocks() throws InterruptedException {
        List<String> defaultStocks = Arrays.asList("AAPL", "GOOG", "AMZN", "SPY", "ASML", "AMD", "tsla", "QQQ", "NVDA", "SHOP");
        addStocks(defaultStocks);
    }
    private void addStocks(List<String> stocks) throws InterruptedException {
        Thread.sleep(500);
        findEl("new UiSelector().text(\"Add Stock\")").click();
        Thread.sleep(3000);
        for (String stock : stocks) {
            findEl("new UiSelector().className(\"android.widget.EditText\")").sendKeys(stock);
            Thread.sleep(4000);
            findEl("new UiSelector().className(\"android.widget.ImageView\").instance(0)").click();
            Thread.sleep(2000);
            findEl("new UiSelector().text(\"Automation\")").click();
            findEl("new UiSelector().text(\"AAAAAAAAA\")").click();
            findEl("new UiSelector().text(\"AAAAAAAA\")").click();
            Thread.sleep(500);
            findEl("new UiSelector().text(\"Add\")").click();
            Thread.sleep(500);
            findEl("new UiSelector().description(\"Close\")").click();
            Thread.sleep(300);
        }
        findEl("new UiSelector().className(\"android.widget.Button\")").click();
    }
//    public void clickAllChildrenInScrollView() {
//        // Locate the ScrollView by its class name
//        MobileElement scrollView = (MobileElement) driver.findElementByAndroidUIAutomator(
//                "new UiSelector().className(\"android.widget.ScrollView\")"
//        );
//
//        // Assume the class name of the child elements is "android.view.View"
//        String childClassName = "android.view.View";
//
//        // Try clicking the first 10 child elements (you can adjust the number as needed)
//        for (int i = 0; i < 10; i++) {
//            try {
//                MobileElement childElement = (MobileElement) driver.findElementByAndroidUIAutomator(
//                        "new UiSelector().className(\"" + childClassName + "\").instance(" + i + ")"
//                );
//                childElement.click();
//            } catch (Exception e) {
//                // If the element is not found, break the loop
//                break;
//            }
//        }
//    }

}