package com.example.demo.Statement;

import com.example.demo.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InsertPersonRows {

    public static void main(String[] args) {

        Connection connection = null;

        try {

            connection = JDBCUtil.getConnection();

            // Insert records
            insertPerson(connection, 1,"Alice", "Doe", "F", "{d '1990/01/01'}", 50000);
            insertPerson(connection, 2,"Bob", "Doe", "M", "{d '1990/01/02'}", 50000);
            insertPerson(connection, 3,"Charlie", "Doe", "M", "{d '1990/01/03'}", 50000);

            // Commit the transaction
            JDBCUtil.commit(connection);
            System.out.println("Inserted persons successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeConnection(connection);
        }

    }

    public static void insertPerson(Connection connection,
                                    int personId,
                                    String firstName,
                                    String lastName,
                                    String gender,
                                    String dob,
                                    double income) throws SQLException{

        // Create SQL String
        // Note the quotation marks around the character and String variables
        String query = "INSERT INTO person (person_id, first_name, last_name, gender, dob, income) " +
                "VALUES (" + personId + ",'" + firstName + "','" + lastName + "', '" + gender + "'," + dob + "," + income + ")";

        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } finally {
            JDBCUtil.closeStatement(statement);
        }
    }
}
