package lib.db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnectionHandler {

    private static MongoClient mongoClient;

    private DBConnectionHandler() {
    }

    static Connection getPostgresConnection(DBConfig conf) {
        try  {
            return DriverManager.getConnection(conf.getUrl(), conf.getUsername(), conf.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static Connection getMySqlConnection(DBConfig conf) {
        try  {
            return DriverManager.getConnection(conf.getUrl(), conf.getUsername(), conf.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static MongoDatabase getMongoDatabase(DBConfig conf) {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(pojoCodecProvider)
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(conf.getUrl()))
                .codecRegistry(pojoCodecRegistry)
                .build();

        try {
            mongoClient = MongoClients.create(settings);
            return mongoClient.getDatabase(conf.getDBName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void closeMongoClient() {
        mongoClient.close();
    }


}
