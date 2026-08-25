package com.hospital.operations;

import com.hospital.app.TeamParameters;
import com.hospital.model.Location;
import com.hospital.model.ServiceRequest;
import com.hospital.structures.indexing.BTree;
import com.hospital.structures.indexing.BinarySearchTree;
import com.hospital.structures.indexing.HashTable;
import com.hospital.structures.indexing.HashTableEntry;
import com.hospital.structures.indexing.RedBlackTree;

/**
 * Loads live Korle-Bu records into BST, red-black tree, B-tree and hash table
 * so indexing is a use case, not only a unit-test demonstration.
 */
public class IndexingEngine {

    private final BinarySearchTree requestBst = new BinarySearchTree();
    private final RedBlackTree requestRbt = new RedBlackTree();
    private final BTree requestBTree = new BTree();
    private final HashTable requestHash = new HashTable(TeamParameters.HASH_TABLE_CAPACITY);
    private final BinarySearchTree locationBst = new BinarySearchTree();
    private int requestCount;
    private int locationCount;

    public void indexRequests(ServiceRequest[] requests) {
        requestCount = 0;
        for (ServiceRequest request : requests) {
            int key = numericSuffix(request.getRequestId());
            String label = request.getCategory() + "/" + request.getUrgency();
            requestBst.insert(key, request.getRequestId(), label);
            requestRbt.insert(key, request.getRequestId(), label);
            requestBTree.insert(key, request.getRequestId(), label);
            requestHash.put(key, request.getRequestId(), label);
            requestCount++;
        }
    }

    public void indexLocations(Location[] locations) {
        locationCount = 0;
        for (Location location : locations) {
            locationBst.insert(numericSuffix(location.getLocationId()), location.getName(), location.getType());
            locationCount++;
        }
    }

    public boolean bstContainsRequest(int numericId) {
        return requestBst.contains(numericId);
    }

    public boolean rbtContainsRequest(int numericId) {
        return requestRbt.contains(numericId);
    }

    public boolean bTreeContainsRequest(int numericId) {
        return requestBTree.contains(numericId);
    }

    public HashTableEntry hashLookup(int numericId) {
        return requestHash.get(numericId);
    }

    public boolean bstContainsLocation(int numericId) {
        return locationBst.contains(numericId);
    }

    public String bstInOrderPreview() {
        return requestBst.inOrderTraversal();
    }

    public int bstHeight() {
        return requestBst.height();
    }

    public int rbtHeight() {
        return requestRbt.height();
    }

    public HashTable hashTable() {
        return requestHash;
    }

    public int requestCount() {
        return requestCount;
    }

    public int locationCount() {
        return locationCount;
    }

    public static int numericSuffix(String id) {
        if (id == null) {
            return 0;
        }
        int value = 0;
        boolean seen = false;
        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            if (c >= '0' && c <= '9') {
                seen = true;
                value = value * 10 + (c - '0');
            }
        }
        return seen ? value : id.hashCode() & 0x7fffffff;
    }
}
