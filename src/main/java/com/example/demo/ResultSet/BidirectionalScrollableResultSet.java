package com.example.demo.ResultSet;

import com.example.demo.JDBCUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BidirectionalScrollableResultSet {

    public static void main(String[] args) {

        Connection connection = null;
        Statement statement = null;

        try {
            connection = JDBCUtil.getConnection();

            // Request a bi-directional scrollable ResultSet
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
            String sqlQuery = "SELECT person_id, first_name, last_name, dob, income FROM person";

            // Execute the Query
            ResultSet rs = statement.executeQuery(sqlQuery);

            // Make sure you got a bi-directional ResultSet
            int cursorType = rs.getType();
            // The concurrency needs to be fetched separately
            int concurrency = rs.getConcurrency();
            //The holdability can be read in the following way
            int holdability = rs.getHoldability();

            if (cursorType == ResultSet.TYPE_FORWARD_ONLY) {
                System.out.println("JDBC driver returned a forward - only cursor.");
            } else {
                System.out.println("The type of this ResultSet object: " + cursorType);
                System.out.println("The concurrency mode of this ResultSet object: " + concurrency);
                System.out.println("The holdability of this ResultSet object: " + holdability);

                // Move the cursor to the last row
                rs.last();

                // Get the last row number, which is the row count
                int rowCount = rs.getRow();
                System.out.println("Row Count: " + rowCount);

                // Place the cursor before the first row to process all rows again
                rs.beforeFirst();
            }

            // Process the result set
            while (rs.next()) {
                System.out.println("Person ID: " + rs.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeStatement(statement);
            JDBCUtil.commit(connection);
            JDBCUtil.closeConnection(connection);
        }

    }

}
