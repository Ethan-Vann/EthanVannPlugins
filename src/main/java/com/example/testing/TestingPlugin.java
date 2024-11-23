package com.example.testing;

import com.example.EthanApiPlugin.Collections.NPCs;
import com.example.EthanApiPlugin.EthanApiPlugin;
import net.runelite.api.Client;
import net.runelite.api.KeyCode;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@PluginDescriptor(name = "Testing", enabledByDefault = false, tags = {"Ethan Vann"})
public class TestingPlugin extends Plugin {
    @Inject
    Client client;
    @Subscribe
    public void onGameTick(GameTick e) {
        if(!client.isKeyPressed(KeyCode.KC_SHIFT)) return;
        NPCs.search().nearestToPlayer().ifPresent(x->System.out.println(EthanApiPlugin.getHeadIcon(x)));
    }
}
