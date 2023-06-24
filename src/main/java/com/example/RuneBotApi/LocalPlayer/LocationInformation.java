package com.example.RuneBotApi.LocalPlayer;

import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.RuneLite;

import java.util.Set;

public final class LocationInformation {
    static Client client = RuneLite.getInjector().getInstance(Client.class);

    public static int getMapSquareId()
    {
        return client.getLocalPlayer().getWorldLocation().getRegionID();
    }

    public static WorldPoint getTile()
    {
        return client.getLocalPlayer().getWorldLocation();
    }

    public static boolean isOnTile(WorldPoint destination)
    {
        return destination.equals(getTile());
    }

    /**
     * returns true if player's current loc is contained within the provided set of WorldPoints
     */
    public static boolean isInTileGroup(Set<WorldPoint> tileGroup)
    {
        return tileGroup.contains(getTile());
    }

    /**
     * @param x1 - the smaller x val
     * @param x2 - the greater x val
     * @param y1 - the smaller y val
     * @param y2 - the greater y val
     * returns true if the player's current loc is within the provided tile range (inclusive)
     */
    public static boolean isInTileRange(int x1, int x2, int y1, int y2)
    {
        WorldPoint loc = getTile();
        if (!(loc.getX() >= x1 && loc.getX() <= x2)) return false;
        return loc.getY() >= y1 && loc.getY() <= y2;
    }
}
