package com.hospital.operations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PharmacyQueueTest {
    @DisplayName("Test PharmacyQueue flow")

    @Test
    void servePatientsInFifoOrder() {
        PharmacyQueue pharmacyQueue = new PharmacyQueue(5);
        pharmacyQueue.addPatient("Patient A");
        pharmacyQueue.addPatient("Patient B");
        pharmacyQueue.addPatient("Patient C");

        assertEquals("Patient A", pharmacyQueue.serveNext());
        assertEquals("Patient B", pharmacyQueue.serveNext());
        assertEquals("Patient C", pharmacyQueue.serveNext());

    }

    @Test
    void testPeekNextPatient() {
        PharmacyQueue pharmacyQueue = new PharmacyQueue(5);
        pharmacyQueue.addPatient("Patient A");
        pharmacyQueue.addPatient("Patient B");

        assertEquals("Patient A", pharmacyQueue.getNextPatient());
        pharmacyQueue.serveNext();
        assertEquals("Patient B", pharmacyQueue.getNextPatient());
    }

    @Test
    void queueSizeAndFullness() {
        PharmacyQueue pharmacyQueue = new PharmacyQueue(3);
        assertEquals(0, pharmacyQueue.getQueueSize());
        assertFalse(pharmacyQueue.isFull());

        pharmacyQueue.addPatient("Patient A");
        pharmacyQueue.addPatient("Patient B");
        assertEquals(2, pharmacyQueue.getQueueSize());
        assertFalse(pharmacyQueue.isFull());

        pharmacyQueue.addPatient("Patient C");
        assertEquals(3, pharmacyQueue.getQueueSize());
        assertTrue(pharmacyQueue.isFull());
    }

    @Test
    void emptyQueueBehavior() {
        PharmacyQueue pharmacyQueue = new PharmacyQueue(3);
        assertFalse(pharmacyQueue.hasPatients());
        assertThrows(IllegalStateException.class, pharmacyQueue::serveNext);
        assertThrows(IllegalStateException.class, pharmacyQueue::getNextPatient);

        pharmacyQueue.addPatient("Patient A");
        pharmacyQueue.addPatient("Patient B");
        pharmacyQueue.addPatient("Patient C");

        pharmacyQueue.serveNext();
        pharmacyQueue.serveNext();
        pharmacyQueue.serveNext();

        assertFalse(pharmacyQueue.hasPatients());
    }

    @Test
    void testQueueOverflow() {
        PharmacyQueue pharmacyQueue = new PharmacyQueue(2);
        pharmacyQueue.addPatient("Patient A");
        pharmacyQueue.addPatient("Patient B");

        assertThrows(IllegalStateException.class, () -> pharmacyQueue.addPatient("Patient C"));
    }

}
