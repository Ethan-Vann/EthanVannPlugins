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

public class GrandExchangeInventory {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    public static List<Widget> inventoryItems = new ArrayList<>();
    static int lastUpdateTick = 0;

    public static ItemQuery search() {
        if (lastUpdateTick < client.getTickCount()) {
            client.runScript(6009, WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER.getId(), 28, 1, -1);
            GrandExchangeInventory.inventoryItems =
                    Arrays.stream(client.getWidget(WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER).getDynamicChildren())
                            .filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
            lastUpdateTick = client.getTickCount();
        }
        return new ItemQuery(inventoryItems);
    }

    public static int getEmptySlots() {
        return 28 - search().result().size();
    }

    public static boolean full() {
        return getEmptySlots() == 0;
    }

    public static int getItemAmount(int itemId) {
        return search().withId(itemId).result().size();
    }

    public static int getItemAmount(String itemName) {
        return search().withName(itemName).result().size();
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.HOPPING || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN || gameStateChanged.getGameState() == GameState.CONNECTION_LOST) {
            GrandExchangeInventory.inventoryItems.clear();
        }
    }
}
