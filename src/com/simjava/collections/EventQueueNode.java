package com.simjava.collections;

import com.simjava.core.Event;

public class EventQueueNode {

    private int primaryPriority;

    private int secondaryPriority;

    public Event event;

    public long insertionIndex;

    public int queueIndex;

    public EventQueueNode(int primaryPriority, int secondaryPriority, Event event, long insertionIndex, int queueIndex){
        this.primaryPriority = primaryPriority;
        this.secondaryPriority = secondaryPriority;
        this.event = event;
        this.insertionIndex = insertionIndex;
        this.queueIndex = queueIndex;
    }

    public int getPrimaryPriority() {
        return primaryPriority;
    }

    public void setPrimaryPriority(int primaryPriority) {
        this.primaryPriority = primaryPriority;
    }

    public int getSecondaryPriority() {
        return secondaryPriority;
    }

    public void setSecondaryPriority(int secondaryPriority) {
        this.secondaryPriority = secondaryPriority;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getInsertionIndex() {
        return insertionIndex;
    }

    public void setInsertionIndex(long insertionIndex) {
        this.insertionIndex = insertionIndex;
    }

    public int getQueueIndex() {
        return queueIndex;
    }

    public void setQueueIndex(int queueIndex) {
        this.queueIndex = queueIndex;
    }
}
