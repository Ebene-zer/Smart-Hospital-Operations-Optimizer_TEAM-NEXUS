package com.hospital.db;

import com.hospital.model.ServiceRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestDAO {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void insert(ServiceRequest obj) throws SQLException, ValidationException {
        validateRequired(obj);
        validateDates(obj);
        if (findById(obj.getRequestId()) != null) {
            throw new ValidationException("Duplicate request_id: " + obj.getRequestId());
        }
        ensureLocationExists(obj.getSourceId(), "source_id");
        ensureLocationExists(obj.getDestinationId(), "destination_id");

        String sql = "INSERT INTO service_requests (request_id, source_id, destination_id, category, urgency, time_submitted, deadline, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, obj.getRequestId());
            statement.setObject(2, obj.getSourceId());
            statement.setObject(3, obj.getDestinationId());
            statement.setString(4, obj.getCategory());
            statement.setInt(5, obj.getUrgency());
            statement.setString(6, obj.getTimeSubmitted());
            statement.setString(7, obj.getDeadline());
            statement.setString(8, obj.getStatus());
            statement.executeUpdate();
        }
    }

    public ServiceRequest findById(int id) throws SQLException {
        String sql = "SELECT request_id, source_id, destination_id, category, urgency, time_submitted, deadline, status FROM service_requests WHERE request_id = ?";
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

    public List<ServiceRequest> findAll() throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT request_id, source_id, destination_id, category, urgency, time_submitted, deadline, status FROM service_requests ORDER BY request_id";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                requests.add(mapRow(rs));
            }
        }
        return requests;
    }

    public void update(ServiceRequest obj) throws SQLException, ValidationException {
        validateRequired(obj);
        validateDates(obj);
        ensureLocationExists(obj.getSourceId(), "source_id");
        ensureLocationExists(obj.getDestinationId(), "destination_id");
        String sql = "UPDATE service_requests SET source_id = ?, destination_id = ?, category = ?, urgency = ?, time_submitted = ?, deadline = ?, status = ? WHERE request_id = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, obj.getSourceId());
            statement.setObject(2, obj.getDestinationId());
            statement.setString(3, obj.getCategory());
            statement.setInt(4, obj.getUrgency());
            statement.setString(5, obj.getTimeSubmitted());
            statement.setString(6, obj.getDeadline());
            statement.setString(7, obj.getStatus());
            statement.setInt(8, obj.getRequestId());
            statement.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM service_requests WHERE request_id = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private void validateRequired(ServiceRequest obj) throws ValidationException {
        if (obj == null) {
            throw new ValidationException("ServiceRequest cannot be null");
        }
        if (isBlank(obj.getCategory()) || isBlank(obj.getTimeSubmitted()) || isBlank(obj.getDeadline())
                || isBlank(obj.getStatus())) {
            throw new ValidationException("Category, time_submitted, deadline and status are required");
        }
        if (obj.getUrgency() == null) {
            throw new ValidationException("Urgency is required");
        }
    }

    private void validateDates(ServiceRequest obj) throws ValidationException {
        try {
            LocalDateTime submitted = LocalDateTime.parse(obj.getTimeSubmitted(), FORMATTER);
            LocalDateTime deadline = LocalDateTime.parse(obj.getDeadline(), FORMATTER);
            if (deadline.isBefore(submitted)) {
                throw new ValidationException("deadline must not be before time_submitted");
            }
        } catch (DateTimeParseException e) {
            throw new ValidationException("Invalid date format: " + e.getMessage());
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

    private ServiceRequest mapRow(ResultSet rs) throws SQLException {
        return new ServiceRequest(
                rs.getInt("request_id"),
                rs.getObject("source_id") == null ? null : rs.getInt("source_id"),
                rs.getObject("destination_id") == null ? null : rs.getInt("destination_id"),
                rs.getString("category"),
                rs.getObject("urgency") == null ? null : rs.getInt("urgency"),
                rs.getString("time_submitted"),
                rs.getString("deadline"),
                rs.getString("status"));
    }
}
