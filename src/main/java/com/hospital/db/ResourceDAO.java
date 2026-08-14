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
            throw new ValidationException("Duplicate resource_id: " + obj.getResourceId());
        }
        ensureLocationExists(obj.getHomeLocationId(), "home_location_id");

        String sql = "INSERT INTO resources (resource_id, type, home_location_id, capacity, availability_status) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, obj.getResourceId());
            statement.setString(2, obj.getType());
            statement.setObject(3, obj.getHomeLocationId());
            statement.setInt(4, obj.getCapacity());
            statement.setString(5, obj.getAvailabilityStatus());
            statement.executeUpdate();
        }
    }

    public Resource findById(int id) throws SQLException {
        String sql = "SELECT resource_id, type, home_location_id, capacity, availability_status FROM resources WHERE resource_id = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
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
        String sql = "SELECT resource_id, type, home_location_id, capacity, availability_status FROM resources ORDER BY resource_id";
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
        ensureLocationExists(obj.getHomeLocationId(), "home_location_id");
        String sql = "UPDATE resources SET type = ?, home_location_id = ?, capacity = ?, availability_status = ? WHERE resource_id = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getType());
            statement.setObject(2, obj.getHomeLocationId());
            statement.setInt(3, obj.getCapacity());
            statement.setString(4, obj.getAvailabilityStatus());
            statement.setInt(5, obj.getResourceId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM resources WHERE resource_id = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private void validateRequired(Resource obj) throws ValidationException {
        if (obj == null) {
            throw new ValidationException("Resource cannot be null");
        }
        if (isBlank(obj.getType()) || isBlank(obj.getAvailabilityStatus())) {
            throw new ValidationException("Type and availability_status are required");
        }
        if (obj.getCapacity() == null) {
            throw new ValidationException("Capacity is required");
        }
    }

    private void ensureLocationExists(Integer locationId, String columnName) throws SQLException, ValidationException {
        if (locationId == null) {
            throw new ValidationException(columnName + " is required");
        }
        String sql = "SELECT 1 FROM locations WHERE location_id = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, locationId);
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
                rs.getInt("resource_id"),
                rs.getString("type"),
                rs.getObject("home_location_id") == null ? null : rs.getInt("home_location_id"),
                rs.getObject("capacity") == null ? null : rs.getInt("capacity"),
                rs.getString("availability_status"));
    }
}
