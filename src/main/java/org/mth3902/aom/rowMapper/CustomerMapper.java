package org.mth3902.aom.rowMapper;

import org.mth3902.aom.model.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new Customer(
                rs.getLong("id"),
                rs.getString("msisdn"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("security_key")
        );
    }
}
