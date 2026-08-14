package com.hospital.graph;

import com.hospital.model.Location;
import com.hospital.model.Road;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GraphBasicTest {

    @Test
    public void buildAndTraverseSmallGraph() {
        Location l1 = new Location("L1","A","A","WARD",0,0);
        Location l2 = new Location("L2","B","A","WARD",0,0);
        Location l3 = new Location("L3","C","A","WARD",0,0);
        Location[] locs = new Location[]{l1,l2,l3};
        Road r1 = new Road("R1","L1","L2",1.0,1.0,0.5);
        Road r2 = new Road("R2","L2","L3",2.0,2.0,0.5);
        Road[] roads = new Road[]{r1,r2};

        Graph g = new Graph(locs, roads, true);
        assertEquals(3, g.size());

        int idx = g.getIndex().indexOf("L1");
        assertTrue(idx >= 0);

        var res = Dijkstra.shortestPath(g, "L1", 1.0);
        assertNotNull(res);
        assertTrue(res.dist[g.getIndex().indexOf("L3")] < Double.POSITIVE_INFINITY);
    }
}
