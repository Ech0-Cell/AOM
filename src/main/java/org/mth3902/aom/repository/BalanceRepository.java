package org.mth3902.aom.repository;

import org.example.VoltDatabase;
import org.mth3902.aom.model.Balance;
import org.mth3902.aom.model.Package;
import org.mth3902.aom.rowMapper.BalanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltType;
import java.time.Instant;

@Repository
public class BalanceRepository {
    private VoltDatabase voltDB;
    private final PackageRepository packageRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BalanceRepository(Environment env, PackageRepository packageRepository, JdbcTemplate jdbcTemplate) {

        this.packageRepository = packageRepository;
        this.jdbcTemplate = jdbcTemplate;

        try {
            this.voltDB = new VoltDatabase(env.getProperty("voltdb.server.host"));
        } catch (Exception e) {
            System.out.println("error in voltdb" + e);
        }
    }
    public Balance getBalanceByMSISDN(String msisdn) throws Exception {

        VoltTable table = voltDB.selectBalanceByMSISDN(msisdn);

        if(table.advanceRow())
        {
            VoltTableRow row = table.fetchRow(0);

            return new Balance(
                    (int) row.get("BAL_LVL_DATA", VoltType.INTEGER),
                    (int) row.get("BAL_LVL_SMS", VoltType.INTEGER),
                    (int) row.get("BAL_LVL_MINUTES", VoltType.INTEGER),
                    row.getDecimalAsBigDecimal("BAL_LVL_MONEY").doubleValue(),
                    row.getTimestampAsLong("EDATE"),
                    row.getTimestampAsLong("SDATE"),
                    row.getDecimalAsBigDecimal("PRICE").doubleValue(),
                    Long.valueOf( (Integer) row.get("PACKAGE_ID", VoltType.INTEGER))
            );
        }

        return null;
    }

    public Balance getBalanceByMSISDNOracle(String MSISDN) {

        String sql = "CALL select_balance_by_msisdn(?)";

        return jdbcTemplate.queryForObject(sql, new BalanceMapper(), MSISDN);
    }

    public void save(long customerId, long packageId) throws Exception {

        Package cellPackage = packageRepository.getPackageById(packageId);

        Instant now = Instant.now();
        long packagePeriodInSeconds = 86400L * cellPackage.getPeriod();
        long startDate = now.getEpochSecond();
        long endDate = now.plusSeconds(packagePeriodInSeconds).getEpochSecond();

        //insert at voltdb
        voltDB.insertBalance(
                voltDB.getNextBalanceId(),
                cellPackage.getId(),
                customerId,
                1,
                cellPackage.getMinAmount(),
                cellPackage.getSmsAmount(),
                cellPackage.getDataAmount(),
                startDate,
                endDate,
                cellPackage.getPrice(),
                100
        );

        //insert at oracle
//        String sql = "CALL insert_customer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        jdbcTemplate.update(sql,
//                voltDB.getNextBalanceId(),
//                cellPackage.getId(),
//                customerId,
//                1,
//                cellPackage.getMinAmount(),
//                cellPackage.getSmsAmount(),
//                cellPackage.getDataAmount(),
//                startDate,
//                endDate,
//                cellPackage.getPrice(),
//                100
//        );

    }

    private long getNextId() {
        try {
            return voltDB.getNextBalanceId();
        } catch (Exception e) {
            System.out.println("voltdb error while getting customer id: " + e);
            return 1;
        }
    }
}
