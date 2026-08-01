package com.hospital.structures.core;

public class Deque<T> {
    //Node
    private class Node {
        T data;
        Node next;
        Node prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    // Fields
    private Node front;
    private Node rear;
    private int size;

    // Constructor
    public Deque() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    //methods
    public void addFront(T element) {
        Node newNode = new Node(element);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            newNode.next = front;
            front.prev = newNode;
            front = newNode;
        }
        size++;
    }

    public void addRear(T element) {
        Node newNode = new Node(element);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            newNode.prev = rear;
            rear = newNode;
        }
        size++;
    }

    public T removeFront() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty.");
        }
        T removedData = front.data;
        front = front.next;
        if (front != null) {
            front.prev = null;
        } else {
            rear = null; // Deque is now empty
        }
        size--;
        return removedData;
    }

    public T removeRear() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty.");
        }
        T removedData = rear.data;
        rear = rear.prev;
        if (rear != null) {
            rear.next = null;
        } else {
            front = null; // Deque is now empty
        }
        size--;
        return removedData;
    }

    public T peekFront() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty.");
        }
        return front.data;
    }

    public T peekRear() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty.");
        }
        return rear.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Deque: [");
        Node current = front;
        while (current != null) {
            sb.append(current.data).append(", ");
            current = current.next;
        }
        sb.delete(sb.length() - 2, sb.length()).append("]");
        return sb.toString();
    }

}
