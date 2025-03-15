package com.automation.core.web;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.automation.core.config.ConfigManager;
import com.automation.core.design.Locators;
import com.automation.core.drivers.DriverManager;
import com.automation.core.logger.LoggerManager;
import com.automation.core.reporting.ExtentManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.testng.Assert;

import static com.automation.core.drivers.DriverManager.getDriver;
import static com.automation.core.drivers.DriverManager.getWait;

public class WebDriverUtils extends ExtentManager implements WebDriverActions {
    public Actions act;
    private static final Logger logger = LoggerManager.getLogger(WebDriverUtils.class);

    public String getAttribute(WebElement ele, String attributeValue) {
        String val = "";
        try {
            val = ele.getAttribute(attributeValue);
        } catch (WebDriverException e) {
            logStep("Attribue value not able to fetch :" + e.getMessage(), "info");
            Assert.fail("Attribue value not able to fetch :" + e.getMessage());
        }
        return val;
    }

    public void moveToElement(WebElement ele) {
        act = new Actions(getDriver());
        act.moveToElement(ele).perform();
    }

    public void dragAndDrop(WebElement eleSoutce, WebElement eleTarget) {
        act = new Actions(getDriver());
        act.dragAndDrop(eleSoutce, eleTarget).perform();
    }

    public void contextClick(WebElement ele) {
        act = new Actions(getDriver());
        act.contextClick(getWait().until(ExpectedConditions.elementToBeClickable(ele))).perform();
    }

    public void hoverAndClick(WebElement ele) {
        act = new Actions(getDriver());
        act.moveToElement(getWait().until(ExpectedConditions.elementToBeClickable(ele))).pause(5000).click().perform();
    }

    public void doubleTap(WebElement ele) {
        act = new Actions(getDriver());
        act.click(getWait().until(ExpectedConditions.elementToBeClickable(ele))).click().perform();
        logStep("Element moved", "info");
    }

    public void doubleClick(WebElement ele) {
        act = new Actions(getDriver());
        act.doubleClick(getWait().until(ExpectedConditions.elementToBeClickable(ele))).perform();
        logStep("Element double clicked", "info");
    }


