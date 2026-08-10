# Disjoint Set Trace — Team 4

**Korle-Bu use case:** check whether proposed links between hospital departments join separate service networks. The implementation supplies `makeSet`, `find`, and `union`; `union` uses rank and `find` applies path compression.

## `makeSet` and union-by-rank trace

Department labels below are report labels for integer elements passed to the actual API.

| Step | Operation | Parent/rank effect |
|---:|---|---|
| 1 | `makeSet(10)` — Surgery | `10 → 10`, rank 0 |
| 2 | `makeSet(20)` — Emergency | `20 → 20`, rank 0 |
| 3 | `makeSet(30)` — Laboratory | `30 → 30`, rank 0 |
| 4 | `makeSet(40)` — Pharmacy | `40 → 40`, rank 0 |
| 5 | `makeSet(50)` — Radiology | `50 → 50`, rank 0 |
| 6 | `union(10, 20)` | equal ranks: `20 → 10`; rank(10) becomes 1 |
| 7 | `union(30, 40)` | equal ranks: `40 → 30`; rank(30) becomes 1 |
| 8 | `union(10, 30)` | equal ranks: `30 → 10`; rank(10) becomes 2 |

`makeSet` ignores an element that already exists. In an equal-rank union, the code attaches the **second** representative to the first and increments the first root’s rank.

## Path compression example

Before `find(40)`, the parent chain is `40 → 30 → 10`.

| Find recursion | Representative returned | Parent after return |
|---|---:|---|
| `findRepresentative(10)` | 10 | `10 → 10` |
| return to node 30 | 10 | `30 → 10` (already so) |
| return to node 40 | 10 | `40 → 10` (compressed) |

Thus `find(40)` returns `10`, and future connectivity checks reach the representative directly.

## Kruskal-style connectivity trace

The project has no `kruskal()` method or weighted-edge type, so this is the required Kruskal evidence expressed with its available `find` and `union` operations after externally ordering proposed department links by distance/cost.

| Ordered link | Weight | `find` representatives before union | Decision | Connectivity state |
|---|---:|---|---|---|
| Surgery (10) — Emergency (20) | 2 | 10, 20 | accept; `union(10,20)` | `{10,20}` |
| Laboratory (30) — Pharmacy (40) | 3 | 30, 40 | accept; `union(30,40)` | `{10,20}`, `{30,40}` |
| Surgery (10) — Laboratory (30) | 5 | 10, 30 | accept; `union(10,30)` | `{10,20,30,40}` |
| Emergency (20) — Laboratory (30) | 6 | 10, 10 | reject: same set (cycle) | unchanged |
| Pharmacy (40) — Radiology (50) | 7 | 10, 50 | accept; `union(40,50)` | `{10,20,30,40,50}` |

All five departments are connected after the final accepted link. The decision rule is precisely the disjoint-set connectivity rule: only union endpoints whose representatives differ.
