package com.example.bigdrizzleplugin;

import com.example.EthanApiPlugin.Inventory;
import com.example.bigdrizzleplugin.builders.*;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.RuneLite;

public class NormalCooking {
    private static final int cookInterface = 17694735;
    private static Client client = RuneLite.getInjector().getInstance(Client.class);
    private enum Inventory{
        COOKED,
        RAW,
        EMPTY
    }
    private static Inventory inventory = Inventory.COOKED;
    public static boolean processClick() {
        boolean ranAction = true;
        if (client.getWidget(17694735) != null){
            BDUtils.invokeMenuAction(WidgetBuilder.widgetAction(client.getWidget(17694735), "Cook"));
        }else if (BDUtils.inventoryCount("Cooked karambwan") > 3 && !BDUtils.bankOpen()){
            BDUtils.invokeMenuAction(ObjectBuilder.objectAction("Bank chest", "Use"));
            inventory = Inventory.COOKED;
        }else if(BDUtils.bankOpen()){
            if (inventory == Inventory.COOKED){
                BDUtils.invokeMenuAction(BankInventoryBuilder.bankInventoryItemAction("Cooked karambwan", "Deposit-All"));
                inventory = Inventory.EMPTY;
            } else if (inventory == Inventory.EMPTY){
                BDUtils.invokeMenuAction(BankBuilder.bankItemAction("Raw karambwan", "Withdraw-All"));
                inventory = Inventory.RAW;
            } else {
                BDUtils.invokeMenuAction(ObjectBuilder.objectAction("Range", "Cook"));
            }
        }else if (inventory == Inventory.RAW) {
            BDUtils.invokeMenuAction(ObjectBuilder.objectAction("Range", "Cook"));
        }else{
            ranAction = false;
        }
        return ranAction;
    }
}