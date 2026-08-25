package com.hospital.benchmark;

import com.hospital.algorithms.PatientAdmission;
import com.hospital.algorithms.search.AdmissionSearch;
import com.hospital.algorithms.sort.AdmissionSort;
import com.hospital.app.TeamParameters;
import com.hospital.db.AlgorithmRunDAO;
import com.hospital.graph.BFS;
import com.hospital.graph.DFS;
import com.hospital.graph.Dijkstra;
import com.hospital.graph.Graph;
import com.hospital.graph.Kruskal;
import com.hospital.graph.Prim;
import com.hospital.model.Location;
import com.hospital.model.Road;
import com.hospital.model.ServiceRequest;
import com.hospital.structures.indexing.BinarySearchTree;
import com.hospital.structures.indexing.HashTable;
import com.hospital.structures.indexing.RedBlackTree;
import com.hospital.structures.indexing.UrgencyHeap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Brief §9 empirical lab: three timed runs per size, averages, optional
 * memory sample, SQLite persistence and CSV/SVG export.
 */
public final class PerformanceLab {

    public static final int[] SEARCH_SORT_SIZES = {100, 500, 1_000, 5_000, 10_000};
    public static final int[] HASH_SIZES = {100, 1_000, 5_000, 10_000, 20_000};
    public static final int[] TREE_SIZES = {100, 1_000, 5_000, 10_000, 20_000};
    public static final int[] HEAP_SIZES = {100, 1_000, 5_000, 10_000, 20_000};
    public static final int[] GRAPH_SIZES = {50, 100, 200, 500};
    private static final int RUNS = 3;

    private PerformanceLab() {
    }

    public static Path outputDirectory() {
        Path dir = Path.of("docs", "performance");
        try {
            Files.createDirectories(dir);
        } catch (IOException ignored) {
        }
        return dir;
    }

    public static String runAll(boolean persist) {
        StringBuilder log = new StringBuilder();
        log.append(machineSpec()).append('\n');
        log.append(runSearch(persist)).append('\n');
        log.append(runSort(persist)).append('\n');
        log.append(runHash(persist)).append('\n');
        log.append(runTrees(persist)).append('\n');
        log.append(runHeap(persist)).append('\n');
        log.append(runGraph(persist)).append('\n');
        writeText(outputDirectory().resolve("machine_spec.txt"), machineSpec());
        if (persist) {
            try {
                log.append("algorithm_runs stored: ").append(new AlgorithmRunDAO().findAll().size()).append('\n');
            } catch (SQLException e) {
                log.append("algorithm_runs count failed: ").append(e.getMessage()).append('\n');
            }
        }
        return log.toString();
    }

    public static String machineSpec() {
        return "Machine specification\n"
                + "  os.name            = " + System.getProperty("os.name") + "\n"
                + "  os.arch            = " + System.getProperty("os.arch") + "\n"
                + "  os.version         = " + System.getProperty("os.version") + "\n"
                + "  java.version       = " + System.getProperty("java.version") + "\n"
                + "  availableProcessors= " + Runtime.getRuntime().availableProcessors() + "\n"
                + "  maxMemoryMB        = " + (Runtime.getRuntime().maxMemory() / (1024 * 1024)) + "\n"
                + "  recordedAt         = " + Instant.now() + "\n"
                + "  repeats per cell   = " + RUNS + " (average of wall-clock ns)\n";
    }

    public static String runSearch(boolean persist) {
        StringBuilder csv = new StringBuilder("algorithm,inputSize,run1Ns,run2Ns,run3Ns,avgNs,memoryKb\n");
        for (int n : SEARCH_SORT_SIZES) {
            PatientAdmission[] unsorted = randomAdmissions(n, n * 3L + 11);
            PatientAdmission[] sorted = copy(unsorted);
            AdmissionSort.mergeSort(sorted, AdmissionSort.Criterion.ADMISSION_TIME);
            // binary search needs patientId order
            AdmissionSort.mergeSort(sorted, AdmissionSort.Criterion.ADMISSION_TIME);
            PatientAdmission[] byId = copy(unsorted);
            sortById(byId);
            int target = -1;
            Row linear = timeRuns(() -> AdmissionSearch.linearSearchByPatientId(unsorted, target));
            Row binary = timeRuns(() -> AdmissionSearch.binarySearchByPatientId(byId, target));
            append(csv, "linear_search", n, linear);
            append(csv, "binary_search", n, binary);
            persistRun(persist, "linear_search", n, linear);
            persistRun(persist, "binary_search", n, binary);
        }
        Path csvPath = outputDirectory().resolve("search_comparison.csv");
        writeText(csvPath, csv.toString());
        writeSvg(outputDirectory().resolve("search_comparison.svg"), "Linear vs binary search", csv.toString(),
                new String[] {"linear_search", "binary_search"});
        return "Wrote " + csvPath.toAbsolutePath();
    }

