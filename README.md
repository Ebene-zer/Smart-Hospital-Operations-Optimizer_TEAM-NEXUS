# Team Distribution

| Team | Member 1 | Member 2 | Member 3 |
| :--- | :--- | :--- | :--- |
| Team 1 | Effah Gilbert | QUAINOO, CINDY NKRUMAH | GARIBAH, JEAN AFIBA |
| Team 2 | Philipa Araba Sarah Yeboah | Ebenezer Fuachie | SABBLAH, SEYRAM AWO |
| Team 3 | Nyamedor Richmond | AMANFU, DUNCAN KWAKU | DAWSON, KWAME OFEI |
| Team 4 | Ampadu Moses Danso | MAWULI-KWAWU, MAWULI | AKURUGU, PRINCE AKULGA |
| Team 5 | Purity Abena Kyei | ASAFO-ADJEI, JIM BENEETH | AFARI, YAW OTENG JNR |

---


# Korle-Bu Smart Hospital Operations Optimizer

Joint DSA project — custom data structures, algorithms, and DB integration
built around a hospital operations dataset (Korle-Bu Teaching Hospital +
Greater Accra referral network).

## 1. Build System — Maven

We're using **Maven**, not Gradle or plain `javac`, because:

- One `pom.xml` gives every team the same dependency versions (JUnit 5, JDBC driver) — no "works on my machine"
- `mvn test` / `mvn package` work identically on Windows, Mac, and any CI we bolt on later
- Standard folder layout (`src/main/java`, `src/test/java`) is exactly what's scaffolded below — nobody has to configure anything

### Prerequisites

- JDK 17+ (`java -version` to check)
- Maven 3.9+ (`mvn -version` to check)
- Git

### Common commands

```bash
# Clone and build
git clone <repo-url>
cd mart-Hospital-Operations-Optimizer_TEAM-NEXUS
mvn compile              # compiles everything

# Run all unit tests (this is where your 40+ tests live)
mvn test

# Package a runnable jar (bundles all dependencies)
mvn package
java -jar target/Smart-Hospital-Operations-Optimizer_TEAM-NEXUS.jar

# Run without packaging, while developing
mvn compile exec:java -Dexec.mainClass="com.hospital.app.Main"
```

If `exec:java` complains it's not found, just run `mvn package` and use the jar instead — the exec plugin isn't wired into `pom.xml` yet; add it if your team prefers that workflow.

### Adding a dependency

Only add a dependency if it's NOT one of the assessed data structures/algorithms
(e.g. it's fine to add a CSV or DB library; it is **not** fine to add a `HashMap`
or `PriorityQueue` import into assessed code — see Section 8 of the brief).
Add new dependencies to `pom.xml` under `<dependencies>` and open a PR so the
whole team pulls the same version.

---

## 2. Project Structure

Mapped directly onto the 5 team assignments so everyone works in their own
lane with minimal merge conflicts.

```

Smart-Hospital-Operations-Optimizer_TEAM-NEXUS/
├── pom.xml
├── README.md
├── .gitignore
│
├── docs/
│ ├── report/ # technical report drafts (docx/pdf)
│ ├── trace-tables/ # the 6 required trace tables go here
│ └── performance/ # CSV results + graphs from the efficiency study
│
└── src/
├── main/
│ ├── java/com/hospital/
│ │ ├── app/ # Main.java — startup/wiring, no team-specific logic
│ │ │
│ │ ├── model/ # Shared domain classes: Patient, Ward, Staff,
│ │ │ # Admission, Ambulance, Bed — everyone reads this,
│ │ │ # only edit with agreement from the team
│ │ │
│ │ ├── db/ # TEAM 1 — Database & Integration
│ │ │ # connection mgmt, DAOs, CSV import/export,
│ │ │ # validation rules, audit_events writer
│ │ │
│ │ ├── structures/
│ │ │ ├── core/ # TEAM 4 — Core Data Structures
│ │ │ │ # DynamicArray, LinkedList, Stack, Queue,
│ │ │ │ # CircularQueue, Deque
│ │ │ │
│ │ │ └── indexing/ # TEAM 2 — Trees & Indexing
│ │ │ # Heap/PriorityQueue, BST, RedBlackTree,
│ │ │ # BTree, HashTable, MySet/MyMap, DisjointSet
│ │ │
│ │ ├── graph/ # TEAM 3 — Graph Algorithms
│ │ │ # Graph (adjacency list + matrix), BFS, DFS,
│ │ │ # Dijkstra, Prim, Kruskal
│ │ │
│ │ ├── algorithms/
│ │ │ ├── search/ # TEAM 5 — linear search, binary search
│ │ │ ├── sort/ # TEAM 5 — selection, insertion, merge, quicksort
│ │ │ └── optimization/ # TEAM 5 — greedy allocation, DP scheduling
│ │ │
│ │ └── util/ # Shared helpers (timers for benchmarking, etc.)
│ │
│ └── resources/
│ ├── db/schema.sql # TEAM 1 — table definitions
│ └── data/ # TEAM 1 — seed CSVs (locations, roads, requests...)
│
└── test/
└── java/com/hospital/ # Mirrors src/main structure 1:1.
├── db/ # Put YOUR unit tests in YOUR package.
├── structures/core/
├── structures/indexing/
├── graph/
└── algorithms/{search,sort,optimization}/
```

### Rules of the road

- **No built-in collections in assessed code.** Don't `import java.util.HashMap`,
  `PriorityQueue`, `Stack`, `ArrayDeque`, `TreeMap`, etc. inside `structures/`,
  `graph/`, or `algorithms/`. Use them freely in `db/` or `app/` if it's just
  plumbing, not assessed logic.
- **Work in your own package.** Team 2 doesn't touch `structures/core`, Team 4
  doesn't touch `structures/indexing`, etc. Cuts merge conflicts to near zero.
- **`model/` is shared.** If you need a new field on `Patient` or `Ward`, ping
  the team before changing it — everyone's code depends on it.
- **One test class per structure/algorithm**, named to match
  (e.g. `RedBlackTreeTest.java`, `DijkstraTest.java`), living in the mirrored
  `test/` package.
- **Commit early, commit often.** Small PRs per structure/algorithm are far
  easier to review than one giant end-of-week dump.

### Suggested branch naming

```
team2/red-black-tree
team3/dijkstra
team5/dp-scheduling
```

Open a PR into `main` per structure/algorithm so trace tables and tests land
alongside the code that produced them.
