# Context file — Team 3 (Graph Algorithms) integration task
Repo: https://github.com/ITZ-PROF/Smart-Hospital-Operations-Optimizer_TEAM-NEXUS

Read this whole file before writing any code. It tells you what the project
already has, exactly what is missing, and the hard rules you must not break
while filling the gap. Your job is ONE thing: implement Team 3's graph
algorithms package so it compiles, integrates with the existing DB/model
layer, and satisfies the brief — without touching any other team's package.

---

## 1. What this project is

University of Ghana DCIT 204/308 joint semester project ("Ghana Smart
Service Operations Optimizer", localised as the **Korle-Bu Smart Hospital
Operations Optimizer**). Five teams of 2-3 each own one vertical slice of a
Java/Maven console app backed by SQLite. Two source PDFs define the rules:
`Hospital_Team_Directions.pdf` (per-team local-context brief) and
`Joint_DSA_Project_Brief.pdf` (the graded rubric/spec). Key facts pulled
from them:

- Java 17, Maven build (`pom.xml` already configured, don't touch it unless
  you need to add a dependency — and graph code should need none).
- **No built-in Java collections in assessed code.** `structures/`,
  `graph/`, and `algorithms/` packages may NOT import `java.util.HashMap`,
  `ArrayList`, `PriorityQueue`, `Stack`, `ArrayDeque`, `TreeMap`, etc.
  `db/` and `app/` (plumbing) are exempt. This applies fully to your graph
  package — you must build any list/queue/priority structure you need
  yourself, or reuse another team's custom structure (see §4).
- At least 3 algorithm parameters project-wide must derive from team
  members' index numbers. Team 3 (you) owns one of these: the **Dijkstra
  road-condition penalty weight**. You do not have an index number on file
  — ask the user for it before hardcoding the parameter, or implement the
  weight as a caller-supplied/configurable multiplier so it's trivially
  satisfiable once supplied.
- 6 mandatory trace tables project-wide; Team 3 owns **Dijkstra**
  (distance + predecessor table) and **Kruskal/Prim** (MST edge list +
  connectivity trace). These go in `docs/trace-tables/` as markdown,
  following the existing house style (see §6).
- Required performance experiment for graph algorithms: BFS/DFS/Dijkstra/MST
  runtime at 50, 100, 200, 500 locations/edges, ≥3 repeated runs, results
  logged (see `AlgorithmRunDAO`, §3).

## 2. What's already built (do not modify unless told to)

Repo layout (only what matters to you shown):

```
src/main/java/com/hospital/
  app/Main.java                — stub entrypoint, prints team checklist, unwired
  model/
    Location.java              — locationId, name, area, type, latitude, longitude
    Road.java                  — roadId, fromLocationId, toLocationId,
                                  distanceKm, travelTimeMin, roadConditionWeight
    Resource.java, ServiceRequest.java  — not needed for graph work
  db/
    DBConnection.java          — static getConnection() -> java.sql.Connection (SQLite)
    LocationDAO.java           — findAll() -> List<Location>, findById(String)
    RoadDAO.java                — findAll() -> List<Road>, findById(String)
    AlgorithmRunDAO.java        — insert(name, inputSize, timeNs, memoryKb, dateRun)
                                   for perf-experiment logging
    AuditEventDAO, ResourceDAO, ServiceRequestDAO, CSVImporter, CSVExporter,
    ValidationException          — Team 1's, not your concern
  structures/core/               — TEAM 2, generic, reusable by you:
    DynamicArray<T>              — get/set/insert/addLast/remove/resize, no
                                    java.util.* backing array (Object[])
    SinglyLinkedList<T>          — addFirst/addLast/insertAfter/remove, Iterable
    Stack<T>, Queue<T>, CircularQueue<T>, Deque<T>
  structures/indexing/            — TEAM 4, custom, reusable by you:
    DisjointSet                  — makeSet(int), find(int), union(int,int),
                                    union-by-rank + path compression.
                                    ⚠ operates on `int` elements only — no
                                    generics. You will map locationId (String,
                                    e.g. "L001") -> int index yourself before
                                    calling it. Needed for Kruskal.
    MinHeap                      — ⚠ NOT reusable as-is: it's hardcoded to
                                    MinHeapNode(patientId, patientName, wardName)
                                    for the ER triage use case, not a generic
                                    priority queue. Do not force-fit it into
                                    Dijkstra/Prim. See §5 for what to do instead.
    BinarySearchTree, RedBlackTree, BTree, HashTable, CustomSet, CustomMap
                                  — unrelated to graph work
  algorithms/                     — TEAM 5, unrelated to graph work
  graph/                          — TEAM 3 (YOU). Currently EMPTY (only .gitkeep).
```

Test tree mirrors `src/main` 1:1. `src/test/java/com/hospital/graph/` exists
but is empty — that's where your tests go.

