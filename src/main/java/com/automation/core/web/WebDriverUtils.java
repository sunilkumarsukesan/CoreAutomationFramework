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
import com.automation.core.design.Locators;
import com.automation.core.reporting.ExtentManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import static com.automation.core.drivers.DriverManager.getDriver;
import static com.automation.core.drivers.DriverManager.getWait;

public class WebDriverUtils implements WebDriverActions {
    public Actions act;

    public String getAttribute(WebElement ele, String attributeValue) {
        String val = "";
        try {
            val = ele.getAttribute(attributeValue);
        } catch (WebDriverException e) {
            ExtentManager.logStep("Attribue value not able to fetch :" + e.getMessage(), "info");
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
        ExtentManager.logStep("Element moved", "info");
    }

    public void doubleClick(WebElement ele) {
        act = new Actions(getDriver());
        act.doubleClick(getWait().until(ExpectedConditions.elementToBeClickable(ele))).perform();
        ExtentManager.logStep("Element double clicked", "info");
    }

    public void waitForApperance(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            ExtentManager.logStep("Element did not appear after 10 seconds", "fail", false);
            Assert.fail("Element did not appear after 10 seconds");
        }
    }

    @Override
    public void click(WebElement ele) {
        String text = "";
        try {
            try {
                Thread.sleep(500);
                getWait().until(ExpectedConditions.elementToBeClickable(ele));
                text = ele.getText();
                if (ele.isEnabled()) {
                    ele.click();
                } else {
                    getDriver().executeScript("arguments[0].click()", ele);
                }
            } catch (Exception e) {
                boolean bFound = false;
                int totalTime = 0;
                while (!bFound && totalTime < 10000) {
                    try {
                        Thread.sleep(500);
                        ele.click();
                        bFound = true;

                    } catch (Exception e1) {
                        bFound = false;
                    }
                    totalTime = totalTime + 500;
                }
                if (!bFound)
                    ele.click();
            }
        } catch (StaleElementReferenceException e) {
            ExtentManager.logStep("The Element " + text + " could not be clicked due to:" + e.getMessage(), "fail");
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        } catch (WebDriverException e) {
            ExtentManager.logStep("The Element " + ele + " could not be clicked due to: " + e.getMessage(), "fail");
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("The Element " + ele + " could not be clicked due to: " + e.getMessage(), "fail");
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        }
    }

    public void clickUsingJs(WebElement ele) {
        try {
            ele.isDisplayed(); // @FindBy return the proxy even if it does not exist !!
        } catch (NoSuchElementException e) {
            ExtentManager.logStep("The Element " + ele + " is not found", "fail");
            Assert.fail("The Element " + ele + " is not found");
        }

        String text = "";
        try {
            try {
                getDriver().executeScript("arguments[0].click()", ele);
            } catch (Exception e) {
                boolean bFound = false;
                int totalTime = 0;
                while (!bFound && totalTime < 10000) {
                    try {
                        Thread.sleep(500);
                        getDriver().executeScript("arguments[0].click()", ele);
                        bFound = true;

                    } catch (Exception e1) {
                        bFound = false;
                    }
                    totalTime = totalTime + 500;
                }
                if (!bFound)
                    getDriver().executeScript("arguments[0].click()", ele);
            }
        } catch (StaleElementReferenceException e) {
            ExtentManager.logStep("The Element " + text + " could not be clicked due to:" + e.getMessage(), "fail");
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        } catch (WebDriverException e) {
            ExtentManager.logStep("The Element " + ele + " could not be clicked due to: " + e.getMessage(), "fail");
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("The Element " + ele + " could not be clicked due to: " + e.getMessage(), "fail");
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        }
    }

    public void click(Locators locatorType, String value) {
        String text = "";
        WebElement ele = locateElement(getBy(locatorType, value));
        try {
            try {
                Thread.sleep(500);
                getWait().until(ExpectedConditions.elementToBeClickable(ele));
                text = ele.getText();
                if (ele.isEnabled()) {
                    ele.click();
                } else {
                    getDriver().executeScript("arguments[0].click()", ele);
                }
            } catch (Exception e) {
                boolean bFound = false;
                int totalTime = 0;
                while (!bFound && totalTime < 10000) {
                    try {
                        Thread.sleep(500);
                        ele = locateElement(getBy(locatorType, value));
                        ele.click();
                        bFound = true;

                    } catch (Exception e1) {
                        bFound = false;
                    }
                    totalTime = totalTime + 500;
                }
                if (!bFound)
                    ele.click();
            }
        } catch (StaleElementReferenceException e) {
            ExtentManager.logStep("The Element " + text + " could not be clicked " + e.getMessage(), "fail");
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        } catch (WebDriverException e) {
            ExtentManager.logStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        }
    }

