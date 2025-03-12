package com.automation.core.web;

import com.automation.core.design.Locators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;

public interface WebDriverActions {

    /**
     * Gets the attribute value of a web element.
     * @param ele The WebElement.
     * @param attributeValue The attribute name.
     * @return The attribute value.
     */
    String getAttribute(WebElement ele, String attributeValue);

    /**
     * Moves to the specified web element.
     * @param ele The WebElement.
     */
    void moveToElement(WebElement ele);

    /**
     * Performs drag and drop between elements.
     * @param eleSource The source WebElement.
     * @param eleTarget The target WebElement.
     */
    void dragAndDrop(WebElement eleSource, WebElement eleTarget);

    /**
     * Performs a context click (right-click) on the element.
     * @param ele The WebElement.
     */
    void contextClick(WebElement ele);

    /**
     * Hovers over an element and clicks it.
     * @param ele The WebElement.
     */
    void hoverAndClick(WebElement ele);

    /**
     * Performs a double-tap on the element.
     * @param ele The WebElement.
     */
    void doubleTap(WebElement ele);

    /**
     * Performs a double-click on the element.
     * @param ele The WebElement.
     */
    void doubleClick(WebElement ele);

    /**
     * Waits for an element to appear.
     * @param element The WebElement.
     */
    void waitForApperance(WebElement element);

    /**
     * Clicks on a web element.
     * @param ele The WebElement.
     */
    void click(WebElement ele);

    /**
     * Clicks on an element using JavaScript Executor.
     * @param ele The WebElement.
     */
    void clickUsingJs(WebElement ele);

    /**
     * Clicks on an element located by a specific locator type.
     * @param locatorType The locator type.
     * @param value The locator value.
     */
    void click(Locators locatorType, String value);

    /**
     * Clicks on an element without taking a snapshot.
     * @param ele The WebElement.
     */
    void clickWithNoSnap(WebElement ele);

    /**
     * Appends text to an input field.
     * @param ele The WebElement.
     * @param data The text to be appended.
     */
    void append(WebElement ele, String data);

    /**
     * Clears the content of an input field.
     * @param ele The WebElement.
     */
    void clear(WebElement ele);

    /**
     * Clears an input field and types new data.
     * @param ele The WebElement.
     * @param data The text to be entered.
     */
    void clearAndType(WebElement ele, CharSequence... data);

    /**
     * Clears an input field and types new data.
     * @param ele The WebElement.
     * @param data The text to be entered.
     */
    void clearAndType(WebElement ele, String data);

    /**
     * Types text and presses the Tab key.
     * @param ele The WebElement.
     * @param data The text to be typed.
     */
    void typeAndTab(WebElement ele, String data);

    /**
     * Types text into an input field.
     * @param ele The WebElement.
     * @param data The text to be typed.
     */
    void type(WebElement ele, String data);

    /**
     * Types text and presses the Enter key.
     * @param ele The WebElement.
     * @param data The text to be typed.
     */
    void typeAndEnter(WebElement ele, String data);

    /**
     * Retrieves text from an element.
     * @param ele The WebElement.
     * @return The retrieved text.
     */
    String getElementText(WebElement ele);

    /**
     * Retrieves the background color of an element.
     * @param ele The WebElement.
     * @return The background color.
     */
    String getBackgroundColor(WebElement ele);

    /**
     * Retrieves the value typed into an input field.
     * @param ele The WebElement.
     * @return The typed value.
     */
    String getTypedText(WebElement ele);

    /**
     * Selects a dropdown option using visible text.
     * @param ele The WebElement.
     * @param value The text of the option.
     */
    void selectDropDownUsingText(WebElement ele, String value);

    /**
     * Selects a dropdown option using its index.
     * @param ele The WebElement.
     * @param index The index of the option.
     */
    void selectDropDownUsingIndex(WebElement ele, int index);

    /**
     * Selects a dropdown option using its value attribute.
     * @param ele The WebElement.
     * @param value The value of the option.
     */
    void selectDropDownUsingValue(WebElement ele, String value);

    /**
     * Verifies if an element displays the exact expected text.
     * @param ele The WebElement.
     * @param expectedText The text to be verified.
     * @return true if the text matches, false otherwise.
     */
    boolean verifyExactText(WebElement ele, String expectedText);

    /**
     * Verifies if an element contains the expected text.
     * @param ele The WebElement.
     * @param expectedText The partial text to be verified.
     * @return true if the text is present, false otherwise.
     */
    boolean verifyPartialText(WebElement ele, String expectedText);

    /**
     * Verifies if an element has an exact attribute value.
     * @param ele The WebElement.
     * @param attribute The attribute name.
     * @param value The expected attribute value.
     * @return true if the attribute matches, false otherwise.
     */

    boolean verifyExactAttribute(WebElement ele, String attribute, String value);

