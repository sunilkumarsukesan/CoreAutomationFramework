package com.automation.core.drivers;

import com.automation.core.config.ConfigManager;
import com.automation.core.utils.LoggerManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;

public class DriverManager {
    private static final Logger logger = LoggerManager.getLogger(DriverManager.class);
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            String browser = ConfigManager.getBrowser();
            logger.info("Initializing browser: " + browser);

            if ("chrome".equalsIgnoreCase(browser)) {
                driver = new ChromeDriver();
            } else {
                logger.error("Unsupported browser: " + browser);
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }

            logger.info("Browser initialized successfully.");
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            logger.info("Closing the browser...");
            driver.quit();
            driver = null;
        }
    }
}
