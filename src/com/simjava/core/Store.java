package com.simjava.core;

import com.simjava.action.ActionImpl;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.List;
import java.util.ArrayList;

public class Store {
    public int Capacity;

    public Environment Environment;

    public Queue<StorePut> PutQueue;
    public Queue<StoreGet> GetQueue;
    public Queue<Object> Items;
    public List<Event> WhenNewQueue;
    public List<Event> WhenAnyQueue;
    public List<Event> WhenFullQueue;
    public List<Event> WhenEmptyQueue;
    public List<Event> WhenChangeQueue;

    public Store(Environment environment, int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be > 0");
        Environment = environment;
        Capacity = capacity;
        PutQueue = new PriorityQueue<StorePut>();
        GetQueue = new PriorityQueue<StoreGet>();
        Items = new PriorityQueue<Object>();
        WhenNewQueue = new ArrayList<Event>();
        WhenAnyQueue = new ArrayList<Event>();
        WhenFullQueue = new ArrayList<Event>();
        WhenEmptyQueue = new ArrayList<Event>();
        WhenChangeQueue = new ArrayList<Event>();
    }
    public Store(Environment environment, Object item, int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be > 0");
        Environment = environment;
        Capacity = capacity;
        PutQueue = new PriorityQueue<StorePut>();
        GetQueue = new PriorityQueue<StoreGet>();
        Items = new PriorityQueue<Object>();
        Items.offer(item);
        WhenNewQueue = new ArrayList<Event>();
        WhenAnyQueue = new ArrayList<Event>();
        WhenFullQueue = new ArrayList<Event>();
        WhenEmptyQueue = new ArrayList<Event>();
        WhenChangeQueue = new ArrayList<Event>();
        if (capacity < Items.size()) throw new IllegalArgumentException("There are more initial items than there is capacity.");
    }

    public StorePut Put(Object item) {
        var put = new StorePut(Environment, new ActionImpl<>(e -> TriggerGet(e)), item);
        PutQueue.offer(put);
        TriggerPut(null);
        return put;
    }

    public StoreGet Get() {
        var get = new StoreGet(Environment, new ActionImpl<>(e -> TriggerPut(e)));
        GetQueue.offer(get);
        TriggerGet(null);
        return get;
    }

    public Event WhenNew() {
        var whenNew = new Event(Environment);
        WhenNewQueue.add(whenNew);
        return whenNew;
    }

    public Event WhenAny() {
        var whenAny = new Event(Environment);
        WhenAnyQueue.add(whenAny);
        TriggerWhenAny();
        return whenAny;
    }

    public Event WhenFull() {
        var whenFull = new Event(Environment);
        WhenFullQueue.add(whenFull);
        TriggerWhenFull();
        return whenFull;
    }

    public Event WhenEmpty() {
        var whenEmpty = new Event(Environment);
        WhenEmptyQueue.add(whenEmpty);
        TriggerWhenEmpty();
        return whenEmpty;
    }

    public Event WhenChange() {
        var whenChange = new Event(Environment);
        WhenChangeQueue.add(whenChange);
        return whenChange;
    }

    protected void DoPut(StorePut put) {
        if (Items.size() < Capacity) {
            Items.offer(put.value);
            put.Succeed(null, 0);
        }
    }

    protected void DoGet(StoreGet get) {
        if (Items.size() > 0) {
            var item = Items.poll();
            get.Succeed(item, 0);
        }
    }

    protected void TriggerPut(Event event) {
        while (PutQueue.size() > 0) {
            var put = PutQueue.peek();
            DoPut(put);
            if (put.isTriggered) {
                PutQueue.poll();
                TriggerWhenNew();
                TriggerWhenAny();
                TriggerWhenFull();
                TriggerWhenChange();
            } else break;
        }
    }

    protected void TriggerGet(Event event) {
        while (GetQueue.size() > 0) {
            var get = GetQueue.peek();
            DoGet(get);
            if (get.isTriggered) {
                GetQueue.poll();
                TriggerWhenEmpty();
                TriggerWhenChange();
            } else break;
        }
    }

    protected void TriggerWhenNew() {
        if (WhenNewQueue.size() == 0) return;
        for (Event evt : WhenNewQueue) {
            evt.Succeed(null, 0);
            WhenNewQueue.clear();
        }
    }

    protected void TriggerWhenAny() {
        if (Items.size() > 0) {
            if (WhenAnyQueue.size() == 0) return;
            for (Event evt : WhenAnyQueue) {
                evt.Succeed(null, 0);
                WhenAnyQueue.clear();
            }
        }
    }

    protected void TriggerWhenFull() {
        if (Items.size() == Capacity) {
            if (WhenFullQueue.size() == 0) return;
            for (Event evt : WhenFullQueue) {
                evt.Succeed(null, 0);
                WhenFullQueue.clear();
            }
        }
    }

    protected void TriggerWhenEmpty() {
        if (Items.size() == 0) {
            if (WhenEmptyQueue.size() == 0) return;
            for (Event evt : WhenEmptyQueue) {
                evt.Succeed(null, 0);
                WhenEmptyQueue.clear();
            }
        }
    }

    protected void TriggerWhenChange() {
        if (WhenChangeQueue.size() == 0) return;
        for (Event evt : WhenChangeQueue) {
            evt.Succeed(null, 0);
            WhenChangeQueue.clear();
        }
    }
}
