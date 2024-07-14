package com.ibidev.ibitrade.tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
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

public class TestBase {

 protected AndroidDriver driver;

 @Before
 public void setUp() throws MalformedURLException {
  DesiredCapabilities cap = getCap();

  URL url = new URL("http://127.0.0.1:4723/");
  driver = new AndroidDriver(url, cap);
  System.out.println("Application started");
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
}
