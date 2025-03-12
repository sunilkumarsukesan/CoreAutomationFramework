package com.automation.core.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    private static Properties coreProperties = new Properties();
    private static Properties testSuiteProperties = new Properties();

    static {
        loadCoreProperties();
        loadTestSuiteProperties();
    }

    private static void loadCoreProperties() {
        try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("CoreAutomationFramework config.properties not found in resources!");
            }
            coreProperties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load CoreAutomationFramework properties", e);
        }
    }


    private static void loadTestSuiteProperties() {
        try {
            FileInputStream testSuiteConfig = new FileInputStream("src/test/resources/config/config.properties"); // Assuming this is in TestAutomationSuite
            testSuiteProperties.load(testSuiteConfig);
        } catch (IOException ignored) {
            // If TestAutomationSuite does not have a config, continue to default.
        }
    }

    public static String getApplicationUrl() {
        return testSuiteProperties.getProperty("baseUrl", coreProperties.getProperty("baseUrl"));
    }

    public static String getBrowser() {
        return testSuiteProperties.getProperty("browser", coreProperties.getProperty("browser", "chrome")); // Default is Chrome
    }

    public static String getApplicationName() {
        return testSuiteProperties.getProperty("ApplicationName", coreProperties.getProperty("ApplicationName", "Salesforce")); // Default is Chrome
    }

    public static String getTestDataPath() {
        return testSuiteProperties.getProperty("testDataPath", coreProperties.getProperty("testDataPath", "src/test/resources/testData/")); // Default is Chrome
    }
}
