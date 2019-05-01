package com.simjava.yieldreturn;

public interface ResultHandler<T> {

    /**
     * This method is called by collectors whenever a result is collected.
     *
     * @param value The collected result
     * @throws CollectionAbortedException The client code requests that the collection is aborted
     */
    void handleResult(T value) throws CollectionAbortedException;
}