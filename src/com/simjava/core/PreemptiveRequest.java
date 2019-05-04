package com.simjava.core;

import com.simjava.action.Action;

public class PreemptiveRequest extends Request implements Comparable<PreemptiveRequest>{

    public double priority;
    public boolean preempt;
    public boolean isPreempt;

    public PreemptiveRequest(Environment environment, Action<Event> callback, Action<Event> disposeCallback, double priorty, boolean preempt) {
        super(environment, callback, disposeCallback);
        this.priority = priorty;
        this.preempt = preempt;
    }

    @Override
    public int compareTo(PreemptiveRequest other) {
        if (this.priority > other.priority) return 1;
        else if (this.priority < other.priority) return -1;

        if (this.time > other.time) return 1;
        else if (this.time < other.time) return -1;

        if (!this.preempt && other.preempt) return 1;
        else if (this.preempt && other.preempt) return -1;

        return 0;
    }

//    @Override
//    public int compareTo(Object o) {
//        if (o instanceof PreemptiveRequest) return compareTo(o);
//        if (o == null) return 1;
//        throw new IllegalArgumentException("Can only compare to other objects of type PreemptiveRequest");
//    }
}
