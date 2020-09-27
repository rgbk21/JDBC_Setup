package com.example.demo.RowSet;

import com.example.demo.RowSetUtil;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import java.sql.SQLException;

public class CachedRowSetTest {

    public static void main(String[] args) {

        RowSetFactory factory = RowSetUtil.getRowSetFactory();
//        readFromPersonTable(factory);
        pagingTest(factory);

    }

    private static void pagingTest(RowSetFactory factory) {

        System.out.println("\n** Testing how paging works in CachedRowSet **");

        try (CachedRowSet cachedRowSet = factory.createCachedRowSet()) {
            // Set the connection parameters
            RowSetUtil.setConnectionParameters(cachedRowSet);

            String sqlQuery = "SELECT person_id, first_name, last_name " +
                    "FROM person";

            cachedRowSet.setCommand(sqlQuery);

            // Set pageSize to 3
            cachedRowSet.setPageSize(4);

            cachedRowSet.execute();

            int pageCounter = 1;

            // Retrieve and print person records one page at a time
            do {
                System.out.println("Page #" + pageCounter + " (Row Count=" + cachedRowSet.size() + ")");
                RowSetUtil.printPersonRecord(cachedRowSet);
                pageCounter++;
            } while (cachedRowSet.nextPage());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void readFromPersonTable(RowSetFactory factory) {

        try (CachedRowSet cachedRowSet = factory.createCachedRowSet()) {
            // Set the connection parameters
            RowSetUtil.setConnectionParameters(cachedRowSet);

            String sqlQuery = "SELECT person_id, first_name, last_name " +
                    "FROM person " +
                    "WHERE person_id BETWEEN ? AND ?";

            cachedRowSet.setCommand(sqlQuery);
            cachedRowSet.setInt(1, 1);
            cachedRowSet.setInt(2, 5);

            cachedRowSet.execute();

            // Print the records in the cached RowSet
            System.out.println("Row Count: " + cachedRowSet.size());
            RowSetUtil.printPersonRecord(cachedRowSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
