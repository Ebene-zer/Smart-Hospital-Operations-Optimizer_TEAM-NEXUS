# Team 5 Search and Sort Trace

**Korle-Bu use case:** search a daily admission log and order patients for discharge planning, triage review, and a paediatric audit. `PatientAdmission` carries `patientId`, admission time, urgency, and age.

## Search trace

| Step | Linear search log (unsorted) | Search for ID 10 | Result |
|---:|---|---|---|
| 1 | `[30, 10, 20]` | compare 30 | continue |
| 2 | `[30, 10, 20]` | compare 10 | index 1 |

`binarySearchByPatientId` validates sorted input before looking up a patient. Passing `[20, 10]` throws `IllegalArgumentException`; this is the required invalid-precondition counterexample.

| Binary step | Sorted roster | Low, middle, high | Decision |
|---:|---|---|---|
| 1 | `[10, 20, 30, 40, 50]` | 0, 2, 4 | 30 is too high for target 20 |
| 2 | unchanged | 0, 0, 1 | 10 is too low |
| 3 | unchanged | 1, 1, 1 | found 20 |

## Sort trace

Input records are `[(ID 1, time 9, urgency 2, age 30), (ID 2, time 7, urgency 5, age 20), (ID 3, time 11, urgency 4, age 40)]`. Urgency is sorted descending; admission time and age ascending. Equal primary values use patient ID as the deterministic tie-breaker.

| Algorithm | Criterion | Meaningful intermediate state | Final patient-ID order |
|---|---|---|---|
| Selection | admission time | choose ID 2 as the minimum and swap with ID 1 | `[2, 1, 3]` |
| Insertion | urgency | insert ID 2 before ID 1; then ID 3 between them | `[2, 3, 1]` |
| Merge | age | merge `[1]` and `[2]` to `[2, 1]`, then merge ID 3 | `[2, 1, 3]` |
| Quicksort | admission time | pivot ID 3 leaves `[1, 2]` left; pivot ID 2 partitions it | `[2, 1, 3]` |

## Complexity

| Algorithm | Best | Average | Worst | Extra space |
|---|---:|---:|---:|---:|
| Linear search | `O(1)` | `O(n)` | `O(n)` | `O(1)` |
| Binary search | `O(1)` | `O(log n)` | `O(log n)` | `O(1)` |
| Selection sort | `O(n²)` | `O(n²)` | `O(n²)` | `O(1)` |
| Insertion sort | `O(n)` | `O(n²)` | `O(n²)` | `O(1)` |
| Merge sort | `O(n log n)` | `O(n log n)` | `O(n log n)` | `O(n)` |
| Quicksort | `O(n log n)` | `O(n log n)` | `O(n²)` | `O(log n)` recursion average |

## Primitive-operation evidence

The counts below follow the three-record trace above and count patient-field comparisons and array-reference writes. They intentionally exclude loop-index arithmetic and method-call overhead, so the measured operations are clear and reproducible from the implementation.

| Algorithm | Trace input and criterion | Patient comparisons | Array-reference writes | Result |
|---|---|---:|---:|---|
| Merge sort | IDs `[1, 2, 3]`, age | 3 | 10 | `[2, 1, 3]` |
| Quicksort | IDs `[1, 2, 3]`, admission time | 3 | 8 (four swaps) | `[2, 1, 3]` |

For merge sort, the two merge stages perform one and two patient comparisons respectively; each writes every merged value to the auxiliary array and then back. For quicksort, the first pivot (ID 3) causes two self-swaps plus its final self-swap; the left two-record partition performs one comparison and one swap. Each `swap` writes two array positions; its local temporary assignment is not an array-reference write. These counts correspond directly to `mergeSort`, `quickSort`, and `swap` in `AdmissionSort`.
