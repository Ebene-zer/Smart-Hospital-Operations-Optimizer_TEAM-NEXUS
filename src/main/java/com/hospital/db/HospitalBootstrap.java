package com.hospital.db;

import java.sql.SQLException;

/**
 * Starts SQLite, applies schema.sql, and seeds from classpath CSVs so a fresh
 * clone or packaged JAR can run without manual database setup.
 */
public final class HospitalBootstrap {

    private HospitalBootstrap() {
    }

    public static void ensureReady() throws SQLException {
        DBConnection.initializeSchema();
        new CSVImporter(DBConnection.getConnection()).importAll();
    }
}
