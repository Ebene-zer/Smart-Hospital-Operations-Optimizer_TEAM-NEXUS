package com.hospital.graph;

import com.hospital.model.Location;
import com.hospital.model.Road;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphEdgeCaseTest {

    @Test
    void disconnectedGraphLeavesUnreachableDistanceInfinite() {
        Location a = new Location("L1", "A", "Korle-Bu", "WARD", 0, 0);
        Location b = new Location("L2", "B", "Korle-Bu", "WARD", 0, 0);
        Location c = new Location("L3", "C", "Tema", "REFERRAL", 0, 0);
        Graph g = new Graph(new Location[] {a, b, c}, new Road[] {
                new Road("R1", "L1", "L2", 1, 1, 1)
        }, true);

        Dijkstra.Result result = Dijkstra.shortestPath(g, "L1", 1.0);
        assertNotNull(result);
        assertEquals(0.0, result.dist[g.getIndex().indexOf("L1")]);
        assertTrue(result.dist[g.getIndex().indexOf("L2")] < Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, result.dist[g.getIndex().indexOf("L3")]);
        assertFalse(DFS.canReach(g, "L1", "L3"));
        assertEquals(-1, BFS.nearestAvailableBed(g, "L1", loc -> "L3".equals(loc.getLocationId())));
        assertNull(Dijkstra.shortestPath(g, "MISSING", 1.0));
    }

    @Test
    void invalidStartIsUnreachable() {
        Location a = new Location("L1", "A", "Korle-Bu", "WARD", 0, 0);
        Graph g = new Graph(new Location[] {a}, new Road[] {}, true);
        assertFalse(DFS.canReach(g, "L1", "NOPE"));
        assertEquals(-1, BFS.nearestAvailableBed(g, "NOPE", loc -> true));
    }
}
