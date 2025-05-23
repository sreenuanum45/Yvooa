package Utility;

import org.testng.Assert;

public class ValidationUtility {
    public static void verifyText(String actual, String expected) {
        Assert.assertEquals(actual, expected, "Text does not match!");
    }

    public static void verifyTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);
    }

    public static void verifyFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message);
    }

}
