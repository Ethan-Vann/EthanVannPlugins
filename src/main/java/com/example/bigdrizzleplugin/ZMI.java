package com.example.bigdrizzleplugin;

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.Skill;
import net.runelite.client.RuneLite;

import java.util.LinkedList;

public class ZMI {
    private static int essenceInPouch = 0;
    private static final String essenceName = "Pure essence";
    static Client client = RuneLite.getInjector().getInstance(Client.class);

    public static LinkedList<MenuEntryMirror> buildActions() {
        LinkedList<MenuEntryMirror> actionList = new LinkedList<>();
        if (BDUtils.inPOH()){
            if (client.getEnergy() < 10000){
                actionList.add(MenuEntryBuilder.clickGameObject("ornate pool"));
            }else{
                actionList.add(ouraniaTele());
            }
        } else if (BDUtils.bankOpen() && !inventoryReadyToLeaveBank()){
            actionList.add(MenuEntryBuilder.depositAllInventoryToBank());
            actionList.add(MenuEntryBuilder.withdrawAll(essenceName));
            actionList.add(fillPouch());
            actionList.add(MenuEntryBuilder.withdrawAll(essenceName));
            actionList.add(fillPouch());
            actionList.add(MenuEntryBuilder.withdrawAll(essenceName));
            MenuEntryMirror altarCLick = MenuEntryBuilder.clickGameObject("Altar");
            altarCLick.setBlockUntilXpDrop(Skill.RUNECRAFT);
            actionList.add(altarCLick);
            essenceInPouch = 40;
        } else if (BDUtils.bankOpen() && inventoryReadyToLeaveBank()){
            actionList.add(MenuEntryBuilder.clickGameObject("Altar"));
        } else if (nearBank()){
            actionList.add(MenuEntryBuilder.clickNPC("Eniola",2));
        }else if (onCraftingTile()){
            MenuEntryMirror altarCLick = MenuEntryBuilder.clickGameObject("Altar");
            altarCLick.setBlockUntilXpDrop(Skill.RUNECRAFT);
            actionList.add(emptyPouch());
            actionList.add(altarCLick);
            actionList.add(emptyPouch());
            actionList.add(altarCLick);
            actionList.add(emptyPouch());
            actionList.add(altarCLick);
            if(shouldGoHome()){
                actionList.add(MenuEntryBuilder.maxCapePohTele());
            }else{
                actionList.add(ouraniaTele());
            }
            essenceInPouch = 0;
        }else if (BDUtils.getGameObject("Ladder") != null && !inventoryReadyToLeaveBank()) {
            actionList.add(MenuEntryBuilder.clickGameObject("Ladder"));
        }else if (BDUtils.getGameObject("Altar") != null){
            actionList.add(MenuEntryBuilder.clickGameObject("Altar"));
        }
        return actionList;
    }

    private static boolean inventoryReadyToLeaveBank(){
        return essenceInPouch == 40 && BDUtils.inventoryCount(essenceName) == 26;
    }

    private static boolean shouldGoHome(){
        return client.getEnergy() < 3000;
    }

    private static MenuEntryMirror fillPouch(){
        return new MenuEntryMirror("Fill pouch", 9, MenuAction.CC_OP, 0, 983043, ItemID.COLOSSAL_POUCH);
    }

    private static MenuEntryMirror emptyPouch(){
        return new MenuEntryMirror("Empty pouch", 3, MenuAction.CC_OP, 0, 9764864, ItemID.COLOSSAL_POUCH);
    }

    private static MenuEntryMirror ouraniaTele(){
        return new MenuEntryMirror("Tele", 1, MenuAction.CC_OP, -1, 14286992, -1, 3);
    }

    private static boolean onCraftingTile(){
        return client.getLocalPlayer().getWorldLocation().getX() == 3058;
    }

    private static boolean nearBank(){
        return client.getLocalPlayer().getWorldLocation().getX() == 3015 && client.getLocalPlayer().getWorldLocation().getY() == 5629;
    }
}
