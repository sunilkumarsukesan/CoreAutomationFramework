package com.automation.core.config;

import com.automation.core.utils.PasswordManagerUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
            FileInputStream testSuiteConfig = new FileInputStream("src/test/resources/config/config.properties");
            testSuiteProperties.load(testSuiteConfig);
        } catch (IOException ignored) {
        }
    }

    public static String getEnvironment() {
        return testSuiteProperties.getProperty("env", coreProperties.getProperty("env", "SIT")).trim();
    }

    public static String getApplicationUrl(String appName) {
        String env = getEnvironment();
        return testSuiteProperties.getProperty(env + "." + appName + ".URL", coreProperties.getProperty(env + "." + appName + ".URL")).trim();
    }

    public static String getApplicationUsername(String appName) {
        String env = getEnvironment();
        return testSuiteProperties.getProperty(env + "." + appName + ".username", coreProperties.getProperty(env + "." + appName + ".username")).trim();
    }

    public static String getApplicationPassword(String appName) throws Exception {
        String env = getEnvironment();
        return new PasswordManagerUtil().decrypt(testSuiteProperties.getProperty(env + "." + appName + ".password", coreProperties.getProperty(env + "." + appName + ".password")).trim());
    }

    public static String getBrowser() {
        return testSuiteProperties.getProperty("browser", coreProperties.getProperty("browser", "chrome").trim());
    }

    public static String getApplication() {
        return testSuiteProperties.getProperty("application", coreProperties.getProperty("application", "test").trim());
    }

    public static String getTestDataPath() {
        return testSuiteProperties.getProperty("testDataPath", coreProperties.getProperty("testDataPath", "src/test/resources/testData/")); // Default is Chrome
    }

    public static long getTimeOut() {
        return Long.parseLong(testSuiteProperties.getProperty("timeout", coreProperties.getProperty("timeout", "10")));
    }
}
