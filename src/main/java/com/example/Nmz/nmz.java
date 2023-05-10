package com.example.Nmz;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.api.events.GameTick;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
        name = "NMZ Anal",
        tags = {"pajau"}
)
public class nmz extends Plugin {
    @Inject
    private Client client;

    private int absPts,ovlFreshRemain,HP;

    @Override
    protected void startUp() throws Exception {
        absPts=client.getVarbitValue(Varbits.NMZ_ABSORPTION);
        ovlFreshRemain=client.getVarbitValue(Varbits.NMZ_OVERLOAD_REFRESHES_REMAINING);
        log.info("Abs: {}     Ovl:{}",absPts,ovlFreshRemain);
    }

    @Override
    protected void shutDown() throws Exception {
        absPts=-1;
        ovlFreshRemain=-1;
    }

    @Subscribe
    void onGameTick(GameTick event){
        if (inNmz()) {
            if (client.getVarbitValue(Varbits.NMZ_ABSORPTION) < 250) {
                //drink absº
            }
        }

    }

    private boolean inNmz(){
        if (client.getLocalPlayer() == null) {
            return false;
        }
        return true;
    }
}
