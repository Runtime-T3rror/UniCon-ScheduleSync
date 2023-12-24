package com.unicon.schedulesync.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/UniCon?user=postgres&password=root";
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }
}
