package com.example.bigdrizzleplugin.builders;

import com.example.EthanApiPlugin.Bank;
import com.example.bigdrizzleplugin.MenuEntryMirror;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.Widget;

import java.util.Optional;

public class BankBuilder {
    public final static int BANK_WIDGET_ID = 786474;
    public static MenuEntryMirror bankItemAction(String bankItemName, String action) {
        Optional<Widget> bankItem = Bank.search().nameContains(bankItemName).first();
        if (bankItem.isPresent()){
            return new WidgetBuilder().widgetAction(bankItem.get(), action);
        }else{
            return null;
        }
    }

    public static MenuEntryMirror depositAllInventoryToBank(){
        return new MenuEntryMirror("Deposit all inventory", 1, MenuAction.CC_OP, -1, BANK_WIDGET_ID, -1);
    }
}
