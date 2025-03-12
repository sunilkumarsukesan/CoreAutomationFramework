package com.automation.core.reporting;

import com.automation.core.drivers.DriverManager;
import com.automation.core.hooks.Hooks;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ExtentManager {
    private static ExtentReports extent;
    private static final Logger logger = Logger.getLogger(ExtentManager.class.getName());
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();
    private static final ConcurrentHashMap<Long, String> categoryMap = new ConcurrentHashMap<>();
    public static String reportFolder = "";

    /**
     * Initializes the Extent Reports instance
     */
    public synchronized static ExtentReports getExtentReports() {
        if (extent == null) {
            // Validate if "reports/" folder exists, if not, create it
            File reportsDirectory = new File("reports");
            if (!reportsDirectory.exists()) {
                boolean isCreated = reportsDirectory.mkdirs();
                if (isCreated) {
                    System.out.println("Created 'reports/' directory.");
                } else {
                    System.err.println("Failed to create 'reports/' directory.");
                }
            }

            // Generate timestamped report folder
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            reportFolder = "reports/" + timestamp;
            new File(reportFolder).mkdirs();

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFolder + "/testReport_" +timestamp + ".html");
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setEncoding("utf-8");
            sparkReporter.config().setReportName("Automation Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        }
        return extent;
    }

    /**
     * Creates a test case (Parent Node) and stores it in ThreadLocal.
     */
    public static synchronized ExtentTest createTest(String testName, String description, String category) {
        ExtentTest test = getExtentReports().createTest(testName, description);
        setTestThread(test);
        long threadId = Thread.currentThread().getId();
        categoryMap.put(threadId, category);

        if (category != null && !category.isEmpty()) {
            test.assignCategory(category);
        }

        return test;
    }

    /**
     * Sets the ExtentTest instance in ThreadLocal.
     */
    public static synchronized void setTestThread(ExtentTest test) {
        testThread.set(test);
    }

    /**
     * Retrieves the current test instance for the thread.
     */
    public static synchronized ExtentTest getCurrentTest() {
        return testThread.get();
    }

    /**
     * Retrieves category based on current thread
     */
    private static String getCategoryForCurrentThread() {
        long threadId = Thread.currentThread().getId();
        return categoryMap.getOrDefault(threadId, "Uncategorized");
    }

    /**
     * Assigns author to the test.
     */
    public static void assignAuthor(String author) {
        ExtentTest test = getCurrentTest();
        if (test != null) {
            test.assignAuthor(author);
            System.out.println("Assigned author: " + author);
        } else {
            logger.warning("Failed to assign author: No active test found.");
        }
    }

    /**
     * Logs a step in the report with an optional screenshot.
     */
    public static void logStep(String stepDescription, String statusAsString, boolean attachScreenshot, WebDriver driver) {
        ExtentTest test = getCurrentTest();

        if (test == null) {
            logger.warning("logStep(): No active test found! Skipping log: " + stepDescription);
            return;
        }

        Status status = Status.valueOf(statusAsString.toUpperCase());
        if (attachScreenshot && driver != null) {
            String screenshotName = takeScreenshot(driver);
            if (screenshotName != null) {
                String relativePath = "./" + screenshotName; // Ensure correct relative path
                test.log(status, stepDescription, MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());
            } else {
                test.log(status, stepDescription);
            }
        } else {
            test.log(status, stepDescription);
        }
    }

    public static void logStep(String stepDescription, String statusAsString) {
        logStep(stepDescription, statusAsString, true, DriverManager.getDriver());
    }

    public static void logStep(String stepDescription, String statusAsString, boolean attachScreenshot) {
        logStep(stepDescription, statusAsString, true, DriverManager.getDriver());
    }

        /**
         * Takes a screenshot and saves it in screenshot folder
         */
        public static String takeScreenshot(WebDriver driver) {
            if (driver == null) {
                logger.warning("WebDriver instance is null. Screenshot not taken.");
                return null;
            }

            try {
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

                // Retrieve the stored category
                String category = getCategoryForCurrentThread();
                if (category == null || category.isEmpty()) {
                    category = "Uncategorized";
                }

                // Define screenshot folder inside report folder
                String screenshotFolder = reportFolder + "/screenshots/" + category;
                File screenshotDir = new File(screenshotFolder);
                if (!screenshotDir.exists()) {
                    screenshotDir.mkdirs();
                }

                // Define screenshot name
                String screenshotName = "screenshot_" + System.currentTimeMillis() + ".jpg";
                String screenshotPath = screenshotFolder + "/" + screenshotName;

                // Save the screenshot
                File destFile = new File(screenshotPath);
                Files.copy(srcFile.toPath(), destFile.toPath());

                return "screenshots/" + category + "/" + screenshotName; // Returning relative path
            } catch (IOException e) {
                logger.warning("Failed to capture screenshot: " + e.getMessage());
                return null;
            }
        }

    /**
     * Flushes the Extent Reports instance at the end of execution.
     */
    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            System.out.println("Flushed Extent Reports.");
        }
    }
}
