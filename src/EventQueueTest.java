import com.simjava.core.Event;
import com.simjava.core.EventQueue;
import com.simjava.core.QueueItem;


public class EventQueueTest {
    public static void main(String[] args){
        EventQueue eventQueue = new EventQueue();
        eventQueue.Push(new QueueItem(new Event(), 90, 1, 5));
        eventQueue.Push(new QueueItem(new Event(), 100, 2, 1));
        eventQueue.Push(new QueueItem(new Event(), 110, 0, 2));
        eventQueue.Push(new QueueItem(new Event(), 100, 1, 3));
        eventQueue.Push(new QueueItem(new Event(), 90, 0, 4));
        eventQueue.Push(new QueueItem(new Event(), 90, 0, 0));
        eventQueue.Push(new QueueItem(new Event(), 100, 0, 6));

        System.out.printf("Queue size is %d\n", eventQueue.Len());
        System.out.printf("Expected EventID order is %d, %d, %d, %d, %d, %d, %d\n", 0, 4, 5, 6, 3, 1, 2);

        for (int i = 0; i < 7; i++){
            QueueItem item = eventQueue.Pop();
            System.out.println(item.getEventID());
        }
    }
}