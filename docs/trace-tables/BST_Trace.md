# Binary Search Tree Trace — Team 4

**Korle-Bu use case:** index patients by numeric patient ID for roster lookup. Each insertion calls `insert(int, String, String)`; IDs smaller than a node go left and larger IDs go right. Duplicate IDs are ignored by the BST implementation.

## Inserted patient roster and search path

| Insert order | Patient record | Position created |
|---:|---|---|
| 1 | KB-450 — Akosua Mensah — Surgical Ward | root |
| 2 | KB-320 — Kofi Asare — Emergency Unit | left of 450 |
| 3 | KB-610 — Yaa Boateng — Maternity Ward | right of 450 |
| 4 | KB-280 — Kwame Osei — Paediatric Ward | left of 320 |
| 5 | KB-390 — Abena Serwaa — Medical Ward | right of 320 |
| 6 | KB-570 — Kojo Antwi — ICU | left of 610 |

| Search step for `searchWithTrace(570)` | Comparison | Printed direction/result |
|---:|---|---|
| 1 | 570 > 450 | `Root (450) -> Right (610)` |
| 2 | 570 < 610 | `-> Left (570)` |
| 3 | 570 = 570 | `-> Found!` |

The trace follows the code's recursive comparisons exactly: `450 → 610 → 570`. A missing ID would stop at a `null` child and `searchWithTrace` prints `-> Not Found!`.

## Sorted `inOrderTraversal()` output

| Order | Output record |
|---:|---|
| 1 | `ID: KB-280   | Name: Kwame Osei         | Location: Paediatric Ward` |
| 2 | `ID: KB-320   | Name: Kofi Asare         | Location: Emergency Unit` |
| 3 | `ID: KB-390   | Name: Abena Serwaa       | Location: Medical Ward` |
| 4 | `ID: KB-450   | Name: Akosua Mensah      | Location: Surgical Ward` |
| 5 | `ID: KB-570   | Name: Kojo Antwi         | Location: ICU` |
| 6 | `ID: KB-610   | Name: Yaa Boateng        | Location: Maternity Ward` |

In-order traversal visits **left → node → right**, so the output is ascending by patient ID. The implementation is an ordinary, unbalanced BST; lookup is efficient when the tree is reasonably balanced but may become linear for an ordered insertion sequence.
