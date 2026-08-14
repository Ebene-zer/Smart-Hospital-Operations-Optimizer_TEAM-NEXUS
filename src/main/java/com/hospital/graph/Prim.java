package com.hospital.graph;

public class Prim {

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
        boolean[] inMST = new boolean[n];
        double[] minWeight = new double[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            minWeight[i] = Double.POSITIVE_INFINITY;
            parent[i] = -1;
        }
        minWeight[0] = 0.0;
        StringBuilder trace = new StringBuilder();
        trace.append("Prim trace:\n");

        for (int iter = 0; iter < n; iter++) {
            int u = -1;
            double best = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                if (!inMST[i] && minWeight[i] < best) {
                    best = minWeight[i]; u = i;
                }
            }
            if (u == -1) break;
            inMST[u] = true;
            trace.append(String.format("Select vertex %d with key=%.2f\n", u, best));
            for (Edge e : g.neighbors(u)) {
                int v = e.to;
                if (!inMST[v] && e.distanceKm < minWeight[v]) {
                    minWeight[v] = e.distanceKm;
                    parent[v] = u;
                    trace.append(String.format("  Update key for %d via %d cost=%.2f\n", v, u, e.distanceKm));
                }
            }
        }

        Edge[] edges = new Edge[n - 1];
        int c = 0; double total = 0.0;
        for (int v = 0; v < n; v++) {
            if (parent[v] != -1) {
                edges[c++] = new Edge(parent[v], v, minWeight[v], 0.0);
                total += minWeight[v];
            }
        }
        Edge[] mst = new Edge[c];
        for (int i = 0; i < c; i++) mst[i] = edges[i];
        return new Result(mst, total, trace.toString());
    }
}
