package com.simjava.core;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ResumeChannel {
    BlockingQueue<String> channel = new LinkedBlockingQueue<String>();

    public void In(String s) throws InterruptedException {
        channel.put(s);
    }

    public String Out() throws InterruptedException {
        return channel.take();
    }
}


//import java.util.LinkedList;
//
//public class ResumeChannel {
//    private LinkedList<String> channel;
//    private final byte[] lock = new byte[1];
//
//    public ResumeChannel(){
//        this.channel = new LinkedList<String>();
//    }
//
//    public void In(String s){
//        synchronized (lock){
//            channel.offer(s);
//        }
//    }
//
//    public String Out(){
//        synchronized (lock){
//            return channel.poll();
//        }
//    }
//}