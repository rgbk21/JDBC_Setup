package com.example.demo;

import javax.sql.RowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;

public class RowSetUtil {

    public static boolean driverLoaded = false;

    public static void setConnectionParameters(RowSet rowSet) throws SQLException {

        // Register the JDBC Driver only once for your Database
        if (!driverLoaded) {
            // Register the OracleDB JDBC Driver
            Driver d = new oracle.jdbc.driver.OracleDriver();
            DriverManager.registerDriver(d);
            driverLoaded = true;
        }

        // Set the rowset database connection properties
        String connectionUrl = "jdbc:oracle:thin:hr/hr@//localhost:1521/orclpdb";
        String userId = "hr";
        String password = "hr";

        rowSet.setUrl(connectionUrl);
        rowSet.setUsername(userId);
        rowSet.setPassword(password);

    }


    // TODO: What exactly just happened?
    public static RowSetFactory getRowSetFactory() {
        try {
            RowSetFactory rsFactory = RowSetProvider.newFactory();
            return rsFactory;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void printPersonRecord(RowSet rowSet) throws SQLException {

        while (rowSet.next()) {
            int personId = rowSet.getInt("person_id");
            String firstName = rowSet.getString("first_name");
            String lastName = rowSet.getString("last_name");

            System.out.println(
                    "RowId: " + rowSet.getRow() +
                    ", Person ID: " + personId +
                    ", First Name: " + firstName +
                    ", Last Name: " + lastName
            );
        }
    }

}
