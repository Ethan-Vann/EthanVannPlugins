package com.example.PacketUtils;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ScriptPreFired;
import net.runelite.client.RuneLite;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;

import javax.inject.Inject;
import javax.swing.*;

@Slf4j
@Singleton
@PluginDescriptor(
        name = "Packet Utils",
        description = "Packet Utils for Plugins",
        enabledByDefault = true,
        tags = {"ethan"}
)
public class PacketUtilsPlugin extends Plugin {
    @Inject
    PacketUtilsConfig config;
    @Inject
    Client client;
    static Client staticClient;
    @Inject
    PacketReflection packetReflection;
    @Inject
    ClientThread thread;
    public static final int CLIENT_REV = 213;
    private static boolean loaded = false;
    @Inject
    private PluginManager pluginManager;

    @Subscribe
    public void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN) {
            loaded = packetReflection.LoadPackets();
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    @Provides
    public PacketUtilsConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(PacketUtilsConfig.class);
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked e) {
        if (config.debug()) {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "Packet Utils", e.toString(), null);
            System.out.println(e);
        }
    }

    @Subscribe
    public void onScriptPreFired(ScriptPreFired e) {
        if (config.debug()) {
            if (e.getScriptId() == 1121) {
                System.out.print("resume pause");
                for (int i = 1; i < e.getScriptEvent().getArguments().length; i++) {
                    System.out.print(":" + e.getScriptEvent().getArguments()[i]);
                }
                System.out.println();
            }
        }
    }

    @Override
    @SneakyThrows
    public void startUp() {
        staticClient = client;
        if (client.getRevision() != CLIENT_REV) {
            SwingUtilities.invokeLater(() ->
            {
                JOptionPane.showMessageDialog(null, "PacketUtils not updated for this rev please " +
                        "wait for " +
                        "plugin update");
                try {
                    pluginManager.setPluginEnabled(this, false);
                    pluginManager.stopPlugin(this);
                } catch (PluginInstantiationException ignored) {
                }
            });
            return;
        }
        thread.invoke(() ->
        {
            if (client.getGameState() != null && client.getGameState() == GameState.LOGGED_IN) {
                loaded = packetReflection.LoadPackets();
            }
        });
        SwingUtilities.invokeLater(() ->
        {
            for (Plugin plugin : pluginManager.getPlugins()) {
                if (plugin.getName().equals("EthanApiPlugin")) {
                    if (pluginManager.isPluginEnabled(plugin)) {
                        continue;
                    }
                    try {
                        pluginManager.setPluginEnabled(plugin, true);
                        pluginManager.startPlugin(plugin);
                    } catch (PluginInstantiationException e) {
                        //e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void shutDown() {
        log.info("Shutdown");
        loaded = false;
    }

    @Inject
    private void init() {
        if (config.alwaysOn() && client.getRevision() == CLIENT_REV) {
            SwingUtilities.invokeLater(() ->
            {
                try {
                    RuneLite.getInjector().getInstance(PluginManager.class).setPluginEnabled(this, true);
                    RuneLite.getInjector().getInstance(PluginManager.class).startPlugin(this);
                } catch (PluginInstantiationException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
