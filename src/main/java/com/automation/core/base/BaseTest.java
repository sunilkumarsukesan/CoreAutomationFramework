package com.automation.core.base;

import com.automation.core.drivers.DriverManager;
import com.automation.core.reporting.ExtentTestManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(com.automation.core.reporting.TestListener.class)
public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        DriverManager.initDriver();  // Ensure this is called first
        WebDriver driver = DriverManager.getDriver(); // Now get the driver
        driver.manage().window().maximize();
    }


    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        ExtentTestManager.endTest();
    }
}
