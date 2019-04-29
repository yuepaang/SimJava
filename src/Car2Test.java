import com.simjava.core.*;
import com.simjava.core.Process;


public class Car2Test {

    public static class Car {
        Environment env;
        Process action;

        public Car(Environment env) {
            this.env = env;
            this.action = env.Process(Run());
        }

        public Iterable<Event> Run(){
            return new Generator<>() {
                @Override
                protected void run() throws InterruptedException {
                    while (true) {
                        System.out.println("Start parking and charging at " + env.now);
                        int chargeDuration = 5;
                        yield(env.Process(Charge(chargeDuration)));

                        System.out.println("Start driving at " + env.now);
                        int tripDuration = 2;
                        yield(env.Timeout(tripDuration, 0));
                    }
                }
            };
        }

        private Iterable<Event> Charge(int duration){
            return new Generator<>() {
                @Override
                protected void run() throws InterruptedException {
                    yield(env.Timeout(duration, 0));
                }
            };
        }
    }

    public static void main(String[] args){
        long startTime =  System.currentTimeMillis();

        Environment environment = new Environment();
        Car c = new Car(environment);
        environment.Run(15);

        long endTime =  System.currentTimeMillis();
        System.out.println("total time : "+ (endTime-startTime) + "ms");
    }
}
