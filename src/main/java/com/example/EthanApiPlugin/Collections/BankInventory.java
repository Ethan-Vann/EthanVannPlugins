package com.example.EthanApiPlugin.Collections;

import com.example.EthanApiPlugin.Collections.query.ItemQuery;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BankInventory {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    static List<Widget> bankInventoryItems = new ArrayList<>();
    static int lastUpdateTick = 0;

    public static ItemQuery search() {
        if (lastUpdateTick < client.getTickCount()) {
            BankInventory.bankInventoryItems =
                    Arrays.stream(client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
            lastUpdateTick = client.getTickCount();
        }
        return new ItemQuery(bankInventoryItems.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }
//    @Subscribe
//    public void onWidgetLoaded(WidgetLoaded e) {
//        if (e.getGroupId() == WidgetID.BANK_INVENTORY_GROUP_ID) {
//            try {
//                BankInventory.bankInventoryItems =
//                        Arrays.stream(client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
//            } catch (NullPointerException err) {
//                BankInventory.bankInventoryItems.clear();
//            }
//        }
//    }

//    @Subscribe
//    public void onItemContainerChanged(ItemContainerChanged e) {
//        if (e.getContainerId() == 93) {
//            if (client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER) == null) {
//                BankInventory.bankInventoryItems.clear();
//                return;
//            }
//            try {
//                BankInventory.bankInventoryItems =
//                        Arrays.stream(client.getWidget(WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
//                System.out.println("bankInventoryItems: "+bankInventoryItems.size());
//                return;
//            } catch (NullPointerException err) {
//                BankInventory.bankInventoryItems.clear();
//                return;
//            }
//        }
//    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.HOPPING || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN || gameStateChanged.getGameState() == GameState.CONNECTION_LOST) {
            BankInventory.bankInventoryItems.clear();
        }
    }
}
