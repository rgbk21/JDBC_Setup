package com.example.demo.ResultSet;

import com.example.demo.JDBCUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static java.sql.ResultSet.*;

public class SupportedResultSetProperties {

    public static void main(String[] args) {

        Connection connection = null;

        try {
            connection = JDBCUtil.getConnection();
            DatabaseMetaData dbmd = connection.getMetaData();

            System.out.println("** Supported result set scrollability **");
            printScrollabilityInfo(dbmd);

            System.out.println("\n** Supported result set concurrency **");
            printConcurrencyInfo(dbmd);

            System.out.println("\n** Supported result set holdability **");
            printHoldabilityInfo(dbmd);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(connection);
        }

    }

    private static void printScrollabilityInfo (DatabaseMetaData dbmd) throws SQLException {

        boolean forwardOnly = dbmd.supportsResultSetType(TYPE_FORWARD_ONLY);
        boolean scrollSensitive = dbmd.supportsResultSetType(TYPE_SCROLL_SENSITIVE);
        boolean scrollInsensitive = dbmd.supportsResultSetType(TYPE_SCROLL_INSENSITIVE);

        System.out.println("Forward-Only: " + forwardOnly);
        System.out.println("Scroll-Sensitive: " + scrollSensitive);
        System.out.println("Scroll-Insensitive: " + scrollInsensitive);

    }

    private static void printConcurrencyInfo (DatabaseMetaData dbmd) throws SQLException {

        boolean forwardOnlyReadOnly = dbmd.supportsResultSetConcurrency(TYPE_FORWARD_ONLY, CONCUR_READ_ONLY);
        boolean forwardOnlyUpdatable = dbmd.supportsResultSetConcurrency(TYPE_FORWARD_ONLY, CONCUR_UPDATABLE);

        boolean scrollSensitiveReadOnly = dbmd.supportsResultSetConcurrency(TYPE_SCROLL_SENSITIVE, CONCUR_READ_ONLY);
        boolean scrollSensitiveUpdatable = dbmd.supportsResultSetConcurrency(TYPE_SCROLL_SENSITIVE, CONCUR_UPDATABLE);

        boolean scrollInsensitiveReadOnly = dbmd.supportsResultSetConcurrency(TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY);
        boolean scrollInsensitiveUpdatable = dbmd.supportsResultSetConcurrency(TYPE_SCROLL_INSENSITIVE, CONCUR_UPDATABLE);

        System.out.println("Scroll Forward-Only and " + "Concurrency Read-Only: " + forwardOnlyReadOnly);
        System.out.println("Scroll Forward-Only and " + "Concurrency Updatable: " + forwardOnlyUpdatable);
        System.out.println("Scroll Sensitive and " + "Concurrency Read-Only: " + scrollSensitiveReadOnly);
        System.out.println("Scroll Sensitive and " + "Concurrency Updatable: " + scrollSensitiveUpdatable);
        System.out.println("Scroll Insensitive and " + "Concurrency Read-Only: " + scrollInsensitiveReadOnly);
        System.out.println("Scroll Insensitive and " + "Concurrency Updatable: " + scrollInsensitiveUpdatable);
    }

    private static void printHoldabilityInfo (DatabaseMetaData dbmd) throws SQLException {

        boolean holdOverCommit = dbmd.supportsResultSetHoldability(HOLD_CURSORS_OVER_COMMIT);
        boolean closeAtCommit = dbmd.supportsResultSetHoldability(CLOSE_CURSORS_AT_COMMIT);

        System.out.println("Hold Over Commit: " + holdOverCommit);
        System.out.println("Close At Commit: " + closeAtCommit);

    }

}
