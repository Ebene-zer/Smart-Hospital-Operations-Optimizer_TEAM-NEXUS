package com.hospital.algorithms.optimization;

import com.hospital.algorithms.PatientAdmission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GreedyResourceAllocatorTest {

    @Test
    void allocate_assignsHighestUrgencyPatientsFirstUntilCapacityRunsOut() {
        PatientAdmission[] admissions = {
                patient(1, 2, 2), patient(2, 5, 1), patient(3, 4, 1)
        };

        GreedyResourceAllocator.AllocationResult result = GreedyResourceAllocator.allocate(admissions, 2);

        assertArrayEquals(new int[] {2, 3}, ids(result.allocated()));
        assertArrayEquals(new int[] {1}, ids(result.unallocated()));
        assertEquals(0, result.remainingUnits());
    }

    @Test
    void allocate_documentsGreedyLimitationWhenOneHighUrgencyCaseUsesAllCapacity() {
        PatientAdmission[] admissions = {
                patient(1, 10, 2), patient(2, 9, 1), patient(3, 8, 1)
        };

        GreedyResourceAllocator.AllocationResult result = GreedyResourceAllocator.allocate(admissions, 2);

        assertArrayEquals(new int[] {1}, ids(result.allocated()));
        assertArrayEquals(new int[] {2, 3}, ids(result.unallocated()));
    }

    @Test
    void allocate_handlesEmptyAndInvalidInputs() {
        assertEquals(0, GreedyResourceAllocator.allocate(new PatientAdmission[0], 0).allocated().length);
        assertThrows(IllegalArgumentException.class, () -> GreedyResourceAllocator.allocate(null, 1));
        assertThrows(IllegalArgumentException.class, () -> GreedyResourceAllocator.allocate(new PatientAdmission[] {patient(1, 1, 1)}, -1));
    }

    private PatientAdmission patient(int id, int urgency, int units) {
        return new PatientAdmission(id, "Patient " + id, 1, urgency, 30, units);
    }

    private int[] ids(PatientAdmission[] admissions) {
        int[] ids = new int[admissions.length];
        for (int index = 0; index < admissions.length; index++) {
            ids[index] = admissions[index].patientId();
        }
        return ids;
    }
}
