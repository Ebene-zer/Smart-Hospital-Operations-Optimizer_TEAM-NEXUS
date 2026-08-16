package com.hospital.db;

import com.opencsv.CSVWriter;

import java.io.*;
import java.sql.*;

public class CSVExporter {

    private final Connection connection;

    public CSVExporter(Connection connection) {
        this.connection = connection;
    }

    public void exportAll(String outputDir) {
        System.out.println("Starting CSV export...");
        exportLocations(outputDir);
        exportRoads(outputDir);
        exportResources(outputDir);
        exportServiceRequests(outputDir);
        System.out.println("CSV export complete. Files saved to: " + outputDir);
    }

    private void exportLocations(String outputDir) {
        String sql = "SELECT * FROM locations";
        String path = outputDir + "/locations_export.csv";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            writer.writeNext(new String[]{
                "locationId","name","area","type","latitude","longitude"
            });
            while (rs.next()) {
                writer.writeNext(new String[]{
                    rs.getString("locationId"),
                    rs.getString("name"),
                    rs.getString("area"),
                    rs.getString("type"),
                    String.valueOf(rs.getDouble("latitude")),
                    String.valueOf(rs.getDouble("longitude"))
                });
            }
            System.out.println("Locations exported to: " + path);
        } catch (IOException | SQLException e) {
            System.err.println("Error exporting locations: " + e.getMessage());
        }
    }

    private void exportRoads(String outputDir) {
        String sql = "SELECT * FROM roads";
        String path = outputDir + "/roads_export.csv";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            writer.writeNext(new String[]{
                "roadId","fromLocationId","toLocationId",
                "distance","travelTime","roadConditionWeight"
            });
            while (rs.next()) {
                writer.writeNext(new String[]{
                    rs.getString("roadId"),
                    rs.getString("fromLocationId"),
                    rs.getString("toLocationId"),
                    String.valueOf(rs.getDouble("distance")),
                    String.valueOf(rs.getInt("travelTime")),
                    String.valueOf(rs.getDouble("roadConditionWeight"))
                });
            }
            System.out.println("Roads exported to: " + path);
        } catch (IOException | SQLException e) {
            System.err.println("Error exporting roads: " + e.getMessage());
        }
    }

    private void exportResources(String outputDir) {
        String sql = "SELECT * FROM resources";
        String path = outputDir + "/resources_export.csv";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            writer.writeNext(new String[]{
                "resourceId","type","homeLocation","capacity","availabilityStatus"
            });
            while (rs.next()) {
                writer.writeNext(new String[]{
                    rs.getString("resourceId"),
                    rs.getString("type"),
                    rs.getString("homeLocation"),
                    String.valueOf(rs.getInt("capacity")),
                    rs.getString("availabilityStatus")
                });
            }
            System.out.println("Resources exported to: " + path);
        } catch (IOException | SQLException e) {
            System.err.println("Error exporting resources: " + e.getMessage());
        }
    }

    private void exportServiceRequests(String outputDir) {
        String sql = "SELECT * FROM service_requests";
        String path = outputDir + "/service_requests_export.csv";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
             CSVWriter writer = new CSVWriter(new FileWriter(path))) {
            writer.writeNext(new String[]{
                "requestId","source","destination","category",
                "urgency","timeSubmitted","deadline","status"
            });
            while (rs.next()) {
                writer.writeNext(new String[]{
                    rs.getString("requestId"),
                    rs.getString("source"),
                    rs.getString("destination"),
                    rs.getString("category"),
                    rs.getString("urgency"),
                    rs.getString("timeSubmitted"),
                    rs.getString("deadline"),
                    rs.getString("status")
                });
            }
            System.out.println("Service requests exported to: " + path);
        } catch (IOException | SQLException e) {
            System.err.println("Error exporting service requests: " + e.getMessage());
        }
    }
}
