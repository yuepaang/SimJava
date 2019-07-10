package com.logilog.simjava.action;

public class ActionImpl<T> implements Action<T> {
    Action<T> action;
    public ActionImpl(Action<T> action) {
        this.action = action;
    }
    @Override
    public void invoke(T arg) {
        this.action.invoke(arg);
    }
}

