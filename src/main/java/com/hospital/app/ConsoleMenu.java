package com.hospital.app;

import com.hospital.algorithms.PatientAdmission;
import com.hospital.algorithms.optimization.BruteForceAllocator;
import com.hospital.algorithms.optimization.GreedyResourceAllocator;
import com.hospital.algorithms.optimization.SurgeryScheduler;
import com.hospital.algorithms.search.AdmissionSearch;
import com.hospital.algorithms.sort.AdmissionSort;
import com.hospital.benchmark.PerformanceLab;
import com.hospital.db.AlgorithmRunDAO;
import com.hospital.db.AuditEventDAO;
import com.hospital.db.HospitalBootstrap;
import com.hospital.db.LocationDAO;
import com.hospital.db.ResourceDAO;
import com.hospital.db.RoadDAO;
import com.hospital.db.ServiceRequestDAO;
import com.hospital.graph.BFS;
import com.hospital.graph.DFS;
import com.hospital.graph.Dijkstra;
import com.hospital.graph.Graph;
import com.hospital.graph.Kruskal;
import com.hospital.graph.Prim;
import com.hospital.graph.TraceGenerator;
import com.hospital.model.Location;
import com.hospital.model.Resource;
import com.hospital.model.Road;
import com.hospital.model.ServiceRequest;
import com.hospital.operations.DispatchDemo;
import com.hospital.operations.EmergencyIntake;
import com.hospital.operations.IndexingEngine;
import com.hospital.operations.NursingShiftManager;
import com.hospital.operations.OPDWaitingList;
import com.hospital.operations.PharmacyQueue;
import com.hospital.operations.UndoManager;
import com.hospital.operations.WardPatientRoster;
import com.hospital.structures.core.DynamicArray;
import com.hospital.structures.core.Queue;
import com.hospital.structures.indexing.HashTableEntry;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Examiner console. No source edits are required to run the demonstrations.
 */
public final class ConsoleMenu {

    private final Scanner scanner;
    private Location[] locations = new Location[0];
    private Road[] roads = new Road[0];
    private ServiceRequest[] requests = new ServiceRequest[0];
    private Resource[] resources = new Resource[0];
    private Graph fullGraph;
    private Graph campusGraph;
    private IndexingEngine index = new IndexingEngine();
    private final UndoManager undo = new UndoManager();

