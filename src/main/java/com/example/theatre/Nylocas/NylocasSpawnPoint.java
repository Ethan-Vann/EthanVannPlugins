//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Nylocas;

import net.runelite.api.Point;

import java.util.HashMap;

enum NylocasSpawnPoint {
    WEST_NORTH(new Point(17, 25)),
    WEST_SOUTH(new Point(17, 24)),
    SOUTH_WEST(new Point(31, 9)),
    SOUTH_EAST(new Point(32, 9)),
    EAST_SOUTH(new Point(46, 24)),
    EAST_NORTH(new Point(46, 25)),
    EAST_BIG(new Point(47, 25)),
    WEST_BIG(new Point(18, 25)),
    SOUTH_BIG(new Point(32, 10));

    private Point point;
    private static final HashMap<Point, NylocasSpawnPoint> lookupMap = new HashMap();

    private NylocasSpawnPoint(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return this.point;
    }

    public static HashMap<Point, NylocasSpawnPoint> getLookupMap() {
        return lookupMap;
    }

    static {
        NylocasSpawnPoint[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            NylocasSpawnPoint v = var0[var2];
            lookupMap.put(v.getPoint(), v);
        }

    }
}
