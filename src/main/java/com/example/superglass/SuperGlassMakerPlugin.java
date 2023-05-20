package com.example.superglass;

import com.example.EthanApiPlugin.*;
import com.example.InteractionApi.NPCInteraction;
import com.example.InteractionApi.TileObjectInteraction;
import com.example.PacketUtils.PacketUtilsPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@PluginDescriptor(name = "Super Glass Maker", description = "", enabledByDefault = false, tags = {"ethan"})
@Slf4j
@PluginDependency(PacketUtilsPlugin.class)
@PluginDependency(EthanApiPlugin.class)
public class SuperGlassMakerPlugin extends Plugin {
    public int timeout = 0;
    @Inject
    Client client;
    @Inject
    EthanApiPlugin api;
    @Inject
    SuperGlassMakerPluginConfig config;

    @Override
    @SneakyThrows
    public void startUp() {
        timeout = 0;
    }

    int timesFailed = 0;

    @Override
    public void shutDown() {

    }

    @Provides
    public SuperGlassMakerPluginConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(SuperGlassMakerPluginConfig.class);
    }

    @Subscribe
    public void onGameTick(GameTick event) throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (timeout > 0) {
            timeout--;
            return;
        }
        Optional<NPC> banker = NPCs.search().withAction("Bank").nearestToPlayer();
        Optional<TileObject> bank = TileObjects.search().withAction("Bank").nearestToPlayer();
        if (client.getWidget(WidgetInfo.BANK_CONTAINER) == null) {
            if (banker.isPresent()) {
                NPCInteraction.interact(banker.get(), "Bank");
                return;
            } else if (bank.isPresent()) {
                TileObjectInteraction.interact(bank.get(), "Bank");
                return;
            } else {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "couldn't find bank or banker", null);
                api.stopPlugin(this);
                return;
            }
        }
        Optional<Widget> sand = Bank.search().withId(ItemID.BUCKET_OF_SAND).first();
        Optional<Widget> glass = BankInventory.search().withId(ItemID.MOLTEN_GLASS).first();
        Optional<Widget> astral = BankInventory.search().withId(ItemID.ASTRAL_RUNE).first();
        Optional<Widget> secondary = Bank.search().withId(config.secondary().getId()).first();
        Widget make_glass = client.getWidget(14286966);
        if (sand.isEmpty() || astral.isEmpty() || secondary.isEmpty() || make_glass == null) {
            if (sand.isEmpty()) {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "no sand", null);
            }
            if (astral.isEmpty()) {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "no astral", null);
            }
            if (secondary.isEmpty()) {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "no secondary", null);
            }
            if (make_glass == null) {
                client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "spell was null", null);
            }
            timesFailed++;
            if (timesFailed > 2) {
                api.stopPlugin(this);
            } else {
                if (banker.isPresent()) {
                    NPCInteraction.interact(banker.get(), "Bank");
                } else if (bank.isPresent()) {
                    TileObjectInteraction.interact(bank.get(), "Bank");
                } else {
                    client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "couldn't find bank or banker", null);
                    api.stopPlugin(this);
                    return;
                }
            }
            return;
        } else {
            timesFailed = 0;
        }
        if (glass.isPresent()) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(glass.get(), "Deposit-All");
        }
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(sand.get(), "Withdraw-" + config.secondary().getSandAmount());
        boolean secondaryWithdrawn = handleSecondary();
        if (!secondaryWithdrawn) {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "no secondary stop plugin", null);
            api.stopPlugin(this);
            return;
        }
        if (banker.isPresent()) {
            NPCInteraction.interact(banker.get(), "Bank");
        } else if (bank.isPresent()) {
            TileObjectInteraction.interact(bank.get(), "Bank");
        } else {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "couldn't find bank or banker try 2", null);
            api.stopPlugin(this);
            return;
        }
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(make_glass, "Cast");
        timeout = 3;
    }

    public boolean handleSecondary() {
        Optional<Widget> secondary = Bank.search().withId(config.secondary().getId()).first();
        if (secondary.isEmpty()) {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "no secondary second try", null);
            return false;
        }
        if (config.secondary() == Secondary.GIANT_SEAWEED) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(secondary.get(), "Withdraw-1");
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(secondary.get(), "Withdraw-1");
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetAction(secondary.get(), "Withdraw-1");
            return true;
        }
        MousePackets.queueClickPacket();
        WidgetPackets.queueWidgetAction(secondary.get(), "Withdraw-" + config.secondary().getSandAmount());
        return true;
    }


}
