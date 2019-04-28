package com.simjava.core;

public interface TimeFormatter {

    public String buildTimeString(TimeInstant instant);

    public String buildTimeString(TimeSpan span);

    public String getUnit();
}