    public void waitForApperance(WebElement element) {
        long timeout = ConfigManager.getTimeOut();
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logStep("Element did not appear after " + timeout + " seconds", "fail", false);
            Assert.fail("Element did not appear after " + timeout + " seconds");
        }
    }

    public void clickUsingJs(WebElement ele) {
        String text = "";
        int attempts = 3; // Retry attempts for handling StaleElementReferenceException
        for (int i = 0; i < attempts; i++) {
            try {
                DriverManager.fluentWait(driver -> {
                    if (ele.isDisplayed() && ele.isEnabled()) {
                        return ele;
                    }
                    return null;
                });
                text = ele.getText();
                getDriver().executeScript("arguments[0].click()", ele);
                return; // Exit loop if click is successful
            } catch (StaleElementReferenceException e) {
                logStep("Retrying JS click due to StaleElementReferenceException: Attempt " + (i + 1), "info");
                getDriver().navigate().refresh();
            } catch (WebDriverException e) {
                logStep("The Element " + text + " could not be clicked due to: " + e.getMessage(), "fail");
                Assert.fail("The Element " + text + " could not be clicked due to: " + e.getMessage());
            } catch (Exception e) {
                logStep("The Element " + text + " could not be clicked due to: " + e.getMessage(), "fail");
                Assert.fail("The Element " + text + " could not be clicked due to: " + e.getMessage());
            }
        }

        // If all retries fail, log failure
        logStep("Failed to click element using JS after multiple attempts: " + text, "fail");
        Assert.fail("Failed to click element using JS after multiple attempts: " + text);
    }

    @Override
    public void click(WebElement ele) {
        String text = "";
        int attempts = 3; // Retry attempts for handling StaleElementReferenceException

        for (int i = 0; i < attempts; i++) {
            try {
                WebElement finalEle = ele;
                DriverManager.fluentWait(driver -> {
                    if (finalEle.isDisplayed() && finalEle.isEnabled()) {
                        return finalEle;
                    }
                    return null;
                });

                getWait().until(ExpectedConditions.elementToBeClickable(ele));
                getWait().until(ExpectedConditions.visibilityOf(ele));
                text = ele.getText();

                if (ele.isEnabled()) {
                    ele.click();
                } else {
                    clickUsingJs(ele);
                }
                return; // Exit loop if click is successful
            } catch (StaleElementReferenceException e) {
                // Re-locate element and retry
                logStep("Retrying click due to StaleElementReferenceException: Attempt " + (i + 1), "info");
                String locatorDetails = ele.toString();  // Example: "By.xpath: //span[text()='App Launcher']"
                Locators locatorType = Locators.valueOf(locatorDetails.split(": ")[0].replace("By.", ""));  // Extracts 'xpath', 'id', etc.
                String value = locatorDetails.split(": ", 2)[1];  // Extracts actual value
                ele = locateElement(locatorType, value); // Re-locate element before retry
            } catch (ElementClickInterceptedException e) {
                clickUsingJs(ele);
                return;
            } catch (WebDriverException e) {
                logStep("The Element " + text + " could not be clicked due to: " + e.getMessage(), "fail");
                Assert.fail("The Element " + text + " could not be clicked due to: " + e.getMessage());
            } catch (Exception e) {
                logStep("The Element " + text + " could not be clicked due to: " + e.getMessage(), "fail");
                Assert.fail("The Element " + text + " could not be clicked due to: " + e.getMessage());
            }
        }

        // If all retries fail, log failure
        logStep("Failed to click element after multiple attempts: " + text, "fail");
        Assert.fail("Failed to click element after multiple attempts: " + text);
    }

    public void clickWithNoSnap(WebElement ele) {
        String text = ele.getText();
        try {
            getWait().until(ExpectedConditions.elementToBeClickable(ele));
            ele.click();
        } catch (StaleElementReferenceException e) {
            logStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        } catch (WebDriverException e) {
            logStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        } catch (Exception e) {
            logStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        }
    }

    @Override
    public void append(WebElement ele, String data) {
        try {
            String attribute = ele.getAttribute("value");
            if (attribute.length() > 1) {
                ele.sendKeys(data);
            } else {
                ele.sendKeys(data);
            }
        } catch (WebDriverException e) {
            logStep("The Element " + ele + " could not be appended \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " could not be appended \n" + e.getMessage());
        }
    }

    @Override
    public void clear(WebElement ele) {
        try {
            ele.clear();
        } catch (ElementNotInteractableException e) {
            logStep("The field is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The field is not Interactable \n" + e.getMessage());
        }
    }

    public void clearAndType(WebElement ele, CharSequence... data) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(ele));
            ele.clear();
            ele.sendKeys(data);
        } catch (ElementNotInteractableException e) {
            logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) { // retry - 1
            pause(500);
            try {
                ele.sendKeys(data);
            } catch (Exception e1) {
                logStep("The Element " + ele + " did not allow to clear / type \n" + e.getMessage(), "fail");
                Assert.fail("The Element " + ele + " did not allow to clear / type \n" + e.getMessage());
            }
        }

    }

    @Override
    public void clearAndType(WebElement ele, String data) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(ele));
            ele.clear();
            ele.sendKeys("", "", data);
        } catch (ElementNotInteractableException e) {
            logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) { // retry - 1
            pause(500);
            try {
                ele.sendKeys(data);
            } catch (Exception e1) {
                logStep("The Element " + ele + " did not allow to clear / type \n" + e.getMessage(), "fail");
                Assert.fail("The Element " + ele + " did not allow to clear / type \n" + e.getMessage());
            }
        }

    }

    public void typeAndTab(WebElement ele, String data) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(ele));
            ele.clear();
            ele.sendKeys("", "", data, Keys.TAB);
        } catch (ElementNotInteractableException e) {
            logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) {
            logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        }

    }

    public void type(WebElement ele, String data) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(ele));
            ele.clear();
            ele.sendKeys("", "", data);
        } catch (ElementNotInteractableException e) {
            logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) {
            logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        }

    }

    public void typeAndEnter(WebElement ele, String data) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(ele));
            ele.clear();
            ele.sendKeys("", "", data, Keys.ENTER);
        } catch (ElementNotInteractableException e) {
            logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) {
            logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());

        }

    }

    @Override
    public String getElementText(WebElement ele) {
        try {
            String text = ele.getText();
            logStep("Text has been retrieved " + text, "info");
            return text;
        } catch (WebDriverException e) {
            logStep("Sorry! text is not available \n" + e.getMessage(), "fail");
            Assert.fail("Sorry! text is not available \n" + e.getMessage());
        } catch (Exception e) {
            logStep("Sorry! text is not available \n" + e.getMessage(), "fail");
            Assert.fail("Sorry! text is not available \n" + e.getMessage());
        }
        return null;
    }

    @Override
    public String getBackgroundColor(WebElement ele) {
        String cssValue = null;
        try {
            cssValue = ele.getCssValue("color");
            logStep("The background color is " + cssValue, "info");
        } catch (WebDriverException e) {
            logStep("Not able to get the background color \n" + e.getMessage(), "fail");
            Assert.fail("Not able to get the background color \n" + e.getMessage());
        } catch (Exception e) {
            logStep("Not able to get the background color \n" + e.getMessage(), "fail");
            Assert.fail("Not able to get the background color \n" + e.getMessage());

        }
        return cssValue;
    }

    @Override
    public String getTypedText(WebElement ele) {
        String attributeValue = null;
        try {
            attributeValue = ele.getAttribute("value");
            logStep("The attribute value is " + attributeValue, "info");
        } catch (WebDriverException e) {
            logStep("Not able to find attribute value \n" + e.getMessage(), "fail");
            Assert.fail("Not able to find attribute value \n" + e.getMessage());
        }
        return attributeValue;
    }

    @Override
    public void selectDropDownUsingText(WebElement ele, String value) {
        try {
            Select sel = new Select(ele);
            sel.selectByVisibleText(value);
        } catch (WebDriverException e) {
            logStep("Not able to select the drop down with text \n" + value, "fail");
            Assert.fail("Not able to select the drop down with text \n" + value);
        }
    }

    @Override
    public void selectDropDownUsingIndex(WebElement ele, int index) {
        try {
            Select sel = new Select(ele);
            sel.selectByIndex(index);
        } catch (WebDriverException e) {
            logStep("Not able to select the drop down with index " + index + " \n" + e.getMessage(), "fail");
            Assert.fail("Not able to select the drop down with index " + index + " \n" + e.getMessage());
        }
    }

    @Override
    public void selectDropDownUsingValue(WebElement ele, String value) {
        try {
            Select sel = new Select(ele);
            sel.selectByValue(value);
        } catch (WebDriverException e) {
            logStep("Not able to select the drop down with value " + value + " \n" + e.getMessage(), "fail");
            Assert.fail("Not able to select the drop down with value " + value + " \n" + e.getMessage());
        }
    }

    @Override
    public boolean verifyExactText(WebElement ele, String expectedText) {
        try {
            String text = ele.getText();
            if (text.contains(expectedText)) {
                return true;
            } else {
                logStep("The expected text " + text + "doesn't equals to the  " + expectedText, "fail");
                Assert.fail("The expected text " + text + "doesn't equals to the  " + expectedText);

            }
        } catch (WebDriverException e) {
            logStep("Unknown exception occured while verifying the Text \n" + e.getMessage(), "fail");
            Assert.fail("Unknown exception occured while verifying the Text \n" + e.getMessage());

        }

        return false;
    }

    @Override
    public boolean verifyPartialText(WebElement ele, String expectedText) {
        try {
            if (ele.getText().contains(expectedText)) {
                return true;
            } else {
                logStep("The expected text doesn't contain the actual " + expectedText, "warning");
                Assert.fail("The expected text doesn't contain the actual " + expectedText);
            }
        } catch (WebDriverException e) {
            logStep("Unknown exception occured while verifying the Text \n" + e.getMessage(), "fail");
            Assert.fail("Unknown exception occured while verifying the Text \n" + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean verifyExactAttribute(WebElement ele, String attribute, String value) {
        try {
            if (ele.getAttribute(attribute).equals(value)) {
                return true;
            } else {
                logStep("The expected attribute :" + attribute + " value does not contains the actual " + value,  "warning");
                Assert.fail("The expected attribute :" + attribute + " value does not contains the actual " + value);
            }
        } catch (WebDriverException e) {
            logStep("Unknown exception occured while verifying the Attribute Text \n" + e.getMessage(), "fail");
            Assert.fail("Unknown exception occured while verifying the Attribute Text \n" + e.getMessage());
        }
        return false;
    }

    @Override
    public void verifyPartialAttribute(WebElement ele, String attribute, String value) {
        try {
            if (ele.getAttribute(attribute).contains(value)) {
                logStep("The expected attribute :" + attribute + " value contains the actual " + value, "pass");
            } else {
                logStep("The expected attribute :" + attribute + " value does not contains the actual " + value,
                        "warning");
                Assert.fail("The expected attribute :" + attribute + " value does not contains the actual " + value);
            }
        } catch (WebDriverException e) {
            logStep("Unknown exception occured while verifying the Attribute Text \n" + e.getMessage(), "fail");
            Assert.fail("Unknown exception occured while verifying the Attribute Text \n" + e.getMessage());
        }

    }

    @Override
    public boolean verifyDisplayed(WebElement ele) {
        try {
            if (ele.isDisplayed()) {
                return true;
            } else {
                logStep("The element " + ele + " is not visible", "warnings");
                Assert.fail("The element " + ele + " is not visible");
            }
        } catch (WebDriverException e) {
            logStep("WebDriverException : \n" + e.getMessage(), "fail");
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }
        return false;

    }

    @Override
    public boolean verifyDisappeared(WebElement ele) {
        try {
            Boolean until = getWait().until(ExpectedConditions.invisibilityOf(ele));
            logStep("Waited for an element to disappear", "info");
            return until;
        } catch (org.openqa.selenium.TimeoutException e) {
            logStep("Element not disappeared \n" + e.getMessage(), "fail");
            Assert.fail("Element not disappeared \n" + e.getMessage());
        } catch (Exception e) {
            logStep("Element not disappeared \n" + e.getMessage(), "fail");
            Assert.fail("Element not disappeared \n" + e.getMessage());
        }
        return false;

    }

    @Override
    public boolean verifyEnabled(WebElement ele) {
        try {
            if (ele.isEnabled()) {
                return true;
            } else {
                logStep("The element " + ele + " is not Enabled", "warning");
                Assert.fail("The element " + ele + " is not enabled");
            }
        } catch (WebDriverException e) {
            logStep("WebDriverException : \n" + e.getMessage(), "fail");
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean verifySelected(WebElement ele) {
        try {
            if (ele.isSelected()) {
                return true;
            } else {
                logStep("The element " + ele + " is not selected", "warning");
                Assert.fail("The element " + ele + " is not selected");
            }
        } catch (WebDriverException e) {
            logStep("WebDriverException : \n" + e.getMessage(), "fail");
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }
        return false;

    }

    @Override
    public WebElement locateElement(Locators type, String value) {
        try {
            switch (type) {
                case CLASS_NAME:
                    return getDriver().findElement(By.className(value));
                case CSS:
                    return getDriver().findElement(By.cssSelector(value));
                case ID:
                    return getDriver().findElement(By.id(value));
                case LINK_TEXT:
                    return getDriver().findElement(By.linkText(value));
                case NAME:
                    return getDriver().findElement(By.name(value));
                case PARTIAL_LINKTEXT:
                    return getDriver().findElement(By.partialLinkText(value));
                case TAGNAME:
                    return getDriver().findElement(By.tagName(value));
                case XPATH:
                    return getDriver().findElement(By.xpath(value));
                default:
                    logger.error("Locator is not Valid for " + value);
                    break;
            }
        } catch (NoSuchElementException e) {
            logStep("The Element with locator:" + type + " Not Found with value: " + value + "\n" + e.getMessage(),
                    "fail");
        }
        return null;
    }

    @Override
    public WebElement locateElement(String value) {
        try {
            WebElement findElementByXpath = getDriver().findElement(By.xpath(value));
            return findElementByXpath;
        } catch (NoSuchElementException e) {
            logStep("The Element with locator Xpath Not Found with value: " + value + "\n" + e.getMessage(), "fail");
            Assert.fail("The Element with locator Xpath Not Found with value: " + value + "\n" + e.getMessage());
        } catch (Exception e) {
            logStep("The Element with locator Xpath Not Found with value: " + value + "\n" + e.getMessage(), "fail");
            Assert.fail("The Element with locator Xpath Not Found with value: " + value + "\n" + e.getMessage());
        }
        return null;
    }

    @Override
    public List<WebElement> locateElements(Locators type, String value) {
        try {
            switch (type) {
                case CLASS_NAME:
                    return getDriver().findElements(By.className(value));
                case CSS:
                    return getDriver().findElements(By.cssSelector(value));
                case ID:
                    return getDriver().findElements(By.id(value));
                case LINK_TEXT:
                    return getDriver().findElements(By.linkText(value));
                case NAME:
                    return getDriver().findElements(By.name(value));
                case PARTIAL_LINKTEXT:
                    return getDriver().findElements(By.partialLinkText(value));
                case TAGNAME:
                    return getDriver().findElements(By.tagName(value));
                case XPATH:
                    return getDriver().findElements(By.xpath(value));
                default:
                    logger.error("Locator is not Valid for " + value);
                    break;
            }
        } catch (NoSuchElementException e) {
            logStep("The Element with locator:" + type + " Not Found with value: " + value + "\n" + e.getMessage(),
                    "fail");
            Assert.fail("The Element with locator:" + type + " Not Found with value: " + value + "\n" + e.getMessage());
        }
        return null;
    }

    @Override
    public void switchToAlert() {
        try {
            getDriver().switchTo().alert();
            logStep("Focus has been switched to Alert", "info", false);
        } catch (NoAlertPresentException e) {
            logStep("There is no alert present.", "fail", false);
            Assert.fail("There is no alert present");
        } catch (WebDriverException e) {
            logStep("WebDriverException : " + e.getMessage(), "fail", false);
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }
    }

    @Override
    public void acceptAlert() {
        String text = "";
        try {
            getWait().until(ExpectedConditions.alertIsPresent());
            Alert alert = getDriver().switchTo().alert();
            text = alert.getText();
            alert.accept();
            logStep("The alert " + text + " is accepted.", "pass", false);
        } catch (NoAlertPresentException e) {
            logStep("There is no alert present.", "fail", false);
            Assert.fail("There is no alert present");
        } catch (WebDriverException e) {
            logStep("WebDriverException : " + e.getMessage(), "fail", false);
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }

    }

    @Override
    public void dismissAlert() {
        String text = "";
        try {
            Alert alert = getDriver().switchTo().alert();
            text = alert.getText();
            alert.dismiss();
            logStep("The alert " + text + " is accepted.", "pass", false);
        } catch (NoAlertPresentException e) {
            logStep("There is no alert present.", "pass", false);
            Assert.fail("There is no alert present");
        } catch (WebDriverException e) {
            logStep("WebDriverException : " + e.getMessage(), "fail", false);
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }

    }

    @Override
    public String getAlertText() {
        String text = "";
        try {
            Alert alert = getDriver().switchTo().alert();
            text = alert.getText();
            logStep("The alert text is " + text, "pass", false);
        } catch (NoAlertPresentException e) {
            logStep("There is no alert present.", "fail", false);
            Assert.fail("There is no alert present");
        } catch (WebDriverException e) {
            logStep("WebDriverException : \n" + e.getMessage(), "fail", false);
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }
        return text;
    }

    @Override
    public void typeAlert(String data) {
        try {
            getDriver().switchTo().alert().sendKeys(data);
        } catch (NoAlertPresentException e) {
            logStep("There is no alert present.", "fail", false);
            Assert.fail("There is no alert present");
        } catch (WebDriverException e) {
            logStep("WebDriverException : \n" + e.getMessage(), "fail", false);
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }
    }

    @Override
    public void switchToWindow(int index) {
        try {
            Set<String> allWindows = getDriver().getWindowHandles();
            List<String> allhandles = new ArrayList<String>(allWindows);
            getDriver().switchTo().window(allhandles.get(index));
            logStep("The Window With index: " + index + " switched successfully", "info", false);
            logStep(getDriver().getTitle(), "info");
        } catch (NoSuchWindowException e) {
            logStep("The Window With index: " + index + " not found\n" + e.getMessage(), "fail", false);
            Assert.fail("The Window With index: " + index + " not found\n" + e.getMessage());
        } catch (Exception e) {
            logStep("The Window With index: " + index + " not found\n" + e.getMessage(), "fail", false);
            Assert.fail("The Window With index: " + index + " not found\n" + e.getMessage());
        }
    }

    @Override
    public boolean switchToWindow(String title) {
        try {
            Set<String> allWindows = getDriver().getWindowHandles();
            for (String eachWindow : allWindows) {
                getDriver().switchTo().window(eachWindow);
                if (getDriver().getTitle().equals(title)) {
                    break;
                }
            }
            logStep("The Window With Title: " + title + "is switched ", "info");
            return true;
        } catch (NoSuchWindowException e) {
            logStep("The Window With Title: " + title + " not found", "fail", false);
            Assert.fail("The Window With Title: " + title + " not found");
        }
        return false;
    }

    @Override
    public void switchToFrame(int index) {
        try {
            Thread.sleep(100);
            getDriver().switchTo().frame(index);
        } catch (NoSuchFrameException e) {
            logStep("No such frame " + e.getMessage(), "warning", false);
            Assert.fail("No such frame " + e.getMessage());
        } catch (Exception e) {
            logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        }

    }

    @Override
    public void switchToFrame(WebElement ele) {
        try {
            getDriver().switchTo().frame(ele);
        } catch (NoSuchFrameException e) {
            logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        } catch (Exception e) {
            logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        }

    }

    public void switchToFrameUsingXPath(String xpath) {
        try {
            getDriver().switchTo().frame(locateElement(Locators.XPATH, xpath));
        } catch (NoSuchFrameException e) {
            logStep("No such frame " + e.getMessage(), "warning", false);
            Assert.fail("No such frame " + e.getMessage());
        } catch (Exception e) {
            logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        }

    }

    @Override
    public void switchToFrame(String idOrName) {
        try {
            getDriver().switchTo().frame(idOrName);
        } catch (NoSuchFrameException e) {
            logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        } catch (Exception e) {
            logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        }
    }

    @Override
    public void defaultContent() {
        try {
            getDriver().switchTo().defaultContent();
        } catch (Exception e) {
            logStep("No such window " + e.getMessage(), "fail", false);
            Assert.fail("No such window " + e.getMessage());
        }
    }

    @Override
    public boolean verifyUrl(String url) {
        if (getDriver().getCurrentUrl().equals(url)) {
            logStep("The url: " + url + " matched successfully", "info");
            return true;
        } else {
            logStep("The url: " + url + " not matched", "fail");
            Assert.fail("The url: " + url + " not matched");
        }
        return false;
    }

    @Override
    public boolean verifyTitle(String title) {
        if (getDriver().getTitle().equals(title)) {
            logStep("Page title: " + title + " matched successfully", "info");
            return true;
        } else {
            logStep("Page url: " + title + " not matched", "fail");
            Assert.fail("Page url: " + title + " not matched");
        }
        return false;
    }

    @Override
    public long takeSnap() {
        long number = (long) Math.floor(Math.random() * 900000000L) + 10000000L;
        try {
            FileUtils.copyFile(getDriver().getScreenshotAs(OutputType.FILE),
                    new File("./" + ExtentManager.reportFolder + "/images/" + number + ".jpg"));
        } catch (WebDriverException e) {
            logStep("The browser has been closed." + e.getMessage(), "fail");
            Assert.fail("The browser has been closed." + e.getMessage());
        } catch (IOException e) {
            logStep("The snapshot could not be taken " + e.getMessage(), "warning");
            Assert.fail("The snapshot could not be taken " + e.getMessage());
        }
        return number;
    }

    @Override
    public void close() {
        try {
            getDriver().close();
            logStep("Browser is closed", "info", false);
        } catch (Exception e) {
            logStep("Browser cannot be closed " + e.getMessage(), "fail", false);
            Assert.fail("Browser cannot be closed " + e.getMessage());
        }
    }

    @Override
    public void quit() {
        try {
            getDriver().quit();
            logStep("Browser is closed", "info", false);
        } catch (Exception e) {
            logStep("Browser cannot be closed " + e.getMessage(), "fail", false);
            Assert.fail("Browser cannot be closed " + e.getMessage());
        }
    }

    public void waitForDisapperance(WebElement element) {
        long timeout = ConfigManager.getTimeOut();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
            wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception e) {
            logStep("Element did not appear after " + timeout + " seconds", "fail", false);
            Assert.fail("Element did not appear after " + timeout + " seconds");
        }

    }

    public void pause(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void chooseDate(WebElement ele, String data) {
        try {
            getDriver().executeScript("arguments[0].setAttribute('value', '" + data + "')", ele);
            logStep("The Data :" + data + " entered Successfully", "pass");
        } catch (ElementNotInteractableException e) {
            logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) {
            logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        }

    }

    public void fileUpload(WebElement ele, String data) {
        try {
            hoverAndClick(ele);
            pause(2000);

            // Store the copied text in the clipboard
            StringSelection stringSelection = new StringSelection(data);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

            // Paste it using Robot class
            Robot robot = new Robot();

            // Enter to confirm it is uploaded
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            Thread.sleep(5000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            logStep("The file is selected Successfully", "pass");
        } catch (Exception e) {
            logStep("The file is not selected", "fail");
            Assert.fail("The file is not selected");
        }

    }

    public void fileUploadWithJs(WebElement ele, String data) {
        try {

            clickUsingJs(ele);
            ;
            pause(2000);

            // Store the copied text in the clipboard
            StringSelection stringSelection = new StringSelection(data);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

            // Paste it using Robot class
            Robot robot = new Robot();

            // Enter to confirm it is uploaded
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);

            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            Thread.sleep(5000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            logStep("The file is selected Successfully", "pass");
        } catch (Exception e) {
            logStep("The file is not selected", "fail");
            Assert.fail("The file is not selected");
        }

    }

    @Override
    public void executeTheScript(String js, WebElement ele) {
        getDriver().executeScript(js, ele);
    }

}