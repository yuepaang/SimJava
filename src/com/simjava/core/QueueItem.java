package com.simjava.core;

public class QueueItem {
    private Event event;
    private int time;
    private int priority;
    private int idx;
    private int eventID;

    public QueueItem(){
        this.event = new Event();
        this.time = 0;
        this.priority = 0;
        this.idx = 0;
        this.eventID = 0;
    }

    public QueueItem(Event event, int time, int priority, int eventID){
        this.event = event;
        this.time = time;
        this.priority = priority;
        this.eventID = eventID;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
}
