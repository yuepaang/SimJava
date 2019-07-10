package com.logilog.simjava.yield;

import java.util.Iterator;

public interface ClosableIterator<T> extends Iterator<T>, AutoCloseable {
    void close();
}
