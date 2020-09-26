package com.example.demo.RowSet;

import com.example.demo.RowSetUtil;

import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetFactory;
import java.sql.SQLException;

public class JDBCRowSetTest {

    public static void main(String[] args) {

        RowSetFactory factory = RowSetUtil.getRowSetFactory();

        readFromPersonTable(factory);
        updatePersonTable(factory);

    }

    private static void updatePersonTable(RowSetFactory factory) {

        // Use try-with-resources block
        try (JdbcRowSet jdbcRowSet = factory.createJdbcRowSet()) {

            // Set the connection parameters
            RowSetUtil.setConnectionParameters(jdbcRowSet);

            // Set the auto-commit mode to false
            jdbcRowSet.setAutoCommit(false);

            // Set the command and input parameters
            String sqlCommand = "SELECT person_id, first_name, last_name, income " +
                    "FROM person " +
                    "WHERE person_id = ?";

            // Set the command to the RowSet object
            jdbcRowSet.setCommand(sqlCommand);
            jdbcRowSet.setInt(1, 5);

            // Retrieve the data
            jdbcRowSet.execute();

            // If a row is retrieved, update it's income.
            // Note the updateDouble, updateRow, commit methods being used.
            if (jdbcRowSet.next()) {
                double oldIncome = jdbcRowSet.getDouble("income");
                double newIncome = oldIncome + 1000;
                jdbcRowSet.updateDouble("income", newIncome);
                jdbcRowSet.updateRow();
                jdbcRowSet.commit();
            }

            // Print the records in the rowset
            RowSetUtil.printPersonRecord(jdbcRowSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void readFromPersonTable(RowSetFactory factory) {

        // Use try-with-resources block
        try (JdbcRowSet jdbcRowSet = factory.createJdbcRowSet()) {

            // Set the connection parameters
            RowSetUtil.setConnectionParameters(jdbcRowSet);

            // Set the command and input parameters
            String sqlCommand = "SELECT person_id, first_name, " +
                    "last_name FROM person " +
                    "WHERE person_id BETWEEN ? AND ?";

            // Set the command to the RowSet object
            jdbcRowSet.setCommand(sqlCommand);
            jdbcRowSet.setInt(1, 1);
            jdbcRowSet.setInt(2, 5);

            // Retrieve the data
            jdbcRowSet.execute();

            // Scroll to the last row to get the row count.
            // It may throw an exception if the underlying JdbcRowSet implementation
            // does not support a bi-directional scrolling result set.
            try {
                jdbcRowSet.last();
                System.out.println("Row Count: " + jdbcRowSet.getRow());
                // Position the cursor before the first row
                jdbcRowSet.beforeFirst();
            }
            catch(SQLException e) {
                System.out.println("JdbcRowSet implementation supports forward-only scrolling");
            }

            // Print the records in the rowset
            RowSetUtil.printPersonRecord(jdbcRowSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
