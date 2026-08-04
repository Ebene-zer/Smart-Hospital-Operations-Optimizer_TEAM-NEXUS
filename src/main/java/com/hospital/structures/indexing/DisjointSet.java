package com.hospital.structures.indexing;

/**
 * Union-Find for grouping patients by infection cluster (e.g. a cholera or
 * measles cluster traced to Ablekuma). union() as new linked cases are
 * admitted; produces the Kruskal-style connectivity trace required in the
 * report. Sync the id-numbering scheme with Team 3 before finalizing —
 * their Kruskal MST implementation calls into this class directly.
 */
public class DisjointSet {

    private final int[] parent;
    private final int[] rank;

    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i; // each element starts as its own set (makeSet)
        }
    }

    public int find(int x) {
        // TODO: path compression — recursively find the root, then point x
        // directly at it
        throw new UnsupportedOperationException("TODO");
    }

    public void union(int x, int y) {
        // TODO: union by rank — attach the smaller-rank root under the
        // larger-rank root; if equal, pick one and increment its rank
        throw new UnsupportedOperationException("TODO");
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }
}
