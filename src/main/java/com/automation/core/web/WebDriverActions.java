package com.automation.core.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * WebDriverActions interface provides an abstraction for Selenium WebDriver utility methods.
 * It defines common browser interaction methods to be implemented by WebDriverUtils.
 * This ensures consistency, reusability, and loose coupling in the automation framework.
 */
public interface WebDriverActions {

    /**
     * Clicks on an element located by the given locator.
     *
     * @param locator The By locator of the element to be clicked.
     */
    void clickElement(By locator);

    /**
     * Enters text into an input field located by the given locator.
     *
     * @param locator The By locator of the input field.
     * @param text    The text to enter into the input field.
     */
    void enterText(By locator, String text);

    /**
     * Retrieves the visible text from an element located by the given locator.
     *
     * @param locator The By locator of the element.
     * @return The extracted text from the element.
     */
    String getText(By locator);

    /**
     * Checks if an element is displayed on the page.
     *
     * @param locator The By locator of the element.
     * @return True if the element is displayed, otherwise false.
     */
    boolean isElementDisplayed(By locator);

    /**
     * Selects a dropdown option by visible text.
     *
     * @param locator The By locator of the dropdown element.
     * @param text    The visible text of the option to be selected.
     */
    void selectDropdownByVisibleText(By locator, String text);

    /**
     * Selects a dropdown option by index.
     *
     * @param locator The By locator of the dropdown element.
     * @param index   The index of the option to be selected.
     */
    void selectDropdownByIndex(By locator, int index);

    /**
     * Retrieves a list of web elements located by the given locator.
     *
     * @param locator The By locator of the elements.
     * @return A list of WebElement objects matching the locator.
     */
    List<WebElement> getElements(By locator);

    /**
     * Switches to an iframe located by the given locator.
     *
     * @param locator The By locator of the iframe.
     */
    void switchToFrame(By locator);

    /**
     * Performs a mouse hover action over an element located by the given locator.
     *
     * @param locator The By locator of the element to hover over.
     */
    void hoverOverElement(By locator);

    /**
     * Scrolls the page until the specified element is in view.
     *
     * @param locator The By locator of the element to scroll to.
     */
    void scrollToElement(By locator);
}
