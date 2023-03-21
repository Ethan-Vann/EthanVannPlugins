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

    public static MenuEntryMirror withdrawFromBank(String itemName, String amountAction){
        Widget bankItemWidget = null;
        Optional<Widget> bankItem = Bank.search().nameContains(itemName).first();
        if (bankItem.isPresent()){
            bankItemWidget = bankItem.get();
        }else{
            return null;
        }
        int identifier = 0;
        for (int i = bankItemWidget.getActions().length - 1; i >= 0; i--) {
            if (bankItemWidget.getActions()[i] != null && bankItemWidget.getActions()[i].equalsIgnoreCase("Withdraw-" + amountAction)){
                identifier = i+1;
                break;
            }
        }
        return new MenuEntryMirror("Withdraw All Item: " + bankItemWidget.getName(), identifier, MenuAction.CC_OP, bankItemWidget.getIndex(), bankItemWidget.getId(), bankItemWidget.getItemId());
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
    public static MenuEntryMirror withdrawAll(String itemName){
        Widget bankItemWidget = null;
        Optional<Widget> bankItem = Bank.search().nameContains(itemName).first();
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
        return new MenuEntryMirror("Withdraw All Item: " + bankItemWidget.getName(), identifier, MenuAction.CC_OP, bankItemWidget.getIndex(), bankItemWidget.getId(), bankItemWidget.getItemId());
    }

    public static MenuEntryMirror doActionOnInventoryItem(String itemName, String action){
        Widget inventoryItemWidget = null;
        Optional<Widget> inventoryItem = Inventory.search().nameContains(itemName).first();
        if (inventoryItem.isPresent()){
            inventoryItemWidget = inventoryItem.get();
        }else{
            return null;
        }
        int identifier = 0;
        for (int i = inventoryItemWidget.getActions().length - 1; i >= 0; i--) {
            if (inventoryItemWidget.getActions()[i] != null && inventoryItemWidget.getActions()[i].equalsIgnoreCase(action)){
                identifier = i+1;
                break;
            }
        }
        MenuAction type = null;
        if (action.equalsIgnoreCase("use")){
            type = MenuAction.WIDGET_TARGET;
        }else{
            type = MenuAction.CC_OP;
        }
        //fix menu action
        return new MenuEntryMirror(action + " " + inventoryItemWidget.getName(), identifier, type, inventoryItemWidget.getIndex(), inventoryItemWidget.getId(), inventoryItemWidget.getItemId());
    }

    public static MenuEntryMirror doActionOnBankInventoryItem(String itemName, String action){
        Widget inventoryBankItemWidget = null;
        Optional<Widget> bankInventoryItem = Inventory.search().nameContains(itemName).first();
        if (bankInventoryItem.isPresent()){
            inventoryBankItemWidget = bankInventoryItem.get();
        }else{
            return null;
        }
        int identifier = 0;
        for (int i = inventoryBankItemWidget.getActions().length - 1; i >= 0; i--) {
            if (inventoryBankItemWidget.getActions()[i] != null && inventoryBankItemWidget.getActions()[i].equalsIgnoreCase(action)){
                identifier = i+1;
                break;
            }
        }
        return new MenuEntryMirror(action + " " + inventoryBankItemWidget.getName(), identifier, MenuAction.CC_OP, inventoryBankItemWidget.getIndex(), inventoryBankItemWidget.getId(), inventoryBankItemWidget.getItemId());
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
        return new MenuEntryMirror("Deposit All Item: " + inventoryItemWidget.getName(), identifier, MenuAction.CC_OP, inventoryItemWidget.getIndex(), 12582914, itemID);
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
        if (TileObjects.search().nameContains(name).nearestToPlayer().isPresent()){
            return clickGameObject(TileObjects.search().nameContains(name).nearestToPlayer().get().getId(), postActionTickDelay);
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

    public static MenuEntryMirror useItemOnGameObject(String gameObjectName){
        GameObject object = null;
        Optional<TileObject> objectOpt = TileObjects.search().withName(gameObjectName).nearestToPlayer();
        if (objectOpt.isPresent() && objectOpt.get() instanceof GameObject){
            object = (GameObject) objectOpt.get();
        }
        return new MenuEntryMirror("Click " + client.getObjectDefinition(object.getId()).getName(), object.getId(), MenuAction.WIDGET_TARGET_ON_GAME_OBJECT,
                object.getSceneMinLocation().getX(), object.getSceneMinLocation().getY(), -1);
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
        if(NPCs.search().nameContains(npcName).nearestToPlayer().isPresent()){
            MenuEntryMirror result = clickNPC(NPCs.search().nameContains(npcName).nearestToPlayer().get().getId());
            result.setPostActionTickDelay(postActionTickDelay);
            return result;
        }
       return null;
    }
    public static MenuEntryMirror clickNPC(String npcName){
        return clickNPC(npcName, 0);
    }


    public static MenuEntryMirror maxCapePohTele(){
        MenuEntryMirror tele = new MenuEntryMirror("POH Tele ", 5, MenuAction.CC_OP,
                -1, 25362448, -1);
        tele.setBlockUntil(() -> BDUtils.getGameObject(ObjectID.PORTAL_4525) != null);
        return tele;
    }
    public static MenuEntryMirror maxCapeCraftingGuildTele(){
        MenuEntryMirror tele = new MenuEntryMirror("Crafting Guild Tele ", 4, MenuAction.CC_OP,
                -1, 25362448, -1, 2);
        tele.setBlockUntil(() -> BDUtils.getNPC("Master Crafter") != null);
        return tele;
    }
    public static MenuEntryMirror lastDestinationRing(){
        return new MenuEntryMirror("Last Destination ", 29229, MenuAction.GAME_OBJECT_FOURTH_OPTION,
                51, 51, -1, 2);
    }

    public static MenuEntryMirror drinkPool(){
        MenuEntryMirror drinkPool = MenuEntryBuilder.clickGameObject("ornate pool");
        if (drinkPool != null){
            drinkPool.setBlockUntil(() -> {
                return client.getBoostedSkillLevel(Skill.HITPOINTS) >= 99 && client.getEnergy() == 10000 && client.getBoostedSkillLevel(Skill.PRAYER) >= 99;
            });
            return drinkPool;
        }
        return null;
    }

    public static MenuEntryMirror useBankChest(){
        MenuEntryMirror useChest = MenuEntryBuilder.clickGameObject("Bank chest");
        if (useChest != null){
            useChest.setBlockUntil(() -> BDUtils.bankOpen());
            return useChest;
        }
        return null;
    }
}
