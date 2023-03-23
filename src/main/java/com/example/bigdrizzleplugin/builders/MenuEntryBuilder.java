package com.example.bigdrizzleplugin.builders;

import com.example.EthanApiPlugin.*;
import com.example.bigdrizzleplugin.BDUtils;
import com.example.bigdrizzleplugin.MenuEntryMirror;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;

import java.util.Optional;

public class MenuEntryBuilder {
    static Client client = RuneLite.getInjector().getInstance(Client.class);

    public MenuEntryMirror depositAllItemToDepositBox(int itemID){
        //This should really be depositBoxInventory
        Widget inventoryItemWidget = null;
        Optional<Widget> inventoryItem = Inventory.search().withId(itemID).first();
        if (inventoryItem.isPresent()){
            inventoryItemWidget = inventoryItem.get();
        }else{
            return null;
        }
        int identifier = 0;
        for (int i = inventoryItemWidget.getActions().length - 1; i >= 0; i--) {
            if (inventoryItemWidget.getActions()[i] != null && inventoryItemWidget.getActions()[i].equalsIgnoreCase("deposit-all")){
                identifier = i+1;
                break;
            }
        }
        return new MenuEntryMirror("Deposit All Item: " + inventoryItemWidget.getName(), identifier, MenuAction.CC_OP, inventoryItemWidget.getIndex(), 12582914, itemID);
    }
    public MenuEntryMirror clickItemInDepositBox(int itemID, int hcIdentifier){
        //This should really be depositBoxInventory
        Widget inventoryItemWidget = null;
        Optional<Widget> inventoryItem = Inventory.search().withId(itemID).first();
        if (inventoryItem.isPresent()){
            inventoryItemWidget = inventoryItem.get();
        }else{
            return null;
        }
        int identifier = 0;
        for (int i = inventoryItemWidget.getActions().length - 1; i >= 0; i--) {
            if (inventoryItemWidget.getActions()[i] != null && inventoryItemWidget.getActions()[i].equalsIgnoreCase("deposit-all")){
                identifier = i+1;
                break;
            }
        }
        //needs to not hardcode identifier once i can gather it with API
        return new MenuEntryMirror("Click item: " + inventoryItemWidget.getName(), hcIdentifier, MenuAction.CC_OP, inventoryItemWidget.getIndex(), 12582914, itemID);
    }
}
