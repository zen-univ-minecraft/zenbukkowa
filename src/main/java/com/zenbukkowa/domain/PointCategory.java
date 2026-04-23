package com.zenbukkowa.domain;

public enum PointCategory {
    TERRA,
    MINERAL,
    ORGANIC,
    AQUATIC,
    VOID,
    CROP;

    public String columnName() {
        return name().toLowerCase() + "_points";
    }
}
