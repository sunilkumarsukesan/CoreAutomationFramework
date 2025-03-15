package com.automation.core.drivers;

import com.automation.core.base.BaseTest;
import com.automation.core.config.ConfigManager;
import com.automation.core.logger.LoggerManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.function.Function;

public class DriverManager {
    private static final ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> wait = new  ThreadLocal<>();
    private static final Logger logger = LoggerManager.getLogger(DriverManager.class);

    public static void initDriver(String browser) {
        if (driver.get() == null) {
            logger.info("Initializing WebDriver for browser: " + browser);
            RemoteWebDriver remoteWebDriver;
            switch (browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--start-maximized");
                    chromeOptions.addArguments("--disable-notifications");
                    driver.set(new ChromeDriver(chromeOptions));
                    break;
                case "firefox":
                    driver.set(new FirefoxDriver());
                    break;
                case "edge":
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--start-maximized");
                    edgeOptions.addArguments("--disable-notifications");
                    driver.set(new EdgeDriver(edgeOptions));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(ConfigManager.getTimeOut()))); // ✅ Initialize WebDriverWait here
            logger.info("WebDriver initialized successfully");
        }
    }

    public static RemoteWebDriver getDriver() {
        if (driver.get() == null) {
            logger.error("Error: WebDriver is not initialized. Call initDriver() first.");
            throw new IllegalStateException("Driver is not initialized. Call initDriver() first.");
        }
        return driver.get();
    }

    public static WebDriverWait getWait() {
        if (wait.get() == null) {
            wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(10))); // ✅ Ensure wait is always initialized
        }
        return wait.get();
    }

    /**
     * FluentWait implementation with polling.
     */
    public static <T> T fluentWait(Function<RemoteWebDriver, T> condition) {
        FluentWait<RemoteWebDriver> fluentWait = new FluentWait<>(getDriver())
                .withTimeout(Duration.ofSeconds(10))  // Max wait time
                .pollingEvery(Duration.ofMillis(500)) // Retry every 500ms
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return fluentWait.until(condition);
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            logger.info("WebDriver quit and removed from ThreadLocal.");
        }
    }
}
