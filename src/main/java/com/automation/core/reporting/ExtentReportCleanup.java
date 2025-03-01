package com.automation.core.reporting;

import com.aventstack.extentreports.ExtentReports;
import org.testng.annotations.AfterSuite;

public class ExtentReportCleanup {

    @AfterSuite
    public void tearDown() {
        ExtentReports extent = ExtentManager.getExtentReports();
        if (extent != null) {
            extent.flush();
        }
    }
}
