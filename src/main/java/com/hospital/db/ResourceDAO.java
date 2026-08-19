package com.hospital.db;

import com.hospital.model.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ResourceDAO - Team 1
 * Handles all database operations for the resources table.
 * Audit events are logged after every insert, update and delete.
 */
public class ResourceDAO {

    // AuditEventDAO used to log all data changes
    private final AuditEventDAO auditEventDAO = new AuditEventDAO();

    /**
     * Inserts a new resource into the database.
     * Logs an INSERT audit event on success.
     */
    public void insert(Resource obj) throws SQLException, ValidationException {
        validateRequired(obj);
        if (findById(obj.getResourceId()) != null) {
            throw new ValidationException("Duplicate resourceId: " + obj.getResourceId());
        }
        ensureLocationExists(obj.getHomeLocationId(), "homeLocation");

        String sql = "INSERT INTO resources (resourceId, type, homeLocation, capacity, availabilityStatus) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, obj.getResourceId());
            statement.setString(2, obj.getType());
            statement.setString(3, obj.getHomeLocationId());
            statement.setInt(4, obj.getCapacity());
            statement.setString(5, obj.getAvailabilityStatus());
            statement.executeUpdate();
        }

        // log audit event after successful insert
        auditEventDAO.insert("INSERT", "resources", obj.getResourceId(),
                "system", LocalDateTime.now().toString());
    }

    /**
     * Finds a resource by its ID.
     * Returns null if not found.
     */
    public Resource findById(String id) throws SQLException {
        String sql = "SELECT resourceId, type, homeLocation, capacity, availabilityStatus " +
                     "FROM resources WHERE resourceId = ?";
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
     * Returns all