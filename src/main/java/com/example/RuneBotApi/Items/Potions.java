package com.example.RuneBotApi.Items;

import com.example.EthanApiPlugin.Collections.Inventory;
import com.example.InteractionApi.InventoryInteraction;
import net.runelite.api.ItemID;

public class Potions {

    public static final int[] brewIds = {ItemID.SARADOMIN_BREW1, ItemID.SARADOMIN_BREW2, ItemID.SARADOMIN_BREW3, ItemID.SARADOMIN_BREW4};

    /**
     * drinks brews with the lowest dose having the highest priority
     * returns true if successful
     */
    public static boolean drinkBrew()
    {
        for (int i : brewIds)
        {
            if (!Inventory.search().withId(i).result().isEmpty())
            {
                InventoryInteraction.useItem(i, "Drink");
                return true;
            }
        }
        return false;
    }
}
