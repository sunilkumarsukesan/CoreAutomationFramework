package com.automation.core.web;

import com.aventstack.extentreports.Status;
import com.automation.core.reporting.ExtentManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class WebDriverUtils {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final Logger logger = Logger.getLogger(WebDriverUtils.class.getName());

    public WebDriverUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
            element.click();
            logger.info("Clicked on element: " + locator);
            ExtentManager.getExtentReports().createTest("Click Element").log(Status.PASS, "Clicked on element: " + locator);
        } catch (Exception e) {
            logger.severe("Failed to click element: " + locator + " - " + e.getMessage());
            ExtentManager.getExtentReports().createTest("Click Element").log(Status.FAIL, "Failed to click element: " + locator);
        }
    }

    public void enterText(By locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
            logger.info("Entered text '" + text + "' into: " + locator);
            ExtentManager.getExtentReports().createTest("Enter Text").log(Status.PASS, "Entered text '" + text + "' into: " + locator);
        } catch (Exception e) {
            logger.severe("Failed to enter text: " + locator + " - " + e.getMessage());
            ExtentManager.getExtentReports().createTest("Enter Text").log(Status.FAIL, "Failed to enter text in: " + locator);
        }
    }

    public String getText(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String text = element.getText();
            logger.info("Extracted text: " + text);
            ExtentManager.getExtentReports().createTest("Get Text").log(Status.PASS, "Extracted text: " + text);
            return text;
        } catch (Exception e) {
            logger.severe("Failed to get text from: " + locator + " - " + e.getMessage());
            ExtentManager.getExtentReports().createTest("Get Text").log(Status.FAIL, "Failed to extract text from: " + locator);
            return "";
        }
    }

    public boolean isElementDisplayed(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            boolean displayed = element.isDisplayed();
            logger.info("Element visibility: " + displayed);
            ExtentManager.getExtentReports().createTest("Element Display").log(Status.PASS, "Element is displayed: " + locator);
            return displayed;
        } catch (Exception e) {
            logger.warning("Element not visible: " + locator);
            ExtentManager.getExtentReports().createTest("Element Display").log(Status.FAIL, "Element not visible: " + locator);
            return false;
        }
    }

    public void selectDropdownByVisibleText(By locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Select dropdown = new Select(element);
            dropdown.selectByVisibleText(text);
            logger.info("Selected dropdown value: " + text);
            ExtentManager.getExtentReports().createTest("Select Dropdown").log(Status.PASS, "Selected dropdown value: " + text);
        } catch (Exception e) {
            logger.severe("Failed to select dropdown value: " + text + " - " + e.getMessage());
            ExtentManager.getExtentReports().createTest("Select Dropdown").log(Status.FAIL, "Failed to select dropdown value: " + text);
        }
    }

    public void selectDropdownByIndex(By locator, int index) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Select dropdown = new Select(element);
            dropdown.selectByIndex(index);
            logger.info("Selected dropdown value at index: " + index);
            ExtentManager.getExtentReports().createTest("Select Dropdown").log(Status.PASS, "Selected dropdown value at index: " + index);
        } catch (Exception e) {
            logger.severe("Failed to select dropdown at index: " + index + " - " + e.getMessage());
            ExtentManager.getExtentReports().createTest("Select Dropdown").log(Status.FAIL, "Failed to select dropdown at index: " + index);
        }
    }

    public List<WebElement> getElements(By locator) {
        try {
            List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            logger.info("Located multiple elements: " + locator);
            return elements;
        } catch (Exception e) {
            logger.warning("Failed to locate elements: " + locator + " - " + e.getMessage());
            return null;
        }
    }
}
