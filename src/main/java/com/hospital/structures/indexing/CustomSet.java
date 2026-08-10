package com.hospital.structures.indexing;

public class CustomSet<T> {

    private static final Object PRESENT = new Object();

    private final CustomMap<T, Object> elements;

    public CustomSet() {
        elements = new CustomMap<>();
    }

    public CustomSet(int capacity) {
        elements = new CustomMap<>(capacity);
    }

    public boolean add(T value) {
        return elements.put(value, PRESENT) == null;
    }

    public boolean contains(T value) {
        return elements.containsKey(value);
    }

    public boolean remove(T value) {
        return elements.remove(value) != null;
    }

    public int size() {
        return elements.size();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public void clear() {
        elements.clear();
    }
}
