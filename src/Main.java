import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Date: 17.05.13
 * Time: 11:17
 *
 * @author Artem Prigoda
 */
public class Main {

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Locks().work();
            new MessageQueue().work();
            new WaitNotify().work();
        }
    }
}
