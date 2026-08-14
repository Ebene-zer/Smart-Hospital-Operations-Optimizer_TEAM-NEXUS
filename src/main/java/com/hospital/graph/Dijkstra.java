package com.hospital.graph;

import com.hospital.model.Location;

public class Dijkstra {

    public static class Result {
        public final double[] dist;
        public final int[] prev;

        public Result(double[] dist, int[] prev) {
            this.dist = dist;
            this.prev = prev;
        }
    }

    public static Result shortestPath(Graph g, String startId, double penaltyMultiplier) {
        GraphIndex idx = g.getIndex();
        int s = idx.indexOf(startId);
        if (s < 0) return null;
        int n = g.size();
        double[] dist = new double[n];
        int[] prev = new int[n];
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            prev[i] = -1;
        }
        dist[s] = 0.0;

        for (int iter = 0; iter < n; iter++) {
            int u = -1;
            double best = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && dist[i] < best) {
                    best = dist[i];
                    u = i;
                }
            }
            if (u == -1) break;
            visited[u] = true;

            for (Edge e : g.neighbors(u)) {
                int v = e.to;
                if (visited[v]) continue;
                double w = e.distanceKm + penaltyMultiplier * e.roadConditionWeight;
                double alt = dist[u] + w;
                if (alt < dist[v]) {
                    dist[v] = alt;
                    prev[v] = u;
                }
            }
        }

        return new Result(dist, prev);
    }

    public static String pathAsString(Graph g, int[] prev, int targetIdx) {
        if (targetIdx < 0) return "";
        StringBuilder sb = new StringBuilder();
        int cur = targetIdx;
        while (cur != -1) {
            if (sb.length() > 0) sb.insert(0, " -> ");
            sb.insert(0, g.getIndex().idAt(cur));
            cur = prev[cur];
        }
        return sb.toString();
    }
}
