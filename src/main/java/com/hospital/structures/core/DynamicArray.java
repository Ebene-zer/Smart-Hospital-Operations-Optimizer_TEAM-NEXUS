package com.hospital.structures.core;


public class DynamicArray<T> {

    private static final int DEFAULT_CAPACITY = 4;

    private Object[] data;
    private int size;


    private final StringBuilder resizeLog = new StringBuilder();

    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be positive");
        }
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return data.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndexForAccess(index);
        return (T) data[index];
    }


    public void set(int index, T value) {
        checkIndexForAccess(index);
        data[index] = value;
    }


    public void insert(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "insert index " + index + " out of bounds for size " + size);
        }
        ensureCapacity(size + 1);
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = value;
        size++;
    }


    public void addLast(T value) {
        insert(size, value);
    }


    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndexForAccess(index);
        T removed = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
        return removed;
    }


    private void ensureCapacity(int minCapacity) {
        if (minCapacity <= data.length) {
            return;
        }
        int oldCapacity = data.length;
        int newCapacity = Math.max(oldCapacity * 2, minCapacity);
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);

        resizeLog.append(String.format(
                "RESIZE: size=%d, oldCapacity=%d -> newCapacity=%d%n",
                size, oldCapacity, newCapacity));

        this.data = newData;
    }

    private void checkIndexForAccess(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "index " + index + " out of bounds for size " + size);
        }
    }


    public String getResizeLog() {
        return resizeLog.toString();
    }
}
