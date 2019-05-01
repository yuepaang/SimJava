package com.simjava.yieldreturn;


public interface YieldAdapter<T> {

    YieldAdapterIterable<T> adapt(Collector<T> client);
}