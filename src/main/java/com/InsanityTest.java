package com;

public record InsanityTest (int order, InsanityText text) implements Comparable<InsanityTest>{

    @Override
    public int compareTo(InsanityTest other) {
        return Integer.compare(order, other.order);
    }
}
