package Utility;

import java.util.Base64;

public class Base64Utility {
    public static String encode(String plainText) {
        return Base64.getEncoder().encodeToString(plainText.getBytes());
    }

    public static String decode(String encodedText) {
        return new String(Base64.getDecoder().decode(encodedText));
    }
}
