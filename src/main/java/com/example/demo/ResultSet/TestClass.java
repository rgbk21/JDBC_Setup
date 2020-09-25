package com.example.demo.ResultSet;

import com.example.demo.JDBCUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static java.sql.ResultSet.TYPE_FORWARD_ONLY;

public class TestClass {

    public static void main(String[] args) throws SQLException {

        Connection connection = JDBCUtil.getConnection();
        DatabaseMetaData dbmd = connection.getMetaData();
        System.out.println(dbmd.ownUpdatesAreVisible(TYPE_FORWARD_ONLY)); // Prints FALSE

    }

}
