package com.example.harpoon2ticker;

import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.EthanApiPlugin.Inventory;
import com.example.PacketUtilsPlugin;
import com.example.Packets.MousePackets;
import com.example.Packets.MovementPackets;
import com.example.Packets.NPCPackets;
import com.example.Packets.WidgetPackets;
import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.widgets.Widget;
import net.runelite.client.Notifier;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@PluginDescriptor(
		name = "SwordFish2Tick",
		enabledByDefault = false,
		tags = {"ethan"}
)
@PluginDependency(EthanApiPlugin.class)
@PluginDependency(PacketUtilsPlugin.class)
public class SwordFish2Tick extends Plugin
{
	int lastHitsplat = 0;
	@Inject
	Notifier notifier;
	@Inject
	Client client;
	@Inject
	EthanApiPlugin api;
	List<WorldPoint> movements = new ArrayList<>();
	boolean moving = false;

	@Subscribe
	public void onGameTick(GameTick e)
	{
		if (client.getGameState() != GameState.LOGGED_IN) return;
		WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
		NPC fishingSpot = client.getNpcs().stream().filter(x -> x.getId() == 7470).min(Comparator.comparingInt(x -> x.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()))).orElse(null);
		if (fishingSpot == null || (fishingSpot.getWorldLocation().getX() == playerLocation.getX() && fishingSpot.getWorldLocation().getY() == playerLocation.getY() + 1))
		{
			return;
		}
		if (movements.size() > 0)
		{
			WorldPoint position = new WorldPoint(movements.get(0).getX(), movements.get(0).getY(),
					movements.get(0).getPlane());
			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher", "moving", null);
			MousePackets.queueClickPacket();
			MovementPackets.queueMovement(position);
			movements.remove(0);
			if (movements.size() == 0)
			{
				moving = false;
			}
			return;
		}
		moving = true;
		if (client.getLocalPlayer().getWorldLocation().getX() == 1762)
		{
			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher","setting movement list 1", null);
			WorldPoint[] array = {playerLocation.dx(1), playerLocation.dx(2), playerLocation.dx(3)};
			movements = new ArrayList<>(Arrays.asList(array));
		}
		else
		{
			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher","setting movement list 2", null);
			WorldPoint[] array = {playerLocation.dx(-1).dy(-1), playerLocation.dx(-2).dy(-1), playerLocation.dx(-3).dy(-1),
					playerLocation.dx(-4).dy(-1), playerLocation.dx(-3)};
			movements = new ArrayList<>(Arrays.asList(array));
		}
		//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher", "moving", null);
		MousePackets.queueClickPacket();
		MovementPackets.queueMovement(movements.get(0));
		movements.remove(0);
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied e)
	{
		if (lastHitsplat == 0)
		{
			lastHitsplat = client.getTickCount();
		}
		else
		{
			if (client.getTickCount() - lastHitsplat != 2)
			{
				notifier.notify("2 tick fisher failed somehow");
				api.stopPlugin(this);
				return;
			}
			lastHitsplat = client.getTickCount();
		}
		if (!e.getActor().equals(client.getLocalPlayer())) return;
		NPC fishingSpot = client.getNpcs().stream().filter(x -> x.getId() == 7470).min(Comparator.comparingInt(x -> x.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()))).orElse(null);
		WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
		if (fishingSpot == null || !(fishingSpot.getWorldLocation().getX() == playerLocation.getX() && fishingSpot.getWorldLocation().getY() == playerLocation.getY() + 1) || moving)
		{
			return;
		}
		Optional<Widget> fish = Inventory.search().idInList(List.of(371, 359)).first();
		if (fish.isPresent())
		{
			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher","dropping fish", null);
			MousePackets.queueClickPacket();
			WidgetPackets.queueWidgetAction(fish.get(), "Drop");
		}
		//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher","fishing", null);
		MousePackets.queueClickPacket();
		NPCPackets.queueNPCAction(fishingSpot, "Harpoon");
	}

	@Override
	public void startUp()
	{
		lastHitsplat = 0;
		moving = false;
		movements = new ArrayList<>();
	}
}
