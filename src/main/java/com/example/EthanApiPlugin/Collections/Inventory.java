package com.example.EthanApiPlugin.Collections;

import com.example.EthanApiPlugin.Collections.query.ItemQuery;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Inventory {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    static List<Widget> inventoryItems = new ArrayList<>();

    public static ItemQuery search() {
        return new ItemQuery(inventoryItems);
    }

    public static int getEmptySlots() {
        return 28 - search().result().size();
    }

    public static int getItemAmount(int itemId) {
        return search().withId(itemId).result().size();
    }

    public static int getItemAmount(String itemName) {
        return search().withName(itemName).result().size();
    }

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged e) {
        client.runScript(6009, 9764864, 28, 1, -1);
        if (e.getContainerId() == 93) {
            Inventory.inventoryItems =
                    Arrays.stream(client.getWidget(WidgetInfo.INVENTORY).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
        }
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged) {
        if (gameStateChanged.getGameState() == GameState.HOPPING || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN || gameStateChanged.getGameState() == GameState.CONNECTION_LOST) {
            Inventory.inventoryItems.clear();
        }
    }
}
