package data.db.conf;

import lib.db.AbstractDBMS;
import lib.db.DbType;

// convention over configuration
public class Postgres extends AbstractDBMS {

    /// we can inject configuration file here

    @Override
    public DbType getType() {
        return DbType.POSTGRESQL;
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
    public String getUser() {
        return "emamagic";
    }

}
