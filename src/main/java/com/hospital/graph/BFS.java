package com.hospital.graph;

import com.hospital.model.Location;
import com.hospital.structures.core.Queue;
import com.hospital.structures.core.SinglyLinkedList;

public class BFS {

    public interface HasBedPredicate {
        boolean test(Location loc);
    }

    public static int nearestAvailableBed(Graph g, String startId, HasBedPredicate predicate) {
        GraphIndex idx = g.getIndex();
        int start = idx.indexOf(startId);
        if (start < 0) return -1;
        int n = g.size();
        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = -1;

        Queue<Integer> q = new Queue<>(n);
        visited[start] = true;
        q.enqueue(start);

        while (!q.isEmpty()) {
            int cur = q.dequeue();
            Location loc = g.locationAt(cur);
            if (predicate.test(loc)) {
                return cur;
            }
            SinglyLinkedList<Edge> nb = g.neighbors(cur);
            for (Edge e : nb) {
                int to = e.to;
                if (!visited[to]) {
                    visited[to] = true;
                    parent[to] = cur;
                    q.enqueue(to);
                }
            }
        }
        return -1;
    }
}
