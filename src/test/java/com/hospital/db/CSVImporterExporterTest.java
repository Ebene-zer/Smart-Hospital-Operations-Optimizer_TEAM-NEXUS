package com.hospital.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CSVImporterExporterTest - Team 1 (Jean Afiba Garibah)
 * Tests that CSVImporter correctly loads seed data into the database
 * and that CSVExporter can write it back out without errors.
 * Uses an in-memory SQLite database so no real DB is needed.
 */
public class CSVImporterExporterTest {

    private Connection connection;
    private CSVImporter importer;
    private CSVExporter exporter;

    @BeforeEach
    public void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        connection.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS locations (" +
            "locationId TEXT PRIMARY KEY, " +
            "name TEXT, area TEXT, type TEXT, " +
            "latitude REAL, longitude REAL)"
        );
        connection.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS roads (" +
            "roadId TEXT PRIMARY KEY, " +
            "fromLocationId TEXT, toLocationId TEXT, " +
            "distance REAL, travelTime INTEGER, " +
            "roadConditionWeight REAL)"
        );
        connection.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS resources (" +
            "resourceId TEXT PRIMARY KEY, " +
            "type TEXT, homeLocation TEXT, " +
            "capacity INTEGER, availabilityStatus TEXT)"
        );
        connection.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS service_requests (" +
            "requestId TEXT PRIMARY KEY, " +
            "source TEXT, destination TEXT, " +
            "category TEXT, urgency TEXT, " +
            "timeSubmitted TEXT, deadline TEXT, status TEXT)"
        );
        importer = new CSVImporter(connection);
        exporter = new CSVExporter(connection);
    }

    /** Test that importAll() runs without throwing any exception. */
    @Test
    public void testImportAllRunsWithoutError() {
        assertDoesNotThrow(() -> importer.importAll(),
            "importAll() should not throw any exception");
    }

    /** Test that at least 50 locations were inserted. */
    @Test
    public void testLocationsImported() throws SQLException {
        importer.importAll();
        ResultSet rs = connection.createStatement()
            .executeQuery("SELECT COUNT(*) AS total FROM locations");
        int count = rs.getInt("total");
        assertTrue(count >= 50,
            "Expected at least 50 locations but got: " + count);
    }

    /** Test that at least 100 roads were inserted. */
    @Test
    public void testRoadsImported() throws SQLException {
        importer.importAll();
        ResultSet rs = connection.createStatement()
            .executeQuery("SELECT COUNT(*) AS total FROM roads");
        int count = rs.getInt("total");
        assertTrue(count >= 100,
            "Expected at least 100 roads but got: " + count);
    }

    /** Test that at least 30 resources were inserted. */
    @Test
    public void testResourcesImported() throws SQLException {
        importer.importAll();
        ResultSet rs = connection.createStatement()
            .executeQuery("SELECT COUNT(*) AS total FROM resources");
        int count = rs.getInt("total");
        assertTrue(count >= 30,
            "Expected at least 30 resources but got: " + count);
    }

    /** Test that at least 300 service requests were inserted. */
    @Test
    public void testServiceRequestsImported() throws SQLException {
        importer.importAll();
        ResultSet rs = connection.createStatement()
            .executeQuery("SELECT COUNT(*) AS total FROM service_requests");
        int count = rs.getInt("total");
        assertTrue(count >= 300,
            "Expected at least 300 service requests but got: " + count);
    }

    /** Test that running importAll() twice does not create duplicates. */
    @Test
    public void testNoDuplicatesOnDoubleImport() throws SQLException {
        importer.importAll();
        int firstCount = getLocationCount();
        importer.importAll();
        int secondCount = getLocationCount();
        assertEquals(firstCount, secondCount,
            "Double import should not create duplicates");
    }

    /** Test that exportAll() runs without throwing any exception. */
    @Test
    public void testExportAllRunsWithoutError() {
        importer.importAll();
        String outputDir = "src/main/resources/data";
        assertDoesNotThrow(() -> exporter.exportAll(outputDir),
            "exportAll() should not throw any exception");
    }

    /** Test that location L001 is the Accident and Emergency Unit. */
    @Test
    public void testSpecificLocationExists() throws SQLException {
        importer.importAll();
        ResultSet rs = connection.createStatement().executeQuery(
            "SELECT name FROM locations WHERE locationId = 'L001'"
        );
        assertTrue(rs.next(), "Location L001 should exist");
        assertEquals("Accident and Emergency Unit", rs.getString("name"),
            "L001 should be the Accident and Emergency Unit");
    }

    private int getLocationCount() throws SQLException {
        ResultSet rs = connection.createStatement()
            .executeQuery("SELECT COUNT(*) AS total FROM locations");
        return rs.getInt("total");
    }
}