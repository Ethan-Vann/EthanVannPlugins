package com.example.RuneBotApi.LocalPlayer;

import net.runelite.api.Client;
import net.runelite.client.RuneLite;

public class LocationInformation {
    static Client client = RuneLite.getInjector().getInstance(Client.class);

    public static int getMapSquareId()
    {
        return client.getLocalPlayer().getWorldLocation().getRegionID();
    }
}
