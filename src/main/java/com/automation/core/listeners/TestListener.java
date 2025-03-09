package com.automation.core.listeners;

import com.automation.core.reporting.ExtentManager;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite Finished: " + context.getName());
        ExtentManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentManager.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription()
        );
        ExtentManager.setTestThread(test); // Ensuring the test is stored
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getCurrentTest().log(Status.PASS, "Test Passed ✅");
        System.out.println("Test Passed: " + result.getMethod().getMethodName());
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
                System.out.println("Failed to attach screenshot: " + e.getMessage());
            }
        }

        System.out.println("Test Failed ❌: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getCurrentTest().log(Status.SKIP, "Test Skipped ⚠️");
        System.out.println("Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("Test Partially Failed: " + result.getMethod().getMethodName());
    }

    private String takeScreenshot(String methodName) {
        try {
            return null; // Implement screenshot capture logic if needed
        } catch (Exception e) {
            System.out.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
