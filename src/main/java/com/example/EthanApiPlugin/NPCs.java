package com.example.EthanApiPlugin;

import com.example.Packets.MousePackets;
import com.example.Packets.NPCPackets;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class NPCs
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	private static List<NPC> npcList = new ArrayList<>();

	public static NPCQuery search()
	{
		return new NPCQuery(npcList);
	}

	public static boolean interact(String name, String... actions)
	{
		return search().withName(name).first().flatMap(npc ->
		{
			MousePackets.queueClickPacket();
			NPCPackets.queueNPCAction(npc, actions);
			return Optional.of(true);
		}).orElse(false);
	}

	public static boolean interact(int id, String... actions)
	{
		return search().hasId(id).first().flatMap(npc ->
		{
			MousePackets.queueClickPacket();
			NPCPackets.queueNPCAction(npc, actions);
			return Optional.of(true);
		}).orElse(false);
	}

	public static boolean interact(Predicate<? super NPC> predicate, String... actions)
	{
		return search().filter(predicate).first().flatMap(npc ->
		{
			MousePackets.queueClickPacket();
			NPCPackets.queueNPCAction(npc, actions);
			return Optional.of(true);
		}).orElse(false);
	}

	public static boolean interactIndex(int index, String... actions)
	{
		return search().indexIs(index).first().flatMap(npc ->
		{
			MousePackets.queueClickPacket();
			NPCPackets.queueNPCAction(npc, actions);
			return Optional.of(true);
		}).orElse(false);
	}

	public static boolean interact(NPC npc, String... actions)
	{
		if (npc == null)
		{
			return false;
		}
		MousePackets.queueClickPacket();
		NPCPackets.queueNPCAction(npc, actions);
		return true;
	}

	@Subscribe(priority = 10000)
	public void onGameTick(GameTick e){
		npcList.clear();
		for (NPC npc : client.getNpcs())
		{
			if(npc==null)
				continue;
			npcList.add(npc);
		}
	}
}
