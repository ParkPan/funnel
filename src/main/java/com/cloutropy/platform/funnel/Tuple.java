package com.cloutropy.platform.funnel;


public class Tuple<A, B> {
    public final A first;
    public final B second;

    public Tuple(A a, B b) {
        this.first = a;
        this.second = b;
    }

    @Override
    public String toString() {
        return String.format("%s=%s",this.first,this.second);
    }
}
