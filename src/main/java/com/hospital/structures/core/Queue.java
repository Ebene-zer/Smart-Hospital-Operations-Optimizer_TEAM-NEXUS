package com.hospital.structures.core;

public class Queue<T> {
    // generic queue implementation using array
    //fields
    private T[] elements;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    //constructor
    public Queue(int capacity){
        this.capacity = capacity;
        elements = (T[]) new Object[capacity];
        front = 0;
        rear = -1;
        size = 0;
    }

    //methods
    public void enqueue(T element){
        if (size == capacity){
            throw new IllegalStateException("Overflow: Queue is full");
        }
        rear = (rear + 1) % capacity;
        elements[rear] = element;
        size++;
    }

    public T dequeue(){
        if (size == 0){
            throw new IllegalStateException("Underflow: Queue is empty");
        }
        T element = elements[front];
        front = (front + 1) % capacity;
        size--;
        return element;
    }

    public T peek(){
        if (size == 0){
            throw new IllegalStateException("Underflow: Queue is empty");
        }
        return elements[front];
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public boolean isFull(){
        return size == capacity;
    }

    public int size(){
        return size;
    }

    public void clear(){
        front = 0;
        rear = -1;
        size = 0;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Queue: [");
        for (int i = 0; i < size; i++){
            sb.append(elements[(front + i) % capacity]);
            if (i < size - 1){
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public void resize(int newCapacity){
        if (newCapacity < size){
            throw new IllegalArgumentException("Capacity cannot be less than the current size of the queue");
        }
        T[] newElements = (T[]) new Object[newCapacity];
        for (int i = 0; i < size; i++){
            newElements[i] = elements[(front + i) % capacity];
        }
        elements = newElements;
        front = 0;
        rear = size - 1;
        capacity = newCapacity;
    }


}
