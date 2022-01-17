package demo111;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Avoid deadlock:
 * try lock in the order way everywhere you lock
 * Use trylock and time outs to kget both locks by a trhead and then continue
 */
public class Runner {

    private Account account1 = new Account();
    private Account account2 = new Account();

    private Lock lock1 = new ReentrantLock();
    private Lock lock2 = new ReentrantLock();

    private void acquireLocks(Lock first, Lock second) throws InterruptedException {

        while (true) {
            // Acquire locks
            boolean gotFirstLock = false;
            boolean gotSecondLock = false;

            try {
                try {
                    // We must block both objects by a thread!
                    gotFirstLock = first.tryLock();
                    gotSecondLock = second.tryLock();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } finally {
                if (gotFirstLock && gotSecondLock) {
                    return;
                }
                if (gotFirstLock) {
                    first.unlock();
                }

                if (gotSecondLock) {
                    second.unlock();
                }
            }

            //locks not acquired
            Thread.sleep(1);
        }
    }

    public void firstThread() throws InterruptedException {

        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            acquireLocks(lock1, lock2);

            try {
                Account.transfer(account1, account2, random.nextInt(100));
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void secondThread() throws InterruptedException {
        Random random = new Random();

        for (int i = 0; i < 10000; i++) {
            acquireLocks(lock2, lock1);

            try {
                Account.transfer(account2, account1, random.nextInt(100));
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void finished() {
        System.out.println("Accoiunt 1 gt balance : " + account1.getBalance());
        System.out.println("Account 2 gt balance : " + account2.getBalance());
        System.out.println("Account 1 gt balance : " + (account1.getBalance() + account2.getBalance()));


    }
}
