package com.hospital.operations;

import com.hospital.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DispatchDemoTest {

    @Test
    void heapPutsCriticalBeforeLow() {
        ServiceRequest[] batch = {
                new ServiceRequest("A", "L001", "L001", "EMERGENCY", "LOW", "2024-01-02 08:00", "2024-01-02 09:00", "PENDING"),
                new ServiceRequest("B", "L001", "L001", "EMERGENCY", "CRITICAL", "2024-01-02 08:05", "2024-01-02 09:00", "PENDING")
        };
        String heap = DispatchDemo.heap(batch);
        assertTrue(heap.indexOf("B") < heap.indexOf("A"));
        String fifo = DispatchDemo.fifo(batch);
        assertTrue(fifo.indexOf("A") < fifo.indexOf("B"));
        assertTrue(DispatchDemo.traumaWalkInTrace().contains("CRITICAL"));
        assertTrue(DispatchDemo.compare(batch).contains("FIFO"));
    }
}
