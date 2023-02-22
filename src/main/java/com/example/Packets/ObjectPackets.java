package com.example.Packets;

import com.example.PacketDef;
import com.example.PacketReflection;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.widgets.Widget;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectPackets {
    @Inject
    Client client;
    @Inject
    PacketReflection packetReflection;

    @SneakyThrows
    public void queueObjectAction(int actionFieldNo, int objectId, int worldPointX, int worldPointY, boolean ctrlDown) {
        int ctrl = ctrlDown ? 1 : 0;
        switch (actionFieldNo) {
            case 1:
                packetReflection.sendPacket(PacketDef.OPLOC1, objectId, worldPointX, worldPointY, ctrl);
                break;
            case 2:
                packetReflection.sendPacket(PacketDef.OPLOC2, objectId, worldPointX, worldPointY, ctrl);
                break;
            case 3:
                packetReflection.sendPacket(PacketDef.OPLOC3, objectId, worldPointX, worldPointY, ctrl);
                break;
            case 4:
                packetReflection.sendPacket(PacketDef.OPLOC4, objectId, worldPointX, worldPointY, ctrl);
                break;
            case 5:
                packetReflection.sendPacket(PacketDef.OPLOC5, objectId, worldPointX, worldPointY, ctrl);
                break;
        }
    }

    @SneakyThrows
    public void queueObjectAction(TileObject object, boolean ctrlDown, String... actionlist) {
        List<String> actions = Arrays.stream(client.getObjectDefinition(object.getId()).getActions()).collect(Collectors.toList());
        Point p;
        if (object instanceof GameObject) {
            GameObject gameObject = (GameObject) object;
            p = gameObject.getSceneMinLocation();
        } else {
            p = new Point(object.getLocalLocation().getSceneX(), object.getLocalLocation().getSceneY());
        }
        LocalPoint lp = new LocalPoint(p.getX(), p.getY());
        WorldPoint wp = WorldPoint.fromScene(client, lp.getX(), lp.getY(), object.getPlane());
        int num = -1;
        for (String action : actions) {
            for (String action2 : actionlist) {
                if (action != null && action.equals(action2)) {
                    num = actions.indexOf(action) + 1;
                }
            }
        }

        if (num < 1 || num > 10) {
            return;
        }
        queueObjectAction(num, object.getId(), wp.getX(), wp.getY(), ctrlDown);
    }

    public void queueWidgetOnTileObject(int objectId, int worldPointX, int worldPointY, int sourceSlot,
                                        int sourceItemId, int sourceWidgetId, boolean ctrlDown) {
        int ctrl = ctrlDown ? 1 : 0;
        packetReflection.sendPacket(PacketDef.OPLOCT, objectId, worldPointX, worldPointY, sourceSlot, sourceItemId,
                sourceWidgetId, ctrl);
    }

    public void queueWidgetOnTileObject(Widget widget, TileObject object) {
        Point p;
        if (object instanceof GameObject) {
            GameObject gameObject = (GameObject) object;
            p = gameObject.getSceneMinLocation();
        } else {
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
