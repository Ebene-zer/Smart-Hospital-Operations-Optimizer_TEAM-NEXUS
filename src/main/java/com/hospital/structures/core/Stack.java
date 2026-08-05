package com.hospital.structures.core;

import java.util.EmptyStackException;

public class Stack<T> {

    private final DynamicArray<T> backing;

    public Stack() {
        this.backing = new DynamicArray<>();
    }

    public boolean isEmpty() {
        return backing.isEmpty();
    }

    public int size() {
        return backing.size();
    }


    public void push(T item) {
        backing.addLast(item);
    }


    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return backing.remove(backing.size() - 1);
    }


    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return backing.get(backing.size() - 1);
    }
}