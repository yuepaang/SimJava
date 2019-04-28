package com.simjava.core;

import com.simjava.yield.Yielderable;

import java.util.Iterator;

public class Environment {

    public int now;
    public int eventID;
    public EventQueue eventQueue;
    public boolean shouldStop;
    public Process activeProcess;

    public Environment(){
        this.now = 0;
        this.eventID = 0;
        this.eventQueue = new EventQueue();
        this.shouldStop = false;
        this.activeProcess = null;
    }

    public void Step(){
        QueueItem queueItem = this.eventQueue.Pop();

        if (this.eventQueue == null){
            setShouldStop(true);
            return;
        }
        Event event = queueItem.getEvent();
        // Time of QueueItem
        setNow(queueItem.getTime());
        System.out.println("Env Now is " + getNow());
        event.Process();
    }

    public void Schedule(Event event, int priority, int delay){
//        eventQueue.Push(new QueueItem(event, this.getNow() + delay, priority, getEventID()+1));
        eventQueue.Push(DoSchedule(delay, event, priority));
        System.out.println("push into queue " + event.getValue());
    }
    public void Schedule(Event event, int priority) {
        eventQueue.Push(DoSchedule(getNow(), event, priority));
    }


    public QueueItem DoSchedule(int delay, Event event, int priority){
        System.out.println("QueueItem time is " + (this.getNow() + delay));
        return new QueueItem(event, this.getNow() + delay, priority, getEventID()+1);
    }

    public void Exit(){
        setShouldStop(true);
    }

    public String Run(int util){
        while (this.getNow() < util && !this.isShouldStop()){
            Step();
        }

        return Run(new Event(this));
    }

    public String Run(Event stopEvent){
        setShouldStop(false);
        if (stopEvent != null){
            if (stopEvent.isProcessed()) return stopEvent.getValue();
        }
        while (!isShouldStop()) {
            Step();
        }
        if (stopEvent == null) return null;
        return stopEvent.getValue();
    }

    public Process Process(Yielderable<Event> generator, int priority) {
        return new Process(this, generator, priority);
    }

    public Timeout Timeout(int delay, int priority, String data) {
        return new Timeout(this, delay, data, true, priority);
    }

    public void Reset(){
        setNow(0);
        setEventQueue(new EventQueue());
    }


}
