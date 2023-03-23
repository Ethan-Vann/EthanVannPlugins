package com.example.bigdrizzleplugin.builders;

import com.example.EthanApiPlugin.NPCs;
import com.example.EthanApiPlugin.TileObjects;
import com.example.bigdrizzleplugin.MenuEntryMirror;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.RuneLite;

import java.util.HashMap;
import java.util.Optional;

import static com.example.PacketReflection.client;


public class ObjectBuilder {

    private static HashMap<Integer, MenuAction> menuMap;
    static {
        menuMap = new HashMap<>();
        menuMap.put(1, MenuAction.GAME_OBJECT_FIRST_OPTION);
        menuMap.put(2, MenuAction.GAME_OBJECT_SECOND_OPTION);
        menuMap.put(3, MenuAction.GAME_OBJECT_THIRD_OPTION);
        menuMap.put(4, MenuAction.GAME_OBJECT_FOURTH_OPTION);
        menuMap.put(5, MenuAction.GAME_OBJECT_FIFTH_OPTION);
    }
    static Client client = RuneLite.getInjector().getInstance(Client.class);

    public static MenuEntryMirror objectAction(String objectName, String action){
        if (objectName == null || action == null){
            return null;
        }
        Optional<TileObject> tileObjectOpt = TileObjects.search().nameContains(objectName).nearestToPlayer();
        if (tileObjectOpt.isPresent()){
            return objectAction(tileObjectOpt.get(), action);
        }
        return null;
    }

    public static MenuEntryMirror objectAction(TileObject tileObject, String action) {
        if (tileObject == null || action == null){
            return null;
        }
        String[] actions = client.getObjectDefinition(tileObject.getId()).getActions();
        int identifier = 0;
        for (int i = actions.length - 1; i >= 0; i--) {
            if (actions[i] != null && actions[i].equalsIgnoreCase(action)){
                identifier = i+1;
                int x,y;
                if (tileObject instanceof GameObject) {
                    x = ((GameObject) tileObject).getSceneMinLocation().getX();
                    y = ((GameObject) tileObject).getSceneMinLocation().getY();
                } else {
                    x = tileObject.getLocalLocation().getX();
                    y = tileObject.getLocalLocation().getY();
                }
                return new MenuEntryMirror(action + " " + client.getObjectDefinition(tileObject.getId()).getName(), tileObject.getId(), menuMap.get(identifier),
                        x, y, -1);
            }
        }
        return null;
    }
}
