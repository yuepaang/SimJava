package com.logilog.simjava.action;

public interface Action<T> {
    public void invoke(T arg);
}
