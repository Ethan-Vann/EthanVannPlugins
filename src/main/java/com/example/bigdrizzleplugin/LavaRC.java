package com.example.bigdrizzleplugin;

import com.example.EthanApiPlugin.BankInventory;
import net.runelite.api.*;
import net.runelite.client.RuneLite;

import java.awt.*;
import java.util.LinkedList;

public class LavaRC {
    private static final String essenceName = "Pure essence";
    private static final int ruinsID = 34817;
    private static int charges = 16;
    static Client client = RuneLite.getInjector().getInstance(Client.class);
//    public static LinkedList<MenuEntryMirror> buildActions() {
//        LinkedList<MenuEntryMirror> actionList = new LinkedList<>();
//        Item binding = client.getItemContainer(InventoryID.EQUIPMENT).getItem(EquipmentInventorySlot.AMULET.getSlotIdx());
//        if (BDUtils.inPOH()){
//            actionList.add(MenuEntryBuilder.drinkPool());
//            actionList.add(MenuEntryBuilder.maxCapeCraftingGuildTele());
//        } else if (BDUtils.getGameObject(ruinsID) != null){
//            actionList.add(enterRuins());
//        } else if (client.getLocalPlayer().getWorldLocation().getX() == 2584){
//            actionList.add(MenuEntryBuilder.doActionOnInventoryItem("pouch", "empty"));
//            actionList.add(craftRunes());
//            actionList.add(MenuEntryBuilder.doActionOnInventoryItem("pouch", "empty"));
//            actionList.add(craftRunes());
//            if(client.getEnergy() < 1200){
//                actionList.add(MenuEntryBuilder.maxCapePohTele());
//            }else{
//                actionList.add(MenuEntryBuilder.maxCapeCraftingGuildTele());
//            }
//        } else if (client.getLocalPlayer().getWorldLocation().getX() == 2576){
//            actionList.add(castMI());
//            actionList.add(craftRunes());
//        }else if (BDUtils.inCraftingGuild() && !BDUtils.bankOpen()){
//            actionList.add(MenuEntryBuilder.useBankChest());
//        } else if (BDUtils.bankOpen()){
//            if (binding == null){
//                actionList.add(withdrawBN());
//            }
//            actionList.add(MenuEntryBuilder.depositAllItemToBank(ItemID.LAVA_RUNE));
//            actionList.add(MenuEntryBuilder.withdrawAll(essenceName));
//            actionList.add(fillPouch(0));
//            actionList.add(MenuEntryBuilder.withdrawAll(essenceName));
//            if (binding == null) {
//                if (!BankInventory.search().nameContains("Binding necklace").result().isEmpty()) {
//                    actionList.add(fillPouch(0));
//                } else {
//                    actionList.add(fillPouch(1));
//                }
//                actionList.add(equipBN());
//            }else{
//                actionList.add(fillPouch(0));
//            }
//            actionList.add(MenuEntryBuilder.withdrawAll(essenceName));
//            actionList.add(teleToFireAltar());
//        }
//        return actionList;
//    }
//
//    public static MenuEntryMirror enterRuins(){
//        MenuEntryMirror enterRuins = MenuEntryBuilder.clickGameObject(ruinsID);
//        if (enterRuins != null){
//            enterRuins.setBlockUntil(() -> BDUtils.getGameObject("Altar") != null);
//            return enterRuins;
//        }
//        return null;
//    }
//
//    public static MenuEntryMirror withdrawBN(){
//        MenuEntryMirror withdraw = MenuEntryBuilder.withdrawFromBank("Binding necklace", "1");
//        if (withdraw != null){
//            withdraw.setPostActionTickDelay(1);
//            return withdraw;
//        }
//        return null;
//    }
//
//    public static MenuEntryMirror castMI(){
//        return new MenuEntryMirror("Magic Imbue", 1, MenuAction.CC_OP, -1, 14286973, -1);
//    }
//
//    public static MenuEntryMirror craftRunes(){
//        MenuEntryMirror altarCLick = MenuEntryBuilder.useItemOnGameObject("Altar");
//        if( altarCLick != null) {
//            altarCLick.getPreActions().add(new MenuEntryMirror("Use earth runes", 0, MenuAction.WIDGET_TARGET, 4, 9764864, 557));
//            altarCLick.setBlockUntilXpDrop(Skill.RUNECRAFT);
//            return altarCLick;
//        }
//        return null;
//    }
//
//    private static MenuEntryMirror fillPouch(int delay){
//        return new MenuEntryMirror("Fill pouch", 9, MenuAction.CC_OP, 0, 983043, ItemID.COLOSSAL_POUCH, delay);
//    }
//
//    private static MenuEntryMirror equipBN(){
//        return new MenuEntryMirror("Wear BN", 9, MenuAction.CC_OP, 3, 983043, ItemID.BINDING_NECKLACE);
//    }
//
//    private static MenuEntryMirror teleToFireAltar() {
//        MenuEntryMirror tele = new MenuEntryMirror("Tele to altar", 6, MenuAction.CC_OP, -1, 25362456, -1);
//        tele.setBlockUntil(() -> BDUtils.getGameObject("Cactus") != null);
//        return tele;
//    }
}
