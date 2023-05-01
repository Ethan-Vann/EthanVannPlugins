//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Verzik;

import net.runelite.api.coords.WorldPoint;

import java.util.Objects;
import java.util.Set;

public class VerzikPoisonTile {
    private static final int VERZIK_P2_POISON_TICKS = 14;
    WorldPoint tile;
    int ticksRemaining;

    public VerzikPoisonTile(WorldPoint tile) {
        this.tile = tile;
        this.ticksRemaining = 14;
    }

    public void decrement() {
        if (this.ticksRemaining > 0) {
            --this.ticksRemaining;
        }

    }

    public boolean isDead() {
        return this.ticksRemaining == 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            VerzikPoisonTile that = (VerzikPoisonTile)o;
            return this.tile.equals(that.tile);
        } else {
            return false;
        }
    }

    public boolean shouldHighlight() {
        return this.ticksRemaining < 4;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.tile});
    }

    static void updateTiles(Set<VerzikPoisonTile> tileSet) {
        tileSet.forEach(VerzikPoisonTile::decrement);
        tileSet.removeIf(VerzikPoisonTile::isDead);
    }

    public WorldPoint getTile() {
        return this.tile;
    }

    public int getTicksRemaining() {
        return this.ticksRemaining;
    }
}
