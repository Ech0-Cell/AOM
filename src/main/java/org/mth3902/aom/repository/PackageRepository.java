package org.mth3902.aom.repository;

import org.example.VoltDatabase;
import org.mth3902.aom.model.Package;
import org.mth3902.aom.rowMapper.PackageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltType;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PackageRepository {
    private VoltDatabase voltDB;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PackageRepository(Environment env, JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;

        try {
            this.voltDB = new VoltDatabase(env.getProperty("voltdb.server.host"));
        } catch (Exception e) {
            System.out.println("error in voltdb" + e);
        }
    }

    public Package getPackageById(long id) throws Exception {

        VoltTable table = voltDB.selectPackagesById(id);

        if(table.advanceRow()) {
            VoltTableRow row = table.fetchRow(0);

            return new Package(
                    Long.valueOf( (Integer) row.get("PACKAGE_ID", VoltType.INTEGER)) ,
                    row.getString("PACKAGE_NAME"),
                    (int) row.get("PERIOD", VoltType.INTEGER),
                    row.getDecimalAsBigDecimal("PRICE").doubleValue(),
                    (int) row.get("AMOUNT_DATA", VoltType.INTEGER),
                    (int) row.get("AMOUNT_MINUTES", VoltType.INTEGER),
                    (int) row.get("AMOUNT_SMS", VoltType.INTEGER)
            );
        }
        return null;
    }

    public Package getPackageByIdOracle(long id) {

        String sql = "CALL select_package_by_id(?)";

        return jdbcTemplate.queryForObject(sql, new PackageMapper(), id);
    }

    public ArrayList<Package> getAllPackages() throws Exception {

        ArrayList<Package> packageList= new ArrayList<Package>();
        VoltTable table = voltDB.selectAllPackages();
        int counter = 0;

        while(table.advanceRow()) {

            VoltTableRow row = table.fetchRow(counter);
            counter++;

            Package cellPackage =  new Package(
                    Long.valueOf( (Integer) row.get("PACKAGE_ID", VoltType.INTEGER)) ,
                    row.getString("PACKAGE_NAME"),
                    (int) row.get("PERIOD", VoltType.INTEGER),
                    row.getDecimalAsBigDecimal("PRICE").doubleValue(),
                    (int) row.get("AMOUNT_DATA", VoltType.INTEGER),
                    (int) row.get("AMOUNT_MINUTES", VoltType.INTEGER),
                    (int) row.get("AMOUNT_SMS", VoltType.INTEGER)
            );

            packageList.add(cellPackage);
        }

        return packageList;
    }

    //TODO cast List to ArrayList
    public List<Package> getAllPackagesOracle(long id) {

        String sql = "CALL select_all_packages()";

        return jdbcTemplate.query(sql, new PackageMapper());
    }
}
