package com.simjava.core;

import com.simjava.action.Action;
import com.simjava.action.ActionImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Initialize extends Event {

    public Environment environment;
    public List<Action> callBackList;
    public Object value;
    public int delay;
    public boolean ok;

    public Initialize(Environment env, Process process) {
        this.environment = env;
        this.callBackList = new ArrayList<>();
        callBackList.add(new ActionImpl<Event>(e -> process.Resume(e)));
        this.value = null;
        this.ok = true;
        env.Schedule(this, URGENT);
    }
}
