package org.mth3902.aom.repository;

import org.example.VoltDatabase;
import org.mth3902.aom.model.Balance;
import org.mth3902.aom.model.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltType;

import java.time.Instant;

@Repository
public class BalanceRepository {
    private VoltDatabase voltDB;
    private PackageRepository packageRepository;

    @Autowired
    public BalanceRepository(Environment env, PackageRepository packageRepository) {

        this.packageRepository = packageRepository;

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
                    row.getDecimalAsBigDecimal("PRICE"),
                    Long.valueOf( (Integer) row.get("PACKAGE_ID", VoltType.INTEGER))
            );
        }

        return null;
    }
    public void save(long customerId, long packageId) throws Exception {

        Package cellPackage = packageRepository.getPackageById(packageId);

        Instant now = Instant.now();
        long packagePeriodInSeconds = 86400L * cellPackage.getPeriod();
        long startDate = now.getEpochSecond();
        long endDate = now.plusSeconds(packagePeriodInSeconds).getEpochSecond();

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
    }

    private long getNextId() {
        try {
            return voltDB.getNextBalanceId();
        } catch (Exception e) {
            System.out.println("voltdb error while getting customer id: " + e);
            return -1;
        }
    }
}
