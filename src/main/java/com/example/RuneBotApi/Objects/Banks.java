package com.example.RuneBotApi.Objects;

import com.example.EthanApiPlugin.Collections.TileObjects;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.InteractionApi.TileObjectInteraction;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;

import java.util.Optional;

public class Banks {
    static Client client = RuneLite.getInjector().getInstance(Client.class);

    public static boolean openNearestBank()
    {

        Optional<TileObject> bank = TileObjects.search().withAction("Bank").nearestToPlayer();

        // if bank isn't open, open it
        if (client.getWidget(WidgetInfo.BANK_CONTAINER) == null)
        {
            if (bank.isPresent())
            {
                TileObjectInteraction.interact(bank.get(), "Bank");
                return true;
            }
            else
            {
                EthanApiPlugin.sendClientMessage("couldn't find bank or banker");
                return false;
            }
        }

        return false;
    }
}
