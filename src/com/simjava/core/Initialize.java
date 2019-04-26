package com.simjava.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Initialize extends Event {

    private Environment environment;
    private List<Function> callBackList;
    private Object value;
    private int delay;
    private boolean ok;

    public Initialize(Environment env, Process process) {
        this.environment = env;
        this.callBackList = new ArrayList<>();
        callBackList.add(process.Resume());
        this.value = null;
        this.ok = true;
        env.Schedule(this, URGENT);
    }
}
