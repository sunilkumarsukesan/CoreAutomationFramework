package com.automation.core.testData;

import com.automation.core.listeners.TestListener;
import com.automation.core.logger.LoggerManager;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class TestDataManager {
    private static final Map<String, Map<String, Map<String, String>>> testDataMap = new HashMap<>();
    private static String currentTestDataFile;  // Store current test data file
    private static String currentRowId;         // Store current RowID
    private static final Logger logger = LoggerManager.getLogger(TestDataManager.class);

    public static void loadTestData(String testDataSheet) throws IOException {
        if (testDataMap.containsKey(testDataSheet)) {
            return; // Avoid reloading if data is already loaded
        }

        File file = new File(testDataSheet);
        FileInputStream fis = new FileInputStream(file);
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheetAt(0);

        if (sheet == null) {
            throw new RuntimeException("Test data sheet '" + testDataSheet + "' not found in " + testDataSheet);
        }

        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new RuntimeException("Header row is missing in test data sheet: " + testDataSheet);
        }

        Map<String, Map<String, String>> sheetData = new HashMap<>();

        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                String rowId = row.getCell(0).getStringCellValue().trim(); // First column as Row ID
                Map<String, String> rowData = new HashMap<>();

                for (int colIndex = 1; colIndex < headerRow.getLastCellNum(); colIndex++) { // Skip first column (RowID)
                    Cell headerCell = headerRow.getCell(colIndex);
                    Cell dataCell = row.getCell(colIndex);

                    if (headerCell != null && dataCell != null) {
                        rowData.put(headerCell.getStringCellValue().trim(), getCellValueAsString(dataCell));
                    }
                }

                sheetData.put(rowId, rowData);
            }
        }

        testDataMap.put(testDataSheet, sheetData);
        workbook.close();
        fis.close();
        logger.info("Loaded test data for sheet: " + testDataSheet);
    }

    // Set the current test data context (called in Hooks or CommonSteps)
    public static void setCurrentTestDataContext(String testDataFile, String rowId) {
        currentTestDataFile = testDataFile;
        currentRowId = rowId;
    }

    // Fetch test data dynamically without passing file and rowId
    public static String getTestData(String columnHeader) {
        if (currentTestDataFile == null || currentRowId == null) {
            throw new IllegalStateException("Test data context is not set. Ensure 'setCurrentTestDataContext()' is called before fetching test data.");
        }

        return testDataMap.getOrDefault(currentTestDataFile, new HashMap<>())
                .getOrDefault(currentRowId, new HashMap<>())
                .get(columnHeader);
    }

    private static String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