    public static String runSort(boolean persist) {
        StringBuilder csv = new StringBuilder("algorithm,inputSize,run1Ns,run2Ns,run3Ns,avgNs,memoryKb\n");
        String[] names = {"selection_sort", "insertion_sort", "merge_sort", "quicksort"};
        for (int n : SEARCH_SORT_SIZES) {
            PatientAdmission[] base = randomAdmissions(n, n * 7L + 3);
            Row selection = timeRuns(() -> AdmissionSort.selectionSort(copy(base), AdmissionSort.Criterion.URGENCY));
            Row insertion = timeRuns(() -> AdmissionSort.insertionSort(copy(base), AdmissionSort.Criterion.URGENCY));
            Row merge = timeRuns(() -> AdmissionSort.mergeSort(copy(base), AdmissionSort.Criterion.URGENCY));
            Row quick = timeRuns(() -> AdmissionSort.quickSort(copy(base), AdmissionSort.Criterion.URGENCY));
            Row[] rows = {selection, insertion, merge, quick};
            for (int i = 0; i < names.length; i++) {
                append(csv, names[i], n, rows[i]);
                persistRun(persist, names[i], n, rows[i]);
            }
        }
        Path csvPath = outputDirectory().resolve("sort_comparison.csv");
        writeText(csvPath, csv.toString());
        writeSvg(outputDirectory().resolve("sort_comparison.svg"), "Sorting comparison", csv.toString(), names);
        return "Wrote " + csvPath.toAbsolutePath();
    }

    public static String runHash(boolean persist) {
        StringBuilder csv = new StringBuilder(
                "algorithm,inputSize,tableSize,loadFactor,collisions,run1Ns,run2Ns,run3Ns,avgNs,memoryKb\n");
        int[] tables = {
                TeamParameters.HASH_TABLE_CAPACITY,
                TeamParameters.nextPrime(TeamParameters.HASH_TABLE_CAPACITY * 4),
                TeamParameters.nextPrime(20_000)
        };
        for (int keys : HASH_SIZES) {
            for (int tableSize : tables) {
                Row timed = timeRuns(() -> fillHash(keys, tableSize));
                HashTable sample = fillHash(keys, tableSize);
                csv.append("hash_put,").append(keys).append(',').append(tableSize).append(',')
                        .append(String.format(Locale.US, "%.3f", sample.loadFactor())).append(',')
                        .append(sample.collisionCount()).append(',')
                        .append(timed.run1).append(',').append(timed.run2).append(',').append(timed.run3).append(',')
                        .append(timed.avg).append(',').append(timed.memoryKb).append('\n');
                persistRun(persist, "hash_put_" + tableSize, keys, timed);
            }
        }
        Path csvPath = outputDirectory().resolve("hash_load_factor.csv");
        writeText(csvPath, csv.toString());
        writeSvg(outputDirectory().resolve("hash_load_factor.svg"), "Hash table insert time",
                toSimpleCsv(csv.toString(), "hash_put"), new String[] {"hash_put"});
        return "Wrote " + csvPath.toAbsolutePath();
    }

    public static String runTrees(boolean persist) {
        StringBuilder csv = new StringBuilder(
                "algorithm,inputSize,height,run1Ns,run2Ns,run3Ns,avgNs,memoryKb\n");
        for (int n : TREE_SIZES) {
            int[] keys = shuffledKeys(n, n * 19L + 2);
            Row bstInsert = timeRuns(() -> fillBst(keys));
            Row rbtInsert = timeRuns(() -> fillRbt(keys));
            BinarySearchTree bst = fillBst(keys);
            RedBlackTree rbt = fillRbt(keys);
            int target = keys[n / 2];
            Row bstSearch = timeRuns(() -> {
                fillBst(keys).contains(target);
            });
            Row rbtSearch = timeRuns(() -> {
                fillRbt(keys).contains(target);
            });
            csv.append("bst_insert,").append(n).append(',').append(bst.height()).append(',')
                    .append(joinTimes(bstInsert)).append('\n');
            csv.append("rbt_insert,").append(n).append(',').append(rbt.height()).append(',')
                    .append(joinTimes(rbtInsert)).append('\n');
            csv.append("bst_search,").append(n).append(',').append(bst.height()).append(',')
                    .append(joinTimes(bstSearch)).append('\n');
            csv.append("rbt_search,").append(n).append(',').append(rbt.height()).append(',')
                    .append(joinTimes(rbtSearch)).append('\n');
            persistRun(persist, "bst_insert", n, bstInsert);
            persistRun(persist, "rbt_insert", n, rbtInsert);
            persistRun(persist, "bst_search", n, bstSearch);
            persistRun(persist, "rbt_search", n, rbtSearch);
        }
        Path csvPath = outputDirectory().resolve("bst_vs_rbt.csv");
        writeText(csvPath, csv.toString());
        writeSvg(outputDirectory().resolve("bst_vs_rbt.svg"), "BST vs red-black search",
                toSimpleCsv(csv.toString(), "bst_search", "rbt_search"),
                new String[] {"bst_search", "rbt_search"});
        return "Wrote " + csvPath.toAbsolutePath();
    }

