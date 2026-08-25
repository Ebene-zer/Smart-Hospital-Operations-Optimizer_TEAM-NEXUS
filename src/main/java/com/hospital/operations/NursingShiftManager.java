package com.hospital.operations;

import com.hospital.structures.core.CircularQueue;

//handle nurse shift assignments using a circular queue
public class NursingShiftManager {
    private final CircularQueue<String> nurses;

    public NursingShiftManager(int capacity) {
        this.nurses = new CircularQueue<>(capacity);
    }

    public void addNurse(String nurseName) {
        nurses.enqueue(nurseName);
    }

    public String assignNurse() {
        if (nurses.isEmpty()) {
            throw new IllegalStateException("No nurse available for assignment");
        }
        String nurse = nurses.dequeue();
        nurses.enqueue(nurse);
        return nurse;
    }

    public String getNextNurse(){
        return nurses.peek();
    }

    public boolean hasNurses() {
        return !nurses.isEmpty();
    }

    public int getNurseCount() {
        return nurses.size();
    }


}
