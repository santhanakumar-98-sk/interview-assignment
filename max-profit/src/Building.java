package org.example;

public enum Building {
    THEATRE    ("T",  5, 1500),
    PUB        ("P",  4, 1000),
    COMMERCIAL ("C", 10, 2000);

    final String code;
    final int time;
    final int earnRate;

    Building(String code, int time, int earnRate) {
        this.code = code;
        this.time = time;
        this.earnRate  = earnRate;
    }
}
