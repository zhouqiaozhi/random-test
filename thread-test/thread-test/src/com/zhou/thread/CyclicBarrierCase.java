package com.zhou.thread;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// Print after every 2 job
public class CyclicBarrierCase {
    AtomicInteger aint = new AtomicInteger(1);
    public void do1() throws InterruptedException {
        var cyclicBarrier = new CyclicBarrier(2, () -> {
            System.out.println(aint.getAndAdd(1) + " block job is complete");
        });
        var ex = Executors.newFixedThreadPool(2);
        ex.execute(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Job1 complete");
                cyclicBarrier.await();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        ex.execute(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Job2 complete");
                cyclicBarrier.await();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        ex.execute(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Job3 complete");
                cyclicBarrier.await();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        ex.execute(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Job4 complete");
                cyclicBarrier.await();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        ex.awaitTermination(4, TimeUnit.SECONDS);
        ex.shutdownNow();
    }

    public static void main(String[] args) throws Exception {
        var c2 = new CyclicBarrierCase();
        c2.do1();
    }
}