    public static String runHeap(boolean persist) {
        StringBuilder csv = new StringBuilder("algorithm,inputSize,run1Ns,run2Ns,run3Ns,avgNs,memoryKb\n");
        for (int n : HEAP_SIZES) {
            ServiceRequest[] requests = randomRequests(n, n * 13L + 5);
            Row insert = timeRuns(() -> {
                UrgencyHeap heap = new UrgencyHeap();
                for (ServiceRequest request : requests) {
                    heap.insert(request);
                }
            });
            Row extract = timeRuns(() -> {
                UrgencyHeap heap = new UrgencyHeap();
                for (ServiceRequest request : requests) {
                    heap.insert(request);
                }
                while (!heap.isEmpty()) {
                    heap.extractMax();
                }
            });
            append(csv, "heap_insert", n, insert);
            append(csv, "heap_extract", n, extract);
            persistRun(persist, "heap_insert", n, insert);
            persistRun(persist, "heap_extract", n, extract);
        }
        Path csvPath = outputDirectory().resolve("heap_dispatch.csv");
        writeText(csvPath, csv.toString());
        writeSvg(outputDirectory().resolve("heap_dispatch.svg"), "Heap insert/extract", csv.toString(),
                new String[] {"heap_insert", "heap_extract"});
        return "Wrote " + csvPath.toAbsolutePath();
    }

    public static String runGraph(boolean persist) {
        StringBuilder csv = new StringBuilder("algorithm,inputSize,run1Ns,run2Ns,run3Ns,avgNs,memoryKb\n");
        String[] names = {"bfs", "dfs", "dijkstra", "prim", "kruskal"};
        for (int n : GRAPH_SIZES) {
            Graph graph = syntheticGraph(n);
            Row bfs = timeRuns(() -> BFS.nearestAvailableBed(graph, "L0", loc -> loc.getLocationId().equals("L" + (n - 1))));
            Row dfs = timeRuns(() -> DFS.canReach(graph, "L0", "L" + (n - 1)));
            Row dijkstra = timeRuns(() -> Dijkstra.shortestPath(graph, "L0", TeamParameters.DIJKSTRA_PENALTY));
            Row prim = timeRuns(() -> Prim.buildMST(graph));
            Row kruskal = timeRuns(() -> Kruskal.buildMST(graph));
            Row[] rows = {bfs, dfs, dijkstra, prim, kruskal};
            for (int i = 0; i < names.length; i++) {
                append(csv, names[i], n, rows[i]);
                persistRun(persist, names[i], n, rows[i]);
            }
        }
        Path csvPath = outputDirectory().resolve("graph_algorithms.csv");
        writeText(csvPath, csv.toString());
        writeSvg(outputDirectory().resolve("graph_algorithms.svg"), "Graph algorithm runtime", csv.toString(), names);
        return "Wrote " + csvPath.toAbsolutePath();
    }

    private static HashTable fillHash(int keys, int tableSize) {
        HashTable table = new HashTable(tableSize);
        for (int i = 0; i < keys; i++) {
            table.put(i * 17 + 3, "P" + i, "W");
        }
        return table;
    }

    private static BinarySearchTree fillBst(int[] keys) {
        BinarySearchTree tree = new BinarySearchTree();
        for (int key : keys) {
            tree.insert(key, "P" + key, "W");
        }
        return tree;
    }

    private static RedBlackTree fillRbt(int[] keys) {
        RedBlackTree tree = new RedBlackTree();
        for (int key : keys) {
            tree.insert(key, "P" + key, "W");
        }
        return tree;
    }

    private static int[] sequentialKeys(int n) {
        int[] keys = new int[n];
        for (int i = 0; i < n; i++) {
            keys[i] = i + 1;
        }
        return keys;
    }

