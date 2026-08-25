package com.hospital.operations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NursingShiftManagerTest {

    @DisplayName("Test nurse assignment")

    @Test
    void testAddNurse() {
        NursingShiftManager manager = new NursingShiftManager(3);
        manager.addNurse("Nurse A");
        assertEquals(1, manager.getNurseCount());
    }

    @Test
    void testAssignNurse() {
        NursingShiftManager manager = new NursingShiftManager(3);

        manager.addNurse("Nurse A");
        manager.addNurse("Nurse B");

        assertEquals("Nurse A", manager.assignNurse());
        assertEquals(2, manager.getNurseCount());
        assertEquals("Nurse B", manager.getNextNurse());
        assertEquals("Nurse B", manager.assignNurse());
        assertEquals(2, manager.getNurseCount());
        assertEquals("Nurse A", manager.getNextNurse());
    }

    @Test
    void testAssignNurseWhenEmpty() {
        NursingShiftManager manager = new NursingShiftManager(3);
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            manager.assignNurse();
        });
        assertEquals("No nurse available for assignment", exception.getMessage());
    }

}
