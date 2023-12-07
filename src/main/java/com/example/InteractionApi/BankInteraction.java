package com.example.InteractionApi;

import com.example.EthanApiPlugin.Collections.Bank;
import com.example.EthanApiPlugin.Collections.Widgets;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import net.runelite.api.widgets.Widget;

import java.util.Optional;
import java.util.function.Predicate;

public class BankInteraction {
    private static final int WITHDRAW_QUANTITY = 3960;
    private static final int WITHDRAW_AS_VARBIT = 3958;
    private static final int WITHDRAW_ITEM_MODE = 0;
    private static final int WITHDRAW_NOTES_MODE = 1;
    private static final int WITHDRAW_ITEM_MODE_WIDGET = 786454;
    private static final int WITHDRAW_NOTE_MODE_WIDGET = 786456;

    private static final String ITEM_MODE_ACTION = "Item";
    private static final String NOTE_MODE_ACTION = "Note";


    public static boolean useItem(String name, boolean noted, String... actions) {
        return Bank.search().withName(name).first().flatMap(item ->
        {
            setWithdrawMode(noted);

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static boolean useItem(int id, boolean noted, String... actions) {
        return Bank.search().withId(id).first().flatMap(item ->
        {
            setWithdrawMode(noted);

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static boolean useItem(Predicate<? super Widget> predicate, boolean noted, String... actions) {
        return Bank.search().filter(predicate).first().flatMap(item ->
        {
            setWithdrawMode(noted);

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static void withdrawX(Widget item, int amount, boolean noted) {
        setWithdrawMode(noted);

        if (EthanApiPlugin.getClient().getVarbitValue(WITHDRAW_QUANTITY) == amount) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(5, item.getId(), item.getItemId(), item.getIndex());
            return;
        }
        BankInteraction.useItem(item, noted, "Withdraw-X");
        EthanApiPlugin.getClient().setVarcStrValue(359, Integer.toString(amount));
        EthanApiPlugin.getClient().setVarcIntValue(5, 7);
        EthanApiPlugin.getClient().runScript(681);
        EthanApiPlugin.getClient().setVarbit(WITHDRAW_QUANTITY, amount);
    }

    public static boolean useItemIndex(int index, boolean noted, String... actions) {
        return Bank.search().indexIs(index).first().flatMap(item ->
        {
            setWithdrawMode(noted);

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static boolean useItem(Widget item, boolean noted, String... actions) {
        if (item == null) {
            return false;
        }

        setWithdrawMode(noted);

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(item, actions);
        return true;
    }

    public static boolean setWithdrawMode(boolean noted) {
        int withdrawAsVarbitValue = EthanApiPlugin.getClient().getVarbitValue(WITHDRAW_AS_VARBIT);
        Optional<Widget> itemWidget = Widgets.search().withId(WITHDRAW_ITEM_MODE_WIDGET).withAction(ITEM_MODE_ACTION).first();
        Optional<Widget> noteWidget = Widgets.search().withId(WITHDRAW_NOTE_MODE_WIDGET).withAction(NOTE_MODE_ACTION).first();
        if (itemWidget.isEmpty() || noteWidget.isEmpty()) {
            return false;
        }

        if (noted && withdrawAsVarbitValue == WITHDRAW_ITEM_MODE) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(noteWidget.get(), "Note");

            return true;
        }

        if (!noted && withdrawAsVarbitValue == WITHDRAW_NOTES_MODE) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(itemWidget.get(), "Item");

            return true;
        }

        return false;
    }
}
