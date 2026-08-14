package com.hospital.graph;

import com.hospital.db.LocationDAO;
import com.hospital.db.RoadDAO;
import com.hospital.model.Location;
import com.hospital.model.Road;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.Instant;

public class TraceGenerator {

    public static void main(String[] args) throws Exception {
        double penalty = 1.0;
        String accidentId = "L046"; // default: Ridge Hospital
        if (args.length > 0) {
            try { penalty = Double.parseDouble(args[0]); } catch (Exception ignored) {}
        }
        if (args.length > 1) {
            accidentId = args[1];
        }

        LocationDAO ldao = new LocationDAO();
        RoadDAO rdao = new RoadDAO();
        var locations = ldao.findAll();
        var roads = rdao.findAll();

        Location[] la = locations.toArray(new Location[0]);
        Road[] ra = roads.toArray(new Road[0]);

        // Build full graph including referrals
        Graph full = new Graph(la, ra, true);

        // Dijkstra trace
        Dijkstra.Result dr = Dijkstra.shortestPath(full, accidentId, penalty);
        try (PrintWriter out = new PrintWriter(new FileWriter("docs/trace-tables/Dijkstra_Trace.md"))) {
            out.printf("# Dijkstra trace generated: %s\n\n", Instant.now());
            out.printf("Source (accident): %s  \n", accidentId);
            out.printf("Penalty multiplier: %.3f\n\n", penalty);
            out.println("| Location | Distance | Predecessor |");
            out.println("|---|---:|---|");
            for (int i = 0; i < full.size(); i++) {
                String id = full.getIndex().idAt(i);
                double d = dr.dist[i];
                String pred = dr.prev[i] >= 0 ? full.getIndex().idAt(dr.prev[i]) : "-";
                out.printf("| %s | %s | %s |\n", id, (d==Double.POSITIVE_INFINITY?"INF":String.format("%.3f", d)), pred);
            }

            out.println();
            // paths to Korle-Bu (L001) and Tema (L050)
            int korle = full.getIndex().indexOf("L001");
            int tema = full.getIndex().indexOf("L050");
            out.println("## Paths");
            if (korle >= 0) {
                out.printf("- To Korle-Bu (L001): distance=%.3f, path=%s\n", dr.dist[korle], Dijkstra.pathAsString(full, dr.prev, korle));
            } else {
                out.println("- Korle-Bu (L001) not present in graph.");
            }
            if (tema >= 0) {
                out.printf("- To Tema General (L050): distance=%.3f, path=%s\n", dr.dist[tema], Dijkstra.pathAsString(full, dr.prev, tema));
            } else {
                out.println("- Tema General (L050) not present in graph.");
            }
        }

        // MST traces: run on Korle-Bu internal layer (exclude REFERRAL)
        Graph internal = new Graph(la, ra, false);
        Kruskal.Result kr = Kruskal.buildMST(internal);
        Prim.Result pr = Prim.buildMST(internal);
        try (PrintWriter out = new PrintWriter(new FileWriter("docs/trace-tables/Kruskal_Prim_Trace.md"))) {
            out.printf("# Kruskal and Prim MST trace generated: %s\n\n", Instant.now());
            out.println("## Kruskal connectivity trace\n");
            out.println("```");
            out.println(kr.trace);
            out.println("```");
            out.println();
            out.println("### Kruskal MST edges (from -> to : cost)");
            out.println();
            for (Edge e : kr.mstEdges) {
                out.printf("- %s -> %s : %.3f\n", internal.getIndex().idAt(e.from), internal.getIndex().idAt(e.to), e.distanceKm);
            }
            out.printf("\nTotal cost: %.3f\n\n", kr.totalCost);

            out.println("## Prim trace\n");
            out.println("```");
            out.println(pr.trace);
            out.println("```");
            out.println();
            out.println("### Prim MST edges (from -> to : cost)");
            out.println();
            for (Edge e : pr.mstEdges) {
                out.printf("- %s -> %s : %.3f\n", internal.getIndex().idAt(e.from), internal.getIndex().idAt(e.to), e.distanceKm);
            }
            out.printf("\nTotal cost: %.3f\n", pr.totalCost);
        }

        // BFS/DFS reachability trace: check connectivity to Blood Bank or fallback to L001
        String bloodId = "L001";
        // try to find a location named BloodBank
        for (Location l : la) {
            if (l.getName() != null && l.getName().toLowerCase().contains("blood")) {
                bloodId = l.getLocationId();
                break;
            }
        }
        // Baseline: check all WARD-type locations can reach bloodId
        StringBuilder bfsdfs = new StringBuilder();
        bfsdfs.append("BFS/DFS reachability trace\n\n");
        bfsdfs.append(String.format("Target (blood bank): %s\n\n", bloodId));
        for (Location l : la) {
            if ("WARD".equals(l.getType())) {
                boolean can = DFS.canReach(internal, l.getLocationId(), bloodId);
                bfsdfs.append(String.format("- %s -> %s : %s\n", l.getLocationId(), bloodId, can ? "REACHABLE" : "UNREACHABLE"));
            }
        }

        // simulate corridor closure: remove first edge found between two ward nodes (if any)
        outer:
        for (Road r : ra) {
            // both endpoints internal?
            int a = internal.getIndex().indexOf(r.getFromLocationId());
            int b = internal.getIndex().indexOf(r.getToLocationId());
            if (a >= 0 && b >= 0) {
                // remove and test
                internal.removeEdge(a, b);
                internal.removeEdge(b, a);
                bfsdfs.append("\nAfter removing one corridor (simulated):\n\n");
                for (Location l : la) {
                    if ("WARD".equals(l.getType())) {
                        boolean can = DFS.canReach(internal, l.getLocationId(), bloodId);
                        bfsdfs.append(String.format("- %s -> %s : %s\n", l.getLocationId(), bloodId, can ? "REACHABLE" : "UNREACHABLE"));
                    }
                }
                break outer;
            }
        }

        try (PrintWriter out = new PrintWriter(new FileWriter("docs/trace-tables/BFS_DFS_Trace.md"))) {
            out.printf("# BFS/DFS reachability trace generated: %s\n\n", Instant.now());
            out.println(bfsdfs.toString());
        }

        System.out.println("Trace files written to docs/trace-tables/");
    }
}
