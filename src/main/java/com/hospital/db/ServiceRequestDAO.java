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

/**
 * ServiceRequestDAO - Team 1
 * Handles all database operations for the service_requests table.
 * Audit events are logged after every insert, update and delete.
 */
public class ServiceRequestDAO {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // AuditEventDAO used to log all data changes
    private final AuditEventDAO auditEventDAO = new AuditEventDAO();

    /**
     * Inserts a new service request into the database.
     * Logs an INSERT audit event on success.
     */
    public void insert(ServiceRequest obj) throws SQLException, ValidationException {
        validateRequired(obj);
        validateDates(obj);
        if (findById(obj.getRequestId()) != null) {
            throw new ValidationException("Duplicate requestId: " + obj.getRequestId());
        }
        ensureLocationExists(obj.getSourceId(), "source");
        ensureLocationExists(obj.getDestinationId(), "destination");

        String sql = "INSERT INTO service_requests (requestId, source, destination, " +
                     "category, urgency, timeSubmitted, deadline, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getRequestId());
            statement.setString(2, obj.getSourceId());
            statement.setString(3, obj.getDestinationId());
            statement.setString(4, obj.getCategory());
            statement.setString(5, obj.getUrgency());
            statement.setString(6, obj.getTimeSubmitted());
            statement.setString(7, obj.getDeadline());
            statement.setString(8, obj.getStatus());
            statement.executeUpdate();
        }

        // log audit event after successful insert
        auditEventDAO.insert("INSERT", "service_requests", obj.getRequestId(),
                "system", LocalDateTime.now().toString());
    }

    /**
     * Finds a service request by its ID.
     * Returns null if not found.
     */
    public ServiceRequest findById(String id) throws SQLException {
        String sql = "SELECT requestId, source, destination, category, urgency, " +
                     "timeSubmitted, deadline, status FROM service_requests WHERE requestId = ?";
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
     * Returns all service requests ordered by requestId.
     */
    public List<ServiceRequest> findAll() throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT requestId, source, destination, category, urgency, " +
                     "timeSubmitted, deadline, status FROM service_requests ORDER BY requestId";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                requests.add(mapRow(rs));
            }
        }
        return requests;
    }

    /**
     * Updates an existing service request in the database.
     * Logs an UPDATE audit event on success.
     */
    public void update(ServiceRequest obj) throws SQLException, ValidationException {
        validateRequired(obj);
        validateDates(obj);
        ensureLocationExists(obj.getSourceId(), "source");
        ensureLocationExists(obj.getDestinationId(), "destination");
        String sql = "UPDATE service_requests SET source = ?, destination = ?, " +
                     "category = ?, urgency = ?, timeSubmitted = ?, deadline = ?, " +
                     "status = ? WHERE requestId = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getSourceId());
            statement.setString(2, obj.getDestinationId());
            statement.setString(3, obj.getCategory());
            statement.setString(4, obj.getUrgency());
            statement.setString(5, obj.getTimeSubmitted());
            statement.setString(6, obj.getDeadline());
            statement.setString(7, obj.getStatus());
            statement.setString(8, obj.getRequestId());
            statement.executeUpdate();
        }

        // log audit event after successful update
        auditEventDAO.insert("UPDATE", "service_requests", obj.getRequestId(),
                "system", LocalDateTime.now().toString());
    }

    /**
     * Deletes a service request by ID from the database.
     * Logs a DELETE audit event on success.
     */
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM service_requests WHERE requestId = ?";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }

        // log audit event after successful delete
        auditEventDAO.insert("DELETE", "service_requests", id,
                "system", LocalDateTime.now().toString());
    }

    private void validateRequired(ServiceRequest obj) throws ValidationException {
        if (obj == null) {
            throw new ValidationException("ServiceRequest cannot be null");
        }
        if (isBlank(obj.getCategory()) || isBlank(obj.getTimeSubmitted()) ||
            isBlank(obj.getDeadline()) || isBlank(obj.getStatus())) {
            throw new ValidationException(