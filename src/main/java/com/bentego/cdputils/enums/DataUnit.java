package com.bentego.cdputils.enums;

// DataUnit.java
public enum DataUnit {
    BYTE(1),
    KILOBYTE(1024),
    MEGABYTE(1024 * 1024),
    GIGABYTE(1024 * 1024 * 1024),
    TERABYTE(1024L * 1024 * 1024 * 1024),
    PETABYTE(1024L * 1024 * 1024 * 1024 * 1024);

    private final long bytes;

    DataUnit(long bytes) {
        this.bytes = bytes;
    }

    public long toBytes() {
        return bytes;
    }
}
