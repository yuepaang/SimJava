package com.simjava.core;

public class ProcessComm{

    Channel<Event> yieldChannel;
    Channel<String> resumeChannel;

    int state;
    String returnVal;

    private class ResumeChannel implements Runnable{

        ReceiveOnlyChannel<String> in;
        SendOnlyChannel<String> out;

        public void run(){
            while (true) {
                String s = in.receive();
            }
        }
    }




}
