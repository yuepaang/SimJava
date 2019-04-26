import com.simjava.core.Environment;
import com.simjava.core.Event;
import com.simjava.core.Process;
import com.simjava.yield.*;

public class CarTest {

    static int parkingDuration = 5;
    static int drivingDuration = 2;

    private static Yielderable<Event> Car(Environment environment){
        return yield ->{
            int i = 0;
            while (true){
                if (i == 15) break;
                System.out.println("Start parking at " + environment.getNow());
                yield.returning(environment.Timeout(parkingDuration, 1, "parking"));

                System.out.println("Start driving at " + environment.getNow());
                yield.returning(environment.Timeout(drivingDuration, 1, "driving"));
                i++;
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

//        environment.Run(15);
    }
}