Seed data (already loaded by Team 1's CSV importer into SQLite on startup,
also readable straight from CSV during development):

- `src/main/resources/data/locations.csv` — **55 locations**, IDs `L001`–`L055`.
  Types include `EMERGENCY`, `WARD`, `ICU`, `ENTRANCE`, `FACILITY`,
  `DEPARTMENT`, and `REFERRAL`. The 7 `REFERRAL` rows are the external
  hospitals: L046 Ridge Hospital, L047 La General Hospital, L048 37 Military
  Hospital, L049 Achimota Hospital, L050 Tema General Hospital, L054 Lister
  Hospital, L055 Trust Hospital. Everything else (`L001`–`L045`, `L051`–`L053`)
  is on the Korle-Bu campus.
- `src/main/resources/data/roads.csv` — **105 edges**, IDs `R001`–`R105`,
  columns `roadId,fromLocationId,toLocationId,distance,travelTime,roadConditionWeight`.
  Each pair appears **once** (undirected — treat the graph as undirected
  unless you decide a directed model fits better, in which case add both
  directions when building the adjacency structures).
  Watch for a Unicode minus sign (U+2212, not ASCII `-`) in `locations.csv`
  longitudes — Team 1's importer already replaces it before parsing, so DB
  reads via `LocationDAO`/`RoadDAO` are clean; only raw CSV parsing would hit it.
- Referral-network edges (Korle-Bu ↔ Ridge/La General/37 Military/
  Achimota/Tema General, plus links to Lister/Trust) run roughly `R086`–`R102`
  and use named-in-brief roads like Guggisberg Ave / Liberation Rd / N1
  Highway / Spintex Rd conceptually — the CSV doesn't label road names,
  only numeric weights, so any "named road" framing in your report is
  narrative dressing over the same weighted edges.

`AlgorithmRunDAO` and the `algorithm_runs` table are already wired for
Team 3's performance-experiment logging — call
`new AlgorithmRunDAO().insert("Dijkstra", inputSize, timeNs, memoryKb, dateRun)`
(etc.) after each timed run.

## 3. What you must build

Package: `com.hospital.graph` (main) / mirrored under
`com.hospital.graph` in `src/test/java/com/hospital/graph` (tests).

Per the brief and `README.md`'s own description of Team 3's scope:

1. **Graph** representation — BOTH adjacency list and adjacency matrix,
   over two logical layers:
   - Layer 1: Korle-Bu internal wards/corridors (non-REFERRAL locations).
   - Layer 2: Greater Accra referral network (Korle-Bu + the 7 REFERRAL
     locations and the roads connecting them).
   Decide whether this is one Graph class instantiated twice with filtered
   location/road sets, or a single graph with a way to query/filter by
   layer — either satisfies the brief; document the choice.
2. **BFS** — nearest available bed from A&E (L001): traverse ward-by-ward
   until a location matching some "has an empty bed" predicate is found.
   Must support excluding/removing an edge to simulate a corridor closure
   (e.g. Radiology corridor under maintenance) and show reachability
   changes.
3. **DFS** — full campus connectivity check: confirm every ward can still
   reach the Blood Bank location after a simulated corridor blockage.
