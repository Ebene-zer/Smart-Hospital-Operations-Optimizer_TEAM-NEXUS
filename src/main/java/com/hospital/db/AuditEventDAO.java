package com.hospital.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AuditEventDAO {

    public void insert(String eventType, String entityTable, String entityId, String performedBy, String timestamp)
            throws SQLException {
        String sql = "INSERT INTO audit_events (event_type, entity_table, entity_id, performed_by, timestamp) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, eventType);
            statement.setString(2, entityTable);
            statement.setString(3, entityId);
            statement.setString(4, performedBy);
            statement.setString(5, timestamp);
            statement.executeUpdate();
        }
    }

    public List<Map<String, Object>> findAll() throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = "SELECT event_id, event_type, entity_table, entity_id, performed_by, timestamp FROM audit_events ORDER BY event_id";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("event_id", rs.getInt("event_id"));
                row.put("event_type", rs.getString("event_type"));
                row.put("entity_table", rs.getString("entity_table"));
                row.put("entity_id", rs.getString("entity_id"));
                row.put("performed_by", rs.getString("performed_by"));
                row.put("timestamp", rs.getString("timestamp"));
                rows.add(row);
            }
        }
        return rows;
    }
}
