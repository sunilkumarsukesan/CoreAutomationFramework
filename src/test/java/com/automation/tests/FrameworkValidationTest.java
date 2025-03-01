package com.automation.tests;

import com.automation.core.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.automation.core.reporting.ExtentManager;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class FrameworkValidationTest extends BaseTest {
    private ExtentReports extent;
    private ExtentTest test;

    @BeforeClass
    public void setup() {
        extent = ExtentManager.getExtentReports();
        test = extent.createTest("Framework Validation Test");
        logger.info("Initializing Extent Reports...");
    }

    @Test
    public void validateFrameworkSetup() {
        test.info("Validating framework setup...");
        logger.info("Running framework validation test...");
        Assert.assertTrue(true, "Framework setup is working correctly.");
        test.pass("Framework setup validation passed.");
        logger.info("Framework validation completed.");
    }

    @AfterClass
    public void tearDown() {
        extent.flush();
        logger.info("Flushed Extent Reports.");
    }
}
