package com.hospital.operations;

import com.hospital.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OPDWaitingListTest {

    @Test
    void arriveAndIteratorWalkInOrder() {
        OPDWaitingList list = new OPDWaitingList();
        ServiceRequest a = req("SR1", "LOW");
        ServiceRequest b = req("SR2", "MEDIUM");
        list.arrive(a);
        list.arrive(b);
        assertEquals(2, list.size());
        assertTrue(list.iteratorWalk().contains("SR1"));
        assertEquals("SR1", list.serveNext().getRequestId());
    }

    @Test
    void bumpAfterReprioritises() {
        OPDWaitingList list = new OPDWaitingList();
        ServiceRequest a = req("SR1", "LOW");
        ServiceRequest b = req("SR2", "MEDIUM");
        ServiceRequest x = req("SRX", "HIGH");
        list.arrive(a);
        list.arrive(b);
        list.bumpAfter(a, x);
        assertEquals("SR1", list.serveNext().getRequestId());
        assertEquals("SRX", list.serveNext().getRequestId());
    }

    @Test
    void emptyServeThrows() {
        assertThrows(IllegalStateException.class, () -> new OPDWaitingList().serveNext());
        assertThrows(IllegalArgumentException.class, () -> new OPDWaitingList().arrive(null));
    }

    private static ServiceRequest req(String id, String urgency) {
        return new ServiceRequest(id, "L001", "L002", "OPD", urgency, "2024-01-02 08:00", "2024-01-02 09:00", "PENDING");
    }
}
