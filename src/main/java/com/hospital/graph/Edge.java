package com.hospital.graph;

public class Edge {
    public final int from;
    public final int to;
    public final double distanceKm;
    public final double roadConditionWeight;

    public Edge(int from, int to, double distanceKm, double roadConditionWeight) {
        this.from = from;
        this.to = to;
        this.distanceKm = distanceKm;
        this.roadConditionWeight = roadConditionWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge e = (Edge) o;
        return this.from == e.from && this.to == e.to;
    }
}
