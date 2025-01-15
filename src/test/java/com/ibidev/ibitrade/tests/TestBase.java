package com.ibidev.ibitrade.tests;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.jetbrains.annotations.NotNull;
import org.junit.After;
import org.junit.Before;
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
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
//documentation for driver: https://javadoc.io/doc/io.appium/java-client/7.2.0/io/appium/java_client/android/AndroidDriver.html
public class TestBase {

 protected AndroidDriver driver;
 private final String username = "ibi116830";
 private final String password = "Eliran3002";

 @Before
 public void setUp() throws MalformedURLException, InterruptedException {
  DesiredCapabilities cap = getCap();
  URL url = new URL("http://127.0.0.1:4723/");
  driver = new AndroidDriver(url, cap);

  logIn();
 }

 @After
 public void tearDown() {
  if (driver != null) {
   driver.quit();
  }
 }


 @NotNull
 private DesiredCapabilities getCap() {
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

 protected WebElement findEl(String element) {
  return driver.findElement(new AppiumBy.ByAndroidUIAutomator(element));
 }

 protected List<WebElement> findList(String element) {
  return driver.findElements(new AppiumBy.ByAndroidUIAutomator(element));
 }

 private void logIn() throws InterruptedException {
  findEl("new UiSelector().text(\"Skip\")").click();
  findEl("new UiSelector().className(\"android.widget.Button\").instance(1)").click();
  findEl("new UiSelector().className(\"android.widget.Button\").instance(0)").click();
  findEl("new UiSelector().resourceId(\"com.ibidev.ibitrade:id/emailEditTextView\")").sendKeys(username);
  findEl("new UiSelector().resourceId(\"com.ibidev.ibitrade:id/passwordEditTextView\")").sendKeys(password);
  findEl("new UiSelector().className(\"android.widget.Button\").instance(0)").click();
  Thread.sleep(10000);
 }

 protected void tap(int x, int y) {
  PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
  Sequence tap = new Sequence(finger, 1);
  tap.addAction(finger.createPointerMove(Duration.ofMillis(50), PointerInput.Origin.viewport(), x, y));
  tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
  tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
  driver.perform(Arrays.asList(tap));
 }

 protected String getCurrentTime() {
  return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
 }

 protected void takeScreenshot(String filePath) {
  File screenshot = driver.getScreenshotAs(OutputType.FILE);
  try {
   Files.createDirectories(Paths.get(filePath).getParent());
   Files.copy(screenshot.toPath(), new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
   System.out.println("Screenshot saved at: " + filePath);
  } catch (IOException e) {
   System.err.println("Failed to save screenshot: " + e.getMessage());
  }
 }

 protected void pressBackNtimes(int N) {
  for (int i = 0; i < N; i++) {
   driver.navigate().back();
  }
 }

 protected boolean isExist(String element) {
   boolean check;
   try{
    check = findEl(element).isDisplayed();
   }
   catch(Exception e){
    check = false;
   }
   return check;
  }
 }

