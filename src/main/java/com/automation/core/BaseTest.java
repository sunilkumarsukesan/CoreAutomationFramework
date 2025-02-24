package com.automation.core;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    @BeforeMethod
    public void setup() {
        System.out.println("Test Setup: Initialize WebDriver or API Client");
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Test Cleanup: Close WebDriver or API Client");
    }
}
