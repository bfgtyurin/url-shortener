package com.vtyurin.app.model;

public class Counter {

    private long visits = 0;

    public Counter() {
    }

    public Counter(long visits) {
        this.visits = visits;
    }

    public long getVisits() {
        return visits;
    }

    public long increment() {
        return ++visits;
    }
}
