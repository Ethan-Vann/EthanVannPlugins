package com.example.Packets;

import com.example.EthanApiPlugin.ETileItem;
import com.example.PacketDef;
import com.example.PacketReflection;
import lombok.SneakyThrows;
import net.runelite.api.widgets.Widget;

public class TileItemPackets
{
	@SneakyThrows
	public static void queueTileItemAction(int actionFieldNo, int objectId, int worldPointX, int worldPointY,
										   boolean ctrlDown)
	{
		int ctrl = ctrlDown ? 1 : 0;
		switch (actionFieldNo)
		{
			case 1:
				PacketReflection.sendPacket(PacketDef.OPOBJ1, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 2:
				PacketReflection.sendPacket(PacketDef.OPOBJ2, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 3:
				PacketReflection.sendPacket(PacketDef.OPOBJ3, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 4:
				PacketReflection.sendPacket(PacketDef.OPOBJ4, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 5:
				PacketReflection.sendPacket(PacketDef.OPOBJ5, objectId, worldPointX, worldPointY, ctrl);
				break;
		}
	}

	public static void queueWidgetOnTileItem(int objectId, int worldPointX, int worldPointY, int sourceSlot,
											 int sourceItemId, int sourceWidgetId, boolean ctrlDown)
	{
		int ctrl = ctrlDown ? 1 : 0;
		PacketReflection.sendPacket(PacketDef.OPOBJT, objectId, worldPointX, worldPointY, sourceSlot, sourceItemId,
				sourceWidgetId, ctrl);
	}

	public static void queueTileItemAction(ETileItem item, boolean ctrlDown)
	{
		queueTileItemAction(3, item.getTileItem().getId(), item.getLocation().getX(), item.getLocation().getY(),
				ctrlDown);
	}

	public static void queueWidgetOnTileItem(ETileItem item, Widget w,
											 boolean ctrlDown)
	{
		queueWidgetOnTileItem(item.getTileItem().getId(), item.getLocation().getX(), item.getLocation().getY(),
				w.getIndex(), w.getItemId(), w.getId(), ctrlDown);
	}
}
