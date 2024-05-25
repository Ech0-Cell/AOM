package org.mth3902.aom.rowMapper;

import org.mth3902.aom.model.Balance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BalanceMapper implements RowMapper<Balance> {
    @Override
    public Balance mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new Balance(
                rs.getInt("BAL_LVL_DATA"),
                rs.getInt("BAL_LVL_SMS"),
                rs.getInt("BAL_LVL_MINUTES"),
                rs.getDouble("BAL_LVL_MONEY"),
                rs.getLong("EDATE"),
                rs.getLong("SDATE"),
                rs.getDouble("PRICE"),
                rs.getLong("PACKAGE_ID")
        );

    }
}