    public void clickWithNoSnap(WebElement ele) {
        String text = ele.getText();
        try {
            getWait().until(ExpectedConditions.elementToBeClickable(ele));
            ele.click();
        } catch (StaleElementReferenceException e) {
            ExtentManager.logStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        } catch (WebDriverException e) {
            ExtentManager.logStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
            Assert.fail("The Element " + text + " could not be clicked due to:" + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("The Element " + ele + " could not be clicked \n" + e.getMessage(), "fail", false);
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
            ExtentManager.logStep("The Element " + ele + " could not be appended \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " could not be appended \n" + e.getMessage());
        }
    }

    @Override
    public void clear(WebElement ele) {
        try {
            ele.clear();
        } catch (ElementNotInteractableException e) {
            ExtentManager.logStep("The field is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The field is not Interactable \n" + e.getMessage());
        }
    }

    /**
     * Overloaded method used to clear the existing value and type the data with
     * keys for tab or enter kind of
     *
     * @param ele  - WebElement from the DOM
     * @param data - Use to type and pass Keys as many needed
     */
    public void clearAndType(WebElement ele, CharSequence... data) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(ele));
            ele.clear();
            ele.sendKeys(data);
        } catch (ElementNotInteractableException e) {
            ExtentManager.logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) { // retry - 1
            pause(500);
            try {
                ele.sendKeys(data);
            } catch (Exception e1) {
                ExtentManager.logStep("The Element " + ele + " did not allow to clear / type \n" + e.getMessage(), "fail");
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
            ExtentManager.logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) { // retry - 1
            pause(500);
            try {
                ele.sendKeys(data);
            } catch (Exception e1) {
                ExtentManager.logStep("The Element " + ele + " did not allow to clear / type \n" + e.getMessage(), "fail");
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
            ExtentManager.logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) {
            ExtentManager.logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        }

    }

    public void type(WebElement ele, String data) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(ele));
            ele.clear();
            ele.sendKeys("", "", data);
        } catch (ElementNotInteractableException e) {
            ExtentManager.logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) {
            ExtentManager.logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        }

    }

    public void typeAndEnter(WebElement ele, String data) {
        try {
            getWait().until(ExpectedConditions.visibilityOf(ele));
            ele.clear();
            ele.sendKeys("", "", data, Keys.ENTER);
        } catch (ElementNotInteractableException e) {
            ExtentManager.logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) {
            ExtentManager.logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());

        }

    }

    @Override
    public String getElementText(WebElement ele) {
        try {
            String text = ele.getText();
            ExtentManager.logStep("Text has been retrieved " + text, "info");
            return text;
        } catch (WebDriverException e) {
            ExtentManager.logStep("Sorry! text is not available \n" + e.getMessage(), "fail");
            Assert.fail("Sorry! text is not available \n" + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("Sorry! text is not available \n" + e.getMessage(), "fail");
            Assert.fail("Sorry! text is not available \n" + e.getMessage());
        }
        return null;
    }

    @Override
    public String getBackgroundColor(WebElement ele) {
        String cssValue = null;
        try {
            cssValue = ele.getCssValue("color");
            ExtentManager.logStep("The background color is " + cssValue, "info");
        } catch (WebDriverException e) {
            ExtentManager.logStep("Not able to get the background color \n" + e.getMessage(), "fail");
            Assert.fail("Not able to get the background color \n" + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("Not able to get the background color \n" + e.getMessage(), "fail");
            Assert.fail("Not able to get the background color \n" + e.getMessage());

        }
        return cssValue;
    }

    @Override
    public String getTypedText(WebElement ele) {
        String attributeValue = null;
        try {
            attributeValue = ele.getAttribute("value");
            ExtentManager.logStep("The attribute value is " + attributeValue, "info");
        } catch (WebDriverException e) {
            ExtentManager.logStep("Not able to find attribute value \n" + e.getMessage(), "fail");
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
            ExtentManager.logStep("Not able to select the drop down with text \n" + value, "fail");
            Assert.fail("Not able to select the drop down with text \n" + value);
        }
    }

    @Override
    public void selectDropDownUsingIndex(WebElement ele, int index) {
        try {
            Select sel = new Select(ele);
            sel.selectByIndex(index);
        } catch (WebDriverException e) {
            ExtentManager.logStep("Not able to select the drop down with index " + index + " \n" + e.getMessage(), "fail");
            Assert.fail("Not able to select the drop down with index " + index + " \n" + e.getMessage());
        }
    }

    @Override
    public void selectDropDownUsingValue(WebElement ele, String value) {
        try {
            Select sel = new Select(ele);
            sel.selectByValue(value);
        } catch (WebDriverException e) {
            ExtentManager.logStep("Not able to select the drop down with value " + value + " \n" + e.getMessage(), "fail");
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
                ExtentManager.logStep("The expected text " + text + "doesn't equals to the  " + expectedText, "warning");
                Assert.fail("The expected text " + text + "doesn't equals to the  " + expectedText);

            }
        } catch (WebDriverException e) {
            ExtentManager.logStep("Unknown exception occured while verifying the Text \n" + e.getMessage(), "fail");
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
                ExtentManager.logStep("The expected text doesn't contain the actual " + expectedText, "warning");
                Assert.fail("The expected text doesn't contain the actual " + expectedText);
            }
        } catch (WebDriverException e) {
            ExtentManager.logStep("Unknown exception occured while verifying the Text \n" + e.getMessage(), "fail");
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
                ExtentManager.logStep("The expected attribute :" + attribute + " value does not contains the actual " + value,  "warning");
                Assert.fail("The expected attribute :" + attribute + " value does not contains the actual " + value);
            }
        } catch (WebDriverException e) {
            ExtentManager.logStep("Unknown exception occured while verifying the Attribute Text \n" + e.getMessage(), "fail");
            Assert.fail("Unknown exception occured while verifying the Attribute Text \n" + e.getMessage());
        }
        return false;
    }

    @Override
    public void verifyPartialAttribute(WebElement ele, String attribute, String value) {
        try {
            if (ele.getAttribute(attribute).contains(value)) {
                ExtentManager.logStep("The expected attribute :" + attribute + " value contains the actual " + value, "pass");
            } else {
                ExtentManager.logStep("The expected attribute :" + attribute + " value does not contains the actual " + value,
                        "warning");
                Assert.fail("The expected attribute :" + attribute + " value does not contains the actual " + value);
            }
        } catch (WebDriverException e) {
            ExtentManager.logStep("Unknown exception occured while verifying the Attribute Text \n" + e.getMessage(), "fail");
            Assert.fail("Unknown exception occured while verifying the Attribute Text \n" + e.getMessage());
        }

    }

    @Override
    public boolean verifyDisplayed(WebElement ele) {
        try {
            if (ele.isDisplayed()) {
                return true;
            } else {
                ExtentManager.logStep("The element " + ele + " is not visible", "warnings");
                Assert.fail("The element " + ele + " is not visible");
            }
        } catch (WebDriverException e) {
            ExtentManager.logStep("WebDriverException : \n" + e.getMessage(), "fail");
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }
        return false;

    }

    @Override
    public boolean verifyDisappeared(WebElement ele) {
        try {
            Boolean until = getWait().until(ExpectedConditions.invisibilityOf(ele));
            ExtentManager.logStep("Waited for an element to disappear", "info");
            return until;
        } catch (org.openqa.selenium.TimeoutException e) {
            ExtentManager.logStep("Element not disappeared \n" + e.getMessage(), "fail");
            Assert.fail("Element not disappeared \n" + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("Element not disappeared \n" + e.getMessage(), "fail");
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
                ExtentManager.logStep("The element " + ele + " is not Enabled", "warning");
                Assert.fail("The element " + ele + " is not enabled");
            }
        } catch (WebDriverException e) {
            ExtentManager.logStep("WebDriverException : \n" + e.getMessage(), "fail");
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
                ExtentManager.logStep("The element " + ele + " is not selected", "warning");
                Assert.fail("The element " + ele + " is not selected");
            }
        } catch (WebDriverException e) {
            ExtentManager.logStep("WebDriverException : \n" + e.getMessage(), "fail");
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }
        return false;

    }

    @Override
    public WebElement locateElement(By locator) {
        String locatorDetails = locator.toString();  // Example: "By.xpath: //span[text()='App Launcher']"
        String locatorType = locatorDetails.split(": ")[0].replace("By.", "");  // Extracts 'xpath', 'id', etc.
        String value = locatorDetails.split(": ", 2)[1];  // Extracts actual value
        try {
            // Wait until element is visible
            getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));

            // Locate the element and return it
            return getDriver().findElement(locator);
        } catch (NoSuchElementException e) {
            ExtentManager.logStep("The Element with locator:" + locatorType + " Not Found with value: " + value + "\n"
                    + e.getMessage(), "fail");
            Assert.fail("The Element with locator:" + locatorType + " Not Found with value: " + value + "\n"
                    + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("The Element with locator:" + locatorType + " Not Found with value: " + value + "\n"
                    + e.getMessage(), "fail");
            Assert.fail("The Element with locator:" + locatorType + " Not Found with value: " + value + "\n"
                    + e.getMessage());
        }
        return null;
    }

    // Helper Method: Convert Locators Enum to By Type
    public By getBy(Locators locatorType, String value) {
        switch (locatorType) {
            case CLASS_NAME: return By.className(value);
            case CSS: return By.cssSelector(value);
            case ID: return By.id(value);
            case LINK_TEXT: return By.linkText(value);
            case NAME: return By.name(value);
            case PARTIAL_LINKTEXT: return By.partialLinkText(value);
            case TAGNAME: return By.tagName(value);
            case XPATH: return By.xpath(value);
            default: throw new IllegalArgumentException("Invalid Locator Type: " + locatorType);
        }
    }

    @Override
    public WebElement locateElement(String value) {
        try {
            WebElement findElementById = getDriver().findElement(By.id(value));
            return findElementById;
        } catch (NoSuchElementException e) {
            ExtentManager.logStep("The Element with locator id Not Found with value: " + value + "\n" + e.getMessage(), "fail");
            Assert.fail("The Element with locator id Not Found with value: " + value + "\n" + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("The Element with locator id Not Found with value: " + value + "\n" + e.getMessage(), "fail");
            Assert.fail("The Element with locator id Not Found with value: " + value + "\n" + e.getMessage());
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
                    System.err.println("Locator is not Valid");
                    break;
            }
        } catch (NoSuchElementException e) {
            ExtentManager.logStep("The Element with locator:" + type + " Not Found with value: " + value + "\n" + e.getMessage(),
                    "fail");
            Assert.fail("The Element with locator:" + type + " Not Found with value: " + value + "\n" + e.getMessage());
        }
        return null;
    }

    @Override
    public void switchToAlert() {
        try {
            getDriver().switchTo().alert();
            ExtentManager.logStep("Focus has been switched to Alert", "info", false);
        } catch (NoAlertPresentException e) {
            ExtentManager.logStep("There is no alert present.", "fail", false);
            Assert.fail("There is no alert present");
        } catch (WebDriverException e) {
            ExtentManager.logStep("WebDriverException : " + e.getMessage(), "fail", false);
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
            ExtentManager.logStep("The alert " + text + " is accepted.", "pass", false);
        } catch (NoAlertPresentException e) {
            ExtentManager.logStep("There is no alert present.", "fail", false);
            Assert.fail("There is no alert present");
        } catch (WebDriverException e) {
            ExtentManager.logStep("WebDriverException : " + e.getMessage(), "fail", false);
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
            ExtentManager.logStep("The alert " + text + " is accepted.", "pass", false);
        } catch (NoAlertPresentException e) {
            ExtentManager.logStep("There is no alert present.", "pass", false);
            Assert.fail("There is no alert present");
        } catch (WebDriverException e) {
            ExtentManager.logStep("WebDriverException : " + e.getMessage(), "fail", false);
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }

    }

    @Override
    public String getAlertText() {
        String text = "";
        try {
            Alert alert = getDriver().switchTo().alert();
            text = alert.getText();
            ExtentManager.logStep("The alert text is " + text, "pass", false);
        } catch (NoAlertPresentException e) {
            ExtentManager.logStep("There is no alert present.", "fail", false);
            Assert.fail("There is no alert present");
        } catch (WebDriverException e) {
            ExtentManager.logStep("WebDriverException : \n" + e.getMessage(), "fail", false);
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }
        return text;
    }

    @Override
    public void typeAlert(String data) {
        try {
            getDriver().switchTo().alert().sendKeys(data);
        } catch (NoAlertPresentException e) {
            ExtentManager.logStep("There is no alert present.", "fail", false);
            Assert.fail("There is no alert present");
        } catch (WebDriverException e) {
            ExtentManager.logStep("WebDriverException : \n" + e.getMessage(), "fail", false);
            Assert.fail("WebDriverException : \n" + e.getMessage());
        }
    }

    @Override
    public void switchToWindow(int index) {
        try {
            Set<String> allWindows = getDriver().getWindowHandles();
            List<String> allhandles = new ArrayList<String>(allWindows);
            getDriver().switchTo().window(allhandles.get(index));
            ExtentManager.logStep("The Window With index: " + index + " switched successfully", "info", false);
            ExtentManager.logStep(getDriver().getTitle(), "info");
        } catch (NoSuchWindowException e) {
            ExtentManager.logStep("The Window With index: " + index + " not found\n" + e.getMessage(), "fail", false);
            Assert.fail("The Window With index: " + index + " not found\n" + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("The Window With index: " + index + " not found\n" + e.getMessage(), "fail", false);
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
            ExtentManager.logStep("The Window With Title: " + title + "is switched ", "info");
            return true;
        } catch (NoSuchWindowException e) {
            ExtentManager.logStep("The Window With Title: " + title + " not found", "fail", false);
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
            ExtentManager.logStep("No such frame " + e.getMessage(), "warning", false);
            Assert.fail("No such frame " + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        }

    }

    @Override
    public void switchToFrame(WebElement ele) {
        try {
            getDriver().switchTo().frame(ele);
        } catch (NoSuchFrameException e) {
            ExtentManager.logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        }

    }

    public void switchToFrameUsingXPath(String xpath) {
        try {
            getDriver().switchTo().frame(locateElement(getBy(Locators.XPATH, xpath)));
        } catch (NoSuchFrameException e) {
            ExtentManager.logStep("No such frame " + e.getMessage(), "warning", false);
            Assert.fail("No such frame " + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        }

    }

    @Override
    public void switchToFrame(String idOrName) {
        try {
            getDriver().switchTo().frame(idOrName);
        } catch (NoSuchFrameException e) {
            ExtentManager.logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        } catch (Exception e) {
            ExtentManager.logStep("No such frame " + e.getMessage(), "fail", false);
            Assert.fail("No such frame " + e.getMessage());
        }
    }

    @Override
    public void defaultContent() {
        try {
            getDriver().switchTo().defaultContent();
        } catch (Exception e) {
            ExtentManager.logStep("No such window " + e.getMessage(), "fail", false);
            Assert.fail("No such window " + e.getMessage());
        }
    }

    @Override
    public boolean verifyUrl(String url) {
        if (getDriver().getCurrentUrl().equals(url)) {
            ExtentManager.logStep("The url: " + url + " matched successfully", "info");
            return true;
        } else {
            ExtentManager.logStep("The url: " + url + " not matched", "fail");
            Assert.fail("The url: " + url + " not matched");
        }
        return false;
    }

    @Override
    public boolean verifyTitle(String title) {
        if (getDriver().getTitle().equals(title)) {
            ExtentManager.logStep("Page title: " + title + " matched successfully", "info");
            return true;
        } else {
            ExtentManager.logStep("Page url: " + title + " not matched", "fail");
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
            ExtentManager.logStep("The browser has been closed." + e.getMessage(), "fail");
            Assert.fail("The browser has been closed." + e.getMessage());
        } catch (IOException e) {
            ExtentManager.logStep("The snapshot could not be taken " + e.getMessage(), "warning");
            Assert.fail("The snapshot could not be taken " + e.getMessage());
        }
        return number;
    }

    @Override
    public void close() {
        try {
            getDriver().close();
            ExtentManager.logStep("Browser is closed", "info", false);
        } catch (Exception e) {
            ExtentManager.logStep("Browser cannot be closed " + e.getMessage(), "fail", false);
            Assert.fail("Browser cannot be closed " + e.getMessage());
        }
    }

    @Override
    public void quit() {
        try {
            getDriver().quit();
            ExtentManager.logStep("Browser is closed", "info", false);
        } catch (Exception e) {
            ExtentManager.logStep("Browser cannot be closed " + e.getMessage(), "fail", false);
            Assert.fail("Browser cannot be closed " + e.getMessage());
        }
    }

    public void waitForDisapperance(WebElement element) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.invisibilityOf(element));
        } catch (Exception e) {
            ExtentManager.logStep("Element did not appear after 10 seconds", "fail", false);
            Assert.fail("Element did not appear after 10 seconds");
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
            ExtentManager.logStep("The Data :" + data + " entered Successfully", "pass");
        } catch (ElementNotInteractableException e) {
            ExtentManager.logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
            Assert.fail("The Element " + ele + " is not Interactable \n" + e.getMessage());
        } catch (WebDriverException e) {
            ExtentManager.logStep("The Element " + ele + " is not Interactable \n" + e.getMessage(), "fail");
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
            ExtentManager.logStep("The file is selected Successfully", "pass");
        } catch (Exception e) {
            ExtentManager.logStep("The file is not selected", "fail");
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
            ExtentManager.logStep("The file is selected Successfully", "pass");
        } catch (Exception e) {
            ExtentManager.logStep("The file is not selected", "fail");
            Assert.fail("The file is not selected");
        }

    }

    @Override
    public void executeTheScript(String js, WebElement ele) {
        getDriver().executeScript(js, ele);
    }

}