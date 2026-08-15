# Phase 1: Core Data Structures Implementation

## Structures:

1. Dynamic Array
   - Implement a dynamic array that can resize itself when it reaches capacity.
2. Linked List
   - Implement a singly linked list with basic operations such as insertion, deletion, and traversal.
3. Stack
   - Implement a stack using an array or a linked list, supporting push, pop, and peek operations.
4. Queue
   - Implement a queue using an array or a linked list, supporting enqueue, dequeue, and peek operations.
5. Circular Queue
   - Implement a circular queue that efficiently utilizes space and supports enqueue, dequeue, and peek operations.
6. Deque
   - Implement a double-ended queue (deque) that allows insertion and deletion from both ends.


---

### Task Assignment:

- Philipa - 1 to 3
- Eben - 4 to 6

---

#### Eben's Progress:

- Array-based Queue Implemented supporting the following methods/operations:
  - `enqueue(T element)`
  - `dequeue()`
  - `peek()`
  - `isEmpty()`
  - `isFull()`
  - `size()`
  - `clear()`
  - `toString()`
  - `resize(int newCapacity)`

- Array-based Circular Queue Implemented supporting the following methods/operations:
  - `enqueue(T element)`
  - `dequeue()`
  - `peek()`
  - `isEmpty()`
  - `isFull()`
  - `size()`
  - `clear()`
  - `toString()`
  - `resize(int newCapacity)`

- Linked Deque Implemented supporting the following methods/operations:
  - `addFirst(T element)`
  - `addLast(T element)`
  - `removeFirst()`
  - `removeLast()`
  - `peekFirst()`
  - `peekLast()`
  - `isEmpty()`
  - `size()`
  - `clear()`
  - `toString()`
---

# Phase 2: Hospital Operations Integration (Business Logic)

Team 2 will integrate the phase 1 data structures into the hospital's operational/business logic. These classes will **use our custom data structures** rather than Java Collections.

### Tasks

| Component               | Data Structure     | Assigned to: |
| ----------------------- |--------------------|--------------|
| `WardPatientRoster`     | Dynamic Array      | Seyram       |
| `OutpatientWaitingList` | Singly Linked List | Philipa      |
| `UndoManager`         | Stack              | Philipa      |
| `NursingShiftManager`   | Circular Queue     | Ebenezer     |
| `EmergencyIntake`       | Deque              | Ebenezer     |
| `PharmacyQueue`          | Queue              | Ebenezer     |

### Package

Work in the hospital-operation classes under:

`com.hospital.operations`

Using generic data structures under:

`com.hospital.structures`

### Requirements

* Use only the custom Team 2 data structures.
* Integrate with the existing `model` classes where appropriate.
* Do not use Java Collection classes (`ArrayList`, `LinkedList`, `Queue`, `Deque`, `Stack`, etc.).
* Keep the data structures generic and reusable; hospital-specific logic belongs in the operation classes.
* Add unit tests for each operation class.
* Ensure all existing tests continue to pass with `mvn clean test`.

### Deliverable

Six tested hospital-operation components demonstrating the practical use of Team 2's custom data structures.



````
Notice: Each member should update this document with their progress and any challenges they encounter accordingly. Regular check-ins will be scheduled to ensure that everyone is on track and to address any issues promptly.
````


