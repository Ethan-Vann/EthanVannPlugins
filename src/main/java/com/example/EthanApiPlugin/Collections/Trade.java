package com.example.EthanApiPlugin.Collections;

import com.example.EthanApiPlugin.Collections.query.ItemQuery;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Trade {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    static List<Widget> yourItems = new ArrayList<>();
    static List<Widget> theirItems = new ArrayList<>();

    static boolean itemUpdate = true;
    static int yoursLastUpdateTick = 0;
    static int theirsLastUpdateTick = 0;

    public static ItemQuery searchYours() {
        if (yoursLastUpdateTick < client.getTickCount()) {
            yourItems.clear();

            try {
                yourItems = Arrays.stream(client.getWidget(21954585).getDynamicChildren()).filter(Objects::nonNull).filter((x) -> {
                    return x.getItemId() != 6512 && x.getItemId() != -1;
                }).collect(Collectors.toList());
                System.out.println(yourItems);
            } catch (NullPointerException var3) {
                yourItems.clear();
            }

            yoursLastUpdateTick = client.getTickCount();
        }
        return new ItemQuery(yourItems.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static ItemQuery searchTheirs() {
        if (theirsLastUpdateTick < client.getTickCount()) {
            theirItems.clear();

            try {
                theirItems = Arrays.stream(client.getWidget(21954588).getDynamicChildren()).filter(Objects::nonNull).filter((x) -> {
                    return x.getItemId() != 6512 && x.getItemId() != -1;
                }).collect(Collectors.toList());
                System.out.println(yourItems);
            } catch (NullPointerException var3) {
                theirItems.clear();
            }

            theirsLastUpdateTick = client.getTickCount();
        }
        return new ItemQuery(theirItems.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public static boolean isTradeOpen() {
        return client.getWidget(335, 25) != null && !client.getWidget(335, 25).isHidden();
    }

    public static boolean isAcceptOpen() {
        return client.getWidget(334, 0) != null && !client.getWidget(334, 0).isHidden();
    }

    public static boolean isAccepted() {
        return !Widgets.search().hiddenState(false).withTextContains("Waiting for other player...").empty();
    }

    public static void acceptTrade() {
        if (isAccepted()) {
            return;
        }

        if (isTradeOpen()) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(1, 21954570, -1, -1);
        } else if (isAcceptOpen()) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(1, 21889037, -1, -1);
        }
    }

    public static void offerX(Widget item, int amount) {
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(item, "Offer-X");
        WidgetPackets.queueResumeCount(amount);
    }

    public static void offerAll(Widget item) {
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(item, "Offer-All");
    }
}
