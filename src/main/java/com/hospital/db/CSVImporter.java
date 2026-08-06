package com.hospital.db;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.sql.*;

public class CSVImporter {

    private final Connection connection;

    public CSVImporter(Connection connection) {
        this.connection = connection;
    }

    public void importAll() {
        System.out.println("Starting CSV import...");
        importLocations();
        importRoads();
        importResources();
        importServiceRequests();
        System.out.println("CSV import complete.");
    }

    private void importLocations() {
        String sql = "INSERT OR IGNORE INTO locations " +
                "(locationId, name, area, type, latitude, longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String path = "src/main/resources/data/locations.csv";
        int count = 0;
        try (CSVReader reader = new CSVReader(new FileReader(path));
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            String[] row;
            reader.readNext(); // skip header
            while ((row = reader.readNext()) != null) {
                stmt.setString(1, row[0].trim());
                stmt.setString(2, row[1].trim());
                stmt.setString(3, row[2].trim());
                stmt.setString(4, row[3].trim());
                stmt.setDouble(5, Double.parseDouble(row[4].trim()));
                stmt.setDouble(6, Double.parseDouble(row[5].trim().replace("\u2212", "-")));
                stmt.executeUpdate();
                count++;
            }
            System.out.println("Locations imported: " + count);
        } catch (IOException | SQLException | CsvValidationException e) {
            System.err.println("Error importing locations: " + e.getMessage());
        }
    }

    private void importRoads() {
        String sql = "INSERT OR IGNORE INTO roads " +
                "(roadId, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String path = "src/main/resources/data/roads.csv";
        int count = 0;
        try (CSVReader reader = new CSVReader(new FileReader(path));
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            String[] row;
            reader.readNext();
            while ((row = reader.readNext()) != null) {
                stmt.setString(1, row[0].trim());
                stmt.setString(2, row[1].trim());
                stmt.setString(3, row[2].trim());
                stmt.setDouble(4, Double.parseDouble(row[3].trim()));
                stmt.setInt(5, Integer.parseInt(row[4].trim()));
                stmt.setDouble(6, Double.parseDouble(row[5].trim()));
                stmt.executeUpdate();
                count++;
            }
            System.out.println("Roads imported: " + count);
        } catch (IOException | SQLException | CsvValidationException e) {
            System.err.println("Error importing roads: " + e.getMessage());
        }
    }

    private void importResources() {
        String sql = "INSERT OR IGNORE INTO resources " +
                "(resourceId, type, homeLocation, capacity, availabilityStatus) " +
                "VALUES (?, ?, ?, ?, ?)";
        String path = "src/main/resources/data/resources.csv";
        int count = 0;
        try (CSVReader reader = new CSVReader(new FileReader(path));
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            String[] row;
            reader.readNext();
            while ((row = reader.readNext()) != null) {
                stmt.setString(1, row[0].trim());
                stmt.setString(2, row[1].trim());
                stmt.setString(3, row[2].trim());
                stmt.setInt(4, Integer.parseInt(row[3].trim()));
                stmt.setString(5, row[4].trim());
                stmt.executeUpdate();
                count++;
            }
            System.out.println("Resources imported: " + count);
        } catch (IOException | SQLException | CsvValidationException e) {
            System.err.println("Error importing resources: " + e.getMessage());
        }
    }

    private void importServiceRequests() {
        String sql = "INSERT OR IGNORE INTO service_requests " +
                "(requestId, source, destination, category, urgency, " +
                "timeSubmitted, deadline, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String path = "src/main/resources/data/service_requests.csv";
        int count = 0;
        try (CSVReader reader = new CSVReader(new FileReader(path));
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            String[] row;
            reader.readNext();
            while ((row = reader.readNext()) != null) {
                stmt.setString(1, row[0].trim());
                stmt.setString(2, row[1].trim());
                stmt.setString(3, row[2].trim());
                stmt.setString(4, row[3].trim());
                stmt.setString(5, row[4].trim());
                stmt.setString(6, row[5].trim());
                stmt.setString(7, row[6].trim());
                stmt.setString(8, row[7].trim());
                stmt.executeUpdate();
                count++;
            }
            System.out.println("Service requests imported: " + count);
        } catch (IOException | SQLException | CsvValidationException e) {
            System.err.println("Error importing service requests: " + e.getMessage());
        }
    }
}