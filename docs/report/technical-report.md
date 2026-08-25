# Korle-Bu Smart Hospital Operations Optimizer

**DCIT 204/308 Joint DSA Semester Project — TEAM NEXUS**  
**Local context:** Korle-Bu Teaching Hospital and the Greater Accra referral network  
**Language:** Java 17 · **Database:** SQLite · **Build:** Maven

## 1. Cover / context

This system stores Korle-Bu operational data in SQLite, reloads it into student-written data structures, and runs search, scheduling, graph and optimisation algorithms that an examiner can invoke from a console menu without editing source.

Team list is in the repository `README.md`.

## 2. Problem statement

See [M1_problem_specification.md](../M1_problem_specification.md). The platform answers next-request, shortest-route, reachability, budget-selection and scalability questions under Ghana-local constraints (campus corridors, N1 referral traffic, urgency, limited beds/theatre hours).

## 3. Dataset and schema

Evidence: [dataset-evidence.md](../dataset-evidence.md). Schema: `src/main/resources/db/schema.sql`. Seed CSVs: `src/main/resources/data/`.

| Entity | Minimum | Actual |
|---|---:|---:|
| Locations | 50 | 55 |
| Roads | 100 | 105 |
| Service requests | 300 | 310 |
| Resources | 30 | 40 |
| Algorithm runs | 30 | produced by menu option 10 |

## 4. Architecture

CSV → `HospitalBootstrap` / `CSVImporter` → SQLite → DAOs → `Graph`, `IndexingEngine`, queues/heaps → algorithms → console + `algorithm_runs` / `audit_events`. Custom structures live in `structures/` and `graph/`; JDBC plumbing in `db/` may use `java.util.List`.

## 5. Data structures

Custom implementations (no prohibited Java collections in assessed packages): DynamicArray, SinglyLinkedList+iterator, Stack, Queue, CircularQueue, Deque, UrgencyHeap (max), MinHeap (patient-id demo), BST, left-leaning Red-Black insert, B-tree insert/split, HashTable with chaining, CustomSet/Map, DisjointSet, Graph adjacency list **and** matrix.

## 6. Algorithms

Linear/binary search; selection/insertion/merge/quicksort; FIFO/circular/deque/heap dispatch; BFS, DFS, Dijkstra (penalty 1.47), Prim, Kruskal (DisjointSet); greedy allocation; DP knapsack-style theatre schedule; brute-force subset search for *n* ≤ 12.

## 7. Correctness evidence

- Unit tests: `mvn test` (well above 40).
- Required traces: binary search, insertion/merge/quicksort (`Team5_SearchSort_Trace.md`); Dijkstra (`Dijkstra_Trace.md`); Kruskal/Prim (`Kruskal_Prim_Trace.md`); DP (`Team5_Optimisation_Trace.md`).
- Proof sketches: [proof-sketches.md](../proof-sketches.md).
- Counterexamples: unsorted binary search; greedy vs brute-force patient count.

## 8. Performance analysis

`com.hospital.benchmark.PerformanceLab` runs each experiment three times, records averages and a memory sample, writes CSV + SVG under `docs/performance/`, and inserts rows into `algorithm_runs`. Machine specification is `docs/performance/machine_spec.txt`.

See [INTERPRETATION.md](../performance/INTERPRETATION.md) for theory-versus-observed discussion. Raw timings: `docs/performance/*.csv`. Line graphs: `docs/performance/*.svg`. Machine spec: `docs/performance/machine_spec.txt`. After `--experiments`, SQLite holds 190 algorithm_runs rows (minimum 30).

## 9. Database integration

`Main` always calls `HospitalBootstrap.ensureReady()`. Live write/undo is menu option 14 (`UndoManager` → `audit_events`). CSV import loads from the classpath so a packaged JAR works.

## 10. Responsible algorithm selection

Use binary search only on sorted keys. Use heap dispatch when urgency matters; FIFO when fairness by arrival matters. Use Dijkstra with a positive penalty, not BFS, when road condition weights matter. Use DP or brute force (small *n*) when greedy’s objective is the wrong one. Do not run brute force beyond 12 requests.

## 11. Individual contribution / oral defense

See [ORAL_DEFENSE.md](../ORAL_DEFENSE.md) and [DEVELOPMENT_LOG.md](../DEVELOPMENT_LOG.md).

## 12. References

Cormen et al., *Introduction to Algorithms*; Sedgewick & Wayne, *Algorithms*; Goodrich, Tamassia & Goldwasser, *Data Structures and Algorithms in Java*; the DCIT 204/308 project brief.
