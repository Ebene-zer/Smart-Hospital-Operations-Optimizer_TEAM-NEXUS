package com.hospital.algorithms.search;

import com.hospital.algorithms.PatientAdmission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdmissionSearchTest {

    @Test
    void linearSearch_findsPatientsInUnsortedDailyLogAndHandlesMissingAndEmptyLogs() {
        PatientAdmission[] log = {patient(30), patient(10), patient(20)};

        assertEquals(1, AdmissionSearch.linearSearchByPatientId(log, 10));
        assertEquals(-1, AdmissionSearch.linearSearchByPatientId(log, 99));
        assertEquals(-1, AdmissionSearch.linearSearchByPatientId(new PatientAdmission[0], 10));
    }

    @Test
    void binarySearch_findsPatientsInSortedRosterIncludingBoundaryValues() {
        PatientAdmission[] roster = {patient(Integer.MIN_VALUE), patient(10), patient(10), patient(Integer.MAX_VALUE)};

        assertEquals(0, AdmissionSearch.binarySearchByPatientId(roster, Integer.MIN_VALUE));
        assertEquals(3, AdmissionSearch.binarySearchByPatientId(roster, Integer.MAX_VALUE));
        assertEquals(10, roster[AdmissionSearch.binarySearchByPatientId(roster, 10)].patientId());
        assertEquals(-1, AdmissionSearch.binarySearchByPatientId(roster, 11));
    }

    @Test
    void binarySearch_rejectsUnsortedInputAsItsDocumentedPrecondition() {
        PatientAdmission[] unsorted = {patient(20), patient(10)};

        assertThrows(IllegalArgumentException.class, () -> AdmissionSearch.binarySearchByPatientId(unsorted, 10));
        assertThrows(IllegalArgumentException.class, () -> AdmissionSearch.linearSearchByPatientId(null, 1));
    }

    private PatientAdmission patient(int id) {
        return new PatientAdmission(id, "Patient " + id, 1, 1, 30, 1);
    }
}
