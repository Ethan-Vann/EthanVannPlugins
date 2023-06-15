package com.example.InventoryUtils;

import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;

import java.util.Arrays;
import java.util.Set;

public class InventoryChecker {


    public static boolean contains(Client client, Set<Integer> setOfItems)
    {
        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
        if (inventory != null){
            Item[] invyItems = inventory.getItems();
            return Arrays.stream(invyItems)
                    .map(Item::getId)
                    .anyMatch(setOfItems::contains);
        }
        return false;
    }

//    public boolean ifInventoryContains(Client client, Set<Integer> SetOfItems) {
//        ItemContainer inventory = client.getItemContainer(InventoryID.INVENTORY);
//        boolean containsItem = false;
//        if (inventory != null) {
//            Item[] items = inventory.getItems();
//
//            for (Item item : items) {
//                if (SetOfItems.contains(item.getId())) {
//                    containsItem = true;
//                    break;
//                }
//            }
//        }
//        return containsItem;
//    }
}
