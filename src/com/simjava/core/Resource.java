package com.simjava.core;

import com.simjava.action.ActionImpl;

import java.util.*;

public class Resource {

    public int capacity;
    public int inUse;
    public int remaining;

    public Environment environment;

    public LinkedList<Request> requestQueue;
    public Queue<Release> releaseQueue;
    public HashSet<Request> user;
    public List<Event> whenAnyQueue;
    public List<Event> whenFullQueue;
    public List<Event> whenEmptyQueue;
    public List<Event> whenChangeQueue;

    public Resource(Environment environment, int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be larger than 0");
        this.environment = environment;
        this.capacity = capacity;
        requestQueue = new LinkedList<>();
        releaseQueue = new PriorityQueue<>();
        user = new HashSet<>();
        whenAnyQueue = new ArrayList<>();
        whenFullQueue = new ArrayList<>();
        whenEmptyQueue = new ArrayList<>();
        whenChangeQueue = new ArrayList<>();
    }

    public Request Request() {
        var request = new Request(environment, new ActionImpl<Event>(e -> TriggerRelease(e)), new ActionImpl<>(e -> DisposeCallback(e)));
        requestQueue.addLast(request);
        TriggerRequest(null);
        return request;
    }

    public Release Release(Request request) {
        Release release = new Release(environment, request, new ActionImpl<>(e -> TriggerRequest(e)));
        releaseQueue.offer(release);
        TriggerRelease(null);
        return release;
    }

    public Event WhenAny() {
        var whenAny = new Event(environment);
        whenAnyQueue.add(whenAny);
        TriggerWhenAny();
        return whenAny;
    }

    public Event whenFull() {
        var whenFull = new Event(environment);
        whenFullQueue.add(whenFull);
        TriggerWhenFull();
        return whenFull;
    }

    public Event WhenEmpty() {
        var whenEmpty = new Event(environment);
        whenEmptyQueue.add(whenEmpty);
        TriggerWhenEmpty();
        return whenEmpty;
    }

    public Event WhenChange() {
        var whenChange = new Event(environment);
        whenChangeQueue.add(whenChange);
        return whenChange;
    }

    protected void DisposeCallback(Event event) {
        var request = (Request)event;
        if (request != null) {
            Release(request);
        }
    }

    protected void DoRequest(Request request) {
        if (user.size() < capacity) {
            user.add(request);
            request.Succeed(null, 0);
        }
    }

    protected void DoRelease(Release release) {
        if (!user.remove(release.request))
            throw new RuntimeException("Released request does not have a user.");
        release.Succeed(null, 0);
    }

    protected void TriggerRequest(Event event) {
        while (requestQueue.size() > 0) {
            var request = requestQueue.getFirst();
            DoRequest(request);
            if (request.isTriggered) {
                requestQueue.removeFirst();
                TriggerWhenEmpty();
                TriggerWhenChange();
            } else break;
        }
    }

    protected void TriggerRelease(Event event) {
        while (releaseQueue.size() > 0) {
            var release = releaseQueue.peek();
            if (release.request.isAlive) {
                if (!requestQueue.remove(release.request))
                    throw new RuntimeException("Failed to cancel a request.");
                release.Succeed(null, 0);
                releaseQueue.poll();
            } else {
                DoRelease(release);
                if (release.isTriggered) {
                    releaseQueue.poll();
                    TriggerWhenAny();
                    TriggerWhenFull();
                    TriggerWhenChange();
                } else break;
            }
        }
    }

    protected  void TriggerWhenAny() {
        if (remaining > 0) {
            if (whenAnyQueue.size() == 0) return;
            for (Event evt:whenAnyQueue) {
                evt.Succeed(null, 0);
                whenAnyQueue.clear();
            }
        }
    }

    protected void TriggerWhenFull() {
        if (inUse == 0) {
            if (whenFullQueue.size() == 0) return;
            for (Event evt:whenFullQueue) {
                evt.Succeed(null, 0);
                whenFullQueue.clear();
            }
        }
    }

    protected void TriggerWhenEmpty() {
        if (remaining == 0) {
            if (whenEmptyQueue.size() == 0) return;
            for (Event evt:whenEmptyQueue) {
                evt.Succeed(null, 0);
                whenEmptyQueue.clear();
            }
        }
    }

    protected void TriggerWhenChange() {
        if (whenChangeQueue.size() == 0) return;
        for (Event evt:whenChangeQueue) {
            evt.Succeed(null, 0);
            whenChangeQueue.clear();
        }
    }
}
