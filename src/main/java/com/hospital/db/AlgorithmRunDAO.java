package com.hospital.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AlgorithmRunDAO {

    public void insert(String algorithmName, int inputSize, long timeNs, int memoryKb, String dateRun)
            throws SQLException {
        String sql = "INSERT INTO algorithm_runs (algorithm_name, input_size, time_ns, memory_kb, date_run) VALUES (?, ?, ?, ?, ?)";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, algorithmName);
            statement.setInt(2, inputSize);
            statement.setLong(3, timeNs);
            statement.setInt(4, memoryKb);
            statement.setString(5, dateRun);
            statement.executeUpdate();
        }
    }

    public List<Map<String, Object>> findAll() throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        String sql = "SELECT run_id, algorithm_name, input_size, time_ns, memory_kb, date_run FROM algorithm_runs ORDER BY run_id";
        Connection connection = DBConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("run_id", rs.getInt("run_id"));
                row.put("algorithm_name", rs.getString("algorithm_name"));
                row.put("input_size", rs.getInt("input_size"));
                row.put("time_ns", rs.getLong("time_ns"));
                row.put("memory_kb", rs.getInt("memory_kb"));
                row.put("date_run", rs.getString("date_run"));
                rows.add(row);
            }
        }
        return rows;
    }
}
