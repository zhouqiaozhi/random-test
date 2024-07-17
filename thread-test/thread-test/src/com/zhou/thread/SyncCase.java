package com.zhou.thread;

public class SyncCase {
    private static final Object obj1 = new Object();
    private final Object obj2 = new Object();
    void do4() {
        synchronized (SyncCase.class) {

        }
    }
    void do3() {
        synchronized (obj2) {

        }
    }
    void do2() {
        synchronized (obj1) {

        }
    }
    void do1() {
        synchronized (this) {
            // notify
            // notifyAll
            // wait
        }
    }
}
