package utilities;

import java.util.HashMap;
import java.util.Map;

public class TestContext {
    // Thread-safe storage for test data
    private static final ThreadLocal<Map<String, String>> testData = ThreadLocal.withInitial(HashMap::new);

    // Private constructor to prevent instantiation
    private TestContext() {}

    public static void setTestData(Map<String, String> data) {
        testData.set(data);
    }

    public static Map<String, String> getTestData() {
        return testData.get();
    }

    public static void clear() {
        testData.remove();
    }
}
