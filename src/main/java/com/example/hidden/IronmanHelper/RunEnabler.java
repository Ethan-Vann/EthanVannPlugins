package com.example.hidden.IronmanHelper;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;


@PluginDescriptor(name = "RunEnabler", description = "", enabledByDefault = false, tags = {"ethan"})
public class RunEnabler extends Plugin{
    @Subscribe
    public void onGameTick(GameTick e) {
        if(!EthanApiPlugin.loggedIn()){
            return;
        }
        if(EthanApiPlugin.getClient().getVarpValue(173)==0&&EthanApiPlugin.getClient().getEnergy()>100){
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(1,10485787,-1,-1);
        }
    }
}
