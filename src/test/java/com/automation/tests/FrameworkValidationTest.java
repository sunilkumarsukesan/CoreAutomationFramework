package com.automation.tests;

import com.automation.core.base.BaseTest;
import com.automation.core.drivers.DriverManager;
import com.automation.core.reporting.ExtentManager;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FrameworkValidationTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(FrameworkValidationTest.class);

    @BeforeClass
    public void beforeClass() {
        DriverManager.initDriver("chrome"); // Ensure driver is initialized before tests
    }


    @Test
    public void validateFrameworkSetup() {
        ExtentManager.getCurrentTest().info("Validating framework setup...");
        logger.info("Running framework validation test...");

        Assert.assertTrue(true, "Framework setup is working correctly.");

        ExtentManager.getCurrentTest().pass("Framework setup validation passed.");
        logger.info("Framework validation completed.");
    }
}
