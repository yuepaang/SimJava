package com.simjava.yieldreturn;

import java.util.Iterator;

public abstract class Generator<T> implements Iterable<T> {

    private ResultHandler<T> resultHandler;

    /**
     * Yield a single result. To be called from within your "run" method.
     *
     * @param t
     */
    protected final void yield(final T t) {
        try {
            resultHandler.handleResult(t);
        } catch (CollectionAbortedException ex) {
            // suppress
        }
    }

    /**
     * The method that will generate your results. Call yield() in here.
     */
    protected abstract void run();

    @Override
    public final Iterator<T> iterator() {
        return new ThreadedYieldAdapter<T>().adapt(new Collector<T>() {

            @Override
            public void collect(final ResultHandler<T> handler) throws CollectionAbortedException {
                resultHandler = handler;
                run();
            }
        }).iterator();
    }
}