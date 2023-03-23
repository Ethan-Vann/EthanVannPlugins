package com.example.bigdrizzleplugin.builders;

import com.example.EthanApiPlugin.NPCs;
import com.example.bigdrizzleplugin.MenuEntryMirror;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;

import java.util.HashMap;
import java.util.Optional;

public class NPCBuilder {

    private static HashMap<Integer, MenuAction> menuMap;
    static {
        menuMap = new HashMap<>();
        menuMap.put(1, MenuAction.NPC_FIRST_OPTION);
        menuMap.put(2, MenuAction.NPC_SECOND_OPTION);
        menuMap.put(3, MenuAction.NPC_THIRD_OPTION);
        menuMap.put(4, MenuAction.NPC_FOURTH_OPTION);
        menuMap.put(5, MenuAction.NPC_FIFTH_OPTION);
    }

    public MenuEntryMirror npcAction(String npcName, String action){
        if (npcName == null || action == null){
            return null;
        }
        Optional<NPC> npcOpt = NPCs.search().nameContains(npcName).nearestToPlayer();
        if (npcOpt.isPresent()){
           return npcAction(npcOpt.get(), action);
        }
        return null;
    }

    public static MenuEntryMirror npcAction(NPC npc, String action){
        if (npc == null || action == null){
            return null;
        }
        String[] actions = npc.getComposition().getActions();
        int identifier = 0;
        for (int i = actions.length - 1; i >= 0; i--) {
            if (actions[i] != null && actions[i].equalsIgnoreCase(action)){
                identifier = i+1;
                return new MenuEntryMirror(action + " " + npc.getComposition().getName() , npc.getIndex(), menuMap.get(identifier), 0,0, -1);
            }
        }
        return null;
    }
}
