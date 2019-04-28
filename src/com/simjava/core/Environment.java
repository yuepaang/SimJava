package com.simjava.core;

import com.simjava.collections.EventQueue;
import com.simjava.collections.EventQueueNode;

public class Environment {

    private int now;
    private int eventID;
    private EventQueue eventQueue;
    private boolean shouldStop;
    private Process activeProcess;

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public EventQueue getEventQueue() {
        return eventQueue;
    }

    public void setEventQueue(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public boolean isShouldStop() {
        return shouldStop;
    }

    public void setShouldStop(boolean shouldStop) {
        this.shouldStop = shouldStop;
    }

    public Process getActiveProcess() {
        return activeProcess;
    }

    public void setActiveProcess(Process activeProcess) {
        this.activeProcess = activeProcess;
    }

    public Environment(){
        this.now = 0;
        this.eventID = 0;
        this.eventQueue = new EventQueue(1024);
        this.shouldStop = false;
        this.activeProcess = null;
    }

    public void Step(){
        Event event;
        EventQueueNode next = this.eventQueue.Dequeue();

        this.setNow(next.primaryPriority);
        event = next.event;
        event.Process();
    }

    public void Schedule(int delay, Event event, int priority){
//        eventQueue.Push(new QueueItem(event, this.getNow() + delay, priority, getEventID()+1));
        int eventTime = getNow() + delay;
        DoSchedule(eventTime, event, priority);
    }

    public void Schedule(Event event, int priority) {
        DoSchedule(getNow(), event, priority);
    }

    public EventQueueNode DoSchedule(int delay, Event event, int priority){
        if (eventQueue.maxSize() == eventQueue.count()) {
            EventQueue oldSchedule = eventQueue;
            eventQueue = new EventQueue(1024);
            for (EventQueueNode e: oldSchedule) {
                eventQueue.Enqueue(e.primaryPriority, e.event, e.secondaryPriority);
            }
        }
        return eventQueue.Enqueue(delay, event, priority);
    }

    public void Exit(){
        setShouldStop(true);
    }

    public Object Run(int until){
        if (until < getNow()) throw new RuntimeException("Simulation end time must lie in the future");
        Event stopEvent = new Event(this);
        EventQueueNode node = DoSchedule(until, stopEvent, 0);
        node.insertionIndex = -1;
        eventQueue.OnNodeUpdated(node);
        return Run(stopEvent);
    }

    public Object Run(Event stopEvent){
        setShouldStop(false);
        if (stopEvent != null){
            if (stopEvent.isProcessed) return stopEvent.value;
        }
        while (!isShouldStop()) {
            Step();
        }
        if (stopEvent == null) return null;
        return stopEvent.value;
    }

    public Process Process(Iterable<Event> generator, int priority) {
        return new Process(this, generator, priority);
    }

    public Timeout Timeout(int delay, int priority, String data) {
        return new Timeout(this, delay, data, true, priority);
    }

    public void Reset(){
        setNow(0);
        setEventQueue(new EventQueue(1024));
    }


}
