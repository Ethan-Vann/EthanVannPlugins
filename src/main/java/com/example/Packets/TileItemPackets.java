package com.example.Packets;

import com.example.PacketDef;
import com.example.PacketReflection;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import net.runelite.api.Client;

public class TileItemPackets
{
	@Inject
	Client client;
	@Inject
	PacketReflection packetReflection;
	@SneakyThrows
	public void queueTileItemAction(int actionFieldNo, int objectId, int worldPointX, int worldPointY,
								   boolean ctrlDown) {
		int ctrl = ctrlDown ? 1 : 0;
		switch (actionFieldNo) {
			case 1:
				packetReflection.sendPacket(PacketDef.OPOBJ1, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 2:
				packetReflection.sendPacket(PacketDef.OPOBJ2, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 3:
				packetReflection.sendPacket(PacketDef.OPOBJ3, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 4:
				packetReflection.sendPacket(PacketDef.OPOBJ4, objectId, worldPointX, worldPointY, ctrl);
				break;
			case 5:
				packetReflection.sendPacket(PacketDef.OPOBJ5, objectId, worldPointX, worldPointY, ctrl);
				break;
		}
	}
	public void queueWidgetOnTileItem(int objectId, int worldPointX, int worldPointY, int sourceSlot,
										int sourceItemId, int sourceWidgetId, boolean ctrlDown) {
		int ctrl = ctrlDown ? 1 : 0;
		packetReflection.sendPacket(PacketDef.OPOBJT, objectId, worldPointX, worldPointY, sourceSlot, sourceItemId,
				sourceWidgetId, ctrl);
	}
}
