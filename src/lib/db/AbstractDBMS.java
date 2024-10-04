package lib.db;

public abstract class AbstractDBMS implements DBMS {

    @Override
    public String getUrl() {
        return String.format("jdbc:%s://%s:%d/%s", getType().string(), getHost(), getPort(), getDBName());
    }


}
