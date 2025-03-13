package com.automation.core.base;

import com.automation.core.config.ConfigManager;
import com.automation.core.drivers.DriverManager;
import com.automation.core.listeners.TestListener;
import com.automation.core.reporting.ExtentManager;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public class BaseTest {
    public RemoteWebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Load config from TestAutomationSuite, fallback to Core config
        String browser = ConfigManager.getBrowser();
        //takes the global config app value (needs to be configured for TestNG)
        String application = ConfigManager.getApplication();
        String baseUrl = ConfigManager.getApplicationUrl(application);

        DriverManager.initDriver(browser);  // Initialize Driver
        driver = DriverManager.getDriver(); // Assign instance to class variable
        driver.manage().window().maximize();
        System.out.println("Navigating to: " + baseUrl);
        driver.get(baseUrl);
    }

    /** ✅ Add this method to allow other classes to access driver **/
    public RemoteWebDriver getDriver() {
        return driver;
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        ExtentManager.flushReports();
    }
}
