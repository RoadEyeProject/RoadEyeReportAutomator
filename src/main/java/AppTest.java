import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class AppTest {

    static AppiumDriver driver;

    public static void main(String[] args) throws MalformedURLException{
        openMobileApp();
    }

    public static void openMobileApp() throws MalformedURLException {
        DesiredCapabilities cap = new DesiredCapabilities();
        //R58M67FGSCF  12  galaxy
        //emulator-5554  15  sdk_gphone64_arm64
        cap.setCapability("deviceName", "galaxy");
        cap.setCapability("udid", "R58M67FGSCF");
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "12");
        cap.setCapability("automationName", "uiAutomator2");
        cap.setCapability("appPackage", "com.ibidev.ibitrade");
        cap.setCapability("appActivity", "com.ibidev.ibitrade.MainActivity");

        URL url = new URL("http://127.0.0.1:4723/");
        driver = new AppiumDriver(url, cap);

        driver.findElement(new AppiumBy.ByAndroidUIAutomator("new UiSelector().text(\"Skip\")")).click();

        System.out.println("Application started");
    }

}
