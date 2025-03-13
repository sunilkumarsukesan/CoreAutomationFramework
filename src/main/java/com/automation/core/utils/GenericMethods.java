package com.automation.core.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ThreadLocalRandom;

public class GenericMethods {

    public enum CaseType { UPPER, LOWER, MIXED }
    public enum DateUnit { DAYS, MONTHS, YEARS }

    public static String generateRandomNumber(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }
        StringBuilder number = new StringBuilder();
        number.append(ThreadLocalRandom.current().nextInt(1, 10)); // First digit (1-9)
        for (int i = 1; i < length; i++) {
            number.append(ThreadLocalRandom.current().nextInt(0, 10)); // Remaining digits (0-9)
        }
        return number.toString();
    }

    public static String generateRandomAlphabets(int length, CaseType caseType) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        StringBuilder alphabets = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomAscii;
            switch (caseType) {
                case UPPER:
                    randomAscii = ThreadLocalRandom.current().nextInt(65, 91); // A-Z
                    break;
                case LOWER:
                    randomAscii = ThreadLocalRandom.current().nextInt(97, 123); // a-z
                    break;
                case MIXED:
                default:
                    randomAscii = ThreadLocalRandom.current().nextBoolean()
                            ? ThreadLocalRandom.current().nextInt(65, 91)  // A-Z
                            : ThreadLocalRandom.current().nextInt(97, 123); // a-z
            }
            alphabets.append((char) randomAscii);
        }
        return alphabets.toString();
    }


        public static String generateRandomAlphanumeric(int length, CaseType caseType) {
            if (length <= 0) {
                throw new IllegalArgumentException("Length must be greater than 0");
            }

            StringBuilder alphanumeric = new StringBuilder();

            for (int i = 0; i < length; i++) {
                int randomAscii;
                boolean isLetter = ThreadLocalRandom.current().nextBoolean(); // Randomly decide Letter or Digit

                if (isLetter) {
                    switch (caseType) {
                        case UPPER:
                            randomAscii = ThreadLocalRandom.current().nextInt(65, 91); // A-Z
                            break;
                        case LOWER:
                            randomAscii = ThreadLocalRandom.current().nextInt(97, 123); // a-z
                            break;
                        case MIXED:
                        default:
                            randomAscii = ThreadLocalRandom.current().nextBoolean()
                                    ? ThreadLocalRandom.current().nextInt(65, 91)  // A-Z
                                    : ThreadLocalRandom.current().nextInt(97, 123); // a-z
                    }
                } else {
                    randomAscii = ThreadLocalRandom.current().nextInt(48, 58); // 0-9
                }

                alphanumeric.append((char) randomAscii);
            }
            return alphanumeric.toString();
        }

    public static String getTodayDate(String format) {
        // Default format if null or empty
        if (format == null || format.isEmpty()) {
            format = "dd-MM-yyyy";
        }

        // Get today's date
        LocalDate today = LocalDate.now();

        try {
            // Try formatting the date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return today.format(formatter);
        } catch (IllegalArgumentException e) {
            return "Error: Invalid date format! Please use a valid pattern (e.g., dd-MM-yyyy, yyyy/MM/dd).";
        }
    }

    public static String getModifiedDate(String inputDate, String format, int value, DateUnit unit) {
        // Default format if null or empty
        if (format == null || format.isEmpty()) {
            format = "dd-MM-yyyy";
        }

        try {
            // Parse the input date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate date = LocalDate.parse(inputDate, formatter);

            // Modify the date based on the unit
            switch (unit) {
                case DAYS:
                    date = date.plusDays(value);
                    break;
                case MONTHS:
                    date = date.plusMonths(value);
                    break;
                case YEARS:
                    date = date.plusYears(value);
                    break;
            }

            // Return the formatted result
            return date.format(formatter);
        } catch (DateTimeParseException e) {
            return "Error: Invalid date format! Ensure input date matches the provided format.";
        } catch (Exception e) {
            return "Error: Something went wrong while modifying the date.";
        }
    }
}
