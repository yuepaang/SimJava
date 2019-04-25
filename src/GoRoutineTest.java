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
            SendOnlyChannel<Integer> ch;
            public Runnable with(SendOnlyChannel<Integer> ch) {this.ch = ch;return this;}

            public void run() {
                for (int i = 2; ; i++) {
                    this.ch.send(i);
                }
            }
        }.with(ch.getSendOnly()));

        for (int i=0;i<10;i++) {
            int prime = ch.receive();
            fmt.Println(prime);
            Channel<Integer> ch1 = Channel.make();
            go(new Filter(ch.getReceiveOnly(),ch1.getSendOnly(),prime));
            ch = ch1;
        }
    }

}
