package org.mth3902.aom.rowMapper;

import org.mth3902.aom.model.Package;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PackageMapper implements RowMapper<Package> {
    @Override
    public Package mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new Package(
                rs.getInt("PACKAGE_ID"),
                rs.getString("PACKAGE_NAME"),
                rs.getInt("PERIOD"),
                rs.getDouble("PRICE"),
                rs.getInt("AMOUNT_DATA"),
                rs.getInt("AMOUNT_MINUTES"),
                rs.getInt("AMOUNT_SMS")
        );
    }
}
