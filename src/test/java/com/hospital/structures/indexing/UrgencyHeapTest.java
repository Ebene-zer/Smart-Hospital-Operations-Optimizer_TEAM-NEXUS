package com.hospital.structures.indexing;

import com.hospital.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrgencyHeapTest {

    @Test
    void extractMaxServesCriticalBeforeWalkIns() {
        UrgencyHeap heap = new UrgencyHeap();
        heap.insert(req("W1", "LOW", "2024-01-02 18:01"));
        heap.insert(req("W2", "MEDIUM", "2024-01-02 18:02"));
        heap.insert(req("T1", "CRITICAL", "2024-01-02 18:03"));
        heap.insert(req("T2", "CRITICAL", "2024-01-02 18:04"));
        assertEquals("T1", heap.extractMax().getRequestId());
        assertEquals("T2", heap.extractMax().getRequestId());
        assertEquals("W2", heap.extractMax().getRequestId());
        assertEquals("W1", heap.extractMax().getRequestId());
        assertTrue(heap.isEmpty());
        assertNull(heap.extractMax());
    }

    @Test
    void rejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> new UrgencyHeap().insert(null));
    }

    private static ServiceRequest req(String id, String urgency, String time) {
        return new ServiceRequest(id, "L001", "L001", "EMERGENCY", urgency, time, time, "PENDING");
    }
}
