# Development log

| Week | Focus | Challenges | Decisions |
|---|---|---|---|
| 1 | Local context, schema, CSVs, Maven skeleton | Aligning hospital narrative with brief entity names | Korle-Bu + Accra referrals; SQLite; packages per team |
| 2 | Core structures and indexing library | Merge conflicts; no built-in collections | Custom arrays/nodes only; JUnit per class |
| 3 | Graph algorithms, search/sort, greedy/DP | Dijkstra penalty and hash size had no index numbers | Configurable then locked in `TeamParameters` |
| 4 | Integration, menu, traces, experiments, report | Main did not seed DB; Queue `isFull` bug; empty traces | Bootstrap on startup; compacting linear queue; TraceGenerator; PerformanceLab |

Challenges still owned by the team: replace index fragments with official student IDs if they differ; record the 5–8 minute demo video from `docs/DEMO_SCRIPT.md`.
