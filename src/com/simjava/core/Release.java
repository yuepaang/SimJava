package com.simjava.core;

import com.simjava.action.Action;

public class Release extends Event {

    public Request request;

    public Release(Environment environment, Request request, Action<Event> callback) {
        super(environment);
        this.request = request;
        callBackList.add(callback);
    }
}
