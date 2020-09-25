package com.example.demo.ResultSet;

import com.example.demo.JDBCUtil;

import java.sql.*;

public class ResultSetUpdate {

    public static void main(String[] args) {

        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();

            // Give everyone a 10% raise
            giveRaise(connection, 10.0);

            // Commit the changes
            JDBCUtil.commit(connection);

        } catch (SQLException e) {
            e.printStackTrace();
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeConnection(connection);
        }

    }

    private static void giveRaise(Connection connection, double pctRaise)  throws SQLException{

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            String sqlQuery = "SELECT person_id, first_name, last_name, income FROM person";
            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(sqlQuery);

            // Make sure our resultset is updatable
            int concurrency = resultSet.getConcurrency();
            if (concurrency != ResultSet.CONCUR_UPDATABLE) {
                System.out.println("The JDBC driver does not support updatable result sets.");
                return;
            }

            // Give everyone a raise
            while (resultSet.next()) {
                double oldIncome = resultSet.getDouble("income");
                double newIncome = 0.0;

                // If the previous income was null, set the income to 50000 by default
                if (resultSet.wasNull()) {
                    newIncome = 50000;
                } else {
                    newIncome = oldIncome + (oldIncome * (pctRaise/100));
                }

                // Update the income column with the new value
                resultSet.updateDouble("income", newIncome);

                // Send the changes to the database
                resultSet.updateRow();

                // Print the details about the changes
                System.out.println("PersonID: " + resultSet.getInt("person_id"));
                System.out.println("First Name: " + resultSet.getString("first_Name"));
                System.out.println("Last Name: " + resultSet.getString("last_Name"));

                // Note that you  can read the changes you did from the same ResultSet,
                // unlike the Insert method
                System.out.println("Previous Income: " + oldIncome);
                System.out.println("New Income: " + resultSet.getDouble("income"));

            }

        } finally {
            JDBCUtil.closeStatement(statement);
        }

    }


}
