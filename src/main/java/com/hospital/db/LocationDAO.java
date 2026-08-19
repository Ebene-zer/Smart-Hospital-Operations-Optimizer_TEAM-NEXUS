package com.hospital.db;

import com.hospital.model.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * LocationDAO - Team 1
 * Handles all database operations for the locations table.
 * Audit events are logged after every insert, update and delete.
 */
public class LocationDAO {

    // AuditEventDAO used to log all data changes
    private final AuditEventDAO auditEventDAO = new AuditEventDAO();

    /**
     * Inserts a new location into the database.
     * Logs an INSERT audit event on success.
     */
    public void insert(Location obj) throws SQLException, ValidationException {
        validateRequired(obj);
        if (findById(obj.getLocationId()) != null) {
            throw new ValidationException("Duplicate locationId: " + obj.getLocationId());
        }

        String sql = "INSERT INTO locations (locationId, name, area, type, latitude, longitude) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getLocationId());
            statement.setString(2, obj.getName());
            statement.setString(3, obj.getArea());
            statement.setString(4, obj.getType());
            statement.setDouble(5, obj.getLatitude());
            statement.setDouble(6, obj.getLongitude());
            statement.executeUpdate();
        }

        // log audit event after successful insert
        auditEventDAO.insert("INSERT", "locations", obj.getLocationId(),
                "system", LocalDateTime.now().toString());
    }

    /**
     * Finds a location by its ID.
     * Returns null if not found.
     */
    public Location findById(String id) throws SQLException {
        String sql = "SELECT locationId, name, area, type, latitude, longitude " +
                     "FROM locations WHERE locationId = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    /**
     * Returns all locations ordered by locationId.
     */
    public List<Location> findAll() throws SQLException {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT locationId, name, area, type, latitude, longitude " +
                     "FROM locations ORDER BY locationId";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                locations.add(mapRow(rs));
            }
        }
        return locations;
    }

    /**
     * Updates an existing location in the database.
     * Logs an UPDATE audit event on success.
     */
    public void update(Location obj) throws SQLException, ValidationException {
        validateRequired(obj);
        String sql = "UPDATE locations SET name = ?, area = ?, type = ?, " +
                     "latitude = ?, longitude = ? WHERE locationId = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getArea());
            statement.setString(3, obj.getType());
            statement.setDouble(4, obj.getLatitude());
            statement.setDouble(5, obj.getLongitude());
            statement.setString(6, obj.getLocationId());
            statement.executeUpdate();
        }

        // log audit event after successful update
        auditEventDAO.insert("UPDATE", "locations", obj.getLocationId(),
                "system", LocalDateTime.now().toString());
    }

    /**
     * Deletes a location by ID from the database.
     * Logs a DELETE audit event on success.
     */
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM locations WHERE locationId = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }

        // log audit event after successful delete
        auditEventDAO.insert("DELETE", "locations", id,
                "system", LocalDateTime.now().toString());
    }

    /**
     * Validates that required fields are present before insert/update.
     */
    private void validateRequired(Location obj) throws ValidationException {
        if (obj == null) {
            throw new ValidationException("Location cannot be null");
        }
        if (isBlank(obj.getName()) || isBlank(obj.getArea()) || isBlank(obj.getType())) {
            throw new ValidationException("Location name, area and type are required");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Maps a ResultSet row to a Location object.
     */
    private Location mapRow(ResultSet rs) throws SQLException {
        return new Location(
                rs.getString("locationId"),
                rs.getString("name"),
                rs.getString("area"),
                rs.getString("type"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"));
    }
}