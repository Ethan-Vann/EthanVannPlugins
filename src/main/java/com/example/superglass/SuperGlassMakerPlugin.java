package com.example.superglass;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.PacketUtilsPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.NPCPackets;
import com.example.Packets.ObjectPackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginManager;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;

@PluginDescriptor(name = "Super Glass Maker", description = "", enabledByDefault = false, tags = {"ethan"})
@Slf4j
@PluginDependency(PacketUtilsPlugin.class)
@PluginDependency(EthanApiPlugin.class)
public class SuperGlassMakerPlugin extends Plugin
{
	public int timeout = 0;
	@Inject
	Client client;
	@Inject
	PluginManager pluginManager;
	@Inject
	WidgetPackets widgetPackets;
	@Inject
	MousePackets mousePackets;
	@Inject
	NPCPackets npcPackets;
	@Inject
	ObjectPackets objectPackets;
	@Inject
	EthanApiPlugin api;
	@Inject
	SuperGlassMakerPluginConfig config;

	@Override
	@SneakyThrows
	public void startUp()
	{
		timeout = 0;
	}

	@Override
	public void shutDown()
	{

	}

	@Provides
	public SuperGlassMakerPluginConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SuperGlassMakerPluginConfig.class);
	}

	@Subscribe
	public void onGameTick(GameTick event) throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException
	{
		if (timeout > 0)
		{
			timeout--;
			return;
		}
		NPC banker = client.getNpcs().stream().filter(npc -> npc.getName().toLowerCase().contains("bank")).sorted(Comparator.comparingInt(x -> x.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()))).findFirst().orElse(null);
		TileObject bank = api.findObject("bank");
		if (client.getWidget(WidgetInfo.BANK_CONTAINER) == null)
		{
			if (!interactBankOrBanker(banker, bank))
			{
				api.stopPlugin(this);
				return;
			}
			return;
		}
		Widget sand = api.getItem(ItemID.BUCKET_OF_SAND, WidgetInfo.BANK_ITEM_CONTAINER);
		Widget glass = api.getItem(ItemID.MOLTEN_GLASS, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
		Widget astral = api.getItem(ItemID.ASTRAL_RUNE, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
		Widget secondary = api.getItem(config.secondary().getId(), WidgetInfo.BANK_ITEM_CONTAINER);
		Widget make_glass = client.getWidget(14286966);
		if (sand == null || astral == null || secondary == null || make_glass == null)
		{
			api.stopPlugin(this);
			return;
		}
		if (glass != null)
		{
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetAction(glass, "Deposit-All");
		}
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(sand, "Withdraw-" + config.secondary().getSandAmount());
		boolean secondaryWithdrawn = handleSecondary();
		if (!interactBankOrBanker(banker, bank)||!secondaryWithdrawn)
		{
			api.stopPlugin(this);
			return;
		}
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(make_glass, "Cast");
		timeout = 3;
	}

	public boolean handleSecondary()
	{
		Widget secondary = api.getItem(config.secondary().getId(), WidgetInfo.BANK_ITEM_CONTAINER);
		if (secondary == null)
		{
			return false;
		}
		if (config.secondary() == Secondary.GIANT_SEAWEED)
		{
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetAction(secondary, "Withdraw-1");
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetAction(secondary, "Withdraw-1");
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetAction(secondary, "Withdraw-1");
			return true;
		}
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(secondary, "Withdraw-"+config.secondary().getSandAmount());
		return true;
	}

	public boolean interactBankOrBanker(NPC banker, TileObject bank)
	{
		if (banker != null)
		{
			mousePackets.queueClickPacket();
			npcPackets.queueNPCAction(banker, "Bank");
			return true;
		}
		if (bank != null)
		{
			mousePackets.queueClickPacket();
			objectPackets.queueObjectAction(bank, false, "Bank");
			return true;
		}
		return false;
	}

}
