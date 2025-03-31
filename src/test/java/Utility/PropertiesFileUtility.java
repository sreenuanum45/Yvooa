package Utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFileUtility {

    private static Properties properties;

    static {
        String env = System.getProperty("env", "dev");
        String configFilePath = "D:/batch264/Yvooa/src/test/resources/configurations/config-" + env + ".properties";
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties = new Properties();
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValueInPropertiesFile(String propertyName) throws Exception {
        // Default properties file path
        String pfPath = "D:/batch264/Yvooa/src/test/resources/configurations/config-test.properties";
        // Access the properties file
        try (FileInputStream fi = new FileInputStream(pfPath)) {
            Properties p = new Properties();
            p.load(fi);
            return p.getProperty(propertyName);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
