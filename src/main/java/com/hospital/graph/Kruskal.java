package com.hospital.graph;

import com.hospital.structures.indexing.DisjointSet;

public class Kruskal {

    public static class Result {
        public final Edge[] mstEdges;
        public final double totalCost;
        public final String trace;

        public Result(Edge[] mstEdges, double totalCost, String trace) {
            this.mstEdges = mstEdges;
            this.totalCost = totalCost;
            this.trace = trace;
        }
    }

    public static Result buildMST(Graph g) {
        int n = g.size();
        // collect edges (undirected, avoid duplicates)
        int maxE = n * (n - 1) / 2;
        Edge[] all = new Edge[maxE];
        int ecount = 0;
        for (int u = 0; u < n; u++) {
            for (Edge e : g.neighbors(u)) {
                if (e.from < e.to) {
                    all[ecount++] = e;
                }
            }
        }

        // trim
        Edge[] edges = new Edge[ecount];
        for (int i = 0; i < ecount; i++) edges[i] = all[i];

        // simple selection sort by distance
        for (int i = 0; i < edges.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < edges.length; j++) {
                if (edges[j].distanceKm < edges[min].distanceKm) min = j;
            }
            if (min != i) {
                Edge tmp = edges[i]; edges[i] = edges[min]; edges[min] = tmp;
            }
        }

        DisjointSet ds = new DisjointSet();
        for (int i = 0; i < n; i++) ds.makeSet(i);

        Edge[] chosen = new Edge[n - 1];
        int chosenCount = 0;
        double total = 0.0;
        StringBuilder trace = new StringBuilder();
        trace.append("Kruskal trace:\n");
        for (int i = 0; i < edges.length && chosenCount < n - 1; i++) {
            Edge e = edges[i];
            int ra = ds.find(e.from);
            int rb = ds.find(e.to);
            trace.append(String.format("Consider edge %s-%s cost=%.2f -> sets(%d,%d) ",
                    e.from, e.to, e.distanceKm, ra, rb));
            if (ra != rb) {
                ds.union(ra, rb);
                chosen[chosenCount++] = e;
                total += e.distanceKm;
                trace.append("ACCEPT\n");
            } else {
                trace.append("REJECT\n");
            }
        }

        // trim chosen
        Edge[] mst = new Edge[chosenCount];
        for (int i = 0; i < chosenCount; i++) mst[i] = chosen[i];

        return new Result(mst, total, trace.toString());
    }
}
