package com.example.demo.Statement;

import com.example.demo.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UpdatePersonTest {

    public static void main(String[] args) {

        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();

            // Give everyone a 5% raise
            giveRaise(connection, 5.0);

            // Commit the transaction
            JDBCUtil.commit(connection);
            System.out.println("Updated person records successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeConnection(connection);
        }

    }

    private static void giveRaise(Connection connection, double pctRaise) throws SQLException{

        String query = "UPDATE person " +
                "SET income = income + (income * " + pctRaise/100 + ")";

        Statement statement = null;

        try {
            statement = connection.createStatement();
            // Note: The executeUpdate returns the number of rows that were updated
            int numOfRowsUpdated = statement.executeUpdate(query);
            System.out.println("Gave raise to " + numOfRowsUpdated + " people");
        } finally {
            JDBCUtil.closeStatement(statement);
        }
    }
}