    private static int[] shuffledKeys(int n, long seed) {
        int[] keys = sequentialKeys(n);
        for (int i = n - 1; i > 0; i--) {
            seed = seed * 1664525L + 1013904223L;
            int j = (int) (Math.abs(seed) % (i + 1));
            int tmp = keys[i];
            keys[i] = keys[j];
            keys[j] = tmp;
        }
        return keys;
    }

    private static Graph syntheticGraph(int n) {
        Location[] locations = new Location[n];
        for (int i = 0; i < n; i++) {
            locations[i] = new Location("L" + i, "Loc" + i, "Accra", "WARD", 5.5, -0.2);
        }
        int edgeCount = Math.min(n * 3, n * (n - 1) / 2);
        Road[] roads = new Road[edgeCount];
        int k = 0;
        for (int i = 0; i < n - 1 && k < edgeCount; i++) {
            roads[k] = new Road("R" + k, "L" + i, "L" + (i + 1), 1.0 + (i % 5), 2, 1.0);
            k++;
        }
        long seed = n * 31L + 9;
        for (int i = 0; k < edgeCount; i++) {
            seed = seed * 1103515245L + 12345L;
            int a = (int) (Math.abs(seed) % n);
            seed = seed * 1103515245L + 12345L;
            int b = (int) (Math.abs(seed) % n);
            if (a == b) {
                b = (a + 1) % n;
            }
            roads[k] = new Road("R" + k, "L" + a, "L" + b, 1.0 + (i % 7), 3, 1.2);
            k++;
        }
        return new Graph(locations, roads, true);
    }

    private static PatientAdmission[] randomAdmissions(int n, long seed) {
        PatientAdmission[] admissions = new PatientAdmission[n];
        for (int i = 0; i < n; i++) {
            seed = seed * 1664525L + 1013904223L;
            int id = (int) (Math.abs(seed) % 1_000_000) + i;
            admissions[i] = new PatientAdmission(id, "P" + i, (int) (Math.abs(seed) % 10_000),
                    (int) (Math.abs(seed / 7) % 10), 1 + (int) (Math.abs(seed / 13) % 80), 1);
        }
        return admissions;
    }

    private static ServiceRequest[] randomRequests(int n, long seed) {
        String[] urgencies = {"LOW", "MEDIUM", "HIGH", "CRITICAL"};
        ServiceRequest[] requests = new ServiceRequest[n];
        for (int i = 0; i < n; i++) {
            seed = seed * 1103515245L + 12345L;
            String urgency = urgencies[(int) (Math.abs(seed) % urgencies.length)];
            requests[i] = new ServiceRequest("SR" + i, "L001", "L002", "ADMISSION", urgency,
                    "2024-01-02 08:" + String.format(Locale.US, "%02d", i % 60),
                    "2024-01-02 12:00", "PENDING");
        }
        return requests;
    }

    private static void sortById(PatientAdmission[] admissions) {
        for (int i = 1; i < admissions.length; i++) {
            PatientAdmission current = admissions[i];
            int j = i - 1;
            while (j >= 0 && admissions[j].patientId() > current.patientId()) {
                admissions[j + 1] = admissions[j];
                j--;
            }
            admissions[j + 1] = current;
        }
    }

    private static PatientAdmission[] copy(PatientAdmission[] source) {
        PatientAdmission[] copy = new PatientAdmission[source.length];
        for (int i = 0; i < source.length; i++) {
            copy[i] = source[i];
        }
        return copy;
    }

    private static Row timeRuns(Runnable action) {
        action.run();
        long[] samples = new long[RUNS];
        long mem = 0;
        for (int i = 0; i < RUNS; i++) {
            long beforeMem = usedMemoryKb();
            long start = System.nanoTime();
            action.run();
            samples[i] = System.nanoTime() - start;
            mem += Math.max(0, usedMemoryKb() - beforeMem);
        }
        long avg = (samples[0] + samples[1] + samples[2]) / RUNS;
        return new Row(samples[0], samples[1], samples[2], avg, (int) (mem / RUNS));
    }

