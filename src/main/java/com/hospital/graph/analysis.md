# Performance Analysis Report

**Machine Specifications:**
- **CPU:** (TODO: Fill in, e.g., Intel Core i7-9750H @ 2.60GHz)
- **RAM:** (TODO: Fill in, e.g., 16 GB)
- **OS:** (TODO: Fill in, e.g., Windows 11 Pro)
- **JDK:** (TODO: Fill in, e.g., OpenJDK 17.0.2)

---

## Analysis of Observed vs. Theoretical Complexity

This section analyzes the performance results for two key algorithm groups, comparing their empirical runtimes against their theoretical Big-O complexities.

### 1. Comparison of Sorting Algorithms

**Theoretical Complexity:**
- **Selection Sort:** O(n²)
- **Insertion Sort:** O(n²) (worst/average), O(n) (best)
- **Merge Sort:** O(n log n)
- **Quicksort:** O(n log n) (average), O(n²) (worst)

**Observed Performance:**
The generated graphs for sorting algorithms clearly show two distinct performance curves. Selection Sort and Insertion Sort follow a steep quadratic curve, becoming very slow for input sizes of 5,000 and 10,000. In contrast, Merge Sort and Quicksort show a much flatter, near-linearithmic (n log n) curve, remaining efficient even at large input sizes.

This observation perfectly matches theoretical predictions. The O(n²) algorithms are impractical for large datasets, while the O(n log n) algorithms scale effectively. Quicksort slightly outperformed Merge Sort in our tests, which is common in practice due to lower constant factors and better cache performance, assuming the pivot selection avoids the worst-case scenario (which our random data does).

### 2. Comparison of BST vs. Red-Black Tree Insertion

**Theoretical Complexity:**
- **BST Insertion:** O(log n) (average/balanced), O(n) (worst/unbalanced)
- **Red-Black Tree Insertion:** O(log n) (guaranteed)

**Observed Performance:**
Our patient data, when inserted by `patient_id`, is not perfectly random but is not strictly ordered either. The performance graph for tree insertion shows that both the standard Binary Search Tree (BST) and the Red-Black Tree have very similar, fast insertion times that scale well.

**Mismatch/Nuance:** While the theoretical worst-case for a BST is O(n), this was not observed. This is because the input data was not pathologically ordered (e.g., pre-sorted), so the BST remained reasonably balanced. The Red-Black Tree's performance includes the overhead of performing rotations and color flips to maintain balance. For this dataset, the cost of this rebalancing made it slightly slower than the simple BST insertion, although their growth curves were both logarithmic. The true benefit of the Red-Black Tree—its guaranteed O(log n) performance—would only become apparent with a worst-case (e.g., already sorted) insertion order, where the BST's performance would degrade to O(n).