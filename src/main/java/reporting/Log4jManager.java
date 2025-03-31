package reporting;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator; // Use Log4j2 Configurator
import org.testng.Assert;
import org.testng.Reporter;

/**
 * Log4jManager provides centralized logging functionalities for the test framework.
 */
public class Log4jManager {
    private static final Logger logger = LogManager.getLogger(Log4jManager.class);

    public static void initLogCfg() {
        String rootPath = System.getProperty("user.dir");
        // Use Log4j2's Configurator to load the configuration file.
        // (Make sure your configuration file is in Log4j2 format; for example, log4j2.properties or log4j2.xml.)
        Configurator.initialize(null, rootPath + File.separator + "src/test/resources/config/log4j2.properties");

        // If you want to create/obtain a logger with a specific name (e.g. "TraceLog")
        Logger traceLogger = LogManager.getLogger("TraceLog");
    }

    /**
     * Logs an informational message with parameters.
     *
     * @param message The message pattern to log.
     * @param params  The parameters to insert into the message.
     */
    public static void info(String message, Object... params) {
        Log4jManager.initLogCfg();
        logger.info(message, params);
        Reporter.log(message + "<br/>");
    }

    /**
     * Logs a warning message with parameters.
     *
     * @param message The message pattern to log.
     * @param params  The parameters to insert into the message.
     */
    public static void warn(String message, Object... params) {
        Log4jManager.initLogCfg();
        logger.warn(message, params);
        Reporter.log("<font color='orange'>" + message + "</font><br/>");
    }

    /**
     * Logs an error message with parameters.
     *
     * @param message The message pattern to log.
     * @param params  The parameters to insert into the message.
     */
    public static void error(String message, Object... params) {
        Log4jManager.initLogCfg();
        logger.error(message, params);
    }

    /**
     * Logs a debug message with parameters.
     *
     * @param message The message pattern to log.
     * @param params  The parameters to insert into the message.
     */
    public static void debug(String message, Object... params) {
        Log4jManager.initLogCfg();
        logger.debug(message, params);
        Reporter.log("<font color='blue'>" + message + "</font><br/>");
    }

    /**
     * Logs an error message along with an exception's stack trace.
     *
     * @param message   The message to log.
     * @param throwable The exception to log.
     */
    public static void error(String message, Throwable throwable) {
        Log4jManager.initLogCfg();
        logger.error(message, throwable);
        Reporter.log("<font color='red'>" + message + "</font><br/>");
    }

    public static void trace(String message, Object... params) {
        Log4jManager.initLogCfg();
        logger.trace(message, params);
        Reporter.log(message + "<br/>");
    }

    public static void fatal(String message, Object... params) {
        logger.fatal(message, params);
    }

    public static void debug(String message) {
        logger.debug(message);
        Reporter.log("<font color='blue'>" + message + "</font><br/>");
    }

    public static void pass(String message) {
        logger.info(message);
        Reporter.log("<font color='green'>" + message + "</font><br/>");
    }

    public static void fail(String message) {
        logger.error(message);
        Reporter.log("<font color='red'>" + message + "</font><br/>");
        Assert.assertFalse(true, "The Test case is failed");
    }

    public static void skip(String message) {
        logger.warn(message);
        Reporter.log("<font color='yellow'>" + message + "</font><br/>");
    }
}
