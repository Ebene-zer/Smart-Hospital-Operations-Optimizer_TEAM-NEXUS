# Performance interpretation (theory vs observed)

**Machine:** Windows 11 amd64, Java 22.0.1, 8 processors, ~2 GB JVM heap. Each cell is the average of **three** timed runs after one untimed warmup. Raw files: `*.csv` and `*.svg` in this folder. SQLite `algorithm_runs` stores well over the required 30 rows.

## Search

Linear search of a missing key is Θ(n). Binary search of a sorted array is Θ(log n) **after** the array is known to be sorted. This implementation also runs `requireSortedByPatientId`, which is Θ(n). Observed times therefore stay in the same order of magnitude as a full scan, and binary search can look *slower* at 10 000 keys because it pays for both the linear precondition check and the log n probe. That is a real mismatch with the textbook “binary is faster” graph; it is also evidence that the precondition is actually executed.

## Sorting

This family matches theory. At n = 10 000, selection (~331 ms) and insertion (~269 ms) are two orders of magnitude slower than merge (~3.4 ms) and quicksort (~2.9 ms), consistent with Θ(n²) versus Θ(n log n). Extra memory samples stay small because we sort primitive object arrays in place (merge uses an auxiliary array, visible as a slightly higher memoryKb).

## Hash table

Capacity 83 (index-derived) at 20 000 keys has load factor ~241 and ~19 917 collisions; insert averages ~55 ms. Capacity 20 011 at the same n has load ~1.0 and 0 collisions on this key set, and is much faster. Collision count grows as soon as load factor exceeds 1, which is exactly what separate chaining predicts.

## BST vs red-black

Shuffled keys: BST height 37 versus RBT height 19 at 20 000 keys. Search/insert times are close because both are O(height); the height column is the clearer correctness evidence that balancing works. Sequential insert of 20 000 keys is avoided in the lab because the recursive BST would overflow the JVM stack — itself a demonstration of worst-case Θ(n) BST height.

## Heap dispatch

Insert grows slowly (~9 ms at 20 000). Extracting every node is Θ(n log n) and reaches ~90 ms at 20 000, matching a binary heap.

## Graph

BFS/DFS stay sub-millisecond on these sizes. Dijkstra grows with n. Kruskal at 500 vertices is the slowest cell (~113 ms) because it sorts all edges with selection sort. That is an implementation choice (no Java `PriorityQueue` in assessed code) and explains why Kruskal does not match the textbook O(E log E) with a heap sort.

## Graphs

Open the SVG files in a browser: `search_comparison.svg`, `sort_comparison.svg`, `hash_load_factor.svg`, `bst_vs_rbt.svg`, `heap_dispatch.svg`, `graph_algorithms.svg`.
