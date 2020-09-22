package com.example.demo;

import java.sql.*;

public class JDBCUtil {

    public static void main(String[] args) {

        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection();
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(conn);
        }

    }

    // https://stackoverflow.com/questions/4832056/java-jdbc-how-to-connect-to-oracle-using-service-name-instead-of-sid
    public static Connection getConnection() throws SQLException{

        // Register the OracleDB JDBC Driver
        Driver d =  new oracle.jdbc.driver.OracleDriver();
        DriverManager.registerDriver(d);

        // Construct the connection URL
        String connectionUrl = "jdbc:oracle:thin:hr/hr@//localhost:1521/orclpdb";
        String userId = "hr";
        String password = "hr";

        // Get a connection
        Connection conn = DriverManager.getConnection(connectionUrl, userId, password);

        // Set the auto-commit to off
        conn.setAutoCommit(false);

        return conn;

    }

    public static void closeConnection(Connection conn) {

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void closeStatement(Statement statement) {

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void closeResultSet(ResultSet resultSet) {

        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void commit(Connection connection) {

        try {
            if (connection != null) {
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void rollback(Connection connection) {

        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}