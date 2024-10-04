package lib.db;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public final class DbConnection {
    private static volatile DbConnection instance;
    private final Connection connection;

    private DbConnection(DBMS db) throws SQLException {
        try {
            this.connection = DriverManager.getConnection(db.getUrl(), db.getUser(), db.getPassword());
        } catch (SQLException e) {
            throw new SQLException("Failed to create a connection", e);
        }
    }


    /// It's better to get an instance of AbstractDBMS rather than creating an object using reflection,
    /// but this is just for experimental purposes.
    public static DbConnection getInstance(Class<? extends AbstractDBMS> dbClass) throws SQLException {
        if (instance == null) {
            synchronized (DbConnection.class) {
                if (instance == null) {
                    try {
                        /// Reflection
                        AbstractDBMS db = dbClass.getDeclaredConstructor().newInstance();
                        instance = new DbConnection(db);
                    } catch (
                            NoSuchMethodException |
                            InvocationTargetException |
                            InstantiationException |
                            IllegalAccessException e
                    ) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        return instance;
    }

    public Connection getConnection() {
        System.out.println("Database connection established");
        return connection;
    }

}
