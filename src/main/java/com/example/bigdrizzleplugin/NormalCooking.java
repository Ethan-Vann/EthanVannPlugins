package com.example.bigdrizzleplugin;

import com.example.EthanApiPlugin.Inventory;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.client.RuneLite;

import java.util.LinkedList;

public class NormalCooking {
    private static int rangeID = 31631;
    private static int bankID = 30087;
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    public static LinkedList<MenuEntryMirror> buildActions(){
        LinkedList<MenuEntryMirror> actionList = new LinkedList<>();
        int cookedKarambwanCount = BDUtils.inventoryCount(ItemID.COOKED_KARAMBWAN);
        int rawKarambwanCount = BDUtils.inventoryCount(ItemID.RAW_KARAMBWAN);
        if (client.getWidget(17694735) != null){
            actionList.add(startCooking());
        } else if(BDUtils.bankOpen() && cookedKarambwanCount > 0){
            actionList.add(MenuEntryBuilder.depositAllItemToBank(ItemID.COOKED_KARAMBWAN));
            actionList.add(MenuEntryBuilder.withdrawAll(ItemID.RAW_KARAMBWAN));
            actionList.add(clickRange());
        } else if (cookedKarambwanCount > 3){
            actionList.add(MenuEntryBuilder.useBankChest());
        }else if (rawKarambwanCount > 0){
            actionList.add(clickRange());
        }
        return actionList;
    }

    public static MenuEntryMirror startCooking(){
        return  new MenuEntryMirror("Cooked Karambwan", 1, MenuAction.CC_OP, -1, 17694735, -1, 2);
    }

    public static MenuEntryMirror clickRange(){
        MenuEntryMirror cookRange = MenuEntryBuilder.clickGameObject("Range");
        if (cookRange != null){
            cookRange.setBlockUntil(() -> client.getWidget(17694735) != null);
            return cookRange;
        }
        return null;
    }
}
