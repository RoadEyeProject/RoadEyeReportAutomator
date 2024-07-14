import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
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

public class AppTest {

    static AppiumDriver driver;

    public static void main(String[] args) throws MalformedURLException{
        openMobileApp();
    }

    public static void tap(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 10);
        tap.addAction(finger.createPointerMove(Duration.ofMillis(10), PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
    }

    public static void takeScreenshot(String filePath) {
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

    public static void pressbackNtimes(int N){
        for(int i=0; i<N; i++){
            driver.navigate().back();
        }
    }

    public static void openMobileApp() throws MalformedURLException {

        //Driver setup------------------------------------------------
        DesiredCapabilities cap = new DesiredCapabilities();

        cap.setCapability("deviceName", "galaxy");
        cap.setCapability("udid", "R58M67FGSCF");
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "12");
        cap.setCapability("automationName", "uiAutomator2");
        cap.setCapability("appPackage", "com.ibidev.ibitrade");
        cap.setCapability("appActivity", "com.ibidev.ibitrade.MainActivity");

        URL url = new URL("http://127.0.0.1:4723/");
        driver = new AppiumDriver(url, cap);
        System.out.println("Application started");
        //Driver setup------------------------------------------------
        driver.findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"Skip\")")).click();

        // Testing terms and conditions page links
        tap(1050, 870); // Taps on "Terms and conditions" ----HARDCODED FOR GALAXY S10+
        driver.findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"com.sec.android.app.sbrowser:id/location_bar_edit_text\")")).click();
        String terms_conditions = driver.findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().resourceId(\"com.sec.android.app.sbrowser:id/location_bar_edit_text\")")).getText();
        if (!terms_conditions.equals("https://www.ibi.co.il/about/app-terms-of-service/")) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filePath = "screenshots/terms_conditions_" + timestamp + ".png";
            pressbackNtimes(2);
            takeScreenshot(filePath);
            System.out.println("Terms and conditions routed to the wrong site: " + terms_conditions);
        } else {
            System.out.println("Terms and conditions routed OK");
            //future additions: testing the site itself for bugs(happened before)
        }

        //tap(960, 400); // Taps on "Privacy Protection policy"
        //tap(680, 1030); // Taps on "Newsletters and marketing phone calls conditions"
    }
}
