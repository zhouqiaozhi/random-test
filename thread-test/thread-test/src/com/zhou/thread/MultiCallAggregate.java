package com.zhou.thread;


import java.util.concurrent.*;

// Parallel 10s timeout
public class MultiCallAggregate {
    long call1time = 1;
    long call2time = 1;
    long timeout = 10;
    ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>();
    int call1() throws InterruptedException {
        Thread.sleep(call1time * 1000);
        System.out.println("response receive call1");
        map.put("call1", 1);
        return 1;
    }

    int call2() throws InterruptedException {
        Thread.sleep(call2time * 1000);
        System.out.println("response receive call2");
        map.put("call2", 1);
        return 1;
    }

    public void do3() {
        System.out.println("----------do3----------");
        try(
                var ex = Executors.newFixedThreadPool(2)
        ) {
            long start = System.currentTimeMillis();
            var res1 = ex.submit(() -> {
                int res = -1;
                try {
                    res = call1();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return res;
            });
            var res2 = ex.submit(() -> {
                int res = -1;
                try {
                    res = call2();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return res;
            });
            Integer res = null;
            try {
                res = res1.get(timeout, TimeUnit.SECONDS) + res2.get(timeout, TimeUnit.SECONDS);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName() + " " + e.getMessage());
            }
            if(res != null) {
                System.out.printf("Task result: %d%n", res);
            }
            ex.shutdownNow();
            while(!ex.isTerminated()) {
                System.out.println("Terminating...");
                Thread.sleep(1000);
            }
            System.out.printf("Terminate use: %ds%n", System.currentTimeMillis() - start);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void do2() {
        System.out.println("----------do2----------");
        try (
            var ex = Executors.newFixedThreadPool(2)
        ){
            long start = System.currentTimeMillis();
            var res1 = ex.submit(() -> {
                int res = -1;
                try {
                    res = call1();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return res;
            });
            var res2 = ex.submit(() -> {
                int res = -1;
                try {
                    res = call2();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return res;
            });
            ex.shutdown();
            var res = ex.awaitTermination(timeout, TimeUnit.SECONDS);
            if(res) {
                System.out.printf("Task result: %d%n", res1.get() + res2.get());
            } else {
                ex.shutdownNow();
                System.out.println("Task cancelled");
            }
            while(!ex.isTerminated()) {
                System.out.println("Terminating...");
                Thread.sleep(1000);
            }
            System.out.printf("Terminate use: %ds%n", System.currentTimeMillis() - start);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void do1() {
        System.out.println("----------do1----------");
        try (
                var ex = Executors.newFixedThreadPool(2)
        ){
            long start = System.currentTimeMillis();
            ex.execute(() -> {
                try {
                    call1();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
            ex.execute(() -> {
                try {
                    call2();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            });
            ex.shutdown();
            var res = ex.awaitTermination(timeout, TimeUnit.SECONDS);
            if(res) {
                System.out.printf("Task result: %d%n", map.get("call1") + map.get("call2"));
            } else {
                ex.shutdownNow();
                System.out.println("Task cancelled");
            }
            while(!ex.isTerminated()) {
                System.out.println("Terminating...");
                Thread.sleep(1000);
            }
            System.out.printf("Terminate use: %ds%n", System.currentTimeMillis() - start);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void main(String[] args) {
        var c = new MultiCallAggregate();
        c.do1();
    }
}
