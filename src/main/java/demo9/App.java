package demo9;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class App {
    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue(10);

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                producer();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    consumer();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();


    }

    private static void producer() {
        Random random = new Random();
        while (true) {
            try {
                // put will wait if the queue is full
                queue.put(random.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void consumer() throws InterruptedException {
        Random random = new Random();
        while (true) {

            Thread.sleep(100);
            if (random.nextInt(10) == 0) {
                Integer take = queue.take(); // if the queue is empty it will wait
                System.out.println("Taken: " + take + " - queue size: " + queue.size());
            }
        }
    }
}
