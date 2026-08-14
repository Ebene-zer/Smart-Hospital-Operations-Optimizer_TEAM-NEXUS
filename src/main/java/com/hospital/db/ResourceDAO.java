package com.hospital.db;

import com.hospital.model.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {

    public void insert(Resource obj) throws SQLException, ValidationException {
        validateRequired(obj);
        if (findById(obj.getResourceId()) != null) {
            throw new ValidationException("Duplicate resourceId: " + obj.getResourceId());
        }
        ensureLocationExists(obj.getHomeLocationId(), "homeLocation");

        String sql = "INSERT INTO resources (resourceId, type, homeLocation, capacity, availabilityStatus) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getResourceId());
            statement.setString(2, obj.getType());
            statement.setString(3, obj.getHomeLocationId());
            statement.setInt(4, obj.getCapacity());
            statement.setString(5, obj.getAvailabilityStatus());
            statement.executeUpdate();
        }
    }

    public Resource findById(String id) throws SQLException {
        String sql = "SELECT resourceId, type, homeLocation, capacity, availabilityStatus FROM resources WHERE resourceId = ?";
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

    public List<Resource> findAll() throws SQLException {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT resourceId, type, homeLocation, capacity, availabilityStatus FROM resources ORDER BY resourceId";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                resources.add(mapRow(rs));
            }
        }
        return resources;
    }

    public void update(Resource obj) throws SQLException, ValidationException {
        validateRequired(obj);
        ensureLocationExists(obj.getHomeLocationId(), "homeLocation");
        String sql = "UPDATE resources SET type = ?, homeLocation = ?, capacity = ?, availabilityStatus = ? WHERE resourceId = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getType());
            statement.setString(2, obj.getHomeLocationId());
            statement.setInt(3, obj.getCapacity());
            statement.setString(4, obj.getAvailabilityStatus());
            statement.setString(5, obj.getResourceId());
            statement.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM resources WHERE resourceId = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
    }

    private void validateRequired(Resource obj) throws ValidationException {
        if (obj == null) {
            throw new ValidationException("Resource cannot be null");
        }
        if (isBlank(obj.getType()) || isBlank(obj.getAvailabilityStatus())) {
            throw new ValidationException("Type and availabilityStatus are required");
        }
        if (obj.getCapacity() == null) {
            throw new ValidationException("Capacity is required");
        }
    }

    private void ensureLocationExists(String locationId, String columnName) throws SQLException, ValidationException {
        if (isBlank(locationId)) {
            throw new ValidationException(columnName + " is required");
        }
        String sql = "SELECT 1 FROM locations WHERE locationId = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, locationId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    throw new ValidationException(
                            "Foreign key not found in locations for " + columnName + ": " + locationId);
                }
            }
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private Resource mapRow(ResultSet rs) throws SQLException {
        return new Resource(
                rs.getString("resourceId"),
                rs.getString("type"),
                rs.getString("homeLocation"),
                rs.getObject("capacity") == null ? null : rs.getInt("capacity"),
                rs.getString("availabilityStatus"));
    }
}
