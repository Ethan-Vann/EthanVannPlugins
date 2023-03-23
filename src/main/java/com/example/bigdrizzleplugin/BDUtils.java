package com.example.bigdrizzleplugin;

import com.example.EthanApiPlugin.Inventory;
import com.example.EthanApiPlugin.NPCs;
import com.example.EthanApiPlugin.TileObjects;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.NPC;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.widgets.Widget;
import net.runelite.client.RuneLite;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class BDUtils {
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    private static final WorldArea craftingGuildArea = new WorldArea(2923, 3273, 22, 23, 0);
    private static final int BANK_INTERFACE_WIDGET_ID = 786474;
    private final static int DEPOSIT_BOX_WIDGET_ID = 12582914;
    private static Class classWithInvokeMenuAction;
    private static Method invokeMenuAction;

    public static boolean inPOH() {
        return client.getMapRegions()[0] == 7769;
    }
    public static boolean inCraftingGuild(){
        return inArea(craftingGuildArea);
    }
    public static boolean inArea(WorldArea area){
        return client.getLocalPlayer().getWorldLocation().isInArea2D(area);
    }
    public static boolean inventoryFull(){
        return Inventory.search().result().size()==28;
    }
    public static int inventoryCount(int itemID){
        return Inventory.search().withId(itemID).result().size();
    }
    public static int inventoryCount(String name){

        return Inventory.search().nameContains(name).result().size();
    }
    public static boolean bankOpen(){
        return client.getWidget(BANK_INTERFACE_WIDGET_ID) != null && !client.getWidget(BANK_INTERFACE_WIDGET_ID).isHidden();
    }
    public static boolean depositBoxOpen(){
        return client.getWidget(DEPOSIT_BOX_WIDGET_ID) != null;
    }

    public static GameObject getGameObject(int gameObjectID){
        Optional<TileObject> objectOpt = TileObjects.search().withId(gameObjectID).nearestToPlayer();
        if (objectOpt.isPresent() && objectOpt.get() instanceof GameObject){
            return (GameObject) objectOpt.get();
        }
        return null;
    }
    public static GameObject getGameObject(String gameObjectName){
        Optional<TileObject> objectOpt = TileObjects.search().nameContains(gameObjectName).nearestToPlayer();
        if (objectOpt.isPresent() && objectOpt.get() instanceof GameObject){
            return (GameObject) objectOpt.get();
        }
        return null;
    }
    public static NPC getNPC(String npcName){
        Optional<NPC> npcOpt = NPCs.search().nameContains(npcName).nearestToPlayer();
        if (npcOpt.isPresent()){
            return npcOpt.get();
        }else{
            return null;
        }
    }
    public static NPC getNPC(int npcID){
        Optional<NPC> npcOpt = NPCs.search().withId(npcID).nearestToPlayer();
        if (npcOpt.isPresent()){
            return npcOpt.get();
        }else{
            return null;
        }
    }
    public static TileObject getTileObject(int tileObjectID){
        Optional<TileObject> objectOpt = TileObjects.search().withId(tileObjectID).nearestToPlayer();
        if (objectOpt.isPresent()){
            return objectOpt.get();
        }
        return null;
    }
    public static TileObject getTileObject(String tileObjectName){
        Optional<TileObject> objectOpt = TileObjects.search().nameContains(tileObjectName).nearestToPlayer();
        if (objectOpt.isPresent()){
            return objectOpt.get();
        }
        return null;
    }

    private static void loadInvokeMethod() {
        try {
            classWithInvokeMenuAction = client.getClass().getClassLoader().loadClass("lk");
            invokeMenuAction = Arrays.stream(classWithInvokeMenuAction.getDeclaredMethods())
                    .filter(method -> method.getName().equals("ku")).findAny().orElse(null);
            assert invokeMenuAction != null;
            invokeMenuAction.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load menuaction class");
        }
    }

    @SneakyThrows
    public static boolean invokeMenuAction(MenuEntryMirror action) {
        loadInvokeMethod();
        if (action == null){
            System.out.println("Tried to invoke a null action!");
            return false;
        }
        log.info("Invoking: " + action.toString());
        //string values are option, target, irrelevant
        int garbageValue = 1849187210;
        int convertedGarbage = garbageValue; //garbageValue.byteValue();
        invokeMenuAction.invoke(null,action.getParam0(),action.getParam1(),action.getMenuAction().getId(),action.getIdentifier(), action.getItemID(), "","",-1,-1,convertedGarbage);
        return true;
    }
}
