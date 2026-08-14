# Team 5 Optimisation Trace

## Greedy bed/ventilator allocation

`GreedyResourceAllocator.allocate` copies the admissions, uses `AdmissionSort.insertionSort` to order them by urgency descending (then patient ID), and assigns a patient when the required resource units fit in the remaining capacity.

| Rank | Patient | Urgency | Units required | Remaining before | Decision | Remaining after |
|---:|---|---:|---:|---:|---|---:|
| 1 | ID 2 | 5 | 1 | 2 | allocate | 1 |
| 2 | ID 3 | 4 | 1 | 1 | allocate | 0 |
| 3 | ID 1 | 2 | 2 | 0 | unallocated | 0 |

### Documented greedy limitation

With capacity 2, ID 1 has urgency 10 and needs 2 units; IDs 2 and 3 have urgencies 9 and 8 and need 1 unit each. The greedy rule selects ID 1, treating one patient. A different objective—maximising patients treated—would select IDs 2 and 3, treating two patients. This is intentional evidence that the urgency-first strategy does not optimise every possible objective.

## Dynamic-programming surgery schedule

For regular hours 4 and overtime hours 1, total capacity is 5. Requests are S1 `(2 hours, benefit 6)`, S2 `(3, 10)`, and S3 `(2, 7)`. `benefitTable[row][hours]` is the best benefit using the first `row` requests.

| Requests considered | 0 | 1 | 2 | 3 | 4 | 5 |
|---|---:|---:|---:|---:|---:|---:|
| none | 0 | 0 | 0 | 0 | 0 | 0 |
| S1 | 0 | 0 | 6 | 6 | 6 | 6 |
| S1, S2 | 0 | 0 | 6 | 10 | 10 | 16 |
| S1, S2, S3 | 0 | 0 | 7 | 10 | 13 | 17 |

Backtracking from `table[3][5] = 17` selects S3 (leaving 3 hours), then S2 (leaving 0): the reconstructed schedule is `[S2, S3]` with 5 hours used.

## Complexity

Greedy allocation uses insertion sort plus one allocation pass: `O(n²)` time and `O(n)` result/copy space. The DP scheduler has `n` requests and capacity `H = regularHours + overtimeHours`: it uses `O(nH)` time and `O(nH)` space, including the returned table required for correctness evidence.
