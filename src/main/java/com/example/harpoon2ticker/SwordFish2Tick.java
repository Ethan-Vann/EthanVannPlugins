//package com.example.harpoon2ticker;
//
//import com.example.EthanApiPlugin.EthanApiPlugin;
//import com.example.PacketUtilsPlugin;
//import com.example.Packets.MousePackets;
//import com.example.Packets.MovementPackets;
//import com.example.Packets.NPCPackets;
//import com.example.Packets.WidgetPackets;
//import com.google.inject.Inject;
//import net.runelite.api.Client;
//import net.runelite.api.GameState;
//import net.runelite.api.NPC;
//import net.runelite.api.coords.WorldPoint;
//import net.runelite.api.events.GameTick;
//import net.runelite.api.events.HitsplatApplied;
//import net.runelite.api.widgets.Widget;
//import net.runelite.api.widgets.WidgetInfo;
//import net.runelite.client.eventbus.Subscribe;
//import net.runelite.client.plugins.Plugin;
//import net.runelite.client.plugins.PluginDependency;
//import net.runelite.client.plugins.PluginDescriptor;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.List;
//
//@PluginDescriptor(
//		name = "SwordFish2Tick",
//		enabledByDefault = false,
//		tags = {"ethan"}
//)
//@PluginDependency(EthanApiPlugin.class)
//@PluginDependency(PacketUtilsPlugin.class)
//public class SwordFish2Tick extends Plugin
//{
//	@Inject
//	NPCPackets npcPackets;
//	@Inject
//	Client client;
//	@Inject
//	EthanApiPlugin api;
//	@Inject
//	MousePackets mousePackets;
//	@Inject
//	MovementPackets movementPackets;
//	@Inject
//	WidgetPackets widgetPackets;
//	List<WorldPoint> movements=new ArrayList<>();
//	boolean moving = false;
//	@Subscribe
//	public void onGameTick(GameTick e){
//		if(client.getGameState()!=GameState.LOGGED_IN) return;
//		WorldPoint playerLocation= client.getLocalPlayer().getWorldLocation();
//		NPC fishingSpot = client.getNpcs().stream().filter(x -> x.getId() == 7470).min(Comparator.comparingInt(x -> x.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()))).orElse(null);
//		if(fishingSpot==null||(fishingSpot.getWorldLocation().getX()==playerLocation.getX()&&fishingSpot.getWorldLocation().getY()==playerLocation.getY()+1)){
//			return;
//		}
//		if(movements.size()>0)
//		{
//			WorldPoint position = new WorldPoint(movements.get(0).getX(), movements.get(0).getY(),
//					movements.get(0).getPlane());
//			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher", "moving", null);
//			mousePackets.queueClickPacket();
//			movementPackets.queueMovement(position);
//			movements.remove(0);
//			if (movements.size() == 0)
//			{
//				moving = false;
//			}
//			return;
//		}
//		moving = true;
//		if(client.getLocalPlayer().getWorldLocation().getX()==1762){
//			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher","setting movement list 1", null);
//			WorldPoint[] array ={playerLocation.dx(1),playerLocation.dx(2),playerLocation.dx(3)} ;
//			movements = new ArrayList<>(Arrays.asList(array));
//		}else{
//			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher","setting movement list 2", null);
//			WorldPoint[] array ={playerLocation.dx(-1).dy(-1),playerLocation.dx(-2).dy(-1),playerLocation.dx(-3).dy(-1),
//					playerLocation.dx(-4).dy(-1),playerLocation.dx(-3)} ;
//			movements = new ArrayList<>(Arrays.asList(array));
//		}
//		//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher", "moving", null);
//		mousePackets.queueClickPacket();
//		movementPackets.queueMovement(movements.get(0));
//		movements.remove(0);
//	}
//	@Subscribe
//	public void onHitsplatApplied(HitsplatApplied e)
//	{
//		if(!e.getActor().equals(client.getLocalPlayer())) return;
//		NPC fishingSpot = client.getNpcs().stream().filter(x -> x.getId() == 7470).min(Comparator.comparingInt(x -> x.getWorldLocation().distanceTo(client.getLocalPlayer().getWorldLocation()))).orElse(null);
//		WorldPoint playerLocation= client.getLocalPlayer().getWorldLocation();
//		if(fishingSpot==null||!(fishingSpot.getWorldLocation().getX()==playerLocation.getX()&&fishingSpot.getWorldLocation().getY()==playerLocation.getY()+1)||moving){
//			return;
//		}
//		Widget fish = api.getItem(371, WidgetInfo.INVENTORY);
//		fish = fish==null?api.getItem(359,WidgetInfo.INVENTORY):fish;
//		if(fish!=null){
//			//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher","dropping fish", null);
//			mousePackets.queueClickPacket();
//			widgetPackets.queueWidgetAction(fish, "Drop");
//		}
//		//client.addChatMessage(ChatMessageType.GAMEMESSAGE, "2TFisher","fishing", null);
//		mousePackets.queueClickPacket();
//		npcPackets.queueNPCAction(fishingSpot,"Harpoon");
//	}
//}
