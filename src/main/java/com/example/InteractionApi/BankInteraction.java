package com.example.InteractionApi;

import com.example.EthanApiPlugin.Collections.Bank;
import com.example.EthanApiPlugin.Collections.Widgets;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class BankInteraction {
    private static final Set<Integer> DEFAULT_WITHDRAW_AMOUNTS = Set.of(1, 5, 10);

    private static final int WITHDRAW_AS_VARBIT = 3958;
    private static final int WITHDRAW_ITEM_MODE = 0;
    private static final int WITHDRAW_NOTES_MODE = 1;
    private static final int WITHDRAW_ITEM_MODE_WIDGET = 786456;
    private static final int WITHDRAW_NOTE_MODE_WIDGET = 786458;

    private static final String ITEM_MODE_ACTION = "Item";
    private static final String NOTE_MODE_ACTION = "Note";

    public static boolean useItem(String name, String... actions) {
        return Bank.search().withName(name).first().flatMap(item ->
        {
            setWithdrawMode(EthanApiPlugin.getClient().getVarbitValue(WITHDRAW_AS_VARBIT));

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static boolean useItem(int id, String... actions) {
        return Bank.search().withId(id).first().flatMap(item ->
        {
            setWithdrawMode(EthanApiPlugin.getClient().getVarbitValue(WITHDRAW_AS_VARBIT));

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static boolean useItem(Predicate<? super Widget> predicate, String... actions) {
        return Bank.search().filter(predicate).first().flatMap(item ->
        {
            setWithdrawMode(EthanApiPlugin.getClient().getVarbitValue(WITHDRAW_AS_VARBIT));

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static void withdrawX(Widget item, int amount) {
        int quantitySet = EthanApiPlugin.getClient().getVarbitValue(VarbitID.BANK_REQUESTEDQUANTITY);

        if (DEFAULT_WITHDRAW_AMOUNTS.contains(amount)
                || quantitySet == amount) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, "Withdraw-" + amount);
            WidgetPackets.queueResumeCount(amount);
            return;
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(item, "Withdraw-X");
        WidgetPackets.queueResumeCount(amount);
        EthanApiPlugin.getClient().setVarbit(VarbitID.BANK_REQUESTEDQUANTITY, amount);
    }

    public static void withdrawX(Widget item, int amount, boolean noted) {
        setWithdrawMode(noted ? WITHDRAW_NOTES_MODE : WITHDRAW_ITEM_MODE);

        int quantitySet = EthanApiPlugin.getClient().getVarbitValue(VarbitID.BANK_REQUESTEDQUANTITY);

        if (DEFAULT_WITHDRAW_AMOUNTS.contains(amount)
                || quantitySet == amount) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, "Withdraw-" + amount);
            WidgetPackets.queueResumeCount(amount);
            return;
        }

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(item, "Withdraw-X");
        WidgetPackets.queueResumeCount(amount);
        EthanApiPlugin.getClient().setVarbit(VarbitID.BANK_REQUESTEDQUANTITY, amount);
    }

    public static boolean useItemIndex(int index, String... actions) {
        return Bank.search().indexIs(index).first().flatMap(item ->
        {
            setWithdrawMode(EthanApiPlugin.getClient().getVarbitValue(WITHDRAW_AS_VARBIT));

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static boolean useItem(Widget item, String... actions) {
        if (item == null) {
            return false;
        }

        setWithdrawMode(EthanApiPlugin.getClient().getVarbitValue(WITHDRAW_AS_VARBIT));

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(item, actions);
        return true;
    }

    public static boolean useItem(String name, boolean noted, String... actions) {
        return Bank.search().withName(name).first().flatMap(item ->
        {
            setWithdrawMode(EthanApiPlugin.getClient().getVarbitValue(WITHDRAW_AS_VARBIT));

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static boolean useItem(int id, boolean noted, String... actions) {
        return Bank.search().withId(id).first().flatMap(item ->
        {
            setWithdrawMode(noted? WITHDRAW_NOTES_MODE : WITHDRAW_ITEM_MODE);

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static boolean useItem(Predicate<? super Widget> predicate, boolean noted, String... actions) {
        return Bank.search().filter(predicate).first().flatMap(item ->
        {
            setWithdrawMode(noted? WITHDRAW_NOTES_MODE : WITHDRAW_ITEM_MODE);

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static boolean useItemIndex(int index, boolean noted, String... actions) {
        return Bank.search().indexIs(index).first().flatMap(item ->
        {
            setWithdrawMode(noted? WITHDRAW_NOTES_MODE : WITHDRAW_ITEM_MODE);

            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(item, actions);
            return Optional.of(true);
        }).orElse(false);
    }

    public static boolean useItem(Widget item, boolean noted, String... actions) {
        if (item == null) {
            return false;
        }

        setWithdrawMode(noted? WITHDRAW_NOTES_MODE : WITHDRAW_ITEM_MODE);

        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(item, actions);
        return true;
    }

    public static boolean isNotedMode() {
        return EthanApiPlugin.getClient().getVarbitValue(VarbitID.BANK_WITHDRAWNOTES) == 1;
    }

    public static boolean setWithdrawMode(int withdrawMode) {
        int withdrawAsVarbitValue = EthanApiPlugin.getClient().getVarbitValue(WITHDRAW_AS_VARBIT);
        Optional<Widget> itemWidget = Widgets.search().withId(WITHDRAW_ITEM_MODE_WIDGET).withAction(ITEM_MODE_ACTION).first();
        Optional<Widget> noteWidget = Widgets.search().withId(WITHDRAW_NOTE_MODE_WIDGET).withAction(NOTE_MODE_ACTION).first();
        if (itemWidget.isEmpty() || noteWidget.isEmpty()) {
            return false;
        }

        if (withdrawMode == WITHDRAW_ITEM_MODE && withdrawAsVarbitValue != WITHDRAW_ITEM_MODE) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(itemWidget.get(), "Item");

            return true;
        }

        if (withdrawMode == WITHDRAW_NOTES_MODE && withdrawAsVarbitValue != WITHDRAW_NOTES_MODE) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(noteWidget.get(), "Note");

            return true;
        }

        return false;
    }
}
