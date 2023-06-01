package com.example.EthanApiPlugin.Utility;

import net.runelite.api.GameObject;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.PacketUtils.PacketReflection.client;

public class WorldAreaUtility {

    public static HashSet<WorldPoint> objectInteractableTiles(TileObject e){

        Point p;
        int x = 1;
        int y = 1;
        if (e instanceof GameObject) {
            GameObject gameObject = (GameObject) e;
            x = gameObject.sizeX();
            y = gameObject.sizeY();
            p = gameObject.getSceneMinLocation();
        } else {
            p = new Point(e.getLocalLocation().getSceneX(), e.getLocalLocation().getSceneY());
        }
        LocalPoint lp = new LocalPoint(p.getX(), p.getY());
        WorldPoint location = WorldPoint.fromScene(client, lp.getX(), lp.getY(), e.getPlane());
        ArrayList<WorldPoint> interactablePoints = new ArrayList<>(new WorldArea(location, x, y).toWorldPointList());
        int corner1 = 0;
        int corner2 = x-1;
        int corner3 = (y*x)-(x-1);
        int corner4 = (y*x)-1;
        interactablePoints.remove(corner1);
        interactablePoints.remove(corner2);
        interactablePoints.remove(corner3);
        interactablePoints.remove(corner4);
        return new HashSet<>(interactablePoints);
    }
}
