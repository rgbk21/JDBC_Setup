package com.example.demo.PreparedStatement;

import com.example.demo.JDBCUtil;

import java.sql.*;

public class InsertPersonRows {

    public static void main(String[] args) {

        Connection connection = null;
        PreparedStatement preparedStmnt = null;

        try {
            connection = JDBCUtil.getConnection();

            preparedStmnt = getPreparedStatement(connection);
            // Need to get dob in java.sql.Date object
            Date dob = Date.valueOf("1990-01-03");

            // Insert two person records
            insertPerson(preparedStmnt, 3,"Charlie", "Doe", "M", dob, 50000);
            insertPerson(preparedStmnt, 4,"Dave", "Doe", "M", null, 50000);

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

    public static void insertPerson(PreparedStatement pStmnt,
                                    int personId,
                                    String firstName,
                                    String lastName,
                                    String gender,
                                    Date dob,
                                    double income) throws SQLException{

        pStmnt.setInt(1, personId);
        pStmnt.setString(2, firstName);
        pStmnt.setString(3, lastName);
        pStmnt.setString(4, gender);

        // Set the dob value properly if it is null
        if (dob == null) {
            pStmnt.setNull(5, Types.DATE);
        }
        else {
            pStmnt.setDate(5, dob);
        }

        pStmnt.setDouble(6, income);

        // Execute the statement
        pStmnt.executeUpdate();

    }

    private static PreparedStatement getPreparedStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStmnt;
        String sqlQuery = "INSERT INTO person " +
                "(person_id, first_name, last_name, gender, dob, income) " +
                "VALUES " +
                "(?, ?, ?, ?, ?, ?)";
        preparedStmnt = connection.prepareStatement(sqlQuery);
        return preparedStmnt;
    }

}
