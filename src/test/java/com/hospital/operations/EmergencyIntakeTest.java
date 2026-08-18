package com.hospital.operations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EmergencyIntakeTest {
    @DisplayName("Test emergency intake")

    @Test
    void emergencyPatientJoinsFront() {
        EmergencyIntake intake = new EmergencyIntake();

        intake.admitRoutine("Patient A");
        intake.admitRoutine("Patient B");
        intake.admitEmergency("Patient C"); // Emergency patient

        assertEquals("Patient C", intake.getNextPatient(), "Emergency patient should be at the front");
        assertEquals("Patient C", intake.attendNext(), "Attending next should return the emergency patient");
    }

    @Test
    void routinePatientJoinsRear() {
        EmergencyIntake intake = new EmergencyIntake();

        intake.admitRoutine("Patient A");
        intake.admitRoutine("Patient B");

        assertEquals("Patient A", intake.attendNext());
        assertEquals("Patient B", intake.attendNext());
    }

    @Test
    void updatePatientCount() {
        EmergencyIntake intake = new EmergencyIntake();

        assertEquals(0, intake.getPatientCount(), "Initial patient count should be 0");

        intake.admitRoutine("Patient A");
        intake.admitEmergency("Patient B");

        assertEquals(2, intake.getPatientCount(), "Patient count should be 2 after admitting two patients");

        intake.attendNext(); // Attend one patient

        assertEquals(1, intake.getPatientCount(), "Patient count should be 1 after attending one patient");
    }

    @Test
    void emptyIntakeBehavior() {
        EmergencyIntake intake = new EmergencyIntake();

        assertFalse(intake.hasPatients(), "Intake should be empty initially");
        assertNull(intake.getNextPatient(), "Next patient should be null when intake is empty");

        intake.admitRoutine("Patient A");
        assertTrue(intake.hasPatients(), "Intake should have patients after admitting one");

        intake.attendNext(); // Attend the only patient
        assertFalse(intake.hasPatients());
    }
}
