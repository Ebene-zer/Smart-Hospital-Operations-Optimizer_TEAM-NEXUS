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
            throw new ValidationException("Duplicate road_id: " + obj.getRoadId());
        }
        ensureLocationExists(obj.getFromLocationId(), "from_location_id");
        ensureLocationExists(obj.getToLocationId(), "to_location_id");

        String sql = "INSERT INTO roads (road_id, from_location_id, to_location_id, distance_km, travel_time_min, road_condition_weight) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, obj.getRoadId());
            statement.setInt(2, obj.getFromLocationId());
            statement.setInt(3, obj.getToLocationId());
            statement.setDouble(4, obj.getDistanceKm());
            statement.setDouble(5, obj.getTravelTimeMin());
            statement.setDouble(6, obj.getRoadConditionWeight());
            statement.executeUpdate();
        }
    }

    public Road findById(int id) throws SQLException {
        String sql = "SELECT road_id, from_location_id, to_location_id, distance_km, travel_time_min, road_condition_weight FROM roads WHERE road_id = ?";
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

    public List<Road> findAll() throws SQLException {
        List<Road> roads = new ArrayList<>();
        String sql = "SELECT road_id, from_location_id, to_location_id, distance_km, travel_time_min, road_condition_weight FROM roads ORDER BY road_id";
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
        ensureLocationExists(obj.getFromLocationId(), "from_location_id");
        ensureLocationExists(obj.getToLocationId(), "to_location_id");
        String sql = "UPDATE roads SET from_location_id = ?, to_location_id = ?, distance_km = ?, travel_time_min = ?, road_condition_weight = ? WHERE road_id = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, obj.getFromLocationId());
            statement.setInt(2, obj.getToLocationId());
            statement.setDouble(3, obj.getDistanceKm());
            statement.setDouble(4, obj.getTravelTimeMin());
            statement.setDouble(5, obj.getRoadConditionWeight());
            statement.setInt(6, obj.getRoadId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM roads WHERE road_id = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private void validateRequired(Road obj) throws ValidationException {
        if (obj == null) {
            throw new ValidationException("Road cannot be null");
        }
        if (obj.getFromLocationId() <= 0 || obj.getToLocationId() <= 0) {
            throw new ValidationException("Road foreign keys must be positive integers");
        }
    }

    private void ensureLocationExists(int locationId, String columnName) throws SQLException, ValidationException {
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

    private Road mapRow(ResultSet rs) throws SQLException {
        return new Road(
                rs.getInt("road_id"),
                rs.getInt("from_location_id"),
                rs.getInt("to_location_id"),
                rs.getDouble("distance_km"),
                rs.getDouble("travel_time_min"),
                rs.getDouble("road_condition_weight"));
    }
}
