import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String DATABASE_URL = "jdbc:sqlite:inventory.db";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static void initializeDatabase() {
        String createItemsTable = "CREATE TABLE IF NOT EXISTS items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "vendorId INTEGER," +
                "FOREIGN KEY (vendorId) REFERENCES vendors (id));";

        String createVendorsTable = "CREATE TABLE IF NOT EXISTS vendors (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "contactInfo TEXT);";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createItemsTable);
            stmt.execute(createVendorsTable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}