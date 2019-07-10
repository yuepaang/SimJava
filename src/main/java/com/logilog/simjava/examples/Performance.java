package com.logilog.simjava.examples;

import com.logilog.simjava.core.*;
import com.logilog.simjava.yield.Yielderable;

public class Performance {

    static Yielderable<Event> Test(Environment env) {
        return yield -> {
            while (true) {
                yield.returning(env.Timeout(1, 0));
            }
        };
    }

    public static void main(String[] args){
        long startTime =  System.currentTimeMillis();

        Environment environment = new Environment();
        environment.Process(Test(environment));
        environment.Run(1000000);

        long endTime =  System.currentTimeMillis();
        System.out.println("total time : "+ (endTime-startTime)/1000.0 + "s");
    }
}
