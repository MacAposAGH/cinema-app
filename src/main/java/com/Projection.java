package com;

public enum Projection {
    _2D("2D"),
    _3D("3D"),
    IMAX("IMAX"),
    VIP("VIP");

    private final String value;

    Projection(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
