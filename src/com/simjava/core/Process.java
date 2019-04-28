package com.simjava.core;

import com.simjava.action.Action;
import com.simjava.action.ActionImpl;
import com.simjava.yield.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class Process extends Event{

    public Environment environment;
    public List<Action> callBackList;
    public Object value;

    public Event target;
    public ClosableIterator<Event> generator;


    public boolean IsAlive(){
        return this.value.equals(PENGDING);
    }

    public Process(Environment environment, Yielderable<Event> generator){
        this.environment = environment;
        this.callBackList = new ArrayList<>();
        this.value = PENGDING;

        this.generator = generator.iterator();
        this.target = new Initialize(environment, this);
    }

    public void Interrupt(String cause) {
        new Interruption(this, cause);
    }

    protected void Resume(Event event) {
        this.environment.activeProcess = this;
        while (true) {
            if (event.OK()) {
            }

        }
    }


}