    public ConsoleMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public void loop() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Select option: ");
            try {
                running = handle(choice);
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    public void demoAll() throws Exception {
        reloadFromDatabase();
        System.out.println("\n=== 1. Load / view data ===\n");
        viewCounts();
        System.out.println("\n=== 2. Data structures ===\n");
        demoStructures();
        System.out.println("\n=== 3. Searching and sorting ===\n");
        demoSearchSort();
        System.out.println("\n=== 4. Scheduling ===\n");
        demoScheduling();
        System.out.println("\n=== 5. Graph algorithms ===\n");
        demoGraph();
        System.out.println("\n=== 6. Optimisation ===\n");
        demoOptimisation();
        System.out.println("\n=== 7. Indexing ===\n");
        demoIndexing();
        System.out.println("\n=== 8. Tests / results ===\n");
        showTestHint();
        System.out.println("\n=== 9. Performance results ===\n");
        showPerformanceResults();
        System.out.println("\n=== Team parameters ===\n");
        System.out.println(TeamParameters.summary());
    }

    private boolean handle(String choice) throws Exception {
        switch (choice.trim()) {
            case "1" -> reloadFromDatabase();
            case "2" -> viewCounts();
            case "3" -> demoStructures();
            case "4" -> demoSearchSort();
            case "5" -> demoScheduling();
            case "6" -> demoGraph();
            case "7" -> demoOptimisation();
            case "8" -> demoIndexing();
            case "9" -> showTestHint();
            case "10" -> runExperiments();
            case "11" -> showPerformanceResults();
            case "12" -> generateTraces();
            case "13" -> System.out.println(TeamParameters.summary());
            case "14" -> liveWriteDemo();
            case "0" -> {
                System.out.println("Goodbye.");
                return false;
            }
            default -> System.out.println("Unknown option.");
        }
        return true;
    }

    private void printMenu() {
        System.out.println();
        System.out.println("====================================================");
        System.out.println(" Korle-Bu Smart Hospital Operations Optimizer");
        System.out.println("====================================================");
        System.out.println("  1. Load / reload data from SQLite");
        System.out.println("  2. View dataset counts and sample records");
        System.out.println("  3. Run data-structure demos");
        System.out.println("  4. Run searching and sorting");
        System.out.println("  5. Run service scheduling (FIFO / circular / deque / heap)");
        System.out.println("  6. Run graph algorithms (BFS / DFS / Dijkstra / MST)");
        System.out.println("  7. Run optimisation (greedy / DP / brute force)");
        System.out.println("  8. Run indexing engine (BST / RBT / B-tree / hash)");
        System.out.println("  9. How to run unit tests");
        System.out.println(" 10. Run performance experiments (writes CSV + DB)");
        System.out.println(" 11. View saved performance results");
        System.out.println(" 12. Generate graph trace tables");
        System.out.println(" 13. Show index-number parameters");
        System.out.println(" 14. Live database write + undo/audit demo");
        System.out.println("  0. Exit");
        System.out.println("====================================================");
    }

    void reloadFromDatabase() throws SQLException {
        HospitalBootstrap.ensureReady();
        List<Location> locationList = new LocationDAO().findAll();
        List<Road> roadList = new RoadDAO().findAll();
        List<ServiceRequest> requestList = new ServiceRequestDAO().findAll();
        List<Resource> resourceList = new ResourceDAO().findAll();
        locations = locationList.toArray(new Location[0]);
        roads = roadList.toArray(new Road[0]);
        requests = requestList.toArray(new ServiceRequest[0]);
        resources = resourceList.toArray(new Resource[0]);
        fullGraph = new Graph(locations, roads, true);
        campusGraph = new Graph(locations, roads, false);
        index = new IndexingEngine();
        index.indexRequests(requests);
        index.indexLocations(locations);
        System.out.println("Loaded from SQLite hospital.db:");
        viewCounts();
    }

    private void viewCounts() {
        System.out.println("  Locations        : " + locations.length);
        System.out.println("  Roads / edges    : " + roads.length);
        System.out.println("  Service requests : " + requests.length);
        System.out.println("  Resources        : " + resources.length);
        if (locations.length > 0) {
            System.out.println("  Sample location  : " + locations[0]);
        }
        if (requests.length > 0) {
            System.out.println("  Sample request   : " + requests[0]);
        }
        try {
            System.out.println("  Algorithm runs   : " + new AlgorithmRunDAO().findAll().size());
            System.out.println("  Audit events     : " + new AuditEventDAO().findAll().size());
        } catch (SQLException e) {
            System.out.println("  Algorithm/audit counts unavailable: " + e.getMessage());
        }
    }

    private void demoStructures() {
        DynamicArray<String> roster = new DynamicArray<>(2);
        roster.addLast("Male Surgical #1");
        roster.addLast("Male Surgical #2");
        roster.addLast("Male Surgical #3");
        System.out.println("DynamicArray resize log:\n" + roster.getResizeLog());

        WardPatientRoster ward = new WardPatientRoster();
        ward.admit("Ama Mensah", "CRITICAL", 8);
        ward.admit("Kofi Asare", "STABLE", 9);
        System.out.println("Ward roster size: " + ward.getTotalPatients());

        PharmacyQueue pharmacy = new PharmacyQueue(8);
        pharmacy.addPatient("OPD-01");
        pharmacy.addPatient("OPD-02");
        System.out.println("Pharmacy FIFO first: " + pharmacy.serveNext());

        NursingShiftManager shifts = new NursingShiftManager(3);
        shifts.addNurse("Nurse Amah");
        shifts.addNurse("Nurse Boateng");
        System.out.println("Shift rotation 1: " + shifts.assignNurse());
        System.out.println("Shift rotation 2: " + shifts.assignNurse());
        System.out.println("Nurses still on roster: " + shifts.getNurseCount());

        EmergencyIntake intake = new EmergencyIntake();
        intake.admitRoutine("Walk-in Ama");
        intake.admitEmergency("Trauma Kofi");
        System.out.println("Deque next (trauma first): " + intake.attendNext());

        if (requests.length >= 3) {
            OPDWaitingList opd = new OPDWaitingList();
            opd.arrive(requests[0]);
            opd.arrive(requests[1]);
            opd.bumpAfter(requests[0], requests[2]);
            System.out.println("OPD iterator walk:\n" + opd.iteratorWalk());
        }

        Queue<String> linear = new Queue<>(2);
        linear.enqueue("A");
        linear.enqueue("B");
        linear.dequeue();
        linear.dequeue();
        linear.enqueue("C");
        System.out.println("Linear queue fill-drain-enqueue still works: " + linear.dequeue());

        undo.record("EDIT", "service_requests", "SR001", "accidental discharge");
        System.out.println("Undo stack peek: " + undo.peek());
        System.out.println("Undo pop: " + undo.undo());
    }

    private void demoSearchSort() {
        PatientAdmission[] log = sampleAdmissions();
        System.out.println("Linear search for ID 20 in unsorted log: index "
                + AdmissionSearch.linearSearchByPatientId(log, 20));
        try {
            AdmissionSearch.binarySearchByPatientId(log, 20);
        } catch (IllegalArgumentException ex) {
            System.out.println("Binary-search precondition counterexample: " + ex.getMessage());
        }
        PatientAdmission[] byId = copy(log);
        AdmissionSort.mergeSort(byId, AdmissionSort.Criterion.ADMISSION_TIME);
        sortCopyById(byId);
        System.out.println("Binary search for ID 20 after sorting by patientId: index "
                + AdmissionSearch.binarySearchByPatientId(byId, 20));

        PatientAdmission[] urgency = copy(log);
        AdmissionSort.selectionSort(copy(log), AdmissionSort.Criterion.AGE);
        AdmissionSort.insertionSort(copy(log), AdmissionSort.Criterion.URGENCY);
        AdmissionSort.mergeSort(copy(log), AdmissionSort.Criterion.ADMISSION_TIME);
        AdmissionSort.quickSort(urgency, AdmissionSort.Criterion.URGENCY);
        System.out.print("Quicksort by urgency (IDs): ");
        for (PatientAdmission admission : urgency) {
            System.out.print(admission.patientId() + " ");
        }
        System.out.println();
    }

    private void demoScheduling() {
        ServiceRequest[] batch = firstPending(12);
        System.out.println(DispatchDemo.compare(batch));
        System.out.println(DispatchDemo.traumaWalkInTrace());
    }

    private void demoGraph() {
        ensureGraph();
        System.out.println("Full graph nodes: " + fullGraph.size() + " (adjacency list + matrix both stored)");
        System.out.println("Campus-only nodes: " + campusGraph.size());
        double[][] matrix = campusGraph.adjacencyMatrix();
        System.out.println("Adjacency-matrix sample campus[0][1] = "
                + (matrix.length > 1 ? matrix[0][1] : "n/a"));

        int bed = BFS.nearestAvailableBed(fullGraph, "L001", loc -> {
            if (!"WARD".equals(loc.getType()) && !"ICU".equals(loc.getType())) {
                return false;
            }
            for (Resource resource : resources) {
                if ("BED".equalsIgnoreCase(resource.getType())
                        && loc.getLocationId().equals(resource.getHomeLocationId())
                        && "AVAILABLE".equalsIgnoreCase(resource.getAvailabilityStatus())) {
                    return true;
                }
            }
            return "WARD".equals(loc.getType());
        });
        if (bed >= 0) {
            System.out.println("BFS nearest bed/ward from A&E: " + fullGraph.getIndex().idAt(bed)
                    + " " + fullGraph.locationAt(bed).getName());
        }

        String blood = "L001";
        for (Location location : locations) {
            if (location.getName() != null && location.getName().toLowerCase().contains("blood")) {
                blood = location.getLocationId();
                break;
            }
        }
        boolean reachable = DFS.canReach(campusGraph, "L001", blood);
        System.out.println("DFS A&E -> Blood Bank (" + blood + "): " + (reachable ? "REACHABLE" : "UNREACHABLE"));
        boolean missing = DFS.canReach(campusGraph, "L001", "L999");
        System.out.println("DFS unreachable destination L999: " + missing);

        Dijkstra.Result dijkstra = Dijkstra.shortestPath(fullGraph, "L046", TeamParameters.DIJKSTRA_PENALTY);
        int tema = fullGraph.getIndex().indexOf("L050");
        int korle = fullGraph.getIndex().indexOf("L001");
        System.out.println("Dijkstra penalty = " + TeamParameters.DIJKSTRA_PENALTY);
        if (dijkstra != null && tema >= 0 && korle >= 0) {
            System.out.println("Ridge(L046) -> Korle-Bu(L001): " + dijkstra.dist[korle]
                    + " path=" + Dijkstra.pathAsString(fullGraph, dijkstra.prev, korle));
            System.out.println("Ridge(L046) -> Tema(L050): " + dijkstra.dist[tema]
                    + " path=" + Dijkstra.pathAsString(fullGraph, dijkstra.prev, tema));
        }

        Kruskal.Result kruskal = Kruskal.buildMST(campusGraph);
        Prim.Result prim = Prim.buildMST(campusGraph);
        System.out.println("Kruskal MST edges=" + kruskal.mstEdges.length + " total=" + kruskal.totalCost);
        System.out.println("Prim    MST edges=" + prim.mstEdges.length + " total=" + prim.totalCost);
        System.out.println(kruskal.trace.split("\n").length > 6
                ? kruskal.trace.lines().limit(8).reduce("", (a, b) -> a + b + "\n") + "...\n"
                : kruskal.trace);
    }

    private void demoOptimisation() {
        PatientAdmission[] small = new PatientAdmission[] {
                new PatientAdmission(1, "Long-stay critical", 9, 10, 54, 2),
                new PatientAdmission(2, "Short stay A", 10, 9, 22, 1),
                new PatientAdmission(3, "Short stay B", 11, 8, 31, 1)
        };
        var greedy = GreedyResourceAllocator.allocate(small, 2);
        System.out.println("Greedy (urgency-first, capacity 2) allocated "
                + greedy.allocated().length + " patient(s); unallocated "
                + greedy.unallocated().length);
        var brute = BruteForceAllocator.allocate(small, 2);
        System.out.println("Brute force maximising patients treated: "
                + brute.patientsTreated() + " patients from " + brute.subsetsExamined() + " subsets");
        System.out.println("Greedy fails this objective because it takes the 2-unit critical case.");

        SurgeryScheduler.SurgeryRequest[] surgeries = {
                new SurgeryScheduler.SurgeryRequest("S1", 2, 6),
                new SurgeryScheduler.SurgeryRequest("S2", 3, 10),
                new SurgeryScheduler.SurgeryRequest("S3", 2, 7)
        };
        var schedule = SurgeryScheduler.schedule(surgeries,
                TeamParameters.DP_REGULAR_HOURS, TeamParameters.DP_OVERTIME_HOURS);
        System.out.println("DP theatre hours (index-derived) = "
                + TeamParameters.DP_REGULAR_HOURS + "+" + TeamParameters.DP_OVERTIME_HOURS);
        System.out.println("DP selected " + schedule.selected().length + " surgeries, benefit="
                + schedule.totalClinicalBenefit() + ", hours used=" + schedule.hoursUsed());
    }

    private void demoIndexing() {
        if (index.requestCount() == 0) {
            index.indexRequests(requests);
            index.indexLocations(locations);
        }
        int sample = requests.length == 0 ? 1 : IndexingEngine.numericSuffix(requests[0].getRequestId());
        System.out.println("Indexed requests=" + index.requestCount() + " locations=" + index.locationCount());
        System.out.println("BST height=" + index.bstHeight() + " RBT height=" + index.rbtHeight());
        System.out.println("BST contains SR#" + sample + ": " + index.bstContainsRequest(sample));
        System.out.println("RBT contains SR#" + sample + ": " + index.rbtContainsRequest(sample));
        System.out.println("B-tree contains SR#" + sample + ": " + index.bTreeContainsRequest(sample));
        HashTableEntry hit = index.hashLookup(sample);
        System.out.println("Hash lookup SR#" + sample + ": "
                + (hit == null ? "miss" : hit.getPatientName() + " / " + hit.getWardName()));
        System.out.println("Hash table size=" + index.hashTable().size()
                + " capacity=" + index.hashTable().capacity()
                + " load=" + String.format("%.3f", index.hashTable().loadFactor())
                + " collisions=" + index.hashTable().collisionCount()
                + " longestChain=" + index.hashTable().longestChain());
        String preview = index.bstInOrderPreview();
        int cut = Math.min(preview.length(), 400);
        System.out.println("BST inorder preview:\n" + preview.substring(0, cut) + (preview.length() > cut ? "..." : ""));
    }

    private void showTestHint() {
        System.out.println("Unit tests live under src/test/java and are run with:");
        System.out.println("  mvn test");
        System.out.println("CI also runs mvn -B -V test on every push to main.");
    }

    private void runExperiments() {
        System.out.println("Running brief §9 experiments (three repeats per size). This can take a few minutes...");
        System.out.println(PerformanceLab.runAll(true));
    }

    private void showPerformanceResults() {
        Path dir = PerformanceLab.outputDirectory();
        String[] files = {
                "machine_spec.txt",
                "search_comparison.csv",
                "sort_comparison.csv",
                "hash_load_factor.csv",
                "bst_vs_rbt.csv",
                "heap_dispatch.csv",
                "graph_algorithms.csv"
        };
        for (String file : files) {
            Path path = dir.resolve(file);
            if (!Files.exists(path)) {
                System.out.println("Missing " + path + " — choose option 10 to generate.");
                continue;
            }
            try {
                String text = Files.readString(path);
                String[] lines = text.split("\n");
                System.out.println("--- " + file + " ---");
                for (int i = 0; i < Math.min(lines.length, 8); i++) {
                    System.out.println(lines[i]);
                }
                if (lines.length > 8) {
                    System.out.println("... (" + lines.length + " lines)");
                }
            } catch (Exception e) {
                System.out.println("Could not read " + path);
            }
        }
        System.out.println("SVG line graphs (open in a browser): docs/performance/*.svg");
    }

    private void generateTraces() throws Exception {
        TraceGenerator.main(new String[] {
                Double.toString(TeamParameters.DIJKSTRA_PENALTY), "L046"
        });
    }

    private void liveWriteDemo() throws Exception {
        ensureGraph();
        undo.record("TRANSFER", "service_requests", "SR-DEMO", "moved to Male Surgical then undone");
        System.out.println("Recorded transfer on undo stack and audit_events.");
        System.out.println("Undone: " + undo.undo());
        System.out.println("Audit rows now: " + new AuditEventDAO().findAll().size());
    }

    private void ensureGraph() {
        if (fullGraph == null) {
            throw new IllegalStateException("Load data first (option 1).");
        }
    }

    private ServiceRequest[] firstPending(int limit) {
        int count = 0;
        for (ServiceRequest request : requests) {
            if ("PENDING".equalsIgnoreCase(request.getStatus()) && count < limit) {
                count++;
            }
        }
        if (count == 0) {
            count = Math.min(limit, requests.length);
            ServiceRequest[] batch = new ServiceRequest[count];
            for (int i = 0; i < count; i++) {
                batch[i] = requests[i];
            }
            return batch;
        }
        ServiceRequest[] batch = new ServiceRequest[count];
        int i = 0;
        for (ServiceRequest request : requests) {
            if ("PENDING".equalsIgnoreCase(request.getStatus()) && i < batch.length) {
                batch[i++] = request;
            }
        }
        return batch;
    }

    private static PatientAdmission[] sampleAdmissions() {
        return new PatientAdmission[] {
                new PatientAdmission(30, "Ama", 9, 2, 30, 1),
                new PatientAdmission(10, "Kofi", 7, 5, 20, 1),
                new PatientAdmission(20, "Esi", 11, 4, 40, 1)
        };
    }

    private static PatientAdmission[] copy(PatientAdmission[] source) {
        PatientAdmission[] copy = new PatientAdmission[source.length];
        System.arraycopy(source, 0, copy, 0, source.length);
        return copy;
    }

    private static void sortCopyById(PatientAdmission[] admissions) {
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

    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "0";
        }
        return scanner.nextLine();
    }
}
