package com.hospital.app;

import com.hospital.db.LocationDAO;
import com.hospital.db.RoadDAO;
import com.hospital.graph.Dijkstra;
import com.hospital.graph.Graph;

public class Main {
    public static void main(String[] args) {
        System.out.println("Korle-Bu Smart Hospital Operations Optimizer");
        System.out.println("Building graph from DB (demo)...");
        try {
            LocationDAO ldao = new LocationDAO();
            RoadDAO rdao = new RoadDAO();
            var locations = ldao.findAll();
            var roads = rdao.findAll();
            // convert to arrays
            com.hospital.model.Location[] la = locations.toArray(new com.hospital.model.Location[0]);
            com.hospital.model.Road[] ra = roads.toArray(new com.hospital.model.Road[0]);

            Graph g = new Graph(la, ra, true); // include referrals
            // Dijkstra demo: use L001 (A&E) as source, compare route to L001 and L050 (Tema)
            double penalty = 1.0; // set per-team index multiplier as needed
            var res = Dijkstra.shortestPath(g, "L001", penalty);
            if (res != null) {
                System.out.println("Dijkstra distances (sample):");
                System.out.println("To L001: " + res.dist[g.getIndex().indexOf("L001")]);
                System.out.println("To L050 (Tema): " + res.dist[g.getIndex().indexOf("L050")]);
                System.out.println("Path to L050: " + Dijkstra.pathAsString(g, res.prev, g.getIndex().indexOf("L050")));
            }
        } catch (Exception ex) {
            System.out.println("Demo graph run failed: " + ex.getMessage());
            ex.printStackTrace();
        }

        System.out.println("Build OK. Replace this with the real startup sequence:");
        System.out.println("  1. Team 1  -> connect to DB, run schema, import CSVs");
        System.out.println("  2. Team 2  -> build indexes from loaded patients");
        System.out.println("  3. Team 3  -> build ward/referral graph");
        System.out.println("  4. Team 4  -> wire up queues/stacks/deques for live operations");
        System.out.println("  5. Team 5  -> run search/sort/greedy/DP demos");
    }
}
