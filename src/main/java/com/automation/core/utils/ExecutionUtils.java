package com.automation.core.utils;

import com.automation.core.config.ConfigManager;
import io.cucumber.java.Scenario;

public class ExecutionUtils {

    public static String getApplicationFromFeatureFile(Scenario scenario) {
        String featurePath = scenario.getUri().toString();
        String[] pathParts = featurePath.split("/");
        if (pathParts.length > 1) {
            return pathParts[pathParts.length - 2]; // Get the folder name before the feature file
        }
        return "default/path";
    }
}
