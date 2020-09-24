package com.example.demo.ResultSet;

import com.example.demo.JDBCUtil;

import java.sql.*;

public class QueryPersonTest {

    public static void main(String[] args) {

        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();

            System.out.println("** Using Statement Object **");
            displayPersonUsingStatement(connection, 1);
            displayPersonUsingStatement(connection, 2);

            /*
            The main() method calls both of these methods to print the details of the same person id.
            In this example, you are not benefiting from precompilation of the PreparedStatement object,
            because you are calling this method separately for each person id.
            If you want to execute the same PreparedStatement with different inputs multiple times,
            you store the reference of the PreparedStatement in your program and reuse it.
            */
            System.out.println("\n** Using PreparedStatement Object **");
            displayPersonUsingPreparedStatement(connection, 1);
            displayPersonUsingPreparedStatement(connection, 2);

        } catch (SQLException e) {
            e.printStackTrace();
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeConnection(connection);
        }

    }

    private static void displayPersonUsingStatement(Connection connection, int personId) throws SQLException {

        String sqlQuery = "SELECT * FROM person " +
                "WHERE person_id = " + personId;

        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // TODO: Why do we need to specify this in Statement but not in PreparedStatement?
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(sqlQuery);
            printResultSet(resultSet);
        } finally {
            // Closing the Statement closes the associated ResultSet
            JDBCUtil.closeStatement(statement);
        }
    }

    private static void displayPersonUsingPreparedStatement(Connection connection, int personId) throws SQLException{

        String sqlQuery = "SELECT * FROM person WHERE person_id = ?";

        PreparedStatement pStmnt = null;
        ResultSet resultSet = null;

        try {
            pStmnt = connection.prepareStatement(sqlQuery);
            pStmnt.setInt(1, personId);
            resultSet = pStmnt.executeQuery();
            printResultSet(resultSet);
        } finally {
            // Closing the Statement closes the ResultSet
            JDBCUtil.closeStatement(pStmnt);
        }

    }

    private static void printResultSet(ResultSet resultSet) throws SQLException{

        while (resultSet.next()) {
            int personId = resultSet.getInt("person_id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String gender = resultSet.getString("gender");

            // Note that the wasNull() method does not take any arguments
            Date dob = resultSet.getDate("dob");
            boolean isDobNull = resultSet.wasNull();

            double income = resultSet.getDouble("income");
            boolean isIncomeNull = resultSet.wasNull();

            System.out.print("Person ID:" + personId);
            System.out.print(", First Name:" + firstName);
            System.out.print(", Last Name:" + lastName);
            System.out.print(", Gender:" + gender);

            if (isDobNull) {
                System.out.print(", DOB:null");
            }
            else {
                System.out.print(", DOB:" + dob.toString());
            }
            if (isIncomeNull) {
                System.out.println(", Income:null");
            }
            else {
                System.out.println(", Income:" + income);
            }

        }


    }

}
