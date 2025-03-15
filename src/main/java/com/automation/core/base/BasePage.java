package com.automation.core.base;

import com.automation.core.design.Locators;
import com.automation.core.drivers.DriverManager;
import com.automation.core.reporting.ExtentManager;
import com.automation.core.web.WebDriverActions;
import com.automation.core.web.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;


public class BasePage extends ExtentManager {
    protected RemoteWebDriver driver;
    private WebDriverActions actions; // Keep private to enforce method exposure via BasePage

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.actions = new WebDriverUtils(); // Assign WebDriverUtils instance
    }

    protected String getAttribute(WebElement ele, String attributeValue) {
        return actions.getAttribute(ele, attributeValue);
    }

    protected void moveToElement(WebElement ele) {
        actions.moveToElement(ele);
    }

    protected void dragAndDrop(WebElement eleSource, WebElement eleTarget) {
        actions.dragAndDrop(eleSource, eleTarget);
    }

    protected void contextClick(WebElement ele) {
        actions.contextClick(ele);
    }

    protected void hoverAndClick(WebElement ele) {
        actions.hoverAndClick(ele);
    }

    protected void doubleTap(WebElement ele) {
        actions.doubleTap(ele);
    }

    protected void doubleClick(WebElement ele) {
        actions.doubleClick(ele);
    }

    protected void waitForApperance(WebElement element) {
        actions.waitForApperance(element);
    }

    protected void click(WebElement ele) {
        actions.click(ele);
    }

    protected void clickWithNoSnap(WebElement ele) {
        actions.clickWithNoSnap(ele);
    }

    protected void append(WebElement ele, String data) {
        actions.append(ele, data);
    }

    protected void clear(WebElement ele) {
        actions.clear(ele);
    }

    protected void clearAndType(WebElement ele, CharSequence... data) {
        actions.clearAndType(ele, data);
    }

    protected void clearAndType(WebElement ele, String data) {
        actions.clearAndType(ele, data);
    }

    protected void typeAndTab(WebElement ele, String data) {
        actions.typeAndTab(ele, data);
    }

    protected void type(WebElement ele, String data) {
        actions.type(ele, data);
    }

    protected void typeAndEnter(WebElement ele, String data) {
        actions.typeAndEnter(ele, data);
    }

    protected String getElementText(WebElement ele) {
        return actions.getElementText(ele);
    }

    protected String getBackgroundColor(WebElement ele) {
        return actions.getBackgroundColor(ele);
    }

    protected String getTypedText(WebElement ele) {
        return actions.getTypedText(ele);
    }

    protected void selectDropDownUsingText(WebElement ele, String value) {
        actions.selectDropDownUsingText(ele, value);
    }

    protected void selectDropDownUsingIndex(WebElement ele, int index) {
        actions.selectDropDownUsingIndex(ele, index);
    }

    protected void selectDropDownUsingValue(WebElement ele, String value) {
        actions.selectDropDownUsingValue(ele, value);
    }

    protected boolean verifyExactText(WebElement ele, String expectedText) {
        return actions.verifyExactText(ele, expectedText);
    }

    protected boolean verifyPartialText(WebElement ele, String expectedText) {
        return actions.verifyPartialText(ele, expectedText);
    }

    protected boolean verifyExactAttribute(WebElement ele, String attribute, String value) {
        return actions.verifyExactAttribute(ele, attribute, value);
    }

    protected void verifyPartialAttribute(WebElement ele, String attribute, String value) {
        actions.verifyPartialAttribute(ele, attribute, value);
    }

    protected boolean verifyDisplayed(WebElement ele) {
        return actions.verifyDisplayed(ele);
    }

    protected boolean verifyDisappeared(WebElement ele) {
        return actions.verifyDisappeared(ele);
    }

    protected boolean verifyEnabled(WebElement ele) {
        return actions.verifyEnabled(ele);
    }

    protected boolean verifySelected(WebElement ele) {
        return actions.verifySelected(ele);
    }

    protected WebElement locateElement(Locators locatorType, String value) {
        return actions.locateElement(locatorType, value);
    }

    protected WebElement locateElement(String value) {
        return actions.locateElement(value);
    }

    protected List<WebElement> locateElements(Locators type, String value) {
        return actions.locateElements(type, value);
    }

    protected void switchToAlert() {
        actions.switchToAlert();
    }

    protected void acceptAlert() {
        actions.acceptAlert();
    }

    protected void dismissAlert() {
        actions.dismissAlert();
    }

    protected String getAlertText() {
        return actions.getAlertText();
    }

    protected void typeAlert(String data) {
        actions.typeAlert(data);
    }

    protected void switchToWindow(int index) {
        actions.switchToWindow(index);
    }

    protected boolean switchToWindow(String title) {
        return actions.switchToWindow(title);
    }

    protected void switchToFrame(int index) {
        actions.switchToFrame(index);
    }

    protected void switchToFrame(WebElement ele) {
        actions.switchToFrame(ele);
    }

    protected void switchToFrameUsingXPath(String xpath) {
        actions.switchToFrameUsingXPath(xpath);
    }

    protected void switchToFrame(String idOrName) {
        actions.switchToFrame(idOrName);
    }

    protected void defaultContent() {
        actions.defaultContent();
    }

    protected boolean verifyUrl(String url) {
        return actions.verifyUrl(url);
    }

    protected boolean verifyTitle(String title) {
        return actions.verifyTitle(title);
    }

    protected long takeSnap() {
        return actions.takeSnap();
    }

    protected void close() {
        actions.close();
    }

    protected void quit() {
        actions.quit();
    }

    protected void waitForDisapperance(WebElement element) {
        actions.waitForDisapperance(element);
    }

    protected void pause(int timeout) {
        actions.pause(timeout);
    }

    protected void chooseDate(WebElement ele, String data) {
        actions.chooseDate(ele, data);
    }

    protected void fileUpload(WebElement ele, String data) {
        actions.fileUpload(ele, data);
    }

    protected void fileUploadWithJs(WebElement ele, String data) {
        actions.fileUploadWithJs(ele, data);
    }

    protected void executeTheScript(String js, WebElement ele) {
        actions.executeTheScript(js, ele);
    }
}
