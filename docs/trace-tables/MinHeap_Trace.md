# Min-Heap Trace — Team 4

**Korle-Bu use case:** dispatch the patient with the lowest numeric ID first. `MinHeap` stores records in an array and compares only `patientId`; `insert` appends then `siftUp`, while `extractMin` replaces the root with the last element then `siftDown`.

## Heap after every insertion

Array order is level order; the first element is the next dispatch candidate.

| Step | Inserted patient record | `siftUp` movement | Heap array after insertion |
|---:|---|---|---|
| 1 | KB-440 — Ama Owusu — Emergency Unit | root; no parent | `[440]` |
| 2 | KB-230 — Kofi Asare — ICU | 230 swaps with 440 | `[230, 440]` |
| 3 | KB-360 — Esi Addo — Surgical Ward | 360 ≥ 230; stop | `[230, 440, 360]` |
| 4 | KB-150 — Yaw Mensah — Paediatric Ward | 150 ↔ 440, then 150 ↔ 230 | `[150, 230, 360, 440]` |
| 5 | KB-310 — Abena Ofori — Medical Ward | 310 ≥ 230; stop | `[150, 230, 360, 440, 310]` |

## `heapify()` and dispatch trace

Calling the public `heapify()` on this already-valid heap runs `siftDown` from `(5 / 2) − 1 = 1` down to 0.

| Heapify step | Children inspected | Resulting array |
|---:|---|---|
| index 1 (230) | 440, 310; 230 is smallest | `[150, 230, 360, 440, 310]` |
| index 0 (150) | 230, 360; 150 is smallest | `[150, 230, 360, 440, 310]` |

| `extractMin()` stage | Operation | Heap array |
|---:|---|---|
| dispatch | returns KB-150 — Yaw Mensah — Paediatric Ward | replace root with last: `[310, 230, 360, 440]` |
| sift-down 1 | compare 310 with 230 and 360; swap with 230 | `[230, 310, 360, 440]` |
| sift-down 2 | 310 ≤ 440; stop | `[230, 310, 360, 440]` |

**Final heap:** `[230, 310, 360, 440]`. The code has no priority field: “minimum” is strictly the smallest patient ID, and equal IDs are permitted as separate entries.
