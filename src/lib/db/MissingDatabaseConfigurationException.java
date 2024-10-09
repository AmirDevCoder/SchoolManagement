package lib.db;

public class MissingDatabaseConfigurationException extends RuntimeException {
    public MissingDatabaseConfigurationException() {
        super("Missing DatabaseConfiguration Annotation Or Configuration class must have an empty-constructor");
    }
}
