package com.hospital.db;

import com.hospital.model.Location;
import com.hospital.model.Resource;
import com.hospital.model.Road;
import com.hospital.model.ServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DatabaseCompatibilityTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        DBConnection.setConnection(connection);
        DBConnection.initializeSchema();
    }

    @AfterEach
    void tearDown() throws Exception {
        DBConnection.closeConnection();
    }

    @Test
    void daoLayerSupportsCsvStyleIdsAndUrgency() throws Exception {
        LocationDAO locationDAO = new LocationDAO();
        RoadDAO roadDAO = new RoadDAO();
        ResourceDAO resourceDAO = new ResourceDAO();
        ServiceRequestDAO serviceRequestDAO = new ServiceRequestDAO();

        Location location = new Location("L001", "Emergency", "Korle-Bu", "EMERGENCY", 5.53, -0.22);
        locationDAO.insert(location);
        roadDAO.insert(new Road("R001", "L001", "L001", 0.1, 2, 1.0));
        resourceDAO.insert(new Resource("RES001", "DOCTOR", "L001", 1, "AVAILABLE"));
        serviceRequestDAO.insert(new ServiceRequest(
                "SR001", "L001", "L001", "EMERGENCY", "CRITICAL",
                "2024-01-02 08:05", "2024-01-02 08:20", "COMPLETED"));

        assertNotNull(locationDAO.findById("L001"));
        assertNotNull(roadDAO.findById("R001"));
        assertNotNull(resourceDAO.findById("RES001"));
        ServiceRequest request = serviceRequestDAO.findById("SR001");
        assertNotNull(request);
        assertEquals("CRITICAL", request.getUrgency());
    }

    @Test
    void foreignKeysAreEnabledOnConnection() {
        assertThrows(SQLException.class, () -> {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO roads (roadId, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight) VALUES (?, ?, ?, ?, ?, ?)")) {
                statement.setString(1, "R999");
                statement.setString(2, "L404");
                statement.setString(3, "L405");
                statement.setDouble(4, 1.0);
                statement.setDouble(5, 3.0);
                statement.setDouble(6, 1.0);
                statement.executeUpdate();
            }
        });
    }
}
