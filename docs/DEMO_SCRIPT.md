# Demonstration script (5–8 minutes)

Record a screen capture of the console. Suggested narration:

1. **0:00** `mvn test` — mention 40+ tests (actually 180+).  
2. **0:40** `mvn package` then `java -jar target/hospital-dsa-project.jar`. Show schema+CSV load (55/105/310/40).  
3. **1:20** Menu option 2 — sample Korle-Bu location and request.  
4. **2:00** Option 5 — FIFO vs heap dispatch; trauma cases first on the heap.  
5. **3:00** Option 6 — Dijkstra Ridge → Korle-Bu vs Tema with penalty 1.47; Kruskal/Prim totals.  
6. **4:00** Option 7 — greedy failure vs brute force; DP hours from index numbers.  
7. **5:00** Option 8 — BST/RBT/B-tree/hash lookup of a live request.  
8. **5:40** Option 14 — undo writes `audit_events`.  
9. **6:10** Option 11 — open a CSV and an SVG graph (or generate with option 10 if not yet run).  
10. **7:00** Option 13 — index-number parameters.

Headless capture used for this repository: `java -jar target/hospital-dsa-project.jar --demo` → `docs/screenshots/console-demo.txt`.
