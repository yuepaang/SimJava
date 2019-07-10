package com.logilog.simjava.core;

public class StopSimulationException extends Exception {
    public Object value;

    public StopSimulationException(Object value) {
        this.value = value;
    }
}
