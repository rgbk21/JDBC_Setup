package com.example.demo.RowSet;

import com.example.demo.RowSetUtil;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.SyncResolver;
import java.sql.Date;
import java.sql.SQLException;

import static javax.sql.rowset.spi.SyncResolver.*;

public class CachedRowSetTest {

    public static void main(String[] args) throws SQLException {

        RowSetFactory factory = RowSetUtil.getRowSetFactory();
//        readFromPersonTable(factory);
//        pagingTest(factory);
        cachedRowSetUpdateTest(factory);

    }

    private static void cachedRowSetUpdateTest(RowSetFactory factory) throws SQLException {

        CachedRowSet cachedRowSet = factory.createCachedRowSet();

        try {
            // Set the connection parameters
            RowSetUtil.setConnectionParameters(cachedRowSet);

            // TODO: For some reason when positions of dob and income are interchanged in this query, SQL throws an exception
            String sqlQuery = "SELECT person_id, first_name, last_name, gender, income, dob FROM person";

            cachedRowSet.setKeyColumns(new int[]{1});
            cachedRowSet.setCommand(sqlQuery);
            cachedRowSet.execute();

            // Print the records in the cached rowset
            System.out.println("Before Update");
            System.out.println("Row Count: " + cachedRowSet.size());
            RowSetUtil.printPersonRecord(cachedRowSet);

            // Update income to 23000.00 for the first row
            if (cachedRowSet.size() > 0) {
                updateRow(cachedRowSet, 1, 63000.00);
            }

            // Insert a new row
            insertNewRow(cachedRowSet);

            // Send changes to the database
            cachedRowSet.acceptChanges();

            System.out.println("After Update");
            System.out.println("Row Count: " + cachedRowSet.size());
            cachedRowSet.beforeFirst();
            RowSetUtil.printPersonRecord(cachedRowSet);

        } catch (SyncProviderException spe) {

            // When acceptChanges() detects some conflicts
            SyncResolver resolver = spe.getSyncResolver();
            // Print the details about the conflicts
            printConflicts(resolver, cachedRowSet);

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            if (cachedRowSet != null) {
                try {
                    cachedRowSet.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void updateRow(CachedRowSet cachedRowSet, int rowNum, double newIncome) throws SQLException {

        // Set the values for columns in the new row
        System.out.println("Updating Row");
        cachedRowSet.absolute(rowNum);
        cachedRowSet.updateDouble("income", newIncome);
        cachedRowSet.updateRow();

    }

    public static void insertNewRow(CachedRowSet cachedRowSet) throws SQLException {

        // Move cursor to the insert-row
        cachedRowSet.moveToInsertRow();

        // Set the values for columns in the new row
        cachedRowSet.updateInt("person_id", 10);
        cachedRowSet.updateString("first_name", "James");
        cachedRowSet.updateString("last_name", "Doe");
        cachedRowSet.updateString("gender", "M");
        cachedRowSet.updateDate("dob", Date.valueOf("1990-01-03"));
        cachedRowSet.updateDouble("income", 0.00);

        // Insert the new row in the rowset. It is not sent to the
        // database, until the acceptChanges() method is called
        cachedRowSet.insertRow();

        // Must move back to the current row
        cachedRowSet.moveToCurrentRow();
    }

    public static void printConflicts(SyncResolver resolver, CachedRowSet cachedRowSet) {

        try {

            while (resolver.nextConflict()){

                int status = resolver.getStatus();

                String operation = "None";

                if (status == INSERT_ROW_CONFLICT) {
                    operation = "insert";
                }
                else if (status == UPDATE_ROW_CONFLICT) {
                    operation = "update";
                }
                else if (status == DELETE_ROW_CONFLICT) {
                    operation = "delete";
                }

                // Get person_id from the database
                Object oldPersonId = resolver.getConflictValue("person_id");

                // Get person ID from the cached rowset
                int row = resolver.getRow();
                cachedRowSet.absolute(row);
                Object newPersonId = cachedRowSet.getObject("person_id");

                // Use setResolvedValue() method to set resolved value for a column
                // resolver.setResolvedValue(columnName,resolvedValue);
                System.out.println("Conflict detected in row #"
                        + row
                        + " during " + operation + " operation."
                        + " person_id in database is " + oldPersonId
                        + " and person_id in rowset is " + newPersonId);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

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
                // Note the size in this example. It's not the entire size of the rows that match the query
                System.out.println("Page #" + pageCounter + " (Row Count=" + cachedRowSet.size() + ")");
                RowSetUtil.printPersonRecord(cachedRowSet);
                pageCounter++;
            } while (cachedRowSet.nextPage());

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void readFromPersonTable(RowSetFactory factory) {

        System.out.println("\n** Testing how paging works in CachedRowSet **");

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
