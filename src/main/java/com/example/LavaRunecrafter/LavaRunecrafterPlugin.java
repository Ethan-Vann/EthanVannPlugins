//package com.example.LavaRunecrafter;
//
//import com.example.ObfuscatedNames;
//import com.example.Packets.MousePackets;
//import com.example.Packets.NPCPackets;
//import com.example.Packets.ObjectPackets;
//import com.example.Packets.WidgetPackets;
//import com.google.inject.Inject;
//import com.google.inject.Provides;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import net.runelite.api.Actor;
//import net.runelite.api.Client;
//import net.runelite.api.GameObject;
//import net.runelite.api.ItemID;
//import net.runelite.api.Player;
//import net.runelite.api.Tile;
//import net.runelite.api.TileObject;
//import net.runelite.api.events.GameTick;
//import net.runelite.api.widgets.Widget;
//import net.runelite.api.widgets.WidgetInfo;
//import net.runelite.client.config.ConfigManager;
//import net.runelite.client.eventbus.Subscribe;
//import net.runelite.client.plugins.Plugin;
//import net.runelite.client.plugins.PluginDescriptor;
//import net.runelite.client.plugins.PluginManager;
//
//import java.lang.reflect.Field;
//import java.util.Arrays;
//
//@PluginDescriptor(
//		name = "Upkeep Plugin",
//		description = "",
//		enabledByDefault = false,
//		tags = {"ethan"}
//)
//@Slf4j
//public class LavaRunecrafterPlugin extends Plugin
//{
//	public int timeout = 0;
//	@Inject
//	Client client;
//	@Inject
//	PluginManager pluginManager;
//	@Inject
//	WidgetPackets widgetPackets;
//	@Inject
//	MousePackets mousePackets;
//	@Inject
//	NPCPackets npcPackets;
//	@Inject
//	ObjectPackets objectPackets;
//	@Inject
//	LavaRunecrafterPluginConfig config;
//
//
//	@Provides
//	public LavaRunecrafterPluginConfig getConfig(ConfigManager configManager)
//	{
//		return configManager.getConfig(LavaRunecrafterPluginConfig.class);
//	}
//
//	@Override
//	@SneakyThrows
//	public void startUp()
//	{
//		timeout = 0;
//	}
//
//	@Override
//	public void shutDown()
//	{
//
//	}
//
//	@Subscribe
//	public void onGameTick(GameTick event)
//	{
//		Player player = client.getLocalPlayer();
//		TileObject bankChest = findObject("bank");
//		if(bankChest!=null){
//			if (client.getWidget(WidgetInfo.BANK_CONTAINER) == null){
//				if(isMoving(player)){
//					return;
//				}
//				mousePackets.queueClickPacket();
//				objectPackets.queueObjectAction(bankChest, false,"Use");
//				return;
//			}
//			Widget essence = getItem(ItemID.PURE_ESSENCE,WidgetInfo.BANK_CONTAINER);
//			essence = essence == null ? getItem(ItemID.RUNE_ESSENCE,WidgetInfo.BANK_CONTAINER) : essence;
//			Widget stamina = getItem(ItemID.STAMINA_POTION1,WidgetInfo.BANK_CONTAINER);
//			Widget dueling = getItem(ItemID.RING_OF_DUELING8,WidgetInfo.BANK_CONTAINER);
//		}
//	}
//
//	@SneakyThrows
//	boolean isMoving(Actor actor){
//		Field pathLength = actor.getClass().getField(ObfuscatedNames.ACTOR_PATH_LENGTH_FIELD);
//		pathLength.setAccessible(true);
//		int pathLengthVal = pathLength.getInt(actor);
//		pathLength.setAccessible(false);
//		return pathLengthVal > 0;
//	}
//	TileObject findObject(String objectName)
//	{
//		for (Tile[][] tile : client.getScene().getTiles())
//		{
//			for (Tile[] tiles : tile)
//			{
//				for (Tile tile1 : tiles)
//				{
//					if (tile1.getGameObjects().length != 0)
//					{
//						GameObject returnVal =
//								Arrays.stream(tile1.getGameObjects()).filter(gameObject -> gameObject != null && client.getObjectDefinition(gameObject.getId()).getName().toLowerCase().contains(objectName.toLowerCase())).findFirst().orElse(null);
//						if (returnVal != null)
//						{
//							return returnVal;
//						}
//					}
//				}
//			}
//		}
//		return null;
//	}
//	public Widget getItem(int id, WidgetInfo container)
//	{
//		Widget[] items = client.getWidget(container).getDynamicChildren();
//		for (int i = 0; i < items.length; i++)
//		{
//			if (items[i].getItemId() == id)
//			{
//				return items[i];
//			}
//		}
//		return null;
//	}
//
//}
