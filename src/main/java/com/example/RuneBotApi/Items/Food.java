package com.example.RuneBotApi.Items;

import com.example.InteractionApi.InventoryInteraction;
import com.example.RuneBotApi.RBApi;
import com.example.RuneBotApi.RBConstants;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;

import java.util.HashSet;
import java.util.Set;

public class Food {
    private static final Client client = RBApi.getClient();
    private static final ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);

    /**
     * this is a bit awkwardly named. It will consume the highest healing food if possible,
     * but it prioritizes brews last since they will lower your stats which sucks if you want to afk
     * use method drinkBrew() in {@link Potions}
     * @return -1 if fail, 1 if a brew was consumed, or 0 if a food item was consumed
     */
    public static int eatBestFood()
    {
        Set<Integer> inventoryIds = new HashSet<>();
        for (int i = 0; i < 29; ++i) {
            Item item = container.getItem(i);
            if (item != null) inventoryIds.add(item.getId());
        }

        for (int food : RBConstants.foodIds)
        {
            if (inventoryIds.contains(food))
            {
                for (int brewIds : RBConstants.brewIds) // if drinking a brew, return 1 so we know when to restore later
                {
                    if (food == brewIds)
                    {
                        InventoryInteraction.useItem(food, "Drink");
                        return 1;
                    }
                }
                InventoryInteraction.useItem(food, "Eat");
                return 0;
            }
        }

        return -1;
    }
}
