package org.mth3902.aom.repository;

import org.mth3902.aom.model.Package;
import org.mth3902.aom.voltdb.VoltDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;
import org.voltdb.VoltTable;
import org.voltdb.VoltTableRow;
import org.voltdb.VoltType;

import java.util.ArrayList;

@Repository
public class PackageRepository {
    private VoltDatabase voltDB;

    @Autowired
    public PackageRepository(Environment env) {

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


}
