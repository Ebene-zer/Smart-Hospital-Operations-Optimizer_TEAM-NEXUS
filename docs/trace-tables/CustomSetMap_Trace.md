# CustomSet and CustomMap Trace — Team 4

**Korle-Bu use case:** track unique patient IDs in a set and associate those IDs with their current wards in a map. `CustomMap` uses a generic array of linked entry chains; `CustomSet` delegates its membership storage to `CustomMap` with a private marker value. `CustomMap` rejects null keys and permits null values. `CustomSet` rejects null elements because it uses them as `CustomMap` keys.

## `CustomMap` collision, update, and removal trace

Using `new CustomMap<String, String>(5)`, the strings `Aa` and `BB` have the same Java `hashCode`, so they exercise one chain. The bucket index is `(key.hashCode() & 0x7fffffff) % 5`; a new key is linked at the head.

| Step | Operation | Result | Size | Relevant bucket (`head → tail`) |
|---:|---|---|---:|---|
| 1 | `put("Aa", "Emergency")` | new entry; returns `null` | 1 | `Aa: Emergency` |
| 2 | `put("BB", "ICU")` | collision; new head; returns `null` | 2 | `BB: ICU → Aa: Emergency` |
| 3 | `get("Aa")` | scan `BB`, then return `Emergency` | 2 | unchanged |
| 4 | `put("Aa", "Theatre")` | scan chain; replace value; returns `Emergency` | 2 | `BB: ICU → Aa: Theatre` |
| 5 | `remove("BB")` | remove head; returns `ICU` | 1 | `Aa: Theatre` |
| 6 | `remove("missing")` | no matching entry; returns `null` | 1 | unchanged |

## `CustomSet` membership trace

`CustomSet` uses `CustomMap.put` and `CustomMap.remove` with a private marker value, so duplicate additions and missing removals leave the size unchanged.

| Step | Operation | Result | Size | Members |
|---:|---|---|---:|---|
| 1 | `add("KB-101")` | `true` | 1 | `KB-101` |
| 2 | `add("KB-102")` | `true` | 2 | `KB-101`, `KB-102` |
| 3 | `add("KB-101")` | `false` (already present) | 2 | unchanged |
| 4 | `contains("KB-102")` | `true` | 2 | unchanged |
| 5 | `remove("KB-101")` | `true` | 1 | `KB-102` |
| 6 | `remove("KB-999")` | `false` (missing) | 1 | unchanged |
