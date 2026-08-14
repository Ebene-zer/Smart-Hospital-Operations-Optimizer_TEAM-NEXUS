package com.hospital.db;

import com.hospital.model.Road;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoadDAO {

    public void insert(Road obj) throws SQLException, ValidationException {
        validateRequired(obj);
        if (findById(obj.getRoadId()) != null) {
            throw new ValidationException("Duplicate roadId: " + obj.getRoadId());
        }
        ensureLocationExists(obj.getFromLocationId(), "fromLocationId");
        ensureLocationExists(obj.getToLocationId(), "toLocationId");

        String sql = "INSERT INTO roads (roadId, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getRoadId());
            statement.setString(2, obj.getFromLocationId());
            statement.setString(3, obj.getToLocationId());
            statement.setDouble(4, obj.getDistanceKm());
            statement.setDouble(5, obj.getTravelTimeMin());
            statement.setDouble(6, obj.getRoadConditionWeight());
            statement.executeUpdate();
        }
    }

    public Road findById(String id) throws SQLException {
        String sql = "SELECT roadId, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight FROM roads WHERE roadId = ?";
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

    public List<Road> findAll() throws SQLException {
        List<Road> roads = new ArrayList<>();
        String sql = "SELECT roadId, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight FROM roads ORDER BY roadId";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                roads.add(mapRow(rs));
            }
        }
        return roads;
    }

    public void update(Road obj) throws SQLException, ValidationException {
        validateRequired(obj);
        ensureLocationExists(obj.getFromLocationId(), "fromLocationId");
        ensureLocationExists(obj.getToLocationId(), "toLocationId");
        String sql = "UPDATE roads SET fromLocationId = ?, toLocationId = ?, distance = ?, travelTime = ?, roadConditionWeight = ? WHERE roadId = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getFromLocationId());
            statement.setString(2, obj.getToLocationId());
            statement.setDouble(3, obj.getDistanceKm());
            statement.setDouble(4, obj.getTravelTimeMin());
            statement.setDouble(5, obj.getRoadConditionWeight());
            statement.setString(6, obj.getRoadId());
            statement.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM roads WHERE roadId = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
    }

    private void validateRequired(Road obj) throws ValidationException {
        if (obj == null) {
            throw new ValidationException("Road cannot be null");
        }
        if (isBlank(obj.getRoadId()) || isBlank(obj.getFromLocationId()) || isBlank(obj.getToLocationId())) {
            throw new ValidationException("Road IDs are required");
        }
    }

    private void ensureLocationExists(String locationId, String columnName) throws SQLException, ValidationException {
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

    private Road mapRow(ResultSet rs) throws SQLException {
        return new Road(
                rs.getString("roadId"),
                rs.getString("fromLocationId"),
                rs.getString("toLocationId"),
                rs.getDouble("distance"),
                rs.getDouble("travelTime"),
                rs.getDouble("roadConditionWeight"));
    }
}