    /**
     * Verifies if an element contains a partial attribute value.
     * @param ele The WebElement.
     * @param attribute The attribute name.
     * @param value The expected partial attribute value.
     */
    void verifyPartialAttribute(WebElement ele, String attribute, String value);

    /**
     * Checks if an element is displayed on the page.
     * @param ele The WebElement.
     * @return true if the element is displayed, false otherwise.
     */
    boolean verifyDisplayed(WebElement ele);

    /**
     * Checks if an element has disappeared from the page.
     * @param ele The WebElement.
     * @return true if the element has disappeared, false otherwise.
     */
    boolean verifyDisappeared(WebElement ele);

    /**
     * Checks if an element is enabled.
     * @param ele The WebElement.
     * @return true if the element is enabled, false otherwise.
     */
    boolean verifyEnabled(WebElement ele);

    /**
     * Checks if an element is selected.
     * @param ele The WebElement.
     * @return true if the element is selected, false otherwise.
     */
    boolean verifySelected(WebElement ele);

    /**
     * Locates an element using the specified Selenium {@code By} locator.
     * Waits until the element is visible before returning it.
     *
     * @param locator The Selenium {@code By} locator.
     * @return The located {@code WebElement}.
     */
    WebElement locateElement(By locator);

    /**
     * Converts a {@code Locators} type and value into a Selenium {@code By} locator.
     *
     * @param locatorType The type of locator (e.g., ID, XPATH, CSS).
     * @param value The locator value.
     * @return The generated {@code By} locator.
     */
    By getBy(Locators locatorType, String value);


    /**
     * Locates an element using its ID.
     * @param value The ID of the element.
     * @return The located WebElement.
     */
    WebElement locateElement(String value);

    /**
     * Locates multiple elements using the specified locator type.
     * @param type The type of locator (e.g., CLASS_NAME, XPATH).
     * @param value The locator value.
     * @return A list of located WebElements.
     */
    List<WebElement> locateElements(Locators type, String value);

         /**
         * Switches to an alert box.
         */
        void switchToAlert();

        /**
         * Accepts an alert box.
         */
        void acceptAlert();

        /**
         * Dismisses an alert box.
         */
        void dismissAlert();

        /**
         * Retrieves the text from an alert box.
         * @return The alert text.
         */
        String getAlertText();

        /**
         * Sends text to an alert box.
         * @param data The text to be sent.
         */
        void typeAlert(String data);

        /**
         * Switches to a specific window using its index.
         * @param index The index of the window.
         */
        void switchToWindow(int index);

        /**
         * Switches to a specific window using its title.
         * @param title The title of the window.
         * @return true if the switch was successful, false otherwise.
         */
        boolean switchToWindow(String title);

        /**
         * Switches to a frame using its index.
         * @param index The index of the frame.
         */
        void switchToFrame(int index);

        /**
         * Switches to a frame using a WebElement.
         * @param ele The WebElement representing the frame.
         */
        void switchToFrame(WebElement ele);

        /**
         * Switches to a frame using its XPath.
         * @param xpath The XPath of the frame.
         */
        void switchToFrameUsingXPath(String xpath);

        /**
         * Switches to a frame using its ID or name.
         * @param idOrName The ID or name of the frame.
         */
        void switchToFrame(String idOrName);

        /**
         * Switches to the default content from an iframe.
         */
        void defaultContent();

        /**
         * Verifies if the current URL matches the expected URL.
         * @param url The expected URL.
         * @return true if the URLs match, false otherwise.
         */
        boolean verifyUrl(String url);

        /**
         * Verifies if the current page title matches the expected title.
         * @param title The expected title.
         * @return true if the titles match, false otherwise.
         */
        boolean verifyTitle(String title);

        /**
         * Captures a screenshot and returns its reference ID.
         * @return The reference ID of the screenshot.
         */
        long takeSnap();

        /**
         * Closes the current browser window.
         */
        void close();

        /**
         * Quits the entire browser session.
         */
        void quit();

        /**
         * Waits for an element to disappear from the DOM.
         * @param element The WebElement to wait for.
         */
        void waitForDisapperance(WebElement element);

        /**
         * Pauses the execution for a specified duration.
         * @param timeout The duration in milliseconds.
         */
        void pause(int timeout);

        /**
         * Selects a date in a date picker.
         * @param ele The WebElement representing the date picker.
         * @param data The date value to select.
         */
        void chooseDate(WebElement ele, String data);

        /**
         * Uploads a file using the file input field.
         * @param ele The WebElement representing the file input field.
         * @param data The path of the file to upload.
         */
        void fileUpload(WebElement ele, String data);

        /**
         * Uploads a file using JavaScript.
         * @param ele The WebElement representing the file input field.
         * @param data The path of the file to upload.
         */
        void fileUploadWithJs(WebElement ele, String data);

        /**
         * Executes a custom JavaScript command.
         * @param js The JavaScript code to execute.
         * @param ele The WebElement on which the script should be executed.
         */
        void executeTheScript(String js, WebElement ele);
}
