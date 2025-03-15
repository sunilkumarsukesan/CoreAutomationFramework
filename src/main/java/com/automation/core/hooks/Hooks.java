package com.automation.core.hooks;

import com.automation.core.config.ConfigManager;
import com.automation.core.drivers.DriverManager;
import com.automation.core.logger.LoggerManager;
import com.automation.core.reporting.ExtentManager;
import com.automation.core.utils.ExecutionUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;

import java.util.Collection;

public class Hooks {
    private static String currentScenarioTag = "";
    public static String application;
    private static final Logger logger = LoggerManager.getLogger(Hooks.class);

    @Before
    public void setUpScenario(Scenario scenario) {
        logger.info("Setting up WebDriver before scenario: " + scenario.getName());
        Collection<String> tags = scenario.getSourceTagNames();

        /// Extract the last tag (assuming it's the scenario-specific tag)
        String currentScenarioTag = tags.stream()
                .reduce((first, second) -> second) // Get the last tag
                .orElse("")
                .replace("@", "");

        // Extract first tag (Feature tag)
        String featureTag = tags.stream()
                .findFirst() // Get the first tag
                .orElse("")
                .replace("@", ""); // Remove '@'

        ScenarioContext.setScenarioTag(currentScenarioTag);

        // Initialize WebDriver
        String browser = ConfigManager.getBrowser();
        DriverManager.initDriver(browser);

        //Get Application Name
        application = ExecutionUtils.getApplicationFromFeatureFile(scenario);
        String baseUrl = ConfigManager.getApplicationUrl(application);

        // Navigate to application URL
        if (baseUrl == null || baseUrl.isEmpty()) {
            throw new IllegalStateException("Base URL is not set. Please check ConfigManager.");
        }
        DriverManager.getDriver().get(baseUrl);
        logger.info("Navigated to URL: " + baseUrl);

        // Initialize Extent Reports
        ExtentTest test = ExtentManager.createTest(currentScenarioTag, scenario.getName(), featureTag);
        ExtentManager.setTestThread(test);
    }

    @After
    public void tearDownScenario(Scenario scenario) {
        logger.info("Quitting WebDriver after scenario: " + scenario.getName());

        if (scenario.isFailed()) {
            ExtentManager.getCurrentTest().log(Status.FAIL, "Scenario Failed");
        } else {
            ExtentManager.getCurrentTest().log(Status.PASS, "Scenario Passed");
        }

        // Flush reports and clean up WebDriver
        ExtentManager.flushReports();
        DriverManager.quitDriver();
    }

    public static String getScenarioSpecificTag() {
        return currentScenarioTag;
    }
}