package com.example.Packets;

import com.example.PacketDef;
import com.example.PacketReflection;
import lombok.SneakyThrows;

public class TileItemPackets
{
	@SneakyThrows
	public void queueTileItemAction(int actionFieldNo, int objectId, int worldPointX, int worldPointY,
								   boolean ctrlDown) {
		int ctrl = ctrlDown ? 1 : 0;
		switch (actionFieldNo) {
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
	public void queueWidgetOnTileItem(int objectId, int worldPointX, int worldPointY, int sourceSlot,
										int sourceItemId, int sourceWidgetId, boolean ctrlDown) {
		int ctrl = ctrlDown ? 1 : 0;
		PacketReflection.sendPacket(PacketDef.OPOBJT, objectId, worldPointX, worldPointY, sourceSlot, sourceItemId,
				sourceWidgetId, ctrl);
	}
}
