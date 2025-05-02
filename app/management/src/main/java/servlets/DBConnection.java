package servlets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DBConnection {
    public static Connection getConnection() throws SQLException {

        // Load properties from db.properties file
        Properties props = new Properties();
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new RuntimeException("Could not find db.properties");
            }

            props.load(input);

            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.password");
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read DB config", e);
        }
    }

    public static void main(String[] args) {
        // Test connection to database
        try {
            Connection conn = getConnection();
            System.out.println("Connected to database successfully!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
        }
    }
}