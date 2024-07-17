package com.zhou.thread;

public class StartVsRun {
    void start() {
        System.out.println("------start------");
        System.out.println(Thread.currentThread().getName());
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
        }).start();
    }

    void run() {
        System.out.println("------run------");
        System.out.println(Thread.currentThread().getName());
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
        }).run();
    }

    public static void main(String[] args) {
        var c = new StartVsRun();
        c.start();
    }
}
