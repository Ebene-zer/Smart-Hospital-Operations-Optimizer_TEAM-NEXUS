package com.hospital.graph;

import com.hospital.model.Location;
import com.hospital.model.Road;
import com.hospital.structures.core.SinglyLinkedList;

public class Graph {
    private final int n;
    private final GraphIndex index;
    private final Location[] indexToLocation;
    @SuppressWarnings("unchecked")
    private final SinglyLinkedList<Edge>[] adjList;
    private final double[][] adjMatrix;

    public static final double INF = Double.POSITIVE_INFINITY;

    public Graph(Location[] locations, Road[] roads, boolean includeReferrals) {
        // build index: include or exclude REFERRAL types
        int count = 0;
        for (int i = 0; i < locations.length; i++) {
            if (includeReferrals || !"REFERRAL".equals(locations[i].getType())) {
                count++;
            }
        }
        this.n = count;
        this.indexToLocation = new Location[n];
        String[] ids = new String[n];
        int p = 0;
        for (int i = 0; i < locations.length; i++) {
            if (includeReferrals || !"REFERRAL".equals(locations[i].getType())) {
                indexToLocation[p] = locations[i];
                ids[p] = locations[i].getLocationId();
                p++;
            }
        }
        this.index = new GraphIndex(ids);

        this.adjList = new SinglyLinkedList[n];
        for (int i = 0; i < n; i++) {
            adjList[i] = new SinglyLinkedList<>();
        }
        this.adjMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjMatrix[i][j] = INF;
            }
        }

        // add roads
        for (Road r : roads) {
            int a = index.indexOf(r.getFromLocationId());
            int b = index.indexOf(r.getToLocationId());
            if (a >= 0 && b >= 0) {
                addEdge(a, b, r.getDistanceKm(), r.getRoadConditionWeight());
                addEdge(b, a, r.getDistanceKm(), r.getRoadConditionWeight());
            }
        }
    }

    public int size() {
        return n;
    }

    public GraphIndex getIndex() {
        return index;
    }

    public Location locationAt(int idx) {
        return indexToLocation[idx];
    }

    public void addEdge(int from, int to, double distanceKm, double roadConditionWeight) {
        Edge e = new Edge(from, to, distanceKm, roadConditionWeight);
        adjList[from].addLast(e);
        adjMatrix[from][to] = distanceKm;
    }

    public void removeEdge(int from, int to) {
        Edge e = new Edge(from, to, 0.0, 0.0);
        adjList[from].remove(e);
        adjMatrix[from][to] = INF;
    }

    public SinglyLinkedList<Edge> neighbors(int idx) {
        return adjList[idx];
    }

    public double[][] adjacencyMatrix() {
        return adjMatrix;
    }
}
