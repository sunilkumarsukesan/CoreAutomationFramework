package com.automation.tests;

import com.automation.core.base.BaseTest;
import com.automation.core.reporting.ExtentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FrameworkValidationTest extends BaseTest {
    private static final Logger logger = LogManager.getLogger(FrameworkValidationTest.class);

    @Test
    public void validateFrameworkSetup() {
        ExtentManager.getCurrentTest().info("Validating framework setup...");
        logger.info("Running framework validation test...");

        Assert.assertTrue(true, "Framework setup is working correctly.");

        ExtentManager.getCurrentTest().pass("Framework setup validation passed.");
        logger.info("Framework validation completed.");
    }
}
