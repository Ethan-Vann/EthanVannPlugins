package com.example.bigdrizzleplugin;

import com.example.EthanApiPlugin.Inventory;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;
import net.runelite.client.RuneLite;

import java.util.LinkedList;

public class TickCooking {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    public static LinkedList<MenuEntryMirror> buildActions() {
        LinkedList<MenuEntryMirror> actionList = new LinkedList<>();
        int cookedKarambwanCount = BDUtils.inventoryCount(ItemID.COOKED_KARAMBWAN);
        int rawKarambwanCount = BDUtils.inventoryCount(ItemID.RAW_KARAMBWAN);
        if(BDUtils.bankOpen() && cookedKarambwanCount > 0) {
            actionList.add(MenuEntryBuilder.depositAllItemToBank(ItemID.COOKED_KARAMBWAN));
            actionList.add(MenuEntryBuilder.withdrawAll(ItemID.RAW_KARAMBWAN));
            actionList.add(clickRange());
        } else if(client.getSelectedWidget() == null && rawKarambwanCount > 0){
            actionList.add(useOnRange());
        } else if (rawKarambwanCount == 0){
            actionList.add(MenuEntryBuilder.useBankChest());
        }
        return actionList;
    }

    public static MenuEntryMirror clickRange(){
        MenuEntryMirror cookRange = MenuEntryBuilder.clickGameObject("Range");
        if (cookRange != null){
            cookRange.setBlockUntilXpDrop(Skill.COOKING);
            return cookRange;
        }
        return null;
    }

    public static MenuEntryMirror useOnRange(){
        MenuEntryMirror cookRange = MenuEntryBuilder.useItemOnGameObject("Range");
        if (cookRange != null){
            //cookRange.setBlockUntilXpDrop(Skill.COOKING);
            cookRange.getPreActions().add(new MenuEntryMirror("Use raw karambwan", 0, MenuAction.WIDGET_TARGET, 27, 9764864, 3142));
            cookRange.setPostActionTickDelay(1);
            return cookRange;
        }
        return null;
    }


}
