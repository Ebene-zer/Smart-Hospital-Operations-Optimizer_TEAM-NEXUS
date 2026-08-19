package com.hospital.algorithms;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class BenchmarkHarness {

    static final int[] SEARCH_SIZES = {100, 500, 1000, 2000, 5000, 10000, 20000};
    static final int[] SORT_SIZES   = {100, 500, 1000, 2000, 5000, 10000, 20000};
    static final long SEED = 42L; // fixed seed -> reproducible benchmark data

    record AlgorithmRun(int runId, String algorithmName, String category, int inputSize,
                         long comparisons, long writesOrSwaps, double timeMs,
                         String target, String timestamp) {}

    public static void main(String[] args) throws IOException {
        List<AlgorithmRun> runs = new ArrayList<>();
        int runId = 1;

        Comparator<Patient> byId = Comparator.comparing(Patient::getPatientId);
        for (int n : SEARCH_SIZES) {
            List<Patient> unsorted = generatePatients(n, SEED + n);
            String missingTarget = "KB-99999999"; // guaranteed absent -> worst case for both

            long t0 = System.nanoTime();
            SearchAlgorithms.SearchResult lin = SearchAlgorithms.linearSearch(unsorted, missingTarget);
            double linMs = (System.nanoTime() - t0) / 1_000_000.0;
            runs.add(new AlgorithmRun(runId++, "linearSearch", "search", n,
                    lin.comparisons, 0, linMs, missingTarget, now()));

            List<Patient> sorted = new ArrayList<>(unsorted);
            sorted.sort(byId);
            t0 = System.nanoTime();
            SearchAlgorithms.SearchResult bin = SearchAlgorithms.binarySearch(sorted, missingTarget, byId, true, false);
            double binMs = (System.nanoTime() - t0) / 1_000_000.0;
            runs.add(new AlgorithmRun(runId++, "binarySearch", "search", n,
                    bin.comparisons, 0, binMs, missingTarget, now()));

            System.out.println("search n=" + n + "  linear comparisons=" + lin.comparisons
                    + " (" + fmt(linMs) + " ms)   binary comparisons=" + bin.comparisons + " (" + fmt(binMs) + " ms)");
        }

        Comparator<Patient> byUrgency = Comparator.comparingInt(Patient::getUrgencyScore);
        for (int n : SORT_SIZES) {
            List<Patient> base = generatePatients(n, SEED + 1000 + n);

            List<Patient> sel = new ArrayList<>(base);
            long t0 = System.nanoTime();
            SortAlgorithms.SortStats selStats = SortAlgorithms.selectionSort(sel, byUrgency);
            double selMs = (System.nanoTime() - t0) / 1_000_000.0;
            runs.add(new AlgorithmRun(runId++, "selectionSort", "sort", n,
                    selStats.comparisons, selStats.writesOrSwaps, selMs, "urgency", now()));

            List<Patient> ins = new ArrayList<>(base);
            t0 = System.nanoTime();
            SortAlgorithms.SortStats insStats = SortAlgorithms.insertionSort(ins, byUrgency, false);
            double insMs = (System.nanoTime() - t0) / 1_000_000.0;
            runs.add(new AlgorithmRun(runId++, "insertionSort", "sort", n,
                    insStats.comparisons, insStats.writesOrSwaps, insMs, "urgency", now()));

            List<Patient> mrg = new ArrayList<>(base);
            t0 = System.nanoTime();
            SortAlgorithms.SortStats mrgStats = SortAlgorithms.mergeSort(mrg, byUrgency, false);
            double mrgMs = (System.nanoTime() - t0) / 1_000_000.0;
            runs.add(new AlgorithmRun(runId++, "mergeSort", "sort", n,
                    mrgStats.comparisons, mrgStats.writesOrSwaps, mrgMs, "urgency", now()));

            List<Patient> qck = new ArrayList<>(base);
            t0 = System.nanoTime();
            SortAlgorithms.SortStats qckStats = SortAlgorithms.quickSort(qck, byUrgency, false);
            double qckMs = (System.nanoTime() - t0) / 1_000_000.0;
            runs.add(new AlgorithmRun(runId++, "quickSort", "sort", n,
                    qckStats.comparisons, qckStats.writesOrSwaps, qckMs, "urgency", now()));

            System.out.println("sort n=" + n
                    + "  selection=" + fmt(selMs) + "ms(" + selStats.comparisons + " cmp)"
                    + "  insertion=" + fmt(insMs) + "ms(" + insStats.comparisons + " cmp)"
                    + "  merge=" + fmt(mrgMs) + "ms(" + mrgStats.comparisons + " cmp)"
                    + "  quick=" + fmt(qckMs) + "ms(" + qckStats.comparisons + " cmp)");
        }

        writeCsv(runs, "algorithm_runs.csv");
        System.out.println("\nWrote " + runs.size() + " rows to algorithm_runs.csv");
    }

    private static void writeCsv(List<AlgorithmRun> runs, String path) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("run_id,algorithm_name,category,input_size,comparisons,writes_or_swaps,time_ms,target,timestamp");
            for (AlgorithmRun r : runs) {
                pw.printf("%d,%s,%s,%d,%d,%d,%.4f,%s,%s%n",
                        r.runId(), r.algorithmName(), r.category(), r.inputSize(),
                        r.comparisons(), r.writesOrSwaps(), r.timeMs(), r.target(), r.timestamp());
            }
        }
    }

    private static List<Patient> generatePatients(int n, long seed) {
        Random rnd = new Random(seed);
        String[] wards = {"A&E", "Male Surgical", "Female Surgical", "Maternity",
                           "Paediatric", "ICU", "Isolation Ward"};
        List<Patient> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            String id = String.format("KB-%08d", i); 
            int admissionTime = 600 + rnd.nextInt(600); 
            int urgency = 1 + rnd.nextInt(10);
            int age = 1 + rnd.nextInt(90);
            String ward = wards[rnd.nextInt(wards.length)];
            list.add(new Patient(id, "Patient-" + i, admissionTime, urgency, age, ward, i));
        }
        Collections.shuffle(list, rnd);
        return list;
    }

    private static String now() { return LocalDateTime.now().toString(); }
    private static String fmt(double ms) { return String.format("%.3f", ms); }
}
