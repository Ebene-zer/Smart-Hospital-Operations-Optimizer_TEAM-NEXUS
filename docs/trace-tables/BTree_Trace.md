# B-Tree Trace — Team 4

**Korle-Bu use case:** a compact multi-key patient index. `BTree` fixes `MINIMUM_DEGREE = 2`, therefore a node holds at most `2t − 1 = 3` patient IDs. A full child is split before the new key is inserted into it.

## Step-by-step insertion and split

| Step | Inserted record | Tree after the operation | Code path |
|---:|---|---|---|
| 1 | KB-104 — Ama Owusu — Emergency Unit | leaf root `[104]` | create first root |
| 2 | KB-112 — Kofi Badu — Surgical Ward | leaf root `[104, 112]` | `insertNonFull` leaf shift/append |
| 3 | KB-120 — Efua Tetteh — Maternity Ward | leaf root `[104, 112, 120]` | root becomes full |
| 4 | KB-108 — Kojo Mensah — ICU | root `[112]`; children `[104, 108]` and `[120]` | full root: `splitChild(newRoot, 0)` promotes 112, then inserts 108 left |
| 5 | KB-116 — Akua Nyarko — Medical Ward | root `[112]`; children `[104, 108]` and `[116, 120]` | descend to right non-full leaf |

Split demonstration at step 4:

```text
Before:  [104 | 112 | 120]     (full leaf root)

After:             [112]
                  /     \
              [104]    [120]

Insert 108:        [112]
                  /     \
          [104 | 108]  [120]
```

The median (`112`) is copied into the new parent, the lower key remains in the old child, and the upper key becomes the new right child—matching `splitChild` for degree 2.

## Search trace

| Search step for `search(116)` | Node examined | Decision |
|---:|---|---|
| 1 | root `[112]` | 116 > 112, recurse to child index 1 |
| 2 | leaf `[116, 120]` | 116 equals key at index 0; return this `BTreeNode` |

The `search` method returns the node containing the key (not a patient record). This branching reduces the number of node visits compared with a binary tree while preserving sorted IDs within each node. Inserting an existing ID updates its name and ward rather than adding another key.
