package com.simjava.core;

import java.util.ArrayList;
import java.util.LinkedList;


public class ProcessCommunication {
    final int stateSuspended = 0;
    final int stateRunning = 1;

    private YieldChannel yieldChannel;
    private ResumeChannel resumeChannel;
    private int state;
    int returnValue;

    public ProcessCommunication(){
        this.yieldChannel = new YieldChannel();
        this.resumeChannel = new ResumeChannel();
        this.state = stateSuspended;
        this.returnValue = 0;

    }

    public String Yield(Event event){
        if (this.state == stateRunning){
            this.state = stateSuspended;
            yieldChannel.In(event);
        }
        String resumeVal = resumeChannel.Out();
        this.state = stateRunning;
        return resumeVal;
    }

    public Event Resume(String s){
        resumeChannel.In(s);
        return yieldChannel.Out();
    }

    public void Finish(int x){
        returnValue = x;
    }
}
