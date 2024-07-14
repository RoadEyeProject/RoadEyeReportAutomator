package com.ibidev.ibitrade.tests;

import org.junit.Test;
import java.net.MalformedURLException;

public class AppTest extends TestBase {

    @Test
    public void testOpenMobileApp() throws MalformedURLException {
        findEl("new UiSelector().text(\"Skip\")").click();
        System.out.println("Skipped first animation page (Figma - Onboarding 5 screen)");

        findEl("new UiSelector().className(\"android.widget.Button\").instance(1)").click();
        System.out.println("Skipped Terms & Conditions page (Figma - Terms & Conditions screen)");
    }

    @Test
    public void testOB_site() {
        findEl("new UiSelector().className(\"android.widget.Button\").instance(1)").click();
        System.out.println("Routed to OB-Trade site (Figma - Onboarding 5 screen)");
    }
}
