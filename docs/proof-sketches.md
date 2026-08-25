# Proof sketches (brief §10 — at least three)

## 1. Loop invariant — binary search (`AdmissionSearch.binarySearchByPatientId`)

**Precondition.** `admissions[0..n)` is sorted non-decreasing by `patientId`. The implementation checks this before searching.

**Invariant.** If the target occurs in the array, it occurs in `admissions[low..high]`.

**Initialization.** `low = 0`, `high = n-1`, so the window is the whole array.

**Maintenance.** Let `mid = low + (high-low)/2`. Because the array is sorted:

- if `admissions[mid].patientId < target`, no index `≤ mid` can hold the target, so `low := mid+1`;
- if `admissions[mid].patientId > target`, no index `≥ mid` can hold it, so `high := mid-1`;
- equality returns `mid`.

**Termination.** Either a match is returned, or `low > high` and the window is empty, so the target is absent (`-1`).

**Counterexample for the precondition.** `[20, 10]` is unsorted; the method throws `IllegalArgumentException` rather than returning a silently wrong index.

## 2. Induction / recurrence — merge sort

Merge sort splits an array of length *n* into two halves, sorts each, and merges.

**Recurrence.** `T(n) = 2 T(n/2) + Θ(n)`, `T(1) = Θ(1)`. By the Master Theorem this is `Θ(n log n)` in every case.

**Correctness (induction on n).** A 1-element array is sorted. Assume both halves of size `< n` are sorted. The merge walk always emits the smaller remaining head, so the output is sorted and contains exactly the union of the two halves.

## 3. Greedy vs optimum — bed allocation

The greedy rule sorts by urgency descending and packs while units remain. It is **not** optimal for “maximise number of patients treated”.

**Counterexample (capacity 2).** Patient 1 needs 2 units at urgency 10; patients 2 and 3 need 1 unit each at urgencies 9 and 8. Greedy takes patient 1 (one treated). The patient-count optimum takes 2 and 3 (two treated). `BruteForceAllocator` enumerates all `2^n` subsets for *n* ≤ 12 and finds that optimum; this is why brute force is demonstrated only on small *n* (`2^20` already exceeds a million subsets).

**DP idea.** `SurgeryScheduler` fills `table[i][h] = max(skip i, take i if duration ≤ h)`. This is the standard 0-1 knapsack recurrence; reconstruction walks from `table[n][H]` whenever the value differs from the row above.
