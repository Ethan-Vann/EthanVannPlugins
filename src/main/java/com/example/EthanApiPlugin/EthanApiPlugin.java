package com.example.EthanApiPlugin;


import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.example.gauntletFlicker.QuickPrayer;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.api.CollisionData;
import net.runelite.api.GameObject;
import net.runelite.api.HeadIcon;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.Point;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
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
		tags = {"ethan"},
		hidden = true
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
	ConfigManager configManager;
	@Inject
	PluginManager pluginManager;
	private int quickPrayerWidgetID = WidgetInfo.MINIMAP_QUICK_PRAYER_ORB.getPackedId();

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
	public int countItem(String str,WidgetInfo container)
	{
		Widget[] items = client.getWidget(container).getDynamicChildren();
		int count = 0;
		for (int i = 0; i < items.length; i++)
		{
			if (WildcardMatcher.matches(str.toLowerCase(), Text.removeTags(items[i].getName()).toLowerCase()))
			{
				count++;
			}
		}
		return count;
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
	public boolean canPathToTile(Tile destinationTile){
		int z = client.getPlane();
		if (z != destinationTile.getPlane())
		{
			return false;
		}

		CollisionData[] collisionData = client.getCollisionMaps();
		if (collisionData == null)
		{
			return false;
		}

		int[][] directions = new int[128][128];
		int[][] distances = new int[128][128];
		int[] bufferX = new int[4096];
		int[] bufferY = new int[4096];

		// Initialise directions and distances
		for (int i = 0; i < 128; ++i)
		{
			for (int j = 0; j < 128; ++j)
			{
				directions[i][j] = 0;
				distances[i][j] = Integer.MAX_VALUE;
			}
		}

		int pSX = client.getLocalPlayer().getLocalLocation().getSceneX();

		int pSY = client.getLocalPlayer().getLocalLocation().getSceneY();
		Point p1 = client.getScene().getTiles()[client.getPlane()][pSX][pSY].getSceneLocation();
		Point p2 = destinationTile.getSceneLocation();

		int middleX = p1.getX();
		int middleY = p1.getY();
		int currentX = middleX;
		int currentY = middleY;
		int offsetX = 64;
		int offsetY = 64;
		// Initialise directions and distances for starting tile
		directions[offsetX][offsetY] = 99;
		distances[offsetX][offsetY] = 0;
		int index1 = 0;
		bufferX[0] = currentX;
		int index2 = 1;
		bufferY[0] = currentY;
		int[][] collisionDataFlags = collisionData[z].getFlags();

		boolean isReachable = false;

		while (index1 != index2)
		{
			currentX = bufferX[index1];
			currentY = bufferY[index1];
			index1 = index1 + 1 & 4095;
			// currentX is for the local coordinate while currentMapX is for the index in the directions and distances arrays
			int currentMapX = currentX - middleX + offsetX;
			int currentMapY = currentY - middleY + offsetY;
			if ((currentX == p2.getX()) && (currentY == p2.getY()))
			{
				isReachable = true;
				break;
			}

			int currentDistance = distances[currentMapX][currentMapY] + 1;
			if (currentMapX > 0 && directions[currentMapX - 1][currentMapY] == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0)
			{
				// Able to move 1 tile west
				bufferX[index2] = currentX - 1;
				bufferY[index2] = currentY;
				index2 = index2 + 1 & 4095;
				directions[currentMapX - 1][currentMapY] = 2;
				distances[currentMapX - 1][currentMapY] = currentDistance;
			}

			if (currentMapX < 127 && directions[currentMapX + 1][currentMapY] == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0)
			{
				// Able to move 1 tile east
				bufferX[index2] = currentX + 1;
				bufferY[index2] = currentY;
				index2 = index2 + 1 & 4095;
				directions[currentMapX + 1][currentMapY] = 8;
				distances[currentMapX + 1][currentMapY] = currentDistance;
			}

			if (currentMapY > 0 && directions[currentMapX][currentMapY - 1] == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
			{
				// Able to move 1 tile south
				bufferX[index2] = currentX;
				bufferY[index2] = currentY - 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX][currentMapY - 1] = 1;
				distances[currentMapX][currentMapY - 1] = currentDistance;
			}

			if (currentMapY < 127 && directions[currentMapX][currentMapY + 1] == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
			{
				// Able to move 1 tile north
				bufferX[index2] = currentX;
				bufferY[index2] = currentY + 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX][currentMapY + 1] = 4;
				distances[currentMapX][currentMapY + 1] = currentDistance;
			}

			if (currentMapX > 0 && currentMapY > 0 && directions[currentMapX - 1][currentMapY - 1] == 0 && (collisionDataFlags[currentX - 1][currentY - 1] & 19136782) == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
			{
				// Able to move 1 tile south-west
				bufferX[index2] = currentX - 1;
				bufferY[index2] = currentY - 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX - 1][currentMapY - 1] = 3;
				distances[currentMapX - 1][currentMapY - 1] = currentDistance;
			}

			if (currentMapX < 127 && currentMapY > 0 && directions[currentMapX + 1][currentMapY - 1] == 0 && (collisionDataFlags[currentX + 1][currentY - 1] & 19136899) == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0 && (collisionDataFlags[currentX][currentY - 1] & 19136770) == 0)
			{
				// Able to move 1 tile north-west
				bufferX[index2] = currentX + 1;
				bufferY[index2] = currentY - 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX + 1][currentMapY - 1] = 9;
				distances[currentMapX + 1][currentMapY - 1] = currentDistance;
			}

			if (currentMapX > 0 && currentMapY < 127 && directions[currentMapX - 1][currentMapY + 1] == 0 && (collisionDataFlags[currentX - 1][currentY + 1] & 19136824) == 0 && (collisionDataFlags[currentX - 1][currentY] & 19136776) == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
			{
				// Able to move 1 tile south-east
				bufferX[index2] = currentX - 1;
				bufferY[index2] = currentY + 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX - 1][currentMapY + 1] = 6;
				distances[currentMapX - 1][currentMapY + 1] = currentDistance;
			}

			if (currentMapX < 127 && currentMapY < 127 && directions[currentMapX + 1][currentMapY + 1] == 0 && (collisionDataFlags[currentX + 1][currentY + 1] & 19136992) == 0 && (collisionDataFlags[currentX + 1][currentY] & 19136896) == 0 && (collisionDataFlags[currentX][currentY + 1] & 19136800) == 0)
			{
				// Able to move 1 tile north-east
				bufferX[index2] = currentX + 1;
				bufferY[index2] = currentY + 1;
				index2 = index2 + 1 & 4095;
				directions[currentMapX + 1][currentMapY + 1] = 12;
				distances[currentMapX + 1][currentMapY + 1] = currentDistance;
			}
		}
		return isReachable;
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
