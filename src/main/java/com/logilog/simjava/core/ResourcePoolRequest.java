package com.logilog.simjava.core;

import com.logilog.simjava.action.Action;

import java.util.function.Function;

public class ResourcePoolRequest extends Request {

    public Function<Object, Boolean> filter;

    public ResourcePoolRequest(Environment environment, Action<Event> callback, Action<Event>disposeCallback, Function<Object, Boolean> filter) {
        super(environment, callback, disposeCallback);
        this.filter = filter;
    }
}
