package com.ibidev.ibitrade.tests;
import org.junit.Test;
import java.net.MalformedURLException;

public class AppTest extends TestBase {

    @Test
    public void emptyTest() {
        String toPrint = findEl("new UiSelector().text(\"My Portfolio\")").getLocation().toString();
        boolean toPrint1 = findEl("new UiSelector().text(\"My Portfolio\")").isDisplayed();
    }

    @Test
    public void watchlistPage() throws MalformedURLException {
        //starting from portfolio page
        findEl("new UiSelector().resourceId(\"com.ibidev.ibitrade:id/watchlistsFragment\")").click();
        boolean oldWatchlists = isExist("new UiSelector().className(\"android.view.View\").instance(3)"); //check for old watchLists to remove
        while (oldWatchlists) {//remove all old watchLists
            findEl("new UiSelector().className(\"android.widget.ImageView\").instance(0)").click();
            findEl("new UiSelector().className(\"android.widget.Button\")").click();
            oldWatchlists = isExist("new UiSelector().className(\"android.view.View\").instance(3)");
        }
        findEl("new UiSelector().className(\"android.widget.Button\").instance(2)").click();
        findEl("new UiSelector().className(\"android.view.View\").instance(1)").sendKeys("Automation 0");
        for (int i = 1; i < 10; i++) {
            String watchlistName = "Automation " + String.valueOf(i);
            findEl("new UiSelector().className(\"android.widget.Button\").instance(2)").click();
            findEl("new UiSelector().className(\"android.widget.EditText\")").sendKeys(watchlistName);
            findEl("new UiSelector().className(\"android.widget.Button\")").click();
        }


    }
}