package com.zenbukkowa.domain;

public enum PointCategory {
    TERRA,
    MINERAL,
    ORGANIC,
    AQUATIC,
    VOID;

    public String columnName() {
        return name().toLowerCase() + "_points";
    }
}
