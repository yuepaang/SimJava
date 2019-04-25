import com.simjava.core.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChannelTest {

    public static void main(String[] args){
        Channel<String> channel = Channel.ofLength(1);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("Sending");
                channel.send("a");
                System.out.println("Sent");
            }
        });

        System.out.println("Receiving");
        System.out.println(channel.receive());
        System.out.println("Received");
    }
}
