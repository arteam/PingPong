import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Date: 17.05.13
 * Time: 11:54
 *
 * @author Artem Prigoda
 */
public class Locks {

    public void work() {
        System.out.println("\nLocks");
        final Lock monitor = new ReentrantLock();
        final Condition condition = monitor.newCondition();
        final AtomicBoolean pingReady = new AtomicBoolean(true);
        final AtomicBoolean pongReady = new AtomicBoolean(false);
        Thread pingThread = new Thread() {
            @Override
            public void run() {
                int i = 0;
                while (i < 3) {
                    monitor.lock();
                    try {
                        while (!pingReady.get()) {
                            try {
                                condition.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                        System.out.println("Ping");
                        pongReady.set(true);
                        pingReady.set(false);
                        condition.signalAll();
                    } finally {
                        monitor.unlock();
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
                    monitor.lock();
                    try {
                        while (!pongReady.get()) {
                            try {
                                condition.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return;
                            }
                        }

                        System.out.println("Pong");
                        pingReady.set(true);
                        pongReady.set(false);
                        condition.signalAll();
                    } finally {
                        monitor.unlock();
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
