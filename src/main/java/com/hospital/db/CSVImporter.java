package com.hospital.db;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        String sql = "INSERT OR IGNORE INTO locations "
                + "(locationId, name, area, type, latitude, longitude) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        int count = 0;
        try (Reader file = openCsv("locations.csv");
             CSVReader reader = new CSVReader(file);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            String[] row;
            reader.readNext();
            while ((row = reader.readNext()) != null) {
                if (isBlankRow(row)) {
                    continue;
                }
                stmt.setString(1, row[0].trim());
                stmt.setString(2, row[1].trim());
                stmt.setString(3, row[2].trim());
                stmt.setString(4, row[3].trim());
                stmt.setDouble(5, parseDouble(row[4]));
                stmt.setDouble(6, parseDouble(row[5]));
                stmt.executeUpdate();
                count++;
            }
            System.out.println("Locations imported: " + count);
        } catch (IOException | SQLException | CsvValidationException e) {
            System.err.println("Error importing locations: " + e.getMessage());
        }
    }

    private void importRoads() {
        String sql = "INSERT OR IGNORE INTO roads "
                + "(roadId, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        int count = 0;
        int skipped = 0;
        try (Reader file = openCsv("roads.csv");
             CSVReader reader = new CSVReader(file);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            String[] row;
            reader.readNext();
            while ((row = reader.readNext()) != null) {
                if (isBlankRow(row)) {
                    continue;
                }
                String from = row[1].trim();
                String to = row[2].trim();
                if (!locationExists(from) || !locationExists(to)) {
                    skipped++;
                    continue;
                }
                stmt.setString(1, row[0].trim());
                stmt.setString(2, from);
                stmt.setString(3, to);
                stmt.setDouble(4, parseDouble(row[3]));
                stmt.setInt(5, (int) parseDouble(row[4]));
                stmt.setDouble(6, parseDouble(row[5]));
                stmt.executeUpdate();
                count++;
            }
            System.out.println("Roads imported: " + count + (skipped == 0 ? "" : " (skipped orphan FKs: " + skipped + ")"));
        } catch (IOException | SQLException | CsvValidationException e) {
            System.err.println("Error importing roads: " + e.getMessage());
        }
    }

    private void importResources() {
        String sql = "INSERT OR IGNORE INTO resources "
                + "(resourceId, type, homeLocation, capacity, availabilityStatus) "
                + "VALUES (?, ?, ?, ?, ?)";
        int count = 0;
        int skipped = 0;
        try (Reader file = openCsv("resources.csv");
             CSVReader reader = new CSVReader(file);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            String[] row;
            reader.readNext();
            while ((row = reader.readNext()) != null) {
                if (isBlankRow(row)) {
                    continue;
                }
                String home = row[2].trim();
                if (!locationExists(home)) {
                    skipped++;
                    continue;
                }
                stmt.setString(1, row[0].trim());
                stmt.setString(2, row[1].trim());
                stmt.setString(3, home);
                stmt.setInt(4, (int) parseDouble(row[3]));
                stmt.setString(5, row[4].trim());
                stmt.executeUpdate();
                count++;
            }
            System.out.println("Resources imported: " + count + (skipped == 0 ? "" : " (skipped orphan FKs: " + skipped + ")"));
        } catch (IOException | SQLException | CsvValidationException e) {
            System.err.println("Error importing resources: " + e.getMessage());
        }
    }

    private void importServiceRequests() {
        String sql = "INSERT OR IGNORE INTO service_requests "
                + "(requestId, source, destination, category, urgency, "
                + "timeSubmitted, deadline, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int count = 0;
        int skipped = 0;
        try (Reader file = openCsv("service_requests.csv");
             CSVReader reader = new CSVReader(file);
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            String[] row;
            reader.readNext();
            while ((row = reader.readNext()) != null) {
                if (isBlankRow(row)) {
                    continue;
                }
                String source = row[1].trim();
                String destination = row[2].trim();
                if (!locationExists(source) || !locationExists(destination)) {
                    skipped++;
                    continue;
                }
                stmt.setString(1, row[0].trim());
                stmt.setString(2, source);
                stmt.setString(3, destination);
                stmt.setString(4, row[3].trim());
                stmt.setString(5, row[4].trim());
                stmt.setString(6, row[5].trim());
                stmt.setString(7, row[6].trim());
                stmt.setString(8, row[7].trim());
                stmt.executeUpdate();
                count++;
            }
            System.out.println("Service requests imported: " + count + (skipped == 0 ? "" : " (skipped orphan FKs: " + skipped + ")"));
        } catch (IOException | SQLException | CsvValidationException e) {
            System.err.println("Error importing service requests: " + e.getMessage());
        }
    }

    private boolean locationExists(String locationId) throws SQLException {
        String sql = "SELECT 1 FROM locations WHERE locationId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, locationId);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

    static Reader openCsv(String fileName) throws IOException {
        InputStream stream = CSVImporter.class.getClassLoader().getResourceAsStream("data/" + fileName);
        if (stream != null) {
            return new InputStreamReader(stream, StandardCharsets.UTF_8);
        }
        Path local = Path.of("src", "main", "resources", "data", fileName);
        if (Files.exists(local)) {
            return Files.newBufferedReader(local, StandardCharsets.UTF_8);
        }
        return new FileReader(fileName);
    }

    private static boolean isBlankRow(String[] row) {
        return row == null || row.length == 0 || row[0] == null || row[0].isBlank();
    }

    static double parseDouble(String raw) {
        return Double.parseDouble(raw.trim().replace("\u2212", "-").replace(",", ""));
    }
}
