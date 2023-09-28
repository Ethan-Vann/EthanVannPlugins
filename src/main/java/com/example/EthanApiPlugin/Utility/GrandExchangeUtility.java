package com.example.EthanApiPlugin.Utility;

import com.example.EthanApiPlugin.Collections.GrandExchangeInventory;
import com.example.EthanApiPlugin.Collections.NPCs;
import com.example.EthanApiPlugin.Collections.Widgets;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.InteractionApi.NPCInteraction;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GrandExchangeUtility {

    private static final int F2P_SLOTS = 3;
    private static final int P2P_SLOTS = 8;

    private static final int PRICE_VARBIT = 4398;
    private static final int QUANTITY_VARBIT = 4396;
    private static final int OFFER_VARBIT = 4439;

    private static final List<Integer> OFFER_SLOTS = List.of(30474247, 30474248, 30474249, 30474250, 304744251, 30474252, 304744253, 304742454);

    public static boolean isOpen() {
        return Widgets.search().withId(WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER.getId()).first().isPresent();
    }

    public static void open() {
        NPCs.search().withAction("Exchange").nearestToPlayer().ifPresent(x -> NPCInteraction.interact(x, "Exchange"));
    }

    public static boolean isSelling() {
        return EthanApiPlugin.getClient().getVarbitValue(OFFER_VARBIT) != 0 && EthanApiPlugin.getClient().getVarbitValue(Varbits.GE_OFFER_CREATION_TYPE) != 0;
    }

    public static boolean isBuying() {
        return EthanApiPlugin.getClient().getVarbitValue(OFFER_VARBIT) != 0 && EthanApiPlugin.getClient().getVarbitValue(Varbits.GE_OFFER_CREATION_TYPE) == 0;
    }

    public static int getItemId() {
        return EthanApiPlugin.getClient().getVarpValue(VarPlayer.CURRENT_GE_ITEM);
    }

    public static void setItem(int id) {
        EthanApiPlugin.getClient().runScript(754, id, 84);
    }

    public static int getPrice() {
        return EthanApiPlugin.getClient().getVarbitValue(PRICE_VARBIT);
    }

    public static int getQuantity() {
        return EthanApiPlugin.getClient().getVarbitValue(QUANTITY_VARBIT);
    }

    public static boolean isFull() {
        boolean isMember = EthanApiPlugin.getClient().getWorldType().contains(WorldType.MEMBERS);
        return getOffers().size() > (isMember ? (P2P_SLOTS - 1) : (F2P_SLOTS - 1));
    }

    public static List<GrandExchangeOffer> getOffers() {
        return Arrays.stream(EthanApiPlugin.getClient().getGrandExchangeOffers()).filter(x -> x.getItemId() > 0).collect(Collectors.toList());
    }

    public static void collect() {
        Widgets.search().hiddenState(false).withText("Collect").first().ifPresent(x -> widgetAction(1, x.getId(), -1, 0));
    }

    public static void buyItem(int id, int amount, int price) {
        if (!isOpen() || isFull()) {
            return;
        }

        if (!isBuying()) {
            widgetAction(1, getFreeSlot(), -1, 3);
            return;
        }

        if (getItemId() != id) {
            setItem(id);
            return;
        }

        if (getQuantity() != amount) {
            Widgets.search().hiddenState(false).withId(WidgetInfo.CHATBOX_FULL_INPUT.getId()).first().ifPresentOrElse(x -> enterAmount(amount), () -> widgetAction(1, 30474265, -1, 7));
            return;
        }

        if (getPrice() != price) {
            Widgets.search().hiddenState(false).withId(WidgetInfo.CHATBOX_FULL_INPUT.getId()).first().ifPresentOrElse(x -> enterAmount(price), () -> widgetAction(1, 30474265, -1, 12));
            return;
        }

        Widgets.search().withAction("Confirm").first().ifPresent(x -> widgetAction(1, x.getId(), -1, -1));
    }

    public static void sellItem(int id, int price) {
        if (!isOpen() || isFull()) {
            return;
        }

        Optional<Widget> itemToSell = GrandExchangeInventory.search().withId(GrandExchangeInventory.search().withId(id).first().isEmpty() ? getNotedId(id) : id).first();

        if (!isSelling()) {
            widgetAction(1, getFreeSlot(), -1, 4);
            return;
        }

        if (getItemId() != id) {
            itemToSell.ifPresent(x -> widgetAction(1, WidgetInfo.GRAND_EXCHANGE_INVENTORY_ITEMS_CONTAINER.getId(), x.getItemId(), x.getIndex()));
            return;
        }

        if (getPrice() != price) {
            Widgets.search().hiddenState(false).withId(WidgetInfo.CHATBOX_FULL_INPUT.getId()).first().ifPresentOrElse(x -> enterAmount(price), () -> widgetAction(1, 30474265, -1, 12));
            return;
        }

        widgetAction(1, 30474265, -1, 6);

        Widgets.search().withAction("Confirm").first().ifPresent(x -> widgetAction(1, x.getId(), -1, -1));
    }

    private static void enterAmount(int amount) {
        EthanApiPlugin.getClient().setVarcStrValue(VarClientStr.INPUT_TEXT, String.valueOf(amount));
        EthanApiPlugin.getClient().runScript(681);
    }

    private static int getFreeSlot() {
        return Widgets.search().hiddenState(false).filter(x -> OFFER_SLOTS.contains(x.getId()) && x.getChild(3) != null && !x.getChild(3).isHidden()).first().map(Widget::getId).orElse(-1);
    }

    private static void widgetAction(int actionFieldNo, int widgetId, int itemId, int childId) {
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(actionFieldNo, widgetId, itemId, childId);
    }

    private static int getNotedId(int id) {
        return EthanApiPlugin.getClient().getItemDefinition(id).getLinkedNoteId();
    }
}
