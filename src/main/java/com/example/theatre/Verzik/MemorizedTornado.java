//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.theatre.Verzik;

import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;

public class MemorizedTornado {
    private NPC npc;
    private WorldPoint lastPosition;
    private WorldPoint currentPosition;

    MemorizedTornado(NPC npc) {
        this.npc = npc;
        this.lastPosition = null;
        this.currentPosition = null;
    }

    public int getRelativeDelta(WorldPoint pt) {
        return this.currentPosition != null && this.lastPosition != null && this.lastPosition.distanceTo(this.currentPosition) != 0 ? pt.distanceTo(this.currentPosition) - pt.distanceTo(this.lastPosition) : -1;
    }

    NPC getNpc() {
        return this.npc;
    }

    WorldPoint getLastPosition() {
        return this.lastPosition;
    }

    void setLastPosition(WorldPoint lastPosition) {
        this.lastPosition = lastPosition;
    }

    WorldPoint getCurrentPosition() {
        return this.currentPosition;
    }

    void setCurrentPosition(WorldPoint currentPosition) {
        this.currentPosition = currentPosition;
    }
}
