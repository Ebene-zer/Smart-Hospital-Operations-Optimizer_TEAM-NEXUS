# M1 — Problem specification, I/O and five major operations

## Problem statement

Design a Korle-Bu operations optimizer that (1) stores locations, roads, service requests and resources in SQLite, (2) reloads those records into custom data structures, and (3) answers:

- Which request is next under FIFO, circular rotation, deque (trauma-to-front) and urgency-heap rules?
- What is the fastest ambulance route under a road-condition penalty?
- Which wards remain reachable if a corridor is closed?
- Which patients/surgeries fit a bed or theatre-hour budget?
- How do the algorithms scale as *n* grows?

**Assumptions.** Campus corridors are bidirectional. Referral edges are included only in the “full” graph. Urgency order is CRITICAL > HIGH > MEDIUM > LOW. Binary search is defined only on arrays sorted by `patientId`.

**Constraints.** Assessed logic may not use `HashMap`, `TreeMap`, `PriorityQueue`, `java.util.Stack` or `ArrayDeque`. Brute force is capped at *n* ≤ 12.

## Input / output

| Operation | Input | Output |
|---|---|---|
| CSV load | classpath CSVs | SQLite tables |
| Dispatch | request batch | ordered IDs per rule |
| Dijkstra | source id, penalty | dist[], prev[], path string |
| MST | campus graph | edge list + total cost |
| Greedy / DP / brute | admissions or surgeries + capacity | selected subset |

## Five major operations (pseudocode)

### 1. Database bootstrap
```
ensureReady():
  initializeSchema()
  importAll(locations, roads, resources, service_requests)
```

### 2. Urgency-heap dispatch
```
insert(request):
  append to heap array; siftUp by urgency then earlier time
extractMax():
  swap root with last; siftDown; return old root
```

### 3. Binary search with precondition
```
require sorted by patientId else throw
low, high := 0, n-1
while low ≤ high:
  mid := (low+high)/2
  compare admissions[mid].patientId with target
```

### 4. Dijkstra
```
dist[s] := 0; others ∞
repeat n times:
  u := unvisited vertex of smallest dist
  for each neighbour v of u:
    relax dist[v] using distance + penalty * roadConditionWeight
```

### 5. DP theatre scheduler
```
table[i][h] := max benefit using first i surgeries and h hours
reconstruct by walking table[n][H] backwards
```

Flow: CSV → SQLite → DAO arrays → Graph / Index / Queues → algorithm → console + `algorithm_runs`.
