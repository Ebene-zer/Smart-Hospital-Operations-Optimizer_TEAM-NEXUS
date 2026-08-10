# Red-Black Tree Trace — Team 4

**Korle-Bu use case:** maintain a patient-ID index while preventing the long chains possible in the ordinary BST. The repository uses left-leaning red-black insertion: new nodes start red, then it may rotate left, rotate right, and flip colours; `insert` finally makes the root black.

## Rotation and colour trace

| Step | Inserted record | State/action |
|---:|---|---|
| 1 | KB-630 — Esi Addo — Maternity Ward | `630` is created red, then the public `insert` colours the root black. |
| 2 | KB-510 — Nana Kwarteng — Emergency Unit | `510` is a red left child of black `630`; no correction is needed. |
| 3 | KB-420 — Adwoa Sarpong — Paediatric Ward | red `510` has red left child `420`; `rotateRight(630)` is applied. |

Before the step-3 rotation (`B` = black, `R` = red):

```text
      630(B)
      /
  510(R)
    /
420(R)
```

After `rotateRight(630)`: the new root inherits 630's former black colour and 630 becomes red.

```text
      510(B)
      /    \
  420(R)  630(R)
```

| Node | Colour before | Colour after | Reason in `rotateRight` |
|---|---|---|---|
| 510 | red | black | receives the former colour of 630 |
| 630 | black | red | the former root is explicitly set red |
| 420 | red | red | unchanged |

If a node has both left and right red children, `flipColors` sets that parent red and both children black; the public method then ensures the root is black. This particular three-record trace demonstrates the right-rotation branch, so no flip is triggered.

## Height discussion

The unrotated three-node chain has height 3 (three levels). After the rotation, the tree has height 2 and keeps the root black. The implementation’s rotations and colour flips are what keep a left-leaning red-black tree from drifting toward ordinary-BST worst-case height; `rootIsBlack()` directly verifies the root-colour invariant exposed by this project.
