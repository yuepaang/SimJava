package com.simjava.collections;

import com.simjava.core.Event;

public class EventQueueNode {

    public int primaryPriority;

    public int secondaryPriority;

    public Event event;

    // Represents the order the node was inserted in
    public long insertionIndex;

    // Represents the current position in the queue
    public int queueIndex;

    public EventQueueNode(int primaryPriority, int secondaryPriority, Event event, long insertionIndex, int queueIndex){
        this.primaryPriority = primaryPriority;
        this.secondaryPriority = secondaryPriority;
        this.event = event;
        this.insertionIndex = insertionIndex;
        this.queueIndex = queueIndex;
    }

}
