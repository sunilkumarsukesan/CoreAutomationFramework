package com.automation.core.hooks;

import com.automation.core.config.ConfigManager;
import com.automation.core.drivers.DriverManager;
import com.automation.core.reporting.ExtentManager;
import com.automation.core.utils.ExecutionUtils;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.Collection;

public class Hooks {
    private static String currentScenarioTag = "";
    public static String application;

    @Before
    public void setUpScenario(Scenario scenario) {
        System.out.println("Setting up WebDriver before scenario: " + scenario.getName());
        Collection<String> tags = scenario.getSourceTagNames();

        /// Extract the last tag (assuming it's the scenario-specific tag)
        String currentScenarioTag = tags.stream()
                .reduce((first, second) -> second) // Get the last tag
                .orElse(""); // Default to empty string if no tags found

        // Remove '@' prefix if needed
        currentScenarioTag = currentScenarioTag.replace("@", "");


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
        System.out.println("Navigated to URL: " + baseUrl);

        // Initialize Extent Reports
        ExtentTest test = ExtentManager.createTest(scenario.getName(), scenario.getSourceTagNames().toString(), currentScenarioTag);
        ExtentManager.setTestThread(test);
    }

    @After
    public void tearDownScenario(Scenario scenario) {
        System.out.println("Quitting WebDriver after scenario: " + scenario.getName());

        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/jpg", "Failure Screenshot");
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