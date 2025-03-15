package com.automation.core.hooks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScenarioContext {
    private static final Map<Long, String> scenarioTagMap = new ConcurrentHashMap<>();

    public static void setScenarioTag(String scenarioTag) {
        scenarioTagMap.put(Thread.currentThread().getId(), scenarioTag);
    }

    public static String getScenarioTagForCurrentThread() {
        return scenarioTagMap.get(Thread.currentThread().getId());
    }

    public static void clearScenarioTag() {
        scenarioTagMap.remove(Thread.currentThread().getId());
    }
}
