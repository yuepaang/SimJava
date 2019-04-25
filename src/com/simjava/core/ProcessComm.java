package com.simjava.core;

public class ProcessComm{

    Channel<Event> yieldChannel;
    Channel<String> resumeChannel;

    int state;
    String returnVal;

    public ProcessComm(){
        Channel<Event> ch0 = Channel.make();
        Channel<String> ch1 = Channel.make();
        this.yieldChannel = ch0;
        this.resumeChannel = ch1;
        this.state = 0;
        this.returnVal = "";
    }

    private class ResumeChannel implements Runnable{

        ReceiveOnlyChannel<String> in;
        SendOnlyChannel<String> out;

        public void run(){
            while (true) {
                String s = in.receive();
            }
        }
    }

    private class YieldChannel implements Runnable{

        ReceiveOnlyChannel<Event> in;
        SendOnlyChannel<Event> out;
        ProcessComm pc;

        public YieldChannel(ReceiveOnlyChannel<Event> _in, SendOnlyChannel<Event> _out, ProcessComm _pc){
            in = _in;
            out = _out;
            pc = _pc;
        }

        public void run(){
            while (true) {
                if (pc.state == 1){
                    pc.state = 0;
                    pc.yieldChannel.receive();
                }
                Event event = in.receive();
            }
        }
    }




}
