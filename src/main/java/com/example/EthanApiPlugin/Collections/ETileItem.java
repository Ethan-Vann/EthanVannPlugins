package com.example.EthanApiPlugin.Collections;

import com.example.Packets.MousePackets;
import com.example.Packets.TileItemPackets;
import net.runelite.api.TileItem;
import net.runelite.api.coords.WorldPoint;

public class ETileItem {
    public WorldPoint location;
    public TileItem tileItem;

    public ETileItem(WorldPoint worldLocation, TileItem tileItem) {
        this.location = worldLocation;
        this.tileItem = tileItem;
    }

    public WorldPoint getLocation() {
        return location;
    }

    public TileItem getTileItem() {
        return tileItem;
    }

    public void interact(boolean ctrlDown) {
        MousePackets.queueClickPacket();
        TileItemPackets.queueTileItemAction(this, ctrlDown);
    }
}
