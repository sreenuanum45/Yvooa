package utilities.ApiUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseUtils {
    private static Connection connection;

    public static Connection connectToDatabase(String url, String user, String password) throws Exception {
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }

    public static ResultSet executeQuery(String query) throws Exception {
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public static void closeConnection() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }


}
