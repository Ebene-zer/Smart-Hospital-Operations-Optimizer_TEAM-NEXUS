package com.hospital.db;

import com.hospital.model.Patient;
import com.hospital.util.ValidationException;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientDAO {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public List<Patient> findAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                patients.add(mapRowToPatient(rs));
            }
        }
        return patients;
    }

    public Patient findById(String patientId) throws SQLException {
        String sql = "SELECT * FROM patients WHERE patient_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToPatient(rs);
                }
            }
        }
        return null;
    }

    public void insert(Patient patient) throws SQLException, ValidationException {
        validatePatient(patient);
        String sql = "INSERT INTO patients(patient_id, name, nhis_number, birth_date, ward_location_id, allergies) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patient.getPatientId());
            pstmt.setString(2, patient.getName());
            pstmt.setString(3, patient.getNhisNumber());
            pstmt.setString(4, DATE_FORMAT.format(patient.getBirthDate()));
            pstmt.setString(5, patient.getWardLocationId());
            pstmt.setString(6, patient.getAllergies());
            pstmt.executeUpdate();
        }
    }

    private void validatePatient(Patient patient) throws ValidationException, SQLException {
        if (patient.getPatientId() == null || patient.getPatientId().trim().isEmpty()) {
            throw new ValidationException("Patient ID cannot be empty.");
        }
        if (findById(patient.getPatientId()) != null) {
            throw new ValidationException(
                    "Patient ID must be unique. ID '" + patient.getPatientId() + "' already exists.");
        }
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            throw new ValidationException("Patient name cannot be empty.");
        }
        if (patient.getNhisNumber() != null && !patient.getNhisNumber().matches("\\d{8}")) {
            throw new ValidationException("NHIS number must be 8 digits.");
        }
        if (patient.getBirthDate() == null || patient.getBirthDate().after(new Date())) {
            throw new ValidationException("Birth date cannot be in the future.");
        }
        if (patient.getWardLocationId() != null) {
            validateForeignKey("locations", "location_id", patient.getWardLocationId(), "Ward Location ID");
        }
    }

    private void validateForeignKey(String tableName, String columnName, String value, String fieldName)
            throws SQLException, ValidationException {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, value);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new ValidationException(
                            fieldName + " '" + value + "' does not exist in table '" + tableName + "'.");
                }
            }
        }
    }

    private Patient mapRowToPatient(ResultSet rs) throws SQLException {
        try {
            String patientId = rs.getString("patient_id");
            String name = rs.getString("name");
            String nhisNumber = rs.getString("nhis_number");
            Date birthDate = DATE_FORMAT.parse(rs.getString("birth_date"));
            String wardLocationId = rs.getString("ward_location_id");
            String allergies = rs.getString("allergies");
            return new Patient(patientId, name, nhisNumber, birthDate, wardLocationId, allergies);
        } catch (ParseException e) {
            throw new SQLException("Failed to parse date from database.", e);
        }
    }

    public void deleteAll() throws SQLException {
        String sql = "DELETE FROM patients";
        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}