package com.zhou.thread;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RentrantLockCase {
    void do2() throws InterruptedException {
        var lock = new ReentrantLock();
        var condition = lock.newCondition();
        var items = new boolean[5];
        new Thread(() -> {
            lock.lock();
            System.out.println("start job");
            while(!items[0] || !items[3] || !items[4]) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("job complete");
            lock.unlock();
        }).start();
        Thread.sleep(2000);
        for(int i = 0; i < 5; i++) {
            final int ii = i;
            new Thread(() -> {
                try {
                    lock.lock();
                    Thread.sleep(1000);
                    items[ii] = true;
                    System.out.println("item " + ii + " ready");
                    condition.signal();
                    lock.unlock();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }).start();
        }
    }
    void do1() {
        var lock = new ReentrantLock();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                lock.lock();
                System.out.println("a");
                lock.unlock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                lock.lock();
                Thread.sleep(2000);
                System.out.println("b");
                lock.unlock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    public static void main(String[] args) throws InterruptedException {
        var c = new RentrantLockCase();
        c.do2();
    }
}
