package Utility;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class MonthConverter {
    // Convert numeric month (String) to full month name
    public static String convertToMonthName(String monthNumber) {
        try {
            int monthInt = Integer.parseInt(monthNumber);
            if(monthInt < 1 || monthInt > 12) {
                throw new IllegalArgumentException("Invalid month number. Must be between 1-12");
            }
            return Month.of(monthInt)
                    .getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid month format. Must be numeric string");
        }
    }

    // Alternative version that returns 3-letter abbreviation
    public static String convertToMonthAbbreviation(String monthNumber) {
        try {
            int monthInt = Integer.parseInt(monthNumber);
            if(monthInt < 1 || monthInt > 12) {
                throw new IllegalArgumentException("Invalid month number. Must be between 1-12");
            }
            return Month.of(monthInt)
                    .getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid month format. Must be numeric string");
        }
    }
}
