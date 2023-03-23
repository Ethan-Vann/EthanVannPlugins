package com.example.bigdrizzleplugin;

import com.example.EthanApiPlugin.Inventory;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.coords.WorldArea;
import net.runelite.client.RuneLite;

import java.util.LinkedList;

public class KarambwanFishing {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    private static WorldArea karambwanFishingArea = new WorldArea(2893, 3110, 14, 13, 0);
//    public static LinkedList<MenuEntryMirror> buildActions(){
//        LinkedList<MenuEntryMirror> actionList = new LinkedList<>();
//        if (BDUtils.depositBoxOpen() &&  Inventory.search().result().size()==28){
//            actionList.add(MenuEntryBuilder.depositAllItemToDepositBox(ItemID.RAW_KARAMBWAN));
//            actionList.add(MenuEntryBuilder.clickItemInDepositBox(ItemID.OPEN_FISH_BARREL,9));
//            actionList.add(MenuEntryBuilder.maxCapePohTele());
//        } else if (BDUtils.inArea(karambwanFishingArea) && !BDUtils.inventoryFull()){
//            actionList.add(MenuEntryBuilder.clickNPC("Fishing spot"));
//        } else if (BDUtils.inArea(karambwanFishingArea) && BDUtils.inventoryFull()) {
//            actionList.add(MenuEntryBuilder.maxCapeCraftingGuildTele());
//        } else if (BDUtils.inCraftingGuild()){
//            actionList.add(MenuEntryBuilder.clickGameObject("Bank Deposit Box"));
//        } else if (BDUtils.inPOH()){
//            actionList.add(MenuEntryBuilder.lastDestinationRing());
//        }
//        return actionList;
//    }
}
