import com.simjava.action.ActionImpl;
import com.simjava.core.*;
import com.simjava.core.Process;
import com.simjava.yield.*;

public class CarTest {

    static int parkingDuration = 5;
    static int drivingDuration = 2;

    private static Iterable<Event> Car(Environment environment){
        return new Generator<Event>() {
            @Override
            protected void run() throws InterruptedException {
                for (int i = 0; i < 5; i++) {
                    System.out.println("Start parking at " + environment.now);
                    yield(environment.Timeout(parkingDuration, 0));
                    System.out.println("Start driving at " + environment.now);
                    yield(environment.Timeout(drivingDuration, 0));
                }
            }
        };

    }


    public static void main(String[] args){
        Environment environment = new Environment();

//        Event evt = new Event(environment);
//        evt.value = "test";
//        evt.AddCallback(new ActionImpl<>(e -> System.out.println(e.value.toString())));
//        evt.callBackList.get(0).invoke(evt);

        environment.Process(Car(environment), 0);
        System.out.println(environment.eventQueue.count());

        environment.Run(15);
    }
}
