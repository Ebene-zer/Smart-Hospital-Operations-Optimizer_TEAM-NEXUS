# Hash Table Trace — Team 4

**Korle-Bu use case:** direct patient-ID lookup. This trace uses the supported `new HashTable(5)` constructor to make collisions visible. The implementation calculates `index = (patientId & 0x7fffffff) % 5` and resolves collisions by inserting a new entry at the **head** of that bucket’s linked chain.

## Insertion and bucket contents

| Step | `put` patient record | Hash index | Bucket contents after insertion (`head → tail`) |
|---:|---|---:|---|
| 1 | KB-105 — Ama Owusu — Emergency Unit | 0 | `0: 105`; `1–4: empty` |
| 2 | KB-110 — Kofi Asare — Surgical Ward | 0 | `0: 110 → 105`; `1–4: empty` |
| 3 | KB-115 — Esi Addo — ICU | 0 | `0: 115 → 110 → 105`; `1–4: empty` |
| 4 | KB-101 — Yaw Mensah — Medical Ward | 1 | `0: 115 → 110 → 105`; `1: 101`; `2–4: empty` |

The second and third insertions collide with the current chain in bucket 0. `put` first scans for an identical ID; because these IDs differ, each new `HashTableEntry` is linked in front. A repeated ID would update its name and ward in place instead of extending the chain.

## Load factor and collision statistics

| Measure | Calculation | Result |
|---|---|---:|
| Capacity | constructor argument | 5 buckets |
| Entries inserted | 105, 110, 115, 101 | 4 |
| Load factor | entries ÷ buckets = 4 ÷ 5 | 0.80 |
| Used buckets | buckets 0 and 1 | 2 |
| Colliding insertions | 110 and 115 entered occupied bucket 0 | 2 |
| Collision rate | 2 ÷ 4 insertions | 50% |
| Longest chain | bucket 0 | 3 entries |

These statistics are trace calculations: the class does not expose a size or collision-counter method. Retrieval of KB-105 follows bucket 0’s chain `115 → 110 → 105`; thus separate chaining preserves correct lookups even at the displayed load factor.
