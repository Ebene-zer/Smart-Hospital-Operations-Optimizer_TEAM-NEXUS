package com.hospital.app;

/**
 * Three algorithm parameters derived from TEAM NEXUS member index numbers
 * (brief §2 localisation / AI-resistance requirement).
 *
 * <p>Each formula uses the last three digits of a named member's University of
 * Ghana student index. If a member's official index differs from the fragment
 * recorded here, replace the fragment and recompute — the menu prints the
 * live values so an examiner can check the arithmetic.
 *
 * <ul>
 *   <li>Ampadu Moses Danso (Team 4) — index fragment {@code 113}: hash-table
 *       capacity = next prime of {@code 50 + (113 % 80)} = {@code 83}.</li>
 *   <li>Nyamedor Richmond (Team 3) — index fragment {@code 447}: Dijkstra
 *       road-condition penalty = {@code 1.0 + (447 % 100) / 100.0} = {@code 1.47}.</li>
 *   <li>Purity Abena Kyei (Team 5) — index fragment {@code 808}: DP theatre
 *       budget = {@code 4 + (808 % 5)} regular hours and {@code 1 + (808 % 4)}
 *       overtime hours → {@code 7} + {@code 1} = {@code 8} hours.</li>
 * </ul>
 */
public final class TeamParameters {

    public static final int AMPADU_INDEX_FRAGMENT = 113;
    public static final int NYAMEDOR_INDEX_FRAGMENT = 447;
    public static final int PURITY_INDEX_FRAGMENT = 808;

    public static final int HASH_TABLE_CAPACITY = nextPrime(50 + (AMPADU_INDEX_FRAGMENT % 80));
    public static final double DIJKSTRA_PENALTY = 1.0 + (NYAMEDOR_INDEX_FRAGMENT % 100) / 100.0;
    public static final int DP_REGULAR_HOURS = 4 + (PURITY_INDEX_FRAGMENT % 5);
    public static final int DP_OVERTIME_HOURS = 1 + (PURITY_INDEX_FRAGMENT % 4);

    private TeamParameters() {
    }

    public static int nextPrime(int n) {
        int candidate = Math.max(2, n);
        while (!isPrime(candidate)) {
            candidate++;
        }
        return candidate;
    }

    private static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        for (int d = 2; d * d <= n; d++) {
            if (n % d == 0) {
                return false;
            }
        }
        return true;
    }

    public static String summary() {
        return "Team parameters (index-derived)\n"
                + "  HASH_TABLE_CAPACITY = " + HASH_TABLE_CAPACITY
                + "  (Ampadu fragment " + AMPADU_INDEX_FRAGMENT + ")\n"
                + "  DIJKSTRA_PENALTY    = " + DIJKSTRA_PENALTY
                + "  (Nyamedor fragment " + NYAMEDOR_INDEX_FRAGMENT + ")\n"
                + "  DP hours            = " + DP_REGULAR_HOURS + " regular + "
                + DP_OVERTIME_HOURS + " overtime"
                + "  (Purity fragment " + PURITY_INDEX_FRAGMENT + ")";
    }
}
