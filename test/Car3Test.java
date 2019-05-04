import com.simjava.core.*;
import com.simjava.yield.Yielderable;

public class Car3Test {
    public static Yielderable<Event> Car (Environment env, String name, Resource bcs, int drivingTime, int chargeDuration) {
        return yield -> {
            yield.returning(env.Timeout(drivingTime, 0));

            System.out.printf("%s arriving at %s\n" ,name, env.now);

            var req = bcs.Request();
            yield.returning(req);
            System.out.printf("%s starting to charge at %d\n" ,name, env.now);
            yield.returning(env.Timeout(chargeDuration, 0));
            System.out.printf("%s leaving the bcs at %s\n" ,name, env.now);
            req.Dispose();

        };
    }

    public static void main(String[] args) {
        var env = new Environment();
        var bcs = new Resource(env, 2);

        for (int i = 0; i < 4; i++) {
            env.Process(Car(env, "Car "+i, bcs, i*2, 5));
        }
        env.Run(null);
    }
}
