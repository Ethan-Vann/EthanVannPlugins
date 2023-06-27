package com.example.RuneBotApi.Npcs;

import com.example.Packets.MousePackets;
import com.example.Packets.NPCPackets;
import net.runelite.api.NPC;

public class NpcAction {

    /**
     * @Deprecated
     * recommended to pass in npc object to properly spoof mouse coordinate location to the server
     */
    public static void queueNPCAction(int actionFieldNo, int npcIndex, boolean ctrlDown)
    {
        MousePackets.queueClickPacket();
        NPCPackets.queueNPCAction(actionFieldNo, npcIndex, ctrlDown);
    }

    public static void queueNPCAction(NPC npc, String... actionList)
    {
        int xLoc = (int)npc.getCanvasTilePoly().getBounds().getX();
        int yLoc = (int)npc.getCanvasTilePoly().getBounds().getY();
        MousePackets.queueClickPacket(xLoc, yLoc);
        NPCPackets.queueNPCAction(npc, actionList);
    }
}
