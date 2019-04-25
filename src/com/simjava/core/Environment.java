package com.simjava.core;

import java.util.Iterator;

public class Environment {

    private int now;
    private int eventID;
    private EventQueue eventQueue;
    private boolean shouldStop;
    Process activeProcess;

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
        this.eventQueue = new EventQueue();
        this.shouldStop = false;
        this.activeProcess = null;
    }

    public void Step(){
        QueueItem queueItem = eventQueue.Pop();

        if (eventQueue == null){
            setShouldStop(true);
            return;
        }
        Event event = queueItem.getEvent();
        this.now = queueItem.getTime();
        event.Process();
    }

    public void Schedule(Event event, int priority, int delay){
        eventQueue.Push(new QueueItem(event, this.getNow() + delay, priority, getEventID()+1));
    }

    public void Exit(){
        setShouldStop(true);
    }

    public String Run(int util){
        Event utilEvent = new Event(this);
        while (!shouldStop) {
            Step();
        }
        return utilEvent.getValue();
    }

    public Process Process(Iterable<Event> generator, int priority) {
        return new Process(this, generator, priority);
    }

    public Timeout Timeout(int delay, int priority ) {
        return new Timeout(this, delay, "", true, 0);
    }

}
