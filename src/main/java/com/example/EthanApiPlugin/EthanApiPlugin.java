package com.example.EthanApiPlugin;


import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.example.gauntletFlicker.QuickPrayer;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.HeadIcon;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.Prayer;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginInstantiationException;
import net.runelite.client.plugins.PluginManager;
import net.runelite.client.util.Text;
import net.runelite.client.util.WildcardMatcher;
import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static net.runelite.api.Varbits.QUICK_PRAYER;

@PluginDescriptor(
		name = "EthanApiPlugin",
		description = "",
		tags = {"ethan"}
)
public class EthanApiPlugin extends Plugin
{
	@Inject
	Client client;
	@Inject
	WidgetPackets widgetPackets;
	@Inject
	MousePackets mousePackets;
	@Inject
	PluginManager pluginManager;
	private int quickPrayerWidgetID = WidgetInfo.MINIMAP_QUICK_PRAYER_ORB.getPackedId();



	public void toggleNormalPrayer(Prayer style){
		mousePackets.queueClickPacket();
		widgetPackets.queueWidgetActionPacket(1,convAPIToWidgetInfo(style).getPackedId(),-1,-1);
	}


	public void toggleNormalPrayers(List<Prayer> styles){
		for(Prayer style: styles){
			mousePackets.queueClickPacket();
			widgetPackets.queueWidgetActionPacket(1,convAPIToWidgetInfo(style).getPackedId(),-1,-1);
		}
	}


	private WidgetInfo convAPIToWidgetInfo(Prayer style){
		return PrayerLocal.valueOf(style.name()).getWidgetInfo();
	}


	public boolean isQuickPrayerActive(QuickPrayer prayer)
	{
		if ((client.getVarbitValue(4102) & (int) Math.pow(2, prayer.getIndex())) == Math.pow(2, prayer.getIndex()))
		{
			return true;
		}
		return false;
	}
	public boolean isQuickPrayerEnabled()
	{
		return client.getVarbitValue(QUICK_PRAYER) == 1;
	}
	public void togglePrayer()
	{
		mousePackets.queueClickPacket(0, 0);
		widgetPackets.queueWidgetActionPacket(1, quickPrayerWidgetID, -1, -1);
	}
	@SneakyThrows
	public HeadIcon getHeadIcon(NPC npc) {
		Method getHeadIconArrayMethod = null;
		for (Method declaredMethod : npc.getComposition().getClass().getDeclaredMethods())
		{
			if(declaredMethod.getReturnType()== short[].class&&declaredMethod.getParameterTypes().length==0){
				getHeadIconArrayMethod = declaredMethod;
			}
		}

		if (getHeadIconArrayMethod == null) {
			return null;
		}
		getHeadIconArrayMethod.setAccessible(true);
		short[] headIconArray = (short[]) getHeadIconArrayMethod.invoke(npc.getComposition());
		if (headIconArray == null || headIconArray.length == 0) {
			return null;
		}
		return HeadIcon.values()[headIconArray[0]];
	}
	public Widget getItem(String str)
	{
		Widget[] items = client.getWidget(WidgetInfo.INVENTORY).getDynamicChildren();
		for (int i = 0; i < items.length; i++)
		{
			if (WildcardMatcher.matches(str.toLowerCase(), Text.removeTags(items[i].getName()).toLowerCase()))
			{
				return items[i];
			}
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

	public int getFirstFreeSlot(WidgetInfo container)
	{
		Widget[] items = client.getWidget(container).getDynamicChildren();
		for (int i = 0; i < items.length; i++)
		{
			if (items[i].getItemId() == 6512)
			{
				return i;
			}
		}
		return -1;
	}
	public int getEmptySlots(WidgetInfo widgetInfo)
	{
		List<Widget> inventoryItems = Arrays.asList(client.getWidget(widgetInfo.getId()).getDynamicChildren());
		return (int) inventoryItems.stream().filter(item -> item.getItemId() == 6512).count();
	}
	public boolean isMoving()
	{
		return client.getLocalPlayer().getPoseAnimation()
				!= client.getLocalPlayer().getIdlePoseAnimation();
	}
	public TileObject findObject(String objectName)
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
	public TileObject findObject(int id)
	{
		ArrayList<TileObject> validObjects = new ArrayList<>();
		Arrays.stream(client.getScene().getTiles()).flatMap(Arrays::stream).flatMap(Arrays::stream).filter(Objects::nonNull).filter(tile -> tile.getGameObjects() != null&&tile.getGameObjects().length != 0).forEach(tile -> {
			GameObject returnVal =
					Arrays.stream(tile.getGameObjects()).filter(gameObject -> gameObject != null && gameObject.getId()==id).findFirst().orElse(null);
			if (returnVal != null)
			{
				validObjects.add(returnVal);
			}
		});
		return validObjects.stream().min(Comparator.comparingInt(x->x.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()))).orElse(null);
	}
	public Widget getItemFromList(int[] list, WidgetInfo container)
	{
		for (int i : list)
		{
			Widget item = getItem(i, container);
			if (item != null)
			{
				return item;
			}
		}
		return null;
	}
	public int checkIfWearing(int[] ids)
	{
		if (client.getItemContainer(InventoryID.EQUIPMENT) != null)
		{
			Item[] equipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
			for (Item item : equipment)
			{
				for (int id : ids)
				{
					if (id == item.getId())
					{
						return item.getId();
					}
				}
			}
		}
		return -1;
	}
	@SneakyThrows
	public void stopPlugin(Plugin plugin)
	{
		SwingUtilities.invokeAndWait(() ->
		{
			try
			{
				pluginManager.stopPlugin(plugin);
				pluginManager.setPluginEnabled(plugin, false);
			}
			catch (PluginInstantiationException e)
			{
				throw new RuntimeException(e);
			}
		});
	}
}
