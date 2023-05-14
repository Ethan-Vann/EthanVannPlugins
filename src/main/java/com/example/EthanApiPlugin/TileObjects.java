package com.example.EthanApiPlugin;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Tile;
import net.runelite.api.TileItem;
import net.runelite.api.TileObject;
import net.runelite.api.events.GameTick;
import net.runelite.client.RuneLite;
import net.runelite.client.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class TileObjects
{
	static Client client = RuneLite.getInjector().getInstance(Client.class);
	static List<TileObject> tileObjects = new ArrayList<>();

	public static TileObjectQuery search()
	{
		return new TileObjectQuery(tileObjects);
	}

	@Subscribe(priority = 10000)
	public void onGameTick(GameTick e)
	{
		tileObjects.clear();
		TileItems.tileItems.clear();
		for (Tile[] tiles : client.getScene().getTiles()[client.getPlane()])
		{
			if (tiles == null)
			{
				continue;
			}
			for (Tile tile : tiles)
			{
				if (tile == null)
				{
					continue;
				}
				if (tile.getGroundItems() != null)
				{
					for (TileItem groundItem : tile.getGroundItems())
					{
						if (groundItem == null)
						{
							continue;
						}
						TileItems.tileItems.add(new ETileItem(tile.getWorldLocation(), groundItem));
					}
				}
				for (GameObject gameObject : tile.getGameObjects())
				{
					if (gameObject == null)
					{
						continue;
					}
					if (gameObject.getId() == -1)
					{
						continue;
					}
					tileObjects.add(gameObject);
				}
				if (tile.getGroundObject() != null)
				{
					if (tile.getGroundObject().getId() == -1)
					{
						continue;
					}
					tileObjects.add(tile.getGroundObject());
				}
				if (tile.getWallObject() != null)
				{
					if (tile.getWallObject().getId() == -1)
					{
						continue;
					}
					tileObjects.add(tile.getWallObject());
				}
				if (tile.getDecorativeObject() != null)
				{
					if (tile.getDecorativeObject().getId() == -1)
					{
						continue;
					}
					tileObjects.add(tile.getDecorativeObject());
				}
			}
		}
	}
}
