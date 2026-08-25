package com.hospital.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeamParametersTest {

    @Test
    void derivedValuesMatchDocumentedFormulae() {
        assertEquals(TeamParameters.nextPrime(50 + (113 % 80)), TeamParameters.HASH_TABLE_CAPACITY);
        assertEquals(1.47, TeamParameters.DIJKSTRA_PENALTY, 1e-9);
        assertEquals(4 + (808 % 5), TeamParameters.DP_REGULAR_HOURS);
        assertEquals(1 + (808 % 4), TeamParameters.DP_OVERTIME_HOURS);
        assertTrue(TeamParameters.HASH_TABLE_CAPACITY >= 2);
        assertTrue(TeamParameters.summary().contains("Ampadu"));
    }
}
