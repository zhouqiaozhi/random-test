package com.zhou.thread;

import java.util.concurrent.Semaphore;
public class SemaphoreCase {
    //debug case2 block
    void do4() throws InterruptedException {
        var s = new Semaphore(1);
        new Thread(() -> {
            try {
                s.acquire(3);
                System.out.println("Job1 complete");
                s.release(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire(2);
                System.out.println("Job2 complete");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire(1);
                System.out.println("Job3 complete");
                s.release(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(5000);
        s.release(1);
    }

    // debug case1
    void do3() throws InterruptedException {
        var s = new Semaphore(0);
        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("Job1 complete");
                s.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("Job2 complete");
                s.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("Job3 complete");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(1000);
        System.out.println(s.getQueueLength()); // debug point
        s.release(1);
    }

    // two at a time
    void do2() {
        var s = new Semaphore(2);
        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("Start Job1");
                Thread.sleep(5000);
                System.out.println("End Job1");
                s.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("Start Job2");
                Thread.sleep(1000);
                System.out.println("End Job2");
                s.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("Start Job3");
                Thread.sleep(1000);
                System.out.println("End Job3");
                s.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    // one at a time
    void do1() {
        var s = new Semaphore(1);
        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("Start Job1");
                Thread.sleep(1000);
                System.out.println("End Job1");
                s.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("Start Job2");
                Thread.sleep(1000);
                System.out.println("End Job2");
                s.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try {
                s.acquire();
                System.out.println("Start Job3");
                Thread.sleep(1000);
                System.out.println("End Job3");
                s.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void main(String[] args) throws InterruptedException {
        var c = new SemaphoreCase();
        c.do1();
    }
}
