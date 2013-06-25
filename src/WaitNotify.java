import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Date: 17.05.13
 * Time: 11:43
 *
 * @author Artem Prigoda
 */
public class WaitNotify {

    public void work() {
        System.out.println("\nWaitNotify");
        final Object monitor = new Object();
        final AtomicBoolean pingReady = new AtomicBoolean(true);
        final AtomicBoolean pongReady = new AtomicBoolean(false);
        Thread pingThread = new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (i < 3) {
                    synchronized (monitor) {
                        while (!pingReady.get()) {
                            try {
                                //System.out.println("Waiting ping");
                                monitor.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println("Ping");
                        pongReady.set(true);
                        pingReady.set(false);
                        monitor.notifyAll();
                    }
                    i++;
                }
            }
        };

        Thread pongThread = new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (i < 3) {
                    synchronized (monitor) {
                        while (!pongReady.get()) {
                            try {
                                monitor.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println("Pong");
                        pingReady.set(true);
                        pongReady.set(false);
                        monitor.notifyAll();
                    }
                    i++;
                }
            }
        };

        try {
            pingThread.start();
            pongThread.start();
            pingThread.join();
            pongThread.join();
            System.out.println("Done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
