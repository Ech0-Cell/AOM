package org.mth3902.aom.repository;

import org.example.VoltDatabase;
import org.mth3902.aom.DTO.RegisterCustomerRequest;
import org.mth3902.aom.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltType;

@Repository
public class CustomerRepository {

    private VoltDatabase voltDB;
    private BalanceRepository balanceRepository;

    @Autowired
    public CustomerRepository(Environment env, BalanceRepository balanceRepository) {

        this.balanceRepository = balanceRepository;

        try {
            this.voltDB = new VoltDatabase(env.getProperty("voltdb.server.host"));
        } catch (Exception e) {
            System.out.println("error in voltdb" + e);
        }
    }

    public RegisterCustomerRequest save(RegisterCustomerRequest customer, String hashedPassword) throws Exception {

        long customerId = getNextId();

        balanceRepository.save(customerId, customer.getPackageID());

        voltDB.insertCustomer(customerId,
                customer.getMsisdn(),
                customer.getName(),
                customer.getSurname(),
                customer.getEmail(),
                hashedPassword,
                "1",
                customer.getSecurityKey());

        return customer;
    }

    public Customer getCustomerByMSISDN(String MSISDN) throws Exception {


        VoltTable table = voltDB.selectCustomerByMSISDN(MSISDN);

        if(table.advanceRow()) {//check if there is customer with such msisdn

            VoltTableRow row = table.fetchRow(0);

            return new Customer(
                    Long.valueOf( (Integer) row.get("CUST_ID", VoltType.INTEGER)),
                    row.getString("msisdn"),
                    row.getString("name"),
                    row.getString("surname"),
                    row.getString("email"),
                    row.getString("password"),
                    row.getString("security_key")
            );
        }
        return null;

    }

    public boolean existsByMSISDN(String MSISDN) throws Exception {

        return getCustomerByMSISDN(MSISDN) != null;
    }

    private long getNextId() {
        try {
            return voltDB.getNextCustomerId();
        } catch (Exception e) {
            System.out.println("voltdb error while getting customer id: " + e);
            return 1;
        }
    }
}
