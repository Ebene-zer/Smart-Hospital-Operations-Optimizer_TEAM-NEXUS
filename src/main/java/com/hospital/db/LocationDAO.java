package com.hospital.db;

import com.hospital.model.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    public void insert(Location obj) throws SQLException, ValidationException {
        validateRequired(obj);
        if (findById(obj.getLocationId()) != null) {
            throw new ValidationException("Duplicate location_id: " + obj.getLocationId());
        }

        String sql = "INSERT INTO locations (location_id, name, area, type, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, obj.getLocationId());
            statement.setString(2, obj.getName());
            statement.setString(3, obj.getArea());
            statement.setString(4, obj.getType());
            statement.setDouble(5, obj.getLatitude());
            statement.setDouble(6, obj.getLongitude());
            statement.executeUpdate();
        }
    }

    public Location findById(int id) throws SQLException {
        String sql = "SELECT location_id, name, area, type, latitude, longitude FROM locations WHERE location_id = ?";
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

    public List<Location> findAll() throws SQLException {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT location_id, name, area, type, latitude, longitude FROM locations ORDER BY location_id";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                locations.add(mapRow(rs));
            }
        }
        return locations;
    }

    public void update(Location obj) throws SQLException, ValidationException {
        validateRequired(obj);
        String sql = "UPDATE locations SET name = ?, area = ?, type = ?, latitude = ?, longitude = ? WHERE location_id = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getName());
            statement.setString(2, obj.getArea());
            statement.setString(3, obj.getType());
            statement.setDouble(4, obj.getLatitude());
            statement.setDouble(5, obj.getLongitude());
            statement.setInt(6, obj.getLocationId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM locations WHERE location_id = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

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

    private Location mapRow(ResultSet rs) throws SQLException {
        return new Location(
                rs.getInt("location_id"),
                rs.getString("name"),
                rs.getString("area"),
                rs.getString("type"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude"));
    }
}
