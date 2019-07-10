package com.logilog.simjava.core;

import com.logilog.simjava.action.Action;

public class Request extends Event {
    public Action<Event> disposeCallback;
    public int time;
    public Process owner;

    public Request(Environment environment, Action<Event> callback, Action<Event> disposeCallback) {
        super(environment);
        callBackList.add(callback);
        this.disposeCallback = disposeCallback;
        time = environment.now;
        owner = environment.activeProcess;
    }

    public void Dispose() {
//        super.finalize();
        if (disposeCallback != null) disposeCallback.invoke(this);
    }
}
