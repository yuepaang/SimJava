import static com.simjava.core.GoRoutines.go;
import com.simjava.core.SendOnlyChannel;
import com.simjava.core.ReceiveOnlyChannel;
import com.simjava.core.Channel;

public class GoRoutineTest {

    static void F(String from){
        for (int i = 0; i < 3; i++){
            System.out.println(from + ":" + i);
        }
    }

    static class Sum implements Runnable {
        ReceiveOnlyChannel<Integer> in;
        SendOnlyChannel<Integer> out;
        int[] s;
        public Sum(ReceiveOnlyChannel<Integer> _in, SendOnlyChannel<Integer> _out, int[] _s) {
            in = _in;
            out = _out;
            s = _s;
        }

        public void run() {
            // Note: It is safe to ignore Intellij's comment about infinite loop, it's a valid Go pattern
            while (true) {
                int sum = 0;
                for (int i = 0; i < s.length; i++) {
                    sum += s[i];
                }
                out.send(sum);
            }
        }
    }

    public static void main(String[] argv) {
        F("direct");

        go(new Runnable() {

            public void run() {
                F("goroutine");
            }
        });

        go(new Runnable() {
            @Override
            public void run() {
                System.out.println("going");
            }
        });

        System.out.println("Done");

    }

}
