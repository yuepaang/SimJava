package com.simjava.yieldreturn;

public interface Collector<T> {

    /**
     * Perform the collection operation.
     *
     * @param handler The processor object to return results to.
     * @throws CollectionAbortedException The collection operation was aborted part way through.
     */
    void collect(ResultHandler<T> handler) throws CollectionAbortedException;
}

