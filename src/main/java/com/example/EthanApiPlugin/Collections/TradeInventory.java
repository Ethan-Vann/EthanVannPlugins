package com.example.EthanApiPlugin.Collections;

import com.example.EthanApiPlugin.Collections.query.ItemQuery;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TradeInventory {
    private static final int TRADE_INVENTORY_PACKED_ID = 22020096;

    static int lastUpdateTick = 0;

    static Client client = RuneLite.getInjector().getInstance(Client.class);
    static List<Widget> tradeInventoryItems = new ArrayList<>();

    public static ItemQuery search() {
        if (lastUpdateTick < client.getTickCount()) {
            client.runScript(6009, 9764864, 28, 1, -1);
            tradeInventoryItems =
                    Arrays.stream(client.getWidget(TRADE_INVENTORY_PACKED_ID).getDynamicChildren()).filter(Objects::nonNull).filter(x -> x.getItemId() != 6512 && x.getItemId() != -1).collect(Collectors.toList());
            lastUpdateTick = client.getTickCount();
        }
        return new ItemQuery(tradeInventoryItems);
    }

    @Subscribe
    public void onGameStateChanged(GameStateChanged gameStateChanged)
    {
        if (gameStateChanged.getGameState() == GameState.HOPPING || gameStateChanged.getGameState() == GameState.LOGIN_SCREEN || gameStateChanged.getGameState() == GameState.CONNECTION_LOST)
        {
            tradeInventoryItems.clear();
        }
    }
}
