package reporting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import io.qameta.allure.Attachment;

/**
 * Utility class for attaching different types of content to Allure reports.
 */
public class AllureAttachments {

    // Text-based attachments

    /**
     * Attaches plain text to the Allure report.
     * @param name    Display name for the attachment
     * @param content Text content to attach
     * @return The attached content
     */
    @Attachment(value = "{0}", type = "text/plain")
    public static String attachText(String name, String content) {
        return content;
    }

    /**
     * Attaches HTML content to the Allure report.
     * @param name        Display name for the attachment
     * @param htmlContent HTML content to attach
     * @return The attached content
     */
    @Attachment(value = "{0}", type = "text/html")
    public static String attachHtml(String name, String htmlContent) {
        return htmlContent;
    }

    /**
     * Attaches JSON content to the Allure report.
     * @param name       Display name for the attachment
     * @param jsonContent JSON content to attach
     * @return The attached content
     */
    @Attachment(value = "{0}", type = "application/json")
    public static String attachJson(String name, String jsonContent) {
        return jsonContent;
    }

    /**
     * Attaches XML content to the Allure report.
     * @param name       Display name for the attachment
     * @param xmlContent XML content to attach
     * @return The attached content
     */
    @Attachment(value = "{0}", type = "application/xml")
    public static String attachXml(String name, String xmlContent) {
        return xmlContent;
    }

    // Binary attachments

    /**
     * Attaches a screenshot to the Allure report.
     * @param screenshot Byte array of the screenshot image (PNG format)
     * @return The attached image bytes
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] attachScreenshot(byte[] screenshot) {
        return screenshot;
    }

    /**
     * Attaches a generic file to the Allure report.
     * @param name    Display name for the attachment
     * @param content File content as byte array
     * @return The attached file bytes
     */
    @Attachment(value = "{0}", type = "application/octet-stream")
    public static byte[] attachFile(String name, byte[] content) {
        return content;
    }

    // File system attachments

    /**
     * Attaches a file from the filesystem to the Allure report.
     * @param name     Display name for the attachment
     * @param filePath Path to the file to attach
     */
    public static void attachFileFromPath(String name, String filePath) {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            attachFile(name, fileContent);
        } catch (IOException e) {
            System.err.println("Failed to attach file from path: " + filePath);
            e.printStackTrace();
        }
    }

    /**
     * Attaches a CSV file to the Allure report.
     * @param name       Display name for the attachment
     * @param csvContent CSV content as string
     * @return The attached content
     */
    @Attachment(value = "{0}", type = "text/csv")
    public static String attachCsv(String name, String csvContent) {
        return csvContent;
    }
}