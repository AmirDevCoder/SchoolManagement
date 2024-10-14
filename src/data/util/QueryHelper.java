package data.util;

import lombok.experimental.UtilityClass;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@UtilityClass
public class QueryHelper {

    /// @param fields The order of field does matter
    public static void setQueryColumn(PreparedStatement stmt, Object... fields) throws SQLException {
        for (int i = 0; i < fields.length; i++) {
            stmt.setObject(i + 1, fields[i]);
        }
    }


}
