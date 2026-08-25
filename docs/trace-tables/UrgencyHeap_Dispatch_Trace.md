# Urgency-heap dispatch order trace

Max-heap keyed by urgency (CRITICAL=4 … LOW=1), tie-broken by earlier `timeSubmitted`.

Busy A&E evening: 10 walk-ins then 2 trauma cases.

| Extract | ID | Urgency | Time |
|---:|---|---|---|
| 1 | SR-T01 | CRITICAL | 18:16 |
| 2 | SR-T02 | CRITICAL | 18:18 |
| 3 | SR-W06 | HIGH | 18:09 |
| 4 | SR-W02 | MEDIUM | 18:03 |
| 5 | SR-W04 | MEDIUM | 18:06 |
| 6 | SR-W08 | MEDIUM | 18:12 |
| 7 | SR-W10 | MEDIUM | 18:15 |
| 8 | SR-W01 | LOW | 18:01 |
| 9 | SR-W03 | LOW | 18:04 |
| 10 | SR-W05 | LOW | 18:08 |
| 11 | SR-W07 | LOW | 18:11 |
| 12 | SR-W09 | LOW | 18:14 |

FIFO on the same batch would serve W01 first and delay both trauma cases. The deque rule jumps CRITICAL/HIGH to the front as they arrive, which is different again from global extractMax. Menu option 5 prints all four orders on live PENDING requests from SQLite.
