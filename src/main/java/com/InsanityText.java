package com;

public record InsanityText (String text) implements Comparable<InsanityText> {
    @Override
    public int compareTo(InsanityText other) {
        return text.compareTo(other.text);
    }
}