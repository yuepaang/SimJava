import com.simjava.core.Environment;
import com.simjava.core.Event;
import com.simjava.core.Generator;
import com.simjava.core.Process;
import com.simjava.yield.*;

public class CarTest {

    static int parkingDuration = 5;
    static int drivingDuration = 2;

    private static Iterable<Event> Car(Environment environment){
        return new Generator<>() {
            @Override
            protected void run() throws InterruptedException {
                for (int i = 0; i < 5; i++) {
                    yield(environment.Timeout(parkingDuration, 0, "parking"));
                    yield(environment.Timeout(drivingDuration, 0, "driving"));
                }
            }
        };

    }


    public static void main(String[] args){
        Environment environment = new Environment();

//        for (Event e : Car(environment)){
//            System.out.println(e.getValue());
//        }
        environment.Process(Car(environment), 0);
        System.out.println(environment.getEventQueue().Len());
//        System.out.println(environment.getEventQueue().Pop().getTime());
//        System.out.println(environment.getEventQueue().Pop().getTime());
//        System.out.println(environment.getEventQueue().Pop().getTime());
//        System.out.println(environment.getEventQueue().Pop().getTime());
//        System.out.println(environment.getEventQueue().Pop().getTime());
//        System.out.println(environment.getEventQueue().Pop().getTime());

//        System.out.println(environment.getEventQueue().Pop().getTime() + environment.getEventQueue().Pop().getEvent().getValue());

        environment.Run(15);
    }
}
