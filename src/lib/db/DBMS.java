package lib.db;

interface DBMS {
    String getUrl();
    DbType getType();
    String getDBName();
    String getHost();
    int getPort();
    String getPassword();
    String getUser();
}
