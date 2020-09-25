package com.example.demo.ResultSet;

import com.example.demo.JDBCUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetInsert {

    public static void main(String[] args) {

        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            addNewRow(connection);

            // Commit the transaction
            JDBCUtil.commit(connection);


        } catch (SQLException e) {
            e.printStackTrace();
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeConnection(connection);
        }

    }

    private static void addNewRow(Connection connection) throws SQLException {

        /*
        Remember not to use SELECT * in a Query. If you use SELECT * instead of specifying the individual columns,
        you end up with a non-updatable read-only ResultSet that throws exception when you try to INSERT into it.
        */
        // https://stackoverflow.com/a/26672027/8742428
        String sqlQuery = "SELECT person_id, first_name, last_name, gender, dob FROM person";
        Statement statement = null;

        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            // Get the ResultSet
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            // Make sure your ResultSet is updatable
            int concurrency = resultSet.getConcurrency();
            if (concurrency != ResultSet.CONCUR_UPDATABLE) {
                System.out.println("The JDBC driver does not support updatable result sets.");
                return;
            }

            // We are inserting 2 rows into the DB in this example

            // First insert a new row to the ResultSet
            resultSet.moveToInsertRow();

            // Update the values in the row
            resultSet.updateInt("person_id", 11);
            resultSet.updateString("first_name", "Kali");
            resultSet.updateString("last_name", "Doe");
            resultSet.updateString("gender", "F");

            // Send the new row to the database
            resultSet.insertRow();

            // Move back to the current row
            resultSet.moveToCurrentRow();
            System.out.println("Current row number after 1st insert is: " + resultSet.getRow());

            //Print all the people
            // Note that this does NOT print the row we just inserted
            while (resultSet.next()) {
                System.out.println("Person_ID: " + resultSet.getInt("person_id") +
                        "First_Name: " + resultSet.getString("first_name") +
                        "Last_Name: " + resultSet.getString("last_name"));
            }

            // Inserting Second Row

            // First insert a new row to the ResultSet
            resultSet.moveToInsertRow();

            // Update the values in the row
            resultSet.updateInt("person_id", 12);
            resultSet.updateString("first_name", "Lana");
            resultSet.updateString("last_name", "Doe");
            resultSet.updateString("gender", "F");

            // Send the new row to the database
            resultSet.insertRow();

            // Move back to the current row
            resultSet.moveToCurrentRow();
            System.out.println("Current row number after 2nd insert is: " + resultSet.getRow());

            // Remember that you have to commit the transaction after this in order for the changes to take effect

        } finally {
            JDBCUtil.closeStatement(statement);
        }
    }


}
