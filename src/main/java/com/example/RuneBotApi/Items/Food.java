package com.example.RuneBotApi.Items;

import com.example.InteractionApi.InventoryInteraction;
import com.example.RuneBotApi.RBApi;
import com.example.RuneBotApi.RBConstants;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Food {
    private static Client client = RBApi.getClient();
    private static ItemContainer container = client.getItemContainer(InventoryID.INVENTORY);

    /**
     * this is a bit awkwardly named. It will consume the highest healing food if possible,
     * but it prioritizes brews last since they will lower your stats which sucks if you want to afk
     * use method drinkBrew() in {@link Potions}
     * @return -1 if fail, 1 if a brew was consumed, or 0 if a food item was consumed
     */
    public static int eatBestFood()
    {
        Set<Integer> inventoryIds = new HashSet<>();
        for (int i = 0; i < 29; ++i)
            inventoryIds.add(Objects.requireNonNull(container.getItem(i)).getId());

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
