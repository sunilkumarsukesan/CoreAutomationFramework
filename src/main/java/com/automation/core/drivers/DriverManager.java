package com.automation.core.drivers;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DriverManager {
    private static final ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<WebDriverWait> wait = new  ThreadLocal<>();

    public static void initDriver(String browser) {
        if (driver.get() == null) {
            System.out.println("Initializing WebDriver for browser: " + browser);
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
            wait.set(new WebDriverWait(getDriver(), Duration.ofSeconds(10))); // ✅ Initialize WebDriverWait here
            System.out.println("WebDriver initialized successfully.");
        }
    }

    public static RemoteWebDriver getDriver() {
        if (driver.get() == null) {
            System.err.println("Error: WebDriver is not initialized. Call initDriver() first.");
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

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            System.out.println("WebDriver quit and removed from ThreadLocal.");
        }
    }
}
