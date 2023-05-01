//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre;

public enum Direction {
    NORTH("N"),
    EAST("E"),
    SOUTH("S"),
    WEST("W"),
    NORTHEAST("NE"),
    NORTHWEST("NW"),
    SOUTHEAST("SE"),
    SOUTHWEST("SW");

    private final String dirName;

    public static Direction getNearestDirection(int angle) {
        int round = angle >>> 9;
        int up = angle & 256;
        if (up != 0) {
            ++round;
        }

        switch (round & 3) {
            case 0:
                return SOUTH;
            case 1:
                return WEST;
            case 2:
                return NORTH;
            case 3:
                return EAST;
            default:
                throw new IllegalStateException();
        }
    }

    public static Direction getPreciseDirection(int angle) {
        int ordinalDirection = (int)Math.round((double)angle / 256.0) % 8;
        switch (ordinalDirection) {
            case 0:
                return SOUTH;
            case 1:
                return SOUTHWEST;
            case 2:
                return WEST;
            case 3:
                return NORTHWEST;
            case 4:
                return NORTH;
            case 5:
                return NORTHEAST;
            case 6:
                return EAST;
            case 7:
                return SOUTHEAST;
            default:
                throw new IllegalStateException();
        }
    }

    private Direction(String dirName) {
        this.dirName = dirName;
    }

    public String getDirName() {
        return this.dirName;
    }
}
