package com.example.bigdrizzleplugin.builders;

import com.example.EthanApiPlugin.Inventory;
import com.example.EthanApiPlugin.TileObjects;
import com.example.bigdrizzleplugin.MenuEntryMirror;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.TileObject;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;

import java.util.Optional;

public class InventoryBuilder {
    static Client client = RuneLite.getInjector().getInstance(Client.class);

    public static MenuEntryMirror inventoryAction(String inventoryItemName, String action) {
        Optional<Widget> inventoryItem = Inventory.search().nameContains(inventoryItemName).first();
        if (inventoryItem.isPresent()) {
            return WidgetBuilder.widgetAction(inventoryItem.get(), action);
        } else {
            return null;
        }
    }

    public static MenuEntryMirror inventoryAction(Widget itemWidget, String action){
        return WidgetBuilder.widgetAction(itemWidget, action);
    }

    public static MenuEntryMirror inventoryActionUse(String inventoryItemName) {
        Optional<Widget> inventoryItem = Inventory.search().nameContains(inventoryItemName).first();
        if (inventoryItem.isPresent()) {
            return WidgetBuilder.widgetActionMenuOverride(inventoryItem.get(), MenuAction.WIDGET_TARGET);
        } else {
            return null;
        }
    }

    public static MenuEntryMirror inventoryActionUse(Widget itemWidget){
        return WidgetBuilder.widgetActionMenuOverride(itemWidget, MenuAction.WIDGET_TARGET);
    }

    public static MenuEntryMirror inventoryActionUseOnItemTarget(String targetItemName) {
        Optional<Widget> inventoryItem = Inventory.search().nameContains(targetItemName).first();
        if (inventoryItem.isPresent()) {
            return WidgetBuilder.widgetActionMenuOverride(inventoryItem.get(), MenuAction.WIDGET_TARGET_ON_WIDGET);
        } else {
            return null;
        }
    }

    public static MenuEntryMirror inventoryActionUseOnItemTarget(Widget targetWidget){
        return WidgetBuilder.widgetActionMenuOverride(targetWidget, MenuAction.WIDGET_TARGET_ON_WIDGET);
    }

    public static MenuEntryMirror inventoryActionUseOnGameObject(String gameObjectName){
        GameObject object = null;
        Optional<TileObject> objectOpt = TileObjects.search().withName(gameObjectName).nearestToPlayer();
        if (objectOpt.isPresent() && objectOpt.get() instanceof GameObject){
            object = (GameObject) objectOpt.get();
            return new MenuEntryMirror("Use item on Object " + client.getObjectDefinition(object.getId()).getName(), object.getId(), MenuAction.WIDGET_TARGET_ON_GAME_OBJECT,
                    object.getSceneMinLocation().getX(), object.getSceneMinLocation().getY(), -1);
        }else{
            return null;
        }
    }
}
