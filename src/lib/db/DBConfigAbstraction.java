package lib.db;

public abstract class DBConfigAbstraction implements DBConfig {

    @Override
    public String getUrl() {
        return switch (getDBType()) {
            case POSTGRESQL, MYSQL -> String.format("jdbc:%s://%s:%d/%s", getDBType().name().toLowerCase(), getHost(), getPort(), getDBName());
            case MONGODB -> String.format("%s://%s:%s@%s:%d", getDBType().name().toLowerCase(), getUsername(), getPassword(), getHost(), getPort());
        };
    }
}
