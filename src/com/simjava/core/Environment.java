package com.simjava.core;

import com.simjava.collections.EventQueue;
import com.simjava.collections.EventQueueNode;
import com.simjava.action.ActionImpl;
import javafx.scene.paint.Stop;

public class Environment {

    public int now;
    public int eventID;
    public EventQueue eventQueue;
    public Process activeProcess;

    public boolean _stopRequested = false;


    public Environment(){
        this.now = 0;
        this.eventID = 0;
        this.eventQueue = new EventQueue(1024);
        this.activeProcess = null;
    }

    public void Step() throws StopSimulationException {
        Event event;
        EventQueueNode next = this.eventQueue.Dequeue();

        now = next.primaryPriority;

        event = next.event;
        try {
            event.Process();
        } catch (StopSimulationException e) {
            doThrow(e);
        }
    }

    public void Schedule(int delay, Event event, int priority){
        if (delay < 0) throw new IllegalArgumentException("Negative delays are not allowed");
        int eventTime = now + delay;
        DoSchedule(eventTime, event, priority);
    }

    public void Schedule(Event event, int priority) {
        DoSchedule(now, event, priority);
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

    public Object Run(int until){
        if (until < now) throw new RuntimeException("Simulation end time must lie in the future");
        Event stopEvent = new Event(this);
        EventQueueNode node = DoSchedule(until, stopEvent, 0);
        node.insertionIndex = -1;
        eventQueue.OnNodeUpdated(node);
        return Run(stopEvent);
    }

    static <E extends Exception> void doThrow(Exception e) throws E {
        throw (E)e;
    }

    public Object Run(Event stopEvent){
        _stopRequested = false;
        if (stopEvent != null){
            if (stopEvent.isProcessed) {
                return stopEvent.value;
            }
            stopEvent.AddCallback(new ActionImpl<>(e -> doThrow(new StopSimulationException(e.value))));
        }

        try {
            // FIXME count = 0
            boolean stop = eventQueue.count() == 0 || _stopRequested;
            while (!stop) {
                Step();
                stop = eventQueue.count() == 0 || _stopRequested;
            }
        } catch (StopSimulationException e) {
            return e.value;
        }
        if (stopEvent == null) return null;
        if (!stopEvent.isTriggered) throw new RuntimeException("No scheduled events left but \"until\" event was not triggered.");
        return stopEvent.value;
    }

    public void StopAsync() {
        _stopRequested = true;
    }

    public Process Process(Iterable<Event> generator, int priority) {
        return new Process(this, generator, priority);
    }

    public Process Process(Iterable<Event> generator) {
        return new Process(this, generator, 0);
    }

    public Timeout Timeout(int delay, int priority) {
        return new Timeout(this, delay, "", true, priority);
    }

    public void Reset() {
        now = 0;
        eventQueue = new EventQueue(1024);
    }
}
