package com.example.demo.ResultSet;

import com.example.demo.JDBCUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResultSetDelete {

    public static void main(String[] args) {

        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            deleteRow(connection, 10);

            // Commit the transaction
            JDBCUtil.commit(connection);

        } catch (SQLException e) {
            e.printStackTrace();
            JDBCUtil.rollback(connection);
        } finally {
            JDBCUtil.closeConnection(connection);
        }

    }

    private static void deleteRow(Connection connection, int idToDelete) throws SQLException {

        Statement statement = null;
        ResultSet resultSet = null;

        String sqlQuery = "SELECT person_id, first_name, last_name FROM person";

        try {
            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                int personId = resultSet.getInt("person_id");

                if (!resultSet.wasNull() && (personId == idToDelete)){
                    resultSet.deleteRow();
                }
            }

        } finally {
            JDBCUtil.closeStatement(statement);
        }

    }

}
