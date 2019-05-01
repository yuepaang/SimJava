package com.simjava.yieldreturn;


public interface YieldAdapterIterable<T> extends Iterable<T> {

    /**
     * Returns an iterator over the results.
     */
    YieldAdapterIterator<T> iterator();
}