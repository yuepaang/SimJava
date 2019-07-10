package com.logilog.simjava.examples;

import com.logilog.simjava.yield.*;
import java.util.ArrayList;

public class YielderableExample {
    static ArrayList<Integer> results = new ArrayList<Integer>();

    public static Yielderable<Integer> OneToFive(){
        return yield -> {
            for (int i = 1; i < 10; i++){
                if (i == 6) yield.breaking();
                yield.returning(i);
            }
        };
    }

    public static void main(String[] args){
        for (Integer number : OneToFive()) results.add(number);
        for (int i = 0; i < results.size(); i++) System.out.println(results.get(i));

    }
}
