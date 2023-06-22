package com.example.RuneBotApi.LocalPlayer;

import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.RuneLite;

public class LocationInformation {
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
}
