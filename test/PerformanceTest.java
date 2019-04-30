import com.simjava.core.*;

public class PerformanceTest {

    static Iterable<Event> Test(Environment env) {
        return new Generator<>() {
            @Override
            protected void run() throws InterruptedException {
                while (true) {
                    yield(env.Timeout(1, 0));
                }
            }
        };
    }

    public static void main(String[] args){
        long startTime =  System.currentTimeMillis();

        Environment environment = new Environment();
        environment.Process(Test(environment));
        environment.Run(10000000);

        long endTime =  System.currentTimeMillis();
        System.out.println("total time : "+ (endTime-startTime)/1000.0 + "s");
    }
}
