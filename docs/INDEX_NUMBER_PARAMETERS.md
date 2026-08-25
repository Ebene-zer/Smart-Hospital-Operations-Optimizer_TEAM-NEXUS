# Index-number derived algorithm parameters

Brief §2 requires at least three algorithm parameters derived from member index numbers. They are implemented in `com.hospital.app.TeamParameters` and printed at every program start.

| Member | Team | Index fragment (last 3 digits) | Parameter | Formula | Live value |
|---|---|---:|---|---|---|
| Ampadu Moses Danso | 4 | 113 | Hash-table capacity | next prime of `50 + (113 % 80)` | **83** |
| Nyamedor Richmond | 3 | 447 | Dijkstra road-condition penalty | `1.0 + (447 % 100) / 100.0` | **1.47** |
| Purity Abena Kyei | 5 | 808 | DP theatre hours | regular `4 + (808 % 5)`, overtime `1 + (808 % 4)` | **7 + 1** |

If a member's official University of Ghana index differs from the fragment recorded here, replace the constant in `TeamParameters.java` and rerun the console menu. The examiner can recompute the arithmetic from the printed summary without editing source.
