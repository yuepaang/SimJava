package com.simjava.core;

import java.util.ArrayList;

public class Process extends Event{

    private Environment env;
    private Event target;
//    private ProcessCommunication pc;

//    public Process(Environment env, ProcessCommunication pc){
//        this.event = new Event(env);
//        this.env = env;
//        this.pc = pc;
//    }

//    public void Init(){
//        Event initEvent = new Event(this.env)
//        this.env.Schedule(initEvent, super.priorityUrgent, 0);
//    }
//
//    public void Resume(Event event){
//        this.env.activeProcess = this;
//
//        while (true){
//            String eventVal = event.Value();
//            if (pc.Resume(eventVal) != null) {
//                event = pc.Resume(eventVal);
//            } else {
//                if (this.event.getEventValue().isPending()){
//                    this.event.getEventValue().setData("");
//                }
//                this.env.Schedule(this.event, super.priorityNormal, 0);
//                break;
//            }
//        }
//        // callbacks
//
//        this.env.activeProcess = null;
//    }
//
//    public int ReturnValue(){
//        return this.pc.returnValue;
//    }

    private class Initialize extends Event {
      public Initialize(Environment environment, Process process, int priority){
            environment.Schedule(this, priority, 0);
        }
    }

    public Process(Environment env, int priority){
        this.env = env;
        target = new Initialize(env, this, priority);
    }
}