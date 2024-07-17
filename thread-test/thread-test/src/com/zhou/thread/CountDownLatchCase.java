package com.zhou.thread;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchCase {

    public void do1() throws InterruptedException {
        var cl = new CountDownLatch(2);
        System.out.println("start " + cl.getCount());
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Job1 complete");
                cl.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Job2 complete");
                cl.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                Thread.sleep(5000);
                System.out.println("Job3 complete");
                cl.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        cl.await();
        System.out.println("do something");
    }
    public static void main(String[] args) throws InterruptedException {
        var c3 = new CountDownLatchCase();
        c3.do1();
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Job4 complete");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
