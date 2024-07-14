package main;

import io.appium.java_client.AppiumBy;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.Date;
import io.appium.java_client.android.AndroidDriver;

public class AppTest {

    private static AndroidDriver driver;

    @org.jetbrains.annotations.NotNull
    private static DesiredCapabilities getCap() {
        DesiredCapabilities cap = new DesiredCapabilities();

        cap.setCapability("deviceName", "galaxy");
        cap.setCapability("udid", "R58M67FGSCF");
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "12");
        cap.setCapability("automationName", "uiAutomator2");
        cap.setCapability("appPackage", "com.ibidev.ibitrade");
        cap.setCapability("appActivity", "com.ibidev.ibitrade.MainActivity");
        return cap;
    }

    private static @NotNull String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    private static void tap(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 50);
        tap.addAction(finger.createPointerMove(Duration.ofMillis(50), PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
    }

    private static void takeScreenshot(String filePath) {
        File screenshot = driver.getScreenshotAs(OutputType.FILE);
        try {
            // Ensure the directory exists
            Files.createDirectories(Paths.get(filePath).getParent());
            Files.copy(screenshot.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved at: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    private static void pressBackNtimes(int N){
        for(int i=0; i<N; i++){
            driver.navigate().back();
        }
    }

    private static WebElement findEl(String element){
        return driver.findElement(new AppiumBy.ByAndroidUIAutomator(element));
    }


    public static void main(String[] args) throws MalformedURLException{
        openMobileApp();
    }

    public static void openMobileApp() throws MalformedURLException {
        DesiredCapabilities cap = getCap();

        URL url = new URL("http://127.0.0.1:4723/");
        driver = new AndroidDriver(url, cap);
        System.out.println("Application started");

        findEl("new UiSelector().text(\"Skip\")").click();
        System.out.println("skipped first animation page (Figma - Onboarding 5 screen)");

        findEl("new UiSelector().className(\"android.widget.Button\").instance(1)").click();
        System.out.println("skipped Terms & Conditions page (Figma - Terms & Conditions screen)");
    }

    public static void OB_site() throws MalformedURLException {
        findEl("new UiSelector().className(\"android.widget.Button\").instance(1)").click();
        System.out.println("routed to OB-Trade site (Figma - Onboarding 5 screen)");
    }
}
