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

Joint DCIT 204/308 project: custom data structures, algorithms, SQLite integration and empirical analysis around Korle-Bu Teaching Hospital and the Greater Accra referral network.

This is **not** a UI project. Examiners run a **console menu**. No source edits are required.

## Prerequisites

- JDK 17+ (`java -version`)
- Maven 3.9+ (`mvn -version`)

## Run

```bash
cd Smart-Hospital-Operations-Optimizer_TEAM-NEXUS-main
mvn test
mvn package
java -jar target/hospital-dsa-project.jar
```

While developing:

```bash
mvn compile exec:java
```

Headless modes (no menu):

```bash
java -jar target/hospital-dsa-project.jar --demo
java -jar target/hospital-dsa-project.jar --traces
java -jar target/hospital-dsa-project.jar --experiments
```

On first start the program creates `hospital.db`, applies `src/main/resources/db/schema.sql`, and imports the classpath CSVs (55 locations, 105 roads, 310 requests, 40 resources).

## Console menu

1. Load / reload SQLite  
2. View counts and sample records  
3. Data-structure demos  
4. Searching and sorting  
5. Scheduling (FIFO / circular / deque / heap)  
6. Graph algorithms  
7. Optimisation (greedy / DP / brute force)  
8. Indexing engine  
9. How to run tests  
10. Performance experiments (CSV + SVG + `algorithm_runs`)  
11. View saved performance results  
12. Generate graph traces  
13. Index-number parameters  
14. Live write + undo/audit  
0. Exit

## Index-number parameters

Documented in [docs/INDEX_NUMBER_PARAMETERS.md](docs/INDEX_NUMBER_PARAMETERS.md): hash capacity **83**, Dijkstra penalty **1.47**, DP theatre hours **7+1**.

## Evidence pack

| Item | Location |
|---|---|
| Technical report | [docs/report/technical-report.md](docs/report/technical-report.md) |
| Dataset note | [docs/dataset-evidence.md](docs/dataset-evidence.md) |
| Problem spec (M1) | [docs/M1_problem_specification.md](docs/M1_problem_specification.md) |
| Proof sketches | [docs/proof-sketches.md](docs/proof-sketches.md) |
| Traces | [docs/trace-tables/](docs/trace-tables/) |
| Performance CSV/SVG | [docs/performance/](docs/performance/) |
| Demo script | [docs/DEMO_SCRIPT.md](docs/DEMO_SCRIPT.md) |
| Oral defense | [docs/ORAL_DEFENSE.md](docs/ORAL_DEFENSE.md) |
| Development log | [docs/DEVELOPMENT_LOG.md](docs/DEVELOPMENT_LOG.md) |

## Rules

Assessed packages (`structures/`, `graph/`, `algorithms/`) must not use built-in `HashMap`, `TreeMap`, `PriorityQueue`, `java.util.Stack` or `ArrayDeque`. JDBC, file I/O, printing, tests and the console menu may use Java utilities.
