package com.example.ezclick;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.EthanApiPlugin.Inventory;
import com.example.EthanApiPlugin.TileObjects;

import com.example.PacketUtils.PacketUtilsPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.ObjectPackets;
import com.example.tooltips.*;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.TileObject;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@PluginDescriptor(name = "EZClick", tags = {"null"})
@PluginDependency(PacketUtilsPlugin.class)
@PluginDependency(EthanApiPlugin.class)
public class EZClickPlugin extends Plugin {

    @Inject
    public Client client;

    public TooltipManager tooltipManager = TooltipManager.INSTANCE;

    public static ArrayList<Tooltip> headerTooltips = new ArrayList<>();

    public static ArrayList<com.example.tooltips.Tooltip> problemTooltips = new ArrayList<>();

    public static ArrayList<com.example.tooltips.Tooltip> footerTooltips = new ArrayList<>();

    Tooltip header = new Tooltip("EZClick", TooltipColors.BLUE);

    Tooltip missingBonesTooltip = new Tooltip("Missing: Bones to offer", TooltipColors.RED);

    public static boolean ezClickActive = false;
    public static boolean ezHouseAltar = false;
    @Subscribe(priority = Integer.MAX_VALUE)
    public void onClientTick(ClientTick clientTick) {
        //RL flips entries frequently
        for (MenuEntry entry : client.getMenuEntries()) {
            if (processHouseAltarTooltips(entry)) {
                ezClickActive = true;
                ezHouseAltar = true;
                return;
            }
        }
        reset();
    }

    public void reset() {
        ezClickActive = false;
        ezHouseAltar = false;
        headerTooltips.clear();
        problemTooltips.clear();
        footerTooltips.clear();
    }

    public void addIfMissing(ArrayList<Tooltip> list, Tooltip tooltip) {
        if (!list.contains(tooltip))
            list.add(tooltip);
    }

    public Widget boneToOffer = null;

    public TileObject altar = null;

    public boolean processHouseAltarTooltips(MenuEntry menu) {
        boolean foundHouseAlter = false;
        if (menu.getOption().equals("Pray") && menu.getTarget().equals("<col=ffff>Altar")) {
            foundHouseAlter = true;
            addIfMissing(headerTooltips, header);
            List<Widget>  bonesToOffer = Inventory.search().nameContains("bones").withAction("Bury").result();
            int bonesToOfferSize = bonesToOffer.size();

            if (bonesToOfferSize == 0) {
                addIfMissing(problemTooltips, missingBonesTooltip);
            } else {
                boneToOffer = bonesToOffer.get(0);
                altar = TileObjects.search().nameContains("Altar").withAction("Pray").result().get(0);
            }
        }
        return foundHouseAlter;
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked) {
        System.out.println("Option: " + menuOptionClicked.getMenuOption());
        System.out.println("Target: " + menuOptionClicked.getMenuTarget());

        if (ezClickActive)
            menuOptionClicked.consume();

        if (ezHouseAltar) {
            if (boneToOffer != null) {
                MousePackets.queueClickPacket();
                ObjectPackets.queueWidgetOnTileObject(boneToOffer, altar);
            }
        }
    }
}
