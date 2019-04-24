package com.simjava.core;

public class ProcessWrapper {
    public ProcessCommunication pc;

    public ProcessWrapper(){
        pc = new ProcessCommunication();
        pc.Yield(new Event());
        pc.Finish(0);
    }

}
