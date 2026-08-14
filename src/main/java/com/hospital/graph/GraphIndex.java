package com.hospital.graph;

public class GraphIndex {
    private final String[] indexToId;

    public GraphIndex(String[] indexToId) {
        this.indexToId = indexToId;
    }

    public int size() {
        return indexToId.length;
    }

    public String idAt(int index) {
        return indexToId[index];
    }

    public int indexOf(String locationId) {
        for (int i = 0; i < indexToId.length; i++) {
            if (indexToId[i].equals(locationId)) {
                return i;
            }
        }
        return -1;
    }
}
