package com.hospital.structures.core;



import java.util.Iterator;
import java.util.NoSuchElementException;



public class SinglyLinkedList<T> implements Iterable<T> {

    private static class Node<T> {
        T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        head = node;
        if (tail == null) {
            tail = node; // list was empty
        }
        size++;
    }


    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }


    public void insertAfter(T targetValue, T newValue) {
        Node<T> current = head;
        while (current != null) {
            if (current.value.equals(targetValue)) {
                Node<T> node = new Node<>(newValue);
                node.next = current.next;
                current.next = node;
                if (current == tail) {
                    tail = node;
                }
                size++;
                return;
            }
            current = current.next;
        }
        throw new NoSuchElementException("targetValue not found in list: " + targetValue);
    }


    public boolean remove(T value) {
        if (head == null) {
            return false;
        }
        if (head.value.equals(value)) {
            head = head.next;
            if (head == null) {
                tail = null; // list is now empty
            }
            size--;
            return true;
        }
        Node<T> prev = head;
        Node<T> current = head.next;
        while (current != null) {
            if (current.value.equals(value)) {
                prev.next = current.next;
                if (current == tail) {
                    tail = prev;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public T peekFirst() {
        if (head == null) {
            throw new NoSuchElementException("list is empty");
        }
        return head.value;
    }

    public T peekLast() {
        if (tail == null) {
            throw new NoSuchElementException("list is empty");
        }
        return tail.value;
    }


    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> cursor = head;

            @Override
            public boolean hasNext() {
                return cursor != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T value = cursor.value;
                cursor = cursor.next;
                return value;
            }
        };
    }
}
