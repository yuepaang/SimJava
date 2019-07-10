package com.logilog.simjava.examples;

import com.logilog.simjava.core.*;
import com.logilog.simjava.yield.Yielderable;

public class Car {

    static int parkingDuration = 5;
    static int drivingDuration = 2;

    public static Yielderable<Event> Car(Environment environment){
        return yield -> {
            while (true) {
//                System.out.println("Start parking at " + environment.now);
                environment.Log("info", "Start parking at " + environment.now);
                yield.returning(environment.Timeout(parkingDuration, 0));
//                System.out.println("Start driving at " + environment.now);
                environment.Log("info", "Start driving at " + environment.now);
                yield.returning(environment.Timeout(drivingDuration, 0));
            }
        };
    }

    public static void main(String[] args){
        long startTime =  System.currentTimeMillis();

        Environment environment = new Environment();
        environment.Process(Car(environment));
        environment.Run(15);

        long endTime =  System.currentTimeMillis();
//        System.out.println("total time : "+ (endTime-startTime) + "ms");
        environment.Log("info", "total time : "+ (endTime-startTime) + "ms");
    }
}
