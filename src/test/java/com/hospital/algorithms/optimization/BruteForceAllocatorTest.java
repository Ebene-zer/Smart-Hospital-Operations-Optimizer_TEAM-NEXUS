package com.hospital.algorithms.optimization;

import com.hospital.algorithms.PatientAdmission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BruteForceAllocatorTest {

    @Test
    void prefersTwoShortStayPatientsOverOneLongStay() {
        PatientAdmission[] admissions = {
                new PatientAdmission(1, "Long", 1, 10, 50, 2),
                new PatientAdmission(2, "A", 2, 9, 20, 1),
                new PatientAdmission(3, "B", 3, 8, 21, 1)
        };
        var greedy = GreedyResourceAllocator.allocate(admissions, 2);
        var brute = BruteForceAllocator.allocate(admissions, 2);
        assertEquals(1, greedy.allocated().length);
        assertEquals(2, brute.patientsTreated());
        assertEquals(8, brute.subsetsExamined());
    }

    @Test
    void rejectsLargeN() {
        PatientAdmission[] tooBig = new PatientAdmission[13];
        for (int i = 0; i < tooBig.length; i++) {
            tooBig[i] = new PatientAdmission(i + 1, "P" + i, 1, 1, 20, 1);
        }
        assertThrows(IllegalArgumentException.class, () -> BruteForceAllocator.allocate(tooBig, 5));
        assertThrows(IllegalArgumentException.class, () -> BruteForceAllocator.allocate(null, 1));
    }
}
