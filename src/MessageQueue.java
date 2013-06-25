import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Date: 17.05.13
 * Time: 11:48
 *
 * @author Artem Prigoda
 */
public class MessageQueue {

    public void work() {
        try {
            System.out.println("\nMessageQueue");
            final BlockingQueue pingQueue = new LinkedBlockingQueue();
            pingQueue.put(new Object());
            final BlockingQueue pongQueue = new LinkedBlockingQueue();
            final CountDownLatch latch = new CountDownLatch(1);
            Thread pingThread = new Thread() {
                @Override
                public void run() {
                    int i = 0;
                    while (i < 3) {
                        try {
                            pingQueue.take();
                            System.out.println("Ping");
                            pongQueue.put(new Object());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
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
                        try {
                            pongQueue.take();
                            System.out.println("Pong");
                            pingQueue.put(new Object());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                    latch.countDown();
                }
            };


            pingThread.start();
            pongThread.start();
            latch.await();
            System.out.println("Done");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
