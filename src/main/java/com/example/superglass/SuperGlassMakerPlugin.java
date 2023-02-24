package com.example.superglass;

import com.example.Packets.MousePackets;
import com.example.Packets.NPCPackets;
import com.example.Packets.ObjectPackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ItemID;
import net.runelite.api.NPC;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

@PluginDescriptor(
		name = "Super Glass Maker",
		description = "",
		enabledByDefault = false,
		tags = {"ethan"}
)
@Slf4j
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

	@Subscribe
	public void onGameTick(GameTick event) throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, IllegalAccessException
	{
		if (timeout > 0)
		{
			timeout--;
			return;
		}
		NPC banker =
				client.getNpcs().stream().filter(npc -> npc.getName().toLowerCase().contains("bank")).sorted(Comparator.comparingInt(x->x.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()))).findFirst().orElse(null);
		TileObject bank = findObject("bank");
		if (bank == null && banker == null)
		{
			try
			{
				pluginManager.stopPlugin(this);
			}
			catch (PluginInstantiationException e)
			{
				e.printStackTrace();
			}
		}
		if (client.getWidget(WidgetInfo.BANK_CONTAINER) == null)
		{
			if (banker != null)
			{
				mousePackets.queueClickPacket();
				npcPackets.queueNPCAction(banker, "Bank");
			}
			else if (bank != null)
			{
				mousePackets.queueClickPacket();
				objectPackets.queueObjectAction(bank, false, "Bank");
			}
			else
			{
				try
				{
					pluginManager.stopPlugin(this);
				}
				catch (PluginInstantiationException e)
				{
					e.printStackTrace();
				}
			}
			return;
		}
		Widget sand = getItem(ItemID.BUCKET_OF_SAND, WidgetInfo.BANK_ITEM_CONTAINER);
		Widget glass = getItem(ItemID.MOLTEN_GLASS, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
		Widget astral = getItem(ItemID.ASTRAL_RUNE, WidgetInfo.BANK_INVENTORY_ITEMS_CONTAINER);
		Widget seaweed = getItem(ItemID.GIANT_SEAWEED, WidgetInfo.BANK_ITEM_CONTAINER);
		Widget make_glass = client.getWidget(14286966);
		System.out.println(sand==null);
		System.out.println(astral==null);
		System.out.println(seaweed==null);
		System.out.println(make_glass==null);
		if (sand == null || astral == null || seaweed == null || make_glass == null)
		{
			try
			{
				pluginManager.stopPlugin(this);
			}
			catch (AssertionError | PluginInstantiationException e)
			{
				//print stack trace
			}
			return;
		}
		if (glass != null)
		{
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetAction(glass, "Deposit-All");
		}
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(sand, "Withdraw-18");
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(seaweed, "Withdraw-1");
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(seaweed, "Withdraw-1");
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(seaweed, "Withdraw-1");
		if (banker != null)
		{
			mousePackets.queueClickPacket();
			npcPackets.queueNPCAction(banker, "Bank");
		}
		else if (bank != null)
		{
			mousePackets.queueClickPacket();
			objectPackets.queueObjectAction(bank, false, "Bank");
		}
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetAction(make_glass, "Cast");
		timeout = 3;
	}

	TileObject findObject(String objectName)
	{
		ArrayList<TileObject> validObjects = new ArrayList<>();
		for (Tile[][] tile : client.getScene().getTiles())
		{
			for (Tile[] tiles : tile)
			{
				for (Tile tile1 : tiles)
				{
					if(tile1==null){
						continue;
					}
					if(tile1.getGameObjects()==null){
						continue;
					}
					if (tile1.getGameObjects().length != 0)
					{
						GameObject returnVal =
								Arrays.stream(tile1.getGameObjects()).filter(gameObject -> gameObject != null && client.getObjectDefinition(gameObject.getId()).getName().toLowerCase().contains(objectName.toLowerCase())).findFirst().orElse(null);
						if (returnVal != null)
						{
							validObjects.add(returnVal);
						}
					}
				}
			}
		}
		if(validObjects.size() > 0)
		{
			return validObjects.stream().sorted(Comparator.comparingInt(x->x.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()))).findFirst().orElse(null);
		}
		return null;
	}

	public Widget getItem(int id, WidgetInfo container)
	{
		Widget[] items = client.getWidget(container).getDynamicChildren();
		for (int i = 0; i < items.length; i++)
		{
			if (items[i].getItemId() == id)
			{
				return items[i];
			}
		}
		return null;
	}

}
