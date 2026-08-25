# Project Report Outline

This document provides a scaffold for the final project report, following the 12-section structure required by the brief.

---

### 1. Cover Page

*   **Course:** DCIT 204/308 - Data Structures & Algorithms I / II
*   **Project Title:** Korle-Bu Smart Hospital Operations Optimizer
*   **Team Name:** TEAM-NEXUS
*   **Date:** (TODO: Fill in submission date)
*   **Team Members:** (TODO: List all team members and their index numbers)

---

### 2. Table of Contents

*   (TODO: Generate after report is complete)

---

### 3. Introduction

*   **Problem Statement:** (TODO: Describe the operational challenges faced by large hospitals like Korle-Bu, such as patient flow management, resource allocation, and emergency response coordination.)
*   **Project Goal:** To design and implement a software system using fundamental data structures and algorithms to model and optimize key hospital operations.
*   **Scope:** The system focuses on patient intake, ward management, staff scheduling, and logistical routing within the Korle-Bu campus and its referral network.

---

### 4. Dataset Description

The system is powered by a set of five interconnected CSV datasets, loaded into an SQLite database at startup.

*   **`locations.csv`**: Contains 55 records defining key points of interest, including hospital wards, departments, facilities, and external referral hospitals.
*   **`roads.csv`**: Contains 105 records defining the connections (edges) between locations, with attributes for distance, travel time, and road condition.
*   **`patients.csv`**: Contains 50+ synthetic patient records with IDs, names, NHIS numbers, and other details for use in indexing and operational scenarios.
*   **`resources.csv`**: Contains 30+ records of hospital resources like ambulances and ventilators.
*   **`service_requests.csv`**: Contains 300+ records of service requests used for optimization algorithm demonstrations.

---

### 5. System Architecture

The project is a Java-based console application built with Maven. The architecture is modular, with distinct packages for each team's responsibilities:

*   `com.hospital.app`: The main application entry point and menu system.
*   `com.hospital.db`: (Team 1) Handles all database connectivity (SQLite), schema management, and data access objects (DAOs).
*   `com.hospital.model`: Contains the plain Java objects (POJOs) representing core entities like `Patient`, `Location`, and `Road`.
*   `com.hospital.structures.core`: (Team 2) Implements fundamental data structures like `DynamicArray`, `LinkedList`, `Stack`, `Queue`, `CircularQueue`, and `Deque`.
*   `com.hospital.structures.indexing`: (Team 4) Implements advanced indexing structures like `BinarySearchTree`, `RedBlackTree`, `HashTable`, and `MinHeap`.
*   `com.hospital.graph`: (Team 3) Implements graph representations and algorithms like `BFS`, `DFS`, `Dijkstra`, and `MST`.
*   `com.hospital.algorithms`: (Team 5) Implements search, sort, and optimization algorithms.
*   `com.hospital.operations`: A business logic layer that composes the core data structures into hospital-specific use cases (e.g., `EmergencyIntake`, `UndoManager`).

---

### 6. Data Structures

*   **Core Structures (Team 2):** `DynamicArray`, `SinglyLinkedList`, `Stack`, `Queue`, `CircularQueue`, `Deque`.
*   **Indexing Structures (Team 4):** `BinarySearchTree`, `RedBlackTree`, `HashTable`, `MinHeap`, `DisjointSet`.
*   **Graph Structures (Team 3):** Adjacency List and Adjacency Matrix representations.
*   **(TODO: Each team should elaborate on the design and implementation of their specific structures here.)**

---

### 7. Algorithms

*   **Graph Algorithms (Team 3):** Breadth-First Search (BFS), Depth-First Search (DFS), Dijkstra's Shortest Path, Prim's MST, Kruskal's MST.
*   **Search Algorithms (Team 5):** Linear Search, Binary Search.
*   **Sorting Algorithms (Team 5):** Selection Sort, Insertion Sort, Merge Sort, Quicksort.
*   **Optimization Algorithms (Team 5):** A greedy algorithm for resource allocation and a dynamic programming solution for surgery scheduling.
*   **(TODO: Each team should elaborate on the design, complexity, and application of their algorithms.)**

---

### 8. System Demonstration

The system is demonstrated via a console menu (`Main.java`) that allows an examiner to trigger various functionalities, including:
*   Database summary view.
*   Live demonstrations of all major data structures and algorithms.
*   Execution of the full performance benchmark suite.
*   **(TODO: Add screenshots of the menu and key demo outputs.)**

---

### 9. Performance Analysis

An empirical analysis of algorithm performance was conducted. The results, including raw CSV data, generated graphs, and a written analysis, are located in the `docs/performance/` directory. The analysis compares the observed runtimes against theoretical Big-O complexities for sorting, searching, tree, and graph algorithms.

---

### 10. Conclusion

*   **(TODO: Summarize the project's achievements, challenges faced, and potential future improvements.)**

---

### 11. References

*   **(TODO: List any external resources, books, or articles consulted.)**

---

### 12. Appendices

*   **Appendix A: Trace Tables:** All required trace tables are located in `docs/trace-tables/`.
*   **Appendix B: Source Code:** The full, commented source code is available in the project repository.