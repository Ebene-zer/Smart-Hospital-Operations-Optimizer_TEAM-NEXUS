package com.hospital.structures.core;

/**
  Generic linear array-based Queue implementation.
  This queue does not wrap around.
 
  @param <T> the type of elements stored in the queue
 */
public class Queue<T> {

    // Fields
    private final T[] elements;
    private final int capacity;
    private int front;
    private int rear;
    private int size;

    
    @SuppressWarnings("unchecked")
    public Queue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0.");
        }

        this.capacity = capacity;
        this.elements = (T[]) new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }


    public void enqueue(T element) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full.");
        }
        compactIfNeeded();
        rear++;
        elements[rear] = element;
        size++;
    }


    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }

        T removed = elements[front];
        elements[front] = null;     // Helps garbage collection
        front++;
        size--;
        if (size == 0) {
            front = 0;
            rear = -1;
        }

        return removed;
    }


    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty.");
        }

        return elements[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public boolean isFull() {
        return size == capacity;
    }

    /**
     * A linear queue leaves unused slots at the front after dequeue. Shift
     * live elements back to index 0 so fill-drain-enqueue reuse works.
     */
    private void compactIfNeeded() {
        if (rear < capacity - 1 || front == 0) {
            return;
        }
        for (int i = 0; i < size; i++) {
            elements[i] = elements[front + i];
        }
        for (int i = size; i <= rear; i++) {
            elements[i] = null;
        }
        rear = size - 1;
        front = 0;
    }


    public int size() {
        return size;
    }

 
    public int capacity() {
        return capacity;
    }


    public void clear() {
        for (int i = front; i <= rear; i++) {
            elements[i] = null;
        }

        front = 0;
        rear = -1;
        size = 0;
    }

    public int getFrontIndex() {
        return front;
    }

    public int getRearIndex() {
        return rear;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Queue: [");

        for (int i = front; i <= rear; i++) {
            sb.append(elements[i]);

            if (i < rear) {
                sb.append(", ");
            }
        }

        sb.append("]");
        return sb.toString();
    }
}