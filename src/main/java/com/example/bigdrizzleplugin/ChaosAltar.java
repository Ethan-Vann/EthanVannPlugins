package com.example.bigdrizzleplugin;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.client.RuneLite;

import java.util.LinkedList;

public class ChaosAltar {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    public static LinkedList<MenuEntryMirror> buildActions() {
        LinkedList<MenuEntryMirror> actionList = new LinkedList<>();
        if (BDUtils.getGameObject("Chaos altar") != null && BDUtils.inventoryCount("bones") > 0){
            actionList.add(useBoneOnAltar());
        }
        return actionList;
    }

    public static MenuEntryMirror useBoneOnAltar(){
        MenuEntryMirror useAltar = MenuEntryBuilder.useItemOnGameObject("Chaos altar");
        if (useAltar != null){
            useAltar.getPreActions().add(MenuEntryBuilder.doActionOnInventoryItem("bones", "use"));
            useAltar.setPostActionTickDelay(1);
            return useAltar;
        }
        return null;
    }

}
