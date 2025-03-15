package com.automation.core.listeners;

import com.automation.core.hooks.Hooks;
import com.automation.core.logger.LoggerManager;
import com.automation.core.reporting.ExtentManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.slf4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final Logger logger = LoggerManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("Test Suite Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Test Suite Finished: " + context.getName());
        ExtentManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        if ((!result.getMethod().toString().equalsIgnoreCase("runScenario"))
        && (!result.getMethod().getDescription().toString().equalsIgnoreCase("Runs Cucumber Scenarios")))
        {
            // Extract category from TestNG groups (if any)
            String[] groups = result.getMethod().getGroups();
            String category = (groups.length > 0) ? groups[0] : "DefaultCategory"; // Use first group or default
            ExtentTest test = ExtentManager.createTest(
                    result.getMethod().getMethodName(),
                    result.getMethod().getDescription(),
                    category
            );
            ExtentManager.setTestThread(test); // Ensuring the test is stored
        }
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentManager.getCurrentTest().log(Status.FAIL, result.getThrowable());

        // Capture Screenshot (if applicable)
        String screenshotPath = takeScreenshot(result.getMethod().getMethodName());
        if (screenshotPath != null) {
            try {
                ExtentManager.getCurrentTest().addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                logger.info("Failed to attach screenshot: " + e.getMessage());
            }
        }

        logger.info("Test Failed : " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getCurrentTest().log(Status.SKIP, "Test Skipped ️");
        logger.info("Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logger.info("Test Partially Failed: " + result.getMethod().getMethodName());
    }

    private String takeScreenshot(String methodName) {
        try {
            return null; // Implement screenshot capture logic if needed
        } catch (Exception e) {
            logger.info("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
