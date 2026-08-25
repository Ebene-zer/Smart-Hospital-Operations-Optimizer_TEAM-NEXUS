package com.hospital.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class SchemaManager {

    public static void ensureTablesExist() throws SQLException {
        if (!tablesExist()) {
            System.out.println("Database tables not found. Creating schema...");
            try (Connection conn = DBConnection.getConnection();
                    Statement stmt = conn.createStatement()) {
                InputStream is = SchemaManager.class.getResourceAsStream("/db/schema.sql");
                if (is == null) {
                    throw new SQLException("Could not find schema.sql in resources.");
                }
                String schemaSql = new BufferedReader(new InputStreamReader(is))
                        .lines().collect(Collectors.joining("\n"));

                // SQLite JDBC driver doesn't support executing multiple statements at once.
                // Split by semicolon and execute one by one.
                for (String sql : schemaSql.split(";")) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql);
                    }
                }
                System.out.println("Schema created successfully.");
            }
        }
    }

    private static boolean tablesExist() throws SQLException {
        // Check for the existence of one of the main tables.
        try (Connection conn = DBConnection.getConnection();
                ResultSet rs = conn.getMetaData().getTables(null, null, "locations", null)) {
            return rs.next();
        }
    }
}