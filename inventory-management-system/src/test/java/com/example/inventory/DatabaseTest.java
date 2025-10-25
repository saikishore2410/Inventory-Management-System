import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseTest {

    private Database database;

    @BeforeEach
    public void setUp() {
        database = new Database();
        database.connect();
    }

    @AfterEach
    public void tearDown() {
        database.disconnect();
    }

    @Test
    public void testDatabaseConnection() {
        Connection connection = database.getConnection();
        assertNotNull(connection, "Database connection should not be null");
    }

    @Test
    public void testDatabaseInitialization() {
        try {
            database.initialize();
            // Additional assertions can be added here to verify the database state
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}