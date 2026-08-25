package com.hospital.operations;

import com.hospital.model.Location;
import com.hospital.model.ServiceRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndexingEngineTest {

    @Test
    void indexesRequestsAndLocationsForLookup() {
        IndexingEngine engine = new IndexingEngine();
        ServiceRequest[] requests = {
                new ServiceRequest("SR007", "L001", "L002", "ADMISSION", "HIGH",
                        "2024-01-02 08:00", "2024-01-02 09:00", "PENDING")
        };
        Location[] locations = {
                new Location("L001", "A&E", "Korle-Bu", "EMERGENCY", 5.5, -0.2)
        };
        engine.indexRequests(requests);
        engine.indexLocations(locations);
        assertTrue(engine.bstContainsRequest(7));
        assertTrue(engine.rbtContainsRequest(7));
        assertTrue(engine.bTreeContainsRequest(7));
        assertNotNull(engine.hashLookup(7));
        assertTrue(engine.bstContainsLocation(1));
        assertEquals(1, engine.requestCount());
    }
}
