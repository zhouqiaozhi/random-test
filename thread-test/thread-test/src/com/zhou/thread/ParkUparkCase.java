package com.zhou.thread;

import java.util.concurrent.locks.LockSupport;

public class ParkUparkCase {
    void do2() throws InterruptedException {
        var t = new Thread(() -> {
            System.out.println("stop");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            LockSupport.park();
            System.out.println("test");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        LockSupport.unpark(t);
    }
    void do1() throws InterruptedException {
        var t = new Thread(() -> {
            System.out.println("stop");
            LockSupport.park();
            System.out.println("test");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t.start();
        Thread.sleep(5000);
        System.out.println("go");
        LockSupport.unpark(t);
    }

    public static void main(String[] args) throws InterruptedException {
        var c = new ParkUparkCase();
        c.do2();
    }
}
