package com.automation.core.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeatureFileParser {

    private static final Pattern FEATURE_LEVEL_PATTERN = Pattern.compile("(?i)TestDataSheet:\\s*(\\S+)");
    private static final Pattern SCENARIO_LEVEL_PATTERN = Pattern.compile("(?i)\\s*TestDataSheet:\\s*(\\S+)");

    public static Map<String, String> extractTestDataSheets(String featureFilePath) throws IOException {
        Map<String, String> testDataMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(featureFilePath))) {
            String line;
            String featureLevelSheet = null;
            String currentScenario = null;

            while ((line = reader.readLine()) != null) {
                // Convert line to lowercase for consistent matching
                String normalizedLine = line.toLowerCase();

                // Check for feature-level test data sheet
                Matcher featureMatcher = FEATURE_LEVEL_PATTERN.matcher(normalizedLine);
                if (featureMatcher.find() && featureLevelSheet == null) {
                    featureLevelSheet = featureMatcher.group(1);
                }

                // Check for scenario-level test data sheet
                Matcher scenarioMatcher = SCENARIO_LEVEL_PATTERN.matcher(normalizedLine);
                if (scenarioMatcher.find() && currentScenario != null) {
                    testDataMap.put(currentScenario, scenarioMatcher.group(1));
                }

                // Identify scenario start
                if (line.trim().toLowerCase().startsWith("scenario")) {
                    currentScenario = line.trim();
                    testDataMap.put(currentScenario, featureLevelSheet); // Default to feature-level sheet
                }
            }
        }
        return testDataMap;
    }

    public static void main(String[] args) throws IOException {
        String featureFilePath = "F:\\Projects\\Java\\TestAutomationSuite\\src\\test\\resources\\features\\Salesforce\\Login.feature";
        Map<String, String> testDataSheets = extractTestDataSheets(featureFilePath);

        for (Map.Entry<String, String> entry : testDataSheets.entrySet()) {
            System.out.println("Scenario: " + entry.getKey() + " -> TestDataSheet: " + entry.getValue());
        }
    }
}