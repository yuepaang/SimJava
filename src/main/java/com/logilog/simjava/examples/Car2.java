package com.logilog.simjava.examples;

import com.logilog.simjava.core.*;
import com.logilog.simjava.core.Process;
import com.logilog.simjava.yield.Yielderable;


public class Car2 {

    public static class Car {
        Environment env;
        Process action;

        public Car(Environment env) {
            this.env = env;
            this.action = env.Process(Run());
        }

        public Yielderable<Event> Run() {
            return yield -> {
                while (true) {
                    System.out.println("Start parking and charging at " + env.now);
                    int chargeDuration = 5;
                    yield.returning(env.Process(Charge(chargeDuration)));

                    System.out.println("Start driving at " + env.now);
                    int tripDuration = 2;
                    yield.returning(env.Timeout(tripDuration, 0));
                }
            };
        }

        private Iterable<Event> Charge(int duration){
            return new Generator<Event>() {
                @Override
                protected void run() throws InterruptedException {
                    yield(env.Timeout(duration, 0));
                }
            };
        }
    }

    public static void main(String[] args){
        long startTime =  System.currentTimeMillis();

        Environment environment = new Environment();
        Car c = new Car(environment);
        environment.Run(15);

        long endTime =  System.currentTimeMillis();
        System.out.println("total time : "+ (endTime-startTime) + "ms");
    }
}
