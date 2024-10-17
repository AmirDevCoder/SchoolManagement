package data.db.conf;

import lib.db.DBConfigAbstraction;
import lib.db.DBConfiguration;
import lib.db.DBType;

// convention over configuration
@DBConfiguration
public class Postgres extends DBConfigAbstraction {

    /// we can inject configuration file here

    @Override
    public DBType getDBType() {
        return DBType.POSTGRESQL;
    }

    @Override
    public String getDBName() {
        return "school";
    }

    @Override
    public String getHost() {
        return "localhost";
    }

    @Override
    public int getPort() {
        return 5432;
    }

    @Override
    public String getPassword() {
        return "1377";
    }

    @Override
    public String getUsername() {
        return "emamagic";
    }

}
