package com.simjava.core;


import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;


public class EventQueue {
    private PriorityQueue<QueueItem> queue;

    public EventQueue(){
        queue = new PriorityQueue<QueueItem>(new Comparator<QueueItem>() {
            @Override
            public int compare(QueueItem o1, QueueItem o2) {
                if (o1.getTime() < o2.getTime()) {
                    return -1;
                }
                if (o1.getTime() == o2.getTime()){
                    if (o1.getPriority() < o2.getPriority()){
                        return -1;
                    }

                    if (o1.getPriority() == o2.getPriority()){
                        if (o1.getEventID() < o2.getEventID()){
                            return -1;
                        }
                    }
                }
                return 1;
            }
        });
    }

    public void Push(QueueItem queueItem){
        int n = queue.size();
        queueItem.setIdx(n);
        queue.offer(queueItem);
    }

    public QueueItem Pop(){
        if (queue.size() <= 0){
            return null;
        }
        QueueItem queueItem = queue.poll();
        queueItem.setIdx(-1);
        return queueItem;
    }

    public int Len(){
        return queue.size();
    }

}
