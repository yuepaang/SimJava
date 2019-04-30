import com.simjava.core.*;

public class CarTest {

    static int parkingDuration = 5;
    static int drivingDuration = 2;

    private static Iterable<Event> Car(Environment environment){
        return new Generator<Event>() {
            @Override
            protected void run() throws InterruptedException {
                while (true) {
                    System.out.println("Start parking at " + environment.now);
                    yield(environment.Timeout(parkingDuration, 0));
                    System.out.println("Start driving at " + environment.now);
                    yield(environment.Timeout(drivingDuration, 0));
                }
            }
        };
    }

    public static void main(String[] args){
        long startTime =  System.currentTimeMillis();

        Environment environment = new Environment();
        environment.Process(Car(environment));
        environment.Run(15);

        long endTime =  System.currentTimeMillis();
        System.out.println("total time : "+ (endTime-startTime) + "ms");
    }
}
