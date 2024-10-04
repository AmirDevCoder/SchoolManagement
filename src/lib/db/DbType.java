package lib.db;

public enum DbType {
    POSTGRESQL("postgresql"),
    MYSQL("mysql");

    private final String type;
    DbType(String type) {
        this.type = type;
    }

    public String string() {
        return type;
    }

}
