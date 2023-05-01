package com.example.Neverlogout;

import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;

import javax.inject.Inject;

public class neverlogout extends Plugin {
    @Inject
    private Client client;

    @Override
    protected void startUp() throws Exception {
        super.startUp();
    }
    @Subscribe
    void onGameTick(GameTick event){
        if(client.getIdleTimeout()<100){
            client.setIdleTimeout(500);
        }
    }
}
