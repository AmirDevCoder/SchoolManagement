package lib.db;

import com.mongodb.client.MongoDatabase;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class Database {
    private volatile static Connection postgresConnection, mysqlConnection;
    private volatile static MongoDatabase mongoDatabase;
    private static final List<DBConfigAbstraction> configs = new ArrayList<>();

    private Database() {
    }

    static {
        try {
            Reflections reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forJavaClassPath())
                    .setScanners(Scanners.TypesAnnotated));
            Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(DBConfiguration.class);

            if (annotatedClasses.isEmpty()) {
                throw new MissingDatabaseConfigurationException();
            }

            for (Class<?> configClass : annotatedClasses) {
                Constructor<?> constructor = configClass.getConstructor();
                // configuration class must have an empty-constructor
                DBConfigAbstraction config = (DBConfigAbstraction) constructor.newInstance();
                configs.add(config);
            }
        } catch (Exception e) {
            throw new MissingDatabaseConfigurationException();
        }
    }

    public static Connection getPostgresConnection() {
        if (postgresConnection == null) {
            synchronized (Database.class) {
                if (postgresConnection == null) {
                    DBConfig config = configs.stream()
                            .filter((conf) -> conf.getDBType() == DBType.POSTGRESQL)
                            .findFirst()
                            .orElseThrow(MissingDatabaseConfigurationException::new);
                    postgresConnection = DBConnectionHandler.getPostgresConnection(config);
                }
            }
        }

        return postgresConnection;
    }

    public static Connection getMySqlConnection() {
        if (mysqlConnection == null) {
            synchronized (Database.class) {
                if (mysqlConnection == null) {
                    DBConfig config = configs.stream()
                            .filter((conf) -> conf.getDBType() == DBType.MYSQL)
                            .findFirst()
                            .orElseThrow(MissingDatabaseConfigurationException::new);
                    mysqlConnection = DBConnectionHandler.getMySqlConnection(config);
                }
            }
        }

        return mysqlConnection;
    }

    public static MongoDatabase getMongoDB() {
        if (mongoDatabase == null) {
            synchronized (Database.class) {
                if (mongoDatabase == null) {
                    DBConfig config = configs.stream()
                            .filter((conf) -> conf.getDBType() == DBType.MONGODB)
                            .findFirst()
                            .orElseThrow(MissingDatabaseConfigurationException::new);
                    mongoDatabase = DBConnectionHandler.getMongoDatabase(config);
                }
            }
        }

        return mongoDatabase;
    }

    public static void closeMongoDBClient() {
        if (mongoDatabase != null) {
            DBConnectionHandler.closeMongoClient();
        }
    }

}
