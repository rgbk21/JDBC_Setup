package com.example.demo.Statement;

import com.example.demo.JDBCUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreatePersonTable {

    public static void main(String[] args) {

        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();

            // Create a SQL String
            // Note that there is no semi-colon at the end of the query
            // This link explains why: https://stackoverflow.com/a/18516309/8742428
            String query = "CREATE TABLE PERSON (" +
                    "    person_id NUMBER(8,0) NOT NULL," +
                    "    first_name VARCHAR2(20) NOT NULL," +
                    "    last_name VARCHAR2(20) NOT NULL," +
                    "    gender CHAR(1) NOT NULL," +
                    "    dob DATE," +
                    "    income NUMBER(10,2)," +
                    "    CONSTRAINT PK_PERSON PRIMARY KEY (person_id)" +
                    ")";

            Statement statement = null;

            try {
                statement = connection.createStatement();
                statement.executeUpdate(query);
            } finally {
                JDBCUtil.closeStatement(statement);
            }

            // Commit the transaction
            JDBCUtil.commit(connection);
            System.out.println("Person2 table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeConnection(connection);
        }
    }
}
