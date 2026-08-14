package com.hospital.graph;

import com.hospital.structures.core.Stack;

public class DFS {

    public static boolean canReach(Graph g, String startId, String targetId) {
        int n = g.size();
        GraphIndex idx = g.getIndex();
        int s = idx.indexOf(startId);
        int t = idx.indexOf(targetId);
        if (s < 0 || t < 0) return false;
        boolean[] visited = new boolean[n];
        Stack<Integer> stack = new Stack<>();
        stack.push(s);
        while (!stack.isEmpty()) {
            int cur = stack.pop();
            if (visited[cur]) continue;
            visited[cur] = true;
            if (cur == t) return true;
            for (Edge e : g.neighbors(cur)) {
                if (!visited[e.to]) stack.push(e.to);
            }
        }
        return false;
    }
}
