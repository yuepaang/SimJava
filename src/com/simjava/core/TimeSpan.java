package com.simjava.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeUnit.*;

public final class TimeSpan implements Comparable<TimeSpan> {

    private final long _durationInEpsilon;

    public TimeSpan(long duration, TimeUnit unit) {
        if (unit == null) {
            throw new RuntimeException("Can't create TimeSpan object");
        }

        _durationInEpsilon = TimeOperations.getEpsilon().convert(duration, unit);
    }
}
