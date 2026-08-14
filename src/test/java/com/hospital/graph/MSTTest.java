package com.hospital.graph;

import com.hospital.model.Location;
import com.hospital.model.Road;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTest {

    @Test
    public void kruskalPrimSmallGraph() {
        Location l1 = new Location("L1","A","A","WARD",0,0);
        Location l2 = new Location("L2","B","A","WARD",0,0);
        Location l3 = new Location("L3","C","A","WARD",0,0);
        Location[] locs = new Location[]{l1,l2,l3};
        Road r1 = new Road("R1","L1","L2",1.0,1.0,0.5);
        Road r2 = new Road("R2","L2","L3",2.0,2.0,0.5);
        Road r3 = new Road("R3","L1","L3",3.0,3.0,0.5);
        Road[] roads = new Road[]{r1,r2,r3};

        Graph g = new Graph(locs, roads, true);
        Kruskal.Result kr = Kruskal.buildMST(g);
        Prim.Result pr = Prim.buildMST(g);

        assertNotNull(kr);
        assertNotNull(pr);
        assertEquals(2, kr.mstEdges.length);
        assertEquals(2, pr.mstEdges.length);
        assertEquals(3.0, kr.totalCost + 0.0, 0.0001);
        assertEquals(3.0, pr.totalCost + 0.0, 0.0001);
    }
}
