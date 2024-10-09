package locator;

import com.mongodb.client.MongoDatabase;
import lib.db.Database;
import lombok.experimental.UtilityClass;

import java.sql.Connection;

public class ConnectionManager {
    private static volatile Connection postgresConnection;
    private static volatile MongoDatabase mongoDatabase;

    private ConnectionManager() {}

    public static Connection getPostgresConnection() {
        if (postgresConnection == null) {
            synchronized (ConnectionManager.class) {
                if (postgresConnection == null) {
                    postgresConnection = Database.getPostgresConnection();
                }
            }
        }
        return postgresConnection;
    }

    public static MongoDatabase getMongoDatabase() {
        if (mongoDatabase == null) {
            synchronized (ConnectionManager.class) {
                if (mongoDatabase == null) {
                    mongoDatabase = Database.getMongoDB();
                }
            }
        }
        return mongoDatabase;
    }

    public static void closeConnections() {
        try {
            if (postgresConnection != null) {
                postgresConnection.close();
                postgresConnection = null;
            }
            if (mongoDatabase != null) {
                Database.closeMongoDBClient();
                mongoDatabase = null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Error closing connections", e);
        }
    }
}