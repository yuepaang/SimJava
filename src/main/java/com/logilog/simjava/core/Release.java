package com.logilog.simjava.core;

import com.logilog.simjava.action.Action;

public class Release extends Event {

    public Request request;

    public Release(Environment environment, Request request, Action<Event> callback) {
        super(environment);
        this.request = request;
        callBackList.add(callback);
    }
}
