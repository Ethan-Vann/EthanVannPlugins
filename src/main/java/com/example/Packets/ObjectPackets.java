package com.example.Packets;

import com.example.PacketDef;
import com.example.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.GameObject;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.PacketReflection.client;

public class ObjectPackets
{
	@SneakyThrows
	public static void queueObjectAction(int actionFieldNo, int objectId, int worldPointX, int worldPointY,
										 boolean ctrlDown)
	{
		int ctrl = ctrlDown ? 1 : 0;
		switch (actionFieldNo)
		{
			case 1:
				PacketReflection.sendPacket(PacketDef.OPLOC1, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 2:
				PacketReflection.sendPacket(PacketDef.OPLOC2, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 3:
				PacketReflection.sendPacket(PacketDef.OPLOC3, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 4:
				PacketReflection.sendPacket(PacketDef.OPLOC4, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 5:
				PacketReflection.sendPacket(PacketDef.OPLOC5, objectId, worldPointX, worldPointY, ctrl);
				break;
		}
	}

	@SneakyThrows
	public static void queueObjectAction(TileObject object, boolean ctrlDown, String... actionlist)
	{
		List<String> actions =
				Arrays.stream(client.getObjectDefinition(object.getId()).getActions()).collect(Collectors.toList());
		if (client.getObjectDefinition(object.getId()).getImpostorIds() != null)
		{
			actions =
					Arrays.stream(client.getObjectDefinition(object.getId()).getImpostor().getActions()).collect(Collectors.toList());
		}
		for (int i = 0; i < actions.size(); i++)
		{
			if (actions.get(i) == null)
				continue;
			actions.set(i, actions.get(i).toLowerCase());
		}
		Point p;
		if (object instanceof GameObject)
		{
			GameObject gameObject = (GameObject) object;
			p = gameObject.getSceneMinLocation();
		}
		else
		{
			p = new Point(object.getLocalLocation().getSceneX(), object.getLocalLocation().getSceneY());
		}
		LocalPoint lp = new LocalPoint(p.getX(), p.getY());
		WorldPoint wp = WorldPoint.fromScene(client, lp.getX(), lp.getY(), object.getPlane());
		int num = -1;
		for (String action : actions)
		{
			for (String action2 : actionlist)
			{
				if (action != null && action.equalsIgnoreCase(action2.toLowerCase()))
				{
					num = actions.indexOf(action) + 1;
				}
			}
		}

		if (num < 1 || num > 10)
		{
			return;
		}
		queueObjectAction(num, object.getId(), wp.getX(), wp.getY(), ctrlDown);
	}

	public static void queueWidgetOnTileObject(int objectId, int worldPointX, int worldPointY, int sourceSlot,
											   int sourceItemId, int sourceWidgetId, boolean ctrlDown)
	{
		int ctrl = ctrlDown ? 1 : 0;
		PacketReflection.sendPacket(PacketDef.OPLOCT, objectId, worldPointX, worldPointY, sourceSlot, sourceItemId,
				sourceWidgetId, ctrl);
	}

	public static void queueWidgetOnTileObject(Widget widget, TileObject object)
	{
		Point p;
		if (object instanceof GameObject)
		{
			GameObject gameObject = (GameObject) object;
			p = gameObject.getSceneMinLocation();
		}
		else
		{
			p = new Point(object.getLocalLocation().getSceneX(), object.getLocalLocation().getSceneY());
		}
		LocalPoint lp = new LocalPoint(p.getX(), p.getY());
		WorldPoint wp = WorldPoint.fromScene(client, lp.getX(), lp.getY(), object.getPlane());
		queueWidgetOnTileObject(object.getId(), wp.getX(), wp.getY(), widget.getIndex(),
				widget.getItemId(),
				widget.getId(),
				false);
	}
}