4. **Dijkstra** — shortest ambulance route from an accident point (model
   as a location on/near the N1, or reuse an existing entrance/emergency
   location) to Korle-Bu vs. rerouting to Tema General (L050), under
   weighted road-condition penalties (the `roadConditionWeight` column,
   scaled by the index-number-derived penalty parameter from §1). Needs a
   distance table + predecessor table trace (mandatory trace #1).
5. **Prim & Kruskal** — minimum-cost oxygen/network line connecting all
   Korle-Bu wards. Build both MSTs, compare total cost and edge order.
   Kruskal must use Team 4's `DisjointSet` (int-keyed — map locationId
   strings to a dense int index range, e.g. via a small array/your own
   linked structure, not `java.util.HashMap`) and produce the required
   Kruskal connectivity trace (mandatory trace #2), superseding the
   placeholder trace currently in `docs/trace-tables/DisjointSet_Trace.md`
   (that doc explicitly says "the project has no kruskal() method or
   weighted-edge type" yet — your work makes that statement obsolete for
   the graph layer; leave Team 4's file alone, your trace goes in its own
   file).

## 4. Reuse rules — what you may and may not import

- Allowed from your own package: anything you write in `com.hospital.graph`.
- Allowed reuse: `com.hospital.structures.core.DynamicArray<T>`,
  `SinglyLinkedList<T>`, `Stack<T>`, `Queue<T>`, `Deque<T>` (Team 2) and
  `com.hospital.structures.indexing.DisjointSet` (Team 4) — these are
  already-approved custom structures, using them is exactly the kind of
  cross-team composition the brief rewards.
- Allowed reuse: `com.hospital.model.Location`, `com.hospital.model.Road`,
  `com.hospital.db.LocationDAO`, `com.hospital.db.RoadDAO`,
  `com.hospital.db.DBConnection`, `com.hospital.db.AlgorithmRunDAO`.
- Not allowed: `java.util.HashMap`, `ArrayList`, `PriorityQueue`, `Stack`,
  `ArrayDeque`, `TreeMap`, `LinkedList`, or any other `java.util` collection,
  anywhere inside `com.hospital.graph` assessed logic (test code is not
  assessed the same way, but prefer consistency there too).
- `com.hospital.structures.indexing.MinHeap` is NOT generically reusable
  (see §2) — do not adapt Dijkstra/Prim to fake patient fields into it.
  Instead, either (a) write a small generic `int`- or `double`-keyed
  array-backed min-priority structure inside `com.hospital.graph` (a
  handful of lines, same sift-up/down pattern as `MinHeap` but keyed on
  distance/cost), or (b) implement Dijkstra/Prim with a plain O(V²)
  array-scan for the minimum (perfectly fine at this graph's scale — 55
  nodes, 105 edges — and simpler to trace by hand for the trace tables).
  Pick one approach and be consistent between Dijkstra and Prim.

## 5. Suggested internal shape (adjust freely, this isn't gospel)

```
com.hospital.graph/
  Graph.java          — build from List<Location> + List<Road>; exposes
                         adjacency list (custom structures) and adjacency
                         matrix (double[][] keyed by an internal int index
                         you assign per locationId); addEdge/removeEdge for
                         corridor-closure simulation; layer filtering.
  BFS.java             — nearestAvailableBed(Graph, start, predicate) or similar
  DFS.java             — connectivityCheck(Graph, start, target)
  Dijkstra.java        — shortestPath(Graph, start, target, penaltyWeight)
                          -> distances + predecessors
  MST.java or Prim.java / Kruskal.java — MST builders, both returning
                         edge list + total cost for comparison
  GraphIndex.java (optional) — the locationId<->int mapping helper, if you
                         don't want to inline it in Graph.java
```

Wire a demo path through `Main.java` alongside the other teams' stub lines
(don't delete their lines, add yours) so an examiner can run the console
app and see Team 3's algorithms execute against the live DB-loaded graph.

## 6. Evidence to produce (deliverables, not just code)

- `docs/trace-tables/Dijkstra_Trace.md` — distance table + predecessor
  path table for at least one real accident-location → Korle-Bu vs.
  → Tema General comparison, using real weights from `roads.csv`. Match the
  markdown table style already used in `docs/trace-tables/DisjointSet_Trace.md`
  and `docs/trace-tables/BST_Trace.md` (step-by-step table, short prose
  explaining the rule being traced).
- `docs/trace-tables/Kruskal_Prim_Trace.md` — MST edge list, total cost,
  and the disjoint-set accept/reject connectivity trace for Kruskal, plus
  Prim's edge order for comparison.
- `docs/trace-tables/BFS_DFS_Trace.md` (or fold into one of the above) —
  reachability trace under a simulated corridor closure/blockage, per the
  brief's requirement for a "trace table and graph diagram" for BFS/DFS.
- Unit tests in `src/test/java/com/hospital/graph/`: normal case, boundary
  case (single node, disconnected graph, empty graph), invalid input case
  for each structure/algorithm — this project's rubric counts tests
  per-structure, matching the pattern in `src/test/java/com/hospital/structures/**`.
- Performance experiment: run BFS/DFS/Dijkstra/MST at 50, 100, 200, 500
  locations/edges (subsets of the seed data, or synthetic extensions if
  the real dataset is smaller than a tier), 3 runs each, log via
  `AlgorithmRunDAO`, export/summarize for `docs/performance/`.

## 7. Build & verify commands

```bash
mvn compile                                    # compiles everything
mvn test                                       # runs all unit tests
mvn test -Dtest=com.hospital.graph.*           # just your package, once it exists
mvn package && java -jar target/hospital-dsa-project.jar
```

JDK 17+, Maven 3.9+ required. SQLite DB and schema are created/seeded by
Team 1's code path (`DBConnection` + `CSVImporter`), so a full `mvn package`
+ run should exercise your graph code against real loaded data if wired
through `Main.java`.

## 8. Open questions for the user (Richmond) before/while implementing

- Actual index number(s) to derive the Dijkstra road-condition penalty
  weight from (brief requires this to be traceable to a real index number,
  not an arbitrary constant).
- Whether to model the "accident on the N1" as a new synthetic location or
  reuse an existing entrance/emergency location as its proxy.
- Whether teammates (AMANFU, DUNCAN KWAKU / DAWSON, KWAME OFEI per
  `README.md`'s team table) are also contributing code, in which case
  agree on file split before implementation to avoid merge conflicts —
  README's suggested branch name for this work is `team3/dijkstra` (and
  similarly `team3/bfs-dfs`, `team3/mst`).
