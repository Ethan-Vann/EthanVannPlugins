package com.example.Crabs;

import net.runelite.api.coords.WorldPoint;

public enum TilePelea {
    TILE_3CRAB_1(new WorldPoint(1771,3473,0)),
    TILE_3CRAB_2(new WorldPoint(1774,3470,0));

    private WorldPoint puntito;

    TilePelea(WorldPoint worldPoint) {
        this.puntito = worldPoint;
    }
}
