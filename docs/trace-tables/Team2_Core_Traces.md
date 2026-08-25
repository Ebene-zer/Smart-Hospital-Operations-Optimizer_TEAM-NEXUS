# Team 2 core structure traces

## Dynamic array resize

Initial capacity 2. Inserting a third ward patient doubles storage.

| Step | size | capacity | event |
|---:|---:|---:|---|
| 1 | 1 | 2 | insert Male Surgical #1 |
| 2 | 2 | 2 | insert Male Surgical #2 |
| 3 | 3 | 4 | RESIZE 2 → 4, then insert #3 |

Live console output: `RESIZE: size=2, oldCapacity=2 -> newCapacity=4`.

## Linked-list iterator (OPD)

Arrivals A, B, C; nurse bumps X after A.

| Position | Patient |
|---:|---|
| 1 | Patient A (08:01) |
| 2 | Patient X — deteriorating, bumped up |
| 3 | Patient B (08:05) |
| 4 | Patient C (08:10) |

## Linear queue front/rear (fill–drain–enqueue)

Capacity 2.

| Op | front | rear | size | isFull | contents |
|---|---:|---:|---:|---|---|
| enqueue A | 0 | 0 | 1 | false | [A] |
| enqueue B | 0 | 1 | 2 | true | [A, B] |
| dequeue | 1 | 1 | 1 | false | [B] |
| dequeue | 0 | -1 | 0 | false | [] (reset) |
| enqueue C | 0 | 0 | 1 | false | [C] |

`isFull` is `size == capacity`. After drain the queue reuses slot 0.

## Circular queue wrap (nursing shifts)

Nurses Amah, Boateng. `assignNurse` dequeues then enqueues the same nurse.

| Assign | Returned | Queue after rotation |
|---:|---|---|
| 1 | Amah | [Boateng, Amah] |
| 2 | Boateng | [Amah, Boateng] |

Roster size stays 2.

## Deque urgent insertion (A&E)

Routine walk-in joins the rear; trauma joins the front.

| Op | Deque front → rear |
|---|---|
| admitRoutine(Walk-in Ama) | Ama |
| admitEmergency(Trauma Kofi) | Kofi, Ama |
| attendNext | returns Kofi |

See also `DispatchDemo.traumaWalkInTrace()` for a 10-walk-in + 2-trauma evening.
