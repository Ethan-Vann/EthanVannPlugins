package com.example.RuneBotApi.Npcs;

import com.example.EthanApiPlugin.Collections.NPCs;
import com.example.EthanApiPlugin.Collections.query.NPCQuery;
import com.example.RuneBotApi.RBConstants;
import net.runelite.api.NPC;

import java.util.Optional;

public class NpcInformation {

    public static Optional<NPC> getNearestNpcWithId(int id)
    {
        return new NPCQuery(
                NPCs.search().withId(id).result()
        ).nearestToPlayer();
    }

}
