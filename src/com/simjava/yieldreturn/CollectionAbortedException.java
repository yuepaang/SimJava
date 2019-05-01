package com.simjava.yieldreturn;

public class CollectionAbortedException extends Exception {

    public CollectionAbortedException() {
    }

    public CollectionAbortedException(String message) {
        super(message);
    }

    public CollectionAbortedException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectionAbortedException(Throwable cause) {
        super(cause);
    }
}
