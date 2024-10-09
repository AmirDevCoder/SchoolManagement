package lib.db;

public interface DBConfig {
    String getUrl();

    DBType getDBType();

    String getHost();

    int getPort();

    String getDBName();

    String getUsername();

    String getPassword();
}