    private static long usedMemoryKb() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / 1024;
    }

    private static void persistRun(boolean persist, String name, int inputSize, Row row) {
        if (!persist) {
            return;
        }
        try {
            new AlgorithmRunDAO().insert(name, inputSize, row.avg, row.memoryKb, LocalDateTime.now().toString());
        } catch (SQLException e) {
            System.err.println("algorithm_runs insert failed for " + name + ": " + e.getMessage());
        }
    }

    private static void append(StringBuilder csv, String algorithm, int n, Row row) {
        csv.append(algorithm).append(',').append(n).append(',')
                .append(row.run1).append(',').append(row.run2).append(',').append(row.run3).append(',')
                .append(row.avg).append(',').append(row.memoryKb).append('\n');
    }

    private static String joinTimes(Row row) {
        return row.run1 + "," + row.run2 + "," + row.run3 + "," + row.avg + "," + row.memoryKb;
    }

    private static String toSimpleCsv(String wideCsv, String... keep) {
        StringBuilder simple = new StringBuilder("algorithm,inputSize,run1Ns,run2Ns,run3Ns,avgNs,memoryKb\n");
        String[] lines = wideCsv.split("\n");
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].isBlank()) {
                continue;
            }
            String[] cols = lines[i].split(",");
            for (String name : keep) {
                if (cols[0].equals(name)) {
                    if (cols.length >= 7) {
                        simple.append(cols[0]).append(',').append(cols[1]).append(',')
                                .append(cols[cols.length - 5]).append(',')
                                .append(cols[cols.length - 4]).append(',')
                                .append(cols[cols.length - 3]).append(',')
                                .append(cols[cols.length - 2]).append(',')
                                .append(cols[cols.length - 1]).append('\n');
                    }
                }
            }
        }
        return simple.toString();
    }

    private static void writeText(Path path, String content) {
        try {
            Files.createDirectories(path.getParent());
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to write " + path, e);
        }
    }

    private static void writeSvg(Path path, String title, String csv, String[] series) {
        String[] lines = csv.split("\n");
        double maxY = 1;
        int minX = Integer.MAX_VALUE;
        int maxX = 0;
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].isBlank()) {
                continue;
            }
            String[] cols = lines[i].split(",");
            if (cols.length < 6) {
                continue;
            }
            int x = Integer.parseInt(cols[1]);
            long y = Long.parseLong(cols[cols.length - 2]);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
        if (minX == Integer.MAX_VALUE) {
            minX = 0;
        }
        int width = 720;
        int height = 420;
        int left = 70;
        int bottom = 360;
        int plotW = 600;
        int plotH = 300;
        String[] colors = {"#1f77b4", "#ff7f0e", "#2ca02c", "#d62728", "#9467bd"};
        StringBuilder svg = new StringBuilder();
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"").append(width)
                .append("\" height=\"").append(height).append("\">\n");
        svg.append("<rect width=\"100%\" height=\"100%\" fill=\"white\"/>\n");
        svg.append("<text x=\"360\" y=\"28\" text-anchor=\"middle\" font-size=\"16\">")
                .append(escape(title)).append("</text>\n");
        svg.append("<line x1=\"").append(left).append("\" y1=\"").append(bottom)
                .append("\" x2=\"").append(left + plotW).append("\" y2=\"").append(bottom)
                .append("\" stroke=\"black\"/>\n");
        svg.append("<line x1=\"").append(left).append("\" y1=\"").append(bottom)
                .append("\" x2=\"").append(left).append("\" y2=\"").append(bottom - plotH)
                .append("\" stroke=\"black\"/>\n");
        svg.append("<text x=\"20\" y=\"200\" transform=\"rotate(-90 20,200)\" font-size=\"12\">avg ns</text>\n");
        svg.append("<text x=\"360\" y=\"400\" text-anchor=\"middle\" font-size=\"12\">input size</text>\n");
        for (int s = 0; s < series.length; s++) {
            StringBuilder points = new StringBuilder();
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].isBlank()) {
                    continue;
                }
                String[] cols = lines[i].split(",");
                if (!cols[0].equals(series[s]) || cols.length < 6) {
                    continue;
                }
                int x = Integer.parseInt(cols[1]);
                long y = Long.parseLong(cols[cols.length - 2]);
                double px = left + (maxX == minX ? 0 : (x - minX) * (double) plotW / (maxX - minX));
                double py = bottom - (y / maxY) * plotH;
                points.append(String.format(Locale.US, "%.1f,%.1f ", px, py));
            }
            svg.append("<polyline fill=\"none\" stroke=\"").append(colors[s % colors.length])
                    .append("\" stroke-width=\"2\" points=\"").append(points).append("\"/>\n");
            svg.append("<text x=\"").append(left + 10 + s * 130).append("\" y=\"50\" font-size=\"12\" fill=\"")
                    .append(colors[s % colors.length]).append("\">").append(escape(series[s])).append("</text>\n");
        }
        svg.append("</svg>\n");
        writeText(path, svg.toString());
    }

    private static String escape(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private record Row(long run1, long run2, long run3, long avg, int memoryKb) {
    }
}
