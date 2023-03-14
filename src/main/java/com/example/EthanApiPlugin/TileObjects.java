package com.example.EthanApiPlugin;

import com.example.Packets.MousePackets;
import com.example.Packets.ObjectPackets;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.TileObject;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TileObjects
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	static List<TileObject> tileObjects = new ArrayList<>();

	public static TileObjectQuery search()
	{
		return new TileObjectQuery(tileObjects);
	}

	public static boolean interact(String name, String... actions)
	{
		return search().withName(name).first().flatMap(tileObject ->
		{
			MousePackets.queueClickPacket();
			ObjectPackets.queueObjectAction(tileObject, false, actions);
			return Optional.of(true);
		}).orElse(false);
	}

	public static boolean interact(int id, String... actions)
	{
		return search().withId(id).first().flatMap(tileObject ->
		{
			MousePackets.queueClickPacket();
			ObjectPackets.queueObjectAction(tileObject, false, actions);
			return Optional.of(true);
		}).orElse(false);
	}

	public static boolean interact(TileObject tileObject, String... actions)
	{
		if (tileObject == null)
		{
			return false;
		}
		MousePackets.queueClickPacket();
		ObjectPackets.queueObjectAction(tileObject, false, actions);
		return true;
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		tileObjects.add(event.getGameObject());
	}

	@Subscribe
	public void onGameObjectDespawned(GameObjectDespawned event)
	{
		tileObjects.remove(event.getGameObject());
	}

	@Subscribe
	public void onWallObjectSpawned(WallObjectSpawned event)
	{
		tileObjects.add(event.getWallObject());
	}

	@Subscribe
	public void onWallObjectDespawned(WallObjectDespawned event)
	{
		tileObjects.remove(event.getWallObject());
	}

	@Subscribe
	public void onGroundObjectSpawned(GroundObjectSpawned event)
	{
		tileObjects.add(event.getGroundObject());
	}

	@Subscribe
	public void onGroundObjectDespawned(GroundObjectDespawned event)
	{
		tileObjects.remove(event.getGroundObject());
	}

	@Subscribe
	public void onDecorativeObjectSpawned(DecorativeObjectSpawned event)
	{
		tileObjects.add(event.getDecorativeObject());
	}

	@Subscribe
	public void onDecorativeObjectDespawned(DecorativeObjectDespawned event)
	{
		tileObjects.remove(event.getDecorativeObject());
	}
	@Subscribe
	public void onGameStateChanged(){
		if(client.getGameState()!= GameState.LOGGED_IN){
			tileObjects.clear();
		}
	}
}
