import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executor;

/**
 * Date: 6/26/13
 * Time: 1:35 AM
 *
 * @author Artem Prigoda
 */
public class Exchangers {
    public void work()  {
        System.out.println("\nExchangers");
        final Exchanger<String> pingExchanger = new Exchanger<String>();
        final Exchanger<String> pongExchanger = new Exchanger<String>();
        final CountDownLatch pings = new CountDownLatch(3);
        final CountDownLatch pongs = new CountDownLatch(3);
        new Thread() {
            @Override
            public void run() {
                setName("thread1");
                while (pings.getCount() != 0) {
                    try {
                        String ping = pingExchanger.exchange(null);
                        System.out.println("[" + Thread.currentThread().getName() + "] "+ ping);
                        pings.countDown();
                        if (pongs.getCount() != 0)
                            pongExchanger.exchange("Pong");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();


        new Thread() {
            @Override
            public void run() {
                setName("thread2");
                while (pongs.getCount() != 0) {
                    try {
                        String pong = pongExchanger.exchange(null);
                        System.out.println("[" + Thread.currentThread().getName() + "] "+ pong);
                        if (pings.getCount() != 0) {
                            pingExchanger.exchange("Ping");
                        }
                        pongs.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        try {
            pingExchanger.exchange("Ping");
            pings.await();
            pongs.await();
            System.out.println("Done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
