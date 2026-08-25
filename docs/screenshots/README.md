# Console evidence (screenshots)

The examiner UI is a console, so captures are text logs rather than window bitmaps.

| File | What it shows |
|---|---|
| [console-demo.txt](console-demo.txt) | Fresh JAR `--demo`: schema+CSV load (55/105/310/40), structures, search/sort counterexample, four dispatch rules, Dijkstra/MST, greedy vs brute force, indexing |
| [trace-generation.txt](trace-generation.txt) | `--traces` writing Dijkstra, Kruskal/Prim and BFS/DFS markdown from the live graph |
| [experiments.txt](experiments.txt) | `--experiments` writing CSV/SVG under `docs/performance/` and rows into `algorithm_runs` |

Open any `docs/performance/*.svg` in a browser for the required line graphs.
