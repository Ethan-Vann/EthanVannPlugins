package com.example.bigdrizzleplugin.builders;

import com.example.EthanApiPlugin.Bank;
import com.example.EthanApiPlugin.BankInventory;
import com.example.bigdrizzleplugin.MenuEntryMirror;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;

import java.util.Optional;

public class BankInventoryBuilder {
    public static MenuEntryMirror bankInventoryItemAction(String bankInventoryItemName, String action) {
        Optional<Widget> bankInventoryItem = BankInventory.search().nameContains(bankInventoryItemName).first();
        if (bankInventoryItem.isPresent()){
            return new WidgetBuilder().widgetAction(bankInventoryItem.get(), action);
        }else{
            return null;
        }
    }
}
