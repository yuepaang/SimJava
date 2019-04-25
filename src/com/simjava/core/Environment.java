package com.simjava.core;

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


    public Environment(){
        this.now = 0;
        this.eventID = 0;
        this.eventQueue = new EventQueue();
        this.shouldStop = false;
    }

    public void Step(){
        QueueItem queueItem = eventQueue.Pop();
        if (eventQueue == null){
            setShouldStop(true);
            return;
        }

        this.now = queueItem.getTime();
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
        return utilEvent.Value();
    }
}
