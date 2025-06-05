package com.example.EthanApiPlugin.Collections;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import net.runelite.api.Client;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.GrandExchangeOfferState;
import net.runelite.api.ItemComposition;
import net.runelite.api.VarPlayer;
import net.runelite.api.WorldType;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.ComponentID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;
import net.runelite.client.util.Text;
import net.runelite.client.util.WildcardMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GrandExchange {

    private static final int F2P_SLOTS = 3;
    private static final int P2P_SLOTS = 8;
    private static final int PRICE_VARIBT = 4398;
    private static final int QUANTITY_VARBIT = 4396;

    private static final Client client = RuneLite.getInjector().getInstance(Client.class);


    /**
     * Checks if the Grand Exchange offer slots are full.
     * @return true if the slots are full, false otherwise.
     */
    public static boolean isFull() {
        boolean isMember = client.getWorldType().contains(WorldType.MEMBERS);
        return getOffers().size() > (isMember ? (P2P_SLOTS - 1) : (F2P_SLOTS - 1));
    }

    /**
     * Starts a buy offer in the Grand Exchange.
     * @param itemId the ID of the item to buy.
     * @param amount the number of items to buy.
     * @param percentIncrease positive for increasing the price by 5%,
     *                            negative for decreasing by 5%.
     */
    public static void startBuyOffer(int itemId, int amount, int percentIncrease, boolean thirty) {
        if (!isOpen() || isFull()) {
            return;
        }

        int slotNumber = freeSlot();
        if (slotNumber == -1) {
            return;
        }

        GrandExchangeSlot slot = GrandExchangeSlot.getBySlot(slotNumber);

        if (slot == null) {
            return;
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, slot.getId(), -1, slot.getBuyChild());  // Select free slot
        setItem(itemId);  // Set item to buy
        setItemQuantity(amount);  // Set quantity to buy

        int ticker;
        if (percentIncrease < 0) {
            ticker = thirty ? 56 : 10;
            percentIncrease = percentIncrease * -1;
        } else {
            ticker = thirty ? 57 : 13;
        }
        int finalFive = percentIncrease;

        for (int i = 0; i < finalFive; i++) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(1, InterfaceID.GeOffers.SETUP_DESC, -1, ticker);  // Adjust price
        }

        // Confirm the offer
        Widgets.search().withAction("Confirm").first().ifPresent(widget -> {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(1, widget.getId(), -1, -1);
        });
    }

    public static void startBuyOffer(int itemId, int amount, int percentIncrease) {
        startBuyOffer(itemId, amount, percentIncrease, false);
    }

    public static boolean startBuyOfferPrice(int itemId, int amount, int price) {
        int slotNumber = freeSlot();
        if (slotNumber == -1) {
            return false;
        }

        GrandExchangeSlot slot = GrandExchangeSlot.getBySlot(slotNumber);
        if (slot == null) {
            return false;
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, slot.getId(), -1, slot.getBuyChild());
        setItem(itemId);  // Set item to buy
        setItemPrice(price);
        setItemQuantity(amount);  // Set quantity to buy
        Widgets.search().withAction("Confirm").first().ifPresent(w -> {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(1, w.getId(), -1, -1);
        });

        return true;
    }

    /**
     * Starts a buy offer based on the item name (supports wildcards).
     * @param itemName the name of the item to buy (case insensitive, supports wildcard *).
     * @param amount the number of items to buy.
     * @param fivePercentIncrease positive for increasing the price by 5%,
     *                            negative for decreasing by 5%.
     */
    public static void startBuyOffer(String itemName, int amount, int fivePercentIncrease) {
        Map.Entry<Integer, ItemComposition> entry = EthanApiPlugin.itemDefs.asMap()
                .entrySet()
                .stream()
                .filter(e -> WildcardMatcher.matches(itemName.toLowerCase(), Text.removeTags(e.getValue().getName().toLowerCase())))
                .findFirst().orElse(null);

        if (entry == null) {
            return;  // Item not found
        }

        startBuyOffer(entry.getValue().getId(), amount, fivePercentIncrease, false);
    }

    /**
     * Starts a sell offer for all of a specific item in the inventory.
     * @param widget the widget representing the item in the inventory.
     * @param percentChange positive for increasing the price by 5%,
     *                            negative for decreasing by 5%.
     */
    public static void startSellOffer(Widget widget, int percentChange, boolean thirty) {
        if (!isOpen() || isFull()) {
            return;
        }


        int slotNumber = freeSlot();
        if (slotNumber == -1) {
            return;
        }

        GrandExchangeSlot slot = GrandExchangeSlot.getBySlot(slotNumber);

        if (slot == null) {
            return;
        }


        int itemId = widget.getItemId();

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, slot.getId(), -1, slot.getSellChild());
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, InterfaceID.GeOffersSide.ITEMS, itemId, widget.getIndex());

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, InterfaceID.GeOffers.SETUP_DESC, -1, 6);  // Adjust price

        int ticker;
        if (percentChange < 0) {
            ticker = thirty ? 56 : 10;
            percentChange = percentChange * -1;
        } else {
            ticker = thirty ? 57 : 13;
        }
        int finalFive = percentChange;

        for (int i = 0; i < finalFive; i++) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(1, InterfaceID.GeOffers.SETUP_DESC, -1, ticker);  // Adjust price
        }

        // Confirm the offer
        Widgets.search().withAction("Confirm").first().ifPresent(w -> {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(1, w.getId(), -1, -1);
        });
    }

    public static void startSellOffer(Widget widget, int fivePercentDecrease) {
        startSellOffer(widget, fivePercentDecrease, false);
    }

    /**
     * Checks if the Grand Exchange window is currently open and visible.
     * @return true if the window is open, false otherwise.
     */
    public static boolean isOpen() {
        return getPage() != Page.CLOSED && getPage() != Page.UNKNOWN;
    }

    /**
     * Checks if the Grand Exchange offer window (buying/selling) is currently open.
     * @return true if an offer window is open, false otherwise.
     */
    public static boolean isOfferOpen() {
        return getPage() == Page.BUYING || getPage() == Page.SELLING;
    }

    /**
     * Retrieves a list of all active Grand Exchange offers.
     * @return a list of active Grand Exchange offers.
     */
    public static List<GrandExchangeOffer> getOffers() {
        return Arrays.stream(client.getGrandExchangeOffers())
                .filter(offer -> offer.getItemId() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Checks if there are any active offers in the Grand Exchange.
     * @return true if there are no offers, false otherwise.
     */
    public static boolean isEmpty() {
        return getOffers().isEmpty();
    }

    /**
     * Interacts with the 'Collect' button in the Grand Exchange to collect all items.
     * Does not check for offer status or visibility beforehand.
     */
    public static void collectAll() {
        if (!readyToCollect()) {
            return;  // No 'Collect' button found
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, InterfaceID.GeOffers.COLLECTALL, -1, 0);  // Interact with 'Collect' button
    }

    /**
     * Checks if there are any completed offers in the Grand Exchange.
     * @return true if there are offers ready to collect
     */

    public static boolean readyToCollect() {
        return !Widgets.search().hiddenState(false).withText("Collect").empty();
    }

    /**
     * Checks if a Grand Exchange offer contains the specified item in at least the given quantity.
     *
     * @param itemId the ID of the item to check for
     * @param amount the minimum quantity of the item required
     * @return true if the item exists in any Grand Exchange offer with at least the specified quantity, false otherwise
     */
    public static boolean hasItem(int itemId, int amount) {
        for (GrandExchangeOffer offer : getOffers()) {
            if (offer.getItemId() == itemId
                    && offer.getTotalQuantity() >= amount) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a Grand Exchange offer contains the specified item in at least 1 quantity.
     *
     * @param itemId the ID of the item to check for
     * @return true if the item exists in any Grand Exchange offer with at least 1 quantity, false otherwise
     */
    public static boolean hasItem(int itemId) {
        return hasItem(itemId, 1);
    }

    /**
     * Checks if a Grand Exchange offer contains the specified item by name in at least the given quantity.
     * The method first resolves the item ID by matching the item name, ignoring tags and case, and then
     * verifies if the item is present in an offer.
     *
     * @param itemName the name of the item to check for
     * @param amount the minimum quantity of the item required
     * @return true if the item exists in any Grand Exchange offer with at least the specified quantity, false otherwise
     */
    public static boolean hasItem(String itemName, int amount) {
        Map.Entry<Integer, ItemComposition> entry = EthanApiPlugin.itemDefs.asMap()
                .entrySet()
                .stream()
                .filter(e -> WildcardMatcher.matches(itemName.toLowerCase(), Text.removeTags(e.getValue().getName().toLowerCase())))
                .filter(e -> e.getValue().getNote() != 799)
                .findFirst().orElse(null);

        if (entry == null || entry.getValue() == null) {
            return false;
        }

        return hasItem(entry.getValue().getId(), amount);
    }

    /**
     * Checks if a Grand Exchange offer contains the specified item by name in at least 1 quantity.
     * The method first resolves the item ID by matching the item name, ignoring tags and case, and then
     * verifies if the item is present in an offer.
     *
     * @param itemName the name of the item to check for
     * @return true if the item exists in any Grand Exchange offer with at least 1 quantity, false otherwise
     */
    public static boolean hasItem(String itemName) {
        return hasItem(itemName, 1);
    }


    private static boolean isSellOpen() {
        return getPage() == Page.SELLING;
    }

    private static boolean isBuyOpen() {
        return getPage() == Page.BUYING;
    }

    private static int getItemId() {
        return client.getVarpValue(VarPlayer.CURRENT_GE_ITEM);
    }

    private static void setItem(int id) { // possibly might need to invoke this on client thread
        MousePackets.queueClickPacket();
        client.runScript(754, id, 84);
    }

    private static int getItemPrice() {
        return client.getVarbitValue(PRICE_VARIBT);
    }

    private static int getItemQuantity() {
        return client.getVarbitValue(QUANTITY_VARBIT);
    }

    private static void setItemPrice(int price) {
        Widget offerWidget = client.getWidget(InterfaceID.GeOffers.SETUP_DESC);
        if (offerWidget != null && offerWidget.getChild(12) != null) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(offerWidget.getChild(12), "Enter price");
            client.setVarcStrValue(359,Integer.toString(price));
            client.setVarcIntValue(5,7);
            client.runScript(681);
            return;
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, InterfaceID.GeOffers.SETUP_DESC, -1, 12);
    }

    private static void setItemQuantity(int quantity) {
        Widget offerWidget = client.getWidget(InterfaceID.GeOffers.SETUP_DESC);
        if (offerWidget != null && offerWidget.getChild(7) != null) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(offerWidget.getChild(7), "Enter quantity");
            client.setVarcStrValue(359,Integer.toString(quantity));
            client.setVarcIntValue(5,7);
            client.runScript(681);
            return;
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetActionPacket(1, InterfaceID.GeOffers.SETUP_DESC, -1, 7);
    }

    private static Page getPage() {
        Widget offerContainer = client.getWidget(InterfaceID.GeOffers.SETUP_DESC);

        if (offerContainer != null && !offerContainer.isHidden()) {
            String text = offerContainer.getChild(20).getText();
            if (text == null || text.isEmpty()) {
                return Page.UNKNOWN;
            }

            if (text.equalsIgnoreCase("sell offer")) {
                return Page.SELLING;
            }

            if (text.equalsIgnoreCase("buy offer")) {
                return Page.BUYING;
            }

            return Page.UNKNOWN;
        }

        Widget homeContainer = client.getWidget(InterfaceID.GeOffers.CONTENTS);

        if (homeContainer != null && !homeContainer.isHidden()) {
            return Page.HOME;
        }

        return Page.CLOSED;
    }

    private static int freeSlot() {
        GrandExchangeOffer[] offers = client.getGrandExchangeOffers();
        for (int slot = 0; slot < 8; slot++) {
            if (offers[slot] == null || offers[slot].getState() == GrandExchangeOfferState.EMPTY) {
                return slot + 1;
            }
        }

        return -1;
    }

    private static boolean isNoted(int id) {
        return client.getItemDefinition(id).getNote() == 799;
    }

    private static int getNotedId(int id) {
        return client.getItemDefinition(id).getLinkedNoteId();
    }

    public enum Page {
        UNKNOWN,
        HOME,
        CLOSED,
        BUYING,
        SELLING
    }
}
