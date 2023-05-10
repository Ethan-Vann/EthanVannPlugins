package com.example.Neverlogout;

import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.time.Duration;

@PluginDescriptor(name = "<html><font color=\"#ff6961\">NeverLogOut</font></html>",
        tags = {"pajau","neverlogout"}
)
public class neverlogout extends Plugin {
    @Inject
    private Client client;

    @Override
    protected void startUp() throws Exception {
        client.setIdleTimeout((int) Duration.ofMinutes(60*5).toMillis() / Constants.CLIENT_TICK_LENGTH);
    }

}
