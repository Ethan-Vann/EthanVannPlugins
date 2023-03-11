package com.example.EthanApiPlugin;

import com.example.Packets.MousePackets;
import com.example.Packets.NPCPackets;
import net.runelite.api.NPC;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class NPCs
{
	private static List<NPC> npcList = new ArrayList<>();

	public static NPCQuery search(){
		return new NPCQuery(npcList);
	}
	public static boolean interact(String name,String... actions){
		return search().withName(name).first().flatMap(npc -> {
			MousePackets.queueClickPacket();
			NPCPackets.queueNPCAction(npc,actions);
			return Optional.of(true);
		}).orElse(false);
	}
	public static boolean interact(int id,String... actions){
		return search().hasId(id).first().flatMap(npc -> {
			MousePackets.queueClickPacket();
			NPCPackets.queueNPCAction(npc,actions);
			return Optional.of(true);
		}).orElse(false);
	}

	public static boolean interact(Predicate<? super NPC> predicate, String... actions){
		return search().filter(predicate).first().flatMap(npc -> {
			MousePackets.queueClickPacket();
			NPCPackets.queueNPCAction(npc,actions);
			return Optional.of(true);
		}).orElse(false);
	}
	public static boolean interactIndex(int index,String... actions){
		return search().indexIs(index).first().flatMap(npc -> {
			MousePackets.queueClickPacket();
			NPCPackets.queueNPCAction(npc,actions);
			return Optional.of(true);
		}).orElse(false);
	}
	public static boolean interact(NPC npc,String... actions){
		if(npc==null){
			return false;
		}
		MousePackets.queueClickPacket();
		NPCPackets.queueNPCAction(npc,actions);
		return true;
	}
	@Subscribe
	public void onNpcSpawned(NpcSpawned event)
	{
		npcList.add(event.getNpc());
	}
	@Subscribe
	public void onNpcDespawned(NpcDespawned event)
	{
		npcList.remove(event.getNpc());
	}
}
