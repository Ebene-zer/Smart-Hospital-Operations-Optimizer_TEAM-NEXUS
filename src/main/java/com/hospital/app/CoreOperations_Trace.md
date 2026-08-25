# Core Data Structure Operations Trace

This document provides traces for the six hospital operation classes, demonstrating their use of the custom data structures from `com.hospital.structures.core`.

---

### 1. `WardPatientRoster` (using `DynamicArray`)

**Scenario:** A ward roster with an initial capacity of 2 admits three patients. The `DynamicArray` must resize itself to accommodate the third patient.

**Trace:**
```
Roster: Adding patient 'Ama'.
Roster: Adding patient 'Kofi'.
Roster: Adding patient 'Yaa'.
! RESIZE EVENT: Roster capacity increased from 2 to 4
```

**Conclusion:** The `DynamicArray` correctly detected it was at capacity and automatically resized from 2 to 4, preventing a data overflow.

---

### 2. `OPDWaitingList` (using `SinglyLinkedList`)

**Scenario:** A patient's condition deteriorates in the Out-Patient Department waiting list. They need to be moved from the end of the list to a higher-priority position.

**Initial State:**
*   List: `Ama -> Kofi -> Yaa -> END`

**Action:** Reprioritize `Yaa` to be immediately after `Ama`.

**Final State:**
*   List: `Ama -> Yaa -> Kofi -> END`

**Conclusion:** The `SinglyLinkedList`'s `insertAfter` and `remove` methods successfully manipulated the list pointers to change the patient order without losing any data.

---

### 3. `EmergencyIntake` (using `Deque`)

**Scenario:** The emergency room is processing walk-in patients when a high-priority ambulance arrives. The ambulance patient must be seen first.

**Trace:**
1.  `addWalkIn(Ama)` -> `addRear` -> Deque: `[Ama]`
2.  `addWalkIn(Kofi)` -> `addRear` -> Deque: `[Ama, Kofi]`
3.  `addAmbulanceArrival(Yaa)` -> `addFront` -> Deque: `[Yaa, Ama, Kofi]`
4.  `seeNextPatient()` -> `removeFront` -> Returns `Yaa`. Deque: `[Ama, Kofi]`

**Conclusion:** The `Deque` correctly handled insertions at both the front and rear, ensuring the high-priority patient was triaged first, as required.

---

### 4. `PharmacyQueue` (using `Queue`)

**Scenario:** Patients queue for prescriptions. They must be served in a strict First-In, First-Out (FIFO) order.

**Trace:**
1.  `joinQueue(Ama)` -> `enqueue` -> Queue: `[Ama]`, front=0, rear=1
2.  `joinQueue(Kofi)` -> `enqueue` -> Queue: `[Ama, Kofi]`, front=0, rear=2
3.  `serveNextPatient()` -> `dequeue` -> Returns `Ama`. Queue: `[_, Kofi]`, front=1, rear=2
4.  `serveNextPatient()` -> `dequeue` -> Returns `Kofi`. Queue: `[_, _]`, front=2, rear=2 (empty)

**Conclusion:** The `Queue` implementation correctly maintained FIFO order, serving patients in the sequence they arrived.

---

### 5. `NursingShiftManager` (using `CircularQueue`)

**Scenario:** A team of 3 nurses rotates through shifts. After a full cycle, the rotation should wrap around seamlessly.

**Trace:**
*   Initial State: `[Nurse A, Nurse B, Nurse C]`
*   `rotateShift()` -> Dequeues `Nurse A`, Enqueues `Nurse A` -> `[Nurse B, Nurse C, Nurse A]`
*   `rotateShift()` -> Dequeues `Nurse B`, Enqueues `Nurse B` -> `[Nurse C, Nurse A, Nurse B]`
*   `rotateShift()` -> Dequeues `Nurse C`, Enqueues `Nurse C` -> `[Nurse A, Nurse B, Nurse C]` (wrapped around)

**Conclusion:** The `CircularQueue` correctly implements the wrap-around logic, allowing for continuous, efficient rotation of staff.

---

### 6. `UndoManager` (using `Stack`)

**Scenario:** A user performs an action (e.g., "Discharge Patient") and then realizes it was a mistake. The system must undo the last action and log both events.

**Trace:**
1.  `executeAction("Discharge Patient P001")` -> Pushes action to `Stack`. Logs `EXECUTE` event to `audit_events` table.
2.  `undoLastAction()` -> Pops action from `Stack`. Calls `undo()` on the action. Logs `UNDO` event to `audit_events` table.

**Conclusion:** The `Stack` correctly managed the history in a Last-In, First-Out (LIFO) manner. The `UndoManager` successfully integrated with the `AuditEventDAO` to create a persistent audit trail, which can be verified by querying the database.