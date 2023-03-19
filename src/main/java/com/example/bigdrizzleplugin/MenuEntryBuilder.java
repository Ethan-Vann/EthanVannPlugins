package com.example.bigdrizzleplugin;

import com.example.EthanApiPlugin.*;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;

import javax.inject.Inject;
import java.awt.*;
import java.util.Optional;

public class MenuEntryBuilder {


    public final static int BANK_WIDGET_ID =786474;
    public final static int BANK_INVENTORY_WIDGET_ID = 983043;
    static Client client = RuneLite.getInjector().getInstance(Client.class);
    public static MenuEntryMirror depositAllInventoryToBank(){
        return new MenuEntryMirror("Deposit all inventory", 1, MenuAction.CC_OP, -1, BANK_WIDGET_ID, -1);
    }
    public static MenuEntryMirror depositAllItemToBank(int itemID){
        Widget bankInventoryItemWidget = null;
        Optional<Widget> bankInventoryItem = BankInventory.search().withId(itemID).first();
        if (bankInventoryItem.isPresent()){
            bankInventoryItemWidget = bankInventoryItem.get();
        }else{
            return null;
        }
        int identifier = 0;
        for (int i = bankInventoryItemWidget.getActions().length - 1; i >= 0; i--) {
            if (bankInventoryItemWidget.getActions()[i].equalsIgnoreCase("deposit-all")){
                identifier = i+1;
                break;
            }
        }
        return new MenuEntryMirror("Deposit All " + bankInventoryItemWidget.getName(), identifier, MenuAction.CC_OP, bankInventoryItemWidget.getIndex(), BANK_INVENTORY_WIDGET_ID, itemID);
    }
    public static MenuEntryMirror withdrawAll(int itemID){
        Widget bankItemWidget = null;
        Optional<Widget> bankItem = Bank.search().withId(itemID).first();
        if (bankItem.isPresent()){
            bankItemWidget = bankItem.get();
        }else{
            return null;
        }
        int identifier = 0;
        for (int i = bankItemWidget.getActions().length - 1; i >= 0; i--) {
            if (bankItemWidget.getActions()[i] != null && bankItemWidget.getActions()[i].equalsIgnoreCase("withdraw-all")){
                identifier = i+1;
                break;
            }
        }
        return new MenuEntryMirror("Withdraw All Item: " + bankItemWidget.getName(), identifier, MenuAction.CC_OP, bankItemWidget.getIndex(), bankItemWidget.getId(), itemID);
    }
    public static MenuEntryMirror depositAllItemToDepositBox(int itemID){
        //This should really be depositBoxInventory
        Widget inventoryItemWidget = null;
        Optional<Widget> inventoryItem = Inventory.search().withId(itemID).first();
        if (inventoryItem.isPresent()){
            inventoryItemWidget = inventoryItem.get();
        }else{
            return null;
        }
        int identifier = 0;
        for (int i = inventoryItemWidget.getActions().length - 1; i >= 0; i--) {
            if (inventoryItemWidget.getActions()[i] != null && inventoryItemWidget.getActions()[i].equalsIgnoreCase("deposit-all")){
                identifier = i+1;
                break;
            }
        }
        //this assumes the UI is set to "All", identifier is hardcoded to 1 until API for depositbox
        return new MenuEntryMirror("Deposit All Item: " + inventoryItemWidget.getName(), 1, MenuAction.CC_OP, inventoryItemWidget.getIndex(), 12582914, itemID);
        //return new MenuEntryMirror("Deposit All Item: " + inventoryItemWidget.getName(), identifier, MenuAction.CC_OP, inventoryItemWidget.getIndex(), 12582914, itemID);
    }
    public static MenuEntryMirror clickItemInDepositBox(int itemID, int hcIdentifier){
        //This should really be depositBoxInventory
        Widget inventoryItemWidget = null;
        Optional<Widget> inventoryItem = Inventory.search().withId(itemID).first();
        if (inventoryItem.isPresent()){
            inventoryItemWidget = inventoryItem.get();
        }else{
            return null;
        }
        int identifier = 0;
        for (int i = inventoryItemWidget.getActions().length - 1; i >= 0; i--) {
            if (inventoryItemWidget.getActions()[i] != null && inventoryItemWidget.getActions()[i].equalsIgnoreCase("deposit-all")){
                identifier = i+1;
                break;
            }
        }
        //needs to not hardcode identifier once i can gather it with API
        return new MenuEntryMirror("Click item: " + inventoryItemWidget.getName(), hcIdentifier, MenuAction.CC_OP, inventoryItemWidget.getIndex(), 12582914, itemID);
    }
    public static MenuEntryMirror clickGameObject(int gameObjectID){
        return clickGameObject(gameObjectID, 0);
    }
    public static MenuEntryMirror clickGameObject(String name, int postActionTickDelay){
        if (TileObjects.search().withName(name).nearestToPlayer().isPresent()){
            return clickGameObject(TileObjects.search().withName(name).nearestToPlayer().get().getId(), postActionTickDelay);
        }
        return null;
    }
    public static MenuEntryMirror clickGameObject(String name){
        return clickGameObject(name, 0);
    }
    public static MenuEntryMirror clickGameObject(int gameObjectID, int postActionTickDelay){
        GameObject object = null;
        Optional<TileObject> objectOpt = TileObjects.search().withId(gameObjectID).nearestToPlayer();
        if (objectOpt.isPresent() && objectOpt.get() instanceof GameObject){
            object = (GameObject) objectOpt.get();
        }
        return new MenuEntryMirror("Click " + client.getObjectDefinition(object.getId()).getName(), object.getId(), MenuAction.GAME_OBJECT_FIRST_OPTION,
                object.getSceneMinLocation().getX(), object.getSceneMinLocation().getY(), -1, postActionTickDelay);
    }

    public static MenuEntryMirror clickNPC(int npcID, int postActionTickDelay){
        NPC npc = null;
        Optional<NPC> npcOpt = NPCs.search().withId(npcID).nearestToPlayer();
        if (npcOpt.isPresent()){
            npc = npcOpt.get();
        }
        return new MenuEntryMirror("Click " + client.getNpcDefinition(npc.getId()).getName(), npc.getIndex(), MenuAction.NPC_FIRST_OPTION,
               0,0, -1, postActionTickDelay);
    }
    public static MenuEntryMirror clickNPC(int npcID){
        return clickNPC(npcID, 0);
    }
    public static MenuEntryMirror clickNPC(String npcName, int postActionTickDelay){
        if(NPCs.search().withName(npcName).nearestToPlayer().isPresent()){
            return clickNPC(NPCs.search().withName(npcName).nearestToPlayer().get().getId());
        }
       return null;
    }
    public static MenuEntryMirror clickNPC(String npcName){
        return clickNPC(npcName, 0);
    }


    public static MenuEntryMirror maxCapePohTele(){
        return new MenuEntryMirror("POH Tele ", 5, MenuAction.CC_OP,
                -1, 25362448, -1, 3);
    }
    public static MenuEntryMirror maxCapeCraftingGuildTele(){
        return new MenuEntryMirror("Crafting Guild Tele ", 4, MenuAction.CC_OP,
                -1, 25362448, -1, 1);
    }
    public static MenuEntryMirror lastDestinationRing(){
        return new MenuEntryMirror("Last Destination ", 29229, MenuAction.GAME_OBJECT_FOURTH_OPTION,
                51, 51, -1, 2);
    }
}
