package com.automation.core.web;

import com.aventstack.extentreports.Status;
import com.automation.core.reporting.ExtentManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

public class WebDriverUtils implements WebDriverActions {
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
            ExtentManager.logStep("Clicked on element: " + locator, Status.PASS);
        } catch (Exception e) {
            ExtentManager.logStep("Failed to click element: " + locator + " - " + e.getMessage(), Status.FAIL);
        }
    }

    public void enterText(By locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            element.clear();
            element.sendKeys(text);
            ExtentManager.logStep("Entered text '" + text + "' into: " + locator, Status.PASS);
        } catch (Exception e) {
            ExtentManager.logStep("Failed to enter text: " + locator + " - " + e.getMessage(), Status.FAIL);
        }
    }

    public String getText(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            String text = element.getText();
            ExtentManager.logStep("Extracted text: " + text, Status.PASS);
            return text;
        } catch (Exception e) {
            ExtentManager.logStep("Failed to get text from: " + locator + " - " + e.getMessage(), Status.FAIL);
            return "";
        }
    }

    public boolean isElementDisplayed(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            ExtentManager.logStep("Element is displayed: " + locator, Status.PASS);
            return element.isDisplayed();
        } catch (Exception e) {
            ExtentManager.logStep("Element not visible: " + locator + " - " + e.getMessage(), Status.FAIL);
            return false;
        }
    }

    public void selectDropdownByVisibleText(By locator, String text) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            new Select(element).selectByVisibleText(text);
            ExtentManager.logStep("Selected dropdown value: " + text, Status.PASS);
        } catch (Exception e) {
            ExtentManager.logStep("Failed to select dropdown value: " + text + " - " + e.getMessage(), Status.FAIL);
        }
    }

    public void selectDropdownByIndex(By locator, int index) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            new Select(element).selectByIndex(index);
            ExtentManager.logStep("Selected dropdown value at index: " + index, Status.PASS);
        } catch (Exception e) {
            ExtentManager.logStep("Failed to select dropdown at index: " + index + " - " + e.getMessage(), Status.FAIL);
        }
    }

    public List<WebElement> getElements(By locator) {
        try {
            List<WebElement> elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            ExtentManager.logStep("Located multiple elements: " + locator, Status.PASS);
            return elements;
        } catch (Exception e) {
            ExtentManager.logStep("Failed to locate elements: " + locator + " - " + e.getMessage(), Status.FAIL);
            return null;
        }
    }

    public void switchToFrame(By locator) {
        try {
            WebElement frame = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            driver.switchTo().frame(frame);
            ExtentManager.logStep("Switched to frame: " + locator, Status.PASS);
        } catch (Exception e) {
            ExtentManager.logStep("Failed to switch to frame: " + locator + " - " + e.getMessage(), Status.FAIL);
        }
    }

    public void hoverOverElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
            ExtentManager.logStep("Hovered over element: " + locator, Status.PASS);
        } catch (Exception e) {
            ExtentManager.logStep("Failed to hover over element: " + locator + " - " + e.getMessage(), Status.FAIL);
        }
    }

    public void scrollToElement(By locator) {
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            ExtentManager.logStep("Scrolled to element: " + locator, Status.PASS);
        } catch (Exception e) {
            ExtentManager.logStep("Failed to scroll to element: " + locator + " - " + e.getMessage(), Status.FAIL);
        }
    }
}
